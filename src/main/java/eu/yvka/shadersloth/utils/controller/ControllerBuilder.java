package eu.yvka.shadersloth.utils.controller;


import eu.yvka.shadersloth.I18N.I18N;
import eu.yvka.shadersloth.context.ApplicationContext;
import eu.yvka.shadersloth.context.ViewContext;
import eu.yvka.shadersloth.controls.FontIcon;
import eu.yvka.shadersloth.utils.ReflectionUtils;
import eu.yvka.shadersloth.utils.actions.ActionEntry;
import eu.yvka.shadersloth.utils.actions.annotations.Action;
import eu.yvka.shadersloth.utils.controller.annotations.Controller;
import eu.yvka.shadersloth.utils.controller.annotations.FXMLViewContext;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Builder class for creating Controller instances.a
 */
public class ControllerBuilder {

	private static final Logger Log = LoggerFactory.getLogger(ControllerBuilder.class);

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	private static ControllerBuilder INSTANCE;

	private Map<Class<? extends AbstractController>, ViewContext> controllerContexts;


	/******************************************************************************
	 *
	 * Constructors
	 *
	 ************************************************************************F******/

	private ControllerBuilder() {
		controllerContexts = new WeakHashMap<>();
	}


	public synchronized static ControllerBuilder getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ControllerBuilder();
		}
		return INSTANCE;
	}

	/******************************************************************************
	 *
	 * Builder methods
	 *
	 ******************************************************************************/

	public <T extends AbstractController> ViewContext<T>  createBy(Class<T> controllerClass) throws ControllerLoaderException {
		try {
			return createBy(controllerClass.newInstance(), controllerClass);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ControllerLoaderException("Could not create controller: " + controllerClass.getSimpleName(), e);
		}
	}

	public <T extends AbstractController> ViewContext<T> createBy(T controller, Class<T> controllerClass) throws ControllerLoaderException {


		// Load meta data
		ControllerMeta meta = new ControllerMeta();
		Controller controllerAnnotation = controllerClass.getAnnotation(Controller.class);

		if (controllerAnnotation != null) {
			if (! controllerAnnotation.title().isEmpty()) {
				String title = I18N.getString(controllerAnnotation.title());
				meta.setTitle(title);
			}

			if (! controllerAnnotation.icon().isEmpty()) {
				meta.setGraphicNode(new FontIcon(controllerAnnotation.icon()));
			}
		}
		// Load all actions
		loadMethodMetaData(controllerClass, controller, meta);

		// Create loader and controller instance
		Parent rootView = null;
		try {
			FXMLLoader loader = createLoader(controller);
			rootView = loader.load();
		} catch(IOException ex) {
			throw new ControllerLoaderException("Failed to load fxml:" + controller.getLocation().toString(), ex);
		} catch(IllegalStateException ex) {
			throw new ControllerLoaderException(
				controller.getClass().getSimpleName() + " don't provide a valid FXML location",
				ex
			);
		}

		ViewContext<T> ctx = new ViewContext<>(controller, rootView, meta);
		ctx.setProperty(ViewContext.PROPERTY_CONTROLLER, controller);

		try {
			injectDependencies(controllerClass, ctx);
		} catch (Exception ex) {
			throw new ControllerLoaderException("Failed to inject dependencies for " + controller.getClass().getSimpleName(), ex);
		}

		return ctx;
	}

	private <T extends AbstractController> void injectFieldValue(Field field, ViewContext<T> ctx) {
		Class<?> requiredFieldClass = ctx.getClass();
		Class<?> fieldType = field.getType();

		if (fieldType.equals(requiredFieldClass) || requiredFieldClass.isAssignableFrom(fieldType)) {
			ReflectionUtils.setFieldValue(field, ctx.getController() ,ctx);
		}
	}

	private <T extends AbstractController> FXMLLoader createLoader(T controller) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(controller.getLocation());
		loader.setResources(controller.getResourceBundle());
		loader.setCharset(Charset.forName("UTF-8"));
		loader.setController(controller);
		loader.setControllerFactory(c -> controller);
		return loader;
	}

	private <T extends AbstractController> void injectDependencies(Class<T> controllerClass, ViewContext<T> ctx) {



		for(Field field : ReflectionUtils.getAllFields(controllerClass)) {
			FXMLViewContext contextAnnotation = field.getAnnotation(FXMLViewContext.class);
			if (contextAnnotation != null) {
				injectFieldValue(field, ctx);
			}
		}
		ApplicationContext.get().getInjector().injectMembers(ctx.getController());

		injectFXMLFields(controllerClass, ctx);
	}

	/**
	 * Because of some restrictions in the FXMLLoader not all fields that are annotated with @FXML will be injected
	 * such as fields that are included by fx:include.
	 */
	private <T extends AbstractController> void injectFXMLFields(Class<T> controllerClass, ViewContext<T> ctx) {
		T controller = ctx.getController();
		Node root = ctx.getRootNode();
		for (Field field : ReflectionUtils.getAllFields(controllerClass)) {
			if (! field.isAnnotationPresent(FXML.class)) continue;
			Object fieldValue = ReflectionUtils.getFieldValue(field, controller);
			// inject only fields that are not already set by the fxml loader
			if (fieldValue == null) {
				String fieldName = field.getName();
				Node nodeToInject = root.lookup("#" + fieldName);
				if ( nodeToInject != null && field.getType().isAssignableFrom(nodeToInject.getClass())) {
					ReflectionUtils.setFieldValue(field, controller, nodeToInject);
				}
			}
		}
	}

	private <T extends AbstractController> void loadMethodMetaData(Class<T> controllerClass, T controller, ControllerMeta meta) {

		List<Method> actionMethods = ReflectionUtils.getMethodsWithAnnotation(controllerClass, Action.class);
		for (Method method : actionMethods) {
			Action action = method.getAnnotation(Action.class);

			final ActionEntry actionEntry = new ActionEntry();
			actionEntry.setTooltip(action.tooltip());
			actionEntry.setText(action.text());
			actionEntry.setIcon(action.fontIcon());
			actionEntry.setOnAction((calledAction) -> {
				try {
					Platform.runLater(() -> ReflectionUtils.invokeMethod(controller, method));
				} catch (Exception ex) {
					Log.error("Failed to invoke the controller action {}{}.", controllerClass.getName(), method.getName());
					throw new IllegalStateException("Could not invoke action method " + method, ex);
				}
			});

			meta.actionEntriesProperty().add(actionEntry);

		}
	}
}

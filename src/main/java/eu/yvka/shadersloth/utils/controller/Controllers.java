package eu.yvka.shadersloth.utils.controller;

import eu.yvka.shadersloth.context.ViewContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller Registry
 */
public class Controllers {

	private static final Logger Log = LoggerFactory.getLogger(Controllers.class);

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	private static Controllers INSTANCE;

	private Map<Class<? extends AbstractController>, ViewContext> controllerMap;


	/******************************************************************************
	 *
	 * Constructors
	 *
	 ************************************************************************F******/

	private Controllers() {
		controllerMap = new HashMap<>();
	}


	public synchronized static Controllers getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Controllers();
		}
		return INSTANCE;
	}

	public void registerController(Class<? extends AbstractController> controllerType) {
		if (! controllerMap.containsKey(controllerType)) {
			String controllerName = controllerType.getSimpleName();
			try {
				Log.info("Register " +  controllerType.getSimpleName());
				AbstractController controllerInstance = (AbstractController) controllerType.newInstance();
				controllerMap.put(controllerType, controllerInstance.getContext());
			} catch (InstantiationException | ControllerLoaderException | IllegalAccessException e ) {
				throw new IllegalStateException("Failed to register " + controllerType.getSimpleName(), e);
			}
		}
	};

	public void unregisterController(Class<? extends AbstractController> controllerType) {
		if (controllerMap.containsKey(controllerType)) {
			ViewContext ctx = controllerMap.remove(controllerType);
			Log.debug("Shutdown controllers {}", controllerType.getName());
		}
	};

	public <T extends AbstractController> T get(Class<T> controllerType) {
		ViewContext<T> ctx = getContext(controllerType);
		return  ctx != null ? ctx.getController() : null;
	}

	public <T extends AbstractController> ViewContext<T> getContext(Class<T> controllerType) {
		if (controllerMap.containsKey(controllerType)) {
			return controllerMap.get(controllerType);
		}
		return null;
	}

}

package eu.yvka.shadersloth.utils.controller;

import eu.yvka.shadersloth.I18N.I18N;
import eu.yvka.shadersloth.context.ViewContext;
import eu.yvka.shadersloth.utils.ExceptionUtils;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * A Controller for a specific view.
 * A view is a fxml file which is load and initialized by this controller.
 * The controller is responsible to manage the specified view.
 */
public abstract class AbstractController {

	/**
	 * The logger for this class
	 */
	private Logger Log = LoggerFactory.getLogger(AbstractController.class);

	/**
	 * The FXML view/file which is related to this controller
	 */
	private URL fxmlFile;

	/**
	 * The current local Resource Bundle.
	 * The {@link ResourceBundle} contains all locale-specific
	 * texts and strings.
	 */
	private ResourceBundle resourceBundle;

	/**
	 * Context of the controlled view.
	 */
	private ViewContext<AbstractController> viewContext;

	/**
	 * Creates a controller for the view which is defined
	 * in the fxml file at the specified location.
     */
	public AbstractController(URL location) {
		this(location, I18N.getResourceBundle());
	}

	/**
	 * <b>This constructor is only for internal use</b>
	 *
	 * Creates a controller for the view which is defined
	 * in the fxml file at the specified location. And specifies
	 * the {code ResourceBundle} which should be used by the controlled
	 * view.
	 *
	 * @param location
	 * @param resources
     */
	protected AbstractController(URL location, ResourceBundle resources) {
		this.fxmlFile = location;
		this.resourceBundle = resources;
	}

	/**
	 * Retrieves the root element of the controlled view.
	 *
	 * @return the root element
     */
	public Parent getRoot() {
		try {
			return getContext().getRootNode();
		} catch (ControllerLoaderException ex) {
			throw new IllegalStateException("Could not obtain root node of " + getClass().getSimpleName(), ex);
		}
	}

	/**
	 * Provides context information about this controller.
	 *
	 * @return context about this view object.
     */
	public ViewContext<AbstractController> getContext() throws ControllerLoaderException {
		if (viewContext == null) {
			initiateController();
		}
		return viewContext;
	}

	/**
	 * Initiate the controller and load the corresponding fxml file which contains the
	 * view of this controller.
	 */
	@SuppressWarnings("unchecked")
	protected void initiateController() throws ControllerLoaderException {
		try {
			viewContext = ControllerBuilder.getInstance().createBy(this, (Class<AbstractController>) getClass());
		} catch (Exception e) {
			throw new ControllerLoaderException("Failed to initiate " + this.getClass().getSimpleName(), e);
		}
		try {
			onFxmlLoaded();
		} catch (Exception ex) {
			String errorLocation = ExceptionUtils.getFileLocationOfTraceElement(ex, 0);
			throw new ControllerLoaderException("Error in invoked onFxmlLoaded of " + getClass().getSimpleName() + errorLocation, ex);
		}
	}

	@PostConstruct
	private void triggerOnFXMLLoaded() {
		onFxmlLoaded();
	}

	/**
	 * A subclass must implement its view initial logic in this method.
	 *
	 * <p>
	 * Fields which are annotated with {@link javafx.fxml.FXML} are first accessible
	 * while this method is invoked by the {@link AbstractController}.
	 * </p>
	 *
	 * </p>
	 * Called when the fxml file was loaded this is the
	 * equivalent to {@link javafx.fxml.Initializable#initialize(URL, ResourceBundle)}.
	 * <p>
	 *
	 */
	abstract protected void onFxmlLoaded();

	/**
	 * @return the assigned resource bundle which
	 * is used for the controlled view.
	 * The {@link ResourceBundle} contains all locale-specific
	 * texts and strings.
	 */
	ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	/**
	 * @return the location of the corresponding fxml file.
	 * This contains the view which is controlled by
	 * this controller.
	 */
	URL getLocation() {
		return fxmlFile;

	}


	/**
	 * Will be called before if controller
	 * is resumed from a pause state.
	 * @deprecated
	 */
	public void onResume() {
	};

	/**
	 * Will be called before a controller is paused.
	 * @deprecated
	 */
	public void onPause() {

	}
}

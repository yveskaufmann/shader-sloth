package eu.yvka.shadersloth.utils.controller;

import eu.yvka.shadersloth.I18N.I18N;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A controller which controls a window and defines its content
 * by specifying a view/fxml file.
 */
public abstract class AbstractWindowController extends AbstractController {

	private static final Logger Log = LoggerFactory.getLogger(AbstractWindowController.class);

	/**
	 * The owner of the window which this controller
	 * controls.
	 */
	private Window owner;

	/**
	 * The Stage of this window.
	 */
	private Stage stage;

	/**
	 * The scene which is controlled by this controller.
	 */
	private Scene scene;

	/**
	 * Defines the title of the window.
	 *
	 * @see #titleProperty()
	 */
	private StringProperty title;

	/**
	 * Creates a {@code WindowController} for a top level window and specifies
	 * the corresponding view and set the default {@link ResourceBundle}.
	 *
	 * Use this constructor for creating a controller for a top level
	 * window.
	 *
	 * @param location a url to the corresponding fxml/view file.
     */
	public AbstractWindowController(URL location) {
		this(location, I18N.getResourceBundle(), null);
	}

	/**
	 * Creates a {@code WindowController} for a non top level window and specifies
	 * the corresponding view and set the default {@link ResourceBundle}.
	 *
	 * Use this constructor for creating a controller for a child window
	 * of the {@code owner} window.
 	 *
	 * @param location a url to the corresponding fxml/view file.
	 *
	 * @param owner the parent window of the controlled window.
     */
	public AbstractWindowController(URL location, Window owner) {
		this(location, I18N.getResourceBundle(), owner);
	}

	/**
	 * Creates a {@code WindowController} for a window and specifies
	 * the corresponding view, resourceBundle and its owner.
	 *
	 * @param location a url to the corresponding fxml/view file.
	 *
	 * @param resourceBundle the {@link ResourceBundle} which should be used for internalization.
	 * This is the place where all translated texts/string are stored
	 *
	 * @param owner the parent window of the controlled window.
	 * If null the controlled window will be a top level window.
     */
	protected AbstractWindowController(URL location, ResourceBundle resourceBundle, Window owner) {
		super(location, resourceBundle);
		this.owner = owner;
	}

	/**
	 * Retrieves the scene of the controlled window.
	 * Enables sub classes to access the scene of the
	 * controlled window.
	 *
	 * @return the scene of the controlled window.
     */
	public Scene getScene() {
		if (scene == null) {
			Parent root = getRoot();
			scene = new Scene(root, 1024, 768, true, SceneAntialiasing.BALANCED);
			onSceneCreated();
		}
		return scene;
	}

	/**
	 * Retrieves the stage of the controlled window.
	 *
	 * @return the stage of the controlled window.
     */
	public Stage getStage() {
		if (stage == null) {
			if (owner instanceof Stage) {
				stage = (Stage) owner;
			} else {
				stage = new Stage();
				if (owner != null) {
					stage.initOwner(owner);
				}
			}

			stage.titleProperty().bind(titleProperty());
			stage.setOnCloseRequest(this::onCloseRequest);
			stage.setScene(getScene());
			stage.sizeToScene();
			onStageCreated();
		}
		return stage;
	}

	/**
	 * Show this window in front all other windows.
	 */
	public void show() {
		getStage().show();
		getStage().toFront();
	}

	/**
	 * Close the controlled window.
	 */
	public void close() {
		getStage().close();
	}

	/**
	 * Defines the title of the window.
	 *
	 * @defaultValue empty string
	 * @return the title property.
	 * @see #getTitle()
	 * @see #setTitle(String)
	 */
	public StringProperty titleProperty() {
		if (title == null) {
			title = new SimpleStringProperty(this, "title", "");
		}
		return title;
	}

	/**
	 * @return the title of the window
     */
	public String getTitle() {
		return titleProperty().get();
	}

	/**
	 * Set the value of the title property.
	 *
	 * @param title the title of the window
	 *
	 * @see #titleProperty()
     */
	public void setTitle(String title) {
		titleProperty().set(title);
	}

	/**
	 * Will be called when the controlled window is requested to be closed.
	 * Enables a sub class to respond to window close events.
	 *
	 * @param windowEvent the event related to the closing action.
	 */
	abstract public void onCloseRequest(WindowEvent windowEvent);

	/**
	 * Called when the scene was created.
	 * A subclass must implement its scene initialization
	 * in this method.
	 */
	abstract protected void onSceneCreated();

	/**
	 * Called when the stage was created.
	 * A subclass must implement its stage initialization
	 * in this method.
	 */
	abstract protected void onStageCreated();


}

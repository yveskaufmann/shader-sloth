package de.yvka.shadersloth.utils;

import de.yvka.shadersloth.I18N.I18N;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractWindowController extends AbstractController {


	private Window owner;
	private Stage stage;
	private Scene scene;

	/**
	 * The title of the window
	 *
	 * @return the title property.
     */
	public StringProperty titleProperty() {
		if (title == null) {
			title = new StringPropertyBase("") {
				@Override
				public Object getBean() {
					return AbstractWindowController.this;
				}

				@Override
				public String getName() {
					return "title";
				}
			};
		}
		return title;
	}
	private StringProperty title;

	public String getTitle() {
		return titleProperty().get();
	}

	public void setTitle(String title) {
		titleProperty().set(title);
	}


	public AbstractWindowController(URL location) {
		this(location, I18N.getResourceBundle(), null);
	}

	public AbstractWindowController(URL location, Window owner) {
		this(location, I18N.getResourceBundle(), owner);
	}

	public AbstractWindowController(URL location, ResourceBundle resourceBundle, Window owner) {
		super(location, resourceBundle);
		this.owner = owner;
	}


	public Scene getScene() {
		if (scene == null) {
			Parent root = getRoot();
			scene = new Scene(root);
			onSceneCreated();
		}
		return scene;
	}

	public Stage getStage() {
		if (stage == null) {
			stage = createStage();
			if (owner != null) {
				stage.initOwner(owner);
			}
			stage.titleProperty().bind(titleProperty());
			stage.setOnCloseRequest(this::onCloseRequest);
			stage.setScene(getScene());
			stage.sizeToScene();
			onStageCreated();
		}
		return stage;
	}

	protected Stage createStage() {
		return new Stage();
	}

	/**
	 * Show this window in front all other windows.
	 */
	public void show() {
		getStage().showAndWait();
		getStage().toFront();
	}

	/**
	 * Close this window.
	 */
	public void close() {
		getStage().close();
	}


	/**
	 * Will be called when the window is requested to be closed.
	 *
	 * @param windowEvent
     */
	abstract public void onCloseRequest(WindowEvent windowEvent);

	/**
	 * Called when the scene was created
	 */
	abstract protected void onSceneCreated();

	/**
	 * Called when the stage was created
	 */
	abstract protected void onStageCreated();


}

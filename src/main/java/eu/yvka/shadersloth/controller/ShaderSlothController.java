package eu.yvka.shadersloth.controller;

import eu.yvka.shadersloth.I18N.I18N;
import eu.yvka.shadersloth.ShaderSloth;
import eu.yvka.shadersloth.controller.genericEditor.GenericEditorController;
import eu.yvka.shadersloth.utils.controller.AbstractWindowController;
import eu.yvka.slothengine.engine.AppSettings;
import eu.yvka.slothengine.engine.JavaFXOffscreenSupport;
import eu.yvka.slothengine.scene.Scene;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.concurrent.CountDownLatch;

public class ShaderSlothController extends AbstractWindowController {

	@FXML private TitledPane sceneTreeRoot;
	@FXML private TitledPane editorRoot;
	@FXML private TitledPane materialEditorRoot;
	@FXML private AnchorPane renderViewRoot;
	@FXML private ImageView renderView;

	private Stage primaryStage;
	private ShaderSloth rendererLoop;
	private final CountDownLatch runningLatch = new CountDownLatch(1);

	private SceneTreeEditor sceneTreeEditor = new SceneTreeEditor(this);
	private GenericEditorController genericEditorController = new GenericEditorController(this);

	public ShaderSlothController(Stage primaryStage) {
		super(ShaderSloth.class.getResource("view/shaderSlothView.fxml"));
		setTitle(I18N.getString("app.title"));
		this.primaryStage = primaryStage;
	}

	@Override
	protected void onFxmlLoaded() {
		assert renderView != null;

		sceneProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) loadScene(newValue);
		});

		sceneTreeRoot.setContent(sceneTreeEditor.getRoot());
		editorRoot.setContent(genericEditorController.getRoot());
		initRenderView();
	}

	@Override
	protected void onStageCreated() {
		getStage().setMinWidth(1024);
		getStage().setMinHeight(768);
		getStage().sizeToScene();
	}

	@Override
	protected void onSceneCreated() {
		getScene().getStylesheets().add(ShaderSloth.class.getResource("css/style.css").toExternalForm());
	}

	/**
	 * This method will run the rendering thread.
	 * This method is adapted from: https://github.com/Spasi/LWJGL-FX/blob/master/src/lwjglfx/GUIController.java
	 *
	 * @param runningLatch
     */
	public void runRenderer(final CountDownLatch runningLatch) {
		AppSettings settings  = new AppSettings();

		settings.set(AppSettings.Width, (int) renderView.getFitWidth());
		settings.set(AppSettings.Height, (int) renderView.getFitHeight());
		settings.set(JavaFXOffscreenSupport.JAVAFX_OFFSCREEN_SUPPORT, new JavaFXOffscreenSupport(renderView, runningLatch));

		rendererLoop = new ShaderSloth();
		rendererLoop.setOnStartedCallback(scene -> Platform.runLater(() -> sceneProperty().set(scene)));
		rendererLoop.start(settings);
	}

	private void loadScene(Scene scene) {
		sceneTreeEditor.loadScene(scene);
	}


	private void initRenderView() {

		renderView.setPreserveRatio(true);
		renderViewRoot.widthProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.intValue() % 2 != 0) {
				newValue = newValue.intValue() + 1;
			}
			renderView.fitWidthProperty().set(newValue.intValue());

		});
		renderViewRoot.heightProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.intValue() % 2 != 0) {
				newValue = newValue.intValue() + 1;
			}
			renderView.fitHeightProperty().set(newValue.intValue());

		});
	}

	@Override
	public void onCloseRequest(WindowEvent windowEvent) {
		runningLatch.countDown();
	}

	@Override
	public void show() {
		getStage().show();
		new Thread(() -> {
			runRenderer(runningLatch);
			Platform.runLater(primaryStage::close);
		}).start();
	}

	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/

	private ObjectProperty<Scene> scene;

	/**
	 * @return the related scene property of this controller.
     */
	public ObjectProperty<Scene> sceneProperty() {
		if (scene == null) {
			scene = new SimpleObjectProperty<Scene>(this, "scene");
		}
		return scene;
	}

	/***
	 * @return the related scene of this controller.
     */
	public Scene getSlothScene() {
		return sceneProperty().get();
	}

	/**
	 * @return the generic editor instance
     */
	public GenericEditorController getGenericEditorController() {
		return genericEditorController;
	}

	/***
	 * @return the scene editor
     */
	public SceneTreeEditor getSceneTreeEditor() {
		return sceneTreeEditor;
	}
}

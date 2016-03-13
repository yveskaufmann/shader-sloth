package de.yvka.shadersloth.controller;

import de.yvka.shadersloth.I18N.I18N;
import de.yvka.shadersloth.ModelController;
import de.yvka.shadersloth.ShaderSloth;
import de.yvka.shadersloth.controller.genericEditor.GenericEditorController;
import de.yvka.shadersloth.controls.SceneTreeCell;
import de.yvka.shadersloth.dialog.GeometryCreateDialog;
import de.yvka.shadersloth.utils.AbstractWindowController;
import de.yvka.slothengine.engine.AppSettings;
import de.yvka.slothengine.engine.JavaFXOffscreenSupport;
import de.yvka.slothengine.scene.Geometry;
import de.yvka.slothengine.scene.Node;
import de.yvka.slothengine.scene.Scene;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class ShaderSlothController extends AbstractWindowController {

	@FXML private TitledPane sceneTreeRoot;
	@FXML private TitledPane editorRoot;
	@FXML private TitledPane materialEditorRoot;
	@FXML private AnchorPane renderViewRoot;
	@FXML private ImageView renderView;

	public ObjectProperty<Scene> slothSceneProperty() {
		if (scene == null) {
			scene = new ObjectPropertyBase<Scene>() {

				@Override
				public Object getBean() {
					return ShaderSlothController.this;
				}

				@Override
				public String getName() {
					return "sceneProperty";
				}
			};
		}
		return scene;
	}
	private ObjectProperty<Scene> scene;
	public Scene getSlothScene() {
		return slothSceneProperty().get();
	}

	private Stage primaryStage;
	private ShaderSloth rendererLoop;
	private final CountDownLatch runningLatch = new CountDownLatch(1);

	private SceneTreeEditor sceneTreeEditor = new SceneTreeEditor(this);
	private GenericEditorController genericEditorController = new GenericEditorController(this);


	public ShaderSlothController(Stage primaryStage) {
		super(ShaderSloth.class.getResource("view/shaderSlothView.fxml"));
		setTitle(I18N.getString("app.title"));
		this.primaryStage = primaryStage;

		slothSceneProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) loadScene(newValue);
		});

		getStage();
	}

	@Override
	protected void onFxmlLoaded() {
		assert renderView != null;
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
		getScene().getStylesheets().add(ShaderSloth.class.getResource("shaderSloth.css").toExternalForm());
	}

	@Override
	protected Stage createStage() {
		return this.primaryStage;
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
		rendererLoop.setOnStartedCallback(scene -> Platform.runLater(() -> slothSceneProperty().set(scene)));
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
		getStage().toFront();
		new Thread(() -> {
			runRenderer(runningLatch);
			Platform.runLater(primaryStage::close);
		}).start();
	}

	public GenericEditorController getGenericEditorController() {
		return genericEditorController;
	}

	public SceneTreeEditor getSceneTreeEditor() {
		return sceneTreeEditor;
	}
}

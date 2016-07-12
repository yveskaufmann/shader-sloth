package eu.yvka.shadersloth.controllers;

import eu.yvka.shadersloth.I18N.I18N;
import eu.yvka.shadersloth.ShaderSlothRenderer;
import eu.yvka.shadersloth.controllers.genericEditor.GenericEditorController;
import eu.yvka.shadersloth.controls.ShaderEditor;
import eu.yvka.shadersloth.services.RenderService;
import eu.yvka.shadersloth.utils.controller.AbstractWindowController;
import eu.yvka.slothengine.scene.Scene;
import eu.yvka.slothengine.shader.source.ShaderSource;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShaderSlothController extends AbstractWindowController {

	private static final Logger Log = LoggerFactory.getLogger(ShaderSlothController.class);

	/******************************************************************************
	 *
	 * FXML Fields
	 *
	 ******************************************************************************/

	@FXML private TitledPane sceneTreeRoot;
	@FXML private TitledPane editorRoot;
	@FXML private TitledPane materialEditorRoot;
	@FXML private AnchorPane renderViewRoot;
	@FXML private ImageView renderView;
	@FXML private TabPane sourceTabs;
	@FXML private Tab sceneTab;

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	private SceneTreeEditorController sceneTreeEditor = new SceneTreeEditorController(this);
	private GenericEditorController genericEditorController = new GenericEditorController(this);
	private MaterialEditorController materialEditorController = new MaterialEditorController(this);
	private RenderService renderService;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public ShaderSlothController(Stage primaryStage) {
		super(ShaderSlothRenderer.class.getResource("view/shaderSlothView.fxml"), primaryStage);
		setTitle(I18N.getString("app.title"));

	}

	@Override
	protected void onFxmlLoaded() {
		assert renderView != null;
		getStage().getIcons().add(new Image(ShaderSlothRenderer.class.getResource("images/material_icon_64x64.png").toExternalForm()));

		sceneTreeRoot.setContent(sceneTreeEditor.getRoot());
		editorRoot.setContent(genericEditorController.getRoot());
		materialEditorRoot.setContent(materialEditorController.getRoot());
		renderService = new RenderService(renderView);

		initSourceView();
		initRenderView();

		renderService.sceneProperty().addListener((observable, oldValue, newValue) -> {
			Platform.runLater(() -> {
				if (newValue != null) loadScene(newValue);
			});
		});
	}


	private void initSourceView() {
		materialEditorController.selectedMaterialProperty().addListener((observable, oldValue, newValue) -> {
			sourceTabs.getTabs().clear();
			if (newValue != null) {
				for (ShaderSource source : newValue.getShader().getShaderSources()) {
					String[] path = source.getName().split("/");
					Tab tab = new Tab(path[path.length - 1]);
					ShaderEditor shaderEditor = new ShaderEditor(source);
					tab.setContent(shaderEditor);
					sourceTabs.getTabs().add(tab);
				}

			}
        });
		sourceTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
	}

	@Override
	protected void onStageCreated() {
		getStage().setWidth(1280);
		getStage().setHeight(1024);
		getStage().centerOnScreen();
	}

	@Override
	public void onCloseRequest(WindowEvent windowEvent) {
		stop();
	}

	@Override
	protected void onSceneCreated() {
		getScene().getStylesheets().add(ShaderSlothRenderer.class.getResource("css/style.css").toExternalForm());
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

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

	public void start() {
		Log.info("Start ShaderSlothRenderer View");
		renderService.start();
	}


	public void stop() {
		Log.info("Stop ShaderSlothRenderer View");
		renderService.cancel();
	}


	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/

	private ObjectProperty<Scene> slothScene;

	/**
	 * @return the related slothScene property of this controllers.
     */
	public ObjectProperty<Scene> sceneProperty() {
		if (slothScene == null) {
			slothScene = new SimpleObjectProperty<Scene>(this, "slothScene");
		}
		return slothScene;
	}

	/***
	 * @return the related slothScene of this controllers.
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
	 * @return the slothScene editor
     */
	public SceneTreeEditorController getSceneTreeEditor() {
		return sceneTreeEditor;
	}

	public ObjectProperty<Scene> slothSceneProperty() {
		if (slothScene == null) {
			slothScene = new SimpleObjectProperty<>(this, "sceneProperty");
		}
		return slothScene;
	}


}

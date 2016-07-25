package eu.yvka.shadersloth.app;

import eu.yvka.shadersloth.app.materialEditor.MaterialEditorController;
import eu.yvka.shadersloth.app.materialEditor.MaterialEvent;
import eu.yvka.shadersloth.app.project.DummyProject;
import eu.yvka.shadersloth.app.project.Project;
import eu.yvka.shadersloth.app.sceneEditor.SceneTreeEditorController;
import eu.yvka.shadersloth.share.I18N.I18N;
import eu.yvka.shadersloth.app.sceneEditor.genericEditor.GenericEditorController;
import eu.yvka.shadersloth.app.menubar.MenuBarController;
import eu.yvka.shadersloth.app.materialEditor.shaders.ShaderEditor;
import eu.yvka.shadersloth.app.renderView.RenderService;
import eu.yvka.shadersloth.share.controller.AbstractWindowController;
import eu.yvka.slothengine.engine.Engine;
import eu.yvka.slothengine.scene.Scene;
import eu.yvka.slothengine.shader.source.ShaderSource;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ShaderSlothController extends AbstractWindowController {

	private static final Logger Log = LoggerFactory.getLogger(ShaderSlothController.class);

	public enum ApplicationAction {
		ACTION_NEW,
		ACTION_OPEN,
		ACTION_SAVE,
		ACTION_SAVE_AS,
		ACTION_OPEN_RECENT,
		ACTION_QUIT,
		ACTION_ABOUT
	}

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
	@FXML private MenuBarController menuBarController;

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	private SceneTreeEditorController sceneTreeEditor = new SceneTreeEditorController(this);
	private GenericEditorController genericEditorController = new GenericEditorController(this);
	private MaterialEditorController materialEditorController = new MaterialEditorController(this);
	private RenderService renderService;
	private ObjectProperty<Project> currentProject;

	/******************************************************************************
	 *
	 * Construction
	 *
	 ******************************************************************************/

	public ShaderSlothController(Stage primaryStage) {
		super(App.class.getResource("view/shaderSlothView.fxml"), primaryStage);
		setTitle(I18N.getString("app.title"));

	}

	@Override
	protected void onFxmlLoaded() {
		assert renderView != null;
		getStage().getIcons().add(new Image(App.class.getResource("images/material_icon_64x64.png").toExternalForm()));

		menuBarController.initAppController(this);

		sceneTreeRoot.setContent(sceneTreeEditor.getRoot());
		editorRoot.setContent(genericEditorController.getRoot());
		materialEditorRoot.setContent(materialEditorController.getRoot());
		renderService = new RenderService(renderView);

		getRoot().addEventHandler(MaterialEvent.ANY, this::onMaterialChanged);

		initSourceView();
		initRenderView();


	}




	private void initSourceView() {
		materialEditorController.selectedMaterialProperty().addListener((observable, oldValue, newValue) -> {
			sourceTabs.getTabs().clear();
			if (newValue != null) {
				for (ShaderSource source : newValue.getShader().getShaderSources()) {
					final File file = new File(source.getName());
					Tab tab = new Tab(file.getName());
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
		performAction(ApplicationAction.ACTION_QUIT);
	}

	@Override
	protected void onSceneCreated() {
		getScene().getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	public void performOpenRecentProject(File file) {
		// TODO: implement open project
	}

	public boolean canPerformAction(ApplicationAction action) {
		boolean canPerform = false;
		switch (action) {
			case ACTION_NEW:
			case ACTION_OPEN:
			case ACTION_OPEN_RECENT:
			case ACTION_QUIT:
				canPerform = true;
			break;

		}
		return canPerform;
	}

	public void performAction(ApplicationAction action) {
		switch (action) {
			case ACTION_NEW:
				performNewProject();
				break;
			case ACTION_QUIT:
				performQuit();
			break;

		}
	}

	private void performNewProject() {
		if (currentProject != null) {
			// TODO: request for save
		}

		final Project newProject = new DummyProject();
		setProject(newProject);
		renderService.setScene(newProject.getScene());
		loadProject(getProject());
	}

	private void loadProject(Project project) {
		final Scene scene = project.getScene();
		renderService.setScene(scene);
		sceneTreeEditor.loadScene(scene);
		materialEditorController.loadProject(project);
	}

	private void performQuit() {
		final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		final ButtonType save = new ButtonType(I18N.getString("alert.btn.save"), ButtonBar.ButtonData.YES);
		final ButtonType discard = new ButtonType(I18N.getString("alert.btn.discard"), ButtonBar.ButtonData.NO);
		final ButtonType cancel = new ButtonType(I18N.getString("alert.btn.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(save, discard, ButtonType.CANCEL);
		alert.setHeaderText(I18N.getString("alert.confirm.changes.header", "UNTITLED"));
		alert.setContentText(I18N.getString("alert.confirm.changes.message"));
		alert.showAndWait().ifPresent((buttonType) -> {
			boolean exitRequested = false;
			if (cancel.equals(buttonType)) {
				exitRequested = false;
			}

			if (discard.equals(buttonType)) {
				exitRequested = true;
			}

			if (save.equals(buttonType)) {
				exitRequested = true;
				// TODO: save unsaved project
			}

			if (exitRequested) {
				stop();
				Platform.runLater(() -> {
					if (renderService.isRunning()) {
						renderService.cancel();
					}
					Platform.exit();
				});
			}
		});
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

	/***
	 * Starts the render service.
	 */
	public void start() {
		Log.info("Start ShaderSlothRenderer View");
		renderService.start();
		Engine.runWhenReady(() -> {
			Platform.runLater(() -> {
				performNewProject();
			});
		});
	}


	/**
	 * Stops the render service
	 */
	public void stop() {
		Log.info("Stop ShaderSlothRenderer View");
		renderService.cancel();
	}

	/******************************************************************************
	 *
	 * Event Handlers
	 *
	 ******************************************************************************/

	private void onMaterialChanged(MaterialEvent event) {
		// notify editors that the material list has changed
		genericEditorController.refresh();
		event.consume();
	}

	/******************************************************************************
	 *
	 * Controller Getter
	 *
	 ******************************************************************************/

	/**
	 * @return the editor controller instance
	 */
	public GenericEditorController getGenericEditorController() {
		return genericEditorController;
	}

	/***
	 * @return the scene editor controller instance
	 */
	public SceneTreeEditorController getSceneTreeEditorController() {
		return sceneTreeEditor;
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
		return null;
	}

	/***
	 * @return the related slothScene of this controllers.
     */
	public Scene getSlothScene() {
		return sceneProperty().get();
	}


	/**
	 *
	 * @return
	 * @deprecated
     */
	public ObjectProperty<Scene> slothSceneProperty() {
		if (slothScene == null) {
			slothScene = new SimpleObjectProperty<>(this, "sceneProperty");
		}
		return slothScene;
	}


	/**
	 * This property provides the currently active project.
	 *
	 * @return the property which contains the current project.
	 */
	public ObjectProperty<Project> currentProjectProperty() {
		if (currentProject == null) {
			currentProject = new SimpleObjectProperty<>(this, "currentProject");
		}
		return currentProject;
	}

	/**
	 * Retrieves the current project
	 *
	 * @return the current project
     */
	public Project getProject() {
		return currentProject == null ? null : currentProject.get();
	}

	/**
	 * Specifies the current project.
	 *
	 * @param project the project which should be loaded
     */
	public void setProject(Project project) {
		currentProjectProperty().set(project);
	}


}

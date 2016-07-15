package eu.yvka.shadersloth.app.controllers;

import eu.yvka.shadersloth.app.App;
import eu.yvka.shadersloth.share.controller.AbstractWindowController;
import javafx.stage.WindowEvent;

public class ModelController extends AbstractWindowController {


	public ModelController() {
		super(App.class.getResource("view/modelEditor.fxml"));
		setTitle("Model Editor");
	}



	@Override
	public void onCloseRequest(WindowEvent windowEvent) {
		if (getStage().getOwner() != null) {
			getStage().getOwner().sizeToScene();
		}
	}

	@Override
	protected void onSceneCreated() {
	}

	@Override
	protected void onStageCreated() {
	}

	@Override
	protected void onFxmlLoaded() {
	}
}

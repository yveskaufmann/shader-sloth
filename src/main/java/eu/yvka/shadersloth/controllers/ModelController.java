package eu.yvka.shadersloth.controllers;

import eu.yvka.shadersloth.ShaderSlothRenderer;
import eu.yvka.shadersloth.utils.controller.AbstractWindowController;
import javafx.stage.WindowEvent;

public class ModelController extends AbstractWindowController {


	public ModelController() {
		super(ShaderSlothRenderer.class.getResource("view/modelEditor.fxml"));
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

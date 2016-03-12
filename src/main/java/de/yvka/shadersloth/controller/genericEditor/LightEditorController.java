package de.yvka.shadersloth.controller.genericEditor;

import de.yvka.shadersloth.ShaderSloth;
import de.yvka.shadersloth.controller.ShaderSlothController;
import de.yvka.shadersloth.controls.NumberInput;
import de.yvka.shadersloth.utils.AbstractController;
import de.yvka.slothengine.scene.light.Light;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class LightEditorController extends AbstractController {

	@FXML private TextField lightId;
	@FXML private NumberInput xPosition;
	@FXML private NumberInput yPosition;
	@FXML private NumberInput zPosition;
	@FXML private ChoiceBox lightTypeChoice;

	private final ShaderSlothController slothController;

	public LightEditorController(ShaderSlothController controller) {
		super(ShaderSloth.class.getResource("view/lightEditor.fxml"));
		this.slothController = controller;
	}

	@Override
	protected void onFxmlLoaded() {

	}

	public void updateData(Light node) {

	}
}

package de.yvka.shadersloth.controller.genericEditor;

import de.yvka.shadersloth.I18N.I18N;
import de.yvka.shadersloth.ShaderSloth;
import de.yvka.shadersloth.controller.ShaderSlothController;
import de.yvka.shadersloth.controls.NumberInput;
import de.yvka.shadersloth.utils.AbstractController;
import de.yvka.slothengine.math.Color;
import de.yvka.slothengine.scene.Geometry;
import de.yvka.slothengine.scene.light.Light;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

import java.util.Optional;

public class LightEditorController extends AbstractController {

	@FXML private TextField lightId;
	@FXML private NumberInput xPosition;
	@FXML private NumberInput yPosition;
	@FXML private NumberInput zPosition;
	@FXML private ChoiceBox<String> lightTypeChoice;
	@FXML private ColorPicker lightIntensity;

	private Optional<Light> selectedNode = Optional.empty();
	private final ShaderSlothController slothController;

	public LightEditorController(ShaderSlothController controller) {
		super(ShaderSloth.class.getResource("view/lightEditor.fxml"));
		this.slothController = controller;
	}

	@Override
	protected void onFxmlLoaded() {

		lightId.textProperty().addListener((currentId, oldId, newId) -> {
			selectedNode.ifPresent((node) -> {
				node.setId(newId);
				slothController.getSceneTreeEditor().notifyUpdate();
			});
		});
		xPosition.valueProperty().addListener((observable, oldValue, newValue) -> {
			selectedNode.ifPresent((node) -> node.getPosition().x = newValue.floatValue());
		});

		yPosition.valueProperty().addListener((observable, oldValue, newValue) -> {
			selectedNode.ifPresent((node) -> node.getPosition().y = newValue.floatValue());
		});

		zPosition.valueProperty().addListener((observable, oldValue, newValue) -> {
			selectedNode.ifPresent((node) -> node.getPosition().z = newValue.floatValue());
		});

		lightIntensity.valueProperty().addListener((observable, oldValue, newValue) -> {
			selectedNode.ifPresent((node) -> {
				node.setColor(Color.fromRGB(newValue.getRed(), newValue.getGreen(), newValue.getBlue()));
			});
		});

		lightTypeChoice.getItems().addAll(
			I18N.getString("light.type.point"),
			I18N.getString("light.type.direction"),
			I18N.getString("light.type.ambient")
		);
		lightTypeChoice.getSelectionModel().selectFirst();

	}

	public void updateData(Light node) {
		selectedNode = Optional.of(node);
		lightId.setText(node.getId());
		xPosition.setValue(node.getPosition().x);
		yPosition.setValue(node.getPosition().y);
		zPosition.setValue(node.getPosition().z);

		Color color = node.getColor();
		lightIntensity.setValue(
			javafx.scene.paint.Color.color(
				color.getRed(),
				color.getBlue(),
				color.getBlue()
			)
		);


	}
}

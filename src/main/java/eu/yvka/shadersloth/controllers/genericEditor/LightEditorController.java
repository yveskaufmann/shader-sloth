package eu.yvka.shadersloth.controllers.genericEditor;

import eu.yvka.shadersloth.I18N.I18N;
import eu.yvka.shadersloth.ShaderSlothRenderer;
import eu.yvka.shadersloth.controllers.ShaderSlothController;
import eu.yvka.shadersloth.controls.NumberInput;
import eu.yvka.shadersloth.utils.controller.AbstractController;
import eu.yvka.slothengine.math.Color;
import eu.yvka.slothengine.scene.light.Light;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.util.Optional;

public class LightEditorController extends AbstractController {

	@FXML private TextField lightId;
	@FXML private NumberInput xPosition;
	@FXML private NumberInput yPosition;
	@FXML private NumberInput zPosition;
	@FXML private ChoiceBox<String> lightTypeChoice;
	@FXML private ColorPicker lightIntensity;
	@FXML private NumberInput lightEnergy;
	@FXML private Slider lightEnergySlider;

	private Optional<Light> selectedNode = Optional.empty();
	private final ShaderSlothController slothController;

	public LightEditorController(ShaderSlothController controller) {
		super(ShaderSlothRenderer.class.getResource("view/lightEditor.fxml"));
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

		lightEnergySlider.setMin(0.0);
		lightEnergySlider.setMax(10.0);
		lightEnergySlider.setBlockIncrement(0.1);
		lightEnergySlider.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
			lightEnergy.setValue(newValue1.doubleValue());
		});


		lightEnergy.setMaxValue(10.0);
		lightEnergy.setMinValue(0.0);
		lightEnergy.valueProperty().addListener((observable, oldValue, newValue) -> {
			lightEnergySlider.setValue(newValue.doubleValue());
			selectedNode.ifPresent((node) -> {
				node.setAttenuation(newValue.floatValue());
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
		lightEnergy.setValue(node.getAttenuation());


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

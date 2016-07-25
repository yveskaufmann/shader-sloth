package eu.yvka.shadersloth.app.sceneEditor.genericEditor;

import eu.yvka.shadersloth.app.App;
import eu.yvka.shadersloth.share.I18N.I18N;
import eu.yvka.shadersloth.app.ShaderSlothController;
import eu.yvka.shadersloth.app.controls.NumberInput;
import eu.yvka.shadersloth.share.controller.AbstractController;
import eu.yvka.slothengine.math.Color;
import eu.yvka.slothengine.scene.light.Light;
import eu.yvka.slothengine.scene.light.LightType;
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
		super(App.class.getResource("view/lightEditor.fxml"));
		this.slothController = controller;
	}

	@Override
	protected void onFxmlLoaded() {

		lightId.textProperty().addListener((currentId, oldId, newId) -> {
			selectedNode.ifPresent((node) -> {
				node.setId(newId);
				slothController.getSceneTreeEditorController().notifyUpdate();
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


		lightEnergy.setMaxValue(10.0);
		lightEnergy.setMinValue(0.0);
		lightEnergy.valueProperty().bindBidirectional(lightEnergySlider.valueProperty());
		lightEnergy.valueProperty().addListener((observable, oldValue, newValue) -> {
			lightEnergySlider.setValue(newValue.doubleValue());
			selectedNode.ifPresent((node) -> {
				node.setAttenuation(newValue.floatValue());
			});
		});

		lightTypeChoice.getItems().addAll(
			I18N.getString("light.type.point"),
			I18N.getString("light.type.ambient")
		);
		// only view able but not change able
		lightTypeChoice.setDisable(true);
		lightTypeChoice.getSelectionModel().selectFirst();

	}

	public void updateData(Light node) {
		selectedNode = Optional.of(node);
		lightId.setText(node.getId());
		xPosition.setValue(node.getPosition().x);
		yPosition.setValue(node.getPosition().y);
		zPosition.setValue(node.getPosition().z);
		lightEnergy.setValue(node.getAttenuation());
		lightTypeChoice.setValue(node.getType().equals(LightType.Ambient) ? lightTypeChoice.getItems().get(1) : lightTypeChoice.getItems().get(0));


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

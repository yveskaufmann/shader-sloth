package de.yvka.shadersloth.controller.genericEditor;

import de.yvka.shadersloth.ShaderSloth;
import de.yvka.shadersloth.controller.ShaderSlothController;
import de.yvka.shadersloth.controls.NumberInput;
import de.yvka.shadersloth.utils.AbstractController;
import de.yvka.slothengine.math.MathUtils;
import de.yvka.slothengine.scene.Geometry;
import de.yvka.slothengine.scene.Node;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModelEditorController extends AbstractController {


	@FXML private TextField nodeId;
	@FXML private NumberInput xPosition;
	@FXML private NumberInput yPosition;
	@FXML private NumberInput zPosition;

	@FXML private NumberInput xScale;
	@FXML private NumberInput yScale;
	@FXML private NumberInput zScale;

	@FXML private NumberInput xRotation;
	@FXML private NumberInput yRotation;
	@FXML private NumberInput zRotation;

	@FXML ChoiceBox<String> materialChooser;

	private Optional<Geometry> selectedNode = Optional.empty();
	private final ShaderSlothController slothController;

	public ModelEditorController(ShaderSlothController controller) {
		super(ShaderSloth.class.getResource("view/modelEditor.fxml"));
		this.slothController = controller;
	}

	@Override
	protected void onFxmlLoaded() {

		nodeId.textProperty().addListener((currentId, oldId, newId) -> {
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

		xScale.setMinValue(0.0);
		xScale.valueProperty().addListener((observable, oldValue, newValue) -> {
			selectedNode.ifPresent((node) -> node.getScale().x = newValue.floatValue());
		});

		yScale.setMinValue(0.0);
		yScale.valueProperty().addListener((observable, oldValue, newValue) -> {
			selectedNode.ifPresent((node) -> node.getScale().y = newValue.floatValue());
		});

		zScale.setMinValue(0.0);
		zScale.valueProperty().addListener((observable, oldValue, newValue) -> {
			selectedNode.ifPresent((node) -> node.getScale().z = newValue.floatValue());
		});

		xRotation.valueProperty().addListener((observable, oldValue, newValue) -> {
			selectedNode.ifPresent((node) -> node.getRotation().rotationX(MathUtils.toRadians(newValue.floatValue())));
		});

		yRotation.valueProperty().addListener((observable, oldValue, newValue) -> {
			selectedNode.ifPresent((node) -> node.getRotation().rotationY(MathUtils.toRadians(newValue.floatValue())));
		});

		zRotation.valueProperty().addListener((observable, oldValue, newValue) -> {
			selectedNode.ifPresent((node) -> node.getRotation().rotationZ(MathUtils.toRadians(newValue.floatValue())));
		});
	}

	public void updateData(Geometry node) {

		selectedNode = Optional.of(node);

		nodeId.setText(node.getId());
		Vector3f position = node.getPosition();
		Vector3f scale = node.getScale();
		Vector3f rotation = new Vector3f();
		node.getRotation().getEulerAnglesXYZ(rotation);

		xPosition.setValue(position.x);
		yPosition.setValue(position.y);
		zPosition.setValue(position.z);

		xRotation.setValue(MathUtils.toDegrees(rotation.x));
		yRotation.setValue(MathUtils.toDegrees(rotation.y));
		zRotation.setValue(MathUtils.toDegrees(rotation.z));

		xScale.setValue(scale.x);
		yScale.setValue(scale.y);
		zScale.setValue(scale.z);
	}
}

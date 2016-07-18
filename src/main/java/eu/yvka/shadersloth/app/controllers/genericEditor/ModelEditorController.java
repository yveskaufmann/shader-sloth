package eu.yvka.shadersloth.app.controllers.genericEditor;

import eu.yvka.shadersloth.app.App;
import eu.yvka.shadersloth.app.controllers.ShaderSlothController;
import eu.yvka.shadersloth.app.controls.NumberInput;
import eu.yvka.shadersloth.share.controller.AbstractController;
import eu.yvka.slothengine.engine.Engine;
import eu.yvka.slothengine.material.Material;
import eu.yvka.slothengine.math.MathUtils;
import eu.yvka.slothengine.scene.Geometry;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.joml.Vector3f;

import java.util.Optional;

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

	@FXML ChoiceBox<Material> materialChooser;

	private Optional<Geometry> selectedNode = Optional.empty();
	private final ShaderSlothController slothController;

	public ModelEditorController(ShaderSlothController controller) {
		super(App.class.getResource("view/modelEditor.fxml"));
		this.slothController = controller;
	}

	@Override
	protected void onFxmlLoaded() {

		nodeId.textProperty().addListener((currentId, oldId, newId) -> {
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

		initMaterialChooser();
	}

	private void initMaterialChooser() {
		materialChooser.setConverter(new StringConverter<Material>() {
			@Override
			public String toString(Material object) {
				return object.getMaterialName();
			}
			@Override
			public Material fromString(String materialName) {
				return Engine.materialManager().getMaterial(materialName).get();
			}
		});

		materialChooser.valueProperty().addListener((observable, oldMaterial, newMaterial) -> {
			if (newMaterial == null) return;
			selectedNode.ifPresent((node) -> node.setMaterial(newMaterial));
		});

		Engine.materialManager().getMaterial().addListener((MapChangeListener<String, Material>) change -> {
			SingleSelectionModel<Material> selectionModel = materialChooser.getSelectionModel();
			Material selection = selectionModel.getSelectedItem();
			materialChooser.setItems(
				FXCollections.observableArrayList(Engine.materialManager().getMaterial().values())
			);

			if (selection != null && materialChooser.getItems().contains(selection)) {
				selectionModel.select(selection);
			} else {
				selectionModel.selectFirst();
			}

        });

		materialChooser.setItems(
			FXCollections.observableArrayList(Engine.materialManager().getMaterial().values())
		);
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

		materialChooser.setValue(node.getMaterial());
	}
}

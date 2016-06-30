package eu.yvka.shadersloth.controllers.genericEditor;

import eu.yvka.shadersloth.ShaderSlothJavaFx;
import eu.yvka.shadersloth.controllers.ShaderSlothController;
import eu.yvka.shadersloth.utils.controller.AbstractController;
import eu.yvka.slothengine.scene.Geometry;
import eu.yvka.slothengine.scene.Node;
import eu.yvka.slothengine.scene.light.Light;
import javafx.scene.layout.StackPane;


public class GenericEditorController extends AbstractController {

	private ShaderSlothController slothController;
	private ModelEditorController modelEditorController;
	private LightEditorController lightEditorController;
	private AbstractController activeController;

	public GenericEditorController(ShaderSlothController controller) {
		super(ShaderSlothJavaFx.class.getResource("view/genericEditor.fxml"));
		this.slothController = controller;
		modelEditorController = new ModelEditorController(controller);
		lightEditorController = new LightEditorController(controller);
	}

	@Override
	protected void onFxmlLoaded() {}

	public void loadNode(Node node) {
		assert node != null;
		StackPane container = (StackPane) getRoot();
		if (node instanceof Geometry) {
			if (activeController != modelEditorController) {
				container.getChildren().clear();
				container.getChildren().add(modelEditorController.getRoot());
				activeController = modelEditorController;
			}
			modelEditorController.updateData((Geometry) node);
		}

		if (node instanceof Light) {
			if (activeController != lightEditorController) {
				container.getChildren().clear();
				container.getChildren().add(lightEditorController.getRoot());
				activeController = lightEditorController;
			}
			lightEditorController.updateData((Light) node);
		}
	}
}

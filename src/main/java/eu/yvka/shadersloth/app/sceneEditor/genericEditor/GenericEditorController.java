package eu.yvka.shadersloth.app.sceneEditor.genericEditor;

import eu.yvka.shadersloth.app.App;
import eu.yvka.shadersloth.app.ShaderSlothController;
import eu.yvka.shadersloth.share.controller.AbstractController;
import eu.yvka.slothengine.scene.Geometry;
import eu.yvka.slothengine.scene.Node;
import eu.yvka.slothengine.scene.light.Light;
import javafx.scene.layout.StackPane;


public class GenericEditorController extends AbstractController {

	private ShaderSlothController slothController;
	private GeometryEditorController geometryEditorController;
	private LightEditorController lightEditorController;
	private AbstractController activeController;
	private Node currentNode = null;

	public GenericEditorController(ShaderSlothController controller) {
		super(App.class.getResource("view/genericEditor.fxml"));
		this.slothController = controller;
		geometryEditorController = new GeometryEditorController(controller);
		lightEditorController = new LightEditorController(controller);
	}

	@Override
	protected void onFxmlLoaded() {}

	public void loadNode(Node node) {
		assert node != null;
		currentNode = node;
		StackPane container = (StackPane) getRoot();
		if (node instanceof Geometry) {
			if (activeController != geometryEditorController) {
				container.getChildren().clear();
				container.getChildren().add(geometryEditorController.getRoot());
				activeController = geometryEditorController;
			}
			geometryEditorController.updateData((Geometry) node);
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

	public void refresh() {
		if (currentNode != null) {
			loadNode(currentNode);
		}
	}
}

package eu.yvka.shadersloth.app.sceneEditor;

import eu.yvka.shadersloth.app.App;
import eu.yvka.shadersloth.app.ShaderSlothController;
import eu.yvka.shadersloth.app.project.Project;
import eu.yvka.shadersloth.share.controller.AbstractController;
import eu.yvka.slothengine.engine.Engine;
import eu.yvka.slothengine.scene.Geometry;
import eu.yvka.slothengine.scene.Node;
import eu.yvka.slothengine.scene.Scene;
import eu.yvka.slothengine.scene.light.Light;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class SceneTreeEditorController extends AbstractController {

	private static final Logger Log = LoggerFactory.getLogger(SceneTreeEditorController.class);

	/******************************************************************************
	 *
	 * FXML Fields
	 *
	 ******************************************************************************/

	@FXML private TreeView<Node> sceneTree;
	@FXML private MenuButton newEntryBtn;

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	private ObjectProperty<Node> selectedNode;
	private ShaderSlothController slothController;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public SceneTreeEditorController(ShaderSlothController slothController) {
		super(App.class.getResource("view/sceneTreeView.fxml"));
		this.slothController = slothController;
	}

	@Override
	protected void onFxmlLoaded() {
		initTree();
	}

	private void initTree() {
		sceneTree.setMinWidth(50);
		sceneTree.setPrefWidth(100);
		sceneTree.setShowRoot(true);
		sceneTree.setMaxWidth(Double.MAX_VALUE);
		sceneTree.setCellFactory(param -> new SceneTreeCell(this));
		sceneTree.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
			if (newValue != null) {

				final Node selectedValue = newValue.getValue();
				slothController.getGenericEditorController().loadNode(selectedValue);

				if (selectedValue instanceof Geometry) {
					Geometry geometry = (Geometry) selectedValue;
					// geometry.getMaterial().getRenderState().setWireframe(true);
				}

				if (oldValue != null && oldValue.getValue() instanceof Geometry) {
					Geometry geometry = (Geometry) oldValue.getValue();
					// geometry.getMaterial().getRenderState().setWireframe(false);
				}
			}
		}));
	}


	public Project getProject() {
		return slothController.getProject();
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	void onNewGeometry(ActionEvent event) {
		invokeNodeCreationDialog(GeometryCreateDialog::new);
		event.consume();
	}

	void onNewLight(ActionEvent event) {
		invokeNodeCreationDialog(LightCreateDialog::new);
		event.consume();
	}


	private void invokeNodeCreationDialog(Supplier<Dialog<? extends Node>> dialogFactory) {
		final Project project = getProject();
		final Scene scene = project.getScene();
		final Dialog<? extends  Node> dialog = dialogFactory.get();
		dialog.initOwner(sceneTree.getScene().getWindow());
		dialog.showAndWait().ifPresent((newNode -> {
			addNodeToTree(scene, newNode);
		}));
	}

	void onRemoveEntry(ActionEvent event) {
		final TreeItem<Node> selection = sceneTree.getSelectionModel().getSelectedItem();
		if (selection != null) {

			final TreeItem<Node> parentItem = selection.getParent();
			if (parentItem != null) {
				// Ensures that this operation is performed in the renderer thread

				final Node parent = parentItem.getValue();
				final Node nodeToRemove = selection.getValue();

				Engine.runWhenReady(() -> {;
					parent.removeChild(nodeToRemove);
				});
				parentItem.getChildren().remove(selection);

			}
		}
		event.consume();
	}

	/**
	 * Load the scene and create a representation in the project tree.
	 *
	 * @param scene the scene to load.
	 */
	public void loadScene(Scene scene) {
		TreeItem<Node> rootNode = new TreeItem<>(scene.getRootNode());
		loadScene(scene.getRootNode(), rootNode);
		sceneTree.setRoot(rootNode);
		sceneTree.getSelectionModel().selectFirst();
	}

	private void loadScene(Node rootNode, TreeItem<Node> rootItem) {
		for (Node node : rootNode.getChildren()) {
			TreeItem<Node> nodeItem = new TreeItem<>(node);
			rootItem.getChildren().add(nodeItem);
			if (! (node instanceof Light)) {
				loadScene(node, nodeItem);
			}
		}
	}



	private void addNodeToTree(Scene scene, Node node) {
		TreeItem<Node> root = sceneTree.getRoot();
		TreeItem<Node> newItem = new TreeItem<>(node);
		if (root == null) {
			root = new TreeItem<Node>(null);
			root.getChildren().add(newItem);
			sceneTree.setRoot(root);
			Engine.runWhenReady(() -> scene.setRootNode(node));

		} else {
			MultipleSelectionModel<TreeItem<Node>> selectedModel = sceneTree.getSelectionModel();
			TreeItem<Node> parentOfNewElement = selectedModel.getSelectedItem();
			if (parentOfNewElement == null) {
				parentOfNewElement = root;
			}
			parentOfNewElement.getChildren().add(newItem);
			Engine.runWhenReady(() -> scene.getRootNode().addChild(node));
		}

		if (node instanceof Light) {
			Engine.runWhenReady(() -> scene.getLightList().add((Light) node));
		}
	}

	public void notifyUpdate() {
		MultipleSelectionModel<TreeItem<Node>> selectionModel = sceneTree.getSelectionModel();
		if (selectionModel != null) {
			TreeItem<Node> item = selectionModel.getSelectedItem();
			Event.fireEvent(item, new TreeItem.TreeModificationEvent<>(TreeItem.valueChangedEvent(), item));
		}
	}

}

package eu.yvka.shadersloth.app.controllers;

import eu.yvka.shadersloth.app.App;
import eu.yvka.shadersloth.app.renderView.ShaderSlothRenderer;
import eu.yvka.shadersloth.app.controls.SceneTreeCell;
import eu.yvka.shadersloth.app.controls.dialog.GeometryCreateDialog;
import eu.yvka.shadersloth.share.controller.AbstractController;
import eu.yvka.slothengine.scene.Geometry;
import eu.yvka.slothengine.scene.Node;
import eu.yvka.slothengine.scene.Scene;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SceneTreeEditorController extends AbstractController {

	private static final Logger Log = LoggerFactory.getLogger(SceneTreeEditorController.class);

	@FXML private TreeView<Node> sceneTree;

	private ObjectProperty<Node> selectedNode;
	private ShaderSlothController slothController;

	public SceneTreeEditorController(ShaderSlothController slothController) {
		super(App.class.getResource("view/sceneTreeView.fxml"));
		this.slothController = slothController;
	}

	@Override
	protected void onFxmlLoaded() {
		initTree();
	}

	/**
	 * Load the scene and create a representation in the project tree.
	 *
	 * @param scene the scene to load.
	 */
	void loadScene(Scene scene) {

		class NodeModel {
			public TreeItem<Node> node;
			public TreeItem<Node> parent;

			public NodeModel(TreeItem<Node> node, TreeItem<Node> parent) {
				this.node = node;
				this.parent = parent;
			}
		}

		NodeModel rootNode = new NodeModel(new TreeItem<>(scene.getRootNode()), null);
		Queue<NodeModel> queue = new ConcurrentLinkedQueue<>();
		queue.add(rootNode);

		while (! queue.isEmpty()) {
			NodeModel node = queue.poll();

			// is rootNode
			if (node.parent != null) {
				node.parent.getChildren().add(node.node);
			}

			for (Node child : node.node.getValue().getChildren()) {
				queue.add(new NodeModel(new TreeItem<>(child), node.node));
			}
		}

		sceneTree.setRoot(rootNode.node);
		sceneTree.getSelectionModel().selectFirst();
	}

	private void initTree() {
		Menu newMenu = new Menu("New");
		MenuItem newModel = new MenuItem("Model");
		newMenu.setOnAction((event -> {
			GeometryCreateDialog dialog = new GeometryCreateDialog();
			dialog.initOwner(sceneTree.getScene().getWindow());
			dialog.showAndWait();

		}));
		newMenu.getItems().add(newModel);
		newMenu.getItems().add(new MenuItem("Light"));
		newMenu.getItems().add(new MenuItem("Geometry"));

		ContextMenu contextMenu = new ContextMenu();
		contextMenu.getItems().add(newMenu);

		sceneTree.setMinWidth(50);
		sceneTree.setPrefWidth(100);
		sceneTree.setMaxWidth(Double.MAX_VALUE);
		sceneTree.setContextMenu(contextMenu);
		sceneTree.setCellFactory(param -> new SceneTreeCell());
		sceneTree.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
			Log.debug("on selectedItemProperty: "  + MessageFormat.format("newValue = {0}, oldValue =  {1}", oldValue, newValue));
			Log.debug("on selectedItemProperty: "  + MessageFormat.format("newValue.getValue = {0}", newValue.getValue()));

			if (newValue != null) {
				slothController.getGenericEditorController().loadNode(newValue.getValue());
			}

			if (newValue.getValue() instanceof Geometry) {
				Geometry geometry = (Geometry) newValue.getValue();
				geometry.getMaterial().getRenderState().enableWireframe(true);
			}

			if (oldValue != null && oldValue.getValue() instanceof Geometry) {
				Geometry geometry = (Geometry) oldValue.getValue();
				geometry.getMaterial().getRenderState().enableWireframe(false);
			}
		}));
	}

	/**
	 * The currently selected node.
	 *
	 * @return the currently selected node null indicates
	 * that no node is selected.
	 */
	public ObjectProperty<Node> selectedNodeProperty() {
		if (selectedNode == null) {
			selectedNode = new ObjectPropertyBase<Node>() {
				@Override
				public java.lang.Object getBean() {
					return SceneTreeEditorController.this;
				}

				@Override
				public String getName() {
					return "selectedNode";
				}
			};
		}
		return selectedNode;
	}

	public Node getSelectedNode() {
		return selectedNodeProperty().get();
	}

	public void notifyUpdate() {
		MultipleSelectionModel<TreeItem<Node>> selectionModel = sceneTree.getSelectionModel();
		if (selectionModel != null) {
			TreeItem<Node> item = selectionModel.getSelectedItem();
			Event.fireEvent(item, new TreeItem.TreeModificationEvent<>(TreeItem.valueChangedEvent(), item));
		}
	}
}
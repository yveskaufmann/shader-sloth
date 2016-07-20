package eu.yvka.shadersloth.app.sceneEditor;

import eu.yvka.shadersloth.app.App;
import eu.yvka.shadersloth.app.controllers.ShaderSlothController;
import eu.yvka.shadersloth.app.project.Project;
import eu.yvka.shadersloth.share.controller.AbstractController;
import eu.yvka.slothengine.engine.Engine;
import eu.yvka.slothengine.scene.Geometry;
import eu.yvka.slothengine.scene.Node;
import eu.yvka.slothengine.scene.Scene;
import eu.yvka.slothengine.scene.light.Light;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
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
	@FXML private Button removeEntryBtn;
	@FXML private MenuItem newGeometryBtn;
	@FXML private MenuItem newLightBtn;

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
		newGeometryBtn.setOnAction(buildNodeCreationHandler(GeometryCreateDialog::new));
		newLightBtn.setOnAction(buildNodeCreationHandler(LightCreateDialog::new));
		removeEntryBtn.setOnAction(this::onRemoveEntry);
		initTree();
	}

	private void initTree() {
		sceneTree.setMinWidth(50);
		sceneTree.setPrefWidth(100);
		sceneTree.setMaxWidth(Double.MAX_VALUE);
		sceneTree.setContextMenu(createTreeContextMenu());
		sceneTree.setCellFactory(param -> new SceneTreeCell());
		sceneTree.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
			Log.debug("on selectedItemProperty: "  + MessageFormat.format("newValue = {0}, oldValue =  {1}", oldValue, newValue));
			Log.debug("on selectedItemProperty: "  + MessageFormat.format("newValue.getValue = {0}", newValue.getValue()));

			if (newValue != null) {
				slothController.getGenericEditorController().loadNode(newValue.getValue());
			}

			if (newValue.getValue() instanceof Geometry) {
				Geometry geometry = (Geometry) newValue.getValue();
				// geometry.getMaterial().getRenderState().setWireframe(true);
			}

			if (oldValue != null && oldValue.getValue() instanceof Geometry) {
				Geometry geometry = (Geometry) oldValue.getValue();
				// geometry.getMaterial().getRenderState().setWireframe(false);
			}
		}));
	}


	private ContextMenu createTreeContextMenu() {
		Menu newMenu = new Menu("New");
		MenuItem newModel = new MenuItem("Model");
		MenuItem light = new MenuItem("Light");
		MenuItem geometry = new MenuItem("Geometry");




		newMenu.getItems().setAll(newModel, light, geometry);

		ContextMenu contextMenu = new ContextMenu();
		contextMenu.getItems().add(newMenu);
		return contextMenu;
	}

	public Project getProject() {
		return slothController.getProject();
	}

	/******************************************************************************
	 *
	 * Event Handling
	 *
	 ******************************************************************************/

	private EventHandler<ActionEvent> buildNodeCreationHandler(Supplier<Dialog<? extends Node>> dialogFactory) {
		return (e) -> {
			final Project project = getProject();
			final Scene scene = project.getScene();
			final Dialog<? extends  Node> dialog = dialogFactory.get();
			dialog.initOwner(sceneTree.getScene().getWindow());
			dialog.showAndWait().ifPresent((geometry -> {
				addNodeToTree(scene, geometry);
			}));
		};
	}

	private void addNodeToTree(Scene scene, Node node) {
		TreeItem<Node> root = sceneTree.getRoot();
		TreeItem<Node> newItem = new TreeItem<>(node);
		if (root == null) {
            sceneTree.setRoot(newItem);
            scene.setRootNode(node);
        } else {
            sceneTree.getRoot().getChildren().add(newItem);
            scene.getRootNode().addChild(node);
        }

		if (node instanceof Light) {
			scene.getLightList().add((Light) node);
		}
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/



	private void onRemoveEntry(ActionEvent event) {
		final TreeItem<Node> selection = sceneTree.getSelectionModel().getSelectedItem();
		if (selection != null) {

			final TreeItem<Node> parent = selection.getParent();
			if (parent != null) {
				// Ensures that this operation is performed in the renderer thread
				Engine.runWhenReady(() -> {;
					Node parentNode = parent.getValue();
					parentNode.removeChild(selection.getValue());
				});
				parent.getChildren().remove(selection);

			}
		}
	}

	/**
	 * Load the scene and create a representation in the project tree.
	 *
	 * @param scene the scene to load.
	 */
	public void loadScene(Scene scene) {

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

	public void notifyUpdate() {
		MultipleSelectionModel<TreeItem<Node>> selectionModel = sceneTree.getSelectionModel();
		if (selectionModel != null) {
			TreeItem<Node> item = selectionModel.getSelectedItem();
			Event.fireEvent(item, new TreeItem.TreeModificationEvent<>(TreeItem.valueChangedEvent(), item));
		}
	}

	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/

	/**
	 * The currently selected node.
	 *
	 * @return the currently selected node null indicates
	 * that no node is selected.
	 */
	public ObjectProperty<Node> selectedNodeProperty() {
		if (selectedNode == null) {
			selectedNode = new SimpleObjectProperty<>(this, "selectedNode");
		}
		return selectedNode;
	}

	public Node getSelectedNode() {
		return selectedNodeProperty().get();
	}

	public void setSelectedNode(Node node) {
		selectedNodeProperty().set(node);
	}


}

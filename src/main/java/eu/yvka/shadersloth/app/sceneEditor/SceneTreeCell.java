package eu.yvka.shadersloth.app.sceneEditor;

import eu.yvka.shadersloth.app.App;
import eu.yvka.shadersloth.app.renderView.ShaderSlothRenderer;
import eu.yvka.slothengine.engine.Engine;
import eu.yvka.slothengine.scene.Geometry;
import eu.yvka.slothengine.scene.Node;
import eu.yvka.slothengine.scene.Scene;
import eu.yvka.slothengine.scene.light.Light;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.controlsfx.tools.Borders;

import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Predicate;

public class SceneTreeCell extends TreeCell<Node> {


	private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

	private Image bulbIcon = null;
	private Image sceneIcon = null;
	private Image meshIcon = null;
	private SceneTreeEditorController sceneTreeEditorController = null;

	public SceneTreeCell(SceneTreeEditorController sceneTreeEditorController) {
		this.sceneTreeEditorController = sceneTreeEditorController;
		initDragAndDrop();
		setContextMenu(createTreeContextMenu());
	}

	public void initDragAndDrop() {

		setOnDragDetected((e) -> {
			int index = SceneTreeCell.this.getIndex();
			if (index >= 0) {
				Dragboard dragboard = getTreeView().startDragAndDrop(TransferMode.MOVE);
				ClipboardContent cc = new ClipboardContent();
				// store the index of the item we want to move
				cc.put(SERIALIZED_MIME_TYPE, index);
				dragboard.setContent(cc);
				dragboard.setDragView(snapshot(null, null));
				e.consume();
			}
		});

		setOnDragOver((e) -> {
			Dragboard dragboard = e.getDragboard();
			int srcIndex = (int) dragboard.getContent(SERIALIZED_MIME_TYPE);
			int curIndex = getIndex();
			System.out.println(srcIndex);
			int dragBehind = isDragBehind(e, srcIndex, curIndex);

			// if we change the sibling order we can ignore index changes while
			// dragBehind is currently not complete implemented
			if (dragBehind != 0 && false) {
				if (dragBehind < 0) {
					setStyle("-fx-border-width: 1px 0 0 0; -fx-border-color: black; -fx-border-style: dashed;");
				} else {
					setStyle("-fx-border-width: 0 0 1px 0; -fx-border-color: black; -fx-border-style: dashed;");
				}
				e.acceptTransferModes(TransferMode.MOVE);
				e.consume();
			// Change parent / child relationship
			} else {
				setUnderline(true);
				setStyle("-fx-border-width: 1px; -fx-border-color: black; -fx-border-style: dashed;");
				if (isDropAcceptable(dragboard, getTreeView())) {
					e.acceptTransferModes(TransferMode.MOVE);
					e.consume();
				}
			}

		});

		setOnDragExited((e) -> {
			setUnderline(false);
			setEffect(null);
			setStyle(null);
			setBorder(null);
			e.consume();
		});

		setOnDragDropped((e) -> {
			final Dragboard dragboard = e.getDragboard();
			int index = (int) dragboard.getContent(SERIALIZED_MIME_TYPE);
			int dragBehind = isDragBehind(e, index, getIndex());

			TreeItem<Node> srcNodeItem = getTreeView().getTreeItem(index);
			TreeItem<Node> targetNodeItem = getTarget(getTreeView(), getTreeItem());

			// change only parent relationship
			if (dragBehind == 0) {
				// change view model only
				srcNodeItem.getParent().getChildren().remove(srcNodeItem);
				targetNodeItem.getChildren().add(srcNodeItem);

				Node srcNode = srcNodeItem.getValue();
				Node targetNode = targetNodeItem.getValue();

				// move the srcNode to its new parent once the engine isn't rendering anymore
				Engine.runWhenReady(() -> {
					srcNode.getParent().removeChild(srcNode);
					targetNode.addChild(srcNode);
				});
			}
			e.setDropCompleted(true);
			e.consume();
		});
	}


	private boolean isDropAcceptable(Dragboard dragboard, TreeView<Node> treeView) {
		boolean isAcceptable = false;
		if (dragboard.hasContent(SERIALIZED_MIME_TYPE)) {
			int srcIndex = (int) dragboard.getContent(SERIALIZED_MIME_TYPE);
			int curIndex = getIndex();

			// srcElement and target element are not the same element
			if (srcIndex != curIndex) {
				TreeItem<Node> srcItem = treeView.getTreeItem(srcIndex);
				TreeItem<Node> curItem = treeView.getTreeItem(curIndex);
				TreeItem<Node> tgtItem = getTarget(treeView, curItem);

				// prevent loops in the scene graph
				isAcceptable = !isParent(srcItem, tgtItem) &&
					srcItem.getParent() != tgtItem &&
					// ... and that lights have no children
					!(tgtItem.getValue() instanceof Light);

			}

		}
		return isAcceptable;
	}

	private TreeItem<Node> getTarget(TreeView<Node> treeView, TreeItem<Node> curItem) {
		TreeItem<Node> tgtItem = treeView.getRoot();
		if (! isEmpty()) {
            tgtItem = curItem;
        }
		return tgtItem;
	}

	private boolean isParent(TreeItem parent, TreeItem child) {
		boolean isParentNode = false;
		while (!isParentNode && child != null) {
			isParentNode = child.getParent() == parent;
			child = child.getParent();
		}
		return isParentNode;
	}


	private int isDragBehind(DragEvent e, int srcIndex, int curIndex) {
		final Point2D sceneCoordinates = localToScene(0d, 0d);
		double centerY = getHeight() * 0.5f;
		double yOffset = e.getSceneY() - (sceneCoordinates.getY()) - centerY;

		// when the dragged node cursor is near the edge
		// then we want to enable sibling order changes
		boolean changeSiblingOrder = Math.abs(yOffset) > centerY * 0.70 && Math.abs(srcIndex - curIndex) > 1;
		if (changeSiblingOrder) {
			return yOffset < 0 ? -1 : 1;
		}
		return 0;
	}

	@Override
	protected void updateItem(Node item, boolean empty) {
		super.updateItem(item, empty);

		if(item == null || empty) {
			setText(null);
			setGraphic(null);
		}

		if (item != null && !empty) {
			setText(item.getId());
			setGraphic(chooseGraphicByNode(item));
		}

	}

	private javafx.scene.Node chooseGraphicByNode(Node item) {
		ImageView imageView = null;

		if (isRootNode(item)) {
			if (sceneIcon == null) {
				sceneIcon = new Image(App.class.getResource("images/project_icon_16x16.png").toExternalForm());
			}
			imageView = new ImageView(sceneIcon);
		}

		if (Geometry.class.isInstance(item)) {
			if (meshIcon == null) {
				meshIcon = new Image(App.class.getResource("images/mesh_icon_16x16.png").toExternalForm());
			}
			imageView =  new ImageView(meshIcon);
		}

		if (Light.class.isInstance(item)) {
			if (bulbIcon == null) {
				bulbIcon = new Image(App.class.getResource("images/bulb_icon_16x16.png").toExternalForm());
			}
			imageView = new ImageView(bulbIcon);
		}

		imageView.setDisable(true);
		return imageView;

	}

	private boolean isRootNode(Node item) {
		return item.getParent() == null;
	}

	private ContextMenu createTreeContextMenu() {
		Menu newMenu = new Menu("New");
		MenuItem newGeometryMenu = new MenuItem("Geometry");
		MenuItem newLightMenu = new MenuItem("Light");
		MenuItem deleteMenuItem = new MenuItem("Delete");

		newGeometryMenu.setOnAction(sceneTreeEditorController::onNewGeometry);
		newLightMenu.setOnAction(sceneTreeEditorController::onNewLight);
		deleteMenuItem.setOnAction(sceneTreeEditorController::onRemoveEntry);


		ContextMenu contextMenu = new ContextMenu(newMenu, deleteMenuItem);

		Predicate<Node> isLight = (node) -> node != null && (node instanceof Light);
		contextMenu.setOnShown((e) -> {
			if (isLight.test(getItem())) {
				newMenu.getItems().clear();
				newMenu.setVisible(false);
			} else {
				newMenu.getItems().setAll(newLightMenu, newGeometryMenu);
				newMenu.setVisible(true);
			}
			TreeItem<Node> root = getTreeView().getRoot();
			TreeItem<Node> curItem = getTreeItem();

			boolean isRootElement = Objects.equals(root, curItem);
			deleteMenuItem.setVisible(! isRootElement && !isEmpty());
			e.consume();
		});

		return contextMenu;
	}


}

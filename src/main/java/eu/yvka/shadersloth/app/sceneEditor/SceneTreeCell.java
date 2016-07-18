package eu.yvka.shadersloth.app.sceneEditor;

import eu.yvka.shadersloth.app.App;
import eu.yvka.shadersloth.app.renderView.ShaderSlothRenderer;
import eu.yvka.slothengine.scene.Geometry;
import eu.yvka.slothengine.scene.Node;
import eu.yvka.slothengine.scene.Scene;
import eu.yvka.slothengine.scene.light.Light;
import javafx.geometry.Point2D;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeView;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.controlsfx.tools.Borders;

public class SceneTreeCell extends TreeCell<Node> {

	private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

	private Image bulbIcon = null;
	private Image sceneIcon = null;
	private Image meshIcon = null;

	public SceneTreeCell() {
		initDragAndDrop();
	}

	public void initDragAndDrop() {

		setOnDragDetected((e) -> {
			Dragboard dragboard = getTreeView().startDragAndDrop(TransferMode.MOVE);
			ClipboardContent cc = new ClipboardContent();
			cc.put(SERIALIZED_MIME_TYPE, getIndex());
			dragboard.setContent(cc);
			dragboard.setDragView(snapshot(null, null));
			e.consume();
		});

		setOnDragOver((e) -> {
			Dragboard dragboard = e.getDragboard();
			int srcIndex = (int) dragboard.getContent(SERIALIZED_MIME_TYPE);
			int curIndex = getIndex();
			Point2D sceneCoordinates = localToScene(0d, 0d);
			double midY = getHeight() * 0.5f;
			double y = e.getSceneY() - (sceneCoordinates.getY()) - midY;

			// when the dragged node cursor is near the edge
			// then we want to enable sibling order changes
			boolean changeSiblingOrder = Math.abs(y) > midY * 0.70;

			// if we change the sibling order we can ignore index changes whi
			if (changeSiblingOrder && Math.abs(srcIndex - curIndex) > 1 && !isEmpty()) {
				boolean dragBehind = y < 0;
				if (dragBehind) {
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
			if (dragboard.hasContent(SERIALIZED_MIME_TYPE)) {
				int index = (int) dragboard.getContent(SERIALIZED_MIME_TYPE);
				TreeItem<Node> srcNode = getTreeView().getTreeItem(index);
				TreeItem<Node> targetNode = getTarget(getTreeItem(), getTreeView());

				srcNode.getParent().getChildren().remove(srcNode);
				targetNode.getChildren().add(srcNode);
			}

			e.setDropCompleted(true);
			e.consume();
		});
	}


	private boolean isDropAcceptable(Dragboard dragboard, TreeView<Node> treeView) {
		boolean isAcceptable = false;
		if (dragboard.hasContent(SERIALIZED_MIME_TYPE)) {
			int srcIndex = (int) dragboard.getContent(SERIALIZED_MIME_TYPE);
			if (srcIndex != getIndex()) {
				TreeItem<Node> srcItem = treeView.getTreeItem(srcIndex);
				TreeItem<Node> target = getTarget(this.getTreeItem(), treeView);
				isAcceptable = !isParent(srcItem, target) && srcItem.getParent() != target ;
			}
		}
		return isAcceptable;
	}


	private TreeItem<Node> getTarget(TreeItem<Node> row, TreeView<Node> treeTableView) {
		TreeItem target = treeTableView.getRoot();
		if (!row.getChildren().isEmpty()) {
			target = row;
		}
		return target;
	}

	private boolean isParent(TreeItem parent, TreeItem child) {
		boolean result = false;
		while (!result && child != null) {
			result = child.getParent() == parent;
			child = child.getParent();
		}
		return result;
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
		if (isRootNode(item)) {
			if (sceneIcon == null) {
				sceneIcon = new Image(App.class.getResource("images/project_icon_16x16.png").toExternalForm());
			}
			return new ImageView(sceneIcon);
		}

		if (Geometry.class.isInstance(item)) {
			if (meshIcon == null) {
				meshIcon = new Image(App.class.getResource("images/mesh_icon_16x16.png").toExternalForm());
			}
			return new ImageView(meshIcon);
		}

		if (Light.class.isInstance(item)) {
			if (bulbIcon == null) {
				bulbIcon = new Image(App.class.getResource("images/bulb_icon_16x16.png").toExternalForm());
			}
			return new ImageView(bulbIcon);
		}

		return null;

	}

	private boolean isRootNode(Node item) {
		return item.getParent() == null;
	}


}

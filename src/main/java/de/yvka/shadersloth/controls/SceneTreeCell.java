package de.yvka.shadersloth.controls;

import de.yvka.slothengine.scene.Geometry;
import de.yvka.slothengine.scene.Node;
import de.yvka.slothengine.scene.light.Light;
import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import de.yvka.shadersloth.ShaderSloth;

public class SceneTreeCell extends TreeCell<Node> {

	private Image bulbIcon = null;
	private Image sceneIcon = null;
	private Image meshIcon = null;

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
			super.updateTreeItem(getTreeItem());
		}
	}

	private javafx.scene.Node chooseGraphicByNode(Node item) {
		if (isRootNode(item)) {
			if (sceneIcon == null) {
				sceneIcon = new Image(ShaderSloth.class.getResource("images/project_icon_16x16.png").toExternalForm());
			}
			return new ImageView(sceneIcon);
		}

		if (Geometry.class.isInstance(item)) {
			if (meshIcon == null) {
				meshIcon = new Image(ShaderSloth.class.getResource("images/mesh_icon_16x16.png").toExternalForm());
			}
			return new ImageView(meshIcon);
		}

		if (Light.class.isInstance(item)) {
			if (bulbIcon == null) {
				bulbIcon = new Image(ShaderSloth.class.getResource("images/bulb_icon_16x16.png").toExternalForm());
			}
			return new ImageView(bulbIcon);
		}

		return null;

	}

	private boolean isRootNode(Node item) {
		return item.getParent() == null;
	}
}

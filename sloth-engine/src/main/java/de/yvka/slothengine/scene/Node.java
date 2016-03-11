package de.yvka.slothengine.scene;

import de.yvka.slothengine.scene.light.Light;
import de.yvka.slothengine.scene.light.Light;

import java.util.ArrayList;
import java.util.List;

public class Node extends Spatial {

	private List<Node> children = new ArrayList<>();

	public Node(String id) {
		super(id);
	}

	public Node getChild(String id) {
		for (Node child : children) {
			if (id.equals(child.getId())) {
				return child;
			}

			Node desiredChild = child.getChild(id);
			if (desiredChild != null) {
				return desiredChild;
			}
		}
		return null;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void addChild(Node node) {
		if (node == null) {
			throw new IllegalArgumentException("Child must be not null");
		}

		if (node == this) {
			throw new IllegalArgumentException("A node cannot become it's  own child");
		}

		if (node.scene != null ) {
			throw new IllegalArgumentException("This node is already attached to a different scene.");
		}

		if (node.hasParent()) {
			node.getParent().removeChild(node);
		}

		node.setParent(this);
		node.scene = scene;
		children.add(node);

		if (node instanceof Light) {
			scene.getLightList().add((Light) node);
		}

	}

	public int removeChild(Node child) {
		if (child.getParent() == this) {
			int childIndex = children.indexOf(child);
			if (childIndex != -1) {

				if (child instanceof Light) {
					scene.getLightList().remove((Light) child);
				}

				child.setParent(null);
				child.scene = null;
				children.remove(childIndex);
				return childIndex;
			}
		}
		return -1;
	}

}

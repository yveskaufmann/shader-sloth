package de.yvka.slothengine.scene.traversal;

import de.yvka.slothengine.scene.Node;

public class DepthFirstTraverse implements GraphTraversal {

	@Override
	public void traverse(Node node, Visitor<Node> visitor, int depth) {
		if (node == null || depth == 0) {
			return;
		}

		for (Node child : node.getChildren()) {
			traverse(child, visitor, depth - 1);
		}
		visitor.visit(node);
	}
}

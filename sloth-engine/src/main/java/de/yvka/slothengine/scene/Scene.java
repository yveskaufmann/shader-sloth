package de.yvka.slothengine.scene;

import de.yvka.slothengine.scene.light.LightList;
import de.yvka.slothengine.scene.camera.Camera;
import de.yvka.slothengine.scene.camera.FreeCamera;
import de.yvka.slothengine.scene.traversal.DepthFirstTraverse;
import de.yvka.slothengine.scene.traversal.GraphTraversal;
import de.yvka.slothengine.scene.traversal.Visitor;
import de.yvka.slothengine.scene.traversal.Visitor;

public class Scene {
	/**
	 * Root Node name.
	 */
	public static final String ROOT_NODE = "Root Node";

	/**
	 * Scene Traverse strategy
	 */
	private GraphTraversal depthFirstTraversal = new DepthFirstTraverse();

	/**
	 * Root Node of this scene
	 */
	private Node rootNode;

	/**
	 * Indicates if this node is active and
	 * should be rendered.
	 */
	private boolean active = true;

	/**
	 * Current Camera of this scene
	 */
	private Camera camera;

	/**
	 * List of lights which are used by this scene
	 */
	private LightList lights;

	/**
	 * Creates a empty active scene.
	 */
	public Scene() {
		camera = new FreeCamera();
		lights = new LightList();
		rootNode = new Node(ROOT_NODE);
		rootNode.scene = this;
	}

	/**
	 * @return the Root node of this scene
     */
	public Node getRootNode() {
		return rootNode;
	}

	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
		this.rootNode.scene = this;
	}

	/**
	 * Determines if this scene is active.
	 *
	 * @return true if the scene is active.
     */
	public boolean isActive() {
		return active;
	}

	/**
	 * Set this Scene active (true) or non active (false).
	 *
	 * @param active true activate this scene and false activate it.
     */
	public void setActive(boolean active) {
		this.active = active;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}


	public LightList getLightList() {
		return this.lights;
	}

	public void traverse(Visitor<Node> visitor) {
		depthFirstTraversal.traverse(rootNode, visitor);
	}

	public void update(float elapsedTime) {
		// TODO: update node
		camera.update(elapsedTime);
	}

	/**
	 * Add nodes to this scene
	 *
	 *  @param node the node to add
     */
	public void add(Node node) {
		rootNode.addChild(node);
	}


	/**
	 * Removes a node from this scene.
	 *
	 *  @param node the node to remove
	 */
	public void remove(Node node) {
		getRootNode().removeChild(node);
	}


}

package de.yvka.slothengine.scene;

import de.yvka.slothengine.math.Transformation;
import de.yvka.slothengine.math.Transformation;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Base class for a spatial hierarchy which is
 * the base class for a simple scene graph.
 *
 *
 * Inspired by in Chapter 4 of David H. Eberly's
 * "3D Game Engine Design A Practical Approach to Real-Time Computer Graphics - Second Edition"
 */
public class Spatial {

	/**
	 * Stores the references to all spatial in order to
	 * support a fast random access to spatial instances.
	 */
	protected static final Map<String, Spatial> instances = new HashMap<>();

	/**
	 * The identifiers which is reserved for spatial which are invalid
	 * or not attached to a scene.
	 */
	protected static final String UNSET_ID = "UNSET_ID";

	/**
	 * The scene the node is attached to
	 */
	protected Scene scene;

	/**
	 * The id of this spatial
	 */
	protected String id = UNSET_ID;

	/**
	 * The optional parent reference.
	 */
	protected Node parent = null;

	/**
	 * Is the local transformation that places the
	 * object in in the coordinate system of its parent.
	 */
	protected final Transformation localTransformation = new Transformation();

	/**
	 * The world transformation of this object which maintains the
	 * the combination of the parent word transformation and this local transformation.
	 */
	protected final Transformation wordTransformation = new Transformation();

	/**
	 * Creates the Spatial which the specified id.
	 *
	 * @param id the specified id
     */
	public Spatial(String id) {

		if (UNSET_ID.equals(id)) {
			throw new IllegalArgumentException("The UNSET_ID is used reserved for internal use only");
		}

		if (! instances.containsKey(id)) {
			instances.put(id, this);
			this.id = id;
		} else {
			throw new IllegalArgumentException("The specified id " + id + "is already used");
		}

		localTransformation.resetTransform();
		wordTransformation.resetTransform();
	}

	/**
	 * Retrieves the id of this spatial.
	 *
	 * @return the id.
     */
	public String getId() {
		return id;
	}

	/**
	 * Retrieves the parent of this spatial if
	 * it is return null then this spatial is
	 * the root of this hierarchy.
	 *
	 * @return the parent spatial or null if this spatial is the root.
     */
	public Node getParent() {
		return parent;
	}

	/**
	 * Determines if this spatial has a parent.
	 *
	 * @return true if this spatial is a child
     */
	public boolean hasParent() {
		return parent != null;
	}

	/**
	 * Defines the parent Node of this spatial.
	 *
	 * The parent must not be set able by a client,
	 * the parent must be set by sub classes of Spatial.
	 *
	 * @param parent
     */
	protected void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * Free this spatial instance from the instances class field.
	 * This method must be called if a spatial instance is not used any more,
	 * in order to prevent memory leaks.
	 */
	void free() {
		instances.remove(id);
		id = UNSET_ID;
	}

	public Matrix4f getTransformMatrix() {
		updateTransform();
		return wordTransformation.getTransformMatrix();
	}

	protected void combineWithParent() {
		if (true) {
			if (parent == null) {
				wordTransformation.set(localTransformation);
			} else {
				wordTransformation.set(localTransformation);
				wordTransformation.combine(parent.wordTransformation);
			}
		}
	}

	public void updateTransform() {
		Stack<Spatial> nodeStack = new Stack<>();
		if (parent == null) {
			wordTransformation.set(localTransformation);
		} else {
			Spatial root = this;
			while (true) {
				Spatial localParent = root.parent;
				if (localParent == null) {
					wordTransformation.set(localTransformation);
					break;
				}
				nodeStack.push(root);
				root = localParent;
			}
		}

		for(Spatial node : nodeStack) {
			node.combineWithParent();
		}
	}

	/**
	 * Retrieves a spatial by it's id name.
	 *
	 * @param id the id of the desired spatial.
	 *
	 * @return the spatial with the specified id or null no such spatial exists.
     */
	Spatial getSpatitialById(String id) {
		return instances.getOrDefault(id, null);
	}


	// TODO: check if transformations must be updated
	//		 then

	public Quaternionf getRotation() {
		return localTransformation.getRotation();
	}

	public void setRotation(Quaternionf rotation) {
		localTransformation.setRotation(rotation);
	}


	public Vector3f getScale() {
		return localTransformation.getScale();
	}

	public void setScale(float scale) {
		localTransformation.setScale(scale);
	}

	public void setScale(Vector3f scaleVector) {
		localTransformation.setScale(scaleVector);
	}

	public Vector3f getPosition() {
		return localTransformation.getPosition();
	}

	public Vector3f getWorldPosition() {
		updateTransform();
		return wordTransformation.getPosition();
	}

	public void setPosition(Vector3f translate) {
		localTransformation.setPosition(translate);
	}

	public void setPosition(float x, float y, float z) {
		localTransformation.setPosition(x, y, z);
	}

	public void resetTransform() {
		localTransformation.resetTransform();
	}

	/**
	 * Retrieves the scene to which
	 * this spatial is attached.
	 *
	 * @return the scene.
     */
	public Scene getScene() {
		if (scene == null) {
			Spatial parent = getParent();
			if (parent != null) {
				scene = parent.getScene();
			}
		}
		return scene;
	}
}

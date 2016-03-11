package de.yvka.slothengine.math;

import org.joml.*;

public class Transformation {

	ChangedAwareVector3f scale = new ChangedAwareVector3f(1f);
	ChangedAwareVector3f position = new ChangedAwareVector3f(0f);
	ChangedAwareQuaternionf rotation = new ChangedAwareQuaternionf();
	Matrix4f transformMatrix = new Matrix4f();

	public Transformation() {
		resetTransform();
	}

	public Vector3f getScale() {
		return scale;
	}

	public Quaternionf getRotation() {
		return rotation;
	}

	public Vector3f getPosition() {
		return position;
	}


	public void setScale(float scale) {
		this.scale.set(scale);
	}

	public void setScale(Vector3f scaleVector) {
		scale.set(scaleVector);
	}

	public void setScale(float x, float y, float z) {
		this.scale.set(x, y, z);
	}

	public void setRotation(Quaternionf rotation) {
		this.rotation.set(rotation);
	}

	public void setPosition(Vector3f translate) {
		this.position.set(translate);
	}

	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}

	public Matrix4f getTransformMatrix() {
		calculateTransformMatrix();
		return transformMatrix;
	}

	public void resetTransform() {
		scale.set(1.0f);
		position.zero();
		rotation.identity();
	}


	private void calculateTransformMatrix() {
		if (position.isChanged() || scale.isChanged() || rotation.isChanged()) {
			transformMatrix.identity();
			transformMatrix.translationRotateScale(position, rotation, scale);
		}
	}

	public void set(Transformation transform) {
		scale.set(transform.scale);
		position.set(transform.position);
		rotation.set(transform.rotation);
	}

	public void combine(Transformation transformation) {
		// scale.mul(transformation.scale);
		transformation.rotation.mul(rotation, rotation);
		// position.mul(transformation.scale);
		position.rotate(rotation);
		transformation.position.add(position, position);
		calculateTransformMatrix();
	}




	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// W R A P P E R - C L A S S E S
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	/**
	 * A simple Vector wrapper which capable to check if it was changed
	 * since the last frame.
	 */
	private final class ChangedAwareVector3f extends Vector3f {

		private static final long serialVersionUID = 1L;

		private float oldX;
		private float oldY;
		private float oldZ;

		public ChangedAwareVector3f() {
		}

		public ChangedAwareVector3f(float d) {
			super(d);
			oldX = oldY = oldZ = d;
		}

		/**
		 * Check if the vector was changed in the last frame
		 * and reset the last frame position to the new position.
		 *
		 * Note: a change will be only notified once by the first call of this method.
		 *
		 * @return true if the vector was changed in the last frame.
         */
		public boolean isChanged() {
			boolean isChanged = oldX != x || oldY != y || oldZ != z;
			if (isChanged) {
				oldX = x;
				oldY = y;
				oldZ = z;
			}
			return isChanged;
		}
	}

	/**
	 * A simple Quaternionf wrapper which capable to check if it was changed
	 * since the last frame.
	 */
	private final class ChangedAwareQuaternionf extends Quaternionf {

		private static final long serialVersionUID = 1L;

		private float oldX;
		private float oldY;
		private float oldZ;
		private float oldW;

		public ChangedAwareQuaternionf() {
			super();
			oldW = 1.0f;
		}

		/**
		 * Check if the Quaternionf was changed in the last frame
		 * and reset the last frame quaternionf to the new rotation.
		 *
		 * Note: a change will be only notified once by the first call of this method.
		 *
		 * @return true if the vector was changed in the last frame.
		 */
		public boolean isChanged() {
			boolean isChanged = oldX != x || oldY != y || oldZ != z || oldW != w;
			if (isChanged) {
				oldX = x;
				oldY = y;
				oldZ = z;
				oldW = w;
			}
			return isChanged;
		}
	}
}

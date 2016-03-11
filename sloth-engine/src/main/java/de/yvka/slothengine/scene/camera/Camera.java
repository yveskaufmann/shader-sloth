package de.yvka.slothengine.scene.camera;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class Camera {

	private final Quaternionf orientation;
	protected final Vector3f position;
	protected final Vector3f up;
	protected final Vector3f direction;
	protected final Vector3f directionUp;
	protected final Vector3f right;

	protected Matrix4f viewMatrix;
	protected Matrix4f projectionMatrix;

	protected float fov;
	protected float aspect;
	protected float near;
	protected float far;

	protected float yaw;
	protected float pitch;
	protected float roll;

	public Camera() {

		this.orientation = new Quaternionf();
		this.position = new Vector3f();
		this.direction = new Vector3f();
		this.directionUp = new Vector3f();
		this.right = new Vector3f();
		this.up = new Vector3f(0.0f, 1.0f, 0.0f);
		this.viewMatrix = new Matrix4f();
		this.projectionMatrix = new Matrix4f();
		this.yaw = this.pitch = this.roll = 0f;
		setupProjection(45, 4f / 3f, 0.1f, 1000.f);
	}

	public abstract void update(float time);


	public void setupProjection(float fov, float aspect, float near, float far) {
		if (this.fov != fov || this.aspect != aspect || this.near != near || this.far != far) {
			projectionMatrix.setPerspective(fov, aspect, near, far);
			this.fov = fov;
			this.aspect = aspect;
			this.near = near;
			this.far = far;
		}
	}

	public void setRotation(float yaw, float pitch, float roll) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public  Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position.set(position);
	}

	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}

	public float getAspectRatio() {
		return aspect;
	}

	public float getFieldOfView() {
		return fov;
	}

	public Vector3f getDirection() {
		return this.direction;
	}

}

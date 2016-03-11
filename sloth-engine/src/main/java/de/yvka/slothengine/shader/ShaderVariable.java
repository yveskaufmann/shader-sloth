package de.yvka.slothengine.shader;

/**
 * Abstraction for a shader variable like an uniform or a attribute.
 */
public abstract class ShaderVariable {

	/**
	 * This prefix is set in front of shader variables
	 * if they are provided from the internal engine.
	 */
	public static final String VAR_PREFIX = "sl_";

	/**
	 * Marks a location as not found by the glsl compiler.
	 */
	public static final int LOCATION_NOT_FOUND = -1;


	/**
	 * Marks a location as not known this is the initial
	 * state of a shader variable. In order to force the
	 * <code>Renderer</code> to request the location
	 * of this shader variable.
	 */
	public static final int LOCATION_UNKNOWN = -2;

	private int location = LOCATION_UNKNOWN;
	private boolean updateRequired = true;
	private String name;

	protected ShaderVariable() {
		reset();
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isUpdateRequired() {
		return this.updateRequired;
	}

	public void disableUpdateRequired() {
		this.updateRequired = false;
	}

	public void enableUpdateRequired() {
		this.updateRequired = true;
	}

	/**
	 * Ressets this shader variable to it's initial
	 * state which contains the location
	 * is set to LOCATION_UNKNOWN and the variable
	 * is marked as onUpdate required.
	 */
	public void reset() {
		location = LOCATION_UNKNOWN;
		this.updateRequired = true;
	}

	@Override
	public String toString() {
		return name;
	}

	public enum VariableType {
		Float,
		Float2,
		Float3,
		Float4,

		Int,
		Int2,
		Int3,
		Int4,

		Matrix3x3,
		Matrix4x4,
	}
}

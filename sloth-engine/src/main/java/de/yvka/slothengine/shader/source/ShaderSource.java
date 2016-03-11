package de.yvka.slothengine.shader.source;


import de.yvka.slothengine.shader.ShaderType;
import de.yvka.slothengine.utils.HardwareObject;

public abstract class ShaderSource extends HardwareObject {

	private ShaderType type;

	protected ShaderSource(ShaderType type) {
		super(ShaderSource.class);
		this.type = type;
	}

	/**
	 * Retrieves the shader source from the
	 * underlying data source.
	 *
	 * @return the source of the shader.
     */
	public abstract String getSource();

	/**
	 * Returns the time that the shader source was last modified.
	 *
	 * @return A long value representing the time when the shader source was last modified.
     */
	public abstract long lastModified();

	/**
	 * Receives the type of this shader.
	 *
	 * @return the type of this shader.
     */
	public ShaderType getType() {
		return type;
	}

	/**
	 * Receives the name of this shader source in order
	 * to identify a shader source by a name.
	 *
	 * @return the name of this shader source.
     */
	public abstract String getName();

	@Override
	public String toString() {
		return String.format("Shader[type: %s, name: %s]", type.toString(), getName());
	}
}

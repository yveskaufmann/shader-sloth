package de.yvka.slothengine.material;


import de.yvka.slothengine.texture.Texture;

public class TextureBinding {
	/**
	 * The shader sampler variable which should receive the texture
	 */
	private String samplerName;

	/**
	 * The texture unit to which the texture is bound.
	 */
	private int uint;

	/**
	 * The texture which should bound to samplerName.
	 */
	private Texture texture;

	/**
	 * Binds the texture to sampler and a texture unit.
	 */
	public TextureBinding(String samplerName, int unit, Texture texture) {
		this.samplerName = samplerName;
		this.uint = unit;
		this.texture = texture;
	}

	/**
	 * Returns the sampler name to which this texture should be bound to.
	 *
	 * @return the sampler name of this binding,
     */
	public String getSamplerName() {
		return samplerName;
	}

	/**
	 * Return the texture unit to which the texture should be bound.
	 *
	 * @return the texture unit
     */
	public int getUint() {
		return uint;
	}

	/**
	 * Return the the texture which should be bound
	 *
	 * @return the texture
     */
	public Texture getTexture() {
		return texture;
	}
}

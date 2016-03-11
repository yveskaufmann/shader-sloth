package de.yvka.slothengine.texture;

import de.yvka.slothengine.renderer.Renderer;
import de.yvka.slothengine.texture.image.Image;
import de.yvka.slothengine.utils.HardwareObject;
import de.yvka.slothengine.renderer.Renderer;
import de.yvka.slothengine.texture.image.Image;
import de.yvka.slothengine.utils.HardwareObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Texture Base Class which acts as entity for a texture.
 * All the related texture operation are perfomed by the renderer.
 */
public class Texture extends HardwareObject {
	/**
	 * Possible dimensions of a Texture.
	 */
	public enum Dimension {
		DIMENSION_1D,
		DIMENSION_2D,
		DIMENSION_3D,
	}

	/**
	 * Possible MinFilters which should handle
	 * situations when the texture should be minimized.
	 */
	public enum MinFilter {
		NearestNeighbour(false),
		Bilinear(false),
		Trilinear(true),
		LINEAR_MIPMAP_NEAREST(true),
		NEAREST_MIPMAP_LINEAR(true),
		NEAREST_MIPMAP_NEAREST(true);

		private boolean requiresMipMaps;

		MinFilter(boolean mipMaps) {
			requiresMipMaps = mipMaps;
		}

		/**
		 * Indicates whether this Filter requires MipMaps.
		 * @return true if mip maps are required.
         */
		public boolean requiresMipMaps() {
			return requiresMipMaps;
		}

	}

	/**
	 * Possible MaxFilters which should handle
	 * situations when the texture should be magnify.
	 */
	public enum MagFilter {
		NearestNeighbour,
		Bilinear,
	}

	/**
	 * Possible WrapModes which should handle situations when
	 * the texture is smaller than the specified texture coordinates.
	 */
	public enum WrapMode {
		Repeast,
		MirroredRepeat,
		ClampEDGE,
		ClampBORDER,
	}

	/**
	 * Texture Coordinate Axis are
	 * required for set and obtain the WrapMode specific
	 * for each axis.
	 */
	public enum TextureAxis {
		S, T, R
	}

	/**
	 * The width of the texture in texel units
	 */
	private int width;

	/**
	 * The height of the texture in texel units
	 */
	private int height;

	/**
	 * The depth of the texture in texel units
	 */
	private int depth;

	/**
	 * The dimension/ the type of this texture.
	 */
	private Dimension dimension = Dimension.DIMENSION_2D;

	/**
	 * The MinFilter for this Texture
	 */
	private MinFilter minFilter = MinFilter.NearestNeighbour;

	/**
	 * MagFilter for this Texture
	 */
	private MagFilter magFilter = MagFilter.NearestNeighbour;

	/**
	 * The WrapModes stored for each specific axis
	 */
	private final Map<TextureAxis, WrapMode> wrapModes = new HashMap<>();

	/**
	 * The amount of times the anisotropic filter should be applied to this texture.
	 */
	private float anisotropicFilteringLevel = 1.0f;

	/**
	 * The image object which acts as source for the texture data.
	 * May be null if this texture, don't provide a image for uploading
	 * to the gpu.
	 */
	private Image image;

	/**
	 * The name of this texture, acts as identification for
	 * this texture.
	 */
	private String name;

	// private ImageFormat targetFormat;

	/**
	 * The Creation of a Texture should done by the TextureManager
	 * not by this Constructor.
	 */
	Texture() {
		super(Texture.class);
	}

	/**
	 * Set the name of this texture.
	 *
	 * @param name
     */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the image which should uploaded to this texture.
	 *
	 * @param image
     */
	void setImage(Image image) {
		this.image = image;
	}

	/**
	 * The width of this texture
	 * @param width
     */
	void setWidth(int width) {
		this.width = width;
	}

	/**
	 * The height of this texture
	 *
	 * @param height
     */
	void setHeight(int height) {
		this.height = height;
	}

	/**
	 * The depth of this texture is important
	 * for 3d Textures.
	 *
	 * @param depth
     */
	void setDepth(int depth) {
		this.depth = depth;
	}

	/**
	 * Set the type or better known the dimension of this Texture.
	 * @param dimension
     */
	void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	/**
	 * Get the WrapMode of a specific texture coordinate axis.
	 *
	 * @param axis the axis for which the mode is requested
	 * @return the wrapMode.
     */
	public WrapMode getWrapMode(TextureAxis axis) {
		WrapMode mode = wrapModes.getOrDefault(axis, WrapMode.Repeast);
		return mode;
	}

	/**
	 * Set the WrapMode for a specific texture coordinate axis.
	 *
	 * @param axis the axis for which the wrap mode should be set.
	 * @param wrapMode the new wrap mode.
     */
	public void setWrapMode(TextureAxis axis, WrapMode wrapMode) {
		wrapModes.put(axis, wrapMode);
		enableUpdateRequired();
	}

	/**
	 * Retrieves the type of this texture.
	 *
	 * @return the type.
     */
	public Dimension getType() {
		return dimension;
	}

	/**
	 * Return the min filter of this Texture.
	 *
	 * @return the MinFilter.
     */
	public MinFilter getMinFilter() {
		return minFilter;
	}

	/**
	 * Set the min filter of this texture.
	 *
	 * @param minFilter the desired min filter.
     */
	public void setMinFilter(MinFilter minFilter) {
		this.minFilter = minFilter;
		enableUpdateRequired();
	}

	/**
	 * Retrieves the mag filter of this texture.
	 *
	 * @return the mag filter of this texture.
     */
	public MagFilter getMagFilter() {
		return magFilter;
	}


	/**
	 * Set the magnification filter of this texture.
	 *
	 * @param magFilter the magnification filter.
     */
	public void setMagFilter(MagFilter magFilter) {
		this.magFilter = magFilter;
	}

	/**
	 * Returns the amount how often this texture should be
	 * filtered by the anisotropic filter. This amount
	 * will here be described as level.
	 *
	 * @return the level of the anisotropic filter.
     */
	public float getAnisotropicLevel() {
		return anisotropicFilteringLevel;
	}

	/**
	 * Set the amount how often this texture should be
	 * filtered by the anisotropic filter. This amount
	 * will here be described as level.
	 *
	 * @param level the level of the anisotropic filter.
     */
	public void setAnisotropicLevel(float level) {
		level = Math.min(0, level);
		this.anisotropicFilteringLevel = level;
		enableUpdateRequired();
	}

	/**
	 * The image data for this texture.
	 *
	 * @return the image object which contains the data for this texture.
     */
	public Image getImage() {
		return image;
	}

	/**
	 * Retrieves the width of this texture.
	 *
	 * @return width of this texture.
     */
	public int getWidth() {
		return width;
	}

	/**
	 * Retrieves the height of this texture.
	 *
	 * @return the height
     */
	public int getHeight() {
		return height;
	}

	/**
	 * Retrieves the depth of this texture.
	 *
	 * @return the depth of this texture.
     */
	public int getDepth() {
		return depth;
	}

	/**
	 * Retrieves the name of this texture.
	 *
	 * @return the name of this texture.
     */
	public String getName() {
		return name;
	}




	@Override
	public void deleteObject(Renderer renderer) {
		renderer.deleteTexture(this);
	}

	@Override
	public void resetObject() {
		enableUpdateRequired();
	}

}

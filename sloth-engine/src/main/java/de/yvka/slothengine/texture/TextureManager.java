package de.yvka.slothengine.texture;

import de.yvka.slothengine.engine.Engine;
import de.yvka.slothengine.engine.EngineComponent;
import de.yvka.slothengine.texture.image.Image;
import de.yvka.slothengine.texture.image.ImageManager;
import de.yvka.slothengine.texture.image.Image;
import de.yvka.slothengine.texture.image.ImageManager;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for creating and obtaining Textures.
 * This is the central place where texture objects are stored.
 */
public class TextureManager implements EngineComponent {

	/**
	 * Indicates that a argument such as depth isn't set.
	 */
	public final int NOT_SET = 0;


	private final Map<String, Texture> textures = new HashMap<>();
	private final ImageManager imageManager = new ImageManager();
	private boolean initialized;

	public Texture createTexture(String name, String imageFileName) {
		Image image;
		try {
			image = imageManager.loadImage(imageFileName);
		} catch (IOException e) {
			throw new TextureException(e, "Could not create the texture:image could not loaded");
		}
		return createTexture(name, image);
	}

	public Texture createTexture(String name, BufferedImage image) {
		return createTexture(name, imageManager.createImageFrom(image));
	}

	public Texture createTexture(String name, Image image) {

		int width = image.getWidth();
		int height = image.getHeight();


		// a height equal lower than one indicates a 1D texture
		height = height <= 1 ? NOT_SET : height;
		Texture texture = createTexture(name, width, height,NOT_SET);
		texture.setImage(image);

		return texture;
	}

	public Texture createTexture(String name, int width, int height, int depth) {

		if (textures.get(name) != null) {
			throw new TextureException("The specified texture " + name + "was already created.\nThe Method \"getTexture\" is the way to go");
		}

		Texture texture = new Texture();
		texture.setName(name);
		texture.setWidth(width);
		texture.setHeight(height);
		texture.setDepth(depth);
		texture.setWrapMode(Texture.TextureAxis.S, Texture.WrapMode.Repeast);
		texture.setWrapMode(Texture.TextureAxis.T, Texture.WrapMode.Repeast);
		texture.setWrapMode(Texture.TextureAxis.R, Texture.WrapMode.Repeast);

		if (width == NOT_SET) {
			throw new IllegalArgumentException("A texture must have at least a width value greater than zero");
		}

		if (height == NOT_SET && depth == NOT_SET) {
			texture.setDimension(Texture.Dimension.DIMENSION_1D);
		} else if(depth == NOT_SET) {
			texture.setDimension(Texture.Dimension.DIMENSION_2D);
		} else {
			texture.setDimension(Texture.Dimension.DIMENSION_3D);
		}

		textures.put(name, texture);
		texture.enableUpdateRequired();

		return texture;
	}

	public Texture getTexture(String name) {
		Texture texture = textures.get(name);
		if (texture == null) {
			throw new TextureException("Could not found the  requested texture: " + name);
		}
		return texture;
	}


	@Override
	public void initialize() {
		imageManager.initialize();
		initialized = true;
	}

	@Override
	public void shutdown() {
		imageManager.shutdown();
		textures.values().forEach((texture -> {
			texture.deleteObject(Engine.getCurrentRenderer());
		}));
		initialized = false;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}


}

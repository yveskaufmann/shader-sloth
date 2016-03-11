package de.yvka.slothengine.texture.image;

/**
 * Adapter for the java.awt.image class
 */
public class BufferedImage implements Image {

	private  java.awt.image.BufferedImage image;
	private  ImageFormat format;

	BufferedImage(java.awt.image.BufferedImage image, ImageFormat format) {
		this.image = image;
		this.format = format;
	}

	public BufferedImage(int width ,int height, ImageFormat format) {
		image = new java.awt.image.BufferedImage(width, height, convertFormatToIntType(format));
		this.format = format;
	}


	@Override
	public int getWidth() {
		return image.getWidth();
	}

	@Override
	public int getHeight() {
		return image.getHeight();
	}

	@Override
	public ImageFormat getFormat() {
		return format;
	}

	@Override
	public ImageReader getReader() {
		return new BufferedImageReader(this);
	}

	@Override
	public boolean isUpdateRequired() {
		return false;
	}

	java.awt.image.BufferedImage getData() {
		return image;
	}

	private int convertFormatToIntType(ImageFormat format) {
		switch (format) {
			case INT_RGB:
				return java.awt.image.BufferedImage.TYPE_INT_RGB;
			case INT_RGBA:
				return java.awt.image.BufferedImage.TYPE_INT_ARGB;
		}
		throw new IllegalArgumentException("Unsupported image format specified");
	}
}

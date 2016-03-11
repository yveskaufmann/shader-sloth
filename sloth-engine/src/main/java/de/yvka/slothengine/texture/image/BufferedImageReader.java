package de.yvka.slothengine.texture.image;

import de.yvka.slothengine.utils.BufferUtils;
import de.yvka.slothengine.utils.TypeSize;
import de.yvka.slothengine.utils.BufferUtils;

import java.nio.ByteBuffer;

public class BufferedImageReader implements ImageReader {

	private BufferedImage image;
	private java.awt.image.BufferedImage imageData;
	private int[] pixelBuffer;

	public BufferedImageReader(BufferedImage image) {
		this.image = image;
		this.imageData = image.getData();
	}

	@Override
	public ByteBuffer getBuffer() {
		return getBuffer(0, 0, image.getWidth(), image.getHeight());
	}

	@Override
	public ByteBuffer getBuffer(int x, int y, int xEnd, int yEnd) {

		int width  = xEnd - x;
		int height = yEnd - y;

		if (width < 0 || height < 0) {
			throw new IllegalArgumentException("XEnd/YEnd must be greater than x/y.");
		}

		int bufferSize = width * height;
		if (pixelBuffer == null || bufferSize != pixelBuffer.length) {
			pixelBuffer = new int[bufferSize];
		}

		//mageData.getRGB(x, y, xEnd, yEnd, pixelBuffer, 0, image.getWidth());
		ByteBuffer imageBuffer = BufferUtils.createByteBuffer(bufferSize * TypeSize.INT);

		for (int yPos = x;  yPos < yEnd; yPos++) {
			for(int xPos = x; xPos < xEnd; xPos++) {
				int rgb = imageData.getRGB(xPos,yPos);
				imageBuffer.put((byte) ((rgb >> 16) & 0xFF)); // Red
				imageBuffer.put((byte) ((rgb >> 8) & 0xFF));  // Green
				imageBuffer.put((byte) (rgb & 0xFF));       // Blue
				imageBuffer.put((byte) ((rgb >> 24) & 0xFF)); // Alpha
			}
		}
		imageBuffer.flip();
		return imageBuffer;
	}
}

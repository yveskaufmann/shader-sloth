package de.yvka.slothengine.texture.image;

import java.nio.ByteBuffer;

/**
 * A raster reader provides access a byte buffer
 * of a corresponding image.
 */
public interface ImageReader {
	ByteBuffer getBuffer();
	ByteBuffer getBuffer(int x, int y, int xEnd, int yEnd);
}

package de.yvka.slothengine.texture.image;

/**
 * Interface to a image which encapsulates
 * the access to the raster data of the image.
 */
public interface Image {



	enum ImageFormat {
		NOT_SUPPORTED(-1),
		INT_RGB(3), INT_RGBA(4), INT_ARGB(4), INT_ARGB_PRE(4),
		INT_BGR(3), BYTE3_BGR(3), BYTE4_ABGR(4), BYTE4_ABGR_PRE(4),
		USHORT_565_RGB(16), USHORT_555_RGB(16), USHORT_GRAY(16),
		BYTE_GRAY(1), BYTE_BINARY(1), BYTE_INDEXED(1);

		public final int bytesPerPixel;

		ImageFormat(int bytesPerPixel) {
			this.bytesPerPixel = bytesPerPixel;
		}
	}

	/**
	 * Retrieves the width of the image in pixels.
	 *
	 * @return the count of pixels per row.
     */
	int getWidth();

	/**
	 * Retrieves the height of the image in pixels.
	 * @return
     */
	int getHeight();

	/**
	 * Retrieves the format of the underlying image.
	 *
	 * @return the format of this image.
     */
	ImageFormat getFormat();


	/**
	 * Creates and returns a <code>Raster Reader</code>
	 * for read data from this image.
	 *
	 * @return the raster reader of this image.
     */
	ImageReader getReader();

	boolean isUpdateRequired();

}

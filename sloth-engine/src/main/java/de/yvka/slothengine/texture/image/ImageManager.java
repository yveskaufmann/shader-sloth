package de.yvka.slothengine.texture.image;

import de.yvka.slothengine.engine.EngineComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Central class for image handling
 * such as loading if images etc.
 */
public class ImageManager implements EngineComponent {


	/**
	 * This array service as map in order to convert an
	 * Java.awt format to the corresponding enum type.
	 */
	private static final Image.ImageFormat[] AWT_IMAGE_FORMAT_MAPPING = new Image.ImageFormat[]{
		Image.ImageFormat.NOT_SUPPORTED, Image.ImageFormat.INT_RGB,
		Image.ImageFormat.INT_ARGB, Image.ImageFormat.INT_ARGB_PRE,
		Image.ImageFormat.INT_BGR, Image.ImageFormat.BYTE3_BGR,
		Image.ImageFormat.BYTE4_ABGR, Image.ImageFormat.BYTE4_ABGR_PRE,
		Image.ImageFormat.USHORT_565_RGB, Image.ImageFormat.USHORT_555_RGB,
		Image.ImageFormat.BYTE_GRAY, Image.ImageFormat.USHORT_GRAY,
		Image.ImageFormat.BYTE_BINARY, Image.ImageFormat.BYTE_INDEXED
	};

	private static final Logger Log = LoggerFactory.getLogger(ImageManager.class);

	/**
	 * The default look up space for the texture images.
	 */
	public static final String ASSETS_TEXTURE_FOLDER = "assets/textures/";

	/**
	 * Flag which indicate if this component is initialized
	 */
	private boolean initialized;

	/**
	 * Loads an image by its name including it's extension.
	 * By default this method looks up for images inside the <code>assests/textures</code>
	 * directory.
	 *
	 * @param filename
	 * 			the local file name including it's extension
	 *
	 * @return an image object containing the loaded image.
	 *
	 * @throws IOException if an error occurs while loading the image
     */
	public Image loadImage(String filename) throws IOException  {
		String imagePath;

		imagePath = ASSETS_TEXTURE_FOLDER + filename;

		try(InputStream in = new FileInputStream(imagePath)) {
			return loadImage(in, filename);
		} catch (FileNotFoundException e) {
			Log.error("The specified image file {} could not be found.", imagePath);
			throw e;
		} catch (IOException e) {
			Log.error("Failed to parseFile the image from the specified file.", e);
			throw e;
		}
	}

	/**
	 * Loads an image by a specified <code>InputStream</code>.
	 *
	 * @param inputStream
	 * 				the input stream to a specified image.
	 * @return	an image object containing the loaded image.
	 *
	 * @throws IOException if an error occurs while loading the image.
     */
	private Image loadImage(InputStream inputStream, String filename) throws IOException {

		BufferedImage imageData;
		try {
			imageData = ImageIO.read(inputStream);
		} catch (IOException e) {
			Log.error("Failed to load the specified image: " + filename, e);
			throw e;
		}

		if (imageData == null) {
			throw new IOException("Failed to load the specified image: " + filename + ": image format not supported");
		}
		return createImageFrom(imageData);
	}

	/**
	 * Creates an image from a specified buffered image
	 *
	 * @param imageData the specified buffered image.
	 * @return Creates a image object.
     */
	public Image createImageFrom(BufferedImage imageData) {
		Image.ImageFormat imageFormat = determineImageFormat(imageData);
		return new de.yvka.slothengine.texture.image.BufferedImage(imageData, imageFormat);
	}

	private Image.ImageFormat determineImageFormat(BufferedImage imageData) {
		int type = imageData.getType();

		if (type <= 0 || type >= AWT_IMAGE_FORMAT_MAPPING.length) {
			throw new IllegalStateException("The loaded image has an unsupported type and can not be loaded");
		}

		return AWT_IMAGE_FORMAT_MAPPING[type];
	}

	@Override
	public void initialize() {
		Log.info("Initialize ImageManager");
		initialized = true;
	}

	@Override
	public void shutdown() {
		Log.info("Shutdown ImageManager");
		initialized = false;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	public static void show( java.awt.Image image ) {
		JFrame frame = new JFrame();
		JPanel imageView = new JPanel();
		imageView.setBackground(java.awt.Color.BLACK);
		imageView.add(new JLabel(new ImageIcon(image)));
		frame.add(imageView);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(image.getWidth(null),image.getHeight(null));
		frame.setVisible(true);
		frame.repaint();
	}
}

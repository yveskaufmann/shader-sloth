package de.yvka.slothengine.renderer.font;

import de.yvka.slothengine.texture.Texture;
import de.yvka.slothengine.texture.TextureManager;
import de.yvka.slothengine.engine.Engine;
import de.yvka.slothengine.renderer.*;
import de.yvka.slothengine.engine.Engine;
import de.yvka.slothengine.math.*;
import de.yvka.slothengine.renderer.*;
import de.yvka.slothengine.texture.TextureManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.yvka.slothengine.shader.Shader;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Class which is responsible to Render
 * text.
 */
public class FontRenderer {

	public static final String FONT_ATLAS_TEXTURE = "fontAtlas";


	private class GridInfo {
		public float rows;
		public float cols;
		public float cellWidth;
		public float cellHeight;
		public float width;
		public float height;

		@Override
		public String toString() {
			return String.format(
				"Grid Info = (rows = %f, cols = %f, cellWidth = %f, cellHeight = %f)",
				rows , cols, cellWidth, cellHeight);
		}
	}

	private class GridPos {
		public final float x;
		public final float y;
		private final float endX;
		private final float endY;

		public GridPos(float x, float y, GridInfo info) {
			this.x = x / info.width;
			this.y = y / info.height;
			this.endX = this.x + info.cellWidth;
			this.endY = this.y + info.cellHeight;
		}
	}

	/**
	 * Name of the shader which is responsible to
	 * render the text quads with the given font sprite texture.
	 */
	private static final String FONT_SHADER = "FontRender";

	/**
	 * Logger for this class
	 */
	private static final Logger Log = LoggerFactory.getLogger(FontRenderer.class);

	/**
	 * This BufferedImage is only used in order to obtain
	 * the metrics of a renderer.font.
	 */
	private final static BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

	/**
	 * The list of chars which should be supported by this renderer.
	 */
	private final char[] SUPPORTED_CHARS ="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789öäü!§.$:%,&;-_#''\"*+?ß=<>(){} ".toCharArray();
	private static final char NEW_LINE = '\n';
	private static final char CARRIAGE_RETURN = '\r';
	private static final char UNKNOWN_CHAR = '?';

	/**
	 * Contains the position in side the renderer.font sheet texture of each character.
	 */
	private Map<Character, GridPos> gridPosMap;

	/**
	 * The renderer.font which should be used for this renderer
	 */
	private Font font;

	/**
	 * The metrics of this renderer.font.
	 */
	private FontMetrics metrics;


	/**
	 * The Charset image
	 */
	private volatile BufferedImage charsetImage;

	/**
	 * Stores info about the created charset image
	 * in order to easily read out these images.
	 */
	private GridInfo gridInfo;

	/**
	 * The shader which is responsible to draw text.
	 */
	private Shader fontShader;

	/**
	 * The texture which contains the font atlas.
	 */
	private Texture texture;

	/**
	 * VAO id
	 */
	private int vaoId;

	/**
	 * Renderer instance
	 */
	private de.yvka.slothengine.renderer.Renderer renderer;



	/**
	 * Render state
	 */
	private RenderState renderState = new RenderState();

	/**
	 * Texture manager
	 */
	private TextureManager textureManager;

	/**
	 * Render Manager
	 */
	private RendererManager rendererManager;

	public FontRenderer(Font font) {
		this.font = font;
		this.metrics = getFontMetrics(font);
		this.gridPosMap = new HashMap<>();
		this.rendererManager = Engine.renderManager();
		this.textureManager = Engine.textureManager();
		new Thread(this::prepareLetterBitmap).start();
	}

	private void prepareLetterBitmap() {
		gridInfo = new GridInfo();
		/**
		 * 	Find the size of the widest letters
		 * 	in order to use this size as the
		 * 	with for the letter grid.
 		 */
		int widthPerLetter = -1;
		int heightPerLetter = metrics.getHeight();
		for (char chr : SUPPORTED_CHARS) {
			int w = metrics.charWidth(chr);

			if (w > widthPerLetter) {
				widthPerLetter = w;
			}
		}

		// Calculates the required size for char set texture.
		gridInfo.width = 32;
		gridInfo.height = 32;
		do {
			do {
				gridInfo.width = gridInfo.height = gridInfo.width * 2;
				gridInfo.cols = gridInfo.width / widthPerLetter;
			} while( gridInfo.cols < 1.0f);

			gridInfo.cols = (float) Math.floor(gridInfo.cols);
			gridInfo.rows = (int)Math.ceil(SUPPORTED_CHARS.length / gridInfo.cols) + 1;

		} while( gridInfo.rows * heightPerLetter > gridInfo.height);

		gridInfo.cellWidth = widthPerLetter / gridInfo.width;
		gridInfo.cellHeight = heightPerLetter / gridInfo.height;

		Log.debug("Grids size of font sprite image (rows = {}, cols = {})", gridInfo.rows, gridInfo.cols);

		BufferedImage charsetImage = new BufferedImage((int)gridInfo.width, (int)gridInfo.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) charsetImage.getGraphics();


		for (int i = 0; i < SUPPORTED_CHARS.length; i++) {
			char chr = SUPPORTED_CHARS[i];
			BufferedImage charImage = writeLetterAsImage(chr);

			int row = (int) Math.floor(i / gridInfo.cols);
			int col = i - row * (int) gridInfo.cols;
			int x = col * widthPerLetter;
			int y = row * heightPerLetter;
			gridPosMap.put(chr, new GridPos(x, y, gridInfo));

			g.drawImage(charImage, x, y, charImage.getWidth(), charImage.getHeight(), null);
		}

		Log.debug("Created renderer.font {} texture sheet with the size ({},{}).",font.toString().replaceAll("java.awt.Font", "") , gridInfo.width, gridInfo.height);
		Log.debug("{}.", gridInfo);
		this.charsetImage = charsetImage;
	}

	/**
	 * Draw a given letter to an <code>BufferedImage</code> in white color
	 * on a transparent background.
	 *
	 * The white color serves as key color which can be replaced
	 * by a fragment shader.
	 *
	 * @param letter the letter which should be draw
     *
	 * @return an image with the rendered letter.
     */
	public BufferedImage writeLetterAsImage(char letter) {

		FontMetrics fontMetrics = getFontMetrics(font);

		int imageWidth = fontMetrics.charWidth(letter);
		int imageHeight = fontMetrics.getHeight();

		// Ensure that with and height are >= 1
		imageWidth = Math.max(1, imageWidth);
		imageHeight = Math.max(1, imageHeight);

		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();

		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(java.awt.Color.white);
		g.setFont(font);

		g.drawString(String.valueOf(letter), 0, fontMetrics.getAscent());
		g.dispose();

		return image;

	}

	/**
	 * Creates a texture object for a given buffered image.
	 *
	 * @param image The image which should be loaded into a texture
	 * @return the <code>id</code> of the created texture.
     */
	private Texture createTexture(BufferedImage image) {

		Texture texture = textureManager.createTexture(FONT_ATLAS_TEXTURE, image);
		texture.setWrapMode(Texture.TextureAxis.S, Texture.WrapMode.ClampEDGE);
		texture.setWrapMode(Texture.TextureAxis.T, Texture.WrapMode.ClampEDGE);
		texture.setMinFilter(Texture.MinFilter.Trilinear);
		texture.setMagFilter(Texture.MagFilter.Bilinear);

		return texture;
	}



	private void prepare() {
		renderer = rendererManager.getRenderer();
		bindFontTexture();

		renderState.setDepthTestMode(RenderState.TestFunc.Off);
		renderState.setBlendMode(RenderState.BlendFunc.Alpha);
		renderer.applyRenderState(renderState);
		fontShader = Engine.getShader(FONT_SHADER);
	}

	private void bindFontTexture() {
		if ( texture == null ) {
			texture = createTexture(this.charsetImage);
			this.charsetImage = null;
		}
		renderer.setTexture(0, texture);
	}

	private void prepareFontShader(char letter, float xStartQuad, float yStartQuad, float xEndQuad, float yEndQuad, de.yvka.slothengine.math.Color color) {
		if (!gridPosMap.containsKey(letter)) {
			letter = UNKNOWN_CHAR;
		}
		GridPos pos = gridPosMap.get(letter);

		// Texture Coords
		fontShader.getUniform("fontSprite").setValue(0);
		fontShader.getUniform("xOffset").setValue(pos.x);
		fontShader.getUniform("yOffset").setValue(pos.y);
		fontShader.getUniform("xEnd").setValue(pos.endX);
		fontShader.getUniform("yEnd").setValue(pos.endY);

		// Quad Position and Size
		fontShader.getUniform("xStartQuad").setValue(xStartQuad);
		fontShader.getUniform("yStartQuad").setValue(yStartQuad);
		fontShader.getUniform("xEndQuad").setValue(xEndQuad);
		fontShader.getUniform("yEndQuad").setValue(yEndQuad);
		fontShader.getUniform("color").setValue(color);

		try {
			renderer.setShader(fontShader);
		} catch (IOException e) {
			Log.error("Font shader error, ", e);
		}

	}

	public void drawString(String text, int x, int y, float fontSize, de.yvka.slothengine.math.Color color) {
		prepare();
		float windowWidth = Engine.getPrimaryWindow().getWidth();
		float windowHeight = Engine.getPrimaryWindow().getHeight();

		float ndc_x_start;
		float ndc_x = x / windowWidth;
		float ndc_y = y / windowHeight;
		ndc_x = ndc_x_start = (ndc_x * 2.0f) - 1.0f;
		ndc_y = -(ndc_y * 2.0f) + 1.0f;

		for(int i = 0; i < text.length(); i++) {
			char letter = text.charAt(i);
			if (letter == CARRIAGE_RETURN) continue;
			if (letter == NEW_LINE) {
				ndc_y -= (float ) metrics.getHeight() / windowHeight;
				ndc_x = ndc_x_start;
				continue;
			}

			float fontWidth = (metrics.charWidth(letter) / windowWidth) * fontSize;
			float fontHeight = (metrics.getHeight() / windowHeight) * fontSize;

			prepareFontShader(letter, ndc_x, ndc_y, ndc_x + fontWidth, ndc_y - fontHeight, color);
			glDrawArrays(GL_TRIANGLES, 0, 6);
			ndc_x += fontWidth;

		}
		endRendering();
	}

	private void endRendering() {
		renderer.applyRenderState(renderState.reset());
	}


	/**
	 * Retrieves the created char set image which contains
	 * the renderer.font as image.
	 *
	 * @return the created char set image.
     */
	public BufferedImage getCharsetImage() {
		return charsetImage;
	}

	/**
	 * Retrieves the metrics of a specified renderer.font.
	 *
	 * @param font the renderer.font for which the metrics are requested
	 * @return the metrics of the specified renderer.font.
     */
	private FontMetrics getFontMetrics(Font font) {
		Graphics2D g = (Graphics2D) dummyImage.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		return g.getFontMetrics(font);
	}


	public static void main(String[] args) throws IOException {

		FontRenderer renderer = new FontRenderer(new Font("Courier", Font.PLAIN, 64));
		BufferedImage image = renderer.getCharsetImage();

		JFrame frame = new JFrame();
		JPanel imageView = new JPanel();
		imageView.setBackground(java.awt.Color.BLACK);
		imageView.add(new JLabel(new ImageIcon(image)));
		frame.add(imageView);
		frame.setTitle("Font Sprite Viewer");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(image.getWidth(), image.getHeight());
		frame.setVisible(true);
		frame.repaint();
	}

}

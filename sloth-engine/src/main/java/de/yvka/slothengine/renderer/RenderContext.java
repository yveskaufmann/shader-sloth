package de.yvka.slothengine.renderer;

import de.yvka.slothengine.shader.Shader;
import de.yvka.slothengine.renderer.RenderState.*;

public class RenderContext {



	RenderContext() {
		reset();
	}

	/**
	 * The winding which is expected for front faces.
	 */
	public RenderState.FaceWinding frontFaceWinding;

	/**
	 * If enabled, do alpha testing. See glAlphaFunc.
	 */
	public boolean alphaTest;

	/**
	 * if enabled, blend the computed fragment color values with the values in the color buffers
	 */
	public boolean blend;

	/**
	 * If enabled, clip geometry against user-defined half space i.
	 */
	public boolean clipDistance;

	/**
	 * Enable the use of a logical operation which is applied between the incoming RGBA color and the RGBA color at the corresponding location in the frame buffer.
	 * <a href="https://www.opengl.org/sdk/docs/man/html/glLogicOp.xhtml">See glLogicOp</a>
	 */
	public boolean colorLogicOp;

	/**
	 * If enabled, debug messages are produced by a debug context.
	 * When disabled, the debug message log is silenced.
	 * Note that in a non-debug context, very few, if any messages might be produced,
	 * even when GL_DEBUG_OUTPUT is enabled.
	 */
	public boolean debugOutput;

	/**
	 * If enabled, debug messages are produced synchronously by a debug context.
	 * If disabled, debug messages may be produced asynchronously.
	 * In particular, they may be delayed relative to the execution of GL commands,
	 * and the debug callback function may be called from a thread other than
	 * that in which the commands are executed.
	 *
	 * <a href="https://www.opengl.org/sdk/docs/man/html/glDebugMessageCallback.xhtml">See glDebugMessageCallback</a>
	 */
	public boolean debugOutputSynchronously;

	/**
	 * If enabled, the −wc <= zc <= wc plane equation is ignored by shadersloth.view volume clipping (effectively, there is no near or far plane clipping).
	 * <a href="https://www.opengl.org/sdk/docs/man/html/glDepthRange.xhtml">See glDepthRange</a>.
	 */
	public boolean depthClamp;

	/**
	 * If enabled, do depth comparisons and onUpdate the depth buffer.
	 * Note that even if the depth buffer exists and the depth mask is non-zero,
	 * the depth buffer is not updated if the depth test is disabled.
	 *
	 * See <a href="https://www.opengl.org/sdk/docs/man/html/glDepthFunc.xhtml">glDepthFunc</a> and <a href="https://www.opengl.org/sdk/docs/man/html/glDepthRange.xhtml">glDepthRange</a>.
	 */
	public boolean depthTesting;

	/**
	 * If enabled, dither color components or indices before they are written to the color buffer.
	 */
	public boolean dither;

	/**
	 * If enabled and the value of GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING
	 * for the framebuffer attachment corresponding to the destination buffer
	 * is GL_SRGB, the R, G, and B destination color values
	 * (after conversion from fixed-point to floating-point) are considered to be encoded
	 * for the sRGB color space and hence are linearized prior to their use in blending.
	 */
	public boolean framebufferSRGB;

	/**
	 * If enabled, draw lines with correct filtering. Otherwise, draw aliased lines.
	 *
	 * <a href="https://www.opengl.org/sdk/docs/man/html/glLineWidth.xhtml">See glLineWidth</a>.
	 */
	public boolean lineSmooth;

	/**
	 * If enabled, use multiple fragment samples in computing the final color of a pixel.
	 * <a href="https://www.opengl.org/sdk/docs/man/html/glSampleCoverage.xhtml">See glSampleCoverage</a>.
	 */
	public boolean multisample;

	/**
	 * If enabled, and if the polygon is rendered in GL_FILL mode,
	 * an offset is added to depth values of a polygon's fragments
	 * before the depth comparison is performed.
	 * <a href="https://www.opengl.org/sdk/docs/man/html/glPolygonOffset.xhtml">See glPolygonOffset</a>.
	 */
	public boolean polygonOffsetFill;

	/**
	 * If enabled, and if the polygon is rendered in GL_LINE mode,
	 * an offset is added to depth values of a polygon's fragments
	 * before the depth comparison is performed.
	 * <a href="https://www.opengl.org/sdk/docs/man/html/glPolygonOffset.xhtml">See glPolygonOffset</a>.
	 */
	public boolean polygonOffsetLine;

	/**
	 * If enabled, an offset is added to depth values of a polygon's fragments
	 * before the depth comparison is performed, if the polygon is rendered in GL_POINT mode.
	 * <a href="https://www.opengl.org/sdk/docs/man/html/glPolygonOffset.xhtml">See glPolygonOffset</a>.
	 */
	public boolean polygonOffsetPoint;

	/**
	 * If enabled, draw polygons with proper filtering.
	 * Otherwise, draw aliased polygons.
	 * For correct antialiased polygons, an alpha buffer is needed and the polygons must be sorted
	 * front to back.
	 */
	public boolean polygonSmooth;

	/**
	 * Enables primitive restarting. If enabled,
	 * any one of the draw commands which transfers a set of generic attribute
	 * array elements to the GL will restart the primitive when the index of the vertex
	 * is equal to the primitive restart index. See glPrimitiveRestartIndex.
	 */
	public boolean primativeRestart;

	/**
	 * Enables primitive restarting with a fixed index.
	 * If enabled, any one of the draw commands which transfers
	 * a set of generic attribute array elements to the GL will restart the primitive
	 * when the index of the vertex is equal to the fixed primitive index for the
	 * specified index type.
	 * The fixed index is equal to 2n−12n−1 where n is equal
	 * to 8 for GL_UNSIGNED_BYTE, 16 for GL_UNSIGNED_SHORT and 32 for GL_UNSIGNED_INT.
	 */
	public boolean primativeRestartFixedIndex;

	/**
	 * If enabled, primitives are discarded after the optional transform feedback stage,
	 * but before rasterization.
	 * Furthermore, when enabled, glClear, glClearBufferData,
	 * glClearBufferSubData, glClearTexImage, and glClearTexSubImage are ignored.
	 */
	public boolean rasterizerDiscard;

	/**
	 * If enabled, compute a temporary coverage value where each bit is determined
	 * by the alpha value at the corresponding sample location.
	 * The temporary coverage value is then ANDed with the fragment coverage value.
	 */
	public boolean sampleAlphaToCoverage;

	/**
	 * If enabled, each sample alpha value is replaced by the maximum representable alpha value.
	 */
	public boolean sampleAlphaToOne;

	/**
	 * If enabled, the fragment's coverage is ANDed with the temporary coverage value.
	 * If GL_SAMPLE_COVERAGE_INVERT is set to GL_TRUE, invert the coverage value. See glSampleCoverage.
	 */
	public boolean sampleCoverage;

	/**
	 * If enabled, the active fragment shader is run once for each covered sample,
	 * or at fraction of this rate as determined by the current value of
	 * GL_MIN_SAMPLE_SHADING_VALUE. See glMinSampleShading.
	 */
	public boolean sampleShading;

	/**
	 * If enabled, the sample coverage mask generated for a fragment during rasterization
	 * will be ANDed with the value of GL_SAMPLE_MASK_VALUE before shading occurs.
	 * See glSampleMaski.
	 */
	public boolean sampleMask;

	/**
	 * If enabled, discard fragments that are outside the scissor rectangle.
	 * <a href="https://www.opengl.org/sdk/docs/man/html/glScissor.xhtml">See glScissor</a>.
	 */
	public boolean scissorTest;

	/**
	 * If enabled, do stencil testing and onUpdate the stencil buffer.
	 * See <a href="https://www.opengl.org/sdk/docs/man/html/glStencilFunc.xhtml">glStencilFunc</a> and
	 *
	 * <a href="https://www.opengl.org/sdk/docs/man/html/glStencilOp.xhtml">glStencilOp</a>.
	 */
	public boolean stencilTest;

	/**
	 * If enabled, cubemap textures are sampled such that when linearly sampling from the border
	 * between two adjacent faces, texels from both faces are used to generate
	 * the final sample value. When disabled, texels from only a single face are used to
	 * construct the final sample value.
	 */
	public boolean textureCubeMapSeamless;

	/**
	 * If enabled and a vertex or geometry shader is active,
	 * then the derived point size is taken from the (potentially clipped)
	 * shader builtin gl_PointSize and clamped to the implementation-dependent point size range.
	 */
	public boolean programPointSize;

	/**
	 * If enabled the renders only wireframe output.
	 */
	public boolean wireframe;


	/**
	 * The current enabled blend function which should be used
	 * if <code>GL_BLEND</code> is enabled.
	 */
	public BlendFunc blendMode;

	/**
	 * Logical Pixel operation which should be used if <code>COLOR_LOGIC_OP</code> is enabled.
	 */
	public LogicalPixelOperation logicOperation;

	/**
	 * Specifies which cull face mode should be use if <code>cullface</code> is enabled.
	 */
	public CullFaceMode cullFaceMode;

	/**
	 * Specifies which depth function should be used if <code>depthTesting is enabled.</code>
	 */
	public TestFunc depthTestMode;

	/**
	 * Specifies the width of rasterized lines.
	 */
	public float lineWith = 1.0f;

	/**
	 * glPolygonOffset - factor
	 *
	 * Specifies a scale factor that is used to create a variable depth offset for each polygon.
	 * The initial value is 0.
	 */
	public float polygonOffsetFactor;

	/**
	 * glPolygonOffset — unit
	 *
	 * Is multiplied by an implementation-specific value to create a constant
	 * depth offset. The initial value is 0.
	 */
	public float polygonOffsetUnits ;

	/**
	 * Specifies the test function for stencil testing.
	 */
	public TestFunc stencilTestFunc = TestFunc.Always;
	public int stencilRef = 0;
	public int stencilMask = 1;

	/**
	 * Specify the diameter of rasterized points,
	 * if <code>programPointSize</code> is disabled.
	 */
	public float pointSize;

	/**
	 * The current bounded shader
	 */
	public Shader boundShader;

	/**
	 * The current bounded vertex buffer object.
	 */
	public int boundVboBuffer;

	/**
	 * The current bounded element array buffer vbo object.
	 */
	public int boundElementArrayVboBuffer;

	/**
	 * Viewport start x coordinate
	 */
	public int viewPortX;

	/**
	 * Viewport start y coordinate
	 */
	public int viewportY;

	/**
	 * Viewport width
	 */
	public int viewPortWidth;

	/**
	 * Viewport height
	 */
	public int viewPortHeight;
	public int scissorX;
	public int scissorY;
	public int scissorWidth;
	public int scissorHeight;

	/**
	 * Current active texture unit
	 */
	public int activeTextureUnit;

	/**
	 * The current bounded textures.
	 * The index specifies to which texture unit the
	 * texture is bounded.
	 *
	 * The count of 80 texture unit comes from the specification
	 * <a href="https://www.opengl.org/sdk/docs/man/docbook4/xhtml/glActiveTexture.xml">glActiveTexture</a>
	 * which says that a  opengl driver must at least support 80 texture units.
	 */
	public final int[] boundTextures = new int[80];

	public void reset() {
		frontFaceWinding = FaceWinding.GL_CCW;
		blend = false;
		clipDistance = false;
		colorLogicOp = false;
		colorLogicOp = false;
		debugOutput = false;
		debugOutputSynchronously = false;
		depthClamp = false;
		depthTesting = false;
		dither = true;
		framebufferSRGB = false;
		lineSmooth = false;
		multisample = true;
		polygonOffsetFill = false;
		polygonOffsetLine = false;
		polygonOffsetPoint = false;
		polygonSmooth = false;
		primativeRestart = false;
		primativeRestartFixedIndex = false;
		rasterizerDiscard = false;
		sampleAlphaToCoverage = false;
		sampleAlphaToOne = false;
		sampleCoverage = false;
		sampleShading = false;
		sampleMask = false;
		scissorTest = false;
		stencilTest = false;
		textureCubeMapSeamless = false;
		programPointSize = false;
		wireframe = false;
		depthTestMode = TestFunc.Off;
		blendMode = BlendFunc.Off;
		logicOperation = LogicalPixelOperation.Copy;
		cullFaceMode = CullFaceMode.Off;
		lineWith = 1.0f;
		polygonOffsetFactor = 0.0f;
		polygonOffsetUnits = 0.0f;
		boundShader = null;
		boundVboBuffer = 0;
		boundElementArrayVboBuffer = 0;
		viewPortX = 0;
		viewportY = 0;
		viewPortWidth = -1;
		viewPortHeight = -1;
		activeTextureUnit = -1;
		for (int i = 0 ; i < boundTextures.length; i++) {
			boundTextures[i] = -1;
		}
	}




}

package de.yvka.slothengine.renderer;

public class RenderState {


	/**
	 * Specifies how the red, green, blue, and alpha destination blending factors are computed
	 * if <code>glEnable(GL_BLEND)</code> is enabled. You can choose
	 * on of these blend function by using the function <code>glBlendFunci</code>.*
	 */
	public enum BlendFunc {
		/**
		 * Disabled Blending
		 */
		Off,
		/**
		 * Blending with GL_ONE and GL_Zero
		 */
		Default,

		/**
		 *
		 */
		Additive, Color, Alpha,
	}

	/**
	 * Logical pixel operations <a href="https://www.opengl.org/sdk/docs/man/html/glLogicOp.xhtml">See glLogicOp</a>
	 */
	public enum LogicalPixelOperation {
		Off,
		Clear,
		Set,
		Copy,
		CopyInverted,
		Noop,
		Invert,
		And,
		Nand,
		Or,
		Nor,
		Xor,
		Equiv,
		AndReverse,
		AndInverted,
		OrReverse,
		OrInverted
	}

	public enum FaceWinding {
		GL_CW,
		GL_CCW
	}

	/**
	 * Cull face modes which can be used when
	 * <code>cullFace</code> is enabled. The initial
	 * value is <code>Back</code>.
	 */
	public enum CullFaceMode {
		Off,
		Front,
		Back,
		FrontAndBack
	}

	/**
	 *  The test functions which could used for <code>depthTesting</code>, <code>stencilTesting</code> and so.
	 */
	public enum TestFunc {

		/**
		 * Testing is not enabled
		 */
		Off,

		/**
		 * Never passes.
		 */
		Never,

		/**
		 * Passes if the incoming depth value is less than the stored depth value.
		 */
		Less,

		/**
		 * Passes if the incoming depth value is equal to the stored depth value.
		 */
		Equal,

		/**
		 * Passes if the incoming depth value is less than or equal to the stored depth value.*/
		LessOrEqual,

		/**
		 * Passes if the incoming depth value is greater than the stored depth value.
		 */
		Greater,

		/**
		 * Passes if the incoming depth value is not equal to the stored depth value.
		 */
		NotEqual,

		/**
		 * Passes if the incoming depth value is greater than or equal to the stored depth value.
		 */
		GreaterOrEqual,

		/**
		 * Always passes.
		 */
		Always
	}

	public enum StencilOperation {
		Off,

		/**
		 * Keeps the current value.
		 */
		Keep,

		/**
		 * Sets the stencil buffer value to 0.
		 */
		Zero,

		/**
		 * Sets the stencil buffer value to ref, as specified by glStencilFunc.
		 */
		Replace,

		/**
		 * Increments the current stencil buffer value. Clamps to the maximum representable unsigned value.
		 */
		Increments,

		/**
		 * Increments the current stencil buffer value.
		 * Wraps stencil buffer value to zero when incrementing the maximum representable unsigned value.
		 */
		Increments_Wrap,

		/**
		 * Decrements the current stencil buffer value. Clamps to 0.
		 */
		Decrement,

		/**
		 * Decrements the current stencil buffer value. Wraps stencil buffer value to the maximum representable unsigned value when decrementing a stencil buffer value of zero.
		 */
		Decrement_Wrap,

		/**
		 * Bitwise inverts the current stencil buffer value.
		 */
		Invert
	}


	private FaceWinding frontWinding;
	private CullFaceMode cullFaceMode;
	private TestFunc depthTestMode;
	private BlendFunc blendMode;
	private float lineWidth;
	private float pointSize;
	private boolean enableFPSCounter;
	private boolean wireframe;
	private boolean lineSmooth;
	private boolean smoothLineEnabled;

	/**
	 * Create a render state instance.
	 */
	public RenderState() {
		reset();
	}

	/**
	 * Retrieves the current cull face mode,
	 * specifies if and which faces should be culled.
	 *
	 * @return current cull face mode.
     */
	public CullFaceMode getCullFaceMode() {
		return cullFaceMode;
	}

	/**
	 * Specifies the current cull face mode.
	 *
	 * @param cullFaceMode the new current cull face mode.
	 *
	 * @return this
     */
	public RenderState setCullFaceMode(CullFaceMode cullFaceMode) {
		this.cullFaceMode = cullFaceMode;
		return this;
	}

	/**
	 * Retrieves the face winding for front faces.
	 *
	 * @return the face winding for front face
	 */
	public FaceWinding getFrontFaceWinding() {
		return frontWinding;
	}

	/**
	 * Defines if CW or CCW faces are viewed as
	 * front faces.
	 *
	 * @param faceWinding the face winding.
	 * @return this instance
     */
	public RenderState setFrontFaceWinding(FaceWinding faceWinding) {
		this.frontWinding = faceWinding;
		return this;
	}

	public TestFunc getDepthTestMode() {
		return depthTestMode;
	}

	public RenderState setDepthTestMode(TestFunc depthTestMode) {
		this.depthTestMode = depthTestMode;
		return this;
	}

	public BlendFunc getBlendMode() {
		return blendMode;
	}

	public RenderState setBlendMode(BlendFunc blendMode) {
		this.blendMode = blendMode;
		return this;
	}

	public float getLineWidth() {
		return lineWidth;
	}

	public RenderState setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
		return this;
	}

	public float getPointSize() {
		return pointSize;
	}

	public RenderState setPointSize(float pointSize) {
		this.pointSize = pointSize;
		return this;
	}


	public boolean isWireframe() {
		return wireframe;
	}

	public RenderState enableWireframe(boolean wireframe) {
		this.wireframe = wireframe;
		return this;
	}

	public RenderState toggleWireframe() {
		this.wireframe = !this.wireframe;
		return this;
	}

	public boolean isFPSCounterEnabled() {
		return enableFPSCounter;
	}

	public RenderState enableFPSCounter(boolean enable) {
		this.enableFPSCounter = enable;
		return this;
	}

	/**
	 * Determines if smooth line rendering should be
	 * enabled.
	 *
	 * @return true if smooth line rendering should be enabled.
     */
	public boolean isSmoothLinesEnabled() {
		return this.lineSmooth;
	}

	/**
	 * Enable smooth line rendering.
	 *
	 * @param enable
	 * @return this instance
     */
	public RenderState enableSmoothLines(boolean enable) {
		this.lineSmooth = enable;
		return this;
	}

	/**
	 * Reset the render state to the default state.
	 *
	 * @return this instance.
     */
	public RenderState reset() {
		wireframe = false;
		enableFPSCounter = false;
		lineSmooth = false;
		frontWinding = FaceWinding.GL_CCW;
		cullFaceMode = CullFaceMode.Back;
		depthTestMode = TestFunc.Less;
		blendMode = BlendFunc.Alpha;
		lineWidth = 1.0f;
		pointSize = 1.0f;
		return this;
	}
}

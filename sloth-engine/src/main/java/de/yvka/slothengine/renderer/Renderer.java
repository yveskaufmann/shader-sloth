package de.yvka.slothengine.renderer;

import de.yvka.slothengine.geometry.Mesh;
import de.yvka.slothengine.geometry.VertexBuffer;
import de.yvka.slothengine.math.Color;
import de.yvka.slothengine.shader.source.ShaderSource;
import de.yvka.slothengine.texture.image.Image;
import de.yvka.slothengine.geometry.VertexBuffer;
import de.yvka.slothengine.shader.source.ShaderSource;
import de.yvka.slothengine.texture.image.Image;
import org.lwjgl.opengl.GL41;
import de.yvka.slothengine.shader.Shader;
import de.yvka.slothengine.texture.Texture;

import java.io.IOException;

/**
 * Interface which acts as abstraction
 * to an gpu driver api such as OpenGL
 * OpenGL:ES, and so on.
 */
public interface Renderer {

	/**
	 * With the help of a render state it's possible to activate
	 * several options of the render implementation such as
	 * enable wireframe, cullFaceMode and so on.
	 *
	 * At this state the caller is responsible to reset
	 * the state before the next object is rendered which
	 * expects a default RenderState.
	 *
	 * @param state the state to apply.
     */
	void applyRenderState(RenderState state);

	/**
	 *  Forces to reset the state of this shader.
	 *  Which triggers that the render rebinds all GPU objects.
	 */
	void invalidateState();

	/**
	 * Defines the color buffer clear color.
	 *
	 * @param clearColor the color which should be used to clear
	 *                   color buffer.
     */
	void setClearColor(Color clearColor);

	/**
	 * Clear the color, depth and/or the stencil buffer.
	 *
	 * @param color if true the color buffer will be cleared.
	 * @param depth if true the depth buffer will be cleared.
	 * @param stencil if true the stencil will be cleared.
     */
	void clearBuffers(boolean color, boolean depth, boolean stencil);

	/**
	 *
	 * Specifies the viewport transformation parameters for all viewports.
	 *
	 * <p>The location of the viewport's bottom-left corner, given by {@code (x, y)}, are clamped to be within the implementation-dependent viewport bounds range.
	 * The viewport bounds range {@code [min, max]} tuple may be determined by calling {@link #glGetFloat GetFloat} with the symbolic
	 * constant {@link GL41#GL_VIEWPORT_BOUNDS_RANGE VIEWPORT_BOUNDS_RANGE}. Viewport width and height are clamped to implementation-dependent maximums when specified. The maximum
	 * width and height may be found by calling {@link #glGetFloat GetFloat} with the symbolic constant {@link #GL_MAX_VIEWPORT_DIMS MAX_VIEWPORT_DIMS}. The
	 * maximum viewport dimensions must be greater than or equal to the larger of the visible dimensions of the display being rendered to (if a display
	 * exists), and the largest renderbuffer image which can be successfully created and attached to a framebuffer object.</p>
	 *
	 * <p>In the initial state, {@code w} and {@code h} for each viewport are set to the width and height, respectively, of the window into which the GL is to do
	 * its rendering. If the default framebuffer is bound but no default framebuffer is associated with the GL context, then {@code w} and {@code h} are
	 * initially set to zero.</p>
	 *
	 * @param x the left viewport coordinate
	 * @param y the bottom viewport coordinate
	 * @param width the viewport width
	 * @param height the viewport height
	 */
	void setViewport(int x, int y, int width, int height);

	/**
	 * Specifies mapping of depth values from normalized device coordinates to window coordinates.
	 *
	 * After clipping and division by w, depth coordinates range from -1-1 to 1, corresponding to the near and far clipping planes.
	 * glDepthRange specifies a linear mapping of the normalized depth coordinates in this range to window depth coordinates.
	 * Regardless of the actual depth buffer implementation, window coordinate depth values are treated as though they range from 0 through 1
	 * (like color components). Thus, the values accepted by glDepthRange are both clamped to this range before they are accepted.
	 * The setting of (0,1) maps the near plane to 0 and the far plane to 1. With this mapping, the depth buffer range is fully utilized.
	 *
	 * @param near Specifies the mapping of the near clipping plane to window coordinates. The initial value is 0.
	 * @param far Specifies the mapping of the far clipping plane to window coordinates. The initial value is 1.
     */
	void setDepthRange(float near, float far);

	/**
	 * Defines the clip rectangle only pixels which lies
	 * in side this rectangle will be updated by the renderer which
	 * implies that the renderer must enable scissor testing.
	 *
	 * @param x the x coordinate of the left lower corner of the rectangle box in window coordinates
	 * @param y the y coordinate of the left lower corner of the rectangle box in window coordinates
	 * @param width the width of the rectangle box in pixels
     * @param height the height of the rectangle box in pixels.
     */
	void setClipRect(int x, int y, int width, int height);

	/**
	 * Clear and disable the clip rectangle and
	 * disable scissor test.
	 */
	void clearClipRect();

	/**
	 * Activate the provided shader which will be used for the
	 * up coming drawMesh calls.
	 *
	 * Note: It's important to call setShader before you
	 * call drawMesh because without any shader the renderer
	 * cannot render the mesh.
	 *
	 * @param shader the shader to activate
	 * @throws IOException if the shader couldn't be loaded
     */
	void setShader(Shader shader) throws IOException;

	/**
	 * Delete the shader object have to called if the shader
	 * isn't not used anymore. Only for internal use
	 * only.
	 *
	 * @param shader shader to delete.
     */
	void deleteShader(Shader shader);

	/**
	 * Delete the shader SourceObject have to called if the shader
	 * isn't not used anymore. Only for internal use
	 * only.
	 *
	 * @param source the source a shader.
     */
	void deleteShaderSource(ShaderSource source);

	/**
	 * Activate the specified texture for the upcoming
	 * render commands and bind it to the specified texture
	 * unit.
	 *
	 * Note: This method have to called before any call to
	 * setShader.
	 *
	 * @param unit the unit to bind the texture to.
	 * @param texture the texture to bind.
     */
	void setTexture(int unit, Texture texture);

	/**
	 * Delete the texture from the GPU.
	 *
	 * @param texture the texture to delete.
     */
	void deleteTexture(Texture texture);

	/**
	 * Delete the specified image which was
	 * used to create an image.
	 *
	 * @param image the image object.
     */
	void deleteImage(Image image);

	/**
	 * Draw a specified mesh object and
	 * must be called after setTexture, setShader
	 * in the described order.
	 *
	 * @param mesh the mesh which should be rendered
     */
	void drawMesh(Mesh mesh);

	/**
	 * Bind or create a vertex buffer on the GPU.
	 *
	 * @param buffer the vertexBuffer to bind or create.
     */
	void updateBuffer(VertexBuffer buffer);

	/**
	 * Delete a vertex buffer from the GPU.
	 *
	 * @param buffer the buffer which should be deleted.
     */
	void deleteBuffer(VertexBuffer buffer);

	/**
	 * Clean up all resources which are used the renderer.
	 * Must be called after the end of the render loop.
	 */
	void cleanUp();

	/**
	 * Reset all GPU objects so that they are
	 * onUpdate or created on the next frame.
	 */
	void resetGLObjects();

	/**
	 * Must be called before a new frame begins.
	 */
	void onNewFrame();

}

package de.yvka.slothengine.window;

import de.yvka.slothengine.engine.Engine;
import de.yvka.slothengine.engine.Engine;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;

/**
 * The window class which encapsulates the
 * window which is used to show the rendering result.
 */
public class Window {

	private static final Logger Log = LoggerFactory.getLogger(Window.class);

	private long windowId;
	private String title;
	private GLFWWindowSizeCallback sizeCallback;
	private boolean isVsyncEnabled = true;
	private State state;
	private boolean isVisible = false;
	private float aspectRatio = 0.0f;

	private enum State {
		ENABLED,
		DISABLED,
		CLEANED
	}

	Window(long windowId, String title) {
		this.windowId = windowId;
		this.title = title;
		this.state = State.DISABLED;
		this.isVsyncEnabled = false;
		enableVsync();
	}

	public Window setTitle(String title) {
		this.title = title;
		GLFW.glfwSetWindowTitle(windowId, title);
		return this;
	}

	public long getWindowId() {
		return windowId;
	}

	public int getWidth() {
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(windowId, widthBuffer, null);
		return widthBuffer.get();
	}

	public Window setWidth(int width) {
		setSize(width, getHeight());
		return this;
	}

	public int getHeight() {
		IntBuffer heightBuffer =  BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(windowId, null, heightBuffer);
		return heightBuffer.get();
	}

	public Window setHeight(int height) {
		setSize(getWidth(), height);
		return this;
	}

	public Window setSize(int width, int height) {
		width = Math.abs(width);
		height = Math.abs(height);

		width = (width < 1) ? 1 : width;
		height = (height < 1) ? 1 : height;

		GLFW.glfwSetWindowSize(windowId, width, height);
		return this;
	}

	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(windowId) == GLFW.GLFW_TRUE;
	}

	public Window enable() {

		if (state != State.DISABLED) {
			throw new IllegalStateException("This window was already enabled");
		}

		state = State.ENABLED;
		GLFW.glfwMakeContextCurrent(windowId);
		GL.createCapabilities();
		GLFW.glfwSetWindowSizeCallback(windowId, sizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				updateViewportSize();
			}
		});

		show();
		updateViewportSize();
		Log.info("Enabled and show window {} and make it to the current OpenGL context.", windowId);
		return this;
	}

	public Window disable() {
		if (state == State.DISABLED) {
			throw new IllegalStateException("This window was already disabled");
		}

		GLFW.glfwSetWindowSizeCallback(windowId, null);
		GLFW.glfwMakeContextCurrent(0L);

		return this;
	}

	public Window onFrameEnd() {
		GLFW.glfwSwapBuffers(windowId);
		GLFW.glfwPollEvents();
		return this;
	}

	public void enableVsync() {
		if (!isVsyncEnabled) {
			GLFW.glfwSwapInterval(1);
			isVsyncEnabled = true;
		}
	}

	public void disableVsync() {
		if (isVsyncEnabled) {
			GLFW.glfwSwapInterval(0);
			isVsyncEnabled = false;
		}
	}

	public void hide() {
		if (isVisible) {
			GLFW.glfwHideWindow(windowId);
			isVisible = false;
			Log.debug("Hide the window {}.", windowId);
		}
	}

	public void show() {
		if (!isVisible) {
			GLFW.glfwShowWindow(windowId);
			isVisible = true;
			Log.debug("Show the window {}.", windowId);
		}
	}

	public void destroy() {
		if (state == State.CLEANED) {
			throw new IllegalArgumentException("The Window was already destroyed");
		}

		if (state == State.DISABLED) {
			disable();
		}

		GLFW.glfwDestroyWindow(windowId);
		state = State.CLEANED;
	}

	public void requestClose() {
		GLFW.glfwSetWindowShouldClose(windowId, 1);
	}

	/**
	 * Update the Viewport and hand it to
	 * the default renderer.
	 */
	public void updateViewportSize() {
		int w =  getWidth();
		int h =  getHeight();

		aspectRatio = (float) ((float)w / (float)h);

		if (Engine.renderManager() != null) {
			Engine.renderManager().updateViewportSize(0, 0, w, h);
		}
	}

	/**
	 * Retrieves the current aspect ratio.
	 *
	 * @return the current aspect ratio.
     */
	public float getAspectRatio() {
		return aspectRatio;
	}

	public boolean isEnabled() {
		return state == State.ENABLED;
	}

	@Override
	public String toString() {
		return "Window{" +
			"windowId=" + windowId +
			", width=" + getWidth() +
			", height=" + getHeight() +
			", title='" + title + '\'' +
			", vsync=" + isVsyncEnabled +
			", state=" + state +
			", isVisible=" + isVisible +
			", aspectRatio=" + aspectRatio +
			'}';
	}
}

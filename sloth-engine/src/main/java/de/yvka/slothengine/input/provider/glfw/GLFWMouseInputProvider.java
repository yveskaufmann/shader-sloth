package de.yvka.slothengine.input.provider.glfw;

import de.yvka.slothengine.input.event.MouseEvent;
import de.yvka.slothengine.input.provider.MouseInputProvider;
import de.yvka.slothengine.utils.BufferUtils;
import de.yvka.slothengine.window.Window;
import de.yvka.slothengine.input.event.MouseEvent;
import de.yvka.slothengine.utils.BufferUtils;
import de.yvka.slothengine.window.Window;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.system.MemoryUtil;


import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

public class GLFWMouseInputProvider extends GLFWInputProvider<MouseEvent> implements MouseInputProvider {

	/**
	 * The amount how far the mouse wheel was rotated.
	 */
	private double mouseWheelAmount;

	/**
	 * Indicates if the mouse wheel was moved.
	 */
	private boolean mouseWheelMoved;

	/**
	 * Previous x cursor position
	 */
	private double previousX;

	/**
	 * Previous y cursor position
	 */
	private double previousY;



	/**
	 *  Previous pressed button
	 */
	private MouseButton previousButton = MouseButton.None;

	/**
	 * Previous Button State
	 */
	private boolean previousButtonState;
	private DoubleBuffer xBuffer;
	private DoubleBuffer yBuffer;

	public GLFWMouseInputProvider(Window window) {
		super(window);
		xBuffer = BufferUtils.createDoubleBuffer(1);
		yBuffer = BufferUtils.createDoubleBuffer(1);
	}

	@Override
	public void moveCursor(double x, double y) {
		glfwSetCursorPos(windowId, x, y);
	}

	@Override
	public void update() {
		MouseEvent event;
		while ((event = eventQueue.poll()) != null) {
			receiver.onMouseEvent(event);
		}
		eventQueue.clear();
	}

	@Override
	public void initialize() {
		glfwSetMouseButtonCallback(windowId, mouseButtonCallback);
		glfwSetCursorPosCallback(windowId, cursorPosCallback);
		glfwSetScrollCallback(windowId, scrollCallback);
		initialized = true;
	}

	@Override
	public void shutdown() {
		mouseButtonCallback.release();
		cursorPosCallback.release();
		scrollCallback.release();
		initialized = false;
	}

	@Override
	public Vector2d cursorPosition() {
		xBuffer.clear(); yBuffer.clear();
		glfwGetCursorPos(windowId, xBuffer, yBuffer);
		xBuffer.rewind(); yBuffer.rewind();

		return new Vector2d(xBuffer.get(), yBuffer.get());
	}

	private GLFWMouseButtonCallback mouseButtonCallback  = new GLFWMouseButtonCallback() {
		@Override
		public void invoke(long window, int button, int action, int mods) {
			MouseButton mouseButton = toMouseButton(button);
			previousButton = mouseButton;
			previousButtonState = action == GLFW_PRESS;
			eventQueue.add(new MouseEvent(mouseButton, previousButtonState, previousX, previousY, mouseWheelAmount));

			if (mouseWheelMoved) {
				mouseWheelAmount = 0.0f;
				mouseWheelMoved = false;
			}

			if (! previousButtonState) {
				previousButton = MouseButton.None;
			}
		}

	};

	private MouseButton toMouseButton(int button) {
		switch (button) {
			case GLFW_MOUSE_BUTTON_1: return MouseButton.Primary;
			case GLFW_MOUSE_BUTTON_2: return MouseButton.Second;
			case GLFW_MOUSE_BUTTON_3: return MouseButton.Middle;
		}
		return MouseButton.None;
	}


	private GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
		@Override
		public void invoke(long window, double xpos, double ypos) {
			previousX = xpos;
			previousY = ypos;
			eventQueue.add(new MouseEvent(previousButton, previousButtonState, previousX, previousY, mouseWheelAmount));
			if (mouseWheelMoved) {
				mouseWheelAmount = 0.0f;
				mouseWheelMoved = false;
			}
		}
	};

	private GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
		@Override
		public void invoke(long window, double xoffset, double yoffset) {
			mouseWheelAmount = yoffset;
			mouseWheelMoved = true;
		}
	};
}

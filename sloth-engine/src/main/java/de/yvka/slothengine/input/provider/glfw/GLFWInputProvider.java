package de.yvka.slothengine.input.provider.glfw;

import de.yvka.slothengine.input.event.InputEvent;
import de.yvka.slothengine.input.provider.BaseInputProvider;
import de.yvka.slothengine.window.Window;
import de.yvka.slothengine.input.provider.BaseInputProvider;


public class GLFWInputProvider<T extends InputEvent> extends BaseInputProvider<T> {
	/**
	 * Window id of the primary window
	 */
	protected long windowId;


	public GLFWInputProvider(Window window) {
		this(window.getWindowId());
	}

	public GLFWInputProvider(long windowId) {
		this.windowId = windowId;
	}

}

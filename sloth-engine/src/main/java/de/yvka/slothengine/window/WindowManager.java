package de.yvka.slothengine.window;

import de.yvka.slothengine.engine.AppSettings;
import de.yvka.slothengine.engine.Engine;
import de.yvka.slothengine.engine.EngineComponent;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Class for creation of Window objects.
 */
public class WindowManager implements EngineComponent {

	private static final Logger Log = LoggerFactory.getLogger(WindowManager.class);

	public static final int DEFAULT_WIDTH = 1024;
	public static final int DEFAULT_HEIGHT = 768;
	public static final String DEFAULT_TITLE = "EngineApp";

	private Map<Integer, Integer> windowHints = null;
	private List<Window> windows = null;
	private int height;
	private int width;
	private String title;
	private boolean initialized = false;

	@Override
	public void initialize() {
		Log.info("Initialize Window Manager");
		if (initialized) {
			throw new IllegalStateException("The WindowManager is already initialized");
		}
		windowHints = new HashMap<>();
		windows = new ArrayList<>();
		setWidth(DEFAULT_WIDTH);
		setHeight(DEFAULT_HEIGHT);
		setTitle(DEFAULT_TITLE);
		reset();

		// Creates a primary window which is required for rendering
		create(Engine.getSettings());
		Log.info("Create Primary window " + getPrimaryWindow());

		initialized = true;
	}

	@Override
	public void shutdown() {
		Log.info("Shutdown window manager");
		if (! initialized) {
			throw new IllegalStateException("The WindowManager is already initialized");
		}
		reset();
		Log.info("Destroy windows");
		for (Window window : windows) {
			window.destroy();
		}
		initialized = false;

	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	private WindowManager reset() {
		windowHints.clear();
		GLFW.glfwDefaultWindowHints();
		setWidth(DEFAULT_WIDTH).
		setHeight(DEFAULT_HEIGHT);
		return this;
	}

	public WindowManager setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
		return this;
	}

	public WindowManager setWidth(int width) {
		this.width = width;
		return this;
	}

	public WindowManager setHeight(int height) {
		this.height = height;
		return this;
	}

	public WindowManager setTitle(String title) {
		this.title = title;
		return this;
	}

	public WindowManager enableWindowAutoIconify(boolean enabled) {
		return setWindowHint(GLFW.GLFW_AUTO_ICONIFY, enabled);
	}

	public WindowManager enableWindowFloating(boolean enable) {
		return setWindowHint(GLFW.GLFW_FLOATING, enable);
	}

	public WindowManager setFocused(boolean focused) {
		return setWindowHint(GLFW.GLFW_FOCUSED, focused);
	}

	public WindowManager setResizeable(boolean resizeable) {
		return setWindowHint(GLFW.GLFW_RESIZABLE, resizeable);
	}


	public WindowManager setGLContextVersion(int major, int minor) {
		setWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, major);
		setWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, minor);
		return this;
	}

	public WindowManager setWindowHint(int target, int hint) {
	 	windowHints.put(target, hint);
		return this;
	}

	public WindowManager setWindowHint(int target, boolean hint) {
		windowHints.put(target, hint ? 1 : 0);
		return this;
	}

	public Window getPrimaryWindow() {
		if (windows.size() == 0) return null;
		return windows.get(0);
	}

	/**
	 * Creates a GLFW Window with the previous
	 *
	 * @return a new created window.
     */
	public Window build() {
		windowHints.forEach(GLFW::glfwWindowHint);

		long windowId = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
		if (windowId == NULL) {
			throw new WindowException("Failed to create a window");
		}

		Window window = new Window(windowId, title);
		windows.add(window);
		reset();

		return window;
	}


	/**
	 * Creates a window which is specified
	 * by a assigned AppSettings Object.
	 *
	 * @param settings the settings object which describes the new window.
	 * @return the new created disabled window.
     */
	public Window create(AppSettings settings) {
		 Window window =
			 setTitle(settings.getString(AppSettings.Title, DEFAULT_TITLE))
			.setResizeable(settings.getBoolean(AppSettings.Resizeable, true))
			.setGLContextVersion(
				settings.getInteger(AppSettings.GLMajorVersion, 3),
				settings.getInteger(AppSettings.GLMinorVersion, 0))
			.setSize(
				settings.getInteger(AppSettings.Width, DEFAULT_WIDTH),
				settings.getInteger(AppSettings.Height, DEFAULT_HEIGHT)
			).build().enable();

		if (settings.getBoolean(AppSettings.VSync, false)) {
			window.enableVsync();
		}

		return window;
	}

	@Override
	public void onFrameEnd() {
		for (Window window : windows) {
			if (window.isEnabled()) {
				window.onFrameEnd();
			}
		}
	}
}

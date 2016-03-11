package de.yvka.slothengine.input;

import de.yvka.slothengine.engine.Engine;
import de.yvka.slothengine.engine.EngineComponent;
import de.yvka.slothengine.input.event.InputEvent;
import de.yvka.slothengine.input.event.KeyEvent;
import de.yvka.slothengine.input.event.MouseEvent;
import de.yvka.slothengine.input.provider.KeyInputProvider;
import de.yvka.slothengine.input.provider.MouseInputProvider;
import de.yvka.slothengine.window.Window;
import de.yvka.slothengine.engine.Engine;
import de.yvka.slothengine.engine.EngineComponent;
import de.yvka.slothengine.input.event.InputEvent;
import de.yvka.slothengine.input.event.MouseEvent;
import de.yvka.slothengine.input.provider.KeyInputProvider;
import org.joml.Vector2d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static de.yvka.slothengine.input.provider.MouseInputProvider.MouseButton;

/**
 * Main entry point for input management like,
 * check key state, mouse states and so on.
 */
public class InputManager implements InputReceiver, EngineComponent {

	private static final Logger Log = LoggerFactory.getLogger(InputManager.class);

	private final MouseInputProvider mouseProvider;
	private final KeyInputProvider keyProvider;
	private final List<InputListener> inputListeners;
	private final Queue<InputEvent> eventQueue;

	private final BitSet mouseKeySet;
	private final Vector2d mousePosition;
	private double mouseWheelAmount;
	private boolean isInitialized;

	/**
	 * Constructor of the InputManager which specifies the concrete input
	 * provider implementations. This Constructor is for internal
	 * use only, the EngineContext is responsible to construct this InputManager.
	 *
	 * @param mouseProvider the mouse provider implementation
	 * @param keyProvider the keyboard provider implementation
	 */
	InputManager(MouseInputProvider mouseProvider, KeyInputProvider keyProvider) {
		this.mouseProvider = mouseProvider;
		this.keyProvider = keyProvider;
		this.inputListeners = new ArrayList<>();
		this.eventQueue = new ConcurrentLinkedQueue<>();

		this.mouseKeySet = new BitSet(0x3);
		this.mousePosition = new Vector2d();
		this.mouseWheelAmount = 0;
	}

	/**
	 * Initialize the input manager must be called
	 * before the input manager could be used.
	 */
	@Override
	public void initialize() {
		Log.info("Initialize InputManager with [" + mouseProvider.getClass().getName() + ", " + keyProvider.getClass().getName() + "]");
		mouseProvider.setInputReceiver(this);
		keyProvider.setInputReceiver(this);
		mouseProvider.initialize();
		keyProvider.initialize();
		isInitialized = true;
		centerMousePosition();

	}



	/**
	 * Clean up the resources of input manager.
	 */
	@Override
	public void shutdown() {
		Log.info("Shutdown InputManager with [" + mouseProvider.getClass().getName() + ", " + keyProvider.getClass().getName() + "]");
		mouseProvider.shutdown();
		keyProvider.shutdown();
		isInitialized = false;
	}

	@Override
	public boolean isInitialized() {
		return isInitialized;
	}

	/**
	 * Update the state of the input manager, which must
	 * be called one time per frame before all other onUpdate
	 * routines.
	 *
	 * @param elaspedTime the time that has passed since the last frame.
	 */
	@Override
	public void onUpdate(float elaspedTime) {
		mouseWheelAmount = 0.0f;
		mouseProvider.update();
		keyProvider.update();
		delegateEvents();
	}

	/**
	 * Delegates the events to registered input listeners.
	 */
	private void delegateEvents() {
		InputEvent event;
		while ((event = eventQueue.poll()) != null) {

			for(InputListener listener : inputListeners) {
				if (event.isConsumed()) continue;

				if (event instanceof KeyEvent) {
					listener.onKeyEvent((KeyEvent) event);
				} else if(event instanceof MouseEvent) {
					listener.onMouseEvent((MouseEvent) event);
				}
			}

		}
	}

	/**
	 * Add a input listener on which the input events will be delegated.
	 *
	 * @param listener the listener to add.
	 */
	public void addListener(InputListener listener) {
		if (! inputListeners.contains(listener)) {
			inputListeners.add(listener);
		}
	}

	/**
	 * Removes a input listener from the input manager.
	 *
	 * @param listener the to be removed listeners
	 */
	public void removeListener(InputListener listener) {
		if (inputListeners.contains(listener)) {
			inputListeners.remove(listener);
		}
	}

	/**
	 * Checks if a specified keyboard button is pressed or rather hold down.
	 *
	 * @param keyButton the specified keyboard button
	 * @return true if the specified mouse button is pressed or hold down.
	 */
	public boolean isKeyPressed(KeyInputProvider.KeyButton keyButton) {
		return keyProvider.isKeyPressed(keyButton);
	}

	/**
	 * Invoked on a mouse event is provided.
	 *
	 * @param event provided mouse event
	 */
	@Override
	public void onMouseEvent(MouseEvent event) {
		mousePosition.set(event.getX(), event.getY());
		mouseWheelAmount = event.getMouseWheelAmount();
		MouseButton button = event.getButton();

		if (button != MouseButton.None) {
			if (event.isPressed()) {
				if (! mouseKeySet.get(button.ordinal())) {
					mouseKeySet.set(button.ordinal());
				}
			} else {
				mouseKeySet.clear(button.ordinal());
			}
		}

		eventQueue.add(event);
	}

	/**
	 * Invoked on a key event is provided
	 *
	 * @param keyEvent provided key event
	 */
	@Override
	public void onKeyEvent(KeyEvent keyEvent) {
		eventQueue.add(keyEvent);
	}

	/**
	 * Set the position of the mouse cursor.
	 *
	 * @param x the x position relative to the viewport
	 * @param y the y position relative to the viewport
	 */
	public void setMousePosition(int x, int y) {
		mousePosition.set(x, y);
		mouseProvider.moveCursor(x, y);
	}

	/**
	 * Returns the current mouse position relative to the viewport.
	 *
	 * @return the mouse position.
	 */
	public Vector2d getMousePosition() {
		return mousePosition;
	}

	/**
	 * Retrieves the latest position of the cursor
	 * from the underlying mouse provider.
	 *
	 * @return the latest mouse position
     */
	public Vector2d asyncCursorPosition() {
		return mouseProvider.cursorPosition();
	}

	/**
	 * The current amount of mouse wheel rotation amount.
	 *
	 * @return the rotation amount.
	 */
	public double getMouseWheelAmount() {
		return mouseWheelAmount;
	}

	/**
	 * Checks if a specified mouse button is pressed or rather hold down.
	 *
	 * @param mouseButton the specified mouse button
	 * @return true if the specified mouse button is pressed or hold down.
	 */
	public boolean isMouseButtonPressed(MouseButton mouseButton) {
		return mouseKeySet.get(mouseButton.ordinal());
	}

	/**
	 * Center the mouse cursor
	 */
	public void centerMousePosition() {
		Window window = Engine.getPrimaryWindow();
		setMousePosition(window.getWidth() >> 1, window.getHeight() >> 1);
	}
}

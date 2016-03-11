package de.yvka.slothengine.input.provider.javafx;

import de.yvka.slothengine.input.provider.KeyInputProvider;
import de.yvka.slothengine.input.event.KeyEvent;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.util.BitSet;


/**
 * A JavaFXKeyProvider retrieves key events from a specified node
 * object and delegate it to registered receivers.
 *
 */
public class JavaFXKeyProvider
		extends JavaFXInputProvider<KeyEvent,javafx.scene.input.KeyEvent>
		implements KeyInputProvider {

	/**
	 * Stores the state of a key as bit per key.
	 * A enabled bit (=1) means that the key was already
	 * pressed.
	 */
	private final BitSet keystate;

	/**
	 * Creates a JavaFXKeyProvider.
	 *
	 * @param node the node which acts as source for mouse events.
	 */
	public JavaFXKeyProvider(Node node) {
		super(node);
		keystate = new BitSet(KeyButton.values().length);
	}

	/**
	 * initializes the native side of the input provider.
	 */
	@Override
	public void initialize() {
		if (!initialized) {
			Platform.runLater(() -> {
				source.addEventHandler(javafx.scene.input.KeyEvent.ANY, this);
				source.addEventHandler(MouseEvent.MOUSE_ENTERED, this::requestFocus);
			});
			initialized = true;
		}
	}

	/**
	 * Destroy the native handler.
	 */
	@Override
	public void shutdown() {
		if (initialized) {
			Platform.runLater(() -> {
				source.removeEventHandler(javafx.scene.input.KeyEvent.ANY, this);
				source.removeEventHandler(MouseEvent.MOUSE_ENTERED, this::requestFocus);
			});
			synchronized (eventQueue) {
				eventQueue.clear();
			}
			initialized = false;
		}
	}

	/**
	 * Queries the input state from the native devices
	 * and delegate it to a InputHandler.
	 */
	@Override
	public void update() {
		KeyEvent event;
		synchronized (eventQueue) {
			while ((event = eventQueue.poll()) != null) {
				receiver.onKeyEvent(event);
			}
			eventQueue.clear();
		}
	}

	/**
	 * Invoked when a specific event of the type for which this handler is
	 * registered happens.
	 *
	 * @param event the event which occurred
	 */
	@Override
	public void handle(javafx.scene.input.KeyEvent event) {
		// We are only interested in keyButton press and release events
		if (javafx.scene.input.KeyEvent.KEY_TYPED.getName().equals(event.getEventType().getName())) return;

		KeyButton keyButton = convertJavaFxKeyCodeToKey(event.getCode());
		boolean pressed = javafx.scene.input.KeyEvent.KEY_PRESSED.equals(event.getEventType());

		if (pressed) {
			keystate.set(keyButton.ordinal());
		} else {
			keystate.clear(keyButton.ordinal());
		}

		KeyEvent keyEvent = new KeyEvent(keyButton, pressed, event.isMetaDown(), event.isAltDown(), event.isShiftDown(), event.isControlDown());
		if (keyEvent.getKeyButton() != KeyButton.None) {
			synchronized (eventQueue) {
				eventQueue.add(keyEvent);
			}
		}
		event.consume();
	}

	/**
	 * Handles a Node entered event and request it's focus.
	 * In order to capture events from a node this node needs the focus.
	 *
	 * @param event
	 */
	private void requestFocus(MouseEvent event) {
		source.requestFocus();
		event.consume();
	}

	private KeyButton convertJavaFxKeyCodeToKey(KeyCode code) {
		switch (code) {
			case ENTER:
				return KeyButton.Enter;
			case BACK_SPACE:
				return KeyButton.BackSpace;
			case TAB:
				return KeyButton.Tab;
			case SHIFT:
				return KeyButton.Shift;
			case CONTROL:
				return KeyButton.Ctrl;
			case ALT:
				return KeyButton.Alt;
			case PAUSE:
				return KeyButton.Pause;
			case CAPS:
				return KeyButton.CapsLock;
			case ESCAPE:
				return KeyButton.Esc;
			case SPACE:
				return KeyButton.Space;
			case PAGE_UP:
				return KeyButton.PageUp;
			case PAGE_DOWN:
				return KeyButton.PageDown;
			case END:
				return KeyButton.End;
			case HOME:
				return KeyButton.Home;
			case LEFT:
				return KeyButton.Left;
			case UP:
				return KeyButton.Up;
			case RIGHT:
				return KeyButton.Right;
			case DOWN:
				return KeyButton.Down;
			case COMMA:
				return KeyButton.Comma;
			case MINUS:
				return KeyButton.Minus;
			case PERIOD:
				return KeyButton.Point;
			case SLASH:
				return KeyButton.Slash;
			case DIGIT0:
				return KeyButton.Digit0;
			case DIGIT1:
				return KeyButton.Digit1;
			case DIGIT2:
				return KeyButton.Digit2;
			case DIGIT3:
				return KeyButton.Digit3;
			case DIGIT4:
				return KeyButton.Digit4;
			case DIGIT5:
				return KeyButton.Digit5;
			case DIGIT6:
				return KeyButton.Digit6;
			case DIGIT7:
				return KeyButton.Digit7;
			case DIGIT8:
				return KeyButton.Digit8;
			case DIGIT9:
				return KeyButton.Digit9;
			case SEMICOLON:
				return KeyButton.Semicolon;
			case EQUALS:
				return KeyButton.Equals;
			case A:
				return KeyButton.A;
			case B:
				return KeyButton.B;
			case C:
				return KeyButton.C;
			case D:
				return KeyButton.D;
			case E:
				return KeyButton.E;
			case F:
				return KeyButton.F;
			case G:
				return KeyButton.G;
			case H:
				return KeyButton.H;
			case I:
				return KeyButton.I;
			case J:
				return KeyButton.J;
			case K:
				return KeyButton.K;
			case L:
				return KeyButton.L;
			case M:
				return KeyButton.M;
			case N:
				return KeyButton.N;
			case O:
				return KeyButton.O;
			case P:
				return KeyButton.P;
			case Q:
				return KeyButton.Q;
			case R:
				return KeyButton.R;
			case S:
				return KeyButton.S;
			case T:
				return KeyButton.T;
			case U:
				return KeyButton.U;
			case V:
				return KeyButton.V;
			case W:
				return KeyButton.W;
			case X:
				return KeyButton.X;
			case Y:
				return KeyButton.Y;
			case Z:
				return KeyButton.Z;
			case OPEN_BRACKET:
				return KeyButton.OpenBracket;
			case BACK_SLASH:
				return KeyButton.BackSlash;
			case CLOSE_BRACKET:
				return KeyButton.ClosedBracket;
			case NUMPAD0:
				return KeyButton.NumPad_0;
			case NUMPAD1:
				return KeyButton.NumPad_1;
			case NUMPAD2:
				return KeyButton.NumPad_2;
			case NUMPAD3:
				return KeyButton.NumPad_3;
			case NUMPAD4:
				return KeyButton.NumPad_4;
			case NUMPAD5:
				return KeyButton.NumPad_5;
			case NUMPAD6:
				return KeyButton.NumPad_6;
			case NUMPAD7:
				return KeyButton.NumPad_7;
			case NUMPAD8:
				return KeyButton.NumPad_8;
			case NUMPAD9:
				return KeyButton.NumPad_9;
			case MULTIPLY:
				return KeyButton.NumPad_Multiply;
			case ADD:
				return KeyButton.NumPad_Plus;
			case SUBTRACT:
				return KeyButton.NumPad_Minus;
			case DECIMAL:
				return KeyButton.NumPad_Comma;
			case DIVIDE:
				return KeyButton.NumPad_Divide;
			case DELETE:
				return KeyButton.Delete;
			case NUM_LOCK:
				return KeyButton.NumLock;
			case SCROLL_LOCK:
				return KeyButton.SCROLL_LOCK;
			case F1:
				return KeyButton.F1;
			case F2:
				return KeyButton.F2;
			case F3:
				return KeyButton.F3;
			case F4:
				return KeyButton.F4;
			case F5:
				return KeyButton.F5;
			case F6:
				return KeyButton.F6;
			case F7:
				return KeyButton.F7;
			case F8:
				return KeyButton.F8;
			case F9:
				return KeyButton.F9;
			case F10:
				return KeyButton.F10;
			case F11:
				return KeyButton.F11;
			case F12:
				return KeyButton.F12;
			case F13:
				return KeyButton.F13;
			case F14:
				return KeyButton.F14;
			case F15:
				return KeyButton.F15;
			case F16:
				return KeyButton.F16;
			case F17:
				return KeyButton.F17;
			case F18:
				return KeyButton.F18;
			case F19:
				return KeyButton.F19;
			case F20:
				return KeyButton.F20;
			case F21:
				return KeyButton.F21;
			case F22:
				return KeyButton.F22;
			case F23:
				return KeyButton.F23;
			case F24:
				return KeyButton.F24;
			case PRINTSCREEN:
				return KeyButton.Print;
			case INSERT:
				return KeyButton.Insert;
			case META:
			case WINDOWS:
			case COMMAND:
				return KeyButton.Meta;
			case BACK_QUOTE:
				return KeyButton.GraveAccent;
			case QUOTE:
				return KeyButton.Apostrophe;
			case ALT_GRAPH:
				return KeyButton.AltGraph;
		}

		return KeyButton.None;
	}

	/**
	 * Determines if the specified keyButton is pressed down.
	 * The method must be implemented thread safe.
	 *
	 * @param keyButton the specified keyButton.
	 * @return true when the keyButton is actually pressed otherwise false if the keyButton is released.
	 */
	@Override
	public boolean isKeyPressed(KeyButton keyButton) {
		// BitSet ensures that it is thread safe :-)
		return keystate.get(keyButton.ordinal());
	}
}

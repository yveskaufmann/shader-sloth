package de.yvka.slothengine.input.provider.glfw;

import de.yvka.slothengine.input.event.KeyEvent;
import de.yvka.slothengine.input.provider.KeyInputProvider;
import de.yvka.slothengine.window.Window;
import de.yvka.slothengine.input.provider.KeyInputProvider;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.BitSet;

import static org.lwjgl.glfw.GLFW.*;

public class GLFWKeyInputProvider extends GLFWInputProvider<KeyEvent>  implements KeyInputProvider {

	private BitSet keyState;

	public GLFWKeyInputProvider(Window window) {
		super(window);
	}

	@Override
	public void initialize() {
		keyState = new BitSet(KeyButton.values().length);
		glfwSetKeyCallback(windowId, keyCallback);
		initialized = true;
	}


	@Override
	public void shutdown() {
		keyState.clear();
		keyCallback.release();
		initialized = false;
	}

	@Override
	public boolean isKeyPressed(KeyButton keyButton) {
		return keyState.get(keyButton.ordinal());
	}

	@Override
	public void update() {
		KeyEvent event;
		while ((event = eventQueue.poll()) != null) {
			receiver.onKeyEvent(event);
		}
		eventQueue.clear();
	}

	private GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			KeyButton keyButton = convertToKeyEvent(key, scancode, action, mods);

			// Lets only consider press and release events
			if (keyButton == null) return;

			boolean isKeyPressEvent = action == GLFW_PRESS || action == GLFW_REPEAT;

			KeyEvent keyEvent = new KeyEvent(
				keyButton,
				isKeyPressEvent,
				(mods & GLFW_MOD_SUPER) > 0,
				(mods & GLFW_MOD_SHIFT) > 0,
				(mods & GLFW_MOD_ALT) > 0,
				(mods & GLFW_MOD_CONTROL) > 0
			);

			if (isKeyPressEvent) {
				keyState.set(keyButton.ordinal());
			} else {
				keyState.clear(keyButton.ordinal());
			}

			eventQueue.add(keyEvent);
		}

		private KeyButton convertToKeyEvent(int key, int scancode, int action, int mods) {
			switch (key) {
				case GLFW_KEY_SPACE: return KeyButton.Space;
				case GLFW_KEY_APOSTROPHE: return KeyButton.Apostrophe;
				case GLFW_KEY_COMMA: return KeyButton.Comma;
				case GLFW_KEY_MINUS: return KeyButton.Minus;
				case GLFW_KEY_PERIOD: return KeyButton.Point;
				case GLFW_KEY_SLASH: return KeyButton.Slash;
				case GLFW_KEY_0: return KeyButton.Digit0;
				case GLFW_KEY_1: return KeyButton.Digit1;
				case GLFW_KEY_2: return KeyButton.Digit2;
				case GLFW_KEY_3: return KeyButton.Digit3;
				case GLFW_KEY_4: return KeyButton.Digit4;
				case GLFW_KEY_5: return KeyButton.Digit5;
				case GLFW_KEY_6: return KeyButton.Digit6;
				case GLFW_KEY_7: return KeyButton.Digit7;
				case GLFW_KEY_8: return KeyButton.Digit8;
				case GLFW_KEY_9: return KeyButton.Digit9;
				case GLFW_KEY_SEMICOLON: return KeyButton.Semicolon;
				case GLFW_KEY_EQUAL: return KeyButton.Equals;
				case GLFW_KEY_A: return KeyButton.A;
				case GLFW_KEY_B: return KeyButton.B;
				case GLFW_KEY_C: return KeyButton.C;
				case GLFW_KEY_D: return KeyButton.D;
				case GLFW_KEY_E: return KeyButton.E;
				case GLFW_KEY_F: return KeyButton.F;
				case GLFW_KEY_G: return KeyButton.G;
				case GLFW_KEY_H: return KeyButton.H;
				case GLFW_KEY_I: return KeyButton.I;
				case GLFW_KEY_J: return KeyButton.J;
				case GLFW_KEY_K: return KeyButton.K;
				case GLFW_KEY_L: return KeyButton.L;
				case GLFW_KEY_M: return KeyButton.M;
				case GLFW_KEY_N: return KeyButton.N;
				case GLFW_KEY_O: return KeyButton.O;
				case GLFW_KEY_P: return KeyButton.P;
				case GLFW_KEY_Q: return KeyButton.Q;
				case GLFW_KEY_R: return KeyButton.R;
				case GLFW_KEY_S: return KeyButton.S;
				case GLFW_KEY_T: return KeyButton.T;
				case GLFW_KEY_U: return KeyButton.U;
				case GLFW_KEY_V: return KeyButton.V;
				case GLFW_KEY_W: return KeyButton.W;
				case GLFW_KEY_X: return KeyButton.X;
				case GLFW_KEY_Y: return KeyButton.Y;
				case GLFW_KEY_Z: return KeyButton.Z;
				case GLFW_KEY_LEFT_BRACKET: return KeyButton.OpenBracket;
				case GLFW_KEY_BACKSLASH: return KeyButton.BackSlash;
				case GLFW_KEY_RIGHT_BRACKET: return KeyButton.ClosedBracket;
				case GLFW_KEY_GRAVE_ACCENT: return KeyButton.GraveAccent;
				case GLFW_KEY_ESCAPE: return KeyButton.Esc;
				case GLFW_KEY_ENTER: return KeyButton.Enter;
				case GLFW_KEY_TAB: return KeyButton.Tab;
				case GLFW_KEY_BACKSPACE: return KeyButton.BackSpace;
				case GLFW_KEY_INSERT: return KeyButton.Insert;
				case GLFW_KEY_DELETE: return KeyButton.Delete;
				case GLFW_KEY_RIGHT: return KeyButton.Right;
				case GLFW_KEY_LEFT: return KeyButton.Left;
				case GLFW_KEY_DOWN: return KeyButton.Down;
				case GLFW_KEY_UP: return KeyButton.Up;
				case GLFW_KEY_PAGE_UP: return KeyButton.PageUp;
				case GLFW_KEY_PAGE_DOWN: return KeyButton.PageDown;
				case GLFW_KEY_HOME: return KeyButton.Home;
				case GLFW_KEY_END: return KeyButton.End;
				case GLFW_KEY_CAPS_LOCK: return KeyButton.CapsLock;
				case GLFW_KEY_SCROLL_LOCK: return KeyButton.SCROLL_LOCK;
				case GLFW_KEY_NUM_LOCK: return KeyButton.NumLock;
				case GLFW_KEY_PRINT_SCREEN: return KeyButton.Print;
				case GLFW_KEY_PAUSE: return KeyButton.Pause;
				case GLFW_KEY_F1: return KeyButton.F1;
				case GLFW_KEY_F2: return KeyButton.F2;
				case GLFW_KEY_F3: return KeyButton.F3;
				case GLFW_KEY_F4: return KeyButton.F4;
				case GLFW_KEY_F5: return KeyButton.F5;
				case GLFW_KEY_F6: return KeyButton.F6;
				case GLFW_KEY_F7: return KeyButton.F7;
				case GLFW_KEY_F8: return KeyButton.F8;
				case GLFW_KEY_F9: return KeyButton.F9;
				case GLFW_KEY_F10: return KeyButton.F10;
				case GLFW_KEY_F11: return KeyButton.F11;
				case GLFW_KEY_F12: return KeyButton.F12;
				case GLFW_KEY_F13: return KeyButton.F13;
				case GLFW_KEY_F14: return KeyButton.F14;
				case GLFW_KEY_F15: return KeyButton.F15;
				case GLFW_KEY_F16: return KeyButton.F16;
				case GLFW_KEY_F17: return KeyButton.F17;
				case GLFW_KEY_F18: return KeyButton.F18;
				case GLFW_KEY_F19: return KeyButton.F19;
				case GLFW_KEY_F20: return KeyButton.F20;
				case GLFW_KEY_F21: return KeyButton.F21;
				case GLFW_KEY_F22: return KeyButton.F22;
				case GLFW_KEY_F23: return KeyButton.F23;
				case GLFW_KEY_F24: return KeyButton.F24;
				case GLFW_KEY_F25: return KeyButton.F25;
				case GLFW_KEY_KP_0: return KeyButton.NumPad_0;
				case GLFW_KEY_KP_1: return KeyButton.NumPad_1;
				case GLFW_KEY_KP_2: return KeyButton.NumPad_2;
				case GLFW_KEY_KP_3: return KeyButton.NumPad_3;
				case GLFW_KEY_KP_4: return KeyButton.NumPad_4;
				case GLFW_KEY_KP_5: return KeyButton.NumPad_5;
				case GLFW_KEY_KP_6: return KeyButton.NumPad_6;
				case GLFW_KEY_KP_7: return KeyButton.NumPad_7;
				case GLFW_KEY_KP_8: return KeyButton.NumPad_8;
				case GLFW_KEY_KP_9: return KeyButton.NumPad_9;
				case GLFW_KEY_KP_DECIMAL: return KeyButton.Comma;
				case GLFW_KEY_KP_DIVIDE: return KeyButton.NumPad_Divide;
				case GLFW_KEY_KP_MULTIPLY: return KeyButton.NumPad_Multiply;
				case GLFW_KEY_KP_SUBTRACT: return KeyButton.NumPad_Minus;
				case GLFW_KEY_KP_ADD: return KeyButton.NumPad_Plus;
				case GLFW_KEY_KP_ENTER: return KeyButton.NumPad_Enter;
				case GLFW_KEY_RIGHT_SHIFT:
				case GLFW_KEY_LEFT_SHIFT: return KeyButton.Shift;
				case GLFW_KEY_RIGHT_CONTROL:
				case GLFW_KEY_LEFT_CONTROL: return KeyButton.Ctrl;
				case GLFW_KEY_RIGHT_ALT:
				case GLFW_KEY_LEFT_ALT: return KeyButton.Alt;
				case GLFW_KEY_RIGHT_SUPER:
				case GLFW_KEY_LEFT_SUPER: return KeyButton.Meta;
			}
			return null;
		}
	};
}

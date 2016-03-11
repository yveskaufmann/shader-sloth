package de.yvka.slothengine.input.event;

import de.yvka.slothengine.input.provider.KeyInputProvider;
import de.yvka.slothengine.input.provider.KeyInputProvider;

/**
 * A KeyButton event which describes the event for keyButton
 * presses and keyButton releases.
 */
public class KeyEvent extends InputEvent {

	public static final int MODIFIER_NONE = 0x0;
	public static final int MODIFIER_SHIFT = 0x1;
	public static final int MODIFIER_ALT = 0x2;
	public static final int MODIFIER_META = 0x4;

	/**
	 * The keyButton which was pressed or released
	 */
	private KeyInputProvider.KeyButton keyButton;

	/**
	 * Flag which indicates that the keyButton was pressed or
	 * released.
	 */
	private boolean pressed;

	/**
	 * Flag which indicates if a meta keyButton was hold down while a keyButton was pressed or released.
	 */
	private boolean metakey;

	/**
	 * Flag which indicates if a alt keyButton was hold down while a keyButton was pressed or released.
	 */
	private boolean altkey;

	/**
	 * Flag which indicates if a shift keyButton was hold down while a keyButton was pressed or released.
	 */
	private boolean shiftkey;

	/**
	 * Flag which indicates if a ctrl keyButton was hold down while a keyButton was pressed or released.
	 */
	private boolean ctrlKey;

	/**
	 * Creates a keyButton event.
	 *
	 * @param keyButton
	 * 			the keyButton which triggered this event
	 * @param pressed
	 * 			indicator if the keyButton was pressed or released
	 * @param modifiers
	 * 			the modifiers which were pressed simultaneously
	 * 			with the keyButton.
	 */
	public KeyEvent(KeyInputProvider.KeyButton keyButton, boolean pressed, int modifiers) {
		this.keyButton = keyButton;
		this.pressed = pressed;
		this.metakey = (modifiers & MODIFIER_META) == MODIFIER_META;
		this.altkey = (modifiers & MODIFIER_ALT) == MODIFIER_ALT;
		this.shiftkey = (modifiers & MODIFIER_SHIFT) == MODIFIER_SHIFT;
	}

	/**
	 * Creates a keyButton event.
	 *
	 * @param keyButton
	 * 			the keyButton which triggered this event
	 * @param pressed
	 * 			indicator if the keyButton was pressed or released
	 * @param metakey
	 * 			Indicator if a meta keyButton was pressed
	 * 			while this event was triggered.
	 * @param altkey
	 * 			Indicator if a alt keyButton was pressed
	 * 			while this event was triggered.
	 * @param shiftkey
	 * 			Indicator if a shift keyButton was pressed
	 * 			while this event was triggered.
	 */
	public KeyEvent(KeyInputProvider.KeyButton keyButton, boolean pressed, boolean metakey, boolean altkey, boolean shiftkey, boolean ctrlKey) {
		this.keyButton = keyButton;
		this.pressed = pressed;
		this.metakey = metakey;
		this.altkey = altkey;
		this.shiftkey = shiftkey;
		this.ctrlKey = ctrlKey;
	}

	/**
	 * Returns the pressed or released keyButton.
	 *
	 * @return the keyButton which triggered this event.
	 */
	public KeyInputProvider.KeyButton getKeyButton() {
		return keyButton;
	}

	/**
	 * Indicates if this event was triggered through
	 * a released or pressed keyButton.
	 *
	 * @return true if the keyButton was pressed.
	 */
	public boolean isPressed() {
		return pressed;
	}

	/**
	 * Indicates if a keyButton and a meta keyButton was pressed
	 * simultaneously.
	 *
	 * @return true if the keyButton and meta keyButton was pressed
	 * simultaneously.
	 */
	public boolean isMetakeyPressed() {
		return metakey;
	}

	/**
	 * Indicates if a keyButton and a alt keyButton was pressed
	 * simultaneously.
	 *
	 * @return true if the keyButton and alt keyButton was pressed
	 * simultaneously.
	 */
	public boolean isAltkeyPressed() {
		return altkey;
	}

	/**
	 * Indicates if a keyButton and a shift keyButton was pressed
	 * simultaneously.
	 *
	 * @return true if the keyButton and shift keyButton was pressed
	 * simultaneously.
	 */
	public boolean isShiftkeyPressed() {
		return shiftkey;
	}

	/**
	 * Indicates if a keyButton and a ctrl keyButton was pressed
	 * simultaneously.
	 *
	 * @return true if the keyButton and ctrl keyButton was pressed
	 * simultaneously.
	 */
	public boolean isControlkeyPressed() {
		return ctrlKey;
	}

	@Override
	public String toString() {
		return "KeyEvent{" +
				"keyButton=" + keyButton +
				", pressed=" + pressed +
				", metakey=" + metakey +
				", altkey=" + altkey +
				", shiftkey=" + shiftkey +
				", ctrlkey=" + ctrlKey +
				'}';
	}
}

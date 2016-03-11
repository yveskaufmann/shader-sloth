package de.yvka.slothengine.input.event;

/**
 * Base class for all input events.
 */
public abstract class InputEvent {

	protected boolean isConsumed = false;

	/**
	 * @return true if this event was already consumed and should be ignored.
	 */
	public boolean isConsumed() {
		return isConsumed;
	}

	/**
	 * Consumes this event which means that this event is marked as
	 * already consumed.
	 */
	public void consume() {
		isConsumed = true;
	}
}

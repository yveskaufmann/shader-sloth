package de.yvka.slothengine.input.provider;

import de.yvka.slothengine.input.InputReceiver;
import de.yvka.slothengine.input.event.InputEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BaseInputProvider<T extends InputEvent> {
	/**
	 * Event queue
	 */
	protected final Queue<T> eventQueue;
	/**
	 * Indicates if this provider was already initialized
	 */
	protected boolean initialized = false;
	/**
	 * A receiver which receives the retrieved input events.
	 */
	protected InputReceiver receiver;

	public BaseInputProvider() {
		eventQueue = new ConcurrentLinkedQueue<>();
		initialized = false;
	}

	/**
	 * Retrieves if the InputProver was already initialized.
	 *
	 * @return true if already initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Add a input provided handler which should receive
	 * the input events.
	 *
	 * @param inputReceiver which receives the input events of this provider.
	 */
	public void setInputReceiver(InputReceiver inputReceiver) {
		receiver = inputReceiver;
	}
}

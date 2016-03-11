package de.yvka.slothengine.engine;

import java.util.HashMap;
import java.util.Map;

/**
 * Interface for a engine component.
 * A component has the following life cycle:
 *  <code><pre>
 *       ____________
 *      | initialize |
 *       -----|------
 *            |
 *            +<----------------------+
 *       _____|________               |
 *      | onFrameStart |              |
 *       -----|--------               |
 *       _____|________               |
 *      | onUpdate     |              |
 *       -----|--------               |
 *       _____|_________              |
 *      | onBeforeRender|             |
 *       -----|---------              |
 *       _____|________               |
 *      | Render       |              |
 *       -----|--------               |
 *       _____|_________              |
 *      | onAfterRender |             |
 *       -----|---------              |
 *       _____|______                 |
 *      | onFrameEnd |                |
 *       -----|------                 |
 *            |                       |
 *            |     Loop = true       |
 *            +-----------------------+
 *            |
 *       ____________
 *      |  shutdown  |
 *       -----|------
 *
 *  </pre></code>
 *
 *
 */
public interface EngineComponent {

	/**
	 * Initialize this component must
	 * be called before the component could be used.
	 */
	void initialize();

	/**
	 * Shutdown this component and
	 * free all hold resources.
	 * Must be called before this component is removed from the heap.
	 */
	void shutdown();

	/**
	 * Retrieves if the Component was already initialized.
	 *
	 * @return true if already initialized
	 */
	boolean isInitialized();

	/**
	 * Will be called before a new Frame is started
	 */
	default void onFrameStart() {}

	/**
	 * TODO: document me
	 * @param elapsedTime
     */
	default void onUpdate(float elapsedTime) {}

	/**
	 * TODO: documemt me
	 * @param elapsedTime
     */
	default void onBeforeRender(float elapsedTime) {}

	/**
	 * TODO: document me
	 * @param elapsedTime
     */
	default void onRender(float elapsedTime) {}

	/**
	 * TODO: document me
	 * @param elapsedTime
     */
	default void onAfterRender(float elapsedTime) {}

	/**
	 * Will be called after a Frame is complete.
	 */
	default void onFrameEnd() {}
}

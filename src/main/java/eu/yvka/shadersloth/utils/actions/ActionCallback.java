package eu.yvka.shadersloth.utils.actions;

/**
 * A Callback which can be assigned to action
 * in order to specify the action which should
 * be performed when a action is activated.
 */
@FunctionalInterface
public interface ActionCallback {
	/**
	 * Invoke the underlying callback and
	 * execute the action.
	 */
	void invoke(ActionEntry actionEntry);
}

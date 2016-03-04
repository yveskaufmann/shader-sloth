package de.yvka.shadersloth.controls.number;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.TwoLevelFocusBehavior;

public class NumberInputBehaviour extends BehaviorBase<NumberInput>{

	private TwoLevelFocusBehavior tlFocus;

	/**
	 * Create a new BehaviorBase for the given control. The Control must not
	 * be null.
	 *
	 * @param control     The control. Must not be null.
	 */
	public NumberInputBehaviour(NumberInput control) {

		super(control, TRAVERSAL_BINDINGS);

		if (com.sun.javafx.scene.control.skin.Utils.isTwoLevelFocus()) {
			tlFocus = new TwoLevelFocusBehavior(control); // needs to be last.
		}
	}

	@Override public void dispose() {
		if (tlFocus != null) tlFocus.dispose();
		super.dispose();
	}

}

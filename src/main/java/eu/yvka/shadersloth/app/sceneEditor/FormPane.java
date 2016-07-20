package eu.yvka.shadersloth.app.sceneEditor;

import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Created by fxdapokalypse on 20.07.16.
 */
public class FormPane extends GridPane {

	public void addFormEntry(int col, int row, String labelText, Control...controls) {
		Label label = new Label(labelText);
		label.setAlignment(Pos.BASELINE_LEFT);
		add(label, col, row);
		col = col + 1;
		for (int i = 0; i < controls.length; i++) {
			add(controls[i], col + i, row);
		}
	}
}

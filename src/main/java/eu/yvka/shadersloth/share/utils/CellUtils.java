package eu.yvka.shadersloth.share.utils;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

/**
 * An convenient helper for implementing
 * cells.
 */
public class CellUtils {

	/***************************************************************************
	 *                                                                         *
	 * TextField Helpers                                                       *
	 *                                                                         *
	 ***************************************************************************/

	public static <T> void updateItem(final Cell<T> cell,
									  final StringConverter<T> converter,
									  final TextField textField) {
		updateItem(cell, converter, null, null, textField);
	}


	public static <T> void updateItem(final Cell<T> cell,
									  final StringConverter<T> converter,
									  final HBox hbox,
									  final Node graphic,
									  final TextField textField) {

		if (cell.isEmpty()) {
			cell.setGraphic(null);
			cell.setText(null);
		} else {
			if (cell.isEditing()) {
				if (textField != null) {
					textField.setText(getItemText(cell, converter));
				}
				cell.setText(null);

				if (graphic != null) {
					hbox.getChildren().setAll(graphic, textField);
					cell.setGraphic(hbox);
				} else {
					cell.setGraphic(textField);
				}
			} else {
				cell.setText(getItemText(cell, converter));
				cell.setGraphic(graphic);
			}
		}

	}

	public static <T> TextField createTextField(Cell<T> cell, StringConverter<T> converter) {
		final TextField textField = new TextField(converter.toString(cell.getItem()));
		textField.setOnAction((event) -> {
			if (converter == null) {
				throw new IllegalStateException(
					"Attempting to convert text input into Object, but provided "
						+ "StringConverter is null. Be sure to set a StringConverter "
						+ "in your cell factory.");
			}
			cell.commitEdit(converter.fromString(textField.getText()));
			event.consume();
		});

		textField.setOnKeyReleased((event) ->  {
			if (KeyCode.ESCAPE.equals(event.getCode())) {
				cell.cancelEdit();
			}
			event.consume();
		});

		return textField;
	}

	public static <T> void startEdit(final Cell<T> cell, final StringConverter<T> converter, TextField textField, final HBox hBox, Node graphic) {
		if (textField != null) {
			textField.setText(getItemText(cell, converter));
		}

		cell.setText(null);

		if (graphic != null) {
			hBox.getChildren().addAll(graphic, textField);
			cell.setGraphic(hBox);
		} else {
			cell.setGraphic(textField);
		}

		if (textField != null) {
			textField.requestFocus();
			textField.selectAll();
		}
	}

	public static <T> void cancelEdit(Cell<T> cell, final StringConverter<T> converter, Node graphic) {
		cell.setText(getItemText(cell, converter));
		cell.setGraphic(graphic);
	}

	public static <T> String getItemText(Cell<T> cell, StringConverter<T> converter) {
		if (converter == null) {
			return cell.getItem() == null ? "" : cell.getItem().toString();
		}
		return converter.toString(cell.getItem());
	}

}

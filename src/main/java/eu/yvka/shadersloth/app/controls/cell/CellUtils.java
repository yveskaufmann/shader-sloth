package eu.yvka.shadersloth.app.controls.cell;

import javafx.scene.control.Cell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;

import java.util.Objects;

/**
 * @Author Yves Kaufmann
 * @since 11.07.2016
 */
public class CellUtils {

	/******************************************************************************
	 *
	 * String Converters
	 *
	 ******************************************************************************/

	public static final StringConverter<?> DEFAULT_STRING_CONVERTER = new StringConverter<Object>() {
		@Override
		public String toString(Object object) {
			return object == null ? "" : object.toString();
		}

		@Override
		public Object fromString(String string) {
			return (Object) string;
		}
	};

	/******************************************************************************
	 *
	 * Convenient Cell Methods
	 *
	 ******************************************************************************/

	/**
	 * Returns the string representation of the passed cell,
	 * a string converter is used to transform a item to a string.
	 * When no converter is specified objects will to convert to strings
	 * by calling its {#link Object{@link #toString()}} method.
	 *
	 * @param cell the cell from which the string should be obtained.
	 * @param converter a converter which is capable to convert a cell item to a string
	 * @param <T> the item type which is stored by a cell
     *
	 * @return the string representation of the passed cell
     */
	public static <T> String getStringFromCell(Cell<T> cell, StringConverter<T> converter) {
		return converter == null ? Objects.toString(cell.getItem()) : converter.toString(cell.getItem());
	}



	/******************************************************************************
	 *
	 * Textfield Cell Support
	 *
	 ******************************************************************************/


	public static <T> TextField createTextField(final Cell<T> cell, final StringConverter<T> converter) {
		final TextField textField = new TextField(getStringFromCell(cell, converter));

		// Adapted from the original javafx.scene.control.cell.CellUtils class
		textField.setOnAction(event -> {
			if (converter == null) {
				throw new IllegalStateException(
					"Attempting to convert text input into Object, but provided "
						+ "StringConverter is null. Be sure to set a StringConverter "
						+ "in your cell factory.");
			}
			cell.commitEdit(converter.fromString(textField.getText()));
			event.consume();
		});
		textField.setOnKeyReleased(t -> {
			if (t.getCode() == KeyCode.ESCAPE) {
				cell.cancelEdit();
				t.consume();
			}
		});
		return textField;
	}

}

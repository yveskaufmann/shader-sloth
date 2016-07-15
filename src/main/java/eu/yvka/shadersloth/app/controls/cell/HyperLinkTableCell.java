package eu.yvka.shadersloth.app.controls.cell;


import eu.yvka.shadersloth.share.context.ApplicationContext;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/**
 * A class containing a {@link TableCell} implementation that draws a
 * {@link Hyperlink} node inside the cell.
 *
 * <p>By default, the TextFieldTableCell is rendered as a {@link Hyperlink} when not
 * being edited, and as a TextField when in editing mode. The TextField will, by
 * default, stretch to fill the entire table cell.
 *
 * @param <T> The type of the elements contained within the TableColumn.
 * @since 11.07.2016
 * @Author Yves Kaufmann
 */
public class HyperLinkTableCell<S, T> extends TableCell<S, T> {

	/***************************************************************************
	 *                                                                         *
	 * Static cell factories                                                   *
	 *                                                                         *
	 **************************************************************************/

	public static <S> Callback<TableColumn<S,String>, TableCell<S,String>> forTableColumn() {
		return forTableColumn(new DefaultStringConverter());
	}


	public static <S,T> Callback<TableColumn<S,T>, TableCell<S,T>> forTableColumn(
		final StringConverter<T> converter) {
		return list -> new HyperLinkTableCell<S,T>(converter);
	}


	/***************************************************************************
	 *                                                                         *
	 * Fields                                                                  *
	 *                                                                         *
	 **************************************************************************/

	private TextField textField;
	private Hyperlink hyperlink;



	/***************************************************************************
	 *                                                                         *
	 * Constructors                                                            *
	 *                                                                         *
	 **************************************************************************/

	/**
	 * Creates a default TextFieldTableCell with a null converter. Without a
	 * {@link StringConverter} specified, this cell will not be able to accept
	 * input from the TextField (as it will not know how to convert this back
	 * to the domain object). It is therefore strongly encouraged to not use
	 * this constructor unless you intend to set the converter separately.
	 */
	public HyperLinkTableCell() {
		this(null);
	}

	/**
	 * Creates a TextFieldTableCell that provides a {@link TextField} when put
	 * into editing mode that allows editing of the cell content. This method
	 * will work on any TableColumn instance, regardless of its generic type.
	 * However, to enable this, a {@link StringConverter} must be provided that
	 * will convert the given String (from what the user typed in) into an
	 * instance of type T. This item will then be passed along to the
	 * {@link TableColumn#onEditCommitProperty()} callback.
	 *
	 * @param converter A {@link StringConverter converter} that can convert
	 *      the given String (from what the user typed in) into an instance of
	 *      type T.
	 */
	public HyperLinkTableCell(StringConverter<T> converter) {
		this.getStyleClass().add("text-field-table-cell");
		setConverter(converter);
		hyperlink = new Hyperlink();
		hyperlink.setOnAction((e) -> {
			// Maybe there is a standard way to obtain the current application object
			ApplicationContext.get().getApplication().getHostServices().showDocument(hyperlink.getText());
		});
	}



	/***************************************************************************
	 *                                                                         *
	 * Properties                                                              *
	 *                                                                         *
	 **************************************************************************/

	// --- converter
	private ObjectProperty<StringConverter<T>> converter =
		new SimpleObjectProperty<StringConverter<T>>(this, "converter");

	/**
	 * The {@link StringConverter} property.
	 */
	public final ObjectProperty<StringConverter<T>> converterProperty() {
		return converter;
	}

	/**
	 * Sets the {@link StringConverter} to be used in this cell.
	 */
	public final void setConverter(StringConverter<T> value) {
		converterProperty().set(value);
	}

	/**
	 * Returns the {@link StringConverter} used in this cell.
	 */
	public final StringConverter<T> getConverter() {
		return converterProperty().get();
	}



	/***************************************************************************
	 *                                                                         *
	 * Public API                                                              *
	 *                                                                         *
	 **************************************************************************/

	/** {@inheritDoc} */
	@Override public void startEdit() {
		if (! isEditable()
			|| ! getTableView().isEditable()
			|| ! getTableColumn().isEditable()) {
			return;
		}
		super.startEdit();

		if (isEditing()) {
			if (textField == null) {
				textField = CellUtils.createTextField(this, getConverter());
			}

			textField.setText(CellUtils.getStringFromCell(this, getConverter()));
			setGraphic(textField);
			setText(null);

			textField.selectAll();
			textField.requestFocus();

		}
	}

	/** {@inheritDoc} */
	@Override public void cancelEdit() {
		super.cancelEdit();
		hyperlink.setText(CellUtils.getStringFromCell(this, getConverter()));
		setGraphic(hyperlink);
	}

	/** {@inheritDoc} */
	@Override public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setGraphic(null);
			setText(null);
		} else {
			if (isEditing()) {
				setText(null);
				textField.setText(CellUtils.getStringFromCell(this, getConverter()));
				setGraphic(textField);
			} else {
				hyperlink.setText(CellUtils.getStringFromCell(this, getConverter()));
				setGraphic(hyperlink);
				setText(null);
			}
		}
	}
}


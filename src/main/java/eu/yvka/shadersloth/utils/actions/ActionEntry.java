package eu.yvka.shadersloth.utils.actions;

import eu.yvka.shadersloth.I18N.I18N;
import eu.yvka.shadersloth.utils.FontAwesomeIcon;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

import java.util.Optional;


public class ActionEntry {

	private ObjectProperty<FontAwesomeIcon> icon;
	private IntegerProperty priority;
	private StringProperty text;
	private StringProperty tooltip;
	private StringProperty id;
	private ObjectProperty<ActionCallback> callback;


	public ActionEntry() {}
	public ActionEntry(FontAwesomeIcon icon) {
		setIcon(icon);
	}

	public ActionEntry(String text) {
		setText(text);
	}

	public ActionEntry(FontAwesomeIcon icon, String text) {
		setIcon(icon);
		setText(text);
	}

	public ActionEntry(FontAwesomeIcon icon, String text, ActionCallback callback) {
		setIcon(icon);
		setText(text);
		setOnAction(callback);
	}

	public ActionEntry(FontAwesomeIcon icon, ActionCallback callback) {
		setIcon(icon);
		setOnAction(callback);
	}

	public final Optional<FontAwesomeIcon> getIconOptional() {
		return icon != null ? Optional.of(icon.get()) : Optional.empty();
	}

	public final FontAwesomeIcon getIcon() {
		Optional<FontAwesomeIcon> icon = getIconOptional();
		if (icon.isPresent()) {
			return icon.get();
		}
		return null;
	}

	public final void setIcon(FontAwesomeIcon icon) {
		iconProperty().set(icon);
	}

	public final ObjectProperty<FontAwesomeIcon> iconProperty() {
		if (icon == null) {
			icon = new SimpleObjectProperty<>(this, "icon");
		}
		return icon;
	}

	public final Optional<ActionCallback> getCallback() {
		return callback != null ? Optional.of(callback.get()) : Optional.empty();
	}

	public final void setOnAction(ActionCallback callback) {
		callbackProperty().set(callback);
	}

	public final ObjectProperty<ActionCallback> callbackProperty() {
		if (callback == null) {
			callback = new SimpleObjectProperty<>(this, "callback");
		}
		return callback;
	}


	public final String getText() {
		return text != null ? text.get() : "";
	}

	public final void setText(String text) {
		if (text == null) text = "";
		textProperty().set(text);
	}

	public final StringProperty textProperty() {
		if (text == null) {
			text = new SimpleStringProperty(this, "text");
			text.addListener(this::convertI18NToString);
		}
		return text;
	}

	public final String getTooltip() {
		return tooltip != null ? tooltip.get() : "";
	}

	public final void setTooltip(String title) {
		if (title == null) title = "";
		textProperty().set(title);
	}

	public final StringProperty tooltipProperty() {
		if (tooltip == null) {
			tooltip = new SimpleStringProperty(this, "tooltip");
			tooltip.addListener(this::convertI18NToString);
		}
		return tooltip;
	}

	private void convertI18NToString(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		if (oldValue != newValue && newValue.startsWith("%")) {
			if (observable instanceof StringProperty) {
				StringProperty.class.cast(observable).setValue(I18N.getString(newValue));
			}
		}
	}

	public final IntegerProperty priorityProperty() {
		if (priority == null) {
			priority = new SimpleIntegerProperty(this, "priority", 0);
		}
		return priority;
	}

	public void setPriority(int priority) {
		 priorityProperty().set(priority);
	}

	public int getPriority() {
		return priority == null ? 0 : priority.get();
	}

	public final String getId() {
		return id != null ? id.get() : "";
	}

	public final void setId(String id) {
		if (id == null) id = "";
		idProperty().set(id);
	}

	public final StringProperty idProperty() {
		if (id == null) {
			id = new SimpleStringProperty(this, "id");
		}
		return id;
	}
}

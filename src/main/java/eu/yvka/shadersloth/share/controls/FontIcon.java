package eu.yvka.shadersloth.share.controls;

import eu.yvka.shadersloth.share.controls.FontAwesomeIcon;
import eu.yvka.shadersloth.share.controls.FontBasedIcon;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;


public class FontIcon extends Label {


	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public FontIcon() {
		super();
		initialize();
	}

	public FontIcon(FontBasedIcon icon) {
		super(icon.getText());
		initialize();
	}

	public FontIcon(String icon) {
		super(icon);
		initialize();
	}

	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/

	private ObjectProperty<FontAwesomeIcon> icon;
	public ObjectProperty<FontAwesomeIcon>  iconProperty() {
		if (icon == null) {
			icon = new SimpleObjectProperty<FontAwesomeIcon>(this, "icon", null) {
				@Override
				protected void invalidated() {
					super.invalidated();
					setText(iconProperty().getValue().getText());
				}
			};
		}
		return icon;
	}

	public FontAwesomeIcon getIcon() {
		return icon == null ? null : icon.get();
	}

	public void setIcon(FontAwesomeIcon icon) {
		iconProperty().setValue(icon);
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	private void initialize() {
		getStyleClass().add(DEFAULT_STYLE_CLASS);
	}

	/******************************************************************************
	 *
	 * Styling
	 *
	 ******************************************************************************/

	public static final String DEFAULT_STYLE_CLASS = "icon-label";
}

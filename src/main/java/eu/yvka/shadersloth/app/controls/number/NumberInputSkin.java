package eu.yvka.shadersloth.app.controls.number;

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import eu.yvka.shadersloth.app.controls.NumberInput;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;

import java.awt.*;
import java.text.NumberFormat;

public class NumberInputSkin extends BehaviorSkinBase<NumberInput, NumberInputBehaviour> {

	private static final String VALUE_PROPERTY = "value";
	private static final String LABEL_PROPERTY = "label";
	private static final String MIN_VALUE_PROPERTY = "minValue";
	private static final String MAX_VALUE_PROPERTY = "maxValue";
	private static final String FORMAT_PROPERTY = "format";


	private TextFormatter<Number> formatter;
	private NumberFormat numberFormat;
	private TextField textField;
	private Label label;

	public NumberInputSkin() {
		super(null, new NumberInputBehaviour(null));
	}

	/**
	 * Constructor for all SkinBase instances.
	 *
	 * @param control The control for which this Skin should attach to.
	 */
	public NumberInputSkin(NumberInput control) {
		super(control, new NumberInputBehaviour(control));
		initialize();
		control.requestLayout();
		registerChangeListener(control.valueProperty(), VALUE_PROPERTY);
		registerChangeListener(control.minValueProperty(), MIN_VALUE_PROPERTY);
		registerChangeListener(control.maxValueProperty(), MAX_VALUE_PROPERTY);
		registerChangeListener(control.labelProperty(), LABEL_PROPERTY);
		registerChangeListener(control.formatProperty(), FORMAT_PROPERTY);
	}

	private void initialize() {

		getSkinnable().setFocusTraversable(false);
		VBox hLayout = new VBox();
		hLayout.setCursor(Cursor.H_RESIZE);
		hLayout.setFocusTraversable(false);

		textField = new TextField();
		textField.setMinWidth(50);
		textField.setMaxWidth(150);
		textField.setText(Double.toString(getSkinnable().getValue()));

		label = new Label();
		label.setAlignment(Pos.CENTER_LEFT);
		label.setLabelFor(textField);
		label.setPadding(new Insets(0, 0, 0, 3));
		label.setVisible(false);
		label.setManaged(false);

		setUpTextFormatter();
		textField.setTextFormatter(formatter);

		hLayout.getChildren().add(label);
		hLayout.getChildren().add(textField);

		getChildren().clear();
		getChildren().add(hLayout);

		eventHandling(hLayout);

		showValue(getSkinnable().getValue());
		showLabel(getSkinnable().getLabel());

	}

	private void eventHandling(final Pane hLayout) {

		hLayout.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {

			double mouseXBeforeClick = 0;
			double mouseYBeforeClick = 0;
			double lastMouseX = 0;

			@Override
			public void handle(MouseEvent event) {

				if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
					lastMouseX = mouseXBeforeClick = event.getX();
					mouseYBeforeClick = event.getY();
				}

				if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
					hLayout.setCursor(Cursor.H_RESIZE);
					resetMouseCursor();
				}

				if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
					hLayout.setCursor(Cursor.NONE);
					double offset = (event.getX() - mouseXBeforeClick) < 0 ? - 1 : 1;
					showValue(formatter.getValue().doubleValue() + offset);
					resetMouseCursor();
				}
			}

			private void resetMouseCursor() {
				try {
                    Point2D mousePos = hLayout.localToScreen(mouseXBeforeClick, mouseYBeforeClick);
                    new Robot().mouseMove((int) mousePos.getX(), (int) mousePos.getY());
                } catch (AWTException e) {
                    e.printStackTrace();
                }
			}
		});

		formatter.valueProperty().addListener((observable, oldValue1, newValue) -> {
			getSkinnable().setValue(newValue.doubleValue());
		});
	}

	private void setUpTextFormatter() {
		numberFormat = NumberFormat.getInstance();
		numberFormat.setMinimumFractionDigits(2);
		NumberStringConverter converter = new NumberStringConverter(numberFormat);
		formatter = new TextFormatter<>(converter);
	}

	@Override
	protected void handleControlPropertyChanged(String propertyReference) {
		switch (propertyReference) {
			case VALUE_PROPERTY: showValue(getSkinnable().getValue()); break;
			case LABEL_PROPERTY: showLabel(getSkinnable().getLabel()); break;
			case FORMAT_PROPERTY:
				numberFormat = getSkinnable().formatProperty().get();
				NumberStringConverter converter = new NumberStringConverter(numberFormat);
				formatter = new TextFormatter<>(converter);
				textField.setTextFormatter(new TextFormatter<>(converter));
				break;
		}
	}
	public void showValue(Double newValue) {
		newValue = Math.max(newValue, getSkinnable().getMinValue());
		newValue = Math.min(newValue, getSkinnable().getMaxValue());
		formatter.setValue(newValue);
	}

	public void showLabel(String labelText) {
		if (!"".equals(labelText.trim())) {
			label.setText(labelText + ":");
			label.setVisible(true);
			label.setManaged(true);
		} else {
			label.setVisible(false);
			label.setManaged(false);
		}
	}
}

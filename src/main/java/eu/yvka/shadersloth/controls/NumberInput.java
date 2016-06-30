package eu.yvka.shadersloth.controls;

import eu.yvka.shadersloth.controls.number.NumberInputSkin;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;

import java.text.NumberFormat;

public class NumberInput extends TextField {

	public static final String DEFAULT_STYLE_CLASS = "numberInput";

	public NumberInput() {
		setValue(0);
		setLabel("");
		setFocusTraversable(false);
		initialize();
	}

	public NumberInput( String label, double value) {
		setValue(value);
		setLabel(label);
		initialize();
	}

	public NumberInput(String label, double value, double minValue, double maxValue) {
		setLabel(label);
		setValue(value);
		setMinValue(minValue);
		setMaxValue(maxValue);
		initialize();
	}

	private void initialize() {
		getStyleClass().setAll(DEFAULT_STYLE_CLASS);
	}

	/**
	 * The current value of this number input controls.
	 */
	private DoubleProperty value;
	public void setValue(double value) {
		valueProperty().set(value);
	}

	public double getValue() {
		return value == null ? 0 : value.getValue();
	}

	public DoubleProperty valueProperty() {
		if (value == null) {
			value = new DoublePropertyBase(0.0) {

				@Override
				protected void invalidated() {
					super.invalidated();
				}

				@Override
				public Object getBean() {
					return NumberInput.this;
				}

				@Override
				public String getName() {
					return "value";
				}
			};
		}
		return value;
	}

	/**
	 * Minimum Value
	 */
	private DoubleProperty minValue;
	public DoubleProperty minValueProperty() {
		if (minValue == null) {
			minValue = new DoublePropertyBase(Double.NEGATIVE_INFINITY) {
				@Override
				public Object getBean() {
					return NumberInput.this;
				}

				@Override
				public String getName() {
					return "minValue";
				}
			};
		}
		return minValue;
	}

	public void setMinValue(double minValue) {
		minValueProperty().set(minValue);
	}

	public double getMinValue() {
		return minValue == null ? Double.MIN_VALUE : minValue.getValue();
	}


	/**
	 * Minimum Value
	 */
	private DoubleProperty maxValue;
	public DoubleProperty maxValueProperty() {
		if (maxValue == null) {
			maxValue = new DoublePropertyBase(Double.MAX_VALUE) {
				@Override
				public Object getBean() {
					return NumberInput.this;
				}

				@Override
				public String getName() {
					return "maxValue";
				}
			};
		}
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		maxValueProperty().set(maxValue);
	}

	public double getMaxValue() {
		return maxValue == null ? Double.MAX_VALUE : maxValue.getValue();
	}

	/**
	 * The the label text
	 */
	private StringProperty label;
	public void setLabel(String value) {
		labelProperty().set(value);
	}

	public String getLabel() {
		return label == null ? "" : label.get();
	}

	public StringProperty labelProperty() {
		if (label == null) {
			label = new StringPropertyBase("") {
				@Override
				public Object getBean() {
					return NumberInput.this;
				}

				@Override
				public String getName() {
					return "label";
				}
			};
		}
		return label;
	}

	private ObjectProperty<NumberFormat> format;
	public void setFormat(NumberFormat format) {
		formatProperty().set(format);
	}

	public NumberFormat getFormat() {
		return format == null ? NumberFormat.getInstance() : format.get();
	}

	public ObjectProperty<NumberFormat> formatProperty() {
		if (format == null) {
			format = new ObjectPropertyBase<NumberFormat>(NumberFormat.getInstance()) {
				@Override
				public Object getBean() {
					return NumberInput.this;
				}

				@Override
				public String getName() {
					return "format";
				}
			};
		}
		return format;
	}


	@Override
	protected Skin<?> createDefaultSkin() {
		return new NumberInputSkin(this);
	}

}

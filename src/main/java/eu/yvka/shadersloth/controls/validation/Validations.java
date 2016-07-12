package eu.yvka.shadersloth.controls.validation;

import javafx.util.converter.NumberStringConverter;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

import java.util.Locale;

import static eu.yvka.shadersloth.I18N.I18N.getString;

/**
 * @Author Yves Kaufmann
 * @since 12.07.2016
 */
public class Validations {

	private static final NumberStringConverter numberStringConverter = new NumberStringConverter(Locale.getDefault());

	public static <T> Validator<T> createEmptyValidator(String label) {
		Validator<T> emptyValidator = Validator.createEmptyValidator(getString("validation.required", prepareLabel(label)));
		return preventValidationOnHiddenFields(emptyValidator);
	}

	private static <T> Validator<T> preventValidationOnHiddenFields(Validator<T> emptyValidator) {
		return (c, value) -> {
			if (c.isDisabled() || c.isDisable() || !c.isVisible() || c.getScene() == null || c.getParent() == null) {
				System.out.println("yeah");
				return ValidationResult.fromMessageIf(c, null, Severity.ERROR, false);
			}
			return emptyValidator.apply(c, value);
		};
	}

	private static String prepareLabel(String label) {
		return label.replace(":", "");
	}

	public static <T extends String> Validator<T>  createMinimumValidator(String label, Number minValue) {
		return preventValidationOnHiddenFields(Validator.createPredicateValidator((T value) -> {
			try {
				Number number = numberStringConverter.fromString(value);
				return number.doubleValue() >= minValue.doubleValue();
			} catch (Exception ex) {}
			return false;
		}, getString("validation.minimum", prepareLabel(label), minValue)));
	}
}

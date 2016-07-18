package eu.yvka.shadersloth.app.errorView;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Yves Kaufmann
 * @since 18.07.2016
 */
public class ShaderError {

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	private StringProperty description;
	private StringProperty shaderName;
	private IntegerProperty lineNumber;

	/******************************************************************************
	 *
	 * Constructor
	 *
	 ******************************************************************************/

	public static ShaderError fromShaderException() {
		return new ShaderError("", "", 1);
	};

	ShaderError(String error, String name, Integer line) {
		description = new SimpleStringProperty(this, "description", error);
		shaderName = new SimpleStringProperty(this, "shaderName", name);
		lineNumber = new SimpleIntegerProperty(this, "lineNumber", line);
	}


	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/

	public String getDescription() {
		return description.get();
	}

	public StringProperty descriptionProperty() {
		return description;
	}

	public void setDescription(String description) {
		this.description.set(description);
	}

	public String getShaderName() {
		return shaderName.get();
	}

	public StringProperty shaderNameProperty() {
		return shaderName;
	}

	public void setShaderName(String shaderName) {
		this.shaderName.set(shaderName);
	}

	public int getLineNumber() {
		return lineNumber.get();
	}

	public IntegerProperty lineNumberProperty() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber.set(lineNumber);
	}
}

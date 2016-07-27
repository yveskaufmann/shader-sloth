package eu.yvka.shadersloth.app.materialEditor.shaders.errors;

import eu.yvka.slothengine.shader.source.ShaderSource;
import javafx.beans.property.*;

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
	private ObjectProperty<ShaderSource> source;

	/******************************************************************************
	 *
	 * Constructor
	 *
	 ******************************************************************************/

	ShaderError(String error, String name, Integer line, ShaderSource source) {
		description = new SimpleStringProperty(this, "description", error);
		shaderName = new SimpleStringProperty(this, "shaderName", name);
		lineNumber = new SimpleIntegerProperty(this, "lineNumber", line);
		this.source = new SimpleObjectProperty<ShaderSource>(source);
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

	public ShaderSource getSource() {
		return source.get();
	}

	public ObjectProperty<ShaderSource> sourceProperty() {
		return source;
	}
}

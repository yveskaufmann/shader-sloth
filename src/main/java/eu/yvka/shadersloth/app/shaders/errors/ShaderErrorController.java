package eu.yvka.shadersloth.app.shaders.errors;

import eu.yvka.slothengine.engine.Engine;
import eu.yvka.slothengine.shader.Shader;
import eu.yvka.slothengine.shader.ShaderErrorListener;
import javafx.fxml.FXML;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yves Kaufmann
 * @since 18.07.2016
 */
public class ShaderErrorController implements ShaderErrorListener {
	// "\s[A-Za-z0-9]+:\s([.*]+)
	private final Pattern NVIDIA_ERROR_PATTERN = Pattern.compile("^[0-9]+\\Q(\\E(\\d+)\\Q)\\E\\s:\\s(?:warning|error)\\s[A-Z0-9]+:\\s(.+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

	/******************************************************************************
	 *
	 * FXML Fields
	 *
	 ******************************************************************************/

	@FXML private ShaderErrorTable errorTable;

	/******************************************************************************
	 *
	 * Constructor
	 *
	 ******************************************************************************/

	public ShaderErrorController() {

	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	public void initialize() {
		Engine.registerShaderErrorListener(this);
	}

	@Override
	public void onCompileError(Shader shader, String error) {
		Matcher m = NVIDIA_ERROR_PATTERN.matcher(error);
		errorTable.getItems().clear();
		while (m != null && m.find()) {
			String line = m.group(1);
			String msg = m.group(2);
			ShaderError shaderError = new ShaderError(msg, shader.getShaderName(), Integer.valueOf(line));
			errorTable.getItems().add(shaderError);
		}
	}

	@Override
	public void onLinkError(Shader shader, String s) {
		System.out.println("link" + s);
	}
}

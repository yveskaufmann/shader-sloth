package eu.yvka.shadersloth.app.materialEditor.shaders;

import eu.yvka.shadersloth.share.I18N.I18N;
import eu.yvka.slothengine.shader.Shader;
import eu.yvka.slothengine.shader.ShaderManager;
import eu.yvka.slothengine.shader.ShaderType;
import eu.yvka.slothengine.shader.source.FileShaderSource;
import eu.yvka.slothengine.shader.source.ShaderSource;

import  eu.yvka.slothengine.engine.Engine;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.Optional;

import static eu.yvka.shadersloth.share.I18N.I18N.getString;
import static java.nio.file.StandardCopyOption.*;

/**
 * Allows it to create template shader files,
 * which are will be used when a new material is generated.
 *
 * @author Yves Kaufmann
 * @since 26.07.2016
 */
public class ShaderTemplateHelper {

	public static Shader createShader(File baseFolder, String name) {
		return createShader(baseFolder, name, false);
	}

	public static Shader createShader(File baseFolder, String name, boolean alwaysOverwrite) {
		final ShaderManager shaderManager = Engine.shaderManager();
		final File vertexShaderFile = new File(baseFolder, shaderManager.toShaderFileName(name, ShaderType.VERTEX));
		final File fragmentShaderSource = new File(baseFolder, shaderManager.toShaderFileName(name, ShaderType.FRAGMENT));

		// Ensure shader only will be overwritten if the user confirms this action
		if ((vertexShaderFile.exists() || fragmentShaderSource.exists())  && !alwaysOverwrite) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setHeaderText(getString("material.overwrite.dlg.title", name));
			alert.setContentText(getString("material.overwrite.dlg.content", name));
			Optional<ButtonType> buttonTypeOptional =  alert.showAndWait();
			if (!buttonTypeOptional.isPresent() || !buttonTypeOptional.get().equals(ButtonType.YES)) {
				return null;
			}
		}

		final Shader template = shaderManager.getShader("Default");

		try {
			File defaultVertexShaderFile = shaderManager.toShaderFile(new File(ShaderManager.ASSET_PATH), "Default", ShaderType.VERTEX);
			File defaultFragmentShaderFile = shaderManager.toShaderFile(new File(ShaderManager.ASSET_PATH), "Default", ShaderType.FRAGMENT);

			vertexShaderFile.getParentFile().mkdirs();
			fragmentShaderSource.getParentFile().mkdirs();

			Files.copy(defaultVertexShaderFile.toPath(), vertexShaderFile.toPath(), REPLACE_EXISTING);
			Files.copy(defaultFragmentShaderFile.toPath(), fragmentShaderSource.toPath(), REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

		final ShaderSource vertexSource = new FileShaderSource(ShaderType.VERTEX, vertexShaderFile);
		final ShaderSource fragmentSource = new FileShaderSource(ShaderType.FRAGMENT, fragmentShaderSource);
		final Shader shader = new Shader(name);
		shader.addSource(vertexSource);
		shader.addSource(fragmentSource);

		return shader;
	}

}

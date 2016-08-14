package eu.yvka.shadersloth;

import eu.yvka.shadersloth.app.materialEditor.shaders.ShaderEditor;
import eu.yvka.shadersloth.app.materialEditor.shaders.parser.Token;
import eu.yvka.slothengine.shader.ShaderType;
import eu.yvka.slothengine.shader.source.FileShaderSource;
import eu.yvka.slothengine.shader.source.ShaderSource;
import eu.yvka.slothengine.shader.source.StringShaderSource;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author Yves Kaufmann
 * @since 10.08.2016
 */
public class TestShaderEditor extends Application {

	private static final String EXAMPLE_CODE = "#version 130\n" +
		"\n" +
		"in vec3 fragmentColor;\n" +
		"out vec4 fragColor;\n" +
		"\n" +
		"uniform int isWireframe;\n" +
		"\n" +
		"void main() {\n" +
		"\t// Output color = color specified in the vertex core.shader, \n" +
		"\t// interpolated between all 3 surrounding vertices\n" +
		"\n" +
		"\tvec3 color = vec3(fragmentColor.r * 0.5);\n" +
		"\n" +
		"\tif (isWireframe == 1) {\n" +
		"\t    color = 1.0 - vec3(0.1, 0.1, 0.1);\n" +
		"\t}\n" +
		"\n" +
		"\tfragColor = vec4(color, 1.0f);\n" +
		"\n" +
		"}\n";

	@Override
	public void start(Stage primaryStage) throws Exception {

		ShaderSource source = new StringShaderSource("TestSource", ShaderType.VERTEX, EXAMPLE_CODE);
		ShaderEditor shaderEditor = new ShaderEditor(source);

		BorderPane root = new BorderPane(shaderEditor);
		root.setPrefWidth(800);
		root.setPrefHeight(600);
		Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

}

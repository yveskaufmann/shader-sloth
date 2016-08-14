package eu.yvka.shadersloth.app.materialEditor.shaders.parser;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * @author Yves Kaufmann
 * @since 13.08.2016
 */
public class GLSLLexerTest {

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

	public static void main(String []args) throws IOException {
		Token token = null;
		GLSLLexer lexer = new GLSLLexer(new StringReader(EXAMPLE_CODE));
		while ((token = lexer.yylex()) != null) {
			System.out.println(lexer.yylength() + " " + lexer.yytext());
		}
	}

}

package eu.yvka.shadersloth.controls;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShaderEditor extends CodeArea {

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


	private static final Pattern GLSL_KEY_WORD = Pattern.compile("(?<TYPE>int|float|vec[234]|mat[234])");
	private static final int TYPE_GROUP = 1 ;

	{
		getStylesheets().add(ShaderEditor.class.getResource("css/glsl-highlighting.css").toExternalForm());
	}

	public ShaderEditor() {
		super();
		setParagraphGraphicFactory(LineNumberFactory.get(this));
		textProperty().addListener((obs, oldText, newText) -> {
			setStyleSpans(0, computeHighlighting(newText));
		});
	}

	public ShaderEditor(String text) {
		this();
		replaceText(0, 0, text);
		getUndoManager().forgetHistory();
		getUndoManager().mark();
	}

	private StyleSpans<? extends Collection<String>> computeHighlighting(String text) {
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		Matcher typeMatcher = GLSL_KEY_WORD.matcher(text);
		int lastKwEnd = 0;

		while (typeMatcher.find()) {
			spansBuilder.add(Collections.emptyList(), typeMatcher.start() - lastKwEnd);
			if (typeMatcher.group(TYPE_GROUP) != null) {
				spansBuilder.add(Collections.singleton("type"), typeMatcher.end() - typeMatcher.start());
			} else {
				spansBuilder.add(Collections.singleton("text"), typeMatcher.end() - typeMatcher.start());
			}
			lastKwEnd = typeMatcher.end();
		}
		spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
		return spansBuilder.create();
	}
}

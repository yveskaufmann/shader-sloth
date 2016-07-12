package eu.yvka.shadersloth.controls;

import eu.yvka.shadersloth.App;
import eu.yvka.slothengine.shader.source.ShaderSource;
import impl.org.controlsfx.skin.AutoCompletePopup;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.*;
import org.fxmisc.wellbehaved.event.EventHandlerHelper;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.input.KeyCombination.CONTROL_DOWN;
import static org.fxmisc.wellbehaved.event.EventPattern.keyPressed;

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
	private AutoCompletePopup<String> autoCompletePopup;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	public ShaderEditor(final ShaderSource source) {
		this();
		sourceProperty().set(source);
	}
	public ShaderEditor() {
		super();
		getStylesheets().add(App.class.getResource("css/glsl-highlighting.css").toExternalForm());
		setParagraphGraphicFactory(LineNumberFactory.get(this));
		textProperty().addListener((obs, oldText, newText) -> {
			setStyleSpans(0, computeHighlighting(newText));
		});

		sourceProperty().addListener((observable, oldValue, newValue) -> {
			replaceText(0,0, newValue.getSource());
			getUndoManager().forgetHistory();
			getUndoManager().mark();
		});

		textProperty().addListener((observable1, oldValue1, newValue1) -> {
			if (source != null) source.get().updateShaderSource(newValue1);
		});

		EventHandler<? super KeyEvent> ctrlS = EventHandlerHelper.on(keyPressed(KeyCode.SPACE, CONTROL_DOWN)).act(this::onCompletion).create();
		EventHandlerHelper.install(onKeyPressedProperty(), ctrlS);
	}

	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	private void onCompletion(KeyEvent keyEvent) {
		if (autoCompletePopup == null) {
			autoCompletePopup = new AutoCompletePopup<>();
			autoCompletePopup.getSuggestions().addAll("vec4", "sin4");

			autoCompletePopup.setHideOnEscape(true);
			setPopupWindow(autoCompletePopup);
			setPopupAlignment(PopupAlignment.CARET_BOTTOM);
			positionCaret(getCaretPosition() + 1);
			positionCaret(getCaretPosition() - 1);
		}
		autoCompletePopup.show(this.getScene().getWindow());


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

	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/

	private ObjectProperty<ShaderSource> source;
	public ObjectProperty<ShaderSource> sourceProperty() {
		if (source == null) {
			source = new SimpleObjectProperty<>(this, "source");
		}
		return source;
	}


}

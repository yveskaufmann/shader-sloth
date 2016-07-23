package eu.yvka.shadersloth.app.sceneEditor;

import eu.yvka.slothengine.math.Color;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ColorPicker;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.AbstractPropertyEditor;
import org.controlsfx.property.editor.PropertyEditor;

/**
 * PropertyEditors contains a set of property editors
 * for shader-sloth related types.
 *
 * @author Yves Kaufmann
 */
public class PropertyEditors {

	/**
	 * Creates a property editor for shader-sloth's
	 * color type.
	 *
	 * @param item the item which should be edit
	 * @return a instance of color property editor
     */
	public static PropertyEditor<Color> createColorEditor(PropertySheet.Item item) {
		final ColorPicker colorPicker = new ColorPicker();

		// holds the current selected color this property is required in
		// order to convert between a javafx color type and a sloth engine color type.
		final SimpleObjectProperty<Color> colorProperty = new SimpleObjectProperty<Color>();

		// set the initial color from the current material
		if (item.getValue() instanceof Color) {
			colorPicker.setValue(createFXColorFromSLColor((Color) item.getValue()));
			colorProperty.setValue((Color) item.getValue());
		}

		// ensure that the colorProperty is updated if the user make any changes
		colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Color newColor = Color.White;
            if (newValue != null) {
                newColor = createColorFrom(newValue);
            }
            colorProperty.set(newColor);
        });

		return new AbstractPropertyEditor<Color, ColorPicker>(item, colorPicker) {
			@Override
			public void setValue(Color color) {
				getEditor().setValue(createFXColorFromSLColor(color));
			}

			@Override
			protected ObservableValue<Color> getObservableValue() {
				return colorProperty;
			}
		};
	}

	private static javafx.scene.paint.Color createFXColorFromSLColor(Color color) {
		return javafx.scene.paint.Color.color(
            color.getRed(),
            color.getBlue(),
            color.getBlue()
        );
	}

	private static Color createColorFrom(javafx.scene.paint.Color value) {
		return Color.fromRGB(value.getRed(), value.getGreen(), value.getBlue());
	}
}

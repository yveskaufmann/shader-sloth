package eu.yvka.shadersloth.app.sceneEditor;

import eu.yvka.slothengine.math.Color;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.adapter.JavaBeanObjectProperty;
import javafx.beans.property.adapter.ReadOnlyJavaBeanObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ColorPicker;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.AbstractPropertyEditor;
import org.controlsfx.property.editor.PropertyEditor;

import java.util.concurrent.Callable;

/**
 * Created by fxdapokalypse on 20.07.16.
 */
public class PropertyEditors {
	public static PropertyEditor<?> createColorEditor(PropertySheet.Item item) {
		return new AbstractPropertyEditor(item, new ColorPicker()) {

			@Override
			public void setValue(Object value) {
				if (value instanceof Color) {
					Color color = (Color) value;

					((ColorPicker)getEditor()).setValue(javafx.scene.paint.Color.color(
						color.getRed(),
						color.getBlue(),
						color.getBlue()
					));
				}
			}

			@Override
			protected ObservableValue<?> getObservableValue() {
				return Bindings.createObjectBinding(new Callable<Color>() {
					@Override
					public Color call() throws Exception {
						return createColorFrom(((ColorPicker)getEditor()).getValue());
					}
				});
			}
		};
	}

	private static Color createColorFrom(javafx.scene.paint.Color value) {
		return Color.fromRGB(value.getRed(), value.getGreen(), value.getBlue());
	}
}

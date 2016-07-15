package eu.yvka.shadersloth.app.model;

import javafx.util.StringConverter;

/**
 * @Author Yves Kaufmann
 * @since 11.07.2016
 */
public class StringConverters {

	public static <T> StringConverter<T> forTXSemanticTag() {
		return new StringConverter<T>() {
			@Override
			public String toString(T tag) {
				return tag == null ? "" : tag.toString();
			}

			@Override
			public T fromString(String string) {
				throw new UnsupportedOperationException("not yet implemented");
			}
		};
	};

}

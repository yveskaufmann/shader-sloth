package eu.yvka.shadersloth.app.shaders.errors;

import java.util.List;

/**
 * @Author Yves Kaufmann
 * @since 13.07.2016
 */
public class ShaderErrorAppender<E> {

	private List<E> list;

	protected void append(E eventObject) {
		list.add(eventObject);
	}

	synchronized
	public void setModel(List<E> list) {
		this.list = list;
	}
}

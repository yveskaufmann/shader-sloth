package eu.yvka.shadersloth.app.shaders;

import ch.qos.logback.core.AppenderBase;

import java.util.List;

/**
 * @Author Yves Kaufmann
 * @since 13.07.2016
 */
public class ShaderErrorAppender<E> extends AppenderBase<E> {

	private List<E> list;

	@Override
	synchronized
	protected void append(E eventObject) {
		list.add(eventObject);
	}

	synchronized
	public void setModel(List<E> list) {
		this.list = list;
	}
}

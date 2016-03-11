package de.yvka.slothengine.renderer;

public class RendererExpception extends RuntimeException {
	public RendererExpception(String message) {
		super(message);
	}

	public RendererExpception(String message, Object ... args) {
		super(String.format(message, args));
	}

}

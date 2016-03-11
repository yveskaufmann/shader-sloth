package de.yvka.slothengine.texture;

public class TextureException extends RuntimeException {
	public TextureException(String s, Object ...args) {
		super(String.format(s, args));
	}

	public TextureException(Exception ex, String s, Object ...args) {
		super(String.format(s, args), ex);
	}
}

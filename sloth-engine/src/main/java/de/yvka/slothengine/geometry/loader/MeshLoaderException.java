package de.yvka.slothengine.geometry.loader;

public class MeshLoaderException extends RuntimeException {


	public MeshLoaderException(String message) {
		super(message);
	}

	public MeshLoaderException(String message, Object ... args) {
		super(String.format(message, args));
	}

	public MeshLoaderException(String message, Exception ex) {
		super(message, ex);
	}
}

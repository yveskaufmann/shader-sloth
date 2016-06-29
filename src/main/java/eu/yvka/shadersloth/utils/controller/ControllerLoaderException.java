package eu.yvka.shadersloth.utils.controller;

/**
 * Indicates error which happens during the controller creation.
 */
public class ControllerLoaderException extends Exception {

	private static final long serialVersionUID = 1L;

	public ControllerLoaderException(String message) {
		super(message);
	}

	public ControllerLoaderException() {
	}

	public ControllerLoaderException(String message, Throwable cause) {
		super(message, cause);
	}

	public ControllerLoaderException(Throwable cause) {
		super(cause);
	}

	public ControllerLoaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

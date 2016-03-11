package de.yvka.slothengine.input.provider;

import org.joml.Vector2d;
import org.joml.Vector2i;

/**
 * Interface for a mouse input provider.
 */
public interface  MouseInputProvider extends InputProvider {

	/**
	 * Possible pressed/pressed button.
	 */
	enum MouseButton {
		Primary,
		Second,
		Middle,
		None
	}

	/**
	 * Moves the pointer to the specified position,
	 * the position is relative to the viewport.
	 *
	 * @param x
	 * @param y
	 */
	void moveCursor(double x, double y);

	/**
	 * Retrieves the current mouse position.
	 *
	 * @return the current mouse position.
     */
	Vector2d cursorPosition();


}

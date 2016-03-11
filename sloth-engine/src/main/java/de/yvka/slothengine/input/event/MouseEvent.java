package de.yvka.slothengine.input.event;

import static de.yvka.slothengine.input.provider.MouseInputProvider.MouseButton;

/**
 * Mouse event including mouse move, Button-Pressed
 * and MouseWheel rotated.
 */
public class MouseEvent extends InputEvent {

	private double x;
	private double y;
	private double mouseWheelAmount;
	private MouseButton button;
	private boolean pressed;

	public MouseEvent(double x, double y, double mouseWheel) {
		this(MouseButton.None, false, x, y, mouseWheel);
	}

	public MouseEvent(MouseButton button, boolean pressed, double x, double y, double mouseWheel) {
		this.x = x;
		this.y = y;
		this.mouseWheelAmount = mouseWheel;
		this.button = button;
		this.pressed = pressed;
	}

	/**
	 *@return true if a button was pressed.
	 */
	public boolean isPressed() {
		return button != MouseButton.None && pressed;
	}

	/**
	 * @return true if a button was released.
	 */
	public boolean isReleased() {
		return button != MouseButton.None && !pressed;
	}

	/**
	 * Returns the pressed/pressed button.
	 *
	 * @return the pressed button or None if no button was pressed or pressed.
	 */
	public MouseButton getButton() {
		return button;
	}

	/**
	 * Returns the x position of the mouse cursor.
	 *
	 * @return x position in pixels.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the y position of the mouse cursor.
	 *
	 * @return y position in pixels.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Returns the amount of mouse wheel rotation.
	 *
	 * @return the rotation amount of the mouse wheel amount.
	 */
	public double getMouseWheelAmount() {
		return this.mouseWheelAmount;
	}

	@Override
	public String toString() {
		return "MouseEvent{" +
				"x=" + x +
				", y=" + y +
				", mouseWheelAmount=" + mouseWheelAmount +
				", button=" + button +
				", pressed=" + pressed +
				'}';
	}
}

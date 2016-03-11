package de.yvka.slothengine.input.provider.javafx;

import de.yvka.slothengine.input.provider.MouseInputProvider;
import de.yvka.slothengine.input.event.MouseEvent;
import de.yvka.slothengine.input.event.MouseEvent;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import org.joml.Vector2d;

import java.awt.*;

/**
 * A JavaFXMouseProvider retrieves mouse events from a specified node
 * object and delegate it to registered receivers.
 *
 */
public class JavaFXMouseProvider extends JavaFXInputProvider<MouseEvent, javafx.scene.input.MouseEvent> implements MouseInputProvider {

	/**
	 * The amount how far the mouse wheel was rotated.
	 */
	private double mouseWheelAmount;

	/**
	 * Indicates if the mouse wheel was moved.
	 */
	private boolean mouseWheelMoved;

	/**
	 * Previous x cursor position
	 */
	private int previousX;

	/**
	 * Previous y cursor position
	 */
	private int previousY;

	/**
	 * Determines if the y coordinate of the mouse should be inversed.
	 */
	private boolean inverseYMouseCoordinate = true; // A hack which is required by the JavaFXOffscreenSupport

	/**
	 * Creates a JavaFXMouseProvider.
	 *
	 * @param node the node which acts as source for mouse events.
	 */
	public JavaFXMouseProvider(Node node) {
		super(node);
	}

	/**
	 * Moves the pointer to the specified position,
	 * the position is relative to the viewport.
	 *
	 * @param x the x position of the cursor
	 * @param y the y position of the cursor
	 */
	@Override
	public void moveCursor(double x, double y) {
		Point2D sourceNodePos = source.localToScreen(x, y);
		try {
			Robot robot = new Robot();
			robot.mouseMove((int) (sourceNodePos.getX()), (int) sourceNodePos.getY());
		} catch (AWTException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Vector2d cursorPosition() {
		Point pos = MouseInfo.getPointerInfo().getLocation();
		Point2D point2D = source.screenToLocal(pos.x, pos.y);
		return new Vector2d(point2D.getX(), point2D.getY());
	}

	/**
	 * initializes the native side of the input provider.
	 */
	@Override
	public void initialize() {
		if (!initialized) {
			Platform.runLater(() -> {
				source.addEventHandler(javafx.scene.input.MouseEvent.ANY, this);
				source.addEventHandler(javafx.scene.input.ScrollEvent.SCROLL, this::handleWheelEvents);
			});
			initialized = true;
		}
	}

	/**
	 * Destroy the native handler.
	 */
	@Override
	public void shutdown() {
		if (initialized) {
			Platform.runLater(() -> {
				source.removeEventHandler(javafx.scene.input.MouseEvent.ANY, this);
				source.removeEventHandler(javafx.scene.input.ScrollEvent.SCROLL, this::handleWheelEvents);
			});
			eventQueue.clear();
			initialized = false;
		}
	}

	/**
	 * Queries the input state from the native devices
	 * and delegate it to a InputHandler.
	 */
	@Override
	public void update() {
		MouseEvent event;
		if (mouseWheelMoved) {
			event = new MouseEvent(MouseButton.None, false, previousX, previousY, mouseWheelAmount);
			receiver.onMouseEvent(event);
			mouseWheelMoved = false;
		}

		synchronized (eventQueue) {
			while ((event = eventQueue.poll()) != null) {
				receiver.onMouseEvent(event);
			}
			eventQueue.clear();
		}
	}

	/**
	 * Invoked when a specific event of the type for which this handler is
	 * registered happens.
	 *
	 * @param event the event which occurred
	 */
	@Override
	public void handle(javafx.scene.input.MouseEvent event) {
		MouseButton mouseButton = toInputMouseEventButton(event.getButton());
		boolean isPressed = event.getEventType().equals(javafx.scene.input.MouseEvent.MOUSE_PRESSED)
			|| event.getEventType().equals(javafx.scene.input.MouseEvent.MOUSE_DRAGGED);

		double x = event.getX();
		double y = inverseYCoordinate(event.getY());

		MouseEvent mouseEvent = new MouseEvent(
			toInputMouseEventButton(event.getButton()),
			isPressed,
			(int) x,
			(int) y,
			mouseWheelAmount
		);

		previousX = (int) x;
		previousY = (int) y;

		synchronized (eventQueue) {
			eventQueue.add(mouseEvent);
		}
		event.consume();
	}

	private MouseButton toInputMouseEventButton(javafx.scene.input.MouseButton button) {
		switch (button) {
			case NONE:
				return MouseButton.None;
			case MIDDLE:
				return MouseButton.Middle;
			case PRIMARY:
				return MouseButton.Primary;
			case SECONDARY:
				return MouseButton.Second;
		}
		return MouseButton.None;
	}


	private void handleWheelEvents(javafx.scene.input.ScrollEvent event) {
		mouseWheelAmount = event.getDeltaY() / event.getMultiplierY();
		mouseWheelMoved = true;
		event.consume();
	}

	private double inverseYCoordinate(double yCoordinate) {
		if (inverseYMouseCoordinate && source instanceof ImageView) {
			yCoordinate = Math.abs(((ImageView)source).getFitHeight() - yCoordinate);
		}
		return yCoordinate;
	}
}

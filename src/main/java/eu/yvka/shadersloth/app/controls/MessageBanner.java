package eu.yvka.shadersloth.app.controls;

import javafx.scene.control.Label;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;

import static java.util.stream.Collectors.joining;

/**
 * Displays messages to the user or shows
 * validation error messages.
 *
 * @Author Yves Kaufmann
 * @since 08.07.2016
 */
public class MessageBanner extends Label {

	enum MessageType {
		INFO,
		WARNING,
		ERROR
	}

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	/**
	 * Creates a hidden message banner
	 */
	public MessageBanner() {
		getStyleClass().addAll("message-banner");
		managedProperty().bind(visibleProperty());
		hide();
	}


	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	/**
	 * Shows this message banner with the specified message.
	 *
	 * @param message the message to show the user.
     */
	public void show(String message) {
		setText(message);
		setVisible(true);
	}

	/**
	 * Shows validation messages to a user if the passed
	 * validation is invalid.
	 */
	public void show(ValidationSupport validationSupport) {
		if (validationSupport.isInvalid()) {
			String messages = validationSupport
				.getValidationResult().getErrors()
				.stream().map(ValidationMessage::getText)
				.sorted().collect(joining("\n"));

			show(messages);
		}
	}

	/**
	 * Hide this message banner.
	 */
	public void hide() {
		setText("");
		setVisible(false);
	}
}

package eu.yvka.shadersloth.app.materialEditor.shaders.parser;

import java.io.IOException;
import java.io.Reader;

/**
 * @author Yves Kaufmann
 * @since 13.08.2016
 */
public class Token {

	/**
	 * Identifier of the type of token
     */
	public int type;

	/**
	 * Start position of this token
     */
	public int start;

	/**
	 * End position of this token
     */
	public int end;

	/**
	 * State in which this token occurred
     */
	public int state;

	/**
	 * Text of this token
     */
	public String value;

	/**
	 * Creates a token of the specified type.
	 *
	 * @param type the type of the new token.
     */
	public Token(int type) {
		this.type = type;
		start = -1;
		end = -1;
	}

	/**
	 * Creates a token with a specified type and
	 * location.
	 *
	 * @param type the type of the new token
	 * @param start the position of the first character of this token
	 * @param end the position of the last character of this token
	 */
	public Token(int type, int start, int end) {
		this(type, start, end, null);
	}

	/**
	 * Creates a token with a specified type,
	 * location and value.
	 *
	 * @param type the type of the new token
	 * @param start the position of the first character of this token
	 * @param end the position of the last character of this token
     * @param value the value of this token.
     */
	public Token(int type, int start, int end, String value) {
		this(type);
		this.start = start;
		this.end = end;
		this.value = value;
	}


	@Override
	public String toString() {
		if (value != null) {
			String.format("Token[type = #%d, start = %d, end = %d, value = %s]", type, start, end, value);
		}
		return String.format("Token[type = #%d, start = %d, end = %d]", type, start, end);
	}
}

package de.yvka.slothengine.utils;

import java.nio.*;

import static org.lwjgl.BufferUtils.createShortBuffer;

public class BufferUtils {

	/**
	 * Creates a FloatBuffer from a specified float array.
	 *
	 * @param buffer the float array
	 * @return the created FloatBuffer.
     */
	public static FloatBuffer createBuffer(float[] buffer) {
		return (FloatBuffer) createFloatBuffer(buffer.length).put(buffer).flip();
	}

	/**
	 * Creates a IntBuffer from a specified int array.
	 *
	 * @param buffer the int array
	 * @return the created IntBuffer.
	 */
	public static IntBuffer createBuffer(int[] buffer) {
		return (IntBuffer) createIntBuffer(buffer.length).put(buffer).flip();
	}

	/**
	 * Creates a ShortBuffer from a specified short array.
	 *
	 * @param buffer the short array
	 * @return the created ShortBuffer.
	 */
	public static ShortBuffer createBuffer(short[] buffer) {
		return (ShortBuffer) createShortBuffer(buffer.length).put(buffer).flip();
	}

	/**
	 * Creates a ByteBuffer from a specified byte array.
	 *
	 * @param buffer the byte array
	 * @return the created ByteBuffer.
	 */
	public static ByteBuffer createBuffer(byte[] buffer) {
		return (ByteBuffer) createByteBuffer(buffer.length).put(buffer).flip();
	}

	/**
	 * Creates a FloatBuffer with a specified capacity.
	 *
	 * @param capacity the capacity of the new created buffer.
	 * @return the new created float buffer.
     */
	public static FloatBuffer createFloatBuffer(int capacity) {
		return org.lwjgl.BufferUtils.createFloatBuffer(capacity);
	}

	/**
	 * Creates a FloatBuffer with a specified capacity.
	 *
	 * @param capacity the capacity of the new created buffer.
	 * @return the new created float buffer.
	 */
	public static IntBuffer createIntBuffer(int capacity) {
		return org.lwjgl.BufferUtils.createIntBuffer(capacity);
	}

	/**
	 * Creates a DoubleBuffer with a specified capacity.
	 *
	 * @param capacity the capacity of the new created buffer.
	 * @return the new created double buffer.
	 */
	public static DoubleBuffer createDoubleBuffer(int capacity) {
		return org.lwjgl.BufferUtils.createDoubleBuffer(capacity);
	}

	/**
	 * Creates a ByteBuffer with a specified capacity.
	 *
	 * @param capacity the capacity of the new created buffer.
	 * @return the new created byte buffer.
	 */
	public static ByteBuffer createByteBuffer(int capacity) {
		return org.lwjgl.BufferUtils.createByteBuffer(capacity);
	}

	/**
	 * Creates a string representation of a buffer.
	 *
	 * @param buffer the buffer
	 * @param stride the elements per line
	 *
	 * @return the string representation.
     */
	public static String toString(Buffer buffer, int stride) {
		StringBuffer str = new StringBuffer();
		buffer.rewind();

		for (int i = 0; i < buffer.limit(); i++) {
			String element = toString(buffer, 4, i);
			str.append(element);

			if ((i + 1) % stride == 0) {
				str.append("\n");
			} else {
				str.append(" ");
			}
		}

		buffer.rewind();
		return str.toString();
	}

	private static String toString(Buffer buffer, int width, int index) {
		Object typeArg;
		Object element;

		if (FloatBuffer.class.isInstance(buffer)) {
			typeArg = ".2f";
			width += 4;
			element = ((FloatBuffer)buffer).get(index);
		} else if (DoubleBuffer.class.isInstance(buffer)) {
			typeArg = ".2f";
			width += 4;
			element = ((DoubleBuffer)buffer).get(index);
		} else if (ByteBuffer.class.isInstance(buffer)) {
			typeArg = "d";
			element = ((ByteBuffer)buffer).get(index);
		} else if (IntBuffer.class.isInstance(buffer)) {
			typeArg = "d";
			element = ((IntBuffer)buffer).get(index);
		} else if (ShortBuffer.class.isInstance(buffer)) {
			typeArg = "d";
			element = ((ShortBuffer)buffer).get(index);
		} else {
			throw new IllegalArgumentException("The specified buffer type" + buffer.getClass().getName() + " isn't supported");
		}

		return String.format("%" + width + typeArg, element);

	}
}

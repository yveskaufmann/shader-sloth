package de.yvka.slothengine.geometry;

import java.nio.*;

/**
 * Abstraction of a vertex attribute pointer.
 */
public class VertexAttributePointer {

	/**
	 * Data type of the corresponding buffer
	 */
	private Format format;

	/**
	 * Count of components must be between 1-4
	 */
	private int components;

	/**
	 * The stride of this buffer
	 */
	private int stride = 0;

	/**
	 * The offset of this buffer
	 */
	private long offset = 0;

	/**
	 * Specifies if the the underlying buffer should be normalized.
	 */
	private boolean normalized = false;


	/**
	 * Creates a vertex attribute pointer where
	 * are stride, offset are initially zero and normalized is disabled.
	 *
	 * @param components count of components per vertex attribute
	 * @param format the format of the buffer.
     */
	public VertexAttributePointer(int components, Format format) {
		this(components, false, format, 0, 0);
	}

	/**
	 * Creates a vertex attribute pointer where are
	 * normalized is disabled by default.
	 *
	 * @param components count of components per vertex attribute
	 * @param format the format of the buffer.
	 * @param stride the stride of the buffer
     * @param offset the offset to the first vertex element in the buffer
     */
	public VertexAttributePointer(int components, Format format, int stride, long offset) {
		this(components, false, format, stride, offset);
	}

	/**
	 * /**
	 * Creates a vertex attribute pointer.
	 *
	 * @param components count of components per vertex attribute
	 * @param normalized specified whether the buffer data should be normalized
	 * @param format the format of the buffer.
	 * @param stride the stride of the buffer
	 * @param offset the offset to the first vertex element in the buffer
	 */
    public VertexAttributePointer(int components, boolean normalized, Format format, int stride, long offset) {
        setComponents(components);
        this.format = format;
		this.normalized =normalized;
        this.stride = stride;
        this.offset = offset;
    }


	/**
	 * Retrieves the count of components per generic
	 * vertex element/attribute.
	 *
	 * @return the count of components per vertex attribute.
	 */
	public int getComponents() {
		return components;
	}

	/**
	 * Specifies the count of components per generic
	 * vertex element/attribute.
	 *
	 * @param components the count of components and must be 1, 2, 3 or 4.
	 * @return this instance in order to support method-chaining..
	 */
	public VertexAttributePointer setComponents(int components) {
		if (components < 1 || components > 4) {
			throw new IllegalArgumentException("The count of components must be between 1, 2, 3 or 4.");
		}
		this.components = components;
		return this;
	}

	/**
	 * Retrieves the data type/format of this a depending buffer.
	 *
	 * @return the format of this buffer.
	 */
	public Format getFormat() {
		return format;
	}

	/**
	 * Defines the data type/format of the depending buffer vertex buffer.
	 *
	 * @return this instance in order to support method-chaining.
	 */
	public VertexAttributePointer setFormat(Format format) {
		this.format = format;
		return this;
	}


	/**
	 * Retrieves whether fixed-point data values should be normalized (true) or
	 * converted directly as fixed-point values (false) when they are accessed.
	 *
	 * @return whether the data should normalized.
	 */
	public boolean getNormalized() {
		return normalized;
	}

	/**
	 * Specifies whether fixed-point data values should be normalized (true) or
	 * converted directly as fixed-point values (false) when they are accessed.
	 *
	 * @param normalized specifies whether the data should normalized.
	 * @return this instance in order to support method-chaining
	 */
	public VertexAttributePointer setNormalized(boolean normalized) {
		this.normalized = normalized;
		return  this;
	}

	/**
	 * Retrieves the byte offset between consecutive generic vertex attributes.
	 * If stride is 0, the generic vertex attributes are understood to be tightly
	 * packed in the array. The initial value is 0.
	 *
	 * @return the stride of the corresponding buffer.
	 */
	public int getStride() {
		return stride;
	}

	/**
	 * Specifies the byte offset between consecutive generic vertex attributes.
	 * If stride is 0, the generic vertex attributes are understood to be tightly
	 * packed in the array. The initial value is 0.
	 *
	 * @param stride the stride of the corresponding buffer.
	 * @return this instance in order to support method-chaining
	 */
	public VertexAttributePointer setStride(int stride) {
		if (stride < 0) {
			throw new IllegalArgumentException("The stride must be >= 0");
		}
		this.stride = stride;
		return this;
	}

	/**
	 * Retrieves the offset of the first component of this first generic vertex attribute
	 * in the underlying interleaved buffer The initial value is 0.
	 *
	 * @return the offset.
	 */
	public long getOffset() {
		return offset;
	}

	/**
	 * Specifies the offset of the first component of this first generic vertex attribute
	 * in the underlying interleaved buffer The initial value is 0.
	 *
	 * @param offset the offset
	 * @return this instance in order to support method chaining.
	 */
	public VertexAttributePointer setOffset(int offset) {
		this.offset = offset;
		return this;
	}

	/**
	 * Specify the possible data type of
	 * a vertex buffer.
	 */
	public enum Format {
		Float(4),
		Double(8),
		Byte(1),
		Unsingned_Byte(1),
		Short(2),
		Unsigned_Short(2),
		Int(4),
		Unsigned_Int(4);

		private byte sizeInBytes;

		Format(int sizeInBytes) {
			this.sizeInBytes = (byte) sizeInBytes;
		}

		/**
		 * Get the size in Byte for this format.
		 *
		 * @return the size in bytes.
         */
		public int getSizeInBytes() {
			return sizeInBytes;
		}

		/**
		 * Tests whether a Buffer is compatible with this format.
		 *
		 * @param data the buffer against which should be tested.
         * @return true if the buffer is compatible.
         */
		public boolean isCompatible(Buffer data) {
			switch (this) {
				case Unsingned_Byte:
				case Byte: return ByteBuffer.class.isInstance(data);
				case Unsigned_Short:
				case Short: return ShortBuffer.class.isInstance(data);
				case Unsigned_Int:
				case Int: return IntBuffer.class.isInstance(data);
				case Float: return FloatBuffer.class.isInstance(data);
				case Double: return  DoubleBuffer.class.isInstance(data);
			}
			return false;
		}

		public Format getUnsignedIfPossible() {
			switch (this) {
				case Unsingned_Byte:
				case Byte: return Unsingned_Byte;
				case Unsigned_Short:
				case Short: return Unsigned_Short;
				case Unsigned_Int:
				case Int: return Unsigned_Int;
				default: return this;
			}
		}
	}
}

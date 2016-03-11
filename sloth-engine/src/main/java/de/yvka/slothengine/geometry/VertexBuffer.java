package de.yvka.slothengine.geometry;

import de.yvka.slothengine.renderer.Renderer;
import de.yvka.slothengine.utils.HardwareObject;

import java.nio.Buffer;

public class VertexBuffer extends HardwareObject {

	/**
	 * The semantic of this vertex buffer
	 */
	public enum Type {
		Vertex,
		Normal,
		TextCoords,
		TextCoords02,
		TextCoords03,
		TextCoords04,
		TextCoords05,
		TextCoords06,
		Color,
		Interleaved, CpuOnly, Index
	}

	public enum Usage {
		/**
		 * The data store contents will be modified once and used at most a few times as source for GL drawing.
		 */
		STREAM_DRAW,

		/**
		 * The data store contents will be modified once and used at most a few times and
		 * used to return that data when queried by the application.
		 */
		STREAM_READ,

		/**
		 * The data store contents will be modified once and used at most a few times and
		 * used as the source for GL drawing and image specification commands.
		 */
		STREAM_COPY,


		/**
		 * The data store contents will be modified once and used many times as
		 * source for GL drawing.
		 */
		STATIC_DRAW,

		/**
		 * The data store contents will be modified once and used many times and
		 * used to return that data when queried by the application.
		 */
		STATIC_READ,

		/**
		 * The data store contents will be modified once and used many times and
		 * used as the source for GL drawing and image specification commands.
		 */
		STATIC_COPY,

		/**
		 * The data store contents will be modified repeatedly and used many times as
		 * source for GL drawing.
		 */
		DYNAMIC_DRAW,

		/**
		 * The data store contents will be modified repeatedly and used many times and
		 * used to return that data when queried by the application.
		 */
		DYNMAIC_READ,

		/**
		 * The data store contents will be modified repeatedly and used many times and
		 * used as the source for GL drawing and image specification commands.
		 */
		DYNAMIC_COPY;

	}

	/**
	 * The type of this vertex buffer
	 */
	private Type type;

	/**
	 * The usage of this buffer
	 */
	private Usage usage;


	/**
	 * Buffer of which holds the data.
	 */
	private Buffer buffer;

	/**
	 * VertexAttributePointer information
	 *
     */
	private VertexAttributePointer pointer;


	/**
	 * Creates a vertex buffer of the specified type
	 * and initialize the vertex attribute pointer as
	 * a 4 components size float pointer.
	 *
	 * @param type the type of this buffer.
     */
	public VertexBuffer(Type type) {
		this(type, null, Usage.STATIC_DRAW, new VertexAttributePointer(4, VertexAttributePointer.Format.Float));
	}

	public VertexBuffer(Type type, Buffer data, Usage usage, VertexAttributePointer vertexAttrPointer) {
		super(VertexBuffer.class);

		this.type = type;
		this.usage = usage;
		this.pointer = vertexAttrPointer;

		if (data != null) {
			setupData(data);
		}

	}

	public VertexBuffer setupData(Buffer data) {

		// Abort if the buffer is already allocated on the gpu
		if (getId() != UNSET_ID) return this;

		if (! pointer.getFormat().isCompatible(data)) {
			throw new IllegalArgumentException(
				String.format(
					"The %s format isn't compatible with the buffer of the type %s",
					pointer.getFormat().name(),
					buffer.getClass().getSimpleName()
				)
			);
		}

		this.buffer = data;
		enableUpdateRequired();
		return this;
	}

	/**
	 * Retrieves the type of this vertex buffer.
	 * The type of a vertex buffer specifies the
	 * semantic of a vertex buffer, the type <code>Type.Vertex</code>
	 * means that the buffer contains the positions of vertices and so on.
	 *
	 * @return the type of this buffer.
     */
	public Type getType() {
		return type;
	}

	/**
	 * Specifies the type of this vertex buffer and
	 * describes the semantic of the underlying buffer data.
	 *
	 * @param type the type of this buffer.
	 * @return this instance in order to support method-chaining.
     */
	public VertexBuffer setType(Type type) {
		this.type = type;
		return this;
	}

	/**
	 * Returns the buffer object which contains
	 * the data and is stored in the java heap
	 * and acts as source for a generic vertex buffer object.
	 *
	 * @return the underlying buffer object.
     */
	public Buffer getBuffer() {
		return buffer;
	}

	/**
	 * Retrieves the expected usage pattern of the underlying data.
	 *
	 * @return the usage of this buffer.
     */
	public Usage getUsage() {
		return usage;
	}

	/**
	 * Retrieves the expected usage pattern of the underlying data.
	 *
	 * @param usage the usage pattern of this buffer.
	 * @return this instance in order to support method-chaining.
     */
	public VertexBuffer setUsage(Usage usage) {
		this.usage = usage;
		return this;
	}

	public VertexAttributePointer getPointer() {
		return pointer;
	}


	@Override
	public void deleteObject(Renderer renderer) {
		renderer.deleteBuffer(this);
		resetObject();
	}

	@Override
	public void resetObject() {
		enableUpdateRequired();
		this.setId(UNSET_ID);
	}
}

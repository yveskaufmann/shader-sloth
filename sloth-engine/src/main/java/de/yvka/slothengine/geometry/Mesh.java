package de.yvka.slothengine.geometry;

import de.yvka.slothengine.geometry.VertexAttributePointer.Format;
import de.yvka.slothengine.renderer.Renderer;
import de.yvka.slothengine.utils.HardwareObject;
import de.yvka.slothengine.renderer.Renderer;
import de.yvka.slothengine.utils.HardwareObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.yvka.slothengine.utils.BufferUtils;
import de.yvka.slothengine.utils.TypeSize;

import java.nio.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.lwjgl.opengl.GL11.*;

public class Mesh extends HardwareObject {

	private static final Logger Log = LoggerFactory.getLogger(Mesh.class);


	/**
	 * Specifies the kinds of primitives which could be used to render
	 * a raw model.
	 */
	public enum Mode {
		/**
		 * <p>
		 * This mode will cause the core.renderer to interpret each individual<br>
		 * vertex of the model as point.
		 * <br><br>
		 * Points are rasterized as screen-aligned squares of a given core.window-space size.<br>
		 * The size can be given by two methods:<br>
		 * <br>
		 * <ul>
		 *     <li>glEnable(GL_PROGRAM_POINT_SIZE) and set the size by the shader output variable float gl_PointSize</li>
		 *     <li>glDisable(GL_PROGRAM_POINT_SIZE) and set the size by  using glPointSize(float size)​</li>
		 * </ul>
		 * </p>
		 */
		POINTS,

		/**
		 * <p>
		 * This mode will cause the renderer to interpret each group of two successive vertices as a single line.<br>
		 * <br>
		 * Which leads to a set of non connected lines if enough vertices are specified.<br>
		 * For example: The vertices 0 and 1 are considered as line, the vertices 2 and 3 are considered as line
		 * and so on.<br>
		 * <br>
		 * If a non even number of vertices was specified the rest of the vertices will
		 * be ignored.<br>
		 * The width of the line can be specified by using glLineWidth(float width).
		 * </p>
		 */
		LINES,

		/**
		 * <p>
		 * This mode will cause the renderer to interpret the adjacent vertices as a connected line.<br>
		 * <br>
		 * If N vertices are specified the rendered line consists of N - 1 segments which implies
		 * that one Vertex leads a non rendered line. <br>
		 * <br>
		 * The width of the line can be specified by using glLineWidth(float width).
		 * </p>
		 *
		 */
		LINE_STRIP,

		/**
		 * <p>
		 * This mode acts like LINE_STRIP, except that the first and last vertices are
		 * also considered as a line.<br>
		 * <br>
		 * If N vertices are specified the rendered line consists of N segments.
		 * </p>
		 *
		 */
		LINE_LOOP,


		/**
		 * <p>
		 * This mode will cause the renderer to interpret each group of three successive vertices as a single triangle.<br>
		 * <br>
		 * Which leads to a set of non connected triangles if enough vertices are specified.<br>
		 * For example: The vertices 0, 1 and 2 are considered as triangle, the vertices 3, 4, 5 are considered as triangle
		 * and so on.<br>
		 * <br>
		 * If a number not divisible by three of vertices was specified the rest of the vertices will
		 * be ignored.N Vertices leads to N/3 triangles.
		 * </p>
		 */
		TRIANGLES,

		/**
		 * <p>
		 * This mode will cause the renderer to interpret each group of three adjacent vertices as a connected triangle.<br>
		 * <br>
		 * Which leads to a set of connected triangles if enough vertices are specified.<br>
		 * The face setTarget of strip triangles is specifies by the winding of the  first triangle.<br>
		 * <br>
		 * If a number not divisible by three of vertices was specified the rest of the vertices will
		 * be ignored. N Vertices leads to N - 2 triangles.
		 * </p>
		 */
		TRIANGLE_STRIP,

		/**
		 * <p>
		 * This mode will cause the renderer to interpret the vertex list as follows:<br>
		 * <br>
		 * The first vertex is always held fixed. From there on, every group of 2 adjacent <br>
		 * vertices form a triangle with the first. So with a vertex list, you get a list of triangles <br>
		 * like so: (0, 1, 2) (0, 2, 3), (0, 3, 4), etc. A vertex stream of n length will generate n-2 triangles.<br>
		 * </p>
		 */
		TRIANGLE_FAN,

		QUADS,
	    QUAD_STRIP;


	}

	private Map<VertexBuffer.Type, VertexBuffer> buffers = new HashMap<>();
	private Mode mode = Mode.TRIANGLES;
	private float lineSize = 1.0f;
	private float pointSize = 1.0f;
	private int vertexCount = -1;
	private int elementCount = -1;

	public Mesh() {
		super(Mesh.class);
	}

	public void setBuffer(VertexBuffer.Type type, int components, FloatBuffer buffer) {
		setBuffer(type, buffer, components, VertexAttributePointer.Format.Float);
	}

	public void setBuffer(VertexBuffer.Type type, int components, float[] buffer) {
		setBuffer(type, components, BufferUtils.createBuffer(buffer));
	}

	public void setBuffer(VertexBuffer.Type type, int components, IntBuffer buffer) {
		setBuffer(type, buffer, components, VertexAttributePointer.Format.Unsigned_Int);
	}

	public void setBuffer(VertexBuffer.Type type, int components, int[] buffer) {
		setBuffer(type, components, BufferUtils.createBuffer(buffer));
	}

	public void setBuffer(VertexBuffer.Type type, int components, ShortBuffer buffer) {
		setBuffer(type, buffer, components, VertexAttributePointer.Format.Unsigned_Short);
	}

	public void setBuffer(VertexBuffer.Type type, int components, short[] buffer) {
		setBuffer(type, components, BufferUtils.createBuffer(buffer));
	}

	public void setBuffer(VertexBuffer.Type type, int components, ByteBuffer buffer) {
		setBuffer(type, buffer, components, VertexAttributePointer.Format.Unsingned_Byte);
	}

	public void setBuffer(VertexBuffer.Type type, int components, byte[] buffer) {
		setBuffer(type, components, BufferUtils.createBuffer(buffer));
	}

	public void setBuffer(VertexBuffer.Type type, Buffer buffer, int components, Format format) {
		VertexBuffer vertexBuffer = createVertexBuffer(type);


		if (type == VertexBuffer.Type.Index) {
			format = format.getUnsignedIfPossible();
		}

		vertexBuffer.getPointer().setComponents(components);
		vertexBuffer.getPointer().setFormat(format);
		vertexBuffer.setupData(buffer);
		calculateCounts();
	}

	public VertexBuffer getBuffer(VertexBuffer.Type type) {
		return buffers.get(type);
	}


	private VertexBuffer createVertexBuffer(VertexBuffer.Type type) {
		VertexBuffer vertexBuffer = buffers.get(type);
		if (vertexBuffer == null) {
			vertexBuffer = new VertexBuffer(type);
			buffers.put(type, vertexBuffer);
		}
		return vertexBuffer;
	}

	public void setPointer(VertexBuffer.Type type, int components, int stride, int offset, Format format) {
		VertexBuffer interleavedBuffer = buffers.get(VertexBuffer.Type.Interleaved);
		if ( interleavedBuffer == null ) {
			throw new IllegalStateException("Pointers can only be set if a interleaved buffer is present");
		}
		// Create vertex buffer object for a pointer in order to simplify the
		// handling of buffers and pointers.
		VertexBuffer vertexBuffer = createVertexBuffer(type);
		VertexAttributePointer pointer = vertexBuffer.getPointer();
		pointer.setComponents(components);
		pointer.setFormat(format);
		pointer.setNormalized(false);
		pointer.setStride(stride);
		pointer.setOffset(offset);

		calculateCounts();
	}


	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public float getLineWidth() {
		return lineSize;
	}

	public void setLineSize(float lineSize) {
		this.lineSize = lineSize;
	}

	public float getPointSize() {
		return pointSize;
	}

	public void setPointSize(float pointSize) {
		this.pointSize = pointSize;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public Stream<VertexBuffer> bufferStream() {
		return buffers.values().stream();
	}

	public Iterable<VertexBuffer> getBuffers() {
		return buffers.values();
	}

	public int getElementCount() {
		return elementCount;
	}

	public void setElementCount(int elementCount) {
		this.elementCount = elementCount;
	}

	public float getLineSize() {
		return lineSize;
	}


	@Override
	public void deleteObject(Renderer renderer) {
		for (VertexBuffer buffer : buffers.values()) {
			renderer.deleteBuffer(buffer);
			buffer.resetObject();
		}
		resetObject();
	}

	@Override
	public void resetObject() {
		enableUpdateRequired();
	}

	private void calculateCounts() {
		VertexBuffer indexBuffer = getBuffer(VertexBuffer.Type.Index);
		VertexBuffer vertexBuffer = getBuffer(VertexBuffer.Type.Vertex);

		if (indexBuffer != null) {
			vertexCount  = indexBuffer.getBuffer().limit();
			elementCount = computeNumElements(vertexCount);
		} else if (vertexBuffer != null) {
			VertexBuffer interleavedBuffer = getBuffer(VertexBuffer.Type.Interleaved);
			VertexAttributePointer pointer = vertexBuffer.getPointer();

			Buffer buffer;
			int vertexComponents;

			if (interleavedBuffer != null) {
				buffer = interleavedBuffer.getBuffer();
				vertexComponents =  pointer.getStride() / TypeSize.FLOAT;
			} else {
				buffer = vertexBuffer.getBuffer();
				vertexComponents = pointer.getComponents();
			}

			if (vertexComponents == 0) {
				Log.warn("Stride and Components must be greater than zero");
				return;
			}

			vertexCount = (int) Math.ceil(buffer.limit() / vertexComponents);
			elementCount = computeNumElements(vertexCount);
		}
	}

	private int computeNumElements(int bufferSize) {
		if (bufferSize < 0) return 0;
		switch (mode) {
			case TRIANGLES:return bufferSize / 3;
			case TRIANGLE_FAN:
			case TRIANGLE_STRIP:return bufferSize - 2;
			case POINTS:return bufferSize;
			case LINES:return bufferSize / 2;
			case LINE_LOOP:return bufferSize;
			case LINE_STRIP:return bufferSize - 1;
			case QUADS: return bufferSize / 4;
			case QUAD_STRIP: return bufferSize / 4;
			default:
				throw new UnsupportedOperationException();
		}
	}
}

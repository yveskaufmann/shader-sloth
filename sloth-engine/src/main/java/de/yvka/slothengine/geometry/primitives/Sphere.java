package de.yvka.slothengine.geometry.primitives;

import de.yvka.slothengine.geometry.Mesh;
import de.yvka.slothengine.geometry.VertexAttributePointer;
import de.yvka.slothengine.geometry.VertexBuffer;
import de.yvka.slothengine.utils.BufferUtils;
import de.yvka.slothengine.utils.TypeSize;
import de.yvka.slothengine.geometry.VertexBuffer;

import static de.yvka.slothengine.math.MathUtils.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Sphere extends Mesh {
	private float radius;
	private int rings;
	private int segments;

	/**
	 * Creates a Sphere mesh with the following parameters:
	 *
	 * @param radius
	 * 				the radius of the sphere
	 * @param rings
	 * 				the count of rings
	 * @param segments
	 * 				the count of segments
     */
	public Sphere(float radius, int rings, int segments) {
		setup(radius, rings, segments);

	}

	private void setup(float radius, int rings, int segments) {

		if (rings < 3) {
			throw new IllegalArgumentException("Rings must be greater or equal 3");
		}

		if (segments < 3) {
			throw new IllegalArgumentException("Segments must be greater or equal 3");
		}


		rings++;
		segments++;

		this.radius = radius;
		this.rings = rings;
		this.segments = segments;

		final float R = 1.0f / (float) (rings - 1);
		final float S = 1.0f / (float) (segments - 1);
		int r, s;

		//V //N //UVs
		int bufferSize = rings * segments * (3 + 3 + 2);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(bufferSize);
		IntBuffer index = BufferUtils.createIntBuffer(rings * segments * 6);


		for (r = 0; r < rings; r++) {
			for (s = 0; s < segments; s++) {
				float y = (float) Math.sin(-PI_2 + PI * r * R);
				float x = (float) (Math.cos(2.0f * PI * s * S) * Math.sin(PI * r * R));
				float z = (float) (Math.sin(2.0 * PI * s * S) * Math.sin(PI * r * R));

				// Position Vector
				buffer.put(x * radius);
				buffer.put(y * radius);
				buffer.put(z * radius);

				// Normal Vector
				buffer.put(x);
				buffer.put(y);
				buffer.put(z);

				// Texture Coordinates
				buffer.put(s * S);
				buffer.put(r * R);
			}
		}
		buffer.flip();

		for(r = 0; r < rings-1; r++) {
			for (s = 0; s < segments - 1; s++) {
				index.put((r + 1) * segments + (s + 1));
				index.put(r * segments + (s + 1));
				index.put(r * segments + s);

				index.put((r) * segments + s);
				index.put((r + 1) * segments + s);
				index.put((r + 1) * segments + (s + 1));

			}
		}
		index.flip();


		int vertexOffset = 0;
		int normalOffset = 3 * TypeSize.FLOAT;
		int uvOffset = normalOffset * 2;
		int stride = uvOffset + TypeSize.FLOAT * 2;

		setMode(Mode.TRIANGLES);
		setBuffer(VertexBuffer.Type.Interleaved, 3, buffer);
		setBuffer(VertexBuffer.Type.Index, 3, index);
		setPointer(VertexBuffer.Type.Vertex, 3, stride, vertexOffset, VertexAttributePointer.Format.Float);
		setPointer(VertexBuffer.Type.Normal, 3, stride, normalOffset , VertexAttributePointer.Format.Float);
		setPointer(VertexBuffer.Type.TextCoords, 2, stride, uvOffset, VertexAttributePointer.Format.Float);
	}

	public float getRadius() {
		return radius;
	}

	public int getRings() {
		return rings;
	}

	public int getSegments() {
		return segments;
	}
}

package de.yvka.slothengine.geometry.primitives;

import de.yvka.slothengine.geometry.Mesh;
import de.yvka.slothengine.geometry.VertexBuffer;
import de.yvka.slothengine.geometry.VertexBuffer;

public class Plane extends Mesh {

	public Plane() {
		setupData();
	}

	private void setupData() {
		setBuffer(VertexBuffer.Type.Vertex, 2, new float[] {
			-1.0f, 1.0f,
			-1.0f, -1.0f,
			1.0f, -1.0f,

			1.0f, -1.0f,
			1.0f, 1.0f,
			-1.0f, 1.0f,
		});

		setBuffer(VertexBuffer.Type.Color, 3, new float[] {
			1.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
		});
	}
}

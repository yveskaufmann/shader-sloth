package de.yvka.slothengine.geometry.primitives;

import de.yvka.slothengine.geometry.Mesh;
import de.yvka.slothengine.geometry.VertexAttributePointer;
import de.yvka.slothengine.geometry.VertexBuffer;
import de.yvka.slothengine.utils.BufferUtils;
import de.yvka.slothengine.utils.TypeSize;
import de.yvka.slothengine.geometry.Mesh;
import de.yvka.slothengine.geometry.VertexAttributePointer;
import de.yvka.slothengine.geometry.VertexBuffer;
import de.yvka.slothengine.utils.BufferUtils;
import de.yvka.slothengine.utils.TypeSize;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Cube extends Mesh {

	public Cube() {
		setupData();
	}

	private void setupData() {
		/***
		 *  A---D    A---D      D
		 *	|  /|    |  /      /|
		 *	| / | -> | /  +   / |
		 *	|/  |    |/      /  |
		 *	B---C    B      B---C
		 *
		 *    A'--D'
		 *   /|  /|
		 *  A---D |
		 *	| / | C'
		 *	|/  |/
		 *	B---C
		 *
		 */
		FloatBuffer interleavedBuffer = BufferUtils.createBuffer(new float[] {
			//    Positions	       Normals           UVs
			-1.0f,-1.0f,-1.0f, 0.0f,-1.0f, 0.0f, 0.0f, 0.0f,
			 1.0f,-1.0f,-1.0f, 0.0f,-1.0f, 0.0f, 1.0f, 0.0f,
			-1.0f,-1.0f, 1.0f, 0.0f,-1.0f, 0.0f, 0.0f, 1.0f,
			 1.0f,-1.0f, 1.0f, 0.0f,-1.0f, 0.0f, 1.0f, 1.0f,
			-1.0f, 1.0f,-1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
			-1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
			 1.0f, 1.0f,-1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
			 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
			-1.0f,-1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
			 1.0f,-1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
			-1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
			 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
			-1.0f,-1.0f,-1.0f, 0.0f, 0.0f,-1.0f, 0.0f, 0.0f,
			-1.0f, 1.0f,-1.0f, 0.0f, 0.0f,-1.0f, 0.0f, 1.0f,
			 1.0f,-1.0f,-1.0f, 0.0f, 0.0f,-1.0f, 1.0f, 0.0f,
			 1.0f, 1.0f,-1.0f, 0.0f, 0.0f,-1.0f, 1.0f, 1.0f,
			-1.0f,-1.0f, 1.0f,-1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
			-1.0f, 1.0f,-1.0f,-1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
			-1.0f,-1.0f,-1.0f,-1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			-1.0f, 1.0f, 1.0f,-1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
			 1.0f,-1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
			 1.0f,-1.0f,-1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
			 1.0f, 1.0f,-1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f
		});


		IntBuffer indicesBuffer = BufferUtils.createBuffer(new int[] {
			// Bottom
			0, 1, 2,
			1, 3, 2,

			// Top
			4, 5, 6,
			6, 5, 7,

			// Front
			8, 9, 10,
			9, 11, 10,

			// Back
			12, 13, 14,
			14, 13, 15,

			// Left
			16, 17, 18,
			16, 19, 17,

			// Right
			20, 21, 22,
			20, 22, 23
		});

		/**
		 * Interleaved buffer layout:
		 *
		 * V1 V2 V3 N1 N2 N3 UV1 UV2
		 */
		int offsetVertex = 0;
		int offsetNormal = 3 * TypeSize.FLOAT;
		int offsetUVs =  2 * offsetNormal;
		int stride = offsetUVs + 2 * TypeSize.FLOAT;

		setMode(Mesh.Mode.TRIANGLES);
		setBuffer(VertexBuffer.Type.Index, 3, indicesBuffer);
		setBuffer(VertexBuffer.Type.Interleaved, 3, interleavedBuffer);

		setPointer(VertexBuffer.Type.Vertex, 3, stride, offsetVertex, VertexAttributePointer.Format.Float);
		setPointer(VertexBuffer.Type.Normal, 3, stride, offsetNormal, VertexAttributePointer.Format.Float);
		setPointer(VertexBuffer.Type.TextCoords, 2, stride, offsetUVs, VertexAttributePointer.Format.Float);

	}
}

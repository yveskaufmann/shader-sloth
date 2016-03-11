package de.yvka.slothengine.geometry.loader;

import de.yvka.slothengine.geometry.Mesh;
import de.yvka.slothengine.geometry.VertexAttributePointer;
import de.yvka.slothengine.geometry.VertexBuffer;
import de.yvka.slothengine.utils.BufferUtils;
import de.yvka.slothengine.geometry.Mesh;
import de.yvka.slothengine.geometry.VertexAttributePointer;
import de.yvka.slothengine.geometry.VertexBuffer;
import de.yvka.slothengine.utils.BufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.yvka.slothengine.utils.TypeSize;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class WaveFrontObjectLoader implements MeshLoader {

	private final Logger Log = LoggerFactory.getLogger(WaveFrontObjectLoader.class);

	private static final String[] SUPPORTED_EXTENSIONS = {"obj"};
	private static final char COMMENT_TAG = '#';
	private static final char VERTEX_TAG = 'v';
	private static final char NORMAL_TAG = 'n';
	private static final char UV_TAG = 't';
	private static final char FACE_TAG = 'f';

	private int indexSize;
	private int lineNumber;
	private int stride;
	private boolean containsVertices;
	private boolean containsUvs;
	private boolean containsNormals;

	@Override
	public boolean canHandle(String extension) {
		return SUPPORTED_EXTENSIONS[0].equals(extension);
	}

	public Mesh load(InputStream in) {
		List<Float> vertices = new ArrayList<>();
		List<Float> normals = new ArrayList<>();
		List<Float> uvs = new ArrayList<>();
		List<Integer>  index = new ArrayList<>();
		Mesh mesh = new Mesh();
		FloatBuffer buffer;

		try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			parseFile(reader, vertices, normals, uvs, index);
			buffer = buildInterleavedBuffer(vertices, normals, uvs, index);
			if (Log.isDebugEnabled()) {
				Log.debug("Object File Info: \n" + String.join("\n",
					"Vertices: " + vertices.size(),
					"Normals: " + normals.size(),
					"TextureCoordinates: " + uvs.size(),
					"Indexes: "  + index.size())
				);
			}
			setupMesh(mesh, buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mesh;
	}

	private FloatBuffer buildInterleavedBuffer(List<Float> vertices, List<Float> normals, List<Float> uvs, List<Integer> indexes) {
		int componentsPerIndex = 0;
		stride = 0;

		if (containsVertices) {
			stride += 3;
			componentsPerIndex++;
		}

		if (containsNormals) {
			stride += 3;
			componentsPerIndex++;
		}

		if (containsUvs) {
			stride += 2;
			componentsPerIndex++;
		}

		if (stride == 0) {
			throw new  MeshLoaderException("No Faces information provided import not possible");
		}

		int indexCount = indexes.size();
		int bufferSize = (indexCount / componentsPerIndex)  * stride;
		FloatBuffer buffer = BufferUtils.createFloatBuffer(bufferSize);

		for (int i = 0; i < indexCount;) {
			int index;
			if (containsVertices) {
				index = indexes.get(i) * 3;
				buffer.put(vertices.get(index));
				buffer.put(vertices.get(index + 1));
				buffer.put(vertices.get(index + 2));
				i+=1;
			}

			if (containsUvs) {
				index = indexes.get(i) * 2;
				buffer.put(uvs.get(index));
				buffer.put(uvs.get(index + 1));
				i+=1;
			}

			if (containsNormals) {
				index = indexes.get(i) * 3;
				buffer.put(normals.get(index));
				buffer.put(normals.get((index + 1)));
				buffer.put(normals.get((index + 2)));
				i+=1;
			}
		}
		buffer.flip();
		stride *= TypeSize.FLOAT;
		return buffer;
	}

	private void setupMesh(Mesh mesh, FloatBuffer buffer) {
		mesh.setMode(Mesh.Mode.TRIANGLES);
		mesh.setBuffer(VertexBuffer.Type.Interleaved, 3, buffer);

		int offset = 0;
		if (containsVertices) {
			mesh.setPointer(VertexBuffer.Type.Vertex, 3, stride, offset, VertexAttributePointer.Format.Float);
			offset += TypeSize.FLOAT * 3;
		}

		if (containsUvs) {
			mesh.setPointer(VertexBuffer.Type.TextCoords, 2, stride, offset,  VertexAttributePointer.Format.Float);
			offset += TypeSize.FLOAT * 2;
		}

		if (containsNormals) {
			mesh.setPointer(VertexBuffer.Type.Normal, 3, stride, offset,  VertexAttributePointer.Format.Float);
			offset += TypeSize.FLOAT * 3;
		}
	}

	private void parseFile(BufferedReader reader, List<Float> vertices, List<Float> normals, List<Float> uvs, List<Integer> index) throws IOException {
		String line;
		char firstChar;
		char secondChar;
		String[] components;
		indexSize = -1;
		lineNumber = 0;
		containsVertices = false;
		containsNormals = false;
		containsUvs = false;

		while((line = reader.readLine()) != null) {
			lineNumber++;
			int len = line.length();
			firstChar = len >= 1 ? line.charAt(0): ' ';
			secondChar = len >= 2 ? line.charAt(1):' ';

			switch (firstChar) {
				case ' ':
				case COMMENT_TAG:
					continue;
				case VERTEX_TAG:
					components = line.split("\\s+");
					switch (secondChar) {
						case NORMAL_TAG:
							parseComponents(3, components, normals);
							break;
						case UV_TAG:
							parseComponents(2, components, uvs);
							break;
						default:
							parseComponents(3, components, vertices);
					}
					break;
				case FACE_TAG:
					components = line.split("\\s");
					if (components.length == 4) {
						parseFaceIndex(components, index);
					} else {
						throw new MeshLoaderException("This Loader can parseFile faces only.");
					}
					break;
			}
		}
	}

	private void parseComponents(int count, String[] components, List<Float> writeTo) {
		// Offset +1 is required because components contains the tag too.
		if (components.length < count + 1) throw new MeshLoaderException("In Line %d are not enough components found", lineNumber);
		for (int i = 0; i < count; i++) {
			writeTo.add(Float.valueOf(components[i + 1]));
		}
	}

	private void parseFaceIndex(String[] triangleComponents, List<Integer> indexList) {
		for (int i = 1; i < triangleComponents.length; i++) {
			String triangleVertexIndex = triangleComponents[i];
			String []indexes = triangleVertexIndex.split("/");
			if (indexSize == -1) {
				indexSize = indexes.length;
				containsVertices = indexSize > 0 && indexes[0].length() > 0;
				containsUvs = indexSize > 1 && indexes[1].length() > 0;
				containsNormals = indexSize > 2 && indexes[2].length() > 0;
				Log.debug("Components Per Index " + indexSize);
			} else if (indexSize != indexes.length) {
				throw new MeshLoaderException("Found in consistent faces definition in line %d.", lineNumber);
			}

			for (String index : indexes) {
				if (index.length() == 0) continue;
				indexList.add((Integer.valueOf(index) - 1));
			}
		}
	}
}

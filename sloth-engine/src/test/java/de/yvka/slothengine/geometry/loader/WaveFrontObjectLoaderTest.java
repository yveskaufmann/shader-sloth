package de.yvka.slothengine.geometry.loader;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WaveFrontObjectLoaderTest {

	@Test
	public void parseFile() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		WaveFrontObjectLoader objectLoader = new WaveFrontObjectLoader();
		URL cube = getClass().getResource("cube.obj");
		List<Float> vertices = new ArrayList<>();
		List<Float> normals = new ArrayList<>();
		List<Float> uvs = new ArrayList<>();
		List<Integer> index = new ArrayList<>();

		try(BufferedReader reader = new BufferedReader(new InputStreamReader(cube.openStream()))) {
			invokeParse(objectLoader, vertices, normals, uvs, index, reader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertEquals(8 * 3, vertices.size());
		assertEquals(6 * 3, normals.size());
		assertEquals(0, uvs.size());
		// 6 Faces * 2 Triangles * (3 Vertices + 3 Normals + 0 Coordinates)
		assertEquals(6 * 2 * (3 + 3), index.size());
	}

	private void invokeParse(WaveFrontObjectLoader objectLoader, List<Float> vertices, List<Float> normals, List<Float> uvs, List<Integer> index, BufferedReader reader) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    /* Calls the following private method:
     * objectLoader.parseFile(reader, vertices, normals, uvs, index);
     */
		Class<? extends WaveFrontObjectLoader> type = objectLoader.getClass();
		Method method = type.getDeclaredMethod("parseFile", BufferedReader.class, List.class, List.class, List.class, List.class);
		method.setAccessible(true);
		method.invoke(objectLoader, reader, vertices, normals, uvs, index);
	}

}

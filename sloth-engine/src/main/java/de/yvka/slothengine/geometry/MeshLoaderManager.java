package de.yvka.slothengine.geometry;

import de.yvka.slothengine.geometry.loader.MeshLoader;
import de.yvka.slothengine.geometry.loader.MeshLoaderException;
import de.yvka.slothengine.geometry.loader.WaveFrontObjectLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A mesh loader is responsible to manage the loading of meshes.
 *
 * Which means that the manager is responsible to choose a
 * ModelLoader which can handle the loading of a specific mesh file.
 */
public class MeshLoaderManager {

	/**
	 * The directory which is used to lookup for model files.
	 */
	private static final String MODEL_DIRECTORY = "assets/models";

	/**
	 * The concrete MeshLoaders which are capable to load a mesh file.
	 */
	private static final List<MeshLoader> loaders =  new ArrayList<>();

	static {
		registerLoader(WaveFrontObjectLoader.class);
	}

	/**
	 * Register a MeshLoader Implementation in order to
	 * add mesh loaders. Which can be used by this manager
	 * to load mesh files.
	 *
	 * @param loaderClass the class which implements the MeshLoader interface.
	 *
	 * @throws MeshLoaderException if the loaderClass don't implements MeshLoader
	 * or the loader class don't provide a non-arg public constructor.
	 */
	public static void registerLoader(Class<? extends  MeshLoader> loaderClass) {

		if (! MeshLoader.class.isAssignableFrom(loaderClass)) {
			throw new MeshLoaderException("A mesh load must implements " + MeshLoader.class.getName());
		}

		MeshLoader loader;
		try {
			loader = loaderClass.newInstance();
			loaders.add(loader);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new MeshLoaderException("Loader don't provide a public non-arg constructor");
		}
	}


	/**
	 * Load a mesh file from the <code>assets/models</code> directory
	 * into a mesh object.
	 *
	 * @param name
	 * 			the name of the mesh file with it's extension e.g: cube.obj.
	 *
	 * @return
	 * 			the mesh object which contains the data from the specified mesh file.
	 *
	 * @throws MeshLoaderException if the mesh file could not be imported as mesh object.
     */
	public Mesh loadMesh(String name) {
		MeshLoader loader = getLoaderByExtension(name);
		return loadMesh(name, loader);
	}

	private Mesh loadMesh(String name, MeshLoader loader) {
		String path = MODEL_DIRECTORY + "/" + name;
		try {
			try (FileInputStream in = new FileInputStream(path)) {
				return loader.load(in);
            }
		} catch (IOException ex) {
			throw new MeshLoaderException("The specified mesh file " + path + "could not be loaded." ,ex);
		}
	}

	private MeshLoader getLoaderByExtension(String name) {
		String extension = "";

		// Obtain the extension of the model file
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex > 0) {
			extension = name.substring(dotIndex + 1);
		}

		for(MeshLoader loader : loaders) {
			if (loader.canHandle(extension)) {
				return loader;
			}
		}

		throw new MeshLoaderException("No loader supports the mesh format:" + extension);
	}

}

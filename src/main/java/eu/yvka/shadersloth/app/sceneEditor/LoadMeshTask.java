package eu.yvka.shadersloth.app.sceneEditor;

import eu.yvka.slothengine.engine.Engine;
import eu.yvka.slothengine.geometry.Mesh;
import eu.yvka.slothengine.geometry.loader.MeshLoaderException;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @Author Yves Kaufmann
 * @since 12.07.2016
 */
public class LoadMeshTask extends Task<Mesh> {

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/
	private static final Logger Log = LoggerFactory.getLogger(LoadMeshTask.class);
	private File meshToLoad;

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	/**
	 * Creates a tasks for loading a mesh.
	 *
	 * @param pathToMesh
     */
	public LoadMeshTask(String pathToMesh) {
		this(new File(pathToMesh));
	}

	/***
	 * Creates a tasks for loading a mesh
	 * @param meshToLoad
     */
	public LoadMeshTask(File meshToLoad) {
		this.meshToLoad = meshToLoad;
	}

	@Override
	protected Mesh call() throws Exception {
		String path = meshToLoad.getAbsolutePath();
		try {
			set(Engine.getMesh(path));
			Log.info("Loaded mesh from " + path);
		} catch (MeshLoaderException ex) {
			Log.info("Failed to load mesh from " + path, ex);
			setException(ex);
			throw ex;
		}
		return getValue();
	};
}

package de.yvka.slothengine.geometry;

import de.yvka.slothengine.engine.EngineComponent;
import de.yvka.slothengine.engine.EngineComponent;

import java.util.HashMap;
import java.util.Map;

public class MeshRepository implements EngineComponent {

	private final Map<String, Mesh> meshes = new HashMap<>();
	private final MeshLoaderManager meshLoader = new MeshLoaderManager();
	private boolean initialized;

	public MeshRepository() {
	}


	/**
	 * Get the mesh which is stored in <code>'filename'</code>.
	 * This method ensures that the mesh is loaded only once and
	 * that only one instance per mesh file is created.
	 *
	 * @param fileName the desired filename which contains the mesh data
	 * @return the desired mesh.
     */
	public Mesh getMesh(String fileName) {
		return getMesh(fileName, true);
	}

	/**
	 * Get the mesh with which is stored in <code>'filename'</code>
	 * if the mesh was already loaded and <code>getCacheMesh</code>
	 * is true the the same mesh instance is returned. When it is desired
	 * a reload of the mesh can be achieved by setting getCacheMesh to
	 * <code>false</code>.
	 *
	 * @param fileName the desired filename which contains the mesh data
	 * @param getCachedMesh if true then the mesh will not reloaded
     * @return the already or reloaded mesh.
     */
	public Mesh getMesh(String fileName, boolean getCachedMesh) {
		Mesh mesh = meshes.get(fileName);
		if (mesh == null || !getCachedMesh) {
			mesh = meshLoader.loadMesh(fileName);
			meshes.put(fileName, mesh);
		}
		return mesh;
	}

	@Override
	public void initialize() {
		initialized = true;
	}

	@Override
	public void shutdown() {
		initialized = false;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}
}

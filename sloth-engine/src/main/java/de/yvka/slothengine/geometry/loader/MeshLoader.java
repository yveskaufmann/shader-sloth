package de.yvka.slothengine.geometry.loader;

import de.yvka.slothengine.geometry.Mesh;

import java.io.InputStream;

public interface MeshLoader {

	boolean canHandle(String extension);
	Mesh load(InputStream in);
}

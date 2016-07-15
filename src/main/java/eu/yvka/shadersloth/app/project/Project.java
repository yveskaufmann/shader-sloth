package eu.yvka.shadersloth.app.project;

import eu.yvka.slothengine.material.Material;
import eu.yvka.slothengine.scene.Scene;
import eu.yvka.slothengine.texture.Texture;
import eu.yvka.slothengine.utils.NameAlreadyInUseException;

import java.io.File;
import java.util.List;

/**
 * Interface of a shader sloth project.
 *
 * @Author Yves Kaufmann
 * @since 14.07.2016
 */
public interface Project {

	String getName();
	void setName(String name) throws NameAlreadyInUseException;

	File getProjectFolder();

	Scene getScene();

	Material createMaterial(String name) throws NameAlreadyInUseException;
	List<Material> getMaterials();
	Material getMaterial(String materialName);
	Material removeMaterial(Material material);
	Material removeMaterial(String materialName);

	Texture createTexture(String name) throws NameAlreadyInUseException;
	List<Texture> getTextures();
	Texture getTexture(String materialName);
	Texture removeTexture(Texture material);
	Texture removeTexture(String materialName);
}

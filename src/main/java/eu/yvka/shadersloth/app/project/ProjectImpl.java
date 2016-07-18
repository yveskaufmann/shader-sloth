package eu.yvka.shadersloth.app.project;

import eu.yvka.shadersloth.app.AppFolder;
import eu.yvka.slothengine.material.BasicMaterial;
import eu.yvka.slothengine.material.Material;
import eu.yvka.slothengine.scene.Scene;
import eu.yvka.slothengine.texture.Texture;
import eu.yvka.slothengine.texture.TextureManager;
import eu.yvka.slothengine.utils.NameAlreadyInUseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Yves Kaufmann
 * @since 14.07.2016
 */
public class ProjectImpl implements Project {

	String name;
	File projectFolder;

	private List<Material> materials = new ArrayList<>();
	private List<Texture> textures = new ArrayList<>();
	private Scene scene = new Scene();

	@Override
	public String getName() {
		return name;
	}


	@Override
	public void setName(String name) throws NameAlreadyInUseException {
		final File projectFolder = new File(AppFolder.getProjectsFolder(), name);
		this.projectFolder = projectFolder;
		this.name = name;
	}

	@Override
	public File getProjectFolder() {
		return projectFolder;
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public Material createMaterial(String name) throws NameAlreadyInUseException {
		if (isMaterialNameInUse(name)) {
			throw new NameAlreadyInUseException(name);
		}
		final Material material = new BasicMaterial();
		material.setMaterialName(name);
		materials.add(material);
		return material;
	}

	@XmlList
	@Override
	public List<Material> getMaterials() {
		return materials;
	}

	@Override
	public Material getMaterial(String materialName) {
		assert materialName != null;

		for (Material material : materials) {
			if ( materialName.equals(material.getMaterialName())) {
				return material;
			}
		}
		return null;
	}

	@Override
	public Material removeMaterial(Material material) {
		if (materials.contains(material)) {
			materials.remove(material);
			return material;
		}
		return null;
	}

	@Override
	public Material removeMaterial(String materialName) {
		final Material materialToRemove = getMaterial(materialName);
		return removeMaterial(materialToRemove);
	}

	@Override
	public Texture createTexture(String name) throws NameAlreadyInUseException {
		if (isTextureNameInUse(name)) {
			throw new NameAlreadyInUseException(name);
		}
		// TODO: allow image loading
		// Allows it to create textures by passing a image
		// final Texture texture = new TextureManager().createTexture();

		return null;
	}

	@Override
	public List<Texture> getTextures() {
		return textures;
	}

	@Override
	public Texture getTexture(String textureName) {
		assert textureName != null;

		for (Texture texture : textures) {
			if ( textureName.equals(texture.getName())) {
				return texture;
			}
		}
		return null;

	}

	@Override
	public Texture removeTexture(Texture texture) {
		if (textures.contains(texture)) {
			textures.remove(texture);
			return texture;
		}
		return null;
	}

	@Override
	public Texture removeTexture(String textureName) {
		final Texture textureToRemove = getTexture(textureName);
		return removeTexture(textureToRemove);
	}

	/******************************************************************************
	 *
	 * Internal Api
	 *
	 ******************************************************************************/

	private void ensureProjectNameIsUnused(File file) throws NameAlreadyInUseException {
		if (file.exists() && file.isDirectory()) {
			throw new NameAlreadyInUseException(file.getAbsolutePath());
		}
	}

	private boolean isMaterialNameInUse(String name) {
		for (Material material : materials) {
			if (name.equals(material.getMaterialName())) return true;
		}
		return false;
	}

	private boolean isTextureNameInUse(String name) {
		for (Texture texture : textures) {
			if (name.equals(texture.getName())) return true;
		}
		return false;
	}
}

package eu.yvka.shadersloth.model;

import eu.yvka.slothengine.geometry.Mesh;
import eu.yvka.slothengine.material.Material;
import eu.yvka.slothengine.scene.Scene;
import eu.yvka.slothengine.scene.light.Light;
import eu.yvka.slothengine.shader.Shader;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @Author Yves Kaufmann
 * @since 29.06.2016
 */
public class Project {

	/**
	 * The name of this project
	 */
	private StringProperty name = new SimpleStringProperty(this, "name");

	/**
	 * The directory of this project
	 */
	private StringProperty projectPath = new SimpleStringProperty(this, "projectPath");

	/**
	 * The scene of this project
	 */
	private ObjectProperty<Scene> scene = new SimpleObjectProperty<>(this, "scene");

	/**
	 * List of lights of this project
	 */
	private ObservableList<Light> lights = FXCollections.observableArrayList();

	/***
	 * List of models of this project
	 */
	private ObservableList<Mesh> models = FXCollections.observableArrayList();

	/***
	 * List of shaders of this project
	 */
	private ObservableList<Shader> shaders = FXCollections.observableArrayList();

	/***
	 * List of materials of this project
	 */
	private ObservableList<Material> materials = FXCollections.observableArrayList();


	public Project() {

	}

	public String getName() {
		return name.get();
	}

	public StringProperty nameProperty() {
		return name;
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getProjectPath() {
		return projectPath.get();
	}

	public StringProperty projectPathProperty() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath.set(projectPath);
	}

	public Scene getScene() {
		return scene.get();
	}

	public ObjectProperty<Scene> sceneProperty() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene.set(scene);
	}

	public ObservableList<Light> getLights() {
		return lights;
	}

	public void setLights(ObservableList<Light> lights) {
		this.lights = lights;
	}

	public ObservableList<Mesh> getModels() {
		return models;
	}

	public void setModels(ObservableList<Mesh> models) {
		this.models = models;
	}

	public ObservableList<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(ObservableList<Material> materials) {
		this.materials = materials;
	}

	public ObservableList<Shader> getShaders() {
		return shaders;
	}

	public void setShaders(ObservableList<Shader> shaders) {
		this.shaders = shaders;
	}
}

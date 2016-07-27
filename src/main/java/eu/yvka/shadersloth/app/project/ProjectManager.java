package eu.yvka.shadersloth.app.project;

import eu.yvka.shadersloth.app.AppFolder;
import eu.yvka.shadersloth.app.materialEditor.shaders.ShaderTemplateHelper;
import eu.yvka.shadersloth.share.I18N.I18N;
import eu.yvka.shadersloth.share.controls.FileAlreadyExistsDialog;
import eu.yvka.slothengine.geometry.Mesh;
import eu.yvka.slothengine.geometry.primitives.Cube;
import eu.yvka.slothengine.material.BasicMaterial;
import eu.yvka.slothengine.material.MaterialManager;
import eu.yvka.slothengine.math.Color;
import eu.yvka.slothengine.scene.Geometry;
import eu.yvka.slothengine.scene.Scene;
import eu.yvka.slothengine.scene.camera.FreeCamera;
import eu.yvka.slothengine.scene.light.PointLight;
import eu.yvka.slothengine.shader.Shader;
import eu.yvka.slothengine.utils.NameAlreadyInUseException;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import static eu.yvka.shadersloth.share.I18N.I18N.getString;

/**
 * @Author Yves Kaufmann
 * @since 14.07.2016
 */
public class ProjectManager {

	private static final Logger Log = LoggerFactory.getLogger(ProjectManager.class);

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	/***
	 * The current active project
     */
	private ObjectProperty<Project> currentProject;


	/******************************************************************************
	 *
	 * Methods
	 *
	 ******************************************************************************/

	/***
	 * Creates a empty project
	 *
	 * @return
     */
	public Project createNewProject()  {
		boolean isInitialProject = currentProject == null || currentProject.get() == null;
		ProjectCreateDialog projectCreateDialog = new ProjectCreateDialog(isInitialProject);
		Optional<String> projectNameOpt = projectCreateDialog.showAndWait();
		if (projectNameOpt.isPresent()) {
			final String projectName = projectNameOpt.get();
			final ProjectImpl project = new ProjectImpl();
			project.setName(projectName);

			// remove the old project folder if it exists
			File projectFolder = project.getProjectFolder();
			if (projectFolder.exists()) {
				try {
					FileUtils.deleteDirectory(projectFolder);
				} catch (IOException e) {
					Log.warn("Failed to remove old project folder");
					return null;
				}
			}

			if (projectFolder.mkdirs()) {
				setCurrentProject(project);
				Shader defaultShader = ShaderTemplateHelper.createShader(projectFolder, "Phong", true);
				BasicMaterial mat = new BasicMaterial(defaultShader);

				Geometry geometry = new Geometry("Box");
				geometry.setMesh(new Cube());
				geometry.setScale(1.0f);
				geometry.setMaterial(mat);

				PointLight pointLight = new PointLight("PointLight");
				pointLight.setPosition(new Vector3f(0.0f, 2.0f, -2.0f));
				pointLight.setColor(new Color(1.0f, 0.0f, 0.0f, 1.0f));
				pointLight.setAttenuation(100.0f);


				final Scene scene = project.getScene();
				scene.getCamera().setPosition(5f, 5f, 5f);
				if (scene.getCamera() instanceof FreeCamera) {
					((FreeCamera) scene.getCamera()).setTarget(new Vector3f(0.0f, 0.0f, 0.0f));
				}

				scene.add(geometry);
				scene.add(pointLight);
				project.getMaterials().add(mat);

				return project;
			}
		}

		return null;
	};


	/**
	 * Save the specified project.
	 *
	 * @param project the project to save.
     */
	public void save(Project project) {

	}

	/**
	 * Load the specified project.
	 *
	 * @param projectDirectory the directory containing project
	 * @return the loaded project or null if the loading failed.
     */
	public Project load(File projectDirectory) {
		return null;
	}

	/******************************************************************************
	 *
	 * Properties
	 *
	 ******************************************************************************/

	/***
	 * Retrieve the current project.
	 *
	 * @return the currently active project
     */
	public Project getCurrentProject() {
		return currentProjectProperty().get();
	}

	/***
	 * Returns a property which contains
	 * the current active project.
	 *
	 * @return a property which contains the current project
     */
	public ObjectProperty<Project> currentProjectProperty() {
		if (currentProject == null) {
			currentProject = new SimpleObjectProperty<>(this, "currentProject", null);
		}
		return currentProject;
	}

	/***
	 * Specifies the current active project.
	 *
	 * @param currentProject the currently active project
	 */
	public void setCurrentProject(Project currentProject) {
		currentProjectProperty().set(currentProject);
	}


}

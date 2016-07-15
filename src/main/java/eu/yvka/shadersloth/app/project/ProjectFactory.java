package eu.yvka.shadersloth.app.project;

import eu.yvka.slothengine.utils.NameAlreadyInUseException;

import java.io.File;
import java.io.IOException;

/**
 * @Author Yves Kaufmann
 * @since 14.07.2016
 */
public class ProjectFactory {

	public Project createEmptyProject(String name) throws NameAlreadyInUseException {
		final ProjectImpl project = new ProjectImpl();
		project.setName(name);
		return project;
	};

	public Project openProject(File file) throws NameAlreadyInUseException, IOException {
		// TODO: load project
		return  null;
	};
}

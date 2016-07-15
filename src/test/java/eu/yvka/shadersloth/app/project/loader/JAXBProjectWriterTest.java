package eu.yvka.shadersloth.app.project.loader;

import eu.yvka.shadersloth.app.project.Project;
import eu.yvka.shadersloth.app.project.ProjectImpl;
import eu.yvka.slothengine.material.Material;
import eu.yvka.slothengine.scene.Scene;
import eu.yvka.slothengine.texture.Texture;
import eu.yvka.slothengine.utils.NameAlreadyInUseException;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author Yves Kaufmann
 * @since 15.07.2016
 */
public class JAXBProjectWriterTest {

	@Test
	public void saveProject() throws Exception {
		Project project = new ProjectImpl();
		project.setName("test");

		JAXBProjectWriter writer = new JAXBProjectWriter();
		writer.saveProject(project);


	}

}

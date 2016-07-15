package eu.yvka.shadersloth.app.project.loader;

import eu.yvka.shadersloth.app.project.Project;
import eu.yvka.shadersloth.app.project.ProjectImpl;
import eu.yvka.shadersloth.app.project.ProjectLoader;
import eu.yvka.shadersloth.app.project.ProjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;

/**
 *
 * @Author Yves Kaufmann
 * @since 15.07.2016
 */
public class JAXBProjectWriter implements ProjectWriter {

	private static final Logger Log = LoggerFactory.getLogger(JAXBProjectWriter.class);

	private JAXBContext jaxbContext;
	private Marshaller projectMarshaller;

	public JAXBProjectWriter() {
		try {
			jaxbContext = JAXBContext.newInstance(ProjectImpl.class);
			projectMarshaller = jaxbContext.createMarshaller();
		} catch (JAXBException ex ){
			throw new IllegalStateException("Failed to initiate jaxbContext");
		}
	}


	@Override
	public void saveProject(Project project) throws IOException {
		final File projectFolder = project.getProjectFolder();
		final File projectFile = new File(projectFolder, ProjectLoader.DEFAULT_PROJECT_FILE);

		if (! projectFolder.isDirectory()) {
			projectFolder.mkdirs();
		}

		if (! projectFile.exists()) {
			projectFile.createNewFile();
		}

		try {
			projectMarshaller.marshal(project, projectFile);
		} catch (JAXBException e) {
			Log.error("Failed to marshal project folder to " + projectFile.getAbsolutePath(), e);
			throw new IOException("Failed to save project", e);
		}
	}
}

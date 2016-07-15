package eu.yvka.shadersloth.app.project;

import javax.xml.bind.JAXBContext;
import java.io.File;
import java.io.IOException;

/**
 * @Author Yves Kaufmann
 * @since 15.07.2016
 */
public interface ProjectWriter {
	public void saveProject(Project project) throws IOException;
}

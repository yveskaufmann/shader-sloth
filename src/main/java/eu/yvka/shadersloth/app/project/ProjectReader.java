package eu.yvka.shadersloth.app.project;

import java.io.File;
import java.io.IOException;

/**
 * @Author Yves Kaufmann
 * @since 15.07.2016
 */
public interface ProjectReader {

	public static final String DEFAULT_PROJECT_FILE = "project.xml";

	Project loadProject(File file) throws IOException;
}

package eu.yvka.shadersloth.app;

import eu.yvka.shadersloth.share.utils.OperatingSystem;

import java.io.File;

/**
 * Created by fxdapokalypse on 10.06.2016.
 */
public class AppFolder {


	private static final String SHADER_SLOTH_APP_DIR = "ShaderSloth";
	private static final String PROJECTS_FOLDER = "projects";
	private static final String ASSETS_FOLDER = "assets";
	private static final String ASSETS_TEXTURE_FOLDER = "textures";
	private static final String ASSETS_MODEL_FOLDER = "models";
	private static final String ASSETS_SHADER_FOLDER = "shaders";

	static {
		prepareAppFolder();
	}

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	/***
	 * The operating system which is running this application
	 */
	private static File appFolder;
	private static File assetsFolder;
	private static File textureFolder;
	private static File modelFolder;
	private static File shaderFolder;
	private static File projectsFolder;


	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	private AppFolder() {}

	/******************************************************************************
	 *
	 * Public API
	 *
	 ******************************************************************************/

	/**
	 * Returns applications data folder of the current operating system,
	 * which is a typical place where applications store there data.
	 *
	 * @return	The application data folder or the working directory if the
	 * 			operating system could not be determined.
	 */
	public static File getAppDirectory() {
		if (appFolder == null) {
			File installationDirectory = getInstallationDirectory();
			System.out.println(installationDirectory);
			appFolder = new File(installationDirectory, SHADER_SLOTH_APP_DIR);
		}
		return appFolder;
	}

	public static File getAssetsFolder() {
		if (assetsFolder == null) {
			assetsFolder = new File(getAppDirectory(), ASSETS_FOLDER);
		}
		return assetsFolder;
	}

	public static File getAssetsTextureFolder() {
		if (textureFolder == null) {
			textureFolder = new File(getAssetsFolder(), ASSETS_TEXTURE_FOLDER);
		}
		return textureFolder;
	}

	public static File getAssetsModelFolder() {
		if (modelFolder == null) {
			modelFolder = new File(getAssetsFolder(), ASSETS_MODEL_FOLDER);
		}
		return modelFolder;
	}

	public static File getAssetsShaderFolder() {
		if (shaderFolder == null) {
			shaderFolder = new File(getAssetsFolder(), ASSETS_SHADER_FOLDER);
		}
		return shaderFolder;
	}

	public static File getProjectsFolder() {
		if (projectsFolder == null) {
			projectsFolder = new File(getAppDirectory(), PROJECTS_FOLDER);
		}
		return projectsFolder;
	}

	private static void prepareAppFolder() {
		if (! getAppDirectory().isDirectory()) {
			getAssetsTextureFolder().mkdirs();
			getAssetsModelFolder().mkdirs();
			getAssetsShaderFolder().mkdirs();
			getProjectsFolder().mkdirs();
		}
	}

	/******************************************************************************
	 *
	 * Internal API
	 *
	 ******************************************************************************/

	private static File getInstallationDirectory() {
		if (OperatingSystem.isWindows()) {
			return new File(System.getenv("APPDATA"));
		}

		if (OperatingSystem.isOSX()) {
			return new File(System.getProperty("user.home"), "/Library/Application Support/");
		}

		if (OperatingSystem.isUnix()) {
			return new File((System.getProperty("user.home")));
		}

		return new File(System.getProperty("user.dir"));
	}

}

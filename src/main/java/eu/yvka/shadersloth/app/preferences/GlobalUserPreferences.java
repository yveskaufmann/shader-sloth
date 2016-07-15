package eu.yvka.shadersloth.app.preferences;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * @Author Yves Kaufmann
 * @since 13.07.2016
 */
public class GlobalUserPreferences {

	/******************************************************************************
	 *
	 * Constants
	 *
	 ******************************************************************************/

	public static final String RECENT_OPENED_PROJECTS = "recentOpenedProjects";
	private static final int MAX_RECENT_PROJECTS = 15;

	/******************************************************************************
	 *
	 * Fields
	 *
	 ******************************************************************************/

	/***
	 * Holds all preferences for
	 */
	private Preferences rootUserPreferences;

	/**
	 * List of recent opened projects
	 */
	private List<String> recentOpenedProjects = new ArrayList<>();

	/**
	 * Create and load preferences.
	 */
	public GlobalUserPreferences() {
		rootUserPreferences = Preferences.userNodeForPackage(GlobalUserPreferences.class).node("ShaderSloth");
	}

	/**
	 * Returns the list of recent opened projects.
	 *
	 * @return a list of strings which are absolute paths to recent opened projects
     */
	public List<String> getRecentOpenedProjects() {
		return recentOpenedProjects;
	}

	public void addRecentOpenedProject(String path) {
		addRecentOpenedProjects(Arrays.asList(path));
	}

	public void addRecentOpenedProjects(List<String> filePaths) {
		for (String path : filePaths) {
			if (recentOpenedProjects.contains(path)) {
				recentOpenedProjects.remove(path);
			}
			recentOpenedProjects.add(0, path);
		}

		while (recentOpenedProjects.size() > MAX_RECENT_PROJECTS) {
			recentOpenedProjects.remove(recentOpenedProjects.size() - 1);
		}

		writeToJavaPreference(RECENT_OPENED_PROJECTS);

	}

	public void removeRecentOpenedProjects(List<String> filePaths) {
		filePaths.forEach(recentOpenedProjects::remove);
		writeToJavaPreference(RECENT_OPENED_PROJECTS);
	}

	public void clearRecentOpenedProjects() {
		recentOpenedProjects.clear();
		writeToJavaPreference(RECENT_OPENED_PROJECTS);
	}

	/**
	 * Writes a specified property to a preferences,
	 * which means that the state of this property
	 * will be persisted.
	 *
	 * @param propertyToWrite the identifier of the property which should be persisted
     */
	public void writeToJavaPreference(String propertyToWrite) {
		switch (propertyToWrite) {
			case RECENT_OPENED_PROJECTS: {
				rootUserPreferences.put(RECENT_OPENED_PROJECTS, String.join(File.pathSeparator, recentOpenedProjects));
			}
			break;
		}
	}

	/**
	 * Reads all properties from a preferences.
	 *
	 */
	public void readFromJavaPreference() {
		final String recentItemsString  = rootUserPreferences.get(RECENT_OPENED_PROJECTS, "");
		final String[] recentItemsArray = recentItemsString.split(File.pathSeparator);
		recentOpenedProjects.clear();
		recentOpenedProjects.addAll(Arrays.asList(recentItemsArray));

	}

}

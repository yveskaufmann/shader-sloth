package eu.yvka.shadersloth.utils;

import java.io.File;

/**
 * Created by fxdapokalypse on 10.06.2016.
 */
public class OperatingSystem {

	/******************************************************************************
	 *
	 * Inner Classes and enums
	 *
	 ******************************************************************************/

	/**
	 * Operating system constants
	 */
	 enum OS {
		WINDOWS,
		OSX,
		UNIX,
		UNKNOWN
	}

	/******************************************************************************
	 *
	 * Constants
	 *
	 ******************************************************************************/

	/***
	 * The operating system which is running this application
	 */
	private final static OS CURRENT_OS = detectOS();

	/******************************************************************************
	 *
	 * Constructors
	 *
	 ******************************************************************************/

	private OperatingSystem() {}

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
	public static File getInstallationDirectory() {
		if (isWindows()) {
			return new File(System.getenv("APPDATA"));
		}

		if (isOSX()) {
			return new File(System.getProperty("user.home"), "/Library/Application Support/");
		}

		if (isUnix()) {
			return new File((System.getProperty("user.home")));
		}

		return new File(System.getProperty("user.dir"));
	}

	/**
	 * Determines if the current os is windows.
	 *
	 * @return if the current os is windows based
	 */
	public static boolean isWindows() {
		return CURRENT_OS == OS.WINDOWS;
	}

	/**
	 * Determines if the current os is mac os.
	 *
	 * @return if the current os is mac os
	 */
	public static boolean isOSX() {
		return CURRENT_OS == OS.OSX;
	}

	/**
	 * Determines if the current os is unix based.
	 *
	 * @return if the current os is unix
	 */
	public static boolean isUnix() {
		return CURRENT_OS == OS.UNIX;
	}

	/******************************************************************************
	 *
	 * Internal API
	 *
	 ******************************************************************************/

	/**
	 * Detects the current Operation System
	 *
	 * @return the current operating system.
     */
	private static OS detectOS() {
		String os = System.getProperty("os.name").toLowerCase();

		if (os.startsWith("win")) return OS.WINDOWS;
		if (os.startsWith("mac")) return OS.OSX;
		if (os.startsWith("unix") || os.startsWith("linux") || os.indexOf("aix") > 0) return OS.UNIX;

		return OS.UNKNOWN;
	}

}

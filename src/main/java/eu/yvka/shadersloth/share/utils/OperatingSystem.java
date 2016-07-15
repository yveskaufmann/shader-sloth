package eu.yvka.shadersloth.share.utils;

/**
 * Helper class for detecting the current operating
 * system.
 *
 * @Author Yves Kaufmann
 * @since 15.07.2016
 */
public enum OperatingSystem {
	WINDOWS,
	OSX,
	UNIX,
	UNKNOWN;

	static {
		CURRENT = detectOS();
	}

	public static final OperatingSystem CURRENT;

	/**
	 * Detects the current Operation System
	 *
	 * @return the current operating system.
	 */
	private static OperatingSystem detectOS() {
		String os = System.getProperty("os.name").toLowerCase();

		if (os.startsWith("win")) return OperatingSystem.WINDOWS;
		if (os.startsWith("mac")) return OperatingSystem.OSX;
		if (os.startsWith("unix") || os.startsWith("linux") || os.indexOf("aix") > 0) return OperatingSystem.UNIX;

		return OperatingSystem.UNKNOWN;
	}

	/**
	 * Determines if the current os is windows.
	 *
	 * @return if the current os is windows based
	 */
	public static boolean isWindows() {
		return CURRENT == OperatingSystem.WINDOWS;
	}

	/**
	 * Determines if the current os is mac os.
	 *
	 * @return if the current os is mac os
	 */
	public static boolean isOSX() {
		return CURRENT == OperatingSystem.OSX;
	}

	/**
	 * Determines if the current os is unix based.
	 *
	 * @return if the current os is unix
	 */
	public static boolean isUnix() {
		return CURRENT == OperatingSystem.UNIX;
	}
}


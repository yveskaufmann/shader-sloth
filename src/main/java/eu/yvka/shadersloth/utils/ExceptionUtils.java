package eu.yvka.shadersloth.utils;

public class ExceptionUtils {
	ExceptionUtils() {
	}

	public static String getFileLocationOfTraceElement(Throwable throwable, int index) {
		assert index > 0;
		StackTraceElement[] stackTrace = throwable.getStackTrace();
		if (stackTrace != null && stackTrace.length >=index) {
			return ExceptionUtils.toFileLocationString(stackTrace[index]);
		}
		return "";
	}

	public static String toFileLocationString(StackTraceElement stackTraceElement) {
		String file = stackTraceElement.getFileName();
		String line = Integer.toString(stackTraceElement.getLineNumber(), 10);
		return ("(" + String.join(":", file, line) + ")");
	}
}

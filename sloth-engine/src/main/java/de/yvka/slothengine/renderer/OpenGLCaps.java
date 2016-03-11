package de.yvka.slothengine.renderer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_EXTENSIONS;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL30.GL_NUM_EXTENSIONS;
import static org.lwjgl.opengl.GL30.glGetStringi;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OpenGLCaps {

	/**
	 * Creates a string representation of the gpu capabilities.
	 * Which contains the features and versions the underlying gpu
	 * are supports.
	 *
	 * @return the string representation of the gpu capabilities.
     */
	public static String createCapsString() {
		GLCapabilities caps = GL.createCapabilities();
		Class<?> capsType = caps.getClass();
		StringBuffer buffer = new StringBuffer();
		buffer.append("Graphic Carps Caps:").append("\n");
		buffer.append("-------------------").append("\n\n");

		for(Field field : capsType.getFields()) {
			if (!field.getType().equals(Boolean.TYPE)) continue;

			try {
				if (field.getBoolean(caps)) {
					buffer.append(field.getName());
					buffer.append("\n");
                }
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return buffer.toString();
	}

	/**
	 * Retrieves a list of <code>OpenGl</code> extensions  which are
	 * supported by the underlying gpu.
	 *
	 * @return the names of extensions which are supported by the underlying gpu.
     */
	public static List<String> supportedExtensions() {
		int countOfExtensions = glGetInteger(GL_NUM_EXTENSIONS);
		List<String> extensions = new ArrayList<>(countOfExtensions);
		for(int i = 0; i < countOfExtensions; i++) {
			extensions.add(glGetStringi(GL_EXTENSIONS, i));
		}

		return extensions;
	}

	public static void main(String[] args) {
		GLFW.glfwInit();
		long windowId = GLFW.glfwCreateWindow(1, 1, "", NULL, NULL);
		GLFW.glfwHideWindow(windowId);
		GLFW.glfwMakeContextCurrent(windowId);

		GL.createCapabilities();
		System.out.println(createCapsString());
		System.out.println("List of supported extensions:" + String.join("\n", supportedExtensions()));

		GLFW.glfwDestroyWindow(windowId);
		GLFW.glfwTerminate();

	}
}

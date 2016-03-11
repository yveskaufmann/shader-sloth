package de.yvka.slothengine.shader;

import de.yvka.slothengine.engine.EngineComponent;
import de.yvka.slothengine.shader.source.FileShaderSource;
import de.yvka.slothengine.shader.source.ShaderSource;
import de.yvka.slothengine.shader.source.ShaderSource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ShaderManager implements EngineComponent {

	public final String ASSET_PATH = "assets/shaders/";
	private Map<String, Shader> shaderMap;
	private boolean initialized;

	public Shader getShader(String shader) {
		return getShader(shader, shader, null);
	}

	public Shader getShader(String vertexShader, String fragmentShader) {
		return getShader(vertexShader, fragmentShader, null);
	}

	public Shader getShader(String vertexShader, String fragmentShader, String geometryShader) {
		String id = vertexShader + "_" + fragmentShader;
		Shader shader = shaderMap.get(id);

		if (geometryShader != null) {
			id += "_" + geometryShader;
		}

		if (shader == null) {
			shader = new Shader(id);
			shader.addSource(getSource(ShaderType.VERTEX, vertexShader));
			shader.addSource(getSource(ShaderType.FRAGMENT, fragmentShader));

			if (geometryShader != null) {
				shader.addSource(getSource(ShaderType.GEOMETRY, geometryShader));
			}

			shaderMap.put(id, shader);
		}

		return shader;
	}

	public ShaderSource getSource(ShaderType type, String name) {
		FileShaderSource fileShaderSource = new FileShaderSource(type,
			new File(ASSET_PATH + name + "." + type.getExtension()));

		return fileShaderSource;
	}


	@Override
	public void initialize() {
		if (shaderMap != null) {
			shutdown();
		}
		shaderMap = new HashMap<>();
		initialized = true;
	}

	@Override
	public void shutdown() {
		// Already performed from the render clean up
		initialized = false;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}
}

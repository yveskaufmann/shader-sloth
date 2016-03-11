package de.yvka.slothengine.scene.light;

import de.yvka.slothengine.shader.Shader;
import de.yvka.slothengine.shader.ShaderVariable;
import de.yvka.slothengine.shader.ShaderVariable;

import java.util.ArrayList;

/**
 * Collection of Lights which an passed to a Shader.
 */
public class LightList extends ArrayList<Light> {

	public static final String LIGHTS_UNIFORM_COUNT = ShaderVariable.VAR_PREFIX + "light_count";

	public void passToShader(Shader shader) {
		shader.getUniform(LIGHTS_UNIFORM_COUNT).setValue(size());
		for (int i = 0; i < size(); i++) {
			this.get(i).passToShader(i, shader);
		}
	}
}

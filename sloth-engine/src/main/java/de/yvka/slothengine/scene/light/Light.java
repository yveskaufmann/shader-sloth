package de.yvka.slothengine.scene.light;

import de.yvka.slothengine.engine.Engine;
import de.yvka.slothengine.geometry.primitives.Sphere;
import de.yvka.slothengine.material.BasicMaterial;
import de.yvka.slothengine.math.Color;
import de.yvka.slothengine.renderer.RenderState;
import de.yvka.slothengine.scene.Geometry;
import de.yvka.slothengine.scene.Node;
import de.yvka.slothengine.shader.Shader;
import de.yvka.slothengine.shader.Uniform;
import de.yvka.slothengine.shader.ShaderVariable;
import de.yvka.slothengine.engine.Engine;
import de.yvka.slothengine.geometry.primitives.Sphere;
import de.yvka.slothengine.material.BasicMaterial;
import de.yvka.slothengine.math.Color;
import de.yvka.slothengine.shader.ShaderVariable;

/**
 * Base type of light entity.
 */
public abstract class Light extends Node {

	class LightGeometry extends Geometry {
		// TODO: Mesh repository in order to share meshes
		public LightGeometry(String id) {
			super(id);

			Shader shader = Engine.shaderManager().getShader("Icon", "Icon", "Icon");

			setMaterial(new BasicMaterial(shader));
			setMesh(new Sphere(0.5f, 4, 4));
			getMaterial().setEnableLightning(false);
			getMaterial().getRenderState().enableWireframe(false);
			getMaterial().getRenderState().setCullFaceMode(RenderState.CullFaceMode.Off);

		}
	}

	public static final String LIGHT_UNIFORM_ARRAY =  ShaderVariable.VAR_PREFIX +"lights";
	public static final String LIGHT_UNIFORM_TYPE = "type";
	public static final String LIGHT_UNIFORM_COLOR = "color";
	public static final String LIGHT_UNIFORM_POSITION = "position";
	public static final String LIGHT_UNIFORM_ATTENUATION = "attenuation";

	private Color color;
	private float attenuation;
	private LightType type;

	public Light(String id, LightType type) {
		super(id);
		this.attenuation = 1.0f;
		this.color = Color.White;
		this.type = type;
		this.addChild(new LightGeometry(id + "_geometry"));
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setAttenuation(float attenuation) {
		this.attenuation = attenuation;
	}

	public float getAttenuation() {
		return attenuation;
	}

	public LightType getType() {
		return type;
	}

	protected void passToShader(int lightId, Shader shader) {
		getLightUniform(lightId, shader, LIGHT_UNIFORM_COLOR).setValue(color);
		getLightUniform(lightId, shader, LIGHT_UNIFORM_ATTENUATION).setValue(attenuation);
		getLightUniform(lightId, shader, LIGHT_UNIFORM_TYPE).setValue(type.ordinal());
	}

	protected Uniform getLightUniform(int lightId, Shader shader, String name) {
		return shader.getUniform(LIGHT_UNIFORM_ARRAY + "[" + lightId + "]." + name);
	}
}

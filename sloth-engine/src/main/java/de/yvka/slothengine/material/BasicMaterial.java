package de.yvka.slothengine.material;

import de.yvka.slothengine.engine.Engine;
import de.yvka.slothengine.math.Color;
import de.yvka.slothengine.renderer.RenderState;
import de.yvka.slothengine.shader.Shader;
import de.yvka.slothengine.shader.ShaderVariable;
import de.yvka.slothengine.texture.Texture;
import de.yvka.slothengine.engine.Engine;
import de.yvka.slothengine.math.Color;
import de.yvka.slothengine.shader.ShaderVariable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.*;

/**
 * Basic Material which provides the default behaviour for materials.
 */
public class BasicMaterial implements Material {

	/**
	 * Shininess of this material
	 */
	float shininess = 10;

	/**
	 * The Diffuse color of this material
	 */
	Color diffuse = Color.White;

	/**
	 * The ambient color of this material
	 */
	Color ambient = Color.White;

	/**
	 * The Specular color of this material
	 */
	Color specular = Color.White;

	/**
	 * The shader which is responsible to render this material/pass
	 */
	Shader shader;

	/**
	 * Specifies if the material should be affected by lightning
	 */
	boolean receivesLights = true;

	/**
	 * Specifies the render state with a geometry should be rendered
	 */
	final RenderState state = new RenderState();

	/**
	 * Contains this and all other render passes which are responsible to
	 * render a geometry.
	 */
	final List<Pass> renderPasses = new ArrayList<>();

	/**
	 * The Parameters which should be passed to shader uniform variables
	 */
	final Map<String, MaterialParameter> parameters = new HashMap<>();

	/**
	 * The Texture Bindings
	 */
	final Map<String, TextureBinding> textures = new HashMap<>();

	/**
	 * Counter for already in used texture units.
	 */
	int textureUnit = 0;

	/**
	 * Creates a basic material with the default shader
	 * as the first pass.
	 */
	public BasicMaterial() {
		shader = Engine.getShader("Default");
		renderPasses.add(this);
	}

	/**
	 * Creates a basic material with a specified shader
	 * as the first pass.
	 */
	public BasicMaterial(Shader shader) {
		this.shader = shader;
		renderPasses.add(this);
	}

	@Override
	public List<Pass> getPasses() {
		return renderPasses;
	}

	@Override
	public Pass createPass() {
		Material newPass = new BasicMaterial(this.shader);
		renderPasses.add(newPass);
		return newPass;
	}

	@Override
	public void preparePass(Pass previousPass) {
		setParameter(MATERIAL_AMBIENT, ambient);
		setParameter(MATERIAL_DIFFUSE, diffuse);
		setParameter(MATERIAL_SPECULAR, specular);
		setParameter(MATERIAL_SHININESS, shininess);
	}

	@Override
	public void postProcessPass(Pass nextPass) {
	}

	@Override
	public RenderState getRenderState() {
		return state;
	}

	@Override
	public Shader getShader() {
		return shader;
	}

	@Override
	public Map<String, TextureBinding> getTextures() {
		return textures;
	}


	public Map<String,MaterialParameter> getMaterialParameters() {
		return parameters;
	}

	@Override
	public boolean isLightningEnabled() {
		return receivesLights;
	}

	@Override
	public Pass setEnableLightning(boolean enableLightning) {
		receivesLights = enableLightning;
		return this;
	}

	@Override
	public Pass setTexture(String name, Texture texture) {
		textures.put(name, new TextureBinding(name, textureUnit, texture));
		setParameter(name, textureUnit++);
		return this;
	}

	public Pass setTexture(String name, String textureName) {
		setTexture(name, Engine.textureManager().getTexture(textureName));
		return this;
	}

	@Override
	public Pass setParameter(String name, float value) {
		setParameterValue(name, value, ShaderVariable.VariableType.Float);
		return this;
	}

	@Override
	public Pass setParameter(String name, float v1, float v2) {
		setParameterValue(name, new float[] {v1, v2}, ShaderVariable.VariableType.Float2);
		return this;
	}

	@Override
	public Pass setParameter(String name, float v1, float v2, float v3) {
		setParameterValue(name, new float[] {v1, v2, v3}, ShaderVariable.VariableType.Float3);
		return this;
	}

	@Override
	public Pass setParameter(String name, float v1, float v2, float v3, float v4) {
		setParameterValue(name, new float[] {v1, v2, v3, v4}, ShaderVariable.VariableType.Float4);
		return this;
	}

	@Override
	public Pass setParameter(String name, int value) {
		setParameterValue(name,value, ShaderVariable.VariableType.Int);
		return this;
	}

	@Override
	public Pass setParameter(String name, int v1, int v2) {
		setParameterValue(name, new int[] {v1, v2}, ShaderVariable.VariableType.Int2);
		return this;
	}

	@Override
	public Pass setParameter(String name, int v1, int v2, int v3) {
		setParameterValue(name, new int[] {v1, v2, v3}, ShaderVariable.VariableType.Int3);
		return this;
	}

	@Override
	public Pass setParameter(String name, int v1, int v2, int v3, int v4) {
		setParameterValue(name, new int[] {v1, v2, v3, v4}, ShaderVariable.VariableType.Int4);
		return this;
	}

	@Override
	public Pass setParameter(String name, Matrix4f matrix) {
		setParameterValue(name, matrix, ShaderVariable.VariableType.Matrix4x4);
		return this;
	}

	@Override
	public Pass setParameter(String name, Matrix3f matrix) {
		setParameterValue(name, matrix, ShaderVariable.VariableType.Matrix3x3);
		return this;
	}


	@Override
	public Pass setParameter(String name, Vector3f vector) {
		setParameter(name, vector.x, vector.y, vector.z);
		return this;
	}

	@Override
	public Pass setParameter(String name, Vector4f vector) {
		setParameter(name, vector.x, vector.y, vector.z, vector.w);
		return this;
	}

	@Override
	public Pass setParameter(String name, Color color) {
		setParameter(name, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
		return this;
	}

	/**
	 * Retrieves the shininess of this material
	 *
	 * @return the shininess of this material
     */
	public float getShininess() {
		return shininess;
	}

	/**
	 * Defines the shininess of this material
	 *
	 * @param shininess specifies how large the spot light are,
	 *                  so higher the value so smaller are the spot lights.
     */
	public void setShininess(float shininess) {
		this.shininess = shininess;
	}

	/**
	 * Retrieves the diffuse color of the material
	 *
	 * @return the diffuse material color
     */
	public Color getDiffuse() {
		return diffuse;
	}

	/**
	 * Defines the diffuse color of the material
	 *
	 * @param diffuse the diffuse color of the material
     */
	public void setDiffuse(Color diffuse) {
		this.diffuse = diffuse;
	}

	/**
	 * Retrieve the ambient color of the material
	 *
	 * @return the ambient color of the material
	 */
	public Color getAmbient() {
		return ambient;
	}


	/**
	 * Defines the ambient color of the material
	 *
	 * @param ambient the ambient color of the material
	 */
	public void setAmbient(Color ambient) {
		this.ambient = ambient;
	}

	/**
	 * Retrieve the specular color of the material
	 *
	 * @return the specular color of the material
	 */
	public Color getSpecular() {
		return specular;
	}

	/**
	 * Defines the specular color of the material
	 *
	 * @param specular the specular color of the material
	 */
	public void setSpecular(Color specular) {
		this.specular = specular;
	}

	private void setParameterValue(String name, Object value, ShaderVariable.VariableType type) {
		MaterialParameter param = parameters.get(name);
		if (param != null) {
			param.value = value;
			param.type = type;
		} else {
			parameters.put(name, new MaterialParameter(name, value, type));
		}
	}

}


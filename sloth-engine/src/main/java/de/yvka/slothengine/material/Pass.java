package de.yvka.slothengine.material;

import de.yvka.slothengine.math.Color;
import de.yvka.slothengine.renderer.RenderState;
import de.yvka.slothengine.shader.Shader;
import de.yvka.slothengine.texture.Texture;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Map;

/**
 * A Pass defines how geometry should be rendered
 * in a single render pass.
 */
public interface Pass {

	/**
	 * Prepare this pass for the upcoming rendering.
	 *
	 * A implementation is responsible to prepare the
	 * shader for the rendering, such as define material
	 * specific uniforms which are called parameters in the
	 * context of Passes/Materials. Or the method can
	 * be used to create a FrameBuffer for Deferred rendering.
	 *
	 * @param previousPass the render pass which was previously used to render a geometry or null if this
	 *                     the first render pass for the actually rendered geometry.
     */
	void preparePass(Pass previousPass);

	/**
	 * Prepare the pass for the next rendering and
	 * provides the possibility to manipulate
	 * the next rendering pass.
	 *
	 * @param nextPass the next rendering pass or null if this was the final rendering pass of geometry.
	 *
	 */
	void postProcessPass(Pass nextPass);

	/**
	 * Returns the rendering state which should be used by the renderer for this pass.
	 *
	 * @return the current render state.
     */
	RenderState getRenderState();

	/**
	 * Returns the Shader which should be used for rendering
	 * the geometry in this pass.
	 *
	 * @return the shader program.
     */
	Shader getShader();


	/**
	 * Returns a map between a texture sampler names
	 * and texture bindings. Contains which textures should
	 * be used to render a geometry.
	 *
	 * @return the texture map
     */
	Map<String, TextureBinding> getTextures();

	/**
	 * Returns the parameters of this material
	 * contains mainly the uniforms which should by
	 * the shader.
	 *
	 * @return the map of material parameters.
     */
	Map<String, MaterialParameter> getMaterialParameters();


	/**
	 * Determines if this material should be affected
	 * by lightning.
	 *
	 * @return if this material should be affected by lightning.
     */
	boolean isLightningEnabled();

	/**
	 * Defines if this material should be affected by lightning.
	 *
	 * @param enableLightning true if the material should be affected lightning.
     */
	Pass setEnableLightning(boolean enableLightning);

	/**
	 * Specifies a mapping between a shader sampler variable
	 * and a texture. This mapping defines which textures should be
	 * passed to the shader which is used to render a geometry.
	 *
	 * @param name the name of sampler variable to which the texture should be bound.
	 * @param texture the texture to bound to.
     */
	Pass setTexture(String name, Texture texture);

	/**
	 * Set the value of a float parameter.
	 *
	 * @param name the name of the parameter.
	 * @param value the value of the parameter.
     */
	Pass setParameter(String name, float value);

	/**
	 * Set the value of a 2 x float parameter.
	 *
	 * @param name the name of the parameter.
	 *
	 * @param v1 the value of the first float
	 * @param v2 the value of the second float
     */
	Pass setParameter(String name, float v1, float v2);

	/**
	 * Set the value of a 3 x float parameter.
	 *
	 * @param name the name of the parameter.
	 *
	 * @param v1 the value of the first float
	 * @param v2 the value of the second float
	 * @param v3 the value of the third float
	 */
	Pass setParameter(String name, float v1, float v2, float v3);

	/**
	 * Set the value of a 4 x float parameter.
	 *
	 * @param name the name of the parameter.
	 *
	 * @param v1 the value of the first float
	 * @param v2 the value of the second float
	 * @param v3 the value of the third float
	 * @param v4 the value of the fourth float
	 */
	Pass setParameter(String name, float v1, float v2, float v3, float v4);

	/**
	 * Set the value of a integer parameter.
	 *
	 * @param name the name of the parameter.
	 * @param value the value of the parameter.
	 */
	Pass setParameter(String name, int value);

	/**
	 * Set the value of a 2 x integer parameter.
	 *
	 * @param name the name of the parameter.
	 *
	 * @param v1 the value of the first integer
	 * @param v2 the value of the second integer
	 */
	Pass setParameter(String name, int v1, int v2);

	/**
	 * Set the value of a 3 x integer parameter.
	 *
	 * @param name the name of the parameter.
	 *
	 * @param v1 the value of the first integer
	 * @param v2 the value of the second integer
	 * @param v3 the value of the third integer
	 */
	Pass setParameter(String name, int v1, int v2, int v3);

	/**
	 * Set the value of a 3 x integer parameter.
	 *
	 * @param name the name of the parameter.
	 *
	 * @param v1 the value of the first integer
	 * @param v2 the value of the second integer
	 * @param v3 the value of the third integer
	 * @param v4 the value of the fourth integer
	 */
	Pass setParameter(String name, int v1, int v2, int v3, int v4);

	/**
	 * Set the value of 4x4 float Matrix parameter
	 *
	 * @param name the name of the parameter
	 * @param matrix the matrix
     */
	Pass setParameter(String name, Matrix4f matrix);

	/**
	 * Set the value of 3x3 float Matrix parameter
	 *
	 * @param name the name of the parameter
	 * @param matrix the matrix
	 */
	Pass setParameter(String name, Matrix3f matrix);

	/**
	 * Set the value of color parameter
	 *
	 * @param name the name of the parameter
	 * @param color the color
	 */
	Pass setParameter(String name, Color color);

	/**
	 * Set the value of vector parameter.
	 *
	 * @param name the name of the parameter
	 * @param vector the vector
     */
	Pass setParameter(String name, Vector3f vector);

	/**
	 * Set the value of vector parameter.
	 *
	 * @param name the name of the parameter
	 * @param vector the vector
	 */
	Pass setParameter(String name, Vector4f vector);
}

package de.yvka.slothengine.material;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

/**
 * A material describes how a mesh should be rendered by the renderer.
 *
 * A material consists of definitions for render passes, a
 * render pass contains the information which are required
 * by the render in order to render this pass.
 * A pass provide the following information:
 *
 * <code>
 *     	<ul>
*     	    <li>Renderer State - The RenderState which is used for this shader.</li>
 *     		<li>Shader Program - The Shader Program which is responsible to render the geometry</li>
 *     		<li>Shader Uniforms - Parameter for the shader</li>
 *     		<li>Texture units - The Binding between Shader Uniform and Textures</li>
 * 		</ul>
 * </code>
 */
public interface Material extends Pass {

	/**
	 * The shininess parameter name of this material
	 */
	public static final String MATERIAL_SHININESS = "sl_material.shininess";

	/**
	 * The diffuse color of this material
	 */
	public static final String MATERIAL_DIFFUSE = "sl_material.diffuse";

	/**
	 * The color of the material
	 */
	public static final String MATERIAL_AMBIENT = "sl_material.ambient";

	/**
	 * The color of the material
	 */
	public static final String MATERIAL_SPECULAR = "sl_material.specular";

	/**
	 * Returns the list of passes
	 * which are assemble this
	 * material.
	 *
	 * @return list of passes
     */
	List<Pass> getPasses();

	/**
	 * Creates a new pass for
	 * this Material.
	 *
	 * @return a new Pass instance which
	 * must be managed and
	 * must be inserted into list which
	 * is returned by getPasses.
     */
	Pass createPass();

}

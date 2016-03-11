package de.yvka.slothengine.shader;

import de.yvka.slothengine.geometry.VertexBuffer;
import de.yvka.slothengine.geometry.VertexBuffer;

/**
 * Representation of a shader attribute which includes
 * vertex positions, normals, colors.
 */
public class Attribute extends ShaderVariable {

	/**
	 * Default Attribute name for vertex position data
	 */
	public final String VERTEX_ATTRIBUTE_NAME = VAR_PREFIX +"position";

	/**
	 * Default Attribute name for normals
	 */
	public final String NORMAL_ATTRIBUTE_NAME = VAR_PREFIX + "normal";

	/**
	 * Default Attribute name for colors
	 */
	public final String COLOR_ATTRIBUTE_NAME = VAR_PREFIX + "color";

	/**
	 * Default Attribute name for texture coordinates 01
	 */
	public final String TEXTCOORDS1_ATTRIBUTE_NAME = VAR_PREFIX + "textcoord01";

	/**
	 * Default Attribute name for texture coordinates 02
	 */
	public final String TEXTCOORDS2_ATTRIBUTE_NAME = VAR_PREFIX + "textcoord02";


	/**
	 * Default Attribute name for texture coordinates 03
	 */
	public final String TEXTCOORDS3_ATTRIBUTE_NAME = VAR_PREFIX + "textcoord03";

	/**
	 * Default Attribute name for texture coordinates 04
	 */
	public final String TEXTCOORDS4_ATTRIBUTE_NAME = VAR_PREFIX + "textcoord04";

	/**
	 * Default Attribute name for texture coordinates 05
	 */
	public final String TEXTCOORDS5_ATTRIBUTE_NAME = VAR_PREFIX +"textcoord05";

	/**
	 * Default Attribute name for texture coordinates 06
	 */
	public final String TEXTCOORDS6_ATTRIBUTE_NAME = VAR_PREFIX + "textcoord06";


	/**
	 * Set the name of this attribute by it's corresponding
	 * VertexBuffer type. You can override this functionality
	 * by set the name of attribute by your self and before the
	 * object is passed to the renderer.
	 *
	 * @param buffer the corresponding vertex buffer.
     */
	public void bindName(VertexBuffer buffer) {
		if (getName() == null) {
			setName(toName(buffer.getType()));
		}
	}

	private String toName(VertexBuffer.Type type) {
		switch (type) {
			case Vertex: return VERTEX_ATTRIBUTE_NAME;
			case Normal: return NORMAL_ATTRIBUTE_NAME;
			case Color: return COLOR_ATTRIBUTE_NAME;
			case TextCoords: return TEXTCOORDS1_ATTRIBUTE_NAME;
			case TextCoords02: return TEXTCOORDS2_ATTRIBUTE_NAME;
			case TextCoords03: return TEXTCOORDS3_ATTRIBUTE_NAME;
			case TextCoords04: return TEXTCOORDS4_ATTRIBUTE_NAME;
			case TextCoords05: return TEXTCOORDS5_ATTRIBUTE_NAME;
			case TextCoords06: return TEXTCOORDS6_ATTRIBUTE_NAME;
		}
		return null;
	}

}

#version 130
/*
 * Letter coords inside the core.texture sprite
 */
uniform float xOffset;
uniform float yOffset;
uniform float xEnd;
uniform float yEnd;

/*
 * Coordinates of the quad in [-1.0:1.0, 1.0:-1.0].
 *
 * Position of the letter quad and it's size.
 */
uniform float xStartQuad = -1.0;
uniform float yStartQuad =  1.0;
uniform float xEndQuad = 1.0;
uniform float yEndQuad = -1.0;

out vec2 uv;

vec2 quadVertices[6] = vec2[6](
	vec2(xStartQuad, yStartQuad),
	vec2(xStartQuad, yEndQuad),
	vec2(xEndQuad, yEndQuad),
	vec2(xEndQuad, yEndQuad),
	vec2(xEndQuad, yStartQuad),
	vec2(xStartQuad, yStartQuad)
);


vec2 quadUvs[6] = vec2[6](
	vec2(xOffset, yOffset),
	vec2(xOffset, yEnd),
	vec2(xEnd, yEnd),
	vec2(xEnd, yEnd),
	vec2(xEnd, yOffset),
	vec2(xOffset, yOffset)
);

void main() {
   	uv = quadUvs[gl_VertexID];
	gl_Position = vec4(quadVertices[gl_VertexID], 0.0, 1.0);
}





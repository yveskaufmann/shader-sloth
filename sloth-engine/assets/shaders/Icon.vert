/**
 * Wireframe Shader based on:
 * http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.138.5421&rep=rep1&type=pdf
 * http://codeflow.org/entries/2012/aug/02/easy-wireframe-display-with-barycentric-coordinates/
 *
 */


#version 150
precision highp float;

uniform mat4 sl_mvp;

in vec3 sl_position;

void main() {
	gl_Position = sl_mvp * vec4(sl_position.xyz, 1.0);
}

#version 150

in vec3 sl_position;
in vec3 sl_normal;

uniform mat4 sl_mvp;

out vec3 normal;

void main() {
	normal = sl_normal;
	gl_Position = vec4(sl_position, 1.0);
}

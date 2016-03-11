#version 330

layout(points) in;
layout(line_strip) out;
layout(max_vertices=2) out;

in  vec3 normal[];
out vec3 color;

uniform mat4 sl_mvp;

void main() {

    color = vec3(1.0);
    gl_Position = sl_mvp *  gl_in[0].gl_Position;
    EmitVertex();

    color = vec3(1.0);
    gl_Position = sl_mvp * vec4(gl_in[0].gl_Position.xyz + normal[0], 1.0);
    EmitVertex();

    EndPrimitive();
}

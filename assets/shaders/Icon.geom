/**
 * Wireframe Shader based on:
 * http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.138.5421&rep=rep1&type=pdf
 * http://codeflow.org/entries/2012/aug/02/easy-wireframe-display-with-barycentric-coordinates/
 *
 */

#version 150

layout(triangles) in;
layout(triangle_strip) out;
layout(max_vertices = 3) out;

out vec3 barycentric;

void main() {

  barycentric = vec3(1.0, 0.0, 0.0);
  gl_Position = gl_in[0].gl_Position;
  EmitVertex();

  barycentric = vec3(0.0, 1.0, 0.0);
  gl_Position = gl_in[1].gl_Position;
  EmitVertex();

  barycentric = vec3(0.0, 0.0, 1.0);
  gl_Position = gl_in[2].gl_Position;
  EmitVertex();

  EndPrimitive();
}

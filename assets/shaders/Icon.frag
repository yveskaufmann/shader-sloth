/**
 * Wireframe Shader based on:
 * http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.138.5421&rep=rep1&type=pdf
 * http://codeflow.org/entries/2012/aug/02/easy-wireframe-display-with-barycentric-coordinates/
 *
 */

#version 150
precision highp float;

in vec3 barycentric;
out vec4 fragmentColor;

#extension GL_OES_standard_derivatives : enable
float edgeFactor(){
    vec3 d = fwidth(barycentric);
    vec3 a3 = smoothstep(vec3(0.0), d*0.8, barycentric);
    return min(min(a3.x, a3.y), a3.z);
}

void main() {
    fragmentColor = vec4(mix(vec3(0.5), vec3(0.0), edgeFactor()), 1.0);
    //fragmentColor = vec4(vec3(0.0), (1.0-edgeFactor())*0.95);
}

precision highp float;
uniform float time;
uniform vec2 resolution;
varying vec3 fPosition;
varying vec3 fNormal;

vec3 lightPos = vec3(1.0, 2.0, 0.0);
vec3 ambientColor = vec3(0.1, 0.1, 0.1);
vec3 diffuseColor = vec3(0.2, 0.2, 0.0);
vec3 specularColor = vec3(0.2, 0.2, 0.0);
float shininess = 20.0;

void main()
{
  vec3 lightDir = normalize(lightPos - fPosition);
  vec3 eyePos = normalize(-fPosition);
  vec3 reflection = normalize(-reflect(lightDir, fNormal));

  vec3 lightDiffuseColor = diffuseColor * max(dot(fNormal, lightDir), 0.0);
  lightDiffuseColor = clamp(lightDiffuseColor, 0.0, 1.0);

  vec3 specularColor = specularColor * pow(max(dot(reflection, eyePos), 0.0),  0.3 * shininess);

  gl_FragColor = vec4(fNormal, 1.0);
  gl_FragColor = vec4(ambientColor + diffuseColor + specularColor, 1.0);
}

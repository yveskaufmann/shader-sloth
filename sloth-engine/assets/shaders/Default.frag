#version 130
precision highp float;

uniform sampler2D diffuseTexture;
uniform mat4 sl_modelMatrix;
uniform vec3 sl_cameraPosition;
uniform vec3 sl_cameraDirection;

#define MAX_COLORS 100
uniform int sl_light_count;
uniform struct Light {
	vec4 position;
	vec4 color;
	float attenuation;
	int type;
} sl_lights[MAX_COLORS];


uniform struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    float shininess;
} sl_material;

in vec3 position;
in vec3 normal;
in vec2 texturecoord;
out vec4 fragmentColor;

vec3 calcLightning(Light light, vec3 texelColor, vec3 normal, vec3 pos, vec3 camDir) {

    vec3 lightDir = light.position.xyz - pos;
    float distanceToLight = length(lightDir);
    lightDir = normalize(lightDir);

    float attenuation = 1.0 / (1.0 + light.attenuation * pow(distanceToLight, 2));

    vec3 ambient = texelColor.rgb * light.color.rgb;

    float diffuseIntensity = max(dot(normal, lightDir), 0.2);
    vec3 diffuse = diffuseIntensity * texelColor.rgb * light.color.rgb;

    float specularIntensity = 0.0;
    if (diffuseIntensity > 0.0) {
        vec3 reflection = normalize(reflect(-lightDir, normal));
        specularIntensity = pow(max( 0.0, dot(camDir, reflection)), sl_material.shininess);
    }

    vec3 specular = specularIntensity * (sl_material.specular.rgb + light.color.rgb);
    return  mix(diffuse, specular, 0.5);
}

void main() {
	vec4 color = texture(diffuseTexture, texturecoord);
    color = vec4(1.0);

    vec3 camDir = normalize(sl_cameraDirection);
    vec3 texelColor = vec3(0.0);
    for(int i = 0; i < sl_light_count; i++ ) {
        texelColor += calcLightning(sl_lights[i], color.rgb, normal, position, camDir);
    }

    texelColor *= sl_material.ambient.rgb;
    vec3 gamma = vec3(1.0/2.2);
    fragmentColor = vec4(texelColor, color.a);

}

#version 130

uniform sampler2D fontSprite;
uniform vec4 color;

in vec2 uv;
out vec4 fragColor;

void main() {
    float alpha = texture(fontSprite, uv).a;
	if (alpha <= 0.0f) discard;

    fragColor =  vec4(color.rgb, alpha);
}





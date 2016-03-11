package de.yvka.slothengine.shader;

public enum  ShaderType {
	VERTEX("vert"),
	FRAGMENT("frag"),
	GEOMETRY("geom"),
	COMPUTE("comp");

	private String extension;

	ShaderType(String extension) {
		this.extension = extension;
	}

	@Override
	public String toString() {
		return this.name() + "_SHADER";
	}

	public String getExtension() {
		return this.extension;
	}
}

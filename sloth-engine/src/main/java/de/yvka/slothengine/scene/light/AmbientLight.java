package de.yvka.slothengine.scene.light;

/**
 * Entity of an only ambient light.
 */
public class AmbientLight extends Light {

	public AmbientLight(String id) {
		super(id, LightType.Ambient);
	}
}

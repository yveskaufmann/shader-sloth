package eu.yvka.shadersloth.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import org.joml.Vector3f;

public class Vector3fModel {

	private final FloatProperty x = new SimpleFloatProperty(0.0f);
	private final FloatProperty y = new SimpleFloatProperty(0.0f);
	private final FloatProperty z = new SimpleFloatProperty(0.0f);


	public Vector3fModel() {
	}

	public float getX() {
		return x.get();
	}

	public void setX(float value) {
		xProperty().set(value);
	}

	public FloatProperty xProperty() {
		return x;
	}

	public float getY() {
		return y.get();
	}

	public void setY(float value) {
		yProperty().set(value);
	}

	public FloatProperty yProperty() {
		return y;
	}

	public float getZ() {
		return z.get();
	}

	public void setZ(float value) {
		zProperty().set(value);
	}

	public FloatProperty zProperty() {
		return z;
	}

	public Vector3f toVector3f() {
		return new Vector3f().set(getX(), getY(), getZ());
	};

}

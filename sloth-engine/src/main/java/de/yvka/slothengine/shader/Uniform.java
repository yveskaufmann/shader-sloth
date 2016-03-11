package de.yvka.slothengine.shader;

import de.yvka.slothengine.material.MaterialParameter;
import de.yvka.slothengine.math.Color;
import de.yvka.slothengine.utils.BufferUtils;
import de.yvka.slothengine.material.MaterialParameter;
import de.yvka.slothengine.math.Color;
import de.yvka.slothengine.utils.BufferUtils;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.nio.FloatBuffer;

public class Uniform extends ShaderVariable {


	private Object value;
	private VariableType type;

	public void setValue(float value) {
		setValue(VariableType.Float, value);
	}

	public void setValue(float v1, float v2) {
		setValue(VariableType.Float2, new float[] {v1, v2});
	}

	public void setValue(float v1, float v2, float v3) {
		setValue(VariableType.Float3, new float[] {v1, v2, v3});
	}

	public void setValue(float v1, float v2, float v3, float v4) {

		setValue(VariableType.Float4, new float[] {v1, v2, v3, v4});
	}

	public void setValue(int v1) {
		setValue(VariableType.Int, v1);
	}

	public void setValue(int v1, int v2) {
		setValue(VariableType.Int2, new int[] {v1, v2});
	}

	public void setValue(int v1, int v2, int v3) {
		setValue(VariableType.Int3, new int[] {v1, v2, v3});
	}

	public void setValue(int v1, int v2, int v3, int v4) {
		setValue(VariableType.Int4, new int[] {v1, v2, v3, v4});
	}

	public void setValue(Matrix4f value) {
		setValue(VariableType.Matrix4x4, value);
	}

	public void setValue(Color color) {
		setValue(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public void setValue(Vector4f value) {
		setValue(value.x, value.y, value.z, value.w);
	}

	public void setValue(Vector3f value) {
		setValue(value.x, value.y, value.z);
	}

	public void setValue(MaterialParameter parameter) {
		setValue(parameter.getType(), parameter.getValue());
	}

	public void setValue(VariableType type, Object value) {
		if (this.value == value) return;

		switch (type) {
			case Float:
			case Float2:
			case Float3:
			case Float4:
			case Int:
			case Int2:
			case Int3:
			case Int4:
				this.value = value;
				break;
			case Matrix3x3:
				Matrix3f matrix3f = (Matrix3f) value;
				if (value != null) {
					this.value = BufferUtils.createFloatBuffer(9);
				}
				matrix3f.get((FloatBuffer) this.value);
				break;
			case Matrix4x4:
				Matrix4f matrix4f = (Matrix4f) value;
				if (value != null) {
					this.value = BufferUtils.createFloatBuffer(16);
				}
				matrix4f.get((FloatBuffer) this.value);
				break;

		}

		this.type = type;
		enableUpdateRequired();
	}

	public Object getValue() {
		return value;
	}

	public VariableType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Uniform variable \"" + super.toString() +"\"";
	}



}

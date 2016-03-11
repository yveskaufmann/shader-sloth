package de.yvka.slothengine.math;

/**
 * Abstraction of rgba value.
 */
public class Color implements Cloneable {

	/**
	 * Color Black
	 */
	public final static Color Black = new Color(0.0f, 0.0f, 0.0f);

	/**
	 * Color White
	 */
	public final static Color White = new Color(1.0f, 1.0f, 1.0f);

	/**
	 * Color LightGrey
	 */
	public final static Color LightGrey = new Color(0.7f, 0.7f, 0.7f);

	/**
	 * Color Medium Gray
	 */
	public final static Color Gray = new Color(0.5f, 0.5f, 0.5f);

	/**
	 * Color Red
	 */
	public final static Color Red = new Color(1.0f, 0.0f, 0.0f);

	 /**
	 * Color Blue
	 */
	 public final static Color Blue = new Color(0.0f, 0.0f, 1.0f);


	/**
	 * Color Green
	 */
	public final static Color Green = new Color(0.0f, 1.0f, 0.0f);


	private float r;
	private float g;
	private float b;
	private float a;

	/**
	 * Color object initialized with the color white.
	 */
	public Color() {
		r = g = b = a = 1.0f;
	}

	/**
	 * Create a new <code>Color</code> object by
	 * passing the rgb components of this color and
	 * the alpha component is set to 1.0 by default.
	 *
	 * @param r the red component of this color
	 * @param g the green component of this color
     * @param b the blue component of this color.
     */
	public Color(float r, float g, float b) {
		this(r, g, b, 1.0f);
	}

	/**
	 * Create a new <code>Color</code> object by
	 * specifying the rgba components of this color.
	 *
	 * @param r the red component of this color
	 * @param g the green component of this color
	 * @param b the blue component of this color.
	 * @param a the alpha component of this color.
	 */
	public Color(float r, float g, float b, float a) {
		set(r, g, b, a);
	}

	/**
	 * A copy constructor for a color object.
	 *
	 * @param color the color object to copy
	 */
	public Color(Color color) {
		set(color);
	}

	/**
	 *
	 * Set the color by specifying the rgba components of this color.
	 *
	 * @param r the red component of this color
	 * @param g the green component of this color
	 * @param b the blue component of this color.
	 * @param a the alpha component of this color.
	 *
	 * @return this object instance.
	 */
	public Color set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return this;
	}

	/**
	 * Set the color components of this <code>Color</code> to those of
	 * set by a specified <code>Color</code> object.
	 *
	 * @param color The new color of this color.
	 * @return this object instance.
     */
	public Color set(Color color) {
		if (color == null) {
			r = g = b = a = 0.0f;
		} else {
			r = color.r;
			g = color.g;
			b = color.b;
			a = color.a;
		}

		return this;
	}

	/**
	 * @return the red component of this color.
     */
	public float getRed() {
		return r;
	}

	/**
	 * @return the green component of this color.
	 */
	public float getGreen() {
		return g;
	}

	/**
	 * @return the blue component of this color.
     */
	public float getBlue() {
		return b;
	}

	/**
	 * @return the alpha component of this color.
     */
	public float getAlpha() {
		return a;
	}

	/**
	 * Ensures that the rgba components are between 0 and 1.
	 *
	 * @return this color object instance.
	 */
	public Color clamp() {
		this.r = MathUtils.clamp(this.r, 0.0f, 1.0f);
		this.g = MathUtils.clamp(this.g, 0.0f, 1.0f);
		this.b = MathUtils.clamp(this.b, 0.0f, 1.0f);
		this.a = MathUtils.clamp(this.a, 0.0f, 1.0f);
		return this;
	}


	@Override
	public String toString() {
		return String.format("Color [%f, %f, %f, %f]", r, g, b, a);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Color color = (Color) o;

		if (Float.compare(color.r, r) != 0) return false;
		if (Float.compare(color.g, g) != 0) return false;
		if (Float.compare(color.b, b) != 0) return false;
		return Float.compare(color.a, a) == 0;

	}

	@Override
	public int hashCode() {
		int result = (r != +0.0f ? Float.floatToIntBits(r) : 0);
		result = 31 * result + (g != +0.0f ? Float.floatToIntBits(g) : 0);
		result = 31 * result + (b != +0.0f ? Float.floatToIntBits(b) : 0);
		result = 31 * result + (a != +0.0f ? Float.floatToIntBits(a) : 0);
		return result;
	}

	@Override
	public Color clone() {
		try {
			return (Color) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e); // Should never happen.
		}
	}
}

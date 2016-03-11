package de.yvka.slothengine.math;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MatrixUtils {

	/**
	 * Build a matrix by set each vector as basis and perform the following multiplication:
	 *<code><pre>
	 *   r[x,y,z] = xAxis
	 *   u[x,y,z] = yAxis
	 *   d[x,y,z] = zAxis
	 *   __             __     __            __
	 *  |  rx  ry  rz  0  |   |  1  0  0  -px |
	 *  |  ux  uy  uz  0  |   |  0  1  0  -py |
	 *  | -dx -dy -dy  0  | X |  0  0  1  -pz |
	 *  |  0   0   0   1  |   |  0  0  0   1  |
	 *  |__             __|   |__           __|
	 *</pre></code>
	 * @param position
	 * 				the position or translation of a point.
	 * @param xAxis
	 * 				the xAxis Basis of the resulting matrix.
	 * @param yAxis
	 * 				the yAxis Basis of the resulting matrix.
	 * @param zAxis
	 * 				the zAxis Basis of the resulting matrix.
     * @return the resulting matrix
     */
	public static Matrix4f from(final Vector3f position, final Vector3f xAxis, final Vector3f yAxis, final Vector3f zAxis) {
		return from(position, xAxis, yAxis, zAxis, new Matrix4f());
	}


	/**
	 * Build a matrix by set each vector as basis and perform the following multiplication:
	 * <code><pre>
	 *   r[x,y,z] = xAxis
	 *   u[x,y,z] = yAxis
	 *   d[x,y,z] = zAxis
	 *   __             __     __            __
	 *  |  rx  ry  rz  0  |   |  1  0  0  -px |
	 *  |  ux  uy  uz  0  |   |  0  1  0  -py |
	 *  | -dx -dy -dy  0  | X |  0  0  1  -pz |
	 *  |  0   0   0   1  |   |  0  0  0   1  |
	 *  |__             __|   |__           __|
	 *</code></pre>
	 * @param position
	 * 				the position or translation of a point.
	 * @param xAxis
	 * 				the xAxis Basis of the resulting matrix.
	 * @param yAxis
	 * 				the yAxis Basis of the resulting matrix.
	 * @param zAxis
	 * 				the zAxis Basis of the resulting matrix.
	 * @param destination
	 * 				the destination matrix which obtain the new orientation
	 * @return the destination matrix
	 */
	public static Matrix4f from(final Vector3f position, final Vector3f xAxis, final Vector3f yAxis, final Vector3f zAxis, Matrix4f destination) {
		destination.set(
			xAxis.x, yAxis.x, -zAxis.x, 0.0f,
			xAxis.y, yAxis.y, -zAxis.y, 0.0f,
			xAxis.z, yAxis.z, -zAxis.z, 0.0f,
			-xAxis.dot(position), -yAxis.dot(position), zAxis.dot(position), 1.0f
		);
		return destination;
	}

}

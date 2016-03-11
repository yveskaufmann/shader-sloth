package de.yvka.slothengine.utils;

/**
 * A class which have to clean up it's
 * underlying resources when they no longer needed.
 *
 * A implementer have to implement it's clean up code
 * into the clean method. The implementation of
 * Cleanable.clean must not be idempotent.
 */
public interface Cleanable extends AutoCloseable {
	void clean();
	default void close() {
		clean();
	}
}

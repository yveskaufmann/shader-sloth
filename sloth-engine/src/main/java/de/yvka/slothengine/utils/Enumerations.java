package de.yvka.slothengine.utils;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * A set of handy methods for handling Enumerations.
 *
 */
public class Enumerations {

	/**
	 * An Iterable wrapper for an Enumeration.
	 *
	 * @param <T> the type of elements managed by this Enumeration.
	 */
	static class IterableEnumeration<T> implements Iterable<T>, Iterator<T> {

		private Enumeration<T> enumeration;

		/**
		 * Wraps an enumeration into an Iterable.
		 *
		 * @param enumeration
		 */
		public IterableEnumeration(Enumeration<T> enumeration) {
			this.enumeration = enumeration;
		}

		@Override
		public Iterator<T> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return enumeration != null && enumeration.hasMoreElements();
		}

		@Override
		public T next() {
			return enumeration.nextElement();
		}

	}

	/**
	 * Converts a given {@link java.util.Enumeration} into an {@link java.util.Iterable}, <br>
	 * in order to iterate over an Enumeration by using the <br>
	 * "For-each Loop" statement.<br>
	 * <br>
	 * For more details please see {@link java.util.Iterable}.
	 * <br>
	 * @param enumeration an Enumeration which should be converted.
	 * @return an Iterable which wraps the specified Enumeration
	 */
	public static <T> Iterable<T> asIterable(Enumeration<T> enumeration) {
		return new IterableEnumeration<T>(enumeration);
	}

	/**
	 * Converts a given {@link java.util.Enumeration} into an {@link java.util.Iterator}, <br>
	 * in order to iterate over an Enumeration by using the <br>
	 * "For-each Loop" statement.<br>
	 * <br>
	 * For more details please see {@link java.util.Iterator}.
	 * <br>
	 * @param enumeration an Enumeration which should be converted.
	 * @return an Iterable which wraps the specified Enumeration
	 */
	public static <T> Iterator<T> asIterator(Enumeration<T> enumeration) {
		return new IterableEnumeration<T>(enumeration);
	}
}

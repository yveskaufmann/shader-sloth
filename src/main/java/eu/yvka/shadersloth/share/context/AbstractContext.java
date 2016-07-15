package eu.yvka.shadersloth.share.context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AbstractContext implements Serializable {



	/**
	 * Abstraction of a property in order to achieve type type safety.
	 * @param <T> the type of the property value
	 */
	private static class PropertyValue<T> implements Serializable {
		@SuppressWarnings("unchecked")
		static final PropertyValue NULL = new PropertyValue(null);
		private final T value;

		PropertyValue(T value) {
			this.value = value;
		}
		@SuppressWarnings("unchecked")
		<ExpectedType> ExpectedType getTypeAware(Class<ExpectedType> expectedType) {

			if (expectedType.isPrimitive() || expectedType.isInstance(this.value))
				return (ExpectedType) this.value;
			return  null;
		}

	}

	/**
	 * The Set of properties nothing special here.
	 */
	protected final Map<String, PropertyValue> properties = new HashMap<>();

	/**
	 * Creates an empty ImporterContext.
	 */
	public AbstractContext() {
	}

	/**
	 * Retrieves the Boolean property by an id.
	 *
	 * @param id the id of the desired boolean property
	 *
	 * @return the boolean property or null if there is no such boolean property.
	 */
	public Boolean getBoolean(String id) {
		return getProperty(id, Boolean.class);
	}

	/**
	 * <p>Defines a boolean property with a specific id.</p>
	 *
	 * @param id the id of the property.
	 * @param bool the value of the property
	 */
	public void setBoolean(String id, Boolean bool) {
		setProperty(id, bool);
	}

	/**
	 * Retrieves the byte property by an id.
	 *
	 * @param id the id of the desired byte property
	 *
	 * @return the byte property or null if there is no such byte property.
	 */
	public Byte getByte(String id) {
		return getProperty(id, Byte.class);
	}

	/**
	 * <p>Defines a byte property with a specific id.</p>
	 *
	 * @param id the id of the property.
	 * @param byteValue the value of the property
	 */
	public void setByte(String id, Byte byteValue) {
		setProperty(id, byteValue);
	}

	/**
	 * Retrieves the short property by an id.
	 *
	 * @param id the id of the desired short property
	 *
	 * @return the short property or null if there is no such byte property.
	 */
	public Short getShort(String id) {
		return getProperty(id, Short.class);
	}

	/**
	 * <p>Defines a short property with a specific id.</p>
	 *
	 * @param id the id of the property.
	 * @param shortValue the value of the property
	 */
	public void setShort(String id, Short shortValue) {
		setProperty(id, shortValue);
	}

	/**
	 * Retrieves the integer property by an id.
	 *
	 * @param id the id of the desired integer property
	 *
	 * @return the integer property or null if there is no such integer property.
	 */
	public Integer getInteger(String id) {
		return getProperty(id, Integer.class);
	}

	/**
	 * <p>Defines a integer property with a specific id.</p>
	 *
	 * @param id the id of the property.
	 * @param integerValue the value of the property
	 */
	public void setInteger(String id, Integer integerValue) {
		setProperty(id, integerValue);
	}

	/**
	 * Retrieves the long property by an id.
	 *
	 * @param id the id of the desired long property
	 *
	 * @return the long property or null if there is no such long property.
	 */
	public Long getLong(String id) {
		return getProperty(id, Long.class);
	}

	/**
	 * <p>Defines a long property with a specific id.</p>
	 *
	 * @param id the id of the property.
	 * @param longValue the value of the property
	 */
	public void setLong(String id, Long longValue) {
		setProperty(id, longValue);
	}

	/**
	 * Retrieves the string property by an id.
	 *
	 * @param id the id of the desired string property
	 *
	 * @return the string property or null if there is no such string property.
	 */
	public String getString(String id) {
		return getProperty(id, String.class);
	}

	/**
	 * <p>Defines a string property with a specific id.</p>
	 *
	 * @param id the id of the property.
	 * @param string the value of the property
	 */
	public void setString(String id, String string) {
		setProperty(id, string);
	}

	/**
	 * <p>Defines a property with a specific id.</p>
	 *
	 * @param id the id of the property.
	 * @param value the value of the property
	 * @param <T> the type of the property.
	 */
	@SuppressWarnings("unchecked")
	public <T> void setProperty(String id, T value) {
		this.properties.put(id, new PropertyValue(value));
	}

	/**
	 * <p>Retrieves a property by it's id.</p>
	 *
	 * <p>In order to achieve type safety a caller must provide
	 * the type of the desired property.</p>
	 *
	 * @param id the id of the desired property.
	 * @param type the expected type of the desired property.
	 * @param <T> the expected type of the desired property
	 *
	 * @return the desired property or null if there is no such a property with the specified id.
	 */
	public <T> T getProperty(String id, Class<T> type) {
		PropertyValue property = properties.getOrDefault(id, PropertyValue.NULL);
		return type.cast(property.getTypeAware(type));
	}

	/**
	 * Checks if there is a property with a specified id and a specific type.
	 *
	 * @param id the id of the property.
	 * @param type the expected type of the property
	 *
	 * @return true the requested property exists.
	 */
	public boolean hasProperty(String id, Class type) {
		return getProperty(id, type) != null;
	}
}

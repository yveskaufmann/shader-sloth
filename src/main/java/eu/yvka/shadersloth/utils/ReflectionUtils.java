package eu.yvka.shadersloth.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ReflectionUtils {

	private static final Logger Log = LoggerFactory.getLogger(ReflectionUtils.class);

	/**
	 * Retrieves a field instance by its name from a given class instance.
	 *
	 * @param fieldName the name of the desired field.
	 * @param instance the instance which has the desired field.
	 * @return the desired field or null if the given instance doesn't provide the desired field.
	 */
	public static Object getFieldValue(String fieldName, Object instance) {
		return getFieldValue(fieldName, instance.getClass(), instance);
	}

	/**
	 * Retrieves a field value by its name from a given class instance.
	 *
	 * @param fieldName the name of the desired field.
	 * @param cls  the class which owns the desired field.
	 * @param instance the instance which has the desired field.
     * @return the desired field or null if the given instance doesn't provide the desired field.
     */
	public static Object getFieldValue(String fieldName, Class<?> cls, Object instance) {
		try {
			Field field = cls.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(instance);

		} catch (NoSuchFieldException | IllegalAccessException e) {
			Log.warn(MessageFormat.format("\"{0}\" is not a accessible field of {1}.", fieldName, cls.getName()),e);
		}
		return null;
	}

	/**
	 * Retrieves a field value by
	 *
	 * @param field the field to obtain the value from
	 * @param instance the instance which has the desired field.
	 * @return the value of the desired field
	 **/
	public static Object getFieldValue(Field field, Object instance) {
		return AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
			boolean naturalAccessStatus = field.isAccessible();
			try {
				field.setAccessible(true);
				return field.get(instance);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Could read value from field " + field, e);
			} finally {
				field.setAccessible(naturalAccessStatus);
			}
		});
	}

	public static void setFieldValue(Field field, Object instance, Object value) {
		boolean naturalAccessStatus = field.isAccessible();

		AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
			try {
				field.setAccessible(true);
				field.set(instance, value);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Could not set value field " + field, e);
			} finally {
				field.setAccessible(naturalAccessStatus);
			}
			return null;
		});
	}

	/***
	 * Retrieve all Fields including all fields of the super classes as well.
	 *
	 * @param cls the class owns the desired fields
	 * @return a list of all declared fields including all fields of the parent classes.
     */
	public static List<Field> getAllFields(Class<?> cls) {
		List<Field> fields = new ArrayList<>();
		Class <?> type = cls;

		while (type != null && !type.equals(Objects.class)) {
			fields.addAll(Arrays.asList(type.getDeclaredFields()));
			type = type.getSuperclass();
		}

		return fields;
	}

	/***
	 * Retrieve all Methods including all methods of the super classes as well.
	 *
	 * @param cls the class owns the desired methods
	 * @return a list of all declared methods including all methods of the parent classes.
	 */
	public static List<Method> getAllMethods(Class<?> cls) {
		return getAllMethods(cls, (m) -> true);
	}

	/***
	 * Retrieve all Methods including all methods of the super classes as well.
	 *
	 * @param cls the class owns the desired methods
	 * @return a list of all declared methods including all methods of the parent classes.
	 */
	public static List<Method> getAllMethods(Class<?> cls, Predicate<Method> filter) {
		List<Method> methods = new ArrayList<>();
		Class <?> type = cls;

		while (type != null && !type.equals(Objects.class)) {
			for (Method method : type.getDeclaredMethods()) {
				if (filter.test(method)) {
					methods.add(method);
				}
			}
			type = type.getSuperclass();
		}

		return methods;
	}

	/***
	 * Retrieve all Methods including all methods of the super classes as well
	 * and excludes all methods which doesn't have the specified annotation
	 * present.
	 *
	 * @param cls the class owns the desired methods
	 * @param annotation only methods with this annotation will be returned
	 * @return a list of all declared methods including all methods of the parent classes.
	 */
	public static List<Method> getMethodsWithAnnotation(Class<?> cls, Class<? extends Annotation> annotation) {
		return getAllMethods(cls, (m) ->m.isAnnotationPresent(annotation));
	}

	public static void invokeMethodsWithAnnotation(Class<?> cls, final Object bean, Class<? extends Annotation> annotation) {
		for(Method method : getMethodsWithAnnotation(cls, annotation)) {
			if (method.isAnnotationPresent(annotation)) {
				invokeMethod(bean, method);
			}
		}
	}

	/**
	 * Invoke a method in the context of a bean object.
	 *
	 * @param bean the context bean
	 * @param method the method to invoke
	 * @param params optional list of
     */
	public static Object invokeMethod(Object bean, Method method, Object...params) {
		final boolean naturalAccessState = method.isAccessible();
		return AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            try {
                method.setAccessible(true);
               return method.invoke(bean, params);
            } catch (InvocationTargetException | IllegalAccessException ex) {
                throw new IllegalStateException("Could not invoke method " + method, ex);
            } finally {
                method.setAccessible(naturalAccessState);
            }
        });
	}

	/**
	 * Invoke a method in the context of a bean object.
	 *
	 * @param bean the context bean
	 * @param methodName the name of the method to invoke
	 * @param params optional list of
	 */
	public static Object invokeMethod(Object bean, String methodName, Object...params) throws NoSuchMethodException {
		Class<?> type =	bean.getClass();
		Class<?>[] paramTypes = Stream.of(params).map(Object::getClass).collect(Collectors.toList()).toArray(new Class[0]);
		Method method = type.getMethod(methodName);
		return invokeMethod(bean, method, params);
	}
}

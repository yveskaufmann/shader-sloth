package de.yvka.slothengine.utils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public abstract class Singleton {

	private static final Map<String, Object> instances = new HashMap<>();

	public synchronized static <T> T of(Class<T> type) {
		if ( !isSingleton(type) ) {
			try {
				Constructor<T> constructor = type.getDeclaredConstructor();
				constructor.setAccessible(true);
				instances.put(type.getName(), constructor.newInstance());
			} catch (Exception e) {
				throw new IllegalArgumentException("The Singleton class required a constructor without arguments", e);
			}
		} 
		
		Object instance = instances.get(type.getName());
		return type.cast(instance);
	}

	private synchronized static <T> boolean isSingleton(Class<T> type) {
		return instances.containsKey(type.getName());
	}
}

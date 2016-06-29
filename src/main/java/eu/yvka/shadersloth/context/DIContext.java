package eu.yvka.shadersloth.context;

/**
 * Encapsulates an DI Injector
 */
public interface DIContext {
	void injectMembers(Object object);
	<T> T getInstance(Class<T> type);
	void destroy();
	void init();
}

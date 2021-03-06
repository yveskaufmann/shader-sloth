package eu.yvka.shadersloth.app;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import eu.yvka.shadersloth.app.preferences.GlobalUserPreferences;
import eu.yvka.shadersloth.app.project.ProjectManager;
import eu.yvka.shadersloth.share.utils.ReflectionUtils;
import eu.yvka.shadersloth.share.controller.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

/***
 * This module contains all xmlbinding for the ShaderSlothRenderer.
 *
 * For more information about guice injection modules
 * see: https://github.com/google/guice/wiki/GettingStarted.
 */
public class ShaderSlothModule extends AbstractModule {

	private Logger Log = LoggerFactory.getLogger(ShaderSlothModule.class);

	@Override
	protected void configure() {
		bind(ProjectManager.class).asEagerSingleton();
		ensurePostConstructIsInvoked();
	}

	@Singleton
	@Provides
	GlobalUserPreferences  provideGlobalUserPreferences() {
		GlobalUserPreferences pref = new GlobalUserPreferences();
		pref.readFromJavaPreference();
		return pref;
	}

	private void ensurePostConstructIsInvoked() {
		// ensures that PostConstruct is invoked after initialization.
		bindListener(new AbstractMatcher<TypeLiteral<?>>() {
			@Override
			public boolean matches(TypeLiteral<?> typeLiteral) {
				return true;
			}
		}, new TypeListener() {
			@Override
			public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
				encounter.register((InjectionListener<I>) injectee -> {
					if (! (injectee instanceof AbstractController)) {
						boolean wasInitialized = false;
						for (Method method : ReflectionUtils.getMethodsWithAnnotation(injectee.getClass(), PostConstruct.class)) {
							ReflectionUtils.invokeMethod(injectee, method);
							wasInitialized = true;
						}

						if (wasInitialized) {
							Log.info("Initialized " + injectee.getClass().getSimpleName());
						}
					}
				});
			}
		});
	}
}

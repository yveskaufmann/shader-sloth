package eu.yvka.shadersloth.utils.controller.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies information about a controllers.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller {
	/**
	 * Specifies the title of controllers,
	 * the title will be rendered in the action bar.
     */
	String title();

	/**
	 * Icon which will be displayed in link buttons
	 * which leads to this controllers view.
	 *
	 * @return a utf8 escaped character which defines the icon.
     */
	String icon() default "";
}

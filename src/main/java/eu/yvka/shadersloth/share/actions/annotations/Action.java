package eu.yvka.shadersloth.share.actions.annotations;

import eu.yvka.shadersloth.share.controls.FontAwesomeIcon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Action {

	/**
	 * Identifier of a action which allows it to reference
	 * a action from outside a constructor.
	 *
	 * @return action identifier.
     */
	String id() default "";

	/**
	 * @return the text description of this
	 * action.
     */
	String text() default "";

	/**
	 * @return the tooltip for this action.
     */
	String tooltip() default "";

	/*
	 * The priority specifies in which order actions are rendered.
	 * A low priority means that the action will be rendered after all
	 * actions with a higher priority.
     */
	int priority() default 0;

	/**
	 * The Font icon which should be used by this action.
	 *
	 * @return
     */
	FontAwesomeIcon fontIcon() default FontAwesomeIcon.NONE;
}

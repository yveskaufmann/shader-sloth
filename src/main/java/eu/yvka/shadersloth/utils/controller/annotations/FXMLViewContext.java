package eu.yvka.shadersloth.utils.controller.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a field which should obtain a {@link net.sharksystem.sharknet.javafx.context.net.sharksystem.sharknet.javafx.context.ViewContext}
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FXMLViewContext {
}

package com.arrow.dashboard.widget.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.arrow.dashboard.widget.annotation.configuration.ConfigurationPersistence;

/**
 * Annotated method will be called when widget has been initialized
 * <p>
 * Developer can expect all topic providers, all messaging channels are
 * configured so widget can start work<br>
 * Note {@link ConfigurationPersistence} method may (will) be called before this one.
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface OnLoad {

}

package com.arrow.dashboard.widget.annotation.configuration;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines a method, that will provide a FIXME-add link page with properties<br>
 * These properties are supposed to be a fast configuration for the widget<br>
 * The aim is to not start configuration process, but just correct some
 * parameters<br>
 * For example - billing rate<br>
 * <p>
 * Annotated method <b>must</b> return FIXME-add link page object
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface FastConfigurationRequest {

}

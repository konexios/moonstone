package com.arrow.dashboard.widget.annotation.configuration;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines a method, that process changes in fast configuration FIXME-add link
 * page<br>
 * Annotated method <b>must</b> have FIXME-add link page object as parameter and
 * return FIXME-add link page object
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface FastConfigurationPageRequest {

}

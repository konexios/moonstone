package com.arrow.dashboard.widget.annotation.configuration;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines a method, that will be called to provide persisted configuration for
 * the widget instance<br>
 * Called by the framework after widget instantiation and when new configuration is saved for this widget<br>
 * Annotated method <b>must</b> have a FIXME-add link configuration object as a
 * parameter
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ConfigurationPersistence {

}

package com.arrow.dashboard.widget.annotation.configuration;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines a method, that handles configuration steps.<br>
 * <p>
 * The sequence is:<br>
 * 1. Special form on front end, that process widget configuring, shows the
 * 'initial' configuration provided by {@link ConfigurationRequest} annotated
 * method<br>
 * 2. When user fill some data and click 'next' - framework will send current
 * (modified) configuration to widget<br>
 * 3. Framework will call the most appropriate method, annotated as
 * {@link ConfigurationPageRequest} to process the configuration<br>
 * 4. Configuration, returned by the method, will be sent to front end to
 * continue the configuration process<br>
 * <p>
 * Annotation has parameters {@link ConfigurationPageRequest#page()} and
 * {@link ConfigurationPageRequest#configurationName()}<br>
 * These parameters is used to find the most appropriate method for the current
 * configuration object, according to configuration page and name<br>
 * <p>
 * The logic of choosing appropriate method:<br>
 * 1. call method that has defined current configuration page and current
 * configuration name<br>
 * 2. call method that has defined current configuration name<br>
 * 3. call method that has defined current configuration page<br>
 * 4. call method that has no defined page and name<br>
 * <p>
 * Annotated method <b>must</b> have FIXME-add link configuration as a parameter
 * and return FIXME-add link configuration object
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ConfigurationPageRequest {
	/**
	 * Page number to check if current configuration has the same current page
	 * to call this method<br>
	 * Optional parameter
	 * 
	 * @return page number
	 */
	int page() default 0;

	/**
	 * Configuration name to check if current configuration has the same name to
	 * call this method<br>
	 * 
	 * @return configuration name
	 */
	String configurationName() default "";
}

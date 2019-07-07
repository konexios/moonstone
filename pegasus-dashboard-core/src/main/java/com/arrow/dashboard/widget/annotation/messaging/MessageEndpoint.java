package com.arrow.dashboard.widget.annotation.messaging;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Endpoint for incoming messages<br>
 * Annotated method will be called when widget receive a message from front end
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface MessageEndpoint {
	/**
	 * Endpoint name<br>
	 * 
	 * @return
	 */
	String value();
}

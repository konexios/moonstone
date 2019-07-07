/**
 * 
 */
package com.arrow.dashboard.widget.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines a widget<br>
 * Instance of the annotated class will be instantiated as a run-time widget
 * instance<br>
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Widget {
	/**
	 * Name of the widget<br>
	 * Simply a label. Not an internal id
	 * 
	 * @return {@link String} name
	 */
	String name();

	/**
	 * Description of the widget<br>
	 * Simply a label. Not an internal id
	 * 
	 * @return {@link String} description
	 */
	String description();
	
	/**
	 * The name of angular directive, associated with this widget<br>
	 * Appropriate directive will be used to show widget on the dashboard<br>
	 * <b>NOTE:</b> Please follow <a href=
	 * "https://docs.angularjs.org/guide/directive">https://docs.angularjs.org/guide/directive</a>
	 * for name format
	 * 
	 * @return {@link String} angular directive name
	 */
	String directive();
}
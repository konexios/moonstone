package com.arrow.dashboard.widget.annotation.property;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.arrow.dashboard.widget.annotation.Widget;

/**
 * Define a FIXME-add link property view instance<br>
 * Instance of annotated class points to the property view and provided to the
 * front end with the property value<br>
 * <p>
 * Property view may have fields - parameters of the view<br>
 * Front end view implementation (basically, angular's directive) will get this
 * property view object and can act accordingly<br>
 * <p>
 * Front end view implementation is defined for this view as a value of the
 * annotation. The same as {@link Widget#directive()}
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface PropertyView {
	/**
	 * The name of the front end view directive<br>
	 * Please refer to {@link Widget#directive()}
	 * 
	 * @return name of front end view directive
	 */
	String value();
}

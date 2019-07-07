package com.arrow.dashboard.widget.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotated method will be called when user has removed widget from the
 * dashboard
 * <p>
 * When widget receives this 'notification' - front end widgets already
 * closed<br>
 * When method return - framework will destroy all resources related to this
 * widget
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface OnDestroy {

}

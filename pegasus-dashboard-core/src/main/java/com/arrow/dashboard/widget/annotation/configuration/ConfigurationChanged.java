package com.arrow.dashboard.widget.annotation.configuration;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines a method, that handles a special case in configuration process: when
 * user navigates to previous, already filled pages<br>
 * <p>
 * This is a special case, because user may change properties, that related to
 * configuration structure or/and other properties values<br>
 * For example:<br>
 * User is configuring a widget, that tracking window position in the car (open,
 * closed)<br>
 * There are three types of cars in the system:<br>
 * <ul>
 * <li>Hatchback: it has two windows (doors)</li>
 * <li>Sedan: it has four windows (doors)</li>
 * <li>Bus: it has 10 windows (two doors with windows and 8 separate
 * windows)</li>
 * </ul>
 * User on the first page in configuration specify the car, he wants to
 * monitor<br>
 * On the second page in configuration, user should specify the window he wants
 * to monitor<br>
 * If user select a car of type hatchback, second page will have two options
 * with appropriate windows ids or names<br>
 * If user select a car of type bus - second page will have 1o options to select
 * appropriate window<br>
 * Potentially, user may navigate to first page from second one, change its
 * choice of the car and click 'next' (if this is supported by current
 * configuration. see documentation for more examples).<br>
 * In this case, the special method with {@link ConfigurationChanged} annotation
 * will be called<br>
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ConfigurationChanged {

}

package com.arrow.dashboard.widget.annotation.upgrade;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Define a method to upgrade widget's configuration<br>
 * Annotated methods <b>must</b> be defined inside class annotated as
 * {@link WidgetUpgrader}<br>
 * One widget upgrader may have several methods to upgrade configurations, but
 * they all <b>must</b> have different {@link UpgradeConfiguration#value()}
 * values
 * <p>
 * Annotated method <b>must</b> have FIXME-add link as a parameter and return
 * FIXME-add link
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface UpgradeConfiguration {
	/**
	 * FIXME-add link Configuration name to be upgraded<br>
	 * 
	 * @return configuration name
	 */
	String value();
}
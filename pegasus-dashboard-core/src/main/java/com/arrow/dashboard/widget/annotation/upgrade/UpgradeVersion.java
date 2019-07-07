package com.arrow.dashboard.widget.annotation.upgrade;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Define a method to upgrade property<br>
 * Annotated methods <b>must</b> be defined inside class annotated as
 * {@link PropertyUpgrader}<br>
 * One property upgrader may have several methods to upgrade properties, but
 * they all <b>must</b> have different {@link UpgradeVersion#value()} values
 * <p>
 * Annotated method <b>must</b> have {@link Object} as a parameter and return
 * {@link Object} - the object is a representation of the property from the
 * database
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface UpgradeVersion {
	/**
	 * Define property version to be upgraded<br>
	 * Upgrade always increment the version (+1)
	 * 
	 * @return
	 */
	int value();
}

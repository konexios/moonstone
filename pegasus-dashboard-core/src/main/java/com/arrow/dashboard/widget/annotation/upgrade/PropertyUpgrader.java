package com.arrow.dashboard.widget.annotation.upgrade;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Define upgrader for property<br>
 * Annotated class will be used to upgrade stored properties FIXME-add link
 * <p>
 * Properties are upgraded on application start, before loading widgets and
 * configurations<br>
 * Methods to upgrade properties <b>must</b> be annotated as
 * {@link UpgradeVersion}
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface PropertyUpgrader {
	/**
	 * Class of FIXME-add property to be upgraded
	 * 
	 * @return
	 */
	Class<?> value();
}

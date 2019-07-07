package com.arrow.dashboard.widget.annotation.upgrade;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Define upgrader for widget<br>
 * <b>NOTE:</b> upgrading widget meaning upgrading stored configuration<br>
 * <p>
 * Annotated class will be used to upgrade stored configuration FIXME-add link
 * for defined widget {@link WidgetUpgrader#value()}
 * <p>
 * Configurations are upgraded after upgrading properties but before
 * instantiating widgets<br>
 * Methods to upgrade configurations <b>must</b> be annotated as
 * {@link UpgradeConfiguration}
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface WidgetUpgrader {
	Class<?> value();
}

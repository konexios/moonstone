package com.arrow.dashboard.widget.annotation.data;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines a data provider field<br>
 * Data provider is an interface, to get any application data from the
 * widget<br>
 * The field value type is always interface of some data provider<br>
 * Appropriate implementation is managed by the application
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface DataProvider {

}

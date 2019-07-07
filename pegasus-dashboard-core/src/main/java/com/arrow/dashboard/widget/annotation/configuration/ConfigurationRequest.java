package com.arrow.dashboard.widget.annotation.configuration;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines a method, that provides 'initial' configuration to front end<br>
 * The sequence is:<br>
 * 1. user click a button to configure a widget<br>
 * 2. framework process this request and opens a special form to configure the
 * widget<br>
 * 3. framework ask widget to provide a configuration, that will be show to the
 * user on that special form<br>
 * This is an 'initial' configuration<br>
 * Annotated method <b>must</b> return FIXME-add link configuration object
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ConfigurationRequest {

}

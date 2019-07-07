package com.arrow.dashboard.widget.annotation.messaging;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Definition for topic of outgoing messages<br>
 * Applicable for:
 * <ul>
 * <li>Field<br>
 * Annotated field is supposed to be a topic provider: it will be used to send
 * messages to front end</li>
 * <li>Method<br>
 * Annotated method <b>must</b> be annotated by {@link MessageEndpoint}<br>
 * This annotation allows method to send returned value to front end</li>
 * </ul>
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface TopicProvider {
	/**
	 * Name of the topic of outgoing messages
	 * 
	 * @return
	 */
	String value();
}

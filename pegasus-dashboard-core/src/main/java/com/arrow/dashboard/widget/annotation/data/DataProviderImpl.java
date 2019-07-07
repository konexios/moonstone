package com.arrow.dashboard.widget.annotation.data;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines an implementation of data provider<br>
 * Annotated class has implementation for data provider interface(s)<br>
 * There are strict requirements for implementation:<br>
 * <ul>
 * <li>Implementation <b>must</b> has the same methods as implemented data
 * provider interface</li>
 * <li>Each method <b>must</b> return the same type</li>
 * <li>Each method <b>must</b> have FIXME-add link User as a first parameter. If
 * interface method has parameters, they exist in the same order after the
 * user</li>
 * </ul>
 * Data provider implementations instances are managed by the framework<br>
 * Appropriate instance of data provider is automatically 'loaded' to widget
 * instance according to configurations
 * 
 * @author dantonov
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface DataProviderImpl {
	/**
	 * Retention type of data provider implementation instance<br>
	 * This is a way how the framework will acquire data provider to be provided
	 * to the widget instance<br>
	 * Available types are:
	 * <ul>
	 * <li>{@link Retention#AUTOWIRED}</li>
	 * <li>{@link Retention#CREATE}</li>
	 * <li>{@link Retention#INSTANCE}</li>
	 * </ul>
	 * 
	 * @author dantonov
	 *
	 */
	enum Retention {
		/**
		 * Data provider implementation instance will be auto-wired from
		 * application context
		 */
		AUTOWIRED,
		/**
		 * Data provider implementation instance will be instantiated each time
		 * new widget is created (widget that requires this data provider).
		 */
		CREATE,
		/**
		 * Data provider implementation instance will be acquired by using
		 * static getInstance() method<br>
		 * This is a support for singleton design pattern
		 */
		INSTANCE
	}

	/**
	 * Array of classes that are implemented as data providers by this data
	 * provider implementation<br>
	 * Each class <b>must</b> have annotation {@link DataProvider} to be a data
	 * provider<br>
	 * Current {@link DataProviderImpl} <b>must</b> have implementation for all
	 * methods for each data provider in this array
	 * 
	 * @return array of classes of implemented data providers
	 */
	Class<?>[] dataProviders();

	/**
	 * Define a retention for annotated data provider implementation
	 * 
	 * @return {@link Retention}
	 */
	Retention retention();
}

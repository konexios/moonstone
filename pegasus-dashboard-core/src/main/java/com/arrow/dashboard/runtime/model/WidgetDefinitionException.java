package com.arrow.dashboard.runtime.model;

/**
 * Defines exception for widget definition<br>
 * All exceptions related to widget class loading are described by this class
 * 
 * @author dantonov
 *
 */
public class WidgetDefinitionException extends Exception {

	private static final long serialVersionUID = 4888721937339877738L;

	public WidgetDefinitionException() {
		super();
	}

	public WidgetDefinitionException(String arg0) {
		super(arg0);
	}

	public WidgetDefinitionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public WidgetDefinitionException(Throwable arg0) {
		super(arg0);
	}

}

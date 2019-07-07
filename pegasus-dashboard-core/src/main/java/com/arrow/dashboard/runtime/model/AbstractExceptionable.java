package com.arrow.dashboard.runtime.model;

import com.arrow.dashboard.DashboardEntityAbstract;

public abstract class AbstractExceptionable extends DashboardEntityAbstract {
	protected WidgetDefinitionException exception;

	public WidgetDefinitionException getException() {
		return exception;
	}

	public boolean hasError() {
		return exception != null;
	}
}

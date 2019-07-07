package com.arrow.dashboard.runtime.model;

import java.io.Serializable;

public class WidgetStateChangeResponse implements Serializable {
	private static final long serialVersionUID = 4681402839100495939L;

	private final static boolean DEFAULT_NOTIFIED_RESULT = false;

	private String widgetId;
	private boolean notified = DEFAULT_NOTIFIED_RESULT;
	private String message;
	private Throwable error;

	public WidgetStateChangeResponse() {
	}

	public String getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}

	public boolean isNotified() {
		return notified;
	}

	public void setNotified(boolean notified) {
		this.notified = notified;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	public WidgetStateChangeResponse withWidgetId(String widgetId) {
		setWidgetId(widgetId);

		return this;
	}

	public WidgetStateChangeResponse withResult(boolean result) {
		setNotified(result);

		return this;
	}

	public WidgetStateChangeResponse withMessage(String message) {
		setMessage(message);

		return this;
	}

	public WidgetStateChangeResponse withError(Throwable error) {
		setError(error);

		return this;
	}
}

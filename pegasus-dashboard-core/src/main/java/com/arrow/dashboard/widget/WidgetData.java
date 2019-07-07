package com.arrow.dashboard.widget;

import java.io.Serializable;

public class WidgetData implements Serializable {
	private static final long serialVersionUID = -2624197772251924329L;

	private WidgetStates state;
	private Object data;

	public WidgetStates getState() {
		return state;
	}

	public void setState(WidgetStates state) {
		this.state = state;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public WidgetData withState(WidgetStates state) {
		setState(state);

		return this;
	}

	public WidgetData withData(Object data) {
		setData(data);

		return this;
	}
}

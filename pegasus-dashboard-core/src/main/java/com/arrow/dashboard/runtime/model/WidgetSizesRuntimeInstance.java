package com.arrow.dashboard.runtime.model;

import java.io.Serializable;

public class WidgetSizesRuntimeInstance implements Serializable {
	private static final long serialVersionUID = -5291490506566725397L;

	private WidgetDimensionsRuntimeInstance small;
	private WidgetDimensionsRuntimeInstance medium;
	private WidgetDimensionsRuntimeInstance large;
	private WidgetDimensionsRuntimeInstance xtraLarge;

	public WidgetDimensionsRuntimeInstance getSmall() {
		return small;
	}

	public void setSmall(WidgetDimensionsRuntimeInstance small) {
		this.small = small;
	}

	public WidgetDimensionsRuntimeInstance getMedium() {
		return medium;
	}

	public void setMedium(WidgetDimensionsRuntimeInstance medium) {
		this.medium = medium;
	}

	public WidgetDimensionsRuntimeInstance getLarge() {
		return large;
	}

	public void setLarge(WidgetDimensionsRuntimeInstance large) {
		this.large = large;
	}

	public WidgetDimensionsRuntimeInstance getXtraLarge() {
		return xtraLarge;
	}

	public void setXtraLarge(WidgetDimensionsRuntimeInstance xtraLarge) {
		this.xtraLarge = xtraLarge;
	}

	@Override
	public String toString() {
		return "WidgetSizes [Small=" + small + ", Medium=" + medium + ", Large=" + large + ", x-large=" + xtraLarge
		        + "]";
	}
}
package com.arrow.pegasus.dashboard.data;

import java.io.Serializable;

public class WidgetSizes implements Serializable {
	private static final long serialVersionUID = 5936111847668729380L;

	private WidgetDimensions small;
	private WidgetDimensions medium;
	private WidgetDimensions large;
	private WidgetDimensions xtraLarge;

	public WidgetDimensions getSmall() {
		return small;
	}

	public void setSmall(WidgetDimensions small) {
		this.small = small;
	}

	public WidgetDimensions getMedium() {
		return medium;
	}

	public void setMedium(WidgetDimensions medium) {
		this.medium = medium;
	}

	public WidgetDimensions getLarge() {
		return large;
	}

	public void setLarge(WidgetDimensions large) {
		this.large = large;
	}

	public WidgetDimensions getXtraLarge() {
		return xtraLarge;
	}

	public void setXtraLarge(WidgetDimensions xtraLarge) {
		this.xtraLarge = xtraLarge;
	}
}
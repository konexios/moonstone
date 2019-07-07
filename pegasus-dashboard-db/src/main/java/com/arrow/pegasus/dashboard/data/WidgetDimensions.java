package com.arrow.pegasus.dashboard.data;

import java.io.Serializable;

public class WidgetDimensions implements Serializable {
	private static final long serialVersionUID = 4945343817316195691L;

	private int width;
	private int height;

	public WidgetDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
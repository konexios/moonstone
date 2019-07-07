package com.arrow.dashboard.runtime.model;

import java.io.Serializable;

public class WidgetDimensionsRuntimeInstance implements Serializable {
	private static final long serialVersionUID = 1748956150966627241L;

	private int width;
	private int height;

	public WidgetDimensionsRuntimeInstance(int width, int height) {
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

	@Override
	public String toString() {
		return "Size [width=" + width + ", height=" + height + "]";
	}
}
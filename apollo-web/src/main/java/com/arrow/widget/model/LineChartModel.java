package com.arrow.widget.model;

import java.io.Serializable;

import com.arrow.widget.WidgetConstants;

import moonstone.acn.client.model.TelemetryItemType;

public class LineChartModel implements Serializable {
	private static final long serialVersionUID = -554393153076742657L;

	private int displayInterval = WidgetConstants.DEFAULT_DISPLAY_INTERVAL;
	private String telemetryName;
	private TelemetryItemType type;
	private Object[] telemetryItems;

	public int getDisplayInterval() {
		return displayInterval;
	}

	public void setDisplayInterval(int displayInterval) {
		this.displayInterval = displayInterval;
	}

	public String getTelemetryName() {
		return telemetryName;
	}

	public void setTelemetryName(String telemetryName) {
		this.telemetryName = telemetryName;
	}

	public TelemetryItemType getType() {
		return type;
	}

	public void setType(TelemetryItemType type) {
		this.type = type;
	}

	public Object[] getTelemetryItems() {
		return telemetryItems;
	}

	public void setTelemetryItems(Object[] telemetryItems) {
		this.telemetryItems = telemetryItems;
	}

	public LineChartModel withDisplayInterval(int displayInterval) {
		setDisplayInterval(displayInterval);

		return this;
	}

	public LineChartModel withTelemetryName(String telemetryName) {
		setTelemetryName(telemetryName);

		return this;
	}

	public LineChartModel withType(TelemetryItemType type) {
		setType(type);

		return this;
	}

	public LineChartModel withTelemetryItems(Object[] telemetryItems) {
		setTelemetryItems(telemetryItems);

		return this;
	}
}
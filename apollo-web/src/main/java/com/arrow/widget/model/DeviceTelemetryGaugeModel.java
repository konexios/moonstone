package com.arrow.widget.model;

import java.io.Serializable;

public class DeviceTelemetryGaugeModel implements Serializable {
	private static final long serialVersionUID = 4713048605909289437L;

	private String name;
	private String value;
	private String minValue;
	private String maxValue;
	private long timestamp;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public double getPercent() {
		double value = Double.valueOf(getValue());
		double minValue = Double.valueOf(getMinValue());
		double maxValue = Double.valueOf(getMaxValue());

		return ((value - minValue) * 100) / (maxValue - minValue);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public DeviceTelemetryGaugeModel withName(String name) {
		setName(name);

		return this;
	}

	public DeviceTelemetryGaugeModel withValue(String value) {
		setValue(value);

		return this;
	}

	public DeviceTelemetryGaugeModel withMinValue(String minValue) {
		setMinValue(minValue);

		return this;
	}

	public DeviceTelemetryGaugeModel withMaxValue(String maxValue) {
		setMaxValue(maxValue);

		return this;
	}

	public DeviceTelemetryGaugeModel withTimestamp(long timestamp) {
		setTimestamp(timestamp);

		return this;
	}
}

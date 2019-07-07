package com.arrow.kronos.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.arrow.acn.client.model.TelemetryItemModel;
import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.acs.AcsSystemException;

@Document(indexName = EsTelemetryItem.INDEX, type = EsTelemetryItem.TYPE, createIndex = false)
public class EsTelemetryItem implements Serializable {

	public static final String INDEX = "telemetry";
	public static final String TYPE = "telemetryItem";

	private static final long serialVersionUID = 4450817749408850170L;

	@Id
	private String id;
	private String applicationId;
	private String deviceId;
	private String name;
	private TelemetryItemType type;
	private long timestamp;
	private String strValue;
	private Long intValue;
	private Double floatValue;
	private Boolean boolValue;
	private LocalDate dateValue;
	private LocalDateTime datetimeValue;
	private String intSqrValue;
	private String floatSqrValue;
	private String intCubeValue;
	private String floatCubeValue;

	public TelemetryItemModel toModel() {
		TelemetryItemModel model = new TelemetryItemModel();
		model.setName(name);
		model.setType(type);
		model.setTimestamp(timestamp);
		model.setStrValue(strValue);
		model.setIntValue(intValue);
		model.setFloatValue(floatValue);
		model.setBoolValue(boolValue);
		model.setDateValue(dateValue == null ? null : dateValue.toString());
		model.setDatetimeValue(datetimeValue == null ? null : datetimeValue.toString());
		model.setIntSqrValue(intSqrValue);
		model.setFloatSqrValue(floatSqrValue);
		model.setFloatCubeValue(floatCubeValue);
		model.setIntCubeValue(intCubeValue);
		model.setFloatCubeValue(floatCubeValue);
		return model;
	}

	public Object value() {
		switch (type) {
		case Boolean:
			return getBoolValue();
		case Date:
			return getDateValue();
		case DateTime:
			return getDatetimeValue();
		case Float:
			return getFloatValue();
		case FloatCube:
			return getFloatCubeValue();
		case FloatSquare:
			return getFloatSqrValue();
		case Integer:
			return getIntValue();
		case IntegerCube:
			return getIntCubeValue();
		case IntegerSquare:
			return getIntSqrValue();
		case String:
			return getStrValue();
		case System:
			return getStrValue();
		default:
			throw new AcsSystemException("UNSUPPORTED TYPE: " + type);
		}
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TelemetryItemType getType() {
		return type;
	}

	public void setType(TelemetryItemType type) {
		this.type = type;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	public Long getIntValue() {
		return intValue;
	}

	public void setIntValue(Long intValue) {
		this.intValue = intValue;
	}

	public Double getFloatValue() {
		return floatValue;
	}

	public void setFloatValue(Double floatValue) {
		this.floatValue = floatValue;
	}

	public Boolean getBoolValue() {
		return boolValue;
	}

	public void setBoolValue(Boolean boolValue) {
		this.boolValue = boolValue;
	}

	public LocalDate getDateValue() {
		return dateValue;
	}

	public void setDateValue(LocalDate dateValue) {
		this.dateValue = dateValue;
	}

	public LocalDateTime getDatetimeValue() {
		return datetimeValue;
	}

	public void setDatetimeValue(LocalDateTime datetimeValue) {
		this.datetimeValue = datetimeValue;
	}

	public String getIntSqrValue() {
		return intSqrValue;
	}

	public void setIntSqrValue(String intSqrValue) {
		this.intSqrValue = intSqrValue;
	}

	public String getFloatSqrValue() {
		return floatSqrValue;
	}

	public void setFloatSqrValue(String floatSqrValue) {
		this.floatSqrValue = floatSqrValue;
	}

	public String getIntCubeValue() {
		return intCubeValue;
	}

	public void setIntCubeValue(String intCubeValue) {
		this.intCubeValue = intCubeValue;
	}

	public String getFloatCubeValue() {
		return floatCubeValue;
	}

	public void setFloatCubeValue(String floatCubeValue) {
		this.floatCubeValue = floatCubeValue;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
}

package com.arrow.kronos.data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.TsDocumentAbstract;
import com.arrow.pegasus.data.profile.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;

import moonstone.acn.client.model.TelemetryItemType;
import moonstone.acs.AcsSystemException;

@Document(collection = LastTelemetryItem.COLLECTION_NAME)
public class LastTelemetryItem extends TsDocumentAbstract {
	private static final long serialVersionUID = 8064777291412607537L;
	public static final String COLLECTION_NAME = "last_telemetry_item";

	private String applicationId;
	@NotBlank
	private String deviceId;
	@NotBlank
	private String name;
	@NotNull
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
	private String binaryValue;

	@Transient
	@JsonIgnore
	private Device refDevice;
	@Transient
	@JsonIgnore
	private Telemetry refTelemetry;
	@Transient
	@JsonIgnore
	private Application refApplication;

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
		case Binary:
			return getBinaryValue();
		default:
			throw new AcsSystemException("UNSUPPORTED TYPE: " + type);
		}
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
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

	public Device getRefDevice() {
		return refDevice;
	}

	public void setRefDevice(Device refDevice) {
		this.refDevice = refDevice;
	}

	public Telemetry getRefTelemetry() {
		return refTelemetry;
	}

	public void setRefTelemetry(Telemetry refTelemetry) {
		this.refTelemetry = refTelemetry;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getBinaryValue() {
		return binaryValue;
	}

	public void setBinaryValue(String binaryValue) {
		this.binaryValue = binaryValue;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.TELEMETRY_ITEM;
	}
}

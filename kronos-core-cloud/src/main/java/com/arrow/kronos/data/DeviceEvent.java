package com.arrow.kronos.data;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.TsDocumentAbstract;
import com.arrow.pegasus.data.profile.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = DeviceEvent.COLLECTION_NAME)
@CompoundIndexes({
		@CompoundIndex(name = "applicationId_deviceId", background = true, def = "{'applicationId': 1, 'deviceId' : 1}") })
public class DeviceEvent extends TsDocumentAbstract {
	private static final long serialVersionUID = 6722387694110970376L;
	public static final String COLLECTION_NAME = "device_event";

	private final static DeviceEventStatus DEFAULT_STATUS = DeviceEventStatus.Open;

	@NotBlank
	private String applicationId;
	@NotBlank
	private String deviceId;
	@NotBlank
	private String deviceActionTypeId;
	@NotBlank
	private String criteria;
	private long expires;
	private int counter = 1;
	@NotNull
	private DeviceEventStatus status = DEFAULT_STATUS;
	private HashMap<String, Object> information = new HashMap<>();

	@Transient
	@JsonIgnore
	private Application refApplication;

	@Transient
	@JsonIgnore
	private Device refDevice;
	@Transient
	@JsonIgnore
	private Telemetry refTelemetry;
	@Transient
	@JsonIgnore
	private DeviceActionType refDeviceActionType;

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceActionTypeId() {
		return deviceActionTypeId;
	}

	public void setDeviceActionTypeId(String deviceActionTypeId) {
		this.deviceActionTypeId = deviceActionTypeId;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public DeviceEventStatus getStatus() {
		return status;
	}

	public void setStatus(DeviceEventStatus status) {
		this.status = status;
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

	public DeviceActionType getRefDeviceActionType() {
		return refDeviceActionType;
	}

	public void setRefDeviceActionType(DeviceActionType refDeviceActionType) {
		this.refDeviceActionType = refDeviceActionType;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.DEVICE_EVENT;
	}

	public HashMap<String, Object> getInformation() {
		return information;
	}

	public void setInformation(HashMap<String, Object> information) {
		this.information = information;
	}

	public void addInformation(String key, Object value) {
		this.information.put(key, value);
	}

	public void addInformation(Map<String, String> map) {
		this.information.putAll(map);
	}
}

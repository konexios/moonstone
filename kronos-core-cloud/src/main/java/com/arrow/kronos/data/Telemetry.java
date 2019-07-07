package com.arrow.kronos.data;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.TsDocumentAbstract;
import com.arrow.pegasus.data.profile.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = Telemetry.COLLECTION_NAME)
public class Telemetry extends TsDocumentAbstract {
	private static final long serialVersionUID = 5460588220302188490L;
	public static final String COLLECTION_NAME = "telemetry";

	private String applicationId;
	@NotBlank
	private String deviceId;
	private long timestamp;
	private int payloadSize;

	@Transient
	@JsonIgnore
	private Device refDevice;
	@Transient
	@JsonIgnore
	private Application refApplication;
	@Transient
	@JsonIgnore
	private List<TelemetryItem> refItems = new ArrayList<>();

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public int getPayloadSize() {
		return payloadSize;
	}

	public void setPayloadSize(int payloadSize) {
		this.payloadSize = payloadSize;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Device getRefDevice() {
		return refDevice;
	}

	public void setRefDevice(Device refDevice) {
		this.refDevice = refDevice;
	}

	public List<TelemetryItem> getRefItems() {
		return refItems;
	}

	public void setRefItems(List<TelemetryItem> refItems) {
		this.refItems = refItems;
	}

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.TELEMETRY;
	}
}

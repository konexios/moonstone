package com.arrow.pegasus.webapi.data;

import java.io.Serializable;
import java.time.Instant;

import com.arrow.pegasus.data.location.LastLocation;
import com.arrow.pegasus.data.location.LastLocationType;
import com.arrow.pegasus.data.location.LocationObjectType;

public class LastLocationModel implements Serializable {
	private static final long serialVersionUID = 2358847620033422358L;

	private LocationObjectType objectType;
	private String objectId;
	private Double latitude;
	private Double longitude;
	private LastLocationType locationType;
	private Instant timestamp;
	private boolean validLocation = false;

	public LastLocationModel() {
	}

	public LastLocationModel(LastLocation lastLocation) {
		this.objectType = lastLocation.getObjectType();
		this.objectId = lastLocation.getObjectId();
		this.latitude = lastLocation.getLatitude();
		this.longitude = lastLocation.getLongitude();
		this.locationType = lastLocation.getLocationType();
		this.timestamp = lastLocation.getTimestamp();

		validLocation = this.latitude != null && this.longitude != null;
	}

	public LocationObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(LocationObjectType objectType) {
		this.objectType = objectType;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public LastLocationType getLocationType() {
		return locationType;
	}

	public void setLocationType(LastLocationType lastLocationType) {
		this.locationType = lastLocationType;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isValidLocation() {
		return validLocation;
	}
}

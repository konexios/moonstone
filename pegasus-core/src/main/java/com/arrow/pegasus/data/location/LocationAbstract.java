package com.arrow.pegasus.data.location;

import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.index.Indexed;

import com.arrow.pegasus.data.DocumentAbstract;

public abstract class LocationAbstract extends DocumentAbstract {
	private static final long serialVersionUID = -6512622741044247316L;

	public static final LastLocationType DEFAULT_LOCATION_TYPE = LastLocationType.DYNAMIC;

	@NotNull
	private LocationObjectType objectType;
	@NotBlank
	@Indexed(unique = true)
	private String objectId;
	private Double latitude;
	private Double longitude;
	@NotNull
	private LastLocationType locationType = DEFAULT_LOCATION_TYPE;
	private Instant timestamp;

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

	public void setLocationType(LastLocationType locationType) {
		this.locationType = locationType;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
}

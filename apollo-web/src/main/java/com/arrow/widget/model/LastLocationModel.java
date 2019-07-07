package com.arrow.widget.model;

import java.time.Instant;

import com.arrow.pegasus.data.location.LastLocationType;

public abstract class LastLocationModel<T extends LastLocationModel<T>> extends WigetModelAbstract<T> {
	private static final long serialVersionUID = 692760549398523461L;

	private String id;
	private String name;
	private String latitude;
	private String longitude;
	private LastLocationType locationType;
	private Instant timestamp;

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
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

	public T withId(String id) {
		setId(id);
		
		return self();
	}
	
	public T withName(String name) {
		setName(name);

		return self();
	}

	public T withLatitude(String latitude) {
		setLatitude(latitude);

		return self();
	}

	public T withLongitude(String longitude) {
		setLongitude(longitude);

		return self();
	}

	public T withLocationType(LastLocationType locationType) {
		setLocationType(locationType);

		return self();
	}

	public T withTimestamp(Instant timestamp) {
		setTimestamp(timestamp);

		return self();
	}
}

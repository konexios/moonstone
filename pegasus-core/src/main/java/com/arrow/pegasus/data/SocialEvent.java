package com.arrow.pegasus.data;

import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.profile.Zone;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = SocialEvent.COLLECTION_NAME)
public class SocialEvent extends AuditableDocumentAbstract {

	private static final long serialVersionUID = -3107066856786918763L;

	public static final String COLLECTION_NAME = "social_event";

	@NotBlank
	private String name;
	@NotNull
	private Instant startDate;
	@NotNull
	private Instant endDate;
	private String latitude;
	private String longitude;
	private Integer zoomLevel;
	@NotBlank
	private String zoneId;

	@Transient
	@JsonIgnore
	private Zone refZone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instant getStartDate() {
		return startDate;
	}

	public void setStartDate(Instant startDate) {
		this.startDate = startDate;
	}

	public Instant getEndDate() {
		return endDate;
	}

	public void setEndDate(Instant endDate) {
		this.endDate = endDate;
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

	public Integer getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(Integer zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public Zone getRefZone() {
		return refZone;
	}

	public void setRefZone(Zone refZone) {
		this.refZone = refZone;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.SOCIAL_EVENT;
	}
}

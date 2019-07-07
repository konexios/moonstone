package com.arrow.pegasus.data;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.profile.Zone;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = PlatformConfig.COLLECTION_NAME)
public class PlatformConfig extends DocumentAbstract {
	private static final long serialVersionUID = -6259734089168950403L;
	public static final String COLLECTION_NAME = "platform_config";

	@NotBlank
	private String zoneSystemName;
	private boolean validateZone = true;

	@Transient
	@JsonIgnore
	private Zone refZone;

	public Zone getRefZone() {
		return refZone;
	}

	public void setRefZone(Zone refZone) {
		this.refZone = refZone;
	}

	public String getZoneSystemName() {
		return zoneSystemName;
	}

	public void setZoneSystemName(String zoneSystemName) {
		this.zoneSystemName = zoneSystemName;
	}

	public boolean isValidateZone() {
		return validateZone;
	}

	public void setValidateZone(boolean validateZone) {
		this.validateZone = validateZone;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.PLATFORM_CONFIG;
	}
}

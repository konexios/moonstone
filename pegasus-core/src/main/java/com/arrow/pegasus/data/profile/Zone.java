package com.arrow.pegasus.data.profile;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "zone")
public class Zone extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = 7978066518720267702L;

	@NotBlank
	@Indexed(unique = true)
	private String systemName;
	@NotBlank
	private String regionId;
	private boolean hidden = CoreConstant.DEFAULT_HIDDEN;

	@Transient
	@JsonIgnore
	private Region refRegion;

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public Region getRefRegion() {
		return refRegion;
	}

	public void setRefRegion(Region refRegion) {
		this.refRegion = refRegion;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.ZONE;
	}
}

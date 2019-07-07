package com.arrow.rhea.data;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.arrow.rhea.RheaCoreConstants;

@Document(collection = "device_category")
public class DeviceCategory extends AuditableDocumentAbstract {
	private static final long serialVersionUID = -7395056340093327347L;

	@NotBlank
	private String name;
	@NotBlank
	private String description;
	private boolean enabled = CoreConstant.DEFAULT_ENABLED;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	protected String getProductPri() {
		return RheaCoreConstants.RHEA_PRI;
	}

	@Override
	protected String getTypePri() {
		return RheaCoreConstants.RheaPri.DEVICE_CATEGORY;
	}
}

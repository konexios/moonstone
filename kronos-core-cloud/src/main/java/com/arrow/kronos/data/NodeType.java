package com.arrow.kronos.data;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.arrow.pegasus.data.profile.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "node_type")
public class NodeType extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = 728058129880104958L;

	@NotBlank
	private String applicationId;
	// @Indexed
	// @NotNull
	// private String deviceCategoryId;

	@Transient
	@JsonIgnore
	private Application refApplication;
	// @Transient
	// @JsonIgnore
	// private DeviceCategory refDeviceCategory;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	// public String getDeviceCategoryId() {
	// return deviceCategoryId;
	// }
	//
	// public void setDeviceCategoryId(String deviceCategoryId) {
	// this.deviceCategoryId = deviceCategoryId;
	// }

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	// public DeviceCategory getRefDeviceCategory() {
	// return refDeviceCategory;
	// }
	//
	// public void setRefDeviceCategory(DeviceCategory refDeviceCategory) {
	// this.refDeviceCategory = refDeviceCategory;
	// }

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.NODE_TYPE;
	}
}

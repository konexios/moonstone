package com.arrow.kronos.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.rhea.data.DeviceProduct;
import com.fasterxml.jackson.annotation.JsonIgnore;

import moonstone.acn.client.model.AcnDeviceCategory;

@Document(collection = "device_type")
public class DeviceType extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = -6239204691991706838L;

	@NotBlank
	private String applicationId;
	private AcnDeviceCategory deviceCategory;
	private String rheaDeviceTypeId;
	private boolean editable = CoreConstant.DEFAULT_EDITABLE;
	private List<DeviceTelemetry> telemetries = new ArrayList<>();
	private Map<String, DeviceStateValueMetadata> stateMetadata = new HashMap<>();
	private List<DeviceAction> actions = new ArrayList<>();

	@Transient
	@JsonIgnore
	private Application refApplication;
	@Transient
	@JsonIgnore
	private DeviceProduct refDeviceProduct;
	@Transient
	@JsonIgnore
	private com.arrow.rhea.data.DeviceType refRheaDeviceType;

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

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public List<DeviceTelemetry> getTelemetries() {
		return telemetries;
	}

	public void setTelemetries(List<DeviceTelemetry> telemetries) {
		this.telemetries = telemetries;
	}

	public Map<String, DeviceStateValueMetadata> getStateMetadata() {
		return stateMetadata;
	}

	public void setStateMetadata(Map<String, DeviceStateValueMetadata> stateMetadata) {
		this.stateMetadata = stateMetadata;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.DEVICE_TYPE;
	}

	public AcnDeviceCategory getDeviceCategory() {
		return deviceCategory;
	}

	public void setDeviceCategory(AcnDeviceCategory deviceCategory) {
		this.deviceCategory = deviceCategory;
	}

	public DeviceProduct getRefDeviceProduct() {
		return refDeviceProduct;
	}

	public void setRefDeviceProduct(DeviceProduct refDeviceProduct) {
		this.refDeviceProduct = refDeviceProduct;
	}

	public String getRheaDeviceTypeId() {
		return rheaDeviceTypeId;
	}

	public void setRheaDeviceTypeId(String rheaDeviceTypeId) {
		this.rheaDeviceTypeId = rheaDeviceTypeId;
	}

	public com.arrow.rhea.data.DeviceType getRefRheaDeviceType() {
		return refRheaDeviceType;
	}

	public void setRefRheaDeviceType(com.arrow.rhea.data.DeviceType refRheaDeviceType) {
		this.refRheaDeviceType = refRheaDeviceType;
	}

	public List<DeviceAction> getActions() {
		return actions;
	}

	public void setActions(List<DeviceAction> actions) {
		this.actions = actions;
	}
}

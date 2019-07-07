package com.arrow.kronos.data;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.arrow.pegasus.data.profile.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "test_procedure")
public class TestProcedure extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = -2158569887428448777L;

	@NotBlank
	private String deviceTypeId;

	@NotBlank
	private String applicationId;

	private List<TestProcedureStep> steps = new ArrayList<>();

	@Transient
	@JsonIgnore
	private Application refApplication;

	@Transient
	@JsonIgnore
	private DeviceType refDeviceType;

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	public DeviceType getRefDeviceType() {
		return refDeviceType;
	}

	public void setRefDeviceType(DeviceType refDeviceType) {
		this.refDeviceType = refDeviceType;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public List<TestProcedureStep> getSteps() {
		return steps;
	}

	public void setSteps(List<TestProcedureStep> steps) {
		this.steps = steps;
	}

	public String getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(String deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.TEST_PROCEDURE;
	}
}

package com.arrow.kronos.data.action;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.arrow.pegasus.data.profile.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "global_action")
public class GlobalAction extends DefinitionCollectionAbstract {

	private static final long serialVersionUID = -681116030802752403L;

	@NotBlank
	private String globalActionTypeId;
	@NotBlank
	private String applicationId;
	@NotBlank
	private String systemName;
	private List<GlobalActionProperty> properties = new ArrayList<>();
	private List<GlobalActionInput> input = new ArrayList<>();

	@Transient
	@JsonIgnore
	private GlobalActionType refGlobalActionType;
	@Transient
	@JsonIgnore
	private Application refApplication;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getGlobalActionTypeId() {
		return globalActionTypeId;
	}

	public void setGlobalActionTypeId(String globalActionTypeId) {
		this.globalActionTypeId = globalActionTypeId;
	}

	public List<GlobalActionProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<GlobalActionProperty> properties) {
		this.properties = properties;
	}

	public List<GlobalActionInput> getInput() {
		return input;
	}

	public void setInput(List<GlobalActionInput> input) {
		this.input = input;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public GlobalActionType getRefGlobalActionType() {
		return refGlobalActionType;
	}

	public void setRefGlobalActionType(GlobalActionType refGlobalActionType) {
		this.refGlobalActionType = refGlobalActionType;
	}

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.GLOBAL_ACTION;
	}
}

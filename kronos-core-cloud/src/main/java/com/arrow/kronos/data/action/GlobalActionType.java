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

@Document(collection = "global_action_type")
public class GlobalActionType extends DefinitionCollectionAbstract {

	private static final long serialVersionUID = -1467123357647564061L;

	private final static boolean DEFAULT_EDITABLE = false;

	@NotBlank
	private String systemName;
	@NotBlank
	private String applicationId;
	private boolean editable = DEFAULT_EDITABLE;
	private List<GlobalActionTypeParameter> parameters = new ArrayList<>();

	@Transient
	@JsonIgnore
	private Application refApplication;

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
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

	public List<GlobalActionTypeParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<GlobalActionTypeParameter> parameters) {
		this.parameters = parameters;
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
		return KronosConstants.KronosPri.GLOBAL_ACTION_TYPE;
	}
}

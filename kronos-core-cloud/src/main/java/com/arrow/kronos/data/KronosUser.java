package com.arrow.kronos.data;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "user")
public class KronosUser extends AuditableDocumentAbstract {
	private static final long serialVersionUID = 4338737726980301193L;

	@NotBlank
	private String userId;
	@NotBlank
	private String applicationId;
	private IoTProvider iotProvider;

	@Transient
	@JsonIgnore
	private User refUser;

	@Transient
	@JsonIgnore
	private Application refApplication;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public IoTProvider getIotProvider() {
		return iotProvider;
	}

	public void setIotProvider(IoTProvider iotProvider) {
		this.iotProvider = iotProvider;
	}

	public User getRefUser() {
		return refUser;
	}

	public void setRefUser(User refUser) {
		this.refUser = refUser;
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
		return KronosConstants.KronosPri.KRONOS_USER;
	}
}

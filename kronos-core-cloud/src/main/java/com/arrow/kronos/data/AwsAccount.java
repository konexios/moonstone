package com.arrow.kronos.data;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.arrow.pegasus.data.Enabled;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "aws_account")
public class AwsAccount extends AuditableDocumentAbstract implements Enabled {
	private static final long serialVersionUID = 5102212560413109037L;

	@NotBlank
	@Indexed
	private String applicationId;
	private String userId;
	@NotBlank
	private String region;
	@NotBlank
	private String login;
	@NotBlank
	private String accessKey;
	@NotBlank
	private String secretKey;
	private String defaultPolicyArn;
	private String defaultPolicyName;
	private boolean enabled = CoreConstant.DEFAULT_ENABLED;

	@Transient
	@JsonIgnore
	private Application refApplication;
	@Transient
	@JsonIgnore
	private User refUser;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	public User getRefUser() {
		return refUser;
	}

	public void setRefUser(User refUser) {
		this.refUser = refUser;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getDefaultPolicyArn() {
		return defaultPolicyArn;
	}

	public void setDefaultPolicyArn(String defaultPolicyArn) {
		this.defaultPolicyArn = defaultPolicyArn;
	}

	public String getDefaultPolicyName() {
		return defaultPolicyName;
	}

	public void setDefaultPolicyName(String defaultPolicyName) {
		this.defaultPolicyName = defaultPolicyName;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.AWS_ACCOUNT;
	}
}

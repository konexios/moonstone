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
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "aws_thing")
public class AwsThing extends AuditableDocumentAbstract implements Enabled {
	private static final long serialVersionUID = -706314285452565498L;

	@NotBlank
	@Indexed
	private String applicationId;
	@NotBlank
	private String awsAccountId;
	@NotBlank
	private String gatewayId;
	private String policyName;
	private String thingName;
	private String thingArn;
	private String certId;
	private String certArn;
	private String certPem;
	private String publicKey;
	private String privateKey;
	private String host;
	private boolean enabled = CoreConstant.DEFAULT_ENABLED;

	@Transient
	@JsonIgnore
	private AwsAccount refAwsAccount;
	@Transient
	@JsonIgnore
	private Application refApplication;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	public AwsAccount getRefAwsAccount() {
		return refAwsAccount;
	}

	public void setRefAwsAccount(AwsAccount refAwsAccount) {
		this.refAwsAccount = refAwsAccount;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getThingArn() {
		return thingArn;
	}

	public void setThingArn(String thingArn) {
		this.thingArn = thingArn;
	}

	public String getThingName() {
		return thingName;
	}

	public void setThingName(String thingName) {
		this.thingName = thingName;
	}

	public String getAwsAccountId() {
		return awsAccountId;
	}

	public void setAwsAccountId(String awsAccountId) {
		this.awsAccountId = awsAccountId;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	public String getCertArn() {
		return certArn;
	}

	public void setCertArn(String certArn) {
		this.certArn = certArn;
	}

	public String getCertPem() {
		return certPem;
	}

	public void setCertPem(String certPem) {
		this.certPem = certPem;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.AWS_DEVICE;
	}
}

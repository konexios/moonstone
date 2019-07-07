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

@Document(collection = "azure_device")
public class AzureDevice extends AuditableDocumentAbstract implements Enabled {
	private static final long serialVersionUID = 2137012655236005197L;

	@NotBlank
	@Indexed
	private String applicationId;
	@NotBlank
	private String azureAccountId;
	@NotBlank
	private String gatewayId;
	@NotBlank
	private String primaryKey;
	@NotBlank
	private String secondaryKey;
	private boolean enabled = CoreConstant.DEFAULT_ENABLED;

	@Transient
	@JsonIgnore
	private AzureAccount refAzureAccount;
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

	public AzureAccount getRefAzureAccount() {
		return refAzureAccount;
	}

	public void setRefAzureAccount(AzureAccount refAzureAccount) {
		this.refAzureAccount = refAzureAccount;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getSecondaryKey() {
		return secondaryKey;
	}

	public void setSecondaryKey(String secondaryKey) {
		this.secondaryKey = secondaryKey;
	}

	public String getAzureAccountId() {
		return azureAccountId;
	}

	public void setAzureAccountId(String azureAccountId) {
		this.azureAccountId = azureAccountId;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.AZURE_DEVICE;
	}
}

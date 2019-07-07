package com.arrow.kronos.data;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.arrow.pegasus.data.Enabled;

@Document(collection = "ibm_device")
public class IbmDevice extends AuditableDocumentAbstract implements Enabled {
	private static final long serialVersionUID = -3655775174786311200L;

	private String applicationId;
	@NotBlank
	private String ibmAccountId;
	@NotBlank
	private String deviceId;
	@NotBlank
	private String authToken;
	private boolean enabled = CoreConstant.DEFAULT_ENABLED;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getIbmAccountId() {
		return ibmAccountId;
	}

	public void setIbmAccountId(String ibmAccountId) {
		this.ibmAccountId = ibmAccountId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.IBM_GATEWAY;
	}
}

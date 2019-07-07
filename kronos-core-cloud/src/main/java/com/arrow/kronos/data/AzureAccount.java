package com.arrow.kronos.data;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosCloudConstants;
import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.arrow.pegasus.data.Enabled;
import com.arrow.pegasus.data.profile.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "azure_account")
public class AzureAccount extends AuditableDocumentAbstract implements Enabled {
	private static final long serialVersionUID = 9182970630015601835L;

	public enum TelemetrySync {
		NONE, CURRENT, FULL
	}

	@NotBlank
	@Indexed
	private String applicationId;
	private String userId;
	@NotBlank
	private String hostName;
	@NotBlank
	private String accessKeyName;
	@NotBlank
	private String accessKey;
	@NotBlank
	private String eventHubName;
	@NotBlank
	private String eventHubEndpoint;
	private int numPartitions = KronosCloudConstants.Azure.DEFAULT_NUM_PARTITIONS;
	private String consumerGroupName;
	private TelemetrySync telemetrySync = TelemetrySync.CURRENT;
	private boolean enabled = CoreConstant.DEFAULT_ENABLED;

	@Transient
	@JsonIgnore
	private Application refApplication;

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

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
		return KronosConstants.KronosPri.AZURE_ACCOUNT;
	}

	public String getAccessKeyName() {
		return accessKeyName;
	}

	public void setAccessKeyName(String accessKeyName) {
		this.accessKeyName = accessKeyName;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getEventHubName() {
		return eventHubName;
	}

	public void setEventHubName(String eventHubName) {
		this.eventHubName = eventHubName;
	}

	public String getEventHubEndpoint() {
		return eventHubEndpoint;
	}

	public void setEventHubEndpoint(String eventHubEndpoint) {
		this.eventHubEndpoint = eventHubEndpoint;
	}

	public int getNumPartitions() {
		return numPartitions;
	}

	public void setNumPartitions(int numPartitions) {
		this.numPartitions = numPartitions;
	}

	public String getConsumerGroupName() {
		return consumerGroupName;
	}

	public void setConsumerGroupName(String consumerGroupName) {
		this.consumerGroupName = consumerGroupName;
	}

	public TelemetrySync getTelemetrySync() {
		return telemetrySync;
	}

	public void setTelemetrySync(TelemetrySync telemetrySync) {
		this.telemetrySync = telemetrySync;
	}
}

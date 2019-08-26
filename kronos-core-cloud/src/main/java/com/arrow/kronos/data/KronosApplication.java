package com.arrow.kronos.data;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosCloudConstants;
import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.arrow.pegasus.data.profile.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;

import moonstone.acs.client.model.YesNoInherit;

@Document(collection = "application")
public class KronosApplication extends AuditableDocumentAbstract {
	private static final long serialVersionUID = 7272694482936576988L;

	@NotBlank
	private String applicationId; // pegasus applicationId
	@NotNull
	private IoTProvider iotProvider;
	private boolean allowCreateGatewayFromDifferentApp = true;
	private YesNoInherit persistTelemetry = YesNoInherit.YES;
	private YesNoInherit indexTelemetry = YesNoInherit.YES;
	private boolean liveTelemetryStreamingEnabled = KronosCloudConstants.LiveTelemetryStreaming.DEFAULT_ENABLED;
	private long liveTelemetryStreamingRetentionSecs = KronosCloudConstants.LiveTelemetryStreaming.DEFAULT_RETENTION_SECS;
	private String defaultSoftwareReleaseEmails;
	private boolean allowGatewayTransfer = false;
	private int telemetryRetentionDays;
	private boolean iotProviderLoopback = true;
	private List<Integration> integrations = new ArrayList<>();

	@Transient
	@JsonIgnore
	private Application refApplication; // pagasus application object

	public int getTelemetryRetentionDays() {
		return telemetryRetentionDays;
	}

	public void setTelemetryRetentionDays(int telemetryRetentionDays) {
		this.telemetryRetentionDays = telemetryRetentionDays;
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

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	public boolean isAllowCreateGatewayFromDifferentApp() {
		return allowCreateGatewayFromDifferentApp;
	}

	public void setAllowCreateGatewayFromDifferentApp(boolean allowCreateGatewayFromDifferentApp) {
		this.allowCreateGatewayFromDifferentApp = allowCreateGatewayFromDifferentApp;
	}

	public YesNoInherit getPersistTelemetry() {
		return persistTelemetry;
	}

	public void setPersistTelemetry(YesNoInherit persistTelemetry) {
		this.persistTelemetry = persistTelemetry;
	}

	public YesNoInherit getIndexTelemetry() {
		return indexTelemetry;
	}

	public void setIndexTelemetry(YesNoInherit indexTelemetry) {
		this.indexTelemetry = indexTelemetry;
	}

	public boolean isLiveTelemetryStreamingEnabled() {
		return liveTelemetryStreamingEnabled;
	}

	public void setLiveTelemetryStreamingEnabled(boolean liveTelemetryStreamingEnabled) {
		this.liveTelemetryStreamingEnabled = liveTelemetryStreamingEnabled;
	}

	public long getLiveTelemetryStreamingRetentionSecs() {
		return liveTelemetryStreamingRetentionSecs;
	}

	public void setLiveTelemetryStreamingRetentionSecs(long liveTelemetryStreamingRetentionSecs) {
		this.liveTelemetryStreamingRetentionSecs = liveTelemetryStreamingRetentionSecs;
	}

	public String getDefaultSoftwareReleaseEmails() {
		return defaultSoftwareReleaseEmails;
	}

	public void setDefaultSoftwareReleaseEmails(String defaultSoftwareReleaseEmails) {
		this.defaultSoftwareReleaseEmails = defaultSoftwareReleaseEmails;
	}

	public boolean isAllowGatewayTransfer() {
		return allowGatewayTransfer;
	}

	public void setAllowGatewayTransfer(boolean allowGatewayTransfer) {
		this.allowGatewayTransfer = allowGatewayTransfer;
	}

	public boolean isIotProviderLoopback() {
		return iotProviderLoopback;
	}

	public void setIotProviderLoopback(boolean iotProviderLoopback) {
		this.iotProviderLoopback = iotProviderLoopback;
	}

	public List<Integration> getIntegrations() {
		return integrations;
	}

	public void setIntegrations(List<Integration> integrations) {
		this.integrations = integrations;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.KRONOS_APPLICATION;
	}
}
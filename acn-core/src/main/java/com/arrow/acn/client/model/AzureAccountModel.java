/*******************************************************************************
 * Copyright (c) 2018 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *******************************************************************************/
package com.arrow.acn.client.model;

import com.arrow.acs.client.model.AuditableDocumentModelAbstract;

public class AzureAccountModel extends AuditableDocumentModelAbstract<AzureAccountModel> {

	private static final long serialVersionUID = -8205580201727669257L;

	private String applicationHid;
	private String userHid;
	private String hostName;
	private String accessKeyName;
	private String accessKey;
	private String eventHubName;
	private String eventHubEndpoint;
	private int numPartitions;
	private String telemetrySync;
	private boolean enabled;
	private String consumerGroupName;

	@Override
	protected AzureAccountModel self() {
		return this;
	}

	public AzureAccountModel withApplicationHid(String applicationHid) {
		setApplicationHid(applicationHid);
		return this;
	}

	public AzureAccountModel withUserHid(String userHid) {
		setUserHid(userHid);
		return this;
	}

	public AzureAccountModel withHostName(String hostName) {
		setHostName(hostName);
		return this;
	}

	public AzureAccountModel withAccessKeyName(String accesskeyName) {
		setAccessKeyName(accesskeyName);
		return this;
	}

	public AzureAccountModel withAccessKey(String accessKey) {
		setAccessKey(accessKey);
		return this;
	}

	public AzureAccountModel withEventHubName(String eventHubName) {
		setEventHubName(eventHubName);
		return this;
	}

	public AzureAccountModel withEventHubEndpoint(String eventHubEndpoint) {
		setEventHubEndpoint(eventHubEndpoint);
		return this;
	}

	public AzureAccountModel withNumPartitions(int numPartitions) {
		setNumPartitions(numPartitions);
		return this;
	}

	public AzureAccountModel withTelemetrySync(String telemetrySync) {
		setTelemetrySync(telemetrySync);
		return this;
	}

	public AzureAccountModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public AzureAccountModel withConsumerGroupName(String consumerGroupName) {
		setConsumerGroupName(consumerGroupName);
		return this;
	}

	public String getApplicationHid() {
		return applicationHid;
	}

	public void setApplicationHid(String applicationHid) {
		this.applicationHid = applicationHid;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
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

	public String getTelemetrySync() {
		return telemetrySync;
	}

	public void setTelemetrySync(String telemetrySync) {
		this.telemetrySync = telemetrySync;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getConsumerGroupName() {
		return consumerGroupName;
	}

	public void setConsumerGroupName(String consumerGroupName) {
		this.consumerGroupName = consumerGroupName;
	}

	public String getUserHid() {
		return userHid;
	}

	public void setUserHid(String userHid) {
		this.userHid = userHid;
	}
}

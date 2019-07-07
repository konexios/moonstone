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

public class AwsAccountModel extends AuditableDocumentModelAbstract<AwsAccountModel> {
	private static final long serialVersionUID = 4379771072996285501L;

	private String accessKey;
	private String applicationHid;
	private String defaultPolicyArn;
	private String defaultPolicyName;
	private String login;
	private String region;
	private String secretKey;
	private String userHid;
	private boolean enabled;

	@Override
	protected AwsAccountModel self() {
		return this;
	}

	public AwsAccountModel withAccessKey() {
		setAccessKey(accessKey);
		return this;
	}

	public AwsAccountModel withApplicationHid(String applicationHid) {
		setApplicationHid(applicationHid);
		return this;
	}

	public AwsAccountModel withDefaultPolicyArn(String defaultPolicyArn) {
		setDefaultPolicyArn(defaultPolicyArn);
		return this;
	}

	public AwsAccountModel withDefaultPolicyName(String defaultPolicyName) {
		setDefaultPolicyName(defaultPolicyName);
		return this;
	}

	public AwsAccountModel withLogin(String login) {
		setLogin(login);
		return this;
	}

	public AwsAccountModel withRegion(String region) {
		setRegion(region);
		return this;
	}

	public AwsAccountModel withSecretKey(String secretKey) {
		setSecretKey(secretKey);
		return this;
	}

	public AwsAccountModel withUserHid(String userHid) {
		setUserHid(userHid);
		return this;
	}

	public AwsAccountModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getApplicationHid() {
		return applicationHid;
	}

	public void setApplicationHid(String applicationHid) {
		this.applicationHid = applicationHid;
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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getUserHid() {
		return userHid;
	}

	public void setUserHid(String userHid) {
		this.userHid = userHid;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}

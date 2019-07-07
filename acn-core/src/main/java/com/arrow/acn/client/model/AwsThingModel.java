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

public class AwsThingModel extends AuditableDocumentModelAbstract<AwsThingModel> {
	private static final long serialVersionUID = -3865374064160170604L;

	private String policyName;
	private String thingName;
	private String thingArn;
	private String certId;
	private String certArn;
	private String certPem;
	private String publicKey;
	private String privateKey;
	private String host;
	private String awsAccountHid;
	private String gatewayHid;
	private boolean enabled;

	@Override
	protected AwsThingModel self() {
		return this;
	}

	public AwsThingModel withPolicyName(String policyName) {
		setPolicyName(policyName);
		return this;
	}

	public AwsThingModel withThingName(String thingName) {
		setThingName(thingName);
		return this;
	}

	public AwsThingModel withThingArn(String thingArn) {
		setThingArn(thingArn);
		return this;
	}

	public AwsThingModel withCertId(String certId) {
		setCertId(certId);
		return this;
	}

	public AwsThingModel withCertArn(String certArn) {
		setCertArn(certArn);
		return this;
	}

	public AwsThingModel withCertPem(String certPem) {
		setCertPem(certPem);
		return this;
	}

	public AwsThingModel withPublicKey(String publicKey) {
		setPublicKey(publicKey);
		return this;
	}

	public AwsThingModel withPrivateKey(String privateKey) {
		setPrivateKey(privateKey);
		return this;
	}

	public AwsThingModel withHost(String host) {
		setHost(host);
		return this;
	}

	public AwsThingModel withAwsAccountId(String awsAccountId) {
		setAwsAccountHid(awsAccountId);
		return this;
	}

	public AwsThingModel withGatewayHid(String gatewayHid) {
		setGatewayHid(gatewayHid);
		return this;
	}

	public AwsThingModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public String getThingName() {
		return thingName;
	}

	public void setThingName(String thingName) {
		this.thingName = thingName;
	}

	public String getThingArn() {
		return thingArn;
	}

	public void setThingArn(String thingArn) {
		this.thingArn = thingArn;
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

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getAwsAccountHid() {
		return awsAccountHid;
	}

	public void setAwsAccountHid(String awsAccountHid) {
		this.awsAccountHid = awsAccountHid;
	}

	public String getGatewayHid() {
		return gatewayHid;
	}

	public void setGatewayHid(String gatewayHid) {
		this.gatewayHid = gatewayHid;
	}
}
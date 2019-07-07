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
package com.arrow.acs.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreateApplicationModel implements Serializable {

	private static final long serialVersionUID = 1096427303857446507L;

	private String zoneHid;
	private String companyHid;
	private String productHid;
	private String subscriptionHid;
	private YesNoInherit apiSigningRequired;
	private String applicationEngineHid;
	private String defaultSamlEntityId;
	private String code;
	private List<ConfigurationPropertyModel> configurations = new ArrayList<>();
	private List<String> productExtensionHids = new ArrayList<>();
	private String description;
	private String name;
	private boolean enabled;

	public CreateApplicationModel withZoneHid(String zoneHid) {
		setZoneHid(zoneHid);
		return this;
	}

	public CreateApplicationModel withCompanyHid(String companyHid) {
		setCompanyHid(companyHid);
		return this;
	}

	public CreateApplicationModel withProductHid(String productHid) {
		setProductHid(productHid);
		return this;
	}

	public CreateApplicationModel withSubscriptionHid(String subscriptionHid) {
		setSubscriptionHid(subscriptionHid);
		return this;
	}

	public CreateApplicationModel withApiSigningRequired(YesNoInherit apiSigningRequired) {
		setApiSigningRequired(apiSigningRequired);
		return this;
	}

	public CreateApplicationModel withApplicationEngineHid(String applicationEngineHid) {
		setApplicationEngineHid(applicationEngineHid);
		return this;
	}

	public CreateApplicationModel withDefaultSamlEntityId(String defaultSamlEntityId) {
		setDefaultSamlEntityId(defaultSamlEntityId);
		return this;
	}

	public CreateApplicationModel withCode(String code) {
		setCode(code);
		return this;
	}

	public CreateApplicationModel withConfigurations(List<ConfigurationPropertyModel> configurations) {
		setConfigurations(configurations);
		return this;
	}

	public CreateApplicationModel withProductExtensionHids(List<String> productExtensionHids) {
		setProductExtensionHids(productExtensionHids);
		return this;
	}

	public CreateApplicationModel withDescription(String description) {
		setDescription(description);
		return this;
	}

	public CreateApplicationModel withName(String name) {
		setName(name);
		return this;
	}

	public CreateApplicationModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public String getZoneHid() {
		return zoneHid;
	}

	public void setZoneHid(String zoneHid) {
		this.zoneHid = zoneHid;
	}

	public String getCompanyHid() {
		return companyHid;
	}

	public void setCompanyHid(String companyHid) {
		this.companyHid = companyHid;
	}

	public String getSubscriptionHid() {
		return subscriptionHid;
	}

	public void setSubscriptionHid(String subscriptionHid) {
		this.subscriptionHid = subscriptionHid;
	}

	public String getProductHid() {
		return productHid;
	}

	public void setProductHid(String productHid) {
		this.productHid = productHid;
	}

	public YesNoInherit getApiSigningRequired() {
		return apiSigningRequired;
	}

	public void setApiSigningRequired(YesNoInherit apiSigningRequired) {
		this.apiSigningRequired = apiSigningRequired;
	}

	public String getApplicationEngineHid() {
		return applicationEngineHid;
	}

	public void setApplicationEngineHid(String applicationEngineHid) {
		this.applicationEngineHid = applicationEngineHid;
	}

	public String getDefaultSamlEntityId() {
		return defaultSamlEntityId;
	}

	public void setDefaultSamlEntityId(String defaultSamlEntityId) {
		this.defaultSamlEntityId = defaultSamlEntityId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<ConfigurationPropertyModel> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<ConfigurationPropertyModel> configurations) {
		this.configurations = configurations;
	}

	public List<String> getProductExtensionHids() {
		return productExtensionHids;
	}

	public void setProductExtensionHids(List<String> productExtensionHids) {
		this.productExtensionHids = productExtensionHids;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiSigningRequired == null) ? 0 : apiSigningRequired.hashCode());
		result = prime * result + ((applicationEngineHid == null) ? 0 : applicationEngineHid.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((companyHid == null) ? 0 : companyHid.hashCode());
		result = prime * result + ((configurations == null) ? 0 : configurations.hashCode());
		result = prime * result + ((defaultSamlEntityId == null) ? 0 : defaultSamlEntityId.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((productExtensionHids == null) ? 0 : productExtensionHids.hashCode());
		result = prime * result + ((productHid == null) ? 0 : productHid.hashCode());
		result = prime * result + ((subscriptionHid == null) ? 0 : subscriptionHid.hashCode());
		result = prime * result + ((zoneHid == null) ? 0 : zoneHid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreateApplicationModel other = (CreateApplicationModel) obj;
		if (apiSigningRequired != other.apiSigningRequired)
			return false;
		if (applicationEngineHid == null) {
			if (other.applicationEngineHid != null)
				return false;
		} else if (!applicationEngineHid.equals(other.applicationEngineHid))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (companyHid == null) {
			if (other.companyHid != null)
				return false;
		} else if (!companyHid.equals(other.companyHid))
			return false;
		if (configurations == null) {
			if (other.configurations != null)
				return false;
		} else if (!configurations.equals(other.configurations))
			return false;
		if (defaultSamlEntityId == null) {
			if (other.defaultSamlEntityId != null)
				return false;
		} else if (!defaultSamlEntityId.equals(other.defaultSamlEntityId))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (enabled != other.enabled)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (productExtensionHids == null) {
			if (other.productExtensionHids != null)
				return false;
		} else if (!productExtensionHids.equals(other.productExtensionHids))
			return false;
		if (productHid == null) {
			if (other.productHid != null)
				return false;
		} else if (!productHid.equals(other.productHid))
			return false;
		if (subscriptionHid == null) {
			if (other.subscriptionHid != null)
				return false;
		} else if (!subscriptionHid.equals(other.subscriptionHid))
			return false;
		if (zoneHid == null) {
			if (other.zoneHid != null)
				return false;
		} else if (!zoneHid.equals(other.zoneHid))
			return false;
		return true;
	}
}

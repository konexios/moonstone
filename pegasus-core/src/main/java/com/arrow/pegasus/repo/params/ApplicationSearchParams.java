package com.arrow.pegasus.repo.params;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

import com.arrow.pegasus.data.YesNoInherit;

public class ApplicationSearchParams extends AuditableDocumentSearchParams {

	private static final long serialVersionUID = -3374472269992953669L;

	private String name;
	private String code;
	private Set<String> companyIds;
	private Set<String> subscriptionIds;
	private Set<String> zoneIds;
	private Set<String> productIds;
	private Set<String> productExtensionIds;
	private EnumSet<YesNoInherit> apiSigningRequired;
	private Boolean enabled;
	private Instant createdBefore;

	private Set<String> codes;
	private boolean includeDisabled = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ApplicationSearchParams withName(String name) {
		setName(name);

		return this;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ApplicationSearchParams withCode(String code) {
		setCode(code);

		return this;
	}

	public Set<String> getCompanyIds() {
		return companyIds;
	}

	public void setCompanyIds(Set<String> companyIds) {
		this.companyIds = companyIds;
	}

	public ApplicationSearchParams addCompanyIds(String... values) {
		this.companyIds = addValues(this.companyIds, values);

		return this;
	}

	public ApplicationSearchParams withCompanyIds(String... companyIds) {
		this.companyIds = null;

		return addCompanyIds(companyIds);
	}

	public Set<String> getSubscriptionIds() {
		return subscriptionIds;
	}

	public void setSubscriptionIds(Set<String> subscriptionIds) {
		this.subscriptionIds = subscriptionIds;
	}

	public ApplicationSearchParams addSubscriptionIds(String... values) {
		this.subscriptionIds = addValues(this.subscriptionIds, values);

		return this;
	}

	public ApplicationSearchParams withSubscriptionIds(String... subscriptionIds) {
		this.subscriptionIds = null;

		return addSubscriptionIds(subscriptionIds);
	}

	public Set<String> getZoneIds() {
		return zoneIds;
	}

	public void setZoneIds(Set<String> zoneIds) {
		this.zoneIds = zoneIds;
	}

	public ApplicationSearchParams addZoneIds(String... zoneIds) {
		this.zoneIds = addValues(this.zoneIds, zoneIds);

		return this;
	}

	public ApplicationSearchParams withZoneIds(String... zoneIds) {
		this.zoneIds = null;

		return addZoneIds(zoneIds);
	}

	public Set<String> getProductIds() {
		return productIds;
	}

	public void setProductIds(Set<String> productIds) {
		this.productIds = productIds;
	}

	public ApplicationSearchParams addProductIds(String... values) {
		this.productIds = addValues(this.productIds, values);

		return this;
	}

	public ApplicationSearchParams withProductIds(String... productIds) {
		this.productIds = null;

		return addProductIds(productIds);
	}

	public Set<String> getProductExtensionIds() {
		return productExtensionIds;
	}

	public void setProductExtensionIds(Set<String> productExtensionIds) {
		this.productExtensionIds = productExtensionIds;
	}

	public ApplicationSearchParams addProductExtensionIds(String... productExtensionIds) {
		this.productExtensionIds = addValues(this.productExtensionIds, productExtensionIds);

		return this;
	}

	public ApplicationSearchParams withProductExtensionIds(String... productExtensionIds) {
		this.productExtensionIds = null;

		return addProductExtensionIds(productExtensionIds);
	}

	public EnumSet<YesNoInherit> getApiSigningRequired() {
		return apiSigningRequired;
	}

	public void setApiSigningRequired(EnumSet<YesNoInherit> apiSigningRequired) {
		this.apiSigningRequired = apiSigningRequired;
	}

	public ApplicationSearchParams withApiSigningRequired(EnumSet<YesNoInherit> apiSigningRequired) {
		setApiSigningRequired(apiSigningRequired);

		return this;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public ApplicationSearchParams withEnabled(Boolean enabled) {
		setEnabled(enabled);

		return this;
	}

	public Set<String> getCodes() {
		return codes;
	}

	public void setCodes(Set<String> codes) {
		this.codes = codes;
	}

	public ApplicationSearchParams addCodes(String... values) {
		this.codes = addValues(this.codes, values);
		return this;
	}

	@Deprecated
	public boolean includeDisabled() {
		return includeDisabled;
	}

	@Deprecated
	public ApplicationSearchParams setIncludeDisabled(boolean includeDisabled) {
		this.includeDisabled = includeDisabled;
		return this;
	}

	public Instant getCreatedBefore() {
		return createdBefore;
	}

	public void setCreatedBefore(Instant createdBefore) {
		this.createdBefore = createdBefore;
	}

	public ApplicationSearchParams createdBefore(Instant date) {
		setCreatedBefore(date);
		return this;
	}
}

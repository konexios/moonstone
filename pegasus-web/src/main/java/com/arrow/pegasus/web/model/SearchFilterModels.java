package com.arrow.pegasus.web.model;

import java.util.EnumSet;

import com.arrow.pegasus.data.YesNoInherit;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.webapi.data.CoreSearchFilterModel;

public class SearchFilterModels {

	public static class ProductSearchFilter extends CoreSearchFilterModel {
		private static final long serialVersionUID = -686273045335630079L;

		// TODO

		public ProductSearchFilter() {
			super();
		}
	}

	public static class CompanySearchFilter extends CoreSearchFilterModel {
		private static final long serialVersionUID = -98293681453019045L;

		private String name;
		private String abbrName;
		private CompanyStatus[] statuses;
		private String[] parentCompanyIds;

		public CompanySearchFilter() {
			super();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAbbrName() {
			return abbrName;
		}

		public void setAbbrName(String abbrName) {
			this.abbrName = abbrName;
		}

		public CompanyStatus[] getStatuses() {
			return statuses;
		}

		public void setStatuses(CompanyStatus[] statuses) {
			this.statuses = statuses;
		}

		public String[] getParentCompanyIds() {
			return parentCompanyIds;
		}

		public void setParentCompanyIds(String[] parentCompanyIds) {
			this.parentCompanyIds = parentCompanyIds;
		}
	}

	public static class SubscriptionSearchFilter extends CoreSearchFilterModel {
		private static final long serialVersionUID = -1257083556597985838L;

		private String name;
		private String[] companyIds;
		private String enabled;

		public SubscriptionSearchFilter() {
			super();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String[] getCompanyIds() {
			return companyIds;
		}

		public void setCompanyIds(String[] companyIds) {
			this.companyIds = companyIds;
		}

		public String getEnabled() {
			return enabled;
		}

		public void setEnabled(String enabled) {
			this.enabled = enabled;
		}
	}

	public static class ApplicationSearchFilter extends CoreSearchFilterModel {
		private static final long serialVersionUID = -4081327193732611920L;

		private String name;
		private String code;
		private String[] companyIds;
		private String[] subscriptionIds;
		private String[] regionIds;
		private String[] zoneIds;
		private String[] productIds;
		private String[] productExtensionIds;
		private EnumSet<YesNoInherit> apiSigningRequired;
		private String enabled;

		public ApplicationSearchFilter() {
			super();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String[] getCompanyIds() {
			return companyIds;
		}

		public void setCompanyIds(String[] companyIds) {
			this.companyIds = companyIds;
		}

		public String[] getSubscriptionIds() {
			return subscriptionIds;
		}

		public void setSubscriptionIds(String[] subscriptionIds) {
			this.subscriptionIds = subscriptionIds;
		}

		public String[] getRegionIds() {
			return regionIds;
		}

		public void setRegionIds(String[] regionIds) {
			this.regionIds = regionIds;
		}

		public String[] getZoneIds() {
			return zoneIds;
		}

		public void setZoneIds(String[] zoneIds) {
			this.zoneIds = zoneIds;
		}

		public String[] getProductIds() {
			return productIds;
		}

		public void setProductIds(String[] productIds) {
			this.productIds = productIds;
		}

		public String[] getProductExtensionIds() {
			return productExtensionIds;
		}

		public void setProductExtensionIds(String[] productExtensionIds) {
			this.productExtensionIds = productExtensionIds;
		}

		public EnumSet<YesNoInherit> getApiSigningRequired() {
			return apiSigningRequired;
		}

		public void setApiSigningRequired(EnumSet<YesNoInherit> apiSigningRequired) {
			this.apiSigningRequired = apiSigningRequired;
		}

		public String getEnabled() {
			return enabled;
		}

		public void setEnabled(String enabled) {
			this.enabled = enabled;
		}
	}

	public static class UserSearchFilter extends CoreSearchFilterModel {
		private static final long serialVersionUID = -8132056256322890439L;

		private String firstName;
		private String lastName;
		private String login;
		private String[] companyIds;
		private UserStatus[] statuses;
		private String firstNameLastNameAndLogin;

		public UserSearchFilter() {
			super();
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String[] getCompanyIds() {
			return companyIds;
		}

		public void setCompanyIds(String[] companyIds) {
			this.companyIds = companyIds;
		}

		public UserStatus[] getStatuses() {
			return statuses;
		}

		public void setStatuses(UserStatus[] statuses) {
			this.statuses = statuses;
		}

		public String getFirstNameLastNameAndLogin() {
			return firstNameLastNameAndLogin;
		}

		public void setFirstNameLastNameAndLogin(String firstNameLastNameAndLogin) {
			this.firstNameLastNameAndLogin = firstNameLastNameAndLogin;
		}
	}

	public static class RoleSearchFilter extends CoreSearchFilterModel {
		private static final long serialVersionUID = -5535384849592097533L;

		private String name;
		private String[] productIds;
		private String[] applicationIds;
		private String[] companyIds;
		private String enabled;
		private String editable;

		public RoleSearchFilter() {
			super();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String[] getProductIds() {
			return productIds;
		}

		public void setProductIds(String[] productIds) {
			this.productIds = productIds;
		}

		public String[] getApplicationIds() {
			return applicationIds;
		}

		public void setApplicationIds(String[] applicationIds) {
			this.applicationIds = applicationIds;
		}

		public String[] getCompanyIds() {
			return companyIds;
		}

		public void setCompanyIds(String[] companyIds) {
			this.companyIds = companyIds;
		}

		public String getEnabled() {
			return enabled;
		}

		public void setEnabled(String enabled) {
			this.enabled = enabled;
		}

		public String getEditable() {
			return editable;
		}

		public void setEditable(String editable) {
			this.editable = editable;
		}
	}

	public static class PrivilegeSearchFilter extends CoreSearchFilterModel {
		private static final long serialVersionUID = -3466701975194934596L;

		private String name;
		private String systemName;
		private String[] productIds;
		private String enabled;

		public PrivilegeSearchFilter() {
			super();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSystemName() {
			return systemName;
		}

		public void setSystemName(String systemName) {
			this.systemName = systemName;
		}

		public String[] getProductIds() {
			return productIds;
		}

		public void setProductIds(String[] productIds) {
			this.productIds = productIds;
		}

		public String getEnabled() {
			return enabled;
		}

		public void setEnabled(String enabled) {
			this.enabled = enabled;
		}
	}

	public static class RegionSearchFilter extends CoreSearchFilterModel {
		private static final long serialVersionUID = -1757061207257516108L;

		// TODO

		public RegionSearchFilter() {
			super();
		}
	}

	public static class ZoneSearchFilter extends CoreSearchFilterModel {
		private static final long serialVersionUID = -4293468573782389202L;

		// TODO

		public ZoneSearchFilter() {
			super();
		}
	}
	
	public static class AccessKeySearchFilter extends CoreSearchFilterModel {
		private static final long serialVersionUID = -4293468573782389202L;

		public enum RelationEntityType {
			MY_KEYS, CHILD_KEYS
		}

		private String name;
		private String[] accessLevels;
		private String[] pri;
		private Long expirationDateFrom;
		private Long expirationDateTo;
		private RelationEntityType relationEntityType;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String[] getAccessLevels() {
			return accessLevels;
		}

		public void setAccessLevels(String[] accessLevels) {
			this.accessLevels = accessLevels;
		}

		public Long getExpirationDateFrom() {
			return expirationDateFrom;
		}

		public void setExpirationDateFrom(Long expirationDateFrom) {
			this.expirationDateFrom = expirationDateFrom;
		}

		public Long getExpirationDateTo() {
			return expirationDateTo;
		}

		public void setExpirationDateTo(Long expirationDateTo) {
			this.expirationDateTo = expirationDateTo;
		}

		public String[] getPri() {
			return pri;
		}

		public void setPri(String[] pri) {
			this.pri = pri;
		}

		public RelationEntityType getRelationEntityType() {
			return relationEntityType;
		}

		public void setRelationEntityType(RelationEntityType relationEntityType) {
			this.relationEntityType = relationEntityType;
		}

		public AccessKeySearchFilter() {
			super();
		}
	}
}

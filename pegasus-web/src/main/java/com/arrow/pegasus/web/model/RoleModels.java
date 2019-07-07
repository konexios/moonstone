package com.arrow.pegasus.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;
import com.arrow.pegasus.webapi.data.CorePrivilegeModels;
import com.arrow.pegasus.webapi.data.CoreProductModels;
import com.arrow.pegasus.webapi.data.CoreRoleModels;
import com.arrow.pegasus.webapi.data.KeyValueOption;

public class RoleModels extends CoreRoleModels {

	public static class RoleFilterOptions implements Serializable {
		private static final long serialVersionUID = 7226811110900953631L;

		private List<ProductModels.ProductOption> productOptions;
		private List<ApplicationModels.ApplicationOption> applicationOptions;
		private List<CompanyModels.CompanyOption> companyOptions;
		private List<KeyValueOption> enabledOptions;
		private List<KeyValueOption> editableOptions;

		public RoleFilterOptions() {
		}

		public List<ProductModels.ProductOption> getProductOptions() {
			return productOptions;
		}

		public void setProductOptions(List<ProductModels.ProductOption> productOptions) {
			this.productOptions = productOptions;
		}

		public RoleFilterOptions withProductOptions(List<ProductModels.ProductOption> productOptions) {
			setProductOptions(productOptions);

			return this;
		}

		public List<ApplicationModels.ApplicationOption> getApplicationOptions() {
			return applicationOptions;
		}

		public void setApplicationOptions(List<ApplicationModels.ApplicationOption> applicationOptions) {
			this.applicationOptions = applicationOptions;
		}

		public RoleFilterOptions withApplicationOptions(List<ApplicationModels.ApplicationOption> applicationOptions) {
			setApplicationOptions(applicationOptions);

			return this;
		}

		public List<CompanyModels.CompanyOption> getCompanyOptions() {
			return companyOptions;
		}

		public void setCompanyOptions(List<CompanyModels.CompanyOption> companyOptions) {
			this.companyOptions = companyOptions;
		}

		public RoleFilterOptions withCompanyOptions(List<CompanyModels.CompanyOption> companyOptions) {
			setCompanyOptions(companyOptions);

			return this;
		}

		public List<KeyValueOption> getEnabledOptions() {
			return enabledOptions;
		}

		public void setEnabledOptions(List<KeyValueOption> enabledOptions) {
			this.enabledOptions = enabledOptions;
		}

		public RoleFilterOptions withEnabledOptions(List<KeyValueOption> enabledOptions) {
			setEnabledOptions(enabledOptions);

			return this;
		}

		public List<KeyValueOption> getEditableOptions() {
			return editableOptions;
		}

		public void setEditableOptions(List<KeyValueOption> editableOptions) {
			this.editableOptions = editableOptions;
		}

		public RoleFilterOptions withEditableOptions(List<KeyValueOption> editableOptions) {
			setEditableOptions(editableOptions);

			return this;
		}
	}

	public static class RoleList extends CoreDocumentModel {
		private static final long serialVersionUID = -6360368408699343226L;

		private String name;
		private String description;
		private String productName;
		private String applicationName;
		private String companyName;
		private boolean enabled;
		private boolean editable;
		private boolean hidden;

		public RoleList(Role role, Application application) {
			super(role.getId(), role.getHid());
			this.name = role.getName();
			this.description = role.getDescription();
			this.productName = role.getRefProduct().getName();
			this.applicationName = application != null ? application.getName()
			        : "UNKNOWN (" + role.getApplicationId() + ")";
			this.companyName = application != null
			        ? application.getRefCompany() != null ? application.getRefCompany().getName()
			                : ("UNKNOWN COMPANY (" + application.getCompanyId() + ")")
			        : "UNKNOWN APPLICATION (" + role.getApplicationId() + ")";
			this.enabled = role.isEnabled();
			this.editable = role.isEditable();
			this.hidden = role.isHidden();
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public String getProductName() {
			return productName;
		}

		public String getApplicationName() {
			return applicationName;
		}

		public String getCompanyName() {
			return companyName;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public boolean isEditable() {
			return editable;
		}

		public boolean isHidden() {
			return hidden;
		}
	}

	public static class RoleModel extends CoreDocumentModel {
		private static final long serialVersionUID = -8801219470360400586L;

		private String name;
		private String description;
		private String productId;
		private String applicationId;
		private boolean enabled;
		private boolean editable;
		private boolean hidden;
		private List<String> privilegeIds;

		public RoleModel() {
			super(null, null);
		}

		public RoleModel(Role role) {
			super(role.getId(), role.getHid());
			this.name = role.getName();
			this.description = role.getDescription();
			this.productId = role.getProductId();
			this.applicationId = role.getApplicationId();
			this.enabled = role.isEnabled();
			this.editable = role.isEditable();
			this.hidden = role.isHidden();
			this.privilegeIds = role.getPrivilegeIds();
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public String getProductId() {
			return productId;
		}

		public String getApplicationId() {
			return applicationId;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public boolean isEditable() {
			return editable;
		}

		public boolean isHidden() {
			return hidden;
		}

		public List<String> getPrivilegeIds() {
			return privilegeIds;
		}
	}

	public static class RoleUpsert implements Serializable {
		private static final long serialVersionUID = -8428536406471030540L;

		private RoleModel role;
		private List<CoreProductModels.ProductOption> productOptions;
		private List<ApplicationModels.ApplicationOption> applicationOptions;
		private List<CorePrivilegeModels.PrivilegeOption> privilegeOptions;

		private RoleUpsert() {
			this.productOptions = new ArrayList<>();
			this.applicationOptions = new ArrayList<>();
			this.privilegeOptions = new ArrayList<>();
		}

		public RoleUpsert(RoleModel role, List<CoreProductModels.ProductOption> productOptions,
		        List<ApplicationModels.ApplicationOption> applicationOptions,
		        List<CorePrivilegeModels.PrivilegeOption> privilegeOptions) {
			this();
			this.role = role;
			if (productOptions != null)
				this.productOptions = productOptions;
			if (applicationOptions != null)
				this.applicationOptions = applicationOptions;
			if (privilegeOptions != null)
				this.privilegeOptions = privilegeOptions;
		}

		public RoleModel getRole() {
			return role;
		}

		public List<CoreProductModels.ProductOption> getProductOptions() {
			return productOptions;
		}

		public List<ApplicationModels.ApplicationOption> getApplicationOptions() {
			return applicationOptions;
		}

		public List<CorePrivilegeModels.PrivilegeOption> getPrivilegeOptions() {
			return privilegeOptions;
		}
	}
}

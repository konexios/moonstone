package com.arrow.pegasus.web.model;

import java.io.Serializable;
import java.util.List;

import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;
import com.arrow.pegasus.webapi.data.CorePrivilegeModels;
import com.arrow.pegasus.webapi.data.KeyValueOption;

public class PrivilegeModels extends CorePrivilegeModels {

	public static class RolePrivilegeOption extends PrivilegeOption {
		private static final long serialVersionUID = -3551627318198703200L;

		private String description;

		public RolePrivilegeOption(Privilege privilege) {
			super(privilege);
			this.description = privilege.getDescription();
		}

		public String getDescription() {
			return description;
		}
	}

	public static class PrivilegeFilterOptions implements Serializable {
		private static final long serialVersionUID = 6132012107061591131L;

		private List<ProductModels.ProductOption> productOptions;
		private List<KeyValueOption> enabledOptions;

		public PrivilegeFilterOptions() {
		}

		public List<ProductModels.ProductOption> getProductOptions() {
			return productOptions;
		}

		public void setProductOptions(List<ProductModels.ProductOption> productOptions) {
			this.productOptions = productOptions;
		}

		public PrivilegeFilterOptions withProductOptions(List<ProductModels.ProductOption> productOptions) {
			setProductOptions(productOptions);

			return this;
		}

		public List<KeyValueOption> getEnabledOptions() {
			return enabledOptions;
		}

		public void setEnabledOptions(List<KeyValueOption> enabledOptions) {
			this.enabledOptions = enabledOptions;
		}

		public PrivilegeFilterOptions withEnabledOptions(List<KeyValueOption> enabledOptions) {
			setEnabledOptions(enabledOptions);

			return this;
		}
	}

	/**
	 * 
	 * The PrivilegeModelList class is used for rendering multiple privileges
	 * (Example: in a table).
	 */
	public static class PrivilegeList extends CoreDocumentModel {
		private static final long serialVersionUID = -7634853553984782185L;

		private String systemName;
		private String name;
		private String description;
		private boolean enabled;
		private boolean hidden;
		private String category;
		private String productName;

		public PrivilegeList(Privilege privilege, Product product) {
			super(privilege.getId(), privilege.getHid());
			this.systemName = privilege.getSystemName();
			this.name = privilege.getName();
			this.description = privilege.getDescription();
			this.enabled = privilege.isEnabled();
			this.hidden = privilege.isHidden();
			this.category = privilege.getCategory();
			this.productName = (product == null ? privilege.getProductId() : product.getName());
		}

		public String getSystemName() {
			return systemName;
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public boolean isHidden() {
			return hidden;
		}

		public String getCategory() {
			return category;
		}

		public String getProductName() {
			return productName;
		}
	}

	/**
	 * The PrivilegeModel class is used for CRUD operations
	 */
	public static class PrivilegeModel extends CoreDocumentModel {
		private static final long serialVersionUID = 4303456916815325992L;

		private String name;
		private String description;
		private String productId;
		private String systemName;
		private boolean enabled;
		private boolean hidden;
		private String category;

		public PrivilegeModel() {
			super(null, null);
		}

		public PrivilegeModel(Privilege privilege) {
			super(privilege.getId(), privilege.getHid());
			this.name = privilege.getName();
			this.description = privilege.getDescription();
			this.productId = privilege.getProductId();
			this.systemName = privilege.getSystemName();
			this.enabled = privilege.isEnabled();
			this.hidden = privilege.isHidden();
			this.category = privilege.getCategory();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getProductId() {
			return productId;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public String getSystemName() {
			return systemName;
		}

		public void setSystemName(String systemName) {
			this.systemName = systemName;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public boolean isHidden() {
			return hidden;
		}

		public void setHidden(boolean hidden) {
			this.hidden = hidden;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}
	}

	public static class PrivilegeUpsert implements Serializable {
		private static final long serialVersionUID = -8441743748284960927L;

		private PrivilegeModel privilege;
		private List<ProductModels.ProductOption> productOptions;

		public PrivilegeUpsert(PrivilegeModel privilege, List<ProductModels.ProductOption> productOptions) {
			this.privilege = privilege;
			this.productOptions = productOptions;
		}

		public PrivilegeModel getPrivilege() {
			return privilege;
		}

		public List<ProductModels.ProductOption> getProductOptions() {
			return productOptions;
		}
	}
}

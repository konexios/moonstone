package com.arrow.pegasus.repo.params;

import java.io.Serializable;
import java.util.Set;

public class RoleSearchParams extends DefinitionDocumentParamsAbstract implements Serializable {
	private static final long serialVersionUID = 1760971415108153083L;

	private Set<String> productIds;
	private Set<String> applicationIds;
	private Boolean editable;
	private Set<String> privilegeIds;
	private Boolean hidden;

	public Set<String> getProductIds() {
		return productIds;
	}

	public void setProductIds(Set<String> productIds) {
		this.productIds = productIds;
	}

	public RoleSearchParams addProductIds(String... productIds) {
		this.productIds = super.addValues(this.productIds, productIds);

		return this;
	}

	public RoleSearchParams withProductIds(String... productIds) {
		this.productIds = null;

		return addProductIds(productIds);
	}

	public Set<String> getApplicationIds() {
		return applicationIds;
	}

	public void setApplicationIds(Set<String> applicationIds) {
		this.applicationIds = applicationIds;
	}

	public RoleSearchParams addApplicationIds(String... applicationIds) {
		this.applicationIds = super.addValues(this.applicationIds, applicationIds);

		return this;
	}

	public RoleSearchParams withApplicationIds(String... applicationIds) {
		this.applicationIds = null;

		return addApplicationIds(applicationIds);
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public Set<String> getPrivilegeIds() {
		return privilegeIds;
	}

	public void setPrivilegeIds(Set<String> privilegeIds) {
		this.privilegeIds = privilegeIds;
	}

	public RoleSearchParams addPrivilegeIds(String... privilegeIds) {
		this.privilegeIds = super.addValues(this.privilegeIds, privilegeIds);

		return this;
	}

	public RoleSearchParams withPrivilegeIds(String... privilegeIds) {
		this.privilegeIds = null;

		return addPrivilegeIds(privilegeIds);
	}
}

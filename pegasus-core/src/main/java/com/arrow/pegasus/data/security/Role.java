package com.arrow.pegasus.data.security;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "role")
public class Role extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = 8623853847056315252L;

	private final static boolean DEFAULT_EDITABLE = true;

	@NotBlank
	private String productId;
	@NotBlank
	@Indexed
	private String applicationId;
	private boolean editable = DEFAULT_EDITABLE;
	private List<String> privilegeIds = new ArrayList<>();
	private boolean hidden = CoreConstant.DEFAULT_HIDDEN;

	@Transient
	@JsonIgnore
	private List<Privilege> refPrivileges = new ArrayList<>();
	@Transient
	@JsonIgnore
	private Product refProduct;
	@Transient
	@JsonIgnore
	private Application refApplication;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public List<String> getPrivilegeIds() {
		return privilegeIds;
	}

	public void setPrivilegeIds(List<String> privilegeIds) {
		this.privilegeIds = privilegeIds;
	}

	public Product getRefProduct() {
		return refProduct;
	}

	public void setRefProduct(Product refProduct) {
		this.refProduct = refProduct;
	}

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	public List<Privilege> getRefPrivileges() {
		return refPrivileges;
	}

	public void setRefPrivileges(List<Privilege> refPrivileges) {
		this.refPrivileges = refPrivileges;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.ROLE;
	}
}

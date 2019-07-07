package com.arrow.pegasus.data.security;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.arrow.pegasus.data.profile.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "privilege")
public class Privilege extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = 2652512851710851927L;

	@NotBlank
	private String productId;
	@NotBlank
	@Indexed(unique = true)
	private String systemName;
	private boolean hidden = CoreConstant.DEFAULT_HIDDEN;
	private String category;

	@Transient
	@JsonIgnore
	private Product refProduct;

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

	public Product getRefProduct() {
		return refProduct;
	}

	public void setRefProduct(Product refProduct) {
		this.refProduct = refProduct;
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

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.PRIVILEGE;
	}
}

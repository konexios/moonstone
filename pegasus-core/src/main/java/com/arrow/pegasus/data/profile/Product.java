package com.arrow.pegasus.data.profile;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "product")
public class Product extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = 3050194229817987710L;

	@NotBlank
	@Indexed(unique = true)
	private String systemName;
	private boolean apiSigningRequired = true;
	private String parentProductId;
	private boolean hidden = CoreConstant.DEFAULT_HIDDEN;
	private List<ConfigurationProperty> configurations = new ArrayList<>();
	private List<ProductFeature> features = new ArrayList<>();

	@Transient
	@JsonIgnore
	private Product refParentProduct;

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public boolean isApiSigningRequired() {
		return apiSigningRequired;
	}

	public void setApiSigningRequired(boolean apiSigningRequired) {
		this.apiSigningRequired = apiSigningRequired;
	}

	public String getParentProductId() {
		return parentProductId;
	}

	public void setParentProductId(String parentProductId) {
		this.parentProductId = parentProductId;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public List<ConfigurationProperty> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<ConfigurationProperty> configurations) {
		this.configurations = configurations;
	}

	public List<ProductFeature> getFeatures() {
		return features;
	}

	public void setFeatures(List<ProductFeature> features) {
		this.features = features;
	}

	public Product getRefParentProduct() {
		return refParentProduct;
	}

	public void setRefParentProduct(Product refParentProduct) {
		this.refParentProduct = refParentProduct;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.PRODUCT;
	}
}

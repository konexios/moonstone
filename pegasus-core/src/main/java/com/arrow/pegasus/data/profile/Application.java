package com.arrow.pegasus.data.profile;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.ApplicationEngine;
import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.arrow.pegasus.data.YesNoInherit;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "application")
public class Application extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = -692921244723877849L;

	@NotBlank
	private String zoneId;
	@NotBlank
	@Indexed
	private String companyId;
	@NotBlank
	private String productId;
	@NotBlank
	private String subscriptionId;
	private String vaultId;
	@NotNull
	private YesNoInherit apiSigningRequired = YesNoInherit.INHERIT;
	private String applicationEngineId;
	private String defaultSamlEntityId;
	@NotBlank
	@Indexed(unique = true)
	private String code;
	private List<ConfigurationProperty> configurations = new ArrayList<>();
	private List<String> productExtensionIds = new ArrayList<>();
	private List<String> productFeatures = new ArrayList<>();

	@Transient
	@JsonIgnore
	private Zone refZone;
	@Transient
	@JsonIgnore
	private Company refCompany;
	@Transient
	@JsonIgnore
	private Product refProduct;
	@Transient
	@JsonIgnore
	private Subscription refSubscription;
	@Transient
	@JsonIgnore
	private ApplicationEngine refApplicationEngine;
	@Transient
	@JsonIgnore
	private List<Product> refProductExtensions = new ArrayList<>();
	@Transient
	@JsonIgnore
	private List<ProductFeature> refProductFeatures = new ArrayList<>();

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getVaultId() {
		return vaultId;
	}

	public void setVaultId(String vaultId) {
		this.vaultId = vaultId;
	}

	public YesNoInherit getApiSigningRequired() {
		return apiSigningRequired;
	}

	public void setApiSigningRequired(YesNoInherit apiSigningRequired) {
		this.apiSigningRequired = apiSigningRequired;
	}

	public String getApplicationEngineId() {
		return applicationEngineId;
	}

	public void setApplicationEngineId(String applicationEngineId) {
		this.applicationEngineId = applicationEngineId;
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

	public List<ConfigurationProperty> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<ConfigurationProperty> configurations) {
		this.configurations = configurations;
	}

	public List<String> getProductExtensionIds() {
		return productExtensionIds;
	}

	public void setProductExtensionIds(List<String> productExtensionIds) {
		this.productExtensionIds = productExtensionIds;
	}

	public List<String> getProductFeatures() {
		return productFeatures;
	}

	public void setProductFeatures(List<String> productFeatures) {
		this.productFeatures = productFeatures;
	}

	public Zone getRefZone() {
		return refZone;
	}

	public void setRefZone(Zone refZone) {
		this.refZone = refZone;
	}

	public Company getRefCompany() {
		return refCompany;
	}

	public void setRefCompany(Company refCompany) {
		this.refCompany = refCompany;
	}

	public Product getRefProduct() {
		return refProduct;
	}

	public void setRefProduct(Product refProduct) {
		this.refProduct = refProduct;
	}

	public Subscription getRefSubscription() {
		return refSubscription;
	}

	public void setRefSubscription(Subscription refSubscription) {
		this.refSubscription = refSubscription;
	}

	public ApplicationEngine getRefApplicationEngine() {
		return refApplicationEngine;
	}

	public void setRefApplicationEngine(ApplicationEngine refApplicationEngine) {
		this.refApplicationEngine = refApplicationEngine;
	}

	public List<Product> getRefProductExtensions() {
		return refProductExtensions;
	}

	public void setRefProductExtensions(List<Product> refProductExtensions) {
		this.refProductExtensions = refProductExtensions;
	}

	public boolean checkApiSigningRequired() {
		if (apiSigningRequired == YesNoInherit.INHERIT) {
			Assert.notNull(refProduct, "refProduct is not populated");
			return refProduct.isApiSigningRequired();
		} else {
			return apiSigningRequired == YesNoInherit.YES;
		}
	}

	public List<ProductFeature> getRefProductFeatures() {
		return refProductFeatures;
	}

	public void setRefProductFeatures(List<ProductFeature> refProductFeatures) {
		this.refProductFeatures = refProductFeatures;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.APPLICATION;
	}
}

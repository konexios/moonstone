package com.arrow.pegasus.data;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.Zone;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = ApplicationEngine.COLLECTION_NAME)
@CompoundIndexes({ @CompoundIndex(name = "name", unique = true, background = true, def = "{'name': 1}") })
public class ApplicationEngine extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = 8245277594240385544L;
	public static final String COLLECTION_NAME = "application_engine";

	@NotBlank
	private String productId;

	private String zoneId;

	@Transient
	@JsonIgnore
	private Product refProduct;

	@Transient
	@JsonIgnore
	private Zone refZone;

	public Zone getRefZone() {
		return refZone;
	}

	public void setRefZone(Zone refZone) {
		this.refZone = refZone;
	}

	public Product getRefProduct() {
		return refProduct;
	}

	public void setRefProduct(Product refProduct) {
		this.refProduct = refProduct;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.APPLICATION_ENGINE;
	}
}

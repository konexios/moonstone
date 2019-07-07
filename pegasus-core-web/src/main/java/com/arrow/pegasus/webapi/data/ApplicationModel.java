package com.arrow.pegasus.webapi.data;

import java.util.ArrayList;
import java.util.List;

public class ApplicationModel extends DefinitionModel {
	private static final long serialVersionUID = 5338883561468955L;

	private String hid;
	private DefinitionModel product;
	private ZoneModel zone;
	private String logoUrl;
	private List<DefinitionModel> privileges;

	public ApplicationModel(String id, String name, String description, String hid, String logoUrl) {
		super(id, name, description);
		this.hid = hid;
		this.logoUrl = logoUrl;
		privileges = new ArrayList<>();
	}

	public String getHid() {
		return hid;
	}

	public DefinitionModel getProduct() {
		return product;
	}

	public void setProduct(DefinitionModel product) {
		this.product = product;
	}

	public ZoneModel getZone() {
		return zone;
	}

	public void setZone(ZoneModel zone) {
		this.zone = zone;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public List<DefinitionModel> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<DefinitionModel> privileges) {
		this.privileges = privileges;
	}
}

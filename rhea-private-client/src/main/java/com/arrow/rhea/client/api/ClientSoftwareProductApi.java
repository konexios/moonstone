package com.arrow.rhea.client.api;

import java.net.URI;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.springframework.stereotype.Component;

import com.arrow.acs.JsonUtils;
import com.arrow.rhea.client.model.SoftwareProductModel;
import com.arrow.rhea.client.search.SoftwareProductSearchCriteria;
import com.arrow.rhea.data.SoftwareProduct;
import com.fasterxml.jackson.core.type.TypeReference;

@Component("RheaClientSoftwareProductApi")
public class ClientSoftwareProductApi extends ClientApiAbstract {
	private static final String SOFTWARE_PRODUCTS_ROOT_URL = WEB_SERVICE_ROOT_URL + "/software-products";

	public List<SoftwareProduct> findAll() {
		try {
			URI uri = buildUri(SOFTWARE_PRODUCTS_ROOT_URL);
			List<SoftwareProduct> result = execute(new HttpGet(uri), new TypeReference<List<SoftwareProduct>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SoftwareProduct create(SoftwareProduct softwareProduct, String who) {
		try {
			URI uri = buildUri(SOFTWARE_PRODUCTS_ROOT_URL);
			SoftwareProductModel model = new SoftwareProductModel().withModel(softwareProduct).withWho(who);
			SoftwareProduct result = execute(new HttpPost(uri), JsonUtils.toJson(model), SoftwareProduct.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SoftwareProduct update(SoftwareProduct softwareProduct, String who) {
		try {
			URI uri = buildUri(SOFTWARE_PRODUCTS_ROOT_URL);
			SoftwareProductModel model = new SoftwareProductModel().withModel(softwareProduct).withWho(who);
			SoftwareProduct result = execute(new HttpPut(uri), JsonUtils.toJson(model), SoftwareProduct.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SoftwareProduct findById(String id) {
		try {
			URI uri = buildUri(SOFTWARE_PRODUCTS_ROOT_URL + "/ids/" + id);
			SoftwareProduct result = execute(new HttpGet(uri), SoftwareProduct.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public List<SoftwareProduct> findAll(String companyId, String[] softwareVendorIds, Boolean enabled) {
		return findAll(companyId, softwareVendorIds, enabled, null);
	}

	public List<SoftwareProduct> findAll(String companyId, String[] softwareVendorIds, Boolean enabled, String name) {
		try {
			SoftwareProductSearchCriteria criteria = new SoftwareProductSearchCriteria().withCompanyId(companyId)
			        .withSoftwareVendorIds(softwareVendorIds).withEnabled(enabled).withName(name);
			URI uri = buildUri(SOFTWARE_PRODUCTS_ROOT_URL, criteria);
			List<SoftwareProduct> result = execute(new HttpGet(uri), criteria,
			        new TypeReference<List<SoftwareProduct>>() {
			        });
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}

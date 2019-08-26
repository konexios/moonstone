package com.arrow.rhea.client.api;

import java.net.URI;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.springframework.stereotype.Component;

import com.arrow.rhea.client.model.DeviceProductModel;
import com.arrow.rhea.client.search.DeviceProductSearchCriteria;
import com.arrow.rhea.data.DeviceProduct;
import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acs.JsonUtils;

@Component("RheaClientDeviceProductApi")
public class ClientDeviceProductApi extends ClientApiAbstract {
	private static final String DEVICE_PRODUCTS_ROOT_URL = WEB_SERVICE_ROOT_URL + "/device-products";

	public List<DeviceProduct> findAll() {
		try {
			URI uri = buildUri(DEVICE_PRODUCTS_ROOT_URL);
			List<DeviceProduct> result = execute(new HttpGet(uri), new TypeReference<List<DeviceProduct>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceProduct create(DeviceProduct deviceProduct, String who) {
		try {
			URI uri = buildUri(DEVICE_PRODUCTS_ROOT_URL);
			DeviceProductModel model = new DeviceProductModel().withModel(deviceProduct).withWho(who);
			DeviceProduct result = execute(new HttpPost(uri), JsonUtils.toJson(model), DeviceProduct.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceProduct update(DeviceProduct deviceProduct, String who) {
		try {
			URI uri = buildUri(DEVICE_PRODUCTS_ROOT_URL);
			DeviceProductModel model = new DeviceProductModel().withModel(deviceProduct).withWho(who);
			DeviceProduct result = execute(new HttpPut(uri), JsonUtils.toJson(model), DeviceProduct.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceProduct findById(String id) {
		try {
			URI uri = buildUri(DEVICE_PRODUCTS_ROOT_URL + "/ids/" + id);
			DeviceProduct result = execute(new HttpGet(uri), DeviceProduct.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public List<DeviceProduct> findAllByDeviceManufacturerIdAndEnabled(String deviceManufacturerId, boolean enabled) {
		try {
			DeviceProductSearchCriteria criteria = new DeviceProductSearchCriteria()
			        .withDeviceManufacturerIds(deviceManufacturerId).withEnabled(enabled);
			URI uri = buildUri(DEVICE_PRODUCTS_ROOT_URL, criteria);
			List<DeviceProduct> result = execute(new HttpGet(uri), criteria, new TypeReference<List<DeviceProduct>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public List<DeviceProduct> findAllByCompanyIdAndEnabled(String companyId, boolean enabled) {
		try {
			DeviceProductSearchCriteria criteria = new DeviceProductSearchCriteria().withCompanyId(companyId)
			        .withEnabled(enabled);
			URI uri = buildUri(DEVICE_PRODUCTS_ROOT_URL, criteria);
			List<DeviceProduct> result = execute(new HttpGet(uri), criteria, new TypeReference<List<DeviceProduct>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public List<DeviceProduct> findAll(String companyId, String[] deviceManufacturerIds, String[] deviceCategories,
	        Boolean enabled) {
		try {
			DeviceProductSearchCriteria criteria = new DeviceProductSearchCriteria().withCompanyId(companyId)
			        .withDeviceManufacturerIds(deviceManufacturerIds).withDeviceCategories(deviceCategories)
			        .withEnabled(enabled);
			URI uri = buildUri(DEVICE_PRODUCTS_ROOT_URL, criteria);
			List<DeviceProduct> result = execute(new HttpGet(uri), criteria, new TypeReference<List<DeviceProduct>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}

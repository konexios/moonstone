package com.arrow.rhea.client.api;

import java.net.URI;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.springframework.stereotype.Component;

import com.arrow.rhea.client.model.DeviceCategoryModel;
import com.arrow.rhea.client.search.DeviceCategorySearchCriteria;
import com.arrow.rhea.data.DeviceCategory;
import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acs.JsonUtils;

@Component("RheaClientDeviceCategoryApi")
public class ClientDeviceCategoryApi extends ClientApiAbstract {
	private static final String DEVICE_CATEGORIES_ROOT_URL = WEB_SERVICE_ROOT_URL + "/device-categories";

	public List<DeviceCategory> findAll() {
		try {
			URI uri = buildUri(DEVICE_CATEGORIES_ROOT_URL);
			List<DeviceCategory> result = execute(new HttpGet(uri), new TypeReference<List<DeviceCategory>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceCategory findByName(String name) {
		try {
			URI uri = buildUri(DEVICE_CATEGORIES_ROOT_URL + "/names/" + name);
			DeviceCategory result = execute(new HttpGet(uri), DeviceCategory.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceCategory findById(String id) {
		try {
			URI uri = buildUri(DEVICE_CATEGORIES_ROOT_URL + "/ids/" + id);
			DeviceCategory result = execute(new HttpGet(uri), DeviceCategory.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceCategory create(DeviceCategory deviceCategory, String who) {
		try {
			DeviceCategoryModel model = new DeviceCategoryModel().withModel(deviceCategory).withWho(who);
			URI uri = buildUri(DEVICE_CATEGORIES_ROOT_URL);
			DeviceCategory result = execute(new HttpPost(uri), JsonUtils.toJson(model), DeviceCategory.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceCategory update(DeviceCategory deviceCategory, String who) {
		try {
			DeviceCategoryModel model = new DeviceCategoryModel().withModel(deviceCategory).withWho(who);
			URI uri = buildUri(DEVICE_CATEGORIES_ROOT_URL);
			DeviceCategory result = execute(new HttpPut(uri), JsonUtils.toJson(model), DeviceCategory.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public List<DeviceCategory> findAll(Boolean enabled) {
		try {
			DeviceCategorySearchCriteria criteria = new DeviceCategorySearchCriteria().withEnabled(enabled);
			URI uri = buildUri(DEVICE_CATEGORIES_ROOT_URL, criteria);
			List<DeviceCategory> result = execute(new HttpGet(uri), criteria,
			        new TypeReference<List<DeviceCategory>>() {
			        });
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}

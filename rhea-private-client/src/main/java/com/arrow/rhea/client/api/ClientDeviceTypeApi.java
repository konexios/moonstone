package com.arrow.rhea.client.api;

import java.net.URI;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.springframework.stereotype.Component;

import com.arrow.acs.JsonUtils;
import com.arrow.rhea.client.model.DeviceTypeModel;
import com.arrow.rhea.client.search.DeviceTypeSearchCriteria;
import com.arrow.rhea.data.DeviceType;
import com.fasterxml.jackson.core.type.TypeReference;

@Component("RheaClientDeviceTypeApi")
public class ClientDeviceTypeApi extends ClientApiAbstract {
	private static final String DEVICE_TYPES_ROOT_URL = WEB_SERVICE_ROOT_URL + "/device-types";

	public List<DeviceType> findAll() {
		try {
			URI uri = buildUri(DEVICE_TYPES_ROOT_URL);
			List<DeviceType> result = execute(new HttpGet(uri), new TypeReference<List<DeviceType>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceType create(DeviceType deviceType, String who) {
		try {
			URI uri = buildUri(DEVICE_TYPES_ROOT_URL);
			DeviceTypeModel model = new DeviceTypeModel().withModel(deviceType).withWho(who);
			DeviceType result = execute(new HttpPost(uri), JsonUtils.toJson(model), DeviceType.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceType update(DeviceType deviceType, String who) {
		try {
			URI uri = buildUri(DEVICE_TYPES_ROOT_URL);
			DeviceTypeModel model = new DeviceTypeModel().withModel(deviceType).withWho(who);
			DeviceType result = execute(new HttpPut(uri), JsonUtils.toJson(model), DeviceType.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceType findById(String id) {
		try {
			URI uri = buildUri(DEVICE_TYPES_ROOT_URL + "/ids/" + id);
			DeviceType result = execute(new HttpGet(uri), DeviceType.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public List<DeviceType> findAll(String companyId, String[] deviceProductIds, String[] names, Boolean enabled) {
		try {
			DeviceTypeSearchCriteria criteria = new DeviceTypeSearchCriteria().withCompanyId(companyId)
			        .withDeviceProductIds(deviceProductIds).withNames(names).withEnabled(enabled);
			URI uri = buildUri(DEVICE_TYPES_ROOT_URL, criteria);
			List<DeviceType> result = execute(new HttpGet(uri), criteria, new TypeReference<List<DeviceType>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}

package com.arrow.rhea.client.api;

import java.net.URI;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.springframework.stereotype.Component;

import com.arrow.acs.JsonUtils;
import com.arrow.rhea.client.model.DeviceManufacturerModel;
import com.arrow.rhea.data.DeviceManufacturer;
import com.fasterxml.jackson.core.type.TypeReference;

@Component("RheaClientDeviceManufacturerApi")
public class ClientDeviceManufacturerApi extends ClientApiAbstract {
	private static final String DEVICE_MANUFACTURERS_ROOT_URL = WEB_SERVICE_ROOT_URL + "/device-manufacturers";

	public List<DeviceManufacturer> findAll() {
		try {
			URI uri = buildUri(DEVICE_MANUFACTURERS_ROOT_URL);
			List<DeviceManufacturer> result = execute(new HttpGet(uri), new TypeReference<List<DeviceManufacturer>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceManufacturer create(DeviceManufacturer deviceManufacturer, String who) {
		try {
			URI uri = buildUri(DEVICE_MANUFACTURERS_ROOT_URL);
			DeviceManufacturerModel model = new DeviceManufacturerModel().withWho(who).withModel(deviceManufacturer);
			DeviceManufacturer result = execute(new HttpPost(uri), JsonUtils.toJson(model), DeviceManufacturer.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceManufacturer update(DeviceManufacturer deviceManufacturer, String who) {
		try {
			URI uri = buildUri(DEVICE_MANUFACTURERS_ROOT_URL);
			DeviceManufacturerModel model = new DeviceManufacturerModel().withWho(who).withModel(deviceManufacturer);
			DeviceManufacturer result = execute(new HttpPut(uri), JsonUtils.toJson(model), DeviceManufacturer.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceManufacturer findById(String id) {
		try {
			URI uri = buildUri(DEVICE_MANUFACTURERS_ROOT_URL + "/ids/" + id);
			DeviceManufacturer result = execute(new HttpGet(uri), DeviceManufacturer.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public List<DeviceManufacturer> findAllByEnabled(boolean enabled) {
		try {
			URI uri = buildUri(DEVICE_MANUFACTURERS_ROOT_URL + "/enabled/" + String.valueOf(enabled));
			List<DeviceManufacturer> result = execute(new HttpGet(uri), new TypeReference<List<DeviceManufacturer>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}

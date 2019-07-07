package com.arrow.rhea.client.api;

import java.net.URI;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;

import com.arrow.rhea.data.DeviceCategory;
import com.arrow.rhea.data.DeviceManufacturer;
import com.arrow.rhea.data.DeviceProduct;
import com.arrow.rhea.data.DeviceType;
import com.arrow.rhea.data.RTURequest;
import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.data.SoftwareRelease;
import com.arrow.rhea.data.SoftwareVendor;
import com.fasterxml.jackson.core.type.TypeReference;

@Component("RheaClientCacheApi")
public class ClientCacheApi extends ClientApiAbstract {
	private static final String CACHE_ROOT_URL = WEB_SERVICE_ROOT_URL + "/cache";

	public Map<String, Long> listAllCacheTimestamps() {
		try {
			return execute(new HttpGet(buildUri(CACHE_ROOT_URL + "/timestamps")),
			        new TypeReference<Map<String, Long>>() {
			        });
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceManufacturer findDeviceManufacturerById(String id) {
		String method = "findDeviceManufacturerById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/device-manufacturers/ids/" + id);
			DeviceManufacturer result = execute(new HttpGet(uri), DeviceManufacturer.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceManufacturer findDeviceManufacturerByHid(String hid) {
		String method = "findDeviceManufacturerByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/device-manufacturers/hids/" + hid);
			DeviceManufacturer result = execute(new HttpGet(uri), DeviceManufacturer.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceProduct findDeviceProductById(String id) {
		String method = "findDeviceProductById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/device-products/ids/" + id);
			DeviceProduct result = execute(new HttpGet(uri), DeviceProduct.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceProduct findDeviceProductByHid(String hid) {
		String method = "findDeviceProductByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/device-products/hids/" + hid);
			DeviceProduct result = execute(new HttpGet(uri), DeviceProduct.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceCategory findDeviceCategoryById(String id) {
		String method = "findDeviceCategoryById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/device-categories/ids/" + id);
			DeviceCategory result = execute(new HttpGet(uri), DeviceCategory.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceCategory findDeviceCategoryByHid(String hid) {
		String method = "findDeviceCategoryByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/device-categories/hids/" + hid);
			DeviceCategory result = execute(new HttpGet(uri), DeviceCategory.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceType findDeviceTypeById(String id) {
		String method = "findDeviceTypeById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/device-types/ids/" + id);
			DeviceType result = execute(new HttpGet(uri), DeviceType.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public DeviceType findDeviceTypeByHid(String hid) {
		String method = "findDeviceTypeByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/device-types/hids/" + hid);
			DeviceType result = execute(new HttpGet(uri), DeviceType.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SoftwareVendor findSoftwareVendorById(String id) {
		String method = "findSoftwareVendorById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/software-vendors/ids/" + id);
			SoftwareVendor result = execute(new HttpGet(uri), SoftwareVendor.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SoftwareVendor findSoftwareVendorByHid(String hid) {
		String method = "findSoftwareVendorByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/software-vendors/hids/" + hid);
			SoftwareVendor result = execute(new HttpGet(uri), SoftwareVendor.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SoftwareProduct findSoftwareProductById(String id) {
		String method = "findSoftwareProductById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/software-products/ids/" + id);
			SoftwareProduct result = execute(new HttpGet(uri), SoftwareProduct.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SoftwareProduct findSoftwareProductByHid(String hid) {
		String method = "findSoftwareProductByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/software-products/hids/" + hid);
			SoftwareProduct result = execute(new HttpGet(uri), SoftwareProduct.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SoftwareRelease findSoftwareReleaseById(String id) {
		String method = "findSoftwareReleaseById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/software-releases/ids/" + id);
			SoftwareRelease result = execute(new HttpGet(uri), SoftwareRelease.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(),
				        result.getMajor() + "." + result.getMinor());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SoftwareRelease findSoftwareReleaseByHid(String hid) {
		String method = "findSoftwareReleaseByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/software-releases/hids/" + hid);
			SoftwareRelease result = execute(new HttpGet(uri), SoftwareRelease.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(),
				        result.getMajor() + "." + result.getMinor());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
	
	public RTURequest findRTURequestById(String id) {
		String method = "findRTURequestById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/rtu-requests/ids/" + id);
			RTURequest result = execute(new HttpGet(uri), RTURequest.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s", result.getId(), result.getHid());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public RTURequest findRTURequestByHid(String hid) {
		String method = "findRTURequestByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/rtu-requests/hids/" + hid);
			RTURequest result = execute(new HttpGet(uri), RTURequest.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s", result.getId(), result.getHid());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
	
	public RTURequest findRTURequestByName(String companyId, String softwareReleaseId) {
		String method = "findRTURequestByName";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/rtu-requests/names/" + companyId + "/" + softwareReleaseId);
			RTURequest result = execute(new HttpGet(uri), RTURequest.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s", result.getId(), result.getHid());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}

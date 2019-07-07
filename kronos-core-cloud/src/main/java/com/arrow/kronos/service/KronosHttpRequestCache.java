package com.arrow.kronos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.Node;
import com.arrow.pegasus.CoreHttpRequestCache;
import com.arrow.rhea.client.api.ClientCacheApi;
import com.arrow.rhea.data.DeviceManufacturer;
import com.arrow.rhea.data.DeviceProduct;
import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.data.SoftwareRelease;
import com.arrow.rhea.data.SoftwareVendor;

@Service
public class KronosHttpRequestCache extends CoreHttpRequestCache {
	@Autowired
	private KronosCache kronosCache;

	@Autowired
	private ClientCacheApi rheaClientCache;

	public Node findNodeById(String id) {
		return doCache(id, s -> String.format("%s:%s", Node.class.getName(), s), s -> kronosCache.findNodeById(s));
	}

	public Device findDeviceById(String id) {
		return doCache(id, s -> String.format("%s:%s", Device.class.getName(), s), s -> kronosCache.findDeviceById(s));
	}

	public Gateway findGatewayById(String id) {
		return doCache(id, s -> String.format("%s:%s", Gateway.class.getName(), s),
		        s -> kronosCache.findGatewayById(s));
	}

	public DeviceType findDeviceTypeById(String id) {
		return doCache(id, s -> String.format("%s:%s", DeviceType.class.getName(), s),
		        s -> kronosCache.findDeviceTypeById(s));
	}

	public SoftwareProduct findSoftwareProductById(String id) {
		return doCache(id, s -> String.format("%s:%s", SoftwareProduct.class.getName(), s),
		        s -> rheaClientCache.findSoftwareProductById(s));
	}

	public SoftwareRelease findSoftwareReleaseById(String id) {
		return doCache(id, s -> String.format("%s:%s", SoftwareRelease.class.getName(), s),
		        s -> rheaClientCache.findSoftwareReleaseById(s));
	}

	public SoftwareVendor findSoftwareVendorById(String id) {
		return doCache(id, s -> String.format("%s:%s", SoftwareVendor.class.getName(), s),
		        s -> rheaClientCache.findSoftwareVendorById(s));
	}

	public DeviceProduct findDeviceProductById(String id) {
		return doCache(id, s -> String.format("%s:%s", DeviceProduct.class.getName(), s),
		        s -> rheaClientCache.findDeviceProductById(s));
	}

	public DeviceManufacturer findDeviceManufacturerById(String id) {
		return doCache(id, s -> String.format("%s:%s", DeviceManufacturer.class.getName(), s),
		        s -> rheaClientCache.findDeviceManufacturerById(s));
	}

	public com.arrow.rhea.data.DeviceType findRheaDeviceTypeById(String id) {
		return doCache(id, s -> String.format("%s:%s", com.arrow.rhea.data.DeviceType.class.getName(), s),
		        s -> rheaClientCache.findDeviceTypeById(s));
	}

	public KronosCache getKronosCache() {
		return kronosCache;
	}

	public ClientCacheApi getRheaClientCache() {
		return rheaClientCache;
	}
}

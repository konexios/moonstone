package com.arrow.rhea.client.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.arrow.rhea.client.api.ClientCacheApi;
import com.arrow.rhea.client.api.ClientDeviceCategoryApi;
import com.arrow.rhea.client.api.ClientDeviceManufacturerApi;
import com.arrow.rhea.client.api.ClientDeviceProductApi;
import com.arrow.rhea.client.api.ClientDeviceTypeApi;
import com.arrow.rhea.client.api.ClientRTURequestApi;
import com.arrow.rhea.client.api.ClientSoftwareProductApi;
import com.arrow.rhea.client.api.ClientSoftwareReleaseApi;
import com.arrow.rhea.client.api.ClientSoftwareVendorApi;
import com.arrow.rhea.data.DeviceCategory;
import com.arrow.rhea.data.DeviceManufacturer;
import com.arrow.rhea.data.DeviceProduct;
import com.arrow.rhea.data.DeviceType;
import com.arrow.rhea.data.RTURequest;
import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.data.SoftwareRelease;
import com.arrow.rhea.data.SoftwareVendor;
import com.arrow.rhea.service.RheaCacheProxyAbstract;

public class RemoteRheaCacheProxy extends RheaCacheProxyAbstract {

	@Autowired
	private ClientCacheApi clientCacheApi;
	@Autowired
	private ClientDeviceManufacturerApi clientDeviceManufacturerApi;
	@Autowired
	private ClientDeviceProductApi clientDeviceProductApi;
	@Autowired
	private ClientDeviceCategoryApi clientDeviceCategoryApi;
	@Autowired
	private ClientDeviceTypeApi clientDeviceTypeApi;
	@Autowired
	private ClientSoftwareVendorApi clientSoftwareVendorApi;
	@Autowired
	private ClientSoftwareProductApi clientSoftwareProductApi;
	@Autowired
	private ClientSoftwareReleaseApi clientSoftwareReleaseApi;
	@Autowired
	private ClientRTURequestApi clientRTURequestApi;

	@Override
	public List<DeviceManufacturer> preloadDeviceManufacturers() {
		return clientDeviceManufacturerApi.findAll();
	}

	@Override
	public List<DeviceProduct> preloadDeviceProducts() {
		return clientDeviceProductApi.findAll();
	}

	@Override
	public List<DeviceCategory> preloadDeviceCategories() {
		return clientDeviceCategoryApi.findAll();
	}

	@Override
	public List<DeviceType> preloadDeviceTypes() {
		return clientDeviceTypeApi.findAll();
	}

	@Override
	public List<SoftwareVendor> preloadSoftwareVendors() {
		return clientSoftwareVendorApi.findAll();
	}

	@Override
	public List<SoftwareProduct> preloadSoftwareProducts() {
		return clientSoftwareProductApi.findAll();
	}

	@Override
	public List<SoftwareRelease> preloadSoftwareReleases() {
		return clientSoftwareReleaseApi.findAll();
	}
	
	@Override
	public List<RTURequest> preloadRTURequests() {
		return clientRTURequestApi.findAll();
	}

	@Override
	public DeviceManufacturer findDeviceManufacturerById(String id) {
		return clientCacheApi.findDeviceManufacturerById(id);
	}

	@Override
	public DeviceManufacturer findDeviceManufacturerByHid(String hid) {
		return clientCacheApi.findDeviceManufacturerByHid(hid);
	}

	@Override
	public DeviceProduct findDeviceProductById(String id) {
		return clientCacheApi.findDeviceProductById(id);
	}

	@Override
	public DeviceProduct findDeviceProductByHid(String hid) {
		return clientCacheApi.findDeviceProductByHid(hid);
	}

	@Override
	public DeviceCategory findDeviceCategoryById(String id) {
		return clientCacheApi.findDeviceCategoryById(id);
	}

	@Override
	public DeviceCategory findDeviceCategoryByHid(String hid) {
		return clientCacheApi.findDeviceCategoryByHid(hid);
	}

	@Override
	public DeviceType findDeviceTypeById(String id) {
		return clientCacheApi.findDeviceTypeById(id);
	}

	@Override
	public DeviceType findDeviceTypeByHid(String hid) {
		return clientCacheApi.findDeviceTypeByHid(hid);
	}

	@Override
	public SoftwareVendor findSoftwareVendorById(String id) {
		return clientCacheApi.findSoftwareVendorById(id);
	}

	@Override
	public SoftwareVendor findSoftwareVendorByHid(String hid) {
		return clientCacheApi.findSoftwareVendorByHid(hid);
	}

	@Override
	public SoftwareProduct findSoftwareProductById(String id) {
		return clientCacheApi.findSoftwareProductById(id);
	}

	@Override
	public SoftwareProduct findSoftwareProductByHid(String hid) {
		return clientCacheApi.findSoftwareProductByHid(hid);
	}

	@Override
	public SoftwareRelease findSoftwareReleaseById(String id) {
		return clientCacheApi.findSoftwareReleaseById(id);
	}

	@Override
	public SoftwareRelease findSoftwareReleaseByHid(String hid) {
		return clientCacheApi.findSoftwareReleaseByHid(hid);
	}

	@Override
	public RTURequest findRTURequestById(String id) {
		return clientCacheApi.findRTURequestById(id);
	}

	@Override
	public RTURequest findRTURequestByHid(String hid) {
		return clientCacheApi.findRTURequestByHid(hid);
	}
	
	@Override
	public RTURequest findRTURequestByName(String companyId, String softwareReleaseId) {
		return clientCacheApi.findRTURequestByName(companyId, softwareReleaseId);
	}
}
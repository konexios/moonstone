package com.arrow.rhea.service;

import java.util.List;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;

import com.arrow.rhea.data.DeviceCategory;
import com.arrow.rhea.data.DeviceManufacturer;
import com.arrow.rhea.data.DeviceProduct;
import com.arrow.rhea.data.DeviceType;
import com.arrow.rhea.data.RTURequest;
import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.data.SoftwareRelease;
import com.arrow.rhea.data.SoftwareVendor;

public interface RheaCacheProxy {

	List<DeviceManufacturer> preloadDeviceManufacturers();

	List<DeviceProduct> preloadDeviceProducts();

	List<DeviceCategory> preloadDeviceCategories();

	List<DeviceType> preloadDeviceTypes();

	List<SoftwareVendor> preloadSoftwareVendors();

	List<SoftwareProduct> preloadSoftwareProducts();

	List<SoftwareRelease> preloadSoftwareReleases();
	
	List<RTURequest> preloadRTURequests();

	@Caching(put = {
	        @CachePut(value = RheaCacheService.RHEA_DEVICE_MANUFACTURER_HIDS, key = "#result.hid", condition = "#result != null") })
	DeviceManufacturer findDeviceManufacturerById(String id);

	@Caching(put = {
	        @CachePut(value = RheaCacheService.RHEA_DEVICE_MANUFACTURER_IDS, key = "#result.id", condition = "#result != null") })
	DeviceManufacturer findDeviceManufacturerByHid(String hid);

	@Caching(put = {
	        @CachePut(value = RheaCacheService.RHEA_DEVICE_PRODUCT_HIDS, key = "#result.hid", condition = "#result != null") })
	DeviceProduct findDeviceProductById(String id);

	@Caching(put = {
	        @CachePut(value = RheaCacheService.RHEA_DEVICE_PRODUCT_IDS, key = "#result.id", condition = "#result != null") })
	DeviceProduct findDeviceProductByHid(String hid);

	@Caching(put = {
	        @CachePut(value = RheaCacheService.RHEA_DEVICE_CATEGORY_HIDS, key = "#result.hid", condition = "#result != null") })
	DeviceCategory findDeviceCategoryById(String id);

	@Caching(put = {
	        @CachePut(value = RheaCacheService.RHEA_DEVICE_CATEGORY_IDS, key = "#result.id", condition = "#result != null") })
	DeviceCategory findDeviceCategoryByHid(String hid);

	@Caching(put = {
	        @CachePut(value = RheaCacheService.RHEA_DEVICE_TYPE_HIDS, key = "#result.hid", condition = "#result != null") })
	DeviceType findDeviceTypeById(String id);

	@Caching(put = {
	        @CachePut(value = RheaCacheService.RHEA_DEVICE_TYPE_IDS, key = "#result.id", condition = "#result != null") })
	DeviceType findDeviceTypeByHid(String hid);

	@Caching(put = {
	        @CachePut(value = RheaCacheService.RHEA_SOFTWARE_VENDOR_HIDS, key = "#result.hid", condition = "#result != null") })
	SoftwareVendor findSoftwareVendorById(String id);

	@Caching(put = {
	        @CachePut(value = RheaCacheService.RHEA_SOFTWARE_VENDOR_IDS, key = "#result.id", condition = "#result != null") })
	SoftwareVendor findSoftwareVendorByHid(String hid);

	@Caching(put = {
	        @CachePut(value = RheaCacheService.RHEA_SOFTWARE_PRODUCT_HIDS, key = "#result.hid", condition = "#result != null") })
	SoftwareProduct findSoftwareProductById(String id);

	@Caching(put = {
	        @CachePut(value = RheaCacheService.RHEA_SOFTWARE_PRODUCT_IDS, key = "#result.id", condition = "#result != null") })
	SoftwareProduct findSoftwareProductByHid(String hid);

	@Caching(put = {
	        @CachePut(value = RheaCacheService.RHEA_SOFTWARE_RELEASE_HIDS, key = "#result.hid", condition = "#result != null") })
	SoftwareRelease findSoftwareReleaseById(String id);

	@Caching(put = {
	        @CachePut(value = RheaCacheService.RHEA_SOFTWARE_RELEASE_IDS, key = "#result.id", condition = "#result != null") })
	SoftwareRelease findSoftwareReleaseByHid(String hid);
	
	@Caching(put = { @CachePut(value = RheaCacheService.RHEA_RTU_REQUEST_NAMES, key = "#result.companyId.concat('-').concat(#result.softwareReleaseId)", condition = "#result != null"),
	        @CachePut(value = RheaCacheService.RHEA_RTU_REQUEST_HIDS, key = "#result.hid", condition = "#result != null") })
	RTURequest findRTURequestById(String id);

	@Caching(put = { @CachePut(value = RheaCacheService.RHEA_RTU_REQUEST_NAMES, key = "#result.companyId.concat('-').concat(#result.softwareReleaseId)", condition = "#result != null"),
	        @CachePut(value = RheaCacheService.RHEA_RTU_REQUEST_IDS, key = "#result.id", condition = "#result != null") })
	RTURequest findRTURequestByHid(String hid);
	
	@Caching(put = { @CachePut(value = RheaCacheService.RHEA_RTU_REQUEST_IDS, key = "#result.id", condition = "#result != null"),
			@CachePut(value = RheaCacheService.RHEA_RTU_REQUEST_HIDS, key = "#result.hid", condition = "#result != null") })
	RTURequest findRTURequestByName(String companyId, String softwareReleaseId);
	
}
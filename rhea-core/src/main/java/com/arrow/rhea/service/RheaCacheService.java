package com.arrow.rhea.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.arrow.pegasus.service.CacheServiceAbstract;
import com.arrow.rhea.data.DeviceCategory;
import com.arrow.rhea.data.DeviceManufacturer;
import com.arrow.rhea.data.DeviceProduct;
import com.arrow.rhea.data.DeviceType;
import com.arrow.rhea.data.RTURequest;
import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.data.SoftwareRelease;
import com.arrow.rhea.data.SoftwareVendor;

@Service
public class RheaCacheService extends CacheServiceAbstract {

    public static final String RHEA_DEVICE_MANUFACTURER_IDS = "rhea_device_manufacturer_ids";
    public static final String RHEA_DEVICE_MANUFACTURER_HIDS = "rhea_device_manufacturer_hids";

    public static final String RHEA_DEVICE_PRODUCT_IDS = "rhea_device_product_ids";
    public static final String RHEA_DEVICE_PRODUCT_HIDS = "rhea_device_product_hids";

    public static final String RHEA_DEVICE_CATEGORY_IDS = "rhea_device_category_ids";
    public static final String RHEA_DEVICE_CATEGORY_HIDS = "rhea_device_category_hids";

    public static final String RHEA_DEVICE_TYPE_IDS = "rhea_device_type_ids";
    public static final String RHEA_DEVICE_TYPE_HIDS = "rhea_device_type_hids";

    public static final String RHEA_SOFTWARE_VENDOR_IDS = "rhea_software_vendor_ids";
    public static final String RHEA_SOFTWARE_VENDOR_HIDS = "rhea_software_vendor_hids";

    public static final String RHEA_SOFTWARE_PRODUCT_IDS = "rhea_software_product_ids";
    public static final String RHEA_SOFTWARE_PRODUCT_HIDS = "rhea_software_product_hids";

    public static final String RHEA_SOFTWARE_RELEASE_IDS = "rhea_software_release_ids";
    public static final String RHEA_SOFTWARE_RELEASE_HIDS = "rhea_software_release_hids";

    public static final String RHEA_RTU_REQUEST_IDS = "rhea_rtu_request_ids";
    public static final String RHEA_RTU_REQUEST_HIDS = "rhea_rtu_request_hids";
    public static final String RHEA_RTU_REQUEST_NAMES = "rhea_rtu_request_names";

    @Autowired
    private RheaCacheProxy proxy;

    @Cacheable(RHEA_DEVICE_MANUFACTURER_IDS)
    public DeviceManufacturer findDeviceManufacturerById(String id) {
        return proxy.findDeviceManufacturerById(id);
    }

    @Cacheable(RHEA_DEVICE_MANUFACTURER_HIDS)
    public DeviceManufacturer findDeviceManufacturerByHid(String hid) {
        return proxy.findDeviceManufacturerByHid(hid);
    }

    @Caching(evict = {
            @CacheEvict(value = RHEA_DEVICE_MANUFACTURER_IDS, key = "#deviceManufacturer.id", condition = "#deviceManufacturer != null"),
            @CacheEvict(value = RHEA_DEVICE_MANUFACTURER_HIDS, key = "#deviceManufacturer.hid", condition = "#deviceManufacturer != null") })
    public void clearDeviceManufacturer(DeviceManufacturer deviceManufacturer) {
        String method = "clearDeviceManufacturer";
        logInfo(method, "id: %s", deviceManufacturer.getId());
        notifyCacheUpdate(RHEA_DEVICE_MANUFACTURER_IDS, deviceManufacturer.getId());
        notifyCacheUpdate(RHEA_DEVICE_MANUFACTURER_HIDS, deviceManufacturer.getHid());
    }

    @CacheEvict(cacheNames = { RHEA_DEVICE_MANUFACTURER_IDS, RHEA_DEVICE_MANUFACTURER_HIDS }, allEntries = true)
    public void clearDeviceManufacturers() {
        String method = "clearDeviceManufacturers";
        logInfo(method, "...");
        notifyCacheUpdate(RHEA_DEVICE_MANUFACTURER_IDS, ALL_KEYS);
        notifyCacheUpdate(RHEA_DEVICE_MANUFACTURER_HIDS, ALL_KEYS);
    }

    @Cacheable(RHEA_DEVICE_PRODUCT_IDS)
    public DeviceProduct findDeviceProductById(String id) {
        return proxy.findDeviceProductById(id);
    }

    @Cacheable(RHEA_DEVICE_PRODUCT_HIDS)
    public DeviceProduct findDeviceProductByHid(String hid) {
        return proxy.findDeviceProductByHid(hid);
    }

    @Caching(evict = {
            @CacheEvict(value = RHEA_DEVICE_PRODUCT_IDS, key = "#deviceProduct.id", condition = "#deviceProduct != null"),
            @CacheEvict(value = RHEA_DEVICE_PRODUCT_HIDS, key = "#deviceProduct.hid", condition = "#deviceProduct != null") })
    public void clearDeviceProduct(DeviceProduct deviceProduct) {
        String method = "clearDeviceProduct";
        logInfo(method, "id: %s", deviceProduct.getId());
        notifyCacheUpdate(RHEA_DEVICE_PRODUCT_IDS, deviceProduct.getId());
        notifyCacheUpdate(RHEA_DEVICE_PRODUCT_HIDS, deviceProduct.getHid());
    }

    @CacheEvict(cacheNames = { RHEA_DEVICE_PRODUCT_IDS, RHEA_DEVICE_PRODUCT_HIDS }, allEntries = true)
    public void clearDeviceProducts() {
        String method = "clearDeviceProducts";
        logInfo(method, "...");
        notifyCacheUpdate(RHEA_DEVICE_PRODUCT_IDS, ALL_KEYS);
        notifyCacheUpdate(RHEA_DEVICE_PRODUCT_HIDS, ALL_KEYS);
    }

    @Cacheable(RHEA_DEVICE_CATEGORY_IDS)
    public DeviceCategory findDeviceCategoryById(String id) {
        return proxy.findDeviceCategoryById(id);
    }

    @Cacheable(RHEA_DEVICE_CATEGORY_HIDS)
    public DeviceCategory findDeviceCategoryByHid(String hid) {
        return proxy.findDeviceCategoryByHid(hid);
    }

    @Caching(evict = {
            @CacheEvict(value = RHEA_DEVICE_CATEGORY_IDS, key = "#deviceCategory.id", condition = "#deviceCategory != null"),
            @CacheEvict(value = RHEA_DEVICE_CATEGORY_HIDS, key = "#deviceCategory.hid", condition = "#deviceCategory != null") })
    public void clearDeviceCategory(DeviceCategory deviceCategory) {
        String method = "clearDeviceCategory";
        logInfo(method, "id: %s", deviceCategory.getId());
        notifyCacheUpdate(RHEA_DEVICE_CATEGORY_IDS, deviceCategory.getId());
        notifyCacheUpdate(RHEA_DEVICE_CATEGORY_HIDS, deviceCategory.getHid());
    }

    @CacheEvict(cacheNames = { RHEA_DEVICE_CATEGORY_IDS, RHEA_DEVICE_CATEGORY_HIDS }, allEntries = true)
    public void clearDeviceCategorys() {
        String method = "clearDeviceCategorys";
        logInfo(method, "...");
        notifyCacheUpdate(RHEA_DEVICE_CATEGORY_IDS, ALL_KEYS);
        notifyCacheUpdate(RHEA_DEVICE_CATEGORY_HIDS, ALL_KEYS);
    }

    @Cacheable(RHEA_DEVICE_TYPE_IDS)
    public DeviceType findDeviceTypeById(String id) {
        return proxy.findDeviceTypeById(id);
    }

    @Cacheable(RHEA_DEVICE_TYPE_HIDS)
    public DeviceType findDeviceTypeByHid(String hid) {
        return proxy.findDeviceTypeByHid(hid);
    }

    @Caching(evict = {
            @CacheEvict(value = RHEA_DEVICE_TYPE_IDS, key = "#deviceType.id", condition = "#deviceType != null"),
            @CacheEvict(value = RHEA_DEVICE_TYPE_HIDS, key = "#deviceType.hid", condition = "#deviceType != null") })
    public void clearDeviceType(DeviceType deviceType) {
        String method = "clearDeviceType";
        logInfo(method, "id: %s", deviceType.getId());
        notifyCacheUpdate(RHEA_DEVICE_TYPE_IDS, deviceType.getId());
        notifyCacheUpdate(RHEA_DEVICE_TYPE_HIDS, deviceType.getHid());
    }

    @CacheEvict(cacheNames = { RHEA_DEVICE_TYPE_IDS, RHEA_DEVICE_TYPE_HIDS }, allEntries = true)
    public void clearDeviceTypes() {
        String method = "clearDeviceTypes";
        logInfo(method, "...");
        notifyCacheUpdate(RHEA_DEVICE_TYPE_IDS, ALL_KEYS);
        notifyCacheUpdate(RHEA_DEVICE_TYPE_HIDS, ALL_KEYS);
    }

    @Cacheable(RHEA_SOFTWARE_VENDOR_IDS)
    public SoftwareVendor findSoftwareVendorById(String id) {
        return proxy.findSoftwareVendorById(id);
    }

    @Cacheable(RHEA_SOFTWARE_VENDOR_HIDS)
    public SoftwareVendor findSoftwareVendorByHid(String hid) {
        return proxy.findSoftwareVendorByHid(hid);
    }

    @Caching(evict = {
            @CacheEvict(value = RHEA_SOFTWARE_VENDOR_IDS, key = "#softwareVendor.id", condition = "#softwareVendor != null"),
            @CacheEvict(value = RHEA_SOFTWARE_VENDOR_HIDS, key = "#softwareVendor.hid", condition = "#softwareVendor != null") })
    public void clearSoftwareVendor(SoftwareVendor softwareVendor) {
        String method = "clearSoftwareVendor";
        logInfo(method, "id: %s", softwareVendor.getId());
        notifyCacheUpdate(RHEA_SOFTWARE_VENDOR_IDS, softwareVendor.getId());
        notifyCacheUpdate(RHEA_SOFTWARE_VENDOR_HIDS, softwareVendor.getHid());
    }

    @CacheEvict(cacheNames = { RHEA_SOFTWARE_VENDOR_IDS, RHEA_SOFTWARE_VENDOR_HIDS }, allEntries = true)
    public void clearSoftwareVendors() {
        String method = "clearSoftwareVendors";
        logInfo(method, "...");
        notifyCacheUpdate(RHEA_SOFTWARE_VENDOR_IDS, ALL_KEYS);
        notifyCacheUpdate(RHEA_SOFTWARE_VENDOR_HIDS, ALL_KEYS);
    }

    @Cacheable(RHEA_SOFTWARE_PRODUCT_IDS)
    public SoftwareProduct findSoftwareProductById(String id) {
        return proxy.findSoftwareProductById(id);
    }

    @Cacheable(RHEA_SOFTWARE_PRODUCT_HIDS)
    public SoftwareProduct findSoftwareProductByHid(String hid) {
        return proxy.findSoftwareProductByHid(hid);
    }

    @Caching(evict = {
            @CacheEvict(value = RHEA_SOFTWARE_PRODUCT_IDS, key = "#softwareProduct.id", condition = "#softwareProduct != null"),
            @CacheEvict(value = RHEA_SOFTWARE_PRODUCT_HIDS, key = "#softwareProduct.hid", condition = "#softwareProduct != null") })
    public void clearSoftwareProduct(SoftwareProduct softwareProduct) {
        String method = "clearSoftwareProduct";
        logInfo(method, "id: %s", softwareProduct.getId());
        notifyCacheUpdate(RHEA_SOFTWARE_PRODUCT_IDS, softwareProduct.getId());
        notifyCacheUpdate(RHEA_SOFTWARE_PRODUCT_HIDS, softwareProduct.getHid());
    }

    @CacheEvict(cacheNames = { RHEA_SOFTWARE_PRODUCT_IDS, RHEA_SOFTWARE_PRODUCT_HIDS }, allEntries = true)
    public void clearSoftwareProducts() {
        String method = "clearSoftwareProducts";
        logInfo(method, "...");
        notifyCacheUpdate(RHEA_SOFTWARE_PRODUCT_IDS, ALL_KEYS);
        notifyCacheUpdate(RHEA_SOFTWARE_PRODUCT_HIDS, ALL_KEYS);
    }

    @Cacheable(RHEA_SOFTWARE_RELEASE_IDS)
    public SoftwareRelease findSoftwareReleaseById(String id) {
        return proxy.findSoftwareReleaseById(id);
    }

    @Cacheable(RHEA_SOFTWARE_RELEASE_HIDS)
    public SoftwareRelease findSoftwareReleaseByHid(String hid) {
        return proxy.findSoftwareReleaseByHid(hid);
    }

    @Caching(evict = {
            @CacheEvict(value = RHEA_SOFTWARE_RELEASE_IDS, key = "#softwareRelease.id", condition = "#softwareRelease != null"),
            @CacheEvict(value = RHEA_SOFTWARE_RELEASE_HIDS, key = "#softwareRelease.hid", condition = "#softwareRelease != null") })
    public void clearSoftwareRelease(SoftwareRelease softwareRelease) {
        String method = "clearSoftwareRelease";
        logInfo(method, "id: %s", softwareRelease.getId());
        notifyCacheUpdate(RHEA_SOFTWARE_RELEASE_IDS, softwareRelease.getId());
        notifyCacheUpdate(RHEA_SOFTWARE_RELEASE_HIDS, softwareRelease.getHid());
    }

    @CacheEvict(cacheNames = { RHEA_SOFTWARE_RELEASE_IDS, RHEA_SOFTWARE_RELEASE_HIDS }, allEntries = true)
    public void clearSoftwareReleases() {
        String method = "clearSoftwareReleases";
        logInfo(method, "...");
        notifyCacheUpdate(RHEA_SOFTWARE_RELEASE_IDS, ALL_KEYS);
        notifyCacheUpdate(RHEA_SOFTWARE_RELEASE_HIDS, ALL_KEYS);
    }

    @Cacheable(RHEA_RTU_REQUEST_IDS)
    public RTURequest findRTURequestById(String id) {
        return proxy.findRTURequestById(id);
    }

    @Cacheable(RHEA_RTU_REQUEST_HIDS)
    public RTURequest findRTURequestByHid(String hid) {
        return proxy.findRTURequestByHid(hid);
    }

    @Cacheable(value = RHEA_RTU_REQUEST_NAMES, key = "#p0.concat('-').concat(#p1)", unless = "#result == null")
    public RTURequest findRTURequestByName(String companyId, String softwareReleaseId) {
        return proxy.findRTURequestByName(companyId, softwareReleaseId);
    }

    @Caching(evict = {
            @CacheEvict(value = RHEA_RTU_REQUEST_IDS, key = "#rtuRequest.id", condition = "#rtuRequest != null"),
            @CacheEvict(value = RHEA_RTU_REQUEST_NAMES, key = "#rtuRequest.companyId.concat('-').concat(#rtuRequest.softwareReleaseId)", condition = "#rtuRequest != null"),
            @CacheEvict(value = RHEA_RTU_REQUEST_HIDS, key = "#rtuRequest.hid", condition = "#rtuRequest != null") })
    public void clearRTURequest(RTURequest rtuRequest) {
        String method = "clearRTURequest";
        logInfo(method, "id: %s", rtuRequest.getId());
        notifyCacheUpdate(RHEA_RTU_REQUEST_IDS, rtuRequest.getId());
        notifyCacheUpdate(RHEA_RTU_REQUEST_NAMES,
                String.format("%s-%s", rtuRequest.getCompanyId(), rtuRequest.getSoftwareReleaseId()));
        notifyCacheUpdate(RHEA_RTU_REQUEST_HIDS, rtuRequest.getHid());
    }

    @CacheEvict(cacheNames = { RHEA_RTU_REQUEST_IDS, RHEA_RTU_REQUEST_NAMES, RHEA_RTU_REQUEST_HIDS }, allEntries = true)
    public void clearRTURequests() {
        String method = "clearRTURequests";
        logInfo(method, "...");
        notifyCacheUpdate(RHEA_RTU_REQUEST_IDS, ALL_KEYS);
        notifyCacheUpdate(RHEA_RTU_REQUEST_NAMES, ALL_KEYS);
        notifyCacheUpdate(RHEA_RTU_REQUEST_HIDS, ALL_KEYS);
    }
}
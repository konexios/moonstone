package com.arrow.rhea.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.arrow.rhea.data.DeviceCategory;
import com.arrow.rhea.data.DeviceManufacturer;
import com.arrow.rhea.data.DeviceProduct;
import com.arrow.rhea.data.DeviceType;
import com.arrow.rhea.data.RTURequest;
import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.data.SoftwareRelease;
import com.arrow.rhea.data.SoftwareVendor;

public class LocalRheaCacheProxy extends RheaCacheProxyAbstract implements RheaCacheProxy {

    @Autowired
    private DeviceManufacturerService deviceManufacturerService;
    @Autowired
    private DeviceProductService deviceProductService;
    @Autowired
    private DeviceCategoryService deviceCategoryService;
    @Autowired
    private DeviceTypeService deviceTypeService;
    @Autowired
    private SoftwareVendorService softwareVendorService;
    @Autowired
    private SoftwareProductService softwareProductService;
    @Autowired
    private SoftwareReleaseService softwareReleaseService;
    @Autowired
    private RTURequestService rtuRequestService;

    public DeviceManufacturer findDeviceManufacturerById(String id) {
        String method = "findDeviceManufacturerById";
        logInfo(method, "looking up manufacturer id: %s", id);
        return deviceManufacturerService.getDeviceManufacturerRepository().findById(id).orElse(null);
    }

    public DeviceManufacturer findDeviceManufacturerByHid(String hid) {
        String method = "findDeviceManufacturerByHid";
        logInfo(method, "looking up manufacturer hid: %s", hid);
        return deviceManufacturerService.getDeviceManufacturerRepository().doFindByHid(hid);
    }

    public DeviceProduct findDeviceProductById(String id) {
        String method = "findDeviceProductById";
        logInfo(method, "looking up device product id: %s", id);
        return deviceProductService
                .populateRefs(deviceProductService.getDeviceProductRepository().findById(id).orElse(null));
    }

    public DeviceProduct findDeviceProductByHid(String hid) {
        String method = "findDeviceProductByHid";
        logInfo(method, "looking up device product hid: %s", hid);
        return deviceProductService.populateRefs(deviceProductService.getDeviceProductRepository().doFindByHid(hid));
    }

    public DeviceCategory findDeviceCategoryById(String id) {
        String method = "findDeviceCategoryById";
        logInfo(method, "looking up device category id: %s", id);
        return deviceCategoryService
                .populateRefs(deviceCategoryService.getDeviceCategoryRepository().findById(id).orElse(null));
    }

    public DeviceCategory findDeviceCategoryByHid(String hid) {
        String method = "findDeviceCategoryByHid";
        logInfo(method, "looking up device category hid: %s", hid);
        return deviceCategoryService.populateRefs(deviceCategoryService.getDeviceCategoryRepository().doFindByHid(hid));
    }

    public DeviceType findDeviceTypeById(String id) {
        String method = "findDeviceTypeById";
        logInfo(method, "looking up device type id: %s", id);
        return deviceTypeService.populateRefs(deviceTypeService.getDeviceTypeRepository().findById(id).orElse(null));
    }

    public DeviceType findDeviceTypeByHid(String hid) {
        String method = "findDeviceTypeById";
        logInfo(method, "looking up device type hid: %s", hid);
        return deviceTypeService.populateRefs(deviceTypeService.getDeviceTypeRepository().doFindByHid(hid));
    }

    public SoftwareVendor findSoftwareVendorById(String id) {
        String method = "findSoftwareVendorById";
        logInfo(method, "looking up software vendor id: %s", id);
        return softwareVendorService
                .populateRefs(softwareVendorService.getSoftwareVendorRepository().findById(id).orElse(null));
    }

    public SoftwareVendor findSoftwareVendorByHid(String hid) {
        String method = "findSoftwareVendorByHid";
        logInfo(method, "looking up software vendor hid: %s", hid);
        return softwareVendorService.populateRefs(softwareVendorService.getSoftwareVendorRepository().doFindByHid(hid));
    }

    public SoftwareProduct findSoftwareProductById(String id) {
        String method = "findSoftwareProductById";
        logInfo(method, "looking up software release id: %s", id);
        return softwareProductService
                .populateRefs(softwareProductService.getSoftwareProductRepository().findById(id).orElse(null));
    }

    public SoftwareProduct findSoftwareProductByHid(String hid) {
        String method = "findSoftwareProductByHid";
        logInfo(method, "looking up software release hid: %s", hid);
        return softwareProductService
                .populateRefs(softwareProductService.getSoftwareProductRepository().doFindByHid(hid));
    }

    public SoftwareRelease findSoftwareReleaseById(String id) {
        String method = "findSoftwareReleaseById";
        logInfo(method, "looking up software release id: %s", id);
        return softwareReleaseService
                .populateRefs(softwareReleaseService.getSoftwareReleaseRepository().findById(id).orElse(null));
    }

    public SoftwareRelease findSoftwareReleaseByHid(String hid) {
        String method = "findSoftwareReleaseByHid";
        logInfo(method, "looking up software release hid: %s", hid);
        return softwareReleaseService
                .populateRefs(softwareReleaseService.getSoftwareReleaseRepository().doFindByHid(hid));
    }

    public RTURequest findRTURequestById(String id) {
        String method = "findRTURequestById";
        logInfo(method, "looking up rtu request id: %s", id);
        return rtuRequestService.populateRefs(rtuRequestService.getRTURequestRepository().findById(id).orElse(null));
    }

    public RTURequest findRTURequestByHid(String hid) {
        String method = "findRTURequestByHid";
        logInfo(method, "looking up rtu request hid: %s", hid);
        return rtuRequestService.populateRefs(rtuRequestService.getRTURequestRepository().doFindByHid(hid));
    }

    public RTURequest findRTURequestByName(String companyId, String softwareReleaseId) {
        String method = "findRTURequestByName";
        logInfo(method, "looking up rtu request name: %s/%s", companyId, softwareReleaseId);
        return rtuRequestService.populateRefs(rtuRequestService.getRTURequestRepository()
                .findByCompanyIdAndSoftwareReleaseId(companyId, softwareReleaseId));
    }

    public List<DeviceManufacturer> preloadDeviceManufacturers() {
        String method = "preloadDeviceManufacturers";
        logInfo(method, "...");
        return deviceManufacturerService.getDeviceManufacturerRepository().findAll();
    }

    public List<DeviceProduct> preloadDeviceProducts() {
        String method = "preloadDeviceProducts";
        logInfo(method, "...");
        return deviceProductService.getDeviceProductRepository().findAll();
    }

    public List<DeviceCategory> preloadDeviceCategories() {
        String method = "preloadDeviceCategories";
        logInfo(method, "...");
        return deviceCategoryService.getDeviceCategoryRepository().findAll();
    }

    public List<DeviceType> preloadDeviceTypes() {
        String method = "preloadDeviceTypes";
        logInfo(method, "...");
        return deviceTypeService.getDeviceTypeRepository().findAll();
    }

    public List<SoftwareVendor> preloadSoftwareVendors() {
        String method = "preloadSoftwareVendors";
        logInfo(method, "...");
        return softwareVendorService.getSoftwareVendorRepository().findAll();
    }

    public List<SoftwareProduct> preloadSoftwareProducts() {
        String method = "preloadSoftwareProducts";
        logInfo(method, "...");
        return softwareProductService.getSoftwareProductRepository().findAll();
    }

    public List<SoftwareRelease> preloadSoftwareReleases() {
        String method = "preloadSoftwareReleases";
        logInfo(method, "...");
        return softwareReleaseService.getSoftwareReleaseRepository().findAll();
    }

    public List<RTURequest> preloadRTURequests() {
        String method = "preloadRTURequests";
        logInfo(method, "...");
        return rtuRequestService.getRTURequestRepository().findAll();
    }
}

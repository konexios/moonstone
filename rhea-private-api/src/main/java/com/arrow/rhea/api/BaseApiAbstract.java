package com.arrow.rhea.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.api.ApiAbstract;
import com.arrow.pegasus.service.FileStoreService;
import com.arrow.rhea.service.DeviceCategoryService;
import com.arrow.rhea.service.DeviceManufacturerService;
import com.arrow.rhea.service.DeviceProductService;
import com.arrow.rhea.service.DeviceTypeService;
import com.arrow.rhea.service.RTURequestService;
import com.arrow.rhea.service.RheaCacheService;
import com.arrow.rhea.service.SoftwareProductService;
import com.arrow.rhea.service.SoftwareReleaseService;
import com.arrow.rhea.service.SoftwareVendorService;

public abstract class BaseApiAbstract extends ApiAbstract {

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
	private FileStoreService fileStoreService;
	@Autowired
	private RheaCacheService rheaCacheService;
	@Autowired
	private RTURequestService rtuRequestService;

	protected String getProductSystemName() {
		return ProductSystemNames.RHEA;
	}

	protected DeviceManufacturerService getDeviceManufacturerService() {
		return deviceManufacturerService;
	}

	protected DeviceProductService getDeviceProductService() {
		return deviceProductService;
	}

	protected DeviceCategoryService getDeviceCategoryService() {
		return deviceCategoryService;
	}

	protected DeviceTypeService getDeviceTypeService() {
		return deviceTypeService;
	}

	protected SoftwareVendorService getSoftwareVendorService() {
		return softwareVendorService;
	}

	protected SoftwareProductService getSoftwareProductService() {
		return softwareProductService;
	}

	protected SoftwareReleaseService getSoftwareReleaseService() {
		return softwareReleaseService;
	}

	protected FileStoreService getFileStoreService() {
		return fileStoreService;
	}

	protected RheaCacheService getRheaCacheService() {
		return rheaCacheService;
	}
	
	protected RTURequestService getRTURequestService() {
		return rtuRequestService;
	}
}

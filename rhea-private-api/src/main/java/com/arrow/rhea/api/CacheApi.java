package com.arrow.rhea.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.api.ApiAbstract;
import com.arrow.rhea.data.DeviceCategory;
import com.arrow.rhea.data.DeviceManufacturer;
import com.arrow.rhea.data.DeviceProduct;
import com.arrow.rhea.data.DeviceType;
import com.arrow.rhea.data.RTURequest;
import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.data.SoftwareRelease;
import com.arrow.rhea.data.SoftwareVendor;
import com.arrow.rhea.service.RheaCacheService;

@RestController(value = "privateRheaCacheApi")
@RequestMapping("/api/v1/private/rhea/cache")
public class CacheApi extends ApiAbstract {

	@Autowired
	private RheaCacheService rheaCacheService;

	@RequestMapping(path = "/device-manufacturers/ids/{id}", method = RequestMethod.GET)
	public DeviceManufacturer findDeviceManufacturerById(@PathVariable String id) {
		return rheaCacheService.findDeviceManufacturerById(id);
	}

	@RequestMapping(path = "/device-manufacturers/hids/{hid}", method = RequestMethod.GET)
	public DeviceManufacturer findDeviceManufacturerByHid(@PathVariable String hid) {
		return rheaCacheService.findDeviceManufacturerByHid(hid);
	}

	@RequestMapping(path = "/device-categories/ids/{id}", method = RequestMethod.GET)
	public DeviceCategory findDeviceCategoryById(@PathVariable String id) {
		return rheaCacheService.findDeviceCategoryById(id);
	}

	@RequestMapping(path = "/device-categories/hids/{hid}", method = RequestMethod.GET)
	public DeviceCategory findDeviceCategoryByHid(@PathVariable String hid) {
		return rheaCacheService.findDeviceCategoryByHid(hid);
	}

	@RequestMapping(path = "/device-products/ids/{id}", method = RequestMethod.GET)
	public DeviceProduct findDeviceProductById(@PathVariable String id) {
		return rheaCacheService.findDeviceProductById(id);
	}

	@RequestMapping(path = "/device-products/hids/{hid}", method = RequestMethod.GET)
	public DeviceProduct findDeviceProductByHid(@PathVariable String hid) {
		return rheaCacheService.findDeviceProductByHid(hid);
	}

	@RequestMapping(path = "/device-types/ids/{id}", method = RequestMethod.GET)
	public DeviceType findDeviceTypeById(@PathVariable String id) {
		return rheaCacheService.findDeviceTypeById(id);
	}

	@RequestMapping(path = "/device-types/hids/{hid}", method = RequestMethod.GET)
	public DeviceType findDeviceTypeByHid(@PathVariable String hid) {
		return rheaCacheService.findDeviceTypeByHid(hid);
	}

	@RequestMapping(path = "/software-vendors/ids/{id}", method = RequestMethod.GET)
	public SoftwareVendor findSoftwareVendorById(@PathVariable String id) {
		return rheaCacheService.findSoftwareVendorById(id);
	}

	@RequestMapping(path = "/software-vendors/hids/{hid}", method = RequestMethod.GET)
	public SoftwareVendor findSoftwareVendorByHid(@PathVariable String hid) {
		return rheaCacheService.findSoftwareVendorByHid(hid);
	}

	@RequestMapping(path = "/software-products/ids/{id}", method = RequestMethod.GET)
	public SoftwareProduct findSoftwareProductById(@PathVariable String id) {
		return rheaCacheService.findSoftwareProductById(id);
	}

	@RequestMapping(path = "/software-products/hids/{hid}", method = RequestMethod.GET)
	public SoftwareProduct findSoftwareProductByHid(@PathVariable String hid) {
		return rheaCacheService.findSoftwareProductByHid(hid);
	}

	@RequestMapping(path = "/software-releases/ids/{id}", method = RequestMethod.GET)
	public SoftwareRelease findSoftwareReleaseById(@PathVariable String id) {
		return rheaCacheService.findSoftwareReleaseById(id);
	}

	@RequestMapping(path = "/software-releases/hids/{hid}", method = RequestMethod.GET)
	public SoftwareRelease findSoftwareReleaseByHid(@PathVariable String hid) {
		return rheaCacheService.findSoftwareReleaseByHid(hid);
	}

	@RequestMapping(path = "/rtu-requests/ids/{id}", method = RequestMethod.GET)
	public RTURequest findRTURequestById(@PathVariable String id) {
		return rheaCacheService.findRTURequestById(id);
	}

	@RequestMapping(path = "/rtu-requests/hids/{hid}", method = RequestMethod.GET)
	public RTURequest findRTURequestByHid(@PathVariable String hid) {
		return rheaCacheService.findRTURequestByHid(hid);
	}

	@RequestMapping(path = "/rtu-requests/names/{companyId}/{softwareReleaseId}", method = RequestMethod.GET)
	public RTURequest findRTURequestByName(@PathVariable String companyId, @PathVariable String softwareReleaseId) {
		return rheaCacheService.findRTURequestByName(companyId, softwareReleaseId);
	}
}

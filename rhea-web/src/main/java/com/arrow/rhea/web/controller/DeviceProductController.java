package com.arrow.rhea.web.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.rhea.data.DeviceManufacturer;
import com.arrow.rhea.data.DeviceProduct;
import com.arrow.rhea.repo.DeviceProductSearchParams;
import com.arrow.rhea.service.DeviceManufacturerService;
import com.arrow.rhea.service.DeviceProductService;
import com.arrow.rhea.web.model.DeviceManufacturerModels.DeviceManufacturerOption;
import com.arrow.rhea.web.model.DeviceProductModels.DeviceProductModel;
import com.arrow.rhea.web.model.DeviceProductModels.DeviceProductOptions;
import com.arrow.rhea.web.model.SearchFilterModels.DeviceProductSearchFilterModel;
import com.arrow.rhea.web.model.SearchResultModels.DeviceProductSearchResultModel;

@RestController
@RequestMapping("/api/rhea/device-products")
public class DeviceProductController extends ControllerAbstract {

	@Autowired
	private DeviceProductService deviceProductService;

	@Autowired
	private DeviceManufacturerService deviceManufacturerService;

	// @Autowired
	// private DeviceCategoryService deviceCategoryService;

	@PreAuthorize("hasAuthority('RHEA_READ_DEVICE_PRODUCTS')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public DeviceProductSearchResultModel find(@RequestBody DeviceProductSearchFilterModel searchFilter) {

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		DeviceProductSearchParams params = new DeviceProductSearchParams();
		// implied/enforced filter
		params.addCompanyIds(getAuthenticatedUser().getCompanyId());
		// user defined filter
		params.setEnabled(searchFilter.isEnabled());
		params.addDeviceManufacturerIds(searchFilter.getDeviceManufacturerIds());
		// params.addDeviceCategoryIds(searchFilter.getDeviceCategoryIds());

		if (searchFilter.getDeviceCategories() != null && searchFilter.getDeviceCategories().length > 0) {
			EnumSet<AcnDeviceCategory> deviceCategorySet = EnumSet.noneOf(AcnDeviceCategory.class);
			for (String name : searchFilter.getDeviceCategories())
				deviceCategorySet.add(AcnDeviceCategory.valueOf(name));
			params.setDeviceCategories(deviceCategorySet);
		}

		Page<DeviceProduct> deviceProducts = deviceProductService.getDeviceProductRepository()
				.findDeviceProducts(pageRequest, params);

		// convert to visual model
		List<DeviceProductModel> deviceProductModels = new ArrayList<>();
		for (DeviceProduct deviceProduct : deviceProducts) {
			deviceProductModels.add(new DeviceProductModel(deviceProductService.populateRefs(deviceProduct)));
		}

		Page<DeviceProductModel> result = new PageImpl<>(deviceProductModels, pageRequest,
				deviceProducts.getTotalElements());

		return new DeviceProductSearchResultModel(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('RHEA_CREATE_DEVICE_PRODUCT')")
	@RequestMapping(method = RequestMethod.POST)
	public DeviceProductModel create(@RequestBody DeviceProductModel model) {
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getDeviceManufacturer(), "deviceManufactorer in model is null");
		Assert.hasText(model.getDeviceCategory(), "deviceCategory in model is empty");

		DeviceManufacturer deviceManufacturer = getRheaCacheService()
				.findDeviceManufacturerById(model.getDeviceManufacturer().getId());
		Assert.notNull(deviceManufacturer,
				"Unable to find deviceManufacturer! deviceManufacturerId=" + model.getDeviceManufacturer().getId());

		// DeviceCategory deviceCategory = getRheaCacheService()
		// .findDeviceCategoryById(model.getDeviceCategory().getId());
		// Assert.notNull(deviceCategory,
		// "Unable to find deviceCategory! deviceCategoryId=" +
		// model.getDeviceCategory().getId());

		DeviceProduct deviceProduct = new DeviceProduct();
		deviceProduct.setCompanyId(getAuthenticatedUser().getCompanyId());
		deviceProduct.setName(model.getName());
		deviceProduct.setDescription(model.getDescription());
		deviceProduct.setEnabled(model.isEnabled());
		deviceProduct.setDeviceManufacturerId(deviceManufacturer.getId());
		// deviceProduct.setDeviceCategoryId(deviceCategory.getId());
		deviceProduct.setDeviceCategory(AcnDeviceCategory.valueOf(model.getDeviceCategory()));

		deviceProduct = deviceProductService.create(deviceProduct, getUserId());

		return new DeviceProductModel(deviceProduct);
	}

	@PreAuthorize("hasAuthority('RHEA_UPDATE_DEVICE_PRODUCT')")
	@RequestMapping(value = "/{deviceProductId}", method = RequestMethod.PUT)
	public DeviceProductModel update(@PathVariable String deviceProductId, @RequestBody DeviceProductModel model) {
		Assert.hasText(deviceProductId, "deviceProductId is empty");
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getDeviceManufacturer(), "deviceManufacturer in model is null");
		Assert.hasText(model.getDeviceCategory(), "deviceCategory in model is empty");

		DeviceManufacturer deviceManufactorer = getRheaCacheService()
				.findDeviceManufacturerById(model.getDeviceManufacturer().getId());
		Assert.notNull(deviceManufactorer,
				"Unable to find deviceManufacturer! deviceManufacturerId=" + model.getDeviceManufacturer().getId());

		// DeviceCategory deviceCategory =
		// getRheaCacheService().findDeviceCategoryById(model.getDeviceCategory().getId());
		// Assert.notNull(deviceCategory,
		// "Unable to find deviceCategory! deviceCategoryId=" +
		// model.getDeviceCategory().getId());

		DeviceProduct deviceProduct = getRheaCacheService().findDeviceProductById(deviceProductId);
		Assert.notNull(deviceProduct, "Unable to find deviceProduct! deviceProductId=" + deviceProductId);
		Assert.isTrue(deviceProduct.isEditable(), "deviceProduct is not editable");
		Assert.isTrue(StringUtils.equals(getAuthenticatedUser().getCompanyId(), deviceProduct.getCompanyId()),
				"user and deviceProduct must have the same companyId");

		deviceProduct.setName(model.getName());
		deviceProduct.setDescription(model.getDescription());
		deviceProduct.setEnabled(model.isEnabled());
		deviceProduct.setDeviceManufacturerId(deviceManufactorer.getId());
		// deviceProduct.setDeviceCategoryId(deviceCategory.getId());
		deviceProduct.setDeviceCategory(AcnDeviceCategory.valueOf(model.getDeviceCategory()));

		deviceProduct = deviceProductService.update(deviceProduct, getUserId());

		return new DeviceProductModel(deviceProduct);
	}

	@PreAuthorize("hasAuthority('RHEA_CREATE_DEVICE_PRODUCT') or hasAuthority('RHEA_UPDATE_DEVICE_PRODUCT')")
	@RequestMapping(value = "/options", method = RequestMethod.GET)
	public DeviceProductOptions options() {

		List<DeviceManufacturer> deviceManufacturers = deviceManufacturerService.getDeviceManufacturerRepository()
				.findAllByEnabled(true);

		List<DeviceManufacturerOption> manufacturerOptions = new ArrayList<>(deviceManufacturers.size());
		deviceManufacturers.stream().forEach(
				deviceManufacturer -> manufacturerOptions.add(new DeviceManufacturerOption(deviceManufacturer)));

		manufacturerOptions
				.sort(Comparator.comparing(DeviceManufacturerOption::getName, String.CASE_INSENSITIVE_ORDER));

		// List<DeviceCategory> deviceCategories =
		// deviceCategoryService.getDeviceCategoryRepository()
		// .findAllByEnabled(true);
		// List<DeviceCategoryOption> categoryOptions = new
		// ArrayList<>(deviceManufacturers.size());
		// deviceCategories.stream().forEach(category -> categoryOptions.add(new
		// DeviceCategoryOption(category)));

		EnumSet<AcnDeviceCategory> deviceCategoryOptions = EnumSet.of(AcnDeviceCategory.GATEWAY,
				AcnDeviceCategory.DEVICE);

		return new DeviceProductOptions(manufacturerOptions, deviceCategoryOptions);
	}
}

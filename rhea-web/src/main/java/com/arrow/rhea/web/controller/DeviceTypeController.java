package com.arrow.rhea.web.controller;

import java.util.ArrayList;
import java.util.Comparator;
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

import com.arrow.rhea.data.DeviceProduct;
import com.arrow.rhea.data.DeviceType;
import com.arrow.rhea.repo.DeviceTypeSearchParams;
import com.arrow.rhea.service.DeviceProductService;
import com.arrow.rhea.service.DeviceTypeService;
import com.arrow.rhea.web.model.DeviceProductModels.DeviceProductOption;
import com.arrow.rhea.web.model.DeviceTypeModels.DeviceTypeModel;
import com.arrow.rhea.web.model.SearchFilterModels.DeviceTypeSearchFilterModel;
import com.arrow.rhea.web.model.SearchResultModels.DeviceTypeSearchResultModel;

@RestController
@RequestMapping("/api/rhea/device-types")
public class DeviceTypeController extends ControllerAbstract {

	@Autowired
	private DeviceTypeService deviceTypeService;

	@Autowired
	private DeviceProductService deviceProductService;

	@PreAuthorize("hasAuthority('RHEA_READ_DEVICE_TYPES')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public DeviceTypeSearchResultModel find(@RequestBody DeviceTypeSearchFilterModel searchFilter) {

		// sorting & paging
		PageRequest pageRequest = new PageRequest(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
		        new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		DeviceTypeSearchParams params = new DeviceTypeSearchParams();
		// implied/enforced filter
		params.addCompanyIds(getAuthenticatedUser().getCompanyId());
		// user defined filter
		params.setEnabled(searchFilter.isEnabled());
		params.addDeviceProductIds(searchFilter.getDeviceProductIds());

		Page<DeviceType> deviceTypes = deviceTypeService.getDeviceTypeRepository().findDeviceTypes(pageRequest, params);

		// convert to visual model
		List<DeviceTypeModel> deviceTypeModels = new ArrayList<>();
		for (DeviceType deviceType : deviceTypes) {
			deviceTypeModels.add(new DeviceTypeModel(deviceTypeService.populateRefs(deviceType)));
		}

		Page<DeviceTypeModel> result = new PageImpl<>(deviceTypeModels, pageRequest, deviceTypes.getTotalElements());

		return new DeviceTypeSearchResultModel(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('RHEA_CREATE_DEVICE_TYPE')")
	@RequestMapping(method = RequestMethod.POST)
	public DeviceTypeModel create(@RequestBody DeviceTypeModel model) {
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getDeviceProduct(), "deviceProduct in model is null");

		DeviceProduct deviceProduct = getRheaCacheService().findDeviceProductById(model.getDeviceProduct().getId());
		Assert.notNull(deviceProduct,
		        "Unable to find deviceProduct! deviceProductId=" + model.getDeviceProduct().getId());
		Assert.isTrue(StringUtils.equals(getAuthenticatedUser().getCompanyId(), deviceProduct.getCompanyId()),
		        "user and deviceProduct must have the same companyId");

		DeviceType deviceType = new DeviceType();
		deviceType.setCompanyId(getAuthenticatedUser().getCompanyId());
		deviceType.setName(model.getName());
		deviceType.setDescription(model.getDescription());
		deviceType.setEnabled(model.isEnabled());
		deviceType.setDeviceProductId(deviceProduct.getId());

		deviceType = deviceTypeService.create(deviceType, getUserId());

		return new DeviceTypeModel(deviceType);
	}

	@PreAuthorize("hasAuthority('RHEA_UPDATE_DEVICE_TYPE')")
	@RequestMapping(value = "/{deviceTypeId}", method = RequestMethod.PUT)
	public DeviceTypeModel update(@PathVariable String deviceTypeId, @RequestBody DeviceTypeModel model) {
		Assert.hasText(deviceTypeId, "deviceTypeId is empty");
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getDeviceProduct(), "deviceProduct is null");

		DeviceProduct deviceProduct = getRheaCacheService().findDeviceProductById(model.getDeviceProduct().getId());
		Assert.notNull(deviceProduct,
		        "Unable to find deviceProduct! deviceProductId=" + model.getDeviceProduct().getId());
		Assert.isTrue(StringUtils.equals(getAuthenticatedUser().getCompanyId(), deviceProduct.getCompanyId()),
		        "user and deviceProduct must have the same companyId");

		DeviceType deviceType = getRheaCacheService().findDeviceTypeById(deviceTypeId);
		Assert.notNull(deviceType, "Unable to find deviceType! deviceTypeId=" + deviceTypeId);
		Assert.isTrue(deviceType.isEditable(), "deviceType is not editable");
		Assert.isTrue(StringUtils.equals(getAuthenticatedUser().getCompanyId(), deviceType.getCompanyId()),
		        "user and deviceType must have the same companyId");

		deviceType.setName(model.getName());
		deviceType.setDescription(model.getDescription());
		deviceType.setEnabled(model.isEnabled());
		deviceType.setDeviceProductId(deviceProduct.getId());

		deviceType = deviceTypeService.update(deviceType, getUserId());

		return new DeviceTypeModel(deviceType);
	}

	@PreAuthorize("hasAuthority('RHEA_CREATE_DEVICE_TYPE') or hasAuthority('RHEA_UPDATE_DEVICE_TYPE')")
	@RequestMapping(value = "/options", method = RequestMethod.GET)
	public List<DeviceProductOption> options() {
		List<DeviceProduct> deviceProducts = deviceProductService.getDeviceProductRepository()
				.findAllByCompanyIdAndEnabled(getAuthenticatedUser().getCompanyId(), true);

		List<DeviceProductOption> options = new ArrayList<>(deviceProducts.size());
		deviceProducts.stream().forEach(deviceProduct -> 
				options.add(new DeviceProductOption(deviceProduct)));

		options.sort(Comparator.comparing(DeviceProductOption::getName,String.CASE_INSENSITIVE_ORDER));
		return options;
	}
}

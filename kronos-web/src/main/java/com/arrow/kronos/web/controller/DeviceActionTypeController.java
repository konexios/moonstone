package com.arrow.kronos.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

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

import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.repo.KronosDocumentSearchParams;
import com.arrow.kronos.service.DeviceActionTypeService;
import com.arrow.kronos.web.model.DeviceActionTypeModels;
import com.arrow.kronos.web.model.SearchResultModels;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.webapi.data.CoreSearchFilterModel;

@RestController
@RequestMapping("/api/kronos/deviceactiontype")
public class DeviceActionTypeController extends BaseControllerAbstract {

	@Autowired
	private DeviceActionTypeService deviceActionTypeService;

	@PreAuthorize("hasAuthority('KRONOS_VIEW_DEVICE_ACTION_TYPES')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public SearchResultModels.DeviceActionTypeSearchResultModel list(@RequestBody CoreSearchFilterModel searchFilter,
			HttpSession session) {

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				Sort.by(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		KronosDocumentSearchParams params = new KronosDocumentSearchParams();
		params.addApplicationIds(getApplicationId(session));

		Page<DeviceActionType> deviceActionTypes = deviceActionTypeService.getDeviceActionTypeRepository()
				.findDeviceActionTypes(pageRequest, params);

		// convert to visual model
		List<DeviceActionTypeModels.DeviceActionTypeDetailsModel> deviceTypeModels = new ArrayList<>();
		for (DeviceActionType deviceActionType : deviceActionTypes) {
			User currentUser = getCoreCacheService().findUserById(deviceActionType.getLastModifiedBy());
			if (currentUser != null) {
				deviceActionType.setLastModifiedBy(getKronosModelUtil().populateDecryptedLogin(currentUser));
			} else if (!deviceActionType.getLastModifiedBy().equals("admin")) {
				deviceActionType.setLastModifiedBy("Unknown");
			}
			deviceTypeModels.add(new DeviceActionTypeModels.DeviceActionTypeDetailsModel(deviceActionType));
		}
		Page<DeviceActionTypeModels.DeviceActionTypeDetailsModel> result = new PageImpl<>(deviceTypeModels, pageRequest,
				deviceActionTypes.getTotalElements());

		return new SearchResultModels.DeviceActionTypeSearchResultModel(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('KRONOS_CREATE_DEVICE_ACTION_TYPE')")
	@RequestMapping(method = RequestMethod.POST)
	public DeviceActionTypeModels.DeviceActionTypeDetailsModel add(
			@RequestBody DeviceActionTypeModels.DeviceActionTypeDetailsModel deviceActionTypeModel,
			HttpSession session) {

		DeviceActionType deviceActionType = new DeviceActionType();
		deviceActionType.setApplicationId(getApplicationId(session)); // mandatory
		// all user-defined action types must be editable
		deviceActionType.setEditable(true);
		deviceActionType.setSystemName(deviceActionTypeModel.getSystemName());
		deviceActionType.setName(deviceActionTypeModel.getName());
		deviceActionType.setDescription(deviceActionTypeModel.getDescription());
		deviceActionType.setEnabled(deviceActionTypeModel.isEnabled());
		deviceActionType.setParameters(deviceActionTypeModel.getParameters());

		deviceActionType = deviceActionTypeService.create(deviceActionType, getUserId());

		return new DeviceActionTypeModels.DeviceActionTypeDetailsModel(deviceActionType);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE_ACTION_TYPE')")
	@RequestMapping(value = "/{deviceActionTypeId}", method = RequestMethod.PUT)
	public DeviceActionTypeModels.DeviceActionTypeDetailsModel edit(@PathVariable String deviceActionTypeId,
			@RequestBody DeviceActionTypeModels.DeviceActionTypeDetailsModel deviceActionTypeModel,
			HttpSession session) {

		DeviceActionType deviceActionType = getKronosCache().findDeviceActionTypeById(deviceActionTypeId);
		Assert.notNull(deviceActionType, "Device Action Type is null");
		// make sure the user and device type have the same application id
		Assert.isTrue(getApplicationId(session).equals(deviceActionType.getApplicationId()),
				"user and device action type must have the same application id");
		Assert.isTrue(deviceActionType.isEditable(), "Device Action Type is not editable");

		deviceActionType.setSystemName(deviceActionTypeModel.getSystemName());
		deviceActionType.setName(deviceActionTypeModel.getName());
		deviceActionType.setDescription(deviceActionTypeModel.getDescription());
		deviceActionType.setEnabled(deviceActionTypeModel.isEnabled());
		deviceActionType.setParameters(deviceActionTypeModel.getParameters());

		deviceActionType = deviceActionTypeService.update(deviceActionType, getUserId());

		return new DeviceActionTypeModels.DeviceActionTypeDetailsModel(deviceActionType);
	}
}

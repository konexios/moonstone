package com.arrow.kronos.web.controller;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
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

import com.arrow.kronos.DeviceActionTypeConstants;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.TelemetryStat;
import com.arrow.kronos.data.TelemetryUnit;
import com.arrow.kronos.repo.DeviceTypeSearchParams;
import com.arrow.kronos.service.DeviceActionTypeService;
import com.arrow.kronos.service.DeviceService;
import com.arrow.kronos.service.GatewayService;
import com.arrow.kronos.service.TelemetryUnitService;
import com.arrow.kronos.web.model.DeviceActionTypeModels;
import com.arrow.kronos.web.model.DeviceTypeModels;
import com.arrow.kronos.web.model.RheaModels;
import com.arrow.kronos.web.model.SearchResultModels;
import com.arrow.kronos.web.model.TelemetryUnitModels;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.webapi.data.CoreSearchFilterModel;
import com.arrow.rhea.client.api.ClientCacheApi;
import com.arrow.rhea.client.api.ClientDeviceManufacturerApi;
import com.arrow.rhea.client.api.ClientDeviceProductApi;
import com.arrow.rhea.client.api.ClientDeviceTypeApi;
import com.arrow.rhea.data.DeviceManufacturer;
import com.arrow.rhea.data.DeviceProduct;

import moonstone.acn.client.model.AcnDeviceCategory;
import moonstone.acs.AcsLogicalException;

@RestController
@RequestMapping("/api/kronos/devicetype")
public class DeviceTypeController extends BaseControllerAbstract {
	@Autowired
	private DeviceService deviceService;

	@Autowired
	private TelemetryUnitService telemetryUnitService;

	@Autowired
	private ClientCacheApi rheaClientCache;

	@Autowired
	private ClientDeviceManufacturerApi clientDeviceManufacturerApi;

	@Autowired
	private ClientDeviceProductApi clientDeviceProductApi;

	// @Autowired
	// private ClientDeviceCategoryApi clientDeviceCategoryApi;

	@Autowired
	private ClientDeviceTypeApi clientDeviceTypeApi;

	@Autowired
	private GatewayService gatewayService;

	@Autowired
	private DeviceActionTypeService deviceActionTypeService;

	@PreAuthorize("hasAuthority('KRONOS_VIEW_DEVICE_TYPES')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public SearchResultModels.DeviceTypeSearchResultModel list(@RequestBody CoreSearchFilterModel searchFilter,
			HttpSession session) {

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				Sort.by(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		DeviceTypeSearchParams params = new DeviceTypeSearchParams();
		params.addApplicationIds(getApplicationId(session));

		Page<DeviceType> deviceTypes = getDeviceTypeService().getDeviceTypeRepository().findDeviceTypes(pageRequest,
				params);

		// convert to visual model
		List<DeviceTypeModels.DeviceTypeListModel> deviceTypeModels = new ArrayList<>();
		Map<String, DeviceTypeModels.DeviceTypeListModel> deviceTypeModelDeviceMap = new HashMap<>();
		Map<String, DeviceTypeModels.DeviceTypeListModel> deviceTypeModelGatewayMap = new HashMap<>();
		for (DeviceType deviceType : deviceTypes) {
			User currentUser = getCoreCacheService().findUserById(deviceType.getLastModifiedBy());
			if (currentUser != null) {
				deviceType.setLastModifiedBy(getKronosModelUtil().populateDecryptedLogin(currentUser));
			} else if (!deviceType.getLastModifiedBy().equals("admin")) {
				deviceType.setLastModifiedBy("Unknown");
			}
			DeviceTypeModels.DeviceTypeListModel deviceTypeModel = new DeviceTypeModels.DeviceTypeListModel(deviceType);

			if (deviceType.getDeviceCategory() == AcnDeviceCategory.DEVICE) {
				deviceTypeModelDeviceMap.put(deviceType.getId(), deviceTypeModel);
			} else if (deviceType.getDeviceCategory() == AcnDeviceCategory.GATEWAY) {
				deviceTypeModelGatewayMap.put(deviceType.getId(), deviceTypeModel);
			}
			deviceTypeModels.add(deviceTypeModel);
		}

		// get number of devices by device type id
		List<TelemetryStat> telemetryDevicesStats = deviceService.getDeviceRepository().doCountDevicesByType(
				deviceTypeModelDeviceMap.keySet().toArray(new String[deviceTypeModelDeviceMap.size()]), true);
		for (TelemetryStat telemetryStat : telemetryDevicesStats) {
			DeviceTypeModels.DeviceTypeListModel deviceTypeModel = deviceTypeModelDeviceMap
					.get(telemetryStat.getName());
			if (deviceTypeModel != null) {
				try {
					deviceTypeModel.setNumDevices(Long.parseLong(telemetryStat.getValue()));
				} catch (NumberFormatException nfe) {
					// ignore
				}
			}
		}

		// get number of gateways by device type id
		List<TelemetryStat> telemetryGatewayStats = gatewayService.getGatewayRepository().countGatewaysByType(
				deviceTypeModelGatewayMap.keySet().toArray(new String[deviceTypeModelGatewayMap.size()]), true);
		for (TelemetryStat telemetryGatewayStat : telemetryGatewayStats) {
			DeviceTypeModels.DeviceTypeListModel deviceTypeGatewayModel = deviceTypeModelGatewayMap
					.get(telemetryGatewayStat.getName());
			if (deviceTypeGatewayModel != null) {
				try {
					deviceTypeGatewayModel.setNumDevices(Long.parseLong(telemetryGatewayStat.getValue()));
				} catch (NumberFormatException nfe) {
					// ignore
				}
			}
		}

		Page<DeviceTypeModels.DeviceTypeListModel> result = new PageImpl<>(deviceTypeModels, pageRequest,
				deviceTypes.getTotalElements());

		return new SearchResultModels.DeviceTypeSearchResultModel(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('KRONOS_VIEW_DEVICE_TYPES')")
	@RequestMapping(value = "/{deviceTypeId}", method = RequestMethod.GET)
	public DeviceTypeModels.DeviceTypeDetailsModel get(@PathVariable String deviceTypeId, HttpSession session) {
		Assert.hasText(deviceTypeId, "deviceTypeId cannot be empty");

		DeviceType deviceType = getKronosCache().findDeviceTypeById(deviceTypeId);
		Assert.notNull(deviceType, "deviceType is null");
		Assert.isTrue(StringUtils.equals(deviceType.getApplicationId(), getApplicationId(session)),
				"applications mismatch");

		DeviceTypeModels.DeviceTypeDetailsModel deviceTypeModel = new DeviceTypeModels.DeviceTypeDetailsModel(
				deviceType);

		// deviceType actions
		List<DeviceAction> deviceActionModels = deviceType.getActions();
		deviceTypeModel.setActions(deviceActionModels);

		if (deviceTypeModel.getDeviceCategory() == AcnDeviceCategory.DEVICE) {
			List<TelemetryStat> telemetryStats = deviceService.getDeviceRepository()
					.doCountDevicesByType(new String[] { deviceType.getId() }, true);
			if (telemetryStats != null && !telemetryStats.isEmpty()) {
				try {
					deviceTypeModel.setNumDevices(Long.parseLong(telemetryStats.get(0).getValue()));
				} catch (NumberFormatException nfe) {
					// ignore
				}
			}
		} else if (deviceTypeModel.getDeviceCategory() == AcnDeviceCategory.GATEWAY) {
			List<TelemetryStat> telemetryStats = gatewayService.getGatewayRepository()
					.countGatewaysByType(new String[] { deviceType.getId() }, true);
			if (telemetryStats != null && !telemetryStats.isEmpty()) {
				try {
					deviceTypeModel.setNumDevices(Long.parseLong(telemetryStats.get(0).getValue()));
				} catch (NumberFormatException nfe) {
					// ignore
				}
			}
		}

		return deviceTypeModel;
	}

	@RequestMapping(value = "/options", method = RequestMethod.POST)
	public DeviceTypeModels.DeviceTypeOptionsModel options(
			@RequestBody DeviceTypeModels.DeviceTypeSelectionModel selection, HttpSession session) {
		DeviceTypeModels.DeviceTypeOptionsModel options = new DeviceTypeModels.DeviceTypeOptionsModel();

		Application application = getCoreCacheService().findApplicationById(getApplicationId(session));

		// fill missing ids in the selection object if possible
		if (StringUtils.isBlank(selection.getProductId()) && StringUtils.isNotBlank(selection.getProductTypeId())) {
			com.arrow.rhea.data.DeviceType rheaDeviceType = rheaClientCache
					.findDeviceTypeById(selection.getProductTypeId());
			selection.setProductId(rheaDeviceType.getDeviceProductId());
		}
		if ((StringUtils.isBlank(selection.getManufacturerId()) || selection.getDeviceCategory() == null)
				&& StringUtils.isNotBlank(selection.getProductId())) {
			DeviceProduct deviceProduct = rheaClientCache.findDeviceProductById(selection.getProductId());
			selection.setManufacturerId(deviceProduct.getDeviceManufacturerId());
			// selection.setCategoryId(deviceProduct.getDeviceCategoryId());
			selection.setDeviceCategory(deviceProduct.getDeviceCategory());
		}

		// get available manufacturers
		List<DeviceManufacturer> manufacturers = clientDeviceManufacturerApi.findAllByEnabled(true);
		List<RheaModels.DeviceManufacturerOption> manufacturerModels = new ArrayList<>(manufacturers.size());
		for (DeviceManufacturer manufacturer : manufacturers) {
			manufacturerModels.add(new RheaModels.DeviceManufacturerOption(manufacturer));
		}
		options.setManufacturers(manufacturerModels);

		// get available device categories
		// List<DeviceCategory> categories =
		// clientDeviceCategoryApi.findAll(true);
		// List<RheaModels.DeviceCategoryOption> categoryModels = new
		// ArrayList<>(manufacturers.size());
		// for (DeviceCategory category : categories) {
		// categoryModels.add(new RheaModels.DeviceCategoryOption(category));
		// }
		// options.setCategories(categoryModels);
		options.setDeviceCategories(EnumSet.of(AcnDeviceCategory.GATEWAY, AcnDeviceCategory.DEVICE));

		// get available device products
		if (StringUtils.isNotBlank(selection.getManufacturerId()) && selection.getDeviceCategory() != null) {
			List<DeviceProduct> deviceProducts = clientDeviceProductApi.findAll(null,
					new String[] { selection.getManufacturerId() },
					new String[] { selection.getDeviceCategory().name() }, true);
			List<RheaModels.DeviceProductOption> deviceProductModels = new ArrayList<>();
			for (DeviceProduct deviceProduct : deviceProducts) {
				deviceProductModels.add(new RheaModels.DeviceProductOption(deviceProduct));
			}
			options.setProducts(deviceProductModels);
		}

		// get available device types
		if (StringUtils.isNotBlank(selection.getProductId())) {
			List<com.arrow.rhea.data.DeviceType> rheaDeviceTypes = clientDeviceTypeApi.findAll(null,
					new String[] { selection.getProductId() }, null, true);
			List<RheaModels.DeviceProductTypeOption> deviceProductTypeModels = new ArrayList<>();
			for (com.arrow.rhea.data.DeviceType rheaDeviceType : rheaDeviceTypes) {
				deviceProductTypeModels.add(new RheaModels.DeviceProductTypeOption(rheaDeviceType));
			}
			options.setProductTypes(deviceProductTypeModels);
		}

		// telemetry units
		List<TelemetryUnit> telemetryUnits = telemetryUnitService.getTelemetryUnitRepository().findAllByEnabled(true);
		List<TelemetryUnitModels.TelemetryUnitOption> telemetryUnitOptions = new ArrayList<>(telemetryUnits.size());
		for (TelemetryUnit telemetryUnit : telemetryUnits) {
			telemetryUnitOptions.add(new TelemetryUnitModels.TelemetryUnitOption(telemetryUnit));
		}
		options.setTelemetryUnits(telemetryUnitOptions);

		// selection
		options.setSelection(selection);

		List<DeviceActionType> actionTypes = deviceActionTypeService.getDeviceActionTypeRepository()
				.findByApplicationId(application.getId());

		List<DeviceActionTypeModels.DeviceActionTypeOption> actionTypesOptions = new ArrayList<>(actionTypes.size());
		for (DeviceActionType actionType : actionTypes) {
			actionTypesOptions.add(new DeviceActionTypeModels.DeviceActionTypeOption(actionType));
		}
		options.setActionTypes(actionTypesOptions);
		// set contentType
		options.setContentTypes(DeviceActionTypeConstants.PostBackURL.ContentType.values());

		return options;
	}

	@PreAuthorize("hasAuthority('KRONOS_CREATE_DEVICE_TYPE')")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public DeviceTypeModels.DeviceTypeDetailsModel createDeviceType(
			@RequestBody DeviceTypeModels.DeviceTypeListModel deviceTypeModel, HttpSession session) {

		DeviceType cachedDeviceType = getKronosCache().findDeviceTypeByName(getApplicationId(session),
				deviceTypeModel.getName());
		if (cachedDeviceType != null) {
			throw new AcsLogicalException("device type already exists");
		}

		DeviceType deviceType = new DeviceType();
		deviceType.setApplicationId(getApplicationId(session)); // mandatory
		deviceType.setName(deviceTypeModel.getName());
		deviceType.setDescription(deviceTypeModel.getDescription());
		deviceType.setDeviceCategory(deviceTypeModel.getDeviceCategory());
		// user-defined device types are always editable
		deviceType.setEnabled(deviceTypeModel.isEnabled());
		// deviceType.setRheaDeviceTypeId(deviceTypeModel.getProductTypeId());
		deviceType = getDeviceTypeService().create(deviceType, getUserId());

		return new DeviceTypeModels.DeviceTypeDetailsModel(deviceType);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE_TYPE')")
	@RequestMapping(value = "/{deviceTypeId}/edit/telemetry", method = RequestMethod.PUT)
	public DeviceTypeModels.DeviceTypeDetailsModel editDeviceTypeTelemetry(@PathVariable String deviceTypeId,
			@RequestBody DeviceTypeModels.DeviceTypeDetailsModel deviceTypeModel, HttpSession session) {

		DeviceType deviceType = getDeviceTypeService().getDeviceTypeRepository().findById(deviceTypeId).orElse(null);
		Assert.notNull(deviceType, "Device Type is null");
		// make sure the user and device type have the same application id
		Assert.isTrue(getApplicationId(session).equals(deviceType.getApplicationId()),
				"user and device type must have the same application id");
		Assert.isTrue((deviceType.isEditable() || !deviceType.isEditable() && isSystemAdmin(getAuthenticatedUser())),
				"Device Type is not editable");
		deviceType.setTelemetries(deviceTypeModel.getTelemetries());
		deviceType = getDeviceTypeService().update(deviceType, getUserId());

		return new DeviceTypeModels.DeviceTypeDetailsModel(deviceType);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE_TYPE')")
	@RequestMapping(value = "/{deviceTypeId}/edit/actions", method = RequestMethod.PUT)
	public DeviceTypeModels.DeviceTypeDetailsModel editDeviceTypeActions(@PathVariable String deviceTypeId,
			@RequestBody DeviceTypeModels.DeviceTypeDetailsModel deviceTypeModel, HttpSession session) {

		DeviceType deviceType = getDeviceTypeService().getDeviceTypeRepository().findById(deviceTypeId).orElse(null);
		Assert.notNull(deviceType, "Device Type is null");
		// make sure the user and device type have the same application id
		Assert.isTrue(getApplicationId(session).equals(deviceType.getApplicationId()),
				"user and device type must have the same application id");
		Assert.isTrue((deviceType.isEditable() || !deviceType.isEditable() && isSystemAdmin(getAuthenticatedUser())),
				"Device Type is not editable");
		deviceType.setActions(deviceTypeModel.getActions());
		deviceType = getDeviceTypeService().update(deviceType, getUserId());

		return new DeviceTypeModels.DeviceTypeDetailsModel(deviceType);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE_TYPE')")
	@RequestMapping(value = "/{deviceTypeId}/edit/statemetadata", method = RequestMethod.PUT)
	public DeviceTypeModels.DeviceTypeDetailsModel editDeviceTypeStateMetaData(@PathVariable String deviceTypeId,
			@RequestBody DeviceTypeModels.DeviceTypeDetailsModel deviceTypeModel, HttpSession session) {

		DeviceType deviceType = getDeviceTypeService().getDeviceTypeRepository().findById(deviceTypeId).orElse(null);
		Assert.notNull(deviceType, "Device Type is null");
		// make sure the user and device type have the same application id
		Assert.isTrue(getApplicationId(session).equals(deviceType.getApplicationId()),
				"user and device type must have the same application id");
		Assert.isTrue((deviceType.isEditable() || !deviceType.isEditable() && isSystemAdmin(getAuthenticatedUser())),
				"Device Type is not editable");
		deviceType.setStateMetadata(deviceTypeModel.getStateMetadata());
		deviceType = getDeviceTypeService().update(deviceType, getUserId());

		return new DeviceTypeModels.DeviceTypeDetailsModel(deviceType);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE_TYPE')")
	@RequestMapping(value = "/{deviceTypeId}/edit/general", method = RequestMethod.PUT)
	public DeviceTypeModels.DeviceTypeDetailsModel editGeneralDeviceType(@PathVariable String deviceTypeId,
			@RequestBody DeviceTypeModels.DeviceTypeDetailsModel deviceTypeModel, HttpSession session) {

		DeviceType deviceType = getDeviceTypeService().getDeviceTypeRepository().findById(deviceTypeId).orElse(null);
		Assert.notNull(deviceType, "Device Type is null");
		// make sure the user and device type have the same application id
		Assert.isTrue(getApplicationId(session).equals(deviceType.getApplicationId()),
				"user and device type must have the same application id");
		Assert.isTrue((deviceType.isEditable() || !deviceType.isEditable() && isSystemAdmin(getAuthenticatedUser())),
				"Device Type is not editable");

		DeviceType cachedDeviceType = getKronosCache().findDeviceTypeByName(deviceType.getApplicationId(),
				deviceTypeModel.getName());
		if (cachedDeviceType != null && !cachedDeviceType.getId().equals(deviceType.getId())) {
			throw new AcsLogicalException("duplicated device type name");
		}

		deviceType.setName(deviceTypeModel.getName());
		deviceType.setDescription(deviceTypeModel.getDescription());
		if (deviceType.getDeviceCategory() != deviceTypeModel.getDeviceCategory()
				&& StringUtils.isNotBlank(deviceType.getRheaDeviceTypeId())) {
			// clear RheaDeviceType because we change device category
			deviceType.setRheaDeviceTypeId(null);
		}
		deviceType.setDeviceCategory(deviceTypeModel.getDeviceCategory());
		deviceType.setEnabled(deviceTypeModel.isEnabled());
		deviceType = getDeviceTypeService().update(deviceType, getUserId());

		return new DeviceTypeModels.DeviceTypeDetailsModel(deviceType);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE_TYPE')")
	@RequestMapping(value = "/{deviceTypeId}/edit/firmware", method = RequestMethod.PUT)
	public DeviceTypeModels.DeviceTypeDetailsModel editDeviceTypeFirmware(@PathVariable String deviceTypeId,
			@RequestBody DeviceTypeModels.DeviceTypeDetailsModel deviceTypeModel, HttpSession session) {

		DeviceType deviceType = getDeviceTypeService().getDeviceTypeRepository().findById(deviceTypeId).orElse(null);
		Assert.notNull(deviceType, "Device Type is null");
		// make sure the user and device type have the same application id
		Assert.isTrue(getApplicationId(session).equals(deviceType.getApplicationId()),
				"user and device type must have the same application id");
		Assert.isTrue((deviceType.isEditable() || !deviceType.isEditable() && isSystemAdmin(getAuthenticatedUser())),
				"Device Type is not editable");

		deviceType.setRheaDeviceTypeId(deviceTypeModel.getProductTypeId());
		deviceType = getDeviceTypeService().update(deviceType, getUserId());

		return new DeviceTypeModels.DeviceTypeDetailsModel(deviceType);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE_TYPE')")
	@RequestMapping(value = "device/{deviceId}", method = RequestMethod.POST)
	public DeviceTypeModels.DeviceTypeDetailsModel syncFromDevice(@PathVariable String deviceId) {
		Assert.notNull(deviceId, "DeviceId is null");

		User authenticatedUser = getAuthenticatedUser();
		boolean checkEditable = !authenticatedUser.isAdmin();
		DeviceType deviceType = getDeviceTypeService().synchronizeTelemetryProperties(deviceId,
				authenticatedUser.getId(), checkEditable);

		return new DeviceTypeModels.DeviceTypeDetailsModel(deviceType);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE_TYPE')")
	@RequestMapping(value = "/{deviceTypeId}/enable", method = RequestMethod.POST)
	public boolean enableDevice(@PathVariable String deviceTypeId, HttpSession session) {
		String applicationId = getApplicationId(session);
		DeviceType deviceType = getKronosCache().findDeviceTypeById(deviceTypeId);
		Assert.notNull(deviceType, "device is null");

		Application application = getCoreCacheService().findApplicationById(deviceType.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and device have the same application id
		Assert.isTrue(applicationId.equals(deviceType.getApplicationId()),
				"user and device must have the same application id");

		Assert.isTrue((deviceType.isEditable() || !deviceType.isEditable() && isSystemAdmin(getAuthenticatedUser())),
				"Device Type is not editable");

		// return deviceService.enable(deviceType.getId(),
		// getUserId()).isEnabled();
		return getDeviceTypeService().enable(deviceType.getId(), getUserId()).isEnabled();
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE_TYPE')")
	@RequestMapping(value = "/{deviceTypeId}/disable", method = RequestMethod.POST)
	public boolean disableDevice(@PathVariable String deviceTypeId, HttpSession session) {
		String applicationId = getApplicationId(session);
		DeviceType deviceType = getKronosCache().findDeviceTypeById(deviceTypeId);
		Assert.notNull(deviceType, "device is null");

		Application application = getCoreCacheService().findApplicationById(deviceType.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and device have the same application id
		Assert.isTrue(applicationId.equals(deviceType.getApplicationId()),
				"user and device must have the same application id");

		Assert.isTrue((deviceType.isEditable() || !deviceType.isEditable() && isSystemAdmin(getAuthenticatedUser())),
				"Device Type is not editable");

		return getDeviceTypeService().disable(deviceType.getId(), getUserId()).isEnabled();
	}
}

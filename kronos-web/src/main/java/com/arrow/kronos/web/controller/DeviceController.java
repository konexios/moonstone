package com.arrow.kronos.web.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acn.AcnEventNames;
import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.acn.client.model.CreateConfigBackupModel;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.StatusModel;
import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.ConfigBackup;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.DeviceEvent;
import com.arrow.kronos.data.DeviceEventStatus;
import com.arrow.kronos.data.DeviceState;
import com.arrow.kronos.data.DeviceStateTrans;
import com.arrow.kronos.data.DeviceStateTrans.Status;
import com.arrow.kronos.data.DeviceStateTrans.Type;
import com.arrow.kronos.data.DeviceStateValue;
import com.arrow.kronos.data.DeviceTelemetry;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.EsTelemetryItem;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.LastTelemetryItem;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.data.SoftwareReleaseSchedule;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.kronos.repo.ConfigBackupSearchParams;
import com.arrow.kronos.repo.DeviceEventSearchParams;
import com.arrow.kronos.repo.DeviceSearchParams;
import com.arrow.kronos.repo.DeviceStateTransSearchParams;
import com.arrow.kronos.repo.SpringDataEsTelemetryItemRepository;
import com.arrow.kronos.repo.TelemetryItemSearchParams;
import com.arrow.kronos.service.ConfigBackupService;
import com.arrow.kronos.service.DeviceActionTypeService;
import com.arrow.kronos.service.DeviceEventService;
import com.arrow.kronos.service.DeviceService;
import com.arrow.kronos.service.DeviceStateService;
import com.arrow.kronos.service.DeviceStateTransService;
import com.arrow.kronos.service.GatewayCommandService;
import com.arrow.kronos.service.GatewayService;
import com.arrow.kronos.service.LastTelemetryItemService;
import com.arrow.kronos.service.NodeService;
import com.arrow.kronos.service.SoftwareReleaseScheduleService;
import com.arrow.kronos.service.TelemetryItemService;
import com.arrow.kronos.web.model.AuditLogModels.AuditLogDetails;
import com.arrow.kronos.web.model.BackupModels;
import com.arrow.kronos.web.model.BulkActionResultModel;
import com.arrow.kronos.web.model.BulkEditActionModels;
import com.arrow.kronos.web.model.DeviceActionModels.DeviceActionModel;
import com.arrow.kronos.web.model.DeviceEventModels.DeviceEventList;
import com.arrow.kronos.web.model.DeviceModels;
import com.arrow.kronos.web.model.DeviceModels.DeviceDetailModel;
import com.arrow.kronos.web.model.DeviceModels.DeviceSettingsModel;
import com.arrow.kronos.web.model.DeviceModels.LastTelemetryModel;
import com.arrow.kronos.web.model.DeviceStateModels.DeviceStateDeleteModel;
import com.arrow.kronos.web.model.DeviceStateTransModels.DeviceStateTransDetailsModel;
import com.arrow.kronos.web.model.DeviceStateTransModels.DeviceStateTransModel;
import com.arrow.kronos.web.model.DeviceTypeModels;
import com.arrow.kronos.web.model.DeviceTypeModels.DeviceTelemetryModel;
import com.arrow.kronos.web.model.GatewayModels;
import com.arrow.kronos.web.model.RheaModels.SoftwareReleaseOption;
import com.arrow.kronos.web.model.SearchFilterModels;
import com.arrow.kronos.web.model.SearchFilterModels.AuditLogSearchFilterModel;
import com.arrow.kronos.web.model.SearchFilterModels.AuditLogSearchFilterOptions;
import com.arrow.kronos.web.model.SearchFilterModels.DeviceDetailsOptions;
import com.arrow.kronos.web.model.SearchFilterModels.DeviceEventSearchFilterModel;
import com.arrow.kronos.web.model.SearchFilterModels.DeviceSearchFilterModel;
import com.arrow.kronos.web.model.SearchFilterModels.DeviceSearchFilterOptions;
import com.arrow.kronos.web.model.SearchFilterModels.DeviceStateHistoryFilterOptions;
import com.arrow.kronos.web.model.SearchFilterModels.DeviceStateTransSearchFilterModel;
import com.arrow.kronos.web.model.SearchResultModels;
import com.arrow.kronos.web.model.SearchResultModels.AuditLogSearchResultModel;
import com.arrow.kronos.web.model.SearchResultModels.DeviceEventSearchResultModel;
import com.arrow.kronos.web.model.SearchResultModels.DeviceSearchResultModel;
import com.arrow.kronos.web.model.SearchResultModels.DeviceStateTransSearchResultModel;
import com.arrow.kronos.web.model.SearchResultModels.SoftwareReleaseTransSearchResultModel;
import com.arrow.kronos.web.model.TelemetryItemModels;
import com.arrow.kronos.web.model.TelemetryItemModels.TelemetryItemChartModel;
import com.arrow.kronos.web.model.TelemetryItemModels.TelemetryItemExporter;
import com.arrow.kronos.web.model.TelemetryItemModels.TelemetryItemModel;
import com.arrow.pegasus.client.api.ClientAccessKeyApi;
import com.arrow.pegasus.client.api.ClientApplicationApi;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.ConfigurationPropertyCategory;
import com.arrow.pegasus.data.event.Event;
import com.arrow.pegasus.data.event.EventBuilder;
import com.arrow.pegasus.data.event.EventParameter;
import com.arrow.pegasus.data.location.LastLocation;
import com.arrow.pegasus.data.location.LastLocationType;
import com.arrow.pegasus.data.location.LocationObjectType;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.service.LastLocationService;
import com.arrow.pegasus.util.CoreConfigurationPropertyUtil;
import com.arrow.pegasus.webapi.data.CoreSearchFilterModel;
import com.arrow.pegasus.webapi.data.DeviceTagModels.DeviceTagOption;
import com.arrow.pegasus.webapi.data.LastLocationModel;
import com.arrow.rhea.data.SoftwareRelease;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RestController
@RequestMapping("/api/kronos/device")
public class DeviceController extends BaseControllerAbstract {
	private static final String TELEMETRY_NAMES_SEPARATOR = ",";
	private static final long MAX_EXPORT_TIME_INTERVAL_MS = 7 * 24 * 3600 * 1000; // 7
																					// days
	private static final int EXPORT_PAGE_SIZE = 100;

	@Autowired
	private DeviceService deviceService;
	@Autowired
	private GatewayService gatewayService;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private TelemetryItemService telemetryItemService;
	@Autowired
	private LastTelemetryItemService lastTelemetryItemService;
	@Autowired
	private DeviceActionTypeService deviceActionTypeService;
	@Autowired
	private DeviceEventService deviceEventService;
	@Autowired
	private GatewayCommandService gatewayCommandService;
	@Autowired
	private SpringDataEsTelemetryItemRepository esTelemetryItemRepository;
	@Autowired
	private LastLocationService lastLocationService;
	@Autowired
	private ClientApplicationApi clientApplicationApi;
	@Autowired
	private ClientAccessKeyApi clientAccessKeyApi;
	@Autowired
	private DeviceStateService deviceStateService;
	@Autowired
	private DeviceStateTransService deviceStateTransService;
	@Autowired
	private ConfigBackupService configBackupService;
	@Autowired
	private SoftwareReleaseScheduleService softwareReleaseScheduleService;

	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public SearchResultModels.DeviceSearchResultModel list(@RequestBody DeviceSearchFilterModel searchFilter,
			@RequestParam(value = "bulkEdit", defaultValue = "false") boolean bulkEdit, HttpSession session) {

		// sorting & paging
		Sort sort = new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField());
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(), sort);

		// convert search filters to search params
		DeviceSearchParams params = new DeviceSearchParams();
		// system implied filters
		params.addApplicationIds(getApplicationId(session));
		if (searchFilter.getHid() != null && !searchFilter.getHid().equals("")) {
			params.addHids(searchFilter.getHid());
		}
		if (searchFilter.getUid() != null && !searchFilter.getUid().equals("")) {
			params.setUid(searchFilter.getUid());
		}
		// user defined filters
		params.addDeviceTypeIds(searchFilter.getDeviceTypeIds());
		params.addGatewayIds(searchFilter.getGatewayIds());
		if (hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			params.addUserIds(searchFilter.getUserIds());
		} else {
			// always filter by user devices only
			params.addUserIds(getAuthenticatedUser().getId());
		}
		params.addNodeIds(searchFilter.getNodeIds());
		params.setEnabled(searchFilter.isEnabled());

		// lookup
		Page<Device> devicePage = deviceService.getDeviceRepository().doFindDevices(pageRequest, params);

		// convert to visual model
		Page<DeviceModels.DeviceList> result = null;
		List<DeviceModels.DeviceList> deviceModels = new ArrayList<>();
		for (Device device : devicePage) {
			DeviceType type = null;
			String ownerName = null;
			String gatewayName = null;
			String nodeName = null;
			// lookup child objects from cache
			if (!StringUtils.isEmpty(device.getDeviceTypeId()))
				type = getKronosCache().findDeviceTypeById(device.getDeviceTypeId());
			if (!StringUtils.isEmpty(device.getUserId())) {
				User user = getCoreCacheService().findUserById(device.getUserId());
				if (user != null)
					ownerName = user.getContact().fullName();
			}
			if (!StringUtils.isEmpty(device.getGatewayId())) {
				Gateway gateway = getKronosCache().findGatewayById(device.getGatewayId());
				if (gateway != null)
					gatewayName = gateway.getName();
			}
			if (!StringUtils.isEmpty(device.getNodeId())) {
				Node node = getKronosCache().findNodeById(device.getNodeId());
				if (node != null)
					nodeName = node.getName();
			}
			deviceModels.add(new DeviceModels.DeviceList(device, type, gatewayName, ownerName, nodeName));
		}
		result = new PageImpl<>(deviceModels, pageRequest, devicePage.getTotalElements());

		List<String> documentIds = null;
		if (bulkEdit) {
			if (devicePage.getNumberOfElements() == devicePage.getTotalElements()) {
				// all devices matching the filter are in the current slice
				documentIds = devicePage.getContent().stream().map(device -> device.getId())
						.collect(Collectors.toList());
			} else {
				// find all deviceIds matching the filter
				documentIds = deviceService.getDeviceRepository().doFindDeviceIds(params, sort);
			}
		}

		return new DeviceSearchResultModel(result, searchFilter, documentIds);
	}

	@RequestMapping(value = "/filter", method = RequestMethod.GET)
	public DeviceSearchFilterOptions getFilterOptions(HttpSession session) {

		Application application = getApplication(session);
		User authenticatedUser = getAuthenticatedUser();

		List<DeviceType> deviceTypes;
		List<Gateway> gateways;
		List<User> users;
		List<Node> nodes;
		List<DeviceTagOption> tags = getAvailableTags(application);

		if (hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			deviceTypes = getAvailableDeviceTypes(application.getId(), AcnDeviceCategory.DEVICE, true);
			gateways = getAvailableGateways(application);
			users = getAvailableUsers(application, authenticatedUser);
			nodes = getAvailableNodes(application);
		} else {
			deviceTypes = getUsedDeviceTypes(application, authenticatedUser);
			gateways = getUsedGateways(application, authenticatedUser);
			users = getAuthenticatedUserOption(authenticatedUser);
			nodes = getUsedNodes(application, authenticatedUser);
		}

		DeviceSearchFilterOptions filterOptions = new DeviceSearchFilterOptions(deviceTypes, gateways, users, nodes,
				tags);

		return filterOptions;
	}

	@RequestMapping(value = "/{deviceId}", method = RequestMethod.GET)
	public DeviceDetailModel getDevice(@PathVariable String deviceId, HttpSession session) {
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		// make sure the user and device have the same application id
		Assert.isTrue(getApplicationId(session).equals(device.getApplicationId()),
				"user and device must have the same application id");
		// if user doesn't have permission to view all devices, he must be able
		// to see only own devices
		if (!hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			Assert.isTrue(getUserId().equals(device.getUserId()), "user must own the device");
		}

		return getDeviceDetails(device);
	}

	@RequestMapping(value = "/{deviceId}/pri", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public String getDevicePri(@PathVariable String deviceId, HttpSession session) {
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		// make sure the user and device have the same application id
		Assert.isTrue(getApplicationId(session).equals(device.getApplicationId()),
				"user and device must have the same application id");
		// if user doesn't have permission to view all devices, he must be able
		// to see only own devices
		if (!hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			Assert.isTrue(getUserId().equals(device.getUserId()), "user must own the device");
		}

		return device.getPri();
	}

	// TODO @PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE_LAST_LOCATION')")
	@RequestMapping(value = "/{deviceId}/lastLocation", method = RequestMethod.POST)
	public LastLocationModel updateLastLocation(@PathVariable String deviceId, @RequestBody LastLocationModel model,
			HttpSession session) {
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getObjectType(), "objectType is null");
		Assert.hasText(model.getObjectId(), "objectId is empty");
		Assert.notNull(model.getLatitude(), "latitude is null");
		Assert.notNull(model.getLongitude(), "longitude is null");

		String applicationId = getApplicationId(session);

		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");

		Application application = getCoreCacheService().findApplicationById(device.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(device.getApplicationId()),
				"user and gateway must have the same application id");

		String lastLocationId = lastLocationService.update(model.getObjectType(), model.getObjectId(),
				model.getLatitude(), model.getLongitude(), LastLocationType.STATIC, Instant.now());

		LastLocation lastLocation = lastLocationService.getLastLocationRepository().findById(lastLocationId)
				.orElse(null);
		Assert.notNull(lastLocation, "lastLocation is null; id: " + lastLocationId);

		return new LastLocationModel(lastLocation);
	}

	@RequestMapping(value = "/options", method = RequestMethod.GET)
	public DeviceDetailsOptions getEditDeviceOptions(@RequestParam(required = false) String deviceTypeId,
			HttpSession session) {
		Application application = getCoreCacheService().findApplicationById(getApplicationId(session));
		User authenticatedUser = getAuthenticatedUser();

		List<DeviceType> deviceTypes = getAvailableDeviceTypes(application.getId(), AcnDeviceCategory.DEVICE, true);
		List<Gateway> gateways = getAvailableGateways(application);
		List<User> users = getAvailableUsers(application, authenticatedUser);
		List<Node> nodes = getAvailableNodes(application);
		List<DeviceTagOption> tags = getAvailableTags(application);
		List<DeviceActionType> actionTypes = getAvailableActionTypes(application);

		List<SoftwareReleaseOption> softwareReleaseOptions = new ArrayList<>();
		if (!StringUtils.isEmpty(deviceTypeId)) {
			DeviceType deviceType = getKronosCache().findDeviceTypeById(deviceTypeId);
			Assert.notNull(deviceType, "deviceType is null");
			softwareReleaseOptions = getAvailableSoftwareReleases(deviceType.getRheaDeviceTypeId(), session);
		}

		return new DeviceDetailsOptions(deviceTypes, gateways, users, nodes, tags, actionTypes,
				DeviceEventStatus.values(), softwareReleaseOptions);
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public DeviceModels.DeviceUpsert device(HttpSession session) {
		Assert.notNull(session, "session is null");

		Device device = new Device();

		List<DeviceTypeModels.DeviceTypeOption> deviceTypeOptions = new ArrayList<>();
		for (DeviceType deviceType : getDeviceTypeService().getDeviceTypeRepository()
				.findByApplicationIdAndEnabled(getApplicationId(session), true)) {
			deviceTypeOptions.add(new DeviceTypeModels.DeviceTypeOption(deviceType));
		}

		return new DeviceModels.DeviceUpsert(new DeviceModels.DeviceModel(device), deviceTypeOptions);
	}

	@PreAuthorize("hasAuthority('KRONOS_CREATE_DEVICE')")
	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public DeviceModels.DeviceModel create(HttpSession session,
			@RequestBody DeviceModels.DeviceRegistrationModel model) {
		Assert.notNull(session, "session is null");
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getName(), "Device name is null");
		Assert.notNull(model.getUid(), "Device UID is null");
		Assert.notNull(model.getDeviceTypeId(), "Device type is null");
		Assert.notNull(model.getGateway(), "Gateway is null");
		Assert.notNull(model.getGateway().getId(), "Gateway id is null");

		String method = "create";
		logInfo(method, "...");

		Device device = new Device();
		device.setName(model.getName());
		device.setUid(model.getUid());
		device.setDeviceTypeId(model.getDeviceTypeId());
		device.setEnabled(model.isEnabled());
		device.setApplicationId(getApplicationId(session));
		device.setGatewayId(model.getGateway().getId());

		// properties
		device.setProperties(model.getProperties());

		// info
		device.setInfo(model.getInfo());

		if (model.getUser() != null) {
			device.setUserId(model.getUser().getId());
		} else {
			device.setUserId(null);
		}

		if (model.getNode() != null) {
			device.setNodeId(model.getNode().getId());
		} else {
			device.setNodeId(null);
		}

		if (model.getSoftwareRelease() != null) {
			device.setSoftwareReleaseId(model.getSoftwareRelease().getId());
		} else {
			device.setSoftwareReleaseId(null);
		}

		device = deviceService.create(device, getUserId());

		return new DeviceModels.DeviceModel(device);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE')")
	@RequestMapping(value = "/{deviceId}/settings", method = RequestMethod.PUT)
	public DeviceSettingsModel save(@PathVariable String deviceId, @RequestBody DeviceSettingsModel deviceSettingsModel,
			HttpSession session) {
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		// make sure the user and device have the same application id
		Assert.isTrue(getApplicationId(session).equals(device.getApplicationId()),
				"user and device must have the same application id");
		// if user doesn't have permission to view all devices, he must be able
		// to edit only own devices
		if (!hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			Assert.isTrue(getUserId().equals(device.getUserId()), "user must own the device");
		}

		// general
		device.setName(deviceSettingsModel.getName());
		// if (deviceDetailModel.getType() != null) {
		// device.setDeviceTypeId(deviceDetailModel.getType().getId());
		// }
		device.setDeviceTypeId(deviceSettingsModel.getDeviceTypeId());
		if (deviceSettingsModel.getUser() != null) {
			device.setUserId(deviceSettingsModel.getUser().getId());
		} else {
			device.setUserId(null);
		}
		if (deviceSettingsModel.getNode() != null) {
			device.setNodeId(deviceSettingsModel.getNode().getId());
		} else {
			device.setNodeId(null);
		}

		// settings
		Map<String, String> newSettings = deviceSettingsModel.getProperties();
		List<String> changedSettingNames = new ArrayList<>();
		for (Map.Entry<String, String> entry : device.getProperties().entrySet()) {
			String newValue = newSettings.get(entry.getKey());
			if (newValue != null && !newValue.equals(entry.getValue())) {
				changedSettingNames.add(entry.getKey());
			}
		}
		device.setProperties(newSettings);

		if (deviceSettingsModel.getSoftwareRelease() != null) {
			device.setSoftwareReleaseId(deviceSettingsModel.getSoftwareRelease().getId());
		} else {
			device.setSoftwareReleaseId(null);
		}

		device = deviceService.update(device, getUserId());

		// send updated settings to the gateway
		Event event = null;
		if (device.getGatewayId() != null && changedSettingNames.size() > 0) {
			EventBuilder builder = EventBuilder.create().applicationId(device.getApplicationId())
					.name(AcnEventNames.ServerToGateway.DEVICE_PROPERTY_CHANGE)
					.parameter(EventParameter.InString("deviceHid", device.getHid()));
			for (String settingName : changedSettingNames) {
				// TODO no setting type info, always use String
				builder.parameter(EventParameter.InString(settingName, newSettings.get(settingName)));
			}
			event = gatewayCommandService.sendEvent(builder.build(), device.getGatewayId(), getUserId());
		}

		DeviceType deviceType = getKronosCache().findDeviceTypeById(device.getDeviceTypeId());
		Assert.notNull(deviceType, "deviceType is null");

		User user = null;
		if (!StringUtils.isEmpty(device.getUserId())) {
			user = getCoreCacheService().findUserById(device.getUserId());
		}

		Node node = null;
		if (!StringUtils.isEmpty(device.getNodeId())) {
			node = nodeService.getNodeRepository().findById(device.getNodeId()).orElse(null);
		}

		SoftwareRelease softwareRelease = null;
		if (!StringUtils.isEmpty(device.getSoftwareReleaseId())) {
			softwareRelease = getRheaCacheService().findSoftwareReleaseById(device.getSoftwareReleaseId());
		}

		return new DeviceSettingsModel(device, deviceType, user, node, event,
				getSoftwareReleaseOption(softwareRelease));
	}

	@PreAuthorize("hasAuthority('KRONOS_DELETE_DEVICE')")
	@RequestMapping(value = "/{deviceId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String deviceId, HttpSession session) {
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		// make sure the user and device have the same application id
		Assert.isTrue(Objects.equals(getApplicationId(session), device.getApplicationId()),
				"user and device must have the same application id");
		// if user doesn't have permission to view all devices, he must be able
		// to edit only own devices
		if (!hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			Assert.isTrue(Objects.equals(getUserId(), device.getUserId()), "user must own the device");
		}

		// List<SoftwareReleaseSchedule> schedules =
		// softwareReleaseScheduleService.getSoftwareReleaseScheduleRepository()
		// .findSoftwareReleaseSchedulesByObjectIds(deviceId);
		// Assert.isTrue(schedules.isEmpty(),
		// String.format("Asset %s is associated with a system job and can't be
		// deleted", device.getName()));

		deviceService.delete(device, getUserId());
	}

	@PreAuthorize("hasAuthority('KRONOS_DELETE_DEVICE')")
	@RequestMapping(value = "/bulkDelete", method = RequestMethod.POST)
	public BulkActionResultModel bulkDelete(@RequestBody String[] deviceIds, HttpSession session) {
		String method = "bulkDelete";

		String applicationId = getApplicationId(session);
		String authenticatedUserId = getUserId();

		Application application = getCoreCacheService().findApplicationById(applicationId);
		checkEnabled(application, "application");

		int total = deviceIds.length, numDeleted = 0, numFailed = 0;
		boolean canViewAllDevices = hasAuthority("KRONOS_VIEW_ALL_DEVICES");

		List<SoftwareReleaseSchedule> schedules = softwareReleaseScheduleService.getSoftwareReleaseScheduleRepository()
				.findSoftwareReleaseSchedulesByObjectIds(deviceIds);
		Set<String> scheduledDevices = schedules.stream().map(SoftwareReleaseSchedule::getObjectIds)
				.flatMap(Collection::stream).collect(Collectors.toSet());

		for (String deviceId : deviceIds) {
			try {
				Device device = getKronosCache().findDeviceById(deviceId);
				if (device != null) {
					// user and device must have the same application id
					Assert.isTrue(StringUtils.equals(applicationId, device.getApplicationId()),
							"user and device must have the same application id");
					if (!canViewAllDevices) {
						Assert.isTrue(StringUtils.equals(authenticatedUserId, device.getUserId()),
								"user must own the device");
					}
					Assert.isTrue(!scheduledDevices.contains(deviceId), String
							.format("Asset %s is associated with a system job and can't be deleted", device.getName()));
					deviceService.delete(device, authenticatedUserId);
				}
				numDeleted++;
			} catch (Exception e) {
				logError(method, "Failed to delete device id=" + deviceId, e);
				numFailed++;
			}
			logInfo(method, "deleted %d devices of total %d; failed to delete %d devices", numDeleted, total,
					numFailed);
		}

		return new BulkActionResultModel(total, numDeleted, numFailed);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE')")
	@RequestMapping(value = "/bulkEditOptions", method = RequestMethod.GET)
	public BulkEditActionModels.AssetsBulkEditModel getBulkEditOptions(HttpSession session) {
		Application application = getApplication(session);
		User authenticatedUser = getAuthenticatedUser();

		List<User> users = getAvailableUsers(application, authenticatedUser);
		List<Node> nodes = getAvailableNodes(application);

		BulkEditActionModels.AssetsBulkEditModel assetsBulkEditModel = new BulkEditActionModels.AssetsBulkEditModel();
		assetsBulkEditModel.setGroupBulkEditField(BulkEditActionModels.GroupBulkEditField.build(nodes));
		assetsBulkEditModel.setOwnerBulkEditField(BulkEditActionModels.OwnerBulkEditField.build(users));
		assetsBulkEditModel.setEnabledBulkEditField(BulkEditActionModels.EnabledBulkEditField.build());

		return assetsBulkEditModel;
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE')")
	@RequestMapping(value = "/bulkEdit", method = RequestMethod.POST)
	public BulkActionResultModel bulkEdit(@RequestBody BulkEditActionModels.DeviceBulkEdit deviceBulkEdit,
			HttpSession session) {
		String method = "bulkEdit";

		Assert.notNull(deviceBulkEdit, "deviceBulkEdit is null");
		Assert.notNull(deviceBulkEdit.getIds(), "ids is null");
		Assert.notNull(deviceBulkEdit.getEditModel(), "deviceBulkEdit model is null");
		Assert.notNull(deviceBulkEdit.getEditModel().getEnabledBulkEditField(), "Enabled field is null");
		Assert.notNull(deviceBulkEdit.getEditModel().getGroupBulkEditField(), "Group field is empty");
		Assert.notNull(deviceBulkEdit.getEditModel().getOwnerBulkEditField(), "Owner field is empty");

		String[] devicesIds = deviceBulkEdit.getIds();

		String applicationId = getApplicationId(session);
		String authenticatedUserId = getUserId();

		Application application = getCoreCacheService().findApplicationById(applicationId);
		checkEnabled(application, "application");

		int total = devicesIds.length;
		int numUpdated = 0;
		int numFailed = 0;

		// analyze bulk edit data
		BulkEditActionModels.BulkEditField groupField = deviceBulkEdit.getEditModel().getGroupBulkEditField();
		boolean editGroup = groupField.isEdit();
		String groupChoice = groupField.getChoice();

		BulkEditActionModels.BulkEditField ownerField = deviceBulkEdit.getEditModel().getOwnerBulkEditField();
		boolean editOwner = ownerField.isEdit();
		String ownerChoice = ownerField.getChoice();

		BulkEditActionModels.BulkEditField enabledField = deviceBulkEdit.getEditModel().getEnabledBulkEditField();
		boolean editEnabled = enabledField.isEdit();
		boolean enabledChoice = enabledField.getChoice().toLowerCase().equals("true");

		if (editGroup || editOwner || editEnabled) {
			for (String deviceId : devicesIds) {
				try {
					Device device = getKronosCache().findDeviceById(deviceId);
					if (device != null) {
						Assert.isTrue(StringUtils.equals(applicationId, device.getApplicationId()),
								"user and device must have the same application id");

						if (editGroup) {
							// edit group
							device.setNodeId(groupChoice);
						}

						if (editOwner) {
							// edit owner
							device.setUserId(ownerChoice);
						}

						if (editEnabled) {
							// edit enabled
							device.setEnabled(enabledChoice);
						}

						deviceService.update(device, authenticatedUserId);
					}
					numUpdated++;
				} catch (Exception e) {
					logError(method, "Failed to update device id = " + deviceId, e);
					numFailed++;
				}
			}
		}

		logInfo(method, "updated %d devices of total %d; failed to update %d gateways", numUpdated, total, numFailed);

		return new BulkActionResultModel(total, numUpdated, numFailed);
	}

	@PreAuthorize("hasAuthority('KRONOS_ADD_DEVICE_TAG') or hasAuthority('KRONOS_REMOVE_DEVICE_TAG')")
	@RequestMapping(value = "/{deviceId}/tags", method = RequestMethod.PUT)
	public Set<String> saveTags(@PathVariable String deviceId, @RequestBody HashSet<String> deviceTags,
			HttpSession session) {
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		// make sure the user and device have the same application id
		Assert.isTrue(getApplicationId(session).equals(device.getApplicationId()),
				"user and device must have the same application id");
		// if user doesn't have permission to view all devices, he must be able
		// to edit only own devices
		if (!hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			Assert.isTrue(getUserId().equals(device.getUserId()), "user must own the device");
		}

		// tags
		if (hasAuthority("KRONOS_ADD_DEVICE_TAG")) {
			device.getTags().addAll(deviceTags);
		}
		if (hasAuthority("KRONOS_REMOVE_DEVICE_TAG")) {
			device.getTags().retainAll(deviceTags);
		}

		device = deviceService.update(device, getUserId());

		return device.getTags();
	}

	@PreAuthorize("hasAuthority('KRONOS_CREATE_DEVICE_TAG')")
	@RequestMapping(value = "/tags", method = RequestMethod.POST)
	public List<DeviceTagOption> addTag(@RequestBody String tagName, HttpSession session) {
		// make sure the tagName doesn't contain the separator
		tagName = tagName.replace(",", "");

		Assert.hasLength(tagName, "tag name cannot be empty");

		Application application = getCoreCacheService().findApplicationById(getApplicationId(session));
		addDeviceTag(application, tagName);

		// persist
		application = clientApplicationApi.update(application, getUserId());

		return getAvailableTags(application);
	}

	@PreAuthorize("hasAuthority('KRONOS_CREATE_DEVICE_ACTION')")
	@RequestMapping(value = "/{deviceId}/action", method = RequestMethod.POST)
	public List<DeviceActionModel> createDeviceAction(@PathVariable String deviceId,
			@RequestBody DeviceActionModel deviceActionModel, HttpSession session) {
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		// make sure the user and device have the same application id
		Assert.isTrue(getApplicationId(session).equals(device.getApplicationId()),
				"user and device must have the same application id");
		// if user doesn't have permission to view all devices, he must be able
		// to edit only own devices
		if (!hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			Assert.isTrue(getUserId().equals(device.getUserId()), "user must own the device");
		}

		DeviceAction deviceAction = deviceActionModel.populate();

		device = deviceService.createDeviceAction(device, deviceAction, getUserId());

		return getKronosModelUtil().populateDeviceActionModels(device.getActions());
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE_ACTION')")
	@RequestMapping(value = "/{deviceId}/action/{actionIndex}", method = RequestMethod.PUT)
	public List<DeviceActionModel> editDeviceAction(@PathVariable String deviceId, @PathVariable int actionIndex,
			@RequestBody DeviceActionModel deviceActionModel, HttpSession session) {
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		// make sure the user and device have the same application id
		Assert.isTrue(getApplicationId(session).equals(device.getApplicationId()),
				"user and device must have the same application id");
		// if user doesn't have permission to view all devices, he must be able
		// to edit only own devices
		if (!hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			Assert.isTrue(getUserId().equals(device.getUserId()), "user must own the device");
		}

		DeviceAction deviceAction = device.getActions().get(actionIndex);

		deviceAction = deviceActionModel.populate(deviceAction);

		device = deviceService.updateDeviceAction(device, actionIndex, deviceAction, getUserId());

		return getKronosModelUtil().populateDeviceActionModels(device.getActions());
	}

	@PreAuthorize("hasAuthority('KRONOS_DELETE_DEVICE_ACTION')")
	@RequestMapping(value = "/{deviceId}/delete-actions", method = RequestMethod.POST)
	public List<DeviceActionModel> deleteDeviceActions(@PathVariable String deviceId,
			@RequestBody List<Integer> actionIndices, HttpSession session) {
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		// make sure the user and device have the same application id
		Assert.isTrue(getApplicationId(session).equals(device.getApplicationId()),
				"user and device must have the same application id");
		// if user doesn't have permission to view all devices, he must be able
		// to edit only own devices
		if (!hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			Assert.isTrue(getUserId().equals(device.getUserId()), "user must own the device");
		}

		device = deviceService.deleteDeviceActions(device, actionIndices, getUserId());

		return getKronosModelUtil().populateDeviceActionModels(device.getActions());
	}

	@RequestMapping(value = "/{deviceId}/events", method = RequestMethod.POST)
	public DeviceEventSearchResultModel getDeviceEvents(@PathVariable String deviceId,
			@RequestBody DeviceEventSearchFilterModel searchFilter, HttpSession session) {
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		// make sure the user and device have the same application id
		Assert.isTrue(getApplicationId(session).equals(device.getApplicationId()),
				"user and device must have the same application id");
		// if user doesn't have permission to view all devices, he must be able
		// to see only own devices
		if (!hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			Assert.isTrue(getUserId().equals(device.getUserId()), "user must own the device");
		}

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		// convert search filters to search params
		DeviceEventSearchParams params = new DeviceEventSearchParams();
		params.addDeviceIds(deviceId);
		// user defined filters
		params.addDeviceActionTypeIds(searchFilter.getDeviceActionTypeIds());
		params.addStatuses(searchFilter.getStatuses());
		Instant createdDateFrom = searchFilter.getCreatedDateFrom() != null
				? Instant.ofEpochSecond(searchFilter.getCreatedDateFrom())
				: null;
		params.setCreatedDateFrom(createdDateFrom);
		Instant createdDateTo = searchFilter.getCreatedDateTo() != null
				? Instant.ofEpochSecond(searchFilter.getCreatedDateTo())
				: null;
		params.setCreatedDateTo(createdDateTo);

		// lookup
		Page<DeviceEvent> deviceEventPage = deviceEventService.getDeviceEventRepository().findDeviceEvents(pageRequest,
				params);

		// convert to visual model
		List<DeviceEventList> deviceEventList = new ArrayList<>();
		for (DeviceEvent deviceEvent : deviceEventPage) {
			DeviceActionType deviceActionType = null;
			// lookup child objects from cache
			if (!StringUtils.isEmpty(deviceEvent.getDeviceActionTypeId()))
				deviceActionType = getKronosCache().findDeviceActionTypeById(deviceEvent.getDeviceActionTypeId());
			deviceEventList.add(new DeviceEventList(deviceEvent, deviceActionType));
		}
		Page<DeviceEventList> result = new PageImpl<>(deviceEventList, pageRequest, deviceEventPage.getTotalElements());

		return new DeviceEventSearchResultModel(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE_EVENT')")
	@RequestMapping(value = "/{deviceId}/close-events", method = RequestMethod.POST)
	public List<DeviceEventList> closeDeviceEvents(@PathVariable String deviceId,
			@RequestBody List<String> deviceEventIds, HttpSession session) {
		String authenticatedUserId = getUserId();
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		// make sure the user and device have the same application id
		Assert.isTrue(getApplicationId(session).equals(device.getApplicationId()),
				"user and device must have the same application id");
		// if user doesn't have permission to view all devices, he must be able
		// to see only own devices
		if (!hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			Assert.isTrue(getUserId().equals(device.getUserId()), "user must own the device");
		}

		Iterable<DeviceEvent> deviceEvents = deviceEventService.getDeviceEventRepository().findAllById(deviceEventIds);
		List<DeviceEventList> result = new ArrayList<>();

		for (DeviceEvent deviceEvent : deviceEvents) {
			if (deviceId.equals(deviceEvent.getDeviceId()) && deviceEvent.getStatus() == DeviceEventStatus.Open) {
				deviceEvent.setStatus(DeviceEventStatus.Closed);
				deviceEvent = deviceEventService.update(deviceEvent, authenticatedUserId);
				deviceEvent = deviceEventService.populate(deviceEvent);
				result.add(new DeviceEventList(deviceEvent, deviceEvent.getRefDeviceActionType()));
			}
		}

		return result;
	}

	@RequestMapping(value = "/{deviceId}/telemetry/{telemetryName}", method = RequestMethod.GET)
	public TelemetryItemChartModel[] getTelemetryData(@PathVariable String deviceId, @PathVariable String telemetryName,
			@RequestParam(value = "from", required = true) long from,
			@RequestParam(value = "to", required = true) long to, HttpSession session) {
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		// make sure the user and device have the same application id
		Assert.isTrue(getApplicationId(session).equals(device.getApplicationId()),
				"user and device must have the same application id");
		// if user doesn't have permission to view all devices, he must be able
		// to edit only own devices
		if (!hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			Assert.isTrue(getUserId().equals(device.getUserId()), "user must own the device");
		}
		Assert.isTrue(from <= to, "from must be not greater than to");

		List<EsTelemetryItem> telemetryItemHits = esTelemetryItemRepository.findAllTelemetryItems(deviceId,
				new String[] { telemetryName }, from, to, SortOrder.ASC);

		return TelemetryItemChartModel.getTelemetryItemChartModels(telemetryItemHits);
	}

	@PreAuthorize("hasAuthority('KRONOS_DELETE_DEVICE_TELEMETRY')")
	@RequestMapping(value = "/{deviceId}/telemetry/bulkDelete", method = RequestMethod.PUT)
	public BulkActionResultModel bulkDeleteLastTelemetries(@PathVariable String deviceId,
			@RequestBody TelemetryItemModels.TelemetryItemsDelete model, HttpSession session) {

		String method = "telemetryBulkDelete";

		Assert.notNull(model.getLastTelemetryIds(), "List telemetryIds is null");

		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		DeviceType deviceType = getKronosCache().findDeviceTypeById(device.getDeviceTypeId());
		Assert.notNull(deviceType, "deviceType is null");
		List<DeviceTelemetry> deviceTelemetrylist = new ArrayList<>(deviceType.getTelemetries());

		Assert.isTrue(getApplicationId(session).equals(device.getApplicationId()),
				"user and device must have the same application id");

		int total = model.getLastTelemetryIds().size(), numDeleted = 0, numFailed = 0;

		for (String telemetryItemId : model.getLastTelemetryIds()) {

			try {
				LastTelemetryItem lastTelemetryItem = lastTelemetryItemService.getLastTelemetryItemRepository()
						.findById(telemetryItemId).orElse(null);

				if (lastTelemetryItem != null) {

					// user and device must have the same application id
					Assert.isTrue(StringUtils.equals(lastTelemetryItem.getDeviceId(), deviceId),
							"deviceId of telemetryItem and device must have the same id");

					lastTelemetryItemService.getLastTelemetryItemRepository().deleteById(lastTelemetryItem.getId());

					if (model.isRemoveTelemetryDefinition()) {
						deviceTelemetrylist.stream()
								.filter(dTelemetry -> dTelemetry.getName().equals(lastTelemetryItem.getName()))
								.forEach(dTelemetry -> deviceType.getTelemetries().remove(dTelemetry));
					}

					numDeleted++;
				} else {
					numFailed++;
				}
			} catch (Exception e) {
				logError(method, "Failed to delete telemetryItem id=" + telemetryItemId, e);
				numFailed++;
			}
			logInfo(method, "deleted %d telemetryItems of total %d; failed to delete %d telemetryItems", numDeleted,
					total, numFailed);
		}

		// remove telemetry definitions from device type
		if (model.isRemoveTelemetryDefinition()) {
			try {
				getDeviceTypeService().update(deviceType, getUserId());
			} catch (Exception e) {
				logError(method, "Failed to delete telemetry definitions from device type", e);
			}
		}

		return new BulkActionResultModel(total, numDeleted, numFailed);
	}

	@RequestMapping(value = "/{deviceId}/export", method = RequestMethod.GET)
	public void exportTelemetry(@PathVariable String deviceId,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "sort", required = true) String sort,
			@RequestParam(value = "names", required = true) String names,
			@RequestParam(value = "from", required = true) long from,
			@RequestParam(value = "to", required = true) long to, HttpSession session, HttpServletResponse response)
			throws IOException {
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		// make sure the user and device have the same application id
		Assert.isTrue(getApplicationId(session).equals(device.getApplicationId()),
				"user and device must have the same application id");
		if (!hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			Assert.isTrue(getUserId().equals(device.getUserId()), "user must own the device");
		}
		Assert.isTrue(from <= to, "from must be not greater than to");
		Assert.isTrue((to - from) <= MAX_EXPORT_TIME_INTERVAL_MS, "max export time interval exceeded");
		Assert.notNull(names, "telemetry names are not defined");
		String[] telemetryNames = names.split(TELEMETRY_NAMES_SEPARATOR);
		Assert.notEmpty(telemetryNames, "telemetry names are empty");

		TelemetryItemExporter exporter = new TelemetryItemExporter(type);
		// set content type
		response.setHeader("Content-Type", exporter.contentType());
		// set file name as (deviceName|deviceId).(json|csv)
		String filename = StringUtils.trim(device.getName());
		filename = (StringUtils.isNotBlank(filename) ? filename : deviceId) + "." + exporter.fileExtension();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		// send headers ASAP to let browser start downloading
		response.flushBuffer();

		// write data
		ServletOutputStream os = response.getOutputStream();
		os.print(exporter.header());

		Pageable pageRequest = PageRequest.of(0, EXPORT_PAGE_SIZE, new Sort(Direction.fromString(sort), "timestamp"));
		TelemetryItemSearchParams params = new TelemetryItemSearchParams();
		params.setDeviceIds(deviceId);
		params.setNames(telemetryNames);
		params.setFromTimestamp(from);
		params.setToTimestamp(to);

		while (true) {
			Page<TelemetryItem> page = telemetryItemService.getTelemetryItemRepository()
					.doFindTelemetryItems(pageRequest, params);
			for (TelemetryItem item : page) {
				os.print(exporter.serialize(item));
			}
			if (page.isLast())
				break;
			response.flushBuffer();
			pageRequest = pageRequest.next();
		}
		os.print(exporter.footer());
		response.flushBuffer();
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE')")
	@RequestMapping(value = "/{deviceId}/enable", method = RequestMethod.POST)
	public boolean enableDevice(@PathVariable String deviceId, HttpSession session) {
		String applicationId = getApplicationId(session);
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		device = deviceService.populate(device);

		Application application = getCoreCacheService().findApplicationById(device.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and device have the same application id
		Assert.isTrue(applicationId.equals(device.getApplicationId()),
				"user and device must have the same application id");

		return deviceService.enable(device.getId(), getUserId()).isEnabled();
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_DEVICE')")
	@RequestMapping(value = "/{deviceId}/disable", method = RequestMethod.POST)
	public boolean disableDevice(@PathVariable String deviceId, HttpSession session) {
		String applicationId = getApplicationId(session);
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		device = deviceService.populate(device);

		Application application = getCoreCacheService().findApplicationById(device.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and device have the same application id
		Assert.isTrue(applicationId.equals(device.getApplicationId()),
				"user and device must have the same application id");

		return deviceService.disable(device.getId(), getUserId()).isEnabled();
	}

	@PreAuthorize("hasAuthority('KRONOS_SEND_COMMAND_TO_DEVICE')")
	@RequestMapping(produces = MediaType.TEXT_PLAIN_VALUE, value = "/{deviceId}/start", method = RequestMethod.POST)
	public String startDevice(@PathVariable String deviceId, HttpSession session) {
		String applicationId = getApplicationId(session);
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");

		Application application = getCoreCacheService().findApplicationById(device.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and device have the same application id
		Assert.isTrue(applicationId.equals(device.getApplicationId()),
				"user and device must have the same application id");

		EventBuilder builder = EventBuilder.create().applicationId(application.getId())
				.name(AcnEventNames.ServerToGateway.DEVICE_START)
				.parameter(EventParameter.InString("deviceHid", device.getHid()));
		Event event = gatewayCommandService.sendEvent(builder.build(), device.getGatewayId(), getUserId());

		logInfo("startDevice", "event has been sent to deviceId=" + device.getId() + ", eventId=" + event.getId());

		return event.getId();
	}

	@PreAuthorize("hasAuthority('KRONOS_SEND_COMMAND_TO_DEVICE')")
	@RequestMapping(produces = MediaType.TEXT_PLAIN_VALUE, value = "/{deviceId}/stop", method = RequestMethod.POST)
	public String stopDevice(@PathVariable String deviceId, HttpSession session) {
		String applicationId = getApplicationId(session);
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");

		Application application = getCoreCacheService().findApplicationById(device.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and device have the same application id
		Assert.isTrue(applicationId.equals(device.getApplicationId()),
				"user and device must have the same application id");

		EventBuilder builder = EventBuilder.create().applicationId(application.getId())
				.name(AcnEventNames.ServerToGateway.DEVICE_STOP)
				.parameter(EventParameter.InString("deviceHid", device.getHid()));
		Event event = gatewayCommandService.sendEvent(builder.build(), device.getGatewayId(), getUserId());

		logInfo("stopDevice", "event has been sent to deviceId=" + device.getId() + ", eventId=" + event.getId());

		return event.getId();
	}

	@PreAuthorize("hasAuthority('KRONOS_SEND_COMMAND_TO_DEVICE')")
	@RequestMapping(produces = MediaType.TEXT_PLAIN_VALUE, value = "/{deviceId}/command", method = RequestMethod.POST)
	public String sendDeviceCommand(@PathVariable String deviceId,
			@RequestBody DeviceModels.DeviceCommandModel commandModel, HttpSession session) {
		String applicationId = getApplicationId(session);
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");

		Application application = getCoreCacheService().findApplicationById(device.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and device have the same application id
		Assert.isTrue(applicationId.equals(device.getApplicationId()),
				"user and device must have the same application id");

		String payload = validateJson(commandModel.getPayload());
		Assert.notNull(payload, "Invalid JSON");

		EventBuilder builder = EventBuilder.create().applicationId(application.getId())
				.name(AcnEventNames.ServerToGateway.DEVICE_COMMAND)
				.parameter(EventParameter.InString("deviceHid", device.getHid()))
				.parameter(EventParameter.InString("command", commandModel.getCommand()))
				.parameter(EventParameter.InString("payload", payload));

		Gateway gateway = getKronosCache().findGatewayById(device.getGatewayId());
		Assert.notNull(gateway, "gateway is not found");
		AccessKey gatewayOwnerKey = clientAccessKeyApi.findOwnerKey(gateway.getPri());
		Assert.notNull(gatewayOwnerKey, "gateway owner is not found");

		Event event = gatewayCommandService.sendEvent(builder.build(), device.getGatewayId(), gatewayOwnerKey,
				getUserId(), commandModel.getExpiration());

		logInfo("sendDeviceCommand", "command " + commandModel.getCommand() + " has been sent to deviceId="
				+ device.getId() + ", eventId=" + event.getId());

		return event.getId();
	}

	@PreAuthorize("hasAuthority('KRONOS_VIEW_DEVICE_AUDIT_LOGS')")
	@RequestMapping(value = "/{deviceId}/logs", method = RequestMethod.POST)
	public AuditLogSearchResultModel listDeviceLogs(@PathVariable String deviceId,
			@RequestBody AuditLogSearchFilterModel searchFilter, HttpSession session) {
		String applicationId = getApplicationId(session);
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");

		Application application = getCoreCacheService().findApplicationById(device.getApplicationId());
		checkEnabled(application, "application");

		Assert.isTrue(applicationId.equals(device.getApplicationId()),
				"user and device must have the same application id");

		return findAuditLogs(deviceId, applicationId, searchFilter);
	}

	@PreAuthorize("hasAuthority('KRONOS_VIEW_DEVICE_AUDIT_LOGS')")
	@RequestMapping(value = "/log/options", method = RequestMethod.GET)
	public AuditLogSearchFilterOptions getDeviceLogOptions(HttpSession session) {
		Application application = getApplication(session);
		User authenticatedUser = getAuthenticatedUser();

		List<User> users = getAvailableUsers(application, authenticatedUser);

		String[] types = { KronosAuditLog.Device.CreateDevice, KronosAuditLog.Device.CreateDeviceAction,
				KronosAuditLog.Device.DisableDevice, KronosAuditLog.Device.EnableDevice,
				KronosAuditLog.Device.UpdateDevice, KronosAuditLog.Device.UpdateDeviceAction,
				KronosAuditLog.Device.DeleteDeviceActions, KronosAuditLog.Device.SendCommand,
				KronosAuditLog.SoftwareReleaseTrans.CreateSoftwareReleaseTrans,
				KronosAuditLog.SoftwareReleaseTrans.UpdateSoftwareReleaseTrans };
		return new AuditLogSearchFilterOptions(users, types);
	}

	@PreAuthorize("hasAuthority('KRONOS_VIEW_DEVICE_AUDIT_LOGS')")
	@RequestMapping(value = "/log/{auditLogId}", method = RequestMethod.GET)
	public AuditLogDetails getDeviceLog(@PathVariable String auditLogId, HttpSession session) {
		return findAuditLog(auditLogId, getApplicationId(session));
	}

	@PreAuthorize("hasAuthority('KRONOS_READ_DEVICE_STATE')")
	@RequestMapping(value = "/{deviceId}/state", method = RequestMethod.GET)
	public Map<String, DeviceStateValue> getDeviceState(@PathVariable String deviceId, HttpSession session) {
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		// make sure the user and device have the same application id
		Assert.isTrue(getApplicationId(session).equals(device.getApplicationId()),
				"user and device must have the same application id");
		// if user doesn't have permission to view all devices, he must be able
		// to see only own devices
		if (!hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			Assert.isTrue(getUserId().equals(device.getUserId()), "user must own the device");
		}

		DeviceState deviceState = deviceStateService.getDeviceStateRepository()
				.findByApplicationIdAndDeviceId(device.getApplicationId(), device.getId());

		return deviceState != null ? deviceState.getStates() : null;
	}

	@PreAuthorize("hasAuthority('KRONOS_UPDATE_DEVICE_STATE')")
	@RequestMapping(produces = MediaType.TEXT_PLAIN_VALUE, value = "/{deviceId}/state", method = RequestMethod.POST)
	public String changeDeviceState(@PathVariable String deviceId, @RequestBody Map<String, String> model,
			HttpSession session) {
		String method = "changeDeviceState";
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		// make sure the user and device have the same application id
		Assert.isTrue(getApplicationId(session).equals(device.getApplicationId()),
				"user and device must have the same application id");
		// if user doesn't have permission to view all devices, he must be able
		// to see only own devices
		if (!hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			Assert.isTrue(getUserId().equals(device.getUserId()), "user must own the device");
		}

		Gateway gateway = getKronosCache().findGatewayById(device.getGatewayId());
		Assert.notNull(gateway, "gateway is null");

		DeviceStateTrans deviceStateTrans = new DeviceStateTrans();
		deviceStateTrans.setApplicationId(device.getApplicationId());
		deviceStateTrans.setDeviceId(device.getId());

		Map<String, DeviceStateValue> states = new HashMap<>();
		Instant now = Instant.now();
		for (Entry<String, String> entry : model.entrySet()) {
			DeviceStateValue deviceStateValue = new DeviceStateValue();
			deviceStateValue.setValue(entry.getValue());
			deviceStateValue.setTimestamp(now);
			states.put(entry.getKey(), deviceStateValue);
		}
		deviceStateTrans.setStates(states);

		deviceStateTrans.setStatus(Status.PENDING);
		deviceStateTrans.setType(Type.REQUEST);

		// persist
		deviceStateTrans = deviceStateTransService.create(deviceStateTrans, getUserId());

		// send event
		String payload = JsonUtils.toJson(deviceStateTrans.getStates());
		EventBuilder eventBuilder = EventBuilder.create().applicationId(device.getApplicationId())
				.name(AcnEventNames.ServerToGateway.DEVICE_STATE_REQUEST)
				.parameter(EventParameter.InString("deviceHid", device.getHid()))
				.parameter(EventParameter.InString("transHid", deviceStateTrans.getHid()))
				.parameter(EventParameter.InString("payload", payload));
		Event event = gatewayCommandService.sendEvent(eventBuilder.build(), gateway.getId(), getUserId());
		logInfo(method, "event has been sent, deviceId=%s, eventId=%s", device.getId(), event.getId());

		return event.getId();
	}

	// all enabled gateways for the application
	private List<Gateway> getAvailableGateways(Application application) {
		return gatewayService.getGatewayRepository().findAllByApplicationIdAndEnabled(application.getId(), true);
	}

	// all configured tags
	private List<DeviceTagOption> getAvailableTags(Application application) {
		List<DeviceTagOption> tags = new ArrayList<>();
		ConfigurationProperty deviceTags = getCoreConfigurationPropertyUtil().getDeviceTags(application);
		if (deviceTags != null && !StringUtils.isEmpty(deviceTags.strValue()))
			for (String tag : deviceTags.strValue().split(","))
				tags.add(new DeviceTagOption(tag));
		tags.sort(Comparator.comparing(DeviceTagOption::getName, String.CASE_INSENSITIVE_ORDER));
		return tags;
	}

	// all action types
	private List<DeviceActionType> getAvailableActionTypes(Application application) {
		return deviceActionTypeService.getDeviceActionTypeRepository().findByApplicationId(application.getId());
	}

	// only device types of user's devices
	private List<DeviceType> getUsedDeviceTypes(Application application, User authenticatedUser) {
		List<String> deviceTypeIds = deviceService.getDeviceRepository()
				.doFindAggregatedDeviceTypeIds(application.getId(), authenticatedUser.getId());
		return getDeviceTypeService().getDeviceTypeRepository().doFindAllByIdsApplicationIdAndEnabled(deviceTypeIds,
				application.getId(), true);
	}

	// only gateways of user's devices
	private List<Gateway> getUsedGateways(Application application, User authenticatedUser) {
		List<String> gatewayIds = deviceService.getDeviceRepository().doFindAggregatedGatewayIds(application.getId(),
				authenticatedUser.getId());
		return gatewayService.getGatewayRepository().doFindByApplicationIdAndGatewayIds(application.getId(),
				gatewayIds.toArray(new String[gatewayIds.size()]));
	}

	// only nodes of user's devices
	private List<Node> getUsedNodes(Application application, User authenticatedUser) {
		List<String> nodeIds = deviceService.getDeviceRepository().doFindAggregatedNodeIds(application.getId(),
				authenticatedUser.getId());
		return nodeService.getNodeRepository().doFindAllByIdsApplicationIdAndEnabled(nodeIds, application.getId(),
				true);
	}

	// limit the list of options for user filter to the currently logged in user
	private List<User> getAuthenticatedUserOption(User authenticatedUser) {
		List<User> users = new ArrayList<>(1);
		users.add(authenticatedUser);
		return users;
	}

	private DeviceDetailModel getDeviceDetails(Device device) {
		Assert.notNull(device, "device is null");

		DeviceType deviceType = getKronosCache().findDeviceTypeById(device.getDeviceTypeId());
		Assert.notNull(deviceType, "deviceType is null");

		Gateway gateway = null;
		if (!StringUtils.isEmpty(device.getGatewayId())) {
			gateway = getKronosCache().findGatewayById(device.getGatewayId());
		}

		User user = null;
		if (!StringUtils.isEmpty(device.getUserId())) {
			user = getCoreCacheService().findUserById(device.getUserId());
		}

		Node node = null;
		if (!StringUtils.isEmpty(device.getNodeId())) {
			node = nodeService.getNodeRepository().findById(device.getNodeId()).orElse(null);
		}

		SoftwareRelease softwareRelease = null;
		if (!StringUtils.isEmpty(device.getSoftwareReleaseId())) {
			softwareRelease = getRheaCacheService().findSoftwareReleaseById(device.getSoftwareReleaseId());
		}

		// last telemetry model
		LastTelemetryModel lastTelemetryModel = getLastTelemetry(device);

		// device actions
		List<DeviceActionModel> deviceActionModels = getKronosModelUtil()
				.populateDeviceActionModels(device.getActions());

		LastLocationModel lastLocationModel = new LastLocationModel();
		lastLocationModel.setObjectType(LocationObjectType.DEVICE);
		lastLocationModel.setObjectId(device.getId());

		LastLocation lastLocation = lastLocationService.getLastLocationRepository()
				.findByObjectTypeAndObjectId(LocationObjectType.DEVICE, device.getId());
		if (lastLocation != null)
			lastLocationModel = new LastLocationModel(lastLocation);

		DeviceDetailModel deviceDetailModel = new DeviceDetailModel(device,
				(gateway != null ? new GatewayModels.GatewayModel(gateway, null, null) : null), deviceType, user, node,
				lastTelemetryModel, deviceActionModels, lastLocationModel, null,
				getSoftwareReleaseOption(softwareRelease));

		// override the generated list in constructor, left the list inside for
		// backwards compatibility for now
		deviceDetailModel.setTelemetries(getDeviceTelemetryOptions(deviceType, lastTelemetryModel));

		return deviceDetailModel;
	}

	/**
	 * This method combines the telemetry definition with the last telemetry for a
	 * device to derive a master list of telemetry, this helps prevent the user not
	 * having telemetry options to select on chart
	 * 
	 * @param deviceType
	 * @param lastTelemetryModel
	 * @return a list of List<DeviceTelemetryModel>
	 */
	private List<DeviceTelemetryModel> getDeviceTelemetryOptions(DeviceType deviceType,
			LastTelemetryModel lastTelemetryModel) {

		Map<String, DeviceTelemetryModel> map = new HashMap<>();

		// device type telemetries
		for (DeviceTelemetry deviceTelemetry : deviceType.getTelemetries())
			map.put(deviceTelemetry.getName(), new DeviceTelemetryModel(deviceTelemetry));

		// last telemetry
		if (lastTelemetryModel != null) {
			for (TelemetryItemModel model : lastTelemetryModel.getTelemetryItems()) {
				DeviceTelemetryModel deviceTelemetryModel = map.get(model.getName());
				if (deviceTelemetryModel == null) {
					deviceTelemetryModel = new DeviceTelemetryModel();
					deviceTelemetryModel.setName(model.getName());
					deviceTelemetryModel.setDescription(model.getName());
					deviceTelemetryModel.setChartable(model.getType().isChartable());
					deviceTelemetryModel.setTypeName(model.getType().name());
					map.put(deviceTelemetryModel.getName(), deviceTelemetryModel);
				}
			}
		}

		List<DeviceTelemetryModel> telemetries = new ArrayList<>(map.values());
		telemetries.sort(Comparator.comparing(DeviceTelemetryModel::getDescription, String.CASE_INSENSITIVE_ORDER));

		return telemetries;
	}

	private LastTelemetryModel getLastTelemetry(Device device) {
		String method = "getLastTelemetry";
		Assert.notNull(device, "device is null");
		Assert.hasText(device.getId(), "device id is empty");

		logInfo(method, "-----> deviceId: %s / %s", device.getId(), device.getUid());
		List<LastTelemetryItem> lastTelemetryItems = lastTelemetryItemService.getLastTelemetryItemRepository()
				.findByDeviceId(device.getId());

		List<TelemetryItemModel> telemetryItemModels = new ArrayList<>();
		long latestTs = 0;
		for (LastTelemetryItem item : lastTelemetryItems) {
			telemetryItemModels.add(new TelemetryItemModel(item));
			long ts = item.getCreatedDate().toEpochMilli();
			if (latestTs < ts) {
				latestTs = ts;
			}
		}

		telemetryItemModels.sort(Comparator.comparing(TelemetryItemModel::getName, String.CASE_INSENSITIVE_ORDER));

		logInfo(method, "-----> found: %d", telemetryItemModels.size());
		return new LastTelemetryModel(TELEMETRY_TIMESTAMP.format(new Timestamp(latestTs)), telemetryItemModels);

		/*
		 * // LastTelemetryModel lastTelemetryModel = null;
		 * 
		 * PageRequest pageRequest = new PageRequest(0, 1, new Sort(Direction.DESC,
		 * "timestamp")); TelemetrySearchParams params = new TelemetrySearchParams();
		 * params.setDeviceIds(device.getId()); Page<Telemetry> page =
		 * telemetryService.getTelemetryRepository().doFindTelemetries( pageRequest,
		 * params); List<Telemetry> telemetries = page.getContent();
		 * 
		 * // last telemetry items // List<TelemetryItem> telemetryItems; if
		 * (telemetries.size() >= 1) { Telemetry telemetry = telemetries.get(0); //
		 * telemetryItems = //
		 * telemetryItemService.getTelemetryItemRepository().findByTelemetryId(
		 * telemetry.getId());
		 * 
		 * // convert to models // List<TelemetryItemModel> telemetryItemModels = //
		 * getKronosModelUtil() // .populateTelemetryItemModels(telemetryItems);
		 * 
		 * // convert to model String modelTimestamp = "N/A"; Timestamp
		 * telemetryTimestamp = new Timestamp(telemetry.getTimestamp()); if
		 * (telemetryTimestamp != null) modelTimestamp =
		 * TELEMETRY_TIMESTAMP.format(telemetryTimestamp); lastTelemetryModel = new
		 * LastTelemetryModel(modelTimestamp, telemetryItemModels); }
		 * 
		 * return lastTelemetryModel;
		 */
	}

	@RequestMapping(value = "/{deviceId}/lastTelemetry", method = RequestMethod.GET)
	public DeviceModels.LastTelemetryModel lastTelemetry(@PathVariable String deviceId) {
		Device device = getKronosCache().findDeviceById(deviceId);

		return (device == null ? null : getLastTelemetry(device));
	}

	@RequestMapping(value = "/{deviceId}/stats", method = RequestMethod.GET)
	public DeviceModels.DeviceStatsModel stats(@PathVariable String deviceId,
			@RequestParam(value = "tz", required = false, defaultValue = "UTC") String tz) {
		Device device = getKronosCache().findDeviceById(deviceId);

		return (device == null ? null : getDeviceStats(device, tz));
	}

	private DeviceModels.DeviceStatsModel getDeviceStats(Device device, String timezone) {
		String method = "getDeviceStats";
		logDebug(method, "timezone %s", timezone);
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone(timezone));

		Calendar toDate = DateUtils.ceiling(now, Calendar.DATE);
		toDate.add(Calendar.MILLISECOND, -1);
		Calendar fromDate = DateUtils.truncate(now, Calendar.DATE);
		fromDate.add(Calendar.DATE, -6);
		logDebug(method, "week stat from %d to %d", fromDate.getTimeInMillis(), toDate.getTimeInMillis());

		long totalTelemetryItemCount = esTelemetryItemRepository.findTelemetryItemCount(device.getId(), null,
				fromDate.getTimeInMillis(), toDate.getTimeInMillis());

		long totalDeviceEventCount = deviceEventService.getDeviceEventRepository().findDeviceEventCount(device.getId(),
				fromDate.toInstant(), toDate.toInstant());

		List<DeviceModels.DeviceTelemetryEventModel> telemetryEventCounts = new ArrayList<>();
		int count = 0;
		while (count <= 6) {
			fromDate = DateUtils.truncate(now, Calendar.DATE);
			fromDate.add(Calendar.DATE, -count);
			toDate = DateUtils.ceiling(fromDate, Calendar.DATE);
			toDate.add(Calendar.MILLISECOND, -1);
			logDebug(method, "chart data from %d to %d", fromDate.getTimeInMillis(), toDate.getTimeInMillis());

			long telemetryCount = esTelemetryItemRepository.findTelemetryItemCount(device.getId(), null,
					fromDate.getTimeInMillis(), toDate.getTimeInMillis());

			long eventCount = deviceEventService.getDeviceEventRepository().findDeviceEventCount(device.getId(),
					fromDate.toInstant(), toDate.toInstant());

			telemetryEventCounts.add(
					new DeviceModels.DeviceTelemetryEventModel(fromDate.getTimeInMillis(), telemetryCount, eventCount));

			count++;
		}

		if (!telemetryEventCounts.isEmpty()) {

			Collections.sort(telemetryEventCounts, new Comparator<DeviceModels.DeviceTelemetryEventModel>() {

				@Override
				public int compare(DeviceModels.DeviceTelemetryEventModel o1,
						DeviceModels.DeviceTelemetryEventModel o2) {
					return o1.getTimestamp().compareTo(o2.getTimestamp());
				}
			});
		}

		return new DeviceModels.DeviceStatsModel(totalTelemetryItemCount, totalDeviceEventCount, telemetryEventCounts);
	}

	// consider to move the function below to the CoreConfigurationPropertyUtil
	private void addDeviceTag(Application application, String tagName) {
		Set<String> tags = new HashSet<>();
		ConfigurationProperty deviceTags = getCoreConfigurationPropertyUtil().getConfigurationProperty(
				application.getConfigurations(), ConfigurationPropertyCategory.MetaData,
				CoreConfigurationPropertyUtil.DEVICE_TAGS);
		if (deviceTags != null && !StringUtils.isEmpty(deviceTags.strValue())) {
			for (String tag : deviceTags.strValue().split(",")) {
				tags.add(tag);
			}
			tags.add(tagName);
			deviceTags.setValue(String.join(",", tags));
		} else {
			deviceTags = ConfigurationProperty.WithStringValue(ConfigurationPropertyCategory.MetaData,
					CoreConfigurationPropertyUtil.DEVICE_TAGS, tagName);
			application.getConfigurations().add(deviceTags);
		}
	}

	private String validateJson(String content) {
		try {
			ObjectMapper objectMapper = JsonUtils.getObjectMapper().copy();
			if (content != null) {
				objectMapper.readTree(content);
				return content;
			} else {
				return objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
						.writeValueAsString(new Object());
			}
		} catch (Exception e) {
			return null;
		}
	}

	@PreAuthorize("hasAuthority('KRONOS_READ_SOFTWARE_RELEASE_TRANS')")
	@RequestMapping(value = "/{deviceId}/softwareReleaseTrans", method = RequestMethod.POST)
	public SoftwareReleaseTransSearchResultModel getSoftwareReleaseTransRecords(@PathVariable String deviceId,
			@RequestBody CoreSearchFilterModel searchFilter, HttpSession session) {
		return getSoftwareReleaseTransRecords(searchFilter, getApplicationId(session), AcnDeviceCategory.DEVICE,
				deviceId);
	}

	@PreAuthorize("hasAuthority('KRONOS_READ_DEVICE_STATE_TRANS')")
	@RequestMapping(value = "/{deviceId}/deviceStateTrans", method = RequestMethod.POST)
	public DeviceStateTransSearchResultModel getDeviceStateTransRecords(@PathVariable String deviceId,
			@RequestBody DeviceStateTransSearchFilterModel searchFilter, HttpSession session) {

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		// convert search filters to search params
		DeviceStateTransSearchParams params = new DeviceStateTransSearchParams();
		// system implied filters
		params.addApplicationIds(getApplicationId(session));
		// user defined filters
		params.addDeviceIds(deviceId);
		params.addTypes(searchFilter.getTypes());
		params.addStatuses(searchFilter.getStatuses());
		Instant createdDateFrom = searchFilter.getCreatedDateFrom() != null
				? Instant.ofEpochSecond(searchFilter.getCreatedDateFrom())
				: null;
		params.setCreatedDateFrom(createdDateFrom);
		Instant createdDateTo = searchFilter.getCreatedDateTo() != null
				? Instant.ofEpochSecond(searchFilter.getCreatedDateTo())
				: null;
		params.setCreatedDateTo(createdDateTo);
		Instant updatedDateFrom = searchFilter.getUpdatedDateFrom() != null
				? Instant.ofEpochSecond(searchFilter.getUpdatedDateFrom())
				: null;
		params.setUpdatedDateFrom(updatedDateFrom);
		Instant updatedDateTo = searchFilter.getUpdatedDateTo() != null
				? Instant.ofEpochSecond(searchFilter.getUpdatedDateTo())
				: null;
		params.setUpdatedDateTo(updatedDateTo);

		// lookup
		Page<DeviceStateTrans> deviceStateTransPage = deviceStateTransService.getDeviceStateTransRepository()
				.findDeviceStateTrans(pageRequest, params);

		// convert to visual model
		Page<DeviceStateTransModel> result = null;
		List<DeviceStateTransModel> models = new ArrayList<>();
		for (DeviceStateTrans deviceStateTrans : deviceStateTransPage) {
			models.add(new DeviceStateTransModel(deviceStateTrans));
		}
		result = new PageImpl<>(models, pageRequest, deviceStateTransPage.getTotalElements());

		return new DeviceStateTransSearchResultModel(result, searchFilter);
	}

	@RequestMapping(value = "/{deviceId}/backups/find", method = RequestMethod.POST)
	public SearchResultModels.ConfigBackupSearchResultModel getDeviceBackups(
			@RequestBody SearchFilterModels.ConfigBackupSearchFilterOptions searchFilter, @PathVariable String deviceId,
			HttpSession session) {

		String applicationId = getApplicationId(session);
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");

		Application application = getCoreCacheService().findApplicationById(device.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(device.getApplicationId()),
				"user and device must have the same application id");

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		ConfigBackupSearchParams params = new ConfigBackupSearchParams();
		params.addNames(searchFilter.getName());
		params.addTypes(EnumSet.of(ConfigBackup.Type.DEVICE));
		params.addObjectIds(deviceId);

		// convert to model
		Page<ConfigBackup> configBackups = configBackupService.getConfigBackupRepository()
				.findConfigBackups(pageRequest, params);
		List<BackupModels.BackupModel> backupModels = new ArrayList<>();
		for (ConfigBackup configBackup : configBackups) {
			BackupModels.BackupModel backupModel = new BackupModels.BackupModel(configBackup);
			User currentUser = getCoreCacheService().findUserById(backupModel.getCreateBy());
			if (currentUser != null) {
				backupModel.setCreateBy(getKronosModelUtil().populateDecryptedLogin(currentUser));
			} else if (!backupModel.getCreateBy().equals("admin")) {
				backupModel.setCreateBy("Unknown");
			}

			backupModels.add(backupModel);

		}

		Page<BackupModels.BackupModel> result = new PageImpl<>(backupModels, pageRequest,
				configBackups.getTotalElements());

		return new SearchResultModels.ConfigBackupSearchResultModel(result, searchFilter);
	}

	@RequestMapping(value = "/{deviceId}/backups/create", method = RequestMethod.POST)
	public BackupModels.BackupModel createDeviceBackup(@PathVariable String deviceId, HttpSession httpSession,
			@RequestBody CreateConfigBackupModel createConfigBackupModel) {
		Assert.notNull(createConfigBackupModel, "createConfigBackupModel model is null");
		Assert.hasText(createConfigBackupModel.getName(), "name is empty");

		return new BackupModels.BackupModel(
				createBackup(deviceId, getApplicationId(httpSession), createConfigBackupModel.getName(), getUserId()));
	}

	@RequestMapping(value = "/backups/create", method = RequestMethod.POST)
	public BulkActionResultModel bulkDeviceBackup(@RequestBody BackupModels.BulkConfigBackupModel bulkConfigBackupModel,
			HttpSession httpSession) {
		String method = "bulkDeviceBackup";
		Assert.notNull(bulkConfigBackupModel, "bulkConfigBackupModel model is null");
		Assert.notNull(bulkConfigBackupModel.getIds(), "Ids is null");
		Assert.hasText(bulkConfigBackupModel.getName(), "name is empty");

		String applicationId = getApplicationId(httpSession);
		String authenticatedUserId = getUserId();

		int total = bulkConfigBackupModel.getIds().length, numSucceeded = 0, numFailed = 0;
		for (String deviceId : bulkConfigBackupModel.getIds()) {
			try {
				createBackup(deviceId, applicationId, bulkConfigBackupModel.getName(), authenticatedUserId);
				numSucceeded++;
			} catch (Exception e) {
				logError(method, "Failed to back up device id=" + deviceId, e);
				numFailed++;
			}
			logInfo(method, "backed up %d devices of total %d; failed to back up %d devices", numSucceeded, total,
					numFailed);
		}

		return new BulkActionResultModel(total, numSucceeded, numFailed);
	}

	@RequestMapping(value = "/{deviceId}/backups/{backupId}/restore", method = RequestMethod.POST)
	public StatusModel restoreDeviceBackup(@PathVariable(value = "deviceId") String deviceId,
			@PathVariable(value = "backupId") String backupId, HttpSession httpSession) {

		Assert.hasText(deviceId, "deviceId is empty");

		String applicationId = getApplicationId(httpSession);

		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");

		Application application = getCoreCacheService().findApplicationById(device.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(device.getApplicationId()),
				"user and device must have the same application id");

		ConfigBackup configBackup = configBackupService.getConfigBackupRepository().findById(backupId).orElse(null);
		Assert.notNull(configBackup, "configBackup is not found");

		deviceService.restoreConfiguration(device, configBackup, configBackup.getObjectId());

		return StatusModel.OK;
	}

	@PreAuthorize("hasAuthority('KRONOS_READ_DEVICE_STATE_TRANS')")
	@RequestMapping(value = "/deviceStateTransFilterOptions", method = RequestMethod.GET)
	public DeviceStateHistoryFilterOptions getDeviceStateTransFilterOptions() {
		return new DeviceStateHistoryFilterOptions();
	}

	@PreAuthorize("hasAuthority('KRONOS_READ_DEVICE_STATE_TRANS')")
	@RequestMapping(value = "/{deviceId}/deviceStateTrans/{deviceStateTransId}", method = RequestMethod.GET)
	public DeviceStateTransDetailsModel getDeviceStateTransDetails(@PathVariable String deviceId,
			@PathVariable String deviceStateTransId, HttpSession session) {
		DeviceStateTrans deviceStateTrans = deviceStateTransService.getDeviceStateTransRepository()
				.findById(deviceStateTransId).orElse(null);
		Assert.notNull(deviceStateTrans, "deviceStateTrans is null");
		Assert.isTrue(StringUtils.equals(deviceId, deviceStateTrans.getDeviceId()),
				"deviceStateTrans does not match deviceId");
		Assert.isTrue(StringUtils.equals(getApplicationId(session), deviceStateTrans.getApplicationId()),
				"deviceStateTrans does not match applicationId");

		return new DeviceStateTransDetailsModel(deviceStateTrans);
	}

	@RequestMapping(value = "/{deviceId}/configuration/cloud/restore", method = RequestMethod.POST)
	public StatusModel syncCloudConfigurationToDevice(@PathVariable(value = "deviceId") String deviceId,
			HttpSession httpSession) {

		Assert.hasText(deviceId, "deviceId is empty");

		String applicationId = getApplicationId(httpSession);

		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "gateway is null");

		Application application = getCoreCacheService().findApplicationById(device.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(device.getApplicationId()),
				"user and gateway must have the same application id");

		Gateway gateway = gatewayService.getGatewayRepository().findById(device.getGatewayId()).orElse(null);

		if (gateway != null) {
			Event event = deviceService.requestConfigurationRestore(device.getId());
			Assert.notNull(event, "event is null");
			logInfo("syncCloudConfigurationToDevice",
					"event has been sent to deviceId=" + device.getId() + ", eventId=" + event.getId());

			return StatusModel.OK.withMessage(event.getId());
		}

		return StatusModel.error("This device does not have a gateway");
	}

	@RequestMapping(value = "/{deviceId}/configuration/cloud/sync", method = RequestMethod.POST)
	public StatusModel syncDeviceConfigurationToCloud(@PathVariable(value = "deviceId") String deviceId,
			HttpSession httpSession) {

		Assert.hasText(deviceId, "deviceId is empty");
		String applicationId = getApplicationId(httpSession);

		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");

		Application application = getCoreCacheService().findApplicationById(device.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(device.getApplicationId()),
				"user and gateway must have the same application id");

		Gateway gateway = gatewayService.getGatewayRepository().findById(device.getGatewayId()).orElse(null);

		if (gateway != null) {
			Event event = deviceService.requestConfigurationUpdate(device.getId());
			Assert.notNull(event, "event is null");
			logInfo("syncDeviceConfigurationToCloud",
					"event has been sent to deviceId=" + device.getId() + ", eventId=" + event.getId());

			return StatusModel.OK.withMessage(event.getId());
		}

		return StatusModel.error("This device does not have a gateway");
	}

	private ConfigBackup createBackup(String deviceId, String applicationId, String name, String userId) {
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");

		Application application = getCoreCacheService().findApplicationById(device.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(device.getApplicationId()),
				"user and device must have the same application id");

		return deviceService.backupConfiguration(device, name, userId);
	}

	@PreAuthorize("hasAuthority('KRONOS_DELETE_DEVICE_STATE')")
	@RequestMapping(value = "/{deviceId}/states/bulkDelete", method = RequestMethod.PUT)
	public BulkActionResultModel deleteDeviceStates(@PathVariable String deviceId,
			@RequestBody DeviceStateDeleteModel model, HttpSession session) {

		String method = "deleteDeviceStates";
		Assert.notNull(model, "DeviceState input model is null");
		Assert.hasText(deviceId, "deviceId is empty");
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		DeviceType deviceType = getKronosCache().findDeviceTypeById(device.getDeviceTypeId());
		Assert.notNull(deviceType, "deviceType is null");

		Assert.isTrue(getApplicationId(session).equals(device.getApplicationId()),
				"user and device must have the same application id");

		DeviceState deviceState = deviceStateService.getDeviceStateRepository()
				.findByApplicationIdAndDeviceId(device.getApplicationId(), device.getId());
		Assert.notNull(deviceState, "deviceState is null");

		int totalStatesToDelete = model.getStates().size();
		int initialDeviceStatesNumber = deviceState.getStates().size();

		for (String state : model.getStates()) {
			deviceState.getStates().remove(state);
			if (model.isRemoveStateDefinition()) {
				deviceType.getStateMetadata().remove(state);
			}
		}
		try {
			deviceState = deviceStateService.update(deviceState, getUserId());
			if (model.isRemoveStateDefinition()) {
				deviceType = getDeviceTypeService().update(deviceType, getUserId());
			}
		} catch (Exception e) {
			logError(method, "Failed to delete some device states", e);
		}

		int deviceStatesNumberAfterUpdate = deviceState.getStates().size();
		int numDeletedDeviceStates = initialDeviceStatesNumber - deviceStatesNumberAfterUpdate;
		int numFailedDeleteDeviceStates = totalStatesToDelete - numDeletedDeviceStates;
		logInfo(method, "deleted %d deviceStates of total %d; failed to delete %d deviceStates", numDeletedDeviceStates,
				totalStatesToDelete, numFailedDeleteDeviceStates);

		return new BulkActionResultModel(totalStatesToDelete, numDeletedDeviceStates, numFailedDeleteDeviceStates);
	}
}

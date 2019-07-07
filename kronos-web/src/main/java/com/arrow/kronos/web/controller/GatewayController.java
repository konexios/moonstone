package com.arrow.kronos.web.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.acn.client.model.CreateConfigBackupModel;
import com.arrow.acs.client.model.StatusModel;
import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.ConfigBackup;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.KronosApplication;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.data.SoftwareReleaseSchedule;
import com.arrow.kronos.data.TestProcedure;
import com.arrow.kronos.repo.ConfigBackupSearchParams;
import com.arrow.kronos.repo.DeviceSearchParams;
import com.arrow.kronos.repo.GatewaySearchParams;
import com.arrow.kronos.service.ConfigBackupService;
import com.arrow.kronos.service.DeviceActionTypeService;
import com.arrow.kronos.service.DeviceEventService;
import com.arrow.kronos.service.DeviceService;
import com.arrow.kronos.service.GatewayService;
import com.arrow.kronos.service.KronosApplicationService;
import com.arrow.kronos.service.NodeService;
import com.arrow.kronos.service.SoftwareReleaseScheduleService;
import com.arrow.kronos.service.TestProcedureService;
import com.arrow.kronos.service.TestResultService;
import com.arrow.kronos.web.model.AuditLogModels.AuditLogDetails;
import com.arrow.kronos.web.model.BackupModels;
import com.arrow.kronos.web.model.BulkActionResultModel;
import com.arrow.kronos.web.model.BulkEditActionModels;
import com.arrow.kronos.web.model.GatewayModels.CreateGatewayModel;
import com.arrow.kronos.web.model.GatewayModels.GatewayDetailsModel;
import com.arrow.kronos.web.model.GatewayModels.GatewayModel;
import com.arrow.kronos.web.model.GatewayModels.GatewayMoveModel;
import com.arrow.kronos.web.model.GatewayModels.GatewayMoveResultModel;
import com.arrow.kronos.web.model.GatewayModels.GatewaySettingsModel;
import com.arrow.kronos.web.model.GatewayModels.NewGatewayModel;
import com.arrow.kronos.web.model.HeartbeatModels.HeartbeatModel;
import com.arrow.kronos.web.model.RheaModels.SoftwareReleaseOption;
import com.arrow.kronos.web.model.SearchFilterModels;
import com.arrow.kronos.web.model.SearchFilterModels.AuditLogSearchFilterModel;
import com.arrow.kronos.web.model.SearchFilterModels.AuditLogSearchFilterOptions;
import com.arrow.kronos.web.model.SearchFilterModels.GatewayDetailsOptions;
import com.arrow.kronos.web.model.SearchFilterModels.GatewaySearchFilterModel;
import com.arrow.kronos.web.model.SearchFilterModels.GatewaySearchFilterOptions;
import com.arrow.kronos.web.model.SearchResultModels;
import com.arrow.kronos.web.model.SearchResultModels.AuditLogSearchResultModel;
import com.arrow.kronos.web.model.SearchResultModels.GatewaySearchResultModel;
import com.arrow.kronos.web.model.SearchResultModels.SoftwareReleaseTransSearchResultModel;
import com.arrow.pegasus.data.AuditLog;
import com.arrow.pegasus.data.event.Event;
import com.arrow.pegasus.data.heartbeat.HeartbeatObjectType;
import com.arrow.pegasus.data.heartbeat.LastHeartbeat;
import com.arrow.pegasus.data.location.LastLocation;
import com.arrow.pegasus.data.location.LastLocationType;
import com.arrow.pegasus.data.location.LocationObjectType;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.repo.LastHeartbeatSearchParams;
import com.arrow.pegasus.service.LastHeartbeatService;
import com.arrow.pegasus.service.LastLocationService;
import com.arrow.pegasus.webapi.data.CoreSearchFilterModel;
import com.arrow.pegasus.webapi.data.LastLocationModel;
import com.arrow.rhea.data.SoftwareRelease;

@RestController
@RequestMapping("/api/kronos/gateway")
public class GatewayController extends BaseControllerAbstract {

	@Autowired
	private GatewayService gatewayService;
	@Autowired
	private LastHeartbeatService lastHeartbeatService;
	@Autowired
	private LastLocationService lastLocationService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private ConfigBackupService configBackupService;
	@Autowired
	private KronosApplicationService kronosApplicationService;
	@Autowired
	private DeviceEventService deviceEventService;
	@Autowired
	private DeviceActionTypeService deviceActionTypeService;
	@Autowired
	private TestProcedureService testProcedureService;
	@Autowired
	private TestResultService testResultService;
	@Autowired
	private SoftwareReleaseScheduleService softwareReleaseScheduleService;

	@PreAuthorize("hasAuthority('KRONOS_VIEW_GATEWAYS')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public GatewaySearchResultModel list(@RequestBody GatewaySearchFilterModel searchFilter,
	        @RequestParam(value = "bulkEdit", defaultValue = "false") boolean bulkEdit, HttpSession session) {

		// sorting & paging
		Sort sort = new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField());
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(), sort);

		GatewaySearchParams params = new GatewaySearchParams();
		params.addApplicationIds(getApplicationId(session));
		if (searchFilter.getHid() != null && !searchFilter.getHid().equals("")) {
			params.addHids(searchFilter.getHid());
		}

		if (searchFilter.getNodeIds() != null && !searchFilter.getNodeIds().equals("")) {
			params.addNodeIds(searchFilter.getNodeIds());
		}
		// user defined filter
		params.addDeviceTypeIds(searchFilter.getDeviceTypeIds());
		params.addGatewayTypes(searchFilter.getGatewayTypes());
		if (searchFilter.getUid() != null && !searchFilter.getUid().equals("")) {
			params.addUids(searchFilter.getUid());
		}
		params.addOsNames(searchFilter.getOsNames());
		params.addSoftwareNames(searchFilter.getSoftwareNames());
		params.addSoftwareVersions(searchFilter.getSoftwareVersions());
		if (hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			params.addUserIds(searchFilter.getUserIds());
		} else {
			params.addUserIds(getAuthenticatedUser().getId());
		}
		params.setEnabled(searchFilter.isEnabled());

		Page<Gateway> gateways = gatewayService.getGatewayRepository().findGateways(pageRequest, params);

		// lookup last heartbeats
		String[] gatewayIds = new String[gateways.getContent().size()];
		int i = 0;
		for (Gateway gateway : gateways.getContent()) {
			gatewayIds[i++] = gateway.getId();
		}
		// pageRequest = new PageRequest(0, Integer.MAX_VALUE);
		LastHeartbeatSearchParams lastHeartbeatParams = new LastHeartbeatSearchParams();
		lastHeartbeatParams.addObjectTypes(HeartbeatObjectType.GATEWAY);
		lastHeartbeatParams.addObjectIds(gatewayIds);

		Page<LastHeartbeat> lastHeartbeats = lastHeartbeatService.getLastHeartbeatRepository()
		        .findLastHeartbeats(pageRequest, lastHeartbeatParams);

		// map gateway id to last heartbeat
		Map<String, LastHeartbeat> gatewayLastHeartbeats = new HashMap<>();
		for (LastHeartbeat lastHeartbeat : lastHeartbeats.getContent()) {
			gatewayLastHeartbeats.put(lastHeartbeat.getObjectId(), lastHeartbeat);
		}

		// convert to visual model
		List<GatewayModel> gatewayModels = new ArrayList<>();
		for (Gateway gateway : gateways) {
			String nodeName = null;
			String ownerName = null;
			if (!StringUtils.isEmpty(gateway.getUserId())) {
				User user = getCoreCacheService().findUserById(gateway.getUserId());
				if (user != null)
					ownerName = user.getContact().fullName();
			}
			if (!StringUtils.isEmpty(gateway.getNodeId())) {
				Node node = getKronosCache().findNodeById(gateway.getNodeId());
				if (node != null)
					nodeName = node.getName();
			}

			if (!StringUtils.isEmpty(gateway.getDeviceTypeId()))
				gateway.setRefDeviceType(getKronosCache().findDeviceTypeById(gateway.getDeviceTypeId()));

			gatewayModels
			        .add(new GatewayModel(gateway, nodeName, ownerName, gatewayLastHeartbeats.get(gateway.getId())));
		}
		Page<GatewayModel> result = new PageImpl<>(gatewayModels, pageRequest, gateways.getTotalElements());

		List<String> documentIds = null;
		if (bulkEdit) {
			if (gateways.getNumberOfElements() == gateways.getTotalElements()) {
				// all gateways matching the filter are in the current slice
				documentIds = gateways.getContent().stream().map(gateway -> gateway.getId())
				        .collect(Collectors.toList());
			} else {
				// find all gatewayIds matching the filter
				documentIds = gatewayService.getGatewayRepository().findGatewayIds(params, sort);
			}
		}

		return new GatewaySearchResultModel(result, searchFilter, documentIds);
	}

	@RequestMapping(value = "/filter", method = RequestMethod.GET)
	public GatewaySearchFilterOptions getFilterOptions(HttpSession session) {

		Application application = getApplication(session);
		User authenticatedUser = getAuthenticatedUser();

		String applicationId = application.getId();
		String userId = authenticatedUser.getId();
		List<DeviceType> deviceTypes;
		List<User> users;
		List<String> osNames;
		List<String> softwareNames;
		List<String> softwareVersions;
		List<Node> nodes;

		if (hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
			deviceTypes = getAvailableDeviceTypes(application.getId(), AcnDeviceCategory.GATEWAY, true);
			users = getAvailableUsers(application, authenticatedUser);
			osNames = gatewayService.getGatewayRepository().findAggregatedOsNames(applicationId);
			softwareNames = gatewayService.getGatewayRepository().findAggregatedSoftwareNames(applicationId);
			softwareVersions = gatewayService.getGatewayRepository().findAggregatedSoftwareVersions(applicationId);
			nodes = getAvailableNodes(application);
		} else {
			deviceTypes = getUsedDeviceTypes(application, authenticatedUser);
			users = getAuthenticatedUserOption(authenticatedUser);
			osNames = gatewayService.getGatewayRepository().findAggregatedOsNames(applicationId, userId);
			softwareNames = gatewayService.getGatewayRepository().findAggregatedSoftwareNames(applicationId, userId);
			softwareVersions = gatewayService.getGatewayRepository().findAggregatedSoftwareVersions(applicationId,
			        userId);
			nodes = getUsedNodes(application, authenticatedUser);
		}

		GatewaySearchFilterOptions filterOptions = new GatewaySearchFilterOptions(deviceTypes, users, osNames,
		        softwareNames, softwareVersions, nodes);
		return filterOptions;
	}

	// only asset types of user's gateways
	private List<DeviceType> getUsedDeviceTypes(Application application, User authenticatedUser) {
		List<String> deviceTypeIds = deviceService.getDeviceRepository()
		        .doFindAggregatedDeviceTypeIds(application.getId(), authenticatedUser.getId());
		return getDeviceTypeService().getDeviceTypeRepository().doFindAllByIdsApplicationIdAndEnabled(deviceTypeIds,
		        application.getId(), true);
	}

	@PreAuthorize("hasAuthority('KRONOS_CREATE_GATEWAY')")
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public NewGatewayModel newGateway(HttpSession session) {
		Application application = getApplication(session);
		User authenticatedUser = getAuthenticatedUser();

		DeviceType defaultGatewayDeviceType = getDeviceTypeService().checkCreateDefaultGatewayType(application.getId(),
		        authenticatedUser.getId());

		return new NewGatewayModel(getAvailableUsers(application, authenticatedUser), authenticatedUser.getId(),
		        getAvailableDeviceTypes(application.getId(), AcnDeviceCategory.GATEWAY, true),
		        defaultGatewayDeviceType.getId());
	}

	@PreAuthorize("hasAuthority('KRONOS_CREATE_GATEWAY')")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public CreateGatewayModel create(@RequestBody CreateGatewayModel model, HttpSession session) {
		String applicationId = getApplicationId(session);
		String authenticatedUserId = getUserId();

		Gateway gateway = new Gateway();
		gateway.setApplicationId(applicationId);
		gateway.setUid(model.getUid());
		gateway.setName(model.getName());
		gateway.setType(model.getType());
		gateway.setDeviceTypeId(model.getDeviceTypeId());
		gateway.setOsName(model.getOsName());
		gateway.setSoftwareName(model.getSoftwareName());
		gateway.setSoftwareVersion(model.getSoftwareVersion());
		gateway.setSdkVersion(model.getSdkVersion());
		gateway.setUserId(model.getUserId());
		gateway.setExternalId(model.getExternalId());
		gateway.setEnabled(model.isEnabled());
		gateway.setPersistTelemetry(model.getPersistTelemetry());
		gateway.setIndexTelemetry(model.getIndexTelemetry());
		gateway.setProperties(model.getProperties());
		gateway.setInfo(model.getInfo());

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		checkEnabled(application, "application");

		gateway = gatewayService.create(gateway, authenticatedUserId);

		return new CreateGatewayModel(gateway);
	}

	@PreAuthorize("hasAuthority('KRONOS_VIEW_GATEWAYS')")
	@RequestMapping(value = "/{gatewayId}", method = RequestMethod.GET)
	public GatewayDetailsModel getGatewayDetails(@PathVariable String gatewayId, HttpSession session) {
		String applicationId = getApplicationId(session);
		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is null");
		gateway = gatewayService.populate(gateway);

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(gateway.getApplicationId()),
		        "user and gateway must have the same application id");

		PageRequest pageable = PageRequest.of(0, 1, new Sort(Direction.DESC, "createdDate"));
		Page<AuditLog> auditLogs = getAuditLogService().getAuditLogRepository().findAuditLogs(pageable, null,
		        new String[] { getApplicationId(session) }, new String[] { KronosAuditLog.Gateway.GatewayCheckin },
		        new String[] { gatewayId }, null, null, null, null);

		Long lastCheckinTime = null;
		if (auditLogs != null && auditLogs.getContent().size() > 0) {
			AuditLog auditLog = auditLogs.getContent().get(0);
			lastCheckinTime = auditLog.getCreatedDate().getEpochSecond();
		}

		LastHeartbeat lastHeartbeat = lastHeartbeatService.getLastHeartbeatRepository()
		        .findByObjectTypeAndObjectId(HeartbeatObjectType.GATEWAY, gatewayId);
		Long lastHeartbeatTime = null;
		if (lastHeartbeat != null) {
			lastHeartbeatTime = lastHeartbeat.getTimestamp();
		}

		LastLocationModel lastLocationModel = new LastLocationModel();
		lastLocationModel.setObjectType(LocationObjectType.GATEWAY);
		lastLocationModel.setObjectId(gatewayId);

		LastLocation lastLocation = lastLocationService.getLastLocationRepository()
		        .findByObjectTypeAndObjectId(LocationObjectType.GATEWAY, gatewayId);
		if (lastLocation != null)
			lastLocationModel = new LastLocationModel(lastLocation);

		SoftwareRelease softwareRelease = null;
		if (!StringUtils.isEmpty(gateway.getSoftwareReleaseId())) {
			softwareRelease = getRheaCacheService().findSoftwareReleaseById(gateway.getSoftwareReleaseId());
		}

		return new GatewayDetailsModel(gateway, gateway.getRefUser(), lastCheckinTime, lastHeartbeatTime,
		        lastLocationModel, getSoftwareReleaseOption(softwareRelease));
	}

	@PreAuthorize("hasAuthority('KRONOS_VIEW_GATEWAYS')")
	@RequestMapping(value = "/{gatewayId}/pri", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public String getDevicePri(@PathVariable String gatewayId, HttpSession session) {
		String applicationId = getApplicationId(session);
		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is null");

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(gateway.getApplicationId()),
		        "user and gateway must have the same application id");

		return gateway.getPri();
	}

	@PreAuthorize("hasAuthority('KRONOS_VIEW_GATEWAYS')")
	@RequestMapping(value = "/options", method = RequestMethod.GET)
	public GatewayDetailsOptions getGatewayDetailsOptions(@RequestParam(required = false) String deviceTypeId,
	        HttpSession session) {
		Application application = getApplication(session);
		User authenticatedUser = getAuthenticatedUser();

		List<DeviceType> deviceTypes = getAvailableDeviceTypes(application.getId(), AcnDeviceCategory.GATEWAY, true);
		List<User> users = getAvailableUsers(application, authenticatedUser);
		List<Node> nodes = getAvailableNodes(application);

		List<SoftwareReleaseOption> softwareReleaseOptions = new ArrayList<>();
		if (!StringUtils.isEmpty(deviceTypeId)) {
			DeviceType deviceType = getKronosCache().findDeviceTypeById(deviceTypeId);
			Assert.notNull(deviceType, "deviceType is null");
			softwareReleaseOptions = getAvailableSoftwareReleases(deviceType.getRheaDeviceTypeId(), session);
		}

		return new GatewayDetailsOptions(deviceTypes, users, nodes, softwareReleaseOptions);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_GATEWAY')")
	@RequestMapping(value = "/{gatewayId}", method = RequestMethod.PUT)
	public GatewaySettingsModel saveGatewayDetails(@PathVariable String gatewayId,
	        @RequestBody GatewaySettingsModel model, HttpSession session) {
		String applicationId = getApplicationId(session);
		String authenticatedUserId = getUserId();

		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is null");

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(gateway.getApplicationId()),
		        "user and gateway must have the same application id");

		gateway.setName(model.getName());
		gateway.setDeviceTypeId(model.getDeviceTypeId());

		String userId;
		if (model.getUser() != null) {
			userId = model.getUser().getId();
		} else {
			userId = null;
		}
		gateway.setUserId(userId);

		if (model.getNode() != null) {
			gateway.setNodeId(model.getNode().getId());
		} else {
			gateway.setNodeId(null);
		}

		// settings
		Map<String, String> newSettings = model.getProperties();
		List<String> changedSettingNames = new ArrayList<>();
		for (Map.Entry<String, String> entry : gateway.getProperties().entrySet()) {
			String newValue = newSettings.get(entry.getKey());
			if (newValue != null && !newValue.equals(entry.getValue())) {
				changedSettingNames.add(entry.getKey());
			}
		}
		gateway.setProperties(newSettings);

		if (model.getSoftwareRelease() != null) {
			gateway.setSoftwareReleaseId(model.getSoftwareRelease().getId());
		} else {
			gateway.setSoftwareReleaseId(null);
		}

		gateway = gatewayService.update(gateway, authenticatedUserId);

		if (model.isAssignOwnerToDevices()) {
			// update owner of all devices connected to the gateway
			PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE);
			DeviceSearchParams params = new DeviceSearchParams();

			params.addApplicationIds(applicationId);
			params.addGatewayIds(gateway.getId());

			// lookup for gateway devices
			Page<Device> devicePage = deviceService.getDeviceRepository().doFindDevices(pageRequest, params);

			for (Device device : devicePage) {
				device.setUserId(userId);
				// persist the device
				deviceService.update(device, authenticatedUserId);
			}
		}

		User user = null;
		if (!StringUtils.isEmpty(gateway.getUserId())) {
			user = getCoreCacheService().findUserById(gateway.getUserId());
		}

		Node node = null;
		if (!StringUtils.isEmpty(gateway.getNodeId())) {
			node = getKronosCache().findNodeById(gateway.getNodeId());
		}

		SoftwareRelease softwareRelease = null;
		if (!StringUtils.isEmpty(gateway.getSoftwareReleaseId())) {
			softwareRelease = getRheaCacheService().findSoftwareReleaseById(gateway.getSoftwareReleaseId());
		}

		return new GatewaySettingsModel(gateway, user, node, model.isAssignOwnerToDevices(),
		        getSoftwareReleaseOption(softwareRelease));
	}

	@PreAuthorize("hasAuthority('KRONOS_DELETE_GATEWAY')")
	@RequestMapping(value = "/{gatewayId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String gatewayId, HttpSession session) {
		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is null");

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(StringUtils.equals(getApplicationId(session), gateway.getApplicationId()),
		        "user and gateway must have the same application id");

		// List<SoftwareReleaseSchedule> schedules =
		// softwareReleaseScheduleService.getSoftwareReleaseScheduleRepository()
		// .findSoftwareReleaseSchedulesByObjectIds(gatewayId);
		// Assert.isTrue(schedules.isEmpty(),
		// String.format("Asset %s is associated with a system job and can't be
		// deleted", gateway.getName()));

		gatewayService.delete(gateway, getUserId());
	}

	@PreAuthorize("hasAuthority('KRONOS_DELETE_GATEWAY')")
	@RequestMapping(value = "/bulkDelete", method = RequestMethod.POST)
	public BulkActionResultModel bulkDelete(@RequestBody String[] gatewaysIds, HttpSession session) {
		String method = "bulkDelete";

		String applicationId = getApplicationId(session);
		String authenticatedUserId = getUserId();

		Application application = getCoreCacheService().findApplicationById(applicationId);
		checkEnabled(application, "application");

		List<SoftwareReleaseSchedule> schedules = softwareReleaseScheduleService.getSoftwareReleaseScheduleRepository()
		        .findSoftwareReleaseSchedulesByObjectIds(gatewaysIds);
		Set<String> scheduledDevices = schedules.stream().map(SoftwareReleaseSchedule::getObjectIds)
		        .flatMap(Collection::stream).collect(Collectors.toSet());

		int total = gatewaysIds.length, numDeleted = 0, numFailed = 0;
		for (String gatewayId : gatewaysIds) {
			try {
				Gateway gateway = getKronosCache().findGatewayById(gatewayId);
				if (gateway != null) {
					// user and gateway must have the same application id
					Assert.isTrue(StringUtils.equals(applicationId, gateway.getApplicationId()),
					        "user and gateway must have the same application id");
					Assert.isTrue(!scheduledDevices.contains(gatewayId), String.format(
					        "Asset %s is associated with a system job and can't be deleted", gateway.getName()));
					gatewayService.delete(gateway, authenticatedUserId);
				}
				numDeleted++;
			} catch (Exception e) {
				logError(method, "Failed to delete gateway id=" + gatewayId, e);
				numFailed++;
			}
			logInfo(method, "deleted %d gateways of total %d; failed to delete %d gateways", numDeleted, total,
			        numFailed);
		}

		return new BulkActionResultModel(total, numDeleted, numFailed);
	}

	@PreAuthorize("hasAuthority('KRONOS_MOVE_GATEWAY')")
	@RequestMapping(value = "/uid/{uid}", method = RequestMethod.GET)
	public List<GatewayMoveModel> findByUid(@PathVariable String uid, HttpSession session) {
		Assert.hasLength(uid, "uid must not be empty");

		String applicationId = getApplicationId(session);
		List<Gateway> gateways = gatewayService.getGatewayRepository().findAllByUid(uid);
		List<GatewayMoveModel> result = new ArrayList<>();
		for (Gateway gateway : gateways) {
			KronosApplication kronosApplication = kronosApplicationService.getKronosApplicationRepository()
			        .findByApplicationId(gateway.getApplicationId());
			result.add(new GatewayMoveModel(gateway, kronosApplicationService.populateRefs(kronosApplication),
			        StringUtils.equals(gateway.getApplicationId(), applicationId)));
		}
		return result;
	}

	@PreAuthorize("hasAuthority('KRONOS_MOVE_GATEWAY')")
	@RequestMapping(value = "/move/{gatewayId}", method = RequestMethod.POST)
	public GatewayMoveResultModel move(@PathVariable String gatewayId,
	        @RequestParam(value = "useExisting", required = false) Boolean useExisting, HttpSession session) {
		String who = getUserId();
		Assert.hasLength(gatewayId, "gatewayId must not be empty");

		Application application = getCoreCacheService().findApplicationById(getApplicationId(session));
		checkEnabled(application, "application");

		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is null");
		Assert.isTrue(!StringUtils.equals(gateway.getApplicationId(), application.getId()),
		        "Gateway id=" + gateway.getId() + ", uid=" + gateway.getUid()
		                + " already belongs to this application, no need to move it");

		KronosApplication kronosApplication = kronosApplicationService.getKronosApplicationRepository()
		        .findByApplicationId(gateway.getApplicationId());
		Assert.isTrue(kronosApplication.isAllowGatewayTransfer(),
		        "Gateway transfer is not allowed for kronos application=" + kronosApplication.getId());

		List<Device> devices = deviceService.getDeviceRepository().findAllByGatewayId(gateway.getId());

		Iterable<TestProcedure> usedTestProcedures = getUsedTestProcedures(gateway, devices);
		Iterable<DeviceType> usedDeviceTypes = getUsedDeviceTypes(gateway, devices, usedTestProcedures);
		Iterable<DeviceActionType> usedDeviceActionTypes = getUsedDeviceActionTypes(gateway, devices, usedDeviceTypes);

		if (useExisting == null) {
			List<DeviceType> existingDeviceTypes = new ArrayList<>();
			List<DeviceActionType> existingDeviceActionTypes = new ArrayList<>();

			// 1. find all existing device types of gateway and it's devices
			// device types
			Set<String> deviceTypeNames = new HashSet<>();
			for (DeviceType deviceType : usedDeviceTypes) {
				deviceTypeNames.add(deviceType.getName());
			}
			if (!deviceTypeNames.isEmpty()) {
				existingDeviceTypes = getDeviceTypeService().getDeviceTypeRepository().findByApplicationIdAndNames(
				        application.getId(), deviceTypeNames.toArray(new String[deviceTypeNames.size()]));
			}

			// 2. find all existing device action types
			Set<String> customDeviceActionTypeSystemNames = new HashSet<>();
			for (DeviceActionType deviceActionType : usedDeviceActionTypes) {
				if (deviceActionType.isEditable()) {
					customDeviceActionTypeSystemNames.add(deviceActionType.getSystemName());
				}
				// else - system action type, always use existing
			}
			if (!customDeviceActionTypeSystemNames.isEmpty()) {
				existingDeviceActionTypes = deviceActionTypeService.getDeviceActionTypeRepository()
				        .findByApplicationIdAndSystemNames(application.getId(), customDeviceActionTypeSystemNames
				                .toArray(new String[customDeviceActionTypeSystemNames.size()]));
			}

			if (!existingDeviceTypes.isEmpty() || !existingDeviceActionTypes.isEmpty()) {
				return new GatewayMoveResultModel(existingDeviceTypes, existingDeviceActionTypes);
			}
		}

		// map used objects
		Map<String, DeviceActionType> mappedDeviceActionTypes = deviceActionTypeService
		        .findOrClone(usedDeviceActionTypes, application, who);
		Map<String, DeviceType> mappedDeviceTypes = getDeviceTypeService().findOrClone(usedDeviceTypes, application,
		        mappedDeviceActionTypes, who);
		Map<String, TestProcedure> mappedTestProcedures = testProcedureService.findOrClone(usedTestProcedures,
		        application, mappedDeviceTypes, who);

		gateway = gatewayService.move(gateway, application, mappedDeviceTypes, mappedDeviceActionTypes,
		        mappedTestProcedures, who);

		return new GatewayMoveResultModel(gateway);
	}

	// TODO @PreAuthorize("hasAuthority('KRONOS_EDIT_GATEWAY_LAST_LOCATION')")
	@RequestMapping(value = "/{gatewayId}/lastLocation", method = RequestMethod.POST)
	public LastLocationModel updateLastLocation(@PathVariable String gatewayId, @RequestBody LastLocationModel model,
	        HttpSession session) {
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getObjectType(), "objectType is null");
		Assert.hasText(model.getObjectId(), "objectId is empty");
		Assert.notNull(model.getLatitude(), "latitude is null");
		Assert.notNull(model.getLongitude(), "longitude is null");

		String applicationId = getApplicationId(session);

		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is null");

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(gateway.getApplicationId()),
		        "user and gateway must have the same application id");

		String lastLocationId = lastLocationService.update(model.getObjectType(), model.getObjectId(),
		        model.getLatitude(), model.getLongitude(), LastLocationType.STATIC, Instant.now());

		LastLocation lastLocation = lastLocationService.getLastLocationRepository().findById(lastLocationId)
		        .orElse(null);
		Assert.notNull(lastLocation, "lastLocation is null; id: " + lastLocationId);

		return new LastLocationModel(lastLocation);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_GATEWAY')")
	@RequestMapping(value = "/{gatewayId}/enable", method = RequestMethod.POST)
	public boolean enableDevice(@PathVariable String gatewayId, HttpSession session) {
		String applicationId = getApplicationId(session);
		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is null");

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(gateway.getApplicationId()),
		        "user and gateway must have the same application id");

		return gatewayService.enable(gateway.getId(), getUserId()).isEnabled();
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_GATEWAY')")
	@RequestMapping(value = "/{gatewayId}/disable", method = RequestMethod.POST)
	public boolean disableDevice(@PathVariable String gatewayId, HttpSession session) {
		String applicationId = getApplicationId(session);
		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is null");

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(gateway.getApplicationId()),
		        "user and gateway must have the same application id");

		return gatewayService.disable(gateway.getId(), getUserId()).isEnabled();
	}

	@PreAuthorize("hasAuthority('KRONOS_VIEW_GATEWAY_AUDIT_LOGS')")
	@RequestMapping(value = "/{gatewayId}/logs", method = RequestMethod.POST)
	public AuditLogSearchResultModel listGatewayLogs(@PathVariable String gatewayId,
	        @RequestBody AuditLogSearchFilterModel searchFilter, HttpSession session) {
		String applicationId = getApplicationId(session);
		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is null");

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(gateway.getApplicationId()),
		        "user and gateway must have the same application id");

		return findAuditLogs(gatewayId, applicationId, searchFilter);
	}

	@PreAuthorize("hasAuthority('KRONOS_VIEW_GATEWAY_AUDIT_LOGS')")
	@RequestMapping(value = "/log/options", method = RequestMethod.GET)
	public AuditLogSearchFilterOptions getGatewayLogOptions(HttpSession session) {
		Application application = getApplication(session);
		User authenticatedUser = getAuthenticatedUser();

		List<User> users = getAvailableUsers(application, authenticatedUser);
		String[] types = new String[] { KronosAuditLog.Gateway.CreateGateway, KronosAuditLog.Gateway.GatewayCheckin,
		        KronosAuditLog.Gateway.UpdateGateway, KronosAuditLog.SoftwareReleaseTrans.CreateSoftwareReleaseTrans,
		        KronosAuditLog.SoftwareReleaseTrans.UpdateSoftwareReleaseTrans };

		return new AuditLogSearchFilterOptions(users, types);
	}

	@PreAuthorize("hasAuthority('KRONOS_VIEW_GATEWAY_AUDIT_LOGS')")
	@RequestMapping(value = "/log/{auditLogId}", method = RequestMethod.GET)
	public AuditLogDetails getGatewayLog(@PathVariable String auditLogId, HttpSession session) {
		return findAuditLog(auditLogId, getApplicationId(session));
	}

	@PreAuthorize("hasAuthority('KRONOS_VIEW_GATEWAYS')")
	@RequestMapping(value = "/{gatewayId}/lastheartbeat", method = RequestMethod.GET)
	public HeartbeatModel getLastHeartbeat(@PathVariable String gatewayId, HttpSession session) {
		String applicationId = getApplicationId(session);
		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is null");

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(gateway.getApplicationId()),
		        "user and gateway must have the same application id");

		LastHeartbeat lastHeartbeat = lastHeartbeatService.getLastHeartbeatRepository()
		        .findByObjectTypeAndObjectId(HeartbeatObjectType.GATEWAY, gatewayId);

		return new HeartbeatModel(lastHeartbeat);
	}

	private List<User> getAuthenticatedUserOption(User authenticatedUser) {
		List<User> users = new ArrayList<>(1);
		users.add(authenticatedUser);
		return users;
	}

	@PreAuthorize("hasAuthority('KRONOS_READ_SOFTWARE_RELEASE_TRANS')")
	@RequestMapping(value = "/{gatewayId}/softwareReleaseTrans", method = RequestMethod.POST)
	public SoftwareReleaseTransSearchResultModel getSoftwareReleaseTransRecords(@PathVariable String gatewayId,
	        @RequestBody CoreSearchFilterModel searchFilter, HttpSession session) {
		return getSoftwareReleaseTransRecords(searchFilter, getApplicationId(session), AcnDeviceCategory.GATEWAY,
		        gatewayId);
	}

	@RequestMapping(value = "/{gatewayId}/backups/find", method = RequestMethod.POST)
	public SearchResultModels.ConfigBackupSearchResultModel getGatewayBackups(
	        @RequestBody SearchFilterModels.ConfigBackupSearchFilterOptions searchFilter,
	        @PathVariable String gatewayId, HttpSession session) {

		String applicationId = getApplicationId(session);
		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is null");

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(gateway.getApplicationId()),
		        "user and gateway must have the same application id");

		// sorting & paging
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
		        new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		ConfigBackupSearchParams params = new ConfigBackupSearchParams();
		params.addNames(searchFilter.getName());
		params.addTypes(EnumSet.of(ConfigBackup.Type.GATEWAY));
		params.addObjectIds(gatewayId);

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

	@RequestMapping(value = "/{gatewayId}/backups/create", method = RequestMethod.POST)
	public BackupModels.BackupModel createGatewayBackup(@PathVariable String gatewayId, HttpSession httpSession,
	        @RequestBody CreateConfigBackupModel createConfigBackupModel) {
		Assert.notNull(createConfigBackupModel, "createConfigBackupModel model is null");
		Assert.hasText(createConfigBackupModel.getName(), "name is empty");

		return new BackupModels.BackupModel(
		        createBackup(gatewayId, getApplicationId(httpSession), createConfigBackupModel.getName(), getUserId()));
	}

	@RequestMapping(value = "/backups/create", method = RequestMethod.POST)
	public BulkActionResultModel bulkGatewayBackup(
	        @RequestBody BackupModels.BulkConfigBackupModel bulkConfigBackupModel, HttpSession httpSession) {
		String method = "bulkGatewayBackup";
		Assert.notNull(bulkConfigBackupModel, "bulkConfigBackupModel model is null");
		Assert.notNull(bulkConfigBackupModel.getIds(), "Ids is null");
		Assert.hasText(bulkConfigBackupModel.getName(), "name is empty");

		String applicationId = getApplicationId(httpSession);
		String authenticatedUserId = getUserId();

		int total = bulkConfigBackupModel.getIds().length, numSucceeded = 0, numFailed = 0;
		for (String gatewayId : bulkConfigBackupModel.getIds()) {
			try {
				createBackup(gatewayId, applicationId, bulkConfigBackupModel.getName(), authenticatedUserId);
				numSucceeded++;
			} catch (Exception e) {
				logError(method, "Failed to back up gateway id=" + gatewayId, e);
				numFailed++;
			}
			logInfo(method, "backed up %d gateways of total %d; failed to back up %d gateways", numSucceeded, total,
			        numFailed);
		}

		return new BulkActionResultModel(total, numSucceeded, numFailed);
	}

	@RequestMapping(value = "/{gatewayId}/backups/{backupId}/restore", method = RequestMethod.POST)
	public StatusModel restoreGatewayBackup(@PathVariable(value = "gatewayId") String gatewayId,
	        @PathVariable(value = "backupId") String backupId, HttpSession httpSession) {

		Assert.hasText(backupId, "backupId is empty");

		String applicationId = getApplicationId(httpSession);

		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is null");

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(gateway.getApplicationId()),
		        "user and gateway must have the same application id");

		ConfigBackup configBackup = configBackupService.getConfigBackupRepository().findById(backupId).orElse(null);
		Assert.notNull(configBackup, "configBackup is not found");

		gatewayService.restoreConfiguration(gateway, configBackup, getUserId());

		return StatusModel.OK;
	}

	@RequestMapping(value = "/{gatewayId}/configuration/cloud/restore", method = RequestMethod.POST)
	public StatusModel syncCloudConfigurationToGateway(@PathVariable(value = "gatewayId") String gatewayId,
	        HttpSession httpSession) {

		Assert.hasText(gatewayId, "gatewayId is empty");

		String applicationId = getApplicationId(httpSession);

		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is null");

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(gateway.getApplicationId()),
		        "user and gateway must have the same application id");

		if (deviceService.getDeviceRepository().findAllByGatewayId(gatewayId).size() > 0) {
			Event event = gatewayService.requestConfigurationRestore(gateway.getId());
			Assert.notNull(event, "event is null");
			logInfo("syncCloudConfigurationToGateway",
			        "event has been sent to gatewayId=" + gateway.getId() + ", eventId=" + event.getId());

			return StatusModel.OK.withMessage(event.getId());
		}

		return StatusModel.error("This gateway does not have a devices");
	}

	@RequestMapping(value = "/{gatewayId}/configuration/cloud/sync", method = RequestMethod.POST)
	public StatusModel syncGatewayConfigurationToCloud(@PathVariable(value = "gatewayId") String gatewayId,
	        HttpSession httpSession) {

		Assert.hasText(gatewayId, "gatewayId is empty");
		String applicationId = getApplicationId(httpSession);

		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is null");

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(gateway.getApplicationId()),
		        "user and gateway must have the same application id");

		if (deviceService.getDeviceRepository().findAllByGatewayId(gatewayId).size() > 0) {
			Event event = gatewayService.requestConfigurationUpdate(gateway.getId());
			Assert.notNull(event, "event is null");
			logInfo("syncCloudConfigurationToEvent",
			        "event has been sent to gatewayId=" + gateway.getId() + ", eventId=" + event.getId());

			return StatusModel.OK.withMessage(event.getId());
		}

		return StatusModel.error("This gateway does not have a devices");
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_GATEWAY')")
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

	@PreAuthorize("hasAuthority('KRONOS_EDIT_GATEWAY')")
	@RequestMapping(value = "/bulkEdit", method = RequestMethod.POST)
	public BulkActionResultModel bulkEdit(@RequestBody BulkEditActionModels.GatewayBulkEdit gatewayBulkEdit,
	        HttpSession session) {
		String method = "bulkEdit";

		Assert.notNull(gatewayBulkEdit, "gatewayBulkEdit is null");
		Assert.notNull(gatewayBulkEdit.getIds(), "ids is null");
		Assert.notNull(gatewayBulkEdit.getEditModel(), "gatewayBulkEdit model is null");
		Assert.notNull(gatewayBulkEdit.getEditModel().getEnabledBulkEditField(), "Enabled field is null");
		Assert.notNull(gatewayBulkEdit.getEditModel().getGroupBulkEditField(), "Group field is empty");
		Assert.notNull(gatewayBulkEdit.getEditModel().getOwnerBulkEditField(), "Owner field is empty");

		String[] gatewaysIds = gatewayBulkEdit.getIds();

		String applicationId = getApplicationId(session);
		String authenticatedUserId = getUserId();

		Application application = getCoreCacheService().findApplicationById(applicationId);
		checkEnabled(application, "application");

		int total = gatewaysIds.length;
		int numUpdated = 0;
		int numFailed = 0;

		// analyze bulk edit data
		BulkEditActionModels.BulkEditField groupField = gatewayBulkEdit.getEditModel().getGroupBulkEditField();
		boolean editGroup = groupField.isEdit();
		String groupChoice = groupField.getChoice();

		BulkEditActionModels.BulkEditField ownerField = gatewayBulkEdit.getEditModel().getOwnerBulkEditField();
		boolean editOwner = ownerField.isEdit();
		String ownerChoice = ownerField.getChoice();

		BulkEditActionModels.BulkEditField enabledField = gatewayBulkEdit.getEditModel().getEnabledBulkEditField();
		boolean editEnabled = enabledField.isEdit();
		boolean enabledChoice = enabledField.getChoice().toLowerCase().equals("true");

		if (editGroup || editOwner || editEnabled) {
			for (String gatewayId : gatewaysIds) {
				try {
					Gateway gateway = getKronosCache().findGatewayById(gatewayId);
					if (gateway != null) {
						Assert.isTrue(StringUtils.equals(applicationId, gateway.getApplicationId()),
						        "user and gateway must have the same application id");

						if (editGroup) {
							// edit group
							gateway.setNodeId(groupChoice);
						}

						if (editOwner) {
							// edit owner
							gateway.setUserId(ownerChoice);
						}

						if (editEnabled) {
							// edit enabled
							gateway.setEnabled(enabledChoice);
						}

						gatewayService.update(gateway, authenticatedUserId);
					}
					numUpdated++;
				} catch (Exception e) {
					logError(method, "Failed to update gateway id = " + gatewayId, e);
					numFailed++;
				}
			}
		}

		logInfo(method, "updated %d gateways of total %d; failed to update %d gateways", numUpdated, total, numFailed);

		return new BulkActionResultModel(total, numUpdated, numFailed);
	}

	private Iterable<DeviceType> getUsedDeviceTypes(Gateway gateway, List<Device> devices,
	        Iterable<TestProcedure> usedTestProcedures) {
		Set<String> deviceTypeIds = new HashSet<>();
		deviceTypeIds.add(gateway.getDeviceTypeId());
		devices.stream().forEach(device -> deviceTypeIds.add(device.getDeviceTypeId()));
		for (TestProcedure testProcedure : usedTestProcedures) {
			deviceTypeIds.add(testProcedure.getDeviceTypeId());
		}
		return getDeviceTypeService().getDeviceTypeRepository().findAllById(deviceTypeIds);
	}

	private Iterable<DeviceActionType> getUsedDeviceActionTypes(Gateway gateway, List<Device> devices,
	        Iterable<DeviceType> usedDeviceTypes) {
		Set<String> deviceActionTypeIds = new HashSet<>();
		// add from DeviceAction objects
		devices.stream().forEach(device -> device.getActions().stream()
		        .forEach(action -> deviceActionTypeIds.add(action.getDeviceActionTypeId())));
		// add from DeviceEvent objects
		List<String> deviceIds = devices.stream().map(device -> device.getId()).collect(Collectors.toList());
		if (!deviceIds.isEmpty()) {
			deviceActionTypeIds
			        .addAll(deviceEventService.getDeviceEventRepository().doFindDeviceActionTypeIds(deviceIds));
		}
		// add from device type DeviceAction objects
		for (DeviceType deviceType : usedDeviceTypes) {
			if (deviceType.getActions() == null)
				continue;
			deviceType.getActions().forEach(action -> deviceActionTypeIds.add(action.getDeviceActionTypeId()));
		}
		return deviceActionTypeService.getDeviceActionTypeRepository().findAllById(deviceActionTypeIds);
	}

	private Iterable<TestProcedure> getUsedTestProcedures(Gateway gateway, List<Device> devices) {
		Set<String> objectIds = new HashSet<>();
		objectIds.add(gateway.getId());
		// add from devices
		devices.stream().map(device -> device.getId()).forEach(objectIds::add);
		List<String> testProcedureIds = testResultService.getTestResultRepository().doFindTestProcedureIds(objectIds);
		return testProcedureService.getTestProcedureRepository().findAllById(testProcedureIds);
	}

	private ConfigBackup createBackup(String gatewayId, String applicationId, String name, String userId) {
		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is null");

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		checkEnabled(application, "application");

		// make sure the user and gateway have the same application id
		Assert.isTrue(applicationId.equals(gateway.getApplicationId()),
		        "user and gateway must have the same application id");

		Assert.hasText(gateway.getDeviceTypeId(), "deviceTypeId is empty");
		Assert.hasText(gateway.getName(), "name is empty");
		Assert.hasText(gateway.getOsName(), "osName is empty");
		Assert.hasText(gateway.getSoftwareName(), "softwareName is empty");
		Assert.hasText(gateway.getSoftwareVersion(), "softwareVersion is empty");
		Assert.hasText(gateway.getUid(), "uid is null");

		return gatewayService.backupConfiguration(gateway, name, userId);
	}

	private List<Node> getUsedNodes(Application application, User authenticatedUser) {
		List<String> nodeIds = gatewayService.getGatewayRepository().findAggregatedNodeIds(application.getId(),
		        authenticatedUser.getId());
		return nodeService.getNodeRepository().doFindAllByIdsApplicationIdAndEnabled(nodeIds, application.getId(),
		        true);
	}
}

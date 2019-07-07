package com.arrow.kronos.web.controller;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.acn.client.model.RightToUseType;
import com.arrow.acs.AcsLogicalException;
import com.arrow.acs.KeyValuePair;
import com.arrow.acs.client.model.StatusModel;
import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.BaseDeviceAbstract;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.EligibleFirmwareChangeGroup;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.KronosApplication;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.data.SoftwareReleaseSchedule;
import com.arrow.kronos.data.SoftwareReleaseSchedule.Status;
import com.arrow.kronos.data.SoftwareReleaseTrans;
import com.arrow.kronos.repo.DeviceSearchParams;
import com.arrow.kronos.repo.DeviceTypeSearchParams;
import com.arrow.kronos.repo.GatewaySearchParams;
import com.arrow.kronos.repo.SoftwareReleaseScheduleSearchParams;
import com.arrow.kronos.repo.SoftwareReleaseScheduleStatusSearchParams;
import com.arrow.kronos.repo.SoftwareReleaseTransSearchParams;
import com.arrow.kronos.service.DeviceService;
import com.arrow.kronos.service.GatewayService;
import com.arrow.kronos.service.NodeService;
import com.arrow.kronos.service.SoftwareReleaseScheduleService;
import com.arrow.kronos.service.SoftwareReleaseTransService;
import com.arrow.kronos.web.model.AuditLogModels.AuditLogDetails;
import com.arrow.kronos.web.model.AuditLogModels.AuditLogList;
import com.arrow.kronos.web.model.AuditLogModels.AuditLogParameter;
import com.arrow.kronos.web.model.AuditLogModels.AuditLogParameterList;
import com.arrow.kronos.web.model.DeviceTypeModels.DeviceTypeOption;
import com.arrow.kronos.web.model.NodeModels.NodeOption;
import com.arrow.kronos.web.model.RheaModels.SoftwareReleaseOption;
import com.arrow.kronos.web.model.SearchFilterModels.SoftwareReleaseScheduleAssetSearchFilterModel;
import com.arrow.kronos.web.model.SearchFilterModels.SoftwareReleaseScheduleAuditLogFilterOptions;
import com.arrow.kronos.web.model.SearchFilterModels.SoftwareReleaseScheduleAuditLogSearchFilterModel;
import com.arrow.kronos.web.model.SearchFilterModels.SoftwareReleaseScheduleFilterOptions;
import com.arrow.kronos.web.model.SearchFilterModels.SoftwareReleaseScheduleSearchFilterModel;
import com.arrow.kronos.web.model.SearchFilterModels.SoftwareReleaseScheduleSearchFilterStatusModel;
import com.arrow.kronos.web.model.SearchResultModels.AuditLogSearchResultModel;
import com.arrow.kronos.web.model.SearchResultModels.SoftwareReleaseScheduleAssetSearchResultModel;
import com.arrow.kronos.web.model.SearchResultModels.SoftwareReleaseScheduleSearchResultModel;
import com.arrow.kronos.web.model.SearchResultModels.SoftwareReleaseScheduleSearchResultStatusModel;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.AssetOption;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.AuditLogUserOption;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.CompletedStatus;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.ObjectModel;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.SoftwareReleaseScheduleAssetModel;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.SoftwareReleaseScheduleAuditModel;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.SoftwareReleaseScheduleDetailsModel;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.SoftwareReleaseScheduleList;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.SoftwareReleaseSchedulePendingOptions;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.SoftwareReleaseScheduleSelection;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.SoftwareReleaseScheduleSelectionOptions;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.SoftwareReleaseScheduleStatusModel;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.SoftwareReleaseScheduleSummaryOptions;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.SoftwareReleaseTransProgressMetrics;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.SoftwareReleaseTransStateMetrics;
import com.arrow.kronos.web.model.SoftwareReleaseTransModels.CancelSoftwareReleaseTransactionsRequestModel;
import com.arrow.kronos.web.model.SoftwareReleaseTransModels.RetrySoftwareReleaseTransactionsRequestModel;
import com.arrow.pegasus.data.AuditLog;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Contact;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.repo.AuditLogSearchParams;
import com.arrow.pegasus.repo.DistinctCountResult;
import com.arrow.pegasus.repo.params.NestedPropertySearchParam;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.pegasus.webapi.data.CoreUserModels.UserOption;
import com.arrow.rhea.client.api.ClientSoftwareReleaseApi;
import com.arrow.rhea.data.DeviceProduct;
import com.arrow.rhea.data.SoftwareRelease;

@RestController
@RequestMapping("/api/kronos/softwarereleaseschedule")
public class SoftwareReleaseScheduleController extends BaseControllerAbstract {

	@Autowired
	private SoftwareReleaseScheduleService softwareReleaseScheduleService;

	@Autowired
	private ClientSoftwareReleaseApi clientSoftwareReleaseApi;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private GatewayService gatewayService;

	@Autowired
	private NodeService nodeService;

	@Autowired
	private SoftwareReleaseTransService softwareReleaseTransService;

	private static final String[] SOFTWARE_RELEASE_SCHEDULE_AUDIT_LOG_TYPES = {
			KronosAuditLog.SoftwareReleaseSchedule.CreateSoftwareReleaseSchedule,
			KronosAuditLog.SoftwareReleaseSchedule.UpdateSoftwareReleaseSchedule,
			KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAlert,
			KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetStarted,
			KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetReceived,
			KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetManuallyReceived,
			KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetSucceeded,
			KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetFailed,
			KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetManuallyFailed,
			KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetExpired,
			KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetRetried,
			KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetManuallyRetried,
			KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetCancelled,
			KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetFirmwareDownload,
			KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetTempTokenCreated,
			KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetTempTokenExpired,
			KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetCommandSentToGateway };

	@PreAuthorize("hasAuthority('KRONOS_READ_SOFTWARE_RELEASE_SCHEDULE')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public SoftwareReleaseScheduleSearchResultModel list(
			@RequestBody SoftwareReleaseScheduleSearchFilterModel searchFilter, HttpSession session) {

		// sorting & paging
		PageRequest pageRequest = new PageRequest(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		SoftwareReleaseScheduleSearchParams params = new SoftwareReleaseScheduleSearchParams();
		params.addApplicationIds(getApplicationId(session));
		if (searchFilter.getScheduledDateFrom() != null) {
			params.setFromScheduledDate(Instant.ofEpochMilli(searchFilter.getScheduledDateFrom()));
		}
		if (searchFilter.getScheduledDateTo() != null) {
			params.setToScheduledDate(Instant.ofEpochMilli(searchFilter.getScheduledDateTo()));
		}
		params.setStatuses(searchFilter.getStatuses());
		if (searchFilter.getDeviceCategories() != null && searchFilter.getDeviceCategories().length > 0) {
			EnumSet<AcnDeviceCategory> deviceCategorySet = EnumSet.noneOf(AcnDeviceCategory.class);
			for (String name : searchFilter.getDeviceCategories())
				deviceCategorySet.add(AcnDeviceCategory.valueOf(name));
			params.setDeviceCategories(deviceCategorySet);
		}

		Page<SoftwareReleaseSchedule> softwareReleaseSchedules = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findSoftwareReleaseSchedules(pageRequest, params);

		// convert to visual model
		List<SoftwareReleaseScheduleList> softwareReleaseScheduleModels = new ArrayList<>();
		for (SoftwareReleaseSchedule softwareReleaseSchedule : softwareReleaseSchedules) {
			softwareReleaseScheduleModels.add(new SoftwareReleaseScheduleList(softwareReleaseSchedule,
					getSoftwareReleaseName(softwareReleaseSchedule.getSoftwareReleaseId())));
		}
		Page<SoftwareReleaseScheduleList> result = new PageImpl<>(softwareReleaseScheduleModels, pageRequest,
				softwareReleaseSchedules.getTotalElements());

		return new SoftwareReleaseScheduleSearchResultModel(result, searchFilter);
	}

	@RequestMapping(value = "/filter-options", method = RequestMethod.GET)
	public SoftwareReleaseScheduleFilterOptions filterOptions() {
		SoftwareReleaseScheduleFilterOptions options = new SoftwareReleaseScheduleFilterOptions();

		// List<DeviceCategory> deviceCategories =
		// clientDeviceCategoryApi.findAll(true);
		// List<DeviceCategoryOption> categories = new
		// ArrayList<>(deviceCategories.size());
		// for (DeviceCategory deviceCategory : deviceCategories) {
		// categories.add(new DeviceCategoryOption(deviceCategory));
		// }
		// options.setCategories(categories);

		options.setDeviceCategories(EnumSet.of(AcnDeviceCategory.GATEWAY, AcnDeviceCategory.DEVICE));

		return options;
	}

	@PreAuthorize("hasAuthority('KRONOS_READ_SOFTWARE_RELEASE_SCHEDULE')")
	@RequestMapping(value = "/{softwareReleaseScheduleId}", method = RequestMethod.GET)
	public SoftwareReleaseScheduleDetailsModel read(@PathVariable String softwareReleaseScheduleId,
			HttpSession session) {
		Assert.hasText(softwareReleaseScheduleId, "softwareReleaseScheduleId is empty");

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findById(softwareReleaseScheduleId).orElse(null);
		Assert.isTrue(StringUtils.equals(getApplicationId(session), softwareReleaseSchedule.getApplicationId()),
				"applicationIds mismatch");

		return new SoftwareReleaseScheduleDetailsModel(softwareReleaseSchedule);
	}

	@PreAuthorize("hasAuthority('KRONOS_READ_SOFTWARE_RELEASE_SCHEDULE')")
	@RequestMapping(value = "/{softwareReleaseScheduleId}/audit", method = RequestMethod.GET)
	public SoftwareReleaseScheduleAuditModel audit(@PathVariable String softwareReleaseScheduleId,
			HttpSession session) {
		Assert.hasText(softwareReleaseScheduleId, "softwareReleaseScheduleId is empty");

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findById(softwareReleaseScheduleId).orElse(null);
		Assert.isTrue(StringUtils.equals(getApplicationId(session), softwareReleaseSchedule.getApplicationId()),
				"applicationIds mismatch");

		SoftwareReleaseScheduleAuditModel model = new SoftwareReleaseScheduleAuditModel(softwareReleaseSchedule);
		model.setProgressMetrics(calculateSoftwareTransProgressMetrics(softwareReleaseSchedule));
		fillStatusModel(model, softwareReleaseSchedule);
		return model;
	}

	@PreAuthorize("hasAuthority('KRONOS_CREATE_SOFTWARE_RELEASE_SCHEDULE') or hasAuthority('KRONOS_UPDATE_SOFTWARE_RELEASE_SCHEDULE')")
	@RequestMapping(value = "/selection-options", method = RequestMethod.POST)
	public SoftwareReleaseScheduleSelectionOptions selectionOptions(
			@RequestBody SoftwareReleaseScheduleSelection selection, HttpSession session) {

		String method = "selectionOptions";
		logDebug(method, "...");

		Application application = getApplication(session);
		KronosApplication kronosApplication = getKronosCache()
				.findKronosApplicationByApplicationId(application.getId());

		boolean hasFirmwareManagementFeature = false;
		if (application.getProductFeatures() != null && !application.getProductFeatures().isEmpty()
				&& application.getProductFeatures().contains(KronosConstants.ProductFeatures.FIRMWARE_MANAGEMENT))
			hasFirmwareManagementFeature = true;
		logDebug(method, "hasFirmwareManagementFeature: " + hasFirmwareManagementFeature);

		SoftwareReleaseScheduleSelectionOptions options = new SoftwareReleaseScheduleSelectionOptions();

		// 1. fill missing ids in the selection object if possible
		if (selection.getDeviceTypeId() == null && selection.getSoftwareReleaseScheduleId() != null) {
			SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
					.getSoftwareReleaseScheduleRepository().findById(selection.getSoftwareReleaseScheduleId())
					.orElse(null);
			if (softwareReleaseSchedule != null && softwareReleaseSchedule.getObjectIds() != null
					&& !softwareReleaseSchedule.getObjectIds().isEmpty()) {
				String objectId = softwareReleaseSchedule.getObjectIds().get(0);
				String deviceTypeId = null;
				switch (softwareReleaseSchedule.getDeviceCategory()) {
				case DEVICE:
					Device device = getKronosCache().findDeviceById(objectId);
					if (device != null) {
						deviceTypeId = device.getDeviceTypeId();
					}
					break;
				case GATEWAY:
					Gateway gateway = getKronosCache().findGatewayById(objectId);
					if (gateway != null) {
						deviceTypeId = gateway.getDeviceTypeId();
					}
					break;
				}
				selection.setDeviceTypeId(deviceTypeId);
				selection.setDeviceCategory(softwareReleaseSchedule.getDeviceCategory());
			}
		}

		// 2. get available device types
		PageRequest pageRequest = new PageRequest(0, Integer.MAX_VALUE, new Sort(Direction.ASC, "name"));
		DeviceTypeSearchParams params = new DeviceTypeSearchParams();
		params.addApplicationIds(application.getId());
		params.setEnabled(true);
		params.setRheaDeviceTypeDefined(true);

		Page<DeviceType> deviceTypes = getDeviceTypeService().getDeviceTypeRepository().findDeviceTypes(pageRequest,
				params);

		List<DeviceTypeOption> deviceTypeModels = new ArrayList<>(deviceTypes.getNumberOfElements());
		for (DeviceType deviceType : deviceTypes) {
			deviceTypeModels.add(new DeviceTypeOption(deviceType));
		}
		options.setDeviceTypes(deviceTypeModels);

		// 3. get software releases for the selected device type
		if (StringUtils.isNotEmpty(selection.getDeviceTypeId())) {
			DeviceType deviceType = getKronosCache().findDeviceTypeById(selection.getDeviceTypeId());
			Assert.notNull(deviceType, "deviceType is null");
			selection.setDeviceCategory(deviceType.getDeviceCategory());

			com.arrow.rhea.data.DeviceType rheaDeviceType = getRheaCacheService()
					.findDeviceTypeById(deviceType.getRheaDeviceTypeId());
			Assert.notNull(rheaDeviceType, "rheaDeviceType is null");

			DeviceProduct deviceProduct = getRheaCacheService()
					.findDeviceProductById(rheaDeviceType.getDeviceProductId());
			Assert.notNull(deviceProduct, "deviceProduct is null");
			selection.setDeviceCategory(deviceProduct.getDeviceCategory());

			Assert.isTrue(deviceType.getDeviceCategory() == deviceProduct.getDeviceCategory(),
					"Invalid asset mapping! Asset Category: " + deviceType.getDeviceCategory()
							+ " and Hardware Product Category: " + deviceProduct.getDeviceCategory());

			// 3.1 check if firmware management feature was purchased
			EnumSet<RightToUseType> rtuTypes = EnumSet.of(RightToUseType.Unrestricted);
			if (hasFirmwareManagementFeature) {
				rtuTypes.add(RightToUseType.Public);
			}

			String[] rtuTypeNames = new String[rtuTypes.size()];
			int rightToUseNameCount = 0;
			for (RightToUseType rtut : rtuTypes) {
				rtuTypeNames[rightToUseNameCount++] = rtut.name();
			}

			List<SoftwareRelease> softwareReleases = clientSoftwareReleaseApi.findAll(null, null,
					new String[] { rheaDeviceType.getId() }, rtuTypeNames, true, null);
			if (hasFirmwareManagementFeature) {
				softwareReleases.addAll(softwareReleaseScheduleService.getPrivateApprovedSwReleases(
						application.getCompanyId(), new String[] { rheaDeviceType.getId() }));
			}

			List<SoftwareReleaseOption> softwareReleaseOptions = new ArrayList<>();
			for (SoftwareRelease softwareRelease : softwareReleases) {
				softwareReleaseOptions.add(new SoftwareReleaseOption(softwareRelease, getRheaCacheService()
						.findSoftwareProductById(softwareRelease.getSoftwareProductId()).getName()));
			}
			softwareReleaseOptions
					.sort(Comparator.comparing(SoftwareReleaseOption::getName, String.CASE_INSENSITIVE_ORDER));

			options.setSoftwareReleases(softwareReleaseOptions);

		}

		// 4. timezones
		List<String> zoneIds = new ArrayList<>(ZoneId.getAvailableZoneIds());
		zoneIds.sort(String::compareToIgnoreCase);
		options.setTimezones(zoneIds);

		// 5. DefaultSoftwareReleaseEmails
		options.setDefaultSoftwareReleaseEmails(kronosApplication.getDefaultSoftwareReleaseEmails());

		// 6. Get available objects
		if (!StringUtils.isEmpty(selection.getSoftwareReleaseId()) && selection.getDeviceCategory() != null) {
			SoftwareRelease softwareRelease = getRheaCacheService()
					.findSoftwareReleaseById(selection.getSoftwareReleaseId());
			List<ObjectModel> objectModels = new ArrayList<>();
			if (softwareRelease.getUpgradeableFromIds() != null && !softwareRelease.getUpgradeableFromIds().isEmpty()) {
				List<Node> nodes = nodeService.getNodeRepository().findByApplicationIdAndEnabled(application.getId(),
						true);
				Map<String, Node> nodesMap = new HashMap<>();
				for (Node node : nodes) {
					nodesMap.put(node.getId(), node);
				}

				// SoftwareReleaseScheduleSearchParams scheduleParams = new
				// SoftwareReleaseScheduleSearchParams();
				// scheduleParams.addApplicationIds(application.getId());
				// List<SoftwareReleaseSchedule> schedules =
				// softwareReleaseScheduleService
				// .getSoftwareReleaseScheduleRepository().findSoftwareReleaseSchedules(scheduleParams);
				// Set<String> scheduledDevices = schedules.stream()
				// .filter(schedule -> schedule.getStatus() == Status.SCHEDULED
				// || schedule.getStatus() == Status.INPROGRESS)
				// .map(SoftwareReleaseSchedule::getObjectIds).flatMap(Collection::stream)
				// .collect(Collectors.toSet());

				switch (selection.getDeviceCategory()) {
				case DEVICE:
					List<Device> availableDevices;
					if (selection.getDeviceTypeId() != null && !selection.getDeviceTypeId().isEmpty()) {
						availableDevices = getDevicesForUpgrade(application.getId(), softwareRelease,
								new String[] { selection.getDeviceTypeId() });
					} else {
						availableDevices = getDevicesForUpgrade(application.getId(), softwareRelease);
					}

					if (availableDevices.size() > 0) {
						Set<String> deviceIdSet = new HashSet<>();
						availableDevices.forEach(device -> {
							deviceIdSet.add(device.getId());
						});

						SoftwareReleaseTransSearchParams softwareReleaseTransSearchParams = new SoftwareReleaseTransSearchParams();
						softwareReleaseTransSearchParams.addApplicationIds(application.getId());
						softwareReleaseTransSearchParams.setDeviceCategories(EnumSet.of(AcnDeviceCategory.DEVICE));
						softwareReleaseTransSearchParams
								.addObjectIds(deviceIdSet.toArray(new String[deviceIdSet.size()]));
						List<SoftwareReleaseTrans> existingDeviceTransactions = softwareReleaseTransService
								.getSoftwareReleaseTransRepository()
								.findSoftwareReleaseTrans(softwareReleaseTransSearchParams);

						Set<String> invalidDevices = new HashSet<>();
						for (SoftwareReleaseTrans softwareReleaseTrans : existingDeviceTransactions) {
							if (softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.PENDING
									|| softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.INPROGRESS
									|| softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.RECEIVED
									|| softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.EXPIRED
									|| softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.ERROR)
								invalidDevices.add(softwareReleaseTrans.getObjectId());
						}

						availableDevices.forEach(device -> {
							final ObjectModel objectModel = populateObjectNames(
									new ObjectModel(device, invalidDevices.contains(device.getId())), nodesMap);
							DeviceType assetType = getAssetType(device.getDeviceTypeId());
							Assert.hasText(assetType.getRheaDeviceTypeId(),
									"rheaDeviceTypeId is empty! assetType: " + assetType.getName());
							objectModel.setHwVersionName(getHardwareVersionName(assetType.getRheaDeviceTypeId()));
							objectModels.add(objectModel);
						});
					}
					break;
				case GATEWAY:

					List<Gateway> availableGateways;
					if (selection.getDeviceTypeId() != null && !selection.getDeviceTypeId().isEmpty()) {
						availableGateways = getGatewaysForUpgrade(application.getId(), softwareRelease,
								new String[] { selection.getDeviceTypeId() });
					} else {
						availableGateways = getGatewaysForUpgrade(application.getId(), softwareRelease);
					}

					if (availableGateways.size() > 0) {
						Set<String> gatewayIdSet = new HashSet<>();
						availableGateways.forEach(device -> {
							gatewayIdSet.add(device.getId());
						});

						SoftwareReleaseTransSearchParams softwareReleaseTransSearchParams = new SoftwareReleaseTransSearchParams();
						softwareReleaseTransSearchParams.addApplicationIds(application.getId());
						softwareReleaseTransSearchParams.setDeviceCategories(EnumSet.of(AcnDeviceCategory.GATEWAY));
						softwareReleaseTransSearchParams
								.addObjectIds(gatewayIdSet.toArray(new String[gatewayIdSet.size()]));
						List<SoftwareReleaseTrans> existingDeviceTransactions = softwareReleaseTransService
								.getSoftwareReleaseTransRepository()
								.findSoftwareReleaseTrans(softwareReleaseTransSearchParams);

						Set<String> invalidGateways = new HashSet<>();
						for (SoftwareReleaseTrans softwareReleaseTrans : existingDeviceTransactions) {
							if (softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.PENDING
									|| softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.INPROGRESS
									|| softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.RECEIVED
									|| softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.EXPIRED
									|| softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.ERROR)
								invalidGateways.add(softwareReleaseTrans.getObjectId());
						}

						availableGateways.forEach(device -> {
							final ObjectModel objectModel = populateObjectNames(
									new ObjectModel(device, invalidGateways.contains(device.getId())), nodesMap);
							DeviceType assetType = getAssetType(device.getDeviceTypeId());
							Assert.hasText(assetType.getRheaDeviceTypeId(),
									"rheaDeviceTypeId is empty! assetType: " + assetType.getName());
							objectModel.setHwVersionName(getHardwareVersionName(assetType.getRheaDeviceTypeId()));
							objectModels.add(objectModel);
						});
					}
					break;
				}
			}
			options.setAvailableObjects(objectModels);
		}
		options.setSelection(selection);
		return options;
	}

	@PreAuthorize("hasAuthority('KRONOS_CREATE_SOFTWARE_RELEASE_SCHEDULE')")
	@RequestMapping(method = RequestMethod.POST)
	public SoftwareReleaseScheduleDetailsModel create(
			@RequestBody SoftwareReleaseScheduleDetailsModel softwareReleaseScheduleModel, HttpSession session) {
		Assert.notNull(softwareReleaseScheduleModel, "SoftwareReleaseScheduleDetailsModel is null");
		Assert.notNull(softwareReleaseScheduleModel.getSoftwareReleaseId(),
				"softwareReleaseId in SoftwareReleaseScheduleDetailsModel is null");
		Assert.isTrue(
				softwareReleaseScheduleModel.isOnDemand() || softwareReleaseScheduleModel.getScheduledDate() != null,
				"scheduledDate in SoftwareReleaseScheduleDetailsModel is null or on demand should be true");

		String method = "create";

		SoftwareReleaseSchedule softwareReleaseSchedule = new SoftwareReleaseSchedule();
		softwareReleaseSchedule.setApplicationId(getApplicationId(session)); // mandatory
		softwareReleaseSchedule.setName(softwareReleaseScheduleModel.getJobName());
		softwareReleaseSchedule.setDeviceTypeId(softwareReleaseScheduleModel.getDeviceTypeId());
		softwareReleaseSchedule.setDeviceCategory(softwareReleaseScheduleModel.getDeviceCategory());
		softwareReleaseSchedule.setComments(softwareReleaseScheduleModel.getComments());
		softwareReleaseSchedule.setObjectIds(softwareReleaseScheduleModel.getObjectIds());
		softwareReleaseSchedule.setNotifyEmails(softwareReleaseScheduleModel.getNotifyEmails());
		softwareReleaseSchedule.setNotifyOnEnd(softwareReleaseScheduleModel.isNotifyOnEnd());
		softwareReleaseSchedule.setNotifyOnStart(softwareReleaseScheduleModel.isNotifyOnStart());
		softwareReleaseSchedule.setNotifyOnSubmit(softwareReleaseScheduleModel.isNotifyOnSubmitted());
		softwareReleaseSchedule.setOnDemand(softwareReleaseScheduleModel.isOnDemand());
		softwareReleaseSchedule.setScheduledDate(getScheduledDate(softwareReleaseScheduleModel.getScheduledDate(),
				softwareReleaseScheduleModel.getLocalTimezone(), softwareReleaseScheduleModel.getTargetTimezone()));

		if (softwareReleaseScheduleModel.getTimeToExpireSeconds() != null
				&& softwareReleaseScheduleModel.getTimeToExpireSeconds() > 0)
			softwareReleaseSchedule.setTimeToExpireSeconds(softwareReleaseScheduleModel.getTimeToExpireSeconds());

		softwareReleaseSchedule.setSoftwareReleaseId(softwareReleaseScheduleModel.getSoftwareReleaseId());
		softwareReleaseSchedule.setStatus(Status.SCHEDULED);

		DeviceType assetType = getKronosCache().findDeviceTypeById(softwareReleaseScheduleModel.getDeviceTypeId());
		Assert.notNull(assetType,
				"deviceType not found! deviceTypeId: " + softwareReleaseScheduleModel.getDeviceTypeId());
		Assert.hasText(assetType.getRheaDeviceTypeId(), "rheaDeviceTypeId is empty");

		com.arrow.rhea.data.DeviceType hardwareVersion = getRheaCacheService()
				.findDeviceTypeById(assetType.getRheaDeviceTypeId());
		Assert.notNull(hardwareVersion,
				"hardwareVersion not found! rheaDeviceTypeId: " + assetType.getRheaDeviceTypeId());
		softwareReleaseSchedule.setHardwareVersionId(hardwareVersion.getId());

		softwareReleaseSchedule = softwareReleaseScheduleService.create(softwareReleaseSchedule, getUserId());

		return new SoftwareReleaseScheduleDetailsModel(softwareReleaseSchedule);
	}

	@PreAuthorize("hasAuthority('KRONOS_UPDATE_SOFTWARE_RELEASE_SCHEDULE')")
	@RequestMapping(value = "/{softwareReleaseScheduleId}", method = RequestMethod.PUT)
	public SoftwareReleaseScheduleDetailsModel edit(@PathVariable String softwareReleaseScheduleId,
			@RequestBody SoftwareReleaseScheduleDetailsModel softwareReleaseScheduleModel, HttpSession session) {
		Assert.hasText(softwareReleaseScheduleId, "softwareReleaseScheduleId is empty");
		Assert.notNull(softwareReleaseScheduleModel, "softwareReleaseScheduleModel is null");

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findById(softwareReleaseScheduleId).orElse(null);
		Assert.isTrue(StringUtils.equals(getApplicationId(session), softwareReleaseSchedule.getApplicationId()),
				"applicationIds mismatch");
		softwareReleaseSchedule.setName(softwareReleaseScheduleModel.getJobName());
		softwareReleaseSchedule.setDeviceTypeId(softwareReleaseScheduleModel.getDeviceTypeId());
		softwareReleaseSchedule.setDeviceCategory(softwareReleaseScheduleModel.getDeviceCategory());
		softwareReleaseSchedule.setComments(softwareReleaseScheduleModel.getComments());
		softwareReleaseSchedule.setObjectIds(softwareReleaseScheduleModel.getObjectIds());
		softwareReleaseSchedule.setNotifyEmails(softwareReleaseScheduleModel.getNotifyEmails());
		softwareReleaseSchedule.setNotifyOnEnd(softwareReleaseScheduleModel.isNotifyOnEnd());
		softwareReleaseSchedule.setNotifyOnStart(softwareReleaseScheduleModel.isNotifyOnStart());
		softwareReleaseSchedule.setNotifyOnSubmit(softwareReleaseScheduleModel.isNotifyOnSubmitted());
		softwareReleaseSchedule.setOnDemand(softwareReleaseScheduleModel.isOnDemand());
		softwareReleaseSchedule.setScheduledDate(getScheduledDate(softwareReleaseScheduleModel.getScheduledDate(),
				softwareReleaseScheduleModel.getLocalTimezone(), softwareReleaseScheduleModel.getTargetTimezone()));
		softwareReleaseSchedule.setSoftwareReleaseId(softwareReleaseScheduleModel.getSoftwareReleaseId());
		softwareReleaseSchedule.setTimeToExpireSeconds(softwareReleaseScheduleModel.getTimeToExpireSeconds());

		softwareReleaseSchedule = softwareReleaseScheduleService.update(softwareReleaseSchedule, getUserId());

		return new SoftwareReleaseScheduleDetailsModel(softwareReleaseSchedule);
	}

	@PreAuthorize("hasAuthority('KRONOS_READ_SOFTWARE_RELEASE_SCHEDULE')")
	@RequestMapping(value = "/{softwareReleaseScheduleId}/logs", method = RequestMethod.POST)
	public AuditLogSearchResultModel findSoftwareReleaseScheduleAuditLogs(
			@PathVariable String softwareReleaseScheduleId,
			@RequestBody SoftwareReleaseScheduleAuditLogSearchFilterModel searchFilter, HttpSession session) {

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findById(softwareReleaseScheduleId).orElse(null);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is not found");

		String applicationId = getApplicationId(session);
		Assert.isTrue(StringUtils.equals(applicationId, softwareReleaseSchedule.getApplicationId()),
				"applicationIds mismatch");
		PageRequest pageRequest = new PageRequest(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		Instant createdDateFrom = null, createdDateTo = null;
		if (searchFilter.getCreatedDateFrom() != null)
			createdDateFrom = Instant.ofEpochSecond(searchFilter.getCreatedDateFrom());

		if (searchFilter.getCreatedDateTo() != null)
			createdDateTo = Instant.ofEpochSecond(searchFilter.getCreatedDateTo());

		AuditLogSearchParams auditLogSearchParams = new AuditLogSearchParams();
		auditLogSearchParams.addApplicationIds(applicationId);
		if (searchFilter.getTypes() != null && searchFilter.getTypes().length > 0) {
			auditLogSearchParams.addTypes(searchFilter.getTypes());
		} else {
			auditLogSearchParams.addTypes(SOFTWARE_RELEASE_SCHEDULE_AUDIT_LOG_TYPES);
		}
		auditLogSearchParams.addObjectIds(softwareReleaseScheduleId);
		auditLogSearchParams.addCreatedDateFrom(createdDateFrom);
		auditLogSearchParams.addCreatedDateTo(createdDateTo);
		auditLogSearchParams.addCreatedBys(searchFilter.getUserIds());
		if (searchFilter.getAssetIds() != null && searchFilter.getAssetIds().length > 0)
			auditLogSearchParams.addNestedProperties(
					new NestedPropertySearchParam("parameters.objectId", searchFilter.getAssetIds()));

		Page<AuditLog> auditLogPage = getAuditLogService().getAuditLogRepository().findAuditLogs(pageRequest,
				auditLogSearchParams);

		// Page<AuditLog> auditLogPage =
		// getAuditLogService().getAuditLogRepository().findAuditLogs(pageRequest,
		// null,
		// new String[] { applicationId }, searchFilter.getTypes(), new String[]
		// { softwareReleaseScheduleId },
		// null, createdDateFrom, createdDateTo, searchFilter.getUserIds());

		List<AuditLogList> models = new ArrayList<>();
		for (AuditLog auditLog : auditLogPage) {
			AuditLogList model = new AuditLogList(auditLog, null);

			String createdBy = getRequestorName(auditLog.getCreatedBy());
			model.withCreatedBy(createdBy);

			Map<String, String> nestedParameters = auditLog.getParameters();
			if (nestedParameters != null) {
				StringBuffer logMessage = new StringBuffer();
				if (StringUtils.equalsIgnoreCase(auditLog.getType(),
						KronosAuditLog.SoftwareReleaseSchedule.CreateSoftwareReleaseSchedule)) {
					// TODO
				} else if (StringUtils.equalsIgnoreCase(auditLog.getType(),
						KronosAuditLog.SoftwareReleaseSchedule.UpdateSoftwareReleaseSchedule)) {
					AuditLogParameterList log = new AuditLogParameterList(auditLog, null);
					log.setCreatedBy(createdBy);
					if (!StringUtils.equals(nestedParameters.get("newStatus"), nestedParameters.get("oldStatus"))) {
						log.addParameter(new AuditLogParameter().withParameter("status")
								.withOldValue(nestedParameters.get("oldStatus"))
								.withNewValue(nestedParameters.get("newStatus")));
					}
					if (!StringUtils.equals(nestedParameters.get("newOnDemand"), nestedParameters.get("oldOnDemand"))
							|| !StringUtils.equals(nestedParameters.get("newScheduledDate"),
									nestedParameters.get("oldScheduledDate"))) {
						log.addParameter(
								new AuditLogParameter().withParameter("scheduledDate")
										.withOldValue(StringUtils.equals(nestedParameters.get("oldOnDemand"), "true")
												? "On Demand"
												: nestedParameters.get("oldScheduledDate"))
										.withNewValue(StringUtils.equals(nestedParameters.get("newOnDemand"), "true")
												? "On Demand"
												: nestedParameters.get("newScheduledDate")));
					}
					if (!StringUtils.equals(nestedParameters.get("newObjectIds"),
							nestedParameters.get("oldObjectIds"))) {
						Set<String> newIds = getObjectIdsFromLogParams(nestedParameters.get("newObjectIds"));
						Set<String> oldIds = getObjectIdsFromLogParams(nestedParameters.get("oldObjectIds"));
						Set<String> idsAdded = new HashSet<>(newIds);
						idsAdded.removeAll(oldIds);
						Set<String> idsRemoved = new HashSet<>(oldIds);
						idsRemoved.removeAll(newIds);
						idsAdded.forEach(id -> {
							String assetName = getAssetName(softwareReleaseSchedule.getDeviceCategory(), id);
							log.addParameter(
									new AuditLogParameter().withParameter("assetAdded").withNewValue(assetName));
						});
						idsRemoved.forEach(id -> {
							String assetName = getAssetName(softwareReleaseSchedule.getDeviceCategory(), id);
							log.addParameter(
									new AuditLogParameter().withParameter("assetRemoved").withOldValue(assetName));
						});
					}
					model = log;
				} else if (StringUtils.equals(auditLog.getType(),
						KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAlert)) {
					AuditLogParameterList log = new AuditLogParameterList(auditLog, null);
					log.setCreatedBy(createdBy);
					if (StringUtils.isNotBlank(nestedParameters.get("objectId"))) {
						String assetName = getAssetName(softwareReleaseSchedule.getDeviceCategory(),
								nestedParameters.get("objectId"));
						log.addParameter(new AuditLogParameter().withParameter("assetName").withNewValue(assetName));
					}
					model = log;
				} else if (StringUtils.equalsIgnoreCase(auditLog.getType(),
						KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetStarted)
						|| StringUtils.equalsIgnoreCase(auditLog.getType(),
								KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetReceived)
						|| StringUtils.equalsIgnoreCase(auditLog.getType(),
								KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetManuallyReceived)
						|| StringUtils.equalsIgnoreCase(auditLog.getType(),
								KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetSucceeded)
						|| StringUtils.equalsIgnoreCase(auditLog.getType(),
								KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetExpired)
						|| StringUtils.equalsIgnoreCase(auditLog.getType(),
								KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetRetried)
						|| StringUtils.equalsIgnoreCase(auditLog.getType(),
								KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetManuallyRetried)
						|| StringUtils.equalsIgnoreCase(auditLog.getType(),
								KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetCancelled)
						|| StringUtils.equalsIgnoreCase(auditLog.getType(),
								KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetFirmwareDownload)
						|| StringUtils.equalsIgnoreCase(auditLog.getType(),
								KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetTempTokenCreated)
						|| StringUtils.equalsIgnoreCase(auditLog.getType(),
								KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetTempTokenExpired)
						|| StringUtils.equalsIgnoreCase(auditLog.getType(),
								KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetCommandSentToGateway)
						|| StringUtils.equalsIgnoreCase(auditLog.getType(),
								KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetFailed)
						|| StringUtils.equalsIgnoreCase(auditLog.getType(),
								KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetManuallyFailed)) {

					String assetUid = nestedParameters.get(KronosAuditLog.SoftwareReleaseSchedule.Params.ASSET_UID);
					String assetName = nestedParameters.get(KronosAuditLog.SoftwareReleaseSchedule.Params.ASSET_NAME);
					String errorMessage = nestedParameters.get(KronosAuditLog.SoftwareReleaseSchedule.Params.ERROR);

					if (StringUtils.isNotEmpty(assetUid))
						logMessage.append(assetUid);
					if (StringUtils.isNotEmpty(assetName)) {

						if (logMessage.length() > 0)
							logMessage.append("\r\n");
						logMessage.append(assetName);
					}

					if (StringUtils.isNotEmpty(errorMessage)) {

						if (logMessage.length() > 0)
							logMessage.append("\r\n");
						logMessage.append(errorMessage);
					}
				} else {
					// do nothing
				}

				model.setLogMessage(logMessage.length() > 0 ? logMessage.toString() : null);
			}
			models.add(model);
		}
		Page<AuditLogList> result = new PageImpl<>(models, pageRequest, auditLogPage.getTotalElements());

		return new AuditLogSearchResultModel(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('KRONOS_READ_SOFTWARE_RELEASE_SCHEDULE')")
	@RequestMapping(value = "/log/options/{softwareReleaseScheduleId}", method = RequestMethod.GET)
	public SoftwareReleaseScheduleAuditLogFilterOptions getAuditLogFilterOptions(HttpSession session,
			@PathVariable String softwareReleaseScheduleId) {
		Application application = getApplication(session);

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findById(softwareReleaseScheduleId).orElse(null);
		Assert.notNull(softwareReleaseSchedule,
				"softwareReleaseSchedule not found! softwareReleaseScheduleId = " + softwareReleaseScheduleId);

		AuditLogSearchParams params = new AuditLogSearchParams();
		params.addApplicationIds(application.getId());
		params.addObjectIds(softwareReleaseScheduleId);
		params.addTypes(SOFTWARE_RELEASE_SCHEDULE_AUDIT_LOG_TYPES);

		// find distinct log types
		List<DistinctCountResult> distinctTypes = getAuditLogService().getAuditLogRepository()
				.countDistinctProperty(params, "type");
		List<String> typeNames = distinctTypes.stream().map(DistinctCountResult::getName).collect(Collectors.toList());

		// find distinct createdBys
		List<DistinctCountResult> distinctCreatedBys = getAuditLogService().getAuditLogRepository()
				.countDistinctProperty(params, "createdBy");
		Map<String, Set<String>> createdBys = new HashMap<>();
		distinctCreatedBys.forEach(createdBy -> {
			String requestorName = getRequestorName(createdBy.getName());
			requestorName = requestorName != null ? requestorName : "Unknown";
			if (createdBys.containsKey(requestorName)) {
				createdBys.get(requestorName).add(createdBy.getName());
			} else {
				HashSet<String> ids = new HashSet<>();
				ids.add(createdBy.getName());
				createdBys.put(requestorName, ids);
			}
		});
		List<AuditLogUserOption> users = createdBys.entrySet().stream()
				.map(entry -> new AuditLogUserOption(entry.getKey(), entry.getValue())).collect(Collectors.toList());

		// find distinct assets
		List<DistinctCountResult> distinctObjectIds = getAuditLogService().getAuditLogRepository()
				.countDistinctProperty(params, "parameters.objectId");
		List<KeyValuePair<String, String>> assets = new ArrayList<>();
		for (DistinctCountResult distinctCountResult : distinctObjectIds) {
			if (StringUtils.isNotEmpty(distinctCountResult.getName())) {
				BaseDeviceAbstract asset = getAsset(softwareReleaseSchedule.getDeviceCategory(),
						distinctCountResult.getName());

				if (asset != null) {
					KeyValuePair<String, String> pair = new KeyValuePair<>(asset.getId(),
							asset.getName() + " (" + asset.getUid() + ")");
					assets.add(pair);
				} else {
					KeyValuePair<String, String> pair = new KeyValuePair<>(distinctCountResult.getName(),
							"Unknown, Asset was Deleted");
					assets.add(pair);
				}
			}
		}

		SoftwareReleaseScheduleAuditLogFilterOptions model = new SoftwareReleaseScheduleAuditLogFilterOptions(
				softwareReleaseSchedule.getCreatedDate().toEpochMilli(), users, typeNames, assets);

		return model;
	}

	@PreAuthorize("hasAuthority('KRONOS_READ_SOFTWARE_RELEASE_SCHEDULE')")
	@RequestMapping(value = "/log/{auditLogId}", method = RequestMethod.GET)
	public AuditLogDetails getSoftwareReleaseScheduleAuditLog(@PathVariable String auditLogId, HttpSession session) {
		return findAuditLog(auditLogId, getApplicationId(session));
	}

	@SuppressWarnings("unused")
	@PreAuthorize("hasAuthority('KRONOS_READ_SOFTWARE_RELEASE_SCHEDULE')")
	@RequestMapping(value = "/{softwareReleaseScheduleId}/assets", method = RequestMethod.POST)
	public SoftwareReleaseScheduleAssetSearchResultModel listAssets(@PathVariable String softwareReleaseScheduleId,
			@RequestBody SoftwareReleaseScheduleAssetSearchFilterModel searchFilter, HttpSession session) {

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findById(softwareReleaseScheduleId).orElse(null);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is not found");

		String applicationId = getApplicationId(session);
		Assert.isTrue(StringUtils.equals(applicationId, softwareReleaseSchedule.getApplicationId()),
				"applicationIds mismatch");

		Sort sort = new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField());

		// always sort by uid if job is either cancelled or scheduled
		if (softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.SCHEDULED
				|| softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.CANCELLED)
			sort = new Sort(Direction.valueOf(searchFilter.getSortDirection()), "uid");

		PageRequest pageRequest = new PageRequest(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(), sort);

		long totalElements = 0;
		List<SoftwareReleaseScheduleAssetModel> content = new ArrayList<>();

		if (softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.SCHEDULED
				|| softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.CANCELLED) {
			// transactions do not exist yet when in either scheduled or
			// cancelled status, must lookup assets by device or gateway entity

			if (softwareReleaseSchedule.getDeviceCategory() == AcnDeviceCategory.GATEWAY) {
				Set<String> objectIdsSet = new HashSet<>(softwareReleaseSchedule.getObjectIds());

				GatewaySearchParams params = new GatewaySearchParams();
				params.addIds(softwareReleaseSchedule.getObjectIds()
						.toArray(new String[softwareReleaseSchedule.getObjectIds().size()]));
				Page<Gateway> gateways = gatewayService.getGatewayRepository().findGateways(pageRequest, params);
				totalElements = softwareReleaseSchedule.getObjectIds().size();

				for (Gateway gateway : gateways) {
					// remove for set
					objectIdsSet.remove(gateway.getId());

					SoftwareReleaseScheduleAssetModel model = new SoftwareReleaseScheduleAssetModel(null, null,
							gateway.getName());
					model.setUid(gateway.getUid());
					model.setFirmwareFrom(getSoftwareReleaseName(gateway.getSoftwareReleaseId()));
					model.setStatus("N/A");
					if (softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.CANCELLED)
						model.setStatus(SoftwareReleaseTrans.Status.CANCELLED.name());

					content.add(model);
				}

				List<SoftwareReleaseScheduleAssetModel> unknownAssets = getUnknownAssets(gateways,
						softwareReleaseSchedule);
				if (!unknownAssets.isEmpty()) {
					content.addAll(unknownAssets);
				}
			} else if (softwareReleaseSchedule.getDeviceCategory() == AcnDeviceCategory.DEVICE) {
				Set<String> objectIdsSet = new HashSet<>(softwareReleaseSchedule.getObjectIds());

				DeviceSearchParams params = new DeviceSearchParams();
				params.addIds(softwareReleaseSchedule.getObjectIds()
						.toArray(new String[softwareReleaseSchedule.getObjectIds().size()]));
				Page<Device> devices = deviceService.getDeviceRepository().doFindDevices(pageRequest, params);
				totalElements = softwareReleaseSchedule.getObjectIds().size();

				for (Device device : devices) {
					// remove for set
					objectIdsSet.remove(device.getId());

					SoftwareReleaseScheduleAssetModel model = new SoftwareReleaseScheduleAssetModel(null, null,
							device.getName());
					model.setUid(device.getUid());
					model.setFirmwareFrom(getSoftwareReleaseName(device.getSoftwareReleaseId()));
					model.setStatus("N/A");
					if (softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.CANCELLED)
						model.setStatus(SoftwareReleaseTrans.Status.CANCELLED.name());

					content.add(model);
				}

				List<SoftwareReleaseScheduleAssetModel> unknownAssets = getUnknownAssets(devices,
						softwareReleaseSchedule);
				if (!unknownAssets.isEmpty()) {
					content.addAll(unknownAssets);
				}
			} else {
				throw new AcsLogicalException(
						"Unsupported category! category=" + softwareReleaseSchedule.getDeviceCategory());
			}
		} else {
			SoftwareReleaseTransSearchParams params = new SoftwareReleaseTransSearchParams();
			params.addSoftwareReleaseScheduleIds(softwareReleaseScheduleId);

			// status filter
			if (!StringUtils.isEmpty(searchFilter.getStatus())) {
				if (!searchFilter.getStatus().equalsIgnoreCase("all"))
					params.setStatuses(EnumSet.of(SoftwareReleaseTrans.Status.valueOf(searchFilter.getStatus())));
			}

			Page<SoftwareReleaseTrans> softwareReleaseTransactions = softwareReleaseTransService
					.getSoftwareReleaseTransRepository().findSoftwareReleaseTrans(pageRequest, params);
			totalElements = softwareReleaseTransactions.getTotalElements();

			Instant now = Instant.now();
			for (SoftwareReleaseTrans softwareReleaseTrans : softwareReleaseTransactions) {

				String name = "Unknown";
				// String uid = "Unknown";
				if (softwareReleaseSchedule.getDeviceCategory() == AcnDeviceCategory.GATEWAY) {
					Gateway gateway = getKronosCache().findGatewayById(softwareReleaseTrans.getObjectId());
					if (gateway != null) {
						name = gateway.getName();
						// uid = gateway.getUid();
					}
				} else if (softwareReleaseSchedule.getDeviceCategory() == AcnDeviceCategory.DEVICE) {
					Device device = getKronosCache().findDeviceById(softwareReleaseTrans.getObjectId());
					if (device != null) {
						name = device.getName();
						// uid = device.getUid();
					}
				} else {
					throw new AcsLogicalException(
							"Unsupported category! category=" + softwareReleaseSchedule.getDeviceCategory());
				}

				SoftwareReleaseScheduleAssetModel model = new SoftwareReleaseScheduleAssetModel(
						softwareReleaseTrans.getId(), softwareReleaseTrans.getHid(), name);
				model.setUid(softwareReleaseTrans.getUid());
				model.setFirmwareFrom(getSoftwareReleaseName(softwareReleaseTrans.getFromSoftwareReleaseId()));
				model.setStatus(softwareReleaseTrans.getStatus().name());
				model.setError(softwareReleaseTrans.getError());
				model.setStarted(softwareReleaseTrans.getStarted());
				model.setEnded(softwareReleaseTrans.getEnded());
				model.setRetryCount(softwareReleaseTrans.getRetryCount());

				if (softwareReleaseTrans.getStarted() != null) {
					Instant startDate = softwareReleaseTrans.getStarted();
					// derive expire date
					Instant derivedExpireDate = startDate.plusSeconds(softwareReleaseTrans.getTimeToExpireSeconds());

					long remainingMinutes = (Duration.between(now, derivedExpireDate).toMinutes());
					if (remainingMinutes < 0)
						remainingMinutes = 0;
					model.setRemainingMinutes(remainingMinutes);
				}

				content.add(model);
			}
			// // sort content
			// if (content.size() > 1 && pageRequest.getSort() != null) {
			// Comparator<SoftwareReleaseScheduleAssetModel> comparator = null;
			// for (Order order : pageRequest.getSort()) {
			// PropertyComparator<SoftwareReleaseScheduleAssetModel>
			// propertyComparator = new PropertyComparator<>(
			// order.getProperty(), order.isIgnoreCase(),
			// Direction.ASC.equals(order.getDirection()));
			// if (comparator == null) {
			// comparator = propertyComparator;
			// } else {
			// comparator.thenComparing(propertyComparator);
			// }
			// }
			// if (comparator != null) {
			// content.sort(comparator);
			// }
			// }
			// // reduce content to one page
			// int fromIndex = pageRequest.getOffset();
			// int toIndex = fromIndex + pageRequest.getPageSize();
			// if (toIndex > softwareReleaseTransactions.size()) {
			// toIndex = softwareReleaseTransactions.size();
			// }
			// if (fromIndex > totalElements) {
			// content = new ArrayList<>();
			// } else {
			// content = content.subList(fromIndex, toIndex);
			// }
		}

		Page<SoftwareReleaseScheduleAssetModel> result = new PageImpl<SoftwareReleaseScheduleAssetModel>(content,
				pageRequest, totalElements);
		return new SoftwareReleaseScheduleAssetSearchResultModel(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('KRONOS_START_SOFTWARE_RELEASE_SCHEDULE')")
	@RequestMapping(value = "/{softwareReleaseScheduleId}/start", method = RequestMethod.POST)
	public SoftwareReleaseScheduleAuditModel start(@PathVariable String softwareReleaseScheduleId,
			HttpSession session) {

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findById(softwareReleaseScheduleId).orElse(null);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is not found");
		Assert.isTrue(softwareReleaseSchedule.getOnDemand(), "softwareReleaseSchedule can't be started on demand");

		String applicationId = getApplicationId(session);
		Assert.isTrue(StringUtils.equals(applicationId, softwareReleaseSchedule.getApplicationId()),
				"applicationIds mismatch");

		softwareReleaseSchedule = softwareReleaseScheduleService.checkAndStart(softwareReleaseSchedule, getUserId());

		SoftwareReleaseScheduleAuditModel responseModel = new SoftwareReleaseScheduleAuditModel(
				softwareReleaseSchedule);
		responseModel.setProgressMetrics(calculateSoftwareTransProgressMetrics(softwareReleaseSchedule));
		fillStatusModel(responseModel, softwareReleaseSchedule);

		return responseModel;
	}

	@PreAuthorize("hasAuthority('KRONOS_CANCEL_SOFTWARE_RELEASE_SCHEDULE')")
	@RequestMapping(value = "/{softwareReleaseScheduleId}/cancel", method = RequestMethod.POST)
	public SoftwareReleaseScheduleAuditModel cancel(@PathVariable String softwareReleaseScheduleId,
			HttpSession session) {

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findById(softwareReleaseScheduleId).orElse(null);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is not found");

		String applicationId = getApplicationId(session);
		Assert.isTrue(StringUtils.equals(applicationId, softwareReleaseSchedule.getApplicationId()),
				"applicationIds mismatch");

		softwareReleaseSchedule = softwareReleaseScheduleService.cancel(softwareReleaseSchedule, getUserId());

		SoftwareReleaseScheduleAuditModel responseModel = new SoftwareReleaseScheduleAuditModel(
				softwareReleaseSchedule);
		responseModel.setProgressMetrics(calculateSoftwareTransProgressMetrics(softwareReleaseSchedule));
		fillStatusModel(responseModel, softwareReleaseSchedule);

		return responseModel;
	}

	@PreAuthorize("hasAuthority('KRONOS_SOFTWARE_RELEASE_TRANSACTION_MOVE_TO_ERROR')")
	@RequestMapping(value = "/transactions/move-to-error", method = RequestMethod.POST)
	public SoftwareReleaseScheduleAuditModel moveSoftwareReleaseTransactionsToError(
			@RequestBody RetrySoftwareReleaseTransactionsRequestModel requestModel, HttpSession session) {
		Assert.notNull(requestModel, "requestModel is null");
		Assert.hasText(requestModel.getSoftwareReleaseScheduleId(), "softwareReleaseScheduleId is empty");
		Assert.notEmpty(requestModel.getSoftwareReleaseTransIds(), "softwareReleaseTransIds is empty");

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findById(requestModel.getSoftwareReleaseScheduleId())
				.orElse(null);
		Assert.isTrue(StringUtils.equals(getApplicationId(session), softwareReleaseSchedule.getApplicationId()),
				"applicationIds mismatch");

		String method = "moveSoftwareReleaseTransactionsToError";
		logDebug(method, "...");
		softwareReleaseTransService.moveToError(requestModel.getSoftwareReleaseTransIds(), getUserId());

		SoftwareReleaseScheduleAuditModel responseModel = new SoftwareReleaseScheduleAuditModel(
				softwareReleaseSchedule);
		responseModel.setProgressMetrics(calculateSoftwareTransProgressMetrics(softwareReleaseSchedule));
		fillStatusModel(responseModel, softwareReleaseSchedule);

		return responseModel;
	}

	@PreAuthorize("hasAuthority('KRONOS_SOFTWARE_RELEASE_TRANSACTION_RETRY_ON_ERROR')")
	@RequestMapping(value = "/transactions/retry-on-error", method = RequestMethod.POST)
	public SoftwareReleaseScheduleAuditModel retrySoftwareReleaseTransactionsOnError(
			@RequestBody RetrySoftwareReleaseTransactionsRequestModel requestModel, HttpSession session) {
		Assert.notNull(requestModel, "requestModel is null");
		Assert.hasText(requestModel.getSoftwareReleaseScheduleId(), "softwareReleaseScheduleId is empty");
		Assert.notEmpty(requestModel.getSoftwareReleaseTransIds(), "softwareReleaseTransIds is empty");

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findById(requestModel.getSoftwareReleaseScheduleId())
				.orElse(null);
		Assert.isTrue(StringUtils.equals(getApplicationId(session), softwareReleaseSchedule.getApplicationId()),
				"applicationIds mismatch");

		String method = "retrySoftwareReleaseTransactionsOnError";
		logDebug(method, "...");
		softwareReleaseTransService.retryInProgressAsset(requestModel.getSoftwareReleaseTransIds(), getUserId());

		SoftwareReleaseScheduleAuditModel responseModel = new SoftwareReleaseScheduleAuditModel(
				softwareReleaseSchedule);
		responseModel.setProgressMetrics(calculateSoftwareTransProgressMetrics(softwareReleaseSchedule));
		fillStatusModel(responseModel, softwareReleaseSchedule);

		return responseModel;
	}

	@PreAuthorize("hasAuthority('KRONOS_RETRY_SOFTWARE_RELEASE_TRANSACTION')")
	@RequestMapping(value = "/transactions/retry", method = RequestMethod.POST)
	public SoftwareReleaseScheduleAuditModel retrySoftwareReleaseTransactions(
			@RequestBody RetrySoftwareReleaseTransactionsRequestModel requestModel, HttpSession session) {
		Assert.notNull(requestModel, "requestModel is null");
		Assert.hasText(requestModel.getSoftwareReleaseScheduleId(), "softwareReleaseScheduleId is empty");
		Assert.notEmpty(requestModel.getSoftwareReleaseTransIds(), "softwareReleaseTransIds is empty");

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findById(requestModel.getSoftwareReleaseScheduleId())
				.orElse(null);
		Assert.isTrue(StringUtils.equals(getApplicationId(session), softwareReleaseSchedule.getApplicationId()),
				"applicationIds mismatch");

		String method = "retrySoftwareReleaseTransactions";
		logDebug(method, "...");
		softwareReleaseTransService.retry(requestModel.getSoftwareReleaseTransIds(), getUserId());

		SoftwareReleaseScheduleAuditModel responseModel = new SoftwareReleaseScheduleAuditModel(
				softwareReleaseSchedule);
		responseModel.setProgressMetrics(calculateSoftwareTransProgressMetrics(softwareReleaseSchedule));
		fillStatusModel(responseModel, softwareReleaseSchedule);

		return responseModel;
	}

	@PreAuthorize("hasAuthority('KRONOS_CANCEL_SOFTWARE_RELEASE_TRANSACTION')")
	@RequestMapping(value = "/transactions/cancel", method = RequestMethod.POST)
	public SoftwareReleaseScheduleAuditModel cancelSoftwareReleaseTransactions(
			@RequestBody CancelSoftwareReleaseTransactionsRequestModel requestModel, HttpSession session) {
		Assert.notNull(requestModel, "requestModel is null");
		Assert.hasText(requestModel.getSoftwareReleaseScheduleId(), "softwareReleaseScheduleId is empty");
		Assert.notEmpty(requestModel.getSoftwareReleaseTransIds(), "softwareReleaseTransIds is empty");

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findById(requestModel.getSoftwareReleaseScheduleId())
				.orElse(null);
		Assert.isTrue(StringUtils.equals(getApplicationId(session), softwareReleaseSchedule.getApplicationId()),
				"applicationIds mismatch");

		String method = "cancelSoftwareReleaseTransactions";
		logDebug(method, "...");
		softwareReleaseTransService.cancel(requestModel.getSoftwareReleaseTransIds(), getUserId());

		SoftwareReleaseScheduleAuditModel responseModel = new SoftwareReleaseScheduleAuditModel(
				softwareReleaseSchedule);
		responseModel.setProgressMetrics(calculateSoftwareTransProgressMetrics(softwareReleaseSchedule));
		fillStatusModel(responseModel, softwareReleaseSchedule);

		return responseModel;
	}

	private Instant getScheduledDate(Long utcMilliseconds, String localTimezone, String targetTimezone) {
		String method = "getScheduledDate";
		if (utcMilliseconds == null)
			return null;

		Instant utcTime = Instant.ofEpochMilli(utcMilliseconds);
		logDebug(method, "UTC date/time: %s, local time zone: %s, target timezone: %s", utcTime.toString(),
				localTimezone, targetTimezone);
		if (!StringUtils.equals(localTimezone, targetTimezone)) {
			ZoneId localZone = ZoneId.of(localTimezone);
			ZoneId targetZone = ZoneId.of(targetTimezone);

			ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(utcTime, localZone);
			logDebug(method, "time in local time zone: %s", zonedDateTime.toString());

			zonedDateTime = zonedDateTime.withZoneSameLocal(targetZone);
			logDebug(method, "time in target time zone: %s", zonedDateTime.toString());

			utcTime = zonedDateTime.toInstant();
			logDebug(method, "time in UTC: %s", utcTime.toString());
		}
		return utcTime;
	}

	// private ObjectType getObjectType(String deviceCategoryId) {
	// DeviceCategory deviceCategory =
	// rheaClientCache.findDeviceCategoryById(deviceCategoryId);
	// if (deviceCategory != null) {
	// return ObjectType.getType(deviceCategory.getName());
	// } else {
	// return null;
	// }
	// }

	private List<Device> getDevicesForUpgrade(String applicationId, SoftwareRelease softwareRelease) {
		PageRequest pageRequest = new PageRequest(0, Integer.MAX_VALUE,
				new Sort(Direction.ASC, new String[] { "name" }));
		DeviceSearchParams params = new DeviceSearchParams();
		params.addApplicationIds(applicationId);
		params.addSoftwareReleaseIds(softwareRelease.getUpgradeableFromIds()
				.toArray(new String[softwareRelease.getUpgradeableFromIds().size()]));

		Page<Device> devices = deviceService.getDeviceRepository().doFindDevices(pageRequest, params);

		return devices.getContent();
	}

	private List<Device> getDevicesForUpgrade(String applicationId, SoftwareRelease softwareRelease,
			String[] deviceTypesIds) {
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.notNull(softwareRelease, "softwareRelease is null");
		Assert.notEmpty(softwareRelease.getUpgradeableFromIds(), "upgradeableFromIds is empty");
		Assert.notEmpty(deviceTypesIds, "deviceTypeIds is empty");

		String method = "getDevicesForUpgrade";

		// PageRequest pageRequest = new PageRequest(0, Integer.MAX_VALUE,
		// new Sort(Direction.ASC, new String[] { "name" }));
		PageRequest pageRequest = new PageRequest(0, Integer.MAX_VALUE);
		DeviceSearchParams params = new DeviceSearchParams();
		params.addApplicationIds(applicationId);
		params.addSoftwareReleaseIds(softwareRelease.getUpgradeableFromIds()
				.toArray(new String[softwareRelease.getUpgradeableFromIds().size()]));

		params.addDeviceTypeIds(deviceTypesIds);

		Page<Device> devices = deviceService.getDeviceRepository().doFindDevices(pageRequest, params);
		logDebug(method, "devices: %s", devices.getTotalElements());

		return devices.getContent();
	}

	private List<Gateway> getGatewaysForUpgrade(String applicationId, SoftwareRelease softwareRelease) {
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.notNull(softwareRelease, "softwareRelease is null");
		Assert.notEmpty(softwareRelease.getUpgradeableFromIds(), "upgradeableFromIds is empty");

		String method = "getGatewaysForUpgrade";

		// PageRequest pageRequest = new PageRequest(0, Integer.MAX_VALUE,
		// new Sort(Direction.ASC, new String[] { "name" }));
		PageRequest pageRequest = new PageRequest(0, Integer.MAX_VALUE);
		GatewaySearchParams params = new GatewaySearchParams();
		params.addApplicationIds(applicationId);
		params.addSoftwareReleaseIds(softwareRelease.getUpgradeableFromIds()
				.toArray(new String[softwareRelease.getUpgradeableFromIds().size()]));

		Page<Gateway> gateways = gatewayService.getGatewayRepository().findGateways(pageRequest, params);
		logDebug(method, "gateways: %s", gateways.getTotalElements());

		return gateways.getContent();
	}

	private List<Gateway> getGatewaysForUpgrade(String applicationId, SoftwareRelease softwareRelease,
			String[] deviceTypesIds) {

		// PageRequest pageRequest = new PageRequest(0, Integer.MAX_VALUE,
		// new Sort(Direction.ASC, new String[] { "name" }));
		PageRequest pageRequest = new PageRequest(0, Integer.MAX_VALUE);
		GatewaySearchParams params = new GatewaySearchParams();
		params.addApplicationIds(applicationId);
		params.addSoftwareReleaseIds(softwareRelease.getUpgradeableFromIds()
				.toArray(new String[softwareRelease.getUpgradeableFromIds().size()]));

		params.addDeviceTypeIds(deviceTypesIds);

		Page<Gateway> gateways = gatewayService.getGatewayRepository().findGateways(pageRequest, params);

		return gateways.getContent();
	}

	private ObjectModel populateObjectNames(ObjectModel object, Map<String, Node> nodesMap) {
		object.setOwnerName(getOwnerName(object.getOwnerId()));
		object.setNodeName(getNodeName(object.getNodeId(), nodesMap));
		object.setSoftwareReleaseName(getSoftwareReleaseName(object.getSoftwareReleaseId()));
		return object;
	}

	private String getOwnerName(String userId) {
		String ownerName = null;
		if (StringUtils.isNotEmpty(userId)) {
			User user = getCoreCacheService().findUserById(userId);
			if (user != null) {
				ownerName = user.getContact().fullName();
			}
		}
		return ownerName;
	}

	private String getNodeName(String nodeId, Map<String, Node> nodesMap) {
		String nodeName = null;
		if (StringUtils.isNotEmpty(nodeId)) {
			Node node = getKronosCache().findNodeById(nodeId);
			if (node != null) {
				nodeName = NodeOption.getNodeFullName(node, nodesMap);
			}
		}
		return nodeName;
	}

	@RequestMapping(value = "/filter-options/{status}", method = RequestMethod.GET)
	public SoftwareReleaseSchedulePendingOptions options(HttpSession session, @PathVariable String status) {
		SoftwareReleaseScheduleSearchParams params = new SoftwareReleaseScheduleSearchParams();
		params.addApplicationIds(getApplicationId(session));

		boolean isEligible = status == null ? false : status.equalsIgnoreCase("ELIGIBLE");

		SoftwareReleaseSchedulePendingOptions options = new SoftwareReleaseSchedulePendingOptions();

		if (!isEligible) {
			if (status != null) {
				if (Status.valueOf(status) == Status.COMPLETE) {
					params.setStatuses(EnumSet.of(Status.COMPLETE, Status.CANCELLED));
				} else {
					params.setStatuses(EnumSet.of(Status.valueOf(status)));
				}
			}

			// List<SoftwareReleaseSchedule> softwareReleaseSchedules =
			// softwareReleaseScheduleService
			// .getSoftwareReleaseScheduleRepository().findSoftwareReleaseSchedules(params);
			// // Requestors
			// List<UserOption> requestorsOptions = new ArrayList<>();
			// List<String> requestors = softwareReleaseSchedules.stream().map(s
			// -> s.getCreatedBy()).distinct()
			// .collect(Collectors.toList());
			// if (requestors != null) {
			// requestors.stream().forEach(r -> {
			// User user = getCoreCacheService().findUserById(r);
			// if (user != null) {
			// requestorsOptions.add(new UserOption(user));
			// }
			// });
			// requestorsOptions.sort(Comparator.comparing(UserOption::getName,
			// String.CASE_INSENSITIVE_ORDER));
			// }
			// Requestors

			Map<String, UserOption> requestorsOptions = new HashMap<>();
			softwareReleaseScheduleService.getSoftwareReleaseScheduleRepository().countDistinctCreatedBy(params)
					.stream().forEach(createdBy -> {
						if (!requestorsOptions.containsKey(createdBy.getName())) {
							UserOption userOption = getUserOption(createdBy.getName());
							if (userOption != null) {
								requestorsOptions.put(createdBy.getName(), userOption);
							}
						}
					});
			options.setRequestors(new ArrayList<UserOption>(requestorsOptions.values()));

			// TODO revisit Start dates
			// List<String> startDates =
			// softwareReleaseSchedules.stream().filter(s ->
			// s.getScheduledDate() != null)
			// .map(s ->
			// s.getScheduledDate().toString()).distinct().collect(Collectors.toList());
			// if (Status.valueOf(status) == Status.SCHEDULED) {
			// startDates.add("On demand");
			// }
			// Collections.sort(startDates);
			// options.setStartDates(startDates);
			// Device types
			// Map<String, DeviceTypeOption> deviceTypeModels = new HashMap<>();
			// softwareReleaseSchedules.stream().forEach(s -> {
			// List<String> objectIds = s.getObjectIds();
			// for (String objectId : objectIds) {
			// DeviceType deviceType = getDeviceType(s.getDeviceCategory(),
			// objectId);
			// if (deviceType != null) {
			// deviceTypeModels.put(deviceType.getId(), new
			// DeviceTypeOption(deviceType));
			// }
			// }
			// });
			// options.setDeviceTypes(deviceTypeModels.values().stream()
			// .sorted(Comparator.comparing(DeviceTypeOption::getName)).collect(Collectors.toList()));

			List<DeviceTypeOption> deviceTypeOptions = new ArrayList<>();
			softwareReleaseScheduleService.getSoftwareReleaseScheduleRepository().countDistinctDeviceTypeId(params)
					.stream().forEach(deviceTypeId -> {
						DeviceType deviceType = getKronosCache().findDeviceTypeById(deviceTypeId.getName());
						if (deviceType != null) {
							deviceTypeOptions.add(new DeviceTypeOption(deviceType));
						}
					});
			options.setDeviceTypes(deviceTypeOptions);

			// TODO revisit Completed dates
			// List<String> endDates = null;
			// if (Status.valueOf(status) == Status.COMPLETE) {
			// endDates = softwareReleaseSchedules.stream().filter(s ->
			// s.getEnded() != null)
			// .map(s ->
			// s.getEnded().toString()).distinct().collect(Collectors.toList());
			// Collections.sort(endDates);
			// }d
			// options.setCompletedDates(enDates);

			// Status
			List<CompletedStatus> statuses = null;
			if (Status.valueOf(status) == Status.COMPLETE) {
				statuses = Arrays.asList(CompletedStatus.values());
			}
			options.setStatuses(statuses);
		}

		return options;
	}

	@RequestMapping(value = "/summary", method = RequestMethod.GET)
	public SoftwareReleaseScheduleSummaryOptions summary(HttpSession session) {

		String method = "summary";
		logDebug(method, "...");

		SoftwareReleaseScheduleSearchParams params = new SoftwareReleaseScheduleSearchParams();
		params.addApplicationIds(getApplicationId(session));
		List<SoftwareReleaseSchedule> softwareReleaseSchedules = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findSoftwareReleaseSchedules(params);
		logDebug(method, "after query of summary...");

		SoftwareReleaseScheduleSummaryOptions options = new SoftwareReleaseScheduleSummaryOptions();

		options.setPendingJobs(
				softwareReleaseSchedules.stream().filter(s -> s.getStatus() == Status.SCHEDULED).count());
		options.setInProgressJobs(
				softwareReleaseSchedules.stream().filter(s -> s.getStatus() == Status.INPROGRESS).count());
		options.setProcessedJobs(softwareReleaseSchedules.stream()
				.filter(s -> s.getStatus() == Status.COMPLETE || s.getStatus() == Status.CANCELLED).count());

		// options.setTotalDevices(deviceService.getDeviceRepository()
		// .findAllByApplicationIdAndEnabled(getApplicationId(session),
		// true).stream().count());
		// options.setTotalGateways(
		// gatewayService.getGatewayRepository().findByApplicationId(getApplicationId(session)).stream().count());
		// options.setEligibleUpgrades(
		// softwareReleaseScheduleService.getNumberOfEligibleUpgrades(getApplicationId(session),
		// true, false));
		return options;
	}

	@RequestMapping(value = "/eligible", method = RequestMethod.GET)
	public List<SoftwareReleaseScheduleModels.EligibleFirmwareChangeGroup> listEligibleFirmwareChangeGroups(
			HttpSession session) {

		List<EligibleFirmwareChangeGroup> eligibleFirmwareChangeGroups = softwareReleaseScheduleService
				.getEligibleUpgrades(getApplicationId(session));

		List<SoftwareReleaseScheduleModels.EligibleFirmwareChangeGroup> list = new ArrayList<>();
		eligibleFirmwareChangeGroups.forEach(item -> {
			DeviceType assetType = getKronosCache().findDeviceTypeById(item.deviceTypeId);
			SoftwareReleaseScheduleModels.EligibleFirmwareChangeGroup eligibleFirmwareChangeGroup = new SoftwareReleaseScheduleModels.EligibleFirmwareChangeGroup();
			eligibleFirmwareChangeGroup.withAssetTypeName(assetType.getName());
			eligibleFirmwareChangeGroup.withAssetTypeId(assetType.getId());
			eligibleFirmwareChangeGroup.withNumberOfAssets(item.numberOfDevices);
			eligibleFirmwareChangeGroup.withHardwareVersionName(getHardwareVersionName(item.rheaDeviceTypeId));
			eligibleFirmwareChangeGroup.withCurrentFirmwareVersion(getSoftwareReleaseName(item.softwareReleaseId));

			List<SoftwareReleaseScheduleModels.AvailableFirmwareVersion> availableFirmwareVersionNames = new ArrayList<>();
			item.newSoftwareReleaseIds.forEach(softwareReleaseId -> {
				availableFirmwareVersionNames.add(new SoftwareReleaseScheduleModels.AvailableFirmwareVersion(
						softwareReleaseId, getSoftwareReleaseName(softwareReleaseId)));
			});

			if (!availableFirmwareVersionNames.isEmpty())
				availableFirmwareVersionNames.sort(Comparator.comparing(
						SoftwareReleaseScheduleModels.AvailableFirmwareVersion::getSoftwareReleaseName,
						String.CASE_INSENSITIVE_ORDER));

			eligibleFirmwareChangeGroup.withAvailableFirmwareVersionNames(availableFirmwareVersionNames);
			list.add(eligibleFirmwareChangeGroup);
		});

		if (!list.isEmpty())
			list.sort(Comparator.comparing(SoftwareReleaseScheduleModels.EligibleFirmwareChangeGroup::getAssetTypeName,
					String.CASE_INSENSITIVE_ORDER));

		return list;
	}

	@RequestMapping(value = "/find/{status}", method = RequestMethod.POST)
	public SoftwareReleaseScheduleSearchResultStatusModel listSwReleaseScheduleStatus(
			@RequestBody SoftwareReleaseScheduleSearchFilterStatusModel searchFilter,
			@PathVariable(required = true) String status, HttpSession session) {

		// sorting & paging
		PageRequest pageRequest = new PageRequest(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		SoftwareReleaseScheduleStatusSearchParams params = new SoftwareReleaseScheduleStatusSearchParams();
		params.addApplicationIds(getApplicationId(session));
		if (searchFilter.getDeviceTypes() != null && searchFilter.getDeviceTypes().length != 0) {

			// commenting out as fix for AC-633 release 2018-11
			// Set<String> objectIds =
			// getObjectIds(searchFilter.getDeviceTypes(),
			// getApplicationId(session));
			// if (!objectIds.isEmpty()) {
			// params.setObjectIds(objectIds);
			// } else {
			// return new SoftwareReleaseScheduleSearchResultStatusModel(null,
			// searchFilter);
			// }
			params.addDeviceTypeIds(searchFilter.getDeviceTypes());
		}
		if (!StringUtils.isEmpty(searchFilter.getStartDates())) {
			if (searchFilter.getStartDates().equals("On demand")) {
				params.setOnDemand(true);
			} else {
				params.setScheduledDate(Instant.parse(searchFilter.getStartDates()).minusNanos(1));
			}
		}
		if (searchFilter.getRequestors() != null && searchFilter.getRequestors().length != 0) {
			params.addRequestorIds(searchFilter.getRequestors());
		}

		if (Status.valueOf(status) == Status.COMPLETE) {
			if (searchFilter.getStatuses() != null && searchFilter.getStatuses().length != 0
					&& searchFilter.getStatuses().length != 3) {
				List<String> listOfStatuses = new ArrayList<>(Arrays.asList(searchFilter.getStatuses()));
				if (listOfStatuses.contains(CompletedStatus.Cancelled.toString())) {
					if (searchFilter.getStatuses().length == 1) {
						params.setStatuses(EnumSet.of(Status.CANCELLED));
					} else {
						params.addCompletedStatuses(CompletedStatus.Cancelled.toString());
					}
				}
				if (listOfStatuses.contains(CompletedStatus.Complete.toString())) {
					params.addCompletedStatuses(CompletedStatus.Complete.toString());
				}
			} else {
				params.setStatuses(EnumSet.of(Status.COMPLETE, Status.CANCELLED));
			}

			if (!StringUtils.isEmpty(searchFilter.getCompletedDates())) {
				params.setCompletedDate(Instant.parse(searchFilter.getCompletedDates()).minusNanos(1));
			}
		} else {
			params.setStatuses(EnumSet.of(Status.valueOf(status)));
		}
		Page<SoftwareReleaseSchedule> softwareReleaseSchedules = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findSoftwareReleaseSchedules(pageRequest, params);

		// collect distinct set of ids for bulk lookup of transactions
		Set<String> softwareReleaseScheduleIdSet = new HashSet<>();
		for (SoftwareReleaseSchedule softwareReleaseSchedule : softwareReleaseSchedules)
			softwareReleaseScheduleIdSet.add(softwareReleaseSchedule.getId());

		// process and populate map
		Map<String, SoftwareReleaseTransProgressMetrics> progressMetricsMap = bulkCalculateSoftwareTransProgressMetrics(
				softwareReleaseScheduleIdSet.toArray(new String[softwareReleaseScheduleIdSet.size()]));

		// convert to visual model
		List<SoftwareReleaseScheduleStatusModel> softwareReleaseSchedulePendingModels = new ArrayList<>();
		for (SoftwareReleaseSchedule softwareReleaseSchedule : softwareReleaseSchedules) {

			SoftwareReleaseScheduleStatusModel model = new SoftwareReleaseScheduleStatusModel(softwareReleaseSchedule);
			fillStatusModel(model, softwareReleaseSchedule);
			// get values from map
			model.setProgressMetrics(progressMetricsMap.get(softwareReleaseSchedule.getId()));
			softwareReleaseSchedulePendingModels.add(model);
		}

		Page<SoftwareReleaseScheduleStatusModel> result = new PageImpl<>(softwareReleaseSchedulePendingModels,
				pageRequest, softwareReleaseSchedules.getTotalElements());

		return new SoftwareReleaseScheduleSearchResultStatusModel(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('KRONOS_UPDATE_SOFTWARE_RELEASE_SCHEDULE')")
	@RequestMapping(value = "/checkAssets/{softwarereleaseScheduleId}", method = RequestMethod.POST)
	public StatusModel checkAssets(@RequestBody List<AssetOption> assets,
			@PathVariable String softwarereleaseScheduleId, HttpSession session) {
		return performCheckAssets(assets, softwarereleaseScheduleId, session);
	}

	@PreAuthorize("hasAuthority('KRONOS_UPDATE_SOFTWARE_RELEASE_SCHEDULE')")
	@RequestMapping(value = "/checkAssets", method = RequestMethod.POST)
	public StatusModel checkAssets(@RequestBody List<AssetOption> assets, HttpSession session) {
		return performCheckAssets(assets, null, session);
	}

	private StatusModel performCheckAssets(List<AssetOption> assets, String softwarereleaseScheduleId,
			HttpSession session) {
		String applicationId = getApplicationId(session);
		for (CoreDefinitionModelOption asset : assets) {
			SoftwareReleaseSchedule schedule = softwareReleaseService.findActiveSoftwareReleaseSchedule(applicationId,
					softwarereleaseScheduleId, asset.getId(), null);
			if (schedule != null) {
				return StatusModel
						.error("Asset " + asset.getName() + " is already being managed by job: " + schedule.getName());
			}
		}
		return StatusModel.OK;
	}

	private void fillStatusModel(SoftwareReleaseScheduleStatusModel model,
			SoftwareReleaseSchedule softwareReleaseSchedule) {
		DeviceType assetType = getAssetType(softwareReleaseSchedule.getDeviceTypeId());
		Assert.hasText(assetType.getRheaDeviceTypeId(), "rheaDeviceTypeId is empty! assetType: " + assetType.getName());

		model.withNewSwVersion(getSoftwareReleaseName(softwareReleaseSchedule.getSoftwareReleaseId()))
				.withRequestor(getRequestorName(softwareReleaseSchedule.getCreatedBy()))
				.withDeviceType(assetType.getName())
				.withHwVersion(getHardwareVersionName(assetType.getRheaDeviceTypeId()));

		model.setStart(getStart(softwareReleaseSchedule));

		// switch (softwareReleaseSchedule.getStatus()) {
		// case SCHEDULED:
		// break;
		// case INPROGRESS:
		// model.withStarted(softwareReleaseSchedule.getStarted());
		// // .withComplete(getCompleted(softwareReleaseSchedule));
		// break;
		// case COMPLETE:
		// case CANCELLED:
		// //
		// model.withStarted(softwareReleaseSchedule.getStarted()).withCompleted(softwareReleaseSchedule.getEnded())
		// //
		// .withStatus(getSwReleaseScheduleTransStatus(softwareReleaseSchedule));
		// break;
		// default:
		// break;
		// }

		model.withStarted(softwareReleaseSchedule.getStarted()).withCompleted(softwareReleaseSchedule.getEnded())
				.withStatus(softwareReleaseSchedule.getStatus());
	}

	// private CompletedStatus
	// getSwReleaseScheduleTransStatus(SoftwareReleaseSchedule
	// softwareReleaseSchedule) {
	// if (softwareReleaseSchedule.getStatus() == Status.CANCELLED)
	// return CompletedStatus.Cancelled;
	// else if (softwareReleaseSchedule.getStatus() == Status.COMPLETE) {
	// if (softwareReleaseSchedule.getCompleteWithError()) {
	// return CompletedStatus.Alerts;
	// } else {
	// SoftwareReleaseTransSearchParams params = new
	// SoftwareReleaseTransSearchParams();
	// params.addSoftwareReleaseScheduleIds(softwareReleaseSchedule.getId());
	// List<SoftwareReleaseTrans> softwareReleaseTrans =
	// softwareReleaseTransService
	// .getSoftwareReleaseTransRepository().findSoftwareReleaseTrans(params);
	// if (!softwareReleaseTrans.isEmpty()) {
	// List<SoftwareReleaseTrans.Status> statuses =
	// softwareReleaseTrans.stream().map(t -> t.getStatus())
	// .collect(Collectors.toList());
	// if (statuses.contains(SoftwareReleaseTrans.Status.ERROR))
	// return CompletedStatus.Alerts;
	// if (!statuses.contains(SoftwareReleaseTrans.Status.ERROR) &&
	// !statuses.contains(null))
	// return CompletedStatus.Completed;
	// }
	// }
	// }
	// return null;
	// }

	private Set<String> getObjectIds(String[] deviceTypes, String applicationId) {
		Set<String> objectIds = new HashSet<>();
		Set<String> deviceTypesFromDevices = new HashSet<>();
		Set<String> deviceTypesFromGateways = new HashSet<>();
		/*
		 * for (String id : searchFilter.getDeviceTypes()) { DeviceType deviceType =
		 * getKronosCache().findDeviceTypeById(id); if (deviceType != null) { switch
		 * (deviceType.getDeviceCategory()) { case DEVICE: deviceTypes.add(id); break;
		 * case GATEWAY: gatewayTypes.add(id); break; } } }
		 */
		Arrays.stream(deviceTypes).forEach(id -> {
			DeviceType deviceType = getKronosCache().findDeviceTypeById(id);
			if (deviceType != null) {
				switch (deviceType.getDeviceCategory()) {
				case DEVICE:
					deviceTypesFromDevices.add(id);
					break;
				case GATEWAY:
					deviceTypesFromGateways.add(id);
					break;
				}
			}
		});
		if (!deviceTypesFromDevices.isEmpty()) {
			objectIds.addAll(deviceService.getDeviceRepository()
					.doFindByApplicationIdAndDeviceTypeIdAndEnabled(applicationId,
							deviceTypesFromDevices.toArray(new String[deviceTypesFromDevices.size()]), true)
					.stream().map(d -> d.getId()).collect(Collectors.toSet()));
		}
		if (!deviceTypesFromGateways.isEmpty()) {
			objectIds.addAll(gatewayService.getGatewayRepository()
					.findByApplicationIdAndDeviceTypeIdAndEnabled(applicationId,
							deviceTypesFromGateways.toArray(new String[deviceTypesFromGateways.size()]), true)
					.stream().map(d -> d.getId()).collect(Collectors.toSet()));
		}
		return objectIds;
	}

	private SoftwareReleaseScheduleModels.SoftwareReleaseTransProgressMetrics calculateSoftwareTransProgressMetrics(
			SoftwareReleaseSchedule softwareReleaseSchedule) {

		// bulk lookup transactions
		SoftwareReleaseTransSearchParams softwareReleaseTransSearchParams = new SoftwareReleaseTransSearchParams();
		softwareReleaseTransSearchParams.addSoftwareReleaseScheduleIds(softwareReleaseSchedule.getId());
		softwareReleaseTransSearchParams.addObjectIds(softwareReleaseSchedule.getObjectIds()
				.toArray(new String[softwareReleaseSchedule.getObjectIds().size()]));
		List<SoftwareReleaseTrans> softwareReleaseTransactions = softwareReleaseTransService
				.getSoftwareReleaseTransRepository().findSoftwareReleaseTrans(softwareReleaseTransSearchParams);

		return calculateSoftwareTransProgressMetrics(softwareReleaseSchedule, softwareReleaseTransactions);
	}

	private Map<String, SoftwareReleaseScheduleModels.SoftwareReleaseTransProgressMetrics> bulkCalculateSoftwareTransProgressMetrics(
			String... softwareReleaseScheduleIds) {

		String method = "bulkCalculateSoftwareTransProgressMetrics";
		logInfo(method, "softwareReleaseScheduleIds: %s",
				(softwareReleaseScheduleIds == null ? "0" : softwareReleaseScheduleIds.length));

		if (softwareReleaseScheduleIds == null || softwareReleaseScheduleIds.length == 0)
			return Collections.emptyMap();

		// bulk lookup jobs
		SoftwareReleaseScheduleSearchParams softwareReleaseScheduleSearchParams = new SoftwareReleaseScheduleSearchParams();
		softwareReleaseScheduleSearchParams.addIds(softwareReleaseScheduleIds);
		List<SoftwareReleaseSchedule> softwareReleaseSchedules = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository()
				.findSoftwareReleaseSchedules(softwareReleaseScheduleSearchParams);

		// store jobs by their ids
		Map<String, SoftwareReleaseSchedule> jobsMap = new HashMap<>();
		for (SoftwareReleaseSchedule softwareReleaseSchedule : softwareReleaseSchedules)
			jobsMap.put(softwareReleaseSchedule.getId(), softwareReleaseSchedule);

		// bulk lookup transactions
		SoftwareReleaseTransSearchParams softwareReleaseTransSearchParams = new SoftwareReleaseTransSearchParams();
		softwareReleaseTransSearchParams.addSoftwareReleaseScheduleIds(softwareReleaseScheduleIds);
		List<SoftwareReleaseTrans> softwareReleaseTransactions = softwareReleaseTransService
				.getSoftwareReleaseTransRepository().findSoftwareReleaseTrans(softwareReleaseTransSearchParams);

		// group transactions with job id
		Map<String, List<SoftwareReleaseTrans>> transactionsMap = new HashMap<>();
		for (SoftwareReleaseTrans softwareReleaseTrans : softwareReleaseTransactions) {
			String key = softwareReleaseTrans.getSoftwareReleaseScheduleId();
			List<SoftwareReleaseTrans> list = transactionsMap.get(key);
			if (list == null)
				list = new ArrayList<>();
			list.add(softwareReleaseTrans);
			transactionsMap.put(key, list);
		}

		// process and populate final map
		Map<String, SoftwareReleaseScheduleModels.SoftwareReleaseTransProgressMetrics> progressMetricsMap = new HashMap<>();
		for (String key : softwareReleaseScheduleIds) {
			SoftwareReleaseSchedule job = jobsMap.get(key);
			List<SoftwareReleaseTrans> transactions = transactionsMap.get(key);

			if (job == null || transactions == null)
				progressMetricsMap.put(key, new SoftwareReleaseScheduleModels.SoftwareReleaseTransProgressMetrics());
			else
				progressMetricsMap.put(key, calculateSoftwareTransProgressMetrics(job, transactions));
		}

		return progressMetricsMap;
	}

	private SoftwareReleaseScheduleModels.SoftwareReleaseTransProgressMetrics calculateSoftwareTransProgressMetrics(
			SoftwareReleaseSchedule softwareReleaseSchedule, List<SoftwareReleaseTrans> softwareReleaseTransactions) {
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is null");

		String method = "calculateSoftwareTransProgressMetrics";
		logInfo(method, "softwareReleaseScheduleId: %s, softwareReleaseTransactions: %s",
				softwareReleaseSchedule.getId(),
				(softwareReleaseTransactions == null ? "0" : softwareReleaseTransactions.size()));

		SoftwareReleaseScheduleModels.SoftwareReleaseTransProgressMetrics model = new SoftwareReleaseScheduleModels.SoftwareReleaseTransProgressMetrics();

		if (softwareReleaseTransactions != null && !softwareReleaseTransactions.isEmpty()) {
			if (softwareReleaseTransactions.size() == 0
					&& softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.SCHEDULED) {
				model.setPending(calculateSoftwareReleaseTransStateMetric(softwareReleaseSchedule.getObjectIds().size(),
						softwareReleaseSchedule.getObjectIds().size()));
				return model;
			} else if (softwareReleaseTransactions.size() == 0
					&& softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.CANCELLED) {
				model.setCancelled(calculateSoftwareReleaseTransStateMetric(
						softwareReleaseSchedule.getObjectIds().size(), softwareReleaseSchedule.getObjectIds().size()));
				return model;
			} else if (softwareReleaseTransactions.size() == 0) {
				return model;
			}

			int totalAssets = softwareReleaseTransactions.size();
			int pendingAssets = 0;
			int inprogressAssets = 0;
			int receivedAssets = 0;
			int completeAssets = 0;
			int cancelledAssets = 0;
			int failedAssets = 0;
			int expiredAssets = 0;
			int endOfLifeAssets = 0;

			for (SoftwareReleaseTrans softwareReleaseTrans : softwareReleaseTransactions) {
				switch (softwareReleaseTrans.getStatus()) {
				case PENDING:
					pendingAssets++;
					break;
				case INPROGRESS:
					inprogressAssets++;
					break;
				case RECEIVED:
					receivedAssets++;
					break;
				case COMPLETE:
					completeAssets++;
					break;
				case CANCELLED:
					cancelledAssets++;
					break;
				case EXPIRED:
					expiredAssets++;
					break;
				case ERROR:
					failedAssets++;
					break;
				default:
					throw new AcsLogicalException("Unsupported state! state=" + softwareReleaseTrans.getStatus());
				}
			}

			endOfLifeAssets = completeAssets + cancelledAssets;

			if (pendingAssets > 0)
				model.setPending(calculateSoftwareReleaseTransStateMetric(totalAssets, pendingAssets));

			if (inprogressAssets > 0)
				model.setInprogress(calculateSoftwareReleaseTransStateMetric(totalAssets, inprogressAssets));

			if (receivedAssets > 0)
				model.setReceived(calculateSoftwareReleaseTransStateMetric(totalAssets, receivedAssets));

			if (completeAssets > 0)
				model.setComplete(calculateSoftwareReleaseTransStateMetric(totalAssets, completeAssets));

			if (cancelledAssets > 0)
				model.setCancelled(calculateSoftwareReleaseTransStateMetric(totalAssets, cancelledAssets));

			if (expiredAssets > 0)
				model.setExpired(calculateSoftwareReleaseTransStateMetric(totalAssets, expiredAssets));

			if (failedAssets > 0)
				model.setFailed(calculateSoftwareReleaseTransStateMetric(totalAssets, failedAssets));

			if (endOfLifeAssets > 0)
				model.setEndOfLife(calculateSoftwareReleaseTransStateMetric(totalAssets, endOfLifeAssets));
		}
		return model;
	}

	private SoftwareReleaseScheduleModels.SoftwareReleaseTransStateMetrics calculateSoftwareReleaseTransStateMetric(
			int totalAssets, int stateAssets) {

		SoftwareReleaseScheduleModels.SoftwareReleaseTransStateMetrics stateMetrics = new SoftwareReleaseTransStateMetrics();
		stateMetrics.setCount(stateAssets);
		if (totalAssets > 0)
			stateMetrics.setPercent(((double) stateAssets / (double) totalAssets) * 100);

		return stateMetrics;
	}

	// private String getCompleted(SoftwareReleaseSchedule
	// softwareReleaseSchedule) {
	// double res = 0;
	//
	// if (!softwareReleaseSchedule.getObjectIds().isEmpty()) {
	// Integer[] completedUpgrades = { 0 };
	// softwareReleaseSchedule.getObjectIds().stream().forEach(objectId -> {
	// SoftwareReleaseTransSearchParams params = new
	// SoftwareReleaseTransSearchParams();
	// params.addObjectIds(objectId);
	// params.addSoftwareReleaseScheduleIds(softwareReleaseSchedule.getId());
	// List<SoftwareReleaseTrans> softwareReleaseTrans =
	// softwareReleaseTransService
	// .getSoftwareReleaseTransRepository().findSoftwareReleaseTrans(params);
	// if (!softwareReleaseTrans.isEmpty()) {
	// softwareReleaseTrans.stream().sorted(new
	// SoftwareReleaseTransComparator())
	// .collect(Collectors.toList());
	// SoftwareReleaseTrans.Status status =
	// softwareReleaseTrans.get(0).getStatus();
	// if (status == SoftwareReleaseTrans.Status.COMPLETE
	// || status == SoftwareReleaseTrans.Status.CANCELLED) {
	// ++completedUpgrades[0];
	// }
	// }
	// });
	// res = (double) completedUpgrades[0] /
	// softwareReleaseSchedule.getObjectIds().size() * 100;
	// }
	// return (int) res + "%";
	// }

	private String getStart(SoftwareReleaseSchedule softwareReleaseSchedule) {
		return softwareReleaseSchedule.getOnDemand() ? "On Demand"
				: softwareReleaseSchedule.getScheduledDate().toString();
	}

	private String getRequestorName(String requestorId) {
		String requestorName = null;
		if (StringUtils.isNotEmpty(requestorId)) {
			if (requestorId.indexOf("arw:pgs:key:") != -1) {
				requestorName = "API";
			} else if (requestorId.toLowerCase().indexOf("admin") != -1) {
				requestorName = "System Administrator";
			} else {
				User user = getCoreCacheService().findUserById(requestorId);
				if (user != null) {
					requestorName = user.getContact().fullName();
				}
			}
		}
		return requestorName;
	}

	private UserOption getUserOption(String requestorId) {
		UserOption result = null;
		if (StringUtils.isNotEmpty(requestorId)) {
			User user = null;
			if (requestorId.indexOf("arw:pgs:key:") != -1) {
				user = new User();
				user.setId("arw:pgs:key:");
				user.setContact(new Contact("API", null, null));
			} else if (requestorId.toLowerCase().indexOf("admin") != -1) {
				user = new User();
				user.setId("admin");
				user.setContact(new Contact("System Administrator", null, null));
			} else {
				user = getCoreCacheService().findUserById(requestorId);
			}
			if (user != null) {
				result = new UserOption(user);
			}
		}
		return result;
	}

	private DeviceType getDeviceType(AcnDeviceCategory category, String objectId) {
		String deviceTypeId = null;
		DeviceType deviceType = null;
		switch (category) {
		case DEVICE:
			Device device = getKronosCache().findDeviceById(objectId);
			if (device != null) {
				deviceTypeId = device.getDeviceTypeId();
			}
			break;
		case GATEWAY:
			Gateway gateway = getKronosCache().findGatewayById(objectId);
			if (gateway != null) {
				deviceTypeId = gateway.getDeviceTypeId();
			}
			break;
		}
		if (deviceTypeId != null) {
			deviceType = getKronosCache().findDeviceTypeById(deviceTypeId);
		}

		return deviceType;
	}

	private com.arrow.kronos.data.DeviceType getAssetType(String assetTypeId) {
		Assert.hasText(assetTypeId, "assetTypeId is empty");

		DeviceType assetType = getKronosCache().findDeviceTypeById(assetTypeId);
		Assert.notNull(assetType, "deviceType not found! deviceTypeId: " + assetTypeId);

		return assetType;
	}

	// private SoftwareReleaseScheduleAssetModel
	// buildSoftwareReleaseScheduleAssetModel(BaseDeviceAbstract asset,
	// SoftwareReleaseSchedule softwareReleaseSchedule) {
	// Assert.notNull(asset, "asset is null");
	// SoftwareReleaseScheduleAssetModel result = new
	// SoftwareReleaseScheduleAssetModel(asset);
	// // owner
	// result.setOwner(getOwnerName(asset.getUserId()));
	// // firmwareFrom
	// result.setFirmwareFrom(
	// getFromSoftwareReleaseNameByAssetAndSoftwareReleaseSchedule(asset,
	// softwareReleaseSchedule));
	// // firmwareTo
	// result.setFirmwareTo(getSoftwareReleaseName(softwareReleaseSchedule.getSoftwareReleaseId()));
	// // group
	// switch (softwareReleaseSchedule.getDeviceCategory()) {
	// case DEVICE:
	// if (asset.getNodeId() != null) {
	// Node node = getRequestCache().findNodeById(asset.getNodeId());
	// if (node != null) {
	// result.setGroup(node.getName());
	// }
	// }
	// break;
	// case GATEWAY:
	// DeviceType deviceType =
	// getRequestCache().findDeviceTypeById(asset.getDeviceTypeId());
	// if (deviceType != null) {
	// result.setGroup(deviceType.getName());
	// }
	// break;
	// }
	// // status & alerts
	// if (softwareReleaseSchedule.getStatus() ==
	// SoftwareReleaseSchedule.Status.INPROGRESS
	// || softwareReleaseSchedule.getStatus() ==
	// SoftwareReleaseSchedule.Status.COMPLETE) {
	// List<SoftwareReleaseTrans> transList =
	// softwareReleaseTransService.getSoftwareReleaseTransRepository()
	// .findSoftwareReleaseTrans(new SoftwareReleaseTransSearchParams()
	// .addSoftwareReleaseScheduleIds(softwareReleaseSchedule.getId())
	// .addObjectIds(asset.getId()));
	// if (!transList.isEmpty()) {
	// SoftwareReleaseTrans trans = transList.get(0);
	// result.setSoftwareReleaseTransId(trans.getId());
	// result.setStatus(trans.getStatus().name());
	// result.setAlerts(trans.getError());
	// }
	// }
	// return result;
	// }

	private String getFromSoftwareReleaseNameByAssetAndSoftwareReleaseSchedule(BaseDeviceAbstract asset,
			SoftwareReleaseSchedule softwareReleaseSchedule) {
		SoftwareReleaseTransSearchParams params = new SoftwareReleaseTransSearchParams();
		params.addSoftwareReleaseScheduleIds(softwareReleaseSchedule.getId());
		params.addObjectIds(asset.getId());
		List<SoftwareReleaseTrans> softwareReleaseTransRecords = getSoftwareReleaseTransService()
				.getSoftwareReleaseTransRepository().findSoftwareReleaseTrans(params);
		if (softwareReleaseTransRecords != null && softwareReleaseTransRecords.size() == 1) {
			// only this case is fine
			String softwareReleaseId = softwareReleaseTransRecords.get(0).getFromSoftwareReleaseId();
			return getSoftwareReleaseName(softwareReleaseId);
		} else {
			return "-";
		}
	}

	private Page<? extends BaseDeviceAbstract> findAssets(SoftwareReleaseSchedule softwareReleaseSchedule,
			PageRequest pageRequest) {
		switch (softwareReleaseSchedule.getDeviceCategory()) {
		case DEVICE: {
			DeviceSearchParams params = new DeviceSearchParams();
			params.addApplicationIds(softwareReleaseSchedule.getApplicationId());
			softwareReleaseSchedule.getObjectIds().forEach(params::addIds);
			return deviceService.getDeviceRepository().doFindDevices(pageRequest, params);
		}
		case GATEWAY: {
			GatewaySearchParams params = new GatewaySearchParams();
			params.addApplicationIds(softwareReleaseSchedule.getApplicationId());
			softwareReleaseSchedule.getObjectIds().forEach(params::addIds);
			return gatewayService.getGatewayRepository().findGateways(pageRequest, params);
		}
		default:
			throw new AcsLogicalException("Unsupported device category");
		}
	}

	private BaseDeviceAbstract getAsset(AcnDeviceCategory category, String objectId) {
		BaseDeviceAbstract result = null;
		switch (category) {
		case DEVICE:
			Device device = getKronosCache().findDeviceById(objectId);
			if (device != null)
				result = device;
			break;
		case GATEWAY:
			Gateway gateway = getKronosCache().findGatewayById(objectId);
			if (gateway != null)
				result = gateway;
			break;
		}
		return result;
	}

	private String getAssetName(AcnDeviceCategory category, String objectId) {
		String result = null;
		switch (category) {
		case DEVICE:
			Device device = getKronosCache().findDeviceById(objectId);
			if (device != null) {
				result = device.getName();
			}
			break;
		case GATEWAY:
			Gateway gateway = getKronosCache().findGatewayById(objectId);
			if (gateway != null) {
				result = gateway.getName();
			}
			break;
		}
		return result;
	}

	private Set<String> getObjectIdsFromLogParams(String params) {
		Set<String> ids = null;
		if (params != null) {
			ids = new HashSet<>(Arrays.asList(params.split("\\s*,\\s*")));
		} else {
			ids = new HashSet<>();
		}
		return ids;
	}

	private List<SoftwareReleaseScheduleAssetModel> getUnknownAssets(Page<?> assets,
			SoftwareReleaseSchedule softwareReleaseSchedule) {
		long totalAssets = softwareReleaseSchedule.getObjectIds().size();
		List<SoftwareReleaseScheduleAssetModel> result = new ArrayList<>();
		if (totalAssets <= 0) {
			return result;
		}
		int pageSize = assets.getSize();
		long unknownAssetsCount = totalAssets - assets.getTotalElements();
		int totalPages = (int) Math.ceil((double) totalAssets / (double) pageSize);
		if (unknownAssetsCount > 0 && assets.getNumberOfElements() < pageSize && assets.getNumber() < totalPages) {
			boolean lastPage = assets.getNumber() == totalPages - 1;
			long assetsPerPage = lastPage ? totalAssets % pageSize : pageSize;
			if (assetsPerPage == 0) {
				assetsPerPage = pageSize;
			}
			long unknownAssetsPerPage = assetsPerPage - assets.getNumberOfElements();
			for (int i = 0; i < unknownAssetsPerPage; i++) {
				SoftwareReleaseScheduleAssetModel model = new SoftwareReleaseScheduleAssetModel(null, null, "Unknown");
				model.setUid("Unknown");
				model.setFirmwareFrom("Unknown");
				model.setStatus("N/A");
				if (softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.CANCELLED)
					model.setStatus(SoftwareReleaseTrans.Status.CANCELLED.name());

				result.add(model);
			}
		}
		return result;
	}

	// private String getHwName(AcnDeviceCategory category, String objectId) {
	// String rheaDeviceTypeName = null;
	// DeviceType deviceType = getDeviceType(category, objectId);
	// if (deviceType != null) {
	// String rheaDeviceTypeId = deviceType.getRheaDeviceTypeId();
	// if (rheaDeviceTypeId != null) {
	// com.arrow.rhea.data.DeviceType rheaDeviceType =
	// rheaClientCache.findDeviceTypeById(rheaDeviceTypeId);
	// if (rheaDeviceType != null) {
	// rheaDeviceTypeName = rheaDeviceType.getName();
	// }
	// }
	// }
	//
	// return rheaDeviceTypeName;
	// }

	// private String getHwId(AcnDeviceCategory category, String objectId) {
	//
	// DeviceType deviceType = getDeviceType(category, objectId);
	// if (deviceType != null) {
	// String rheaDeviceTypeId = deviceType.getRheaDeviceTypeId();
	// if (rheaDeviceTypeId != null) {
	// com.arrow.rhea.data.DeviceType rheaDeviceType =
	// rheaClientCache.findDeviceTypeById(rheaDeviceTypeId);
	// if (rheaDeviceType != null) {
	// return rheaDeviceType.getId();
	// }
	// }
	// }
	//
	// return null;
	// }
}

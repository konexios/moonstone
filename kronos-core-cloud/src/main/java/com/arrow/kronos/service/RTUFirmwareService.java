package com.arrow.kronos.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.EligibleFirmwareChangeGroup;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.SoftwareReleaseSchedule;
import com.arrow.kronos.data.SoftwareReleaseSchedule.Status;
import com.arrow.kronos.repo.DeviceSearchParams;
import com.arrow.kronos.repo.DeviceTypeSearchParams;
import com.arrow.kronos.repo.GatewaySearchParams;
import com.arrow.kronos.repo.SoftwareReleaseScheduleRepository;
import com.arrow.kronos.repo.SoftwareReleaseScheduleSearchParams;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.rhea.client.api.ClientSoftwareReleaseApi;
import com.arrow.rhea.data.SoftwareRelease;

import moonstone.acn.client.model.RightToUseType;

@Service
public class RTUFirmwareService extends KronosServiceAbstract {

	@Autowired
	private SoftwareReleaseScheduleRepository softwareReleaseScheduleRepository;
	@Autowired
	private DeviceTypeService deviceTypeService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private GatewayService gatewayService;
	@Autowired
	private ClientSoftwareReleaseApi rheaClientSoftwareReleaseApi;

	@Autowired
	private KronosHttpRequestCache requestCache;

	public SoftwareReleaseScheduleRepository getSoftwareReleaseScheduleRepository() {
		return softwareReleaseScheduleRepository;
	}

	private Collection<EligibleFirmwareChangeGroup> getInnerEligibleModels(String applicationId, List<DeviceType> deviceTypes) {
		Assert.hasText(applicationId, "applicationId is empty");

		String method = "getInnerEligibleModels";

		Application application = requestCache.findApplicationById(applicationId, false);
		Assert.notNull(application, "application not found! applicationId: " + applicationId);

		// STEP 1
		// lookup active assets types that have a rheaDeviceTypeId
		List<DeviceType> assetTypes = new ArrayList<>();
		if(deviceTypes != null) {
			assetTypes = deviceTypes;
		} else {
			DeviceTypeSearchParams assetTypeSearchParams = new DeviceTypeSearchParams();
			assetTypeSearchParams.addApplicationIds(applicationId);
			assetTypeSearchParams.setEnabled(true);
			assetTypeSearchParams.setRheaDeviceTypeDefined(true);
			assetTypes = deviceTypeService.getDeviceTypeRepository()
					.findDeviceTypes(assetTypeSearchParams);
		}
		logDebug(method, "assetTypes size: %s", (assetTypes == null ? 0 : assetTypes.size()));

		Map<String, EligibleFirmwareChangeGroup> eligibleGroupResult = new HashMap<>();
		if (assetTypes != null && !assetTypes.isEmpty()) {
			// derive if the firmware management feature has been purchased
			boolean hasFirmwareManagementFeature = false;
			if (application.getProductFeatures() != null && !application.getProductFeatures().isEmpty()
			        && application.getProductFeatures().contains(KronosConstants.ProductFeatures.FIRMWARE_MANAGEMENT))
				hasFirmwareManagementFeature = true;
			logDebug(method, "hasFirmwareManagementFeature: " + hasFirmwareManagementFeature);

			Map<String, DeviceType> assetTypeMap = new HashMap<String, DeviceType>();
			assetTypes.stream().forEach(assetType -> {
				assetTypeMap.put(assetType.getId(), assetType);
			});

			Set<EligibleFirmwareChangeGroup> logicalEligibleGroupSet = new HashSet<>();
			if (!assetTypeMap.isEmpty()) {
				// STEP 2
				// derive a distinct set of asset type, hardware version,
				// firmware version based on devices
				DeviceSearchParams deviceSearchParams = new DeviceSearchParams();
				deviceSearchParams.addApplicationIds(applicationId);
				deviceSearchParams.setEnabled(true);
				deviceSearchParams.setSoftwareReleaseIdDefined(true);
				deviceSearchParams
				        .addDeviceTypeIds(assetTypeMap.keySet().toArray(new String[assetTypeMap.keySet().size()]));

				List<Device> devices = deviceService.getDeviceRepository().doFindAllDevices(deviceSearchParams);
				logDebug(method, "devices size: %s", devices.size());
				devices.stream().forEach(device -> {
					DeviceType assetType = assetTypeMap.get(device.getDeviceTypeId());
					Assert.notNull(assetType,
					        "assetType is missing from map! assetTypeId: " + device.getDeviceTypeId());
					logicalEligibleGroupSet.add(
					        new EligibleFirmwareChangeGroup(device.getDeviceTypeId(), assetType.getRheaDeviceTypeId(),
					                device.getSoftwareReleaseId(), assetType.getDeviceCategory()));
				});
				logDebug(method, "logicalEligibleGroupSet size: %s", logicalEligibleGroupSet.size());

				// STEP 3
				// derive a distinct set of asset type, hardware version,
				// firmware version based on gateways
				GatewaySearchParams gatewaySearchParams = new GatewaySearchParams();
				gatewaySearchParams.addApplicationIds(applicationId);
				gatewaySearchParams.setEnabled(true);
				gatewaySearchParams.setSoftwareReleaseIdDefined(true);
				gatewaySearchParams
				        .addDeviceTypeIds(assetTypeMap.keySet().toArray(new String[assetTypeMap.keySet().size()]));

				List<Gateway> gateways = gatewayService.getGatewayRepository().findGateways(gatewaySearchParams);
				logDebug(method, "gateways size: %s", gateways.size());
				gateways.stream().forEach(gateway -> {
					DeviceType assetType = assetTypeMap.get(gateway.getDeviceTypeId());
					Assert.notNull(assetType,
					        "assetType is missing from map! assetTypeId: " + gateway.getDeviceTypeId());
					logicalEligibleGroupSet.add(
					        new EligibleFirmwareChangeGroup(gateway.getDeviceTypeId(), assetType.getRheaDeviceTypeId(),
					                gateway.getSoftwareReleaseId(), assetType.getDeviceCategory()));
				});
				logDebug(method, "logicalEligibleGroupSet size: %s", logicalEligibleGroupSet.size());
			}
			logDebug(method, "logicalEligibleGroupSet size: %s", logicalEligibleGroupSet.size());

			// STEP 4
			// find software releases based on logical groups
			
			// if firmware management feature was purchased then include right
			// to use types
			List<SoftwareRelease> softwareReleases = new ArrayList<>();
			if (hasFirmwareManagementFeature) {
				EnumSet<RightToUseType> rtuTypes = EnumSet.of(RightToUseType.Private);
				
				String[] rtuTypeNames = new String[rtuTypes.size()];
				int rightToUseNameCount = 0;
				for (RightToUseType rtut : rtuTypes) {
					rtuTypeNames[rightToUseNameCount++] = rtut.name();
				}
				
				softwareReleases = rheaClientSoftwareReleaseApi
				        .findAll(null, null, logicalEligibleGroupSet.stream()
				                       .map(logicalGroup -> logicalGroup.rheaDeviceTypeId).toArray(String[]::new),
				                       rtuTypeNames, true, null);
			}
			logDebug(method, "softwareReleases size: %s", softwareReleases.size());

			// STEP 5
			// evaluate each software release
			softwareReleases.forEach(softwareRelease -> {
				softwareRelease.setRefSoftwareProduct(
				        requestCache.findSoftwareProductById(softwareRelease.getSoftwareProductId()));
				logDebug(method, "checking software release: %s major: %s minor: %s build: %s",
				        softwareRelease.getRefSoftwareProduct().getName(), softwareRelease.getMajor(),
				        softwareRelease.getMinor(), softwareRelease.getBuild());

				List<String> upgradeableFromIds = softwareRelease.getUpgradeableFromIds();
				List<String> deviceTypeIds = softwareRelease.getDeviceTypeIds();
				if (!upgradeableFromIds.isEmpty() && !deviceTypeIds.isEmpty()) {
					for (EligibleFirmwareChangeGroup eligibleGroup : logicalEligibleGroupSet) {
						if (upgradeableFromIds.contains(eligibleGroup.softwareReleaseId)
						        && deviceTypeIds.contains(eligibleGroup.rheaDeviceTypeId)) {
							String key = eligibleGroup.softwareReleaseId + "|" + eligibleGroup.rheaDeviceTypeId;
							EligibleFirmwareChangeGroup existingEligibleGroup = eligibleGroupResult.get(key);
							if (existingEligibleGroup == null)
								existingEligibleGroup = eligibleGroup;

							existingEligibleGroup.newSoftwareReleaseIds.add(softwareRelease.getId());
							eligibleGroupResult.put(key, eligibleGroup);
						}
					}
				}
			});

			// STEP 6
			// derive the count of devices and gateways
			SoftwareReleaseScheduleSearchParams params = new SoftwareReleaseScheduleSearchParams();
			params.addApplicationIds(applicationId);
			if (!eligibleGroupResult.isEmpty()) {
				Set<String> scheduledDevices = getSoftwareReleaseScheduleRepository().findSoftwareReleaseSchedules
						(params).stream().filter(schedule ->
								schedule.getStatus() == Status.SCHEDULED ||
								schedule.getStatus() == Status.INPROGRESS).map(
						SoftwareReleaseSchedule::getObjectIds).flatMap(Collection::stream).collect(
						Collectors.toSet());
				eligibleGroupResult.values().forEach(eligibleGroup -> {
					switch (eligibleGroup.category) {
					case DEVICE:
						DeviceSearchParams deviceSearchParams = new DeviceSearchParams();
						deviceSearchParams.addApplicationIds(applicationId);
						deviceSearchParams.setEnabled(true);
						deviceSearchParams.addDeviceTypeIds(eligibleGroup.deviceTypeId);
						deviceSearchParams.addSoftwareReleaseIds(eligibleGroup.softwareReleaseId);
						List<Device> devices = deviceService.getDeviceRepository().doFindAllDevices(deviceSearchParams);
						int numberOfDevices = 0;
						for (Device device : devices) {
							if (!scheduledDevices.contains(device.getId())) {
								numberOfDevices++;
							}
						}
						eligibleGroup.setNumberOfDevices(numberOfDevices);
						break;
					case GATEWAY:
						GatewaySearchParams gatewaySearchParams = new GatewaySearchParams();
						gatewaySearchParams.addApplicationIds(applicationId);
						gatewaySearchParams.setEnabled(true);
						gatewaySearchParams.addDeviceTypeIds(eligibleGroup.deviceTypeId);
						gatewaySearchParams.addSoftwareReleaseIds(eligibleGroup.softwareReleaseId);
						List<Gateway> gateways = gatewayService.getGatewayRepository().findGateways
								(gatewaySearchParams);
						int numberOfGateways = 0;
						for (Gateway gateway : gateways) {
							if (!scheduledDevices.contains(gateway.getId())) {
								numberOfGateways++;
							}
						}
						eligibleGroup.setNumberOfDevices(numberOfGateways);
						break;
					}

					logDebug(method,
					        "eligibleGroup: deviceTypeId: %s, rheaDeviceTypeId: %s, softwareReleaseId: %s, newSoftwareReleaseId: %s, numberOfDevices: %s, newSoftwareVersions: %s",
					        eligibleGroup.deviceTypeId, eligibleGroup.rheaDeviceTypeId, eligibleGroup.softwareReleaseId,
					        eligibleGroup.newSoftwareReleaseIds, eligibleGroup.numberOfDevices,
					        eligibleGroup.newSoftwareReleaseIds.size());
				});
			}
		}

		return eligibleGroupResult.values();
	}
	
	public List<EligibleFirmwareChangeGroup> getRTUEligibleUpgrades(String applicationId, List<DeviceType> assetTypes) {
		Assert.hasText(applicationId, "applicationId is empty");

		Collection<EligibleFirmwareChangeGroup> eligibleGroups = getInnerEligibleModels(applicationId, assetTypes);
		if (eligibleGroups != null)
			return new ArrayList<>(eligibleGroups);
		else
			return Collections.emptyList();
	}

}

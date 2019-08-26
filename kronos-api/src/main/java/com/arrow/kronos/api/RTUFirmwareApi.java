package com.arrow.kronos.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.EligibleFirmwareChangeGroup;
import com.arrow.kronos.service.KronosHttpRequestCache;
import com.arrow.kronos.service.RTUFirmwareService;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.rhea.client.api.ClientCacheApi;
import com.arrow.rhea.client.api.ClientRTURequestApi;
import com.arrow.rhea.data.RTURequest;
import com.arrow.rhea.data.SoftwareRelease;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import moonstone.acn.client.model.RightToUseStatus;
import moonstone.acn.client.model.SoftwareReleaseScheduleModel;
import moonstone.acn.client.model.RTUFirmwareModels.AvailableFirmwareVersion;
import moonstone.acn.client.model.RTUFirmwareModels.RTUFirmwareModel;
import moonstone.acn.client.model.RTUFirmwareModels.RTURequestedFirmwareModel;
import moonstone.acs.client.model.PagingResultModel;
import moonstone.acs.client.model.StatusModel;

@RestController
@RequestMapping("/api/v1/kronos/rtu")
public class RTUFirmwareApi extends SoftwareReleaseApiAbstract {
	@Autowired
	private ClientCacheApi rheaClientCache;
	@Autowired
	private ClientRTURequestApi clientRTURequestApi;
	@Autowired
	private RTUFirmwareService rtuFirmwareService;
	@Autowired
	public KronosHttpRequestCache requestCache;

	@ApiOperation(value = "request right to use firmware", response = SoftwareReleaseScheduleModel.class)
	@RequestMapping(path = "/request/{softwareReleaseHid}", method = RequestMethod.PUT)
	public StatusModel request(
			@ApiParam(value = "required software release hid", required = true) @PathVariable(name = "softwareReleaseHid") String softwareReleaseHid, 
			HttpServletRequest request) {
		String method = "request";
		AccessKey accessKey = validateCanReadApplication(getProductSystemName());
		Assert.hasText(softwareReleaseHid, "softwareReleaseHid is empty");
		
		SoftwareRelease softwareRelease = rheaClientCache.findSoftwareReleaseByHid(softwareReleaseHid);
		Assert.notNull(softwareRelease, "softwareRelease was not found");
		Application application = getCoreCacheService().findApplicationById(accessKey.getApplicationId());
		Assert.notNull(application, "application was not found");
		
		auditLog(method, accessKey.getApplicationId(), null, accessKey.getId(), request);

		RTURequest checkRequest = rheaClientCache.findRTURequestByName(application.getCompanyId(), softwareRelease.getId());
		if(checkRequest == null) {
			RTURequest rtuRequest = new RTURequest();
			Assert.notNull(request, "rtuRequest is not found");
			rtuRequest.setCompanyId(application.getCompanyId());
			rtuRequest.setFirmwareVersion(getSoftwareReleaseName(softwareRelease.getId()));
			rtuRequest.setSoftwareReleaseId(softwareRelease.getId());
			rtuRequest = clientRTURequestApi.create(rtuRequest, accessKey.getId());
			if(rtuRequest == null)
				return StatusModel.error("RTURequest was not created.");
		} else {
			return StatusModel.error("RTURequest with softwareRelease:" 
					+ getSoftwareReleaseName(softwareRelease.getId()) + " already exists.");
		}

		return StatusModel.OK;
	}

	@ApiOperation(value = "get list requested firmware")
	@RequestMapping(path = "/find", method = RequestMethod.GET)
	public PagingResultModel<RTURequestedFirmwareModel> getRequestedFirmware(
	        @RequestParam(name = "status", required = false) RightToUseStatus status,
	        @RequestParam(name = "_page", required = false, defaultValue = "0") int page,
	        @RequestParam(name = "_size", required = false, defaultValue = "100") int size) {

		Assert.isTrue(page >= 0, "page must be positive");
		Assert.isTrue(size >= 0 && size <= KronosConstants.PageResult.MAX_SIZE,
		        "size must be between 0 and " + KronosConstants.PageResult.MAX_SIZE);

		AccessKey accessKey = validateCanReadApplication(getProductSystemName());
		Application application = getCoreCacheService().findApplicationById(accessKey.getApplicationId());
		Assert.notNull(application, "application was not found");

		PagingResultModel<RTURequestedFirmwareModel> result = new PagingResultModel<>();
		result.setPage(page);
		PageRequest pageRequest = PageRequest.of(page, size);

		String[] statusesArray = null;
		if (status != null) {
			statusesArray = new String[] { status.toString() };
		}

		Page<RTURequest> rtuRequests = clientRTURequestApi.findAll(pageRequest,
		        new String[] { application.getCompanyId() }, statusesArray, (String[]) null);

		List<RTURequestedFirmwareModel> data = rtuRequests.getContent().stream()
		        .map(rtuRequest -> buildSoftwareReleaseScheduleModel(rtuRequest))
		        .collect(Collectors.toCollection(ArrayList::new));

		result.withTotalPages(rtuRequests.getTotalPages()).withTotalSize(rtuRequests.getTotalElements())
		        .withSize(rtuRequests.getNumberOfElements()).withData(data);
		return result;
	}

	private RTURequestedFirmwareModel buildSoftwareReleaseScheduleModel(RTURequest rtuRequest) {
		RTURequestedFirmwareModel rtuRequestFirmwareModel = new RTURequestedFirmwareModel();

		SoftwareRelease softwareRelease = requestCache.findSoftwareReleaseById(rtuRequest.getSoftwareReleaseId());
		String softwareReleaseHid = null;
		if (softwareRelease != null) {
			rtuRequestFirmwareModel.withOwnerEmail(softwareRelease.getOwnerEmail());
			rtuRequestFirmwareModel.withOwnerName(softwareRelease.getOwnerName());
			softwareReleaseHid = softwareRelease.getHid();
		}
		rtuRequestFirmwareModel.setAvailableFirmwareVersion(
		        new AvailableFirmwareVersion(softwareReleaseHid, rtuRequest.getFirmwareVersion()));

		rtuRequestFirmwareModel.setStatus(rtuRequest.getStatus());
		return rtuRequestFirmwareModel;
	}

	private List<RTUFirmwareModel> getRTUAvailableModelList(List<DeviceType> deviceTypes,
			String applicationId) {
		Application application = getCoreCacheService().findApplicationById(applicationId);
		Assert.notNull(application, "application was not found");

		List<EligibleFirmwareChangeGroup> eligibleFirmwareChangeGroups = rtuFirmwareService
		        .getRTUEligibleUpgrades(applicationId, deviceTypes);

		// convert to visual model
		List<RTUFirmwareModel> list = new ArrayList<>();
		eligibleFirmwareChangeGroups.stream().forEach(item -> {
			DeviceType assetType = requestCache.findDeviceTypeById(item.deviceTypeId);
			Assert.notNull(assetType, "deviceType was not found");
			SoftwareRelease softwareRelease = requestCache.findSoftwareReleaseById(item.softwareReleaseId);
			Assert.notNull(softwareRelease, "softwareRelease was not found");
			
			item.newSoftwareReleaseIds.forEach(softwareReleaseId -> {
			    RTURequest request = rheaClientCache.findRTURequestByName(application.getCompanyId(),
		                softwareReleaseId);
			    if (request == null) {
				    RTUFirmwareModel rtuFirmwareModel = new RTUFirmwareModel();
				    rtuFirmwareModel.withDeviceTypeHid(assetType.getHid());
				    rtuFirmwareModel.withDeviceTypeName(assetType.getName());
				    rtuFirmwareModel.withNumberOfAssets(item.numberOfDevices);
				    rtuFirmwareModel.withHardwareVersionName(getHardwareVersionName(item.rheaDeviceTypeId));
				    rtuFirmwareModel.withCurrentFirmwareVersion(new AvailableFirmwareVersion(softwareRelease.getHid(),
		                    getSoftwareReleaseName(item.softwareReleaseId)));

				    SoftwareRelease availableSoftwareRelease = requestCache.findSoftwareReleaseById(softwareReleaseId);
				    rtuFirmwareModel.withAvailableFirmwareVersion(new AvailableFirmwareVersion(
		                    availableSoftwareRelease.getHid(), getSoftwareReleaseName(softwareReleaseId)));
				    list.add(rtuFirmwareModel);
			    }
		    });
		});
		list.sort(Comparator.comparing(RTUFirmwareModel::getDeviceTypeName, String.CASE_INSENSITIVE_ORDER));
		return list;
	}

	@ApiOperation(value = "get list available firmware")
	@RequestMapping(path = "/find/available", method = RequestMethod.GET)
	public List<RTUFirmwareModel> getAvailableFirmware(
	        @RequestParam(name = "deviceTypeHid", required = false) String deviceTypeHid) {

		AccessKey accessKey = validateCanReadApplication(getProductSystemName());
		
		List<DeviceType> deviceTypes = null;
		if(StringUtils.hasText(deviceTypeHid)) {
			DeviceType deviceType = getKronosCache().findDeviceTypeByHid(deviceTypeHid);
			Assert.notNull(deviceType, "deviceType was not found");
			deviceTypes = new ArrayList<>();
			deviceTypes.add(deviceType);
		}

		return getRTUAvailableModelList(deviceTypes, accessKey.getApplicationId());
	}

	protected String getHardwareVersionName(String assetTypeId) {
		return getHardwareVersion(assetTypeId).getName();
	}

	private com.arrow.rhea.data.DeviceType getHardwareVersion(String hardwareVersionId) {
		Assert.hasText(hardwareVersionId, "hardwareVersionId is empty");

		com.arrow.rhea.data.DeviceType hardwareVersion = requestCache.findRheaDeviceTypeById(hardwareVersionId);
		Assert.notNull(hardwareVersion, "hardwareVersion not found! rheaDeviceTypeId: " + hardwareVersionId);

		return hardwareVersion;
	}
}

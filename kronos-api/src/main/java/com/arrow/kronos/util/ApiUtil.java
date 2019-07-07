package com.arrow.kronos.util;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.arrow.acn.client.model.AuditLogModel;
import com.arrow.acn.client.model.ConfigBackupModel;
import com.arrow.acn.client.model.DeviceActionModel;
import com.arrow.acn.client.model.DeviceModel;
import com.arrow.acn.client.model.LastLocationModel;
import com.arrow.acn.client.model.LastLocationRegistrationModel;
import com.arrow.acn.client.model.TestProcedureStepModel;
import com.arrow.acn.client.model.TestResultModel;
import com.arrow.acn.client.model.TestResultStepModel;
import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.ConfigBackup;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.TestProcedure;
import com.arrow.kronos.data.TestProcedureStep;
import com.arrow.kronos.data.TestResult;
import com.arrow.kronos.data.TestResultStep;
import com.arrow.kronos.service.DeviceService;
import com.arrow.kronos.service.KronosCache;
import com.arrow.pegasus.data.AuditLog;
import com.arrow.pegasus.data.location.LastLocation;
import com.arrow.pegasus.data.location.LastLocationType;
import com.arrow.pegasus.data.location.LocationObjectType;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.service.CoreCacheService;
import com.arrow.pegasus.service.LastLocationService;

@Component
public class ApiUtil {

	@Autowired
	private CoreCacheService coreCacheService;
	@Autowired
	private KronosCache kronosCache;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private LastLocationService lastLocationService;

	public Instant parseDateParam(String dateString) {
		Instant result = null;
		if (dateString != null) {
			try {
				result = Instant.parse(dateString);
			} catch (Exception e) {
				throw new AcsLogicalException("Invalid date format");
			}
		}
		return result;
	}

	public Integer validateSize(Integer size) {
		int maxSize = KronosConstants.PageResult.MAX_SIZE;
		if (size == null) {
			size = maxSize;
		}
		Assert.isTrue(size > 0 && size <= maxSize, "size must be between 1 and " + maxSize);
		return size;
	}

	public Integer validatePage(Integer page) {
		if (page == null) {
			page = 0;
		}
		Assert.isTrue(page >= 0, "page must be positive");
		return page;
	}

	public PageRequest buildPageRequest(int page, int size, String sortField, String sortDirection) {
		PageRequest pageRequest = null;
		if (sortField != null) {
			Direction direction = sortDirection != null ? Direction.valueOf(sortDirection) : Direction.ASC;
			pageRequest = PageRequest.of(page, size, direction, sortField);
		} else {
			pageRequest = PageRequest.of(page, size);
		}
		return pageRequest;
	}

	public Set<String> getUserIdsByHids(String[] userHids) {
		Set<String> userIds = new HashSet<>();
		if (userHids != null) {
			Arrays.stream(userHids).forEach(userHid -> {
				User user = coreCacheService.findUserByHid(userHid);
				Assert.notNull(user, "user with hid: " + userHid + " not found");
				userIds.add(user.getId());
			});
		}
		return userIds;
	}

	/**
	 * @deprecated use
	 *             {@link com.arrow.kronos.api.BaseApiAbstract#buildAuditLogModel(AuditLog, String)}
	 */
	@Deprecated
	public AuditLogModel buildAuditLogModel(AuditLog log, String hid) {
		if (log == null) {
			return null;
		}
		AuditLogModel model = new AuditLogModel();
		String createdBy = log.getCreatedBy();
		User user = coreCacheService.findUserById(log.getCreatedBy());
		if (user != null) {
			createdBy = user.getContact() != null ? user.getContact().fullName() : createdBy;
		}
		model.setCreatedBy(createdBy);
		model.setCreatedDate(log.getCreatedDate());
		model.setObjectHid(hid);
		model.setProductName(log.getProductName());
		model.setType(log.getType());
		Map<String, String> parameters = new HashMap<>();
		parameters.putAll(log.getParameters());
		model.setParameters(parameters);
		return model;
	}

	/**
	 * @deprecated use
	 *             {@link com.arrow.kronos.api.BaseApiAbstract#buildDeviceModel(Device)}
	 */
	@Deprecated
	public DeviceModel buildDeviceModel(Device device) {
		if (device != null) {
			device = deviceService.populate(device);
			DeviceModel model = new DeviceModel();
			model.setHid(device.getHid());
			if (device.getRefDeviceType() != null) {
				model.setType(device.getRefDeviceType().getName());
			} else {
				model.setType("ERROR");
			}
			if (StringUtils.isNotEmpty(device.getGatewayId())) {
				Gateway gateway = kronosCache.findGatewayById(device.getGatewayId());
				if (gateway != null) {
					model.setGatewayHid(gateway.getHid());
				} else {
					model.setGatewayHid("ERROR");
				}
			}
			if (StringUtils.isNoneEmpty(device.getUserId())) {
				User user = coreCacheService.findUserById(device.getUserId());
				if (user != null) {
					model.setUserHid(user.getHid());
				} else {
					model.setUserHid("ERROR");
				}
			}
			model.setEnabled(device.isEnabled());
			model.setName(device.getName());
			model.setUid(device.getUid());
			model.setCreatedDate(device.getCreatedDate());
			model.setInfo(device.getInfo());
			model.setProperties(device.getProperties());
			model.setCreatedDate(device.getCreatedDate());
			model.setCreatedBy(device.getCreatedBy());
			model.setLastModifiedDate(device.getLastModifiedDate());
			model.setLastModifiedBy(device.getLastModifiedBy());
			model.setSoftwareName(device.getSoftwareName());
			model.setSoftwareVersion(device.getSoftwareVersion());

			return model;
		} else {
			return null;
		}
	}

	/**
	 * @deprecated use
	 *             {@link com.arrow.kronos.api.BaseApiAbstract#buildLastLocationModel(LastLocation,String)}
	 */
	@Deprecated
	public LastLocationModel buildLastLocationModel(LastLocation lastLocation, String hid) {
		if (lastLocation == null) {
			return null;
		}
		LastLocationModel result = new LastLocationModel();
		result.setHid(lastLocation.getHid());
		result.setObjectType(lastLocation.getObjectType().toString());
		result.setObjectHid(hid);
		result.setLocationType(lastLocation.getLocationType().toString());
		result.setLatitude(lastLocation.getLatitude());
		result.setLongitude(lastLocation.getLongitude());
		result.setTimestamp(lastLocation.getTimestamp() == null ? null : lastLocation.getTimestamp().toString());
		return result;
	}

	public LastLocation updateLastLocation(LastLocationRegistrationModel model, LocationObjectType objectType,
			String objectId) {
		Assert.notNull(model, "lastLocationRegistrationModel is null");

		Double latitude = model.getLatitude();
		Assert.notNull(latitude, "latitude is null");
		Assert.isTrue(latitude >= -90 && latitude <= 90, "invalid latitude");

		Double longitude = model.getLongitude();
		Assert.notNull(longitude, "longitude is null");
		Assert.isTrue(longitude >= -180 && longitude <= 180, "invalid longitude");

		Assert.hasText(model.getLocationType(), "location type is empty");
		LastLocationType lastLocationType = LastLocationType.valueOf(model.getLocationType());

		LastLocation lastLocation = new LastLocation();
		lastLocation.setObjectType(objectType);
		lastLocation.setObjectId(objectId);
		lastLocation.setLocationType(lastLocationType);
		lastLocation.setLatitude(latitude);
		lastLocation.setLongitude(longitude);
		lastLocation = lastLocationService.update(lastLocation);
		return lastLocation;
	}

	@Deprecated
	public List<TestResultStepModel> buildTestResultStepModelList(List<TestResultStep> steps) {
		if (steps != null) {
			return steps.stream().map(this::buildTestResultStepModel).collect(Collectors.toCollection(ArrayList::new));
		} else {
			return null;
		}
	}

	@Deprecated
	public TestResultStepModel buildTestResultStepModel(TestResultStep step) {
		TestResultStepModel result = new TestResultStepModel();
		result.setComment(step.getComment());
		if (step.getEnded() != null) {
			result.setEnded(step.getEnded().toString());
		}
		result.setError(step.getError());
		if (step.getStarted() != null) {
			result.setStarted(step.getStarted().toString());
		}
		if (step.getStatus() != null) {
			result.setStatus(step.getStatus().toString());
		}
		if (step.getDefinition() != null) {
			result.setDefinition(buildTestProcedureStepModel(step.getDefinition()));
		}
		return result;
	}

	/**
	 * @deprecated use
	 *             {@link com.arrow.kronos.api.BaseApiAbstract#buildTestResultModel(TestResult)}
	 */
	@Deprecated
	public TestResultModel buildTestResultModel(TestResult testResult) {
		if (testResult == null) {
			return null;
		}
		TestResultModel result = new TestResultModel();
		result.setHid(testResult.getHid());
		if (testResult.getEnded() != null) {
			result.setEnded(testResult.getEnded().toString());
		}
		if (testResult.getStarted() != null) {
			result.setStarted(testResult.getStarted().toString());
		}
		result.setCreatedDate(testResult.getCreatedDate());
		result.setStatus(testResult.getStatus().toString());
		result.setSteps(buildTestResultStepModelList(testResult.getSteps()));
		TestProcedure testProcedure = kronosCache.findTestProcedureById(testResult.getTestProcedureId());
		if (testProcedure != null) {
			result.setTestProcedureHid(testProcedure.getHid());
		}
		// TODO use device category
		Gateway gateway = kronosCache.findGatewayById(testResult.getObjectId());
		if (gateway != null) {
			result.setObjectHid(gateway.getHid());
		} else {
			Device device = kronosCache.findDeviceById(testResult.getObjectId());
			if (device != null) {
				result.setObjectHid(device.getHid());
			}
		}
		return result;
	}

	/**
	 * @deprecated use
	 *             {@link com.arrow.kronos.api.BaseApiAbstract#buildTestProcedureStepModel(TestProcedureStep)}
	 */
	@Deprecated
	private TestProcedureStepModel buildTestProcedureStepModel(TestProcedureStep step) {
		TestProcedureStepModel model = new TestProcedureStepModel();
		model.setId(step.getId());
		model.setDescription(step.getDescription());
		model.setName(step.getName());
		model.setSortOrder(step.getSortOrder());
		return model;
	}

	public DeviceActionModel buildDeviceActionModel(int index, DeviceAction deviceAction,
			DeviceActionType deviceActionType) {
		Assert.notNull(deviceAction, "deviceAction is null");
		Assert.notNull(deviceActionType, "deviceActionType is null");
		DeviceActionModel model = new DeviceActionModel();
		model.setIndex(index);
		model.setCriteria(deviceAction.getCriteria());
		model.setNoTelemetry(deviceAction.isNoTelemetry());
		model.setNoTelemetryTime(deviceAction.getNoTelemetryTime());
		model.setDescription(deviceAction.getDescription());
		model.setEnabled(deviceAction.isEnabled());
		model.setExpiration(deviceAction.getExpiration());
		model.setParameters(deviceAction.getParameters());
		model.setSystemName(deviceActionType.getSystemName());
		return model;
	}

	public List<DeviceActionModel> buildDeviceActionModelList(List<DeviceAction> deviceActions) {
		if (deviceActions == null || deviceActions.isEmpty()) {
			return new ArrayList<>();
		}
		List<DeviceActionModel> result = new ArrayList<>(deviceActions.size());
		for (int i = 0; i < deviceActions.size(); i++) {
			DeviceAction deviceAction = deviceActions.get(i);
			DeviceActionType deviceActionType = kronosCache
					.findDeviceActionTypeById(deviceAction.getDeviceActionTypeId());
			result.add(buildDeviceActionModel(i, deviceAction, deviceActionType));
		}
		return result;
	}

	/**
	 * @deprecated use
	 *             {@link com.arrow.kronos.api.BaseApiAbstract#buildConfigBackupModel(ConfigBackup)}
	 */
	@Deprecated
	public ConfigBackupModel buildConfigBackupModel(ConfigBackup configBackup) {
		if (configBackup == null) {
			return null;
		}
		ConfigBackupModel result = new ConfigBackupModel();
		result.withCreatedDate(configBackup.getCreatedDate()).withHid(configBackup.getHid())
				.withName(configBackup.getName())
				.withType(ConfigBackupModel.Type.valueOf(configBackup.getType().name()));
		return result;
	}
}
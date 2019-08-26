package com.arrow.kronos.api;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.converter.TelemetryItemConverter;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceTelemetry;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.LastTelemetryItem;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.kronos.data.TelemetryStat;
import com.arrow.kronos.repo.TelemetryItemSearchParams;
import com.arrow.kronos.service.DeviceService;
import com.arrow.kronos.service.LastTelemetryItemService;
import com.arrow.kronos.service.TelemetryItemService;
import com.arrow.kronos.util.ApiUtil;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Application;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import moonstone.acn.client.model.TelemetryItemModel;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.acn.client.model.TelemetryStatModel;
import moonstone.acs.AcsLogicalException;
import moonstone.acs.JsonUtils;
import moonstone.acs.KeyValuePair;
import moonstone.acs.client.model.ListResultModel;
import moonstone.acs.client.model.PagingResultModel;
import moonstone.acs.client.model.StatusModel;

@RestController
@RequestMapping("/api/v1/kronos/telemetries")
public class TelemetryApi extends BaseApiAbstract {

	@Autowired
	private TelemetrySender processor;

	@Autowired
	private TelemetryItemService telemetryItemService;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private LastTelemetryItemService lastTelemetryItemService;

	@Autowired
	private ApiUtil apiUtil;

	@Deprecated
	@RequestMapping(path = "", method = RequestMethod.POST)
	public StatusModel create(
			@ApiParam(value = "telemetry", required = true) @RequestBody(required = false) Map<String, String> body,
			HttpServletRequest request) {
		String method = "create";
		TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {
		};
		Map<String, String> model = JsonUtils.fromJson(getApiPayload(), typeRef);
		Assert.notNull(model, "no payload received");

		// validate access key
		AccessKey accessKey = validateCanUpdateDevice(findDeviceHid(model));
		auditLog(method, accessKey.getApplicationId(), null, accessKey.getId(), request);

		processor.process(model);
		return StatusModel.OK;
	}

	@RequestMapping(path = "/devices/{deviceHid}", method = RequestMethod.POST)
	public StatusModel create(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "deviceHid", required = true) String deviceHid,
			@ApiParam(value = "telemetry", required = true) @RequestBody(required = false) Map<String, String> body,
			HttpServletRequest request) {
		String method = "create";
		TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {
		};
		Map<String, String> model = JsonUtils.fromJson(getApiPayload(), typeRef);
		Assert.notNull(model, "no payload received");

		// validate access key
		AccessKey accessKey = validateCanUpdateDevice(deviceHid);
		auditLog(method, accessKey.getApplicationId(), null, accessKey.getId(), request);

		model.put(TelemetryItemType.System.buildName(KronosConstants.Telemetry.DEVICE_HID), deviceHid);
		processor.process(model);
		return StatusModel.OK;
	}

	@Deprecated
	@RequestMapping(path = "/batch", method = RequestMethod.POST)
	public StatusModel batchCreate(
			@ApiParam(value = "telemetry", required = true) @RequestBody(required = false) List<Map<String, String>> body,
			HttpServletRequest request) {
		String method = "batchCreate";
		TypeReference<List<Map<String, String>>> typeRef = new TypeReference<List<Map<String, String>>>() {
		};
		List<Map<String, String>> models = JsonUtils.fromJson(getApiPayload(), typeRef);
		Assert.notNull(models, "no payload received");

		// validate access key
		models.forEach(model -> {
			AccessKey accessKey = validateCanUpdateDevice(findDeviceHid(model));
			auditLog(method, accessKey.getApplicationId(), null, accessKey.getId(), request);
		});

		models.forEach(model -> {
			processor.process(model);
		});
		return StatusModel.OK;
	}

	@RequestMapping(path = "/devices/{deviceHid}/batch", method = RequestMethod.POST)
	public StatusModel batchCreate(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "deviceHid", required = true) String deviceHid,
			@ApiParam(value = "telemetry", required = true) @RequestBody(required = false) List<Map<String, String>> body,
			HttpServletRequest request) {
		String method = "batchCreate";
		TypeReference<List<Map<String, String>>> typeRef = new TypeReference<List<Map<String, String>>>() {
		};
		List<Map<String, String>> models = JsonUtils.fromJson(getApiPayload(), typeRef);
		Assert.notNull(models, "no payload received");

		// validate access key
		AccessKey accessKey = validateCanUpdateDevice(deviceHid);
		auditLog(method, accessKey.getApplicationId(), null, accessKey.getId(), request);

		models.forEach(model -> {
			model.put(TelemetryItemType.System.buildName(KronosConstants.Telemetry.DEVICE_HID), deviceHid);
			processor.process(model);
		});
		return StatusModel.OK;
	}

	@RequestMapping(path = "/devices/{deviceHid}/bulkDeleteLastTelemetry", method = RequestMethod.PUT)
	public StatusModel bulkDelete(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "deviceHid", required = true) String deviceHid,
			@ApiParam(value = "remove definition if exist") @RequestParam(value = "removeDefinitions", required = false) Boolean removeDefinitions,
			@ApiParam(value = "telemetryIds", required = true) @RequestBody(required = false) List<String> body,
			HttpServletRequest request) {
		String method = "bulkDelete";
		TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {
		};

		List<String> models = JsonUtils.fromJson(getApiPayload(), typeRef);

		// validate access key
		AccessKey accessKey = validateCanUpdateDevice(deviceHid);

		Device device = getKronosCache().findDeviceByHid(deviceHid);
		DeviceType deviceType = getKronosCache().findDeviceTypeById(device.getDeviceTypeId());
		Assert.notNull(deviceType, "deviceType is null");
		List<DeviceTelemetry> deviceTelemetrylist = new ArrayList<>(deviceType.getTelemetries());

		auditLog(method, accessKey.getApplicationId(), device.getId(), accessKey.getId(), request);

		List<LastTelemetryItem> lastTelemetryItems = (List<LastTelemetryItem>) lastTelemetryItemService
				.getLastTelemetryItemRepository().findAllById(models);

		Assert.isTrue(lastTelemetryItems != null && !lastTelemetryItems.isEmpty(),
				"Last telemetry items by ids not found");

		for (LastTelemetryItem lastTelemetryItem : lastTelemetryItems) {

			Assert.isTrue(device.getId().equals(lastTelemetryItem.getDeviceId()),
					"Device has not permission for lastTelemetryId = " + lastTelemetryItem.getId());

			lastTelemetryItemService.getLastTelemetryItemRepository().delete(lastTelemetryItem);

			if (removeDefinitions != null && removeDefinitions) {
				deviceTelemetrylist.stream()
						.filter(dTelemetry -> dTelemetry.getName().equals(lastTelemetryItem.getName()))
						.forEach(dTelemetry -> deviceType.getTelemetries().remove(dTelemetry));
			}
		}

		if (removeDefinitions != null && removeDefinitions) {
			getDeviceTypeService().update(deviceType, accessKey.getId());
		}

		return StatusModel.OK;
	}

	@RequestMapping(path = "/devices/{deviceHid}", method = RequestMethod.GET)
	public PagingResultModel<TelemetryItemModel> findByDeviceHid(
			// @formatter:off
			@PathVariable String deviceHid,
			@ApiParam(value = "fromTimestamp", required = true) @RequestParam(name = "fromTimestamp", required = true) String fromTimestamp,
			@ApiParam(value = "toTimestamp", required = true) @RequestParam(name = "toTimestamp", required = true) String toTimestamp,
			@ApiParam(value = "telemetry names") @RequestParam(name = "telemetryNames", required = false) String telemetryNames,
			@ApiParam(value = "page index") @RequestParam(name = "_page", required = false) Integer page,
			@ApiParam(value = "items per page") @RequestParam(name = "_size", required = false) Integer size) {
		// @formatter:on

		String method = "findByDeviceHid";
		logInfo(method, "deviceHid: %s, from: %s, to: %s", deviceHid, fromTimestamp, toTimestamp);

		validateCanReadDevice(deviceHid);

		return doFindBy(Collections.singletonList(getKronosCache().findDeviceByHid(deviceHid)), fromTimestamp,
				toTimestamp, telemetryNames, page, size);
	}

	@RequestMapping(path = "/nodes/{nodeHid}", method = RequestMethod.GET)
	public PagingResultModel<TelemetryItemModel> findByNodeHid(
			// @formatter:off
			@PathVariable String nodeHid,
			@ApiParam(value = "fromTimestamp", required = true) @RequestParam(name = "fromTimestamp", required = true) String fromTimestamp,
			@ApiParam(value = "toTimestamp", required = true) @RequestParam(name = "toTimestamp", required = true) String toTimestamp,
			@ApiParam(value = "telemetry names") @RequestParam(name = "telemetryNames", required = false) String telemetryNames,
			@ApiParam(value = "page index") @RequestParam(name = "_page", required = false) Integer page,
			@ApiParam(value = "items per page") @RequestParam(name = "_size", required = false) Integer size) {
		// @formatter:on

		String method = "findByNodeHid";
		logInfo(method, "nodeHid: %s, from: %s, to: %s", nodeHid, fromTimestamp, toTimestamp);

		validateCanReadNode(nodeHid);

		List<Device> devices = new ArrayList<>();
		Node node = getKronosCache().findNodeByHid(nodeHid);
		for (Device device : deviceService.getDeviceRepository().findAllByNodeIdAndEnabled(node.getId(), true)) {
			devices.add(device);
		}
		return doFindBy(devices, fromTimestamp, toTimestamp, telemetryNames, page, size);
	}

	@RequestMapping(path = "/applications/{applicationHid}", method = RequestMethod.GET)
	public PagingResultModel<TelemetryItemModel> findByApplicationHid(
			// @formatter:off
			@PathVariable String applicationHid,
			@ApiParam(value = "fromTimestamp", required = true) @RequestParam(name = "fromTimestamp", required = true) String fromTimestamp,
			@ApiParam(value = "toTimestamp", required = true) @RequestParam(name = "toTimestamp", required = true) String toTimestamp,
			@ApiParam(value = "telemetry names") @RequestParam(name = "telemetryNames", required = false) String telemetryNames,
			@ApiParam(value = "page index") @RequestParam(name = "_page", required = false) Integer page,
			@ApiParam(value = "items per page") @RequestParam(name = "_size", required = false) Integer size) {
		// @formatter:on

		String method = "findByApplicationHid";
		logInfo(method, "applicationHid: %s, from: %s, to: %s", applicationHid, fromTimestamp, toTimestamp);

		AccessKey accessKey = validateCanReadApplication(getProductSystemName());

		Application application = getCoreCacheService().findApplicationByHid(applicationHid);
		checkEnabled(application, "application");

		if (!StringUtils.equals(application.getId(), accessKey.getApplicationId())) {
			throw new AcsLogicalException("applicationId mismatched!");
		}

		List<Device> devices = new ArrayList<>();
		for (Device device : deviceService.getDeviceRepository().findAllByApplicationIdAndEnabled(application.getId(),
				true)) {
			devices.add(device);
		}
		logInfo(method, "devices size: %d", devices.size());
		return doFindBy(devices, fromTimestamp, toTimestamp, telemetryNames, page, size);
	}

	@RequestMapping(path = "/devices/{deviceHid}/latest", method = RequestMethod.GET)
	public ListResultModel<TelemetryItemModel> getLastTelemetry(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "deviceHid") String deviceHid) {

		validateCanReadDevice(deviceHid);
		Device device = getKronosCache().findDeviceByHid(deviceHid);

		List<LastTelemetryItem> lastTelemetries = lastTelemetryItemService.getLastTelemetryItemRepository()
				.findByDeviceId(device.getId());
		List<TelemetryItemModel> data = new ArrayList<>();
		lastTelemetries.forEach(telemetry -> {
			data.add(buildLastTelemetryItemModel(telemetry, device));
		});
		ListResultModel<TelemetryItemModel> result = new ListResultModel<>();
		return result.withSize(data.size()).withData(data);
	}

	@ApiOperation(value = "telemetry item count")
	@RequestMapping(path = "/devices/{deviceHid}/count", method = RequestMethod.GET)
	public TelemetryStatModel findTelemetryItemCount(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "deviceHid") String deviceHid,
			@ApiParam(value = "telemetryName", required = true) @RequestParam(name = "telemetryName", required = true) String telemetryName,
			@ApiParam(value = "fromTimestamp", required = true) @RequestParam(name = "fromTimestamp", required = true) String fromTimestamp,
			@ApiParam(value = "toTimestamp", required = true) @RequestParam(name = "toTimestamp", required = true) String toTimestamp) {

		Assert.hasText(telemetryName, "telemetryName is empty");
		long from = apiUtil.parseDateParam(fromTimestamp).toEpochMilli();
		long to = apiUtil.parseDateParam(toTimestamp).toEpochMilli();
		Assert.isTrue(from <= to, "fromTimestamp must be less than toTimestamp");

		validateCanReadDevice(deviceHid);
		Device device = getKronosCache().findDeviceByHid(deviceHid);
		long count = 0;
		if ("*".equals(telemetryName)) {
			count = telemetryItemService.getTelemetryItemRepository().findTelemetryItemCount(device.getId(), from, to);
		} else {
			count = telemetryItemService.getTelemetryItemRepository().findTelemetryItemCount(device.getId(),
					telemetryName, from, to);
		}
		TelemetryStatModel result = new TelemetryStatModel();
		result.setDeviceHid(device.getHid());
		result.setName(telemetryName);
		result.setValue(String.valueOf(count));
		return result;
	}

	@ApiOperation(value = "count items for multiple telemetries")
	@RequestMapping(path = "/devices/{deviceHid}/batch/count", method = RequestMethod.GET)
	public ListResultModel<TelemetryStatModel> findTelemetryItemCount(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "deviceHid") String deviceHid,
			@ApiParam(value = "telemetryNames", required = true) @RequestParam(name = "telemetryNames", required = true) String[] telemetryNames,
			@ApiParam(value = "fromTimestamp", required = true) @RequestParam(name = "fromTimestamp", required = true) String fromTimestamp,
			@ApiParam(value = "toTimestamp", required = true) @RequestParam(name = "toTimestamp", required = true) String toTimestamp) {

		Assert.notEmpty(telemetryNames, "telemetryNames is empty");
		long from = apiUtil.parseDateParam(fromTimestamp).toEpochMilli();
		long to = apiUtil.parseDateParam(toTimestamp).toEpochMilli();
		Assert.isTrue(from <= to, "fromTimestamp must be less than toTimestamp");

		validateCanReadDevice(deviceHid);
		Device device = getKronosCache().findDeviceByHid(deviceHid);
		List<TelemetryStatModel> data = new ArrayList<>();
		for (String telemetryName : new HashSet<String>(Arrays.asList(telemetryNames))) {
			long count = 0;
			if ("*".equals(telemetryName)) {
				count = telemetryItemService.getTelemetryItemRepository().findTelemetryItemCount(device.getId(), from,
						to);
			} else {
				count = telemetryItemService.getTelemetryItemRepository().findTelemetryItemCount(device.getId(),
						telemetryName, from, to);
			}
			TelemetryStatModel stat = new TelemetryStatModel();
			stat.setDeviceHid(deviceHid);
			stat.setName(telemetryName);
			stat.setValue(String.valueOf(count));
			data.add(stat);
		}
		return new ListResultModel<TelemetryStatModel>().withData(data).withSize(data.size());
	}

	@RequestMapping(path = "/devices/{deviceHid}/min", method = RequestMethod.GET)
	public TelemetryStatModel findMinTelemetryItem(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "deviceHid") String deviceHid,
			@ApiParam(value = "telemetryName", required = true) @RequestParam(name = "telemetryName", required = true) String telemetryName,
			@ApiParam(value = "telemetryItemType", required = true) @RequestParam(name = "telemetryItemType", required = true) String telemetryItemType,
			@ApiParam(value = "fromTimestamp", required = true) @RequestParam(name = "fromTimestamp", required = true) String fromTimestamp,
			@ApiParam(value = "toTimestamp", required = true) @RequestParam(name = "toTimestamp", required = true) String toTimestamp) {

		Assert.hasText(telemetryName, "telemetryName is empty");
		long from = apiUtil.parseDateParam(fromTimestamp).toEpochMilli();
		long to = apiUtil.parseDateParam(toTimestamp).toEpochMilli();
		Assert.isTrue(from <= to, "fromTimestamp must be less than toTimestamp");
		TelemetryItemType type = validateTelemetryItemType(telemetryItemType);

		validateCanReadDevice(deviceHid);
		Device device = getKronosCache().findDeviceByHid(deviceHid);
		TelemetryStat stat = telemetryItemService.getTelemetryItemRepository().findMinTelemetryItem(device.getId(),
				telemetryName, type, from, to);
		Assert.notNull(stat, "minTelemetryItem not found");
		return buildTelemetryStatModel(stat, device);
	}

	@RequestMapping(path = "/devices/{deviceHid}/max", method = RequestMethod.GET)
	public TelemetryStatModel findMaxTelemetryItem(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "deviceHid") String deviceHid,
			@ApiParam(value = "telemetryName", required = true) @RequestParam(name = "telemetryName", required = true) String telemetryName,
			@ApiParam(value = "telemetryItemType", required = true) @RequestParam(name = "telemetryItemType", required = true) String telemetryItemType,
			@ApiParam(value = "fromTimestamp", required = true) @RequestParam(name = "fromTimestamp", required = true) String fromTimestamp,
			@ApiParam(value = "toTimestamp", required = true) @RequestParam(name = "toTimestamp", required = true) String toTimestamp) {

		Assert.hasText(telemetryName, "telemetryName is empty");
		long from = apiUtil.parseDateParam(fromTimestamp).toEpochMilli();
		long to = apiUtil.parseDateParam(toTimestamp).toEpochMilli();
		Assert.isTrue(from <= to, "fromTimestamp must be less than toTimestamp");
		TelemetryItemType type = validateTelemetryItemType(telemetryItemType);

		validateCanReadDevice(deviceHid);
		Device device = getKronosCache().findDeviceByHid(deviceHid);
		TelemetryStat stat = telemetryItemService.getTelemetryItemRepository().findMaxTelemetryItem(device.getId(),
				telemetryName, type, from, to);
		Assert.notNull(stat, "maxTelemetryItem not found");
		return buildTelemetryStatModel(stat, device);
	}

	@RequestMapping(path = "/devices/{deviceHid}/avg", method = RequestMethod.GET)
	public TelemetryStatModel findAvgTelemetryItem(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "deviceHid") String deviceHid,
			@ApiParam(value = "telemetryName", required = true) @RequestParam(name = "telemetryName", required = true) String telemetryName,
			@ApiParam(value = "telemetryItemType", required = true) @RequestParam(name = "telemetryItemType", required = true) String telemetryItemType,
			@ApiParam(value = "fromTimestamp", required = true) @RequestParam(name = "fromTimestamp", required = true) String fromTimestamp,
			@ApiParam(value = "toTimestamp", required = true) @RequestParam(name = "toTimestamp", required = true) String toTimestamp) {

		Assert.hasText(telemetryName, "telemetryName is empty");
		long from = apiUtil.parseDateParam(fromTimestamp).toEpochMilli();
		long to = apiUtil.parseDateParam(toTimestamp).toEpochMilli();
		Assert.isTrue(from <= to, "fromTimestamp must be less than toTimestamp");
		TelemetryItemType type = validateTelemetryItemType(telemetryItemType);

		validateCanReadDevice(deviceHid);
		Device device = getKronosCache().findDeviceByHid(deviceHid);
		TelemetryStat stat = telemetryItemService.getTelemetryItemRepository().findAvgTelemetryItem(device.getId(),
				telemetryName, type, from, to);
		Assert.notNull(stat, "avgTelemetryItem not found");
		return buildTelemetryStatModel(stat, device);
	}

	private PagingResultModel<TelemetryItemModel> doFindBy(List<Device> devices, String fromTimestamp,
			String toTimestamp, String telemetryNames, Integer page, Integer size) {

		String method = "doFindBy";
		int maxSize = KronosConstants.PageResult.MAX_SIZE;
		if (size == null) {
			size = maxSize;
		}
		if (page == null) {
			page = 0;
		}
		Assert.isTrue(page >= 0, "page must be positive");
		Assert.isTrue(size > 0 && size <= maxSize, "size must be between 1 and " + maxSize);

		PagingResultModel<TelemetryItemModel> result = new PagingResultModel<>();
		result.setPage(page);

		PageRequest pageRequest = PageRequest.of(page, size);
		TelemetryItemSearchParams params = new TelemetryItemSearchParams();

		long from, to = 0;
		try {
			from = Instant.parse(fromTimestamp).toEpochMilli();
		} catch (Exception e) {
			throw new AcsLogicalException("invalid datetime format");
		}
		try {
			to = Instant.parse(toTimestamp).toEpochMilli();
		} catch (Exception e) {
			throw new AcsLogicalException("invalid datetime format");
		}
		Assert.isTrue(from <= to, "fromTimestamp must be less than toTimestamp");
		params.setFromTimestamp(from);
		params.setToTimestamp(to);
		result.setSize(pageRequest.getPageSize());

		List<TelemetryItemModel> data = new ArrayList<>();
		if (devices != null && devices.size() > 0) {
			Map<String, Device> deviceMap = new HashMap<>();
			for (Device device : devices) {
				deviceMap.put(device.getId(), device);
			}

			params.setDeviceIds(deviceMap.keySet().toArray(new String[deviceMap.size()]));

			Set<String> names = new HashSet<>();
			if (StringUtils.isNotEmpty(telemetryNames)) {
				for (String token : telemetryNames.split(",", -1)) {
					names.add(token.trim());
				}
			}
			if (names.size() > 0) {
				params.setNames(names.toArray(new String[names.size()]));
			}

			logInfo(method, "doFindTelemetryItems() ...");
			Page<TelemetryItem> list = telemetryItemService.getTelemetryItemRepository()
					.doFindTelemetryItems(pageRequest, params);
			list.forEach(item -> {
				data.add(TelemetryItemConverter.toModel(item, deviceMap.get(item.getDeviceId())));
			});
			result.setTotalPages(list.getTotalPages());
			result.setTotalSize(list.getTotalElements());
		}

		result.setData(data);
		return result;
	}

	List<TelemetryItemModel> doFindBy(List<Device> devices, String fromTimestamp, String toTimestamp,
			String telemetryNames) {

		String method = "doFindBy";

		long from, to = 0;
		try {
			from = Instant.parse(fromTimestamp).toEpochMilli();
		} catch (Exception e) {
			throw new AcsLogicalException("invalid datetime format");
		}
		try {
			to = Instant.parse(toTimestamp).toEpochMilli();
		} catch (Exception e) {
			throw new AcsLogicalException("invalid datetime format");
		}
		Assert.isTrue(from <= to, "fromTimestamp must be less than toTimestamp");

		List<TelemetryItemModel> result = new ArrayList<>();
		if (devices != null && devices.size() > 0) {
			Set<String> nameSet = new HashSet<>();
			if (StringUtils.isNotEmpty(telemetryNames)) {
				for (String token : telemetryNames.split(",", -1)) {
					nameSet.add(token.trim());
				}
			}
			String[] names = nameSet.toArray(new String[nameSet.size()]);

			Map<String, Device> deviceMap = new HashMap<>();
			for (Device device : devices) {
				deviceMap.put(device.getId(), device);
			}
			List<String> deviceIds = new ArrayList<>(deviceMap.keySet());
			List<List<String>> partitions = ListUtils.partition(deviceIds, 10);
			for (List<String> partition : partitions) {
				logInfo(method, "doFindTelemetryItems() size: %d", partition.size());
				String[] ids = partition.toArray(new String[partition.size()]);
				List<TelemetryItem> list = telemetryItemService.getTelemetryItemRepository().findTelemetryItems(ids,
						names, from, to);
				list.forEach(item -> {
					result.add(TelemetryItemConverter.toModel(item, deviceMap.get(item.getDeviceId())));
				});
			}
		}

		return result;
	}

	private TelemetryItemModel buildLastTelemetryItemModel(LastTelemetryItem item, Device device) {
		TelemetryItemModel model = new TelemetryItemModel();
		model.setBinaryValue(item.getBinaryValue());
		model.setBoolValue(item.getBoolValue());
		model.setDatetimeValue(item.getDatetimeValue() == null ? null : item.getDatetimeValue().toString());
		model.setDateValue(item.getDateValue() == null ? null : item.getDateValue().toString());
		model.setDeviceHid(device.getHid());
		model.setFloatCubeValue(item.getFloatCubeValue());
		model.setFloatSqrValue(item.getFloatSqrValue());
		model.setFloatValue(item.getFloatValue());
		model.setIntCubeValue(item.getIntCubeValue());
		model.setIntSqrValue(item.getIntSqrValue());
		model.setIntValue(item.getIntValue());
		model.setName(item.getName());
		model.setStrValue(item.getStrValue());
		model.setTimestamp(item.getTimestamp());
		model.setType(item.getType());
		return model;
	}

	private TelemetryStatModel buildTelemetryStatModel(TelemetryStat stat, Device device) {
		if (stat == null) {
			return null;
		}
		TelemetryStatModel model = new TelemetryStatModel();
		model.setDeviceHid(device.getHid());
		model.setName(stat.getName());
		model.setValue(stat.getValue());
		return model;
	}

	private TelemetryItemType validateTelemetryItemType(String telemetryItemType) {
		try {
			return TelemetryItemType.valueOf(telemetryItemType);
		} catch (Exception e) {
			throw new AcsLogicalException("invalid telemetryItemType");
		}
	}

	private String findDeviceHid(Map<String, String> payload) {
		String method = "findDeviceHid";
		for (String name : payload.keySet()) {
			String value = payload.get(name);
			if (StringUtils.isNotEmpty(value)) {
				KeyValuePair<TelemetryItemType, String> pair = TelemetryItemType.parse(name);
				if (pair != null) {
					String sysKey = pair.getValue();
					if (pair.getKey().isSystem()) {
						if (StringUtils.equals(sysKey, KronosConstants.Telemetry.DEVICE_HID)) {
							logInfo(method, "found deviceHid: %s", value);
							return value;
						}
					}
				}
			}
		}
		return null;
	}
}

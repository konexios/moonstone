package com.arrow.kronos.api;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acn.AcnEventNames;
import com.arrow.acn.client.model.DeviceStateModel;
import com.arrow.acn.client.model.DeviceStateRequestModel;
import com.arrow.acn.client.model.DeviceStateUpdateModel;
import com.arrow.acn.client.model.DeviceStateValueModel;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.StatusModel;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceState;
import com.arrow.kronos.data.DeviceStateTrans;
import com.arrow.kronos.data.DeviceStateTrans.Status;
import com.arrow.kronos.data.DeviceStateTrans.Type;
import com.arrow.kronos.data.DeviceStateValue;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.service.DeviceStateService;
import com.arrow.kronos.service.DeviceStateTransService;
import com.arrow.kronos.service.GatewayCommandService;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.event.Event;
import com.arrow.pegasus.data.event.EventBuilder;
import com.arrow.pegasus.data.event.EventParameter;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/kronos/devices")
public class DeviceStateApi extends DeviceApiAbstract {

	@Autowired
	private DeviceStateService deviceStateService;

	@Autowired
	private DeviceStateTransService deviceStateTransService;

	@Autowired
	private GatewayCommandService gatewayCommandService;

	@ApiOperation(value = "find device state", response = DeviceStateModel.class)
	@RequestMapping(path = "/{hid}/state", method = RequestMethod.GET)
	public DeviceStateModel findByDeviceHid(@ApiParam(value = "device hid", required = true) @PathVariable String hid) {
		validateCanReadDevice(hid);

		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device is null");

		DeviceState deviceState = deviceStateService.getDeviceStateRepository()
		        .findByApplicationIdAndDeviceId(device.getApplicationId(), device.getId());
		Assert.notNull(deviceState, "deviceState is null");

		return buildDeviceStateModel(device, deviceState);
	}

	@ApiOperation(value = "create new device state request transaction", response = HidModel.class)
	@RequestMapping(path = "/{hid}/state/request", method = RequestMethod.POST)
	public HidModel createStateRequestTrans(@PathVariable String hid,
	        @ApiParam(value = "state request model", required = true) @RequestBody(required = false) DeviceStateRequestModel body,
	        HttpServletRequest request) {
		String method = "createStateRequestTrans";

		validateCanUpdateDevice(hid);

		DeviceStateRequestModel model = JsonUtils.fromJson(getApiPayload(), DeviceStateRequestModel.class);
		Assert.notNull(model, "state request model is null");

		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device is null");

		Gateway gateway = getKronosCache().findGatewayById(device.getGatewayId());
		Assert.notNull(gateway, "gateway is null");

		AccessKey accessKey = validateCanWriteGateway(gateway.getHid());
		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		DeviceStateTrans deviceStateTrans = getDeviceStateTrans(device, model);
		deviceStateTrans.setType(Type.REQUEST);

		// persist
		deviceStateTrans = deviceStateTransService.create(deviceStateTrans, accessKey.getId());

		// after create and persist DeviceStateTrans object, send request to
		// gateway similar to GatewayApi.sendDeviceCommand using this type -
		// AcnEventNames.ServerToGateway.DEVICE_STATE_REQUEST
		doSendDeviceStateRequestCommand(accessKey, gateway, device, deviceStateTrans);

		return new HidModel().withHid(deviceStateTrans.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "create new device state update transaction", response = HidModel.class)
	@RequestMapping(path = "/{hid}/state/update", method = RequestMethod.POST)
	public HidModel createStateUpdateTrans(@PathVariable String hid,
	        @ApiParam(value = "state update model", required = true) @RequestBody(required = false) DeviceStateUpdateModel body,
	        HttpServletRequest request) {
		String method = "createStateUpdateTrans";

		AccessKey accessKey = validateCanUpdateDevice(hid);

		DeviceStateUpdateModel model = JsonUtils.fromJson(getApiPayload(), DeviceStateUpdateModel.class);
		Assert.notNull(model, "state update model is null");

		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device is null");

		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		DeviceStateTrans deviceStateTrans = getDeviceStateTrans(device, model);
		deviceStateTrans.setType(Type.UPDATE);

		// persist
		deviceStateTrans = deviceStateTransService.create(deviceStateTrans, accessKey.getId());

		return new HidModel().withHid(deviceStateTrans.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "mark device state transaction as succeeded", response = StatusModel.class)
	@RequestMapping(path = "/{hid}/state/trans/{transHid}/succeeded", method = RequestMethod.PUT)
	public StatusModel transSucceeded(@PathVariable String hid, @PathVariable String transHid,
	        HttpServletRequest request) {
		String method = "transSucceeded";

		AccessKey accessKey = validateCanUpdateDevice(hid);

		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device is null");

		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		DeviceStateTrans deviceStateTrans = deviceStateTransService.getDeviceStateTransRepository()
		        .doFindByHid(transHid);
		Assert.notNull(deviceStateTrans, "deviceStateTrans is null");
		Assert.isTrue(deviceStateTrans.getDeviceId().equals(device.getId()), "device does not match");

		if (deviceStateTrans.getType() == Type.UPDATE) {
			return StatusModel.error("device state transaction of type UPDATE can not be modified");
		} else {
			deviceStateTrans.setStatus(Status.COMPLETE);

			// persist
			deviceStateTrans = deviceStateTransService.update(deviceStateTrans, accessKey.getId());

			return StatusModel.OK;
		}
	}

	@ApiOperation(value = "mark device state transaction as received", response = StatusModel.class)
	@RequestMapping(path = "/{hid}/state/trans/{transHid}/received", method = RequestMethod.PUT)
	public StatusModel transReceived(@PathVariable String hid, @PathVariable String transHid,
	        HttpServletRequest request) {
		String method = "transReceived";

		AccessKey accessKey = validateCanUpdateDevice(hid);

		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device is null");

		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		DeviceStateTrans deviceStateTrans = deviceStateTransService.getDeviceStateTransRepository()
		        .doFindByHid(transHid);
		Assert.notNull(deviceStateTrans, "deviceStateTrans is null");
		Assert.isTrue(deviceStateTrans.getDeviceId().equals(device.getId()), "device does not match");

		if (deviceStateTrans.getType() == Type.UPDATE) {
			return StatusModel.error("device state transaction of type UPDATE can not be modified");
		} else {
			deviceStateTrans.setStatus(Status.RECEIVED);

			// persist
			deviceStateTrans = deviceStateTransService.update(deviceStateTrans, accessKey.getId());

			return StatusModel.OK;
		}
	}

	@ApiOperation(value = "mark device state transaction as failed", response = StatusModel.class)
	@RequestMapping(path = "/{hid}/state/trans/{transHid}/failed", method = RequestMethod.PUT)
	public StatusModel transFailed(@PathVariable String hid, @PathVariable String transHid,
	        @RequestBody(required = false) Map<String, String> body, HttpServletRequest request) {
		String method = "transFailed";

		AccessKey accessKey = validateCanUpdateDevice(hid);

		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device is null");

		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		DeviceStateTrans deviceStateTrans = deviceStateTransService.getDeviceStateTransRepository()
		        .doFindByHid(transHid);
		Assert.notNull(deviceStateTrans, "deviceStateTrans is null");
		Assert.isTrue(deviceStateTrans.getDeviceId().equals(device.getId()), "device does not match");

		if (deviceStateTrans.getType() == Type.UPDATE) {
			return StatusModel.error("device state transaction of type UPDATE can not be modified");
		} else {
			Map<String, String> parameters = JsonUtils.fromJson(getApiPayload(),
			        new TypeReference<Map<String, String>>() {
			        });
			deviceStateTrans.setError(parameters.get("error"));
			deviceStateTrans.setStatus(Status.ERROR);

			// persist
			deviceStateTrans = deviceStateTransService.update(deviceStateTrans, accessKey.getId());

			return StatusModel.OK;
		}
	}

	private DeviceStateModel buildDeviceStateModel(Device device, DeviceState deviceState) {
		Map<String, DeviceStateValueModel> states = new HashMap<>();

		for (Entry<String, DeviceStateValue> entry : deviceState.getStates().entrySet()) {
			states.put(entry.getKey(), new DeviceStateValueModel().withValue(entry.getValue().getValue())
			        .withTimestamp(entry.getValue().getTimestamp()));
		}

		return buildModel(new DeviceStateModel(), deviceState).withDeviceHid(device.getHid()).withStates(states);
	}

	private Event doSendDeviceStateRequestCommand(AccessKey accessKey, Gateway gateway, Device device, DeviceStateTrans deviceStateTrans) {
		String method = "doSendDeviceStateRequestCommand";
		Assert.notNull(accessKey, "accessKey is null");
		Assert.notNull(gateway, "gateway is null");
		Assert.notNull(device, "device is null");
		Assert.notNull(deviceStateTrans, "device is null");
		Assert.isTrue(gateway.getId().equals(device.getGatewayId()), "gateway does not match");

		String payload = JsonUtils.toJson(deviceStateTrans.getStates());

		EventBuilder eventBuilder = EventBuilder.create().applicationId(device.getApplicationId())
		        .name(AcnEventNames.ServerToGateway.DEVICE_STATE_REQUEST)
		        .parameter(EventParameter.InString("deviceHid", device.getHid()))
		        .parameter(EventParameter.InString("transHid", deviceStateTrans.getHid()))
		        .parameter(EventParameter.InString("payload", payload));
		Event event = gatewayCommandService.sendEvent(eventBuilder.build(), gateway.getId(), accessKey);
		logInfo(method, "event has been sent to device=%s: eventHid=%s", device.getHid(), event.getHid());
		return event;
	}

	private DeviceStateTrans getDeviceStateTrans(Device device, DeviceStateRequestModel model) {
		DeviceStateTrans deviceStateTrans = new DeviceStateTrans();
		deviceStateTrans.setApplicationId(device.getApplicationId());
		deviceStateTrans.setDeviceId(device.getId());

		Map<String, DeviceStateValue> states = new HashMap<>();
		Instant timestamp = StringUtils.isEmpty(model.getTimestamp()) ? Instant.now()
		        : Instant.parse(model.getTimestamp());
		for (Entry<String, String> entry : model.getStates().entrySet()) {
			DeviceStateValue deviceStateValue = new DeviceStateValue();
			deviceStateValue.setValue(entry.getValue());
			deviceStateValue.setTimestamp(timestamp);
			states.put(entry.getKey(), deviceStateValue);
		}
		deviceStateTrans.setStates(states);

		deviceStateTrans.setStatus(Status.PENDING);

		return deviceStateTrans;
	}

	@ApiOperation(value = "delete device states", response = StatusModel.class)
	@RequestMapping(path = "/{deviceHid}/state/delete", method = RequestMethod.PUT)
	public StatusModel deleteDeviceStates(
	        @ApiParam(value = "device hid", required = true) @PathVariable(value = "deviceHid", required = true) String deviceHid,
	        @ApiParam(value = "remove states definition if exist") @RequestParam(value = "removeStatesDefinition", required = false) Boolean removeStatesDefinition,
	        @ApiParam(value = "states", required = true) @RequestBody(required = false) List<String> body,
	        HttpServletRequest request) {
		String method = "deleteDeviceStates";

		TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {
		};

		List<String> states = JsonUtils.fromJson(getApiPayload(), typeRef);
		Assert.notNull(states, "states is null");

		// validate access key
		AccessKey accessKey = validateCanUpdateDevice(deviceHid);

		Device device = getKronosCache().findDeviceByHid(deviceHid);
		Assert.notNull(device, "device is null");

		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		DeviceType deviceType = getKronosCache().findDeviceTypeById(device.getDeviceTypeId());
		Assert.notNull(deviceType, "deviceType is null");
		DeviceState deviceState = deviceStateService.getDeviceStateRepository()
		        .findByApplicationIdAndDeviceId(device.getApplicationId(), device.getId());
		Assert.notNull(deviceState, "deviceState is null");

		for (String state : states) {
			deviceState.getStates().remove(state);
			if (removeStatesDefinition) {
				deviceType.getStateMetadata().remove(state);
			}
		}

		deviceState = deviceStateService.update(deviceState, accessKey.getId());
		if (removeStatesDefinition) {
			deviceType = getDeviceTypeService().update(deviceType, accessKey.getId());
		}

		return StatusModel.OK;
	}
}

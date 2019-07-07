package com.arrow.kronos.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acn.client.model.DeviceActionModel;
import com.arrow.acn.client.model.DeviceActionTypeModel;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.ListResultModel;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.service.DeviceActionTypeService;
import com.arrow.kronos.service.NodeService;
import com.arrow.kronos.util.ApiUtil;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Application;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/kronos")
public class DeviceActionApi extends DeviceApiAbstract {

	@Autowired
	private DeviceActionTypeService deviceActionTypeService;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private ApiUtil apiUtil;

	@ApiOperation(value = "list available action types")
	@RequestMapping(path = "/devices/actions/types", method = RequestMethod.GET)
	public ListResultModel<DeviceActionTypeModel> getAvailableActionTypes() {

		AccessKey accessKey = getValidatedAccessKey(ProductSystemNames.KRONOS);

		List<DeviceActionType> deviceActionTypes = deviceActionTypeService.getDeviceActionTypeRepository()
		        .findByApplicationIdAndEnabled(accessKey.getApplicationId(), true);
		Application application = getCoreCacheService().findApplicationById(accessKey.getApplicationId());
		List<DeviceActionTypeModel> data = new ArrayList<>();
		deviceActionTypes
		        .forEach(deviceActionType -> data.add(buildDeviceActionTypeModel(application, deviceActionType)));
		return new ListResultModel<DeviceActionTypeModel>().withData(data).withSize(data.size());
	}

	@ApiOperation(value = "create new device action", response = HidModel.class)
	@RequestMapping(path = "/devices/{hid}/actions", method = RequestMethod.POST)
	public HidModel create(@ApiParam(value = "device hid", required = true) @PathVariable(value = "hid") String hid,
	        @ApiParam(value = "device action model", required = true) @RequestBody(required = false) DeviceActionModel body, 
	        HttpServletRequest request) {
		String method = "create";

		AccessKey accessKey = validateCanUpdateDevice(hid);
		Device device = getKronosCache().findDeviceByHid(hid);

		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		DeviceAction deviceAction = buildDeviceAction(device.getApplicationId());
		device = getDeviceService().createDeviceAction(device, deviceAction, accessKey.getId());
		return new HidModel().withHid(device.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "update existing device action", response = HidModel.class)
	@RequestMapping(path = "/devices/{hid}/actions/{index}", method = RequestMethod.PUT)
	public HidModel update(@ApiParam(value = "device hid", required = true) @PathVariable(value = "hid") String hid,
	        @ApiParam(value = "device action index", required = true) @PathVariable(value = "index") int index,
	        @ApiParam(value = "device action model", required = true) @RequestBody(required = false) DeviceActionModel body,
	        HttpServletRequest request) {
		String method = "update";

		AccessKey accessKey = validateCanUpdateDevice(hid);

		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device is not found");
		Assert.notEmpty(device.getActions(), "device actions list is empty");
		Assert.isTrue(index >= 0 && index < device.getActions().size(), "invalid index");

		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		DeviceAction deviceAction = device.getActions().get(index);
		Assert.notNull(deviceAction, "device action is null");
		deviceAction = updateDeviceAction(deviceAction);

		device = getDeviceService().updateDeviceAction(device, index, deviceAction, accessKey.getId());
		return new HidModel().withHid(device.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "list existing device actions for a device")
	@RequestMapping(path = "/devices/{hid}/actions", method = RequestMethod.GET)
	public ListResultModel<DeviceActionModel> getDeviceActions(
	        @ApiParam(value = "device hid", required = true) @PathVariable(value = "hid") String hid) {

		validateCanReadDevice(hid);
		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device is null");

		List<DeviceActionModel> data = apiUtil.buildDeviceActionModelList(device.getActions());
		return new ListResultModel<DeviceActionModel>().withData(data).withSize(data.size());
	}

	@ApiOperation(value = "delete device action", response = HidModel.class)
	@RequestMapping(path = "/devices/{hid}/actions/{index}", method = RequestMethod.DELETE)
	public HidModel delete(@ApiParam(value = "device hid", required = true) @PathVariable(value = "hid") String hid,
	        @ApiParam(value = "device action index", required = true) @PathVariable(value = "index") int index,
	        HttpServletRequest request) {
		String method = "delete";

		AccessKey accessKey = validateCanUpdateDevice(hid);
		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device is null");

		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		List<Integer> actionIndices = new ArrayList<>();
		actionIndices.add(index);
		device = getDeviceService().deleteDeviceActions(device, actionIndices, accessKey.getId());
		return new HidModel().withHid(device.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "create a new device action for a specific device type", response = HidModel.class)
	@RequestMapping(path = "/devices/types/{hid}/actions", method = RequestMethod.POST)
	public HidModel createDeviceTypeAction(
	        @ApiParam(value = "device type hid", required = true) @PathVariable(value = "hid") String hid,
	        @ApiParam(value = "device action model", required = true) @RequestBody(required = false) DeviceActionModel body,
	        HttpServletRequest request) {
		String method = "createDeviceTypeAction";

		AccessKey accessKey = validateCanWriteApplication(ProductSystemNames.KRONOS);

		DeviceType deviceType = getKronosCache().findDeviceTypeByHid(hid);
		Assert.notNull(deviceType, "device type does not exist");

		auditLog(method, deviceType.getApplicationId(), deviceType.getId(), accessKey.getId(), request);

		Assert.isTrue(deviceType.getApplicationId().equals(accessKey.getApplicationId()), "applicationId mismatched!");

		DeviceAction deviceAction = buildDeviceAction(deviceType.getApplicationId());
		deviceType = getDeviceTypeService().createDeviceAction(deviceType.getId(), deviceAction, accessKey.getId());
		return new HidModel().withHid(deviceType.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "update an existing device action for a specific device type", response = HidModel.class)
	@RequestMapping(path = "/devices/types/{hid}/actions/{index}", method = RequestMethod.PUT)
	public HidModel updateDeviceTypeAction(
	        @ApiParam(value = "device type hid", required = true) @PathVariable(value = "hid") String hid,
	        @ApiParam(value = "device action index", required = true) @PathVariable(value = "index") int index,
	        @ApiParam(value = "device action model", required = true) @RequestBody(required = false) DeviceActionModel body,
	        HttpServletRequest request) {
		String method = "updateDeviceTypeAction";

		AccessKey accessKey = validateCanWriteApplication(ProductSystemNames.KRONOS);

		DeviceType deviceType = getKronosCache().findDeviceTypeByHid(hid);
		Assert.notNull(deviceType, "device type is not found");

		auditLog(method, deviceType.getApplicationId(), deviceType.getId(), accessKey.getId(), request);

		Assert.isTrue(deviceType.getApplicationId().equals(accessKey.getApplicationId()), "applicationId mismatched!");
		Assert.notEmpty(deviceType.getActions(), "device actions list is empty");
		Assert.isTrue(index >= 0 && index < deviceType.getActions().size(), "invalid index");

		DeviceAction deviceAction = deviceType.getActions().get(index);
		Assert.notNull(deviceAction, "device action is null");
		deviceAction = updateDeviceAction(deviceAction);

		deviceType = getDeviceTypeService().updateDeviceAction(deviceType.getId(), index, deviceAction,
		        accessKey.getId());
		return new HidModel().withHid(deviceType.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "list existing device actions for a specific device type")
	@RequestMapping(path = "/devices/types/{hid}/actions", method = RequestMethod.GET)
	public ListResultModel<DeviceActionModel> getDeviceTypeActions(
	        @ApiParam(value = "device type hid", required = true) @PathVariable(value = "hid") String hid) {

		AccessKey accessKey = getValidatedAccessKey(ProductSystemNames.KRONOS);

		DeviceType deviceType = getKronosCache().findDeviceTypeByHid(hid);
		Assert.notNull(deviceType, "device type is not found");
		Assert.isTrue(deviceType.getApplicationId().equals(accessKey.getApplicationId()), "applicationId mismatched!");

		List<DeviceActionModel> data = apiUtil.buildDeviceActionModelList(deviceType.getActions());
		return new ListResultModel<DeviceActionModel>().withData(data).withSize(data.size());
	}

	@ApiOperation(value = "delete a device action from a specific device type", response = HidModel.class)
	@RequestMapping(path = "/devices/types/{hid}/actions/{index}", method = RequestMethod.DELETE)
	public HidModel deleteDeviceTypeAction(
	        @ApiParam(value = "device type hid", required = true) @PathVariable(value = "hid") String hid,
	        @ApiParam(value = "device action index", required = true) @PathVariable(value = "index") int index,
	        HttpServletRequest request) {
		String method = "deleteDeviceTypeAction";

		AccessKey accessKey = validateCanWriteApplication(ProductSystemNames.KRONOS);

		DeviceType deviceType = getKronosCache().findDeviceTypeByHid(hid);
		Assert.notNull(deviceType, "device type is not found");

		auditLog(method, deviceType.getApplicationId(), deviceType.getId(), accessKey.getId(), request);

		Assert.isTrue(deviceType.getApplicationId().equals(accessKey.getApplicationId()), "applicationId mismatched!");

		deviceType = getDeviceTypeService().deleteDeviceActions(deviceType.getId(), Arrays.asList(index),
		        accessKey.getId());
		return new HidModel().withHid(deviceType.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "create a new device action for a specific node", response = HidModel.class)
	@RequestMapping(path = "/nodes/{hid}/actions", method = RequestMethod.POST)
	public HidModel createNodeAction(
	        @ApiParam(value = "node hid", required = true) @PathVariable(value = "hid") String hid,
	        @ApiParam(value = "device action model", required = true) @RequestBody(required = false) DeviceActionModel body,
	        HttpServletRequest request) {
		String method = "createNodeAction";

		AccessKey accessKey = validateCanWriteNode(hid);

		Node node = getKronosCache().findNodeByHid(hid);
		Assert.notNull(node, "node is null");

		auditLog(method, node.getApplicationId(), node.getId(), accessKey.getId(), request);

		Assert.isTrue(node.getApplicationId().equals(accessKey.getApplicationId()), "applicationId mismatched!");

		DeviceAction deviceAction = buildDeviceAction(accessKey.getApplicationId());

		node = nodeService.createDeviceAction(node, deviceAction, accessKey.getId());
		return new HidModel().withHid(node.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "update an existing device action for a specific node", response = HidModel.class)
	@RequestMapping(path = "/nodes/{hid}/actions/{index}", method = RequestMethod.PUT)
	public HidModel updateNodeAction(
	        @ApiParam(value = "node hid", required = true) @PathVariable(value = "hid") String hid,
	        @ApiParam(value = "device action index", required = true) @PathVariable(value = "index") int index,
	        @ApiParam(value = "device action model", required = true) @RequestBody(required = false) DeviceActionModel body,
	        HttpServletRequest request) {
		String method = "updateNodeAction";

		AccessKey accessKey = validateCanWriteNode(hid);

		Node node = getKronosCache().findNodeByHid(hid);
		Assert.notNull(node, "node is null");
		Assert.isTrue(node.getApplicationId().equals(accessKey.getApplicationId()), "applicationId mismatched!");
		Assert.notEmpty(node.getActions(), "device actions list is empty");
		Assert.isTrue(index >= 0 && index < node.getActions().size(), "invalid index");

		auditLog(method, node.getApplicationId(), node.getId(), accessKey.getId(), request);

		DeviceAction deviceAction = node.getActions().get(index);
		deviceAction = updateDeviceAction(deviceAction);

		node = nodeService.updateDeviceAction(node, index, deviceAction, accessKey.getId());
		return new HidModel().withHid(node.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "delete a device action from a specific node", response = HidModel.class)
	@RequestMapping(path = "/nodes/{hid}/actions/{index}", method = RequestMethod.DELETE)
	public HidModel deleteNodeAction(
	        @ApiParam(value = "node hid", required = true) @PathVariable(value = "hid") String hid,
	        @ApiParam(value = "device action index", required = true) @PathVariable(value = "index") int index,
	        HttpServletRequest request) {
		String method = "deleteNodeAction";

		AccessKey accessKey = validateCanWriteNode(hid);

		Node node = getKronosCache().findNodeByHid(hid);
		Assert.notNull(node, "node is null");
		Assert.isTrue(node.getApplicationId().equals(accessKey.getApplicationId()), "applicationId mismatched!");
		auditLog(method, node.getApplicationId(), node.getId(), accessKey.getId(), request);

		node = nodeService.deleteDeviceActions(node, Arrays.asList(index), accessKey.getId());
		return new HidModel().withHid(node.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "list existing device actions for a specific node")
	@RequestMapping(path = "/nodes/{hid}/actions", method = RequestMethod.GET)
	public ListResultModel<DeviceActionModel> getNodeActions(
	        @ApiParam(value = "node hid", required = true) @PathVariable(value = "hid") String hid) {

		AccessKey accessKey = validateCanReadNode(hid);

		Node node = getKronosCache().findNodeByHid(hid);
		Assert.notNull(node, "node is null");
		Assert.isTrue(node.getApplicationId().equals(accessKey.getApplicationId()), "applicationId mismatched!");

		List<DeviceActionModel> data = apiUtil.buildDeviceActionModelList(node.getActions());
		return new ListResultModel<DeviceActionModel>().withData(data).withSize(data.size());
	}

	private DeviceAction buildDeviceAction(String applicationId) {
		DeviceActionModel model = JsonUtils.fromJson(getApiPayload(), DeviceActionModel.class);
		Assert.notNull(model, "DeviceActionModel is null");
		model.trim();
		if (!model.isNoTelemetry()) {
			Assert.hasText(model.getCriteria(), "criteria is empty");
		}
		Assert.hasText(model.getSystemName(), "systemName is empty");

		DeviceActionType deviceActionType = deviceActionTypeService.getDeviceActionTypeRepository()
		        .findByApplicationIdAndSystemName(applicationId, model.getSystemName());
		Assert.notNull(deviceActionType, "deviceActionType not found");

		DeviceAction deviceAction = new DeviceAction();
		deviceAction.setDeviceActionTypeId(deviceActionType.getId());
		deviceAction.setDescription(model.getDescription());
		deviceAction.setCriteria(model.getCriteria());
		deviceAction.setNoTelemetry(model.isNoTelemetry());
		deviceAction.setNoTelemetryTime(model.getNoTelemetryTime());
		deviceAction.setExpiration(model.getExpiration());
		deviceAction.setParameters(model.getParameters());
		deviceAction.setEnabled(model.isEnabled());
		return deviceAction;
	}

	private DeviceActionTypeModel buildDeviceActionTypeModel(Application application,
	        DeviceActionType deviceActionType) {
		Assert.notNull(deviceActionType, "deviceActionType is null");
		Assert.notNull(application, "application is null");
		DeviceActionTypeModel model = buildModel(new DeviceActionTypeModel(), deviceActionType);
		model.setApplicationHid(application.getHid());
		model.setParameters(deviceActionType.getParameters());
		model.setSystemName(deviceActionType.getSystemName());
		return model;
	}

	private DeviceAction updateDeviceAction(DeviceAction deviceAction) {
		Assert.notNull(deviceAction, "device action is null");
		DeviceActionModel model = JsonUtils.fromJson(getApiPayload(), DeviceActionModel.class);
		Assert.notNull(model, "DeviceActionModel is null");
		model.trim();

		if (StringUtils.isNotEmpty(model.getCriteria())) {
			deviceAction.setCriteria(model.getCriteria());
		}
		if (StringUtils.isNotEmpty(model.getDescription())) {
			deviceAction.setDescription(model.getDescription());
		}
		deviceAction.setNoTelemetry(model.isNoTelemetry());
		deviceAction.setNoTelemetryTime(model.getNoTelemetryTime());
		deviceAction.setEnabled(model.isEnabled());
		deviceAction.setExpiration(model.getExpiration());
		deviceAction.setParameters(model.getParameters());
		return deviceAction;
	}
}

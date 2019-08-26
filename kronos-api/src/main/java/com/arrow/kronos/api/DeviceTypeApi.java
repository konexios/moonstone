package com.arrow.kronos.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.data.DeviceStateValueMetadata;
import com.arrow.kronos.data.DeviceTelemetry;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.TelemetryUnit;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AccessKey;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import moonstone.acn.client.model.AcnDeviceCategory;
import moonstone.acn.client.model.DeviceStateValueMetadataModel;
import moonstone.acn.client.model.DeviceTypeModel;
import moonstone.acn.client.model.DeviceTypeRegistrationModel;
import moonstone.acn.client.model.DeviceTypeTelemetryModel;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.acs.AcsLogicalException;
import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.HidModel;
import moonstone.acs.client.model.ListResultModel;

@RestController
@RequestMapping("/api/v1/kronos/devices/types")
public class DeviceTypeApi extends BaseApiAbstract {

	@ApiOperation(value = "list existing device types")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ListResultModel<DeviceTypeModel> listDeviceTypes() {

		AccessKey accessKey = getValidatedAccessKey(getProductName());

		List<DeviceTypeModel> data = new ArrayList<>();
		List<DeviceType> deviceTypes = getDeviceTypeService().getDeviceTypeRepository()
				.findByApplicationIdAndEnabled(accessKey.getApplicationId(), true);
		if (deviceTypes != null) {
			deviceTypes.forEach(type -> data.add(buildDeviceTypeModel(type)));
		}
		return new ListResultModel<DeviceTypeModel>().withData(data).withSize(data.size());
	}

	@ApiOperation(value = "create new device type")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public HidModel create(
			@ApiParam(value = "device type model", required = true) @RequestBody(required = false) DeviceTypeRegistrationModel body,
			HttpServletRequest request) {
		String method = "create";

		AccessKey accessKey = validateCanWriteApplication(getProductName());

		DeviceTypeRegistrationModel model = JsonUtils.fromJson(getApiPayload(), DeviceTypeRegistrationModel.class);
		Assert.notNull(model, "model is null");
		model.trim();
		Assert.hasText(model.getName(), "name is empty");

		DeviceType deviceType = getKronosCache().findDeviceTypeByName(accessKey.getApplicationId(), model.getName());
		if (deviceType != null) {
			auditLog(method, deviceType.getApplicationId(), deviceType.getId(), accessKey.getId(), request);
			return new HidModel().withHid(deviceType.getHid()).withMessage("device type already exists");
		}
		if (model.getDeviceCategory() == null) {
			model.setDeviceCategory(AcnDeviceCategory.DEVICE);
		}
		deviceType = buildDeviceType(deviceType, model);
		deviceType.setApplicationId(accessKey.getApplicationId());
		deviceType = getDeviceTypeService().create(deviceType, accessKey.getId());

		auditLog(method, deviceType.getApplicationId(), deviceType.getId(), accessKey.getId(), request);

		return new HidModel().withHid(deviceType.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "update existing device type")
	@RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
	public HidModel update(
			@ApiParam(value = "device type hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "device type model", required = true) @RequestBody(required = false) DeviceTypeRegistrationModel body,
			HttpServletRequest request) {
		String method = "update";

		AccessKey accessKey = validateCanWriteApplication(getProductName());

		DeviceType deviceType = getDeviceTypeService().getDeviceTypeRepository().doFindByHid(hid);
		Assert.notNull(deviceType, "device type does not exist");
		auditLog(method, deviceType.getApplicationId(), deviceType.getId(), accessKey.getId(), request);
		Assert.isTrue(deviceType.getApplicationId().equals(accessKey.getApplicationId()), "applicationId mismatched!");

		DeviceTypeRegistrationModel model = JsonUtils.fromJson(getApiPayload(), DeviceTypeRegistrationModel.class);
		Assert.notNull(model, "model is null");
		model.trim();
		if (model.getName() != null) {
			DeviceType foundByName = getKronosCache().findDeviceTypeByName(accessKey.getApplicationId(),
					model.getName());
			if (foundByName != null && !foundByName.getId().equals(deviceType.getId())) {
				throw new AcsLogicalException("duplicated device type name");
			}
		}
		deviceType = buildDeviceType(deviceType, model);
		deviceType = getDeviceTypeService().update(deviceType, accessKey.getId());
		return new HidModel().withHid(deviceType.getHid()).withMessage("OK");
	}

	private String getProductName() {
		return ProductSystemNames.KRONOS;
	}

	private DeviceTelemetry buildDeviceTelemetry(DeviceTypeTelemetryModel model,
			Map<String, TelemetryUnit> telemetryUnits) {
		Assert.notNull(model, "device telemetry is null");
		Assert.hasText(model.getName(), "device telemetry name is empty");
		Assert.hasText(model.getType(), "device telemetry type is empty");
		Assert.notNull(telemetryUnits, "telemetryUnits is null");
		DeviceTelemetry deviceTelemetry = new DeviceTelemetry();
		deviceTelemetry.setVariables(model.getVariables());
		deviceTelemetry.setDescription(model.getDescription());
		deviceTelemetry.setName(model.getName());
		deviceTelemetry.setType(TelemetryItemType.valueOf(model.getType()));
		String telemetryUnitHid = model.getTelemetryUnitHid();
		if (StringUtils.hasText(telemetryUnitHid)) {
			TelemetryUnit telemetryUnit = telemetryUnits.get(telemetryUnitHid);
			if (telemetryUnit == null) {
				telemetryUnit = getKronosCache().findTelemetryUnitByHid(telemetryUnitHid);
				Assert.notNull(telemetryUnit, "telemetry unit not found for hid: " + telemetryUnitHid);
				telemetryUnits.put(telemetryUnitHid, telemetryUnit);
			}
			deviceTelemetry.setTelemetryUnitId(telemetryUnit.getId());
		}
		return deviceTelemetry;
	}

	private DeviceType buildDeviceType(DeviceType deviceType, DeviceTypeRegistrationModel model) {
		if (deviceType == null) {
			deviceType = new DeviceType();
		}
		deviceType.setEnabled(model.isEnabled());
		if (StringUtils.hasText(model.getDescription())) {
			deviceType.setDescription(model.getDescription());
		}
		if (StringUtils.hasText(model.getName())) {
			deviceType.setName(model.getName());
		}
		if (model.getTelemetries() != null) {
			List<DeviceTelemetry> telemetries = new ArrayList<>();
			Map<String, TelemetryUnit> telemetryUnits = new HashMap<>();
			for (DeviceTypeTelemetryModel telemetryModel : model.getTelemetries()) {
				telemetries.add(buildDeviceTelemetry(telemetryModel, telemetryUnits));
			}
			deviceType.setTelemetries(telemetries);
		}
		if (model.getStateMetadata() != null) {
			for (DeviceStateValueMetadataModel item : model.getStateMetadata().values()) {
				DeviceStateValueMetadata metadata = new DeviceStateValueMetadata();
				metadata.setName(item.getName());
				metadata.setDescription(item.getDescription());
				metadata.setType(item.getType());
				deviceType.getStateMetadata().put(metadata.getName(), metadata);
			}
		}
		if (model.getDeviceCategory() != null) {
			deviceType.setDeviceCategory(model.getDeviceCategory());
		}
		return deviceType;
	}

	private DeviceTypeTelemetryModel buildDeviceTypeTelemetryModel(DeviceTelemetry deviceTelemetry,
			Map<String, TelemetryUnit> telemetryUnits) {
		Assert.notNull(deviceTelemetry, "device telemetry is null");
		Assert.notNull(telemetryUnits, "telemetryUnits is null");
		DeviceTypeTelemetryModel telemetry = new DeviceTypeTelemetryModel();
		telemetry.setDescription(deviceTelemetry.getDescription());
		telemetry.setName(deviceTelemetry.getName());
		telemetry.setType(deviceTelemetry.getType().toString());
		telemetry.setVariables(deviceTelemetry.getVariables());
		String telemetryUnitId = deviceTelemetry.getTelemetryUnitId();
		if (telemetryUnitId != null) {
			TelemetryUnit telemetryUnit = telemetryUnits.get(telemetryUnitId);
			if (telemetryUnit == null) {
				telemetryUnit = getKronosCache().findTelemetryUnitById(telemetryUnitId);
				if (telemetryUnit != null) {
					telemetry.setTelemetryUnitHid(telemetryUnit.getHid());
				}
			}
		}
		return telemetry;
	}

	private DeviceTypeModel buildDeviceTypeModel(DeviceType deviceType) {
		Assert.notNull(deviceType, "device type is null");
		DeviceTypeModel model = buildModel(new DeviceTypeModel(), deviceType);
		model.setDeviceCategory(deviceType.getDeviceCategory());
		if (deviceType.getTelemetries() != null) {
			List<DeviceTypeTelemetryModel> telemetries = new ArrayList<>();
			Map<String, TelemetryUnit> telemetryUnits = new HashMap<>();
			for (DeviceTelemetry deviceTelemetry : deviceType.getTelemetries()) {
				telemetries.add(buildDeviceTypeTelemetryModel(deviceTelemetry, telemetryUnits));
			}
			model.setTelemetries(telemetries);
		}
		if (deviceType.getStateMetadata() != null) {
			for (DeviceStateValueMetadata item : deviceType.getStateMetadata().values()) {
				model.getStateMetadata().put(item.getName(), new DeviceStateValueMetadataModel()
						.withName(item.getName()).withType(item.getType()).withDescription(item.getDescription()));
			}
		}
		return model;
	}
}

package com.arrow.rhea.api;

import java.util.List;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.rhea.client.model.DeviceManufacturerModel;
import com.arrow.rhea.data.DeviceManufacturer;

import moonstone.acs.JsonUtils;

@RestController(value = "privateRheaDeviceManufacturerApi")
@RequestMapping("/api/v1/private/rhea/device-manufacturers")
public class DeviceManufacturerApi extends BaseApiAbstract {

	@RequestMapping(path = "", method = RequestMethod.POST)
	public DeviceManufacturer create(@RequestBody(required = false) DeviceManufacturerModel body) {
		DeviceManufacturerModel model = JsonUtils.fromJson(getApiPayload(), DeviceManufacturerModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getWho(), "who is empty");
		Assert.notNull(model.getModel(), "DeviceManufacturer is null");
		Assert.hasText(model.getModel().getName(), "name is empty");
		Assert.hasText(model.getModel().getDescription(), "description is empty");

		return getDeviceManufacturerService().create(model.getModel(), model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.PUT)
	public DeviceManufacturer update(@RequestBody(required = false) DeviceManufacturerModel body) {
		DeviceManufacturerModel model = JsonUtils.fromJson(getApiPayload(), DeviceManufacturerModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getWho(), "who is empty");
		Assert.notNull(model.getModel(), "DeviceManufacturer is null");
		Assert.hasText(model.getModel().getName(), "name is empty");
		Assert.hasText(model.getModel().getDescription(), "description is empty");

		DeviceManufacturer deviceManufacturer = getDeviceManufacturerService().getDeviceManufacturerRepository()
				.findById(model.getModel().getId()).orElse(null);
		Assert.notNull(deviceManufacturer, "deviceManufacturer is not found");
		deviceManufacturer.setDescription(model.getModel().getDescription());
		deviceManufacturer.setEditable(model.getModel().isEditable());
		deviceManufacturer.setEnabled(model.getModel().isEnabled());
		deviceManufacturer.setName(model.getModel().getName());

		return getDeviceManufacturerService().update(deviceManufacturer, model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<DeviceManufacturer> findAll() {
		return getDeviceManufacturerService().getDeviceManufacturerRepository().findAll();
	}

	@RequestMapping(path = "/ids/{id}", method = RequestMethod.GET)
	public DeviceManufacturer findById(@PathVariable(name = "id", required = true) String id) {
		return getDeviceManufacturerService().getDeviceManufacturerRepository().findById(id).orElse(null);
	}

	@RequestMapping(path = "/enabled/{enabled}", method = RequestMethod.GET)
	public List<DeviceManufacturer> findAllByEnabled(@PathVariable(name = "enabled", required = true) boolean enabled) {
		return getDeviceManufacturerService().getDeviceManufacturerRepository().findAllByEnabled(enabled);
	}
}

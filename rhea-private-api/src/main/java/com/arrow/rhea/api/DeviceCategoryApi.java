package com.arrow.rhea.api;

import java.util.List;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.rhea.client.model.DeviceCategoryModel;
import com.arrow.rhea.data.DeviceCategory;

import moonstone.acs.JsonUtils;

@RestController(value = "privateRheaDeviceCategoryApi")
@RequestMapping("/api/v1/private/rhea/device-categories")
public class DeviceCategoryApi extends BaseApiAbstract {

	@RequestMapping(path = "", method = RequestMethod.POST)
	public DeviceCategory create(@RequestBody(required = false) DeviceCategoryModel body) {
		DeviceCategoryModel model = JsonUtils.fromJson(getApiPayload(), DeviceCategoryModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getWho(), "who is empty");
		Assert.notNull(model.getModel(), "DeviceCategory is null");
		Assert.hasText(model.getModel().getDescription(), "description is empty");
		Assert.hasText(model.getModel().getName(), "name is empty");

		DeviceCategory deviceCategory = getDeviceCategoryService().getDeviceCategoryRepository()
				.findByName(model.getModel().getName());
		Assert.isNull(deviceCategory, "deviceCategory already exists");

		return getDeviceCategoryService().create(model.getModel(), model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.PUT)
	public DeviceCategory update(@RequestBody(required = false) DeviceCategoryModel body) {
		DeviceCategoryModel model = JsonUtils.fromJson(getApiPayload(), DeviceCategoryModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getWho(), "who is empty");
		Assert.notNull(model.getModel(), "DeviceCategory is null");
		Assert.hasText(model.getModel().getDescription(), "description is empty");
		Assert.hasText(model.getModel().getName(), "name is empty");
		Assert.hasText(model.getModel().getId(), "id is empty");

		DeviceCategory deviceCategory = getDeviceCategoryService().getDeviceCategoryRepository()
				.findById(model.getModel().getId()).orElse(null);
		Assert.notNull(deviceCategory, "deviceCategory is not found");
		deviceCategory.setDescription(model.getModel().getDescription());
		deviceCategory.setEnabled(model.getModel().isEnabled());
		if (!StringUtils.equals(deviceCategory.getName(), model.getModel().getName())) {
			DeviceCategory category = getDeviceCategoryService().getDeviceCategoryRepository()
					.findByName(model.getModel().getName());
			Assert.isNull(category, "name already exists");
			deviceCategory.setName(model.getModel().getName());
		}

		return getDeviceCategoryService().update(deviceCategory, model.getWho());
	}

	@RequestMapping(path = "/ids/{id}", method = RequestMethod.GET)
	public DeviceCategory findById(@PathVariable(name = "id", required = true) String id) {
		return getDeviceCategoryService().getDeviceCategoryRepository().findById(id).orElse(null);
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<DeviceCategory> findAll(@RequestParam(name = "enabled", required = false) Boolean enabled) {
		if (enabled == null) {
			return getDeviceCategoryService().getDeviceCategoryRepository().findAll();
		} else {
			return getDeviceCategoryService().getDeviceCategoryRepository().findAllByEnabled(enabled);
		}
	}

	@RequestMapping(path = "/names/{name}", method = RequestMethod.GET)
	public DeviceCategory findByName(@PathVariable(name = "name", required = true) String name) {
		return getDeviceCategoryService().getDeviceCategoryRepository().findByName(name);
	}
}

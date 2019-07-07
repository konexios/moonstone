package com.arrow.rhea.api;

import java.util.List;
import java.util.Set;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acs.JsonUtils;
import com.arrow.rhea.client.model.DeviceTypeModel;
import com.arrow.rhea.data.DeviceType;
import com.arrow.rhea.repo.DeviceTypeSearchParams;

@RestController(value = "privateRheaDeviceTypeApi")
@RequestMapping("/api/v1/private/rhea/device-types")
public class DeviceTypeApi extends BaseApiAbstract {

	@RequestMapping(path = "", method = RequestMethod.POST)
	public DeviceType create(@RequestBody(required = false) DeviceTypeModel body) {
		DeviceTypeModel model = JsonUtils.fromJson(getApiPayload(), DeviceTypeModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getWho(), "who is empty");
		Assert.notNull(model.getModel(), "DeviceType is null");
		Assert.hasText(model.getModel().getCompanyId(), "companyId is empty");
		Assert.hasText(model.getModel().getDescription(), "description is empty");
		Assert.hasText(model.getModel().getName(), "name is empty");

		return getDeviceTypeService().create(model.getModel(), model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.PUT)
	public DeviceType update(@RequestBody(required = false) DeviceTypeModel body) {
		DeviceTypeModel model = JsonUtils.fromJson(getApiPayload(), DeviceTypeModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getWho(), "who is empty");
		Assert.notNull(model.getModel(), "DeviceType is null");
		Assert.hasText(model.getModel().getCompanyId(), "companyId is empty");
		Assert.hasText(model.getModel().getDescription(), "description is empty");
		Assert.hasText(model.getModel().getName(), "name is empty");
		Assert.hasText(model.getModel().getId(), "id is empty");

		DeviceType deviceType = getDeviceTypeService().getDeviceTypeRepository().findById(model.getModel().getId())
				.orElse(null);
		Assert.notNull(deviceType, "deviceType is not found");
		deviceType.setCompanyId(model.getModel().getCompanyId());
		deviceType.setDescription(model.getModel().getDescription());
		deviceType.setDeviceProductId(model.getModel().getDeviceProductId());
		deviceType.setEditable(model.getModel().isEditable());
		deviceType.setEnabled(model.getModel().isEnabled());
		deviceType.setName(model.getModel().getName());

		return getDeviceTypeService().update(deviceType, model.getWho());
	}

	@RequestMapping(path = "/ids/{id}", method = RequestMethod.GET)
	public DeviceType findById(@PathVariable(name = "id", required = true) String id) {
		return getDeviceTypeService().getDeviceTypeRepository().findById(id).orElse(null);
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<DeviceType> findAll(@RequestParam(name = "companyId", required = false) Set<String> companyIds,
			@RequestParam(name = "deviceProductId", required = false) Set<String> deviceProductIds,
			@RequestParam(name = "name", required = false) Set<String> names,
			@RequestParam(name = "enabled", required = false) Boolean enabled) {
		DeviceTypeSearchParams params = new DeviceTypeSearchParams();
		if (companyIds != null) {
			companyIds.forEach(params::addCompanyIds);
		}
		if (deviceProductIds != null) {
			deviceProductIds.forEach(params::addDeviceProductIds);
		}
		if (names != null) {
			names.forEach(params::addNames);
		}
		params.setEnabled(enabled);
		return getDeviceTypeService().getDeviceTypeRepository().findDeviceTypes(params);
	}
}

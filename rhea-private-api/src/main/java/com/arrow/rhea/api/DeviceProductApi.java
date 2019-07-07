package com.arrow.rhea.api;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.acs.JsonUtils;
import com.arrow.rhea.client.model.DeviceProductModel;
import com.arrow.rhea.data.DeviceProduct;
import com.arrow.rhea.repo.DeviceProductSearchParams;

@RestController(value = "privateRheaDeviceProductApi")
@RequestMapping("/api/v1/private/rhea/device-products")
public class DeviceProductApi extends BaseApiAbstract {

	@RequestMapping(path = "", method = RequestMethod.POST)
	public DeviceProduct create(@RequestBody(required = false) DeviceProductModel body) {
		DeviceProductModel model = JsonUtils.fromJson(getApiPayload(), DeviceProductModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getWho(), "who is empty");
		Assert.notNull(model.getModel(), "DeviceProduct is null");
		Assert.hasText(model.getModel().getName(), "name is empty");
		Assert.hasText(model.getModel().getDescription(), "description is empty");
		Assert.hasText(model.getModel().getCompanyId(), "companyId is empty");
		Assert.hasText(model.getModel().getDeviceManufacturerId(), "deviceManufacturerId is empty");
		Assert.notNull(model.getModel().getDeviceCategory(), "deviceCategory is null");

		return getDeviceProductService().create(model.getModel(), model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.PUT)
	public DeviceProduct update(@RequestBody(required = false) DeviceProductModel body) {
		DeviceProductModel model = JsonUtils.fromJson(getApiPayload(), DeviceProductModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getWho(), "who is empty");
		DeviceProduct product = model.getModel();
		Assert.notNull(product, "DeviceProduct is null");
		Assert.hasText(product.getName(), "name is empty");
		Assert.hasText(product.getDescription(), "description is empty");
		Assert.hasText(product.getCompanyId(), "companyId is empty");
		Assert.hasText(product.getDeviceManufacturerId(), "deviceManufacturerId is empty");
		Assert.notNull(model.getModel().getDeviceCategory(), "deviceCategory is null");
		Assert.hasText(product.getId(), "id is empty");

		DeviceProduct deviceProduct = getDeviceProductService().getDeviceProductRepository().findById(product.getId())
				.orElse(null);
		Assert.notNull(deviceProduct, "deviceProduct is not found");
		deviceProduct.setCompanyId(product.getCompanyId());
		deviceProduct.setDescription(product.getDescription());
		deviceProduct.setDeviceCategory(product.getDeviceCategory());
		deviceProduct.setDeviceManufacturerId(product.getDeviceManufacturerId());
		deviceProduct.setEditable(product.isEditable());
		deviceProduct.setEnabled(product.isEnabled());
		deviceProduct.setName(product.getName());

		return getDeviceProductService().update(deviceProduct, model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<DeviceProduct> findAllBy(@RequestParam(name = "companyId", required = false) Set<String> companyIds,
			@RequestParam(name = "deviceManufacturerId", required = false) Set<String> deviceManufacturerIds,
			@RequestParam(name = "deviceCategory", required = false) Set<String> deviceCategories,
			@RequestParam(name = "enabled", required = false) Boolean enabled) {
		DeviceProductSearchParams params = new DeviceProductSearchParams();
		if (companyIds != null) {
			companyIds.forEach(params::addCompanyIds);
		}
		if (deviceManufacturerIds != null) {
			deviceManufacturerIds.forEach(params::addDeviceManufacturerIds);
		}
		if (deviceCategories != null) {
			EnumSet<AcnDeviceCategory> deviceCategorySet = EnumSet.noneOf(AcnDeviceCategory.class);
			for (String name : deviceCategories) {
				deviceCategorySet.add(AcnDeviceCategory.valueOf(name));
			}

			params.setDeviceCategories(deviceCategorySet);
		}
		params.setEnabled(enabled);
		return getDeviceProductService().getDeviceProductRepository().findDeviceProducts(params);
	}

	@RequestMapping(path = "/ids/{id}", method = RequestMethod.GET)
	public DeviceProduct findById(@PathVariable(name = "id", required = true) String id) {
		return getDeviceProductService().getDeviceProductRepository().findById(id).orElse(null);
	}
}

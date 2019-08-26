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

import com.arrow.rhea.client.model.SoftwareProductModel;
import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.repo.SoftwareProductSearchParams;

import moonstone.acs.JsonUtils;

@RestController(value = "privateRheaSoftwareProductApi")
@RequestMapping("/api/v1/private/rhea/software-products")
public class SoftwareProductApi extends BaseApiAbstract {

	@RequestMapping(path = "", method = RequestMethod.POST)
	public SoftwareProduct create(@RequestBody(required = false) SoftwareProductModel body) {
		SoftwareProductModel model = JsonUtils.fromJson(getApiPayload(), SoftwareProductModel.class);
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getModel(), "SoftwareProduct is null");
		Assert.hasText(model.getWho(), "who is empty");
		Assert.hasText(model.getModel().getCompanyId(), "companyId is empty");
		Assert.hasText(model.getModel().getSoftwareVendorId(), "softwareVendorId is empty");
		Assert.hasText(model.getModel().getName(), "name is empty");
		Assert.hasText(model.getModel().getDescription(), "description is empty");

		return getSoftwareProductService().create(model.getModel(), model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.PUT)
	public SoftwareProduct update(@RequestBody(required = false) SoftwareProductModel body) {
		SoftwareProductModel model = JsonUtils.fromJson(getApiPayload(), SoftwareProductModel.class);
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getModel(), "SoftwareProduct is null");
		Assert.hasText(model.getWho(), "who is empty");
		Assert.hasText(model.getModel().getCompanyId(), "companyId is empty");
		Assert.hasText(model.getModel().getSoftwareVendorId(), "softwareVendorId is empty");
		Assert.hasText(model.getModel().getName(), "name is empty");
		Assert.hasText(model.getModel().getDescription(), "description is empty");
		Assert.hasText(model.getModel().getId(), "id is empty");

		SoftwareProduct softwareProduct = getSoftwareProductService().getSoftwareProductRepository()
				.findById(model.getModel().getId()).orElse(null);
		Assert.notNull(softwareProduct, "softwareProduct is not found");
		softwareProduct.setCompanyId(model.getModel().getCompanyId());
		softwareProduct.setDescription(model.getModel().getDescription());
		softwareProduct.setEnabled(model.getModel().isEnabled());
		softwareProduct.setName(model.getModel().getName());
		softwareProduct.setSoftwareVendorId(model.getModel().getSoftwareVendorId());

		return getSoftwareProductService().update(softwareProduct, model.getWho());
	}

	@RequestMapping(path = "/ids/{id}", method = RequestMethod.GET)
	public SoftwareProduct findById(@PathVariable(name = "id", required = true) String id) {
		return getSoftwareProductService().getSoftwareProductRepository().findById(id).orElse(null);
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<SoftwareProduct> findAllBy(@RequestParam(name = "companyId", required = false) Set<String> companyIds,
			@RequestParam(name = "softwareVendorId", required = false) Set<String> softwareVendorIds,
			@RequestParam(name = "enabled", required = false) Boolean enabled,
			@RequestParam(name = "name", required = false) String name) {
		SoftwareProductSearchParams params = new SoftwareProductSearchParams();
		if (companyIds != null) {
			companyIds.forEach(params::addCompanyIds);
		}
		if (softwareVendorIds != null) {
			softwareVendorIds.forEach(params::addSoftwareVendorIds);
		}
		params.setEnabled(enabled);
		params.setName(name);

		return getSoftwareProductService().getSoftwareProductRepository().findSoftwareProducts(params);
	}
}

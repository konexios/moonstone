package com.arrow.rhea.api;

import java.util.List;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.rhea.client.model.SoftwareVendorModel;
import com.arrow.rhea.data.SoftwareVendor;

import moonstone.acs.JsonUtils;

@RestController(value = "privateRheaSoftwareVendorApi")
@RequestMapping("/api/v1/private/rhea/software-vendors")
public class SoftwareVendorApi extends BaseApiAbstract {

	@RequestMapping(path = "", method = RequestMethod.POST)
	public SoftwareVendor create(@RequestBody(required = false) SoftwareVendorModel body) {

		SoftwareVendorModel model = JsonUtils.fromJson(getApiPayload(), SoftwareVendorModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getWho(), "who is empty");
		Assert.notNull(model.getModel(), "SoftwareVendor is null");
		Assert.hasText(model.getModel().getCompanyId(), "companyId is empty");
		Assert.hasText(model.getModel().getDescription(), "description is empty");
		Assert.hasText(model.getModel().getName(), "name is empty");

		return getSoftwareVendorService().create(model.getModel(), model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.PUT)
	public SoftwareVendor update(@RequestBody(required = false) SoftwareVendorModel body) {
		SoftwareVendorModel model = JsonUtils.fromJson(getApiPayload(), SoftwareVendorModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getWho(), "who is empty");
		Assert.notNull(model.getModel(), "SoftwareVendor is null");
		Assert.hasText(model.getModel().getCompanyId(), "companyId is empty");
		Assert.hasText(model.getModel().getDescription(), "description is empty");
		Assert.hasText(model.getModel().getName(), "name is empty");
		Assert.hasText(model.getModel().getId(), "id is empty");

		SoftwareVendor softwareVendor = getSoftwareVendorService().getSoftwareVendorRepository()
				.findById(model.getModel().getId()).orElse(null);
		Assert.notNull(softwareVendor, "softwareVendor is not found");
		softwareVendor.setCompanyId(model.getModel().getCompanyId());
		softwareVendor.setDescription(model.getModel().getDescription());
		softwareVendor.setEnabled(model.getModel().isEnabled());
		softwareVendor.setName(model.getModel().getName());

		return getSoftwareVendorService().update(softwareVendor, model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<SoftwareVendor> findAll() {
		return getSoftwareVendorService().getSoftwareVendorRepository().findAll();
	}

	@RequestMapping(path = "/companies/{companyId}/enabled/{enabled}", method = RequestMethod.GET)
	public List<SoftwareVendor> findAllByCompanyIdAndEnabled(
			@PathVariable(name = "companyId", required = true) String companyId,
			@PathVariable(name = "enabled", required = true) boolean enabled) {
		return getSoftwareVendorService().getSoftwareVendorRepository().findAllByCompanyIdAndEnabled(companyId,
				enabled);
	}

	@RequestMapping(path = "/ids/{id}", method = RequestMethod.GET)
	public SoftwareVendor findById(@PathVariable(name = "id", required = true) String id) {
		return getSoftwareVendorService().getSoftwareVendorRepository().findById(id).orElse(null);
	}
}

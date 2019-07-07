package com.arrow.pegasus.api;

import java.util.List;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acs.JsonUtils;
import com.arrow.pegasus.client.model.ApplicationEngineChangeModel;
import com.arrow.pegasus.data.ApplicationEngine;

@RestController(value = "localPegasusApplicationEngineApi")
@RequestMapping("/api/v1/local/pegasus/applicationEngines")
public class ApplicationEngineApi extends BaseApiAbstract {

	@RequestMapping(path = "/products/{productId}", method = RequestMethod.GET)
	public List<ApplicationEngine> findAllByProductId(@PathVariable(required = true) String productId) {
		return getApplicationEngineService().getApplicationEngineRepository().findAllByProductId(productId);
	}

	@RequestMapping(path = "/ids/{id}", method = RequestMethod.GET)
	public ApplicationEngine findById(@PathVariable(name = "id", required = true) String id) {
		ApplicationEngine applicationEngine = getApplicationEngineService().getApplicationEngineRepository()
				.findById(id).orElse(null);
		Assert.notNull(applicationEngine, "applicationEngine not found");
		return applicationEngine;
	}

	@RequestMapping(path = "/names/{name}", method = RequestMethod.GET)
	public ApplicationEngine findByName(@PathVariable(name = "name", required = true) String name) {
		ApplicationEngine applicationEngine = getApplicationEngineService().getApplicationEngineRepository()
				.findByName(name);
		Assert.notNull(applicationEngine, "applicationEngine not found");
		return applicationEngine;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ApplicationEngine create(@RequestBody(required = false) ApplicationEngineChangeModel body) {
		ApplicationEngineChangeModel model = JsonUtils.fromJson(getApiPayload(), ApplicationEngineChangeModel.class);
		Assert.notNull(model, "application engine model is null");
		Assert.notNull(model.getApplicationEngine(), "applicationEngine is null");
		Assert.hasText(model.getApplicationEngine().getName(), "name is empty");
		Assert.hasText(model.getApplicationEngine().getDescription(), "description is empty");
		checkProduct(model.getApplicationEngine().getProductId());
		checkZone(model.getApplicationEngine().getZoneId());
		return getApplicationEngineService().create(model.getApplicationEngine(), model.getWho());
	}
}

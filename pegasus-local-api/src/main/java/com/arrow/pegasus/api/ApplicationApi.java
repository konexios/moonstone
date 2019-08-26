package com.arrow.pegasus.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.client.model.ApplicationChangeModel;
import com.arrow.pegasus.client.model.ApplicationRefsModel;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.repo.params.ApplicationSearchParams;

import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.PagingResultModel;

@RestController(value = "localPegasusApplicationApi")
@RequestMapping("/api/v1/local/pegasus/applications")
public class ApplicationApi extends BaseApiAbstract {

	@RequestMapping(path = "/codes/{code}", method = RequestMethod.GET)
	public Application findByCode(@PathVariable(name = "code", required = true) String code) {
		Application application = getApplicationService().getApplicationRepository().findByCode(code);
		Assert.notNull(application, "application not found");
		return application;
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<Application> findAll() {
		return getApplicationService().getApplicationRepository().findAll();
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public Application create(@RequestBody(required = false) ApplicationChangeModel body) {
		ApplicationChangeModel model = JsonUtils.fromJson(getApiPayload(), ApplicationChangeModel.class);
		Assert.notNull(model, "application model is null");
		Assert.notNull(model.getApplication(), "application is null");
		Assert.hasText(model.getWho(), "who is empty");
		return getApplicationService().create(model.getApplication(), model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.PUT)
	public Application update(@RequestBody(required = false) ApplicationChangeModel body) {
		ApplicationChangeModel model = JsonUtils.fromJson(getApiPayload(), ApplicationChangeModel.class);
		Assert.notNull(model, "application model is null");
		Assert.notNull(model.getApplication(), "application is null");
		Assert.hasText(model.getWho(), "who is empty");
		Application application = getApplicationService().getApplicationRepository()
				.findById(model.getApplication().getId()).orElse(null);
		Assert.notNull(application, "application not found");
		return getApplicationService().update(model.getApplication(), model.getWho());
	}

	@RequestMapping(path = "/refs", method = RequestMethod.POST)
	public ApplicationRefsModel populateRefs(@RequestBody(required = false) Application body) {
		Application model = JsonUtils.fromJson(getApiPayload(), Application.class);
		Assert.notNull(model, "application model is null");
		Assert.hasText(model.getProductId(), "productId is empty");
		Assert.hasText(model.getCompanyId(), "companyId is empty");
		Assert.hasText(model.getSubscriptionId(), "subscriptionId is empty");
		return new ApplicationRefsModel().withApplication(getCoreCacheHelper().populateApplication(model));
	}

	@RequestMapping(path = "/application-engines/{applicationEngineId}", method = RequestMethod.GET)
	public List<Application> findAllByApplicationEngineId(
			@PathVariable(name = "applicationEngineId", required = true) String applicationEngineId) {
		return getApplicationService().getApplicationRepository().findAllByApplicationEngineId(applicationEngineId);
	}

	@RequestMapping(path = "/ids/{id}", method = RequestMethod.GET)
	public Application findById(@PathVariable(name = "id", required = true) String id) {
		Application application = getApplicationService().getApplicationRepository().findById(id).orElse(null);
		Assert.notNull(application, "application not found");
		return application;
	}

	@RequestMapping(path = "/hids/{hid}", method = RequestMethod.GET)
	public Application findByHid(@PathVariable(name = "hid", required = true) String hid) {
		Application application = getApplicationService().getApplicationRepository().doFindByHid(hid);
		Assert.notNull(application, "application not found");
		return application;
	}

	@RequestMapping(path = "/products/{productId}/enabled/{enabled}", method = RequestMethod.GET)
	public List<Application> findByProductIdAndEnabled(
			@PathVariable(name = "productId", required = true) String productId,
			@PathVariable(name = "enabled", required = true) boolean enabled) {
		return getApplicationService().getApplicationRepository().findByProductIdAndEnabled(productId, enabled);
	}

	@RequestMapping(path = "/product-extensions/{productExtensionId}/enabled/{enabled}", method = RequestMethod.GET)
	public List<Application> findByProductExtensionIdAndEnabled(
			@PathVariable(name = "productExtensionId", required = true) String productExtensionId,
			@PathVariable(name = "enabled", required = true) boolean enabled) {
		ApplicationSearchParams params = new ApplicationSearchParams();
		params.setEnabled(enabled);
		params.addProductExtensionIds(productExtensionId);
		return getApplicationService().getApplicationRepository().findApplications(params);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public Long delete(@PathVariable(name = "id", required = true) String id) {
		Application application = getApplicationService().getApplicationRepository().findById(id).orElse(null);
		Assert.notNull(application, "application not found");
		return getApplicationService().deleteApplication(id, getAccessKey().getId());
	}

	@RequestMapping(path = "/find", method = RequestMethod.POST)
	public List<Application> findApplications(@RequestBody(required = false) ApplicationSearchParams body) {
		ApplicationSearchParams params = JsonUtils.fromJson(getApiPayload(), ApplicationSearchParams.class);
		Assert.notNull(params, "search criteria is missing");
		return getApplicationService().getApplicationRepository().findApplications(params);
	}

	@RequestMapping(path = "/find/pages", method = RequestMethod.POST)
	public PagingResultModel<Application> findApplications(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "" + DEFAULT_PAGE_SIZE) int size,
			@RequestParam(name = "sortDirection", required = false) String[] sortDirection,
			@RequestParam(name = "sortProperty", required = false) String[] sortProperty,
			@RequestBody(required = false) ApplicationSearchParams body) {
		ApplicationSearchParams params = JsonUtils.fromJson(getApiPayload(), ApplicationSearchParams.class);
		Assert.notNull(params, "search criteria is missing");
		PageRequest pageRequest;
		if (sortDirection != null && sortProperty != null) {
			List<Order> orders = new ArrayList<>();
			for (int i = 0; i < sortDirection.length && i < sortProperty.length; i++) {
				orders.add(new Order(Direction.fromString(sortDirection[i]), sortProperty[i]));
			}
			pageRequest = PageRequest.of(page, size, Sort.by(orders));
		} else {
			pageRequest = PageRequest.of(page, size);
		}
		Page<Application> applications = getApplicationService().getApplicationRepository()
				.findApplications(pageRequest, params);
		PagingResultModel<Application> result = new PagingResultModel<>();
		result.withPage(applications.getNumber()).withTotalPages(applications.getTotalPages())
				.withTotalSize(applications.getTotalElements());
		result.withData(applications.getContent()).withSize(applications.getSize());
		return result;
	}
}

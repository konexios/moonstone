package com.arrow.pegasus.api;

import java.time.Instant;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acs.JsonUtils;
import com.arrow.pegasus.client.model.CreateUpdateModel;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.repo.params.ApplicationSearchParams;
import com.fasterxml.jackson.core.type.TypeReference;

@RestController(value = "localPegasusSubscriptionApi")
@RequestMapping("/api/v1/local/pegasus/subscriptions")
public class SubscriptionApi extends BaseApiAbstract {

	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<Subscription> findAll() {
		return getSubscriptionService().getSubscriptionRepository().findAll();
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public Subscription create(@RequestBody(required = false) CreateUpdateModel<Subscription> body) {
		CreateUpdateModel<Subscription> model = JsonUtils.fromJson(getApiPayload(),
				new TypeReference<CreateUpdateModel<Subscription>>() {
				});
		Assert.notNull(model, "subscription model is null");
		Assert.notNull(model.getModel(), "subscription is null");
		Assert.hasText(model.getWho(), "who is empty");
		return getSubscriptionService().create(model.getModel(), model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.PUT)
	public Subscription update(@RequestBody(required = false) CreateUpdateModel<Subscription> body) {
		CreateUpdateModel<Subscription> model = JsonUtils.fromJson(getApiPayload(),
				new TypeReference<CreateUpdateModel<Subscription>>() {
				});
		Assert.notNull(model, "subscription model is null");
		Assert.notNull(model.getModel(), "subscription is null");
		Assert.hasText(model.getWho(), "who is empty");
		Subscription application = getSubscriptionService().getSubscriptionRepository()
				.findById(model.getModel().getId()).orElse(null);
		Assert.notNull(application, "subscription not found");
		return getSubscriptionService().update(model.getModel(), model.getWho());
	}

	@RequestMapping(path = "/ids/{id}", method = RequestMethod.GET)
	public Subscription findById(@PathVariable(name = "id", required = true) String id) {
		Subscription subscription = getSubscriptionService().getSubscriptionRepository().findById(id).orElse(null);
		Assert.notNull(subscription, "subscription not found");
		return subscription;
	}

	@RequestMapping(path = "/hids/{hid}", method = RequestMethod.GET)
	public Subscription findByHid(@PathVariable(name = "hid", required = true) String hid) {
		Subscription subscription = getSubscriptionService().getSubscriptionRepository().doFindByHid(hid);
		Assert.notNull(subscription, "subscription not found");
		return subscription;
	}

	@RequestMapping(path = "/{subscriptionId}/applications/count", method = RequestMethod.GET)
	public Long getApplicationCount(@PathVariable(name = "subscriptionId", required = true) String subscriptionId,
			@RequestParam(name = "createdBefore", required = false) String createdBefore,
			@RequestParam(name = "enabled", required = false) String enabled) {
		Instant createdBeforeParam = createdBefore != null ? Instant.parse(createdBefore) : null;
		Boolean enabledParam = enabled != null ? Boolean.valueOf(enabled) : null;
		ApplicationSearchParams params = new ApplicationSearchParams().addSubscriptionIds(subscriptionId)
				.withEnabled(enabledParam).createdBefore(createdBeforeParam);
		return getApplicationService().getApplicationRepository().findApplicationCount(params);
	}

	@RequestMapping(path = "{id}", method = RequestMethod.DELETE)
	public Long delete(@PathVariable(name = "id", required = true) String id) {
		Subscription subscription = getSubscriptionService().getSubscriptionRepository().findById(id).orElse(null);
		Assert.notNull(subscription, "subscription not found");
		return getSubscriptionService().deleteSubscription(id, getAccessKey().getId());
	}
}

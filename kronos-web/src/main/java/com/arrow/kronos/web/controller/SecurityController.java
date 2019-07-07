package com.arrow.kronos.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.web.model.ApplicationModels.ApplicationModel;
import com.arrow.kronos.web.model.ApplicationModels.SubscriptionExpirationModel;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.webapi.SecurityApiAbstract;

@RestController
@RequestMapping("/api/v1/core/security")
public class SecurityController extends SecurityApiAbstract {

	@Value("${com.arrow.kronos.subscription.expire.days:30}")
	private int expirationWarnDays;

	@Override
	@RequestMapping(path = "/application/{applicationId}", method = RequestMethod.GET)
	public ApplicationModel application(HttpSession session, @PathVariable String applicationId) {
		Application application = super.application(session, applicationId);
		Subscription subscription = getCoreCacheService().findSubscriptionById(application.getSubscriptionId());
		if (subscription == null) {
			throw new AcsLogicalException("subscription not found");
		}
		ApplicationModel model = new ApplicationModel(application);
		SubscriptionExpirationModel subscriptionExpiration = new SubscriptionExpirationModel(subscription.getEndDate(),
		        expirationWarnDays);
		model.setSubscriptionExpiration(subscriptionExpiration);
		return model;
	}
}

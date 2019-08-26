package com.arrow.pegasus.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.CoreAuditLog;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.repo.SubscriptionRepository;

import moonstone.acs.AcsLogicalException;

@Service
public class SubscriptionService extends BaseServiceAbstract {
	@Autowired
	private SubscriptionRepository subscriptionRepository;
	@Autowired
	private AccessKeyService accessKeyService;
	@Autowired
	private ApplicationService applicationService;

	public SubscriptionRepository getSubscriptionRepository() {
		return subscriptionRepository;
	}

	public Subscription populateRefs(Subscription subscription) {

		if (subscription == null)
			return subscription;

		subscription.setRefCompany(getCoreCacheService().findCompanyById(subscription.getCompanyId()));
		Assert.notNull(subscription.getRefCompany(), "refCompnay is null");

		return subscription;
	}

	public Subscription create(Subscription subscription, String who) {
		String method = "create";

		// logical checks
		if (subscription == null) {
			logInfo(method, "subscription is null");
			throw new AcsLogicalException("subscription is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		subscription = subscriptionRepository.doInsert(subscription, who);

		// TODO revisit, currently not calling cache because newly created
		// subscriptions do not affect any other objects

		accessKeyService.createOwnerKey(subscription.getCompanyId(), subscription.getId(), null, "SubscriptionOwnerKey",
				subscription.getPri(), who);

		getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.Subscription.CREATE_SUBSCRIPTION)
				.productName(ProductSystemNames.PEGASUS).objectId(subscription.getId()).by(who));

		return subscription;
	}

	public Subscription update(Subscription subscription, String who) {
		String method = "update";

		// logical checks
		if (subscription == null) {
			logInfo(method, "subscription is null");
			throw new AcsLogicalException("subscription is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		subscription = subscriptionRepository.doSave(subscription, who);

		getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.Subscription.UPDATE_SUBSCRIPTION)
				.productName(ProductSystemNames.PEGASUS).objectId(subscription.getId()).by(who));

		// re-cache
		getCoreCacheService().clearSubscription(subscription);

		return subscription;
	}

	public Long deleteSubscription(String subscriptionId, String who) {
		String method = "deleteSubscription";
		Assert.hasText(subscriptionId, "subscriptionId is empty");
		Assert.hasLength(who, "who is empty");
		long result = 0;

		// delete applications
		List<Application> applications = applicationService.getApplicationRepository()
				.findBySubscriptionId(subscriptionId);
		logInfo(method, "found %d applications to delete", applications.size());
		for (Application application : applications) {
			try {
				logInfo(method, "deleting applicationId: %s", application.getId());
				long count = applicationService.deleteApplication(application.getId(), who);
				logInfo(method, "application deleted, count: %d", count);
				result += count;
			} catch (Throwable t) {
				logError(method, "error deleting accessKeys", t);
			}
		}

		Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(null);
		if (subscription != null) {
			// delete subscription
			try {
				logInfo(method, "deleting subscriptionId: %s", subscriptionId);
				subscriptionRepository.deleteById(subscriptionId);
				logInfo(method, "subscriptionId deleted: %s", subscriptionId);
				result++;
			} catch (Throwable t) {
				logError(method, "error deleting subscription", t);
			}
			// auditLog
			getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.Subscription.DELETE_SUBSCRIPTION)
					.productName(ProductSystemNames.PEGASUS).objectId(subscription.getId()).by(who)
					.parameter("name", subscription.getName()).parameter("result", String.valueOf(result)));

			// clear subscription cache
			getCoreCacheService().clearSubscription(subscription);
		} else {
			logWarn(method, "Subscription not found: %s", subscriptionId);
		}

		logInfo(method, "result: %d", result);
		return result;
	}
}

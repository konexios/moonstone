package com.arrow.pegasus.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.pegasus.CoreAuditLog;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.repo.CompanyRepository;

@Service
public class CompanyService extends BaseServiceAbstract {

	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private AccessKeyService accessKeyService;
	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private AuthService authService;
	@Autowired
	private UserService userService;

	public CompanyRepository getCompanyRepository() {
		return companyRepository;
	}

	public Company create(Company company, String who) {
		String method = "create";

		// logical checks
		if (company == null) {
			logInfo(method, "company is null");
			throw new AcsLogicalException("company is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		company = companyRepository.doInsert(company, who);

		accessKeyService.createOwnerKey(company.getId(), null, null, "CompanyOwnerKey", company.getPri(), who);

		// TODO revisit, currently not calling cache because newly created
		// companies do not affect any other objects

		getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.Company.CREATE_COMPANY)
				.productName(ProductSystemNames.PEGASUS).objectId(company.getId()).by(who)
				.parameter("name", company.getName()).parameter("abbrName", company.getAbbrName()));

		return company;
	}

	public Company update(Company company, String who) {
		String method = "update";

		// logical checks
		if (company == null) {
			logInfo(method, "company is null");
			throw new AcsLogicalException("company is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		company = companyRepository.doSave(company, who);

		getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.Company.UPDATE_COMPANY)
				.productName(ProductSystemNames.PEGASUS).objectId(company.getId()).by(who)
				.parameter("name", company.getName()).parameter("abbrName", company.getAbbrName()));

		// re-cache
		getCoreCacheService().clearCompany(company);

		return company;
	}

	public Company populateRefs(Company company) {

		if (company == null)
			return company;

		if (!StringUtils.isEmpty(company.getParentCompanyId()))
			company.setRefParentCompany(getCoreCacheService().findCompanyById(company.getParentCompanyId()));

		return company;
	}

	public Long deleteCompany(String companyId, String who) {
		String method = "deleteCompany";

		Assert.hasText(companyId, "companyId is empty");
		Assert.hasText(who, "who is empty");

		long result = 0;

		// delete accessKeys
		try {
			logInfo(method, "deleting accessKeys for companyId: %s", companyId);
			long count = accessKeyService.deleteByCompanyId(companyId, who);
			logInfo(method, "deleted %d accessKeys", count);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting accessKeys", t);
		}

		// delete auths
		try {
			logInfo(method, "deleting auths for companyId: %s", companyId);
			long count = authService.deleteByCompanyId(companyId, who);
			logInfo(method, "deleted %d auths", count);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting auths", t);
		}

		// delete users
		try {
			logInfo(method, "deleting users for companyId: %s", companyId);
			long count = userService.deleteByCompanyId(companyId, who);
			logInfo(method, "deleted %d users", count);
			result += count;
		} catch (Throwable t) {
			logError(method, "error deleting users", t);
		}

		// delete subscriptions
		List<Subscription> subscriptions = subscriptionService.getSubscriptionRepository().findByCompanyId(companyId);
		logInfo(method, "found %d subscriptions to delete", subscriptions.size());
		for (Subscription subscription : subscriptions) {
			try {
				logInfo(method, "deleting subscriptionId: %s", subscription.getId());
				long count = subscriptionService.deleteSubscription(subscription.getId(), who);
				logInfo(method, "subscription deleted, count: %d", count);
				result += count;
			} catch (Throwable t) {
				logError(method, "error deleting subscription", t);
			}
		}

		// delete company
		Company company = companyRepository.findById(companyId).orElse(null);
		if (company != null) {
			try {
				logInfo(method, "deleting companyId: %s", companyId);
				companyRepository.deleteById(companyId);
				logInfo(method, "companyId deleted: %s", companyId);
				result++;
			} catch (Throwable t) {
				logError(method, "error deleting company", t);
			}
			// auditLog
			getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.Company.DELETE_COMPANY)
					.productName(ProductSystemNames.PEGASUS).objectId(companyId).by(who)
					.parameter("name", company.getName()).parameter("result", String.valueOf(result)));

			// clear company cache
			getCoreCacheService().clearCompany(company);
		} else {
			logWarn(method, "company not found: %s", companyId);
		}

		logInfo(method, "result: %d", result);
		return result;

	}
}

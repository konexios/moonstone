package com.arrow.pegasus.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.AccessKeyModel;
import com.arrow.acs.client.model.ApplicationModel;
import com.arrow.acs.client.model.CreateApplicationModel;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.ListResultModel;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.acs.client.model.UpdateApplicationModel;
import com.arrow.pegasus.NotAuthorizedException;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.repo.params.ApplicationSearchParams;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController(value = "pegasusApplicationApi")
@RequestMapping("/api/v1/pegasus/applications")
public class ApplicationApi extends BaseApiAbstract {

	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public ApplicationModel findByHid(@PathVariable String hid) {
		validateCanReadApplicationWithHid(hid);
		Application application = getCoreCacheService().findApplicationByHid(hid);
		return toApplicationModel(application);
	}

	@RequestMapping(path = "/{hid}/access-keys", method = RequestMethod.GET)
	public ListResultModel<AccessKeyModel> findAccessKeysByHid(@PathVariable String hid) {
		validateCanReadApplicationWithHid(hid);
		Application application = getCoreCacheService().findApplicationByHid(hid);
		List<AccessKey> accessKeys = getAccessKeyService().getAccessKeyRepository().findByPri(application.getPri());
		List<AccessKeyModel> data = new ArrayList<>();
		accessKeys.forEach(key -> {
			data.add(toAccessKeyModel(key));
		});
		return new ListResultModel<AccessKeyModel>().withSize(data.size()).withData(data);
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public PagingResultModel<ApplicationModel> findBy(
			// @formatter:off
			@RequestParam(name = "companyHid", required = false) String companyHid,
			@RequestParam(name = "productHid", required = false) String productHid,
			@RequestParam(name = "subscriptionHid", required = false) String subscriptionHid,
			@RequestParam(name = "code", required = false) String code,
			@RequestParam(name = "includeDisabled", required = false, defaultValue = "false") boolean includeDisabled,
			@RequestParam(name = "_page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "_size", required = false, defaultValue = "" + DEFAULT_PAGE_SIZE) int size) {
		// @formatter:on

		String method = "findBy";
		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());

		ApplicationSearchParams params = new ApplicationSearchParams();
		if (!includeDisabled) {
			params.setEnabled(true);
		}

		Subscription subscription = null;
		if (StringUtils.isNotEmpty(subscriptionHid)) {
			subscription = getCoreCacheService().findSubscriptionByHid(StringUtils.trim(subscriptionHid));
			Assert.notNull(subscription, "subscription not found");
			if (!StringUtils.equals(accessKey.getCompanyId(), subscription.getCompanyId())) {
				logWarn(method, "companyId mismatched!");
			}
		}

		Company company = null;
		if (StringUtils.isNotEmpty(companyHid)) {
			company = getCoreCacheService().findCompanyByHid(StringUtils.trim(companyHid));
			Assert.notNull(company, "company not found");
			if (!StringUtils.equals(accessKey.getCompanyId(), company.getId())) {
				logWarn(method, "companyId mismatched!");
			}
		} else {
			company = accessKey.getRefCompany();
		}

		if ((company == null || !hasPrivilege(accessKey::canRead, company))
				&& (subscription == null || !accessKey.canRead(subscription))) {
			throw new NotAuthorizedException();
		}
		if (company != null) {
			params.addCompanyIds(company.getId());
		}
		if (subscription != null) {
			params.addSubscriptionIds(subscription.getId());
		}

		if (StringUtils.isNotEmpty(productHid)) {
			Product product = getCoreCacheService().findProductByHid(StringUtils.trim(productHid));
			Assert.notNull(product, "product not found");
			params.addProductIds(product.getId());
		}

		if (StringUtils.isNotEmpty(code)) {
			params.addCodes(StringUtils.trim(code));
		}

		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Application> applications = getApplicationService().getApplicationRepository()
				.findApplications(pageRequest, params);
		List<ApplicationModel> data = new ArrayList<>();
		applications.forEach(app -> {
			data.add(toApplicationModel(app));
		});

		PagingResultModel<ApplicationModel> result = new PagingResultModel<>();
		result.setData(data);
		result.setPage(page);
		result.setSize(pageRequest.getPageSize());
		result.setTotalPages(applications.getTotalPages());
		result.setTotalSize(applications.getTotalElements());
		return result;
	}

	@ApiOperation(value = "create application")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public HidModel createApplication(
			@ApiParam(value = "create application model", required = true) @RequestBody(required = false) CreateApplicationModel body) {

		CreateApplicationModel model = JsonUtils.fromJson(getApiPayload(), CreateApplicationModel.class);
		Assert.notNull(model, "model is null");

		AccessKey accessKey = validateCanCreateApplication(model.getCompanyHid(), model.getSubscriptionHid());

		Application application = getApplicationService().create(fromCreateApplicationModel(model), accessKey.getId());
		return new HidModel().withHid(application.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "update application")
	@RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
	public HidModel updateApplication(
			@ApiParam(value = "application hid", required = true) @PathVariable(value = "hid", required = true) String hid,
			@ApiParam(value = "update application model", required = true) @RequestBody(required = false) UpdateApplicationModel body) {

		UpdateApplicationModel model = JsonUtils.fromJson(getApiPayload(), UpdateApplicationModel.class);
		Assert.notNull(model, "model is null");

		Application application = getCoreCacheService().findApplicationByHid(hid);
		Assert.notNull(application, "application is not found");

		AccessKey accessKey = validateCanWriteApplicationWithHid(hid);

		application = getApplicationService().update(populateApplication(application, model), accessKey.getId());
		return new HidModel().withHid(application.getHid()).withMessage("OK");
	}

	AccessKey validateCanReadApplicationWithHid(String hid) {
		String method = "validateCanReadApplicationWithHid";
		Application application = getCoreCacheService().findApplicationByHid(hid);
		Assert.notNull(application, "invalid application hid");
		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());
		if (!StringUtils.equals(accessKey.getCompanyId(), application.getCompanyId())) {
			logWarn(method, "companyId mismatched!");
		}
		Company company = getCoreCacheService().findCompanyById(application.getCompanyId());
		Assert.notNull(company, "company not found");
		Subscription subscription = getCoreCacheService().findSubscriptionById(application.getSubscriptionId());
		if (!hasPrivilege(accessKey::canRead, company) && !accessKey.canRead(application)
				&& (subscription == null || !accessKey.canRead(subscription))) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	AccessKey validateCanWriteApplicationWithHid(String hid) {
		String method = "validateCanWriteApplicationWithHid";
		Application application = getCoreCacheService().findApplicationByHid(hid);
		Assert.notNull(application, "invalid application hid");
		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());
		if (!StringUtils.equals(accessKey.getCompanyId(), application.getCompanyId())) {
			logWarn(method, "companyId mismatched!");
		}
		Company company = getCoreCacheService().findCompanyById(application.getCompanyId());
		Subscription subscription = getCoreCacheService().findSubscriptionById(application.getSubscriptionId());
		if (!hasPrivilege(accessKey::canWrite, company) && !accessKey.canWrite(application)
				&& !accessKey.canWrite(subscription)) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	AccessKey validateCanCreateApplication(String companyHid, String subscriptionHid) {
		Assert.hasText(companyHid, "companyHid is empty");
		Assert.hasText(subscriptionHid, "subscriptionHid is empty");
		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());
		Company company = getCoreCacheService().findCompanyByHid(companyHid);
		Assert.notNull(company, "company is not found");
		Subscription subscription = getCoreCacheService().findSubscriptionByHid(subscriptionHid);
		Assert.notNull(subscription, "subscription is not found");
		Assert.isTrue(subscription.getCompanyId().equals(company.getId()), "companyId mismatched");
		if (!hasPrivilege(accessKey::canWrite, company) && !accessKey.canWrite(subscription)) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}
}

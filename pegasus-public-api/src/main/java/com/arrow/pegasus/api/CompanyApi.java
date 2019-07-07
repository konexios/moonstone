package com.arrow.pegasus.api;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

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
import com.arrow.acs.client.model.CompanyModel;
import com.arrow.acs.client.model.CreateCompanyModel;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.ListResultModel;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.acs.client.model.SubscriptionModel;
import com.arrow.acs.client.model.UpdateCompanyModel;
import com.arrow.acs.client.model.UserModel;
import com.arrow.acs.client.model.UserStatus;
import com.arrow.pegasus.NotAuthorizedException;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.CompanyStatus;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.repo.params.CompanySearchParams;
import com.arrow.pegasus.repo.params.UserSearchParams;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController(value = "pegasusCompanyApi")
@RequestMapping("/api/v1/pegasus/companies")
public class CompanyApi extends BaseApiAbstract {

	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public CompanyModel findByHid(@PathVariable String hid) {
		validateCanReadCompanyWithHid(hid);
		Company company = getCoreCacheService().findCompanyByHid(hid);
		return toCompanyModel(company);
	}

	@RequestMapping(path = "/{hid}/subscriptions", method = RequestMethod.GET)
	public ListResultModel<SubscriptionModel> findSubscriptionsByHid(@PathVariable String hid,
			@RequestParam(name = "includeDisabled", required = false, defaultValue = "false") boolean includeDisabled) {

		validateCanReadCompanyWithHid(hid);
		Company company = getCoreCacheService().findCompanyByHid(hid);
		List<SubscriptionModel> data = new ArrayList<>();
		List<Subscription> subscriptions = includeDisabled
				? getSubscriptionService().getSubscriptionRepository().findByCompanyId(company.getId())
				: getSubscriptionService().getSubscriptionRepository().findByCompanyIdAndEnabled(company.getId(), true);
		subscriptions.forEach(subscription -> {
			if (subscription != null) {
				data.add(toSubscriptionModel(subscription));
			}
		});
		return new ListResultModel<SubscriptionModel>().withSize(data.size()).withData(data);
	}

	@RequestMapping(path = "/{hid}/access-keys", method = RequestMethod.GET)
	public ListResultModel<AccessKeyModel> findAccessKeysByHid(@PathVariable String hid) {
		validateCanReadCompanyWithHid(hid);
		Company company = getCoreCacheService().findCompanyByHid(hid);
		Assert.notNull(company, "invalid company hid");
		List<AccessKeyModel> data = new ArrayList<>();
		getAccessKeyService().getAccessKeyRepository().findByPri(company.getPri()).forEach(key -> {
			data.add(toAccessKeyModel(key));
		});
		return new ListResultModel<AccessKeyModel>().withData(data).withSize(data.size());
	}

	@RequestMapping(path = "/{hid}/users", method = RequestMethod.GET)
	public PagingResultModel<UserModel> findBy(
			// @formatter:off
			@PathVariable String hid, @RequestParam(name = "login", required = false) String login,
			@RequestParam(name = "status", required = false) UserStatus status,
			@RequestParam(name = "firstName", required = false) String firstName,
			@RequestParam(name = "lastName", required = false) String lastName,
			@RequestParam(name = "sipUri", required = false) String sipUri,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "_page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "_size", required = false, defaultValue = "" + DEFAULT_PAGE_SIZE) int size) {
		// @formatter:on
		validateCanReadCompanyWithHid(hid);

		Company company = getCoreCacheService().findCompanyByHid(hid);
		PageRequest pageRequest = PageRequest.of(page, size);
		EnumSet<com.arrow.pegasus.data.profile.UserStatus> statuses = EnumSet
				.noneOf(com.arrow.pegasus.data.profile.UserStatus.class);
		if (status != null) {
			statuses.add(fromUserStatusModel(status));
		}
		UserSearchParams params = new UserSearchParams();
		params.addCompanyIds(company.getId());
		params.setLogin(login);
		params.setStatuses(statuses);
		params.setFirstName(firstName);
		params.setLastName(lastName);
		params.setSipUri(sipUri);
		params.setEmail(email);
		Page<User> users = getUserService().getUserRepository().findUsers(pageRequest, params);
		List<UserModel> data = users.getContent().stream().map(this::toUserModel)
				.collect(Collectors.toCollection(ArrayList::new));
		PagingResultModel<UserModel> result = new PagingResultModel<UserModel>();
		result.setData(data);
		result.setPage(page);
		result.setSize(pageRequest.getPageSize());
		result.setTotalPages(users.getTotalPages());
		result.setTotalSize(users.getTotalElements());
		return result;
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ListResultModel<CompanyModel> findBy(
			// @formatter:off
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "abbrName", required = false) String abbrName,
			@RequestParam(name = "status", required = false) String status) {
		// @formatter:on

		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());

		EnumSet<CompanyStatus> statuses = EnumSet.noneOf(CompanyStatus.class);
		if (status != null) {
			statuses.add(CompanyStatus.valueOf(status));
		}
		CompanySearchParams params = new CompanySearchParams();
		params.setAbbrName(abbrName);
		params.setName(name);
		params.setStatuses(statuses);
		List<Company> companies = getCompanyService().getCompanyRepository().findCompanies(params);
		List<CompanyModel> data = new ArrayList<>();
		companies.forEach(company -> {
			if (hasPrivilege(accessKey::canRead, company)) {
				data.add(toCompanyModel(company));
			}
		});
		return new ListResultModel<CompanyModel>().withSize(data.size()).withData(data);
	}

	@ApiOperation(value = "create company")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public HidModel createCompany(
			@ApiParam(value = "create company model", required = true) @RequestBody(required = false) CreateCompanyModel body) {

		CreateCompanyModel model = JsonUtils.fromJson(getApiPayload(), CreateCompanyModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getParentCompanyHid(), "parentCompanyHid is empty");

		AccessKey accessKey = validateCanWriteCompanyWithHid(model.getParentCompanyHid());

		Company company = getCompanyService().create(fromCreateCompanyModel(model), accessKey.getId());
		return new HidModel().withHid(company.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "update company")
	@RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
	public HidModel updateCompany(
			@ApiParam(value = "company hid", required = true) @PathVariable(value = "hid", required = true) String hid,
			@ApiParam(value = "update company model", required = true) @RequestBody(required = false) UpdateCompanyModel body) {

		UpdateCompanyModel model = JsonUtils.fromJson(getApiPayload(), UpdateCompanyModel.class);
		Assert.notNull(model, "model is null");

		Company company = getCoreCacheService().findCompanyByHid(hid);
		Assert.notNull(company, "company is not found");

		AccessKey accessKey = validateCanWriteCompanyWithHid(company.getHid());

		company = getCompanyService().update(populateCompany(company, model), accessKey.getId());
		return new HidModel().withHid(company.getHid()).withMessage("OK");
	}

	AccessKey validateCanReadCompanyWithHid(String hid) {
		String method = "validateCanReadCompanyWithHid";
		Company company = getCoreCacheService().findCompanyByHid(hid);
		Assert.notNull(company, "invalid company hid");
		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());
		if (!StringUtils.equals(accessKey.getCompanyId(), company.getId())) {
			logWarn(method, "companyId mismatched!");
		}
		if (!hasPrivilege(accessKey::canRead, company)) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	AccessKey validateCanWriteCompanyWithHid(String hid) {
		String method = "validateCanWriteCompanyWithHid";
		Company company = getCoreCacheService().findCompanyByHid(hid);
		Assert.notNull(company, "invalid company hid");
		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());
		if (!StringUtils.equals(accessKey.getCompanyId(), company.getId())) {
			logWarn(method, "companyId mismatched!");
		}
		if (!hasPrivilege(accessKey::canWrite, company)) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}
}

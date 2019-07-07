package com.arrow.pegasus.api;

import java.time.Instant;
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

import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.pegasus.client.model.CompanyChangeModel;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.repo.params.CompanySearchParams;
import com.arrow.pegasus.repo.params.SubscriptionSearchParams;

@RestController(value = "localPegasusCompanyApi")
@RequestMapping("/api/v1/local/pegasus/companies")
public class CompanyApi extends BaseApiAbstract {

	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<Company> findAll() {
		return getCompanyService().getCompanyRepository().findAll();
	}

	@RequestMapping(path = "/{companyId}/subscriptions/count", method = RequestMethod.GET)
	public Long getSubscriptionCount(@PathVariable(name = "companyId", required = true) String companyId,
			@RequestParam(name = "createdBefore", required = false) String createdBefore,
			@RequestParam(name = "enabled", required = false) String enabled) {
		Instant dateParam = createdBefore != null ? Instant.parse(createdBefore) : null;
		Boolean enabledParam = enabled != null ? Boolean.valueOf(enabled) : null;
		SubscriptionSearchParams params = new SubscriptionSearchParams().addCompanyIds(companyId)
				.createdBefore(dateParam).withEnabled(enabledParam);
		return getSubscriptionService().getSubscriptionRepository().findSubscriptionCount(params);
	}

	@RequestMapping(path = "", method = RequestMethod.PUT)
	public Company update(@RequestBody(required = false) CompanyChangeModel body) {
		CompanyChangeModel model = JsonUtils.fromJson(getApiPayload(), CompanyChangeModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getWho(), "who is empty");
		Assert.notNull(model.getCompany(), "company is null");
		Assert.hasText(model.getCompany().getName(), "name is empty");
		Assert.hasText(model.getCompany().getAbbrName(), "abbrName is empty");
		Assert.notNull(model.getCompany().getPasswordPolicy(), "passwordPolicy is null");

		Company company = getCoreCacheService().findCompanyById(model.getCompany().getId());
		Assert.notNull(company, "company not found");

		company = getCompanyService().update(model.getCompany(), model.getWho());
		return company;
	}

	@RequestMapping(path = "{id}", method = RequestMethod.DELETE)
	public Long delete(@PathVariable(name = "id", required = true) String id) {
		Company company = getCompanyService().getCompanyRepository().findById(id).orElse(null);
		Assert.notNull(company, "company not found");
		return getCompanyService().deleteCompany(id, getAccessKey().getId());
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public Company findById(@PathVariable(name = "id", required = true) String id) {
		Company company = getCompanyService().getCompanyRepository().findById(id).orElse(null);
		Assert.notNull(company, "company not found");
		return company;
	}

	@RequestMapping(path = "/find", method = RequestMethod.POST)
	public List<Company> findCompanies(@RequestBody(required = false) CompanySearchParams body) {
		CompanySearchParams params = JsonUtils.fromJson(getApiPayload(), CompanySearchParams.class);
		Assert.notNull(params, "search criteria is missing");
		return getCompanyService().getCompanyRepository().findCompanies(params);
	}

	@RequestMapping(path = "/find/pages", method = RequestMethod.POST)
	public PagingResultModel<Company> findCompanies(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "" + DEFAULT_PAGE_SIZE) int size,
			@RequestParam(name = "sortDirection", required = false) String[] sortDirection,
			@RequestParam(name = "sortProperty", required = false) String[] sortProperty,
			@RequestBody(required = false) CompanySearchParams body) {
		CompanySearchParams params = JsonUtils.fromJson(getApiPayload(), CompanySearchParams.class);
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
		Page<Company> companies = getCompanyService().getCompanyRepository().findCompanies(pageRequest, params);
		PagingResultModel<Company> result = new PagingResultModel<>();
		result.withPage(companies.getNumber()).withTotalPages(companies.getTotalPages())
				.withTotalSize(companies.getTotalElements());
		result.withData(companies.getContent()).withSize(companies.getSize());
		return result;
	}
}

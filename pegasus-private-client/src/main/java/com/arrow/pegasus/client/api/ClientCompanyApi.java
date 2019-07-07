package com.arrow.pegasus.client.api;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.pegasus.client.model.CompanyChangeModel;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.repo.params.CompanySearchParams;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class ClientCompanyApi extends ClientApiAbstract {
	private static final String COMPANIES_ROOT_URL = WEB_SERVICE_ROOT_URL + "/companies";

	private static final TypeReference<PagingResultModel<Company>> COMPANY_PAGING_RESULT_TYPE_REF = new TypeReference<PagingResultModel<Company>>() {
	};
	private static final TypeReference<List<Company>> COMPANY_LIST_TYPE_REF = new TypeReference<List<Company>>() {
	};

	public List<Company> findAll() {
		String method = "findAll";
		try {
			URI uri = buildUri(COMPANIES_ROOT_URL);
			List<Company> result = execute(new HttpGet(uri), new TypeReference<List<Company>>() {
			});
			logDebug(method, "found %d companies", result.size());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public long getSubscriptionCount(String companyId, Instant createdBefore, Boolean enabled) {
		try {
			URIBuilder uriBuilder = new URIBuilder(
			        buildUri(COMPANIES_ROOT_URL + "/" + companyId + "/subscriptions/count"));
			if (createdBefore != null) {
				uriBuilder.addParameter("createdBefore", createdBefore.toString());
			}
			if (enabled != null) {
				uriBuilder.addParameter("enabled", enabled.toString());
			}
			return execute(new HttpGet(uriBuilder.build()), Long.class);
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Company update(Company company, String who) {
		String method = "update";
		try {
			URI uri = buildUri(COMPANIES_ROOT_URL);
			CompanyChangeModel model = new CompanyChangeModel().withCompany(company).withWho(who);
			Company result = execute(new HttpPut(uri), JsonUtils.toJson(model), Company.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}

	}

	public Long delete(String id) {
		String method = "delete";
		try {
			URI uri = buildUri(COMPANIES_ROOT_URL + "/" + id);
			Long result = execute(new HttpDelete(uri), Long.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %d", result);
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Company findById(String id) {
		String method = "findById";
		try {
			URI uri = buildUri(COMPANIES_ROOT_URL + "/" + id);
			Company result = execute(new HttpGet(uri), Company.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", result);
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public List<Company> findCompanies(CompanySearchParams params) {
		Assert.notNull(params, "params is null");
		String method = "findCompanies";
		try {
			URI uri = buildUri(COMPANIES_ROOT_URL + "/find");
			List<Company> result = execute(new HttpPost(uri), JsonUtils.toJson(params), COMPANY_LIST_TYPE_REF);
			if (result != null && isDebugEnabled())
				logDebug(method, "found %d companies", result.size());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Page<Company> findCompanies(Pageable pageable, CompanySearchParams params) {
		Assert.notNull(params, "params is null");
		Assert.notNull(pageable, "pageable is null");
		String method = "findCompanies";
		try {
			URIBuilder uriBuilder = new URIBuilder(buildUri(COMPANIES_ROOT_URL + "/find/pages"));
			uriBuilder.addParameter("page", String.valueOf(pageable.getPageNumber()));
			uriBuilder.addParameter("size", String.valueOf(pageable.getPageSize()));
			Sort sort = pageable.getSort();
			if (sort != null) {
				sort.forEach(order -> {
					uriBuilder.addParameter("sortDirection", order.getDirection().toString());
					uriBuilder.addParameter("sortProperty", order.getProperty());
				});
			}
			URI uri = uriBuilder.build();
			PagingResultModel<Company> model = execute(new HttpPost(uri), JsonUtils.toJson(params),
			        COMPANY_PAGING_RESULT_TYPE_REF);
			logDebug(method, "found %d companies of %d total", model.getData().size(), model.getTotalSize());
			Page<Company> page = new PageImpl<Company>(model.getData(), pageable, model.getTotalSize());
			return page;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}
}

package com.arrow.pegasus.client.api;

import java.net.URI;
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
import com.arrow.pegasus.client.model.ApplicationChangeModel;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.repo.params.ApplicationSearchParams;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class ClientApplicationApi extends ClientApiAbstract {
	private static final String APPLICATIONS_ROOT_URL = WEB_SERVICE_ROOT_URL + "/applications";

	private static final TypeReference<PagingResultModel<Application>> APPLICATION_PAGING_RESULT_TYPE_REF = new TypeReference<PagingResultModel<Application>>() {
	};
	private static final TypeReference<List<Application>> APPLICATION_LIST_TYPE_REF = new TypeReference<List<Application>>() {
	};

	public Application findByCode(String code) {
		String method = "findByCode";
		try {
			URI uri = buildUri(APPLICATIONS_ROOT_URL + "/codes/" + code);
			Application result = execute(new HttpGet(uri), Application.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public List<Application> findAll() {
		try {
			URI uri = buildUri(APPLICATIONS_ROOT_URL);
			List<Application> result = execute(new HttpGet(uri), new TypeReference<List<Application>>() {
			});
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Application create(Application application, String who) {
		String method = "create";
		try {
			URI uri = buildUri(APPLICATIONS_ROOT_URL);
			ApplicationChangeModel model = new ApplicationChangeModel().withApplication(application).withWho(who);
			Application result = execute(new HttpPost(uri), JsonUtils.toJson(model), Application.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Application update(Application application, String who) {
		String method = "update";
		try {
			URI uri = buildUri(APPLICATIONS_ROOT_URL);
			ApplicationChangeModel model = new ApplicationChangeModel().withApplication(application).withWho(who);
			Application result = execute(new HttpPut(uri), JsonUtils.toJson(model), Application.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public List<Application> findAllByApplicationEngineId(String applicationEngineId) {
		try {
			URI uri = buildUri(APPLICATIONS_ROOT_URL + "/application-engines/" + applicationEngineId);
			List<Application> result = execute(new HttpGet(uri), new TypeReference<List<Application>>() {
			});
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Application findById(String id) {
		String method = "findById";
		try {
			URI uri = buildUri(APPLICATIONS_ROOT_URL + "/ids/" + id);
			Application result = execute(new HttpGet(uri), Application.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Application findByHid(String hid) {
		String method = "findByHid";
		try {
			URI uri = buildUri(APPLICATIONS_ROOT_URL + "/hids/" + hid);
			Application result = execute(new HttpGet(uri), Application.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public List<Application> findByProductIdAndEnabled(String productId, boolean enabled) {
		try {
			URI uri = buildUri(APPLICATIONS_ROOT_URL + "/products/" + productId + "/enabled/" + enabled);
			List<Application> result = execute(new HttpGet(uri), new TypeReference<List<Application>>() {
			});
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public List<Application> findByProductExtensionIdAndEnabled(String productExtensionId, boolean enabled) {
		try {
			URI uri = buildUri(
			        APPLICATIONS_ROOT_URL + "/product-extensions/" + productExtensionId + "/enabled/" + enabled);
			List<Application> result = execute(new HttpGet(uri), new TypeReference<List<Application>>() {
			});
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Long delete(String id) {
		String method = "delete";
		try {
			URI uri = buildUri(APPLICATIONS_ROOT_URL + "/" + id);
			Long result = execute(new HttpDelete(uri), Long.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %d", result);
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public List<Application> findApplications(ApplicationSearchParams params) {
		Assert.notNull(params, "params is null");
		String method = "findApplications";
		try {
			URI uri = buildUri(APPLICATIONS_ROOT_URL + "/find");
			List<Application> result = execute(new HttpPost(uri), JsonUtils.toJson(params), APPLICATION_LIST_TYPE_REF);
			if (result != null && isDebugEnabled())
				logDebug(method, "found %d applications", result.size());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Page<Application> findApplications(Pageable pageable, ApplicationSearchParams params) {
		Assert.notNull(params, "params is null");
		Assert.notNull(pageable, "pageable is null");
		String method = "findApplications";
		try {
			URIBuilder uriBuilder = new URIBuilder(buildUri(APPLICATIONS_ROOT_URL + "/find/pages"));
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
			PagingResultModel<Application> model = execute(new HttpPost(uri), JsonUtils.toJson(params),
			        APPLICATION_PAGING_RESULT_TYPE_REF);
			logDebug(method, "found %d applications of %d total", model.getData().size(), model.getTotalSize());
			Page<Application> page = new PageImpl<Application>(model.getData(), pageable, model.getTotalSize());
			return page;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}
}

package com.arrow.pegasus.client.api;

import java.net.URI;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.pegasus.client.model.PrivilegeChangeModel;
import com.arrow.pegasus.data.security.Privilege;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class ClientPrivilegeApi extends ClientApiAbstract {
	private static final String PRIVILEGES_ROOT_URL = WEB_SERVICE_ROOT_URL + "/privileges";

	public List<Privilege> findByProductIdAndEnabled(String productId, boolean enabled) {
		try {
			URI uri = buildUri(PRIVILEGES_ROOT_URL + "/products/" + productId + "/enabled/" + enabled);
			List<Privilege> result = execute(new HttpGet(uri), new TypeReference<List<Privilege>>() {
			});
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Page<Privilege> findAll(PageRequest pageRequest) {
		Assert.notNull(pageRequest, "pageRequest is null");
		try {
			URIBuilder uriBuilder = new URIBuilder(buildUri(PRIVILEGES_ROOT_URL));
			uriBuilder.addParameter("size", String.valueOf(pageRequest.getPageSize())).addParameter("page",
			        String.valueOf(pageRequest.getPageNumber()));
			Sort sort = pageRequest.getSort();
			if (sort != null) {
				sort.forEach(order -> uriBuilder.addParameter("sortDirection", order.getDirection().toString())
				        .addParameter("sortProperty", order.getProperty()));
			}
			PagingResultModel<Privilege> result = execute(new HttpGet(uriBuilder.build()),
			        new TypeReference<PagingResultModel<Privilege>>() {
			        });
			return new PageImpl<Privilege>(result.getData(), pageRequest, result.getTotalSize());
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Privilege findById(String id) {
		String method = "findById";
		try {
			URI uri = buildUri(PRIVILEGES_ROOT_URL + "/ids/" + id);
			Privilege result = execute(new HttpGet(uri), Privilege.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, systemName: %s", result.getId(), result.getHid(),
				        result.getSystemName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Privilege create(Privilege privilege, String who) {
		String method = "create";
		try {
			URI uri = buildUri(PRIVILEGES_ROOT_URL);
			PrivilegeChangeModel model = new PrivilegeChangeModel().withPrivilege(privilege).withWho(who);
			Privilege result = execute(new HttpPost(uri), JsonUtils.toJson(model), Privilege.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, systemName: %s", result.getId(), result.getHid(),
				        result.getSystemName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Privilege update(Privilege privilege, String who) {
		String method = "update";
		try {
			URI uri = buildUri(PRIVILEGES_ROOT_URL);
			PrivilegeChangeModel model = new PrivilegeChangeModel().withPrivilege(privilege).withWho(who);
			Privilege result = execute(new HttpPut(uri), JsonUtils.toJson(model), Privilege.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, systemName: %s", result.getId(), result.getHid(),
				        result.getSystemName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}
}

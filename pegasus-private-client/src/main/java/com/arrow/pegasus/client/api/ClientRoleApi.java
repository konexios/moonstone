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
import com.arrow.pegasus.client.model.RoleChangeModel;
import com.arrow.pegasus.data.security.Role;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class ClientRoleApi extends ClientApiAbstract {
	private static final String ROLES_ROOT_URL = WEB_SERVICE_ROOT_URL + "/roles";

	public List<Role> findAll() {
		try {
			URI uri = buildUri(ROLES_ROOT_URL);
			List<Role> result = execute(new HttpGet(uri), new TypeReference<List<Role>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Role findById(String roleId) {
		Assert.hasText(roleId, "roleId is empty");
		String method = "findById";
		try {
			URI uri = buildUri(ROLES_ROOT_URL + "/" + roleId);
			Role role = execute(new HttpGet(uri), Role.class);
			if (role != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", role.getId(), role.getHid(), role.getName());
			return role;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Role findByNameAndApplicationId(String name, String applicationId) {
		Assert.hasText(name, "name is empty");
		Assert.hasText(applicationId, "applicationId is empty");
		String method = "findByNameAndApplicationId";
		try {
			URI uri = new URIBuilder(buildUri(ROLES_ROOT_URL + "/applications/" + applicationId + "/names"))
			        .addParameter("name", name).build();
			Role role = execute(new HttpGet(uri), Role.class);
			if (role != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", role.getId(), role.getHid(), role.getName());
			return role;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public List<Role> findByApplicationIdAndEnabled(String applicationId, boolean enabled) {
		Assert.hasText(applicationId, "applicationId is empty");
		try {
			URI uri = buildUri(
			        ROLES_ROOT_URL + "/applications/" + applicationId + "/enabled/" + String.valueOf(enabled));
			List<Role> result = execute(new HttpGet(uri), new TypeReference<List<Role>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public List<Role> findByEnabled(boolean enabled) {
		try {
			URI uri = buildUri(ROLES_ROOT_URL + "/enabled/" + String.valueOf(enabled));
			List<Role> result = execute(new HttpGet(uri), new TypeReference<List<Role>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Page<Role> findAllByApplicationId(PageRequest pageRequest, String applicationId) {
		Assert.notNull(pageRequest, "pageRequest is null");
		String method = "findAllByApplicationId";
		try {
			URIBuilder uriBuilder = new URIBuilder(buildUri(ROLES_ROOT_URL + "/applications"));
			uriBuilder.addParameter("applicationId", applicationId);
			uriBuilder.addParameter("size", String.valueOf(pageRequest.getPageSize())).addParameter("page",
			        String.valueOf(pageRequest.getPageNumber()));
			Sort sort = pageRequest.getSort();
			if (sort != null) {
				sort.forEach(order -> uriBuilder.addParameter("sortDirection", order.getDirection().toString())
				        .addParameter("sortProperty", order.getProperty()));
			}
			URI uri = uriBuilder.build();
			PagingResultModel<Role> model = execute(new HttpGet(uri), new TypeReference<PagingResultModel<Role>>() {
			});
			logDebug(method, "%s", JsonUtils.toJson(model));
			Page<Role> page = new PageImpl<Role>(model.getData(), pageRequest, model.getTotalSize());
			return page;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public List<Role> findByApplicationId(String applicationId) {
		Assert.hasText(applicationId, "applicationId is empty");
		try {
			URI uri = buildUri(ROLES_ROOT_URL + "/applications/" + applicationId);
			List<Role> result = execute(new HttpGet(uri), new TypeReference<List<Role>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Role findByIdAndApplicationId(String roleId, String applicationId) {
		Assert.hasText(roleId, "roleId is empty");
		Assert.hasText(applicationId, "applicationId is empty");
		String method = "findByIdAndApplicationId";
		try {
			URI uri = buildUri(ROLES_ROOT_URL + "/" + roleId + "/applications/" + applicationId);
			Role role = execute(new HttpGet(uri), Role.class);
			if (role != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", role.getId(), role.getHid(), role.getName());
			return role;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Role create(Role role, String who) {
		String method = "create";
		try {
			URI uri = buildUri(ROLES_ROOT_URL);
			RoleChangeModel model = new RoleChangeModel().withRole(role).withWho(who);
			Role result = execute(new HttpPost(uri), JsonUtils.toJson(model), Role.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Role update(Role role, String who) {
		String method = "update";
		try {
			URI uri = buildUri(ROLES_ROOT_URL);
			RoleChangeModel model = new RoleChangeModel().withRole(role).withWho(who);
			Role result = execute(new HttpPut(uri), JsonUtils.toJson(model), Role.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}

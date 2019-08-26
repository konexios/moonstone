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

import com.arrow.pegasus.client.model.AccessKeyCreateModel;
import com.arrow.pegasus.client.model.CreateUpdateModel;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AccessPrivilege;
import com.arrow.pegasus.repo.params.AccessKeySearchParams;
import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.PagingResultModel;
import moonstone.acs.client.search.SearchCriteria;

@Component
public class ClientAccessKeyApi extends ClientApiAbstract {
	private static final String ACCESS_KEYS_ROOT_URL = WEB_SERVICE_ROOT_URL + "/accessKeys";

	public AccessKey create(String companyId, String applicationId, List<AccessPrivilege> privileges, String who) {
		return create(companyId, applicationId, null, null, privileges, who);
	}

	public AccessKey create(String companyId, String applicationId, String name, Instant expiration,
	        List<AccessPrivilege> privileges, String who) {
		return create(companyId, applicationId, null, null, name, expiration, privileges, who);
	}

	public AccessKey create(String companyId, String applicationId, String apiKey, String secretKey, Instant expiration,
	        List<AccessPrivilege> privileges, String who) {
		return create(companyId, applicationId, apiKey, secretKey, null, expiration, privileges, who);
	}

	public AccessKey create(String companyId, String applicationId, String apiKey, String secretKey, String name,
	        Instant expiration, List<AccessPrivilege> privileges, String who) {
		String method = "create";
		try {
			AccessKeyCreateModel model = new AccessKeyCreateModel();
			model.setCompanyId(companyId);
			model.setApplicationId(applicationId);
			model.setApiKey(apiKey);
			model.setSecretKey(secretKey);
			model.setName(name);
			model.setExpiration(expiration);
			model.setPrivileges(privileges);

			CreateUpdateModel<AccessKeyCreateModel> createModel = new CreateUpdateModel<AccessKeyCreateModel>(who,
			        model);
			URI uri = buildUri(ACCESS_KEYS_ROOT_URL);
			AccessKey result = execute(new HttpPost(uri), JsonUtils.toJson(createModel), AccessKey.class);
			logDebug(method, "id: %s, hid: %s", result.getId(), result.getHid());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public AccessKey update(AccessKey accessKey, String who) {
		Assert.notNull(accessKey, "accessKey is null");
		String method = "update";
		try {
			URI uri = buildUri(ACCESS_KEYS_ROOT_URL + "/" + accessKey.getId());
			CreateUpdateModel<AccessKey> model = new CreateUpdateModel<AccessKey>(who, accessKey);
			AccessKey result = execute(new HttpPut(uri), JsonUtils.toJson(model), AccessKey.class);
			logDebug(method, "id: %s, hid: %s", result.getId(), result.getHid());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public AccessKey findById(String accessKeyId) {
		Assert.hasText(accessKeyId, "accessKeyId is empty");
		String method = "findById";
		try {
			URI uri = buildUri(ACCESS_KEYS_ROOT_URL + "/" + accessKeyId);
			AccessKey result = execute(new HttpGet(uri), AccessKey.class);
			logDebug(method, "id: %s, hid: %s", result.getId(), result.getHid());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public AccessKey findOwnerKey(String pri) {
		Assert.hasText(pri, "pri is empty");
		String method = "findOwnerKey";
		try {
			URI uri = buildUri(ACCESS_KEYS_ROOT_URL + "/owner/" + pri);
			AccessKey result = execute(new HttpGet(uri), AccessKey.class);
			if (result == null)
				logDebug(method, "OwnerAccessKey not found, pri=" + pri);
			else
				logDebug(method, "id: %s, hid: %s", result.getId(), result.getHid());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Page<AccessKey> findAccessKeys(Pageable pageable, AccessKeySearchParams params) {
		Assert.notNull(pageable, "pageable is null");
		String method = "findAccessKeys";
		try {
			URIBuilder uriBuilder = new URIBuilder(buildUri(ACCESS_KEYS_ROOT_URL + "/find"));
			uriBuilder.addParameter("page", String.valueOf(pageable.getPageNumber())).addParameter("size",
			        String.valueOf(pageable.getPageSize()));
			Sort sort = pageable.getSort();
			if (sort != null) {
				sort.forEach(order -> uriBuilder.addParameter("sortDirection", order.getDirection().toString())
				        .addParameter("sortProperty", order.getProperty()));
			}
			URI uri = uriBuilder.build();
			PagingResultModel<AccessKey> model = execute(new HttpPost(uri), JsonUtils.toJson(params),
			        new TypeReference<PagingResultModel<AccessKey>>() {
			        });
			logDebug(method, "found %d access keys of %d total", model.getData().size(), model.getTotalSize());
			Page<AccessKey> page = new PageImpl<AccessKey>(model.getData(), pageable, model.getTotalSize());
			return page;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public void removePriFromAccessKeys(String pri, String who) {
		Assert.hasText(pri, "pri is empty");
		try {
			SearchCriteria criteria = new SearchCriteria() {
				{
					this.simpleCriteria.put("who", who);
				}
			};
			URI uri = buildUri(ACCESS_KEYS_ROOT_URL + "/pri/" + pri, criteria);
			execute(new HttpDelete(uri), criteria, new TypeReference<Void>() {
			});
		} catch (Throwable t) {
			throw handleException(t);
		}
	}
}

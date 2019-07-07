package com.arrow.rhea.client.api;

import java.net.URI;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.rhea.client.model.RTURequestModel;
import com.arrow.rhea.client.search.RTURequestSearchCriteria;
import com.arrow.rhea.data.RTURequest;
import com.fasterxml.jackson.core.type.TypeReference;

@Component("RheaClientRTURequestApi")
public class ClientRTURequestApi extends ClientApiAbstract {
	private static final String RTU_REQUESTS_ROOT_URL = WEB_SERVICE_ROOT_URL + "/rtu-requests";

	public List<RTURequest> findAll() {
		try {
			URI uri = buildUri(RTU_REQUESTS_ROOT_URL);
			List<RTURequest> result = execute(new HttpGet(uri), new TypeReference<List<RTURequest>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public RTURequest create(RTURequest rtuRequest, String who) {
		try {
			URI uri = buildUri(RTU_REQUESTS_ROOT_URL);
			RTURequestModel model = new RTURequestModel().withModel(rtuRequest).withWho(who);
			RTURequest result = execute(new HttpPost(uri), JsonUtils.toJson(model), RTURequest.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public RTURequest update(RTURequest rtuRequest, String who) {
		try {
			URI uri = buildUri(RTU_REQUESTS_ROOT_URL);
			RTURequestModel model = new RTURequestModel().withModel(rtuRequest).withWho(who);
			RTURequest result = execute(new HttpPut(uri), JsonUtils.toJson(model), RTURequest.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public RTURequest findById(String id) {
		try {
			URI uri = buildUri(RTU_REQUESTS_ROOT_URL + "/ids/" + id);
			RTURequest result = execute(new HttpGet(uri), RTURequest.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public List<RTURequest> findAll(String[] companyIds, String[] statuses, 
			String[] softwareReleaseIds) {
		try {
			RTURequestSearchCriteria criteria = new RTURequestSearchCriteria().withCompanyIds(companyIds)
			        .withStatuses(statuses)
			        .withSoftwareReleaseIds(softwareReleaseIds);
			URI uri = buildUri(RTU_REQUESTS_ROOT_URL, criteria);
			List<RTURequest> result = execute(new HttpGet(uri), criteria,
			        new TypeReference<List<RTURequest>>() {
			        });
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Page<RTURequest> findAll(PageRequest pageRequest, String[] companyIds, String[] statuses, 
			String[] softwareReleaseIds) {
		try {
			RTURequestSearchCriteria criteria = new RTURequestSearchCriteria().withCompanyIds(companyIds)
					.withStatuses(statuses)			       
			        .withSoftwareReleaseIds(softwareReleaseIds).withCompanyIds(companyIds)
			        .withPage(pageRequest.getPageNumber()).withSize(pageRequest.getPageSize());
			Sort sort = pageRequest.getSort();
			if (sort != null) {
				sort.forEach(order -> criteria.withSortDirection(order.getDirection().toString())
				        .withSortProperty(order.getProperty()));
			}

			URI uri = buildUri(RTU_REQUESTS_ROOT_URL + "/pages", criteria);

			PagingResultModel<RTURequest> model = execute(new HttpGet(uri), criteria,
			        new TypeReference<PagingResultModel<RTURequest>>() {
			        });
			Page<RTURequest> result = new PageImpl<RTURequest>(model.getData(), pageRequest,
			        model.getTotalSize());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}

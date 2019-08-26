/*******************************************************************************
 * Copyright (c) 2018 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *******************************************************************************/
package moonstone.acs.client.api;

import java.net.URI;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acs.JsonUtils;
import moonstone.acs.client.api.ApiConfig;
import moonstone.acs.client.model.AccessKeyModel;
import moonstone.acs.client.model.CompanyModel;
import moonstone.acs.client.model.HidModel;
import moonstone.acs.client.model.ListResultModel;
import moonstone.acs.client.model.PagingResultModel;
import moonstone.acs.client.model.SubscriptionModel;
import moonstone.acs.client.model.UserModel;
import moonstone.acs.client.search.UserSearchCriteria;

public final class CompanyApi extends AcsApiAbstract {
	private static final String COMPANIES_ROOT_URL = WEB_SERVICE_ROOT_URL + "/companies";

	// instantiation is expensive for these objects
	private TypeReference<ListResultModel<AccessKeyModel>> accessKeyModelTypeRef;
	private TypeReference<ListResultModel<SubscriptionModel>> subscriptionModelTypeRef;
	private TypeReference<PagingResultModel<UserModel>> userModelTypeRef;

	CompanyApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	public CompanyModel findByHid(String hid) {
		String method = "findByHid";
		try {
			URI uri = buildUri(String.format("%s/%s", COMPANIES_ROOT_URL, hid));
			CompanyModel result = execute(new HttpGet(uri), CompanyModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public HidModel createCompany(CompanyModel model) {
		String method = "createCompany";
		try {
			URI uri = buildUri(COMPANIES_ROOT_URL);
			HidModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), HidModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public HidModel updateCompany(String hid, CompanyModel model) {
		String method = "createCompany";
		try {
			URI uri = buildUri(String.format("%s/%s", COMPANIES_ROOT_URL, hid));
			HidModel result = execute(new HttpPut(uri), JsonUtils.toJson(model), HidModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public ListResultModel<AccessKeyModel> findAccessKeysByHid(String hid) {
		String method = "findAllBy";
		try {
			URI uri = buildUri(String.format("%s/%s/access-keys", COMPANIES_ROOT_URL, hid));
			ListResultModel<AccessKeyModel> result = execute(new HttpGet(uri), getAccessKeyModelTypeRef());
			log(method, result);
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public ListResultModel<SubscriptionModel> findSubscriptionsByHid(String hid) {
		String method = "findAllBy";
		try {
			URI uri = buildUri(String.format("%s/%s/subscriptions", COMPANIES_ROOT_URL, hid));
			ListResultModel<SubscriptionModel> result = execute(new HttpGet(uri), getSubscriptionModelTypeRef());
			log(method, result);
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public PagingResultModel<UserModel> findUsersByHidAndCriteria(String hid, UserSearchCriteria criteria) {
		String method = "findUsersByHidAndCriteria";
		try {
			URI uri = buildUri(String.format("%s/%s/users", COMPANIES_ROOT_URL, hid), criteria);
			PagingResultModel<UserModel> result = execute(new HttpGet(uri), getUserModelTypeRef());
			log(method, result);
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	synchronized TypeReference<ListResultModel<AccessKeyModel>> getAccessKeyModelTypeRef() {
		return accessKeyModelTypeRef != null ? accessKeyModelTypeRef
				: (accessKeyModelTypeRef = new TypeReference<ListResultModel<AccessKeyModel>>() {
				});
	}

	synchronized TypeReference<ListResultModel<SubscriptionModel>> getSubscriptionModelTypeRef() {
		return subscriptionModelTypeRef != null ? subscriptionModelTypeRef
				: (subscriptionModelTypeRef = new TypeReference<ListResultModel<SubscriptionModel>>() {
				});
	}

	synchronized TypeReference<PagingResultModel<UserModel>> getUserModelTypeRef() {
		return userModelTypeRef != null ? userModelTypeRef
				: (userModelTypeRef = new TypeReference<PagingResultModel<UserModel>>() {
				});
	}
}

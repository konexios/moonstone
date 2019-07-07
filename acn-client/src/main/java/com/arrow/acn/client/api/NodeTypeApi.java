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
package com.arrow.acn.client.api;

import java.net.URI;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import com.arrow.acn.client.AcnClientException;
import com.arrow.acn.client.model.NodeTypeModel;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.api.ApiConfig;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.ListResultModel;
import com.fasterxml.jackson.core.type.TypeReference;

public final class NodeTypeApi extends ApiAbstract {
	private final String NODE_TYPES_BASE_URL = API_BASE + "/nodes/types";
	private final String SPECIFIC_NODE_TYPE_URL = NODE_TYPES_BASE_URL + "/{hid}";

	private TypeReference<ListResultModel<NodeTypeModel>> nodeTypeModelTypeRef;

	NodeTypeApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	/**
	 * Sends GET request to obtain parameters of all available node types
	 *
	 * @return list of {@link NodeTypeModel} containing type parameters
	 *
	 * @throws AcnClientException if request failed
	 */
	public ListResultModel<NodeTypeModel> listExistingNodeTypes() {
		String method = "listExistingNodeTypes";
		try {
			URI uri = buildUri(NODE_TYPES_BASE_URL);
			ListResultModel<NodeTypeModel> result = execute(new HttpGet(uri), getNodeTypeModelTypeRef());
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends POST request to create new node type according to {@code model} passed
	 * 
	 * @param model {@link NodeTypeModel} representing parameters of node type to be
	 *              created
	 *
	 * @return {@link HidModel} containing {@code hid} of node type created
	 *
	 * @throws AcnClientException if request failed
	 */
	public HidModel createNewNodeType(NodeTypeModel model) {
		String method = "createNewNodeType";
		try {
			URI uri = buildUri(NODE_TYPES_BASE_URL);
			HidModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), HidModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends PUT request to update existing node type according to {@code model}
	 * passed
	 *
	 * @param hid   {@link String} representing {@code hid} of node type to be
	 *              updated
	 * @param model {@link NodeTypeModel} representing node type parameters to be
	 *              updated
	 *
	 * @return {@link HidModel} containing {@code hid} of node type updated
	 *
	 * @throws AcnClientException if request failed
	 */
	public HidModel updateExistingNodeType(String hid, NodeTypeModel model) {
		String method = "updateExistingNodeType";
		try {
			URI uri = buildUri(SPECIFIC_NODE_TYPE_URL.replace("{hid}", hid));
			HidModel result = execute(new HttpPut(uri), JsonUtils.toJson(model), HidModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	private synchronized TypeReference<ListResultModel<NodeTypeModel>> getNodeTypeModelTypeRef() {
		return nodeTypeModelTypeRef != null ? nodeTypeModelTypeRef
				: (nodeTypeModelTypeRef = new TypeReference<ListResultModel<NodeTypeModel>>() {
				});
	}
}

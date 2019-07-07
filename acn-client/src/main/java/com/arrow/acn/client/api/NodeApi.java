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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import com.arrow.acn.client.AcnClientException;
import com.arrow.acn.client.model.NodeModel;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.api.ApiConfig;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.ListResultModel;
import com.fasterxml.jackson.core.type.TypeReference;

public final class NodeApi extends ApiAbstract {
	private static final String NODES_BASE_URL = API_BASE + "/nodes";
	private static final String SPECIFIC_NODE_URL = NODES_BASE_URL + "/{hid}";
	private static final String FIND_BY_HID_URL = SPECIFIC_NODE_URL;

	private static final Pattern PATTERN = Pattern.compile("{hid}", Pattern.LITERAL);

	private TypeReference<ListResultModel<NodeModel>> nodeModelTypeRef;

	NodeApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	/**
	 * Sends GET request to obtain parameters of node specified by its {@code hid}
	 *
	 * @param hid {@link String} representing specific node
	 *
	 * @return {@link NodeModel} containing node parameters
	 *
	 * @throws AcnClientException if request failed
	 */
	public NodeModel findByHid(String hid) {
		String method = "findByHid";
		try {
			URI uri = buildUri(PATTERN.matcher(FIND_BY_HID_URL).replaceAll(Matcher.quoteReplacement(hid)));
			NodeModel result = execute(new HttpGet(uri), NodeModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends GET request to obtain parameters of all available nodes
	 *
	 * @return list of {@link NodeModel} containing node parameters
	 *
	 * @throws AcnClientException if request failed
	 */
	public ListResultModel<NodeModel> listExistingNodes() {
		String method = "listExistingNodes";
		try {
			URI uri = buildUri(NODES_BASE_URL);
			ListResultModel<NodeModel> result = execute(new HttpGet(uri), getNodeModelTypeRef());
			logDebug(method, "size: %s", result.getSize());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends POST request to create new node according to {@code model} passed
	 * 
	 * @param model {@link NodeModel} representing parameters of node to be created
	 *
	 * @return {@link HidModel} containing {@code hid} of node created
	 *
	 * @throws AcnClientException if request failed
	 */
	public HidModel createNewNode(NodeModel model) {
		String method = "createNewNode";
		try {
			URI uri = buildUri(NODES_BASE_URL);
			HidModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), HidModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Sends PUT request to update existing node according to {@code model} passed
	 *
	 * @param hid   {@link String} representing {@code hid} of node to be updated
	 * @param model {@link NodeModel} representing node parameters to be updated
	 *
	 * @return {@link HidModel} containing {@code hid} of node updated
	 *
	 * @throws AcnClientException if request failed
	 */
	public HidModel updateExistingNode(String hid, NodeModel model) {
		String method = "updateExistingNode";
		try {
			URI uri = buildUri(SPECIFIC_NODE_URL.replace("{hid}", hid));
			HidModel result = execute(new HttpPut(uri), JsonUtils.toJson(model), HidModel.class);
			log(method, result);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	private synchronized TypeReference<ListResultModel<NodeModel>> getNodeModelTypeRef() {
		return nodeModelTypeRef != null ? nodeModelTypeRef
				: (nodeModelTypeRef = new TypeReference<ListResultModel<NodeModel>>() {
				});
	}
}

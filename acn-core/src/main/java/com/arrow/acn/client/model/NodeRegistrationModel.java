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
package com.arrow.acn.client.model;

import java.io.Serializable;

import com.arrow.acs.AcsUtils;

public class NodeRegistrationModel implements Serializable {
	private static final long serialVersionUID = 2661468449210522066L;

	private String name;
	private String description;
	private String parentNodeHid;
	private String nodeTypeHid;
	private boolean enabled = true;

	public void trim() {
		name = AcsUtils.trimToNull(name);
		description = AcsUtils.trimToNull(description);
		parentNodeHid = AcsUtils.trimToNull(parentNodeHid);
		nodeTypeHid = AcsUtils.trimToNull(nodeTypeHid);
	}

	public NodeRegistrationModel withName(String name) {
		setName(name);
		return this;
	}

	public NodeRegistrationModel withDescription(String description) {
		setDescription(description);
		return this;
	}

	public NodeRegistrationModel withParentNodeHid(String parentNodeHid) {
		setParentNodeHid(parentNodeHid);
		return this;
	}

	public NodeRegistrationModel withNodeTypeHid(String nodeTypeHid) {
		setNodeTypeHid(nodeTypeHid);
		return this;
	}

	public NodeRegistrationModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParentNodeHid() {
		return parentNodeHid;
	}

	public void setParentNodeHid(String parentNodeHid) {
		this.parentNodeHid = parentNodeHid;
	}

	public String getNodeTypeHid() {
		return nodeTypeHid;
	}

	public void setNodeTypeHid(String nodeTypeHid) {
		this.nodeTypeHid = nodeTypeHid;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}

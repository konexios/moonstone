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

import com.arrow.acs.client.model.DefinitionModelAbstract;

public class NodeModel extends DefinitionModelAbstract<NodeModel> {
	private static final long serialVersionUID = 3412346788005924813L;

	private boolean enabled;
	private String parentNodeHid;
	private String nodeTypeHid;

	@Override
	protected NodeModel self() {
		return this;
	}

	public NodeModel withEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public NodeModel withParentNodeHid(String parentNodeHid) {
		setParentNodeHid(parentNodeHid);
		return this;
	}

	public NodeModel withNodeTypeHid(String nodeTypeHid) {
		setNodeTypeHid(nodeTypeHid);
		return this;
	}

	public String getParentNodeHid() {
		return parentNodeHid;
	}

	public void setParentNodeHid(String parentNodeHid) {
		this.parentNodeHid = parentNodeHid;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getNodeTypeHid() {
		return nodeTypeHid;
	}

	public void setNodeTypeHid(String nodeTypeHid) {
		this.nodeTypeHid = nodeTypeHid;
	}
}

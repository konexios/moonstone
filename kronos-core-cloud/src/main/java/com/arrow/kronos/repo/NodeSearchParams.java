package com.arrow.kronos.repo;

import java.time.Instant;
import java.util.Set;

public class NodeSearchParams extends KronosDocumentSearchParams {
	private static final long serialVersionUID = -7915194943392809375L;

	private Set<String> parentNodeIds;
	private Set<String> nodeTypeIds;
	private Instant createdBefore;

	public Set<String> getParentNodeIds() {
		return parentNodeIds;
	}

	public NodeSearchParams addParentNodeIds(String... parentNodeIds) {
		this.parentNodeIds = super.addValues(this.parentNodeIds, parentNodeIds);

		return this;
	}

	public Set<String> getNodeTypeIds() {
		return super.getValues(nodeTypeIds);
	}

	public NodeSearchParams addNodeTypeIds(String... nodeTypeIds) {
		this.nodeTypeIds = super.addValues(this.nodeTypeIds, nodeTypeIds);

		return this;
	}

	public Instant getCreatedBefore() {
		return createdBefore;
	}

	public void setCreatedBefore(Instant createdBefore) {
		this.createdBefore = createdBefore;
	}

	public NodeSearchParams addCreatedBefore(Instant createdBefore) {
		setCreatedBefore(createdBefore);
		return this;
	}
}

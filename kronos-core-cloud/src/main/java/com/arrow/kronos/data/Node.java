package com.arrow.kronos.data;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.arrow.pegasus.data.profile.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "node")
public class Node extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = -377685948423700020L;

	@NotBlank
	private String applicationId;
	private String parentNodeId;
	@NotBlank
	private String nodeTypeId;
	private List<DeviceAction> actions = new ArrayList<>();

	@Transient
	@JsonIgnore
	private Application refApplication;
	@Transient
	@JsonIgnore
	private Node refParentNode;
	@Transient
	@JsonIgnore
	private NodeType refNodeType;

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	public Node getRefParentNode() {
		return refParentNode;
	}

	public void setRefParentNode(Node refParentNode) {
		this.refParentNode = refParentNode;
	}

	public NodeType getRefNodeType() {
		return refNodeType;
	}

	public void setRefNodeType(NodeType refNodeType) {
		this.refNodeType = refNodeType;
	}

	public String getParentNodeId() {
		return parentNodeId;
	}

	public void setParentNodeId(String parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getNodeTypeId() {
		return nodeTypeId;
	}

	public void setNodeTypeId(String nodeTypeId) {
		this.nodeTypeId = nodeTypeId;
	}

	public List<DeviceAction> getActions() {
		return actions;
	}

	public void setActions(List<DeviceAction> actions) {
		this.actions = actions;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.NODE;
	}
}

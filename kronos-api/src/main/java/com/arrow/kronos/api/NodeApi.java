package com.arrow.kronos.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.data.Node;
import com.arrow.kronos.data.NodeType;
import com.arrow.kronos.service.NodeService;
import com.arrow.kronos.service.NodeTypeService;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AuditLog;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import moonstone.acn.client.model.NodeModel;
import moonstone.acn.client.model.NodeRegistrationModel;
import moonstone.acs.AcsLogicalException;
import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.HidModel;
import moonstone.acs.client.model.ListResultModel;

@RestController
@RequestMapping("/api/v1/kronos/nodes")
public class NodeApi extends BaseApiAbstract {

	@Autowired
	private NodeService nodeService;
	@Autowired
	private NodeTypeService nodeTypeService;

	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public NodeModel findByHid(@PathVariable String hid) {
		validateCanReadNode(hid);
		return buildNodeModel(getKronosCache().findNodeByHid(hid));
	}

	@ApiOperation(value = "list existing nodes")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ListResultModel<NodeModel> listNodes() {
		AccessKey accessKey = validateCanReadApplication(getProductSystemName());

		List<Node> nodes = nodeService.getNodeRepository().findByApplicationIdAndEnabled(accessKey.getApplicationId(),
		        true);
		List<NodeModel> data = new ArrayList<>();

		if (nodes != null) {
			nodes.forEach(node -> data.add(buildNodeModel(node)));
		}
		return new ListResultModel<NodeModel>().withData(data).withSize(data.size());
	}

	@ApiOperation(value = "create new node")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public HidModel create(
	        @ApiParam(value = "node model", required = true) @RequestBody(required = false) NodeRegistrationModel body,
	        HttpServletRequest request) {
		String method = "create";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());

		AuditLog auditLog = auditLog(method, accessKey.getApplicationId(), null, accessKey.getId(), request);

		NodeRegistrationModel model = JsonUtils.fromJson(getApiPayload(), NodeRegistrationModel.class);
		Assert.notNull(model, "model is null");
		model.trim();
		Assert.hasText(model.getName(), "name is empty");
		Assert.hasText(model.getDescription(), "description is empty");
		Assert.hasText(model.getNodeTypeHid(), "node type id is empty");
		NodeType nodeType = getValidatedNodeType(model.getNodeTypeHid(), accessKey.getApplicationId());
		String parentNodeId = null;
		if (StringUtils.hasText(model.getParentNodeHid())) {
			Node parentNode = getValidatedParentNode(model.getParentNodeHid(), null, accessKey.getApplicationId());
			parentNodeId = parentNode.getId();
		}

		Node node = new Node();
		node.setApplicationId(accessKey.getApplicationId());
		node.setDescription(model.getDescription());
		node.setEnabled(model.isEnabled());
		node.setName(model.getName());
		node.setNodeTypeId(nodeType.getId());
		node.setParentNodeId(parentNodeId);

		node = nodeService.create(node, accessKey.getId());

		auditLog.setObjectId(node.getId());
		getAuditLogService().getAuditLogRepository().doSave(auditLog, accessKey.getId());

		return new HidModel().withHid(node.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "update existing node")
	@RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
	public HidModel update(@ApiParam(value = "node hid", required = true) @PathVariable(value = "hid") String hid,
	        @ApiParam(value = "node model", required = true) @RequestBody(required = false) NodeRegistrationModel body,
	        HttpServletRequest request) {
		String method = "update";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());

		Node node = nodeService.getNodeRepository().doFindByHid(hid);
		Assert.notNull(node, "node does not exist");

		auditLog(method, node.getApplicationId(), node.getId(), accessKey.getId(), request);

		Assert.isTrue(accessKey.getApplicationId().equals(node.getApplicationId()), "applicationId mismatched!");

		NodeRegistrationModel model = JsonUtils.fromJson(getApiPayload(), NodeRegistrationModel.class);
		Assert.notNull(model, "model is null");
		model.trim();

		String message = "OK";
		node.setEnabled(model.isEnabled());
		if (!model.isEnabled()) {
			List<Node> children = nodeService.getNodeRepository()
			        .findByApplicationIdAndParentNodeId(accessKey.getApplicationId(), node.getId());
			if (children != null && !children.isEmpty()) {
				message = "Warning: disabled node has children";
			}
		}
		if (StringUtils.hasText(model.getDescription())) {
			node.setDescription(model.getDescription());
		}
		if (StringUtils.hasText(model.getName())) {
			node.setName(model.getName());
		}
		if (StringUtils.hasText(model.getNodeTypeHid())) {
			NodeType nodeType = getValidatedNodeType(model.getNodeTypeHid(), accessKey.getApplicationId());
			node.setNodeTypeId(nodeType.getId());
		}
		if (StringUtils.hasText(model.getParentNodeHid())) {
			Node parentNode = getValidatedParentNode(model.getParentNodeHid(), node.getId(),
			        accessKey.getApplicationId());
			node.setParentNodeId(parentNode.getId());
		} else {
			node.setParentNodeId(null);
		}
		node = nodeService.update(node, accessKey.getId());

		return new HidModel().withHid(node.getHid()).withMessage(message);
	}

	private NodeModel buildNodeModel(Node node) {
		Assert.notNull(node, "node is null");
		NodeModel result = buildModel(new NodeModel(), node);
		if (node.getParentNodeId() != null) {
			Node parentNode = getKronosCache().findNodeById(node.getParentNodeId());
			if (parentNode != null) {
				result.setParentNodeHid(parentNode.getHid());
			} else {
				result.setParentNodeHid("ERROR");
			}
		}
		NodeType nodeType = getKronosCache().findNodeTypeById(node.getNodeTypeId());
		if (nodeType != null) {
			result.setNodeTypeHid(nodeType.getHid());
		}
		return result;
	}

	private NodeType getValidatedNodeType(String hid, String applicationId) {
		NodeType nodeType = nodeTypeService.getNodeTypeRepository().doFindByHid(hid);
		Assert.notNull(nodeType, "node type does not exist");
		Assert.isTrue(applicationId.equals(nodeType.getApplicationId()), "node type does not match");
		Assert.isTrue(nodeType.isEnabled(), "node type is disabled");
		return nodeType;
	}

	private Node getValidatedParentNode(String hid, String nodeId, String applicationId) {
		Node parentNode = nodeService.getNodeRepository().doFindByHid(hid);
		Assert.notNull(parentNode, "parent node is not found");
		Assert.isTrue(applicationId.equals(parentNode.getApplicationId()), "parent node does not match");
		Assert.isTrue(parentNode.isEnabled(), "parent node is disabled");
		if (nodeId != null) {
			Assert.isTrue(!parentNode.getId().equals(nodeId), "invalid parent node");
			validateParentNode(hid, nodeId, applicationId);
		}
		return parentNode;
	}

	private void validateParentNode(String hid, String nodeId, String applicationId) {
		List<Node> children = nodeService.getNodeRepository().findByApplicationIdAndParentNodeId(applicationId, nodeId);
		for (Node item : children) {
			if (hid.equals(item.getHid())) {
				throw new AcsLogicalException("invalid parent node");
			}
			validateParentNode(hid, item.getId(), applicationId);
		}
	}
}

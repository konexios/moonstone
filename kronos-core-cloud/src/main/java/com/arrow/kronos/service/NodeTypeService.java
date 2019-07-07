package com.arrow.kronos.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.NodeType;
import com.arrow.kronos.repo.NodeTypeRepository;
import com.arrow.pegasus.data.AuditLogBuilder;

@Service
public class NodeTypeService extends KronosServiceAbstract {

	@Autowired
	private NodeTypeRepository nodeTypeRepository;

	public NodeTypeRepository getNodeTypeRepository() {
		return nodeTypeRepository;
	}

	public NodeType create(NodeType nodeType, String who) {
		String method = "create";

		// logical checks
		if (nodeType == null) {
			logInfo(method, "nodeType is null");
			throw new AcsLogicalException("nodeType is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// insert
		nodeType = nodeTypeRepository.doInsert(nodeType, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.NodeType.CreateNodeType)
		        .applicationId(nodeType.getApplicationId()).objectId(nodeType.getId()).by(who)
		        .parameter("name", nodeType.getName()));

		return nodeType;
	}

	public NodeType update(NodeType nodeType, String who) {
		String method = "update";

		// logical checks
		if (nodeType == null) {
			logInfo(method, "nodeType is null");
			throw new AcsLogicalException("nodeType is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// update
		nodeType = nodeTypeRepository.doSave(nodeType, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.NodeType.UpdateNodeType)
		        .applicationId(nodeType.getApplicationId()).objectId(nodeType.getId()).by(who)
		        .parameter("name", nodeType.getName()));

		// clear cache
		NodeType cachedNodeType = getKronosCache().findNodeTypeById(nodeType.getId());
		if (cachedNodeType != null) {
			getKronosCache().clearNodeType(cachedNodeType);
		}

		return nodeType;
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return nodeTypeRepository.deleteByApplicationId(applicationId);
	}
}

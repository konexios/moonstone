package com.arrow.kronos.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.repo.DeviceSearchParams;
import com.arrow.kronos.repo.GatewaySearchParams;
import com.arrow.kronos.repo.NodeRepository;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AccessPrivilege;
import com.arrow.pegasus.data.AccessPrivilege.AccessLevel;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Application;

@Service
public class NodeService extends KronosServiceAbstract {

    @Autowired
    private GatewayService gatewayService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private NodeRepository nodeRepository;

    public NodeRepository getNodeRepository() {
        return nodeRepository;
    }

    public Node create(Node node, String who) {
        String method = "create";

        // logical checks
        if (node == null) {
            logInfo(method, "node is null");
            throw new AcsLogicalException("node is null");
        }

        if (StringUtils.isEmpty(who)) {
            logInfo(method, "who is empty");
            throw new AcsLogicalException("who is empty");
        }

        // insert
        node = nodeRepository.doInsert(node, who);

        // create ownerKey
        Application application = getCoreCacheService().findApplicationById(node.getApplicationId());
        checkEnabled(application, "application");
        AccessKey ownerKey = getClientAccessKeyApi().create(application.getCompanyId(), application.getId(),
                Collections.singletonList(new AccessPrivilege(node.getPri(), AccessLevel.OWNER)), who);
        logInfo(method, "created ownerKey: %s", ownerKey.getId());

        // write audit log
        getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Node.CreateNode)
                .applicationId(node.getApplicationId()).objectId(node.getId()).by(who).parameter("name", node.getName())
                .parameter("description", node.getDescription()));

        return node;
    }

    public Node update(Node node, String who) {
        String method = "update";

        // logical checks
        if (node == null) {
            logInfo(method, "node is null");
            throw new AcsLogicalException("node is null");
        }

        if (StringUtils.isEmpty(who)) {
            logInfo(method, "who is empty");
            throw new AcsLogicalException("who is empty");
        }

        // update
        node = nodeRepository.doSave(node, who);

        // write audit log
        getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Node.UpdateNode)
                .applicationId(node.getApplicationId()).objectId(node.getId()).by(who).parameter("name", node.getName())
                .parameter("description", node.getDescription()));

        // clear cache
        Node cachedNode = getKronosCache().findNodeById(node.getId());
        if (cachedNode != null) {
            getKronosCache().clearNode(cachedNode);
        }

        return node;
    }

    public Node populateRefs(Node node) {

        if (node == null)
            return node;

        if (!StringUtils.isEmpty(node.getParentNodeId())) {
            node.setRefParentNode(getKronosCache().findNodeById(node.getParentNodeId()));
            Assert.notNull(node.getRefParentNode(), "refParentNode is null");
        }

        node.setRefNodeType(getKronosCache().findNodeTypeById(node.getNodeTypeId()));
        Assert.notNull(node.getRefNodeType(), "refNodeType is null");

        return node;
    }

    public Node createDeviceAction(Node node, DeviceAction deviceAction, String who) {
        Assert.notNull(node, "node is null");
        Assert.notNull(deviceAction, "deviceAction is null");
        Assert.notNull(deviceAction.getDeviceActionTypeId(), "deviceActionTypeId is null");
        Assert.hasText(who, "who is empty");

        node.getActions().add(deviceAction);

        node = nodeRepository.doSave(node, who);

        getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Device.CreateDeviceAction)
                .applicationId(node.getApplicationId()).productName(ProductSystemNames.KRONOS).objectId(node.getId())
                .by(who).parameter("deviceActionIndex", String.valueOf(node.getActions().size() - 1))
                .parameter("deviceActionTypeId", deviceAction.getDeviceActionTypeId()));

        getKronosCache().clearNode(node);

        return node;
    }

    public Node updateDeviceAction(Node node, int index, DeviceAction deviceAction, String who) {
        Assert.notNull(node, "node is null");
        Assert.notNull(deviceAction, "deviceAction is null");
        Assert.notNull(deviceAction.getDeviceActionTypeId(), "deviceActionTypeId is null");
        Assert.isTrue(index >= 0 && index < node.getActions().size(), "deviceActionIndex is out of bounds");
        Assert.hasText(who, "who is empty");

        node.getActions().set(index, deviceAction);

        node = nodeRepository.doSave(node, who);

        getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Device.UpdateDeviceAction)
                .applicationId(node.getApplicationId()).productName(ProductSystemNames.KRONOS).objectId(node.getId())
                .by(who).parameter("deviceActionIndex", String.valueOf(index))
                .parameter("deviceActionTypeId", deviceAction.getDeviceActionTypeId()));

        getKronosCache().clearNode(node);

        return node;
    }

    public Node deleteDeviceActions(Node node, List<Integer> actionIndices, String who) {
        Assert.notNull(node, "node is null");
        Assert.notNull(actionIndices, "actionIndices is null");
        Assert.hasText(who, "who is empty");

        List<DeviceAction> actions = node.getActions();
        List<DeviceAction> deletedActions = new ArrayList<>(actionIndices.size());
        for (int i = 0; i < actions.size(); i++) {
            if (actionIndices.contains(i)) {
                deletedActions.add(actions.get(i));
            }
        }
        actions.removeAll(deletedActions);

        node = nodeRepository.doSave(node, who);

        getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Device.DeleteDeviceActions)
                .applicationId(node.getApplicationId()).productName(ProductSystemNames.KRONOS).objectId(node.getId())
                .by(who).parameter("deletedActionsCount", String.valueOf(deletedActions.size())));

        getKronosCache().clearNode(node);

        return node;
    }

    public int deleteByApplicationId(String applicationId, String who) {
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(who, "who is empty");

        String method = "deleteByApplicationId";
        logInfo(method, "applicationId: %s, who: %s", applicationId, who);

        List<Node> nodes = nodeRepository.findByApplicationId(applicationId);
        for (Node node : nodes)
            delete(node.getId(), who);

        return nodes.size();
    }

    public void delete(String nodeId, String who) {
        Assert.hasText(nodeId, "nodeId is empty");
        Assert.hasText(who, "who is empty");

        String method = "delete";
        logInfo(method, "nodeId: %s, who: %s", nodeId, who);

        Node node = getNodeRepository().findById(nodeId).orElse(null);
        Assert.notNull(node, "Node not found! nodeId=" + nodeId);

        // gateways
        removeAssociatedGateways(nodeId, who);

        // devices
        removeAssociatedDevices(nodeId, who);

        // remove node
        nodeRepository.deleteById(nodeId);

        // clear cache
        getKronosCache().clearNode(node);
    }

    private void removeAssociatedGateways(String nodeId, String who) {
        Assert.hasText(nodeId, "nodeId is empty");
        Assert.hasText(who, "who is empty");

        String method = "removeAssociatedGateways";
        logInfo(method, "nodeId: %s, who: %s", nodeId, who);

        GatewaySearchParams params = new GatewaySearchParams();
        params.addNodeIds(nodeId);

        List<Gateway> gateways = gatewayService.getGatewayRepository().findGateways(params);
        logDebug(method, "gateways: %s", gateways.size());
        for (Gateway gateway : gateways) {
            gateway.setNodeId(null);
            gatewayService.update(gateway, who);
        }
    }

    private void removeAssociatedDevices(String nodeId, String who) {
        Assert.hasText(nodeId, "nodeId is empty");
        Assert.hasText(who, "who is empty");

        String method = "removeAssociatedDevices";
        logInfo(method, "nodeId: %s, who: %s", nodeId, who);

        DeviceSearchParams params = new DeviceSearchParams();
        params.addNodeIds(nodeId);

        List<Device> devices = deviceService.getDeviceRepository().doFindAllDevices(params);
        logDebug(method, "devices: %s", devices.size());
        for (Device device : devices) {
            device.setNodeId(null);
            deviceService.update(device, who);
        }
    }
}

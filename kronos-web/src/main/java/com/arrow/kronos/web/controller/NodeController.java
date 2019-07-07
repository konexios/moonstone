package com.arrow.kronos.web.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.data.NodeType;
import com.arrow.kronos.data.Telemetry;
import com.arrow.kronos.repo.NodeSearchParams;
import com.arrow.kronos.repo.TelemetrySearchParams;
import com.arrow.kronos.service.DeviceService;
import com.arrow.kronos.service.NodeService;
import com.arrow.kronos.service.NodeTypeService;
import com.arrow.kronos.service.TelemetryService;
import com.arrow.kronos.web.model.DeviceModels.DeviceTree;
import com.arrow.kronos.web.model.NodeModels.NodeModel;
import com.arrow.kronos.web.model.NodeModels.NodeOptionsModel;
import com.arrow.kronos.web.model.NodeModels.NodeTreeLevelModel;
import com.arrow.kronos.web.model.NodeTypeModels.NodeTypeOption;
import com.arrow.kronos.web.model.SearchFilterModels.NodeSearchFilterModel;
import com.arrow.kronos.web.model.SearchResultModels.NodeSearchResultModel;
import com.arrow.pegasus.data.profile.User;

@RestController
@RequestMapping("/api/kronos/node")
public class NodeController extends BaseControllerAbstract {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private NodeTypeService nodeTypeService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private TelemetryService telemetryService;

    @PreAuthorize("hasAuthority('KRONOS_VIEW_NODES')")
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public NodeSearchResultModel list(@RequestBody NodeSearchFilterModel searchFilter, HttpSession session) {

        // sorting & paging
        PageRequest pageRequest = new PageRequest(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
                new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

        NodeSearchParams params = new NodeSearchParams();
        params.addApplicationIds(getApplicationId(session));
        // user defined filter
        params.addNodeTypeIds(searchFilter.getNodeTypeIds());
        params.setEnabled(searchFilter.isEnabled());

        Page<Node> nodes = nodeService.getNodeRepository().findNodes(pageRequest, params);

        // convert to visual model
        List<NodeModel> nodeModels = getNodeModels(nodes);
        Page<NodeModel> result = new PageImpl<>(nodeModels, pageRequest, nodes.getTotalElements());

        return new NodeSearchResultModel(result, searchFilter);
    }

    @PreAuthorize("hasAuthority('KRONOS_VIEW_NODES')")
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public NodeTreeLevelModel list(HttpSession session) {
        return getChildNodes(getApplicationId(session), null);
    }

    @PreAuthorize("hasAuthority('KRONOS_VIEW_NODES')")
    @RequestMapping(value = "/find/{parentNodeId}", method = RequestMethod.GET)
    public NodeTreeLevelModel list(@PathVariable String parentNodeId, HttpSession session) {
        return getChildNodes(getApplicationId(session), parentNodeId);
    }

    @PreAuthorize("hasAuthority('KRONOS_VIEW_NODES')")
    @RequestMapping(value = "/nodetypes", method = RequestMethod.GET)
    public List<NodeTypeOption> getNodeTypes(HttpSession session) {
        String applicationId = getApplicationId(session);

        List<NodeType> nodeTypes = nodeTypeService.getNodeTypeRepository().findByApplicationIdAndEnabled(applicationId,
                true);

        List<NodeTypeOption> nodeTypeOptions = new ArrayList<>(nodeTypes.size());
        for (NodeType nodeType : nodeTypes) {
            nodeTypeOptions.add(new NodeTypeOption(nodeType));
        }

        nodeTypeOptions.sort(Comparator.comparing(NodeTypeOption::getName, String.CASE_INSENSITIVE_ORDER));
        return nodeTypeOptions;
    }

    @PreAuthorize("hasAuthority('KRONOS_VIEW_NODES')")
    @RequestMapping(value = "/nodeoptions", method = RequestMethod.GET)
    public NodeOptionsModel getNodeOptions(HttpSession session) {
        String applicationId = getApplicationId(session);

        List<NodeType> nodeTypes = nodeTypeService.getNodeTypeRepository().findByApplicationIdAndEnabled(applicationId,
                true);
        List<Node> nodes = nodeService.getNodeRepository().findByApplicationIdAndEnabled(applicationId, true);

        return new NodeOptionsModel(nodeTypes, nodes);
    }

    @PreAuthorize("hasAuthority('KRONOS_CREATE_NODE')")
    @RequestMapping(method = RequestMethod.POST)
    public NodeModel add(@RequestBody NodeModel nodeModel, HttpSession session) {
        String applicationId = getApplicationId(session);
        Assert.notNull(nodeModel.getNodeType(), "node type is empty");
        NodeType nodeType = getKronosCache().findNodeTypeById(nodeModel.getNodeType().getId());
        Assert.notNull(nodeType, "node type is not found");
        Assert.isTrue(applicationId.equals(nodeType.getApplicationId()),
                "node type and user must have the same application id");
        Node parentNode = null;
        if (nodeModel.getParentNode() != null) {
            parentNode = getKronosCache().findNodeById(nodeModel.getParentNode().getId());
            Assert.notNull(parentNode, "parent node is not found");
            Assert.isTrue(applicationId.equals(parentNode.getApplicationId()),
                    "parent node and user must have the same application id");
        }

        Node node = new Node();
        node.setApplicationId(applicationId); // mandatory
        node.setName(nodeModel.getName());
        node.setDescription(nodeModel.getDescription());
        node.setNodeTypeId(nodeType.getId());
        node.setParentNodeId(parentNode != null ? parentNode.getId() : null);
        node.setEnabled(nodeModel.isEnabled());

        node = nodeService.create(node, getUserId());

        return new NodeModel(node, nodeType, parentNode);
    }

    @PreAuthorize("hasAuthority('KRONOS_EDIT_NODE')")
    @RequestMapping(value = "/{nodeId}", method = RequestMethod.PUT)
    public NodeModel edit(@PathVariable String nodeId, @RequestBody NodeModel nodeModel, HttpSession session) {
        String applicationId = getApplicationId(session);
        Node node = getKronosCache().findNodeById(nodeId);
        Assert.notNull(node, "node is not found");
        Assert.isTrue(applicationId.equals(node.getApplicationId()), "node and user must have the same application id");
        NodeType nodeType = getKronosCache().findNodeTypeById(nodeModel.getNodeType().getId());
        Assert.notNull(nodeType, "node type is not found");
        Assert.isTrue(applicationId.equals(nodeType.getApplicationId()),
                "node type and user must have the same application id");
        Node parentNode = null;
        if (nodeModel.getParentNode() != null) {
            parentNode = getKronosCache().findNodeById(nodeModel.getParentNode().getId());
            Assert.notNull(parentNode, "parent node is not found");
            Assert.isTrue(applicationId.equals(parentNode.getApplicationId()),
                    "parent node and user must have the same application id");
        }

        node.setName(nodeModel.getName());
        node.setDescription(nodeModel.getDescription());
        node.setNodeTypeId(nodeType.getId());
        node.setParentNodeId(parentNode != null ? parentNode.getId() : null);
        node.setEnabled(nodeModel.isEnabled());

        node = nodeService.update(node, getUserId());

        return new NodeModel(node, nodeType, parentNode);
    }

    private NodeTreeLevelModel getChildNodes(String applicationId, String parentNodeId) {
        List<Node> nodes = null;
        List<DeviceTree> deviceModels;
        if (StringUtils.isEmpty(parentNodeId)) {
            nodes = nodeService.getNodeRepository().findByApplicationIdAndParentNodeId(applicationId, null);
            deviceModels = new ArrayList<>();
        } else {
            Node parentNode = getKronosCache().findNodeById(parentNodeId);
            Assert.notNull(parentNode, "parent node is null");
            Assert.isTrue(parentNode.getApplicationId().equals(applicationId),
                    "user and parent node must have the same application id");

            nodes = nodeService.getNodeRepository().findByApplicationIdAndParentNodeId(applicationId, parentNodeId);

            String myUserId = getUserId();

            List<Device> devices;
            if (hasAuthority("KRONOS_VIEW_ALL_DEVICES")) {
                devices = deviceService.getDeviceRepository().findAllByApplicationIdAndNodeId(applicationId,
                        parentNodeId);
            } else {
                // filter by userId
                devices = deviceService.getDeviceRepository().findAllByApplicationIdAndNodeIdAndUserId(applicationId,
                        parentNodeId, myUserId);
            }

            deviceModels = new ArrayList<>(devices.size());
            for (Device device : devices) {
                DeviceType type = null;
                String ownerName = null;
                String gatewayName = null;
                boolean myDevice = false;
                Long lastTelemetryDate = null;
                // lookup child objects from cache
                if (!StringUtils.isEmpty(device.getDeviceTypeId()))
                    type = getKronosCache().findDeviceTypeById(device.getDeviceTypeId());
                if (!StringUtils.isEmpty(device.getUserId())) {
                    myDevice = myUserId.equals(device.getUserId());
                    User user = getCoreCacheService().findUserById(device.getUserId());
                    if (user != null)
                        ownerName = user.getContact().fullName();
                }
                if (!StringUtils.isEmpty(device.getGatewayId())) {
                    Gateway gateway = getKronosCache().findGatewayById(device.getGatewayId());
                    if (gateway != null)
                        gatewayName = gateway.getName();
                }

                // find last telemetry timestamp
                PageRequest pageRequest = new PageRequest(0, 1, new Sort(Direction.DESC, "timestamp"));
                TelemetrySearchParams params = new TelemetrySearchParams();
                params.setDeviceIds(device.getId());
                Page<Telemetry> page = telemetryService.getTelemetryRepository().doFindTelemetries(pageRequest, params);
                List<Telemetry> telemetries = page.getContent();
                if (telemetries != null && telemetries.size() > 0) {
                    lastTelemetryDate = telemetries.get(0).getTimestamp();
                }

                deviceModels.add(new DeviceTree(device, type, ownerName, gatewayName, myDevice, lastTelemetryDate));
            }
            deviceModels.sort(Comparator.comparing(DeviceTree::getName, String.CASE_INSENSITIVE_ORDER));
        }

        List<NodeModel> nodeModels = getNodeModels(nodes);
        nodeModels.sort(Comparator.comparing(NodeModel::getName, String.CASE_INSENSITIVE_ORDER));

        return new NodeTreeLevelModel(nodeModels, deviceModels);
    }

    private List<NodeModel> getNodeModels(Iterable<Node> nodes) {
        List<NodeModel> nodeModels = new ArrayList<>();
        if (nodes != null) {
            for (Node node : nodes) {
                NodeType nodeType = getKronosCache().findNodeTypeById(node.getNodeTypeId());
                Node parentNode = null;
                if (node.getParentNodeId() != null) {
                    parentNode = getKronosCache().findNodeById(node.getParentNodeId());
                }

                User currentUser = getCoreCacheService().findUserById(node.getLastModifiedBy());
                if (currentUser != null) {
                    node.setLastModifiedBy(getKronosModelUtil().populateDecryptedLogin(currentUser));
                } else if (!node.getLastModifiedBy().equals("admin") && !node.getLastModifiedBy().equals("demo")) {
                   node.setLastModifiedBy("Unknown");
                }

                nodeModels.add(new NodeModel(node, nodeType, parentNode));
            }
        }

        return nodeModels;
    }
}

package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.arrow.kronos.data.Node;
import com.arrow.kronos.data.NodeType;
import com.arrow.kronos.web.model.DeviceModels.DeviceTree;
import com.arrow.kronos.web.model.NodeTypeModels.NodeTypeOption;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;

public class NodeModels {

    public static class NodeOption extends CoreDefinitionModelOption {
        private static final long serialVersionUID = -5200848137968936124L;

        public static final String PATH_SEPARATOR = "/";
        public static final String CIRCULAR_INDICATOR = "..." + PATH_SEPARATOR;
        public static final String DESCRIPTION_SEPARATOR = " - ";

        public static String getNodePath(Node node, Map<String, Node> nodes) {
            String path = "";
            Set<String> usedNodes = new HashSet<>();
            while (node != null) {
                if (path.length() > 0) {
                    // add path separator
                    path = PATH_SEPARATOR + path;
                }
                path = node.getName() + path;
                if (usedNodes.contains(node.getId())) {
                    // circullar references detected
                    path = CIRCULAR_INDICATOR + path;
                    break;
                }
                usedNodes.add(node.getId());
                if (node.getParentNodeId() == null) {
                    // root node
                    path = PATH_SEPARATOR + path;
                    break;
                }
                // get parent node
                node = nodes.get(node.getParentNodeId());
            }

            return path;
        }

        public static String getNodeFullName(Node node, Map<String, Node> nodes) {
            String path = getNodePath(node, nodes);
            return path + DESCRIPTION_SEPARATOR + node.getDescription();
        }

        public NodeOption() {
            super(null, null, null);
        }

        public NodeOption(Node node) {
            super(node.getId(), node.getHid(), node.getName());
        }

        public NodeOption(Node node, String fullName) {
            super(node.getId(), node.getHid(), fullName);
        }
    }

    public static class NodeModel extends NodeOption {
        private static final long serialVersionUID = 9121085726750203798L;

        private String description;
        private NodeTypeOption nodeType;
        private NodeOption parentNode;
        private boolean enabled;
        private String lastModifiedBy;
        private Instant lastModifiedDate;

        public NodeModel() {
            super();
        }

        public NodeModel(Node node, NodeType nodeType, Node parentNode) {
            super(node);

            this.description = node.getDescription();
            this.nodeType = new NodeTypeOption(nodeType);
            this.parentNode = parentNode != null ? new NodeOption(parentNode) : null;
            this.enabled = node.isEnabled();
            this.lastModifiedBy = node.getLastModifiedBy();
            this.lastModifiedDate = node.getLastModifiedDate();
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public NodeTypeOption getNodeType() {
            return nodeType;
        }

        public void setNodeType(NodeTypeOption nodeType) {
            this.nodeType = nodeType;
        }

        public NodeOption getParentNode() {
            return parentNode;
        }

        public void setParentNode(NodeOption parentNode) {
            this.parentNode = parentNode;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getLastModifiedBy() {
            return lastModifiedBy;
        }

        public void setLastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
        }

        public Instant getLastModifiedDate(){
            return lastModifiedDate;
        }

    }

    public static class NodeOptionsModel implements Serializable {
        private static final long serialVersionUID = -1329396146331148261L;

        private List<NodeTypeOption> types;
        private List<NodeOption> nodes;

        public NodeOptionsModel(List<NodeType> nodeTypes, List<Node> nodes) {
            this.types = new ArrayList<>(nodeTypes.size());
            for (NodeType nodeType : nodeTypes) {
                this.types.add(new NodeTypeOption(nodeType));
            }
            this.types.sort(Comparator.comparing(NodeTypeOption::getName, String.CASE_INSENSITIVE_ORDER));

            Map<String, Node> nodesMap = new HashMap<>();
            for (Node node : nodes) {
                nodesMap.put(node.getId(), node);
            }
            this.nodes = new ArrayList<>(nodes.size());
            for (Node node : nodes) {
                this.nodes.add(new NodeOption(node, NodeOption.getNodeFullName(node, nodesMap)));
            }
            this.nodes.sort(Comparator.comparing(NodeOption::getName, String.CASE_INSENSITIVE_ORDER));
        }

        public List<NodeTypeOption> getTypes() {
            return types;
        }

        public List<NodeOption> getNodes() {
            return nodes;
        }

    }

    public static class NodeTreeLevelModel implements Serializable {
        private static final long serialVersionUID = 5818191379138776887L;

        private List<NodeModel> nodes;
        private List<DeviceTree> devices;

        public NodeTreeLevelModel(List<NodeModel> nodes, List<DeviceTree> devices) {
            super();
            this.nodes = nodes;
            this.devices = devices;
        }

        public List<NodeModel> getNodes() {
            return nodes;
        }

        public List<DeviceTree> getDevices() {
            return devices;
        }

    }
}

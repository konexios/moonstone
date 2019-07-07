package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.arrow.kronos.data.Node;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class NodeRepositoryExtensionImpl extends RepositoryExtensionAbstract<Node> implements NodeRepositoryExtension {

    public NodeRepositoryExtensionImpl() {
        super(Node.class);
    }

    @Override
    public List<Node> doFindAllByIdsApplicationIdAndEnabled(List<String> ids, String applicationId, boolean enabled) {
        List<Criteria> criteria = new ArrayList<Criteria>();
        criteria = addCriteria(criteria, "id", ids);
        criteria = addCriteria(criteria, "applicationId", applicationId);
        criteria = addCriteria(criteria, "enabled", enabled);

        Query query = doProcessCriteria(criteria);

        return doFind(query);
    }

    @Override
    public Page<Node> findNodes(Pageable pageable, NodeSearchParams params) {
        String methodName = "findNodes";
        logInfo(methodName, "...");
        List<Criteria> criteria = new ArrayList<Criteria>(3);
        if (params != null) {
            criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
            criteria = addCriteria(criteria, "nodeTypeId", params.getNodeTypeIds());
            criteria = addCriteria(criteria, "parentNodeId", params.getParentNodeIds());
            criteria = addCriteria(criteria, "enabled", params.getEnabled());
        }

        return doProcessQuery(pageable, criteria);
    }

    public long findNodeCount(NodeSearchParams params) {
        String methodName = "findNodeCount";
        logInfo(methodName, "...");
        List<Criteria> criteria = buildCriteria(params);
        return doCount(doProcessCriteria(criteria));
    }

    private List<Criteria> buildCriteria(NodeSearchParams params) {
        List<Criteria> criteria = new ArrayList<>();
        if (params != null) {
            criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
            criteria = addCriteria(criteria, "nodeTypeId", params.getNodeTypeIds());
            criteria = addCriteria(criteria, "parentNodeId", params.getParentNodeIds());
            criteria = addCriteria(criteria, "enabled", params.getEnabled());
            if (params.getCreatedBefore() != null) {
                criteria = addCriteria(criteria, "createdDate", null, params.getCreatedBefore());
            }
        }
        return criteria;
    }
}

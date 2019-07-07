package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.arrow.kronos.data.NodeType;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class NodeTypeRepositoryExtensionImpl extends RepositoryExtensionAbstract<NodeType>
        implements NodeTypeRepositoryExtension {

    public NodeTypeRepositoryExtensionImpl() {
        super(NodeType.class);
    }

    @Override
    public List<NodeType> doFindAllByIdsApplicationIdAndEnabled(List<String> ids, String applicationId,
            boolean enabled) {
        List<Criteria> criteria = new ArrayList<Criteria>();
        criteria = addCriteria(criteria, "id", ids);
        criteria = addCriteria(criteria, "applicationId", applicationId);
        criteria = addCriteria(criteria, "enabled", enabled);

        Query query = doProcessCriteria(criteria);

        return doFind(query);
    }

    @Override
    public Page<NodeType> findNodeTypes(Pageable pageable, KronosDocumentSearchParams params) {
        String methodName = "findNodeTypes";
        logInfo(methodName, "...");
        List<Criteria> criteria = new ArrayList<Criteria>(2);
        if (params != null) {
            criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
            criteria = addCriteria(criteria, "enabled", params.getEnabled());
        }

        return doProcessQuery(pageable, criteria);
    }
}

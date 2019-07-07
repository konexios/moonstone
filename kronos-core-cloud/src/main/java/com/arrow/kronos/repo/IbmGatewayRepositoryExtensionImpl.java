package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.arrow.kronos.data.IbmGateway;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class IbmGatewayRepositoryExtensionImpl extends RepositoryExtensionAbstract<IbmGateway>
        implements IbmGatewayRepositoryExtension {

    public IbmGatewayRepositoryExtensionImpl() {
        super(IbmGateway.class);
    }

    @Override
    public List<IbmGateway> findIbmGateways(IbmGatewaySearchParams params) {
        String method = "findIbmGateways";
        logDebug(method, "...");
        List<Criteria> criteria = new ArrayList<>();
        if (params != null) {
            criteria = addCriteria(criteria, "ibmAccountId", params.getIbmAccountIds());
            criteria = addCriteria(criteria, "gatewayId", params.getGatewayIds());
            criteria = addCriteria(criteria, "enabled", params.getEnabled());
        }
        Query query = doProcessCriteria(criteria);
        return doFind(query);
    }
}

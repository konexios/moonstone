package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.arrow.kronos.data.AwsThing;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class AwsThingRepositoryExtensionImpl extends RepositoryExtensionAbstract<AwsThing>
        implements AwsThingRepositoryExtension {

    public AwsThingRepositoryExtensionImpl() {
        super(AwsThing.class);
    }

    @Override
    public List<AwsThing> findAwsThings(AwsThingSearchParams params) {
        String method = "findAwsThings";
        logInfo(method, "...");
        List<Criteria> criteria = new ArrayList<Criteria>();
        if (params != null) {
            criteria = addCriteria(criteria, "awsAccountId", params.getAwsAccountIds());
            criteria = addCriteria(criteria, "gatewayId", params.getGatewayIds());
            criteria = addCriteria(criteria, "enabled", params.getEnabled());
        }
        Query query = doProcessCriteria(criteria);
        return doFind(query);
    }
}

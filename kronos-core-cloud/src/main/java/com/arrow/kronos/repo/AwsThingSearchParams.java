package com.arrow.kronos.repo;

import java.util.Set;

public class AwsThingSearchParams extends KronosDocumentSearchParams {

    private static final long serialVersionUID = 6769850766400674528L;

    private Set<String> awsAccountIds;
    private Set<String> gatewayIds;

    public Set<String> getAwsAccountIds() {
        return awsAccountIds;
    }

    public AwsThingSearchParams addAwsAccountIds(String... awsAccountIds) {
        this.awsAccountIds = addValues(this.awsAccountIds, awsAccountIds);
        return this;
    }

    public Set<String> getGatewayIds() {
        return gatewayIds;
    }

    public AwsThingSearchParams addGatewayIds(String... gatewayIds) {
        this.gatewayIds = addValues(this.gatewayIds, gatewayIds);
        return this;
    }
}

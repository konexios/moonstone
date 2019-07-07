package com.arrow.kronos.repo;

import java.util.Set;

public class IbmGatewaySearchParams extends KronosDocumentSearchParams {

    private static final long serialVersionUID = -5826409762489058048L;

    private Set<String> ibmAccountIds;
    private Set<String> gatewayIds;

    public Set<String> getIbmAccountIds() {
        return ibmAccountIds;
    }

    public IbmGatewaySearchParams addIbmAccountIds(String... ibmAccountIds) {
        this.ibmAccountIds = addValues(this.ibmAccountIds, ibmAccountIds);
        return this;
    }

    public Set<String> getGatewayIds() {
        return gatewayIds;
    }

    public IbmGatewaySearchParams addGatewayIds(String... gatewayIds) {
        this.gatewayIds = addValues(this.gatewayIds, gatewayIds);
        return this;
    }
}

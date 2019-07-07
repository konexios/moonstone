package com.arrow.kronos.repo;

import java.util.Set;

public class IbmDeviceSearchParams extends KronosDocumentSearchParams {

    private static final long serialVersionUID = 913287708248918386L;

    private Set<String> ibmAccountIds;
    private Set<String> deviceIds;

    public Set<String> getIbmAccountIds() {
        return ibmAccountIds;
    }

    public IbmDeviceSearchParams addIbmAccountIds(String... ibmAccountIds) {
        this.ibmAccountIds = addValues(this.ibmAccountIds, ibmAccountIds);
        return this;
    }

    public Set<String> getDeviceIds() {
        return deviceIds;
    }

    public IbmDeviceSearchParams addDeviceIds(String... deviceIds) {
        this.deviceIds = addValues(this.deviceIds, deviceIds);
        return this;
    }

}

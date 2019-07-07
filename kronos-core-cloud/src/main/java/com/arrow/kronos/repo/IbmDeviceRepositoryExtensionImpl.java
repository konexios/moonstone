package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.arrow.kronos.data.IbmDevice;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class IbmDeviceRepositoryExtensionImpl extends RepositoryExtensionAbstract<IbmDevice>
        implements IbmDeviceRepositoryExtension {

    public IbmDeviceRepositoryExtensionImpl() {
        super(IbmDevice.class);
    }

    @Override
    public List<IbmDevice> findIbmDevices(IbmDeviceSearchParams params) {
        String method = "findIbmDevices";
        logInfo(method, "...");
        List<Criteria> criteria = new ArrayList<>();
        if (params != null) {
            criteria = addCriteria(criteria, "ibmAccountId", params.getIbmAccountIds());
            criteria = addCriteria(criteria, "deviceId", params.getDeviceIds());
            criteria = addCriteria(criteria, "enabled", params.getEnabled());
        }
        Query query = doProcessCriteria(criteria);
        return doFind(query);
    }
}

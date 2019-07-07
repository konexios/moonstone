package com.arrow.rhea.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.repo.RepositoryExtensionAbstract;
import com.arrow.rhea.data.DeviceManufacturer;

public class DeviceManufacturerRepositoryExtensionImpl extends RepositoryExtensionAbstract<DeviceManufacturer>
        implements DeviceManufacturerRepositoryExtension {
    public DeviceManufacturerRepositoryExtensionImpl() {
        super(DeviceManufacturer.class);
    }

    @Override
    public Page<DeviceManufacturer> findDeviceManufacturers(Pageable pageable, DeviceManufacturerSearchParams params) {
        String methodName = "findDeviceManufacturers";
        logInfo(methodName, "...");
        List<Criteria> criteria = new ArrayList<Criteria>(2);
        if (params != null) {
            criteria = addCriteria(criteria, "enabled", params.getEnabled());
        }

        return doProcessQuery(pageable, criteria);
    }
}

package com.arrow.kronos.repo;

import com.arrow.kronos.data.DeviceState;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class DeviceStateRepositoryExtensionImpl extends RepositoryExtensionAbstract<DeviceState>
        implements DeviceStateRepositoryExtension {

    public DeviceStateRepositoryExtensionImpl() {
        super(DeviceState.class);
    }

}

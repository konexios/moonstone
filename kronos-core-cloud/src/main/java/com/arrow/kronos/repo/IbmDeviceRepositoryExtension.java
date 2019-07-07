package com.arrow.kronos.repo;

import java.util.List;

import com.arrow.kronos.data.IbmDevice;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface IbmDeviceRepositoryExtension extends RepositoryExtension<IbmDevice> {
    List<IbmDevice> findIbmDevices(IbmDeviceSearchParams params);
}

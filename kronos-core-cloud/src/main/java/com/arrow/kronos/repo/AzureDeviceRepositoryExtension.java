package com.arrow.kronos.repo;

import java.util.List;

import com.arrow.kronos.data.AzureDevice;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface AzureDeviceRepositoryExtension extends RepositoryExtension<AzureDevice> {
	List<AzureDevice> findAzureDevices(AzureDeviceSearchParams params);
}

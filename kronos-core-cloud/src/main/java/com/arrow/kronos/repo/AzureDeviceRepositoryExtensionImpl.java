package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.arrow.kronos.data.AzureDevice;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class AzureDeviceRepositoryExtensionImpl extends RepositoryExtensionAbstract<AzureDevice>
		implements AzureDeviceRepositoryExtension {

	public AzureDeviceRepositoryExtensionImpl() {
		super(AzureDevice.class);
	}

	@Override
	public List<AzureDevice> findAzureDevices(AzureDeviceSearchParams params) {
		String method = "findAzureDevices";
		logInfo(method, "...");
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "azureAccountId", params.getAzureAccountIds());
			criteria = addCriteria(criteria, "gatewayId", params.getGatewayIds());
			criteria = addCriteria(criteria, "enabled", params.getEnabled());
		}
		Query query = doProcessCriteria(criteria);
		return doFind(query);
	}
}

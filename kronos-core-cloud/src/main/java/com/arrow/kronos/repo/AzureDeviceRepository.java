package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.AzureDevice;

@Repository
public interface AzureDeviceRepository extends MongoRepository<AzureDevice, String>, AzureDeviceRepositoryExtension {
	List<AzureDevice> findAllByGatewayIdAndEnabled(String gatewayId, boolean enabled);

	AzureDevice findByGatewayIdAndAzureAccountId(String gatewayId, String azureAccountId);

	AzureDevice findByGatewayId(String gatewayId);

	Long deleteByGatewayId(String gatewayId);

	Long deleteByApplicationId(String applicationId);
}

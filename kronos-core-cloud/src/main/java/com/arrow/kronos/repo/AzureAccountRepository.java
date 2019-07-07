package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.AzureAccount;

@Repository
public interface AzureAccountRepository extends MongoRepository<AzureAccount, String>, AzureAccountRepositoryExtension {
	List<AzureAccount> findAllByApplicationIdAndEnabled(String applicationId, boolean enabled);

	AzureAccount findFirstByHostName(String hostName);

	List<AzureAccount> findAllByApplicationIdAndUserId(String applicationId, String userId);

	List<AzureAccount> findAllByApplicationId(String applicationId);

	Long deleteByApplicationId(String applicationId);
}

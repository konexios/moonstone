package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.AwsAccount;

@Repository
public interface AwsAccountRepository extends MongoRepository<AwsAccount, String>, AwsAccountRepositoryExtension {
	List<AwsAccount> findAllByApplicationIdAndEnabled(String applicationId, boolean enabled);

	List<AwsAccount> findAllByUserIdAndEnabled(String userId, boolean enabled);

	List<AwsAccount> findAllByApplicationIdAndUserId(String applicationId, String userId);

	List<AwsAccount> findAllByApplicationId(String applicationId);

	Long deleteByApplicationId(String applicationId);
}

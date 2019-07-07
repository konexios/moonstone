package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.IbmAccount;

@Repository
public interface IbmAccountRepository extends MongoRepository<IbmAccount, String>, IbmAccountRepositoryExtension {
	IbmAccount findByApplicationIdAndOrganizationId(String applicationId, String organizationId);

	List<IbmAccount> findAllByApplicationIdAndEnabled(String applicationId, boolean enabled);

	List<IbmAccount> findAllByUserIdAndEnabled(String userId, boolean enabled);

	List<IbmAccount> findAllByApplicationIdAndUserId(String applicationId, String userId);

	List<IbmAccount> findAllByApplicationIdAndUserIdAndEnabled(String applicationId, String userId, boolean enabled);

	List<IbmAccount> findByApplicationId(String applicationId);

	Long deleteByApplicationId(String applicationId);
}

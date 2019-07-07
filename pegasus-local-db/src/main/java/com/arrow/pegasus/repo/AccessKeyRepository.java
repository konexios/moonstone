package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.AccessKey;

@Repository
public interface AccessKeyRepository extends MongoRepository<AccessKey, String>, AccessKeyRepositoryExtension {
	AccessKey findByHashedApiKey(String hashedApiKey);

	List<AccessKey> findByCompanyId(String companyId);

	List<AccessKey> findByApplicationId(String applicationId);

	Long deleteByCompanyId(String companyId);

	Long deleteByApplicationId(String applicationId);
}
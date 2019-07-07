package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.profile.Application;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String>, ApplicationRepositoryExtension {

	Application findByNameAndCompanyId(String name, String companyId);

	Application findByName(String name);

	Application findByCode(String code);

	List<Application> findAllByApplicationEngineId(String applicationEngineId);

	List<Application> findByEnabled(boolean enabled);

	List<Application> findByCompanyId(String companyId);

	List<Application> findByCompanyIdAndEnabled(String companyId, boolean enabled);

	List<Application> findByProductIdAndEnabled(String productId, boolean enabled);

	List<Application> findByProductId(String productId);

	List<Application> findBySubscriptionId(String subscriptionId);

	List<Application> findBySubscriptionIdAndEnabled(String subscriptionId, boolean enabled);

	Long deleteByCompanyId(String companyId);

	Long deleteBySubscriptionId(String subscriptionId);
}

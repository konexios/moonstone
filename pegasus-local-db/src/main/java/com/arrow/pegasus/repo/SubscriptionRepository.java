package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.profile.Subscription;

@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, String>, SubscriptionRepositoryExtension {
	Subscription findByNameAndCompanyId(String name, String companyId);

	List<Subscription> findByEnabled(boolean enabled);

	List<Subscription> findByCompanyId(String companyId);

	List<Subscription> findByCompanyIdAndEnabled(String companyId, boolean enabled);

	Long deleteByCompanyId(String companyId);
}

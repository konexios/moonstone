package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.AwsThing;

@Repository
public interface AwsThingRepository extends MongoRepository<AwsThing, String>, AwsThingRepositoryExtension {
	List<AwsThing> findAllByGatewayIdAndEnabled(String gatewayId, boolean enabled);

	List<AwsThing> findAllByAwsAccountIdAndHostAndEnabled(String awsAccountId, String host, boolean enabled);

	Long deleteByGatewayId(String gatewayId);

	Long deleteByApplicationId(String applicationId);
}

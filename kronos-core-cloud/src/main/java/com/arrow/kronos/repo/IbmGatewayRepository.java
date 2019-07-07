package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.IbmGateway;

@Repository
public interface IbmGatewayRepository extends MongoRepository<IbmGateway, String>, IbmGatewayRepositoryExtension {
	List<IbmGateway> findAllByGatewayIdAndEnabled(String gatewayId, boolean enabled);

	IbmGateway findByGatewayId(String gatewayId);

	IbmGateway findByGatewayIdAndIbmAccountId(String gatewayId, String ibmAccountId);

	Long deleteByGatewayId(String gatewayId);

	Long deleteByApplicationId(String applicationId);
}

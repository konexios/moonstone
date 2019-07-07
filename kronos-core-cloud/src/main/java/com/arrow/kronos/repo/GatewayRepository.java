package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.Gateway;

@Repository
public interface GatewayRepository extends MongoRepository<Gateway, String>, GatewayRepositoryExtension {

    Gateway findByApplicationIdAndUid(String applicationId, String uid);

    List<Gateway> findAllByEnabled(boolean enabled);

    List<Gateway> findAllByApplicationId(String applicationId);

    List<Gateway> findAllByApplicationIdAndEnabled(String applicationId, boolean enabled);

    List<Gateway> findAllByApplicationIdAndUserIdAndEnabled(String applicationId, String userId, boolean enabled);

    List<Gateway> findAllByUid(String uid);

    Long deleteByApplicationId(String applicationId);
}
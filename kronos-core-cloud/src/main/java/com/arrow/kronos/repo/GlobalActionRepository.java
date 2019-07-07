package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.action.GlobalAction;

@Repository
public interface GlobalActionRepository extends MongoRepository<GlobalAction, String>, GlobalActionRepositoryExtension {

	GlobalAction findByApplicationIdAndSystemName(String applicationId, String systemName);

	List<GlobalAction> findByGlobalActionTypeId(String globalActionType);

	Long deleteByApplicationId(String applicationId);
}

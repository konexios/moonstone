package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.action.GlobalActionType;

@Repository
public interface GlobalActionTypeRepository
        extends MongoRepository<GlobalActionType, String>, GlobalActionTypeRepositoryExtension {

	GlobalActionType findByApplicationIdAndSystemName(String applicationId, String systemName);

	List<GlobalActionType> findByApplicationId(String applicationId);

	Long deleteByApplicationId(String applicationId);
}

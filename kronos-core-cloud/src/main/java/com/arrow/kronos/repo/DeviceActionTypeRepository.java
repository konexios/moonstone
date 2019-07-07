package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.DeviceActionType;

@Repository
public interface DeviceActionTypeRepository
        extends MongoRepository<DeviceActionType, String>, DeviceActionTypeRepositoryExtension {
	DeviceActionType findByName(String name);

	DeviceActionType findByApplicationIdAndSystemName(String applicationId, String systemName);

	List<DeviceActionType> findByEnabled(boolean enabled);

	List<DeviceActionType> findByApplicationId(String applicationId);

	List<DeviceActionType> findByApplicationIdAndEnabled(String applicationId, boolean enabled);

	Long deleteByApplicationId(String applicationId);
}
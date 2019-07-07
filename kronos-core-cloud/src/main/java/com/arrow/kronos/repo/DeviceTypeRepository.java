package com.arrow.kronos.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.DeviceType;

@Repository
public interface DeviceTypeRepository extends MongoRepository<DeviceType, String>, DeviceTypeRepositoryExtension {
	DeviceType findFirstByApplicationIdAndName(String applicationId, String name);

	List<DeviceType> findByEnabled(boolean enabled);

	List<DeviceType> findByApplicationId(String applicationId);

	List<DeviceType> findByApplicationIdAndEnabled(String applicationId, boolean enabled);

	DeviceType findByRheaDeviceTypeId(String rheaDeviceTypeId);

	Long deleteByApplicationId(String applicationId);
}
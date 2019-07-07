package com.arrow.rhea.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.rhea.data.DeviceCategory;

@Repository
public interface DeviceCategoryRepository
        extends MongoRepository<DeviceCategory, String>, DeviceCategoryRepositoryExtension {
	List<DeviceCategory> findAllByEnabled(boolean enabled);

	DeviceCategory findByName(String name);
}
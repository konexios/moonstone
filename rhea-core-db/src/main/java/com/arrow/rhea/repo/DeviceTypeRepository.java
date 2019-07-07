package com.arrow.rhea.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.rhea.data.DeviceType;

@Repository
public interface DeviceTypeRepository extends MongoRepository<DeviceType, String>, DeviceTypeRepositoryExtension {
	public List<DeviceType> findByCompanyIdAndEnabled(String companyId, boolean enabled);
}
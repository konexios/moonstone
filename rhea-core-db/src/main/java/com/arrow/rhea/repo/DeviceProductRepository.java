package com.arrow.rhea.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.rhea.data.DeviceProduct;

@Repository
public interface DeviceProductRepository
        extends MongoRepository<DeviceProduct, String>, DeviceProductRepositoryExtension {
	List<DeviceProduct> findAllByCompanyIdAndEnabled(String companyId, boolean enabled);
}
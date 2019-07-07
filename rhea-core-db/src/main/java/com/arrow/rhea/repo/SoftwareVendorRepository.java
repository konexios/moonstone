package com.arrow.rhea.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.rhea.data.SoftwareVendor;

@Repository
public interface SoftwareVendorRepository
        extends MongoRepository<SoftwareVendor, String>, SoftwareVendorRepositoryExtension {
	List<SoftwareVendor> findAllByCompanyIdAndEnabled(String companyId, boolean enabled);
}
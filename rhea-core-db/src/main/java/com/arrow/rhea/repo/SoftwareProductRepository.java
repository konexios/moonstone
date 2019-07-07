package com.arrow.rhea.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.rhea.data.SoftwareProduct;

@Repository
public interface SoftwareProductRepository
        extends MongoRepository<SoftwareProduct, String>, SoftwareProductRepositoryExtension {
	List<SoftwareProduct> findAllByCompanyIdAndEnabled(String companyId, boolean enabled);
}

package com.arrow.rhea.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.rhea.data.SoftwareRelease;

@Repository
public interface SoftwareReleaseRepository
        extends MongoRepository<SoftwareRelease, String>, SoftwareReleaseRepositoryExtension {
	List<SoftwareRelease> findAllByCompanyIdAndEnabled(String companyId, boolean enabled);
}
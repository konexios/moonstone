package com.arrow.rhea.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.rhea.data.RTURequest;

@Repository
public interface RTURequestRepository
        extends MongoRepository<RTURequest, String>, RTURequestRepositoryExtension {
	
	public RTURequest findByCompanyIdAndSoftwareReleaseId(String companyId, 
			String softwareReleaseId);
}
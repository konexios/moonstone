package com.arrow.pegasus.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.location.LastLocation;
import com.arrow.pegasus.data.location.LocationObjectType;

@Repository
public interface LastLocationRepository extends MongoRepository<LastLocation, String>, LastLocationRepositoryExtension {
	public LastLocation findByObjectTypeAndObjectId(LocationObjectType objectType, String objectId);

	public Long deleteByObjectTypeAndObjectId(LocationObjectType objectType, String objectId);
}

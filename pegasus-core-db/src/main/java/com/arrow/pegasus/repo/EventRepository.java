package com.arrow.pegasus.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.event.Event;

@Repository
public interface EventRepository extends MongoRepository<Event, String>, EventRepositoryExtension {
	long deleteByApplicationId(String applicationId);
}
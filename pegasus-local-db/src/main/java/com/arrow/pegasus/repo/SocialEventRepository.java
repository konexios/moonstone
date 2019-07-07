package com.arrow.pegasus.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.SocialEvent;

@Repository
public interface SocialEventRepository extends MongoRepository<SocialEvent, String>, SocialEventRepositoryExtension {

	SocialEvent findByName(String name);
}

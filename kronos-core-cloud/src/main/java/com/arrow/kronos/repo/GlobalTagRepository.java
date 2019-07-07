package com.arrow.kronos.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.kronos.data.GlobalTag;

@Repository
public interface GlobalTagRepository extends MongoRepository<GlobalTag, String>, GlobalTagRepositoryExtension {

	GlobalTag findByName(String name);
}

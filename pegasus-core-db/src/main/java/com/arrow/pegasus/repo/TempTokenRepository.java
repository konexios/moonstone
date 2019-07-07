package com.arrow.pegasus.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.TempToken;

@Repository
public interface TempTokenRepository extends MongoRepository<TempToken, String>, TempTokenRepositoryExtension {
}

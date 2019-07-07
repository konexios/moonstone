package com.arrow.pegasus.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.MigrationTask;

@Repository
public interface MigrationTaskRepository
        extends MongoRepository<MigrationTask, String>, MigrationTaskRepositoryExtension {
    public MigrationTask findByName(String name);
}

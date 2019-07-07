package com.arrow.pegasus.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.data.ApplicationEngine;

@Repository
public interface ApplicationEngineRepository
        extends MongoRepository<ApplicationEngine, String>, ApplicationEngineRepositoryExtension {
	public List<ApplicationEngine> findAllByProductId(String productId);

	public List<ApplicationEngine> findByEnabled(boolean enabled);

	public List<ApplicationEngine> findByProductIdAndEnabled(String productId, boolean enabled);

	public ApplicationEngine findByName(String name);

	public ApplicationEngine findByNameAndEnabled(String name, boolean enabled);
}

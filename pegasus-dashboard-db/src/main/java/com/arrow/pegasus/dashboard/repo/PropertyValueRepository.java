package com.arrow.pegasus.dashboard.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.dashboard.data.PropertyValue;

@Repository
public interface PropertyValueRepository
        extends MongoRepository<PropertyValue, String>, PropertyValueRepositoryExtension {

	public List<PropertyValue> findByPagePropertyId(String pagePropertyId);

	public List<PropertyValue> findByType(String type);

	// public List<PropertyValue> findByTypeAndVersion(String type, int
	// version);
}

package com.arrow.pegasus.dashboard.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.dashboard.data.PageProperty;

@Repository
public interface PagePropertyRepository extends MongoRepository<PageProperty, String>, PagePropertyRepositoryExtension {
	public List<PageProperty> findByConfigurationPageIdOrderByPropertyNumberAsc(String pageId);

}

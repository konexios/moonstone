package com.arrow.pegasus.dashboard.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.dashboard.data.ConfigurationPage;

@Repository
public interface ConfigurationPageRepository
        extends MongoRepository<ConfigurationPage, String>, ConfigurationPageRepositoryExtension {
	public List<ConfigurationPage> findBywidgetConfigurationIdOrderByPageNumberAsc(String configurationId);

}

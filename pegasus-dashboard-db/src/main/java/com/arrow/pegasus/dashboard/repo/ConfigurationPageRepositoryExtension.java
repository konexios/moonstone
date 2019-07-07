package com.arrow.pegasus.dashboard.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.arrow.pegasus.dashboard.data.ConfigurationPage;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface ConfigurationPageRepositoryExtension extends RepositoryExtension<ConfigurationPage> {

	public Page<ConfigurationPage> findConfigurationPages(Pageable pageable, ConfigurationPageSearchParams params);

	public List<ConfigurationPage> findConfigurationPages(ConfigurationPageSearchParams params);
	
	public List<ConfigurationPage> findConfigurationPages(ConfigurationPageSearchParams params, Sort sort);
}

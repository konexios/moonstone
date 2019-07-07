package com.arrow.pegasus.dashboard.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.dashboard.data.WidgetConfiguration;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface WidgetConfigurationRepositoryExtension extends RepositoryExtension<WidgetConfiguration> {

	public Page<WidgetConfiguration> findWidgetConfigurations(Pageable pageable,
	        WidgetConfigurationSearchParams params);

	public List<WidgetConfiguration> findWidgetConfigurations(WidgetConfigurationSearchParams params);
}

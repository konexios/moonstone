package com.arrow.pegasus.dashboard.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.dashboard.data.WidgetConfiguration;

@Repository
public interface WidgetConfigurationRepository
        extends MongoRepository<WidgetConfiguration, String>, WidgetConfigurationRepositoryExtension {

	public WidgetConfiguration findByWidgetId(String widgetId);

	public WidgetConfiguration findByWidgetIdAndName(String widgetId, String name);

}

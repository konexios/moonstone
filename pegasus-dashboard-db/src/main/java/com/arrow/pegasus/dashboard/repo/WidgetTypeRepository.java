package com.arrow.pegasus.dashboard.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.dashboard.data.WidgetType;

@Repository
public interface WidgetTypeRepository extends MongoRepository<WidgetType, String>, WidgetTypeRepositoryExtension {
    WidgetType findByClassName(String className);
}

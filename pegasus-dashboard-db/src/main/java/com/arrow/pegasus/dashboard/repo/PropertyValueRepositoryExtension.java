package com.arrow.pegasus.dashboard.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.dashboard.data.PropertyValue;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface PropertyValueRepositoryExtension extends RepositoryExtension<PropertyValue> {

	public Page<PropertyValue> findPropertyValues(Pageable pageable, PropertyValueSearchParams params);

	public List<PropertyValue> findPropertyValues(PropertyValueSearchParams params);
}

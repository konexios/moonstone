package com.arrow.pegasus.dashboard.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.pegasus.dashboard.data.PropertyValue;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class PropertyValueRepositoryImpl extends RepositoryExtensionAbstract<PropertyValue>
        implements PropertyValueRepositoryExtension {

	public PropertyValueRepositoryImpl() {
		super(PropertyValue.class);
	}

	@Override
	public Page<PropertyValue> findPropertyValues(Pageable pageable, PropertyValueSearchParams params) {
		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<PropertyValue> findPropertyValues(PropertyValueSearchParams params) {
		return doProcessQuery(buildCriteria(params));
	}

	private List<Criteria> buildCriteria(PropertyValueSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "_id", params.getIds());
			criteria = addCriteria(criteria, "hid", params.getHids());
			criteria = addCriteria(criteria, "pagePropertyId", params.getPagePropertyIds());
		}
		return criteria;
	}
}

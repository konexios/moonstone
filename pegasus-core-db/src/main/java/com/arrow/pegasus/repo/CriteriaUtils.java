package com.arrow.pegasus.repo;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

public class CriteriaUtils {

	public static Query createQuery(List<Criteria> criteria) {
		Assert.notNull(criteria, "criteria is null");
		Query query = new Query();
		for (Criteria c : criteria)
			query.addCriteria(c);

		return query;
	}

	public static List<Criteria> addCriteria(List<Criteria> criteria, String property, Instant fromFilter,
	        Instant toFilter) {
		Assert.hasText(property, "property is empty");

		if (fromFilter != null && toFilter != null)
			criteria.add(Criteria.where(property).gte(fromFilter).lt(toFilter));
		else if (fromFilter != null)
			criteria.add(Criteria.where(property).gt(fromFilter));
		else if (toFilter != null)
			criteria.add(Criteria.where(property).lt(toFilter));

		return criteria;
	}

	public static List<Criteria> addCriteria(List<Criteria> criteria, String property, Long fromFilter, Long toFilter) {
		Assert.hasText(property, "property is empty");

		if (fromFilter != null && toFilter != null)
			criteria.add(Criteria.where(property).gte(fromFilter).lt(toFilter));
		else if (fromFilter != null)
			criteria.add(Criteria.where(property).gt(fromFilter));
		else if (toFilter != null)
			criteria.add(Criteria.where(property).lt(toFilter));

		return criteria;
	}

	public static List<Criteria> addCriteria(List<Criteria> criteria, String property, Set<String> filters) {
		Assert.hasText(property, "property is empty");

		if (filters != null && !filters.isEmpty())
			if (filters.size() == 1)
				criteria.add(Criteria.where(property).is(filters.toArray()[0]));
			else
				criteria.add(Criteria.where(property).in(filters.toArray()));

		return criteria;
	}

	public static List<Criteria> addNotCriteria(List<Criteria> criteria, String property, Set<String> filters) {
		Assert.hasText(property, "property is empty");

		if (filters != null && !filters.isEmpty())
			if (filters.size() == 1)
				criteria.add(Criteria.where(property).ne(filters.toArray()[0]));
			else
				criteria.add(Criteria.where(property).nin(filters.toArray()));

		return criteria;
	}

	public static List<Criteria> addCriteria(List<Criteria> criteria, String property, EnumSet<?> filters) {
		Assert.hasText(property, "property is empty");

		if (filters != null && !filters.isEmpty())
			criteria.add(Criteria.where(property).in(filters.stream().map(Enum::name).collect(Collectors.toList())));

		return criteria;
	}

	public static List<Criteria> addCriteria(List<Criteria> criteria, String property, List<String> filters) {
		Assert.hasText(property, "property is empty");

		if (filters != null && !filters.isEmpty())
			criteria = addCriteria(criteria, property, filters.toArray(new String[filters.size()]));

		return criteria;
	}

	public static List<Criteria> addNotCriteria(List<Criteria> criteria, String property, List<String> filters) {
		Assert.hasText(property, "property is empty");

		if (filters != null && !filters.isEmpty())
			criteria = addNotCriteria(criteria, property, filters.toArray(new String[filters.size()]));

		return criteria;
	}

	public static List<Criteria> addCriteria(List<Criteria> criteria, String property, String[] filters) {
		Assert.hasText(property, "property is empty");

		if (filters != null && filters.length > 0) {
			if (filters.length == 1)
				criteria = addCriteria(criteria, property, filters[0]);
			else
				criteria.add(Criteria.where(property).in((Object[]) filters));
		}

		return criteria;
	}

	public static List<Criteria> addNotCriteria(List<Criteria> criteria, String property, String[] filters) {
		Assert.hasText(property, "property is empty");

		if (filters != null && filters.length > 0) {
			if (filters.length == 1)
				criteria = addNotCriteria(criteria, property, filters[0]);
			else
				criteria.add(Criteria.where(property).nin((Object[]) filters));
		}

		return criteria;
	}

	public static List<Criteria> addCriteria(List<Criteria> criteria, String property, Boolean filter) {
		Assert.hasText(property, "property is empty");

		if (filter != null)
			criteria.add(Criteria.where(property).is(filter));

		return criteria;
	}

	public static List<Criteria> addCriteria(List<Criteria> criteria, String property, String filter) {
		Assert.hasText(property, "property is empty");

		if (filter != null)
			criteria.add(Criteria.where(property).is(filter));

		return criteria;
	}

	public static List<Criteria> addNotCriteria(List<Criteria> criteria, String property, String filter) {
		Assert.hasText(property, "property is empty");

		if (filter != null)
			criteria.add(Criteria.where(property).ne(filter));

		return criteria;
	}

	public static List<Criteria> addExistsCriteria(List<Criteria> criteria, String property, Boolean exists) {
		Assert.hasText(property, "property is empty");

		if (exists != null)
			criteria.add(Criteria.where(property).exists(exists));

		return criteria;
	}
}

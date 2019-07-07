package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.kronos.data.TelemetryUnit;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class TelemetryUnitRepositoryExtensionImpl extends RepositoryExtensionAbstract<TelemetryUnit>
        implements TelemetryUnitRepositoryExtension {

	public TelemetryUnitRepositoryExtensionImpl() {
		super(TelemetryUnit.class);
	}

	@Override
	public Page<TelemetryUnit> findTelemetryUnits(Pageable pageable, KronosDocumentSearchParams params) {
		String methodName = "findTelemetryUnits";
		logInfo(methodName, "...");
		List<Criteria> criteria = new ArrayList<Criteria>(1);
		if (params != null) {
			criteria = addCriteria(criteria, "enabled", params.getEnabled());
		}

		return doProcessQuery(pageable, criteria);
	}

	@Override
	public Page<TelemetryUnit> findTelemetryUnits(Pageable pageable, TelemetryUnitSearchParams params) {
		String methodName = "findTelemetryUnits";
		logInfo(methodName, "...");

		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<TelemetryUnit> findTelemetryUnits(TelemetryUnitSearchParams params) {
		String methodName = "findTelemetryUnits";
		logInfo(methodName, "...");

		return doProcessQuery(buildCriteria(params));
	}

	@Override
	public List<TelemetryUnit> findTelemetryUnits(TelemetryUnitSearchParams params, Sort sort) {
		String methodName = "findTelemetryUnits";
		logInfo(methodName, "...");

		if (sort != null) {
			return doFind(doProcessCriteria(buildCriteria(params)).with(sort));
		} else {
			return findTelemetryUnits(params);
		}
	}

	private List<Criteria> buildCriteria(TelemetryUnitSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "enabled", params.getEnabled());
			if (!StringUtils.isEmpty(params.getName())) {
				criteria.add(Criteria.where("name").regex(params.getName(), "i"));
			}
			if (!StringUtils.isEmpty(params.getSystemName())) {
				criteria.add(Criteria.where("systemName").regex(params.getSystemName(), "i"));
			}
		}
		return criteria;
	}
}

package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.DocumentId;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class DeviceTypeRepositoryExtensionImpl extends RepositoryExtensionAbstract<DeviceType>
        implements DeviceTypeRepositoryExtension {

	public DeviceTypeRepositoryExtensionImpl() {
		super(DeviceType.class);
	}

	@Override
	public List<DeviceType> findDeviceTypes(DeviceTypeSearchParams params) {
		String methodName = "findDeviceTypes";
		logInfo(methodName, "...");

		return doProcessQuery(buildCriteria(params));
	}

	@Override
	public Page<DeviceType> findDeviceTypes(Pageable pageable, DeviceTypeSearchParams params) {
		String methodName = "findDeviceTypes";
		logInfo(methodName, "...");

		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<DeviceType> doFindAllByIdsApplicationIdAndEnabled(List<String> ids, String applicationId,
	        boolean enabled) {
		// List<Criteria> criteria = new ArrayList<Criteria>();
		// criteria = addCriteria(criteria, "id", ids);
		// criteria = addCriteria(criteria, "applicationId", applicationId);
		// criteria = addCriteria(criteria, "enabled", enabled);

		DeviceTypeSearchParams params = new DeviceTypeSearchParams();
		params.addIds(ids.toArray(new String[ids.size()]));
		params.addApplicationIds(applicationId);
		params.setEnabled(enabled);

		Query query = doProcessCriteria(buildCriteria(params));

		return doFind(query);
	}

	public List<DeviceType> findByApplicationIdAndNames(String applicationId, String[] names) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		// TODO search params needs to support names, can't convert this to use
		// params until then
		criteria = addCriteria(criteria, "name", names);
		criteria = addCriteria(criteria, "applicationId", applicationId);

		Query query = doProcessCriteria(criteria);

		return doFind(query);
	}

	@Override
	public List<String> findAllDeviceTypesWithNoTelemetryActions() {

		List<Criteria> criteria = new ArrayList<Criteria>();

		criteria.add(Criteria.where("actions").elemMatch(Criteria.where("noTelemetry").is(true)));
		criteria.add(Criteria.where("enabled").is(true));

		Query query = doProcessCriteria(criteria);
		query.fields().include("id");

		return getOperations().find(query, DocumentId.class, getOperations().getCollectionName(getDocumentClass()))
		        .stream().map(docId -> docId.getId()).collect(Collectors.toList());
	}

	private List<Criteria> buildCriteria(DeviceTypeSearchParams params) {

		List<Criteria> criteria = new ArrayList<Criteria>();

		if (params != null) {
			criteria = addCriteria(criteria, "id", params.getIds());
			criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
			criteria = addCriteria(criteria, "enabled", params.getEnabled());

			if (params.getRheaDeviceTypeDefined() != null) {
				if (params.getRheaDeviceTypeDefined()) {
					criteria.add(Criteria.where("rheaDeviceTypeId").ne(null));
				} else {
					criteria.add(Criteria.where("rheaDeviceTypeId").is(null));
				}
			}

			criteria = addCriteria(criteria, "deviceCategory", params.getDeviceCategories());
		}

		return criteria;
	}

}

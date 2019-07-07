package com.arrow.kronos.repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.arrow.kronos.data.SoftwareReleaseSchedule;
import com.arrow.pegasus.repo.DistinctCountResult;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class SoftwareReleaseScheduleRepositoryExtensionImpl extends RepositoryExtensionAbstract<SoftwareReleaseSchedule>
        implements SoftwareReleaseScheduleRepositoryExtension {

	public SoftwareReleaseScheduleRepositoryExtensionImpl() {
		super(SoftwareReleaseSchedule.class);
	}

	@Override
	public Page<SoftwareReleaseSchedule> findSoftwareReleaseSchedules(Pageable pageable,
	        SoftwareReleaseScheduleSearchParams params) {
		String method = "findSoftwareReleaseSchedules";
		logInfo(method, "...");
		return doProcessQuery(pageable, buildCriteria(params));
	}

	@Override
	public List<SoftwareReleaseSchedule> findSoftwareReleaseSchedules(SoftwareReleaseScheduleSearchParams params) {
		String method = "findSoftwareReleaseSchedules";
		logInfo(method, "...");
		return doProcessQuery(buildCriteria(params));
	}

	@Override
	public long findSoftwareReleaseScheduleCount(SoftwareReleaseScheduleSearchParams params) {
		String method = "findSoftwareReleaseScheduleCount";
		logDebug(method, "...");
		return doCount(doProcessCriteria(buildCriteria(params)));
	}

	@Override
	public Page<SoftwareReleaseSchedule> findSoftwareReleaseSchedules(Pageable pageable,
	        SoftwareReleaseScheduleStatusSearchParams params) {
		String method = "findSoftwareReleaseSchedules";
		logInfo(method, "...");
		return doProcessQuery(pageable, buildPendingCriteria(params));
	}

	@Override
	public List<SoftwareReleaseSchedule> findSoftwareReleaseSchedulesByObjectIds(String... objectIds) {
		String method = "findSoftwareReleaseSchedulesByObjectIds";
		logInfo(method, "...");
		return doProcessQuery(buildCriteria(objectIds));
	}

	public List<DistinctCountResult> countDistinctCreatedBy(SoftwareReleaseScheduleSearchParams params) {
		String method = "countDistinctCreatedBy";
		logInfo(method, "...");
		List<DistinctCountResult> result = doDistinctCount(SoftwareReleaseSchedule.COLLECTION_NAME,
		        buildCriteria(params), "createdBy");
		for (DistinctCountResult item : result) {
			logDebug(method, "---> name: %s, count: %d", item.getName(), item.getCount());
		}
		return result;
	}

	public List<DistinctCountResult> countDistinctDeviceTypeId(SoftwareReleaseScheduleSearchParams params) {
		String method = "countDistinctDeviceTypeId";
		logInfo(method, "...");
		List<DistinctCountResult> result = doDistinctCount(SoftwareReleaseSchedule.COLLECTION_NAME,
		        buildCriteria(params), "deviceTypeId");
		logInfo(method, "result: %d", result.size());
		for (DistinctCountResult item : result) {
			logDebug(method, "---> name: %s, count: %d", item.getName(), item.getCount());
		}
		return result;
	}

	public List<DistinctCountResult> countDistinctStatus(SoftwareReleaseScheduleSearchParams params) {
		String method = "countDistinctDeviceTypeId";
		logInfo(method, "...");
		List<DistinctCountResult> result = doDistinctCount(SoftwareReleaseSchedule.COLLECTION_NAME,
		        buildCriteria(params), "status");
		for (DistinctCountResult item : result) {
			logDebug(method, "---> name: %s, count: %d", item.getName(), item.getCount());
		}
		return result;
	}

	private List<Criteria> buildCriteria(String... objectIds) {
		return Collections.singletonList(Criteria.where("objectIds").in((Object[]) objectIds));
	}

	private List<Criteria> buildCriteria(SoftwareReleaseScheduleSearchParams params) {
		List<Criteria> criteria = new ArrayList<>();
		if (params != null) {
			criteria = addCriteria(criteria, "_id", params.getIds());
			criteria = addCriteria(criteria, "scheduledDate", params.getFromScheduledDate(),
			        params.getToScheduledDate());
			criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
			criteria = addCriteria(criteria, "softwareReleaseId", params.getSoftwareReleaseIds());
			criteria = addCriteria(criteria, "deviceCategory", params.getDeviceCategories());
			criteria = addCriteria(criteria, "objectIds", params.getObjectIds());
			criteria = addCriteria(criteria, "status", params.getStatuses());
			criteria = addCriteria(criteria, "notifyOnStart", params.getNotifyOnStart());
			criteria = addCriteria(criteria, "notifyOnEnd", params.getNotifyOnEnd());
			criteria = addCriteria(criteria, "notifyOnSubmit", params.getNotifyOnSubmit());
			criteria = addCriteria(criteria, "onDemand", params.getOnDemand());
			if (!StringUtils.isEmpty(params.getName())) {
				criteria.add(Criteria.where("name").regex(params.getName(), "i"));
			}
			criteria = addCriteria(criteria, "deviceTypeId", params.getDeviceTypeIds());
			criteria = addCriteria(criteria, "hardwareVersionId", params.getHardwareVersionIds());
		}
		return criteria;
	}

	private List<Criteria> buildPendingCriteria(SoftwareReleaseScheduleStatusSearchParams params) {
		List<Criteria> criteria = new ArrayList<>();
		if (params != null) {
			if (params.getOnDemand()) {
				criteria = addCriteria(criteria, "onDemand", params.getOnDemand());
			} else {
				criteria = addCriteria(criteria, "scheduledDate", params.getScheduledDate(), null);
			}
			criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
			criteria = addCriteria(criteria, "objectIds", params.getObjectIds());
			if (params.getRequestorIds() != null && !params.getRequestorIds().isEmpty()) {
				Criteria[] orCriteria = params.getRequestorIds().stream()
				        .map(id -> Criteria.where("createdBy").regex(id, "i")).toArray(Criteria[]::new);
				criteria.add(new Criteria().orOperator(orCriteria));
			}
			criteria = addCriteria(criteria, "hid", params.getHids());
			criteria = addCriteria(criteria, "ended", params.getCompletedDate(), null);
			criteria = addCriteria(criteria, "deviceTypeId", params.getDeviceTypeIds());

			if (params.getCompletedStatuses() != null && params.getCompletedStatuses().size() != 0) {
				Criteria c1 = null, c2 = null;
				if (params.getCompletedStatuses().contains("Cancelled")) {
					Criteria cancelled = Criteria.where("status").is(SoftwareReleaseSchedule.Status.CANCELLED);
					c1 = cancelled;
				}
				if (params.getCompletedStatuses().contains("Complete")) {
					Criteria complete = Criteria.where("status").is(SoftwareReleaseSchedule.Status.COMPLETE);
					if (c1 != null) {
						c2 = complete;
					} else {
						c1 = complete;
					}
				}
				if (c1 != null && c2 != null) {
					criteria.add(new Criteria().orOperator(c1, c2));
				} else {
					criteria.add(c1);
				}
			} else {
				criteria = addCriteria(criteria, "status", params.getStatuses());
			}
		}
		return criteria;
	}
}

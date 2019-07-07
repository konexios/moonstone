package com.arrow.kronos.repo;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.Assert;

import com.arrow.kronos.data.TelemetryStat;
import com.arrow.kronos.data.TestResult;
import com.arrow.pegasus.repo.RepositoryExtensionAbstract;

public class TestResultRepositoryExtensionImpl extends RepositoryExtensionAbstract<TestResult>
        implements TestResultRepositoryExtension {

	public TestResultRepositoryExtensionImpl() {
		super(TestResult.class);
	}

	@Override
	public Page<TestResult> findTestResult(Pageable pageable, TestResultSearchParams params) {
		return doProcessQuery(pageable, buildCriteria(params));
	}
	
	private List<Criteria> buildCriteria(TestResultSearchParams params) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (params != null) {
			criteria = addCriteria(criteria, "testProcedureId", params.getTestProcedureIds());
			criteria = addCriteria(criteria, "status", params.getStatuses());
			//criteria = addCriteria(criteria, "category", params.getCategories());
			criteria = addCriteria(criteria, "applicationId", params.getApplicationIds());
			criteria = addCriteria(criteria, "objectId", params.getObjectId());
			criteria = addCriteria(criteria, "createdDate", params.getCreatedAfter(), params.getCreatedBefore());
			criteria = addCriteria(criteria, "steps.status", params.getStepStatuses());
		}
		return criteria;
	}

	@Override
	public List<String> doFindTestProcedureIds(Collection<String> objectIds) {
		String methodName = "doFindTestProcedureIds";
		logInfo(methodName, "...");

		Assert.notEmpty(objectIds, "objectIds is empty");

		Aggregation agg = newAggregation(match(Criteria.where("objectId").in(objectIds)), group("testProcedureId"));

		AggregationResults<TelemetryStat> groupResults = getOperations().aggregate(agg, TestResult.COLLECTION_NAME,
		        TelemetryStat.class);
		return groupResults.getMappedResults().stream().map(stat -> stat.getName()).collect(Collectors.toList());
	}
}

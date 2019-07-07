package com.arrow.kronos.repo;

import java.time.Instant;
import java.util.Set;

public class TestResultSearchParams extends KronosDocumentSearchParams {
	private static final long serialVersionUID = 7620431241453360423L;

	private Set<String> statuses;
	private Set<String> testProcedureIds;
	//private Set<String> categories;
	private String objectId;
	private Instant createdBefore;
	private Instant createdAfter;
	private Set<String> stepStatuses;

	public Set<String> getStatuses() {
		return statuses;
	}

	public TestResultSearchParams addStatuses(String... statuses) {
		this.statuses = super.addValues(this.statuses, statuses);
		return this;
	}

	public Set<String> getTestProcedureIds() {
		return super.getValues(testProcedureIds);
	}

	public TestResultSearchParams addTestProcedureIds(String... testProcedureId) {
		this.testProcedureIds = super.addValues(this.testProcedureIds, testProcedureId);
		return this;
	}

	/*public Set<String> getCategories() {
		return categories;
	}

	public TestResultSearchParams addCategories(String... categories) {
		this.categories = super.addValues(this.categories, categories);
		return this;
	}*/

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public TestResultSearchParams addObjectId(String objectId) {
		setObjectId(objectId);
		return this;
	}

	public Instant getCreatedBefore() {
		return createdBefore;
	}

	public void setCreatedBefore(Instant createdBefore) {
		this.createdBefore = createdBefore;
	}

	public TestResultSearchParams addCreatedBefore(Instant createdBefore) {
		setCreatedBefore(createdBefore);
		return this;
	}

	public Instant getCreatedAfter() {
		return createdAfter;
	}

	public void setCreatedAfter(Instant createdAfter) {
		this.createdAfter = createdAfter;
	}

	public TestResultSearchParams addCreatedAfter(Instant createdAfter) {
		setCreatedAfter(createdAfter);
		return this;
	}

	public Set<String> getStepStatuses() {
		return stepStatuses;
	}

	public void setStepStatuses(Set<String> stepStatuses) {
		this.stepStatuses = stepStatuses;
	}

	public TestResultSearchParams addStepStatuses(String... stepStatuses) {
		this.stepStatuses = super.addValues(this.stepStatuses, stepStatuses);
		return this;
	}
}

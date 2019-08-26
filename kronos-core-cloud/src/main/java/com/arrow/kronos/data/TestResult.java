package com.arrow.kronos.data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.fasterxml.jackson.annotation.JsonIgnore;

import moonstone.acn.client.model.AcnDeviceCategory;

@Document(collection = TestResult.COLLECTION_NAME)
public class TestResult extends AuditableDocumentAbstract {
	private static final long serialVersionUID = 8204551910009150062L;
	public static final String COLLECTION_NAME = "test_result";

	public enum Status {
		PENDING, INPROGRESS, COMPLETE
	}

	@NotBlank
	private String objectId;
	@NotNull
	private Status status;
	@NotBlank
	private String testProcedureId;
	@NotNull
	private AcnDeviceCategory deviceCategory;
	// TODO remove post migration to deviceCategory
	private String tempCategory;
	@NotBlank
	private String applicationId;
	private List<TestResultStep> steps = new ArrayList<>();
	private Instant started;
	private Instant ended;

	@Transient
	@JsonIgnore
	private TestProcedure refTestProcedure;

	public TestProcedure getRefTestProcedure() {
		return refTestProcedure;
	}

	public void setRefTestProcedure(TestProcedure refTestProcedure) {
		this.refTestProcedure = refTestProcedure;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<TestResultStep> getSteps() {
		return steps;
	}

	public void setSteps(List<TestResultStep> steps) {
		this.steps = steps;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getTestProcedureId() {
		return testProcedureId;
	}

	public void setTestProcedureId(String testProcedureId) {
		this.testProcedureId = testProcedureId;
	}

	public AcnDeviceCategory getDeviceCategory() {
		return deviceCategory;
	}

	public void setDeviceCategory(AcnDeviceCategory deviceCategory) {
		this.deviceCategory = deviceCategory;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.TEST_RESULT;
	}

	public Instant getStarted() {
		return started;
	}

	public void setStarted(Instant started) {
		this.started = started;
	}

	public Instant getEnded() {
		return ended;
	}

	public void setEnded(Instant ended) {
		this.ended = ended;
	}

	// TODO remove post migration to deviceCategory
	public String getTempCategory() {
		return tempCategory;
	}

	// TODO remove post migration to deviceCategory
	public void setTempCategory(String tempCategory) {
		this.tempCategory = tempCategory;
	}
}

package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import com.arrow.kronos.data.DeviceCategory;
import com.arrow.kronos.data.TestResult;
import com.arrow.kronos.data.TestResult.Status;
import com.arrow.kronos.data.TestResultStep;
import com.arrow.kronos.web.model.TestProcedureModels.TestProcedureOptionModel;
import com.arrow.kronos.web.model.TestProcedureModels.TestProcedureStepModel;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

public class TestResultModels {
	public static class TestResultModelAbstract extends CoreDocumentModel {
		private static final long serialVersionUID = -9116847482715232421L;

		private Status status;
		private Instant started;
		private Instant ended;
		//private DeviceCategory category; 

		public TestResultModelAbstract() {
			super(null, null);
		}

		public TestResultModelAbstract(TestResult testResult) {
			super(testResult.getId(), testResult.getHid());
			this.status = testResult.getStatus();
			this.started = testResult.getStarted();
			this.ended = testResult.getEnded();
			//this.category = testResult.getCategory();
		}

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
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

		/*public DeviceCategory getCategory() {
            return category;
        }

        public void setCategory(DeviceCategory category) {
            this.category = category;
        }*/
	}


	public static class TestResultDetailsModel extends TestResultModelAbstract {
		private static final long serialVersionUID = -9116847482715232421L;

		private String testProcedureName;
		private String objectName;

		public TestResultDetailsModel() {
			super();
		}

		public TestResultDetailsModel(TestResult testResult) {
			super(testResult);
		}

		public String getTestProcedureName() {
			return testProcedureName;
		}

		public void setTestProcedureName(String testProcedureName) {
			this.testProcedureName = testProcedureName;
		}

		public String getObjectName() {
			return objectName;
		}

		public void setObjectName(String objectName) {
			this.objectName = objectName;
		}
	}

	public static class TestResultCommonModel extends TestResultDetailsModel {
		private static final long serialVersionUID = -2506959177989639911L;
 
		private String applicationId;
		private List<TestResultStepModel> steps = new ArrayList<>();

		public TestResultCommonModel() {
			super();
		}

		public TestResultCommonModel(TestResult testResult) {
			super(testResult);
			this.applicationId = testResult.getApplicationId();
		}

		public String getApplicationId() {
			return applicationId;
		}

		public void setApplicationId(String applicationId) {
			this.applicationId = applicationId;
		}

		public List<TestResultStepModel> getSteps() {
			return steps;
		}

		public TestResultCommonModel withSteps(List<TestResultStepModel> steps) {
			this.steps = steps;
			return this;
		}
	}

	public static class TestResultStepModel implements Serializable{
		private static final long serialVersionUID = -7989945557423866773L;

		private TestProcedureStepModel definition;
		private String comment;
		private String error;
		private TestResultStep.Status status;
		private Instant started;
		private Instant ended;

		public TestResultStepModel() {
		}

		public TestResultStepModel(TestResultStep testResultStep) {
			this.status = testResultStep.getStatus();
			this.comment = testResultStep.getComment();
			this.error = testResultStep.getError();
			this.started = testResultStep.getStarted();
			this.ended = testResultStep.getEnded();
		}

		public TestProcedureStepModel getDefinition() {
			return definition;
		}

		public void setDefinition(TestProcedureStepModel definition) {
			this.definition = definition;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public TestResultStep.Status getStatus() {
			return status;
		}

		public void setStatus(TestResultStep.Status status) {
			this.status = status;
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
	}

	public static class TestResultOptionsModel implements Serializable{
		private static final long serialVersionUID = -1213641821372021853L;

		private List<TestResult.Status> statuses;
		//private List<DeviceCategory> categories;
		private List<TestProcedureOptionModel> testProcedures;
		private List<TestResultStep.Status> stepStatuses = Arrays.asList(TestResultStep.Status.values());

		public TestResultOptionsModel(List<TestResult.Status> statuses,
		        /* List<DeviceCategory> categories, */ List<TestProcedureOptionModel> testProcedures) {
			this.statuses = statuses;
			//this.categories = categories;
			this.testProcedures = testProcedures;
		}

		/*public List<DeviceCategory> getCategories() {
    		return categories;
    	}*/

		public List<TestResult.Status> getStatuses() {
			return statuses;
		}

		public List<TestProcedureOptionModel> getTestProcedures() {
			return testProcedures;
		}

		public List<TestResultStep.Status> getStepStatuses() {
			return stepStatuses;
		}
	}
}

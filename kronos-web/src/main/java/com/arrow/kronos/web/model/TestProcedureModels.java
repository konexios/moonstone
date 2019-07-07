package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.arrow.kronos.data.TestProcedure;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

public class TestProcedureModels {

	public static class TestProcedureModel extends CoreDocumentModel {
		private static final long serialVersionUID = 3329632160362899541L;

		private String name;
		private String description;
		private boolean enabled;
		private String lastModifiedBy;
		private Instant lastModifiedDate;

		public TestProcedureModel() {
			super(null, null);
			this.name = "";
			this.description = "";
			this.enabled = true;
		}

		public TestProcedureModel(TestProcedure testProcedure) {
			super(testProcedure.getId(), testProcedure.getHid());
			this.name = testProcedure.getName();
			this.description = testProcedure.getDescription();
			this.enabled = testProcedure.isEnabled();
			this.lastModifiedBy = testProcedure.getLastModifiedBy();
			this.lastModifiedDate = testProcedure.getLastModifiedDate();
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public boolean isEnabled() {
			return enabled;
		}
		public String getLastModifiedBy() {
			return lastModifiedBy;
		}

		public void setLastModifiedBy(String lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
		}

		public Instant getLastModifiedDate(){
			return lastModifiedDate;
		}
	}

	public static class TestProcedureDetailsModel extends TestProcedureModel {
		private static final long serialVersionUID = -8750795793326802448L;

		private String deviceTypeName;

		public TestProcedureDetailsModel() {
		}

		public TestProcedureDetailsModel(TestProcedure testProcedure) {
			super(testProcedure);
		}

		public String getDeviceTypeName() {
			return deviceTypeName;
		}

		public void setDeviceTypeName(String deviceTypeName) {
			this.deviceTypeName = deviceTypeName;
		}

	}

	public static class TestProcedureCommonModel extends TestProcedureModel {
		private static final long serialVersionUID = -3821532668650858259L;

		private DeviceTypeModels.DeviceTypeOption option = new DeviceTypeModels.DeviceTypeOption();
		private String applicationId;
		private List<TestProcedureStepModel> steps = new ArrayList<>();

		public TestProcedureCommonModel() {}

		public TestProcedureCommonModel(TestProcedure testProcedure) {
			super(testProcedure);
			this.applicationId = testProcedure.getApplicationId();
		}

		public String getApplicationId() {
			return applicationId;
		}

		public void setApplicationId(String applicationId) {
			this.applicationId = applicationId;
		}

		public List<TestProcedureStepModel> getSteps() {
			return steps;
		}

		public TestProcedureCommonModel withSteps(List<TestProcedureStepModel> steps) {
			this.steps = steps;
			return this;
		}

		public DeviceTypeModels.DeviceTypeOption getDeviceTypeOption() {
			return option;
		}

		public TestProcedureCommonModel withDeviceTypeOption(DeviceTypeModels.DeviceTypeOption option) {
			this.option = option;
			return this;
		}

		public void setDeviceTypeOption(DeviceTypeModels.DeviceTypeOption option) {
			this.option = option;
		}
	}

	public static class TestProcedureStepModel implements Serializable{
		private static final long serialVersionUID = -7916098307172891536L;

		private String id;
		private String name;
		private String description;
		private int sortOrder;

		public TestProcedureStepModel() {
		}

		public TestProcedureStepModel(String id, String name, String description, int sortOrder) {
			this.id = id;
			this.name = name;
			this.description = description;
			this.sortOrder = sortOrder;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public int getSortOrder() {
			return sortOrder;
		}

		public void setSortOrder(int sortOrder) {
			this.sortOrder = sortOrder;
		}
	}

	public static class TestProcedureOptionModel implements Serializable {
		private static final long serialVersionUID = 5394962630130187064L;

		private String id;
		private String name;

		public TestProcedureOptionModel() {
		}

		public TestProcedureOptionModel(TestProcedure testProcedure) {
			this.id = testProcedure.getId();
			this.name = testProcedure.getName();
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}

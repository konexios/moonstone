package com.arrow.kronos.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.TestProcedure;
import com.arrow.kronos.data.TestResult;
import com.arrow.kronos.repo.TestResultRepository;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Application;

@Service
public class TestResultService extends KronosServiceAbstract {

	@Autowired
	private TestResultRepository testResultRepository;

	public TestResultRepository getTestResultRepository() {
		return testResultRepository;
	}

	public TestResult create(TestResult testResult, String who) {
		String method = "create";

		// logical checks
		if (testResult == null) {
			logInfo(method, "testResult is null");
			throw new AcsLogicalException("testResult is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// insert
		testResult = testResultRepository.doInsert(testResult, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.TestResult.CreateTestResult)
		        .applicationId(testResult.getApplicationId()).objectId(testResult.getId()).by(who));

		return testResult;
	}

	public TestResult update(TestResult testResult, String who) {
		String method = "update";

		// logical checks
		if (testResult == null) {
			logInfo(method, "testResult is null");
			throw new AcsLogicalException("testResult is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// update
		testResult = testResultRepository.doSave(testResult, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.TestResult.UpdateTestResult)
		        .applicationId(testResult.getApplicationId()).objectId(testResult.getId()).by(who));

		return testResult;
	}

	public void deleteBy(Device device, String who) {
		String method = "deleteBy";

		// logical checks
		if (device == null) {
			logInfo(method, "device is null");
			throw new AcsLogicalException("device is null");
		}

		deleteBy(AcnDeviceCategory.DEVICE, device.getId(), device.getApplicationId(), who);
	}

	public void deleteBy(Gateway gateway, String who) {
		String method = "deleteBy";

		// logical checks
		if (gateway == null) {
			logInfo(method, "gateway is null");
			throw new AcsLogicalException("gateway is null");
		}

		deleteBy(AcnDeviceCategory.GATEWAY, gateway.getId(), gateway.getApplicationId(), who);
	}

	private void deleteBy(AcnDeviceCategory category, String objectId, String applicationId, String who) {
		String method = "deleteBy";
		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// delete
		Long numDeleted = testResultRepository.deleteByDeviceCategoryAndObjectId(category, objectId);
		logInfo(method, "Test results have been deleted for object type=" + category.name() + ", id=" + objectId
		        + ", total " + numDeleted);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.TestResult.DeleteTestResult)
		        .applicationId(applicationId).parameter("deviceCategory", category.name())
		        .parameter("objectId", objectId).parameter("numDeleted", "" + numDeleted).by(who));
	}

	public void moveBy(Device device, Application toApplication, Map<String, TestProcedure> mappedTestProcedures,
	        String who) {
		String method = "moveBy";

		// logical checks
		if (device == null) {
			logInfo(method, "device is null");
			throw new AcsLogicalException("device is null");
		}

		moveBy(AcnDeviceCategory.DEVICE, device.getId(), toApplication, mappedTestProcedures, who);
	}

	public void moveBy(Gateway gateway, Application toApplication, Map<String, TestProcedure> mappedTestProcedures,
	        String who) {
		String method = "moveBy";

		// logical checks
		if (gateway == null) {
			logInfo(method, "gateway is null");
			throw new AcsLogicalException("gateway is null");
		}

		moveBy(AcnDeviceCategory.GATEWAY, gateway.getId(), toApplication, mappedTestProcedures, who);
	}

	private void moveBy(AcnDeviceCategory category, String objectId, Application toApplication,
	        Map<String, TestProcedure> mappedTestProcedures, String who) {
		String method = "moveBy";
		if (toApplication == null) {
			logInfo(method, "toApplication is null");
			throw new AcsLogicalException("toApplication is null");
		}
		if (mappedTestProcedures == null) {
			logInfo(method, "mappedTestProcedures is null");
			throw new AcsLogicalException("mappedTestProcedures is null");
		}
		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		List<TestResult> testResults = testResultRepository.findAllByDeviceCategoryAndObjectId(category, objectId);
		for (TestResult testResult : testResults) {
			String fromApplicationId = testResult.getApplicationId();
			String fromTestProcedureId = testResult.getTestProcedureId();
			TestProcedure testProcedure = mappedTestProcedures.get(fromTestProcedureId);
			Assert.notNull(testProcedure,
			        "failed to map test procedure of test result to the one in target application");
			testResult.setApplicationId(toApplication.getId());
			testResult.setTestProcedureId(testProcedure.getId());

			testResult = this.update(testResult, who);

			// write audit log
			getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.TestResult.MoveTestResultOut)
			        .applicationId(fromApplicationId).objectId(testResult.getId())
			        .parameter("toApplicationId", toApplication.getId())
			        .parameter("fromTestProcedureId", fromTestProcedureId)
			        .parameter("toTestProcedureId", testResult.getTestProcedureId()).by(who));
			getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.TestResult.MoveTestResultIn)
			        .applicationId(testResult.getApplicationId()).objectId(testResult.getId())
			        .parameter("fromApplicationId", fromApplicationId)
			        .parameter("fromTestProcedureId", fromTestProcedureId)
			        .parameter("toTestProcedureId", testResult.getTestProcedureId()).by(who));
		}
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return testResultRepository.deleteByApplicationId(applicationId);
	}
}

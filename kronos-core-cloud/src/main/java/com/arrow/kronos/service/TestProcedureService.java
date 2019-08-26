package com.arrow.kronos.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.TestProcedure;
import com.arrow.kronos.repo.TestProcedureRepository;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Application;

import moonstone.acs.AcsLogicalException;

@Service
public class TestProcedureService extends KronosServiceAbstract {

	@Autowired
	private TestProcedureRepository testProcedureRepository;

	public TestProcedureRepository getTestProcedureRepository() {
		return testProcedureRepository;
	}

	public TestProcedure create(TestProcedure testProcedure, String who) {
		String method = "create";

		// logical checks
		if (testProcedure == null) {
			logInfo(method, "testProcedure is null");
			throw new AcsLogicalException("testProcedure is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// insert
		testProcedure = testProcedureRepository.doInsert(testProcedure, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.TestProcedure.CreateTestProcedure)
		        .applicationId(testProcedure.getApplicationId()).objectId(testProcedure.getId()).by(who)
		        .parameter("name", testProcedure.getName()));

		return testProcedure;
	}

	public TestProcedure update(TestProcedure testProcedure, String who) {
		String method = "update";

		// logical checks
		if (testProcedure == null) {
			logInfo(method, "testProcedure is null");
			throw new AcsLogicalException("testProcedure is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// update
		testProcedure = testProcedureRepository.doSave(testProcedure, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.TestProcedure.UpdateTestProcedure)
		        .applicationId(testProcedure.getApplicationId()).objectId(testProcedure.getId()).by(who)
		        .parameter("name", testProcedure.getName()));

		getKronosCache().clearTestProcedure(testProcedure);

		return testProcedure;
	}

	public TestProcedure clone(TestProcedure fromTestProcedure, Application application, DeviceType deviceType,
	        String who) {
		Assert.notNull(fromTestProcedure, "fromTestProcedure is null");
		Assert.notNull(application, "application is null");
		Assert.notNull(deviceType, "deviceType is null");
		Assert.hasText(who, "who is empty");

		TestProcedure testProcedure = new TestProcedure();
		testProcedure.setApplicationId(application.getId());
		testProcedure.setDeviceTypeId(deviceType.getId());
		testProcedure.setName(fromTestProcedure.getName());
		testProcedure.setDescription(fromTestProcedure.getDescription());
		testProcedure.setEnabled(fromTestProcedure.isEnabled());
		testProcedure.setSteps(fromTestProcedure.getSteps());

		return this.create(testProcedure, who);
	}

	public Map<String, TestProcedure> findOrClone(Iterable<TestProcedure> testProcedures, Application application,
	        Map<String, DeviceType> mappedDeviceTypes, String who) {
		Assert.notNull(testProcedures, "testProcedures is null");
		Assert.notNull(application, "application is null");
		Assert.notNull(mappedDeviceTypes, "mappedDeviceTypes is null");
		Assert.hasText(who, "who is empty");

		Map<String, TestProcedure> result = new HashMap<>();
		for (TestProcedure testProcedure : testProcedures) {
			if (result.containsKey(testProcedure.getId()))
				continue;

			DeviceType deviceType = mappedDeviceTypes.get(testProcedure.getDeviceTypeId());
			Assert.notNull(deviceType, "failed to map device type of test procedure to the one in target application");
			TestProcedure toTestProcedure = testProcedureRepository
			        .findByApplicationIdAndDeviceTypeIdAndEnabled(application.getId(), deviceType.getId(), true);
			if (toTestProcedure == null) {
				toTestProcedure = this.clone(testProcedure, application, deviceType, who);
			}
			result.put(testProcedure.getId(), toTestProcedure);
		}
		return result;
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return testProcedureRepository.deleteByApplicationId(applicationId);
	}
}

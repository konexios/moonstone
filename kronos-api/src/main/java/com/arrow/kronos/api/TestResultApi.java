package com.arrow.kronos.api;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.TestProcedure;
import com.arrow.kronos.data.TestProcedureStep;
import com.arrow.kronos.data.TestResult;
import com.arrow.kronos.data.TestResultStep;
import com.arrow.kronos.repo.TestResultSearchParams;
import com.arrow.kronos.service.TestResultService;
import com.arrow.pegasus.data.AccessKey;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import moonstone.acn.client.model.TestProcedureStepModel;
import moonstone.acn.client.model.TestResultModel;
import moonstone.acn.client.model.TestResultStepModel;
import moonstone.acs.AcsLogicalException;
import moonstone.acs.client.model.PagingResultModel;

@RestController
@RequestMapping("/api/v1/kronos/testresults")
public class TestResultApi extends BaseApiAbstract {
	@Autowired
	private TestResultService testResultService;

	@ApiOperation(value = "list existing TestResults")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public PagingResultModel<TestResultModel> listTestResults(
	        @RequestParam(name = "testProcedureHid", required = false) String testProcedureHid,
	        @RequestParam(name = "objectHid", required = false) String objectHid,
	        @RequestParam(name = "status", required = false) String status,
	        @RequestParam(name = "_page", required = false, defaultValue = "0") int page,
	        @RequestParam(name = "_size", required = false, defaultValue = "100") int size) {

		// validation
		Assert.isTrue(page >= 0, "page must be positive");
		Assert.isTrue(size >= 0 && size <= KronosConstants.PageResult.MAX_SIZE,
		        "size must be between 0 and " + KronosConstants.PageResult.MAX_SIZE);

		PagingResultModel<TestResultModel> result = new PagingResultModel<>();
		result.setPage(page);
		PageRequest pageRequest = PageRequest.of(page, size);
		TestResultSearchParams params = new TestResultSearchParams();

		AccessKey accessKey = validateCanReadApplication(getProductSystemName());
		params.addApplicationIds(accessKey.getApplicationId());

		if (StringUtils.isNotEmpty(objectHid)) {
			params.setObjectId(populateObjectIdByHid(objectHid));
		}

		if (StringUtils.isNotEmpty(status)) {
			params.addStatuses(status);
		}

		if (StringUtils.isNotEmpty(testProcedureHid)) {
			params.addTestProcedureIds(populateTestProcedureIdByHid(testProcedureHid));
		}

		List<TestResultModel> data = new ArrayList<>();
		Page<TestResult> testResults = testResultService.getTestResultRepository().findTestResult(pageRequest, params);

		if (testResults != null) {
			testResults.forEach(testResult -> data.add(buildTestResultModel(testResult)));
		}

		result.setData(data);
		result.setSize(pageRequest.getPageSize());
		result.setTotalPages(testResults.getTotalPages());
		result.setTotalSize(testResults.getTotalElements());

		return result;
	}

	// @ApiOperation(value = "create new test result")
	// @RequestMapping(path = "", method = RequestMethod.POST)
	// public HidModel create(
	// @ApiParam(value = "test result model", required = true)
	// @RequestBody(required = false) TestResultRegistrationModel body) {

	// TestResultModel model = JsonUtils.fromJson(getApiPayload(),
	// TestResultModel.class);
	// Assert.notNull(model, "model is null");
	//
	// AccessKey accessKey = validateCanWriteGateway(model.getObjectHid());
	//
	// TestProcedure testProcedure =
	// getKronosCache().findTestProcedureByHid(model.getTestProcedureHid());
	// Assert.notNull(testProcedure, "testProcedure is null");
	// // TestResult findByTestProcedureId =
	// testResultService.getTestResultRepository().findByTestProcedureId(testProcedure.getId());
	// if (findByTestProcedureId != null) {
	// throw new AcsLogicalException("duplicated test procedure id");
	// }
	//
	// String objectId = populateObjectIdByHid(model.getObjectHid());
	// Assert.hasText(objectId, "objectId is empty! hid=" +
	// model.getObjectHid());
	// TestResult findByObjectId =
	// testResultService.getTestResultRepository().findByObjectId(objectId);
	// if (findByObjectId != null) {
	// throw new AcsLogicalException("duplicated object id");
	// }
	//
	// TestResult testResult = new TestResult();
	// testResult = buildTestResult(testResult, model,
	// getTestProcedureStepsIds(testProcedure));
	// testResult.setApplicationId(accessKey.getApplicationId());
	// testResult = testResultService.create(testResult, accessKey.getId());
	// return new HidModel().withHid(testResult.getHid()).withMessage("OK");
	// }

	// @ApiOperation(value = "update existing test result")
	// @RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
	// public HidModel update(
	// @ApiParam(value = "test result hid", required = true) @PathVariable(value
	// = "hid") String hid,
	// @ApiParam(value = "test result model", required = true)
	// @RequestBody(required = false) TestResultRegistrationModel body) {
	//
	// TestResult testResult =
	// testResultService.getTestResultRepository().doFindByHid(hid);
	// Assert.notNull(testResult, "test result does not exist");
	//
	// TestResultModel model = JsonUtils.fromJson(getApiPayload(),
	// TestResultModel.class);
	// Assert.notNull(model, "model is null");
	// AccessKey accessKey = validateCanWriteGateway(model.getObjectHid());
	// Assert.isTrue(testResult.getApplicationId().equals(accessKey.getApplicationId()),
	// "applicationId mismatched!");
	//
	// TestProcedure testProcedure =
	// getKronosCache().findTestProcedureByHid(model.getTestProcedureHid());
	// Assert.notNull(testProcedure, "testProcedure is null");
	// if (!testProcedure.getId().equals(testResult.getTestProcedureId())) {
	// TestResult findByTestProcedureId =
	// testResultService.getTestResultRepository()
	// .findByTestProcedureId(testProcedure.getId());
	// if (findByTestProcedureId != null) {
	// throw new AcsLogicalException("duplicated test procedure id");
	// }
	// }
	//
	// String objectId = populateObjectIdByHid(model.getObjectHid());
	// Assert.notNull(objectId, "object was not found");
	// if (!objectId.equals(testResult.getObjectId())) {
	// TestResult findByObjectId =
	// testResultService.getTestResultRepository().findByObjectId(objectId);
	// if (findByObjectId != null) {
	// throw new AcsLogicalException("duplicated object id");
	// }
	// }
	//
	// testResult = buildTestResult(testResult, model,
	// getTestProcedureStepsIds(testProcedure));
	// testResult = testResultService.update(testResult, accessKey.getId());
	// return new HidModel().withHid(testResult.getHid()).withMessage("OK");
	// }

	List<String> getTestProcedureStepsIds(TestProcedure testProcedure) {
		return testProcedure.getSteps().stream().map(tp -> tp.getId()).collect(Collectors.toList());
	}

	@ApiOperation(value = "get test result by hid")
	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public TestResultModel getTestResult(
	        @ApiParam(value = "test result hid", required = true) @PathVariable(value = "hid") String hid) {

		Assert.notNull(hid, "testResultHid is null");
		TestResult testResult = testResultService.getTestResultRepository().doFindByHid(hid);
		Assert.notNull(testResult, "test result was not found");

		validateCanRead(testResult.getObjectId());

		return buildTestResultModel(testResult);
	}

	/*
	 * private String populateObjectHidById(TestResult testResult) { String
	 * objectHid = new String(); //switch (testResult.getCategory()) { //case
	 * DEVICE: // Device device =
	 * getKronosCache().findDeviceById(testResult.getObjectId()); //
	 * Assert.notNull(device, "device is null"); // objectHid = device.getHid();
	 * // break; //case GATEWAY: Gateway gateway =
	 * getKronosCache().findGatewayById(testResult.getObjectId());
	 * Assert.notNull(gateway, "gateway is null"); objectHid = gateway.getHid();
	 * // break; //} return objectHid; }
	 */

	private AccessKey validateCanRead(String objectId) {
		Assert.notNull(objectId, "objectId is null");
		AccessKey accessKey = null;
		Gateway gateway = getKronosCache().findGatewayById(objectId);
		if (gateway != null) {
			accessKey = validateCanReadGateway(gateway.getHid());
		} else {
			Device device = getKronosCache().findDeviceById(objectId);
			if (device != null) {
				accessKey = validateCanReadDevice(device.getHid());
			}
		}
		Assert.notNull(accessKey, "object was not validated");
		return accessKey;
	}

	TestResult buildTestResult(TestResult testResult, TestResultModel model, List<String> testProcedureStepsIds) {
		if (testResult == null) {
			testResult = new TestResult();
		}
		// testResult.setCategory(model.getCategory());
		testResult.setStatus(TestResult.Status.valueOf(model.getStatus()));
		testResult.setTestProcedureId(populateTestProcedureIdByHid(model.getTestProcedureHid()));
		testResult.setObjectId(populateObjectIdByHid(model.getObjectHid()));
		testResult.setStarted(Instant.parse(model.getStarted()));
		testResult.setEnded(Instant.parse(model.getEnded()));
		if (model.getSteps() != null) {
			// check duplicates of testProcedureStepIds
			List<String> testProcedureStepIdList = model.getSteps().stream().map(s -> s.getDefinition().getId())
			        .collect(Collectors.toList());
			Set<String> testProcedureStepIdSet = new HashSet<>(testProcedureStepIdList);
			if (testProcedureStepIdList.size() != testProcedureStepIdSet.size()) {
				throw new AcsLogicalException("duplicated test procedure step id");
			}

			List<TestResultStep> steps = new ArrayList<>();
			for (TestResultStepModel stepModel : model.getSteps()) {
				if (!testProcedureStepsIds.contains(stepModel.getDefinition().getId())) {
					throw new AcsLogicalException("test procedure step was not found");
				}
				steps.add(buildTestResultStep(stepModel));
			}
			testResult.setSteps(steps);
		}
		return testResult;
	}

	/*
	 * private String populateObjectIdByHid(TestResultModel model) { String
	 * objectId = new String(); switch (model.getCategory()) { case DEVICE:
	 * Device device = getKronosCache().findDeviceByHid(model.getObjectHid());
	 * Assert.notNull(device, "device is null"); objectId = device.getId();
	 * break; case GATEWAY: Gateway gateway =
	 * getKronosCache().findGatewayByHid(model.getObjectHid());
	 * Assert.notNull(gateway, "gateway is null"); objectId = gateway.getId();
	 * break; } return objectId; }
	 */

	private String populateObjectIdByHid(String hid) {
		Assert.notNull(hid, "objectHid is null");
		String objectId = null;
		Gateway gateway = getKronosCache().findGatewayByHid(hid);
		if (gateway != null) {
			objectId = gateway.getId();
		} else {
			Device device = getKronosCache().findDeviceByHid(hid);
			if (device != null) {
				objectId = device.getId();
			}
		}
		Assert.notNull(objectId, "object was not found");
		return objectId;
	}

	private String populateTestProcedureIdByHid(String testProcedureHid) {
		Assert.notNull(testProcedureHid, "testProcedureHid is null");
		TestProcedure testProcedure = getKronosCache().findTestProcedureByHid(testProcedureHid);
		Assert.notNull(testProcedure, "testProcedure is null");
		return testProcedure.getId();
	}

	private TestResultStep buildTestResultStep(TestResultStepModel stepModel) {
		Assert.notNull(stepModel, "testResultStepModel is null");
		TestResultStep step = new TestResultStep();
		step.setComment(stepModel.getComment());
		step.setError(stepModel.getError());
		step.setDefinition(buildTestProcedureStep(stepModel.getDefinition()));
		step.setStatus(TestResultStep.Status.valueOf(stepModel.getStatus()));
		step.setStarted(Instant.parse(stepModel.getStarted()));
		step.setEnded(Instant.parse(stepModel.getEnded()));
		return step;
	}

	private TestProcedureStep buildTestProcedureStep(TestProcedureStepModel model) {
		TestProcedureStep step = new TestProcedureStep();
		step.setId(model.getId());
		step.setDescription(model.getDescription());
		step.setName(model.getName());
		step.setSortOrder(model.getSortOrder());
		return step;
	}
}

package com.arrow.kronos.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.acn.client.model.ErrorPayload;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.HidModel;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.TestProcedure;
import com.arrow.kronos.data.TestResult;
import com.arrow.kronos.data.TestResultStep;
import com.arrow.kronos.service.TestProcedureService;
import com.arrow.kronos.service.TestResultService;
import com.arrow.kronos.service.TestSuiteService;
import com.arrow.pegasus.NotAuthorizedException;
import com.arrow.pegasus.data.AccessKey;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/kronos/testsuite")
public class TestSuiteApi extends BaseApiAbstract {

	@Autowired
	private TestSuiteService testSuiteService;
	@Autowired
	private TestProcedureService testProcedureService;
	@Autowired
	private TestResultService testResultService;

	@ApiOperation(value = "start new gateway test using first found enabled test procedure")
	@RequestMapping(path = "/gateways/{gatewayHid}", method = RequestMethod.POST)
	public HidModel testGatewayByHid(
			@ApiParam(value = "test gateway hid", required = true) @PathVariable(value = "gatewayHid") String gatewayHid,
			HttpServletRequest request) {
		String method = "testGatewayByHid";
		AccessKey accessKey = validateCanWriteGateway(gatewayHid);
		Gateway gateway = getKronosCache().findGatewayByHid(gatewayHid);

		auditLog(method, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);

		TestProcedure testProcedure = findTestProcedure(gateway.getApplicationId(), gateway.getDeviceTypeId());
		return testGateway(testProcedure, gateway, accessKey);
	}

	@ApiOperation(value = "start new gateway test for test procedure with provided HID")
	@RequestMapping(path = "/gateways/{gatewayHid}/test-procedures/hids/{testProcedureHid}", method = RequestMethod.POST)
	public HidModel testGatewayByHidAndTestProcedureHid(
			@ApiParam(value = "test gateway hid", required = true) @PathVariable(value = "gatewayHid") String gatewayHid,
			@ApiParam(value = "test procedure hid", required = true) @PathVariable(value = "testProcedureHid") String testProcedureHid,
			HttpServletRequest request) {
		String method = "testGatewayByHidAndTestProcedureHid";
		AccessKey accessKey = validateCanWriteGateway(gatewayHid);
		Gateway gateway = getKronosCache().findGatewayByHid(gatewayHid);

		auditLog(method, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);

		TestProcedure testProcedure = findTestProcedure(gateway.getApplicationId(), testProcedureHid,
				gateway.getDeviceTypeId());
		return testGateway(testProcedure, gateway, accessKey);
	}

	@ApiOperation(value = "start new gateway test for test procedure with provided name")
	@RequestMapping(path = "/gateways/{gatewayHid}/test-procedures/names/{testProcedureName}", method = RequestMethod.POST)
	public HidModel testGatewayByHidAndTestProcedureName(
			@ApiParam(value = "test gateway hid", required = true) @PathVariable(value = "gatewayHid") String gatewayHid,
			@ApiParam(value = "test procedure name", required = true) @PathVariable(value = "testProcedureName") String testProcedureName,
			HttpServletRequest request) {
		String method = "testGatewayByHidAndTestProcedureName";
		AccessKey accessKey = validateCanWriteGateway(gatewayHid);
		Gateway gateway = getKronosCache().findGatewayByHid(gatewayHid);

		auditLog(method, gateway.getApplicationId(), gateway.getId(), accessKey.getId(), request);

		TestProcedure testProcedure = findTestProcedureByName(gateway.getApplicationId(), testProcedureName);
		return testGateway(testProcedure, gateway, accessKey);
	}

	@ApiOperation(value = "start new device test using first found enabled test procedure")
	@RequestMapping(path = "/devices/{deviceHid}", method = RequestMethod.POST)
	public HidModel testDeviceByHid(
			@ApiParam(value = "test device hid", required = true) @PathVariable(value = "deviceHid") String deviceHid,
			HttpServletRequest request) {
		String method = "testDeviceByHid";
		AccessKey accessKey = validateCanUpdateDevice(deviceHid);
		Device device = getKronosCache().findDeviceByHid(deviceHid);

		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		TestProcedure testProcedure = findTestProcedure(device.getApplicationId(), device.getDeviceTypeId());
		return testDevice(testProcedure, device, accessKey);
	}

	@ApiOperation(value = "start new device test for test procedure with provided HID")
	@RequestMapping(path = "/devices/{deviceHid}/test-procedures/hids/{testProcedureHid}", method = RequestMethod.POST)
	public HidModel testDeviceByHidAndTestProcedureHid(
			@ApiParam(value = "test device hid", required = true) @PathVariable(value = "deviceHid") String deviceHid,
			@ApiParam(value = "test procedure hid", required = true) @PathVariable(value = "testProcedureHid") String testProcedureHid,
			HttpServletRequest request) {
		String method = "testDeviceByHidAndTestProcedureHid";
		AccessKey accessKey = validateCanUpdateDevice(deviceHid);
		Device device = getKronosCache().findDeviceByHid(deviceHid);

		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		TestProcedure testProcedure = findTestProcedure(device.getApplicationId(), testProcedureHid,
				device.getDeviceTypeId());
		return testDevice(testProcedure, device, accessKey);
	}

	@ApiOperation(value = "start new device test for test procedure with provided name")
	@RequestMapping(path = "/devices/{deviceHid}/test-procedures/names/{testProcedureName}", method = RequestMethod.POST)
	public HidModel testDeviceByHidAndTestProcedureName(
			@ApiParam(value = "test device hid", required = true) @PathVariable(value = "deviceHid") String deviceHid,
			@ApiParam(value = "test procedure name", required = true) @PathVariable(value = "testProcedureName") String testProcedureName,
			HttpServletRequest request) {
		String method = "testDeviceByHidAndTestProcedureName";
		AccessKey accessKey = validateCanUpdateDevice(deviceHid);
		Device device = getKronosCache().findDeviceByHid(deviceHid);

		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		TestProcedure testProcedure = findTestProcedureByName(device.getApplicationId(), testProcedureName);
		return testDevice(testProcedure, device, accessKey);
	}

	@ApiOperation(value = "begin test")
	@RequestMapping(path = "/tests/{testResultHID}/begin", method = RequestMethod.PUT)
	public HidModel beginTest(
			@ApiParam(value = "test result hid", required = true) @PathVariable(value = "testResultHID") String testResultHID,
			HttpServletRequest request) {
		String method = "beginTest";

		// lookup testResult by HID
		TestResult testResult = testResultService.getTestResultRepository().doFindByHid(testResultHID);
		Assert.notNull(testResult, "testResult is not found");

		// access key validation, use the tempCategory property of the
		// testResult to determine access key validation. If the tempCategory is
		// "G" then validateCanWriteGateway. If the tempCategory is "D" then
		// validateCanWriteDevice. Otherwise throw NotAuthorizedException.
		AccessKey accessKey = validateCanTest(testResult);

		auditLog(method, testResult.getApplicationId(), testResult.getId(), accessKey.getId(), request);

		testResult = testSuiteService.beginTest(testResult.getId(), accessKey.getPri());
		return new HidModel().withHid(testResult.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "begin step # of test")
	@RequestMapping(path = "/tests/{testResultHID}/steps/{stepNumber}/begin", method = RequestMethod.PUT)
	public HidModel beginStep(
			@ApiParam(value = "test result hid", required = true) @PathVariable(value = "testResultHID") String testResultHID,
			@ApiParam(value = "test result step number", required = true) @PathVariable(value = "stepNumber") Integer stepNumber,
			HttpServletRequest request) {
		String method = "beginStep";

		// lookup testResult by HID
		TestResult testResult = testResultService.getTestResultRepository().doFindByHid(testResultHID);
		Assert.notNull(testResult, "testResult is not found");

		// access key validation, use the tempCategory property of the
		// testResult to determine access key validation. If the tempCategory is
		// "G" then validateCanWriteGateway. If the tempCategory is "D" then
		// validateCanWriteDevice. Otherwise throw NotAuthorizedException.
		AccessKey accessKey = validateCanTest(testResult);

		auditLog(method, testResult.getApplicationId(), testResult.getId(), accessKey.getId(), request);

		testResult = testSuiteService.beginStep(testResult.getId(), stepNumber, accessKey.getPri());
		return new HidModel().withHid(testResult.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "end step # of test with success")
	@RequestMapping(path = "/tests/{testResultHID}/steps/{stepNumber}/succeeded", method = RequestMethod.PUT)
	public HidModel endStepSucceeded(
			@ApiParam(value = "test result hid", required = true) @PathVariable(value = "testResultHID") String testResultHID,
			@ApiParam(value = "test result step number", required = true) @PathVariable(value = "stepNumber") Integer stepNumber,
			HttpServletRequest request) {
		String method = "endStepSucceeded";

		TestResultStep.Status status = TestResultStep.Status.SUCCESS;

		return endStep(testResultHID, stepNumber, status, method, request);
	}

	@ApiOperation(value = "end step # of test with failure")
	@RequestMapping(path = "/tests/{testResultHID}/steps/{stepNumber}/failed", method = RequestMethod.PUT)
	public HidModel endStepFailed(
			@ApiParam(value = "test result hid", required = true) @PathVariable(value = "testResultHID") String testResultHID,
			@ApiParam(value = "test result step number", required = true) @PathVariable(value = "stepNumber") Integer stepNumber,
			@ApiParam(value = "test result error payload", required = true) @RequestBody(required = false) ErrorPayload body,
			HttpServletRequest request) {
		String method = "endStepFailed";

		ErrorPayload model = JsonUtils.fromJson(getApiPayload(), ErrorPayload.class);
		Assert.notNull(model, "ErrorPayload is null");

		return endStep(testResultHID, stepNumber, TestResultStep.Status.FAIL, model, method, request);
	}

	@ApiOperation(value = "skip step # of test")
	@RequestMapping(path = "/tests/{testResultHID}/steps/{stepNumber}/skip", method = RequestMethod.PUT)
	public HidModel skipStep(
			@ApiParam(value = "test result hid", required = true) @PathVariable(value = "testResultHID") String testResultHID,
			@ApiParam(value = "test result step number", required = true) @PathVariable(value = "stepNumber") Integer stepNumber,
			HttpServletRequest request) {
		String method = "skipStep";

		// lookup testResult by HID
		TestResult testResult = testResultService.getTestResultRepository().doFindByHid(testResultHID);
		Assert.notNull(testResult, "testResult is not found");

		// access key validation, use the tempCategory property of the
		// testResult to determine access key validation. If the tempCategory is
		// "G" then validateCanWriteGateway. If the tempCategory is "D" then
		// validateCanWriteDevice. Otherwise throw NotAuthorizedException.
		AccessKey accessKey = validateCanTest(testResult);

		auditLog(method, testResult.getApplicationId(), testResult.getId(), accessKey.getId(), request);

		testResult = testSuiteService.skipStep(testResult.getId(), stepNumber, accessKey.getPri());
		return new HidModel().withHid(testResult.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "end test")
	@RequestMapping(path = "/tests/{testResultHID}/end", method = RequestMethod.PUT)
	public HidModel endTest(
			@ApiParam(value = "test result hid", required = true) @PathVariable(value = "testResultHID") String testResultHID,
			HttpServletRequest request) {
		String method = "endTest";

		// lookup testResult by HID
		TestResult testResult = testResultService.getTestResultRepository().doFindByHid(testResultHID);
		Assert.notNull(testResult, "testResult is not found");

		// access key validation, use the tempCategory property of the
		// testResult to determine access key validation. If the tempCategory is
		// "G" then validateCanWriteGateway. If the tempCategory is "D" then
		// validateCanWriteDevice. Otherwise throw NotAuthorizedException.
		AccessKey accessKey = validateCanTest(testResult);

		auditLog(method, testResult.getApplicationId(), testResult.getId(), accessKey.getId(), request);

		testResult = testSuiteService.endTest(testResult.getId(), accessKey.getPri());
		return new HidModel().withHid(testResult.getHid()).withMessage("OK");
	}

	private HidModel testGateway(TestProcedure testProcedure, Gateway gateway, AccessKey accessKey) {
		Assert.notNull(testProcedure, "test procedure is not found");
		Assert.isTrue(testProcedure.getDeviceTypeId().equals(gateway.getDeviceTypeId()), "deviceType does not match");

		TestResult testResult = testSuiteService.createTest(testProcedure.getId(), gateway.getId(),
				AcnDeviceCategory.GATEWAY, accessKey.getPri());

		// return an HidModel
		return new HidModel().withHid(testResult.getHid()).withMessage("OK");
	}

	private HidModel testDevice(TestProcedure testProcedure, Device device, AccessKey accessKey) {
		Assert.notNull(testProcedure, "test procedure is not found");
		Assert.isTrue(testProcedure.getDeviceTypeId().equals(device.getDeviceTypeId()), "deviceType does not match");

		TestResult testResult = testSuiteService.createTest(testProcedure.getId(), device.getId(),
				AcnDeviceCategory.DEVICE, accessKey.getPri());

		// return an HidModel with the testResultHID
		return new HidModel().withHid(testResult.getHid()).withMessage("OK");
	}

	private HidModel endStep(String testResultHID, Integer stepNumber, TestResultStep.Status status, String method,
			HttpServletRequest request) {
		return endStep(testResultHID, stepNumber, status, null, method, request);
	}

	private HidModel endStep(String testResultHID, Integer stepNumber, TestResultStep.Status status,
			ErrorPayload errorPayload, String method, HttpServletRequest request) {
		TestResult testResult = testResultService.getTestResultRepository().doFindByHid(testResultHID);
		Assert.notNull(testResult, "testResult is not found");

		// access key validation, use the tempCategory property of the
		// testResult to determine access key validation. If the tempCategory is
		// "G" then validateCanWriteGateway. If the tempCategory is "D" then
		// validateCanWriteDevice. Otherwise throw NotAuthorizedException.
		AccessKey accessKey = validateCanTest(testResult);

		auditLog(method, testResult.getApplicationId(), testResult.getId(), accessKey.getId(), request);

		testResult = testSuiteService.endStep(testResult.getId(), stepNumber, status,
				errorPayload == null ? null : errorPayload.getError(), accessKey.getPri());
		return new HidModel().withHid(testResult.getHid()).withMessage("OK");
	}

	private TestProcedure findTestProcedure(String applicationId, String deviceTypeId) {
		return findTestProcedure(applicationId, null, deviceTypeId);
	}

	private TestProcedure findTestProcedure(String applicationId, String testProcedureHid, String deviceTypeId) {
		TestProcedure testProcedure = null;
		if (StringUtils.isEmpty(testProcedureHid)) {
			testProcedure = testProcedureService.getTestProcedureRepository()
					.findByApplicationIdAndDeviceTypeIdAndEnabled(applicationId, deviceTypeId, true);
		} else {
			testProcedure = getKronosCache().findTestProcedureByHid(testProcedureHid);
		}
		return testProcedure;
	}

	private TestProcedure findTestProcedureByName(String applicationId, String testProcedureName) {
		return testProcedureService.getTestProcedureRepository().findByApplicationIdAndNameAndEnabled(applicationId,
				testProcedureName, true);
	}

	private AccessKey validateCanTest(TestResult testResult) {
		switch (testResult.getDeviceCategory()) {
		case DEVICE:
			Device device = getKronosCache().findDeviceById(testResult.getObjectId());
			Assert.notNull(device, "device is not found");
			return validateCanUpdateDevice(device.getHid());
		case GATEWAY:
			Gateway gateway = getKronosCache().findGatewayById(testResult.getObjectId());
			Assert.notNull(gateway, "gateway is not found");
			return validateCanWriteGateway(gateway.getHid());
		default:
			throw new NotAuthorizedException();
		}
	}
}

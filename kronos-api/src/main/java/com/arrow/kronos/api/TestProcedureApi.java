package com.arrow.kronos.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.TestProcedure;
import com.arrow.kronos.data.TestProcedureStep;
import com.arrow.kronos.repo.TestProcedureSearchParams;
import com.arrow.kronos.service.TestProcedureService;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AuditLog;
import com.arrow.pegasus.service.CryptoService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import moonstone.acn.client.model.TestProcedureModel;
import moonstone.acn.client.model.TestProcedureRegistrationModel;
import moonstone.acn.client.model.TestProcedureStepModel;
import moonstone.acs.AcsLogicalException;
import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.HidModel;
import moonstone.acs.client.model.PagingResultModel;

@RestController
@RequestMapping("/api/v1/kronos/testprocedures")
public class TestProcedureApi extends BaseApiAbstract {

	@Autowired
	private TestProcedureService testProcedureService;
	@Autowired
	private CryptoService cryptoService;

	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public TestProcedureModel findByHid(@PathVariable String hid) {
		validateCanReadTestProcedure(hid);
		return buildTestProcedureModel(getKronosCache().findTestProcedureByHid(hid));
	}

	@ApiOperation(value = "list existing TestProcedures")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public PagingResultModel<TestProcedureModel> findAllBy(
	        @RequestParam(name = "deviceTypeHid", required = false) String deviceTypeHid,
	        @RequestParam(name = "enabled", required = false) String enabled,
	        @RequestParam(name = "_page", required = false, defaultValue = "0") int page,
	        @RequestParam(name = "_size", required = false, defaultValue = "100") int size) {

		// validation
		Assert.isTrue(page >= 0, "page must be positive");
		Assert.isTrue(size >= 0 && size <= KronosConstants.PageResult.MAX_SIZE,
		        "size must be between 0 and " + KronosConstants.PageResult.MAX_SIZE);

		PagingResultModel<TestProcedureModel> result = new PagingResultModel<>();
		result.setPage(page);
		PageRequest pageRequest = PageRequest.of(page, size);
		TestProcedureSearchParams params = new TestProcedureSearchParams();

		AccessKey accessKey = validateCanReadApplication(ProductSystemNames.KRONOS);
		params.addApplicationIds(accessKey.getApplicationId());

		if (StringUtils.isNotEmpty(deviceTypeHid)) {
			params.addDeviceTypeIds(populateDeviceTypeIdByHid(deviceTypeHid, accessKey));
		}

		if (StringUtils.isNotEmpty(enabled)) {
			params.setEnabled(enabled.equalsIgnoreCase(Boolean.TRUE.toString()));
		}

		List<TestProcedureModel> data = new ArrayList<>();
		Page<TestProcedure> testProcedures = testProcedureService.getTestProcedureRepository()
		        .findTestProcedure(pageRequest, params);

		if (testProcedures != null) {
			testProcedures.forEach(tp -> data.add(buildTestProcedureModel(tp)));
		}
		result.setData(data);
		result.setSize(pageRequest.getPageSize());
		result.setTotalPages(testProcedures.getTotalPages());
		result.setTotalSize(testProcedures.getTotalElements());

		return result;
	}

	@ApiOperation(value = "create new test procedure")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public HidModel create(
	        @ApiParam(value = "test procedure model", required = true) @RequestBody(required = false) TestProcedureRegistrationModel body,
	        HttpServletRequest request) {
		String method = "create";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());
		AuditLog auditLog = auditLog(method, accessKey.getApplicationId(), null, accessKey.getId(), request);

		TestProcedureRegistrationModel model = JsonUtils.fromJson(getApiPayload(),
		        TestProcedureRegistrationModel.class);
		Assert.notNull(model, "model is null");
		model.trim();

		TestProcedure testProcedure = new TestProcedure();
		testProcedure = buildTestProcedure(testProcedure, model, accessKey);
		testProcedure.setApplicationId(accessKey.getApplicationId());
		if (model.getSteps() != null) {
			List<TestProcedureStep> testProcedureSteps = new ArrayList<>();
			for (TestProcedureStepModel testProcedureStepModel : model.getSteps()) {
				testProcedureSteps.add(buildTestProcedureStep(testProcedureStepModel));
			}
			testProcedure.setSteps(testProcedureSteps);
		}
		testProcedure = testProcedureService.create(testProcedure, accessKey.getId());
		auditLog.setObjectId(testProcedure.getId());
		getAuditLogService().getAuditLogRepository().doSave(auditLog, accessKey.getId());
		return new HidModel().withHid(testProcedure.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "update existing TestProcedure")
	@RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
	public HidModel update(
	        @ApiParam(value = "test procedure hid", required = true) @PathVariable(value = "hid") String hid,
	        @ApiParam(value = "test procedure model", required = true) @RequestBody(required = false) TestProcedureRegistrationModel body,
	        HttpServletRequest request) {
		String method = "update";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());

		Assert.notNull(hid, "hid is null");
		TestProcedure testProcedure = getKronosCache().findTestProcedureByHid(hid);
		Assert.notNull(testProcedure, "test procedure is null");

		auditLog(method, testProcedure.getApplicationId(), testProcedure.getId(), accessKey.getId(), request);

		TestProcedureRegistrationModel model = JsonUtils.fromJson(getApiPayload(),
		        TestProcedureRegistrationModel.class);
		Assert.notNull(model, "model is null");
		model.trim();

		testProcedure = buildTestProcedure(testProcedure, model, accessKey);
		testProcedure.setSteps(updateTestProcedureSteps(model.getSteps(), testProcedure.getSteps()));
		testProcedure = testProcedureService.update(testProcedure, accessKey.getId());

		return new HidModel().withHid(testProcedure.getHid()).withMessage("OK");
	}

	private TestProcedureModel buildTestProcedureModel(TestProcedure testProcedure) {
		Assert.notNull(testProcedure, "test procedure is null");
		TestProcedureModel model = buildModel(new TestProcedureModel(), testProcedure);
		model.setDeviceTypeHid(populateDeviceTypeHidById(testProcedure.getDeviceTypeId()));
		if (testProcedure.getSteps() != null) {
			List<TestProcedureStepModel> testProcedureSteps = new ArrayList<>();
			for (TestProcedureStep testProcedureStep : testProcedure.getSteps()) {
				testProcedureSteps.add(buildTestProcedureStepModel(testProcedureStep));
			}
			model.setSteps(testProcedureSteps);
		}
		return model;
	}

	private String populateDeviceTypeHidById(String deviceTypeId) {
		String deviceTypeHid = new String();
		DeviceType deviceType = getKronosCache().findDeviceTypeById(deviceTypeId);
		Assert.notNull(deviceType, "device type is null");
		deviceTypeHid = deviceType.getHid();
		return deviceTypeHid;
	}

	private String populateDeviceTypeIdByHid(String deviceTypeHid, AccessKey accessKey) {
		String deviceTypeId = new String();
		DeviceType deviceType = getKronosCache().findDeviceTypeByHid(deviceTypeHid);
		Assert.notNull(deviceType, "device type is null");
		Assert.isTrue(deviceType.getApplicationId().equals(accessKey.getApplicationId()),
		        "applicationId of deviceType mismatched!");
		deviceTypeId = deviceType.getId();
		return deviceTypeId;
	}

	private TestProcedure buildTestProcedure(TestProcedure testProcedure, TestProcedureRegistrationModel model,
	        AccessKey accessKey) {
		if (testProcedure == null) {
			testProcedure = new TestProcedure();
		}
		testProcedure.setEnabled(model.isEnabled());
		testProcedure.setDescription(model.getDescription());
		testProcedure.setName(model.getName());
		testProcedure.setDeviceTypeId(populateDeviceTypeIdByHid(model.getDeviceTypeHid(), accessKey));
		return testProcedure;
	}

	private List<TestProcedureStep> updateTestProcedureSteps(List<TestProcedureStepModel> steps,
	        List<TestProcedureStep> currentSteps) {
		List<TestProcedureStep> testProcedureSteps = new ArrayList<>();

		// check duplicates of ids
		List<String> stepIdList = steps.stream().map(s -> s.getId()).collect(Collectors.toList());
		Set<String> stepIdListWithoutDuplicates = new HashSet<>(stepIdList);
		if (stepIdList.size() != stepIdListWithoutDuplicates.size()) {
			throw new AcsLogicalException("duplicated test procedure step id");
		}

		List<String> currentIds = currentSteps.stream().map(s -> s.getId()).collect(Collectors.toList());
		steps.stream().forEach(stepModel -> {
			String id = null;
			if (stepModel.getId() != null && currentIds.contains(stepModel.getId())) {
				id = stepModel.getId();
			} else {
				id = cryptoService.getCrypto().randomToken();
			}
			testProcedureSteps.add(new TestProcedureStep(id, stepModel.getName(), stepModel.getDescription(),
			        stepModel.getSortOrder()));

		});
		return testProcedureSteps;
	}

	private TestProcedureStep buildTestProcedureStep(TestProcedureStepModel model) {
		TestProcedureStep testProcedureStep = new TestProcedureStep();
		testProcedureStep.setId(cryptoService.getCrypto().randomToken());
		testProcedureStep.setDescription(model.getDescription());
		testProcedureStep.setName(model.getName());
		testProcedureStep.setSortOrder(model.getSortOrder());
		return testProcedureStep;
	}
}

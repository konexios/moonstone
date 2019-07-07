package com.arrow.kronos.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Gateway;
//import com.arrow.kronos.data.Device;
//import com.arrow.kronos.data.DeviceCategory;
//import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.TestProcedure;
import com.arrow.kronos.data.TestProcedureStep;
import com.arrow.kronos.data.TestResult;
import com.arrow.kronos.data.TestResultStep;
import com.arrow.kronos.repo.TestResultSearchParams;
import com.arrow.kronos.service.TestProcedureService;
import com.arrow.kronos.service.TestResultService;
import com.arrow.kronos.web.model.SearchFilterModels.TestResultSearchFilterModel;
import com.arrow.kronos.web.model.SearchResultModels;
import com.arrow.kronos.web.model.TestProcedureModels.TestProcedureOptionModel;
import com.arrow.kronos.web.model.TestProcedureModels.TestProcedureStepModel;
import com.arrow.kronos.web.model.TestResultModels;
import com.arrow.kronos.web.model.TestResultModels.TestResultCommonModel;
import com.arrow.kronos.web.model.TestResultModels.TestResultStepModel;

@RestController
@RequestMapping("/api/kronos/testresult")
public class TestResultController extends BaseControllerAbstract {
    @Autowired
    private TestResultService testResultService;
    @Autowired
    private TestProcedureService testProcedureService;

    @PreAuthorize("hasAuthority('KRONOS_READ_TEST_RESULTS') or hasAuthority('KRONOS_READ_DEVICE_TEST_RESULTS') or hasAuthority('KRONOS_READ_GATEWAY_TEST_RESULTS')")
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public SearchResultModels.TestResultSearchResultModel list(@RequestBody TestResultSearchFilterModel searchFilter,
            HttpSession session) {

        // sorting & paging
        PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
                new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

        TestResultSearchParams params = new TestResultSearchParams();
        params.addApplicationIds(getApplicationId(session));
        if (searchFilter.getStatuses() != null) {
            params.addStatuses(searchFilter.getStatuses());
        }
        if (searchFilter.getTestProcedureIds() != null) {
            params.addTestProcedureIds(searchFilter.getTestProcedureIds());
        }
        /*
         * if(searchFilter.getCategories() != null) {
         * params.addCategories(searchFilter.getCategories()); }
         */
        if (searchFilter.getObjectId() != null) {
            params.setObjectId(searchFilter.getObjectId());
        }
        if (searchFilter.getStepStatuses() != null) {
            params.addStepStatuses(searchFilter.getStepStatuses());
        }

        Page<TestResult> testResults = testResultService.getTestResultRepository().findTestResult(pageRequest, params);

        // convert to visual model
        List<TestResultModels.TestResultDetailsModel> testResultModels = new ArrayList<>();
        for (TestResult testResult : testResults) {
            TestResultModels.TestResultDetailsModel testResultModel = new TestResultModels.TestResultDetailsModel(
                    testResult);
            testResultModel.setTestProcedureName(populateTestProcedureNameById(testResult.getTestProcedureId()));
            testResultModel.setObjectName(populateObjectName(testResult));
            testResultModels.add(testResultModel);
        }

        Page<TestResultModels.TestResultDetailsModel> result = new PageImpl<>(testResultModels, pageRequest,
                testResults.getTotalElements());

        return new SearchResultModels.TestResultSearchResultModel(result, searchFilter);
    }

    @RequestMapping(value = "/options", method = RequestMethod.GET)
    public TestResultModels.TestResultOptionsModel options(HttpSession session) {
        List<TestResult.Status> statuses = new ArrayList<>(TestResult.Status.values().length);
        for (TestResult.Status s : TestResult.Status.values()) {
            statuses.add(s);
        }

        /*
         * List<DeviceCategory> categories = new
         * ArrayList<>(DeviceCategory.values().length); for (DeviceCategory c :
         * DeviceCategory.values()) { categories.add(c); }
         */

        List<TestProcedure> testProcedures = testProcedureService.getTestProcedureRepository()
                .findByApplicationId(getApplicationId(session));
        List<TestProcedureOptionModel> testProcedureOptionModels = new ArrayList<>(testProcedures.size());
        testProcedures.stream().forEach(t -> testProcedureOptionModels.add(new TestProcedureOptionModel(t)));

        return new TestResultModels.TestResultOptionsModel(statuses, /* categories, */ testProcedureOptionModels);
    }

    @PreAuthorize("hasAuthority('KRONOS_READ_TEST_RESULTS')")
    @RequestMapping(value = "/{testResultId}", method = RequestMethod.GET)
    public TestResultModels.TestResultCommonModel getTestResult(@PathVariable String testResultId,
            HttpSession session) {
        Assert.notNull(testResultId, "testResultId is null");
        TestResult testResult = testResultService.getTestResultRepository().findById(testResultId).orElse(null);
        Assert.notNull(testResult, "test result is null");
        Assert.isTrue(getApplicationId(session).equals(testResult.getApplicationId()),
                "user and test result must have the same application id");

        return buildTestResultCommonModel(testResult);
    }

    @PreAuthorize("hasAuthority('KRONOS_UPDATE_TEST_RESULT')")
    @RequestMapping(value = "/{testResultId}", method = RequestMethod.PUT)
    public TestResultCommonModel edit(@PathVariable String testResultId,
            @RequestBody TestResultModels.TestResultCommonModel model, HttpSession session) {
        Assert.notNull(testResultId, "testResultId is null");
        TestResult testResult = testResultService.getTestResultRepository().findById(testResultId).orElse(null);
        Assert.notNull(testResult, "test result is null");
        Assert.isTrue(getApplicationId(session).equals(testResult.getApplicationId()),
                "user and test result must have the same application id");

        testResult.setSteps(updateTestResultSteps(model.getSteps(), testResult.getSteps()));

        testResult = testResultService.update(testResult, getUserId());

        return buildTestResultCommonModel(testResult);
    }

    private List<TestResultStep> updateTestResultSteps(List<TestResultStepModel> testResultStepModels,
            List<TestResultStep> currentSteps) {
        Map<String, TestResultStepModel> updateStepMap = testResultStepModels.stream()
                .collect(Collectors.toMap(x -> x.getDefinition().getId(), x -> x));

        currentSteps.stream().forEach(currentStep -> {
            TestResultStepModel stepModel = updateStepMap.get(currentStep.getDefinition().getId());
            if (stepModel != null) {
                currentStep.setComment(stepModel.getComment());
            }
        });
        return currentSteps;
    }

    private List<TestResultStepModel> buildTestResultStepModel(List<TestResultStep> steps, String testProcedureId) {
        List<TestResultStepModel> testResultStepModels = new ArrayList<>();
        steps.stream().forEach(step -> {

            TestResultStepModel model = new TestResultStepModel(step);
            model.setDefinition(buildTestProcedureStepModel(step.getDefinition()));
            testResultStepModels.add(model);
        });

        if (!testResultStepModels.isEmpty())
            Collections.sort(testResultStepModels, new Comparator<TestResultStepModel>() {
                @Override
                public int compare(TestResultStepModel o1, TestResultStepModel o2) {
                    if (o1.getDefinition().getSortOrder() < o2.getDefinition().getSortOrder())
                        return -1;
                    else if (o1.getDefinition().getSortOrder() > o2.getDefinition().getSortOrder())
                        return 1;
                    else
                        return 0;
                }
            });

        return testResultStepModels;
    }

    private TestResultCommonModel buildTestResultCommonModel(TestResult testResult) {
        TestResultCommonModel model = new TestResultCommonModel(testResult);
        model.setTestProcedureName(populateTestProcedureNameById(testResult.getTestProcedureId()));
        model.setObjectName(populateObjectName(testResult));
        return model.withSteps(buildTestResultStepModel(testResult.getSteps(), testResult.getTestProcedureId()));
    }

    private String populateTestProcedureNameById(String testProcedureId) {
        Assert.notNull(testProcedureId, "test procedure id is null");
        TestProcedure testProcedure = getKronosCache().findTestProcedureById(testProcedureId);
        Assert.notNull(testProcedure, "test procedure is null");
        return testProcedure.getName();
    }

    private String populateObjectName(TestResult testResult) {
        String objectName = null;

        switch (testResult.getDeviceCategory()) {
        case GATEWAY:
            Gateway gateway = getKronosCache().findGatewayById(testResult.getObjectId());
            Assert.notNull(gateway, "gateway is null");
            objectName = gateway.getName() + " - " + gateway.getUid();
            break;
        case DEVICE:
            Device device = getKronosCache().findDeviceById(testResult.getObjectId());
            Assert.notNull(device, "device is null");
            objectName = device.getName() + " - " + device.getUid();
            break;
        default:
            throw new AcsLogicalException(
                    "Unsupported device category! deviceCategory=" + testResult.getDeviceCategory().name());
        }

        return objectName;
    }

    private TestProcedureStepModel buildTestProcedureStepModel(TestProcedureStep step) {
        return new TestProcedureStepModel(step.getId(), step.getName(), step.getDescription(), step.getSortOrder());
    }
}

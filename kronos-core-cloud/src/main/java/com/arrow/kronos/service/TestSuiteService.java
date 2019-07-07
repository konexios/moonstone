package com.arrow.kronos.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.data.TestProcedure;
import com.arrow.kronos.data.TestProcedureStep;
import com.arrow.kronos.data.TestResult;
import com.arrow.kronos.data.TestResultStep;

@Service
public class TestSuiteService extends KronosServiceAbstract {

    @Autowired
    private TestResultService testResultService;

    @Autowired
    private TestProcedureService testProcedureService;

    public TestResult createTest(String testProcedureId, String objectId, AcnDeviceCategory deviceCategory,
            String who) {
        Assert.hasText(objectId, "objectId is empty!");
        Assert.notNull(deviceCategory, "deviceCategory is null!");

        String method = "createTest";

        if (StringUtils.isEmpty(testProcedureId)) {
            logInfo(method, "testProcedureId is empty");
            throw new AcsLogicalException("testProcedureId is empty");
        }

        // lookup test procedure
        TestProcedure testProcedure = testProcedureService.getTestProcedureRepository().findById(testProcedureId)
                .orElse(null);
        if (testProcedure == null) {
            logInfo(method, "testProcedure is null");
            throw new AcsLogicalException("testProcedure is null");
        }

        // create test result (Pending)
        TestResult testResult = new TestResult();
        testResult.setApplicationId(testProcedure.getApplicationId());
        testResult.setObjectId(objectId);
        testResult.setStatus(TestResult.Status.PENDING);
        testResult.setDeviceCategory(deviceCategory);
        testResult.setTestProcedureId(testProcedure.getId());

        // create test steps from test procedure (Pending)
        List<TestResultStep> steps = new ArrayList<>();

        // steps based on test procedure
        List<TestProcedureStep> procedureSteps = testProcedure.getSteps();
        if (procedureSteps == null || procedureSteps.isEmpty()) {
            logInfo(method, "testProcedure does not have defined test steps");
            throw new AcsLogicalException("Test procedure does not have any defined steps!");
        }

        // sort test procedure steps by sort order
        procedureSteps.sort(Comparator.comparing(TestProcedureStep::getSortOrder));
        for (TestProcedureStep testProcedureStep : procedureSteps) {
            TestResultStep step = new TestResultStep();
            step.setDefinition(testProcedureStep);
            step.setStatus(TestResultStep.Status.PENDING);
            steps.add(step);
        }

        testResult.setSteps(steps);

        return testResultService.create(testResult, who);
    }

    public TestResult beginTest(String testResultId, String who) {
        String method = "beginTest";

        if (StringUtils.isEmpty(testResultId)) {
            logInfo(method, "testResultId is empty");
            throw new AcsLogicalException("testResultId is empty");
        }

        // update test result (InProgress & capture started)
        TestResult testResult = testResultService.getTestResultRepository().findById(testResultId).orElse(null);
        if (testResult == null) {
            logInfo(method, "Unable to find testResult! id=" + testResultId);
            throw new AcsLogicalException("Invalid test! Unable to find test for " + testResultId);
        }

        if (testResult.getStatus() != TestResult.Status.PENDING) {
            logInfo(method, "testResult is not ready to begin, status=" + testResult.getStatus().name());
            throw new AcsLogicalException("Invalid request, test result is not Pending!");
        }

        testResult.setStatus(TestResult.Status.INPROGRESS);
        testResult.setStarted(Instant.now());

        return testResultService.update(testResult, who);
    }

    public TestResult endTest(String testResultId, String who) {
        String method = "endTest";

        // update test result (Complete & capture ended)
        TestResult testResult = testResultService.getTestResultRepository().findById(testResultId).orElse(null);
        if (testResult == null) {
            logInfo(method, "Unable to find testResult! id=" + testResultId);
            throw new AcsLogicalException("Invalid test! Unable to find test for " + testResultId);
        }

        if (testResult.getStatus() != TestResult.Status.INPROGRESS) {
            logInfo(method, "testResult is not ready for end, status=" + testResult.getStatus().name());
            throw new AcsLogicalException("Invalid request, test result is not In Progress!");
        }

        testResult.setStatus(TestResult.Status.COMPLETE);
        testResult.setEnded(Instant.now());

        // check steps, any step that is not in a state of Success or Fail
        // update to be Skipped
        for (TestResultStep step : testResult.getSteps()) {
            if (step.getStatus() == TestResultStep.Status.PENDING) {
                step.setStatus(TestResultStep.Status.SKIPPED);
            } else if (step.getStatus() == TestResultStep.Status.SUCCESS
                    || step.getStatus() == TestResultStep.Status.FAIL
                    || step.getStatus() == TestResultStep.Status.SKIPPED) {
                continue;
            } else {
                logInfo(method, "Invalid status for test result step! Step=[" + step.getDefinition().getName() + "]");
                throw new AcsLogicalException(
                        "Invalid status for test result step! Step=[" + step.getDefinition().getName() + "]");
            }
        }

        return testResultService.update(testResult, who);
    }

    public TestResult beginStep(String testResultId, Integer stepNumber, String who) {
        String method = "beginStep";

        // update step (InProgress & capture started)
        TestResult testResult = testResultService.getTestResultRepository().findById(testResultId).orElse(null);
        if (testResult == null) {
            logInfo(method, "Unable to find testResult! id=" + testResultId);
            throw new AcsLogicalException("Invalid test! Unable to find test for " + testResultId);
        }

        if (testResult.getStatus() != TestResult.Status.INPROGRESS) {
            logInfo(method, "testResult is not ready for beginStep, status=" + testResult.getStatus().name());
            throw new AcsLogicalException("Invalid request, test result is not In Progress!");
        }

        TestResultStep testResultStep = getTestResultStep(testResult, stepNumber);

        if (testResultStep.getStatus() != TestResultStep.Status.PENDING) {
            logInfo(method, "testResultStep is not ready for beginStep, status=" + testResultStep.getStatus().name());
            throw new AcsLogicalException("Invalid request, test result step is not Pending!");
        }

        testResultStep.setStatus(TestResultStep.Status.INPROGRESS);
        testResultStep.setStarted(Instant.now());
        testResult.getSteps().set(getStepNumberIndex(stepNumber), testResultStep);

        return testResultService.update(testResult, who);
    }

    public TestResult endStep(String testResultId, Integer stepNumber, TestResultStep.Status status, String error,
            String who) {
        String method = "skipStep";

        // update step (Success/Fail + error & capture ended)
        TestResult testResult = testResultService.getTestResultRepository().findById(testResultId).orElse(null);
        if (testResult == null) {
            logInfo(method, "Unable to find testResult! id=" + testResultId);
            throw new AcsLogicalException("Invalid test! Unable to find test for " + testResultId);
        }

        if (testResult.getStatus() != TestResult.Status.INPROGRESS) {
            logInfo(method, "testResult is not ready for endStep, status=" + testResult.getStatus().name());
            throw new AcsLogicalException("Invalid request, test result is not In Progress!");
        }

        // if the status is Fail then require the error to be not empty
        if (status == TestResultStep.Status.FAIL && StringUtils.isEmpty(error)) {
            logInfo(method, "error is empty");
            throw new AcsLogicalException("Error is required for Failed test step!");
        }

        TestResultStep testResultStep = getTestResultStep(testResult, stepNumber);

        if (testResultStep.getStatus() != TestResultStep.Status.INPROGRESS) {
            logInfo(method, "testResultStep is not ready for endStep, status=" + testResultStep.getStatus().name());
            throw new AcsLogicalException("Invalid request, test result step is not In Progress!");
        }

        testResultStep.setError(error);
        testResultStep.setStatus(status);
        testResultStep.setEnded(Instant.now());
        testResult.getSteps().set(getStepNumberIndex(stepNumber), testResultStep);

        return testResultService.update(testResult, who);
    }

    public TestResult skipStep(String testResultId, Integer stepNumber, String who) {
        String method = "skipStep";

        // update step (Skipped)
        TestResult testResult = testResultService.getTestResultRepository().findById(testResultId).orElse(null);
        if (testResult == null) {
            logInfo(method, "Unable to find testResult! id=" + testResultId);
            throw new AcsLogicalException("Invalid test! Unable to find test for " + testResultId);
        }

        if (testResult.getStatus() != TestResult.Status.INPROGRESS) {
            logInfo(method, "testResult is not ready for skipStep, status=" + testResult.getStatus().name());
            throw new AcsLogicalException("Invalid request, test result is not In Progress!");
        }

        TestResultStep testResultStep = getTestResultStep(testResult, stepNumber);

        if (testResultStep.getStatus() != TestResultStep.Status.PENDING) {
            logInfo(method, "testResultStep is not ready for skipStep, status=" + testResultStep.getStatus().name());
            throw new AcsLogicalException("Invalid request, test result step is not Pending!");
        }

        testResultStep.setStatus(TestResultStep.Status.SKIPPED);
        testResult.getSteps().set(getStepNumberIndex(stepNumber), testResultStep);

        return testResultService.update(testResult, who);
    }

    private TestResultStep getTestResultStep(TestResult testResult, int stepNumber) {
        String method = "getTestResultStep";

        if (testResult == null || testResult.getSteps() == null || testResult.getSteps().isEmpty())
            return null;

        if (stepNumber <= 0) {
            logInfo(method, "Invalid step number! stepNumber=" + stepNumber);
            throw new AcsLogicalException("Invalid step number!");
        }

        TestResultStep testResultStep = null;
        try {
            testResultStep = testResult.getSteps().get(getStepNumberIndex(stepNumber));
        } catch (Exception e) {
            logInfo(method, "Invalid step number! stepNumber=" + stepNumber);
            logInfo(method, e.getMessage());
            throw new AcsLogicalException("Invalid step number!");
        }

        return testResultStep;
    }

    private Integer getStepNumberIndex(int stepNumber) {
        String method = "getStepNumberIndex";

        if (stepNumber <= 0) {
            logInfo(method, "Invalid step number! stepNumber=" + stepNumber);
            throw new AcsLogicalException("Invalid step number!");
        }

        return (stepNumber - 1);
    }
}

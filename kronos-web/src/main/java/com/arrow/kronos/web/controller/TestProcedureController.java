package com.arrow.kronos.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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

import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.TestProcedure;
import com.arrow.kronos.data.TestProcedureStep;
import com.arrow.kronos.repo.TestProcedureSearchParams;
import com.arrow.kronos.service.TestProcedureService;
import com.arrow.kronos.web.model.DeviceTypeModels;
import com.arrow.kronos.web.model.SearchFilterModels.TestProcedureSearchFilterModel;
import com.arrow.kronos.web.model.SearchResultModels;
import com.arrow.kronos.web.model.TestProcedureModels;
import com.arrow.kronos.web.model.TestProcedureModels.TestProcedureStepModel;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.service.CryptoService;

@RestController
@RequestMapping("/api/kronos/testprocedure")
public class TestProcedureController extends BaseControllerAbstract {
    @Autowired
    private TestProcedureService testProcedureService;
    @Autowired
    private CryptoService cryptoService;

    @PreAuthorize("hasAuthority('KRONOS_READ_TEST_PROCEDURES')")
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public SearchResultModels.TestProcedureSearchResultModel list(
            @RequestBody TestProcedureSearchFilterModel searchFilter, HttpSession session) {

        // sorting & paging
        PageRequest pageRequest = new PageRequest(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
                new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

        TestProcedureSearchParams params = new TestProcedureSearchParams();
        params.addApplicationIds(getApplicationId(session));
        params.setEnabled(searchFilter.isEnabled());
        params.addDeviceTypeIds(searchFilter.getDeviceTypeIds());

        Page<TestProcedure> testProcedures = testProcedureService.getTestProcedureRepository()
                .findTestProcedure(pageRequest, params);

        // convert to visual model
        List<TestProcedureModels.TestProcedureDetailsModel> testProcedureModels = new ArrayList<>();
        for (TestProcedure testProcedure : testProcedures) {
            TestProcedureModels.TestProcedureDetailsModel testProcedureModel = new TestProcedureModels.TestProcedureDetailsModel(
                    testProcedure);
            DeviceType deviceType = getDeviceTypeService().getDeviceTypeRepository()
                    .findById(testProcedure.getDeviceTypeId()).orElse(null);
            testProcedureModel.setDeviceTypeName(deviceType.getName());

            User currentUser = getCoreCacheService().findUserById(testProcedureModel.getLastModifiedBy());
            if (currentUser != null) {
                testProcedureModel.setLastModifiedBy(getKronosModelUtil().populateDecryptedLogin(currentUser));
            } else if (!testProcedureModel.getLastModifiedBy().equals("admin")
                    && !testProcedureModel.getLastModifiedBy().equals("demo")) {
                testProcedureModel.setLastModifiedBy("Unknown");
            }

            testProcedureModels.add(testProcedureModel);
        }

        Page<TestProcedureModels.TestProcedureDetailsModel> result = new PageImpl<>(testProcedureModels, pageRequest,
                testProcedures.getTotalElements());

        return new SearchResultModels.TestProcedureSearchResultModel(result, searchFilter);
    }

    @PreAuthorize("hasAuthority('KRONOS_READ_TEST_PROCEDURES')")
    @RequestMapping(value = "/{testProcedureId}", method = RequestMethod.GET)
    public TestProcedureModels.TestProcedureCommonModel getTestProcedure(@PathVariable String testProcedureId,
            HttpSession session) {
        Assert.notNull(testProcedureId, "testProcedureId is null");
        TestProcedure testProcedure = getKronosCache().findTestProcedureById(testProcedureId);
        Assert.notNull(testProcedure, "test procedure is null");
        Assert.isTrue(getApplicationId(session).equals(testProcedure.getApplicationId()),
                "user and test procedure must have the same application id");

        DeviceType deviceType = getKronosCache().findDeviceTypeById(testProcedure.getDeviceTypeId());
        Assert.notNull(deviceType, "Device Type is null");

        return new TestProcedureModels.TestProcedureCommonModel(testProcedure)
                .withDeviceTypeOption(new DeviceTypeModels.DeviceTypeOption(deviceType))
                .withSteps(buildTestProcedureStepModel(testProcedure.getSteps()));
    }

    @RequestMapping(value = "/options", method = RequestMethod.GET)
    public List<DeviceTypeModels.DeviceTypeOption> options(HttpSession session) {
        List<DeviceType> deviceTypes = getDeviceTypeService().getDeviceTypeRepository()
                .findByApplicationIdAndEnabled(getApplicationId(session), true);
        List<DeviceTypeModels.DeviceTypeOption> options = new ArrayList<>(deviceTypes.size());
        for (DeviceType deviceType : deviceTypes) {
            options.add(new DeviceTypeModels.DeviceTypeOption(deviceType));
        }

        options.sort(Comparator.comparing(DeviceTypeModels.DeviceTypeOption::getName, String.CASE_INSENSITIVE_ORDER));

        return options;
    }

    @PreAuthorize("hasAuthority('KRONOS_CREATE_TEST_PROCEDURE')")
    @RequestMapping(method = RequestMethod.POST)
    public TestProcedureModels.TestProcedureCommonModel add(
            @RequestBody TestProcedureModels.TestProcedureCommonModel model, HttpSession session) {

        TestProcedure testProcedure = new TestProcedure();
        testProcedure.setApplicationId(getApplicationId(session)); // mandatory
        testProcedure.setName(model.getName());
        testProcedure.setDescription(model.getDescription());
        testProcedure.setEnabled(model.isEnabled());

        DeviceType deviceType = getKronosCache().findDeviceTypeById(model.getDeviceTypeOption().getId());
        Assert.notNull(deviceType, "Device Type is null");
        Assert.isTrue(getApplicationId(session).equals(deviceType.getApplicationId()),
                "test procedure and device type must have the same application id");

        testProcedure.setDeviceTypeId(deviceType.getId());

        List<TestProcedureStep> testProcedureSteps = new ArrayList<>();
        model.getSteps().stream().forEach(stepModel -> {
            testProcedureSteps.add(new TestProcedureStep(cryptoService.getCrypto().randomToken(), stepModel.getName(),
                    stepModel.getDescription(), stepModel.getSortOrder()));
        });
        testProcedure.setSteps(testProcedureSteps);

        testProcedure = testProcedureService.create(testProcedure, getUserId());

        return new TestProcedureModels.TestProcedureCommonModel(testProcedure)
                .withDeviceTypeOption(new DeviceTypeModels.DeviceTypeOption(deviceType))
                .withSteps(buildTestProcedureStepModel(testProcedure.getSteps()));
    }

    @PreAuthorize("hasAuthority('KRONOS_UPDATE_TEST_PROCEDURE')")
    @RequestMapping(value = "/{testProcedureId}", method = RequestMethod.PUT)
    public TestProcedureModels.TestProcedureCommonModel edit(@PathVariable String testProcedureId,
            @RequestBody TestProcedureModels.TestProcedureCommonModel model, HttpSession session) {

        TestProcedure testProcedure = getKronosCache().findTestProcedureById(testProcedureId);
        Assert.notNull(testProcedureId, "test procedure is null");

        testProcedure.setName(model.getName());
        testProcedure.setDescription(model.getDescription());
        testProcedure.setEnabled(model.isEnabled());
        testProcedure.setSteps(updateTestProcedureStep(model.getSteps(), testProcedure.getSteps()));

        DeviceType deviceType = getKronosCache().findDeviceTypeById(model.getDeviceTypeOption().getId());
        Assert.notNull(deviceType, "Device Type is null");
        Assert.isTrue(testProcedure.getApplicationId().equals(deviceType.getApplicationId()),
                "test procedure and device type must have the same application id");
        testProcedure.setDeviceTypeId(deviceType.getId());

        testProcedure = testProcedureService.update(testProcedure, getUserId());

        return new TestProcedureModels.TestProcedureCommonModel(testProcedure)
                .withDeviceTypeOption(new DeviceTypeModels.DeviceTypeOption(deviceType))
                .withSteps(buildTestProcedureStepModel(testProcedure.getSteps()));
    }

    private List<TestProcedureStep> updateTestProcedureStep(List<TestProcedureStepModel> steps,
            List<TestProcedureStep> currentSteps) {
        List<TestProcedureStep> testProcedureSteps = new ArrayList<>();
        List<String> currentIds = currentSteps.stream().map(s -> s.getId()).collect(Collectors.toList());
        steps.stream().forEach(stepModel -> {
            String id = null;
            if (stepModel.getId() != null && currentIds.contains(stepModel.getId())) {
                id = stepModel.getId();
            } else if (stepModel.getId() == null) {
                id = cryptoService.getCrypto().randomToken();
            }
            testProcedureSteps.add(new TestProcedureStep(id, stepModel.getName(), stepModel.getDescription(),
                    stepModel.getSortOrder()));

        });
        return testProcedureSteps;
    }

    private List<TestProcedureStepModel> buildTestProcedureStepModel(List<TestProcedureStep> steps) {
        List<TestProcedureStepModel> testProcedureStepModels = new ArrayList<>();
        steps.stream().forEach(step -> {
            testProcedureStepModels.add(new TestProcedureStepModel(step.getId(), step.getName(), step.getDescription(),
                    step.getSortOrder()));
        });

        if (!testProcedureStepModels.isEmpty())
            Collections.sort(testProcedureStepModels, new Comparator<TestProcedureStepModel>() {
                @Override
                public int compare(TestProcedureStepModel o1, TestProcedureStepModel o2) {
                    if (o1.getSortOrder() < o2.getSortOrder())
                        return -1;
                    else if (o1.getSortOrder() > o2.getSortOrder())
                        return 1;
                    else
                        return 0;
                }
            });

        return testProcedureStepModels;
    }
}

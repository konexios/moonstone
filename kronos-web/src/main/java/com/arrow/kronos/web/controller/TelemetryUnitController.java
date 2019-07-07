package com.arrow.kronos.web.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acs.JsonUtils;
import com.arrow.kronos.data.TelemetryUnit;
import com.arrow.kronos.repo.KronosDocumentSearchParams;
import com.arrow.kronos.service.TelemetryUnitService;
import com.arrow.kronos.web.exception.InvalidInputException;
import com.arrow.kronos.web.model.SearchFilterModels.KronosSearchFilterModel;
import com.arrow.kronos.web.model.SearchResultModels;
import com.arrow.kronos.web.model.TelemetryUnitModels.TelemetryUnitModel;
import com.arrow.pegasus.data.profile.User;

@RestController
@RequestMapping("/api/kronos/telemetryunit")
public class TelemetryUnitController extends BaseControllerAbstract {

    private static final int MAX_NAME_LENGTH = 1000;

    @Autowired
    private TelemetryUnitService telemetryUnitService;

    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public SearchResultModels.TelemetryUnitSearchResultModel list(@RequestBody KronosSearchFilterModel searchFilter) {
        Assert.isTrue(getAuthenticatedUser().isAdmin(), "Not authorized");

        // sorting & paging
        PageRequest pageRequest = new PageRequest(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
                new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

        KronosDocumentSearchParams params = new KronosDocumentSearchParams();
        params.setEnabled(searchFilter.isEnabled());

        Page<TelemetryUnit> telemetryUnits = telemetryUnitService.getTelemetryUnitRepository()
                .findTelemetryUnits(pageRequest, params);

        // convert to visual model
        List<TelemetryUnitModel> telemetryUnitModels = new ArrayList<>();
        for (TelemetryUnit telemetryUnit : telemetryUnits) {
            User currentUser = getCoreCacheService().findUserById(telemetryUnit.getLastModifiedBy());
            if (currentUser != null) {
                telemetryUnit.setLastModifiedBy(getKronosModelUtil().populateDecryptedLogin(currentUser));
            } else if (!telemetryUnit.getLastModifiedBy().equals("admin") && !telemetryUnit.getLastModifiedBy().equals("demo")) {
                telemetryUnit.setLastModifiedBy("Unknown");
            }

            telemetryUnitModels.add(new TelemetryUnitModel(telemetryUnit));
        }
        Page<TelemetryUnitModel> result = new PageImpl<>(telemetryUnitModels, pageRequest,
                telemetryUnits.getTotalElements());

        return new SearchResultModels.TelemetryUnitSearchResultModel(result, searchFilter);
    }

    @RequestMapping(method = RequestMethod.POST)
    public TelemetryUnitModel add(@RequestBody TelemetryUnitModel telemetryUnitModel) {
        Assert.isTrue(getAuthenticatedUser().isAdmin(), "Not authorized");

        validateTelemetryUnitModel(telemetryUnitModel);

        TelemetryUnit telemetryUnit = new TelemetryUnit();
        telemetryUnit.setName(telemetryUnitModel.getName());
        telemetryUnit.setDescription(telemetryUnitModel.getDescription());
        telemetryUnit.setSystemName(telemetryUnitModel.getSystemName());
        telemetryUnit.setEnabled(telemetryUnitModel.isEnabled());

        telemetryUnit = telemetryUnitService.create(telemetryUnit, getUserId());

        return new TelemetryUnitModel(telemetryUnit);
    }

    @RequestMapping(value = "/{telemetryUnitId}", method = RequestMethod.PUT)
    public TelemetryUnitModel edit(@PathVariable String telemetryUnitId,
            @RequestBody TelemetryUnitModel telemetryUnitModel) {
        Assert.isTrue(getAuthenticatedUser().isAdmin(), "Not authorized");

        TelemetryUnit telemetryUnit = getKronosCache().findTelemetryUnitById(telemetryUnitId);
        Assert.notNull(telemetryUnit, "Telemetry Unit is null");

        validateTelemetryUnitModel(telemetryUnitModel, telemetryUnitId);

        telemetryUnit.setName(telemetryUnitModel.getName());
        telemetryUnit.setDescription(telemetryUnitModel.getDescription());
        telemetryUnit.setSystemName(telemetryUnitModel.getSystemName());
        telemetryUnit.setEnabled(telemetryUnitModel.isEnabled());

        telemetryUnit = telemetryUnitService.update(telemetryUnit, getUserId());

        return new TelemetryUnitModel(telemetryUnit);
    }

    private void validateTelemetryUnitModel(TelemetryUnitModel telemetryUnitModel) {
    	validateTelemetryUnitModel(telemetryUnitModel, null);
    }

    private void validateTelemetryUnitModel(TelemetryUnitModel telemetryUnitModel, String telemetryUnitId) {
       Assert.notNull(telemetryUnitModel, "telemetryUnitModel is null");
       Assert.hasText(telemetryUnitModel.getSystemName(), "systemName is empty");
       Assert.hasText(telemetryUnitModel.getName(), "name is empty");
       Assert.hasText(telemetryUnitModel.getDescription(), "description is empty");

       telemetryUnitModel.setSystemName(telemetryUnitModel.getSystemName().trim());

       Map<String, String> inputValidation = new HashMap<>();
       if (telemetryUnitModel.getName().getBytes(StandardCharsets.UTF_8).length > MAX_NAME_LENGTH) {
           inputValidation.put("name", "Name is too long");
       }
       if (telemetryUnitModel.getSystemName().getBytes(StandardCharsets.UTF_8).length > MAX_NAME_LENGTH) {
           inputValidation.put("systemName", "System Name is too long");
       } else {
           TelemetryUnit foundBySystemName = getKronosCache()
                   .findTelemetryUnitBySystemName(telemetryUnitModel.getSystemName());
           if (foundBySystemName != null
                   && (telemetryUnitId == null || !foundBySystemName.getId().equals(telemetryUnitId))) {
               inputValidation.put("systemName", "System Name already exists");
           }
       }
       if (!inputValidation.isEmpty()) {
           throw new InvalidInputException(JsonUtils.toJson(inputValidation));
       }
    }
}

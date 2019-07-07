package com.arrow.rhea.web.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.arrow.rhea.data.DeviceManufacturer;
import com.arrow.rhea.repo.DeviceManufacturerSearchParams;
import com.arrow.rhea.service.DeviceManufacturerService;
import com.arrow.rhea.web.model.DeviceManufacturerModels.DeviceManufacturerModel;
import com.arrow.rhea.web.model.SearchFilterModels.DeviceManufacturerSearchFilterModel;
import com.arrow.rhea.web.model.SearchResultModels.DeviceManufacturerSearchResultModel;

@RestController
@RequestMapping("/api/rhea/device-manufacturers")
public class DeviceManufacturerController extends ControllerAbstract {

    @Autowired
    private DeviceManufacturerService deviceManufacturerService;

    @PreAuthorize("hasAuthority('RHEA_READ_DEVICE_MANUFACTURERS')")
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public DeviceManufacturerSearchResultModel find(@RequestBody DeviceManufacturerSearchFilterModel searchFilter) {

        // sorting & paging
        PageRequest pageRequest = new PageRequest(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
                new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

        DeviceManufacturerSearchParams params = new DeviceManufacturerSearchParams();
        // implied/enforced filter
        params.addCompanyIds(getAuthenticatedUser().getCompanyId());
        // user defined filter
        params.setEnabled(searchFilter.isEnabled());

        Page<DeviceManufacturer> deviceManufacturers = deviceManufacturerService.getDeviceManufacturerRepository()
                .findDeviceManufacturers(pageRequest, params);

        // convert to visual model
        List<DeviceManufacturerModel> deviceManufacturerModels = new ArrayList<>();
        for (DeviceManufacturer deviceManufacturer : deviceManufacturers) {
            deviceManufacturerModels.add(new DeviceManufacturerModel(deviceManufacturer));
        }

        Page<DeviceManufacturerModel> result = new PageImpl<>(deviceManufacturerModels, pageRequest,
                deviceManufacturers.getTotalElements());

        return new DeviceManufacturerSearchResultModel(result, searchFilter);
    }

    @PreAuthorize("hasAuthority('RHEA_CREATE_DEVICE_MANUFACTURER')")
    @RequestMapping(method = RequestMethod.POST)
    public DeviceManufacturerModel create(@RequestBody DeviceManufacturerModel model) {
        Assert.notNull(model, "model is null");

        DeviceManufacturer deviceManufacturer = new DeviceManufacturer();
        deviceManufacturer.setName(model.getName());
        deviceManufacturer.setDescription(model.getDescription());
        deviceManufacturer.setEnabled(model.isEnabled());

        deviceManufacturer = deviceManufacturerService.create(deviceManufacturer, getUserId());

        return new DeviceManufacturerModel(deviceManufacturer);
    }

    @PreAuthorize("hasAuthority('RHEA_UPDATE_DEVICE_MANUFACTURER')")
    @RequestMapping(value = "/{deviceManufacturerId}", method = RequestMethod.PUT)
    public DeviceManufacturerModel update(@PathVariable String deviceManufacturerId,
            @RequestBody DeviceManufacturerModel model) {
        Assert.hasText(deviceManufacturerId, "deviceManufacturerId is empty");
        Assert.notNull(model, "model is null");

        DeviceManufacturer deviceManufacturer = deviceManufacturerService.getDeviceManufacturerRepository()
                .findById(deviceManufacturerId).orElse(null);
        Assert.notNull(deviceManufacturer,
                "Unable to find deviceManufacturer! deviceManufacturerId=" + deviceManufacturerId);
        Assert.isTrue(deviceManufacturer.isEditable(), "deviceManufacturer is not editable");

        deviceManufacturer.setName(model.getName());
        deviceManufacturer.setDescription(model.getDescription());
        deviceManufacturer.setEnabled(model.isEnabled());

        deviceManufacturer = deviceManufacturerService.update(deviceManufacturer, getUserId());

        return new DeviceManufacturerModel(deviceManufacturer);
    }
}

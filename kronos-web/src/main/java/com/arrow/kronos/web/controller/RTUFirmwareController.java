package com.arrow.kronos.web.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
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
import com.arrow.kronos.data.EligibleFirmwareChangeGroup;
import com.arrow.kronos.service.RTUFirmwareService;
import com.arrow.kronos.web.model.DeviceTypeModels.DeviceTypeOption;
import com.arrow.kronos.web.model.RTUFirmwareModels.RTUAvailableFirmwareModel;
import com.arrow.kronos.web.model.RTUFirmwareModels.RTURequestFirmwareModel;
import com.arrow.kronos.web.model.SearchFilterModels.RTUFirmwareAvailableFilterOptions;
import com.arrow.kronos.web.model.SearchFilterModels.RTUFirmwareRequestedFilterOptions;
import com.arrow.kronos.web.model.SearchFilterModels.RTURequestedSearchFilterModel;
import com.arrow.kronos.web.model.SearchFilterModels.RTUSearchFilterModel;
import com.arrow.kronos.web.model.SearchResultModels.RTUSearchResultModel;
import com.arrow.kronos.web.model.SoftwareReleaseScheduleModels.AvailableFirmwareVersion;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.rhea.client.api.ClientRTURequestApi;
import com.arrow.rhea.data.RTURequest;
import com.arrow.rhea.data.SoftwareRelease;

import moonstone.acn.client.model.RightToUseStatus;
import moonstone.acs.client.model.StatusModel;

@RestController
@RequestMapping("/api/kronos/rtu")
public class RTUFirmwareController extends BaseControllerAbstract {

    @Autowired
    private ClientRTURequestApi clientRTURequestApi;

    @Autowired
    private RTUFirmwareService rtuFirmwareService;

    @RequestMapping(value = "/options", method = RequestMethod.GET)
    public RTUFirmwareRequestedFilterOptions filterOptions(HttpSession session) {
        return new RTUFirmwareRequestedFilterOptions(EnumSet.allOf(RightToUseStatus.class));
    }

    @RequestMapping(value = "/options/available", method = RequestMethod.GET)
    public RTUFirmwareAvailableFilterOptions filterOptionsAvailable(HttpSession session) {
        List<RTUAvailableFirmwareModel> eligibleFirmwareChangeGroups = getRTUAvailableModelList(null, session);
        List<DeviceTypeOption> deviceTypeModels = eligibleFirmwareChangeGroups.stream()
                .map(group -> group.getDeviceType()).distinct().collect(Collectors.toList());
        return new RTUFirmwareAvailableFilterOptions(deviceTypeModels);
    }

    @PreAuthorize("hasAuthority('KRONOS_RIGHT_TO_USE_FIRMWARE')")
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public RTUSearchResultModel getRequestedFirmware(@RequestBody RTURequestedSearchFilterModel searchFilter,
            HttpSession session) {

        // sorting & paging
        PageRequest pageRequest = new PageRequest(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
                new Sort(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

        Application app = getApplication(session);
        // findAll(PageRequest pageRequest, String[] companyIds, String[]
        // statuses, String[] softwareReleaseIds)
        Page<RTURequest> rtuRequests = clientRTURequestApi.findAll(pageRequest, new String[] { app.getCompanyId() },
                searchFilter.getStatuses(), null);

        // convert to visual model
        List<RTURequestFirmwareModel> rrtuRequestFirmwareModels = new ArrayList<>();
        for (RTURequest rtuRequest : rtuRequests) {
            RTURequestFirmwareModel rtuRequestFirmwareModel = new RTURequestFirmwareModel();
            SoftwareRelease softwareRelease = getRheaCacheService()
                    .findSoftwareReleaseById(rtuRequest.getSoftwareReleaseId());
            if (softwareRelease != null) {
                rtuRequestFirmwareModel.withOwnerEmail(softwareRelease.getOwnerEmail());
                rtuRequestFirmwareModel.withOwnerName(softwareRelease.getOwnerName());
            }
            rtuRequestFirmwareModel.setAvailableFirmwareVersion(
                    new AvailableFirmwareVersion(rtuRequest.getSoftwareReleaseId(), rtuRequest.getFirmwareVersion()));
            rtuRequestFirmwareModel.withStatus(rtuRequest.getStatus().toString());

            rrtuRequestFirmwareModels.add(rtuRequestFirmwareModel);
        }
        Page<RTURequestFirmwareModel> result = new PageImpl<>(rrtuRequestFirmwareModels, pageRequest,
                rtuRequests.getTotalElements());

        return new RTUSearchResultModel(result, searchFilter);
    }

    @PreAuthorize("hasAuthority('KRONOS_RIGHT_TO_USE_FIRMWARE')")
    @RequestMapping(value = "/find/available", method = RequestMethod.POST)
    public List<RTUAvailableFirmwareModel> getAvailableFirmware(@RequestBody RTUSearchFilterModel searchFilter,
            HttpSession session) {

        List<DeviceType> deviceTypes = null;
        if (searchFilter != null && searchFilter.getDeviceTypeIds() != null
                && searchFilter.getDeviceTypeIds().length > 0) {
            deviceTypes = new ArrayList<>();
            for (String deviceTypeId : searchFilter.getDeviceTypeIds()) {
                DeviceType assetType = getKronosCache().findDeviceTypeById(deviceTypeId);
                if (assetType != null)
                    deviceTypes.add(assetType);
            }
        }

        return getRTUAvailableModelList(deviceTypes, session);
    }

    private List<RTUAvailableFirmwareModel> getRTUAvailableModelList(List<DeviceType> deviceTypes,
            HttpSession session) {
        Application app = getApplication(session);
        List<EligibleFirmwareChangeGroup> eligibleFirmwareChangeGroups = rtuFirmwareService
                .getRTUEligibleUpgrades(app.getId(), deviceTypes);

        List<RTUAvailableFirmwareModel> list = new ArrayList<>();
        eligibleFirmwareChangeGroups.forEach(item -> {
            DeviceType assetType = getKronosCache().findDeviceTypeById(item.deviceTypeId);
            RTUAvailableFirmwareModel rtuAvailableFirmwareModel = new RTUAvailableFirmwareModel();
            rtuAvailableFirmwareModel.withDeviceType(new DeviceTypeOption(assetType));
            rtuAvailableFirmwareModel.withNumberOfAssets(item.numberOfDevices);
            rtuAvailableFirmwareModel.withHardwareVersionName(getHardwareVersionName(item.rheaDeviceTypeId));
            rtuAvailableFirmwareModel.withCurrentFirmwareVersion(new AvailableFirmwareVersion(item.softwareReleaseId,
                    getSoftwareReleaseName(item.softwareReleaseId)));

            List<AvailableFirmwareVersion> availableFirmwareVersions = new ArrayList<>();
            item.newSoftwareReleaseIds.forEach(softwareReleaseId -> {

                RTURequest checkRequest = getRheaCacheService().findRTURequestByName(app.getCompanyId(),
                        softwareReleaseId);
                if (checkRequest == null) {
                    availableFirmwareVersions.add(new AvailableFirmwareVersion((softwareReleaseId),
                            getSoftwareReleaseName(softwareReleaseId)));
                }

            });

            if (!availableFirmwareVersions.isEmpty()) {
                availableFirmwareVersions.sort(Comparator.comparing(AvailableFirmwareVersion::getSoftwareReleaseName,
                        String.CASE_INSENSITIVE_ORDER));
                rtuAvailableFirmwareModel.withAvailableFirmwareVersions(availableFirmwareVersions);
                list.add(rtuAvailableFirmwareModel);
            }
        });

        if (!list.isEmpty())
            list.sort(
                    Comparator.comparing(RTUAvailableFirmwareModel::getDeviceTypeName, String.CASE_INSENSITIVE_ORDER));

        return list;
    }

    @PreAuthorize("hasAuthority('KRONOS_RIGHT_TO_REQUEST_FIRMWARE')")
    @RequestMapping(value = "/request/{softwareReleaseId}", method = RequestMethod.PUT)
    public StatusModel request(@PathVariable String softwareReleaseId, HttpSession session) {
        Assert.hasText(softwareReleaseId, "softwareReleaseId is empty");
        Application app = getApplication(session);
        // check if RTURequest with unique combination
        // deviceTypeId+softwareReleaseId already exists
        RTURequest checkRequest = getRheaCacheService().findRTURequestByName(app.getCompanyId(), softwareReleaseId);
        if (checkRequest == null) {
            RTURequest request = new RTURequest();
            Assert.notNull(request, "rtuRequest is not found");
            request.setCompanyId(app.getCompanyId());
            request.setFirmwareVersion(getSoftwareReleaseName(softwareReleaseId));
            request.setSoftwareReleaseId(softwareReleaseId);
            request = clientRTURequestApi.create(request, getUserId());
            if (request == null)
                return StatusModel.error("RTURequest was not created.");
        } else {
            return StatusModel.error("RTURequest with softwareRelease:" + getSoftwareReleaseName(softwareReleaseId)
                    + " already exists.");
        }

        return StatusModel.OK;
    }

}

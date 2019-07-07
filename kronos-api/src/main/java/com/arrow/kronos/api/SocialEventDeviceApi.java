package com.arrow.kronos.api;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arrow.acn.client.model.CreateSocialEventDeviceModel;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.acs.client.model.StatusModel;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.api.model.SocialEventDeviceModel;
import com.arrow.kronos.data.SocialEventDevice;
import com.arrow.kronos.repo.SocialEventDeviceSearchParams;
import com.arrow.kronos.service.SocialEventDeviceService;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AuditLog;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/kronos/social/event/devices")
public class SocialEventDeviceApi extends BaseApiAbstract {

    @Autowired
    private SocialEventDeviceService socialEventDeviceService;

    @ApiIgnore
    @ApiOperation(value = "find social event device by hid", response = SocialEventDeviceModel.class)
    @RequestMapping(path = "/{hid}", method = RequestMethod.GET)
    public SocialEventDeviceModel findByHid(@PathVariable(name = "hid") String hid) {
        getValidatedAccessKey(getProductSystemName());
        SocialEventDevice socialEventDevice = getKronosCache().findSocialEventDeviceByHid(hid);
        Assert.notNull(socialEventDevice, "softwareReleaseSchedule is not found");
        return buildSocialEventDeviceModel(socialEventDevice);
    }

    @ApiIgnore
    @ApiOperation(value = "find social event devices by device type hid", response = SocialEventDeviceModel.class)
    @RequestMapping(path = "/device/type/{deviceTypeHid}", method = RequestMethod.GET)
    public List<SocialEventDeviceModel> findByDeviceTypeHid(
            @PathVariable(name = "deviceTypeHid") String deviceTypeHid) {
        getValidatedAccessKey(getProductSystemName());
        SocialEventDeviceSearchParams params = new SocialEventDeviceSearchParams();
        params.addDeviceTypeIds(deviceTypeHid);
        List<SocialEventDevice> socialEventDevices = socialEventDeviceService.findSocialEventDevices(params);
        List<SocialEventDeviceModel> result = socialEventDevices.stream()
                .map(socialEventDevice -> buildSocialEventDeviceModel(socialEventDevice))
                .collect(Collectors.toCollection(ArrayList::new));
        return result;
    }

    private SocialEventDeviceModel buildSocialEventDeviceModel(SocialEventDevice socialEventDevice) {
        SocialEventDeviceModel result = new SocialEventDeviceModel();
        // result.setCreatedBy(socialEventDevice.getCreatedBy().toString());
        // result.setCreatedDate(socialEventDevice.getCreatedDate().toString());
        // result.setLastModifiedDate(socialEventDevice.getLastModifiedDate().toString());
        // result.setLastModifiedBy(socialEventDevice.getLastModifiedBy());
        // result.setHid(socialEventDevice.getHid());
        result.setMacAddress(socialEventDevice.getMacAddress());
        result.setDeviceTypeHid(socialEventDevice.getDeviceTypeId());
        result.setPinCode(socialEventDevice.getPinCode());
        return result;
    }

    @ApiIgnore
    @ApiOperation(value = "create new social event device", response = HidModel.class)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public HidModel create(@RequestBody(required = false) CreateSocialEventDeviceModel body,
            HttpServletRequest request) {
        String method = "create";

        AccessKey accessKey = validateCanWriteApplication(getProductSystemName());

        AuditLog auditLog = auditLog(method, accessKey.getApplicationId(), null, accessKey.getId(), request);

        CreateSocialEventDeviceModel model = JsonUtils.fromJson(getApiPayload(), CreateSocialEventDeviceModel.class);
        Assert.notNull(model, "model is null");
        Assert.hasText(model.getDeviceTypeHid(), "deviceTypeHid is empty");
        Assert.hasText(model.getMacAddress(), "macAddress is empty");
        Assert.hasText(model.getPinCode(), "pinCode is empty");

        SocialEventDevice socialEventDevice = new SocialEventDevice();
        socialEventDevice.setDeviceTypeId(model.getDeviceTypeHid());
        socialEventDevice.setMacAddress(model.getMacAddress());
        socialEventDevice.setPinCode(model.getPinCode());

        socialEventDevice = socialEventDeviceService.create(socialEventDevice, accessKey.getPri());

        auditLog.setObjectId(socialEventDevice.getId());
        getAuditLogService().getAuditLogRepository().doSave(auditLog, accessKey.getId());

        return new HidModel().withHid(socialEventDevice.getHid()).withMessage("OK");
    }

    @ApiIgnore
    @ApiOperation(value = "update existing social event device", response = HidModel.class)
    @RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
    public HidModel update(@PathVariable(name = "hid") String hid,
            @RequestBody(required = false) CreateSocialEventDeviceModel body, HttpServletRequest request) {
        String method = "update";

        AccessKey accessKey = validateCanWriteApplication(getProductSystemName());
        CreateSocialEventDeviceModel model = JsonUtils.fromJson(getApiPayload(), CreateSocialEventDeviceModel.class);

        SocialEventDevice socialEventDevice = socialEventDeviceService.getSocialEventDeviceRepository()
                .doFindByHid(hid);
        Assert.notNull(socialEventDevice, "socialEventDevice is not found");

        auditLog(method, accessKey.getApplicationId(), socialEventDevice.getId(), accessKey.getId(), request);

        socialEventDevice = buildSocialEventDevice(model, socialEventDevice);

        socialEventDevice = socialEventDeviceService.update(socialEventDevice, accessKey.getPri());

        return new HidModel().withHid(socialEventDevice.getHid()).withMessage("OK");
    }

    private SocialEventDevice buildSocialEventDevice(CreateSocialEventDeviceModel model,
            SocialEventDevice socialEventDevice) {
        Assert.notNull(model, "model is null");
        Assert.notNull(socialEventDevice, "socialEventDevice is null");

        if (StringUtils.hasText(model.getMacAddress())) {
            socialEventDevice.setMacAddress(model.getMacAddress());
        }
        if (StringUtils.hasText(model.getPinCode())) {
            socialEventDevice.setPinCode(model.getPinCode());
        }
        if (StringUtils.hasText(model.getDeviceTypeHid())) {
            socialEventDevice.setDeviceTypeId(model.getDeviceTypeHid());
        }
        return socialEventDevice;
    }

    @ApiIgnore
    @ApiOperation(value = "find social event devices")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public PagingResultModel<SocialEventDeviceModel> findAllBy(
            @RequestParam(name = "deviceTypeHids", required = false) Set<String> deviceTypeHids,
            @RequestParam(name = "updatedBefore", required = false) String updatedBefore,
            @RequestParam(name = "updatedAfter", required = false) String updatedAfter,
            @RequestParam(name = "createdBefore", required = false) String createdBefore,
            @RequestParam(name = "createdAfter", required = false) String createdAfter,
            @RequestParam(name = "macAddresses", required = false) Set<String> macAddresses,
            @RequestParam(name = "_page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "_size", required = false, defaultValue = "100") int size) {

        Assert.isTrue(page >= 0, "page must be positive");
        Assert.isTrue(size >= 0 && size <= KronosConstants.PageResult.MAX_SIZE,
                "size must be between 0 and " + KronosConstants.PageResult.MAX_SIZE);

        getValidatedAccessKey(getProductSystemName());

        PagingResultModel<SocialEventDeviceModel> result = new PagingResultModel<>();
        result.setPage(page);
        PageRequest pageRequest = PageRequest.of(page, size);

        SocialEventDeviceSearchParams params = new SocialEventDeviceSearchParams();

        if (StringUtils.hasText(updatedBefore)) {
            params.setUpdatedBefore(Instant.parse(updatedBefore));
        }

        if (StringUtils.hasText(updatedAfter)) {
            params.setUpdatedAfter(Instant.parse(updatedAfter));
        }

        if (StringUtils.hasText(createdBefore)) {
            params.setCreatedBefore(Instant.parse(createdBefore));
        }

        if (StringUtils.hasText(createdAfter)) {
            params.setCreatedAfter(Instant.parse(createdAfter));
        }

        if (deviceTypeHids != null) {
            deviceTypeHids.forEach(hid -> params.addDeviceTypeIds(hid));
        }

        if (macAddresses != null) {
            macAddresses.forEach(macAddress -> params.addMacAddresses(macAddress));
        }

        Page<SocialEventDevice> socialEventDevices = socialEventDeviceService.findSocialEventDevices(pageRequest,
                params);

        List<SocialEventDeviceModel> data = socialEventDevices.getContent().stream()
                .map(socialEventDevice -> buildSocialEventDeviceModel(socialEventDevice))
                .collect(Collectors.toCollection(ArrayList::new));

        result.withTotalPages(socialEventDevices.getTotalPages()).withTotalSize(socialEventDevices.getTotalElements())
                .withSize(socialEventDevices.getNumberOfElements()).withData(data);
        return result;
    }

    /**
     * Usage example: curl -X POST --header "Content-Type: multipart/form-data"
     * --header "Accept: application/json" --header "x-auth-token: <apiKey>" -F
     * deviceTypeName=<deviceTypeName> -F file="@<pathToXlsFile>"
     * "<baseURL>/api/v1/kronos/social/event/devices/import/xls"
     */
    @ApiIgnore
    @RequestMapping(path = "/import/xls", method = RequestMethod.POST, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public StatusModel importSpreadsheet(@RequestPart(value = "file", required = true) MultipartFile file,
            @RequestPart(value = "deviceTypeName", required = true) String deviceTypeName, HttpServletRequest request) {
        String method = "importSpreadsheet";

        AccessKey accessKey = validateCanWriteApplication(getProductSystemName());

        auditLog(method, accessKey.getApplicationId(), null, accessKey.getId(), request);

        try {
            List<String> log = socialEventDeviceService.importSpreadsheet(file.getInputStream(), deviceTypeName,
                    accessKey.getId());
            return StatusModel.OK.withMessage(String.join("\n", log));
        } catch (Exception e) {
            return StatusModel.error(e.getMessage());
        }
    }
}

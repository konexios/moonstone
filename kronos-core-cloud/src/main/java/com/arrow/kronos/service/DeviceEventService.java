package com.arrow.kronos.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.DeviceEvent;
import com.arrow.kronos.repo.DeviceEventRepository;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Application;

@Service
public class DeviceEventService extends KronosServiceAbstract {

    @Autowired
    private DeviceEventRepository deviceEventRepository;

    public DeviceEventRepository getDeviceEventRepository() {
        return deviceEventRepository;
    }

    public boolean createOrUpdate(DeviceEvent event, DeviceAction action) {
        String key = new DeviceActionKeyBuilder(event, action).build();
        return doCreateOrUpdate(event, key);
    }

    public boolean createOrUpdate(DeviceEvent event, DeviceActionWrapper action) {
        String key = new DeviceActionKeyBuilder(event, action).build();
        return doCreateOrUpdate(event, key);
    }

    private boolean doCreateOrUpdate(DeviceEvent event, String key) {
        String method = "createOrUpdate";

        // try to push EMPTY id first
        boolean success = getRedis().opsForValue().setIfAbsent(key, "");
        if (success) {
            long expires = event.getExpires();
            // TODO need to revisit
            // make sure there's an expiration
            if (expires <= 0) {
                expires = KronosConstants.DeviceEvent.DEFAULT_EXPIRES_SECS;
            }
            deviceEventRepository.doInsert(event, null);
            getRedis().opsForValue().set(key, event.getId(), expires, TimeUnit.SECONDS);
            logInfo(method, "set new key: %s, expires: %d", key, expires);
            return true;
        } else {
            String id = getRedis().opsForValue().get(key);
            if (StringUtils.isEmpty(id)) {
                logError(method, "deviceEvent is either expired or not fully created yet for: %s", key);
            } else {
                long result = deviceEventRepository.incrementCounter(id);
                logInfo(method, "incrementCounter result: %d", result);
            }
            return false;
        }
    }

    public DeviceEvent update(DeviceEvent event, String who) {
        Assert.notNull(event, "device event is null");

        // find device object since we need applicationId for audit log
        Assert.notNull(event.getDeviceId(), "device id is null");
        Device device = getKronosCache().findDeviceById(event.getDeviceId());
        Assert.notNull(device, "device is null");

        event = deviceEventRepository.doSave(event, who);

        getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceEvent.UpdateDeviceEvent)
                .applicationId(device.getApplicationId()).productName(ProductSystemNames.KRONOS).objectId(event.getId())
                .by(who).parameter("status", event.getStatus().name()));

        return event;
    }

    public void deleteBy(Device device, String who) {
        String method = "deleteBy";

        Assert.notNull(device, "device is null");
        Assert.notNull(who, "who is null");

        Long numDeleted = deviceEventRepository.deleteByDeviceId(device.getId());

        logInfo(method, "Device events have been deleted for device id=" + device.getId() + ", total " + numDeleted);

        if (numDeleted > 0) {
            // write audit log
            getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceEvent.DeleteDeviceEvent)
                    .applicationId(device.getApplicationId()).parameter("deviceId", device.getId())
                    .parameter("numDeleted", "" + numDeleted).by(who));
        }
    }

    public DeviceEvent populate(DeviceEvent deviceEvent) {

        if (!StringUtils.isEmpty(deviceEvent.getDeviceActionTypeId()))
            deviceEvent.setRefDeviceActionType(
                    getKronosCache().findDeviceActionTypeById(deviceEvent.getDeviceActionTypeId()));

        return deviceEvent;
    }

    public void moveBy(Device device, Application toApplication, Map<String, DeviceActionType> mappedDeviceActionTypes,
            String who) {
        Assert.notNull(device, "device is null");
        Assert.notNull(toApplication, "toApplication is null");
        Assert.notNull(mappedDeviceActionTypes, "mappedDeviceActionTypes is null");
        Assert.notNull(who, "who is null");

        List<DeviceEvent> events = deviceEventRepository.findAllByDeviceId(device.getId());
        for (DeviceEvent event : events) {
            if (event.getDeviceActionTypeId() != null) {
                DeviceActionType toDeviceActionType = mappedDeviceActionTypes.get(event.getDeviceActionTypeId());
                Assert.notNull(toDeviceActionType,
                        "failed to map device action type of device event to the one in target application");

                String fromDeviceActionTypeId = event.getDeviceActionTypeId();
                event.setDeviceActionTypeId(toDeviceActionType.getId());
                event = this.update(event, who);

                getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceEvent.MoveDeviceEventOut)
                        .applicationId(device.getApplicationId()).productName(ProductSystemNames.KRONOS)
                        .objectId(event.getId()).by(who).parameter("fromDeviceActionType", fromDeviceActionTypeId)
                        .parameter("toDeviceActionType", toDeviceActionType.getId()));
                getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceEvent.MoveDeviceEventIn)
                        .applicationId(toApplication.getId()).productName(ProductSystemNames.KRONOS)
                        .objectId(event.getId()).by(who).parameter("fromDeviceActionType", fromDeviceActionTypeId)
                        .parameter("toDeviceActionType", toDeviceActionType.getId()));
            }
        }
    }

    public Long deleteByApplicationId(String applicationId, String who) {
        String method = "deleteByApplicationId";
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(who, "who is empty");
        logInfo(method, "applicationId: %s, who: %s", applicationId, who);
        return deviceEventRepository.deleteByApplicationId(applicationId);
    }

    public static class DeviceActionKeyBuilder {

        private String deviceId;
        private String deviceActionTypeId;
        private String criteria;
        private Map<String, String> parameters;
        private String ownerId;
        private int index;

        public DeviceActionKeyBuilder(DeviceEvent event, DeviceAction action) {
            this.deviceId = event.getDeviceId();
            this.deviceActionTypeId = action.getDeviceActionTypeId();
            this.criteria = action.getCriteria();
            this.parameters = action.getParameters();
        }

        public DeviceActionKeyBuilder(DeviceEvent event, DeviceActionWrapper action) {
            this.deviceId = event.getDeviceId();
            this.deviceActionTypeId = action.getDeviceActionTypeId();
            this.criteria = action.getCriteria();
            this.parameters = action.getParameters();
            this.ownerId = action.getOwnerId();
            this.index = action.getIndex();
        }

        public String build() {
            HashCodeBuilder hashBuilder = new HashCodeBuilder().append(deviceId).append(deviceActionTypeId)
                    .append(criteria).append(index).append(ownerId);
            if (parameters != null) {
                parameters.forEach((key, value) -> hashBuilder.append(key).append(value));
            }
            return String.format("Action-%s/%s/%s", deviceId, deviceActionTypeId,
                    Integer.toHexString(hashBuilder.build()));
        }
    }
}

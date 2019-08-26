package com.arrow.kronos.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.LastTelemetryItem;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.kronos.repo.LastTelemetryItemRepository;
import com.arrow.pegasus.CoreConstant;

import moonstone.acs.KeyValuePair;

@Service
public class LastTelemetryItemService extends KronosServiceAbstract {

    @Autowired
    private LastTelemetryItemRepository lastTelemetryItemRepository;

    private Map<String, KeyValuePair<String, Long>> map = new ConcurrentHashMap<String, KeyValuePair<String, Long>>();

    public LastTelemetryItemRepository getLastTelemetryItemRepository() {
        return lastTelemetryItemRepository;
    }

    public void update2(TelemetryItem item) {
        String method = "update2";

        String key = item.getDeviceId() + "/" + item.getName();
        KeyValuePair<String, Long> existing = map.get(key);

        boolean create = existing == null;

        // try to look up existing in database (only once!)
        if (create) {
            List<LastTelemetryItem> items = lastTelemetryItemRepository.findByDeviceIdAndName(item.getDeviceId(),
                    item.getName());
            if (items.size() > 0) {
                LastTelemetryItem last = items.get(items.size() - 1);
                map.put(key, existing = new KeyValuePair<String, Long>(last.getId(), last.getTimestamp()));
                create = false;

                // clean up duplicates
                if (items.size() > 1) {
                    for (int i = 0; i < items.size() - 1; i++) {
                        try {
                            LastTelemetryItem delete = items.get(i);
                            logInfo(method, "deleting duplicate record: %s", delete.getId());
                            lastTelemetryItemRepository.deleteById(delete.getId());
                        } catch (Exception e) {
                            logError(method, e);
                        }
                    }
                }
            }
        }

        boolean update = existing != null && existing.getValue() < item.getTimestamp();
        if (!create && !update) {
            return;
        }

        LastTelemetryItem result = new LastTelemetryItem();
        if (update) {
            result.setId(existing.getKey());
        }

        result.setDeviceId(item.getDeviceId());
        result.setName(item.getName());
        result.setType(item.getType());
        result.setApplicationId(item.getApplicationId());
        result.setTimestamp(item.getTimestamp());
        result.setBinaryValue(item.getBinaryValue());
        result.setBoolValue(item.getBoolValue());
        result.setDatetimeValue(item.getDatetimeValue());
        result.setDateValue(item.getDateValue());
        result.setFloatCubeValue(item.getFloatCubeValue());
        result.setFloatSqrValue(item.getFloatSqrValue());
        result.setFloatValue(item.getFloatValue());
        result.setIntCubeValue(item.getIntCubeValue());
        result.setIntSqrValue(item.getIntSqrValue());
        result.setIntValue(item.getIntValue());
        result.setStrValue(item.getStrValue());

        if (create) {
            lastTelemetryItemRepository.doInsert(result, CoreConstant.ADMIN_USER);
            logDebug(method, "inserted new item for %s / %s", result.getDeviceId(), result.getName());
        } else if (update) {
            lastTelemetryItemRepository.doSave(result, CoreConstant.ADMIN_USER);
            logInfo(method, "updated new value for %s / %s", result.getDeviceId(), result.getName());
        }
        map.put(key, new KeyValuePair<String, Long>(result.getId(), result.getTimestamp()));
    }

    public LastTelemetryItem update(TelemetryItem item) {
        String method = "update";

        List<LastTelemetryItem> items = lastTelemetryItemRepository.findByDeviceIdAndName(item.getDeviceId(),
                item.getName());
        boolean create = items == null || items.isEmpty();

        LastTelemetryItem result = null;
        if (create) {
            result = new LastTelemetryItem();
            result.setDeviceId(item.getDeviceId());
            result.setName(item.getName());
            result.setType(item.getType());
        } else {
            result = items.get(0);

            // clean up duplicates
            if (items.size() > 1) {
                for (int i = 1; i < items.size(); i++) {
                    try {
                        LastTelemetryItem delete = items.get(i);
                        logInfo(method, "deleting duplicate record: %s", delete.getId());
                        lastTelemetryItemRepository.deleteById(delete.getId());
                    } catch (Exception e) {
                        logError(method, e);
                    }
                }
            }
        }

        result.setApplicationId(item.getApplicationId());
        // result.setTimestamp(item.getTimestamp());

        result.setBinaryValue(item.getBinaryValue());
        result.setBoolValue(item.getBoolValue());
        result.setDatetimeValue(item.getDatetimeValue());
        result.setDateValue(item.getDateValue());
        result.setFloatCubeValue(item.getFloatCubeValue());
        result.setFloatSqrValue(item.getFloatSqrValue());
        result.setFloatValue(item.getFloatValue());
        result.setIntCubeValue(item.getIntCubeValue());
        result.setIntSqrValue(item.getIntSqrValue());
        result.setIntValue(item.getIntValue());
        result.setStrValue(item.getStrValue());

        if (create) {
            result = lastTelemetryItemRepository.doInsert(result, CoreConstant.ADMIN_USER);
            logDebug(method, "inserted new item for %s / %s", result.getDeviceId(), result.getName());
        } else {
            if (item.getTimestamp() > result.getTimestamp()) {
                result.setTimestamp(item.getTimestamp());
                result = lastTelemetryItemRepository.doSave(result, CoreConstant.ADMIN_USER);
                logInfo(method, "updated new value for %s / %s", result.getDeviceId(), result.getName());
            } else {
                logDebug(method, "ignored obsolete value for %s / %s", result.getDeviceId(), result.getName());
            }
        }

        return result;
    }

    public void deleteBy(Device device) {
        String method = "deleteBy";
        Assert.notNull(device, "device is null");
        Long numDeleted = lastTelemetryItemRepository.deleteByDeviceId(device.getId());
        logInfo(method,
                "Last telemetry items have been deleted for device id=" + device.getId() + ", total " + numDeleted);
    }

    public Long deleteByApplicationId(String applicationId, String who) {
        String method = "deleteByApplicationId";
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(who, "who is empty");
        logInfo(method, "applicationId: %s, who: %s", applicationId, who);
        return lastTelemetryItemRepository.deleteByApplicationId(applicationId);
    }
}

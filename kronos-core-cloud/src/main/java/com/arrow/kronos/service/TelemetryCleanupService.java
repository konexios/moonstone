package com.arrow.kronos.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog.TelemetryCleanup;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.KronosApplication;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.service.AuditLogService;

@Service
public class TelemetryCleanupService extends KronosServiceAbstract {

    @Autowired
    private TelemetryService telemetryService;
    @Autowired
    private TelemetryItemService telemetryItemService;
    @Autowired
    private KronosCache kronosCache;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private KronosApplicationService kronosApplicationService;
    @Autowired
    private SpringDataEsTelemetryItemService springDataEsTelemetryItemService;

    private static final String AUDIT_LOG_PARAM_COUNT = "count";
    private static final String AUDIT_LOG_PARAM_TIMESTAMP = "timestamp";

    public void removeByDeviceId(String deviceId, int numberOfDays) {
        Assert.hasText(deviceId, "deviceId is empty");
        Assert.isTrue(numberOfDays >= 0, "numberOfDays must be positive");

        String method = "removeByDeviceId";

        Device device = kronosCache.findDeviceById(deviceId);
        Assert.notNull(device, "device is not found");
        Instant minDateThreshold = getMinDateThreshold(numberOfDays);
        logDebug(method, "numberOfDays: %s, minDateThreshold: %s", numberOfDays, minDateThreshold);

        doRemoveTelemetryByDeviceId(deviceId, minDateThreshold);
    }

    public void removeByGatewayId(String gatewayId, int numberOfDays) {
        Assert.hasText(gatewayId, "gatewayId is empty");
        Assert.isTrue(numberOfDays >= 0, "numberOfDays must be positive");

        String method = "removeByGatewayId";

        Gateway gateway = kronosCache.findGatewayById(gatewayId);
        Assert.notNull(gateway, "gateway is not found");
        Instant minDateThreshold = getMinDateThreshold(numberOfDays);
        logDebug(method, "numberOfDays: %s, minThreshold: %s", numberOfDays, minDateThreshold);

        deviceService.getDeviceRepository().findAllByGatewayId(gatewayId)
                .forEach(device -> doRemoveTelemetryByDeviceId(device.getId(), minDateThreshold));
    }

    public List<String> removeAll(int numberOfDays) throws Exception {
        String method = "removeAll";
        Assert.isTrue(numberOfDays >= 0, "numberOfDays must be positive");

        long toTimestamp = getMinDateThreshold(numberOfDays).toEpochMilli();
        logInfo(method, "numberOfDays: %d, toTimestamp: %d", numberOfDays, toTimestamp);

        AtomicLong applicationCount = new AtomicLong(0);
        AtomicLong telemetryItemCount = new AtomicLong(0);
        AtomicLong telemetryCount = new AtomicLong(0);

        // TODO need to revisit
        // AtomicLong esTelemetryItemCount = new AtomicLong(0);

        Stream<KronosApplication> stream = kronosApplicationService.getKronosApplicationRepository().findAll()
                .parallelStream();
        new ForkJoinPool().submit(() -> stream.forEach(kronosApp -> {
            logInfo(method, "applicationId: %s, retentionDays: %d", kronosApp.getApplicationId(),
                    kronosApp.getTelemetryRetentionDays());
            if (kronosApp.getTelemetryRetentionDays() > 0) {
                telemetryItemCount
                        .addAndGet(doRemoveTelemetryItemsByApplicationId(kronosApp.getApplicationId(), toTimestamp));
                telemetryCount.addAndGet(doRemoveTelemetriesByApplicationId(kronosApp.getApplicationId(), toTimestamp));

                // TODO need to revisit
                // esTelemetryItemCount.addAndGet(doRemoveEsTelemetryItemsByApplicationId(application.getId(),
                // toTimestamp));
                applicationCount.incrementAndGet();
            }
        })).get();

        List<String> result = new ArrayList<>();
        result.add("Applications: " + applicationCount.get() + " records affected");
        result.add("TelemetryItem: " + telemetryItemCount.get() + " records deleted");
        result.add("Telemetry: " + telemetryCount.get() + " records deleted");

        // TODO need to revisit
        // result.add("EsTelemetryItem: " + esTelemetryItemCount.get() + "
        // records deleted");

        logInfo(method, String.join(", ", result));
        return result;
    }

    private Instant getMinDateThreshold(int numberOfDays) {
        return Instant.now().truncatedTo(ChronoUnit.DAYS).minus(numberOfDays, ChronoUnit.DAYS);
    }

    void doRemoveTelemetryByApplicationId(final String applicationId, final Instant date) {
        long toTimestamp = date.toEpochMilli();

        doRemoveTelemetryItemsByApplicationId(applicationId, toTimestamp);
        doRemoveTelemetriesByApplicationId(applicationId, toTimestamp);

        // TODO need to revisit
        // doRemoveEsTelemetryItemsByApplicationId(applicationId, toTimestamp);
    }

    void doRemoveTelemetryByDeviceId(final String deviceId, final Instant date) {
        long toTimestamp = date.toEpochMilli();

        doRemoveTelemetryItemsByDeviceId(deviceId, toTimestamp);
        doRemoveTelemetriesByDeviceId(deviceId, toTimestamp);

        // TODO need to revisit
        // doRemoveEsTelemetryItemsByDeviceId(deviceId, toTimestamp);
    }

    private long doRemoveTelemetryItemsByApplicationId(String applicationId, long toTimestamp) {
        long itemRecords = telemetryItemService.getTelemetryItemRepository()
                .removeByApplicationIdAndTimestamps(applicationId, 0, toTimestamp);
        auditLogService.save(AuditLogBuilder.create().type(TelemetryCleanup.CleanupTelemetryItemByApplicationId)
                .productName(ProductSystemNames.KRONOS).objectId(applicationId)
                .parameter(AUDIT_LOG_PARAM_COUNT, String.valueOf(itemRecords))
                .parameter(AUDIT_LOG_PARAM_TIMESTAMP, String.valueOf(toTimestamp)));
        return itemRecords;
    }

    private long doRemoveTelemetryItemsByDeviceId(String deviceId, long toTimestamp) {
        long itemRecords = telemetryItemService.getTelemetryItemRepository().removeByDeviceIdAndTimestamps(deviceId, 0,
                toTimestamp);
        auditLogService.save(AuditLogBuilder.create().type(TelemetryCleanup.CleanupTelemetryItemByDeviceId)
                .productName(ProductSystemNames.KRONOS).objectId(deviceId)
                .parameter(AUDIT_LOG_PARAM_COUNT, String.valueOf(itemRecords))
                .parameter(AUDIT_LOG_PARAM_TIMESTAMP, String.valueOf(toTimestamp)));
        return itemRecords;
    }

    private long doRemoveTelemetriesByApplicationId(String applicationId, long toTimestamp) {
        long telemetryRecords = telemetryService.getTelemetryRepository()
                .removeByApplicationIdAndTimestamps(applicationId, 0, toTimestamp);
        auditLogService.save(AuditLogBuilder.create().type(TelemetryCleanup.CleanupTelemetryByApplicationId)
                .productName(ProductSystemNames.KRONOS).objectId(applicationId)
                .parameter(AUDIT_LOG_PARAM_COUNT, String.valueOf(telemetryRecords))
                .parameter(AUDIT_LOG_PARAM_TIMESTAMP, String.valueOf(toTimestamp)));
        return telemetryRecords;
    }

    private long doRemoveTelemetriesByDeviceId(String deviceId, long toTimestamp) {
        long telemetryRecords = telemetryService.getTelemetryRepository().removeByDeviceIdAndTimestamps(deviceId, 0,
                toTimestamp);
        auditLogService.save(AuditLogBuilder.create().type(TelemetryCleanup.CleanupTelemetryByDeviceId)
                .productName(ProductSystemNames.KRONOS).objectId(deviceId)
                .parameter(AUDIT_LOG_PARAM_COUNT, String.valueOf(telemetryRecords))
                .parameter(AUDIT_LOG_PARAM_TIMESTAMP, String.valueOf(toTimestamp)));
        return telemetryRecords;
    }

    void doRemoveEsTelemetryItemsByApplicationId(String applicationId, long toTimestamp) {
        springDataEsTelemetryItemService.getItemRepository().remove(applicationId, null, 0, toTimestamp);
        auditLogService.save(AuditLogBuilder.create().type(TelemetryCleanup.CleanupElasticSearchByApplicationId)
                .productName(ProductSystemNames.KRONOS).objectId(applicationId)
                .parameter(AUDIT_LOG_PARAM_TIMESTAMP, String.valueOf(toTimestamp)));
    }

    void doRemoveEsTelemetryItemsByDeviceId(String deviceId, long toTimestamp) {
        springDataEsTelemetryItemService.getItemRepository().remove(null, deviceId, 0, toTimestamp);
        auditLogService.save(AuditLogBuilder.create().type(TelemetryCleanup.CleanupElasticSearchByDeviceId)
                .productName(ProductSystemNames.KRONOS).objectId(deviceId)
                .parameter(AUDIT_LOG_PARAM_TIMESTAMP, String.valueOf(toTimestamp)));
    }
}

package com.arrow.widget.provider;

import java.util.List;

import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arrow.dashboard.runtime.model.UserWrapper;
import com.arrow.dashboard.widget.WidgetDataProviderAbstract;
import com.arrow.dashboard.widget.annotation.data.DataProviderImpl;
import com.arrow.dashboard.widget.annotation.data.DataProviderImpl.Retention;
import com.arrow.kronos.data.EsTelemetryItem;
import com.arrow.kronos.repo.EsTelemetryItemSearchParams;
import com.arrow.kronos.service.SpringDataEsTelemetryItemService;

@Component
@DataProviderImpl(dataProviders = EsTelemetryItemDataProvider.class, retention = Retention.AUTOWIRED)
public class EsTelemetryItemDataProviderImpl extends WidgetDataProviderAbstract {

    @Autowired
    private SpringDataEsTelemetryItemService springDataEsTelemetryItemService;

    public List<EsTelemetryItem> findAllTelemetryItems(UserWrapper userWrapper, String deviceId,
            String[] telemetryNames, long fromTimestamp, long toTimestamp, SortOrder sortOrder) {
        return springDataEsTelemetryItemService.getItemRepository()
                .findAllTelemetryItems(new EsTelemetryItemSearchParams().addDeviceId(deviceId)
                        .addTelemetryNames(telemetryNames).addFromTimestamp(fromTimestamp).addToTimestamp(toTimestamp),
                        sortOrder);
    }
}
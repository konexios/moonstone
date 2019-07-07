package com.arrow.widget.provider;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.arrow.dashboard.runtime.model.UserWrapper;
import com.arrow.dashboard.widget.WidgetDataProviderAbstract;
import com.arrow.dashboard.widget.annotation.data.DataProviderImpl;
import com.arrow.dashboard.widget.annotation.data.DataProviderImpl.Retention;
import com.arrow.kronos.data.LastTelemetryItem;
import com.arrow.kronos.repo.LastTelemetryItemSearchParams;
import com.arrow.kronos.service.LastTelemetryItemService;

@Component
@DataProviderImpl(dataProviders = LastTelemetryItemDataProvider.class, retention = Retention.AUTOWIRED)
public class LastTelemetryItemDataProviderImpl extends WidgetDataProviderAbstract {

	@Autowired
	private LastTelemetryItemService lastTelemetryItemService;

	public List<LastTelemetryItem> findByDeviceId(UserWrapper userWrapper, String deviceId) {
		Assert.hasText(deviceId, "deviceId is empty");

		return lastTelemetryItemService.getLastTelemetryItemRepository().findByDeviceId(deviceId);
	}

	public Page<LastTelemetryItem> findLastTelemetryItems(UserWrapper userWrapper,
	        LastTelemetryItemSearchParams params) {
		Assert.notNull(params, "params is null");

		params.addApplicationIds(userWrapper.getApplicationId());

		return lastTelemetryItemService.getLastTelemetryItemRepository()
		        .findLastTelemetryItems(new PageRequest(0, Integer.MAX_VALUE), params);
	}

	public LastTelemetryItem findLastTelemetryItem(UserWrapper userWrapper, LastTelemetryItemSearchParams params) {
		Assert.notNull(params, "params is null");

		LastTelemetryItem result = null;
		Page<LastTelemetryItem> lastTelemetryItems = findLastTelemetryItems(userWrapper, params);
		if (lastTelemetryItems != null && lastTelemetryItems.getTotalElements() > 0) {
			result = lastTelemetryItems.getContent().get(0);
		}

		return result;
	}
}
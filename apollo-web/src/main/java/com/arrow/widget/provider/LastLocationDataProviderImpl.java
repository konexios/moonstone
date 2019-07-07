package com.arrow.widget.provider;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.arrow.dashboard.runtime.model.UserWrapper;
import com.arrow.dashboard.widget.WidgetDataProviderAbstract;
import com.arrow.dashboard.widget.annotation.data.DataProviderImpl;
import com.arrow.dashboard.widget.annotation.data.DataProviderImpl.Retention;
import com.arrow.pegasus.data.location.LastLocation;
import com.arrow.pegasus.repo.LastLocationSearchParams;
import com.arrow.pegasus.service.LastLocationService;

@Component
@DataProviderImpl(dataProviders = LastLocationDataProvider.class, retention = Retention.AUTOWIRED)
public class LastLocationDataProviderImpl extends WidgetDataProviderAbstract {

	@Autowired
	private LastLocationService lastLocationService;

	public List<LastLocation> findLastLocations(UserWrapper userWrapper, LastLocationSearchParams params) {
		Assert.notNull(params, "params is null");

		return lastLocationService.getLastLocationRepository().findLastLocations(params);
	}
}
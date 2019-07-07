package com.arrow.widget.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.arrow.dashboard.runtime.model.UserWrapper;
import com.arrow.dashboard.widget.WidgetDataProviderAbstract;
import com.arrow.dashboard.widget.annotation.data.DataProviderImpl;
import com.arrow.dashboard.widget.annotation.data.DataProviderImpl.Retention;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.service.DeviceTypeService;

@Component
@DataProviderImpl(dataProviders = DeviceTypeDataProvider.class, retention = Retention.AUTOWIRED)
public class DeviceTypeDataProviderImpl extends WidgetDataProviderAbstract {

    @Autowired
    private DeviceTypeService deviceTypeService;

    public DeviceType findDeviceType(UserWrapper userWrapper, String deviceTypeId) {
        Assert.notNull(userWrapper, "userWrapper is null");
        Assert.hasText(deviceTypeId, "deviceTypeId is empty");

        return deviceTypeService.getDeviceTypeRepository().findById(deviceTypeId).orElse(null);
    }
}
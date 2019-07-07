package com.arrow.widget.provider;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.arrow.dashboard.runtime.model.UserWrapper;
import com.arrow.dashboard.widget.WidgetDataProviderAbstract;
import com.arrow.dashboard.widget.annotation.data.DataProviderImpl;
import com.arrow.dashboard.widget.annotation.data.DataProviderImpl.Retention;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.repo.DeviceSearchParams;
import com.arrow.kronos.service.DeviceService;

@Component
@DataProviderImpl(dataProviders = DeviceDataProvider.class, retention = Retention.AUTOWIRED)
public class DeviceDataProviderImpl extends WidgetDataProviderAbstract {

    @Autowired
    private DeviceService deviceService;

    public List<Device> findDevices(UserWrapper userWrapper) {
        Assert.notNull(userWrapper, "userWrapper is null");

        DeviceSearchParams params = new DeviceSearchParams();
        params.addApplicationIds(userWrapper.getApplicationId());
        params.setEnabled(true);

        // check if the user can only see his/her devices
        if (!userWrapper.hasPrivilege("KRONOS_VIEW_ALL_DEVICES"))
            params.addUserIds(userWrapper.getUserId());

        return deviceService.getDeviceRepository().doFindAllDevices(params);
    }

    public Device findDeviceByUid(UserWrapper userWrapper, String uid) {
        Assert.notNull(userWrapper, "userWrapper is null");
        Assert.hasText(uid, "uid is empty");

        DeviceSearchParams params = new DeviceSearchParams();
        params.addApplicationIds(userWrapper.getApplicationId());
        params.setEnabled(true);
        params.addUids(uid);

        // check if the user can only see his/her devices
        if (!userWrapper.hasPrivilege("KRONOS_VIEW_ALL_DEVICES"))
            params.addUserIds(userWrapper.getUserId());

        List<Device> devices = deviceService.getDeviceRepository().doFindAllDevices(params);

        return (!devices.isEmpty() ? devices.get(0) : null);
    }
}
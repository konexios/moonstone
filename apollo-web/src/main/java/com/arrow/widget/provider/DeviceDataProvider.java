package com.arrow.widget.provider;

import java.util.List;

import com.arrow.kronos.data.Device;

public interface DeviceDataProvider {

	List<Device> findDevices();

	Device findDeviceByUid(String uid);
}
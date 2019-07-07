package com.arrow.widget.provider;

import com.arrow.kronos.data.DeviceType;

public interface DeviceTypeDataProvider {

	DeviceType findDeviceType(String deviceTypeId);
}
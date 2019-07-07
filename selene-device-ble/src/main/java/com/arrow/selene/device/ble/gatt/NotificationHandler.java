package com.arrow.selene.device.ble.gatt;

import com.arrow.selene.device.ble.sensor.BleSensor;

public interface NotificationHandler {
    void handleNotification(String handle, byte[] bytes);
    void handleNotification(BleSensor<?, ?> sensor, byte[] bytes);
}

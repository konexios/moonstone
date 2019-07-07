package com.arrow.selene.device.ble.gatt;

import java.util.Map;

import com.arrow.selene.device.ble.BleInfo;

public interface Gatt {
	void connect(String bleInterface, String bleAddress, long retryInterval, boolean randomAddress);

	void disconnect();

	boolean isConnected();

	void setNotificationHandler(NotificationHandler handler);

	void setConnectionHandler(ConnectionHandler handler);

	void controlSensor(String uuid, String value);

	void enableNotification(String uuid, String value);

	void disableNotification(String uuid, String value);

	void setPeriod(String uuid, String value);

	byte[] readValue(String uuid);

	BleInfo populateInfo(BleInfo info);

	Map<String, String> getCharacteristicMap();
}

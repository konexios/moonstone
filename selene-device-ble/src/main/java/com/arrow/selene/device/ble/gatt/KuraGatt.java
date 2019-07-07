package com.arrow.selene.device.ble.gatt;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.kura.KuraException;
import org.eclipse.kura.bluetooth.BluetoothGatt;
import org.eclipse.kura.bluetooth.BluetoothLeNotificationListener;

import com.arrow.selene.Loggable;
import com.arrow.selene.device.ble.BleInfo;
import com.arrow.selene.device.ble.BleUtils;

public class KuraGatt extends Loggable implements Gatt {

	private final static long DEFAULT_CHECK_GATT_INTERVAL_MS = 5000;

	private Timer gattCheckTimer;
	private boolean gattConnected;
	private BluetoothGatt bluetoothGatt;
	private ConnectionHandler handler;
	private String bleInterface;
	private String bleAddress;
	private AtomicBoolean isChecking = new AtomicBoolean(false);
	private Map<String, String> readHandleMap = new HashMap<>();
	private Map<String, String> writeHandleMap = new HashMap<>();

	@Override
	public void disconnect() {
		if (bluetoothGatt != null) {
			bluetoothGatt.disconnect();
		}
		if (gattCheckTimer != null) {
			gattCheckTimer.cancel();
		}
	}

	@Override
	public boolean isConnected() {
		return gattConnected;
	}

	@Override
	public void setNotificationHandler(NotificationHandler handler) {
		bluetoothGatt.setBluetoothLeNotificationListener(new KuraNotificationListener(handler));
	}

	@Override
	public void setConnectionHandler(ConnectionHandler handler) {
		this.handler = handler;
	}

	@Override
	public void controlSensor(String uuid, String value) {
		String method = "controlSensor";
		String handle = writeHandleMap.get(uuid);

		logDebug(method, "uuid %s, value %s", uuid, value);

		if (handle != null) {
			logInfo(method, "handle: %s, value: %s", handle, value);
			bluetoothGatt.writeCharacteristicValue(handle, value);
		} else {
			logWarn(method, "handle not found for uuid: %s", uuid);
		}
	}

	@Override
	public void enableNotification(String uuid, String value) {
		String method = "enableNotification";
		String handle = readHandleMap.get(uuid);
		if (handle != null) {
			int handleValue = Integer.parseInt(handle.substring(2), 16) + 1;
			String notificationHandle = String.format("0x%04x", handleValue);
			bluetoothGatt.writeCharacteristicValue(notificationHandle, value);
		} else {
			logWarn(method, "handle not found for uuid: %s", uuid);
		}
	}

	@Override
	public void disableNotification(String uuid, String value) {
		String method = "disableNotification";
		String handle = readHandleMap.get(uuid);
		if (handle != null) {
			int handleValue = Integer.parseInt(handle.substring(2), 16) + 1;
			String notificationHandle = String.format("0x%04x", handleValue);
			bluetoothGatt.writeCharacteristicValue(notificationHandle, value);
		} else {
			logWarn(method, "handle not found for uuid: %s", uuid);
		}
	}

	@Override
	public void setPeriod(String uuid, String value) {
		String method = "setPeriod";
		String handle = writeHandleMap.get(uuid);
		if (handle != null) {
			logInfo(method, "handle: %s, value: %s", handle, value);
			bluetoothGatt.writeCharacteristicValue(handle, value);
		} else {
			logWarn(method, "handle not found for uuid: %s", uuid);
		}
	}

	@Override
	public byte[] readValue(String uuid) {
		String method = "readValue";
		byte[] result = null;
		try {
			String handle = readHandleMap.get(uuid);
			if (handle != null) {
				result = BleUtils.hexStringToByteArray(bluetoothGatt.readCharacteristicValue(handle));
				logDebug(method, "\nreadValue(%s) = %s \n", uuid, Arrays.toString(result));

			} else {
				logWarn(method, "handle not found for uuid: %s", uuid);
			}
		} catch (KuraException e) {
			logWarn(method, "error during reading characteristics value: %s", e.getMessage());
		}
		return result;
	}

	@Override
	public void connect(String bleInterface, String bleAddress, long retryInterval, boolean randomAddress) {
		String method = "connect";
		this.bleInterface = bleInterface;
		this.bleAddress = bleAddress;
//		bluetoothGatt = new BluetoothGattImpl(bleAddress);
		bluetoothGatt = new CustomBluetoothGattImpl(bleAddress);
		logInfo(method, "trying to connect - interface: %s, address: %s, random: %s", bleInterface, bleAddress,
				randomAddress);

		logInfo(method, "starting gattCheckTimer for %s ...", DEFAULT_CHECK_GATT_INTERVAL_MS);
		gattCheckTimer = new Timer();
		gattCheckTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				checkGatt();
			}
		}, 0, DEFAULT_CHECK_GATT_INTERVAL_MS);
	}

	@Override
	public BleInfo populateInfo(BleInfo info) {
		info.setDeviceName(readStrCharacteristic(Characteristics.DEVICE_NAME));
		info.setFirmwareRevision(readStrCharacteristic(Characteristics.FIRMWARE_REVISION));
		info.setManufacturerName(readStrCharacteristic(Characteristics.MANUFACTURER_NAME));
		info.setModelNumber(readStrCharacteristic(Characteristics.MODEL_NUMBER));
		info.setSerialNumber(readStrCharacteristic(Characteristics.SERIAL_NUMBER));
		info.setSoftwareRevision(readStrCharacteristic(Characteristics.SOFTWARE_REVISION));
		return info;
	}

	private String readStrCharacteristic(String key) {
		String method = "readStrChar";
		String handle = readHandleMap.get(key);
		if (handle != null) {
			try {
				String handleReadResult = BleUtils.hexAsciiToString(bluetoothGatt.readCharacteristicValue(handle));

				logInfo(method, "key %s has value %s", key, handleReadResult);

				return handleReadResult;
			} catch (Exception e) {
				logError(method, "error reading characteristic: %s", key);
			}
		} else {
			logWarn(method, "handle not found for key: %s", key);
		}
		return null;
	}

	private void checkGatt() {
		String method = "checkGatt";
		if (isChecking.compareAndSet(false, true)) {
			try {
				boolean connected = false;
				if (!gattConnected) {
					try {
						logInfo(method, "connecting to interface: %s, address: %s ...", bleInterface, bleAddress);
						bluetoothGatt.connect(bleInterface);
						connected = true;
						logInfo(method, "connected!");
					} catch (KuraException e) {
						logWarn(method, "error during connection attempt: %s", e.getMessage());
						bluetoothGatt.disconnect();
					}
				} else {
					try {
						logInfo(method, "checking connection ...");
						connected = bluetoothGatt.checkConnection();
					} catch (Throwable e) {
						logWarn(method, "error during connection check: %s", e.getMessage());
					}
				}
				if (connected && !gattConnected) {
					logInfo(method, "device %s connected, delay a few seconds for full connection ...", bleAddress);

					// some delay for full connection
					try {
						Thread.sleep(DEFAULT_CHECK_GATT_INTERVAL_MS);
					} catch (Throwable t) {
					}

					loadMaps();
					handler.handleConnection();
				}
				if (!connected && gattConnected) {
					logInfo(method, "device %s disconnected", bleAddress);
					handler.handleDisconnection();
				}
				gattConnected = connected;
				logInfo(method, "device %s connectionStatus: %s", bleAddress, gattConnected);
			} catch (Exception e) {
				logError(method, e);
			}
			isChecking.set(false);
		}
	}

	private void loadMaps() {
		String method = "loadMaps";
		logInfo(method, "loading handle map...");
		bluetoothGatt.getCharacteristics("0x0001", "0x00ff").forEach(characteristic -> {
			String uuid = characteristic.getUuid().toString().toLowerCase();
			String valueHandle = characteristic.getValueHandle();
			String handle = characteristic.getHandle();
			int properties = characteristic.getProperties();
			logDebug(method, "characteristic uuid (%s) --> value handle (%s), handle (%s)", uuid, valueHandle, handle);

			if ((properties & 0x10) > 0 || (properties & 0x20) > 0 || (properties & 0x02) > 0) {
				// property `notify` or `indicate` or `read`
				readHandleMap.put(uuid, valueHandle);
				logDebug(method, "\n readHandleMap.put(" + uuid + ", " + valueHandle + ")");
				logDebug(method, "read handle (%s)", valueHandle);
			}

			if ((properties & 0x04) > 0 || (properties & 0x08) > 0) {
				// property `write` or `write without response`
				writeHandleMap.put(uuid, valueHandle);
				logDebug(method, "write handle (%s)", valueHandle);
			}

		});
	}

	@Override
	public Map<String, String> getCharacteristicMap() {
		// return characteristics that are readable or notify-able
		return Collections.unmodifiableMap(readHandleMap);
	}

	private static class KuraNotificationListener implements BluetoothLeNotificationListener {
		private final NotificationHandler handler;

		public KuraNotificationListener(NotificationHandler handler) {
			this.handler = handler;
		}

		@Override
		public void onDataReceived(String s, String s1) {
			handler.handleNotification(s, BleUtils.hexStringToByteArray(s1));
		}
	}
}

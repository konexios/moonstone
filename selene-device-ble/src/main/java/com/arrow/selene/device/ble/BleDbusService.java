package com.arrow.selene.device.ble;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.exceptions.DBusExecutionException;

import com.arrow.acn.client.utils.Utils;
import com.arrow.selene.device.dbus.Adapter1;
import com.arrow.selene.device.dbus.Device1;
import com.arrow.selene.device.dbus.GattCharacteristic1;
import com.arrow.selene.device.dbus.ObjectManager;
import com.arrow.selene.device.dbus.Properties;
import com.arrow.selene.engine.service.DbusService;
import com.arrow.selene.service.ServiceAbstract;

public class BleDbusService extends ServiceAbstract {
	private static final String BLUEZ_IFACE = "org.bluez";
	private static final String BLUEZ_PATH = "/org/bluez/";

	private static class SingletonHolder {
		private static final BleDbusService singleton = new BleDbusService();
	}

	public static BleDbusService getInstance() {
		return SingletonHolder.singleton;
	}

	private DbusService dbusService = DbusService.getInstance();

	public DbusService getDbusService() {
		return dbusService;
	}

	public void connectBle(String bleInterface, String bleAddress) throws DBusException {
		String method = "connectBle";
		Validate.notNull(bleInterface, "bleInterface is null");
		Validate.notEmpty(bleInterface, "bleInterface is empty");
		Validate.notNull(bleAddress, "bleAddress is null");
		Validate.notEmpty(bleAddress, "bleAddress is empty");

		discoverBleDevice(bleInterface, bleAddress);
		boolean result = isBleConnected(bleInterface, bleAddress);
		logInfo(method, "result: %s", result);
		if (!result) {
			logInfo(method, "getDevice1().connect() ...");
			getDevice1(bleInterface, bleAddress).connect();
		}
	}

	public void disconnectBle(String bleInterface, String bleAddress) throws DBusException {
		Validate.notNull(bleInterface, "bleInterface is null");
		Validate.notEmpty(bleInterface, "bleInterface is empty");
		Validate.notNull(bleAddress, "bleAddress is null");
		Validate.notEmpty(bleAddress, "bleAddress is empty");
		getDevice1(bleInterface, bleAddress).disconnect();
	}

	private void discoverBleDevice(String bleInterface, String bleAddress) throws DBusException {
		String method = "discoverBleDevice";

		// check if the device is available in dbus cache
		boolean devicePresent = checkBleDevicePresent(bleInterface, bleAddress);

		while (!devicePresent) {
			logInfo(method, "Device path is not available in dbus ...");
			logInfo(method, "Starting discovery in dbus ...");
			if (!isDiscovering(bleInterface)) {
				startBleDiscovery(bleInterface);
			}
			Utils.sleep(5000);
			logInfo(method, "Stopping discovery in dbus ...");
			stopBleDiscovery(bleInterface);

			// check again for availability of device
			devicePresent = checkBleDevicePresent(bleInterface, bleAddress);
		}
	}

	public boolean isBleConnected(String bleInterface, String bleAddress) throws DBusException {
		Validate.notNull(bleInterface, "bleInterface is null");
		Validate.notEmpty(bleInterface, "bleInterface is empty");
		Validate.notNull(bleAddress, "bleAddress is null");
		Validate.notEmpty(bleAddress, "bleAddress is empty");

		Properties properties = dbusService.getConnection().getRemoteObject(BLUEZ_IFACE,
		        buildDevicePath(bleInterface, bleAddress), Properties.class);
		return properties.get(Device1.DEVICE1_IFACE, "Connected");
	}

	public boolean checkBleDevicePresent(String bleInterface, String bleAddress) throws DBusException {
		Validate.notNull(bleInterface, "bleInterface is null");
		Validate.notEmpty(bleInterface, "bleInterface is empty");
		Validate.notNull(bleAddress, "bleAddress is null");
		Validate.notEmpty(bleAddress, "bleAddress is empty");
		ObjectManager objectManager = dbusService.getConnection().getRemoteObject(BLUEZ_IFACE, "/",
		        ObjectManager.class);
		return objectManager.getManagedObjects().containsKey(new Path(buildDevicePath(bleInterface, bleAddress)));
	}

	public List<String> getDeviceCharacteristicsPath(String bleInterface, String bleAddress, String devicePath)
	        throws DBusException {
		Validate.notNull(bleInterface, "bleInterface is null");
		Validate.notEmpty(bleInterface, "bleInterface is empty");
		Validate.notNull(bleAddress, "bleAddress is null");
		Validate.notEmpty(bleAddress, "bleAddress is empty");
		ObjectManager objectManager = dbusService.getConnection().getRemoteObject(BLUEZ_IFACE, "/",
		        ObjectManager.class);

		String deviceAddressPath = buildDevicePath(bleInterface, bleAddress);
		String regex = deviceAddressPath + "/service[0-9a-fA-F]{4}/char[0-9a-fA-F]{4}";
		List<String> result = objectManager.getManagedObjects().keySet().stream()
		        .filter(path -> (path.getPath().matches(regex))).map(path -> path.getPath())
		        .collect(Collectors.toList());
		return result;
	}

	public void startBleDiscovery(String bleInterface) throws DBusException {
		Validate.notNull(bleInterface, "bleInterface is null");
		Validate.notEmpty(bleInterface, "bleInterface is empty");
		if (!isDiscovering(bleInterface)) {
			Adapter1 adapter = dbusService.getConnection().getRemoteObject(BLUEZ_IFACE, BLUEZ_PATH + bleInterface,
			        Adapter1.class);
			adapter.startDiscovery();
		}
	}

	public void stopBleDiscovery(String bleInterface) throws DBusException {
		Validate.notNull(bleInterface, "bleInterface is null");
		Validate.notEmpty(bleInterface, "bleInterface is empty");
		if (isDiscovering(bleInterface)) {
			Adapter1 adapter = dbusService.getConnection().getRemoteObject(BLUEZ_IFACE, BLUEZ_PATH + bleInterface,
			        Adapter1.class);
			adapter.stopDiscovery();
		}
	}

	private boolean isDiscovering(String bleInterface) throws DBusException {
		Properties properties = dbusService.getConnection().getRemoteObject(BLUEZ_IFACE, BLUEZ_PATH + bleInterface,
		        Properties.class);
		return properties.<Boolean> get(Adapter1.ADAPTER1_IFACE, "Discovering");
	}

	public void stopNotify(String bleInterface, String bleAddress, String parameter) throws DBusException {
		Validate.notNull(bleInterface, "bleInterface is null");
		Validate.notEmpty(bleInterface, "bleInterface is empty");
		Validate.notNull(bleAddress, "bleAddress is null");
		Validate.notEmpty(bleAddress, "bleAddress is empty");
		Validate.notNull(parameter, "parameter is null");
		Validate.notEmpty(parameter, "parameter is empty");
		if (isNotifying(bleInterface, bleAddress, parameter)) {
			getGattCharacteristic1(bleInterface, bleAddress, parameter).stopNotify();
		}
	}

	public void startNotify(String bleInterface, String bleAddress, String parameter) throws DBusException {
		Validate.notNull(bleInterface, "bleInterface is null");
		Validate.notEmpty(bleInterface, "bleInterface is empty");
		Validate.notNull(bleAddress, "bleAddress is null");
		Validate.notEmpty(bleAddress, "bleAddress is empty");
		Validate.notNull(parameter, "parameter is null");
		Validate.notEmpty(parameter, "parameter is empty");
		if (!isNotifying(bleInterface, bleAddress, parameter)) {
			getGattCharacteristic1(bleInterface, bleAddress, parameter).startNotify();
		}
	}

	private boolean isNotifying(String bleInterface, String bleAddress, String parameter) throws DBusException {
		Properties properties = dbusService.getConnection().getRemoteObject(BLUEZ_IFACE, parameter, Properties.class);
		return properties.<Boolean> get(GattCharacteristic1.GATT_CHARACTERISTIC1_IFACE, "Notifying");
	}

	public void writeValue(String bleInterface, String bleAddress, String parameter, byte[] bytes)
	        throws DBusException {
		Validate.notNull(bleInterface, "bleInterface is null");
		Validate.notEmpty(bleInterface, "bleInterface is empty");
		Validate.notNull(bleAddress, "bleAddress is null");
		Validate.notEmpty(bleAddress, "bleAddress is empty");
		Validate.notNull(parameter, "parameter is null");
		Validate.notEmpty(parameter, "parameter is empty");
		Validate.notNull(bytes, "parameter is null");
		getGattCharacteristic1(bleInterface, bleAddress, parameter).writeValue(bytes, Collections.emptyMap());
	}

	public byte[] readValue(String bleInterface, String bleAddress, String parameter) throws DBusException {
		String method = "readValue";
		Validate.notNull(bleInterface, "bleInterface is null");
		Validate.notEmpty(bleInterface, "bleInterface is empty");
		Validate.notNull(bleAddress, "bleAddress is null");
		Validate.notEmpty(bleAddress, "bleAddress is empty");
		Validate.notNull(parameter, "parameter is null");
		Validate.notEmpty(parameter, "parameter is empty");

		byte[] value = null;
		try {
			value = getGattCharacteristic1(bleInterface, bleAddress, parameter).readValue(Collections.emptyMap());
		} catch (DBusExecutionException e) {
			logWarn(method, "value was not read: %s", e);
		}
		return value;
	}

	public String getGattCharacteristicsUuid(String path) throws DBusException {
		Validate.notNull(path, "parameter is null");
		Validate.notEmpty(path, "parameter is empty");
		Properties properties = dbusService.getConnection().getRemoteObject(BLUEZ_IFACE, path, Properties.class);
		return properties.get(GattCharacteristic1.GATT_CHARACTERISTIC1_IFACE, "UUID");
	}

	public List<String> getGattCharacteristicsFlags(String path) throws DBusException {
		Validate.notNull(path, "parameter is null");
		Validate.notEmpty(path, "parameter is empty");
		Properties properties = dbusService.getConnection().getRemoteObject(BLUEZ_IFACE, path, Properties.class);
		return properties.get(GattCharacteristic1.GATT_CHARACTERISTIC1_IFACE, "Flags");
	}

	public Device1 getDevice1(String bleInterface, String bleAddress) throws DBusException {
		return dbusService.getConnection().getRemoteObject(BLUEZ_IFACE, buildDevicePath(bleInterface, bleAddress),
		        Device1.class);
	}

	public GattCharacteristic1 getGattCharacteristic1(String bleInterface, String bleAddress, String parameter)
	        throws DBusException {
		return dbusService.getConnection().getRemoteObject(BLUEZ_IFACE, parameter, GattCharacteristic1.class);
	}

	public String buildDevicePath(String bleInterface, String bleAddress) {
		return BLUEZ_PATH + bleInterface + "/" + "dev_" + bleAddress.replace(':', '_');
	}

	public Map<String, String> discoverBleDevices(String bleInterface, long timeout) throws DBusException {
		String method = "discoverBleDevices";
		Map<String, String> devices = new HashMap<>();
		if (dbusService.connect()) {
			// Only discover if timeout is provided
			if (timeout > 0) {
				logInfo(method, "Starting discovery in dbus ...");
				if (!isDiscovering(bleInterface)) {
					startBleDiscovery(bleInterface);
				}
				Utils.sleep(timeout);
				logInfo(method, "Stopping discovery in dbus ...");
				stopBleDiscovery(bleInterface);
			}
			ObjectManager objectManager = dbusService.getConnection().getRemoteObject(BLUEZ_IFACE, "/",
			        ObjectManager.class);

			String devicePrefix = BLUEZ_PATH + bleInterface + "/" + "dev_";
			String deviceKey = devicePrefix + "([0-9A-Fa-f]{2}_){5}([0-9A-Fa-f]{2})$";
			for (Path path : objectManager.getManagedObjects().keySet()) {
				if (path.getPath().matches(deviceKey)) {
					Map<String, Map<String, Variant<?>>> interfaceMap = objectManager.getManagedObjects().get(path);
					String macAddress = path.getPath().substring(devicePrefix.length()).replace('_', ':');
					Variant<?> var = interfaceMap.get(Device1.DEVICE1_IFACE).get("Name");
					if (var != null) {
						logDebug(method, "%s ==== %s", macAddress, var.toString());
						devices.put(macAddress, var.getValue().toString());
					}
				}
			}
		}
		return devices;
	}
}

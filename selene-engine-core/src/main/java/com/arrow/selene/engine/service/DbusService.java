package com.arrow.selene.engine.service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.lang3.Validate;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

import com.arrow.selene.device.dbus.Connection;
import com.arrow.selene.device.dbus.NetworkManager;
import com.arrow.selene.device.dbus.Properties;
import com.arrow.selene.device.dbus.Settings;
import com.arrow.selene.device.dbus.Wireless;
import com.arrow.selene.service.ServiceAbstract;

public class DbusService extends ServiceAbstract {
	private static final String DEVICE_IFACE = "org.freedesktop.NetworkManager.Device";
	private static final String ACTIVE_IFACE = "org.freedesktop.NetworkManager.Connection.Active";
	private static final String AP_IFACE = "org.freedesktop.NetworkManager.AccessPoint";
	private static final UInt32 WIFI_DEVICE_TYPE = new UInt32(2L);

	private DBusConnection connection;

	private static class SingletonHolder {
		static final DbusService SINGLETON = new DbusService();
	}

	public static DbusService getInstance() {
		return SingletonHolder.SINGLETON;
	}

	public synchronized boolean connect() {
		String method = "connect";
		boolean result = true;
		if (connection == null) {
			try {
				logInfo(method, "connecting to DBus...");
				connection = DBusConnection.getConnection(DBusConnection.SYSTEM);
				logInfo(method, "connected to DBus");
			} catch (DBusException e) {
				result = false;
				logError(method, "cannot connect to DBus");
			}
		}
		return result;
	}

	public synchronized void disconnect() {
		String method = "disconnect";
		if (connection != null) {
			logInfo(method, "disconnecting from DBus...");
			connection.disconnect();
			logInfo(method, "disconnected from DBus");
		}
	}

	public synchronized Path findAccessPoint(Path devicePath, String ssid) throws DBusException {
		validateDevicePath(devicePath);
		validateSsid(ssid);
		String method = "findAccessPoint";
		Path result = null;
		logInfo(method, "searching for access point...");
		Wireless wireless = connection.getRemoteObject(NetworkManager.NM_IFACE, devicePath.getPath(), Wireless.class);
		byte[] ssidBytes = ssid.getBytes(StandardCharsets.UTF_8);
		for (Path path : wireless.getAccessPoints()) {
			Properties properties = connection.getRemoteObject(NetworkManager.NM_IFACE, path.getPath(),
					Properties.class);
			if (Arrays.equals(ssidBytes, properties.get(AP_IFACE, "Ssid"))) {
				result = path;
				logInfo(method, "access point found");
				break;
			}
		}
		if (result == null) {
			logWarn(method, "access point not found");
		}
		return result;
	}

	public synchronized Path findWifiDevice(String deviceName) throws DBusException {
		validateDeviceName(deviceName);
		String method = "findWifiDevice";
		Path result = null;
		logInfo(method, "searching for device...");
		NetworkManager networkManager = connection.getRemoteObject(NetworkManager.NM_IFACE, NetworkManager.NM_PATH,
				NetworkManager.class);
		for (Path path : networkManager.getDevices()) {
			Properties properties = connection.getRemoteObject(NetworkManager.NM_IFACE, path.getPath(),
					Properties.class);
			if (Objects.equals(WIFI_DEVICE_TYPE, properties.get(DEVICE_IFACE, "DeviceType"))
					&& Objects.equals(properties.get(DEVICE_IFACE, "Interface"), deviceName)) {
				result = path;
				logInfo(method, "device found");
				break;
			}
		}
		if (result == null) {
			logWarn(method, "device not found");
		}
		return result;
	}

	public synchronized Path findActiveConnection(Path devicePath) throws DBusException {
		validateDevicePath(devicePath);
		String method = "findActiveConnection";
		Path result = null;
		logInfo(method, "searching for active connection");
		Properties properties = connection.getRemoteObject(NetworkManager.NM_IFACE, NetworkManager.NM_PATH,
				Properties.class);
		for (Path path : properties.<Vector<Path>>get(NetworkManager.NM_IFACE, "ActiveConnections")) {
			Properties acProps = connection.getRemoteObject(NetworkManager.NM_IFACE, path.getPath(), Properties.class);
			if (acProps.<Vector<Path>>get(ACTIVE_IFACE, "Devices").stream()
					.anyMatch(device -> Objects.equals(device, devicePath))) {
				result = path;
				logInfo(method, "active connection found");
				break;
			}
		}
		if (result == null) {
			logWarn(method, "active connection not found");
		}
		return result;
	}

	public synchronized Path activateWifiConnection(String deviceName, String ssid) throws DBusException {
		validateDeviceName(deviceName);
		validateSsid(ssid);
		String method = "activateWifiConnection";
		logInfo(method, "activating connection...");
		Path result = null;
		Path devicePath = findWifiDevice(deviceName);
		if (devicePath != null) {
			Path accessPoint = findAccessPoint(devicePath, ssid);
			if (accessPoint != null) {
				NetworkManager networkManager = connection.getRemoteObject(NetworkManager.NM_IFACE,
						NetworkManager.NM_PATH, NetworkManager.class);
				networkManager.activateConnection(new Path("/"), devicePath, accessPoint);
				result = accessPoint;
				logInfo(method, "connection activated");
			}
		}
		if (result == null) {
			logWarn(method, "failed to activate connection");
		}
		return result;
	}

	public synchronized Path createOrUpdateConnection(String ssid, String authAlg, String keyMgmt, String password)
			throws DBusException {
		validateSsid(ssid);
		Validate.notNull(authAlg, "authAlg is null");
		Validate.notEmpty(authAlg, "authAlg is empty");
		Validate.notNull(keyMgmt, "keyMgmt is null");
		Validate.notEmpty(keyMgmt, "keyMgmt is empty");
		Validate.notNull(password, "password is null");
		Validate.notEmpty(password, "password is empty");

		String method = "createOrUpdateConnection";
		Path result = null;
		Path settingsPath = findWirelessSettingsBySsid(ssid);
		if (settingsPath != null) {
			Connection connectionSettings = connection.getRemoteObject(NetworkManager.NM_IFACE, settingsPath.getPath(),
					Connection.class);
			Map<String, Map<String, Variant<?>>> settings = fillSettings(connectionSettings.getSettings(), ssid,
					authAlg, keyMgmt, password);
			connectionSettings.update(settings);
			result = settingsPath;
			logInfo(method, "existing connection updated");
		} else {
			Settings connectionSettings = connection.getRemoteObject(NetworkManager.NM_IFACE, Settings.SETTINGS_PATH,
					Settings.class);
			Map<String, Map<String, Variant<?>>> settings = fillSettings(new HashMap<>(), ssid, authAlg, keyMgmt,
					password);
			result = connectionSettings.addConnection(settings);
			logInfo(method, "new connection created");
		}
		return result;
	}

	public synchronized void deactivateConnection(String deviceName) throws DBusException {
		validateDeviceName(deviceName);
		String method = "deactivateConnection";
		logInfo(method, "deactivating connection...");
		Path activeConnection = findActiveConnection(findWifiDevice(deviceName));
		if (activeConnection != null) {
			NetworkManager networkManager = connection.getRemoteObject(NetworkManager.NM_IFACE, NetworkManager.NM_PATH,
					NetworkManager.class);
			networkManager.deactivateConnection(activeConnection);
			logInfo(method, "connection deactivated");
		} else {
			logWarn(method, "failed to deactivate connection");
		}
	}

	public synchronized List<Path> getAccessPoints(Path devicePath) throws DBusException {
		validateDevicePath(devicePath);
		return connection.getRemoteObject(NetworkManager.NM_IFACE, devicePath.getPath(), Wireless.class)
				.getAccessPoints();
	}

	public synchronized String getAccessPointSsid(String accessPointPath) throws DBusException {
		validateAccessPointPath(accessPointPath);
		Properties properties = connection.getRemoteObject(NetworkManager.NM_IFACE, accessPointPath, Properties.class);
		return new String(properties.get(AP_IFACE, "Ssid"), StandardCharsets.UTF_8);
	}

	public synchronized Path findWirelessSettingsBySsid(String ssid) throws DBusException {
		validateSsid(ssid);

		String method = "findWirelessSettingsBySsid";
		logInfo(method, "searching for settings...");
		Settings settings = connection.getRemoteObject(NetworkManager.NM_IFACE, Settings.SETTINGS_PATH, Settings.class);
		Path result = null;
		for (Path path : settings.listConnections()) {
			Connection conn = connection.getRemoteObject(NetworkManager.NM_IFACE, path.getPath(), Connection.class);
			Map<String, Variant<?>> wireless = conn.getSettings().get("802-11-wireless");
			if (wireless != null) {
				if (Arrays.equals(ssid.getBytes(StandardCharsets.UTF_8), (byte[]) wireless.get("ssid").getValue())) {
					result = path;
					logInfo(method, "settings found");
					break;
				}
			}
		}
		if (result == null) {
			logInfo(method, "settings not found");
		}
		return result;
	}

	public synchronized <T extends DBusSignal> void addSignalHandler(Class<T> clazz, DBusSigHandler<T> handler)
			throws DBusException {
		connection.addSigHandler(clazz, handler);
	}

	public synchronized <T extends DBusSignal> void removeSignalHandler(Class<T> clazz, DBusSigHandler<T> handler)
			throws DBusException {
		connection.removeSigHandler(clazz, handler);
	}

	private Map<String, Map<String, Variant<?>>> fillSettings(Map<String, Map<String, Variant<?>>> properties,
			String ssid, String authAlg, String keyMgmt, String password) {
		Map<String, Map<String, Variant<?>>> settings = new HashMap<>();
		for (Entry<String, Map<String, Variant<?>>> entry : properties.entrySet()) {
			settings.put(entry.getKey(), new HashMap<>(entry.getValue()));
		}

		Map<String, Variant<?>> connection = settings.get("connection");
		if (connection == null) {
			connection = new HashMap<>();
			connection.put("id", new Variant<>(ssid));
			connection.put("uuid",
					new Variant<>(
							UUID.nameUUIDFromBytes((ssid + System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8))
									.toString()));
			connection.put("type", new Variant<>("802-11-wireless"));
			settings.put("connection", connection);
		}

		Map<String, Variant<?>> wireless = settings.get("802-11-wireless");
		if (wireless == null) {
			wireless = new HashMap<>();
			settings.put("802-11-wireless", wireless);
			wireless.put("security", new Variant<>("802-11-wireless-security"));
			wireless.put("ssid", new Variant<>(ssid.getBytes(StandardCharsets.UTF_8)));
			wireless.put("mode", new Variant<>("infrastructure"));
		}

		Map<String, Variant<?>> ipv4 = settings.get("ipv4");
		if (ipv4 == null) {
			ipv4 = new HashMap<>();
			settings.put("ipv4", ipv4);
			ipv4.put("method", new Variant<>("auto"));
		}

		Map<String, Variant<?>> ipv6 = settings.get("ipv6");
		if (ipv6 == null) {
			ipv6 = new HashMap<>();
			settings.put("ipv6", ipv6);
			ipv6.put("method", new Variant<>("auto"));
		}

		Map<String, Variant<?>> wirelessSecuritySettings = settings.computeIfAbsent("802-11-wireless-security",
				k -> new HashMap<>());
		wirelessSecuritySettings.put("auth-alg", new Variant<>(authAlg));
		wirelessSecuritySettings.put("key-mgmt", new Variant<>(keyMgmt));
		wirelessSecuritySettings.put("psk", new Variant<>(password));

		return settings;
	}

	public Map<String, Variant<?>> getAccessPointProperties(String accessPointPath) throws DBusException {
		validateAccessPointPath(accessPointPath);
		return connection.getRemoteObject(NetworkManager.NM_IFACE, accessPointPath, Properties.class).getAll(AP_IFACE);
	}

	public DBusConnection getConnection() {
		return connection;
	}

	private static void validateDeviceName(String deviceName) {
		Validate.notNull(deviceName, "deviceName is null");
		Validate.notEmpty(deviceName, "deviceName is empty");
	}

	private static void validateDevicePath(Path devicePath) {
		Validate.notNull(devicePath, "devicePath is null");
		Validate.notEmpty(devicePath.getPath(), "devicePath is empty");
	}

	private static void validateSsid(String ssid) {
		Validate.notNull(ssid, "ssid is null");
		Validate.notEmpty(ssid, "ssid is empty");
	}

	private void validateAccessPointPath(String accessPointPath) {
		Validate.notNull(accessPointPath, "accessPointPath is null");
		Validate.notEmpty(accessPointPath, "accessPointPath is empty");
	}
}

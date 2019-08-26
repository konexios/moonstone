package moonstone.selene.device.ble.gatt;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.exceptions.DBusExecutionException;

import moonstone.acn.client.utils.Utils;
import moonstone.selene.Loggable;
import moonstone.selene.device.ble.BleDbusService;
import moonstone.selene.device.ble.BleInfo;
import moonstone.selene.device.ble.BleModule;
import moonstone.selene.device.ble.BleUtils;
import moonstone.selene.device.dbus.Device1;
import moonstone.selene.device.dbus.GattCharacteristic1;
import moonstone.selene.device.dbus.Properties.PropertiesChanged;

public class DbusGatt extends Loggable implements Gatt {
    private BleDbusService bleDbusService = BleDbusService.getInstance();

    private boolean gattConnected;
    private String bleInterface;
    private String bleAddress;
    private ConnectionHandler handler;
    private BleModule<?, ?, ?, ?> module;
    private ConnectionChangedHandler connectionChangedHandler = new ConnectionChangedHandler();
    private String devicePath;
    private Map<String, String> readableCharPathMap = new HashMap<>();
    private Map<String, String> writeableCharPathMap = new HashMap<>();

    DbusGatt(BleModule<?, ?, ?, ?> module) {
        this.module = module;
    }

    @Override
    public void disconnect() {
        String method = "disconnect";
        try {
            bleDbusService.getDbusService().removeSignalHandler(PropertiesChanged.class, connectionChangedHandler);
        } catch (DBusException e) {
            logWarn(method, "could not remove signal handler");
        }
        disconnectBle();
    }

    private void disconnectBle() {
        String method = "disconnectBle";
        try {
            bleDbusService.disconnectBle(bleInterface, bleAddress);
        } catch (DBusException e) {
            logWarn(method, "could not disconnect device");
        }
    }

    @Override
    public boolean isConnected() {
        return gattConnected;
    }

    @Override
    public void setNotificationHandler(NotificationHandler handler) {
        String method = "setNotificationHandler";
        try {
            bleDbusService.getDbusService().addSignalHandler(PropertiesChanged.class,
                    new PropertiesChangedHandler(handler));
        } catch (DBusException e) {
            logError(method, "cannot set property change notification", e);
        }
    }

    @Override
    public void setConnectionHandler(ConnectionHandler handler) {
        this.handler = handler;
    }

    @Override
    public void controlSensor(String uuid, String value) {
        String method = "controlSensor";
        try {
            String path = writeableCharPathMap.get(uuid);
            if (path != null) {
                bleDbusService.writeValue(bleInterface, bleAddress, path, BleUtils.hexStringToByteArray(value));
            } else {
                logWarn(method, "dbus path not found for uuid: %s", uuid);
            }
        } catch (DBusException e) {
            logError(method, "cannot write value", e);
        }
    }

    @Override
    public void enableNotification(String uuid, String value) {
        String method = "enableNotification";
        try {
            String path = readableCharPathMap.get(uuid);
            if (path != null) {
                bleDbusService.startNotify(bleInterface, bleAddress, path);
            } else {
                logWarn(method, "dbus path not found for uuid: %s", uuid);
            }
        } catch (DBusException e) {
            logError(method, "cannot enable notification", e);
        } catch (DBusExecutionException e) {
            logWarn(method, "notification enabling is in progress");
        }
    }

    @Override
    public void disableNotification(String uuid, String value) {
        String method = "disableNotification";
        try {
            String path = readableCharPathMap.get(uuid);
            if (path != null) {
                bleDbusService.stopNotify(bleInterface, bleAddress, path);
            } else {
                logWarn(method, "dbus path not found for uuid: %s", uuid);
            }
        } catch (DBusException e) {
            logError(method, "cannot disable notification", e);
        }
    }

    @Override
    public void setPeriod(String uuid, String value) {
        String method = "setPeriod";
        try {
            String path = readableCharPathMap.get(uuid);
            if (path != null) {
                bleDbusService.writeValue(bleInterface, bleAddress, path, BleUtils.hexStringToByteArray(value));
            } else {
                logWarn(method, "dbus path not found for uuid: %s", uuid);
            }
        } catch (DBusException e) {
            logError(method, "cannot write value", e);
        }
    }

    @Override
    public byte[] readValue(String uuid) {
        String method = "readValue";
        byte[] result = null;
        try {
            String path = readableCharPathMap.get(uuid);
            if (path != null) {
                result = bleDbusService.readValue(bleInterface, bleAddress, path);
            } else {
                logWarn(method, "dbus path not found for uuid: %s", uuid);
            }
        } catch (DBusException e) {
            logError(method, "cannot read value", e);
        }
        return result;
    }

    @Override
    public void connect(String bleInterface, String bleAddress, long retryInterval, boolean randomAddress) {
        String method = "connect";
        this.bleInterface = bleInterface;
        this.bleAddress = bleAddress;
        devicePath = bleDbusService.buildDevicePath(bleInterface, bleAddress);
        logInfo(method, "trying to connect - interface: %s, address: %s", bleInterface, bleAddress, randomAddress);
        if (bleDbusService.getDbusService().connect()) {
            try {
                bleDbusService.getDbusService().addSignalHandler(PropertiesChanged.class, connectionChangedHandler);
                gattConnected = bleDbusService.isBleConnected(bleInterface, bleAddress);
                if (gattConnected) {
                    loadMaps();
                    handler.handleConnection();
                } else {
                    connectBle();
                }
            } catch (DBusException e) {
                logError(method, "cannot add connection handler", e);
            }
        } else {
            logError(method, "cannot connect to DBus");
        }
    }

    @Override
    public BleInfo populateInfo(BleInfo info) {
        String method = "populateInfo";
        logWarn(method, "not implemented for DbusGatt!");
        return info;
    }

    private void connectBle() {
        String method = "connectBle";
        while (!gattConnected) {
            try {
                logInfo(method, "connecting to device ...");
                bleDbusService.connectBle(bleInterface, bleAddress);
            } catch (DBusException e) {
                logError(method, "cannot connect to device", e);
            } catch (DBusExecutionException e) {
                logError(method, e);
                disconnectBle();
            }
            try {
                logInfo(method, "retrying in 5s ...");
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                // ignore
            }
        }
    }

    @Override
    public Map<String, String> getCharacteristicMap() {
        // return characteristics that are readable or notify-able
        return Collections.unmodifiableMap(readableCharPathMap);
    }

    public BleModule<?, ?, ?, ?> getModule() {
        return module;
    }

    private void loadMaps() {
        String method = "loadMaps";
        logInfo(method, "loading maps for device (%s) at path (%s)", bleAddress, devicePath);
        try {
            Set<String> paths = new HashSet<>();

            // Make sure all paths are discovered; try 10 times
            int count = 10;
            while (count > 0) {
                paths.addAll(bleDbusService.getDeviceCharacteristicsPath(bleInterface, bleAddress, devicePath));
                if (paths.isEmpty()) {
                    logWarn(method, "paths are not found, retrying in 5 seconds...");
                    Utils.sleep(5000L);
                } else {
                    break;
                }
                count--;
            }

            paths.forEach(path -> {
                try {
                    String uuid = bleDbusService.getGattCharacteristicsUuid(path);
                    List<String> flags = bleDbusService.getGattCharacteristicsFlags(path);

                    if (flags.contains("notify") || flags.contains("indicate") || flags.contains("read")) {
                        readableCharPathMap.put(uuid, path);
                    }
                    if (flags.contains("write") || flags.contains("write-without-response")) {
                        writeableCharPathMap.put(uuid, path);
                    }
                } catch (DBusException e) {
                    logError(method, "cannot fetch characteristic flags", e);
                } catch (DBusExecutionException e) {
                    logWarn(method, "cannot fetch characteristic flags", e);
                }
            });
        } catch (DBusException e) {
            logError(method, "cannot load maps", e);
        }
    }

    private class PropertiesChangedHandler implements DBusSigHandler<PropertiesChanged> {
        private final NotificationHandler handler;

        public PropertiesChangedHandler(NotificationHandler handler) {
            this.handler = handler;
        }

        @Override
        public void handle(PropertiesChanged propertiesChanged) {
            if (Objects.equals(propertiesChanged.getIface(), GattCharacteristic1.GATT_CHARACTERISTIC1_IFACE)) {
                propertiesChanged.getProperties().entrySet().stream()
                        .filter(entry -> entry.getValue().getValue().getClass().isArray()).forEach(entry -> {
                            byte[] bytes = (byte[]) entry.getValue().getValue();
                            String path = propertiesChanged.getPath();
                            handler.handleNotification(path, bytes);
                        });
            }
        }
    }

    private class ConnectionChangedHandler implements DBusSigHandler<PropertiesChanged> {
        @Override
        public void handle(PropertiesChanged props) {
            String method = "ConnectionChangedHandler.handle";
            if (Objects.equals(props.getIface(), Device1.DEVICE1_IFACE)
                    && Objects.equals(props.getPath(), devicePath)) {
                props.getProperties().entrySet().stream().filter(entry -> Objects.equals(entry.getKey(), "Connected"))
                        .forEach(entry -> {
                            gattConnected = (Boolean) entry.getValue().getValue();
                            if (gattConnected) {
                                logInfo(method, "device connected");
                                loadMaps();
                                handler.handleConnection();
                            } else {
                                handler.handleDisconnection();
                                logInfo(method, "device disconnected");
                                connectBle();
                            }
                        });
            }
        }
    }
}

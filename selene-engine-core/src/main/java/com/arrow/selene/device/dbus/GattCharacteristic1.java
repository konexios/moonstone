package com.arrow.selene.device.dbus;

import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusMemberName;
import org.freedesktop.dbus.Variant;

@DBusInterfaceName(GattCharacteristic1.GATT_CHARACTERISTIC1_IFACE)
public interface GattCharacteristic1 extends DBusInterface {
    String GATT_CHARACTERISTIC1_IFACE = "org.bluez.GattCharacteristic1";

    @DBusMemberName("ReadValue")
    byte[] readValue(Map<String, Variant<?>> options);

    @DBusMemberName("WriteValue")
    void writeValue(byte[] value, Map<String, Variant<?>> options);

    @DBusMemberName("StartNotify")
    void startNotify();

    @DBusMemberName("StopNotify")
    void stopNotify();
}

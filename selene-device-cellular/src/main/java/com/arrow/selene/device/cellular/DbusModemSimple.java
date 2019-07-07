package com.arrow.selene.device.cellular;

import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusMemberName;
import org.freedesktop.dbus.Variant;

@DBusInterfaceName(DbusModemSimple.MODEM_SIMPLE_IFACE)
public interface DbusModemSimple extends DBusInterface {
    String MODEM_SIMPLE_IFACE = DbusModem.MODEM_IFACE + ".Simple";

    @DBusMemberName("GetStatus")
    Map<String, Variant<?>> getStatus();
}

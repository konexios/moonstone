package com.arrow.selene.device.cellular;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusMemberName;
import org.freedesktop.dbus.UInt32;

@DBusInterfaceName(DbusModem.MODEM_IFACE)
public interface DbusModem extends DBusInterface {
    String MODEM_IFACE = DbusModemManager1.MODEM_MANAGER1_IFACE + ".Modem";

    @DBusMemberName("Command")
    String Command(String command, UInt32 timeout);
}

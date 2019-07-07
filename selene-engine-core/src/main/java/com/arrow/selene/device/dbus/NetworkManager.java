package com.arrow.selene.device.dbus;

import java.util.List;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusMemberName;
import org.freedesktop.dbus.Path;

@DBusInterfaceName(NetworkManager.NM_IFACE)
public interface NetworkManager extends DBusInterface {
    String NM_IFACE = "org.freedesktop.NetworkManager";
    String NM_PATH = "/org/freedesktop/NetworkManager";

    @DBusMemberName("ActivateConnection")
    Path activateConnection(Path connection, Path device, Path accessPoint);

//        TODO need investigate how to return 2 objects from method
//        @DBusMemberName("AddAndActivateConnection")
//        Path[] addAndActivateConnection(Map<String, Map<String, Variant>> connection, Path device, Path accessPoint);

    @DBusMemberName("DeactivateConnection")
    void deactivateConnection(Path device);

    @DBusMemberName("GetDevices")
    List<Path> getDevices();
}

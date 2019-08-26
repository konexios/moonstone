package moonstone.selene.device.dbus;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusMemberName;

@DBusInterfaceName(Device1.DEVICE1_IFACE)
public interface Device1 extends DBusInterface {
    String DEVICE1_IFACE = "org.bluez.Device1";

    @DBusMemberName("Connect")
    void connect();

    @DBusMemberName("Disconnect")
    void disconnect();
}

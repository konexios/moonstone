package moonstone.selene.device.dbus;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusMemberName;

@DBusInterfaceName(Adapter1.ADAPTER1_IFACE)
public interface Adapter1 extends DBusInterface {
    String ADAPTER1_IFACE = "org.bluez.Adapter1";

    @DBusMemberName("StartDiscovery")
    void startDiscovery();

    @DBusMemberName("StopDiscovery")
    void stopDiscovery();
}

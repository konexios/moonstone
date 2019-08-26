package moonstone.selene.device.dbus;

import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusMemberName;
import org.freedesktop.dbus.Variant;

@DBusInterfaceName(Connection.CONNECTION_IFACE)
public interface Connection extends DBusInterface {
    String CONNECTION_IFACE = NetworkManager.NM_IFACE + ".Settings.Connection";

    @DBusMemberName("GetSettings")
    Map<String, Map<String, Variant<?>>> getSettings();

    @DBusMemberName("Update")
    void update(Map<String, Map<String, Variant<?>>> properties);
}

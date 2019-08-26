package moonstone.selene.device.dbus;

import java.util.List;
import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusMemberName;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.Variant;

@DBusInterfaceName(Settings.SETTINGS_IFACE)
public interface Settings extends DBusInterface {
    String SETTINGS_IFACE = NetworkManager.NM_IFACE + ".Settings";
    String SETTINGS_PATH = NetworkManager.NM_PATH + "/Settings";

    @DBusMemberName("AddConnection")
    Path addConnection(Map<String, Map<String, Variant<?>>> connection);

    @DBusMemberName("ListConnections")
    List<Path> listConnections();
}

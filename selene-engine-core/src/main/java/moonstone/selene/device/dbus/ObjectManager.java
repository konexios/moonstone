package moonstone.selene.device.dbus;

import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusMemberName;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.Variant;

@DBusInterfaceName(ObjectManager.OBJECT_MANAGER_IFACE)
public interface ObjectManager extends DBusInterface {
    String OBJECT_MANAGER_IFACE = "org.freedesktop.DBus.ObjectManager";

    @DBusMemberName("GetManagedObjects")
    Map<Path, Map<String, Map<String, Variant<?>>>> getManagedObjects();
}

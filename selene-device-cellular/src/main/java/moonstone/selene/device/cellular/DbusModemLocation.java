package moonstone.selene.device.cellular;

import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusMemberName;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;

@DBusInterfaceName(DbusModemLocation.MODEM_LOCATION_IFACE)
public interface DbusModemLocation extends DBusInterface {
    String MODEM_LOCATION_IFACE = DbusModemManager1.MODEM_MANAGER1_IFACE + ".Modem.Location";

    @DBusMemberName("GetLocation")
    Map<UInt32, Variant<?>> getLocation();
}

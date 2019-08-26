package moonstone.selene.device.cellular;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;

@DBusInterfaceName(DbusModemManager1.MODEM_MANAGER1_IFACE)
public interface DbusModemManager1 extends DBusInterface {
    String MODEM_MANAGER1_IFACE = "org.freedesktop.ModemManager1";
    String MODEM_MANAGER1_PATH = "/org/freedesktop/ModemManager1";
}

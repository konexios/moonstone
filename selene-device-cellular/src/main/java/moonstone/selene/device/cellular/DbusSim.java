package moonstone.selene.device.cellular;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;

@DBusInterfaceName(DbusSim.SIM_IFACE)
public interface DbusSim extends DBusInterface {
    String SIM_IFACE = DbusModemManager1.MODEM_MANAGER1_IFACE + ".Sim";
}

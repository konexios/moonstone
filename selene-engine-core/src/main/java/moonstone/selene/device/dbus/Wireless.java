package moonstone.selene.device.dbus;

import java.util.List;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusMemberName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.exceptions.DBusException;

@DBusInterfaceName(Wireless.WIRELESS_IFACE)
public interface Wireless extends DBusInterface {
    String WIRELESS_IFACE = NetworkManager.NM_IFACE + ".Device.Wireless";

    @DBusMemberName("GetAccessPoints")
    List<Path> getAccessPoints();

    @DBusInterfaceName(WIRELESS_IFACE)
    class AccessPointAdded extends DBusSignal {
        private Path accessPoint;

        public AccessPointAdded(String path, Path accessPoint) throws DBusException {
            super(path, accessPoint);
            this.accessPoint = accessPoint;
        }

        public Path getAccessPoint() {
            return accessPoint;
        }
    }

    @DBusInterfaceName(WIRELESS_IFACE)
    class AccessPointRemoved extends DBusSignal {
        private Path accessPoint;

        public AccessPointRemoved(String path, Path accessPoint) throws DBusException {
            super(path, accessPoint);
            this.accessPoint = accessPoint;
        }

        public Path getAccessPoint() {
            return accessPoint;
        }
    }
}

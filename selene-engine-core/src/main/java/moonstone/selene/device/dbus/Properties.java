package moonstone.selene.device.dbus;

import java.util.List;
import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusMemberName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

@DBusInterfaceName(Properties.PROPERTIES_IFACE)
public interface Properties extends DBusInterface {
    String PROPERTIES_IFACE = "org.freedesktop.DBus.Properties";

    @DBusMemberName("Get")
    <A> A get(String iface, String name);

    @DBusMemberName("Set")
    <A> void set(String iface, String name, A value);

    @DBusMemberName("GetAll")
    Map<String, Variant<?>> getAll(String iface);

    @DBusInterfaceName(PROPERTIES_IFACE)
    class PropertiesChanged extends DBusSignal {
        private String iface;
        private Map<String, Variant<?>> properties;
        private List<String> var3;

        public PropertiesChanged(String path, String iface, Map<String, Variant<?>> properties, List<String> var3)
                throws DBusException {
            super(path, iface, properties, var3);
            this.iface = iface;
            this.properties = properties;
            this.var3 = var3;
        }

        public String getIface() {
            return iface;
        }

        public Map<String, Variant<?>> getProperties() {
            return properties;
        }

        public List<String> getVar3() {
            return var3;
        }
    }
}

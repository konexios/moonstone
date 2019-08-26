package moonstone.selene.device.ble.beacon.advertising;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.freedesktop.dbus.UInt16;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

import moonstone.selene.device.ble.BleDbusService;
import moonstone.selene.device.ble.beacon.BeaconControllerModule;
import moonstone.selene.device.ble.beacon.BeaconPacket;
import moonstone.selene.device.ble.beacon.BeaconPacket.BeaconData;
import moonstone.selene.device.dbus.Properties.PropertiesChanged;

public class DbusAdvertisingReader extends AdvertisingReader {

    private BleDbusService bleDbusService = BleDbusService.getInstance();

    protected DbusAdvertisingReader(BeaconControllerModule controller) {
        super(controller);
    }

    @Override
    public void startScan(BeaconControllerModule controller) {
        String method = "startScan";
        try {
            logInfo(method, "starting scan...");
            bleDbusService.getDbusService().connect();
            bleDbusService.startBleDiscovery(controller.getInfo().getBleInterface());
            bleDbusService.getDbusService().addSignalHandler(PropertiesChanged.class, propertiesChanged -> {
                @SuppressWarnings("unchecked")
                Variant<Map<UInt16, Variant<byte[]>>> manufacturerData = (Variant<Map<UInt16, Variant<byte[]>>>) propertiesChanged
                        .getProperties().get("ManufacturerData");
                if (manufacturerData != null) {
                    String macAddress = propertiesChanged.getPath()
                            .substring(propertiesChanged.getPath().lastIndexOf("/dev_") + 5).replace('_', ':');

                    if (ignoredMacSet.contains(macAddress)) {
                        logDebug(method, "ignored: %s", macAddress);
                        return;
                    }
                    logInfo(method, "mac: %s", macAddress);
                    BeaconPacket packet = new BeaconPacket();
                    packet.setBroadcastAddress(macAddress);

                    Map<UInt16, Variant<byte[]>> value = manufacturerData.getValue();
                    if (value != null) {
                        for (Entry<UInt16, Variant<byte[]>> entry : value.entrySet()) {
                            UInt16 companyId = entry.getKey();
                            logDebug(method, "found companyId: %s", companyId);

                            short companyIdValue = companyId.shortValue();
                            String companyIdString = String.format("0x%02X", companyIdValue);
                            String moduleClass = controller.getInfo().findMapping(companyIdString);
                            if (StringUtils.isNotEmpty(moduleClass)) {
                                BeaconData beaconData = new BeaconData();
                                beaconData.setType(BeaconPacket.ADV_DATA_TYPE_MANUFACTURER);
                                byte[] dataBytes = new byte[entry.getValue().getValue().length + 2];
                                dataBytes[0] = (byte) (companyIdValue & 0xFF);
                                dataBytes[1] = (byte) (companyIdValue >> 8);
                                System.arraycopy(entry.getValue().getValue(), 0, dataBytes, 2,
                                        entry.getValue().getValue().length);
                                beaconData.setData(dataBytes);
                                packet.getData().add(beaconData);
                                handlePacket(macAddress, packet, companyIdString, moduleClass);
                            } else {
                                ignoredMacSet.add(macAddress);
                                logInfo(method, "unsupported companyId: %s", companyId);
                            }
                        }
                    }
                }
            });
        } catch (DBusException e) {
            logError(method, "error during scan starting", e);
        }
    }

    @Override
    public void cleanup() {
        String method = "clean";
        try {
            logInfo(method, "stopping discovery...");
            bleDbusService.stopBleDiscovery(controller.getInfo().getBleInterface());
            logInfo(method, "discovery stopped");
        } catch (DBusException e) {
            logError(method, "cannot stop discovery", e);
        }
    }
}

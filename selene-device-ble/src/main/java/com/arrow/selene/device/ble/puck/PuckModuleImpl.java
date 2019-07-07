package com.arrow.selene.device.ble.puck;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Validate;

import com.arrow.acs.AcsUtils;
import com.arrow.selene.device.ble.beacon.BeaconControllerModule;
import com.arrow.selene.device.ble.beacon.BeaconPacket;
import com.arrow.selene.device.ble.beacon.BeaconPacket.BeaconData;
import com.arrow.selene.engine.DeviceModuleAbstract;
import com.arrow.selene.engine.ModuleState;
import com.arrow.selene.engine.Utils;

public class PuckModuleImpl extends DeviceModuleAbstract<PuckInfo, PuckProperties, PuckStates, PuckData>
        implements PuckModule {
    private static final Pattern COLON = Pattern.compile(":", Pattern.LITERAL);

    @Override
    public void init(BeaconControllerModule controller, BeaconPacket packet) {
        Validate.notNull(controller, "controller is null");
        Validate.notNull(packet, "packet is null");
        Properties props = new Properties();
        props.setProperty("bleInterface", controller.getInfo().getBleInterface());
        props.setProperty("maxPollingIntervalMs",
                String.valueOf(controller.getProperties().getBeaconMaxPollingIntervalMs()));
        props.setProperty("bleAddress", packet.getBroadcastAddress());
        PuckDataImpl<?> data = parsePacket(packet);
        props.setProperty("companyId", data.getCompanyId());
        props.setProperty("name", String.format("SensorPuck-%s", data.getAddress()));
        props.setProperty("uid", String.format("puck-%s",
                COLON.matcher(packet.getBroadcastAddress()).replaceAll(Matcher.quoteReplacement("")).toLowerCase()));
        init(props);
    }

    @Override
    public void receive(BeaconPacket packet) {
        Validate.notNull(packet, "packet is null");
        if (getState() == ModuleState.STARTED) {
            if (getService() != null) {
                getService().execute(() -> queueDataForSending(parsePacket(packet)));
            } else {
                queueDataForSending(parsePacket(packet));
            }
        }
    }

    @Override
    protected PuckProperties createProperties() {
        return new PuckProperties();
    }

    @Override
    protected PuckInfo createInfo() {
        return new PuckInfo();
    }

    @Override
    protected PuckStates createStates() {
        return new PuckStates();
    }

    @Override
    public String getMacAddress() {
        return getInfo().getBleAddress();
    }

    private PuckDataImpl<?> parsePacket(BeaconPacket packet) {
        String method = "parsePacket";
        PuckDataImpl<?> data = new PuckDataImpl<>();
        BeaconData beaconData = packet.findBeaconData(BeaconPacket.ADV_DATA_TYPE_MANUFACTURER);
        Validate.notNull(beaconData, "Manufacturer ADV record not found!");
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(beaconData.getData()));
        try {
            short companyId = Utils.readSwappedShort(dis);
            logDebug(method, "companyId: %s", Integer.toHexString(companyId));
            data.setCompanyId(String.format("0x%02X", companyId));

            byte mode = dis.readByte();
            data.setMode(mode);
            logDebug(method, "mode: %d", mode);

            int sequence = dis.readByte() & 0xff;
            data.setSequence(sequence);
            logDebug(method, "sequence: %d", sequence);

            short address = Utils.readSwappedShort(dis);
            data.setAddress(Integer.toHexString(address));
            logDebug(method, "address: %s", data.getAddress());

            if (mode == (byte) 0x00) {
                double humidity = (double) Utils.readSwappedShort(dis) / 10;
                logDebug(method, "humidity: %.2f", humidity);
                data.setHumidity(humidity);

                double temperature = Utils.celsiusToFahrenheit(Utils.readSwappedShort(dis) / 10);
                logDebug(method, "temperature: %.2f", temperature);
                data.setTemperature(temperature);

                double light = Utils.readSwappedShort(dis) * 2.0;
                logDebug(method, "light: %.2f", light);
                data.setLight(light);

                int uv = dis.readByte() & 0xff;
                logDebug(method, "uv: %d", (int) uv);
                data.setUvIndex(uv);
            } else if (mode == (byte) 0x01) {
                int heartRateState = dis.readByte() & 0xff;
                logDebug(method, "heartRateState: %d", heartRateState);
                data.setHeartRateState(heartRateState);

                int heartRate = dis.readByte() & 0xff;
                logDebug(method, "heartRate: %d", heartRate);
                data.setHeartRate(heartRate);
            } else {
                logError(method, "UNKNOWN MODE: %d", mode);
            }
        } catch (Exception e) {
            logError("unable to parse packet", e);
        } finally {
            AcsUtils.close(dis);
        }
        return data;
    }
}

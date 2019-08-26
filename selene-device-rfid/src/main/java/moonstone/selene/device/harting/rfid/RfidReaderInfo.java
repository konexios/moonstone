package moonstone.selene.device.harting.rfid;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import moonstone.selene.engine.DeviceInfo;

public class RfidReaderInfo extends DeviceInfo {
    private static final long serialVersionUID = -4077271649829068952L;

    public static final String DEFAULT_DEVICE_TYPE = "harting-rfid-reader-r400";

    private String address = "192.168.10.10";
    private int port = 10001;
    private int configVersion = -1;
    private byte[] softwareRevision = new byte[3];
    private int hardwareType;
    private int readerFirmware;
    private byte[] transponderTypes = new byte[2];
    private int maxRxBufferSize;
    private int maxTxBufferSize;

    public RfidReaderInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }

    @Override
    public RfidReaderInfo populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        address = map.getOrDefault("address", address);
        port = Integer.parseInt(map.getOrDefault("port", "" + port));
        configVersion = Integer.parseInt(map.getOrDefault("configVersion", "" + configVersion));
        softwareRevision = map.getOrDefault("softwareRevision", new String(softwareRevision, StandardCharsets.UTF_8))
                .getBytes();
        hardwareType = Integer.parseInt(map.getOrDefault("hardwareType", "" + hardwareType));
        readerFirmware = Integer.parseInt(map.getOrDefault("readerFirmware", "" + readerFirmware));
        transponderTypes = map.getOrDefault("transponderTypes", new String(transponderTypes, StandardCharsets.UTF_8))
                .getBytes();
        maxRxBufferSize = Integer.parseInt(map.getOrDefault("maxRxBufferSize", "" + maxRxBufferSize));
        maxTxBufferSize = Integer.parseInt(map.getOrDefault("maxTxBufferSize", "" + maxTxBufferSize));
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        if (address != null)
            map.put("address", address);
        map.put("port", "" + port);
        map.put("configVersion", Integer.toString(configVersion));
        if (softwareRevision != null)
            map.put("softwareRevision", new String(softwareRevision, StandardCharsets.UTF_8));
        map.put("hardwareType", Integer.toString(hardwareType));
        map.put("readerFirmware", Integer.toString(readerFirmware));
        if (transponderTypes != null)
            map.put("transponderTypes", new String(transponderTypes, StandardCharsets.UTF_8));
        map.put("maxRxBufferSize", Integer.toString(maxRxBufferSize));
        map.put("maxTxBufferSize", Integer.toString(maxTxBufferSize));
        return map;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(int configVersion) {
        this.configVersion = configVersion;
    }

    public byte[] getSoftwareRevision() {
        return softwareRevision;
    }

    public void setSoftwareRevision(byte[] softwareRevision) {
        this.softwareRevision = softwareRevision;
    }

    public int getHardwareType() {
        return hardwareType;
    }

    public void setHardwareType(int hardwareType) {
        this.hardwareType = hardwareType;
    }

    public int getReaderFirmware() {
        return readerFirmware;
    }

    public void setReaderFirmware(int readerFirmware) {
        this.readerFirmware = readerFirmware;
    }

    public byte[] getTransponderTypes() {
        return transponderTypes;
    }

    public void setTransponderTypes(byte[] transponderTypes) {
        this.transponderTypes = transponderTypes;
    }

    public int getMaxRxBufferSize() {
        return maxRxBufferSize;
    }

    public void setMaxRxBufferSize(int maxRxBufferSize) {
        this.maxRxBufferSize = maxRxBufferSize;
    }

    public int getMaxTxBufferSize() {
        return maxTxBufferSize;
    }

    public void setMaxTxBufferSize(int maxTxBufferSize) {
        this.maxTxBufferSize = maxTxBufferSize;
    }
}

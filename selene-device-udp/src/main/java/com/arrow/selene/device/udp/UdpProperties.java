package com.arrow.selene.device.udp;

import java.util.Map;

import com.arrow.selene.engine.DeviceProperties;

public class UdpProperties extends DeviceProperties {
    private static final long serialVersionUID = 4231327270469530971L;
    static final String ALL_INTERFACES_ADDRESS = "0.0.0.0";

    private String address = ALL_INTERFACES_ADDRESS;
    private int port;

    @Override
    public UdpProperties populateFrom(Map<String, String> map) {
        super.populateFrom(map);
        address = map.getOrDefault("address", address);
        port = Integer.parseInt(map.getOrDefault("port", Integer.toString(port)));
        return this;
    }

    @Override
    public Map<String, String> populateTo(Map<String, String> map) {
        super.populateTo(map);
        if (address != null)
            map.put("address", address);
        map.put("port", Integer.toString(port));
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
}

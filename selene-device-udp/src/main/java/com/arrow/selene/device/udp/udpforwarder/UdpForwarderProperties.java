package com.arrow.selene.device.udp.udpforwarder;

import com.arrow.selene.device.udp.UdpProperties;

public class UdpForwarderProperties extends UdpProperties {
    private static final long serialVersionUID = 816255533713205903L;

    private String deviceAddress;
    private int devicePort;

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public int getDevicePort() {
        return devicePort;
    }

    public void setDevicePort(int devicePort) {
        this.devicePort = devicePort;
    }
}

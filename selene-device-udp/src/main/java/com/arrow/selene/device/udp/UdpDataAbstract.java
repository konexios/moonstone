package com.arrow.selene.device.udp;

import com.arrow.selene.engine.DeviceDataAbstract;

public abstract class UdpDataAbstract extends DeviceDataAbstract implements UdpData {

    private byte[] rawData;

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

    protected byte[] getRawData() {
        return rawData;
    }
}

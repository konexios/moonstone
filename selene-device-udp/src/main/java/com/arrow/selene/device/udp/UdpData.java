package com.arrow.selene.device.udp;

import com.arrow.selene.engine.DeviceData;

public interface UdpData extends DeviceData {
    int getSize();

    boolean parseRawData(byte[] data);
}

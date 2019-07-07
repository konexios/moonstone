package com.arrow.selene.device.udp.gps;

import com.arrow.selene.device.udp.UdpInfo;

public class GpsInfo extends UdpInfo {
    private static final long serialVersionUID = -601838211747914834L;

    public static final String DEFAULT_DEVICE_TYPE = "gps-nmea";

    public GpsInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }
}

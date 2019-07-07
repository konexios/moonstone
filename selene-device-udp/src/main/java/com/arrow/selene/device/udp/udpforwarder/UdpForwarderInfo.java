package com.arrow.selene.device.udp.udpforwarder;

import com.arrow.selene.device.udp.UdpInfo;

public class UdpForwarderInfo extends UdpInfo {
    private static final long serialVersionUID = 7121557380998426190L;

    public static final String DEFAULT_DEVICE_TYPE = "udp-forwarder";

    public UdpForwarderInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }
}

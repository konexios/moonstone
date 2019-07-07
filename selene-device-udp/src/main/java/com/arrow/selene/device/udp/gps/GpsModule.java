package com.arrow.selene.device.udp.gps;

import com.arrow.selene.device.udp.UdpModuleAbstract;

public class GpsModule extends UdpModuleAbstract<GpsInfo, GpsProperties, GpsStates, GpsData> {
    @Override
    public GpsData createUdpData() {
        return new GpsData();
    }

    @Override
    protected GpsProperties createProperties() {
        return new GpsProperties();
    }

    @Override
    protected GpsInfo createInfo() {
        return new GpsInfo();
    }

    @Override
    protected GpsStates createStates() {
        return new GpsStates();
    }
}

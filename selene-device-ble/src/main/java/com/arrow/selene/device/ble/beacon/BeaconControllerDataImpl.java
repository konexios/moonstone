package com.arrow.selene.device.ble.beacon;

import java.util.Collections;
import java.util.List;

import com.arrow.acn.client.IotParameters;
import com.arrow.selene.data.Telemetry;
import com.arrow.selene.engine.DeviceDataAbstract;

public class BeaconControllerDataImpl extends DeviceDataAbstract implements BeaconControllerData {

    @Override
    public IotParameters writeIoTParameters() {
        return new IotParameters();
    }

    @Override
    public List<Telemetry> writeTelemetries() {
        return Collections.emptyList();
    }
}

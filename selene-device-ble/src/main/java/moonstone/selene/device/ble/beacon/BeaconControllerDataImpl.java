package moonstone.selene.device.ble.beacon;

import java.util.Collections;
import java.util.List;

import moonstone.acn.client.IotParameters;
import moonstone.selene.data.Telemetry;
import moonstone.selene.engine.DeviceDataAbstract;

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

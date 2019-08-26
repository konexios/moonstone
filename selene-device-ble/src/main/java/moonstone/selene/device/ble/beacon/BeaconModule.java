package moonstone.selene.device.ble.beacon;

import moonstone.selene.engine.Module;

public interface BeaconModule extends Module {
    void init(BeaconControllerModule controller, BeaconPacket packet);

    String getMacAddress();

    void receive(BeaconPacket packet);
}

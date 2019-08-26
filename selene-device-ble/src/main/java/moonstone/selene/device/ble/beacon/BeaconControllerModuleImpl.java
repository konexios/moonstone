package moonstone.selene.device.ble.beacon;

import moonstone.selene.device.ble.beacon.advertising.AdvertisingFactory;
import moonstone.selene.device.ble.beacon.advertising.AdvertisingReader;
import moonstone.selene.engine.DeviceModuleAbstract;

public class BeaconControllerModuleImpl extends
		DeviceModuleAbstract<BeaconControllerInfo, BeaconControllerProperties, BeaconControllerStates, BeaconControllerData>
		implements BeaconControllerModule {

	private AdvertisingReader advertisingReader;

	@Override
	protected void startDevice() {
		String method = "BeaconControllerModuleImpl.sendCommand";
		super.startDevice();
		logInfo(method, "isUseDbus: %s", getProperties().isUseDbus());
		advertisingReader = AdvertisingFactory.initReader(this, getProperties().isUseDbus());
		advertisingReader.startScan(this);
	}

	@Override
	public void stop() {
		advertisingReader.setShuttingDown(true);
		super.stop();
		advertisingReader.cleanup();
	}

	@Override
	protected BeaconControllerProperties createProperties() {
		return new BeaconControllerProperties();
	}

	@Override
	protected BeaconControllerInfo createInfo() {
		return new BeaconControllerInfo();
	}

	@Override
	protected BeaconControllerStates createStates() {
		return new BeaconControllerStates();
	}
}

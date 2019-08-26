package moonstone.selene.device.ble.rhythmp;

import java.util.Properties;

import moonstone.selene.device.ble.BleModuleAbstract;

public class RhythmPlusModuleImpl
		extends BleModuleAbstract<RhythmPlusInfo, RhythmPlusProperties, RhythmPlusStates, RhythmPlusData> {

	@Override
	public boolean isRandomAddress() {
		return true;
	}

	@Override
	public void init(Properties props) {
		HeartRateSensor heartRateSensor = new HeartRateSensor();
		heartRateSensor.init(props);
		registerSensor(heartRateSensor);
		super.init(props);
	}

	@Override
	protected RhythmPlusProperties createProperties() {
		return new RhythmPlusProperties();
	}

	@Override
	protected RhythmPlusInfo createInfo() {
		return new RhythmPlusInfo();
	}

	@Override
	protected RhythmPlusStates createStates() {
		return new RhythmPlusStates();
	}
}

package moonstone.selene.device.zigbee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import moonstone.acn.client.IotParameters;
import moonstone.selene.data.Telemetry;
import moonstone.selene.device.xbee.zdo.data.Neighbor;
import moonstone.selene.engine.DeviceDataAbstract;

public class ZigBeeHealthData extends DeviceDataAbstract implements ZigBeeData {
	private Map<String, Neighbor> neighbors;

	public ZigBeeHealthData(Map<String, Neighbor> neighbors) {
		this.neighbors = neighbors;
	}

	@Override
	public IotParameters writeIoTParameters() {
		IotParameters result = new IotParameters();
		for (Entry<String, Neighbor> entry : neighbors.entrySet()) {
			result.setInteger(String.format("LQI: %s", entry.getKey()), entry.getValue().getLqi());
			result.setBoolean(String.format("On: %s", entry.getKey()), entry.getValue().isOn());
		}
		return result;
	}

	@Override
	public List<Telemetry> writeTelemetries() {
		List<Telemetry> result = new ArrayList<>(neighbors.size() * 2);
		for (Entry<String, Neighbor> entry : neighbors.entrySet()) {
			result.add(writeIntTelemetry(String.format("LQI: %s", entry.getKey()), (long) entry.getValue().getLqi()));
			result.add(writeBooleanTelemetry(String.format("On: %s", entry.getKey()), entry.getValue().isOn()));
		}
		return result;
	}
}

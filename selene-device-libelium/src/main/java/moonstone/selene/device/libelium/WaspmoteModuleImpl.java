package moonstone.selene.device.libelium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import moonstone.acs.JsonUtils;
import moonstone.selene.device.libelium.data.Sensor;
import moonstone.selene.device.libelium.data.SensorParser;
import moonstone.selene.engine.DeviceModuleAbstract;

public class WaspmoteModuleImpl extends DeviceModuleAbstract<WaspmoteInfo, WaspmoteProperties, WaspmoteStates,
		WaspmoteData>
		implements WaspmoteModule {

	private final MeshliumModule meshlium;
	private WaspmoteDataImpl payload;

	public WaspmoteModuleImpl(MeshliumModule meshlium) {
		this.meshlium = meshlium;
		this.payload = new WaspmoteDataImpl();
	}

	@Override
	public void processSensorData(SensorParser data) {
		String method = "processSensorData";
		logDebug(method, "processing id: %s, sensor: %s, value: %s", data.getIdWasp(), data.getSensor(),
		        data.getValue());
		try {
			Sensor sensor = meshlium.getSensorInfo(data.getSensor());
			if (sensor != null) {
				payload.reset();
				payload.setName(data.getSensor());
				payload.setTimestamp(data.getTimestamp().getTime());
				int type = sensor.getValue();
				if (type == 0 || type == 1 || type == 4) {
					if (sensor.getFields().equals(1)) {
						payload.setLongValue(Long.parseLong(data.getValue()));
					} else {
						List<Long> values = new ArrayList<>();
						Arrays.asList(data.getValue().split(";", -1))
						        .forEach(value -> values.add(Long.parseLong(value)));
						if (values.size() > 0)
							payload.setLongArrayValue(values.toArray(new Long[values.size()]));
					}
				} else if (type == 2) {
					if (sensor.getFields().equals(1)) {
						payload.setDoubleValue(Double.parseDouble(data.getValue()));
					} else {
						List<Double> values = new ArrayList<>();
						Arrays.asList(data.getValue().split(";", -1))
						        .forEach(value -> values.add(Double.parseDouble(value)));
						if (values.size() > 0)
							payload.setDoubleArrayValue(values.toArray(new Double[values.size()]));
					}
				} else {
					if (sensor.getFields().equals(1)) {
						payload.setStringValue(data.getValue());
					} else {
						List<String> values = new ArrayList<>();
						Arrays.asList(data.getValue().split(";", -1)).forEach(value -> values.add(value));
						if (values.size() > 0)
							payload.setStringArrayValue(values.toArray(new String[values.size()]));
					}
				}
				queueDataForSending(payload, true);
				if (!getInfo().getInfo().containsKey(data.getSensor())) {
					logInfo(method, "updating device info with sensor definition: %s", data.getSensor());
					getInfo().getInfo().put(data.getSensor(), JsonUtils.toJson(sensor));
					persistUpdatedDeviceInfo();
				}
			} else {
				logError(method, "ERROR: sensor definition not found in database: %s", data.getSensor());
			}
		} catch (Throwable t) {
			logError(method, "ERROR processing data", t);
		}
	}

	@Override
	protected WaspmoteProperties createProperties() {
		return new WaspmoteProperties();
	}

	@Override
	protected WaspmoteInfo createInfo() {
		return new WaspmoteInfo();
	}

	@Override
	protected WaspmoteStates createStates() {
		return new WaspmoteStates();
	}
}

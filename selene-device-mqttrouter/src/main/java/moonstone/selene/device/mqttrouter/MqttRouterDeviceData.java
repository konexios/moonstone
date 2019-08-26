package moonstone.selene.device.mqttrouter;

import java.util.ArrayList;
import java.util.List;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.selene.data.Telemetry;
import moonstone.selene.engine.DeviceDataAbstract;

public class MqttRouterDeviceData extends DeviceDataAbstract {

	private String strData;

	@Override
	public IotParameters writeIoTParameters() {
		IotParameters result = getParsedIotParameters();
		if (strData != null) {
			result = new IotParameters();
			result.setDirty(true);
			result.setString("rawString", strData);
		}
		if (result == null) {
			result = new IotParameters();
		}
		return result;
	}

	@Override
	public List<Telemetry> writeTelemetries() {

		List<Telemetry> result = getParsedTelemetries();
		if (result == null) {
			result = new ArrayList<>();
		}
		if (strData != null) {
			result.add(writeStringTelemetry(TelemetryItemType.String, "rawString", strData));
		}
		return result;
	}

	public String getStrData() {
		return strData;
	}

	public void setStrData(String strData) {
		this.strData = strData;
	}

}

package moonstone.selene.device.self;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.selene.data.Telemetry;
import moonstone.selene.engine.DeviceDataAbstract;

public class SelfDataImpl extends DeviceDataAbstract implements SelfData {
	private byte[] payload;

	public SelfDataImpl withPayload(byte[] payload) {
		setPayload(payload);
		return this;
	}

	@Override
	public IotParameters writeIoTParameters() {
		IotParameters result = getParsedIotParameters();
		if (result == null) {
			result = new IotParameters();
		}
		if (!isParsedFully()) {
			if (payload != null && payload.length > 0) {
				result.setString("payload", new String(payload, StandardCharsets.UTF_8));
			}
		}
		return result;
	}

	@Override
	public List<Telemetry> writeTelemetries() {
		List<Telemetry> result = getParsedTelemetries();
		if (result == null) {
			result = new ArrayList<>(1);
		}
		if (!isParsedFully()) {
			if (payload != null && payload.length > 0) {
				result.add(writeStringTelemetry(TelemetryItemType.String, "payload", new String(payload,
						StandardCharsets.UTF_8)));
			}
		}
		return result;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}
}

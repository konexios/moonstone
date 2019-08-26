package moonstone.selene.device.ble.simba.handlers;

import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.SerializationUtils;

import moonstone.acs.Loggable;
import moonstone.selene.device.ble.simba.SimbaStates;
import moonstone.selene.device.ble.simba.sensor.LedControl;
import moonstone.selene.engine.state.State;
import moonstone.selene.engine.state.StateChangeHandler;

public class LedStateChangeHandler extends Loggable implements StateChangeHandler<SimbaStates> {

	private LedControl ledControl;

	public LedStateChangeHandler(LedControl ledControl) {
		this.ledControl = ledControl;
	}

	@Override
	public void handle(SimbaStates currentStates, Map<String, State> newStates) {
		String method = "handle";
		for (String key : newStates.keySet()) {
			logInfo(method, "****************** key: %s, value: %s", key, newStates.get(key).getValue());
		}
		logInfo(method, "currentStates: %s", currentStates);
		SimbaStates requestStates = SerializationUtils.clone(currentStates);
		requestStates.importStates(newStates);
		logInfo(method, "requestStates: %s", requestStates);

		int value = 0;
		State led = requestStates.getLed();
		value |= Boolean.parseBoolean(led.getValue()) ? 1 : 0;

		logInfo(method, "***************** led: %s", String.valueOf(led));

		byte[] telemetry = new byte[5];
		telemetry[0] = (byte) 0x20;
		telemetry[1] = (byte) 0x00;
		telemetry[2] = (byte) 0x00;
		telemetry[3] = (byte) 0x00;
		telemetry[4] = (byte) value;

		ledControl.setTelemetry(Hex.encodeHexString(telemetry));
	}
}

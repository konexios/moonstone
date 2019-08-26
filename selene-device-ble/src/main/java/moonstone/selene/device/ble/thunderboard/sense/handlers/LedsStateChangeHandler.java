package moonstone.selene.device.ble.thunderboard.sense.handlers;

import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.SerializationUtils;

import moonstone.acs.Loggable;
import moonstone.selene.device.ble.thunderboard.sense.ThunderboardSenseStates;
import moonstone.selene.device.ble.thunderboard.sense.sensor.RgbLedsControl;
import moonstone.selene.engine.state.State;
import moonstone.selene.engine.state.StateChangeHandler;

public class LedsStateChangeHandler extends Loggable implements StateChangeHandler<ThunderboardSenseStates> {
	private static final int LED0_BITMASK = 0b0001;
	private static final int LED1_BITMASK = 0b0010;
	private static final int LED2_BITMASK = 0b0100;
	private static final int LED3_BITMASK = 0b1000;

	private RgbLedsControl rgbLedsControl;

	public LedsStateChangeHandler(RgbLedsControl rgbLedsControl) {
		this.rgbLedsControl = rgbLedsControl;
	}

	@Override
	public void handle(ThunderboardSenseStates currentStates, Map<String, State> newStates) {
		String method = "handle";
		for (String key : newStates.keySet()) {
			logInfo(method, "****************** key: %s, value: %s", key, newStates.get(key).getValue());
		}
		logInfo(method, "currentStates: %s", currentStates);
		ThunderboardSenseStates requestStates = SerializationUtils.clone(currentStates);
		requestStates.importStates(newStates);
		logInfo(method, "requestStates: %s", requestStates);

		int value = 0;
		State led0 = requestStates.getLed0();
		value |= Boolean.parseBoolean(led0.getValue()) ? LED0_BITMASK : 0;

		State led1 = requestStates.getLed1();
		value |= Boolean.parseBoolean(led1.getValue()) ? LED1_BITMASK : 0;

		State led2 = requestStates.getLed2();
		value |= Boolean.parseBoolean(led2.getValue()) ? LED2_BITMASK : 0;

		State led3 = requestStates.getLed3();
		value |= Boolean.parseBoolean(led3.getValue()) ? LED3_BITMASK : 0;

		int color = Integer.valueOf(requestStates.getColor().getValue());
		byte red = (byte) (color >> 16 & 0xFF);
		byte green = (byte) (color >> 8 & 0xFF);
		byte blue = (byte) (color & 0xFF);

		logInfo(method, "***************** value: %d, color: %d, red: %d, green: %d, blue: %d", value, color, red,
		        green, blue);

		byte[] telemetry = new byte[4];
		telemetry[0] = (byte) value;
		telemetry[1] = red;
		telemetry[2] = green;
		telemetry[3] = blue;
		rgbLedsControl.setTelemetry(Hex.encodeHexString(telemetry));
	}
}

package com.arrow.selene.device.wifi;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;

import com.arrow.acn.client.IotParameters;
import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.selene.data.Telemetry;
import com.arrow.selene.engine.DbusUtils;
import com.arrow.selene.engine.DeviceDataAbstract;

public class AccessPointData extends DeviceDataAbstract {
	private static final String SSID = "ssid";
	private static final String STRENGTH = "strength";
	private static final String LAST_SEEN = "lastSeen";
	private static final String HW_ADDRESS = "hwAddress";
	private static final String FLAGS = "flags";
	private static final String FREQUENCY = "frequency";
	private static final String MAX_BITRATE = "maxBitrate";
	private static final String MODE = "mode";
	private static final String RSN_FLAGS = "rsnFlags";
	private static final String WPA_FLAGS = "wpaFlags";
	private static final String ERROR = "error";

	private String ssid;
	private byte strength;
	private int lastSeen;
	private String hwAddress;
	private int flags;
	private int frequency;
	private int maxBitrate;
	private int mode;
	private int rsnFlags;
	private int wpaFlags;
	private String error;

	public void parseData(Map<String, Variant<?>> data) {
		ssid = new String(DbusUtils.getByteArray(data.get("Ssid")), StandardCharsets.UTF_8);
		strength = (Byte) data.get("Strength").getValue();
		lastSeen = (Integer) data.get("LastSeen").getValue();
		hwAddress = (String) data.get("HwAddress").getValue();
		flags = ((UInt32) data.get("Flags").getValue()).intValue();
		frequency = ((UInt32) data.get("Frequency").getValue()).intValue();
		maxBitrate = ((UInt32) data.get("MaxBitrate").getValue()).intValue();
		mode = ((UInt32) data.get("Mode").getValue()).intValue();
		rsnFlags = ((UInt32) data.get("RsnFlags").getValue()).intValue();
		wpaFlags = ((UInt32) data.get("WpaFlags").getValue()).intValue();
	}

	@Override
	public IotParameters writeIoTParameters() {
		IotParameters result = new IotParameters();
		if (error == null) {
			result.setString(SSID, ssid);
			result.setInteger(STRENGTH, (int) strength);
			result.setInteger(LAST_SEEN, lastSeen);
			result.setString(HW_ADDRESS, hwAddress);
			result.setInteger(FLAGS, flags);
			result.setInteger(FREQUENCY, frequency);
			result.setInteger(MAX_BITRATE, maxBitrate);
			result.setInteger(MODE, mode);
			result.setInteger(RSN_FLAGS, rsnFlags);
			result.setInteger(WPA_FLAGS, wpaFlags);
		} else {
			result.setString(ERROR, error);
		}
		return result;
	}

	@Override
	public List<Telemetry> writeTelemetries() {
		List<Telemetry> result = new ArrayList<>();
		if (error == null) {
			result.add(writeStringTelemetry(TelemetryItemType.String, SSID, ssid));
			result.add(writeIntTelemetry(STRENGTH, (long) strength));
			result.add(writeIntTelemetry(LAST_SEEN, (long) lastSeen));
			result.add(writeStringTelemetry(TelemetryItemType.String, HW_ADDRESS, hwAddress));
			result.add(writeIntTelemetry(FLAGS, (long) flags));
			result.add(writeIntTelemetry(FREQUENCY, (long) frequency));
			result.add(writeIntTelemetry(MAX_BITRATE, (long) maxBitrate));
			result.add(writeIntTelemetry(MODE, (long) mode));
			result.add(writeIntTelemetry(RSN_FLAGS, (long) rsnFlags));
			result.add(writeIntTelemetry(WPA_FLAGS, (long) wpaFlags));
		} else {
			result.add(writeStringTelemetry(TelemetryItemType.String, ERROR, error));
		}
		return result;
	}

	public void setError(String error) {
		this.error = error;
	}
}

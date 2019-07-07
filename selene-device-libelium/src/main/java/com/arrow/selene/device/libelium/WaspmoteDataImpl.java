package com.arrow.selene.device.libelium;

import java.util.ArrayList;
import java.util.List;

import com.arrow.acn.client.IotParameters;
import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.selene.data.Telemetry;
import com.arrow.selene.engine.DeviceDataAbstract;
import com.arrow.selene.engine.EngineConstants;

public class WaspmoteDataImpl extends DeviceDataAbstract implements WaspmoteData {
	private String name;
	private Long longValue;
	private Long[] longArrayValue;
	private Double doubleValue;
	private Double[] doubleArrayValue;
	private String stringValue;
	private String[] stringArrayValue;

	private IotParameters iotParameter = new IotParameters();
	private List<Telemetry> telemetries = new ArrayList<>();

	public void reset() {
		name = null;
		longValue = null;
		longArrayValue = null;
		doubleValue = null;
		doubleArrayValue = null;
		stringValue = null;
		stringArrayValue = null;

		iotParameter.clear();
		telemetries.clear();
	}

	@Override
	public IotParameters writeIoTParameters() {
		iotParameter.clear();
		if (longValue != null) {
			iotParameter.setLong(name, longValue);
		} else if (longArrayValue != null) {
			for (int i = 0; i < longArrayValue.length; i++) {
				iotParameter.setLong(name + "." + i, longArrayValue[i]);
			}
		} else if (doubleValue != null) {
			iotParameter.setDouble(name, doubleValue, EngineConstants.FORMAT_DECIMAL_2);
		} else if (doubleArrayValue != null) {
			for (int i = 0; i < doubleArrayValue.length; i++) {
				iotParameter.setDouble(name + "." + i, doubleArrayValue[i], EngineConstants.FORMAT_DECIMAL_2);
			}
		} else if (stringValue != null) {
			iotParameter.setString(name, stringValue);
		} else if (stringArrayValue != null) {
			for (int i = 0; i < stringArrayValue.length; i++) {
				iotParameter.setString(name + "." + i, stringArrayValue[i]);
			}
		}
		return iotParameter;
	}

	@Override
	public List<Telemetry> writeTelemetries() {
		telemetries.clear();
		if (longValue != null) {
			telemetries.add(writeIntTelemetry(name, longValue));
		} else if (longArrayValue != null) {
			for (int i = 0; i < longArrayValue.length; i++) {
				telemetries.add(writeIntTelemetry(name + "." + i, longArrayValue[i]));
			}
		} else if (doubleValue != null) {
			telemetries.add(writeFloatTelemetry(name, doubleValue));
		} else if (doubleArrayValue != null) {
			for (int i = 0; i < doubleArrayValue.length; i++) {
				telemetries.add(writeFloatTelemetry(name + "." + i, doubleArrayValue[i]));
			}
		} else if (stringValue != null) {
			telemetries.add(writeStringTelemetry(TelemetryItemType.String, name, stringValue));
		} else if (stringArrayValue != null) {
			for (int i = 0; i < stringArrayValue.length; i++) {
				telemetries.add(writeStringTelemetry(TelemetryItemType.String, name + "." + i, stringArrayValue[i]));
			}
		}
		return telemetries;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getLongValue() {
		return longValue;
	}

	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}

	public Double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Long[] getLongArrayValue() {
		return longArrayValue;
	}

	public void setLongArrayValue(Long[] longArrayValue) {
		this.longArrayValue = longArrayValue;
	}

	public Double[] getDoubleArrayValue() {
		return doubleArrayValue;
	}

	public void setDoubleArrayValue(Double[] doubleArrayValue) {
		this.doubleArrayValue = doubleArrayValue;
	}

	public String[] getStringArrayValue() {
		return stringArrayValue;
	}

	public void setStringArrayValue(String[] stringArrayValue) {
		this.stringArrayValue = stringArrayValue;
	}
}

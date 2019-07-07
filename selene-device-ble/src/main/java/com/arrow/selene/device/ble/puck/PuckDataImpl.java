package com.arrow.selene.device.ble.puck;

import java.util.List;

import com.arrow.acn.client.IotParameters;

import com.arrow.selene.SeleneException;
import com.arrow.selene.data.Telemetry;
import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.sensor.SensorDataImpl;
import com.arrow.selene.engine.EngineConstants;
import com.arrow.selene.engine.Utils;

public class PuckDataImpl<Data extends SensorData<?>> extends SensorDataImpl<Data> implements PuckData {

	private enum HrmState {
		IDLE,
		NO_SIGNAL,
		ACQUIRING,
		ACTIVE,
		INVALID,
		ERROR
	}

	private String companyId;
	private int mode;
	private int sequence;
	private String address;

	// biometric
	private int heartRateState;
	private int heartRate;

	// environment
	private double humidity;
	private double temperature;
	private double light;
	private int uvIndex;

	public PuckDataImpl() {
		super(null);
	}

	@Override
	public IotParameters writeIoTParameters() {
		IotParameters result = new IotParameters();
		if (mode == 1) {
			if (heartRateState == HrmState.ACTIVE.ordinal()) {
				result.setInteger("heartRate", heartRate);
			} else {
				return result;
			}
		} else if (mode == 0) {
			result.setDouble("humidity", humidity, EngineConstants.FORMAT_DECIMAL_2);
			result.setDouble("temperature", temperature, EngineConstants.FORMAT_DECIMAL_2);
			result.setDouble("light", light, EngineConstants.FORMAT_DECIMAL_2);
			result.setInteger("uvIndex", uvIndex);
		} else {
			throw new SeleneException("unsupported mode: " + mode);
		}
		result.setInteger("mode", mode);
		result.setInteger("sequence", sequence);
		return result;
	}

	@Override
	public List<Telemetry> writeTelemetries() {
		List<Telemetry> result = super.writeTelemetries();
		if (mode == 1) {
			if (heartRateState == 3) {
				result.add(writeIntTelemetry("heartRate", (long) heartRate));
			} else {
				return result;
			}
		} else if (mode == 0) {
			result.add(writeFloatTelemetry("humidity", Utils.trim2Decimals(humidity)));
			result.add(writeFloatTelemetry("temperature", Utils.trim2Decimals(temperature)));
			result.add(writeFloatTelemetry("light", Utils.trim2Decimals(light)));
			result.add(writeIntTelemetry("uvIndex", (long) uvIndex));
		} else {
			throw new SeleneException("unsupported mode: " + mode);
		}
		result.add(writeIntTelemetry("mode", (long) mode));
		result.add(writeIntTelemetry("sequence", (long) sequence));
		return result;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getHeartRateState() {
		return heartRateState;
	}

	public void setHeartRateState(int heartRateState) {
		this.heartRateState = heartRateState;
	}

	public int getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getLight() {
		return light;
	}

	public void setLight(double light) {
		this.light = light;
	}

	public int getUvIndex() {
		return uvIndex;
	}

	public void setUvIndex(int uvIndex) {
		this.uvIndex = uvIndex;
	}
}

package com.arrow.selene.engine;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.arrow.acn.client.IotParameters;
import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.selene.Loggable;
import com.arrow.selene.TelemetryUtils;
import com.arrow.selene.data.Telemetry;

public abstract class DeviceDataAbstract extends Loggable implements DeviceData {

	private long timestamp;

	// used by ScriptEngine
	private IotParameters parsedIotParameters;
	private List<Telemetry> parsedTelemetries;
	private boolean parsedFully = false;

	public DeviceDataAbstract() {
		setTimestamp(Instant.now().toEpochMilli());
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public IotParameters getParsedIotParameters() {
		return parsedIotParameters;
	}

	public void setParsedIotParameters(IotParameters parsedIotParameters) {
		this.parsedIotParameters = parsedIotParameters;
	}

	public List<Telemetry> getParsedTelemetries() {
		return parsedTelemetries;
	}

	public void setParsedTelemetries(List<Telemetry> parsedTelemetries) {
		this.parsedTelemetries = parsedTelemetries;
	}

	public boolean isParsedFully() {
		return parsedFully;
	}

	public void setParsedFully(boolean parsedFully) {
		this.parsedFully = parsedFully;
	}

	protected Telemetry writeStringTelemetry(TelemetryItemType type, String name, String value) {
		return TelemetryUtils.withString(type, name, value, timestamp);
	}

	protected Telemetry writeIntTelemetry(String name, Long value) {
		return TelemetryUtils.withInt(name, value, timestamp);
	}

	protected Telemetry writeFloatTelemetry(String name, Double value) {
		return TelemetryUtils.withFloat(name, value, timestamp);
	}

	protected Telemetry writeBooleanTelemetry(String name, Boolean value) {
		return TelemetryUtils.withBoolean(name, value, timestamp);
	}

	protected Telemetry writeDateTelemetry(TelemetryItemType type, String name, LocalDate value) {
		return TelemetryUtils.withDate(type, name, value, timestamp);
	}

	protected Telemetry writeDateTimeTelemetry(TelemetryItemType type, String name, LocalDateTime value) {
		return TelemetryUtils.withDateTime(type, name, value, timestamp);
	}

	protected Telemetry writeBinaryTelemetry(String name, byte[] value) {
		return TelemetryUtils.withBinary(name, value, timestamp);
	}

	protected Telemetry writeRawTelemetry(String name, String value) {
		return TelemetryUtils.withRaw(name, value, timestamp);
	}
}

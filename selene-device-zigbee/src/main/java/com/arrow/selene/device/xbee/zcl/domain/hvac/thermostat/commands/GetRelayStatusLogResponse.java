package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetRelayStatusLogResponse extends ClusterSpecificCommand<GetRelayStatusLogResponse> {
	private int timeOfDay;
	private int relayStatus;
	private short temperature;
	private int humidityPercentage;
	private short setPoint;
	private int unreadEntries;

	public int getTimeOfDay() {
		return timeOfDay;
	}

	public GetRelayStatusLogResponse withTimeOfDay(int timeOfDay) {
		this.timeOfDay = timeOfDay;
		return this;
	}

	public int getRelayStatus() {
		return relayStatus;
	}

	public GetRelayStatusLogResponse withRelayStatus(int relayStatus) {
		this.relayStatus = relayStatus;
		return this;
	}

	public short getTemperature() {
		return temperature;
	}

	public GetRelayStatusLogResponse withTemperature(short temperature) {
		this.temperature = temperature;
		return this;
	}

	public int getHumidityPercentage() {
		return humidityPercentage;
	}

	public GetRelayStatusLogResponse withHumidityPercentage(int humidityPercentage) {
		this.humidityPercentage = humidityPercentage;
		return this;
	}

	public short getSetPoint() {
		return setPoint;
	}

	public GetRelayStatusLogResponse withSetPoint(short setPoint) {
		this.setPoint = setPoint;
		return this;
	}

	public int getUnreadEntries() {
		return unreadEntries;
	}

	public GetRelayStatusLogResponse withUnreadEntries(int unreadEntries) {
		this.unreadEntries = unreadEntries;
		return this;
	}

	@Override
	protected int getId() {
		return HvacThermostatClusterCommands.GET_RELAY_STATUS_LOG_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(11);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) timeOfDay);
		buffer.putShort((short) relayStatus);
		buffer.putShort(temperature);
		buffer.put((byte) humidityPercentage);
		buffer.putShort(setPoint);
		buffer.putShort((short) unreadEntries);
		return buffer.array();
	}

	@Override
	public GetRelayStatusLogResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 11, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		timeOfDay = Short.toUnsignedInt(buffer.getShort());
		relayStatus = Short.toUnsignedInt(buffer.getShort());
		temperature = buffer.getShort();
		humidityPercentage = Byte.toUnsignedInt(buffer.get());
		setPoint = buffer.getShort();
		unreadEntries = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}

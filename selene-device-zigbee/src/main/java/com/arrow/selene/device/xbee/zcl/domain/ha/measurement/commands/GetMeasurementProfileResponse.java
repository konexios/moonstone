package com.arrow.selene.device.xbee.zcl.domain.ha.measurement.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.data.AttributeRecord;

public class GetMeasurementProfileResponse extends ClusterSpecificCommand<GetMeasurementProfileResponse> {
	// private static final LocalDateTime UTC_TIME_START = LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0);
	private int startTime;
	private byte status;
	private byte profileIntervalPeriod;
	private int numberOfIntervalsDelivered;
	private int attributeId;
	private List<AttributeRecord> intervals;

	public int getStartTime() {
		return startTime;
	}

	public GetMeasurementProfileResponse withStartTime(int startTime) {
		this.startTime = startTime;
		return this;
	}

	public byte getStatus() {
		return status;
	}

	public GetMeasurementProfileResponse withStatus(byte status) {
		this.status = status;
		return this;
	}

	public byte getProfileIntervalPeriod() {
		return profileIntervalPeriod;
	}

	public GetMeasurementProfileResponse withProfileIntervalPeriod(byte profileIntervalPeriod) {
		this.profileIntervalPeriod = profileIntervalPeriod;
		return this;
	}

	public int getNumberOfIntervalsDelivered() {
		return numberOfIntervalsDelivered;
	}

	public GetMeasurementProfileResponse withNumberOfIntervalsDelivered(int numberOfIntervalsDelivered) {
		this.numberOfIntervalsDelivered = numberOfIntervalsDelivered;
		return this;
	}

	public int getAttributeId() {
		return attributeId;
	}

	public GetMeasurementProfileResponse withAttributeId(int attributeId) {
		this.attributeId = attributeId;
		return this;
	}

	public List<AttributeRecord> getIntervals() {
		return intervals;
	}

	public GetMeasurementProfileResponse withIntervals(List<AttributeRecord> intervals) {
		this.intervals = intervals;
		return this;
	}

	@Override
	protected int getId() {
		return ElectricalMeasurementClusterCommands.GET_MEASUREMENT_PROFILE_RESPONSE_COMMAND_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		int size = 0;
		for (AttributeRecord interval : intervals) {
			size += interval.calcSize();
		}
		ByteBuffer buffer = ByteBuffer.allocate(9 + size);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		//buffer.putInt((int) ChronoUnit.SECONDS.between(LocalDateTime.now(), UTC_TIME_START));
		buffer.putInt(startTime);
		buffer.put(status);
		buffer.put(profileIntervalPeriod);
		buffer.put((byte) numberOfIntervalsDelivered);
		buffer.putShort((short) attributeId);
		for (AttributeRecord attribute : intervals) {
			buffer.put(attribute.getRawValue());
		}
		return buffer.array();
	}

	@Override
	public GetMeasurementProfileResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 9, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		startTime = buffer.getInt();
		status = buffer.get();
		profileIntervalPeriod = buffer.get();
		numberOfIntervalsDelivered = Byte.toUnsignedInt(buffer.get());
		attributeId = Short.toUnsignedInt(buffer.getShort());
		byte[] data = new byte[buffer.remaining()];
		buffer.get(data);
		intervals = AttributeRecord.parse(data);
		return this;
	}
}

package com.arrow.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.metering.data.IntervalChanel;

public class GetProfile extends ClusterSpecificCommand<GetProfile> {
	private IntervalChanel intervalChanel;
	private long endTime;
	private int numberOfPeriods;

	public IntervalChanel getIntervalChanel() {
		return intervalChanel;
	}

	public GetProfile withIntervalChanel(IntervalChanel intervalChanel) {
		this.intervalChanel = intervalChanel;
		return this;
	}

	public long getEndTime() {
		return endTime;
	}

	public GetProfile withEndTime(long endTime) {
		this.endTime = endTime;
		return this;
	}

	public int getNumberOfPeriods() {
		return numberOfPeriods;
	}

	public GetProfile withNumberOfPeriods(int numberOfPeriods) {
		this.numberOfPeriods = numberOfPeriods;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.GET_PROFILE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(6);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) intervalChanel.ordinal());
		buffer.putInt((int) endTime);
		buffer.put((byte) numberOfPeriods);
		return buffer.array();
	}

	@Override
	public GetProfile fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 6, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		intervalChanel = IntervalChanel.values()[Byte.toUnsignedInt(buffer.get())];
		endTime = Integer.toUnsignedLong(buffer.getInt());
		numberOfPeriods = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}

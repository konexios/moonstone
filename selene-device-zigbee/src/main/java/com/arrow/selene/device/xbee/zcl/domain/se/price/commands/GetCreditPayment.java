package com.arrow.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetCreditPayment extends ClusterSpecificCommand<GetCreditPayment> {
	private long latestEndTime;
	private int numberOfRecords;

	public long getLatestEndTime() {
		return latestEndTime;
	}

	public GetCreditPayment withLatestEndTime(long latestEndTime) {
		this.latestEndTime = latestEndTime;
		return this;
	}

	public int getNumberOfRecords() {
		return numberOfRecords;
	}

	public GetCreditPayment withNumberOfRecords(int numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.GET_CREDIT_PAYMENT_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(5);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) latestEndTime);
		buffer.put((byte) numberOfRecords);
		return buffer.array();
	}

	@Override
	public GetCreditPayment fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 5, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		latestEndTime = Integer.toUnsignedLong(buffer.getInt());
		numberOfRecords = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}

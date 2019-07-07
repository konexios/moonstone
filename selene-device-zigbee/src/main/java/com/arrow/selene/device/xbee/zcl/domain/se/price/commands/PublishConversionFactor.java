package com.arrow.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class PublishConversionFactor extends ClusterSpecificCommand<PublishConversionFactor> {
	private long issuerEventId;
	private long startTime;
	private long conversionFactor;
	private byte conversionFactorTrailingDigit;

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public PublishConversionFactor withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getStartTime() {
		return startTime;
	}

	public PublishConversionFactor withStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public long getConversionFactor() {
		return conversionFactor;
	}

	public PublishConversionFactor withConversionFactor(long conversionFactor) {
		this.conversionFactor = conversionFactor;
		return this;
	}

	public byte getConversionFactorTrailingDigit() {
		return conversionFactorTrailingDigit;
	}

	public PublishConversionFactor withConversionFactorTrailingDigit(byte conversionFactorTrailingDigit) {
		this.conversionFactorTrailingDigit = conversionFactorTrailingDigit;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.PUBLISH_CONVERSION_FACTOR_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(13);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) startTime);
		buffer.putInt((int) conversionFactor);
		buffer.put((byte) (conversionFactorTrailingDigit << 4));
		return buffer.array();
	}

	@Override
	public PublishConversionFactor fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 13, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		startTime = Integer.toUnsignedLong(buffer.getInt());
		conversionFactor = Integer.toUnsignedLong(buffer.getInt());
		conversionFactorTrailingDigit = (byte) (buffer.get() >> 4);
		return this;
	}
}

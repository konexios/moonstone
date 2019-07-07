package com.arrow.selene.device.xbee.zcl.domain.general.powerprofile.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetOverallSchedulePriceResponse extends ClusterSpecificCommand<GetOverallSchedulePriceResponse> {
	private int currency;
	private long price;
	private int priceTrailingDigit;

	public int getCurrency() {
		return currency;
	}

	public GetOverallSchedulePriceResponse withCurrency(int currency) {
		this.currency = currency;
		return this;
	}

	public long getPrice() {
		return price;
	}

	public GetOverallSchedulePriceResponse withPrice(long price) {
		this.price = price;
		return this;
	}

	public int getPriceTrailingDigit() {
		return priceTrailingDigit;
	}

	public GetOverallSchedulePriceResponse withPriceTrailingDigit(int priceTrailingDigit) {
		this.priceTrailingDigit = priceTrailingDigit;
		return this;
	}

	@Override
	protected int getId() {
		return PowerProfileClusterCommands.GET_OVERALL_SCHEDULE_PRICE_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(7);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) currency);
		buffer.putInt((int) price);
		buffer.put((byte) priceTrailingDigit);
		return buffer.array();
	}

	@Override
	public GetOverallSchedulePriceResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 7, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		currency = Short.toUnsignedInt(buffer.getShort());
		price =Integer.toUnsignedLong(buffer.getInt());
		priceTrailingDigit = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}

package com.arrow.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.price.data.TariffType;

public class CancelTariff extends ClusterSpecificCommand<CancelTariff> {
	private long providerId;
	private long issuerEventId;
	private TariffType tariffType;

	public long getProviderId() {
		return providerId;
	}

	public CancelTariff withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public CancelTariff withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public TariffType getTariffType() {
		return tariffType;
	}

	public CancelTariff withTariffType(TariffType tariffType) {
		this.tariffType = tariffType;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.CANCEL_TARIFF_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(9);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.putInt((int) issuerEventId);
		buffer.put((byte) tariffType.ordinal());
		return buffer.array();
	}

	@Override
	public CancelTariff fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 9, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		providerId = Integer.toUnsignedLong(buffer.getInt());
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		tariffType = TariffType.values()[Byte.toUnsignedInt(buffer.get())];
		return this;
	}
}

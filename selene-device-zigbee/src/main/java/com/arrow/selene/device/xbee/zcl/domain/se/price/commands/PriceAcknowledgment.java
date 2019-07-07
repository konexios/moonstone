package com.arrow.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.price.data.PriceControl;

public class PriceAcknowledgment extends ClusterSpecificCommand<PriceAcknowledgment> {
	private long providerId;
	private long issuerEventId;
	private long priceAckTime;
	private Set<PriceControl> priceControls;

	public long getProviderId() {
		return providerId;
	}

	public PriceAcknowledgment withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public PriceAcknowledgment withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getPriceAckTime() {
		return priceAckTime;
	}

	public PriceAcknowledgment withPriceAckTime(long priceAckTime) {
		this.priceAckTime = priceAckTime;
		return this;
	}

	public Set<PriceControl> getPriceControls() {
		return priceControls;
	}

	public PriceAcknowledgment withPriceControls(Set<PriceControl> priceControls) {
		this.priceControls = priceControls;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.PRICE_ACKNOWLEDGEMENT_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(13);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) priceAckTime);
		buffer.put((byte) PriceControl.getValue(priceControls));
		return buffer.array();
	}

	@Override
	public PriceAcknowledgment fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 13, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		providerId = Integer.toUnsignedLong(buffer.getInt());
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		priceAckTime = Integer.toUnsignedLong(buffer.getInt());
		priceControls = PriceControl.getByValue(buffer.get());
		return this;
	}
}

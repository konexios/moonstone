package com.arrow.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.price.data.TierLabel;

public class PublishTierLabels extends ClusterSpecificCommand<PublishTierLabels> {
	private long providerId;
	private long issuerEventId;
	private long issuerTariffId;
	private int commandIndex;
	private int totalNumberOfCommands;
	private TierLabel[] tierLabels;

	public long getProviderId() {
		return providerId;
	}

	public PublishTierLabels withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public PublishTierLabels withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getIssuerTariffId() {
		return issuerTariffId;
	}

	public PublishTierLabels withIssuerTariffId(long issuerTariffId) {
		this.issuerTariffId = issuerTariffId;
		return this;
	}

	public int getCommandIndex() {
		return commandIndex;
	}

	public PublishTierLabels withCommandIndex(int commandIndex) {
		this.commandIndex = commandIndex;
		return this;
	}

	public int getTotalNumberOfCommands() {
		return totalNumberOfCommands;
	}

	public PublishTierLabels withTotalNumberOfCommands(int totalNumberOfCommands) {
		this.totalNumberOfCommands = totalNumberOfCommands;
		return this;
	}

	public TierLabel[] getTierLabels() {
		return tierLabels;
	}

	public PublishTierLabels withTierLabels(TierLabel[] tierLabels) {
		this.tierLabels = tierLabels;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.PUBLISH_TIER_LABELS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		int size = 0;
		for (TierLabel tierLabel : tierLabels) {
			size += 2 + tierLabel.getTierLabel().length();
		}
		ByteBuffer buffer = ByteBuffer.allocate(15 + size);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) issuerTariffId);
		buffer.put((byte) commandIndex);
		buffer.put((byte) totalNumberOfCommands);
		buffer.put((byte) tierLabels.length);
		for (TierLabel tierLabel : tierLabels) {
			buffer.put((byte) tierLabel.getTierId());
			buffer.put((byte) tierLabel.getTierLabel().length());
			buffer.put(tierLabel.getTierLabel().getBytes(StandardCharsets.UTF_8));
		}
		return buffer.array();
	}

	@Override
	public PublishTierLabels fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 15, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		providerId = Integer.toUnsignedLong(buffer.getInt());
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		issuerTariffId = Integer.toUnsignedLong(buffer.getInt());
		commandIndex = Byte.toUnsignedInt(buffer.get());
		totalNumberOfCommands = Byte.toUnsignedInt(buffer.get());
		tierLabels = new TierLabel[Byte.toUnsignedInt(buffer.get())];
		int i = 0;
		while (buffer.hasRemaining()) {
			int tierId = Byte.toUnsignedInt(buffer.get());
			byte[] array = new byte[Byte.toUnsignedInt(buffer.get())];
			buffer.get(array);
			tierLabels[i] = new TierLabel().withTierId(tierId).withTierLabel(new String(array, StandardCharsets.UTF_8));
			i++;
		}
		return this;
	}
}

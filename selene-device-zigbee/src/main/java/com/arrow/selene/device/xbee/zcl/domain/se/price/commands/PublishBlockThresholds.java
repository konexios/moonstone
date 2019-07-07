package com.arrow.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.price.data.BlockThresholdControl;
import com.digi.xbee.api.utils.ByteUtils;

public class PublishBlockThresholds extends ClusterSpecificCommand<PublishBlockThresholds> {
	private long providerId;
	private long issuerEventId;
	private long startTime;
	private long issuerTariffId;
	private int commandIndex;
	private int totalNumberOfCommands;
	private Set<BlockThresholdControl> blockThresholdControls;
	private long[][] blockThresholds;

	public long getProviderId() {
		return providerId;
	}

	public PublishBlockThresholds withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public PublishBlockThresholds withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getStartTime() {
		return startTime;
	}

	public PublishBlockThresholds withStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public long getIssuerTariffId() {
		return issuerTariffId;
	}

	public PublishBlockThresholds withIssuerTariffId(long issuerTariffId) {
		this.issuerTariffId = issuerTariffId;
		return this;
	}

	public int getCommandIndex() {
		return commandIndex;
	}

	public PublishBlockThresholds withCommandIndex(int commandIndex) {
		this.commandIndex = commandIndex;
		return this;
	}

	public int getTotalNumberOfCommands() {
		return totalNumberOfCommands;
	}

	public PublishBlockThresholds withTotalNumberOfCommands(int totalNumberOfCommands) {
		this.totalNumberOfCommands = totalNumberOfCommands;
		return this;
	}

	public Set<BlockThresholdControl> getBlockThresholdControls() {
		return blockThresholdControls;
	}

	public PublishBlockThresholds withBlockThresholdControls(Set<BlockThresholdControl> blockThresholdControls) {
		this.blockThresholdControls = blockThresholdControls;
		return this;
	}

	public long[][] getBlockThresholds() {
		return blockThresholds;
	}

	public PublishBlockThresholds withBlockThresholds(long[][] blockThresholds) {
		this.blockThresholds = blockThresholds;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.PUBLISH_BLOCK_THRESHOLDS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		int size = 0;
		for (long[] blockThreshold : blockThresholds) {
			size += 1 + blockThreshold.length * 6;
		}
		ByteBuffer buffer = ByteBuffer.allocate(19 + size);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) startTime);
		buffer.putInt((int) issuerTariffId);
		buffer.put((byte) commandIndex);
		buffer.put((byte) totalNumberOfCommands);
		buffer.put((byte) BlockThresholdControl.getValue(blockThresholdControls));
		for (long[] blockThreshold : blockThresholds) {
			buffer.put((byte) blockThreshold.length);
			for (long item : blockThreshold) {
				buffer.put(ByteUtils.swapByteArray(ByteUtils.longToByteArray(item)), 0, 6);
			}
		}
		return buffer.array();
	}

	@Override
	public PublishBlockThresholds fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 19, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		providerId = Integer.toUnsignedLong(buffer.getInt());
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		startTime = Integer.toUnsignedLong(buffer.getInt());
		issuerTariffId = Integer.toUnsignedLong(buffer.getInt());
		commandIndex = Byte.toUnsignedInt(buffer.get());
		totalNumberOfCommands = Byte.toUnsignedInt(buffer.get());
		blockThresholdControls = BlockThresholdControl.getByValue(buffer.get());
		List<long[]> values = new ArrayList<>();
		while(buffer.hasRemaining()) {
			int length = Byte.toUnsignedInt(buffer.get());
			long[] array = new long[length];
			for (int i = 0; i < array.length; i++) {
				byte[] value = new byte[6];
				buffer.get(value);
				array[i] = ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(value));
			}
		}
		blockThresholds = values.toArray(new long[][]{});
		return this;
	}
}

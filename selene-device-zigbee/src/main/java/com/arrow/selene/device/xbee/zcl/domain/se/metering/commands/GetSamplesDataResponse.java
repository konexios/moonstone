package com.arrow.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.metering.data.SampleType;
import com.digi.xbee.api.utils.ByteUtils;

public class GetSamplesDataResponse extends ClusterSpecificCommand<GetSamplesDataResponse> {
	private int sampleId;
	private long sampleTimestamp;
	private SampleType sampleType;
	private int sampleRequestInterval;
	private int[] samples;

	public int getSampleId() {
		return sampleId;
	}

	public GetSamplesDataResponse withSampleId(int sampleId) {
		this.sampleId = sampleId;
		return this;
	}

	public long getSampleTimestamp() {
		return sampleTimestamp;
	}

	public GetSamplesDataResponse withSampleTimestamp(long sampleTimestamp) {
		this.sampleTimestamp = sampleTimestamp;
		return this;
	}

	public SampleType getSampleType() {
		return sampleType;
	}

	public GetSamplesDataResponse withSampleType(SampleType sampleType) {
		this.sampleType = sampleType;
		return this;
	}

	public int getSampleRequestInterval() {
		return sampleRequestInterval;
	}

	public GetSamplesDataResponse withSampleRequestInterval(int sampleRequestInterval) {
		this.sampleRequestInterval = sampleRequestInterval;
		return this;
	}

	public int[] getSamples() {
		return samples;
	}

	public GetSamplesDataResponse withSamples(int[] samples) {
		this.samples = samples;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.GET_SAMPLED_DATA_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(11 + samples.length * 3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) sampleId);
		buffer.putInt((int) sampleTimestamp);
		buffer.put((byte) sampleType.ordinal());
		buffer.putShort((short) sampleRequestInterval);
		buffer.putShort((short) samples.length);
		for (int item : samples) {
			buffer.put(ByteUtils.swapByteArray(ByteUtils.intToByteArray(item)), 0, 3);
		}
		return buffer.array();
	}

	@Override
	public GetSamplesDataResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 11, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		sampleId = Short.toUnsignedInt(buffer.getShort());
		sampleTimestamp = Integer.toUnsignedLong(buffer.getInt());
		sampleType = SampleType.values()[Byte.toUnsignedInt(buffer.get())];
		sampleRequestInterval = Short.toUnsignedInt(buffer.getShort());
		samples = new int[Short.toUnsignedInt(buffer.getShort())];
		for (int i = 0; i < samples.length; i++) {
			byte[] bytes = new byte[3];
			buffer.get(bytes);
			samples[i] = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(bytes));
		}
		return this;
	}
}

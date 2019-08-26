package moonstone.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.metering.data.SampleType;

public class StartSampling extends ClusterSpecificCommand<StartSampling> {
	private long issuerEventId;
	private long startSamplingTime;
	private SampleType sampleType;
	private int sampleRequestInterval;
	private int maxNumberOfSamples;

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public StartSampling withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getStartSamplingTime() {
		return startSamplingTime;
	}

	public StartSampling withStartSamplingTime(long startSamplingTime) {
		this.startSamplingTime = startSamplingTime;
		return this;
	}

	public SampleType getSampleType() {
		return sampleType;
	}

	public StartSampling withSampleType(SampleType sampleType) {
		this.sampleType = sampleType;
		return this;
	}

	public int getSampleRequestInterval() {
		return sampleRequestInterval;
	}

	public StartSampling withSampleRequestInterval(int sampleRequestInterval) {
		this.sampleRequestInterval = sampleRequestInterval;
		return this;
	}

	public int getMaxNumberOfSamples() {
		return maxNumberOfSamples;
	}

	public StartSampling withMaxNumberOfSamples(int maxNumberOfSamples) {
		this.maxNumberOfSamples = maxNumberOfSamples;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.START_SAMPLING_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(13);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) startSamplingTime);
		buffer.put((byte) sampleType.ordinal());
		buffer.putShort((short) sampleRequestInterval);
		buffer.putShort((short) maxNumberOfSamples);
		return buffer.array();
	}

	@Override
	public StartSampling fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 13, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		startSamplingTime = Integer.toUnsignedLong(buffer.getInt());
		sampleType = SampleType.values()[Byte.toUnsignedInt(buffer.get())];
		sampleRequestInterval = Short.toUnsignedInt(buffer.getShort());
		maxNumberOfSamples = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}

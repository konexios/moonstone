package moonstone.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.metering.data.SampleType;

public class GetSampledData extends ClusterSpecificCommand<GetSampledData> {
	private int sampleId;
	private long earliestSampleTime;
	private SampleType sampleType;
	private int numberOfSamples;

	public int getSampleId() {
		return sampleId;
	}

	public GetSampledData withSampleId(int sampleId) {
		this.sampleId = sampleId;
		return this;
	}

	public long getEarliestSampleTime() {
		return earliestSampleTime;
	}

	public GetSampledData withEarliestSampleTime(long earliestSampleTime) {
		this.earliestSampleTime = earliestSampleTime;
		return this;
	}

	public SampleType getSampleType() {
		return sampleType;
	}

	public GetSampledData withSampleType(SampleType sampleType) {
		this.sampleType = sampleType;
		return this;
	}

	public int getNumberOfSamples() {
		return numberOfSamples;
	}

	public GetSampledData withNumberOfSamples(int numberOfSamples) {
		this.numberOfSamples = numberOfSamples;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.GET_SAMPLED_DATA_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(9);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) sampleId);
		buffer.putInt((int) earliestSampleTime);
		buffer.put((byte) sampleType.ordinal());
		buffer.putShort((short) numberOfSamples);
		return buffer.array();
	}

	@Override
	public GetSampledData fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 9, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		sampleId = Short.toUnsignedInt(buffer.getShort());
		earliestSampleTime = Integer.toUnsignedLong(buffer.getInt());
		sampleType = SampleType.values()[Byte.toUnsignedInt(buffer.get())];
		numberOfSamples = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}

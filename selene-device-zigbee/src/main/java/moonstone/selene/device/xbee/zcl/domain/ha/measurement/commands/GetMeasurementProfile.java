package moonstone.selene.device.xbee.zcl.domain.ha.measurement.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetMeasurementProfile extends ClusterSpecificCommand<GetMeasurementProfile> {
	private int attributeId;
	private int startTime;
	private int numberOfIntervalsDelivered;

	public int getAttributeId() {
		return attributeId;
	}

	public GetMeasurementProfile withAttributeId(int attributeId) {
		this.attributeId = attributeId;
		return this;
	}

	public int getStartTime() {
		return startTime;
	}

	public GetMeasurementProfile withStartTime(int startTime) {
		this.startTime = startTime;
		return this;
	}

	public int getNumberOfIntervalsDelivered() {
		return numberOfIntervalsDelivered;
	}

	public GetMeasurementProfile withNumberOfIntervalsDelivered(int numberOfIntervalsDelivered) {
		this.numberOfIntervalsDelivered = numberOfIntervalsDelivered;
		return this;
	}

	@Override
	protected int getId() {
		return ElectricalMeasurementClusterCommands.GET_MEASUREMENT_PROFILE_COMMAND_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(7);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) attributeId);
		buffer.putInt(startTime);
		buffer.put((byte) numberOfIntervalsDelivered);
		return buffer.array();
	}

	@Override
	public GetMeasurementProfile fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 7, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		attributeId = Short.toUnsignedInt(buffer.getShort());
		startTime = buffer.getInt();
		numberOfIntervalsDelivered = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}

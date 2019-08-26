package moonstone.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.CalorificValueUnit;

public class PublishCalorificValue extends ClusterSpecificCommand<PublishCalorificValue> {
	private long issuerEventId;
	private long startTime;
	private long calorificValue;
	private CalorificValueUnit calorificValueUnit;
	private byte calorificValueTrailingDigit;

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public PublishCalorificValue withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getStartTime() {
		return startTime;
	}

	public PublishCalorificValue withStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public long getCalorificValue() {
		return calorificValue;
	}

	public PublishCalorificValue withCalorificValue(long calorificValue) {
		this.calorificValue = calorificValue;
		return this;
	}

	public CalorificValueUnit getCalorificValueUnit() {
		return calorificValueUnit;
	}

	public PublishCalorificValue withCalorificValueUnit(CalorificValueUnit calorificValueUnit) {
		this.calorificValueUnit = calorificValueUnit;
		return this;
	}

	public byte getCalorificValueTrailingDigit() {
		return calorificValueTrailingDigit;
	}

	public PublishCalorificValue withCalorificValueTrailingDigit(byte calorificValueTrailingDigit) {
		this.calorificValueTrailingDigit = calorificValueTrailingDigit;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.PUBLISH_CALORIFIC_VALUE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) startTime);
		buffer.putInt((int) calorificValue);
		buffer.put((byte) calorificValueUnit.ordinal());
		buffer.put((byte) (calorificValueTrailingDigit << 4));
		return buffer.array();
	}

	@Override
	public PublishCalorificValue fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 14, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		byte[] label = new byte[buffer.get()];
		buffer.get(label);
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		startTime = Integer.toUnsignedLong(buffer.getInt());
		calorificValue = Integer.toUnsignedLong(buffer.getInt());
		calorificValueUnit = CalorificValueUnit.values()[Byte.toUnsignedInt(buffer.get())];
		calorificValueTrailingDigit = (byte) (buffer.get() >> 4);
		return this;
	}
}

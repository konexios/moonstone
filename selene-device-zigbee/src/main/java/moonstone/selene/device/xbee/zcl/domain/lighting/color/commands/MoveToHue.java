package moonstone.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.lighting.color.data.Direction;

public class MoveToHue extends ClusterSpecificCommand<MoveToHue> {
	private int hue;
	private Direction direction;
	private int transactionTime;

	public int getHue() {
		return hue;
	}

	public MoveToHue withHue(int hue) {
		this.hue = hue;
		return this;
	}

	public Direction getDirection() {
		return direction;
	}

	public MoveToHue withDirection(Direction direction) {
		this.direction = direction;
		return this;
	}

	public int getTransactionTime() {
		return transactionTime;
	}

	public MoveToHue withTransactionTime(int transactionTime) {
		this.transactionTime = transactionTime;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.MOVE_TO_HUE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) hue);
		buffer.put((byte) direction.ordinal());
		buffer.putShort((short) transactionTime);
		return buffer.array();
	}

	@Override
	public MoveToHue fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 4, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		hue = Byte.toUnsignedInt(buffer.get());
		direction = Direction.values()[Byte.toUnsignedInt(buffer.get())];
		transactionTime = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}

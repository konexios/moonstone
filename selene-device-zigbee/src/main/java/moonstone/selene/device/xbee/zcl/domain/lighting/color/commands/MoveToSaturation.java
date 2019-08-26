package moonstone.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class MoveToSaturation extends ClusterSpecificCommand<MoveToSaturation> {
	private int saturation;
	private int transactionTime;

	public int getSaturation() {
		return saturation;
	}

	public MoveToSaturation withSaturation(int saturation) {
		this.saturation = saturation;
		return this;
	}

	public int getTransactionTime() {
		return transactionTime;
	}

	public MoveToSaturation withTransactionTime(int transactionTime) {
		this.transactionTime = transactionTime;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.MOVE_TO_SATURATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) saturation);
		buffer.putShort((short) transactionTime);
		return buffer.array();
	}

	@Override
	public MoveToSaturation fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 3, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		saturation = Byte.toUnsignedInt(buffer.get());
		transactionTime = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}

package moonstone.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class MoveToHueAndSaturation extends ClusterSpecificCommand<MoveToHueAndSaturation> {
	private int hue;
	private int saturation;
	private int transactionTime;

	public int getHue() {
		return hue;
	}

	public MoveToHueAndSaturation withHue(int hue) {
		this.hue = hue;
		return this;
	}

	public int getSaturation() {
		return saturation;
	}

	public MoveToHueAndSaturation withSaturation(int saturation) {
		this.saturation = saturation;
		return this;
	}

	public int getTransactionTime() {
		return transactionTime;
	}

	public MoveToHueAndSaturation withTransactionTime(int transactionTime) {
		this.transactionTime = transactionTime;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.MOVE_TO_HUE_AND_SATURATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) hue);
		buffer.put((byte) saturation);
		buffer.putShort((short) transactionTime);
		return buffer.array();
	}

	@Override
	public MoveToHueAndSaturation fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 4, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		hue = Byte.toUnsignedInt(buffer.get());
		saturation = Byte.toUnsignedInt(buffer.get());
		transactionTime = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}

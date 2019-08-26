package moonstone.selene.device.xbee.zcl.domain.general.identify.commands;

import org.apache.commons.lang3.Validate;

import com.digi.xbee.api.utils.ByteUtils;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class Identify extends ClusterSpecificCommand<Identify> {
	private int identifyTime;

	public int getIdentifyTime() {
		return identifyTime;
	}

	public Identify withIdentifyTime(int identifyTime) {
		this.identifyTime = identifyTime;
		return this;
	}

	@Override
	protected int getId() {
		return IdentifyClusterCommands.IDENTIFY_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return ByteUtils.swapByteArray(ByteUtils.shortToByteArray((short) identifyTime));
	}

	@Override
	public Identify fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		identifyTime = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(payload));
		return this;
	}
}

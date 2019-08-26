package moonstone.selene.device.xbee.zcl.domain.closures.door.commands;

import org.apache.commons.lang3.Validate;

import com.digi.xbee.api.utils.ByteUtils;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class ClearRfidCode extends ClusterSpecificCommand<ClearRfidCode> {
	private int userId;

	public int getUserId() {
		return userId;
	}

	public ClearRfidCode withUserId(int userId) {
		this.userId = userId;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.CLEAR_RFID_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return ByteUtils.swapByteArray(ByteUtils.shortToByteArray((short) userId));
	}

	@Override
	protected ClearRfidCode fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		userId = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(payload));
		return this;
	}
}

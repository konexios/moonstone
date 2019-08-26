package moonstone.selene.device.xbee.zcl.domain.closures.window.commands;

import com.digi.xbee.api.utils.ByteUtils;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GoToTiltValue extends ClusterSpecificCommand<GoToTiltValue> {
	private int tiltValue;

	public int getTiltValue() {
		return tiltValue;
	}

	public GoToTiltValue withTiltValue(int tiltValue) {
		this.tiltValue = tiltValue;
		return this;
	}

	@Override
	protected int getId() {
		return WindowCoveringClusterCommands.WINDOW_COVERING_GO_TO_TILT_VALUE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return ByteUtils.swapByteArray(ByteUtils.shortToByteArray((short) tiltValue));
	}

	@Override
	public GoToTiltValue fromPayload(byte[] payload) {
		tiltValue = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(payload));
		return this;
	}
}

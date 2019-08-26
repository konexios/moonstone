package moonstone.selene.device.xbee.zcl.domain.security.wd.commands;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class Squawk extends ClusterSpecificCommand<Squawk> {
	private boolean squawkMode;
	private boolean strobe;
	private byte squawkLevel;

	public boolean isSquawkMode() {
		return squawkMode;
	}

	public Squawk withSquawkMode(boolean squawkMode) {
		this.squawkMode = squawkMode;
		return this;
	}

	public boolean isStrobe() {
		return strobe;
	}

	public Squawk withStrobe(boolean strobe) {
		this.strobe = strobe;
		return this;
	}

	public byte getSquawkLevel() {
		return squawkLevel;
	}

	public Squawk withSquawkLevel(byte squawkLevel) {
		this.squawkLevel = squawkLevel;
		return this;
	}

	@Override
	protected int getId() {
		return SecurityWdClusterCommands.SQUAWK_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		int value = squawkLevel;
		if (squawkMode) {
			value |= 0b00010000;
		}
		if (strobe) {
			value |= 0b00001000;
		}
		return new byte[]{(byte) value};
	}

	@Override
	public Squawk fromPayload(byte[] payload) {
		byte value = payload[0];
		squawkMode = (value & 0b00010000) != 0;
		strobe = (value & 0b00001000) != 0;
		squawkLevel = (byte) (value & 0b00000011);
		return this;
	}
}

package moonstone.selene.device.harting.rfid.command;

import moonstone.selene.engine.Utils;

public class GetSoftwareVersion extends AbstractExtendedCommand {
	private static final long serialVersionUID = -8546865242975720673L;
	private static final int LENGTH = 0;
	private static final int CONTROL = 0x65;

	@Override
	protected byte[] buildPayload() {
		return Utils.EMPTY_BYTE_ARRAY;
	}

	@Override
	protected int getControl() {
		return CONTROL;
	}

	@Override
	protected int getLength() {
		return LENGTH;
	}
}

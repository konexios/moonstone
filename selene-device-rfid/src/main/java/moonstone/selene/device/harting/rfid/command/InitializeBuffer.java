package moonstone.selene.device.harting.rfid.command;

import moonstone.selene.engine.Utils;

public class InitializeBuffer extends AbstractExtendedCommand {
	private static final long serialVersionUID = -9014082879677521741L;
	private static final int LENGTH = 0;
	private static final int CONTROL = 0x33;

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

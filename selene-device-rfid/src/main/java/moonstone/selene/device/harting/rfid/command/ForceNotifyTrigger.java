package moonstone.selene.device.harting.rfid.command;

public class ForceNotifyTrigger extends AbstractExtendedCommand {
	private static final long serialVersionUID = -4094380627898497989L;

	private static final int LENGTH = 1;
	private static final int CONTROL = 0x34;
	private static final byte[] PAYLOAD = {0};

	@Override
	protected byte[] buildPayload() {
		return PAYLOAD;
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

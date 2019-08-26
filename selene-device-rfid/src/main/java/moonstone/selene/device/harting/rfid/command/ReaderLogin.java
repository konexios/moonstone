package moonstone.selene.device.harting.rfid.command;

public class ReaderLogin extends AbstractExtendedCommand {
	private static final long serialVersionUID = 8902036055157795715L;
	private static final int LENGTH = 4;
	private static final int CONTROL = 0xa0;
	private byte[] readerId;

	public byte[] getReaderId() {
		return readerId;
	}

	public void setReaderId(byte[] readerId) {
		this.readerId = readerId;
	}

	@Override
	protected byte[] buildPayload() {
		return readerId;
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

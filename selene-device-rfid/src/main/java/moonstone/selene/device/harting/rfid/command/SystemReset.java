package moonstone.selene.device.harting.rfid.command;

public class SystemReset extends AbstractExtendedCommand {
	private static final long serialVersionUID = 168051784884096426L;
	private static final int LENGTH = 1;
	private static final int CONTROL = 0x64;
	private Mode mode;

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	@Override
	protected byte[] buildPayload() {
		return new byte[]{(byte) Mode.buid(mode)};
	}

	@Override
	protected int getControl() {
		return CONTROL;
	}

	@Override
	protected int getLength() {
		return LENGTH;
	}

	enum Mode {
		RF_CONTROLLER,
		ACC;

		public static int buid(Mode mode) {
			return mode.ordinal();
		}
	}
}

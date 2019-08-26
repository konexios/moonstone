package moonstone.selene.device.harting.rfid.command;

public class RfOutputOnOff extends AbstractExtendedCommand {
	private static final long serialVersionUID = -318074019543229327L;
	private static final int LENGTH = 1;
	private static final int CONTROL = 0x6a;
	private RfOutput rfOutput;

	public RfOutput getRfOutput() {
		return rfOutput;
	}

	public void setRfOutput(RfOutput rfOutput) {
		this.rfOutput = rfOutput;
	}

	public RfOutputOnOff withRfOutput(RfOutput rfOutput) {
		this.rfOutput = rfOutput;
		return this;
	}

	@Override
	protected byte[] buildPayload() {
		return new byte[]{(byte) RfOutput.build(rfOutput)};
	}

	@Override
	protected int getControl() {
		return CONTROL;
	}

	@Override
	protected int getLength() {
		return LENGTH;
	}

	public static class RfOutput {
		private HostMode hostMode;
		private AntennaOutput antennaOutput;

		public HostMode getHostMode() {
			return hostMode;
		}

		public AntennaOutput getAntennaOutput() {
			return antennaOutput;
		}

		public void setHostMode(HostMode hostMode) {
			this.hostMode = hostMode;
		}

		public void setAntennaOutput(AntennaOutput antennaOutput) {
			this.antennaOutput = antennaOutput;
		}

		public RfOutput withHostMode(HostMode hostMode) {
			this.hostMode = hostMode;
			return this;
		}

		public RfOutput withAntennaOutput(AntennaOutput antennaOutput) {
			this.antennaOutput = antennaOutput;
			return this;
		}

		public static int build(RfOutput value) {
			int result = 0;
			result |= HostMode.build(value.hostMode) << 7;
			result |= AntennaOutput.build(value.antennaOutput);
			return result;
		}
	}

	public enum HostMode {
		AUTO_READ,
		HOST_MODE;

		public static int build(HostMode value) {
			return value.ordinal();
		}
	}

	public enum AntennaOutput {
		RF_OFF,
		RF_POWER_1,
		RF_POWER_2,
		RF_POWER_3,
		RF_POWER_4;

		public static int build(AntennaOutput value) {
			return value.ordinal();
		}
	}
}

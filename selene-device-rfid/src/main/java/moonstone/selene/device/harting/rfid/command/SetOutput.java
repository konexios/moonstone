package moonstone.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;
import java.util.List;

public class SetOutput extends AbstractExtendedCommand {
	private static final long serialVersionUID = -2400485740828418767L;
	private static final int CONTROL = 0x72;

	private int mode = 1;
	private List<OutputRecord> outputRecords;

	@Override
	protected byte[] buildPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(getLength());
		buffer.put(0, (byte) mode);
		buffer.put(1, (byte) outputRecords.size());
		for (OutputRecord record : outputRecords) {
			buffer.put(OutputRecord.build(record));
		}
		return buffer.array();
	}

	@Override
	protected int getControl() {
		return CONTROL;
	}

	@Override
	protected int getLength() {
		return 2 + 4 * outputRecords.size();
	}

	public static class OutputRecord {
		private OutputNumber outputNumber;
		private OutputState outputState;
		private int outputTime;

		public OutputNumber getOutputNumber() {
			return outputNumber;
		}

		public void setOutputNumber(OutputNumber outputNumber) {
			this.outputNumber = outputNumber;
		}

		public OutputState getOutputState() {
			return outputState;
		}

		public void setOutputState(OutputState outputState) {
			this.outputState = outputState;
		}

		public int getOutputTime() {
			return outputTime;
		}

		public void setOutputTime(int outputTime) {
			this.outputTime = outputTime;
		}

		public OutputRecord withOutputNumber(OutputNumber outputNumber) {
			this.outputNumber = outputNumber;
			return this;
		}

		public OutputRecord withOutputState(OutputState outputState) {
			this.outputState = outputState;
			return this;
		}

		public OutputRecord withOutputTime(int outputTime) {
			this.outputTime = outputTime;
			return this;
		}

		public static byte[] build(OutputRecord value) {
			ByteBuffer buffer = ByteBuffer.allocate(4);
			buffer.put((byte) OutputNumber.build(value.outputNumber));
			buffer.put((byte) OutputState.build(value.outputState));
			buffer.putShort((short) value.outputTime);
			return buffer.array();
		}
	}

	public static class OutputNumber {
		private OutputType outputType;
		private int outNumber;

		public OutputType getOutputType() {
			return outputType;
		}

		public int getOutNumber() {
			return outNumber;
		}

		public void setOutputType(OutputType outputType) {
			this.outputType = outputType;
		}

		public void setOutNumber(int outNumber) {
			this.outNumber = outNumber;
		}

		public OutputNumber withOutputType(OutputType outputType) {
			this.outputType = outputType;
			return this;
		}

		public OutputNumber withOutNumber(int outNumber) {
			this.outNumber = outNumber;
			return this;
		}

		public static int build(OutputNumber value) {
			int result = 0;
			result |= OutputType.build(value.outputType) << 5;
			result |= value.outNumber;
			return result;
		}
	}

	public enum OutputType {
		DIGITAL_OUTPUT,
		LED,
		BUZZER,
		RELAYS;

		public static int build(OutputType value) {
			return value.ordinal();
		}
	}

	public static class OutputState {
		private OutputFrequency outputFrequency;
		private OutputMode outputMode;

		public OutputFrequency getOutputFrequency() {
			return outputFrequency;
		}

		public OutputMode getOutputMode() {
			return outputMode;
		}

		public void setOutputFrequency(OutputFrequency outputFrequency) {
			this.outputFrequency = outputFrequency;
		}

		public void setOutputMode(OutputMode outputMode) {
			this.outputMode = outputMode;
		}

		public OutputState withOutputFrequency(OutputFrequency outputFrequency) {
			this.outputFrequency = outputFrequency;
			return this;
		}

		public OutputState withOutputMode(OutputMode outputMode) {
			this.outputMode = outputMode;
			return this;
		}

		public static int build(OutputState value) {
			int result = 0;
			result |= OutputFrequency.build(value.outputFrequency) << 2;
			result |= OutputMode.build(value.outputMode);
			return result;
		}
	}

	public enum OutputMode {
		UNCHANGED,
		ON,
		OFF,
		FLASH;

		public static int build(OutputMode value) {
			return value.ordinal();
		}
	}

	public enum OutputFrequency {
		FREQUENCY_8HZ,
		FREQUENCY_4HZ,
		FREQUENCY_2HZ,
		FREQUENCY_1HZ;

		public static int build(OutputFrequency value) {
			return value.ordinal();
		}
	}
}

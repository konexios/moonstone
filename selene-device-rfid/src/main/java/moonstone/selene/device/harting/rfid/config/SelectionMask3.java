package moonstone.selene.device.harting.rfid.config;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SelectionMask3 implements ConfigParameter<SelectionMask3> {
	private static final long serialVersionUID = 6688492345327011433L;

	private static final int ID = 26;

	private int maskLength;
	private Mode mode;
	private int startPointer;
	private byte[] mask;

	private static final List<String> ALL = new ArrayList<>();

	public SelectionMask3(int maskLength, Mode mode, int startPointer, byte[] mask) {
		this.maskLength = maskLength;
		this.mode = mode;
		this.startPointer = startPointer;
		this.mask = mask;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.put(0, (byte) maskLength);
		buffer.put(1, (byte) Mode.build(mode));
		buffer.putShort(2, (short) startPointer);
		buffer.put(mask);
		return buffer.array();
	}

	@Override
	public SelectionMask3 parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		int maskLength = Byte.toUnsignedInt(buffer.get(0));
		Mode mode = Mode.extract(buffer.get(1));
		int startPointer = Short.toUnsignedInt(buffer.getShort(2));
		byte[] mask = new byte[11];
		buffer.get(mask);
		return new SelectionMask3(maskLength, mode, startPointer, mask);
	}

	@Override
	public int getId() {
		return ID;
	}

	static class Mode {
		private boolean st;
		private Logic logic;
		private boolean negation;
		private Bank bank;

		public Mode(boolean st, Logic logic, boolean negation, Bank bank) {
			this.st = st;
			this.logic = logic;
			this.negation = negation;
			this.bank = bank;
		}

		public static int build(Mode value) {
			int result = 0;
			result |= value.st ? 0b10000000 : 0;
			result |= Logic.build(value.logic) << 3;
			result |= value.negation ? 0b00000100 : 0;
			result |= Bank.build(value.bank);
			return result;
		}

		public static Mode extract(int value) {
			boolean st = (value & 0b10000000) == 0b10000000;
			Logic logic = Logic.extract(value >> 3 & 0x01);
			boolean negation = (value & 0b00000100) == 0b00000100;
			Bank bank = Bank.extract(value & 0x03);
			return new Mode(st, logic, negation, bank);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Mode mode = (Mode) o;
			return st == mode.st && negation == mode.negation && logic == mode.logic && bank == mode.bank;
		}

		@Override
		public int hashCode() {

			return Objects.hash(st, logic, negation, bank);
		}
	}

	enum Logic {
		OR_OPERATION,
		AND_OPERATION;

		public static int build(Logic value) {
			return value.ordinal();
		}

		public static Logic extract(int value) {
			for (Logic item : values()) {
				if (item.ordinal() == value) {
					return item;
				}
			}
			return null;
		}
	}

	enum Bank {
		RESERVED,
		EPC,
		TID,
		USER;

		public static int build(Bank value) {
			return value.ordinal();
		}

		public static Bank extract(int value) {
			for (Bank item : values()) {
				if (item.ordinal() == value) {
					return item;
				}
			}
			return null;
		}
	}

	@Override
	public boolean updateState(String name, String value) {
		return false;
	}

	@Override
	public Map<String, String> getStates() {
		return Collections.emptyMap();
	}

	@Override
	public List<String> getParams() {
		return ALL;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SelectionMask3 that = (SelectionMask3) o;
		return maskLength == that.maskLength && startPointer == that.startPointer && Objects.equals(mode, that.mode) &&
				Arrays.equals(mask, that.mask);
	}

	@Override
	public int hashCode() {

		int result = Objects.hash(maskLength, mode, startPointer);
		result = 31 * result + Arrays.hashCode(mask);
		return result;
	}
}

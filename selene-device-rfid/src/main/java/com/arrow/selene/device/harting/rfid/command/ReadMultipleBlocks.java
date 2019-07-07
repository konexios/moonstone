package com.arrow.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;

public class ReadMultipleBlocks extends HostCommand {
	private static final long serialVersionUID = 7133462436102133788L;
	private static final int ID = 0x23;
	private static final int LENGTH = 4;

	private Mode mode;
	private byte[] uid;
	private Bank bank;
	private byte[] accessPassword;
	int dbAddress;
	int dbNumber;

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public byte[] getUid() {
		return uid;
	}

	public void setUid(byte[] uid) {
		this.uid = uid;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public byte[] getAccessPassword() {
		return accessPassword;
	}

	public void setAccessPassword(byte[] accessPassword) {
		this.accessPassword = accessPassword;
	}

	public int getDbAddress() {
		return dbAddress;
	}

	public void setDbAddress(int dbAddress) {
		this.dbAddress = dbAddress;
	}

	public int getDbNumber() {
		return dbNumber;
	}

	public void setDbNumber(int dbNumber) {
		this.dbNumber = dbNumber;
	}

	public ReadMultipleBlocks withMode(Mode mode) {
		this.mode = mode;
		return this;
	}

	public ReadMultipleBlocks withUid(byte[] uid) {
		this.uid = uid;
		return this;
	}

	public ReadMultipleBlocks withBank(Bank bank) {
		this.bank = bank;
		return this;
	}

	public ReadMultipleBlocks withAccessPassword(byte[] accessPassword) {
		this.accessPassword = accessPassword;
		return this;
	}

	public ReadMultipleBlocks withDbAddress(int dbAddress) {
		this.dbAddress = dbAddress;
		return this;
	}

	public ReadMultipleBlocks withDbNumber(int dbNumber) {
		this.dbNumber = dbNumber;
		return this;
	}

	protected byte[] buildPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(getLength());
		buffer.put(0, (byte) ID);
		buffer.put(1, (byte) Mode.build(mode));
		int shift = 2;
		if (mode.uid) {
			buffer.put(shift, (byte) uid.length);
			shift++;
			buffer.position(shift);
			buffer.put(uid);
			shift += uid.length;
		}
		buffer.put(shift, (byte) Bank.build(bank));
		shift++;
		if (bank.accessPassword) {
			buffer.put(shift, (byte) accessPassword.length);
			shift++;
			buffer.put(accessPassword);
			shift += accessPassword.length;
		}
		if (mode.extendedAddress) {
			buffer.putShort(shift, (short) dbAddress);
			shift += 2;
		} else {
			buffer.put(shift, (byte) dbAddress);
			shift++;
		}
		buffer.put(shift, (byte) dbNumber);
		return buffer.array();
	}

	@Override
	protected int getLength() {
		return LENGTH + (mode.uid ? 1 + uid.length : 0) + (bank.accessPassword ? 1 + accessPassword.length : 0) +
				(mode.extendedAddress ? 2 : 1);
	}

	public static class Mode {
		private boolean readCompleteBank;
		private boolean extendedAddress;
		private boolean uid;
		private boolean addressed;

		public boolean isReadCompleteBank() {
			return readCompleteBank;
		}

		public void setReadCompleteBank(boolean readCompleteBank) {
			this.readCompleteBank = readCompleteBank;
		}

		public boolean isExtendedAddress() {
			return extendedAddress;
		}

		public void setExtendedAddress(boolean extendedAddress) {
			this.extendedAddress = extendedAddress;
		}

		public boolean isUid() {
			return uid;
		}

		public void setUid(boolean uid) {
			this.uid = uid;
		}

		public boolean isAddressed() {
			return addressed;
		}

		public void setAddressed(boolean addressed) {
			this.addressed = addressed;
		}

		public Mode withReadCompleteBank(boolean readCompleteBank) {
			this.readCompleteBank = readCompleteBank;
			return this;
		}

		public Mode withExtendedAddress(boolean extendedAddress) {
			this.extendedAddress = extendedAddress;
			return this;
		}

		public Mode withUid(boolean uid) {
			this.uid = uid;
			return this;
		}

		public Mode withAddressed(boolean addressed) {
			this.addressed = addressed;
			return this;
		}

		public static int build(Mode value) {
			int result = 0;
			result |= value.addressed ? 0x01 : 0x00;
			result |= value.uid ? 0x10 : 0x00;
			result |= value.extendedAddress ? 0x20 : 0x00;
			result |= value.readCompleteBank ? 0x40 : 0x00;
			return result;
		}
	}

	public static class Bank {
		private BankNumber bankNumber;
		private boolean accessPassword;

		public BankNumber getBankNumber() {
			return bankNumber;
		}

		public void setBankNumber(BankNumber bankNumber) {
			this.bankNumber = bankNumber;
		}

		public boolean isAccessPassword() {
			return accessPassword;
		}

		public void setAccessPassword(boolean accessPassword) {
			this.accessPassword = accessPassword;
		}

		public Bank withBankNumber(BankNumber bankNumber) {
			this.bankNumber = bankNumber;
			return this;
		}

		public Bank withAccessPassword(boolean accessPassword) {
			this.accessPassword = accessPassword;
			return this;
		}

		public static int build(Bank value) {
			int result = 0;
			result |= value.bankNumber.ordinal();
			result |= value.accessPassword ? 0x80 : 0x00;
			return result;
		}
	}

	public enum BankNumber {
		RESERVED,
		EPC,
		TID,
		USER_MEMORY
	}
}

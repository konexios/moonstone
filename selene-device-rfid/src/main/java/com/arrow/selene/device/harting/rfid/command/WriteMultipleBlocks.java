package com.arrow.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;

public class WriteMultipleBlocks extends HostCommand {
	private static final long serialVersionUID = -6220943892787577159L;

	private static final int ID = 0x24;
	private static final int LENGTH = 5;

	private Mode mode;
	private byte[] uid;
	private Bank bank;
	private byte[] accessPassword;
	private int dbAddress;
	private int dbNumber;
	private int dbSize;
	private byte[] data;

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

	public int getDbSize() {
		return dbSize;
	}

	public void setDbSize(int dbSize) {
		this.dbNumber = dbSize;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public WriteMultipleBlocks withMode(Mode mode) {
		this.mode = mode;
		return this;
	}

	public WriteMultipleBlocks withUid(byte[] uid) {
		this.uid = uid;
		return this;
	}

	public WriteMultipleBlocks withBank(Bank bank) {
		this.bank = bank;
		return this;
	}

	public WriteMultipleBlocks withAccessPassword(byte[] accessPassword) {
		this.accessPassword = accessPassword;
		return this;
	}

	public WriteMultipleBlocks withDbAddress(int dbAddress) {
		this.dbAddress = dbAddress;
		return this;
	}

	public WriteMultipleBlocks withDbNumber(int dbNumber) {
		this.dbNumber = dbNumber;
		return this;
	}

	public WriteMultipleBlocks withDbSize(int dbSize) {
		this.dbSize = dbSize;
		return this;
	}

	public WriteMultipleBlocks withData(byte[] data) {
		this.data = data;
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
		shift++;
		buffer.put(shift, (byte) dbSize);
		shift++;
		buffer.position(shift);
		buffer.put(data);
		return buffer.array();
	}

	@Override
	protected int getLength() {
		return LENGTH + (mode.uid ? 1 + uid.length : 0) + (bank.accessPassword ? 1 + accessPassword.length : 0) +
				(mode.extendedAddress ? 2 : 1);
	}

	public static class Mode {
		private boolean extendedAddress;
		private boolean uid;
		private boolean addressed;

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

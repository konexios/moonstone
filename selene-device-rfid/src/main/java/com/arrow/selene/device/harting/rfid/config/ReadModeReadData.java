package com.arrow.selene.device.harting.rfid.config;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.SerializationUtils;

public class ReadModeReadData implements ConfigParameter<ReadModeReadData> {
	private static final long serialVersionUID = 3369215816262971697L;

	private static final int ID = 11;

	private static final String OPERATING_MODE_DATA_SELECTOR_UID = "OperatingMode_DataSelector_UID";
	private static final String OPERATING_MODE_DATA_SELECTOR_DATA = "OperatingMode_DataSelector_Data";
	private static final String OPERATING_MODE_DATA_SELECTOR_ANTENNA_NO = "OperatingMode_DataSelector_AntennaNo";
	private static final String OPERATING_MODE_DATA_SELECTOR_TIME = "OperatingMode_DataSelector_Time";
	private static final String OPERATING_MODE_DATA_SELECTOR_DATE = "OperatingMode_DataSelector_Date";
	private static final String OPERATING_MODE_DATA_SELECTOR_INPUT_EVENTS = "OperatingMode_DataSelector_InputEvents";
	private static final String OPERATING_MODE_DATA_SELECTOR_RSSI = "OperatingMode_DataSelector_RSSI";
	private static final String OPERATING_MODE_DATA_SELECTOR_MODE_ENABLE_ANTENNA_POOL =
			"OperatingMode_DataSelector_Mode_Enable_AntennaPool";
	private static final String OPERATING_MODE_DATA_SELECTOR_MODE_READ_COMPLETE_BANK =
			"OperatingMode_DataSelector_Mode_ReadCompleteBank";
	private static final String OPERATING_MODE_DATA_SELECTOR_MODE_ACTION_ON_EPC =
			"OperatingMode_DataSelector_Mode_ActionOnEPC";
	private static final String OPERATING_MODE_DATA_SOURCE_BANK_NO = "OperatingMode_DataSource_BankNo";
	private static final String OPERATING_MODE_DATA_SOURCE_FIRST_DATA_BLOCK =
			"OperatingMode_DataSource_FirstDataBlock";
	private static final String OPERATING_MODE_DATA_SOURCE_NO_OF_DATA_BLOCKS =
			"OperatingMode_DataSource_NoOfDataBlocks";
	private static final String OPERATING_MODE_DATA_SOURCE_BYTE_ORDER_OF_DATA =
			"OperatingMode_DataSource_ByteOrderOfData";
	private static final String OPERATING_MODE_DATA_SOURCE_NUMBER_OF_BYTES = "OperatingMode_DataSource_NumberOfBytes";
	private static final String OPERATING_MODE_DATA_FORMAT_BUS_ADDRESS_PREFIX =
			"OperatingMode_DataFormat_BusAddressPrefix";
	private static final String OPERATING_MODE_DATA_SOURCE_FIRST_BYTE = "OperatingMode_DataSource_FirstByte";

	private TrData1 trData1;
	private TrData2 trData2;
	private TrData3 trData3;
	private Bank bank;
	private int dbAddress;
	private int dbNumber;
	private int dStart;
	private int dLength;

	private static final List<String> ALL = new ArrayList<>();

	static {
		ALL.add(OPERATING_MODE_DATA_SELECTOR_UID);
		ALL.add(OPERATING_MODE_DATA_SELECTOR_DATA);
		ALL.add(OPERATING_MODE_DATA_SELECTOR_ANTENNA_NO);
		ALL.add(OPERATING_MODE_DATA_SELECTOR_TIME);
		ALL.add(OPERATING_MODE_DATA_SELECTOR_DATE);
		ALL.add(OPERATING_MODE_DATA_SELECTOR_INPUT_EVENTS);
		ALL.add(OPERATING_MODE_DATA_SELECTOR_RSSI);
		ALL.add(OPERATING_MODE_DATA_SELECTOR_MODE_ENABLE_ANTENNA_POOL);
		ALL.add(OPERATING_MODE_DATA_SELECTOR_MODE_READ_COMPLETE_BANK);
		ALL.add(OPERATING_MODE_DATA_SELECTOR_MODE_ACTION_ON_EPC);
		ALL.add(OPERATING_MODE_DATA_SOURCE_BANK_NO);
		ALL.add(OPERATING_MODE_DATA_SOURCE_FIRST_DATA_BLOCK);
		ALL.add(OPERATING_MODE_DATA_SOURCE_NO_OF_DATA_BLOCKS);
		ALL.add(OPERATING_MODE_DATA_SOURCE_BYTE_ORDER_OF_DATA);
		ALL.add(OPERATING_MODE_DATA_SOURCE_NUMBER_OF_BYTES);
		ALL.add(OPERATING_MODE_DATA_FORMAT_BUS_ADDRESS_PREFIX);
		ALL.add(OPERATING_MODE_DATA_SOURCE_FIRST_BYTE);
	}

	public TrData1 getTrData1() {
		return trData1;
	}

	public ReadModeReadData withTrData1(TrData1 trData1) {
		this.trData1 = trData1;
		return this;
	}

	public TrData2 getTrData2() {
		return trData2;
	}

	public ReadModeReadData withTrData2(TrData2 trData2) {
		this.trData2 = trData2;
		return this;
	}

	public TrData3 getTrData3() {
		return trData3;
	}

	public ReadModeReadData withTrData3(TrData3 trData3) {
		this.trData3 = trData3;
		return this;
	}

	public Bank getBank() {
		return bank;
	}

	public ReadModeReadData withBank(Bank bank) {
		this.bank = bank;
		return this;
	}

	public int getDbAddress() {
		return dbAddress;
	}

	public ReadModeReadData withDbAddress(int dbAddress) {
		this.dbAddress = dbAddress;
		return this;
	}

	public int getDbNumber() {
		return dbNumber;
	}

	public ReadModeReadData withDbNumber(int dbNumber) {
		this.dbNumber = dbNumber;
		return this;
	}

	public int getdStart() {
		return dStart;
	}

	public ReadModeReadData withdStart(int dStart) {
		this.dStart = dStart;
		return this;
	}

	public int getdLength() {
		return dLength;
	}

	public ReadModeReadData withdLength(int dLength) {
		this.dLength = dLength;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.put(0, (byte) TrData1.build(trData1));
		buffer.put(1, (byte) TrData2.build(trData2));
		buffer.put(2, (byte) TrData3.build(trData3));
		buffer.put(3, (byte) Bank.build(bank));
		buffer.putShort(4, (short) dbAddress);
		buffer.putShort(8, (short) dbNumber);
		buffer.put(11, (byte) dStart);
		buffer.putShort(12, (short) dLength);
		return buffer.array();
	}

	@Override
	public ReadModeReadData parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		TrData1 trData1 = TrData1.extract(buffer.get(0));
		TrData2 trData2 = TrData2.extract(buffer.get(1));
		TrData3 trData3 = TrData3.extract(buffer.get(2));
		Bank bank = Bank.extract(buffer.get(3));
		int dbAddress = Short.toUnsignedInt(buffer.getShort(4));
		int dbNumber = Short.toUnsignedInt(buffer.getShort(8));
		int dStart = Byte.toUnsignedInt(buffer.get(11));
		int dLength = Short.toUnsignedInt(buffer.getShort(12));
		return new ReadModeReadData().withTrData1(trData1).withTrData2(trData2).withTrData3(trData3).withBank(bank)
				.withDbAddress(dbAddress).withDbNumber(dbNumber).withdStart(dStart).withdLength(dLength);
	}

	@Override
	public int getId() {
		return ID;
	}

	static class TrData1 {
		private boolean extension;
		private boolean date;
		private boolean timer;
		private boolean ant;
		private boolean byteOrder;
		private boolean db;
		private boolean snr;

		public TrData1(boolean extension, boolean date, boolean timer, boolean ant, boolean byteOrder, boolean db,
		               boolean snr) {
			this.extension = extension;
			this.date = date;
			this.timer = timer;
			this.ant = ant;
			this.byteOrder = byteOrder;
			this.db = db;
			this.snr = snr;
		}

		public static int build(TrData1 value) {
			int result = 0;
			result |= value.extension ? 0b10000000 : 0;
			result |= value.date ? 0b01000000 : 0;
			result |= value.timer ? 0b00100000 : 0;
			result |= value.ant ? 0b00010000 : 0;
			result |= value.byteOrder ? 0b00001000 : 0;
			result |= value.db ? 0b00000010 : 0;
			result |= value.snr ? 0b00000001 : 0;
			return result;
		}

		public static TrData1 extract(int value) {
			boolean extension = (value & 0b10000000) == 0b10000000;
			boolean date = (value & 0b01000000) == 0b01000000;
			boolean timer = (value & 0b00100000) == 0b00100000;
			boolean ant = (value & 0b00010000) == 0b00010000;
			boolean byteOrder = (value & 0b00001000) == 0b00001000;
			boolean db = (value & 0b00000010) == 0b00000010;
			boolean snr = (value & 0b00000001) == 0b00000001;
			return new TrData1(extension, date, timer, ant, byteOrder, db, snr);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			TrData1 trData1 = (TrData1) o;
			return extension == trData1.extension && date == trData1.date && timer == trData1.timer &&
					ant == trData1.ant && byteOrder == trData1.byteOrder && db == trData1.db && snr == trData1.snr;
		}

		@Override
		public int hashCode() {

			return Objects.hash(extension, date, timer, ant, byteOrder, db, snr);
		}
	}

	static class TrData2 {
		private boolean antExt;
		private boolean in;

		public TrData2(boolean antExt, boolean in) {
			this.antExt = antExt;
			this.in = in;
		}

		public static int build(TrData2 value) {
			int result = 0;
			result |= value.antExt ? 0b00010000 : 0;
			result |= value.in ? 0b00000001 : 0;
			return result;
		}

		public static TrData2 extract(int value) {
			boolean antExt = (value & 0b00010000) == 0b00010000;
			boolean in = (value & 0b00000001) == 0b00000001;
			return new TrData2(antExt, in);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			TrData2 trData2 = (TrData2) o;
			return antExt == trData2.antExt && in == trData2.in;
		}

		@Override
		public int hashCode() {

			return Objects.hash(antExt, in);
		}
	}

	static class TrData3 {
		private boolean actionOnEpc;
		private boolean readCompleteBank;
		private boolean antStore;
		private boolean comPrefix;

		public TrData3(boolean actionOnEpc, boolean readCompleteBank, boolean antStore, boolean comPrefix) {
			this.actionOnEpc = actionOnEpc;
			this.readCompleteBank = readCompleteBank;
			this.antStore = antStore;
			this.comPrefix = comPrefix;
		}

		public static int build(TrData3 value) {
			int result = 0;
			result |= value.actionOnEpc ? 0b01000000 : 0;
			result |= value.readCompleteBank ? 0b00001000 : 0;
			result |= value.antStore ? 0b000000010 : 0;
			result |= value.comPrefix ? 0b00000001 : 0;
			return result;
		}

		public static TrData3 extract(int value) {
			boolean actionOnEpc = (value & 0b01000000) != 0;
			boolean readCompleteBank = (value & 0b00001000) != 0;
			boolean antStore = (value & 0b00000010) != 0;
			boolean comPrefix = (value & 0b00000001) != 0;
			return new TrData3(actionOnEpc, readCompleteBank, antStore, comPrefix);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			TrData3 trData3 = (TrData3) o;
			return actionOnEpc == trData3.actionOnEpc && readCompleteBank == trData3.readCompleteBank &&
					antStore == trData3.antStore && comPrefix == trData3.comPrefix;
		}

		@Override
		public int hashCode() {

			return Objects.hash(actionOnEpc, readCompleteBank, antStore, comPrefix);
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
		ReadModeReadData stored = SerializationUtils.clone(this);
		switch (name) {
			case OPERATING_MODE_DATA_SELECTOR_UID: {
				trData1.snr = Boolean.valueOf(value);
				break;
			}
			case OPERATING_MODE_DATA_SELECTOR_DATA: {
				trData1.db = Boolean.valueOf(value);
				break;
			}
			case OPERATING_MODE_DATA_SOURCE_BYTE_ORDER_OF_DATA: {
				if (Objects.equals(value.toUpperCase(Locale.getDefault()), "MSB")) {
					trData1.byteOrder = false;
				} else if (Objects.equals(value.toUpperCase(Locale.getDefault()), "LSB")) {
					trData1.byteOrder = true;
				}
				break;
			}
			case OPERATING_MODE_DATA_SELECTOR_ANTENNA_NO: {
				trData1.ant = Boolean.parseBoolean(value);
				break;
			}
			case OPERATING_MODE_DATA_SELECTOR_TIME: {
				trData1.timer = Boolean.parseBoolean(value);
				break;
			}
			case OPERATING_MODE_DATA_SELECTOR_DATE: {
				trData1.date = Boolean.parseBoolean(value);
				break;
			}
			case OPERATING_MODE_DATA_SELECTOR_INPUT_EVENTS: {
				trData2.in = Boolean.parseBoolean(value);
				trData1.extension = trData2.in || trData2.antExt;
				break;
			}
			case OPERATING_MODE_DATA_SELECTOR_RSSI: {
				trData2.antExt = Boolean.parseBoolean(value);
				trData1.extension = trData2.in || trData2.antExt;
				break;
			}
			case OPERATING_MODE_DATA_SELECTOR_MODE_ACTION_ON_EPC: {
				trData3.actionOnEpc = Boolean.parseBoolean(value);
				break;
			}
			case OPERATING_MODE_DATA_SELECTOR_MODE_READ_COMPLETE_BANK: {
				trData3.readCompleteBank = Boolean.parseBoolean(value);
				break;
			}
			case OPERATING_MODE_DATA_SELECTOR_MODE_ENABLE_ANTENNA_POOL: {
				trData3.antStore = Boolean.parseBoolean(value);
				break;
			}
			case OPERATING_MODE_DATA_FORMAT_BUS_ADDRESS_PREFIX: {
				trData3.comPrefix = Boolean.parseBoolean(value);
				break;
			}
			case OPERATING_MODE_DATA_SOURCE_BANK_NO: {
				bank = Bank.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
			case OPERATING_MODE_DATA_SOURCE_FIRST_DATA_BLOCK: {
				dbAddress = Integer.parseInt(value);
				break;
			}
			case OPERATING_MODE_DATA_SOURCE_NO_OF_DATA_BLOCKS: {
				dbNumber = Integer.parseInt(value);
				break;
			}
			case OPERATING_MODE_DATA_SOURCE_FIRST_BYTE: {
				dStart = Integer.getInteger(value);
				break;
			}
			case OPERATING_MODE_DATA_SOURCE_NUMBER_OF_BYTES: {
				dLength = Integer.getInteger(value);
				break;
			}
		}
		return !equals(stored);
	}

	@Override
	public Map<String, String> getStates() {
		Map<String, String> result = new HashMap<>();
		result.put(OPERATING_MODE_DATA_SELECTOR_UID, Boolean.toString(trData1.snr));
		result.put(OPERATING_MODE_DATA_SELECTOR_DATA, Boolean.toString(trData1.db));
		result.put(OPERATING_MODE_DATA_SELECTOR_ANTENNA_NO, Boolean.toString(trData1.ant));
		result.put(OPERATING_MODE_DATA_SELECTOR_TIME, Boolean.toString(trData1.timer));
		result.put(OPERATING_MODE_DATA_SELECTOR_DATE, Boolean.toString(trData1.date));
		result.put(OPERATING_MODE_DATA_SELECTOR_INPUT_EVENTS, Boolean.toString(trData2.in));
		result.put(OPERATING_MODE_DATA_SELECTOR_RSSI, Boolean.toString(trData2.antExt));
		result.put(OPERATING_MODE_DATA_SELECTOR_MODE_ENABLE_ANTENNA_POOL, Boolean.toString(trData3.antStore));
		result.put(OPERATING_MODE_DATA_SELECTOR_MODE_READ_COMPLETE_BANK, Boolean.toString(trData3.readCompleteBank));
		result.put(OPERATING_MODE_DATA_SELECTOR_MODE_ACTION_ON_EPC, Boolean.toString(trData3.actionOnEpc));
		result.put(OPERATING_MODE_DATA_SOURCE_BANK_NO, bank.name());
		result.put(OPERATING_MODE_DATA_SOURCE_FIRST_DATA_BLOCK, Integer.toString(dbAddress));
		result.put(OPERATING_MODE_DATA_SOURCE_NO_OF_DATA_BLOCKS, Integer.toString(dbNumber));
		result.put(OPERATING_MODE_DATA_SOURCE_BYTE_ORDER_OF_DATA, trData1.byteOrder ? "LSB" : "MSB");
		result.put(OPERATING_MODE_DATA_SOURCE_NUMBER_OF_BYTES, Integer.toString(dLength));
		result.put(OPERATING_MODE_DATA_FORMAT_BUS_ADDRESS_PREFIX, Boolean.toString(trData3.comPrefix));
		result.put(OPERATING_MODE_DATA_SOURCE_FIRST_BYTE, Integer.toString(dStart));
		return result;
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
		ReadModeReadData that = (ReadModeReadData) o;
		return dbAddress == that.dbAddress && dbNumber == that.dbNumber && dStart == that.dStart &&
				dLength == that.dLength && Objects.equals(trData1, that.trData1) && Objects.equals(trData2,
				that.trData2) && Objects.equals(trData3, that.trData3) && bank == that.bank;
	}

	@Override
	public int hashCode() {
		return Objects.hash(trData1, trData2, trData3, bank, dbAddress, dbNumber, dStart, dLength);
	}
}

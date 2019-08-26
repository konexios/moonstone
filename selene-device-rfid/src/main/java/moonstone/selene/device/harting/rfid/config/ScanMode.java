package moonstone.selene.device.harting.rfid.config;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.SerializationUtils;

public class ScanMode implements ConfigParameter<ScanMode> {
	private static final long serialVersionUID = -7386721373418142933L;

	private static final int ID = 13;

	private static final String OPERATING_MODE_SCAN_MODE_DATA_FORMAT_FORMAT =
			"OperatingMode_ScanMode_DataFormat_Format";
	private DbFormat dbFormat;
	private static final String OPERATING_MODE_SCAN_MODE_DATA_FORMAT_SEPARATION_CHAR =
			"OperatingMode_ScanMode_DataFormat_SeparationChar";
	private Separator separator;
	private static final String OPERATING_MODE_SCAN_MODE_DATA_FORMAT_USER_SEPARATION_CHAR =
			"OperatingMode_ScanMode_DataFormat_UserSeparationChar";
	private char userSeparator;
	private static final String OPERATING_MODE_SCAN_MODE_DATA_FORMAT_END_CHAR =
			"OperatingMode_ScanMode_DataFormat_EndChar";
	private Separator end;
	private static final String OPERATING_MODE_SCAN_MODE_DATA_FORMAT_USER_END_CHAR1 =
			"OperatingMode_ScanMode_DataFormat_UserEndChar1";
	private char userEnd1;
	private static final String OPERATING_MODE_SCAN_MODE_DATA_FORMAT_USER_END_CHAR2 =
			"OperatingMode_ScanMode_DataFormat_UserEndChar2";
	private char userEnd2;
	private static final String OPERATING_MODE_SCAN_MODE_DATA_FORMAT_USER_END_CHAR3 =
			"OperatingMode_ScanMode_DataFormat_UserEndChar3";
	private char userEnd3;
	private static final String OPERATING_MODE_SCAN_MODE_DATA_FORMAT_USER_HEADER_CHAR1 =
			"OperatingMode_ScanMode_DataFormat_UserHeaderChar1";
	private char userHeader1;
	private static final String OPERATING_MODE_SCAN_MODE_DATA_FORMAT_USER_HEADER_CHAR2 =
			"OperatingMode_ScanMode_DataFormat_UserHeaderChar2";
	private char userHeader2;
	private static final String OPERATING_MODE_SCAN_MODE_DATA_FORMAT_USER_HEADER_CHAR3 =
			"OperatingMode_ScanMode_DataFormat_UserHeaderChar3";
	private char userHeader3;
	private static final String OPERATING_MODE_SCAN_MODE_DATA_FORMAT_USER_HEADER_CHAR4 =
			"OperatingMode_ScanMode_DataFormat_UserHeaderChar4";
	private char userHeader4;
	private static final String OPERATING_MODE_SCAN_MODE_DATA_FORMAT_NO_OF_USER_HEADER_CHARS =
			"OperatingMode_ScanMode_DataFormat_NoOfUserHeaderChars";
	private static final String OPERATING_MODE_SCAN_MODE_DATA_FORMAT_NO_OF_USER_END_CHARS =
			"OperatingMode_ScanMode_DataFormat_NoOfUserEndChars";
	private LengthUser lengthUser;

	private static final List<String> ALL = new ArrayList<>();

	static {
		ALL.add(OPERATING_MODE_SCAN_MODE_DATA_FORMAT_FORMAT);
		ALL.add(OPERATING_MODE_SCAN_MODE_DATA_FORMAT_SEPARATION_CHAR);
		ALL.add(OPERATING_MODE_SCAN_MODE_DATA_FORMAT_USER_SEPARATION_CHAR);
		ALL.add(OPERATING_MODE_SCAN_MODE_DATA_FORMAT_END_CHAR);
		ALL.add(OPERATING_MODE_SCAN_MODE_DATA_FORMAT_USER_END_CHAR1);
		ALL.add(OPERATING_MODE_SCAN_MODE_DATA_FORMAT_USER_END_CHAR2);
		ALL.add(OPERATING_MODE_SCAN_MODE_DATA_FORMAT_USER_END_CHAR3);
		ALL.add(OPERATING_MODE_SCAN_MODE_DATA_FORMAT_USER_HEADER_CHAR1);
		ALL.add(OPERATING_MODE_SCAN_MODE_DATA_FORMAT_USER_HEADER_CHAR2);
		ALL.add(OPERATING_MODE_SCAN_MODE_DATA_FORMAT_USER_HEADER_CHAR3);
		ALL.add(OPERATING_MODE_SCAN_MODE_DATA_FORMAT_USER_HEADER_CHAR4);
		ALL.add(OPERATING_MODE_SCAN_MODE_DATA_FORMAT_NO_OF_USER_HEADER_CHARS);
		ALL.add(OPERATING_MODE_SCAN_MODE_DATA_FORMAT_NO_OF_USER_END_CHARS);
	}

	public DbFormat getDbFormat() {
		return dbFormat;
	}

	public ScanMode withDbFormat(DbFormat dbFormat) {
		this.dbFormat = dbFormat;
		return this;
	}

	public Separator getSeparator() {
		return separator;
	}

	public ScanMode withSeparator(Separator separator) {
		this.separator = separator;
		return this;
	}

	public char getUserSeparator() {
		return userSeparator;
	}

	public ScanMode withUserSeparator(char userSeparator) {
		this.userSeparator = userSeparator;
		return this;
	}

	public Separator getEnd() {
		return end;
	}

	public ScanMode withEnd(Separator end) {
		this.end = end;
		return this;
	}

	public char getUserEnd1() {
		return userEnd1;
	}

	public ScanMode withUserEnd1(char userEnd1) {
		this.userEnd1 = userEnd1;
		return this;
	}

	public char getUserEnd2() {
		return userEnd2;
	}

	public ScanMode withUserEnd2(char userEnd2) {
		this.userEnd2 = userEnd2;
		return this;
	}

	public char getUserEnd3() {
		return userEnd3;
	}

	public ScanMode withUserEnd3(char userEnd3) {
		this.userEnd3 = userEnd3;
		return this;
	}

	public char getUserHeader1() {
		return userHeader1;
	}

	public ScanMode withUserHeader1(char userHeader1) {
		this.userHeader1 = userHeader1;
		return this;
	}

	public char getUserHeader2() {
		return userHeader2;
	}

	public ScanMode withUserHeader2(char userHeader2) {
		this.userHeader2 = userHeader2;
		return this;
	}

	public char getUserHeader3() {
		return userHeader3;
	}

	public ScanMode withUserHeader3(char userHeader3) {
		this.userHeader3 = userHeader3;
		return this;
	}

	public char getUserHeader4() {
		return userHeader4;
	}

	public ScanMode withUserHeader4(char userHeader4) {
		this.userHeader4 = userHeader4;
		return this;
	}

	public LengthUser getLengthUser() {
		return lengthUser;
	}

	public ScanMode withLengthUser(LengthUser lengthUser) {
		this.lengthUser = lengthUser;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.put(0, (byte) DbFormat.build(dbFormat));
		buffer.put(1, (byte) Separator.build(separator));
		buffer.put(1, (byte) userSeparator);
		buffer.put(3, (byte) Separator.build(end));
		buffer.put(4, (byte) userEnd1);
		buffer.put(5, (byte) userEnd2);
		buffer.put(6, (byte) userEnd3);
		buffer.put(8, (byte) userHeader1);
		buffer.put(9, (byte) userHeader2);
		buffer.put(10, (byte) userHeader3);
		buffer.put(11, (byte) userHeader4);
		buffer.put(13, (byte) LengthUser.build(lengthUser));
		return buffer.array();
	}

	@Override
	public ScanMode parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		DbFormat dbFormat = DbFormat.extract(buffer.get(0));
		Separator separator = Separator.extract(buffer.get(1));
		char userSeparator = (char) buffer.get(0);
		Separator end = Separator.extract(buffer.get(3));
		char userEnd1 = (char) buffer.get(4);
		char userEnd2 = (char) buffer.get(5);
		char userEnd3 = (char) buffer.get(6);
		char userHeader1 = (char) buffer.get(8);
		char userHeader2 = (char) buffer.get(9);
		char userHeader3 = (char) buffer.get(10);
		char userHeader4 = (char) buffer.get(11);
		LengthUser lengthUser = LengthUser.extract(buffer.get(13));
		return new ScanMode().withDbFormat(dbFormat).withSeparator(separator).withUserSeparator(userSeparator).withEnd(
				end).withUserEnd1(userEnd1).withUserEnd2(userEnd2).withUserEnd3(userEnd3).withUserHeader1(userHeader1)
				.withUserHeader2(userHeader2).withUserHeader3(userHeader3).withUserHeader4(userHeader4).withLengthUser(
						lengthUser);
	}

	@Override
	public int getId() {
		return ID;
	}

	enum DbFormat {
		UNFORMATTED(0b0000),
		ASCII(0b0010);

		private final int value;

		DbFormat(int value) {
			this.value = value;
		}

		public static int build(DbFormat value) {
			return value.value;
		}

		public static DbFormat extract(int value) {
			for (DbFormat item : values()) {
				if (item.value == value) {
					return item;
				}
			}
			return null;
		}
	}

	enum Separator {
		CR_LF,
		LF,
		CR,
		TAB,
		SEMICOLON,
		COMMA,
		SPACE,
		USER;

		public static int build(Separator value) {
			return 1 << value.ordinal();
		}

		public static Separator extract(int value) {
			return values()[(int) (Math.log10(value) / Math.log10(2))];
		}
	}

	static class LengthUser {
		private int headerLength;
		private int endLength;

		public LengthUser(int headerLength, int endLength) {
			this.headerLength = headerLength;
			this.endLength = endLength;
		}

		public static int build(LengthUser value) {
			return (value.headerLength << 4) + value.endLength;
		}

		public static LengthUser extract(int value) {
			int headerLength = value >> 4 & 0x0f;
			int endLength = value & 0x0f;
			return new LengthUser(headerLength, endLength);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			LengthUser that = (LengthUser) o;
			return headerLength == that.headerLength && endLength == that.endLength;
		}

		@Override
		public int hashCode() {
			return Objects.hash(headerLength, endLength);
		}
	}

	@Override
	public boolean updateState(String name, String value) {
		ScanMode stored = SerializationUtils.clone(this);
		switch (name) {
		}
		return !equals(stored);
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
		ScanMode scanMode = (ScanMode) o;
		return userSeparator == scanMode.userSeparator && userEnd1 == scanMode.userEnd1 &&
				userEnd2 == scanMode.userEnd2 && userEnd3 == scanMode.userEnd3 && userHeader1 == scanMode
				.userHeader1 &&
				userHeader2 == scanMode.userHeader2 && userHeader3 == scanMode.userHeader3 &&
				userHeader4 == scanMode.userHeader4 && dbFormat == scanMode.dbFormat &&
				separator == scanMode.separator && end == scanMode.end && Objects.equals(lengthUser,
				scanMode.lengthUser);
	}

	@Override
	public int hashCode() {

		return Objects.hash(dbFormat, separator, userSeparator, end, userEnd1, userEnd2, userEnd3, userHeader1,
				userHeader2, userHeader3, userHeader4, lengthUser);
	}
}

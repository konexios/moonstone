package moonstone.selene.device.harting.rfid.config;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.SerializationUtils;

public class ReadModeFilter implements ConfigParameter<ReadModeFilter> {
	private static final long serialVersionUID = -7191114549544762680L;

	private static final int ID = 12;

	private static final String OPERATING_MODE_FILTER_TRANSPONDER_VALID_TIME =
			"OperatingMode_Filter_TransponderValidTime";
	private int validTime;
	private TrIdSource source;
	private int dbAddress;
	private int dbNumber;
	private static final String OPERATING_MODE_FILTER_ENABLE_INPUT1_EVENT = "OperatingMode_Filter_Enable_Input1Event";
	private static final String OPERATING_MODE_FILTER_ENABLE_INPUT2_EVENT = "OperatingMode_Filter_Enable_Input2Event";
	private Set<InputEventFilter> inputEventFilter;
	private static final String OPERATING_MODE_FILTER_ENABLE_TRIGGER_EVENT =
			"OperatingMode_Filter_Enable_TriggerEvent";
	private static final String OPERATING_MODE_FILTER_ENABLE_TIMEOUT_EVENT =
			"OperatingMode_Filter_Enable_TimeoutEvent";
	private Set<StatusEventFilter> statusEventFilter;

	private static final List<String> ALL = new ArrayList<>();

	static {
		ALL.add(OPERATING_MODE_FILTER_TRANSPONDER_VALID_TIME);
		ALL.add(OPERATING_MODE_FILTER_ENABLE_INPUT1_EVENT);
		ALL.add(OPERATING_MODE_FILTER_ENABLE_INPUT2_EVENT);
		ALL.add(OPERATING_MODE_FILTER_ENABLE_TRIGGER_EVENT);
		ALL.add(OPERATING_MODE_FILTER_ENABLE_TIMEOUT_EVENT);
	}

	public int getValidTime() {
		return validTime;
	}

	public ReadModeFilter withValidTime(int validTime) {
		this.validTime = validTime;
		return this;
	}

	public TrIdSource getSource() {
		return source;
	}

	public ReadModeFilter withSource(TrIdSource source) {
		this.source = source;
		return this;
	}

	public int getDbAddress() {
		return dbAddress;
	}

	public ReadModeFilter withDbAddress(int dbAddress) {
		this.dbAddress = dbAddress;
		return this;
	}

	public int getDbNumber() {
		return dbNumber;
	}

	public ReadModeFilter withDbNumber(int dbNumber) {
		this.dbNumber = dbNumber;
		return this;
	}

	public Set<InputEventFilter> getInputEventFilter() {
		return inputEventFilter;
	}

	public ReadModeFilter withInputEventFilter(Set<InputEventFilter> inputEventFilter) {
		this.inputEventFilter = inputEventFilter;
		return this;
	}

	public Set<StatusEventFilter> getStatusEventFilter() {
		return statusEventFilter;
	}

	public ReadModeFilter withStatusEventFilter(Set<StatusEventFilter> statusEventFilter) {
		this.statusEventFilter = statusEventFilter;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.putShort(0, (short) validTime);
		buffer.put(2, (byte) TrIdSource.build(source));
		buffer.putShort(3, (short) dbAddress);
		buffer.put(5, (byte) dbNumber);
		buffer.put(6, (byte) InputEventFilter.build(inputEventFilter));
		buffer.put(7, (byte) StatusEventFilter.build(statusEventFilter));
		return buffer.array();
	}

	@Override
	public ReadModeFilter parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		int validTime = Short.toUnsignedInt(buffer.getShort(0));
		TrIdSource source = TrIdSource.extract(buffer.get(2));
		int dbAddress = Short.toUnsignedInt(buffer.getShort(3));
		int dbNumber = Byte.toUnsignedInt(buffer.get(5));
		Set<InputEventFilter> inputEventFilter = InputEventFilter.extract(buffer.get(6));
		Set<StatusEventFilter> statusEventFilter = StatusEventFilter.extract(buffer.get(7));
		return new ReadModeFilter().withValidTime(validTime).withSource(source).withDbAddress(dbAddress).withDbNumber(
				dbNumber).withInputEventFilter(inputEventFilter).withStatusEventFilter(statusEventFilter);
	}

	@Override
	public int getId() {
		return ID;
	}

	enum TrIdSource {
		DATA_BLOCK,
		SERIAL_NUMBER;

		public static int build(TrIdSource value) {
			return value.ordinal();
		}

		public static TrIdSource extract(int value) {
			for (TrIdSource item : values()) {
				if (item.ordinal() == value) {
					return item;
				}
			}
			return null;
		}
	}

	enum InputEventFilter {
		IN1,
		IN2;

		public static Set<InputEventFilter> extract(int value) {
			Set<InputEventFilter> result = EnumSet.noneOf(InputEventFilter.class);
			for (InputEventFilter item : values()) {
				if ((value >> item.ordinal() & 0x01) == 1) {
					result.add(item);
				}
			}
			return result;
		}

		public static int build(Iterable<InputEventFilter> value) {
			int result = 0;
			for (InputEventFilter item : value) {
				result |= 1 << item.ordinal();
			}
			return result;
		}
	}

	enum StatusEventFilter {
		TIMEOUT,
		BRM;

		public static Set<StatusEventFilter> extract(int value) {
			Set<StatusEventFilter> result = EnumSet.noneOf(StatusEventFilter.class);
			for (StatusEventFilter item : values()) {
				if ((value >> item.ordinal() & 0x01) == 1) {
					result.add(item);
				}
			}
			return result;
		}

		public static int build(Iterable<StatusEventFilter> value) {
			int result = 0;
			for (StatusEventFilter item : value) {
				result |= 1 << item.ordinal();
			}
			return result;
		}
	}

	@Override
	public boolean updateState(String name, String value) {
		ReadModeFilter stored = SerializationUtils.clone(this);
		switch (name) {
			case OPERATING_MODE_FILTER_TRANSPONDER_VALID_TIME: {
				validTime = Integer.parseInt(value);
				break;
			}
			case OPERATING_MODE_FILTER_ENABLE_INPUT1_EVENT: {
				if (Boolean.parseBoolean(value)) {
					inputEventFilter.add(InputEventFilter.IN1);
				} else {
					inputEventFilter.remove(InputEventFilter.IN1);
				}
				break;
			}
			case OPERATING_MODE_FILTER_ENABLE_INPUT2_EVENT: {
				if (Boolean.parseBoolean(value)) {
					inputEventFilter.add(InputEventFilter.IN2);
				} else {
					inputEventFilter.remove(InputEventFilter.IN2);
				}
				break;
			}
			case OPERATING_MODE_FILTER_ENABLE_TIMEOUT_EVENT: {
				if (Boolean.parseBoolean(value)) {
					statusEventFilter.add(StatusEventFilter.TIMEOUT);
				} else {
					statusEventFilter.remove(StatusEventFilter.TIMEOUT);
				}
				break;
			}
			case OPERATING_MODE_FILTER_ENABLE_TRIGGER_EVENT: {
				if (Boolean.parseBoolean(value)) {
					statusEventFilter.add(StatusEventFilter.BRM);
				} else {
					statusEventFilter.remove(StatusEventFilter.BRM);
				}
				break;
			}
		}
		return !equals(stored);
	}

	@Override
	public Map<String, String> getStates() {
		Map<String, String> result = new HashMap<>();
		result.put(OPERATING_MODE_FILTER_TRANSPONDER_VALID_TIME, Integer.toString(validTime));
		result.put(OPERATING_MODE_FILTER_ENABLE_INPUT1_EVENT,
				Boolean.toString(inputEventFilter.contains(InputEventFilter.IN1)));
		result.put(OPERATING_MODE_FILTER_ENABLE_INPUT2_EVENT,
				Boolean.toString(inputEventFilter.contains(InputEventFilter.IN2)));
		result.put(OPERATING_MODE_FILTER_ENABLE_TRIGGER_EVENT,
				Boolean.toString(statusEventFilter.contains(StatusEventFilter.BRM)));
		result.put(OPERATING_MODE_FILTER_ENABLE_TIMEOUT_EVENT,
				Boolean.toString(statusEventFilter.contains(StatusEventFilter.TIMEOUT)));
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
		ReadModeFilter that = (ReadModeFilter) o;
		return validTime == that.validTime && dbAddress == that.dbAddress && dbNumber == that.dbNumber &&
				source == that.source && Objects.equals(inputEventFilter, that.inputEventFilter) && Objects.equals(
				statusEventFilter, that.statusEventFilter);
	}

	@Override
	public int hashCode() {

		return Objects.hash(validTime, source, dbAddress, dbNumber, inputEventFilter, statusEventFilter);
	}
}

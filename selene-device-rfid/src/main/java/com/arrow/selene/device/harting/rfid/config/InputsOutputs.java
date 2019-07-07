package com.arrow.selene.device.harting.rfid.config;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.SerializationUtils;

public class InputsOutputs implements ConfigParameter<InputsOutputs> {
	private static final long serialVersionUID = 7926299858105989052L;

	private static final int ID = 2;

	private static final String DIGITAL_IO_OUTPUT_NO1_IDLE_MODE = "DigitalIO_Output_No1_IdleMode";
	private static final String DIGITAL_IO_OUTPUT_NO2_IDLE_MODE = "DigitalIO_Output_No2_IdleMode";
	private static final String DIGITAL_IO_RELAY_NO1_IDLE_MODE = "DigitalIO_Relay_No1_IdleMode";
	private static final String DIGITAL_IO_RELAY_NO2_IDLE_MODE = "DigitalIO_Relay_No2_IdleMode";
	private IdleMode idleMode;

	private static final String DIGITAL_IO_OUTPUT_NO1_ACTIVE_STATE = "DigitalIO_Output_No1_ActiveState";
	private static final String DIGITAL_IO_OUTPUT_NO2_ACTIVE_STATE = "DigitalIO_Output_No2_ActiveState";
	private static final String DIGITAL_IO_RELAY_NO1_ACTIVE_STATE = "DigitalIO_Relay_No1_ActiveState";
	private static final String DIGITAL_IO_RELAY_NO2_ACTIVE_STATE = "DigitalIO_Relay_No2_ActiveState";
	private FlashMode flashMode;

	private static final String DIGITAL_IO_INPUT_NO1_MODE = "DigitalIO_Input_No1_Mode";
	private static final String DIGITAL_IO_INPUT_NO2_MODE = "DigitalIO_Input_No2_Mode";
	private Set<Input> inActive;

	private static final String DIGITAL_IO_RELAY_NO1_SETTLING_TIME = "DigitalIO_Relay_No1_SettlingTime";
	private int rel1Time;
	private static final String DIGITAL_IO_OUTPUT_NO1_SETTLING_TIME = "DigitalIO_Output_No1_SettlingTime";
	private int out1Time;
	private static final String DIGITAL_IO_RELAY_NO2_SETTLING_TIME = "DigitalIO_Relay_No2_SettlingTime";
	private int rel2Time;
	private static final String DIGITAL_IO_OUTPUT_NO2_SETTLING_TIME = "DigitalIO_Output_No2_SettlingTime";
	private int out2Time;

	private static final List<String> ALL = new ArrayList<>();

	static {
		ALL.add(DIGITAL_IO_OUTPUT_NO1_IDLE_MODE);
		ALL.add(DIGITAL_IO_OUTPUT_NO2_IDLE_MODE);
		ALL.add(DIGITAL_IO_RELAY_NO1_IDLE_MODE);
		ALL.add(DIGITAL_IO_RELAY_NO2_IDLE_MODE);
		ALL.add(DIGITAL_IO_OUTPUT_NO1_ACTIVE_STATE);
		ALL.add(DIGITAL_IO_OUTPUT_NO2_ACTIVE_STATE);
		ALL.add(DIGITAL_IO_RELAY_NO1_ACTIVE_STATE);
		ALL.add(DIGITAL_IO_RELAY_NO2_ACTIVE_STATE);
		ALL.add(DIGITAL_IO_INPUT_NO1_MODE);
		ALL.add(DIGITAL_IO_INPUT_NO2_MODE);
		ALL.add(DIGITAL_IO_RELAY_NO1_SETTLING_TIME);
		ALL.add(DIGITAL_IO_OUTPUT_NO1_SETTLING_TIME);
		ALL.add(DIGITAL_IO_RELAY_NO2_SETTLING_TIME);
		ALL.add(DIGITAL_IO_OUTPUT_NO2_SETTLING_TIME);
	}

	public InputsOutputs withIdleMode(IdleMode idleMode) {
		this.idleMode = idleMode;
		return this;
	}

	public InputsOutputs withFlashMode(FlashMode flashMode) {
		this.flashMode = flashMode;
		return this;
	}

	public InputsOutputs withInActive(Set<Input> inActive) {
		this.inActive = inActive;
		return this;
	}

	public InputsOutputs withRel1Time(int rel1Time) {
		this.rel1Time = rel1Time;
		return this;
	}

	public InputsOutputs withOut1Time(int out1Time) {
		this.out1Time = out1Time;
		return this;
	}

	public InputsOutputs withRel2Time(int rel2Time) {
		this.rel2Time = rel2Time;
		return this;
	}

	public InputsOutputs withOut2Time(int out2Time) {
		this.out2Time = out2Time;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.putShort(0, (short) IdleMode.build(idleMode));
		buffer.putShort(2, (short) FlashMode.build(flashMode));
		buffer.put(4, Input.build(inActive));
		buffer.putShort(6, (short) rel1Time);
		buffer.putShort(8, (short) out1Time);
		buffer.putShort(10, (short) rel2Time);
		buffer.put(13, (byte) out2Time);
		return buffer.array();
	}

	@Override
	public InputsOutputs parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		IdleMode idleMode = IdleMode.extract(buffer.getShort(0));
		FlashMode flashMode = FlashMode.extract(buffer.getShort(2));
		Set<Input> inActive = Input.extract(buffer.get(4));
		int rel1Time = Short.toUnsignedInt(buffer.getShort(6));
		int out1Time = Short.toUnsignedInt(buffer.getShort(8));
		int rel2Time = Short.toUnsignedInt(buffer.getShort(10));
		int out2Time = Short.toUnsignedInt(buffer.get(13));
		return new InputsOutputs().withIdleMode(idleMode).withFlashMode(flashMode).withInActive(inActive).withRel1Time(
				rel1Time).withOut1Time(out1Time).withRel2Time(rel2Time).withOut2Time(out2Time);
	}

	@Override
	public int getId() {
		return ID;
	}

	static class IdleMode {
		Mode rel1;
		Mode out2;
		Mode out1;
		Mode rel2;

		public IdleMode(Mode rel1, Mode out2, Mode out1, Mode rel2) {
			this.rel1 = rel1;
			this.out2 = out2;
			this.out1 = out1;
			this.rel2 = rel2;
		}

		public static IdleMode extract(int value) {
			Mode rel1 = Mode.values()[value >> 14 & 0b11];
			Mode out2 = Mode.values()[value >> 12 & 0b11];
			Mode out1 = Mode.values()[value >> 10 & 0b11];
			Mode rel2 = Mode.values()[value >> 8 & 0b11];
			return new IdleMode(rel1, out2, out1, rel2);
		}

		public static int build(IdleMode value) {
			int result = 0;
			result |= value.rel1.ordinal() << 14;
			result |= value.out2.ordinal() << 12;
			result |= value.out1.ordinal() << 10;
			result |= value.rel2.ordinal() << 8;
			return result;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			IdleMode idleMode = (IdleMode) o;
			return rel1 == idleMode.rel1 && out2 == idleMode.out2 && out1 == idleMode.out1 && rel2 == idleMode.rel2;
		}

		@Override
		public int hashCode() {

			return Objects.hash(rel1, out2, out1, rel2);
		}
	}

	enum Mode {
		UNCHANGED,
		ON,
		OFF,
		FLASH
	}

	static class FlashMode {
		Frequency rel1;
		Frequency out2;
		Frequency out1;
		Frequency rel2;

		public FlashMode(Frequency rel1, Frequency out2, Frequency out1, Frequency rel2) {
			this.rel1 = rel1;
			this.out2 = out2;
			this.out1 = out1;
			this.rel2 = rel2;
		}

		public static FlashMode extract(int value) {
			Frequency rel1 = Frequency.values()[value >> 14 & 0b11];
			Frequency out2 = Frequency.values()[value >> 12 & 0b11];
			Frequency out1 = Frequency.values()[value >> 10 & 0b11];
			Frequency rel2 = Frequency.values()[value >> 8 & 0b11];
			return new FlashMode(rel1, out2, out1, rel2);
		}

		public static int build(FlashMode value) {
			int result = 0;
			result |= value.rel1.ordinal() << 14;
			result |= value.out2.ordinal() << 12;
			result |= value.out1.ordinal() << 10;
			result |= value.rel2.ordinal() << 6;
			return result;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			FlashMode flashMode = (FlashMode) o;
			return rel1 == flashMode.rel1 && out2 == flashMode.out2 && out1 == flashMode.out1 && rel2 == flashMode
					.rel2;
		}

		@Override
		public int hashCode() {

			return Objects.hash(rel1, out2, out1, rel2);
		}
	}

	enum Frequency {
		FREQUENCY_0HZ,
		FREQUENCY_4HZ,
		FREQUENCY_2HZ,
		FREQUENCY_1HZ
	}

	enum Input {
		IN1,
		IN2;

		public static Set<Input> extract(byte value) {
			Set<Input> result = EnumSet.noneOf(Input.class);
			for (Input item : values()) {
				if ((value >> item.ordinal() & 0x01) == 1) {
					result.add(item);
				}
			}
			return result;
		}

		public static byte build(Iterable<Input> value) {
			byte result = 0;
			for (Input item : value) {
				result |= 1 << item.ordinal();
			}
			return result;
		}
	}

	@Override
	public boolean updateState(String name, String value) {
		InputsOutputs stored = SerializationUtils.clone(this);
		switch (name) {
			case DIGITAL_IO_INPUT_NO1_MODE: {
				if (Objects.equals(value.toLowerCase(Locale.getDefault()), "open")) {
					inActive.add(Input.IN1);
				} else if (Objects.equals(value.toLowerCase(Locale.getDefault()), "closed")) {
					inActive.remove(Input.IN1);
				}
				break;
			}
			case DIGITAL_IO_INPUT_NO2_MODE: {
				if (Objects.equals(value.toLowerCase(Locale.getDefault()), "open")) {
					inActive.add(Input.IN2);
				} else if (Objects.equals(value.toLowerCase(Locale.getDefault()), "closed")) {
					inActive.remove(Input.IN2);
				}
				break;
			}
			case DIGITAL_IO_OUTPUT_NO1_ACTIVE_STATE: {
				flashMode.out1 = Frequency.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
			case DIGITAL_IO_OUTPUT_NO2_ACTIVE_STATE: {
				flashMode.out2 = Frequency.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
			case DIGITAL_IO_RELAY_NO1_ACTIVE_STATE: {
				flashMode.rel1 = Frequency.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
			case DIGITAL_IO_RELAY_NO2_ACTIVE_STATE: {
				flashMode.rel2 = Frequency.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
			case DIGITAL_IO_OUTPUT_NO1_IDLE_MODE: {
				idleMode.out1 = Mode.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
			case DIGITAL_IO_OUTPUT_NO2_IDLE_MODE: {
				idleMode.out2 = Mode.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
			case DIGITAL_IO_RELAY_NO1_IDLE_MODE: {
				idleMode.rel1 = Mode.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
			case DIGITAL_IO_RELAY_NO2_IDLE_MODE: {
				idleMode.rel2 = Mode.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
			case DIGITAL_IO_OUTPUT_NO1_SETTLING_TIME: {
				out1Time = Integer.parseInt(value);
				break;
			}
			case DIGITAL_IO_OUTPUT_NO2_SETTLING_TIME: {
				out2Time = Integer.parseInt(value);
				break;
			}
			case DIGITAL_IO_RELAY_NO1_SETTLING_TIME: {
				rel1Time = Integer.parseInt(value);
				break;
			}
			case DIGITAL_IO_RELAY_NO2_SETTLING_TIME: {
				rel2Time = Integer.parseInt(value);
				break;
			}
		}
		return !equals(stored);
	}

	@Override
	public Map<String, String> getStates() {
		Map<String, String> result = new HashMap<>();
		result.put(DIGITAL_IO_OUTPUT_NO1_IDLE_MODE, idleMode.out1.name());
		result.put(DIGITAL_IO_OUTPUT_NO2_IDLE_MODE, idleMode.out2.name());
		result.put(DIGITAL_IO_RELAY_NO1_IDLE_MODE, idleMode.rel1.name());
		result.put(DIGITAL_IO_RELAY_NO2_IDLE_MODE, idleMode.rel2.name());
		result.put(DIGITAL_IO_OUTPUT_NO1_ACTIVE_STATE, flashMode.out1.name());
		result.put(DIGITAL_IO_OUTPUT_NO2_ACTIVE_STATE, flashMode.out2.name());
		result.put(DIGITAL_IO_RELAY_NO1_ACTIVE_STATE, flashMode.rel1.name());
		result.put(DIGITAL_IO_RELAY_NO2_ACTIVE_STATE, flashMode.rel2.name());
		result.put(DIGITAL_IO_INPUT_NO1_MODE, inActive.contains(Input.IN1) ? "OPEN" : "CLOSED");
		result.put(DIGITAL_IO_INPUT_NO2_MODE, inActive.contains(Input.IN2) ? "OPEN" : "CLOSED");
		result.put(DIGITAL_IO_RELAY_NO1_SETTLING_TIME, Integer.toString(rel1Time));
		result.put(DIGITAL_IO_OUTPUT_NO1_SETTLING_TIME, Integer.toString(out1Time));
		result.put(DIGITAL_IO_RELAY_NO2_SETTLING_TIME, Integer.toString(rel2Time));
		result.put(DIGITAL_IO_OUTPUT_NO2_SETTLING_TIME, Integer.toString(out2Time));
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
		InputsOutputs that = (InputsOutputs) o;
		return rel1Time == that.rel1Time && out1Time == that.out1Time && rel2Time == that.rel2Time &&
				out2Time == that.out2Time && Objects.equals(idleMode, that.idleMode) && Objects.equals(flashMode,
				that.flashMode) && Objects.equals(inActive, that.inActive);
	}

	@Override
	public int hashCode() {

		return Objects.hash(idleMode, flashMode, inActive, rel1Time, out1Time, rel2Time, out2Time);
	}
}

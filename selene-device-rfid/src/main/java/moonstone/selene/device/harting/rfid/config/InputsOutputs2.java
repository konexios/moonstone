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

public class InputsOutputs2 implements ConfigParameter<InputsOutputs2> {
	private static final long serialVersionUID = 1431665671535277646L;
	private static final int ID = 9;

	private static final String DIGITAL_IO_OUTPUT_NO1_READ_EVENT_ACTIVATION_ANTENNA1 =
			"DigitalIO_Output_No1_ReadEventActivation_Antenna1";
	private static final String DIGITAL_IO_OUTPUT_NO1_READ_EVENT_ACTIVATION_ANTENNA2 =
			"DigitalIO_Output_No1_ReadEventActivation_Antenna2";
	private static final String DIGITAL_IO_OUTPUT_NO1_READ_EVENT_ACTIVATION_ANTENNA3 =
			"DigitalIO_Output_No1_ReadEventActivation_Antenna3";
	private static final String DIGITAL_IO_OUTPUT_NO1_READ_EVENT_ACTIVATION_ANTENNA4 =
			"DigitalIO_Output_No1_ReadEventActivation_Antenna4";
	private Set<Antenna> output1;
	private static final String DIGITAL_IO_OUTPUT_NO2_READ_EVENT_ACTIVATION_ANTENNA1 =
			"DigitalIO_Output_No2_ReadEventActivation_Antenna1";
	private static final String DIGITAL_IO_OUTPUT_NO2_READ_EVENT_ACTIVATION_ANTENNA2 =
			"DigitalIO_Output_No2_ReadEventActivation_Antenna2";
	private static final String DIGITAL_IO_OUTPUT_NO2_READ_EVENT_ACTIVATION_ANTENNA3 =
			"DigitalIO_Output_No2_ReadEventActivation_Antenna3";
	private static final String DIGITAL_IO_OUTPUT_NO2_READ_EVENT_ACTIVATION_ANTENNA4 =
			"DigitalIO_Output_No2_ReadEventActivation_Antenna4";
	private Set<Antenna> output2;
	private static final String DIGITAL_IO_RELAY_NO1_READ_EVENT_ACTIVATION_ANTENNA1 =
			"DigitalIO_Relay_No1_ReadEventActivation_Antenna1";
	private static final String DIGITAL_IO_RELAY_NO1_READ_EVENT_ACTIVATION_ANTENNA2 =
			"DigitalIO_Relay_No1_ReadEventActivation_Antenna2";
	private static final String DIGITAL_IO_RELAY_NO1_READ_EVENT_ACTIVATION_ANTENNA3 =
			"DigitalIO_Relay_No1_ReadEventActivation_Antenna3";
	private static final String DIGITAL_IO_RELAY_NO1_READ_EVENT_ACTIVATION_ANTENNA4 =
			"DigitalIO_Relay_No1_ReadEventActivation_Antenna4";
	private Set<Antenna> relay1;
	private static final String DIGITAL_IO_RELAY_NO2_READ_EVENT_ACTIVATION_ANTENNA1 =
			"DigitalIO_Relay_No2_ReadEventActivation_Antenna1";
	private static final String DIGITAL_IO_RELAY_NO2_READ_EVENT_ACTIVATION_ANTENNA2 =
			"DigitalIO_Relay_No2_ReadEventActivation_Antenna2";
	private static final String DIGITAL_IO_RELAY_NO2_READ_EVENT_ACTIVATION_ANTENNA3 =
			"DigitalIO_Relay_No2_ReadEventActivation_Antenna3";
	private static final String DIGITAL_IO_RELAY_NO2_READ_EVENT_ACTIVATION_ANTENNA4 =
			"DigitalIO_Relay_No2_ReadEventActivation_Antenna4";
	private Set<Antenna> relay2;

	private static final List<String> ALL = new ArrayList<>();

	static {
		ALL.add(DIGITAL_IO_OUTPUT_NO1_READ_EVENT_ACTIVATION_ANTENNA1);
		ALL.add(DIGITAL_IO_OUTPUT_NO1_READ_EVENT_ACTIVATION_ANTENNA2);
		ALL.add(DIGITAL_IO_OUTPUT_NO1_READ_EVENT_ACTIVATION_ANTENNA3);
		ALL.add(DIGITAL_IO_OUTPUT_NO1_READ_EVENT_ACTIVATION_ANTENNA4);
		ALL.add(DIGITAL_IO_OUTPUT_NO2_READ_EVENT_ACTIVATION_ANTENNA1);
		ALL.add(DIGITAL_IO_OUTPUT_NO2_READ_EVENT_ACTIVATION_ANTENNA2);
		ALL.add(DIGITAL_IO_OUTPUT_NO2_READ_EVENT_ACTIVATION_ANTENNA3);
		ALL.add(DIGITAL_IO_OUTPUT_NO2_READ_EVENT_ACTIVATION_ANTENNA4);
		ALL.add(DIGITAL_IO_RELAY_NO1_READ_EVENT_ACTIVATION_ANTENNA1);
		ALL.add(DIGITAL_IO_RELAY_NO1_READ_EVENT_ACTIVATION_ANTENNA2);
		ALL.add(DIGITAL_IO_RELAY_NO1_READ_EVENT_ACTIVATION_ANTENNA3);
		ALL.add(DIGITAL_IO_RELAY_NO1_READ_EVENT_ACTIVATION_ANTENNA4);
		ALL.add(DIGITAL_IO_RELAY_NO2_READ_EVENT_ACTIVATION_ANTENNA1);
		ALL.add(DIGITAL_IO_RELAY_NO2_READ_EVENT_ACTIVATION_ANTENNA2);
		ALL.add(DIGITAL_IO_RELAY_NO2_READ_EVENT_ACTIVATION_ANTENNA3);
		ALL.add(DIGITAL_IO_RELAY_NO2_READ_EVENT_ACTIVATION_ANTENNA4);
	}

	public InputsOutputs2 withOutput1(Set<Antenna> output1) {
		this.output1 = output1;
		return this;
	}

	public InputsOutputs2 withOutput2(Set<Antenna> output2) {
		this.output2 = output2;
		return this;
	}

	public InputsOutputs2 withRelay1(Set<Antenna> relay1) {
		this.relay1 = relay1;
		return this;
	}

	public InputsOutputs2 withRelay2(Set<Antenna> relay2) {
		this.relay2 = relay2;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.put(0, (byte) Antenna.build(output1));
		buffer.put(1, (byte) Antenna.build(output2));
		buffer.put(7, (byte) Antenna.build(relay1));
		buffer.put(8, (byte) Antenna.build(relay2));
		return buffer.array();
	}

	@Override
	public InputsOutputs2 parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		Set<Antenna> output1 = Antenna.extract(buffer.get(0));
		Set<Antenna> output2 = Antenna.extract(buffer.get(1));
		Set<Antenna> relay1 = Antenna.extract(buffer.get(7));
		Set<Antenna> relay2 = Antenna.extract(buffer.get(8));

		return new InputsOutputs2().withOutput1(output1).withOutput2(output2).withRelay1(relay1).withRelay2(relay2);
	}

	@Override
	public int getId() {
		return ID;
	}

	enum Antenna {
		ANT1,
		ANT2,
		ANT3,
		ANT4;

		public static Set<Antenna> extract(int value) {
			Set<Antenna> result = EnumSet.noneOf(Antenna.class);
			for (Antenna item : values()) {
				if ((value >> item.ordinal() & 0x01) == 1) {
					result.add(item);
				}
			}
			return result;
		}

		public static int build(Iterable<Antenna> value) {
			int result = 0;
			for (Antenna item : value) {
				result |= 1 << item.ordinal();
			}
			return result;
		}
	}

	@Override
	public boolean updateState(String name, String value) {
		InputsOutputs2 stored = SerializationUtils.clone(this);
		switch (name) {
			case DIGITAL_IO_OUTPUT_NO1_READ_EVENT_ACTIVATION_ANTENNA1: {
				if (Boolean.parseBoolean(value)) {
					output1.add(Antenna.ANT1);
				} else {
					output1.remove(Antenna.ANT1);
				}
				break;
			}
			case DIGITAL_IO_OUTPUT_NO1_READ_EVENT_ACTIVATION_ANTENNA2: {
				if (Boolean.parseBoolean(value)) {
					output1.add(Antenna.ANT2);
				} else {
					output1.remove(Antenna.ANT2);
				}
				break;
			}
			case DIGITAL_IO_OUTPUT_NO1_READ_EVENT_ACTIVATION_ANTENNA3: {
				if (Boolean.parseBoolean(value)) {
					output1.add(Antenna.ANT3);
				} else {
					output1.remove(Antenna.ANT3);
				}
				break;
			}
			case DIGITAL_IO_OUTPUT_NO1_READ_EVENT_ACTIVATION_ANTENNA4: {
				if (Boolean.parseBoolean(value)) {
					output1.add(Antenna.ANT4);
				} else {
					output1.remove(Antenna.ANT4);
				}
				break;
			}
			case DIGITAL_IO_OUTPUT_NO2_READ_EVENT_ACTIVATION_ANTENNA1: {
				if (Boolean.parseBoolean(value)) {
					output2.add(Antenna.ANT1);
				} else {
					output2.remove(Antenna.ANT1);
				}
				break;
			}
			case DIGITAL_IO_OUTPUT_NO2_READ_EVENT_ACTIVATION_ANTENNA2: {
				if (Boolean.parseBoolean(value)) {
					output2.add(Antenna.ANT2);
				} else {
					output2.remove(Antenna.ANT2);
				}
				break;
			}
			case DIGITAL_IO_OUTPUT_NO2_READ_EVENT_ACTIVATION_ANTENNA3: {
				if (Boolean.parseBoolean(value)) {
					output2.add(Antenna.ANT3);
				} else {
					output2.remove(Antenna.ANT3);
				}
				break;
			}
			case DIGITAL_IO_OUTPUT_NO2_READ_EVENT_ACTIVATION_ANTENNA4: {
				if (Boolean.parseBoolean(value)) {
					output2.add(Antenna.ANT4);
				} else {
					output2.remove(Antenna.ANT4);
				}
				break;
			}
			case DIGITAL_IO_RELAY_NO1_READ_EVENT_ACTIVATION_ANTENNA1: {
				if (Boolean.parseBoolean(value)) {
					relay1.add(Antenna.ANT1);
				} else {
					relay1.remove(Antenna.ANT1);
				}
				break;
			}
			case DIGITAL_IO_RELAY_NO1_READ_EVENT_ACTIVATION_ANTENNA2: {
				if (Boolean.parseBoolean(value)) {
					relay1.add(Antenna.ANT2);
				} else {
					relay1.remove(Antenna.ANT2);
				}
				break;
			}
			case DIGITAL_IO_RELAY_NO1_READ_EVENT_ACTIVATION_ANTENNA3: {
				if (Boolean.parseBoolean(value)) {
					relay1.add(Antenna.ANT3);
				} else {
					relay1.remove(Antenna.ANT3);
				}
				break;
			}
			case DIGITAL_IO_RELAY_NO1_READ_EVENT_ACTIVATION_ANTENNA4: {
				if (Boolean.parseBoolean(value)) {
					relay1.add(Antenna.ANT4);
				} else {
					relay1.remove(Antenna.ANT4);
				}
				break;
			}
			case DIGITAL_IO_RELAY_NO2_READ_EVENT_ACTIVATION_ANTENNA1: {
				if (Boolean.parseBoolean(value)) {
					relay2.add(Antenna.ANT1);
				} else {
					relay2.remove(Antenna.ANT1);
				}
				break;
			}
			case DIGITAL_IO_RELAY_NO2_READ_EVENT_ACTIVATION_ANTENNA2: {
				if (Boolean.parseBoolean(value)) {
					relay2.add(Antenna.ANT2);
				} else {
					relay2.remove(Antenna.ANT2);
				}
				break;
			}
			case DIGITAL_IO_RELAY_NO2_READ_EVENT_ACTIVATION_ANTENNA3: {
				if (Boolean.parseBoolean(value)) {
					relay2.add(Antenna.ANT3);
				} else {
					relay2.remove(Antenna.ANT3);
				}
				break;
			}
			case DIGITAL_IO_RELAY_NO2_READ_EVENT_ACTIVATION_ANTENNA4: {
				if (Boolean.parseBoolean(value)) {
					relay2.add(Antenna.ANT4);
				} else {
					relay2.remove(Antenna.ANT4);
				}
				break;
			}
		}
		return !equals(stored);
	}

	@Override
	public Map<String, String> getStates() {
		Map<String, String> result = new HashMap<>();
		result.put(DIGITAL_IO_OUTPUT_NO1_READ_EVENT_ACTIVATION_ANTENNA1,
				Boolean.toString(output1.contains(Antenna.ANT1)));
		result.put(DIGITAL_IO_OUTPUT_NO1_READ_EVENT_ACTIVATION_ANTENNA2,
				Boolean.toString(output1.contains(Antenna.ANT2)));
		result.put(DIGITAL_IO_OUTPUT_NO1_READ_EVENT_ACTIVATION_ANTENNA3,
				Boolean.toString(output1.contains(Antenna.ANT3)));
		result.put(DIGITAL_IO_OUTPUT_NO1_READ_EVENT_ACTIVATION_ANTENNA4,
				Boolean.toString(output1.contains(Antenna.ANT4)));
		result.put(DIGITAL_IO_OUTPUT_NO2_READ_EVENT_ACTIVATION_ANTENNA1,
				Boolean.toString(output2.contains(Antenna.ANT1)));
		result.put(DIGITAL_IO_OUTPUT_NO2_READ_EVENT_ACTIVATION_ANTENNA2,
				Boolean.toString(output2.contains(Antenna.ANT2)));
		result.put(DIGITAL_IO_OUTPUT_NO2_READ_EVENT_ACTIVATION_ANTENNA3,
				Boolean.toString(output2.contains(Antenna.ANT3)));
		result.put(DIGITAL_IO_OUTPUT_NO2_READ_EVENT_ACTIVATION_ANTENNA4,
				Boolean.toString(output2.contains(Antenna.ANT4)));
		result.put(DIGITAL_IO_RELAY_NO1_READ_EVENT_ACTIVATION_ANTENNA1,
				Boolean.toString(relay1.contains(Antenna.ANT1)));
		result.put(DIGITAL_IO_RELAY_NO1_READ_EVENT_ACTIVATION_ANTENNA2,
				Boolean.toString(relay1.contains(Antenna.ANT2)));
		result.put(DIGITAL_IO_RELAY_NO1_READ_EVENT_ACTIVATION_ANTENNA3,
				Boolean.toString(relay1.contains(Antenna.ANT3)));
		result.put(DIGITAL_IO_RELAY_NO1_READ_EVENT_ACTIVATION_ANTENNA4,
				Boolean.toString(relay1.contains(Antenna.ANT4)));
		result.put(DIGITAL_IO_RELAY_NO2_READ_EVENT_ACTIVATION_ANTENNA1,
				Boolean.toString(relay2.contains(Antenna.ANT1)));
		result.put(DIGITAL_IO_RELAY_NO2_READ_EVENT_ACTIVATION_ANTENNA2,
				Boolean.toString(relay2.contains(Antenna.ANT2)));
		result.put(DIGITAL_IO_RELAY_NO2_READ_EVENT_ACTIVATION_ANTENNA3,
				Boolean.toString(relay2.contains(Antenna.ANT3)));
		result.put(DIGITAL_IO_RELAY_NO2_READ_EVENT_ACTIVATION_ANTENNA4,
				Boolean.toString(relay2.contains(Antenna.ANT4)));
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
		InputsOutputs2 that = (InputsOutputs2) o;
		return Objects.equals(output1, that.output1) && Objects.equals(output2, that.output2) && Objects.equals(relay1,
				that.relay1) && Objects.equals(relay2, that.relay2);
	}

	@Override
	public int hashCode() {

		return Objects.hash(output1, output2, relay1, relay2);
	}
}

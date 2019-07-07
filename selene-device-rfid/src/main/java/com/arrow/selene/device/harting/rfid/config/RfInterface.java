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

public class RfInterface implements ConfigParameter<RfInterface> {
	private static final long serialVersionUID = 6816698180163970596L;

	private static final int ID = 3;

	private static final String TRANSPONDER_DRIVER_UHF_EPC_CLASS1_GEN2 = "Transponder_Driver_UHF_EPC_Class1Gen2";
	private Set<TransponderType> drivers;
	private static final String AIR_INTERFACE_ANTENNA_UHF_NO1_OUTPUT_POWER =
			"AirInterface_Antenna_UHF_No1_OutputPower";
	private Level rfPowerAnt1;
	private static final String AIR_INTERFACE_REGION_UHF_REGULATION = "AirInterface_Region_UHF_Regulation";
	private Region region;
	private Set<DcPower> dcPower;
	private FrequencyUsage frequencyUsage;
	private int numOfPreferredChannels;
	private int preferredChannels;

	private static final List<String> ALL = new ArrayList<>();

	static {
		ALL.add(TRANSPONDER_DRIVER_UHF_EPC_CLASS1_GEN2);
		ALL.add(AIR_INTERFACE_ANTENNA_UHF_NO1_OUTPUT_POWER);
		ALL.add(AIR_INTERFACE_REGION_UHF_REGULATION);
	}

	public RfInterface() {
	}

	public RfInterface(Set<TransponderType> drivers, Level rfPowerAnt1, Region region, Set<DcPower> dcPower,
	                   FrequencyUsage frequencyUsage, int numOfPreferredChannels, int preferredChannels) {
		this.drivers = drivers;
		this.rfPowerAnt1 = rfPowerAnt1;
		this.region = region;
		this.dcPower = dcPower;
		this.frequencyUsage = frequencyUsage;
		this.numOfPreferredChannels = numOfPreferredChannels;
		this.preferredChannels = preferredChannels;
	}

	public RfInterface withDrivers(Set<TransponderType> drivers) {
		this.drivers = drivers;
		return this;
	}

	public RfInterface withRfPowerAnt1(Level rfPowerAnt1) {
		this.rfPowerAnt1 = rfPowerAnt1;
		return this;
	}

	public RfInterface withRegion(Region region) {
		this.region = region;
		return this;
	}

	public RfInterface withDcPower(Set<DcPower> dcPower) {
		this.dcPower = dcPower;
		return this;
	}

	public RfInterface withFrequencyUsage(FrequencyUsage frequencyUsage) {
		this.frequencyUsage = frequencyUsage;
		return this;
	}

	public RfInterface withNumOfPreferredChannels(int numOfPreferredChannels) {
		this.numOfPreferredChannels = numOfPreferredChannels;
		return this;
	}

	public RfInterface withPreferredChannels(int preferredChannels) {
		this.preferredChannels = preferredChannels;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.putShort(0, (short) TransponderType.build(drivers));
		buffer.put(2, (byte) Level.build(rfPowerAnt1));
		buffer.put(3, (byte) Region.build(region));
		buffer.put(5, (byte) DcPower.build(dcPower));
		buffer.putShort(8, (short) FrequencyUsage.build(frequencyUsage));
		buffer.put(11, (byte) numOfPreferredChannels);
		buffer.putShort(12, (short) preferredChannels);
		return buffer.array();
	}

	@Override
	public RfInterface parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		Set<TransponderType> drivers = TransponderType.extract(buffer.getShort(0));
		Level rfPowerAnt1 = Level.extract(buffer.get(2));
		Region region = Region.extract(buffer.get(3));
		Set<DcPower> dcPower = DcPower.extract(buffer.get(5));
		FrequencyUsage frequencyUsage = FrequencyUsage.extract(buffer.getShort(8));
		int numOfPreferredChannels = buffer.get(11);
		int preferredChannels = buffer.getShort(12);
		return new RfInterface(drivers, rfPowerAnt1, region, dcPower, frequencyUsage, numOfPreferredChannels,
				preferredChannels);
	}

	@Override
	public int getId() {
		return ID;
	}

	public enum TransponderType {
		EPC_CLASS1_GEN2(4);

		private final int value;

		TransponderType(int value) {
			this.value = value;
		}

		public static Set<TransponderType> extract(int value) {
			Set<TransponderType> result = EnumSet.noneOf(TransponderType.class);
			for (TransponderType item : values()) {
				if ((value >> item.value & 0x01) == 1) {
					result.add(item);
				}
			}
			return result;
		}

		public static int build(Iterable<TransponderType> value) {
			int result = 0;
			for (TransponderType item : value) {
				result |= 1 << item.value;
			}
			return result;
		}
	}

	public enum Level {
		WATT_0_3(0x12),
		WATT_0_4(0x13),
		WATT_0_5(0x14),
		WATT_0_6(0x15),
		WATT_0_7(0x16),
		WATT_0_8(0x17),
		WATT_1_0(0x19),
		WATT_1_2(0x1b),
		WATT_1_4(0x1d),
		WATT_1_6(0x1f),
		WATT_1_8(0x21),
		WATT_2_0(0x23),
		WATT_2_2(0x25),
		WATT_2_4(0x27),
		WATT_2_7(0x2a),
		WATT_3_0(0x2d),
		WATT_3_3(0x30),
		WATT_3_6(0x33),
		WATT_4_0(0x37);

		private final int value;

		Level(int value) {
			this.value = value;
		}

		public static Level extract(int value) {
			for (Level item : values()) {
				if (value == item.value) {
					return item;
				}
			}
			return null;
		}

		public static int build(Level value) {
			return value.value;
		}
	}

	public enum Region {
		EUROPE(0x06),
		ASIA_OCEANIA(0x16),
		RUSSIA(0x26),
		AFRICA(0x36),
		INDIA(0x46),
		AMERICA(0x04),
		CHINA(0x14),
		AUSTRALIA_NEW_ZEALAND(0x24),
		BRAZIL(0x34),
		ISRAEL(0x44),
		UNKNOWN(0xff);

		private final int value;

		Region(int value) {
			this.value = value;
		}

		public static Region extract(int value) {
			for (Region item : values()) {
				if (value == item.value) {
					return item;
				}
			}
			return null;
		}

		public static int build(Region value) {
			return value.value;
		}
	}

	public enum DcPower {
		ANT1,
		ANT2,
		ANT3,
		ANT4;

		public static Set<DcPower> extract(int value) {
			Set<DcPower> result = EnumSet.noneOf(DcPower.class);
			for (DcPower item : values()) {
				if ((value >> item.ordinal() & 0x01) == 1) {
					result.add(item);
				}
			}
			return result;
		}

		public static int build(Iterable<DcPower> value) {
			int result = 0;
			for (DcPower item : value) {
				result |= 1 << item.ordinal();
			}
			return result;
		}
	}

	public static class FrequencyUsage {
		int upperChanel;
		int lowerChanel;

		public FrequencyUsage(int upperChanel, int lowerChanel) {
			this.upperChanel = upperChanel;
			this.lowerChanel = lowerChanel;
		}

		public FrequencyUsage withUpperChanel(int upperChanel) {
			this.upperChanel = upperChanel;
			return this;
		}

		public FrequencyUsage withLowerChanel(int lowerChanel) {
			this.lowerChanel = lowerChanel;
			return this;
		}

		public static FrequencyUsage extract(int value) {
			int upperChanel = value >> 8 & 0x3f;
			int lowerChanel = value & 0x3f;
			return new FrequencyUsage(upperChanel, lowerChanel);
		}

		public static int build(FrequencyUsage value) {
			return (value.upperChanel << 8) + value.lowerChanel;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			FrequencyUsage that = (FrequencyUsage) o;
			return upperChanel == that.upperChanel && lowerChanel == that.lowerChanel;
		}

		@Override
		public int hashCode() {
			return Objects.hash(upperChanel, lowerChanel);
		}
	}

	@Override
	public boolean updateState(String name, String value) {
		RfInterface stored = SerializationUtils.clone(this);
		switch (name) {
			case AIR_INTERFACE_ANTENNA_UHF_NO1_OUTPUT_POWER: {
				rfPowerAnt1 = Level.valueOf("WATT_" + value.replace('.', '_'));
				break;
			}
			case AIR_INTERFACE_REGION_UHF_REGULATION: {
				region = Region.valueOf(value.toUpperCase(Locale.getDefault()));
				break;
			}
			case TRANSPONDER_DRIVER_UHF_EPC_CLASS1_GEN2: {
				if (Boolean.parseBoolean(value)) {
					drivers.add(TransponderType.EPC_CLASS1_GEN2);
				} else {
					drivers.remove(TransponderType.EPC_CLASS1_GEN2);
				}
				break;
			}
		}
		return !equals(stored);
	}

	@Override
	public Map<String, String> getStates() {
		Map<String, String> result = new HashMap<>();
		result.put(TRANSPONDER_DRIVER_UHF_EPC_CLASS1_GEN2,
				Boolean.toString(drivers.contains(TransponderType.EPC_CLASS1_GEN2)));
		result.put(AIR_INTERFACE_ANTENNA_UHF_NO1_OUTPUT_POWER, rfPowerAnt1.name());
		result.put(AIR_INTERFACE_REGION_UHF_REGULATION, region.name());
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
		RfInterface that = (RfInterface) o;
		return numOfPreferredChannels == that.numOfPreferredChannels && preferredChannels == that.preferredChannels &&
				Objects.equals(drivers, that.drivers) && rfPowerAnt1 == that.rfPowerAnt1 && region == that.region &&
				Objects.equals(dcPower, that.dcPower) && Objects.equals(frequencyUsage, that.frequencyUsage);
	}

	@Override
	public int hashCode() {

		return Objects.hash(drivers, rfPowerAnt1, region, dcPower, frequencyUsage, numOfPreferredChannels,
				preferredChannels);
	}
}

package com.arrow.selene.device.harting.rfid.config;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.SerializationUtils;

public class AntennaMultiplexing implements ConfigParameter<AntennaMultiplexing> {
	private static final long serialVersionUID = -546840793500696691L;

	private static final int ID = 15;

	private static final String AIR_INTERFACE_MULTIPLEXER_ENABLE = "AirInterface_Multiplexer_Enable";
	private static final String AIR_INTERFACE_MULTIPLEXER_UHF_INTERNAL_SELECTED_ANTENNAS =
			"AirInterface_Multiplexer_UHF_Internal_SelectedAntennas";
	private static final String AIR_INTERFACE_MULTIPLEXER_UHF_EXTERNAL_OUTPUT_NO1 =
			"AirInterface_Multiplexer_UHF_External_Output_No1";
	private static final String AIR_INTERFACE_MULTIPLEXER_UHF_EXTERNAL_OUTPUT_NO2 =
			"AirInterface_Multiplexer_UHF_External_Output_No2";
	private static final String AIR_INTERFACE_MULTIPLEXER_UHF_EXTERNAL_OUTPUT_NO3 =
			"AirInterface_Multiplexer_UHF_External_Output_No3";
	private static final String AIR_INTERFACE_MULTIPLEXER_UHF_EXTERNAL_OUTPUT_NO4 =
			"AirInterface_Multiplexer_UHF_External_Output_No4";
	private boolean multiplexing;
	private Set<AntennaOutInt> antennaOut;
	private Set<AntennaOutExt> antennaOutExt1;
	private Set<AntennaOutExt> antennaOutExt2;
	private Set<AntennaOutExt> antennaOutExt3;
	private Set<AntennaOutExt> antennaOutExt4;

	private static final List<String> ALL = new ArrayList<>();

	static {
		ALL.add(AIR_INTERFACE_MULTIPLEXER_ENABLE);
		ALL.add(AIR_INTERFACE_MULTIPLEXER_UHF_INTERNAL_SELECTED_ANTENNAS);
		ALL.add(AIR_INTERFACE_MULTIPLEXER_UHF_EXTERNAL_OUTPUT_NO1);
		ALL.add(AIR_INTERFACE_MULTIPLEXER_UHF_EXTERNAL_OUTPUT_NO2);
		ALL.add(AIR_INTERFACE_MULTIPLEXER_UHF_EXTERNAL_OUTPUT_NO3);
		ALL.add(AIR_INTERFACE_MULTIPLEXER_UHF_EXTERNAL_OUTPUT_NO4);
	}

	public AntennaMultiplexing withMultiplexing(boolean multiplexing) {
		this.multiplexing = multiplexing;
		return this;
	}

	public AntennaMultiplexing withAntennaOut(Set<AntennaOutInt> antennaOut) {
		this.antennaOut = antennaOut;
		return this;
	}

	public AntennaMultiplexing withAntennaOutExt1(Set<AntennaOutExt> antennaOutExt1) {
		this.antennaOutExt1 = antennaOutExt1;
		return this;
	}

	public AntennaMultiplexing withAntennaOutExt2(Set<AntennaOutExt> antennaOutExt2) {
		this.antennaOutExt2 = antennaOutExt2;
		return this;
	}

	public AntennaMultiplexing withAntennaOutExt3(Set<AntennaOutExt> antennaOutExt3) {
		this.antennaOutExt3 = antennaOutExt3;
		return this;
	}

	public AntennaMultiplexing withAntennaOutExt4(Set<AntennaOutExt> antennaOutExt4) {
		this.antennaOutExt4 = antennaOutExt4;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.put(0, (byte) (multiplexing ? 1 : 0));
		buffer.put(1, (byte) AntennaOutInt.build(antennaOut));
		buffer.put(10, (byte) AntennaOutExt.build(antennaOutExt1));
		buffer.put(11, (byte) AntennaOutExt.build(antennaOutExt2));
		buffer.put(12, (byte) AntennaOutExt.build(antennaOutExt3));
		buffer.put(13, (byte) AntennaOutExt.build(antennaOutExt4));
		return buffer.array();
	}

	@Override
	public AntennaMultiplexing parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		boolean multiplexing = buffer.get(0) == 1;
		Set<AntennaOutInt> antennaOut = AntennaOutInt.extract(Byte.toUnsignedInt(buffer.get(1)) >> 3);
		Set<AntennaOutExt> antennaOutExt1 = AntennaOutExt.extract(buffer.get(10));
		Set<AntennaOutExt> antennaOutExt2 = AntennaOutExt.extract(buffer.get(11));
		Set<AntennaOutExt> antennaOutExt3 = AntennaOutExt.extract(buffer.get(12));
		Set<AntennaOutExt> antennaOutExt4 = AntennaOutExt.extract(buffer.get(13));
		return new AntennaMultiplexing().withMultiplexing(multiplexing).withAntennaOut(antennaOut).withAntennaOutExt1(
				antennaOutExt1).withAntennaOutExt2(antennaOutExt2).withAntennaOutExt3(antennaOutExt3)
				.withAntennaOutExt4(antennaOutExt4);
	}

	@Override
	public int getId() {
		return ID;
	}

	public enum AntennaOutInt {
		ANT1,
		ANT2,
		ANT3,
		ANT4;

		public static Set<AntennaOutInt> extract(int value) {
			Set<AntennaOutInt> result = EnumSet.noneOf(AntennaOutInt.class);
			for (AntennaOutInt item : values()) {
				if ((value >> item.ordinal() & 0x01) == 1) {
					result.add(item);
				}
			}
			return result;
		}

		public static int build(Iterable<AntennaOutInt> value) {
			int result = 0;
			for (AntennaOutInt item : value) {
				result |= 1 << item.ordinal();
			}
			return result;
		}
	}

	public enum AntennaOutExt {
		ANT1,
		ANT2,
		ANT3,
		ANT4,
		ANT5,
		ANT6,
		ANT7,
		ANT8;

		public static Set<AntennaOutExt> extract(int value) {
			Set<AntennaOutExt> result = EnumSet.noneOf(AntennaOutExt.class);
			for (AntennaOutExt item : values()) {
				if ((value >> item.ordinal() & 0x01) == 1) {
					result.add(item);
				}
			}
			return result;
		}

		public static int build(Iterable<AntennaOutExt> value) {
			int result = 0;
			for (AntennaOutExt item : value) {
				result |= 1 << item.ordinal();
			}
			return result;
		}
	}

	@Override
	public boolean updateState(String name, String value) {
		AntennaMultiplexing stored = SerializationUtils.clone(this);
		switch (name) {
			case AIR_INTERFACE_MULTIPLEXER_ENABLE: {
				multiplexing = Boolean.parseBoolean(value);
				break;
			}
			case AIR_INTERFACE_MULTIPLEXER_UHF_INTERNAL_SELECTED_ANTENNAS: {
				antennaOut = Arrays.stream(value.split(",")).map(
						s -> AntennaOutInt.valueOf(s.toUpperCase(Locale.getDefault()))).collect(Collectors.toSet());
				break;
			}
			case AIR_INTERFACE_MULTIPLEXER_UHF_EXTERNAL_OUTPUT_NO1: {
				antennaOutExt1 = Arrays.stream(value.split(",")).map(
						s -> AntennaOutExt.valueOf(s.toUpperCase(Locale.getDefault()))).collect(Collectors.toSet());
				break;
			}
			case AIR_INTERFACE_MULTIPLEXER_UHF_EXTERNAL_OUTPUT_NO2: {
				antennaOutExt2 = Arrays.stream(value.split(",")).map(
						s -> AntennaOutExt.valueOf(s.toUpperCase(Locale.getDefault()))).collect(Collectors.toSet());
				break;
			}
			case AIR_INTERFACE_MULTIPLEXER_UHF_EXTERNAL_OUTPUT_NO3: {
				antennaOutExt3 = Arrays.stream(value.split(",")).map(
						s -> AntennaOutExt.valueOf(s.toUpperCase(Locale.getDefault()))).collect(Collectors.toSet());
				break;
			}
			case AIR_INTERFACE_MULTIPLEXER_UHF_EXTERNAL_OUTPUT_NO4: {
				antennaOutExt4 = Arrays.stream(value.split(",")).map(
						s -> AntennaOutExt.valueOf(s.toUpperCase(Locale.getDefault()))).collect(Collectors.toSet());
				break;
			}
		}
		return !equals(stored);
	}

	@Override
	public Map<String, String> getStates() {
		Map<String, String> result = new HashMap<>();
		result.put(AIR_INTERFACE_MULTIPLEXER_ENABLE, Boolean.toString(multiplexing));
		result.put(AIR_INTERFACE_MULTIPLEXER_UHF_INTERNAL_SELECTED_ANTENNAS,
				String.join(",", antennaOut.stream().map(Enum::name).collect(Collectors.toSet())));
		result.put(AIR_INTERFACE_MULTIPLEXER_UHF_EXTERNAL_OUTPUT_NO1,
				String.join(",", antennaOutExt1.stream().map(Enum::name).collect(Collectors.toSet())));
		result.put(AIR_INTERFACE_MULTIPLEXER_UHF_EXTERNAL_OUTPUT_NO2,
				String.join(",", antennaOutExt2.stream().map(Enum::name).collect(Collectors.toSet())));
		result.put(AIR_INTERFACE_MULTIPLEXER_UHF_EXTERNAL_OUTPUT_NO3,
				String.join(",", antennaOutExt3.stream().map(Enum::name).collect(Collectors.toSet())));
		result.put(AIR_INTERFACE_MULTIPLEXER_UHF_EXTERNAL_OUTPUT_NO4,
				String.join(",", antennaOutExt4.stream().map(Enum::name).collect(Collectors.toSet())));
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
		AntennaMultiplexing that = (AntennaMultiplexing) o;
		return multiplexing == that.multiplexing && Objects.equals(antennaOut, that.antennaOut) && Objects.equals(
				antennaOutExt1, that.antennaOutExt1) && Objects.equals(antennaOutExt2, that.antennaOutExt2) &&
				Objects.equals(antennaOutExt3, that.antennaOutExt3) && Objects.equals(antennaOutExt4,
				that.antennaOutExt4);
	}

	@Override
	public int hashCode() {
		return Objects.hash(multiplexing, antennaOut, antennaOutExt1, antennaOutExt2, antennaOutExt3, antennaOutExt4);
	}
}

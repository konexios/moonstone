package com.arrow.selene.device.harting.rfid.config;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TransponderParameters implements ConfigParameter<TransponderParameters> {
	private static final long serialVersionUID = 8228467363402160685L;

	private static final int ID = 4;

	private IdDataInterpretation idDataInterpretation;
	private TidLength tidLength;

	private static final List<String> ALL = new ArrayList<>();

	public IdDataInterpretation getIdDataInterpretation() {
		return idDataInterpretation;
	}

	public TransponderParameters withIdDataInterpretation(IdDataInterpretation idDataInterpretation) {
		this.idDataInterpretation = idDataInterpretation;
		return this;
	}

	public TidLength getTidLength() {
		return tidLength;
	}

	public TransponderParameters withTidLength(TidLength tidLength) {
		this.tidLength = tidLength;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.put(12, (byte) IdDataInterpretation.build(idDataInterpretation));
		buffer.put(13, (byte) TidLength.build(tidLength));
		return buffer.array();
	}

	@Override
	public TransponderParameters parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		IdDataInterpretation idDataInterpretation = IdDataInterpretation.extract(Byte.toUnsignedInt(buffer.get(12)));
		TidLength tidLength = TidLength.extract(Byte.toUnsignedInt(buffer.get(13)));
		return new TransponderParameters().withIdDataInterpretation(idDataInterpretation).withTidLength(tidLength);
	}

	@Override
	public int getId() {
		return ID;
	}

	enum IdDataInterpretation {
		AUTOMATIC(0x00),
		IP_AND_TID(0x02);

		private final int value;

		IdDataInterpretation(int value) {
			this.value = value;
		}

		public static IdDataInterpretation extract(int value) {
			for (IdDataInterpretation item : values()) {
				if (value == item.value) {
					return item;
				}
			}
			return null;
		}

		public static int build(IdDataInterpretation value) {
			return value.value;
		}
	}

	enum TidLength {
		AUTOMATIC(0x00),
		LENGTH_32_BITS(0x20),
		LENGTH_64_BITS(0x40),
		LENGTH_96_BITS(0x60);

		private final int value;

		TidLength(int value) {
			this.value = value;
		}

		public static TidLength extract(int value) {
			for (TidLength item : values()) {
				if (value == item.value) {
					return item;
				}
			}
			return null;
		}

		public static int build(TidLength value) {
			return value.value;
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
		TransponderParameters that = (TransponderParameters) o;
		return idDataInterpretation == that.idDataInterpretation && tidLength == that.tidLength;
	}

	@Override
	public int hashCode() {

		return Objects.hash(idDataInterpretation, tidLength);
	}
}

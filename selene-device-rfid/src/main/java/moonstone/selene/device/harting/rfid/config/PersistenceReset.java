package moonstone.selene.device.harting.rfid.config;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PersistenceReset implements ConfigParameter<PersistenceReset> {
	private static final long serialVersionUID = 8447188951078864668L;

	private static final int ID = 16;

	private PersistenceAntennaMode persistenceAntennaMode;
	private int persistenceResetTime1;
	private int persistenceResetTime2;
	private int persistenceResetTime3;
	private int persistenceResetTime4;

	private static final List<String> ALL = new ArrayList<>();

	public PersistenceAntennaMode getPersistenceAntennaMode() {
		return persistenceAntennaMode;
	}

	public PersistenceReset withPersistenceAntennaMode(PersistenceAntennaMode persistenceAntennaMode) {
		this.persistenceAntennaMode = persistenceAntennaMode;
		return this;
	}

	public int getPersistenceResetTime1() {
		return persistenceResetTime1;
	}

	public PersistenceReset withPersistenceResetTime1(int persistenceResetTime1) {
		this.persistenceResetTime1 = persistenceResetTime1;
		return this;
	}

	public int getPersistenceResetTime2() {
		return persistenceResetTime2;
	}

	public PersistenceReset withPersistenceResetTime2(int persistenceResetTime2) {
		this.persistenceResetTime2 = persistenceResetTime2;
		return this;
	}

	public int getPersistenceResetTime3() {
		return persistenceResetTime3;
	}

	public PersistenceReset withPersistenceResetTime3(int persistenceResetTime3) {
		this.persistenceResetTime3 = persistenceResetTime3;
		return this;
	}

	public int getPersistenceResetTime4() {
		return persistenceResetTime4;
	}

	public PersistenceReset withPersistenceResetTime4(int persistenceResetTime4) {
		this.persistenceResetTime4 = persistenceResetTime4;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.put(0, (byte) PersistenceAntennaMode.build(persistenceAntennaMode));
		buffer.putShort(2, (short) persistenceResetTime1);
		buffer.putShort(4, (short) persistenceResetTime2);
		buffer.putShort(6, (short) persistenceResetTime3);
		buffer.putShort(8, (short) persistenceResetTime4);
		return buffer.array();
	}

	@Override
	public PersistenceReset parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		PersistenceAntennaMode persistenceAntennaMode = PersistenceAntennaMode.extract(
				Byte.toUnsignedInt(buffer.get(0)));
		int persistenceResetTime1 = Short.toUnsignedInt(buffer.getShort(2));
		int persistenceResetTime2 = Short.toUnsignedInt(buffer.getShort(4));
		int persistenceResetTime3 = Short.toUnsignedInt(buffer.getShort(6));
		int persistenceResetTime4 = Short.toUnsignedInt(buffer.getShort(8));
		return new PersistenceReset().withPersistenceAntennaMode(persistenceAntennaMode).withPersistenceResetTime1(
				persistenceResetTime1).withPersistenceResetTime2(persistenceResetTime2).withPersistenceResetTime3(
				persistenceResetTime3).withPersistenceResetTime4(persistenceResetTime4);
	}

	@Override
	public int getId() {
		return ID;
	}

	enum PersistenceAntennaMode {
		ALL_PORTS_ACT_AS_ONE_READING_POINT,
		EACH_PORT_ACTS_AS_READING_POINT;

		public static int build(PersistenceAntennaMode value) {
			return value.ordinal();
		}

		public static PersistenceAntennaMode extract(int value) {
			for (PersistenceAntennaMode item : values()) {
				if (item.ordinal() == value) {
					return item;
				}
			}
			return null;
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
		PersistenceReset that = (PersistenceReset) o;
		return persistenceResetTime1 == that.persistenceResetTime1 &&
				persistenceResetTime2 == that.persistenceResetTime2 &&
				persistenceResetTime3 == that.persistenceResetTime3 &&
				persistenceResetTime4 == that.persistenceResetTime4 &&
				persistenceAntennaMode == that.persistenceAntennaMode;
	}

	@Override
	public int hashCode() {

		return Objects.hash(persistenceAntennaMode, persistenceResetTime1, persistenceResetTime2,
				persistenceResetTime3,
				persistenceResetTime4);
	}
}

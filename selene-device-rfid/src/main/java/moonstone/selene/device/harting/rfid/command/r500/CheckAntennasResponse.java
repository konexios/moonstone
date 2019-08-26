package moonstone.selene.device.harting.rfid.command.r500;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.harting.rfid.command.Response;

public class CheckAntennasResponse implements Response<CheckAntennasResponse> {
	private static final long serialVersionUID = 4678184177146122964L;
	private int status;
	private Set<AntennaOutExternal> antennaOutExternals;
	private Set<AntennaOutExternal> antennaOutExternals1;
	private Set<AntennaOutExternal> antennaOutExternals2;
	private Set<AntennaOutExternal> antennaOutExternals3;
	private Set<AntennaOutExternal> antennaOutExternals4;

	public CheckAntennasResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public CheckAntennasResponse withAntennaOutExternals(Set<AntennaOutExternal> antennaOutExternals) {
		this.antennaOutExternals = antennaOutExternals;
		return this;
	}

	public CheckAntennasResponse withAntennaOutExternals1(Set<AntennaOutExternal> antennaOutExternals1) {
		this.antennaOutExternals1 = antennaOutExternals1;
		return this;
	}

	public CheckAntennasResponse withAntennaOutExternals2(Set<AntennaOutExternal> antennaOutExternals2) {
		this.antennaOutExternals2 = antennaOutExternals2;
		return this;
	}

	public CheckAntennasResponse withAntennaOutExternals3(Set<AntennaOutExternal> antennaOutExternals3) {
		this.antennaOutExternals3 = antennaOutExternals3;
		return this;
	}

	public CheckAntennasResponse withAntennaOutExternals4(Set<AntennaOutExternal> antennaOutExternals4) {
		this.antennaOutExternals4 = antennaOutExternals4;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public Set<AntennaOutExternal> getAntennaOutExternals() {
		return antennaOutExternals;
	}

	public Set<AntennaOutExternal> getAntennaOutExternals1() {
		return antennaOutExternals1;
	}

	public Set<AntennaOutExternal> getAntennaOutExternals2() {
		return antennaOutExternals2;
	}

	public Set<AntennaOutExternal> getAntennaOutExternals3() {
		return antennaOutExternals3;
	}

	public Set<AntennaOutExternal> getAntennaOutExternals4() {
		return antennaOutExternals4;
	}

	public CheckAntennasResponse parse(int mode, byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		int status = Byte.toUnsignedInt(buffer.get(2));
		Set<AntennaOutExternal> antennaOutExternals = AntennaOutExternal.extract(buffer.get(3));
		Set<AntennaOutExternal> antennaOutExternals1 = AntennaOutExternal.extract(buffer.get(4));
		Set<AntennaOutExternal> antennaOutExternals2 = AntennaOutExternal.extract(buffer.get(5));
		Set<AntennaOutExternal> antennaOutExternals3 = AntennaOutExternal.extract(buffer.get(6));
		Set<AntennaOutExternal> antennaOutExternals4 = AntennaOutExternal.extract(buffer.get(7));
		return new CheckAntennasResponse().withStatus(status).withAntennaOutExternals(antennaOutExternals)
				.withAntennaOutExternals1(antennaOutExternals1).withAntennaOutExternals2(antennaOutExternals2)
				.withAntennaOutExternals3(antennaOutExternals3).withAntennaOutExternals4(antennaOutExternals4);
	}

	enum AntennaOutExternal {
		ANT1,
		ANT2,
		ANT3,
		ANT4,
		ANT5,
		ANT6,
		ANT7,
		ANT8;

		public static Set<AntennaOutExternal> extract(int value) {
			Set<AntennaOutExternal> result = EnumSet.noneOf(AntennaOutExternal.class);
			for (AntennaOutExternal item : values()) {
				if ((value >> item.ordinal() & 0x01) == 1) {
					result.add(item);
				}
			}
			return result;
		}

		public static int build(Iterable<AntennaOutExternal> value) {
			int result = 0;
			for (AntennaOutExternal item : value) {
				result |= 1 << item.ordinal();
			}
			return result;
		}
	}
}

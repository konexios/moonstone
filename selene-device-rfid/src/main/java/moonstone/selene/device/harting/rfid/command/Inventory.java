package moonstone.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

public class Inventory extends HostCommand {
	private static final long serialVersionUID = 1558127417458322629L;
	private static final int ID = 0x01;
	private Mode mode;
	private Set<AntennaSelector> antennaSelector;

	@Override
	protected byte[] buildPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(getLength());
		buffer.put(0, (byte) ID);
		buffer.put(1, (byte) Mode.build(mode));
		if (mode.antenna == Antenna.REQUEST_WITH_ANTENNA_NUMBER) {
			buffer.put(2, (byte) AntennaSelector.build(antennaSelector));
		}
		return buffer.array();
	}

	@Override
	protected int getLength() {
		return mode.antenna == Antenna.REQUEST_WITHOUT_ANTENNA_NUMBER ? 2 : 3;
	}

	@Override
	public int getCommandMode() {
		return mode.antenna == Antenna.REQUEST_WITH_ANTENNA_NUMBER ? 1 : 0;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public Set<AntennaSelector> getAntennaSelector() {
		return antennaSelector;
	}

	public void setAntennaSelector(Set<AntennaSelector> antennaSelector) {
		this.antennaSelector = antennaSelector;
	}

	public Inventory withMode(Mode mode) {
		this.mode = mode;
		return this;
	}

	public Inventory withAntennaSelector(Set<AntennaSelector> antennaSelector) {
		this.antennaSelector = antennaSelector;
		return this;
	}

	public static class Mode {
		private More more;
		private Antenna antenna;

		public More getMore() {
			return more;
		}

		public void setMore(More more) {
			this.more = more;
		}

		public Antenna getAntenna() {
			return antenna;
		}

		public void setAntenna(Antenna antenna) {
			this.antenna = antenna;
		}

		public Mode withMore(More more) {
			this.more = more;
			return this;
		}

		public Mode withAntenna(Antenna antenna) {
			this.antenna = antenna;
			return this;
		}

		public static int build(Mode value) {
			int result = 0;
			result |= More.build(value.more) << 7;
			result |= Antenna.build(value.antenna) << 4;
			return result;
		}
	}

	public enum More {
		NEW_INVENTORY_REQUESTED,
		MORE_DATA_REQUESTED;

		public static int build(More value) {
			return value.ordinal();
		}
	}

	public enum Antenna {
		REQUEST_WITHOUT_ANTENNA_NUMBER,
		REQUEST_WITH_ANTENNA_NUMBER;

		public static int build(Antenna value) {
			return value.ordinal();
		}
	}

	public enum AntennaSelector {
		ANT1,
		ANT2,
		ANT3,
		ANT4;

		public static Set<AntennaSelector> extract(int value) {
			Set<AntennaSelector> result = EnumSet.noneOf(AntennaSelector.class);
			for (AntennaSelector item : values()) {
				if ((value >> item.ordinal() & 0x01) == 1) {
					result.add(item);
				}
			}
			return result;
		}

		public static int build(Iterable<AntennaSelector> value) {
			int result = 0;
			for (AntennaSelector item : value) {
				result |= 1 << item.ordinal();
			}
			return result;
		}
	}
}

package moonstone.selene.device.xbee.zcl.domain.se.price.data;

import java.util.EnumSet;
import java.util.Set;

public enum BlockPeriodControl {
	PRICE_ACKNOWLEDGEMENT_NOT_REQUIRED((byte) 0, false),
	PRICE_ACKNOWLEDGEMENT_REQUIRED((byte) 0, true),
	NON_REPEATING_BLOCK((byte) 1, false),
	REPEATING_BLOCK((byte) 1, true);

	private final byte bit;
	private final boolean value;

	BlockPeriodControl(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<BlockPeriodControl> getByValue(byte value) {
		Set<BlockPeriodControl> result = EnumSet.noneOf(BlockPeriodControl.class);
		for (BlockPeriodControl item : values()) {
			if (((value >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	public static int getValue(Set<BlockPeriodControl> items) {
		int result = 0;
		for (BlockPeriodControl item : items) {
			if (item.value) {
				result |= 1 << item.bit;
			}
		}
		return result;
	}
}

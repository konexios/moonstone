package moonstone.selene.device.xbee.zcl.domain.se.price.data;

import java.util.EnumSet;
import java.util.Set;

public enum BlockThresholdControl {
	SPECIFIC_TOU_TIER((byte) 0, false),
	ALL_TOU_TIERS_OR_BLOCK_ONLY_CHARGING ((byte) 0, true);

	private final byte bit;
	private final boolean value;

	BlockThresholdControl(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<BlockThresholdControl> getByValue(byte value) {
		Set<BlockThresholdControl> result = EnumSet.noneOf(BlockThresholdControl.class);
		for (BlockThresholdControl item : values()) {
			if (((value >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	public static int getValue(Set<BlockThresholdControl> items) {
		int result = 0;
		for (BlockThresholdControl item : items) {
			if (item.value) {
				result |= 1 << item.bit;
			}
		}
		return result;
	}
}

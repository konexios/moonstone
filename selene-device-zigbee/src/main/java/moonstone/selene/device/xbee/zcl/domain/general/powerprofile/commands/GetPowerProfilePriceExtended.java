package moonstone.selene.device.xbee.zcl.domain.general.powerprofile.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.EnumSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.general.powerprofile.data.Options;

public class GetPowerProfilePriceExtended
		extends ClusterSpecificCommand<GetPowerProfilePriceExtended> {
	private Set<Options> options;
	private int powerProfileId;
	private int powerProfileStartTime;

	public Set<Options> getOptions() {
		return options;
	}

	public GetPowerProfilePriceExtended withOptions(Set<Options> options) {
		this.options = options;
		return this;
	}

	public int getPowerProfileId() {
		return powerProfileId;
	}

	public GetPowerProfilePriceExtended withPowerProfileId(int powerProfileId) {
		this.powerProfileId = powerProfileId;
		return this;
	}

	public int getPowerProfileStartTime() {
		return powerProfileStartTime;
	}

	public GetPowerProfilePriceExtended withPowerProfileStartTime(int powerProfileStartTime) {
		this.powerProfileStartTime = powerProfileStartTime;
		return this;
	}

	@Override
	protected int getId() {
		return PowerProfileClusterCommands.GET_POWER_PROFILE_PRICE_EXTENDED_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(options.contains(Options.POWER_PROFILE_START_TIME_PRESENT) ? 4 : 2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		int options = 0;
		for (Options option : this.options) {
			options |= 1 << option.ordinal();
		}
		buffer.put((byte) options);
		buffer.put((byte) powerProfileId);
		if(this.options.contains(Options.POWER_PROFILE_START_TIME_PRESENT)) {
			buffer.putShort((short) powerProfileStartTime);
		}
		return buffer.array();
	}

	@Override
	public GetPowerProfilePriceExtended fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2 || payload.length == 4, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		byte optionsValue = buffer.get();
		options = EnumSet.noneOf(Options.class);
		for (Options item : Options.values()) {
			if((optionsValue >> item.ordinal() & 0x01) == 1) {
				options.add(item);
			}
		}
		powerProfileId = Byte.toUnsignedInt(buffer.get());
		if(this.options.contains(Options.POWER_PROFILE_START_TIME_PRESENT)) {
			powerProfileStartTime = Short.toUnsignedInt(buffer.getShort());
		}
		return this;
	}
}

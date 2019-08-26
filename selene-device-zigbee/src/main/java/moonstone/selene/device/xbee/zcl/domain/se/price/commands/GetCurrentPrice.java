package moonstone.selene.device.xbee.zcl.domain.se.price.commands;

import java.util.Set;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.CommandOptions;

public class GetCurrentPrice extends ClusterSpecificCommand<GetCurrentPrice> {
	private Set<CommandOptions> commandOptions;

	public Set<CommandOptions> getCommandOptions() {
		return commandOptions;
	}

	public GetCurrentPrice withCommandOptions(Set<CommandOptions> commandOptions) {
		this.commandOptions = commandOptions;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.GET_CURRENT_PRICE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		int value = 0;
		for (CommandOptions item : commandOptions) {
			value |= 1 << item.ordinal();
		}
		return new byte[]{(byte) value};
	}

	@Override
	public GetCurrentPrice fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 1, "payload length is incorrect");
		commandOptions = CommandOptions.getByValue(payload[0]);
		return this;
	}
}

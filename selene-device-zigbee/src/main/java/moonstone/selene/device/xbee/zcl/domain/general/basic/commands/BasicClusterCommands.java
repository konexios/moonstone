package moonstone.selene.device.xbee.zcl.domain.general.basic.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class BasicClusterCommands {
	public static final int RESET_TO_FACTORY_DEFAULTS_COMMAND_ID = 0x00;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_RECEIVED.put(RESET_TO_FACTORY_DEFAULTS_COMMAND_ID,
				new ImmutablePair<>("Reset To Factory Defaults", ResetToFactoryDefaults.class));
	}
}

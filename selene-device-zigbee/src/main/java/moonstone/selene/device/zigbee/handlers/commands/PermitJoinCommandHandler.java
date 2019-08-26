package moonstone.selene.device.zigbee.handlers.commands;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.digi.xbee.api.models.XBee64BitAddress;

import moonstone.acs.AcsSystemException;
import moonstone.acs.JsonUtils;
import moonstone.selene.device.xbee.zdo.PermitJoinRequest;
import moonstone.selene.device.xbee.zdo.ZdoCommands;
import moonstone.selene.device.zigbee.ZigBeeCoordinatorModule;
import moonstone.selene.device.zigbee.data.PermitJoinPayload;

public class PermitJoinCommandHandler implements CommandHandler {
	public static final String COMMAND = "permitJoin";
	private static final String PAYLOAD = "{\"duration\":\"<number> [0-255]\"}";
	private static final Pattern SPACES = Pattern.compile(" ", Pattern.LITERAL);
	private static final int MAX_PERMIT_DURATIONS_SEC = 0xFF;
	public static final int DEFAULT_DURATION = 10;

	private ZigBeeCoordinatorModule module;

	public PermitJoinCommandHandler(ZigBeeCoordinatorModule module) {
		this.module = module;
	}

	@Override
	public void handle(String command, String payload) {
		String method = "PermitJoinCommandHandler.handle";
		int duration = DEFAULT_DURATION;
		if (!Objects.equals(SPACES.matcher(payload).replaceAll(Matcher.quoteReplacement("")), "{}")) {
			try {
				duration = JsonUtils.fromJson(payload, PermitJoinPayload.class).getDuration();
			} catch (AcsSystemException e) {
				module.logWarn(method, "cannot permit join: expected numeric value [0-255]");
				return;
			}
		}
		if (duration > MAX_PERMIT_DURATIONS_SEC || duration < 0) {
			module.logInfo(method, "cannot permit join: expected numeric value [0-255], received %d", duration);
		} else {
			if (duration == MAX_PERMIT_DURATIONS_SEC) {
				module.logWarn(method, "duration=255 passed; join will be always permitted");
			}
			module.logInfo(method, "permitting join for %d seconds", duration);
			module.sendZdoMessage(XBee64BitAddress.COORDINATOR_ADDRESS, ZdoCommands.PERMIT_JOIN_REQ,
			        PermitJoinRequest.toPayload(module.nextSequence(), duration));
		}
	}

	@Override
	public String getName() {
		return COMMAND;
	}

	@Override
	public String getPayload() {
		return PAYLOAD;
	}
}

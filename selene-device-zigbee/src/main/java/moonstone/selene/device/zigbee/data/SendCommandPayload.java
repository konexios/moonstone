package moonstone.selene.device.zigbee.data;

import java.io.Serializable;

import moonstone.selene.device.xbee.zcl.ApplicationProfiles;

public final class SendCommandPayload implements Serializable {
	private static final long serialVersionUID = 2356817709052351794L;

	private int dstEndpoint;
	private int profileId = ApplicationProfiles.HOME_AUTOMATION_PROFILE;
	private int clusterId;
	private int commandId;

	public int getDstEndpoint() {
		return dstEndpoint;
	}

	public int getProfileId() {
		return profileId;
	}

	public int getClusterId() {
		return clusterId;
	}

	public int getCommandId() {
		return commandId;
	}
}

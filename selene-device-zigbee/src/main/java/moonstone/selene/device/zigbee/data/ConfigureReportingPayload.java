package moonstone.selene.device.zigbee.data;

import java.io.Serializable;

import moonstone.selene.device.xbee.zcl.ApplicationProfiles;

public final class ConfigureReportingPayload implements Serializable {
	private static final long serialVersionUID = 6274101766041520345L;

	private int attributeId;
	private int min;
	private int max;
	private int endpoint;
	private int clusterId;
	private int profileId = ApplicationProfiles.HOME_AUTOMATION_PROFILE;

	public int getAttributeId() {
		return attributeId;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public int getEndpoint() {
		return endpoint;
	}

	public int getClusterId() {
		return clusterId;
	}

	public int getProfileId() {
		return profileId;
	}
}

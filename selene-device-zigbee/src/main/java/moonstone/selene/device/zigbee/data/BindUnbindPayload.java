package moonstone.selene.device.zigbee.data;

import java.io.Serializable;

public final class BindUnbindPayload implements Serializable {
	private static final long serialVersionUID = 5123361112498441809L;

	private int srcEndpoint;
	private String dstUid;
	private Integer dstEndpoint;
	private int clusterId;

	public int getSrcEndpoint() {
		return srcEndpoint;
	}

	public String getDstUid() {
		return dstUid;
	}

	public Integer getDstEndpoint() {
		return dstEndpoint;
	}

	public int getClusterId() {
		return clusterId;
	}
}

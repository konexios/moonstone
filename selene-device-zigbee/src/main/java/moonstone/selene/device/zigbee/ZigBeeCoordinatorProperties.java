package moonstone.selene.device.zigbee;

public class ZigBeeCoordinatorProperties extends ZigBeePropertiesAbstract {
	private static final long serialVersionUID = 8398354336926410215L;

	private int networkDiscoveryInterval = 5000;
	private int healthCheckInterval = 5;
	private int pollingRetryInterval = 1000;
	private boolean discovering = true;
	private boolean autoJoin = true;
	private String extendedPanId;
	private long messageSendingRetryInterval = 5000L;

	public String getExtendedPanId() {
		return extendedPanId;
	}

	public void setExtendedPanId(String extendedPanId) {
		this.extendedPanId = extendedPanId;
	}

	public int getNetworkDiscoveryInterval() {
		return networkDiscoveryInterval;
	}

	public void setNetworkDiscoveryInterval(int networkDiscoveryInterval) {
		this.networkDiscoveryInterval = networkDiscoveryInterval;
	}

	public int getHealthCheckInterval() {
		return healthCheckInterval;
	}

	public void setHealthCheckInterval(int healthCheckInterval) {
		this.healthCheckInterval = healthCheckInterval;
	}

	public int getPollingRetryInterval() {
		return pollingRetryInterval;
	}

	public void setPollingRetryInterval(int pollingRetryInterval) {
		this.pollingRetryInterval = pollingRetryInterval;
	}

	public boolean isDiscovering() {
		return discovering;
	}

	public void setDiscovering(boolean discovering) {
		this.discovering = discovering;
	}

	public boolean isAutoJoin() {
		return autoJoin;
	}

	public void setAutoJoin(boolean autoJoin) {
		this.autoJoin = autoJoin;
	}

	public long getMessageSendingRetryInterval() {
		return messageSendingRetryInterval;
	}

	public void setMessageSendingRetryInterval(long messageSendingRetryInterval) {
		this.messageSendingRetryInterval = messageSendingRetryInterval;
	}
}

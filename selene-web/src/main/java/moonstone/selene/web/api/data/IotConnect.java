package moonstone.selene.web.api.data;

import moonstone.selene.data.BaseEntity;

public class IotConnect extends BaseEntity {
	private static final long serialVersionUID = 8354949023579470350L;

	private String iotConnectMqtt;
	private String iotConnectMqttVHost;

	public String getIotConnectMqtt() {
		return iotConnectMqtt;
	}

	public void setIotConnectMqtt(String iotConnectMqtt) {
		this.iotConnectMqtt = iotConnectMqtt;
	}

	public String getIotConnectMqttVHost() {
		return iotConnectMqttVHost;
	}

	public void setIotConnectMqttVHost(String iotConnectMqttVHost) {
		this.iotConnectMqttVHost = iotConnectMqttVHost;
	}
}

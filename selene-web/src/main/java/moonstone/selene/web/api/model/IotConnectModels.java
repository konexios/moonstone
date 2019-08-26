package moonstone.selene.web.api.model;

import java.io.Serializable;
import java.time.Instant;

import moonstone.selene.web.api.data.IotConnect;

public class IotConnectModels {

	public static class IotConnectModel extends BaseModels.BaseEntity {
		private static final long serialVersionUID = 466604688373581312L;

		private String iotConnectMqtt;
		private String iotConnectMqttVHost;

		public IotConnectModel(IotConnect iotConnect) {
			super(iotConnect.getId(), iotConnect.isEnabled(), Instant.ofEpochMilli(iotConnect.getCreatedTs()),
					Instant.ofEpochMilli(iotConnect.getModifiedTs()));
			this.iotConnectMqtt = iotConnect.getIotConnectMqtt();
			this.iotConnectMqttVHost = iotConnect.getIotConnectMqttVHost();
		}

		public String getIotConnectMqtt() {
			return iotConnectMqtt;
		}

		public String getIotConnectMqttVHost() {
			return iotConnectMqttVHost;
		}
	}

	public static class IotConnectUpsert implements Serializable {
		private static final long serialVersionUID = -8391447392155963433L;

		private IotConnectModel iotConnect;

		public IotConnectUpsert(IotConnectModel iotConnect) {
			this.iotConnect = iotConnect;
		}

		public IotConnectModel getIotConnect() {
			return iotConnect;
		}
	}

}

package moonstone.selene.web.api.model;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import moonstone.acn.client.model.CloudPlatform;
import moonstone.selene.SeleneException;
import moonstone.selene.data.Gateway;

public class GatewayModels {

	public static class CloudPlatformOption extends BaseModels.EnumOption<CloudPlatform> {
		private static final long serialVersionUID = -2597911615392542056L;

		public CloudPlatformOption(CloudPlatform cloudPlatform) {
			super(cloudPlatform);
		}
	}

	public static class GatewayModel extends BaseModels.BaseEntity {
		private static final long serialVersionUID = -5410212832056537301L;

		private String hid;
		private String name;
		private String uid;
		private String iotConnectUrl;
		private String topology;
		private long heartBeatIntervalMs;
		private int purgeTelemetryIntervalDays;
		private int purgeMessagesIntervalDays;
		private CloudPlatform cloudPlatform;
		private Map<String, String> properties = new HashMap<>();
		private String apiKey;
		private String secretKey;

		public GatewayModel() {
			super(0, false, Instant.ofEpochMilli(0), Instant.ofEpochMilli(0));
		}

		public GatewayModel(Gateway gateway) {
			super(gateway.getId(), gateway.isEnabled(), Instant.ofEpochMilli(gateway.getCreatedTs()),
			        Instant.ofEpochMilli(gateway.getModifiedTs()));
			this.hid = gateway.getHid();
			this.name = gateway.getName();
			this.uid = gateway.getUid();
			this.iotConnectUrl = gateway.getIotConnectUrl();
			this.topology = gateway.getTopology();
			this.heartBeatIntervalMs = gateway.getHeartBeatIntervalMs();
			this.purgeTelemetryIntervalDays = gateway.getPurgeTelemetryIntervalDays();
			this.purgeMessagesIntervalDays = gateway.getPurgeMessagesIntervalDays();
			this.cloudPlatform = gateway.getCloudPlatform();
			this.apiKey = gateway.getApiKey();
			this.secretKey = gateway.getSecretKey();

			if (!StringUtils.isEmpty(gateway.getProperties())) {
				try {
					TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
					};
					JsonFactory factory = new JsonFactory();
					ObjectMapper mapper = new ObjectMapper(factory);
					this.properties = mapper.readValue(gateway.getProperties(), typeRef);
				} catch (JsonParseException e) {
					throw new SeleneException(e.getMessage());
				} catch (JsonMappingException e) {
					throw new SeleneException(e.getMessage());
				} catch (IOException e) {
					throw new SeleneException(e.getMessage());
				}
			}
		}

		public String getHid() {
			return hid;
		}

		public String getName() {
			return name;
		}

		public String getUid() {
			return uid;
		}

		public String getIotConnectUrl() {
			return iotConnectUrl;
		}

		public String getTopology() {
			return topology;
		}

		public long getHeartBeatIntervalMs() {
			return heartBeatIntervalMs;
		}

		public int getPurgeTelemetryIntervalDays() {
			return purgeTelemetryIntervalDays;
		}

		public int getPurgeMessagesIntervalDays() {
			return purgeMessagesIntervalDays;
		}

		public CloudPlatform getCloudPlatform() {
			return cloudPlatform;
		}

		public Map<String, String> getProperties() {
			return properties;
		}

		public String getApiKey() {
			return apiKey;
		}

		public String getSecretKey() {
			return secretKey;
		}
	}

	public static class GatewayUpsert implements Serializable {
		private static final long serialVersionUID = 2173986954555661203L;

		private GatewayModel gateway;
		private List<CloudPlatformOption> cloudPlatformOptions = new ArrayList<>();

		public GatewayUpsert(GatewayModel gateway, List<CloudPlatformOption> cloudPlatformOptions) {
			this.gateway = gateway;
			this.cloudPlatformOptions = cloudPlatformOptions;
		}

		public GatewayModel getGateway() {
			return gateway;
		}

		public List<CloudPlatformOption> getCloudPlatformOptions() {
			return cloudPlatformOptions;
		}
	}
}

package moonstone.selene.web.api.model;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import moonstone.selene.SeleneException;
import moonstone.selene.data.Device;
import moonstone.selene.service.DeviceService;
import moonstone.selene.service.TelemetryService;

public class DeviceModels {

	public static class DeviceList extends BaseModels.EntityAbstract {

		private static final long serialVersionUID = 6435926449868830104L;

		private String name;
		private String uid;
		private String hid;
		private String type;
		private boolean enabled;
		private String gateway;
		private String status;
		private long lasttelemetry;

		public DeviceList(Device device) {
			super(device.getId());
			this.name = device.getName();
			this.uid = device.getUid();
			this.type = device.getType();
			this.enabled = device.isEnabled();
			this.gateway = DeviceService.getInstance().find(device.getGatewayId()).getName();
			this.status = device.getStatus();
			this.hid = device.getHid();
			this.lasttelemetry = (long) fetchTimestamp(device);
		}

		public String getName() {
			return name;
		}

		public String getUid() {
			return uid;
		}

		public String getHid() {
			return hid;
		}

		public String getType() {
			return type;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public String getGateway() {
			return gateway;

		}

		public String getStatus() {
			return status;

		}

		public Long fetchTimestamp(Device device) {
			long times = 0;
			times = TelemetryService.getInstance().findLastTimestamp(device.getId());

			return times;
		}

		public long getTimestamp() {
			return lasttelemetry;

		}
	}

	public static class DeviceModel extends BaseModels.BaseEntity {
		private static final long serialVersionUID = 5426064633474386200L;

		private String hid;
		private String name;
		private String type;
		private String uid;
		private String userHid;
		private long gatewayId;
		private Map<String, String> properties = new HashMap<>();
		private Map<String, String> info = new HashMap<>();

		public DeviceModel(Device device) {
			super(device.getId(), device.isEnabled(), Instant.ofEpochMilli(device.getCreatedTs()),
			        Instant.ofEpochMilli(device.getModifiedTs()));
			this.hid = device.getHid();
			this.name = device.getName();
			this.type = device.getType();
			this.uid = device.getUid();
			this.userHid = device.getUserHid();
			this.gatewayId = device.getGatewayId();

			if (!StringUtils.isEmpty(device.getProperties())) {
				try {
					TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
					};
					JsonFactory factory = new JsonFactory();
					ObjectMapper mapper = new ObjectMapper(factory);
					this.properties = mapper.readValue(device.getProperties(), typeRef);
				} catch (JsonParseException e) {
					throw new SeleneException(e.getMessage());
				} catch (JsonMappingException e) {
					throw new SeleneException(e.getMessage());
				} catch (IOException e) {
					throw new SeleneException(e.getMessage());
				}
			}
			if (!StringUtils.isEmpty(device.getInfo())) {
				try {
					TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
					};
					JsonFactory factory = new JsonFactory();
					ObjectMapper mapper = new ObjectMapper(factory);
					this.info = mapper.readValue(device.getInfo(), typeRef);
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

		public String getType() {
			return type;
		}

		public String getUid() {
			return uid;
		}

		public String getUserHid() {
			return userHid;
		}

		public long getGatewayId() {
			return gatewayId;
		}

		public Map<String, String> getProperties() {
			return properties;
		}

		public Map<String, String> getInfo() {
			return info;
		}
	}

	public static class DeviceUpsert implements Serializable {
		private static final long serialVersionUID = -7909604277758040638L;

		private DeviceModel device;

		public DeviceUpsert(DeviceModel device) {
			this.device = device;
		}

		public DeviceModel getDevice() {
			return device;
		}
	}
}

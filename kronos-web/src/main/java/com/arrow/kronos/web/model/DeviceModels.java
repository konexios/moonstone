package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceStateValueMetadata;
import com.arrow.kronos.data.DeviceTelemetry;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.web.model.DeviceActionModels.DeviceActionModel;
import com.arrow.kronos.web.model.DeviceTypeModels.DeviceTelemetryModel;
import com.arrow.kronos.web.model.RheaModels.SoftwareReleaseOption;
import com.arrow.kronos.web.model.TelemetryItemModels.TelemetryItemModel;
import com.arrow.pegasus.data.event.Event;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;
import com.arrow.pegasus.webapi.data.LastLocationModel;

public class DeviceModels {

	public static class DeviceOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = 5779855747752727590L;
		private static final String UID_SEPARATOR = " - ";

		public DeviceOption() {
			super(null, null, null);
		}

		public DeviceOption(Device device) {
			super(device.getId(), device.getHid(), device.getName() + UID_SEPARATOR + device.getUid());
		}
	}

	public static class DeviceCore extends CoreDocumentModel {
		private static final long serialVersionUID = -4584140195275397987L;

		private String name;
		private String uid;
		private String deviceTypeName;
		private String ownerName;
		private String gatewayName;
		private boolean enabled;
		private Instant lastModifiedDate;

		public DeviceCore(Device device, DeviceType deviceType, String gatewayName, String ownerName) {
			super(device.getId(), device.getHid());
			this.name = device.getName();
			this.uid = device.getUid();
			this.deviceTypeName = deviceType != null ? deviceType.getName() : null;
			this.ownerName = ownerName;
			this.gatewayName = gatewayName;
			this.enabled = device.isEnabled();
			this.lastModifiedDate = device.getLastModifiedDate();
		}

		public String getName() {
			return name;
		}

		public String getUid() {
			return uid;
		}

		public String getDeviceTypeName() {
			return deviceTypeName;
		}

		public String getOwnerName() {
			return ownerName;
		}

		public String getGatewayName() {
			return gatewayName;
		}

		public boolean isEnabled() {
			return enabled;
		}
		
		public Instant getLastModifiedDate() {
			return lastModifiedDate;
		}

		public void setLastModifiedDate(Instant lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}
	}

	public static class DeviceTree extends DeviceCore {
		private static final long serialVersionUID = 4026128042800531142L;

		public boolean myDevice;
		public Long lastTelemetryDate;

		public DeviceTree(Device device, DeviceType deviceType, String ownerName, String gatewayName, boolean myDevice,
		        Long lastTelemetryDate) {
			super(device, deviceType, gatewayName, ownerName);

			this.myDevice = myDevice;
			this.lastTelemetryDate = lastTelemetryDate;
		}

		public boolean isMyDevice() {
			return myDevice;
		}

		public Long getLastTelemetryDate() {
			return lastTelemetryDate;
		}

	}

	public static class DeviceList extends DeviceCore {
		private static final long serialVersionUID = 322947644660764824L;

		private String nodeName;

		public DeviceList(Device device, DeviceType deviceType, String gatewayName, String ownerName, String nodeName) {
			super(device, deviceType, gatewayName, ownerName);
			this.nodeName = nodeName;
		}

		public String getNodeName() {
			return nodeName;
		}
	}

	// public static class NewDeviceModel extends CoreDocumentModel {
	// private static final long serialVersionUID = -4085140366067509654L;
	//
	// private String name;
	// private String deviceTypeId;
	// private String uid;
	// private boolean enabled;
	//
	// public NewDeviceModel() {
	// super(null, null);
	// }
	//
	// public NewDeviceModel(Device device, DeviceType deviceType) {
	// super(device.getId(), device.getHid());
	// this.name = device.getName();
	// this.deviceTypeId = device.getDeviceTypeId();
	// this.uid = device.getUid();
	// this.enabled = device.isEnabled();
	// }
	//
	// public String getName() {
	// return name;
	// }
	//
	// public String getDeviceTypeId() {
	// return deviceTypeId;
	// }
	//
	// public String getUid() {
	// return uid;
	// }
	//
	// public boolean isEnabled() {
	// return enabled;
	// }
	// }

	public static class DeviceModel extends CoreDocumentModel {
		private static final long serialVersionUID = -4085140366067509654L;

		private String name;
		private String deviceTypeId;
		private String uid;
		private boolean enabled;
		private String softwareName;
		private String softwareVersion;

		public DeviceModel() {
			super(null, null);
		}

		public DeviceModel(Device device) {
			super(device.getId(), device.getHid());
			this.name = device.getName();
			this.deviceTypeId = device.getDeviceTypeId();
			this.uid = device.getUid();
			this.enabled = device.isEnabled();
			this.softwareName = device.getSoftwareName();
			this.softwareVersion = device.getSoftwareVersion();
		}

		public String getName() {
			return name;
		}

		public String getDeviceTypeId() {
			return deviceTypeId;
		}

		public String getUid() {
			return uid;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public String getSoftwareName() {
			return softwareName;
		}

		public String getSoftwareVesrion() {
			return softwareVersion;
		}
	}

	public static class DeviceUpsert implements Serializable {
		private static final long serialVersionUID = -3761123079464851832L;

		private DeviceModel device;
		private List<DeviceTypeModels.DeviceTypeOption> deviceTypeOptions;

		public DeviceUpsert(DeviceModel device, List<DeviceTypeModels.DeviceTypeOption> deviceTypeOptions) {
			this.device = device;
			this.deviceTypeOptions = deviceTypeOptions;
		}

		public DeviceModel getDevice() {
			return device;
		}

		public List<DeviceTypeModels.DeviceTypeOption> getDeviceTypeOptions() {
			return deviceTypeOptions;
		}
	}

	public static class LastTelemetryModel implements Serializable {
		private static final long serialVersionUID = 8886016568126935232L;

		private String timestamp;
		private List<TelemetryItemModel> telemetryItems = new ArrayList<>();

		public LastTelemetryModel(String timestamp, List<TelemetryItemModel> telemetryItems) {
			this.timestamp = timestamp;
			if (telemetryItems != null)
				this.telemetryItems = telemetryItems;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public List<TelemetryItemModel> getTelemetryItems() {
			return telemetryItems;
		}
	}

	public static class DeviceTelemetryEventModel implements Serializable {
		private static final long serialVersionUID = 7674535746005728272L;

		private Long timestamp;
		private Long telemetryCount;
		private Long deviceEventCount;

		public DeviceTelemetryEventModel(long timestamp, long telemetryCount, long deviceEventCount) {
			this.timestamp = timestamp;
			this.telemetryCount = telemetryCount;
			this.deviceEventCount = deviceEventCount;
		}

		public Long getTimestamp() {
			return timestamp;
		}

		public Long getTelemetryCount() {
			return telemetryCount;
		}

		public void setTelemetryCount(Long telemetryCount) {
			this.telemetryCount = telemetryCount;
		}

		public Long getDeviceEventCount() {
			return deviceEventCount;
		}

		public void setDeviceEventCount(Long deviceEventCount) {
			this.deviceEventCount = deviceEventCount;
		}
	}

	public static class DeviceStatsModel implements Serializable {
		private static final long serialVersionUID = 5238391952002602680L;

		private long telemetryItemCount;
		private long deviceEventCount;
		private List<DeviceTelemetryEventModel> telemetryEventCounts;

		public DeviceStatsModel(long telemetryItemCount, long deviceEventCount,
		        List<DeviceTelemetryEventModel> telemetryEventCounts) {
			this.telemetryItemCount = telemetryItemCount;
			this.deviceEventCount = deviceEventCount;
			this.telemetryEventCounts = telemetryEventCounts;
		}

		public long getTelemetryItemCount() {
			return telemetryItemCount;
		}

		public long getDeviceEventCount() {
			return deviceEventCount;
		}

		public List<DeviceTelemetryEventModel> getTelemetryEventCounts() {
			return telemetryEventCounts;
		}
	}

	public static class DeviceSettingsModel extends DeviceModel {
		private static final long serialVersionUID = 2116378562237912917L;

		private UserModels.UserOption user = null;
		private NodeModels.NodeOption node = null;
		private Map<String, String> properties = new HashMap<>();
		private String propertyChangeEventId = null;
		private List<DeviceTelemetryModel> telemetries;
		private SoftwareReleaseOption softwareRelease = null;
		private Map<String, DeviceStateValueMetadata> stateMetadata;
		private boolean deviceTypeEditable = false;

		public DeviceSettingsModel() {
			super();
		}

		public DeviceSettingsModel(Device device, DeviceType deviceType, User user, Node node, Event event,
		        SoftwareReleaseOption softwareRelease) {
			super(device);

			if (user != null) {
				this.user = new UserModels.UserOption(user);
			}

			if (node != null) {
				this.node = new NodeModels.NodeOption(node);
			}

			// properties
			this.properties = device.getProperties();

			this.propertyChangeEventId = event != null ? event.getId() : null;

			// device telemetries
			this.telemetries = new ArrayList<>(deviceType.getTelemetries().size());
			for (DeviceTelemetry deviceTelemetry : deviceType.getTelemetries()) {
				this.telemetries.add(new DeviceTelemetryModel(deviceTelemetry));
			}
			this.telemetries
			        .sort(Comparator.comparing(DeviceTelemetryModel::getDescription, String.CASE_INSENSITIVE_ORDER));

			this.stateMetadata = deviceType.getStateMetadata();

			this.softwareRelease = softwareRelease;

			this.deviceTypeEditable = deviceType.isEditable();
		}

		public Map<String, String> getProperties() {
			return properties;
		}

		public void setProperties(Map<String, String> properties) {
			this.properties = properties;
		}

		public UserModels.UserOption getUser() {
			return user;
		}

		public void setUser(UserModels.UserOption user) {
			this.user = user;
		}

		public NodeModels.NodeOption getNode() {
			return node;
		}

		public void setNode(NodeModels.NodeOption node) {
			this.node = node;
		}

		public String getPropertyChangeEventId() {
			return propertyChangeEventId;
		}

		public void setPropertyChangeEventId(String propertyChangeEventId) {
			this.propertyChangeEventId = propertyChangeEventId;
		}

		public List<DeviceTelemetryModel> getTelemetries() {
			return telemetries;
		}

		public void setTelemetries(List<DeviceTelemetryModel> telemetries) {
			this.telemetries = telemetries;
		}

		public SoftwareReleaseOption getSoftwareRelease() {
			return softwareRelease;
		}

		public void setSoftwareRelease(SoftwareReleaseOption softwareRelease) {
			this.softwareRelease = softwareRelease;
		}

		public Map<String, DeviceStateValueMetadata> getStateMetadata() {
			return stateMetadata;
		}

		public void setStateMetadata(Map<String, DeviceStateValueMetadata> stateMetadata) {
			this.stateMetadata = stateMetadata;
		}

		public boolean isDeviceTypeEditable() {
			return deviceTypeEditable;
		}

		public void setDeviceTypeEditable(boolean deviceTypeEditable) {
			this.deviceTypeEditable = deviceTypeEditable;
		}
	}

	public static class DeviceDetailModel extends DeviceSettingsModel {
		private static final long serialVersionUID = -1769952510025258381L;

		private GatewayModels.GatewayModel gateway;
		private Map<String, String> info = new HashMap<>();
		private List<DeviceActionModel> actions = null;
		private Set<String> tags = new HashSet<>();
		private LastTelemetryModel lastTelemetry;
		private LastLocationModel lastLocation;
		private DeviceStatsModel deviceStats = new DeviceStatsModel(0, 0, new ArrayList<>());

		public DeviceDetailModel() {
			super();
		}

		public DeviceDetailModel(Device device, GatewayModels.GatewayModel gateway, DeviceType deviceType, User user,
		        Node node, LastTelemetryModel lastTelemetry, List<DeviceActionModel> actions,
		        LastLocationModel lastLocation, DeviceStatsModel deviceStats, SoftwareReleaseOption softwareRelease) {
			super(device, deviceType, user, node, null, softwareRelease);

			this.gateway = gateway;

			// info
			this.info = device.getInfo();

			// last telemetry
			this.lastTelemetry = lastTelemetry;

			// actions
			this.actions = actions;

			// tags
			this.tags = device.getTags();

			this.lastLocation = lastLocation;

			if (deviceStats != null)
				this.deviceStats = deviceStats;
		}

		public GatewayModels.GatewayModel getGateway() {
			return gateway;
		}

		public Map<String, String> getInfo() {
			return info;
		}

		public LastTelemetryModel getLastTelemetry() {
			return lastTelemetry;
		}

		public void setLastTelemetry(LastTelemetryModel lastTelemetry) {
			this.lastTelemetry = lastTelemetry;
		}

		public List<DeviceActionModel> getActions() {
			return actions;
		}

		public void setActions(List<DeviceActionModel> actions) {
			this.actions = actions;
		}

		public Set<String> getTags() {
			return tags;
		}

		public void setTags(Set<String> tags) {
			this.tags = tags;
		}

		public LastLocationModel getLastLocation() {
			return lastLocation;
		}

		public DeviceStatsModel getDeviceStats() {
			return deviceStats;
		}
	}

	public static class DeviceRegistrationModel extends DeviceSettingsModel {
		private static final long serialVersionUID = -1769952510025258381L;

		private GatewayModels.GatewayOption gateway;
		private Map<String, String> info = new HashMap<>();

		public DeviceRegistrationModel() {
			super();
		}

		public DeviceRegistrationModel(Device device, GatewayModels.GatewayOption gateway, DeviceType deviceType,
		        User user, Node node, SoftwareReleaseOption softwareRelease) {
			super(device, deviceType, user, node, null, softwareRelease);

			this.gateway = gateway;
			this.info = device.getInfo();
		}

		public GatewayModels.GatewayOption getGateway() {
			return gateway;
		}

		public Map<String, String> getInfo() {
			return info;
		}
	}

	public static class DeviceCommandModel implements Serializable {
		private static final long serialVersionUID = -1368626337070680361L;

		private String command;
		private String payload;
		private Long expiration;

		public DeviceCommandModel() {
		}

		public String getCommand() {
			return command;
		}

		public void setCommand(String command) {
			this.command = command;
		}

		public String getPayload() {
			return payload;
		}

		public void setPayload(String payload) {
			this.payload = payload;
		}

		public Long getExpiration() {
			return expiration;
		}

		public void setExpiration(Long expiration) {
			this.expiration = expiration;
		}
	}
}

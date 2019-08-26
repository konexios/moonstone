package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.KronosApplication;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.web.model.RheaModels.SoftwareReleaseOption;
import com.arrow.kronos.web.model.SearchFilterModels.NewGatewayOptions;
import com.arrow.pegasus.data.GatewayType;
import com.arrow.pegasus.data.heartbeat.LastHeartbeat;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;
import com.arrow.pegasus.webapi.data.LastLocationModel;

import moonstone.acs.client.model.YesNoInherit;

public class GatewayModels {

	public static class GatewayOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = 6286389158839274713L;

		public GatewayOption() {
			super(null, null, null);
		}

		public GatewayOption(Gateway gateway) {
			super(gateway.getId(), gateway.getHid(), gateway.getName());
		}
	}

	public static class GatewayModel extends GatewayOption {
		private static final long serialVersionUID = -1455640945305262688L;

		private String uid;
		private String deviceTypeName;
		private GatewayType type;
		private String nodeName;
		private String ownerName;
		private String osName;
		private String softwareName;
		private String softwareVersion;
		private boolean enabled;
		private String deviceTypeId;
		private Instant lastModifiedDate;
		private Long lastHeartbeat;

		public GatewayModel(Gateway gateway) {
			super(gateway);

			this.uid = gateway.getUid();
			this.deviceTypeName = gateway.getRefDeviceType() != null ? gateway.getRefDeviceType().getName() : null;
			this.type = gateway.getType();
			this.osName = gateway.getOsName();
			this.softwareName = gateway.getSoftwareName();
			this.softwareVersion = gateway.getSoftwareVersion();
			this.enabled = gateway.isEnabled();
			this.deviceTypeId = gateway.getDeviceTypeId();
			this.lastModifiedDate = gateway.getLastModifiedDate();
		}

		public GatewayModel(Gateway gateway, String nodeName, String ownerName) {
			this(gateway);

			this.nodeName = nodeName;
			this.ownerName = ownerName;
		}

		public GatewayModel(Gateway gateway, String nodeName, String ownerName, LastHeartbeat lastHeartbeat) {
			this(gateway, nodeName, ownerName);

			this.lastHeartbeat = lastHeartbeat != null ? lastHeartbeat.getTimestamp() : null;
		}

		public String getUid() {
			return uid;
		}

		public String getDeviceTypeName() {
			return deviceTypeName;
		}

		public void setDeviceTypeName(String deviceTypeName) {
			this.deviceTypeName = deviceTypeName;
		}

		public GatewayType getType() {
			return type;
		}

		public void setType(GatewayType type) {
			this.type = type;
		}

		public String getNodeName() {
			return nodeName;
		}

		public void setNodeName(String nodeName) {
			this.nodeName = nodeName;
		}

		public String getOwnerName() {
			return ownerName;
		}

		public String getOsName() {
			return osName;
		}

		public void setOsName(String osName) {
			this.osName = osName;
		}

		public String getSoftwareName() {
			return softwareName;
		}

		public void setSoftwareName(String softwareName) {
			this.softwareName = softwareName;
		}

		public String getSoftwareVersion() {
			return softwareVersion;
		}

		public void setSoftwareVersion(String softwareVersion) {
			this.softwareVersion = softwareVersion;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public String getDeviceTypeId() {
			return deviceTypeId;
		}

		public Instant getLastModifiedDate() {
			return lastModifiedDate;
		}

		public void setLastModifiedDate(Instant lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}

		public Long getLastHeartbeat() {
			return lastHeartbeat;
		}

		public void setLastHeartbeat(Long lastHeartbeat) {
			this.lastHeartbeat = lastHeartbeat;
		}
	}

	public static class GatewayDetailsModel extends GatewayModel {
		private static final long serialVersionUID = -5648039799599825890L;

		private UserModels.UserOption user = null;
		private NodeModels.NodeOption node = null;
		private Long lastCheckinTime;
		private Long lastHeartbeatTime;
		private LastLocationModel lastLocation;
		private SoftwareReleaseOption softwareRelease = null;
		private Map<String, String> properties = new HashMap<>();
		private Map<String, String> info = new HashMap<>();

		public GatewayDetailsModel(Gateway gateway, User user, Long lastCheckinTime, Long lastHeartbeatTime,
		        LastLocationModel lastLocation, SoftwareReleaseOption softwareRelease) {
			super(gateway, (gateway.getRefNode() != null ? gateway.getRefNode().getName() : null),
			        (user != null ? user.getContact().fullName() : null));
			this.user = user != null ? new UserModels.UserOption(user) : null;
			this.node = gateway.getRefNode() != null ? new NodeModels.NodeOption(gateway.getRefNode()) : null;
			this.lastCheckinTime = lastCheckinTime;
			this.lastHeartbeatTime = lastHeartbeatTime;
			this.lastLocation = lastLocation;
			this.softwareRelease = softwareRelease;

			// properties
			this.properties = gateway.getProperties();

			// info
			this.info = gateway.getInfo();
		}

		public UserModels.UserOption getUser() {
			return user;
		}

		public NodeModels.NodeOption getNode() {
			return node;
		}

		public Long getLastCheckinTime() {
			return lastCheckinTime;
		}

		public Long getLastHeartbeatTime() {
			return lastHeartbeatTime;
		}

		public LastLocationModel getLastLocation() {
			return lastLocation;
		}

		public SoftwareReleaseOption getSoftwareRelease() {
			return softwareRelease;
		}

		public void setSoftwareRelease(SoftwareReleaseOption softwareRelease) {
			this.softwareRelease = softwareRelease;
		}

		public Map<String, String> getProperties() {
			return properties;
		}

		public void setProperties(Map<String, String> properties) {
			this.properties = properties;
		}

		public Map<String, String> getInfo() {
			return info;
		}

		public void setInfo(Map<String, String> info) {
			this.info = info;
		}
	}

	public static class GatewaySettingsModel extends GatewayOption {
		private static final long serialVersionUID = -4191379625121313080L;

		private String deviceTypeId;
		private UserModels.UserOption user;
		private NodeModels.NodeOption node = null;
		private Map<String, String> properties = new HashMap<>();
		private boolean assignOwnerToDevices;
		private SoftwareReleaseOption softwareRelease = null;

		public GatewaySettingsModel() {
			super();
		}

		public GatewaySettingsModel(Gateway gateway, User user, Node node) {
			super(gateway);
			this.deviceTypeId = gateway.getDeviceTypeId();
			this.user = user != null ? new UserModels.UserOption(user) : null;
			this.node = node != null ? new NodeModels.NodeOption(node) : null;

			// properties
			this.properties = gateway.getProperties();
		}

		public GatewaySettingsModel(Gateway gateway, User user, Node node, boolean assignOwnerToDevices,
		        SoftwareReleaseOption softwareRelease) {
			this(gateway, user, node);
			this.assignOwnerToDevices = assignOwnerToDevices;
			this.softwareRelease = softwareRelease;

			// properties
			this.properties = gateway.getProperties();
		}

		public String getDeviceTypeId() {
			return deviceTypeId;
		}

		public UserModels.UserOption getUser() {
			return user;
		}

		public NodeModels.NodeOption getNode() {
			return node;
		}

		public Map<String, String> getProperties() {
			return properties;
		}

		public void setProperties(Map<String, String> properties) {
			this.properties = properties;
		}

		public boolean isAssignOwnerToDevices() {
			return assignOwnerToDevices;
		}

		public SoftwareReleaseOption getSoftwareRelease() {
			return softwareRelease;
		}

		public void setSoftwareRelease(SoftwareReleaseOption softwareRelease) {
			this.softwareRelease = softwareRelease;
		}
	}

	public static class CreateGatewayModel implements Serializable {
		private static final long serialVersionUID = 7556786589531453644L;

		private String uid;
		private String name;
		private GatewayType type = GatewayType.Local;
		private String deviceTypeId;
		private String osName;
		private String softwareName;
		private String softwareVersion;
		private String sdkVersion;
		private String userId;
		private String externalId;
		private boolean enabled = true;
		private YesNoInherit persistTelemetry = YesNoInherit.INHERIT;
		private YesNoInherit indexTelemetry = YesNoInherit.INHERIT;
		private Map<String, String> info = new HashMap<>();
		private Map<String, String> properties = new HashMap<>();

		public CreateGatewayModel() {
		}

		public CreateGatewayModel(Gateway gateway) {
			this.uid = gateway.getUid();
			this.name = gateway.getName();
			this.type = gateway.getType();
			this.deviceTypeId = gateway.getDeviceTypeId();
			this.osName = gateway.getOsName();
			this.softwareName = gateway.getSoftwareName();
			this.softwareVersion = gateway.getSoftwareVersion();
			this.sdkVersion = gateway.getSdkVersion();
			this.userId = gateway.getUserId();
			this.externalId = gateway.getExternalId();
			this.enabled = gateway.isEnabled();
			this.persistTelemetry = gateway.getPersistTelemetry();
			this.indexTelemetry = gateway.getIndexTelemetry();
			this.info = gateway.getInfo();
			this.properties = gateway.getProperties();
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public GatewayType getType() {
			return type;
		}

		public void setType(GatewayType type) {
			this.type = type;
		}

		public String getDeviceTypeId() {
			return deviceTypeId;
		}

		public void setDeviceTypeId(String deviceTypeId) {
			this.deviceTypeId = deviceTypeId;
		}

		public String getOsName() {
			return osName;
		}

		public void setOsName(String osName) {
			this.osName = osName;
		}

		public String getSoftwareName() {
			return softwareName;
		}

		public void setSoftwareName(String softwareName) {
			this.softwareName = softwareName;
		}

		public String getSoftwareVersion() {
			return softwareVersion;
		}

		public void setSoftwareVersion(String softwareVersion) {
			this.softwareVersion = softwareVersion;
		}

		public String getSdkVersion() {
			return sdkVersion;
		}

		public void setSdkVersion(String sdkVersion) {
			this.sdkVersion = sdkVersion;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getExternalId() {
			return externalId;
		}

		public void setExternalId(String externalId) {
			this.externalId = externalId;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public YesNoInherit getPersistTelemetry() {
			return persistTelemetry;
		}

		public void setPersistTelemetry(YesNoInherit persistTelemetry) {
			this.persistTelemetry = persistTelemetry;
		}

		public YesNoInherit getIndexTelemetry() {
			return indexTelemetry;
		}

		public void setIndexTelemetry(YesNoInherit indexTelemetry) {
			this.indexTelemetry = indexTelemetry;
		}

		public Map<String, String> getInfo() {
			return info;
		}

		public Map<String, String> getProperties() {
			return properties;
		}
	}

	public static class NewGatewayModel implements Serializable {
		private static final long serialVersionUID = -3393545719062368133L;

		private CreateGatewayModel gateway;
		private NewGatewayOptions options;

		public NewGatewayModel(List<User> users, String defaultUserId, List<DeviceType> deviceTypes,
		        String defaultDeviceTypeId) {
			this.gateway = new CreateGatewayModel();
			this.gateway.setUserId(defaultUserId);
			this.gateway.setDeviceTypeId(defaultDeviceTypeId);

			this.options = new NewGatewayOptions(users, deviceTypes);
		}

		public CreateGatewayModel getGateway() {
			return gateway;
		}

		public NewGatewayOptions getOptions() {
			return options;
		}
	}

	public static class GatewayMoveModel extends CoreDocumentModel {
		private static final long serialVersionUID = -6430577588520939617L;

		private String name;
		private String uid;
		private GatewayType type;
		private String applicationName;
		private boolean allowGatewayTransfer;
		private boolean associated;

		public GatewayMoveModel() {
			super(null, null);
		}

		public GatewayMoveModel(Gateway gateway, KronosApplication kronosApplication, boolean associated) {
			super(gateway.getId(), gateway.getHid());

			this.name = gateway.getName();
			this.uid = gateway.getUid();
			this.type = gateway.getType();
			this.applicationName = kronosApplication.getRefApplication().getName();
			this.allowGatewayTransfer = kronosApplication.isAllowGatewayTransfer() && !associated;
			this.associated = associated;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public GatewayType getType() {
			return type;
		}

		public void setType(GatewayType type) {
			this.type = type;
		}

		public String getApplicationName() {
			return applicationName;
		}

		public void setApplicationName(String applicationName) {
			this.applicationName = applicationName;
		}

		public boolean isAllowGatewayTransfer() {
			return allowGatewayTransfer;
		}

		public void setAllowGatewayTransfer(boolean allowGatewayTransfer) {
			this.allowGatewayTransfer = allowGatewayTransfer;
		}

		public boolean isAssociated() {
			return associated;
		}

		public void setAssociated(boolean associated) {
			this.associated = associated;
		}
	}

	public static class GatewayMoveResultModel implements Serializable {
		private static final long serialVersionUID = 5811216540620807537L;

		private boolean moved;
		private String name;
		private String uid;
		private Boolean enabled;
		private List<CoreDefinitionModelOption> existingDeviceTypes;
		private List<CoreDefinitionModelOption> existingDeviceActionTypes;

		public GatewayMoveResultModel(Gateway gateway) {
			this.moved = true;
			this.name = gateway.getName();
			this.uid = gateway.getUid();
			this.enabled = gateway.isEnabled();
		}

		public GatewayMoveResultModel(List<DeviceType> deviceTypes, List<DeviceActionType> deviceActionTypes) {
			// used to indicate there are existing device types, device action
			// types and/or test procedures
			this.moved = false;
			if (deviceTypes != null && !deviceTypes.isEmpty()) {
				this.existingDeviceTypes = deviceTypes.stream()
				        .map(deviceType -> new CoreDefinitionModelOption(deviceType.getId(), deviceType.getHid(),
				                deviceType.getName()))
				        .collect(Collectors.toList());
			}
			if (deviceActionTypes != null && !deviceActionTypes.isEmpty()) {
				this.existingDeviceActionTypes = deviceActionTypes.stream()
				        .map(deviceActionType -> new CoreDefinitionModelOption(deviceActionType.getId(),
				                deviceActionType.getHid(), deviceActionType.getName()))
				        .collect(Collectors.toList());
			}
		}

		public boolean isMoved() {
			return moved;
		}

		public String getName() {
			return name;
		}

		public String getUid() {
			return uid;
		}

		public Boolean getEnabled() {
			return enabled;
		}

		public List<CoreDefinitionModelOption> getExistingDeviceTypes() {
			return existingDeviceTypes;
		}

		public List<CoreDefinitionModelOption> getExistingDeviceActionTypes() {
			return existingDeviceActionTypes;
		}
	}
}

package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arrow.kronos.DeviceActionTypeConstants.PostBackURL.ContentType;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceActionType;
//import com.arrow.kronos.data.DeviceCategory;
import com.arrow.kronos.data.DeviceEventStatus;
import com.arrow.kronos.data.DeviceStateTrans;
//import com.arrow.kronos.data.DeviceProduct;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.data.SoftwareReleaseSchedule;
import com.arrow.kronos.web.model.DeviceActionTypeModels.DeviceActionTypeOption;
import com.arrow.kronos.web.model.DeviceModels.DeviceOption;
import com.arrow.kronos.web.model.DeviceTypeModels.DeviceTypeOption;
import com.arrow.kronos.web.model.GatewayModels.GatewayOption;
import com.arrow.kronos.web.model.NodeModels.NodeOption;
import com.arrow.kronos.web.model.RheaModels.SoftwareReleaseOption;
import com.arrow.kronos.web.model.UserModels.UserOption;
import com.arrow.pegasus.data.GatewayType;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.webapi.data.CoreSearchFilterModel;
import com.arrow.pegasus.webapi.data.DeviceTagModels.DeviceTagOption;

import moonstone.acn.client.model.AcnDeviceCategory;
import moonstone.acn.client.model.RightToUseStatus;
import moonstone.acs.KeyValuePair;

public class SearchFilterModels {

	public static class KronosSearchFilterModel extends CoreSearchFilterModel {
		private static final long serialVersionUID = 8285571777531367991L;

		private Boolean enabled;

		public Boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}
	}

	public static class DeviceSearchFilterModel extends KronosSearchFilterModel {
		private static final long serialVersionUID = 7320227662835882792L;

		private String[] deviceTypeIds;
		private String[] gatewayIds;
		private String[] userIds;
		private String[] nodeIds;
		private String hid;
		private String uid;

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getHid() {
			return hid;
		}

		public void setHid(String hid) {
			this.hid = hid;
		}

		public DeviceSearchFilterModel() {
			super();
		}

		public String[] getDeviceTypeIds() {
			return deviceTypeIds;
		}

		public void setDeviceTypeIds(String[] deviceTypeIds) {
			this.deviceTypeIds = deviceTypeIds;
		}

		public String[] getGatewayIds() {
			return gatewayIds;
		}

		public void setGatewayIds(String[] gatewayIds) {
			this.gatewayIds = gatewayIds;
		}

		public String[] getUserIds() {
			return userIds;
		}

		public void setUserIds(String[] userIds) {
			this.userIds = userIds;
		}

		public String[] getNodeIds() {
			return nodeIds;
		}

		public void setNodeIds(String[] nodeIds) {
			this.nodeIds = nodeIds;
		}

	}

	public static class DeviceEventSearchFilterModel extends CoreSearchFilterModel {
		private static final long serialVersionUID = -4700070050462721942L;

		private String[] deviceActionTypeIds;
		private String[] statuses;
		// seconds since epoch
		private Long createdDateFrom;
		private Long createdDateTo;

		public DeviceEventSearchFilterModel() {
			super();
		}

		public String[] getDeviceActionTypeIds() {
			return deviceActionTypeIds;
		}

		public void setDeviceActionTypeIds(String[] deviceActionTypeIds) {
			this.deviceActionTypeIds = deviceActionTypeIds;
		}

		public String[] getStatuses() {
			return statuses;
		}

		public void setStatuses(String[] statuses) {
			this.statuses = statuses;
		}

		public Long getCreatedDateFrom() {
			return createdDateFrom;
		}

		public void setCreatedDateFrom(Long createdDateFrom) {
			this.createdDateFrom = createdDateFrom;
		}

		public Long getCreatedDateTo() {
			return createdDateTo;
		}

		public void setCreatedDateTo(Long createdDateTo) {
			this.createdDateTo = createdDateTo;
		}

	}

	public static class NodeSearchFilterModel extends KronosSearchFilterModel {
		private static final long serialVersionUID = 8767161401710291023L;

		private String[] nodeTypeIds;

		public NodeSearchFilterModel() {
			super();
		}

		public String[] getNodeTypeIds() {
			return nodeTypeIds;
		}

		public void setNodeTypeIds(String[] nodeTypeIds) {
			this.nodeTypeIds = nodeTypeIds;
		}
	}

	public static class GatewaySearchFilterModel extends KronosSearchFilterModel {
		private static final long serialVersionUID = -4095645175335362258L;

		private String[] deviceTypeIds;
		private EnumSet<GatewayType> gatewayTypes;
		private String[] osNames;
		private String[] softwareNames;
		private String[] softwareVersions;
		private String[] userIds;
		private String uid;
		private String hid;
		private String[] nodeIds;

		public String getHid() {
			return hid;
		}

		public void setHid(String hid) {
			this.hid = hid;
		}

		public String[] getDeviceTypeIds() {
			return deviceTypeIds;
		}

		public void setDeviceTypeIds(String[] deviceTypeIds) {
			this.deviceTypeIds = deviceTypeIds;
		}

		public EnumSet<GatewayType> getGatewayTypes() {
			return gatewayTypes;
		}

		public void setGatewayTypes(EnumSet<GatewayType> gatewayTypes) {
			this.gatewayTypes = gatewayTypes;
		}

		public String[] getOsNames() {
			return osNames;
		}

		public void setOsNames(String[] osNames) {
			this.osNames = osNames;
		}

		public String[] getSoftwareNames() {
			return softwareNames;
		}

		public void setSoftwareNames(String[] softwareNames) {
			this.softwareNames = softwareNames;
		}

		public String[] getSoftwareVersions() {
			return softwareVersions;
		}

		public void setSoftwareVersions(String[] softwareVersions) {
			this.softwareVersions = softwareVersions;
		}

		public String[] getUserIds() {
			return userIds;
		}

		public void setUserIds(String[] userIds) {
			this.userIds = userIds;
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String[] getNodeIds() {
			return nodeIds;
		}

		public void setNodeIds(String[] nodeIds) {
			this.nodeIds = nodeIds;
		}

	}

	public static class AuditLogSearchFilterModel extends CoreSearchFilterModel {
		private static final long serialVersionUID = -4700070050462721942L;

		// seconds since epoch
		private Long createdDateFrom;
		private Long createdDateTo;

		private String[] userIds;
		private String[] types;

		public AuditLogSearchFilterModel() {
			super();
		}

		public Long getCreatedDateFrom() {
			return createdDateFrom;
		}

		public void setCreatedDateFrom(Long createdDateFrom) {
			this.createdDateFrom = createdDateFrom;
		}

		public Long getCreatedDateTo() {
			return createdDateTo;
		}

		public void setCreatedDateTo(Long createdDateTo) {
			this.createdDateTo = createdDateTo;
		}

		public String[] getUserIds() {
			return userIds;
		}

		public void setUserIds(String[] userIds) {
			this.userIds = userIds;
		}

		public String[] getTypes() {
			return types;
		}

		public void setTypes(String[] types) {
			this.types = types;
		}
	}

	public static class AccessKeySearchFilterModel extends CoreSearchFilterModel {
		private static final long serialVersionUID = -698415611365699223L;

		private String name;
		private String[] accessLevels;
		private String[] pri;
		private Long expirationDateFrom;
		private Long expirationDateTo;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String[] getAccessLevels() {
			return accessLevels;
		}

		public void setAccessLevels(String[] accessLevels) {
			this.accessLevels = accessLevels;
		}

		public Long getExpirationDateFrom() {
			return expirationDateFrom;
		}

		public void setExpirationDateFrom(Long expirationDateFrom) {
			this.expirationDateFrom = expirationDateFrom;
		}

		public Long getExpirationDateTo() {
			return expirationDateTo;
		}

		public void setExpirationDateTo(Long expirationDateTo) {
			this.expirationDateTo = expirationDateTo;
		}

		public String[] getPri() {
			return pri;
		}

		public void setPri(String[] pri) {
			this.pri = pri;
		}
	}

	public static class SoftwareReleaseScheduleSearchFilterModel extends CoreSearchFilterModel {
		private static final long serialVersionUID = -3823455598074237140L;

		private Long scheduledDateFrom;
		private Long scheduledDateTo;
		// private String[] deviceCategoryIds;
		private String[] deviceCategories;
		private EnumSet<SoftwareReleaseSchedule.Status> statuses;

		public Long getScheduledDateFrom() {
			return scheduledDateFrom;
		}

		public Long getScheduledDateTo() {
			return scheduledDateTo;
		}

		// public String[] getDeviceCategoryIds() {
		// return deviceCategoryIds;
		// }

		public String[] getDeviceCategories() {
			return deviceCategories;
		}

		public EnumSet<SoftwareReleaseSchedule.Status> getStatuses() {
			return statuses;
		}
	}

	public static class TestProcedureSearchFilterModel extends KronosSearchFilterModel {
		private static final long serialVersionUID = -4700070050462721942L;

		private String[] deviceTypeIds;

		public TestProcedureSearchFilterModel() {
			super();
		}

		public String[] getDeviceTypeIds() {
			return deviceTypeIds;
		}

		public void setDeviceTypeIds(String[] deviceTypeIds) {
			this.deviceTypeIds = deviceTypeIds;
		}
	}

	public static class TestResultSearchFilterModel extends CoreSearchFilterModel {
		private static final long serialVersionUID = -7451618558675288236L;

		private String[] statuses;
		private String[] testProcedureIds;
		// private String[] categories;
		private String objectId;
		private String[] stepStatuses;

		public TestResultSearchFilterModel() {
			super();
		}

		public String[] getStatuses() {
			return statuses;
		}

		public void addStatus(String[] statuses) {
			this.statuses = statuses;
		}

		public String[] getTestProcedureIds() {
			return testProcedureIds;
		}

		public void addTestProcedureId(String[] testProcedureId) {
			this.testProcedureIds = testProcedureId;
		}

		/*
		 * public String[] getCategories() { return categories; }
		 * 
		 * public void addCategory(String[] categories){ this.categories = categories; }
		 */

		public String getObjectId() {
			return objectId;
		}

		public void addObjectId(String objectId) {
			this.objectId = objectId;
		}

		public String[] getStepStatuses() {
			return stepStatuses;
		}

		public void setStepStatuses(String[] stepStatuses) {
			this.stepStatuses = stepStatuses;
		}
	}

	public static class DeviceStateTransSearchFilterModel extends CoreSearchFilterModel {
		private static final long serialVersionUID = 2562057007011563184L;

		private String[] types;
		private String[] statuses;
		private Long createdDateFrom;
		private Long createdDateTo;
		private Long updatedDateFrom;
		private Long updatedDateTo;

		public String[] getTypes() {
			return types;
		}

		public void setTypes(String[] types) {
			this.types = types;
		}

		public String[] getStatuses() {
			return statuses;
		}

		public void setStatuses(String[] statuses) {
			this.statuses = statuses;
		}

		public Long getCreatedDateFrom() {
			return createdDateFrom;
		}

		public void setCreatedDateFrom(Long createdDateFrom) {
			this.createdDateFrom = createdDateFrom;
		}

		public Long getCreatedDateTo() {
			return createdDateTo;
		}

		public void setCreatedDateTo(Long createdDateTo) {
			this.createdDateTo = createdDateTo;
		}

		public Long getUpdatedDateFrom() {
			return updatedDateFrom;
		}

		public void setUpdatedDateFrom(Long updatedDateFrom) {
			this.updatedDateFrom = updatedDateFrom;
		}

		public Long getUpdatedDateTo() {
			return updatedDateTo;
		}

		public void setUpdatedDateTo(Long updatedDateTo) {
			this.updatedDateTo = updatedDateTo;
		}

	}

	public static class SoftwareReleaseScheduleSearchFilterStatusModel extends CoreSearchFilterModel {
		private static final long serialVersionUID = -2776349985717279267L;

		private String[] deviceTypes;
		private String startDates;
		private String[] requestors;
		private String completedDates;
		private String[] statuses;

		public String[] getStatuses() {
			return statuses;
		}

		public String[] getRequestors() {
			return requestors;
		}

		public String getStartDates() {
			return startDates;
		}

		public String getCompletedDates() {
			return completedDates;
		}

		public String[] getDeviceTypes() {
			return deviceTypes;
		}
	}

	public static class SoftwareReleaseScheduleAssetSearchFilterModel extends CoreSearchFilterModel {
		private static final long serialVersionUID = 3805175243379891318L;

		public String status;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}

	public static class UserOptions implements Serializable {
		private static final long serialVersionUID = -2302360321778653556L;

		private List<UserOption> users;

		public UserOptions(List<User> users) {
			this.users = new ArrayList<>(users.size());
			for (User user : users) {
				this.users.add(new UserOption(user));
			}
			this.users.sort(Comparator.comparing(UserOption::getName, String.CASE_INSENSITIVE_ORDER));
		}

		public List<UserOption> getUsers() {
			return users;
		}
	}

	public static class DeviceSearchFilterOptions extends UserOptions {

		private static final long serialVersionUID = -5303153564202533342L;

		private List<DeviceTypeOption> deviceTypes;
		private List<GatewayOption> gateways;
		private List<NodeOption> nodes;
		private List<DeviceTagOption> tags;

		public DeviceSearchFilterOptions(List<DeviceType> deviceTypes, List<Gateway> gateways, List<User> users,
				List<Node> nodes, List<DeviceTagOption> tags) {
			super(users);

			this.deviceTypes = new ArrayList<>(deviceTypes.size());
			for (DeviceType deviceType : deviceTypes) {
				this.deviceTypes.add(new DeviceTypeOption(deviceType));
			}
			this.deviceTypes.sort(Comparator.comparing(DeviceTypeOption::getName, String.CASE_INSENSITIVE_ORDER));

			this.gateways = new ArrayList<>(gateways.size());
			for (Gateway gateway : gateways) {
				this.gateways.add(new GatewayOption(gateway));
			}
			this.gateways.sort(Comparator.comparing(GatewayOption::getName, String.CASE_INSENSITIVE_ORDER));

			Map<String, Node> nodesMap = new HashMap<>();
			for (Node node : nodes) {
				nodesMap.put(node.getId(), node);
			}
			this.nodes = new ArrayList<>(nodes.size());
			for (Node node : nodes) {
				this.nodes.add(new NodeOption(node, NodeOption.getNodeFullName(node, nodesMap)));
			}
			this.nodes.sort(Comparator.comparing(NodeOption::getName, String.CASE_INSENSITIVE_ORDER));

			this.tags = tags;
		}

		public List<DeviceTypeOption> getDeviceTypes() {
			return deviceTypes;
		}

		public List<GatewayOption> getGateways() {
			return gateways;
		}

		public List<NodeOption> getNodes() {
			return nodes;
		}

		public List<DeviceTagOption> getTags() {
			return tags;
		}
	}

	public static class DeviceDetailsOptions extends DeviceSearchFilterOptions {

		private static final long serialVersionUID = -5619530380925117993L;

		private List<DeviceActionTypeOption> actionTypes;
		private DeviceEventStatus[] eventStatuses;
		private ContentType[] contentTypes;
		private List<SoftwareReleaseOption> softwareReleases;

		public DeviceDetailsOptions(List<DeviceType> deviceTypes, List<Gateway> gateways, List<User> users,
				List<Node> nodes, List<DeviceTagOption> tags, List<DeviceActionType> actionTypes,
				DeviceEventStatus[] eventStatuses, List<SoftwareReleaseOption> softwareReleases) {
			super(deviceTypes, gateways, users, nodes, tags);

			this.actionTypes = new ArrayList<>(actionTypes.size());
			for (DeviceActionType actionType : actionTypes) {
				this.actionTypes.add(new DeviceActionTypeOption(actionType));
			}

			this.eventStatuses = eventStatuses;
			this.contentTypes = ContentType.values();

			this.softwareReleases = softwareReleases;
			this.softwareReleases
					.sort(Comparator.comparing(SoftwareReleaseOption::getName, String.CASE_INSENSITIVE_ORDER));
		}

		public List<DeviceActionTypeOption> getActionTypes() {
			return actionTypes;
		}

		public void setActionTypes(List<DeviceActionTypeOption> actionTypes) {
			this.actionTypes = actionTypes;
		}

		public DeviceEventStatus[] getEventStatuses() {
			return eventStatuses;
		}

		public void setEventStatuses(DeviceEventStatus[] eventStatuses) {
			this.eventStatuses = eventStatuses;
		}

		public ContentType[] getContentTypes() {
			return contentTypes;
		}

		public void setContentTypes(ContentType[] contentTypes) {
			this.contentTypes = contentTypes;
		}

		public List<SoftwareReleaseOption> getSoftwareReleases() {
			return softwareReleases;
		}
	}

	public static class AuditLogSearchFilterOptions extends UserOptions {
		private static final long serialVersionUID = 1547589102807243925L;

		private String[] types;

		public AuditLogSearchFilterOptions(List<User> users, String[] types) {
			super(users);

			this.types = types;
		}

		public String[] getTypes() {
			return types;
		}
	}

	public static class GatewaySearchFilterOptions extends UserOptions {
		private static final long serialVersionUID = -5983044973068104318L;

		private List<DeviceTypeOption> deviceTypes;
		private List<String> osNames;
		private List<String> softwareNames;
		private List<String> softwareVersions;
		private List<GatewayType> gatewayTypes;
		private List<NodeOption> nodes;

		public GatewaySearchFilterOptions(List<DeviceType> deviceTypes, List<User> users, List<String> osNames,
				List<String> softwareNames, List<String> softwareVersions, List<Node> nodes) {
			super(users);

			this.deviceTypes = new ArrayList<>(deviceTypes.size());
			for (DeviceType deviceType : deviceTypes) {
				this.deviceTypes.add(new DeviceTypeOption(deviceType));
			}
			this.deviceTypes.sort(Comparator.comparing(DeviceTypeOption::getName, String.CASE_INSENSITIVE_ORDER));

			this.osNames = osNames;
			if (this.osNames != null) {
				this.osNames.sort(String.CASE_INSENSITIVE_ORDER);
			}
			this.softwareNames = softwareNames;
			if (this.softwareNames != null) {
				this.softwareNames.sort(String.CASE_INSENSITIVE_ORDER);
			}
			this.softwareVersions = softwareVersions;
			if (this.softwareVersions != null) {
				this.softwareVersions.sort(String.CASE_INSENSITIVE_ORDER);
			}
			Map<String, Node> nodesMap = new HashMap<>();
			for (Node node : nodes) {
				nodesMap.put(node.getId(), node);
			}
			this.nodes = new ArrayList<>(nodes.size());
			for (Node node : nodes) {
				this.nodes.add(new NodeOption(node, NodeOption.getNodeFullName(node, nodesMap)));
			}
			this.nodes.sort(Comparator.comparing(NodeOption::getName, String.CASE_INSENSITIVE_ORDER));

			this.gatewayTypes = Arrays.asList(GatewayType.values());
			this.gatewayTypes.sort(Comparator.comparing(GatewayType::toString, String.CASE_INSENSITIVE_ORDER));
		}

		public List<DeviceTypeOption> getDeviceTypes() {
			return deviceTypes;
		}

		public List<String> getOsNames() {
			return osNames;
		}

		public List<String> getSoftwareNames() {
			return softwareNames;
		}

		public List<String> getSoftwareVersions() {
			return softwareVersions;
		}

		public List<GatewayType> getGatewayTypes() {
			return this.gatewayTypes;
		}

		public List<NodeOption> getNodes() {
			return nodes;
		}
	}

	public static class GatewayDetailsOptions implements Serializable {
		private static final long serialVersionUID = 7923704376693172002L;

		private List<DeviceTypeOption> deviceTypes;
		private List<UserOption> users;
		private List<NodeOption> nodes;
		private List<SoftwareReleaseOption> softwareReleases;

		public GatewayDetailsOptions(List<DeviceType> deviceTypes, List<User> users, List<Node> nodes,
				List<SoftwareReleaseOption> softwareReleases) {

			this.deviceTypes = new ArrayList<DeviceTypeOption>(deviceTypes.size());
			for (DeviceType deviceType : deviceTypes) {
				this.deviceTypes.add(new DeviceTypeOption(deviceType));
			}
			this.deviceTypes.sort(Comparator.comparing(DeviceTypeOption::getName, String.CASE_INSENSITIVE_ORDER));

			this.users = new ArrayList<>(users.size());
			for (User user : users) {
				this.users.add(new UserOption(user));
			}
			this.users.sort(Comparator.comparing(UserOption::getName, String.CASE_INSENSITIVE_ORDER));

			Map<String, Node> nodesMap = new HashMap<>();
			for (Node node : nodes) {
				nodesMap.put(node.getId(), node);
			}
			this.nodes = new ArrayList<>(nodes.size());
			for (Node node : nodes) {
				this.nodes.add(new NodeOption(node, NodeOption.getNodeFullName(node, nodesMap)));
			}
			this.nodes.sort(Comparator.comparing(NodeOption::getName, String.CASE_INSENSITIVE_ORDER));

			this.softwareReleases = softwareReleases;
			this.softwareReleases
					.sort(Comparator.comparing(SoftwareReleaseOption::getName, String.CASE_INSENSITIVE_ORDER));
		}

		public List<DeviceTypeOption> getDeviceTypes() {
			return deviceTypes;
		}

		public List<UserOption> getUsers() {
			return users;
		}

		public List<NodeOption> getNodes() {
			return nodes;
		}

		public List<SoftwareReleaseOption> getSoftwareReleases() {
			return softwareReleases;
		}
	}

	public static class NewGatewayOptions implements Serializable {
		private static final long serialVersionUID = 8215055520905400603L;

		private GatewayType[] types = GatewayType.values();
		private List<UserOption> users;
		private List<DeviceTypeOption> deviceTypes;

		public NewGatewayOptions(List<User> users, List<DeviceType> deviceTypes) {
			this.users = new ArrayList<>(users.size());
			for (User user : users) {
				this.users.add(new UserOption(user));
			}
			this.users.sort(Comparator.comparing(UserOption::getName, String.CASE_INSENSITIVE_ORDER));

			this.deviceTypes = new ArrayList<>(deviceTypes.size());
			for (DeviceType deviceType : deviceTypes) {
				this.deviceTypes.add(new DeviceTypeOption(deviceType));
			}
			this.deviceTypes.sort(Comparator.comparing(DeviceTypeOption::getName, String.CASE_INSENSITIVE_ORDER));
		}

		public GatewayType[] getTypes() {
			return types;
		}

		public List<UserOption> getUsers() {
			return users;
		}

		public List<DeviceTypeOption> getDeviceTypes() {
			return deviceTypes;
		}
	}

	public static class AccessKeyOptions implements Serializable {
		private static final long serialVersionUID = -3613640261448760666L;

		private String devicePriPrefix;
		private String gatewayPriPrefix;
		private String nodePriPrefix;

		private List<DeviceOption> devices;
		private List<GatewayOption> gateways;
		private List<NodeOption> nodes;

		public AccessKeyOptions(List<Device> devices, List<Gateway> gateways, List<Node> nodes) {
			Device deviceSample = new Device();
			deviceSample.setHid("");
			this.devicePriPrefix = deviceSample.getPri();

			Gateway gatewaySample = new Gateway();
			gatewaySample.setHid("");
			this.gatewayPriPrefix = gatewaySample.getPri();

			Node nodeSample = new Node();
			nodeSample.setHid("");
			this.nodePriPrefix = nodeSample.getPri();

			this.devices = new ArrayList<>(devices.size());
			for (Device device : devices) {
				this.devices.add(new DeviceOption(device));
			}
			this.devices.sort(Comparator.comparing(DeviceOption::getName, String.CASE_INSENSITIVE_ORDER));

			this.gateways = new ArrayList<>(gateways.size());
			for (Gateway gateway : gateways) {
				this.gateways.add(new GatewayOption(gateway));
			}
			this.gateways.sort(Comparator.comparing(GatewayOption::getName, String.CASE_INSENSITIVE_ORDER));

			Map<String, Node> nodesMap = new HashMap<>();
			for (Node node : nodes) {
				nodesMap.put(node.getId(), node);
			}
			this.nodes = new ArrayList<>(nodes.size());
			for (Node node : nodes) {
				this.nodes.add(new NodeOption(node, NodeOption.getNodePath(node, nodesMap)));
			}
			this.nodes.sort(Comparator.comparing(NodeOption::getName, String.CASE_INSENSITIVE_ORDER));
		}

		public String getDevicePriPrefix() {
			return devicePriPrefix;
		}

		public String getGatewayPriPrefix() {
			return gatewayPriPrefix;
		}

		public String getNodePriPrefix() {
			return nodePriPrefix;
		}

		public List<DeviceOption> getDevices() {
			return devices;
		}

		public List<GatewayOption> getGateways() {
			return gateways;
		}

		public List<NodeOption> getNodes() {
			return nodes;
		}

	}

	public static class SoftwareReleaseScheduleAuditLogFilterOptions implements Serializable {

		private static final long serialVersionUID = -5593078361102010161L;

		private long fromDate;
		private List<SoftwareReleaseScheduleModels.AuditLogUserOption> users;
		private List<String> types;
		private List<KeyValuePair<String, String>> assets;

		public SoftwareReleaseScheduleAuditLogFilterOptions(long fromDate,
				List<SoftwareReleaseScheduleModels.AuditLogUserOption> users, List<String> types,
				List<KeyValuePair<String, String>> assets) {
			this.fromDate = fromDate;
			this.users = users;
			this.types = types;
			this.assets = assets;
		}

		public void setAssets(List<KeyValuePair<String, String>> assets) {
			this.assets = assets;
		}

		public List<KeyValuePair<String, String>> getAssets() {
			return assets;
		}

		public List<String> getTypes() {
			return types;
		}

		public void setTypes(List<String> types) {
			this.types = types;
		}

		public List<SoftwareReleaseScheduleModels.AuditLogUserOption> getUsers() {
			return users;
		}

		public void setUsers(List<SoftwareReleaseScheduleModels.AuditLogUserOption> users) {
			this.users = users;
		}

		public long getFromDate() {
			return fromDate;
		}

		public void setFromDate(long fromDate) {
			this.fromDate = fromDate;
		}
	}

	public static class SoftwareReleaseScheduleAuditLogSearchFilterModel extends AuditLogSearchFilterModel {
		private static final long serialVersionUID = 3611373134994427438L;

		private String[] assetIds;

		public String[] getAssetIds() {
			return assetIds;
		}

		public void setAssetIds(String[] assetIds) {
			this.assetIds = assetIds;
		}
	}

	public static class SoftwareReleaseScheduleFilterOptions implements Serializable {
		private static final long serialVersionUID = 128406093618738096L;

		// private List<DeviceCategoryOption> categories;
		private EnumSet<AcnDeviceCategory> deviceCategories;
		private SoftwareReleaseSchedule.Status[] statuses = SoftwareReleaseSchedule.Status.values();

		// public List<DeviceCategoryOption> getCategories() {
		// return categories;
		// }
		//
		// public void setCategories(List<DeviceCategoryOption> categories) {
		// this.categories = categories;
		// }

		public EnumSet<AcnDeviceCategory> getDeviceCategories() {
			return deviceCategories;
		}

		public void setDeviceCategories(EnumSet<AcnDeviceCategory> deviceCategories) {
			this.deviceCategories = deviceCategories;
		}

		public SoftwareReleaseSchedule.Status[] getStatuses() {
			return statuses;
		}
	}

	public static class DeviceStateHistoryFilterOptions implements Serializable {
		private static final long serialVersionUID = -2771498121865003324L;

		private DeviceStateTrans.Type[] types = DeviceStateTrans.Type.values();
		private DeviceStateTrans.Status[] statuses = DeviceStateTrans.Status.values();

		public DeviceStateTrans.Type[] getTypes() {
			return types;
		}

		public DeviceStateTrans.Status[] getStatuses() {
			return statuses;
		}
	}

	public static class RTUFirmwareAvailableFilterOptions implements Serializable {
		private static final long serialVersionUID = 7228846664173529652L;

		private List<DeviceTypeOption> deviceTypes;

		public RTUFirmwareAvailableFilterOptions(List<DeviceTypeOption> deviceTypes) {
			this.deviceTypes = deviceTypes;
			this.deviceTypes.sort(Comparator.comparing(DeviceTypeOption::getName, String.CASE_INSENSITIVE_ORDER));
		}

		public List<DeviceTypeOption> getDeviceTypes() {
			return deviceTypes;
		}

	}

	public static class RTUFirmwareRequestedFilterOptions implements Serializable {
		private static final long serialVersionUID = -6520487998501509901L;
		private EnumSet<RightToUseStatus> statuses;

		public RTUFirmwareRequestedFilterOptions(EnumSet<RightToUseStatus> enumSet) {
			this.statuses = enumSet;
		}

		public EnumSet<RightToUseStatus> getStatuses() {
			return statuses;
		}
	}

	public static class ConfigBackupSearchFilterOptions extends CoreSearchFilterModel {
		private static final long serialVersionUID = -3216445311568106305L;

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public static class MyDevicesSearchFilterModel extends CoreSearchFilterModel {
		private static final long serialVersionUID = -8753425892357055463L;

		private String name;
		private String deviceTypeName;
		private Instant lastTimestamp;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDeviceTypeName() {
			return deviceTypeName;
		}

		public void setDeviceTypeName(String deviceTypeName) {
			this.deviceTypeName = deviceTypeName;
		}

		public Instant getLastTimestamp() {
			return lastTimestamp;
		}

		public void setLastTimestamp(Instant lastTimestamp) {
			this.lastTimestamp = lastTimestamp;
		}
	}

	public static class MyDeviceEventSearchFilterModel extends CoreSearchFilterModel {
		private static final long serialVersionUID = 2211625911994782323L;
	}

	public static class MyGatewaySearchFilterModel extends CoreSearchFilterModel {
		private static final long serialVersionUID = 4472060164347435629L;
	}

	public static class RTUSearchFilterModel implements Serializable {
		private static final long serialVersionUID = 7822938376901447882L;

		private String[] deviceTypeIds;

		public RTUSearchFilterModel() {
			super();
		}

		public String[] getDeviceTypeIds() {
			return deviceTypeIds;
		}

		public void setDeviceTypeIds(String[] deviceTypeIds) {
			this.deviceTypeIds = deviceTypeIds;
		}
	}

	public static class RTURequestedSearchFilterModel extends CoreSearchFilterModel {
		private static final long serialVersionUID = -1701959554261939396L;

		private String[] statuses;

		public RTURequestedSearchFilterModel() {
			super();
		}

		public String[] getStatuses() {
			return statuses;
		}

		public void addStatus(String[] statuses) {
			this.statuses = statuses;
		}
	}
}

package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.DeviceEvent;
import com.arrow.kronos.data.DeviceEventStatus;
import com.arrow.kronos.data.Gateway;
import com.arrow.pegasus.data.heartbeat.LastHeartbeat;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

public class HomeModels {

	public static class HomePage implements Serializable {
		private static final long serialVersionUID = -8999857343613237594L;

		private List<MyDevice> myDevices = new ArrayList<>();
		private List<HomeModels.MyGateway> myGateways = new ArrayList<>();

		public HomePage(List<MyDevice> myDevices, List<HomeModels.MyGateway> myGateways) {
			if (myDevices != null)
				this.myDevices = myDevices;

			if (myGateways != null)
				this.myGateways = myGateways;
		}

		public List<MyDevice> getMyDevices() {
			return myDevices;
		}

		public List<HomeModels.MyGateway> getMyGateways() {
			return myGateways;
		}
	}

	public static class MyDevice extends CoreDocumentModel {
		private static final long serialVersionUID = -3663962435527790594L;

		private String name;
		private String deviceTypeName;
		private String uid;
		private Long lastTimestamp;

		public MyDevice(Device device, String deviceTypeName, Long lastTimestamp) {
			super(device.getId(), device.getUid());

			this.name = device.getName();
			this.deviceTypeName = deviceTypeName;
			this.uid = device.getUid();
			this.lastTimestamp = lastTimestamp;
		}

		public String getName() {
			return name;
		}

		public String getDeviceTypeName() {
			return deviceTypeName;
		}

		public String getUid() {
			return uid;
		}

		public Long getLastTimestamp() {
			return lastTimestamp;
		}
	}

	public static class MyGateway extends CoreDocumentModel {
		private static final long serialVersionUID = 8483867656826927385L;

		private String name;
		private String gatewayTypeName;
		private String uid;
		private Long lastHeartbeat;

		public MyGateway(Gateway gateway, String gatewayTypeName, LastHeartbeat lastHeartbeat) {
			super(gateway.getId(), gateway.getHid());

			this.name = gateway.getName();
			this.gatewayTypeName = gatewayTypeName;
			this.uid = gateway.getUid();
			this.lastHeartbeat = lastHeartbeat != null ? lastHeartbeat.getTimestamp() : null;
		}

		public String getName() {
			return name;
		}

		public String getGatewayTypeName() {
			return gatewayTypeName;
		}

		public String getUid() {
			return uid;
		}

		public Long getLastHeartbeat() {
			return lastHeartbeat;
		}
	}

	public static class MyDeviceEvent extends CoreDocumentModel {
		private static final long serialVersionUID = 5361558331375014961L;

		private String deviceActionTypeName = "Unknown";
		private DeviceEventStatus status;
		private String deviceName;
		private String deviceId;
		private long createdDate;
		private int count;

		public MyDeviceEvent(DeviceEvent deviceEvent, DeviceActionType deviceActionType, Device device) {
			super(deviceEvent.getId(), deviceEvent.getHid());
			this.status = deviceEvent.getStatus();
			if (deviceActionType != null)
				this.deviceActionTypeName = deviceActionType.getName();
			this.createdDate = deviceEvent.getCreatedDate().toEpochMilli();
			this.deviceName = device.getName();
			this.deviceId = device.getId();
			this.count = deviceEvent.getCounter();
		}

		public String getDeviceActionTypeName() {
			return deviceActionTypeName;
		}

		public DeviceEventStatus getStatus() {
			return status;
		}

		public String getDeviceName() {
			return deviceName;
		}

		public String getDeviceId() {
			return deviceId;
		}

		public long getCreatedDate() {
			return createdDate;
		}

		public int getCount() {
			return count;
		}
	}
}

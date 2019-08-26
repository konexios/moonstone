package moonstone.selene;
public interface SeleneEventNames {
	String GATEWAY_CHECK_STATUS = "checkGatewayStatus";
	String DEVICE_CHECK_STATUS = "checkDeviceStatus";
	String DEVICE_START = "startDevice";
	String DEVICE_UPDATE = "updateDevice";
	String DEVICE_STOP = "stopDevice";
	String DEVICE_REFRESH = "refreshDeviceData";
	String DEVICE_LOAD = "loadDevice";
	String DEVICE_UNLOAD = "unloadDevice";
	String PERFORM_COMMAND = "performCommand";
	String GATEWAY_STOP = "stopGateway";
	String BLE_DISCOVERY = "bleDiscovery";

	interface DeviceLoadParams {
		String DEVICE_TYPE = "deviceType";
		String DEVICE_UID = "deviceUid";
		String DEVICE_HID = "deviceHid";
	}

	interface BleDiscoveryParams {
		String DISCOVERY_TIMEOUT = "discoveryTimeout";
		String INTERFACE = "interface";
	}
}

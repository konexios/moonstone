package moonstone.selene.device.mqttrouter;

import moonstone.selene.device.mqtt.MqttDeviceProperties;

public class MqttRouterProperties extends MqttDeviceProperties {

	private static final long serialVersionUID = 5339586104827426450L;

	private String telemetryTopics;
	private int deviceUidToken = 1;
	private boolean deviceRegistrationOverMqtt = false;
	private String deviceRegistrationTopic = "selene/mqtt/device/register";
	private String routerRequestTopic = "selene/mqtt/router/request";
	private String routerResponseTopic = "selene/mqtt/router/response";
	private String devices;
	private String protocol;
	private String deviceNameTag;
	private String deviceUidTag;
	private String clientId;
	private String caCertPath;
	private String clientCertPath;
	private String privateKeyPath;
	private boolean mqttBrokerCertified = false;
	private String statusTopic = "selene/mqtt/status";
	private String stateControlTopic = "selene/mqtt/device/control";
	private String cmdMqttTopic = "${deviceType}/${deviceUid}/cmd";
	private String stateTransposerScript;
	private String registrationTransposerScript;
	private String telemetryTransposerScript;

	public String getDeviceNameTag() {
		return deviceNameTag;
	}

	public void setDeviceNameTag(String deviceNameTag) {
		this.deviceNameTag = deviceNameTag;
	}

	public String getDeviceUidTag() {
		return deviceUidTag;
	}

	public void setDeviceUidTag(String deviceUidTag) {
		this.deviceUidTag = deviceUidTag;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getDevices() {
		return devices;
	}

	public void setDevices(String devices) {
		this.devices = devices;
	}

	public String getTelemetryTopics() {
		return telemetryTopics;
	}

	public void setTelemetryTopics(String telemetryTopics) {
		this.telemetryTopics = telemetryTopics;
	}

	public boolean isDeviceRegistrationOverMqtt() {
		return deviceRegistrationOverMqtt;
	}

	public void setDeviceRegistrationOverMqtt(boolean deviceRegistrationOverMqtt) {
		this.deviceRegistrationOverMqtt = deviceRegistrationOverMqtt;
	}

	public String getDeviceRegistrationTopic() {
		return deviceRegistrationTopic;
	}

	public void setDeviceRegistrationTopic(String deviceRegistrationTopic) {
		this.deviceRegistrationTopic = deviceRegistrationTopic;
	}

	public int getDeviceUidToken() {
		return deviceUidToken;
	}

	public void setDeviceUidToken(int deviceUidToken) {
		this.deviceUidToken = deviceUidToken;
	}

	public boolean isMqttBrokerCertified() {
		return mqttBrokerCertified;
	}

	public void setMqttBrokerCertified(boolean mqttBrokerCertified) {
		this.mqttBrokerCertified = mqttBrokerCertified;
	}

	public String getCaCertPath() {
		return caCertPath;
	}

	public void setCaCertPath(String caCertPath) {
		this.caCertPath = caCertPath;
	}

	public String getClientCertPath() {
		return clientCertPath;
	}

	public void setClientCertPath(String clientCertPath) {
		this.clientCertPath = clientCertPath;
	}

	public String getPrivateKeyPath() {
		return privateKeyPath;
	}

	public void setPrivateKeyPath(String privateKeyPath) {
		this.privateKeyPath = privateKeyPath;
	}

	public String getStatusTopic() {
		return statusTopic;
	}

	public void setStatusTopic(String statusTopic) {
		this.statusTopic = statusTopic;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getCmdMqttTopic() {
		return cmdMqttTopic;
	}

	public void setCmdMqttTopic(String cmdMqttTopic) {
		this.cmdMqttTopic = cmdMqttTopic;
	}

	public String getStateTransposerScript() {
		return stateTransposerScript;
	}

	public void setStateTransposerScript(String stateTransposerScript) {
		this.stateTransposerScript = stateTransposerScript;
	}

	public String getRegistrationTransposerScript() {
		return registrationTransposerScript;
	}

	public void setRegistrationTransposerScript(String registrationTransposerScript) {
		this.registrationTransposerScript = registrationTransposerScript;
	}

	public String getTelemetryTransposerScript() {
		return telemetryTransposerScript;
	}

	public void setTelemetryTransposerScript(String telemetryTransposerScript) {
		this.telemetryTransposerScript = telemetryTransposerScript;
	}

	public String getStateControlTopic() {
		return stateControlTopic;
	}

	public void setStateControlTopic(String stateControlTopic) {
		this.stateControlTopic = stateControlTopic;
	}

	public String getRouterRequestTopic() {
		return routerRequestTopic;
	}

	public void setRouterRequestTopic(String routerRequestTopic) {
		this.routerRequestTopic = routerRequestTopic;
	}

	public String getRouterResponseTopic() {
		return routerResponseTopic;
	}

	public void setRouterResponseTopic(String routerResponseTopic) {
		this.routerResponseTopic = routerResponseTopic;
	}

}

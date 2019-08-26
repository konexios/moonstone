package moonstone.selene.device.mqtt;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import moonstone.acn.client.cloud.CustomMqttClient;
import moonstone.acn.client.cloud.MessageListener;
import moonstone.acs.AcsSystemException;
import moonstone.selene.engine.DeviceModuleAbstract;

public abstract class MqttDeviceModuleAbstract<Info extends MqttDeviceInfo, Prop extends MqttDeviceProperties, States extends MqttDeviceStates, Data extends MqttDeviceData>
        extends DeviceModuleAbstract<Info, Prop, States, Data> implements MessageListener {

	protected CustomMqttClient mqttClient;
	protected List<String> subscriptionTopics;
	
	// adding outgoing topic

	@Override
	protected void startDevice() {
		super.startDevice();
		mqttConnect();
	}

	protected void mqttConnect() {
		String method = "mqttConnect";

		if (mqttClient != null) {
			logWarn(method, "mqttClient is already initialized!");
			return;
		}

		String clientId = generateClientId();
		logInfo(method, "creating new MQTT client, clientId: %s", clientId);
		mqttClient = new CustomMqttClient(getProperties().getMqttUrl(), clientId);

		// set username / password if configured
		String userName = getProperties().getMqttUserName();
		String password = getProperties().getMqttPassword();
		if (StringUtils.isNotEmpty(userName)) {
			mqttClient.getOptions().setUserName(userName);
		}
		if (StringUtils.isNotEmpty(password)) {
			mqttClient.getOptions().setPassword(password.toCharArray());
		}

		List<String> topics = getSubscriptionTopics();
		if (topics == null || topics.size() == 0) {
			throw new AcsSystemException("topics are not defined!");
		}
		mqttClient.setTopics(topics.toArray(new String[topics.size()]));

		mqttClient.setListener(this);

		// connect now
		logInfo(method, "connecting to MQTT broker: %s", getProperties().getMqttUrl());
		mqttClient.connect(false);
	}

	@Override
	public void stop() {
		String method = "stop";
		super.stop();
		if (mqttClient != null) {
			try {
				// make sure to disconnect from broker
				logInfo(method, "stopping MQTT client ...");
				mqttClient.disconnect();
			} catch (Throwable t) {
			}
			mqttClient = null;
		}
	}

	protected CustomMqttClient getMqttClient() {
		return mqttClient;
	}

	protected String generateClientId() {
		return getClass().getSimpleName();
	}

	protected List<String> getSubscriptionTopics() {
		return subscriptionTopics;
	}

	protected void setSubscriptionTopics(List<String> subscriptionTopics) {
		this.subscriptionTopics = subscriptionTopics;
	}
}

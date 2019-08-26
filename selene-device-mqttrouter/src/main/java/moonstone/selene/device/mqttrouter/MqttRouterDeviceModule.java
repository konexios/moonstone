package moonstone.selene.device.mqttrouter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acs.JsonUtils;
import moonstone.selene.device.mqttrouter.handlers.DeviceStateChangeHandler;
import moonstone.selene.engine.DeviceData;
import moonstone.selene.engine.DeviceModuleAbstract;
import moonstone.selene.engine.EngineConstants;
import moonstone.selene.engine.Utils;
import moonstone.selene.engine.state.State;
import moonstone.selene.engine.state.StateUpdate;
import moonstone.selene.model.StatusModel;

/**
 * MqttRouterDeviceModule class built on top of DeviceModuleAbstract class . It
 * is responsible for receiving device-telemetry from device-adapter and further
 * publishing it to cloud, also it will receive device related command from
 * cloud and routes it to mqtt-router.
 */
public class MqttRouterDeviceModule extends
        DeviceModuleAbstract<MqttRouterDeviceInfo, MqttRouterDeviceProperties, MqttRouterDeviceStates, MqttRouterDeviceData> {

	private MqttRouterModule router;

	public MqttRouterDeviceModule(MqttRouterModule router) {
		this.router = router;
		handlers = Collections.singletonList(new DeviceStateChangeHandler(this));
	}

	@Override
	public void init(Properties props) {

		super.init(props);
		registerState();
	}

	protected void registerState() {
		String method = "registerState";

		if (getInfo().getInfo().containsKey("Properties")) {

			ArrayList<HashMap<String, ?>> properties = JsonUtils.fromJson(getInfo().getInfo().get("Properties"),
			        new TypeReference<ArrayList<HashMap<String, ?>>>() {
			        });

			properties.stream()
			        .filter(propertiesValue -> Objects.equals(propertiesValue.get("operation"), "w")
			                || Objects.equals(propertiesValue.get("operation"), "rw"))
			        .forEach(propertiesValue -> getStates().addDeviceStates((String) propertiesValue.get("name")));
		} else
			logInfo(method, "Properties not found in device info");

	}

	/**
	 * Process-Message method will receive device-telemetry from device-adapter
	 * and further it converts it to iot-parameters and adds it to
	 * telemetry-queue.
	 * 
	 * @param payload
	 *            which contains telemetry information.
	 */
	public void processMessage(String payload) {
		String method = "processMessage";

		if (StringUtils.isBlank(payload)) {
			logWarn(method, "ignored empty payload");
		} else {
			MqttRouterDeviceData data = new MqttRouterDeviceData();
			try {
				Utils.populateDeviceData(data, JsonUtils.fromJson(payload, EngineConstants.MAP_TYPE_REF));
				data.setParsedFully(true);
			} catch (Exception e) {
				data.setStrData(payload);
				logInfo(method, "sending raw data to cloud", e);

			}
			logInfo(method, "adding data into queue");
			getService().submit(() -> queueDataForSending(data));
		}
	}

	/**
	 * performCommand method will receive device related commands from cloud and
	 * routes the data to mqtt router.
	 * 
	 * 
	 * @param bytes
	 *            contains command and payload in byte format.
	 * 
	 * @return status of command received.
	 */
	@Override
	public StatusModel performCommand(byte... bytes) {
		String payload = new String(bytes);
		router.routeCommand(getInfo().getType(), getInfo().getUid(), payload);
		return StatusModel.OK.withMessage("Command Received");
	}

	public void setDeviceStates(Map<String, State> data) {
		router.routeDeviceStates(getInfo().getType(), getInfo().getUid(), getInfo().getName(), data);
	}

	@Override
	protected MqttRouterDeviceProperties createProperties() {
		return new MqttRouterDeviceProperties();
	}

	@Override
	protected MqttRouterDeviceInfo createInfo() {
		return new MqttRouterDeviceInfo();

	}

	@Override
	protected MqttRouterDeviceStates createStates() {
		return new MqttRouterDeviceStates();

	}

	@Override
	protected void queueDataForSending(DeviceData data, boolean processSequential) {
		String method = "queueDataForSending";
		super.queueDataForSending(data, processSequential);
		logInfo(method, "queueDataForSending data :: %s", data.getParsedIotParameters());

		// Fetch state properties from telemetry payload.
		Map<String, String> state = data.getParsedIotParameters().keySet().stream()
		        .filter(key -> getStates().getDeviceStates().containsKey(key.split("\\|", 2)[1])).collect(
		                Collectors.toMap(key -> key.split("\\|", 2)[1], key -> data.getParsedIotParameters().get(key)));

		if (!(state.isEmpty())) {
			Map<String, String> updatedStates = getStates().importStates(getStates().extractStates(state));
			if (!updatedStates.isEmpty()) {
				queueStatesForSending(new StateUpdate().withStates(updatedStates));
			}
			logInfo(method, "adding state change to queueStatesForSending :: %s", updatedStates);
		}
	}

}

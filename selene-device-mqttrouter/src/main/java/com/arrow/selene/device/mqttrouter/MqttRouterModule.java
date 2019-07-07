package com.arrow.selene.device.mqttrouter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.text.StringSubstitutor;

import com.arrow.acn.client.cloud.CustomMqttClient;
import com.arrow.acs.AcsLogicalException;
import com.arrow.acs.AcsSystemException;
import com.arrow.acs.JsonUtils;
import com.arrow.selene.SeleneException;
import com.arrow.selene.dao.DeviceDao;
import com.arrow.selene.data.Device;
import com.arrow.selene.device.mqtt.MqttDeviceModuleAbstract;
import com.arrow.selene.engine.EngineConstants;
import com.arrow.selene.engine.SslUtil;
import com.arrow.selene.engine.Utils;
import com.arrow.selene.engine.service.ConfigService;
import com.arrow.selene.engine.service.DeviceService;
import com.arrow.selene.engine.service.ModuleService;
import com.arrow.selene.engine.state.State;
import com.arrow.selene.model.StatusModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

/**
 * MqttRouterModule class built on top of MqttDeviceModuleAbstract class. It is
 * responsible for runtime device registration, publishing device telemetry to
 * cloud, processing device related commands.
 */
public class MqttRouterModule
        extends MqttDeviceModuleAbstract<MqttRouterInfo, MqttRouterProperties, MqttRouterStates, MqttRouterData> {

    private Map<String, MqttRouterDeviceModule> modules = new HashMap<>();
    private AtomicBoolean moduleStatus = new AtomicBoolean(true);

    /**
     * It is responsible for broker connection, once connected it will subscribe
     * the topic's based on the configuration. It also supports TLS/SSL broker
     * connection.
     * 
     */
    @Override
    protected void mqttConnect() {

        String method = "mqttConnect";

        if (mqttClient != null) {
            logWarn(method, "mqttClient is already initialized!");
            return;
        }

        if (StringUtils.isEmpty(getProperties().getMqttUrl())) {
            throw new AcsSystemException("mqtt url is not defined!");
        }

        String clientId = getProperties().getClientId();
        if (StringUtils.isEmpty(clientId)) {
            mqttClient = new CustomMqttClient(getProperties().getMqttUrl());
        } else {
            mqttClient = new CustomMqttClient(getProperties().getMqttUrl(), clientId);
        }
        logInfo(method, "creating new MQTT client, clientId: %s", clientId);

        // set username / password if configured
        String userName = getProperties().getMqttUserName();
        String password = getProperties().getMqttPassword();

        if (StringUtils.isNotEmpty(userName)) {
            mqttClient.getOptions().setUserName(userName);
        }
        if (StringUtils.isNotEmpty(password)) {
            mqttClient.getOptions().setPassword(password.toCharArray());
        }

        // set ssl certificates if configured
        if (getProperties().isMqttBrokerCertified()) {
            try {
                String caCert = new String(Files.readAllBytes(Paths.get(getProperties().getCaCertPath())));
                String clientCert = new String(Files.readAllBytes(Paths.get(getProperties().getClientCertPath())));
                String privateKey = new String(Files.readAllBytes(Paths.get(getProperties().getPrivateKeyPath())));
                mqttClient.getOptions().setSocketFactory(SslUtil.getSocketFactory(caCert, clientCert, privateKey));
            } catch (IOException e) {
                throw new SeleneException("Something wrong with certificated files", e);
            } catch (Exception e) {
                throw new SeleneException("Error to creating mqtt connection for new device", e);
            }
        }

        List<String> topics = getSubscriptionTopics();
        if (topics == null || topics.isEmpty()) {
            throw new AcsSystemException("topics are not defined!");
        }

        mqttClient.setTopics(topics.toArray(new String[topics.size()]));
        mqttClient.setListener(this);

        // connect now
        logInfo(method, "connecting to MQTT broker: %s", getProperties().getMqttUrl());
        mqttClient.connect(false);

    }

    /**
     * On device startup, reinitializes the properties detail from database.
     */
    @Override
    public void start() {
        Set<Properties> props = DeviceService.getInstance().findAll().stream()
                .filter(device -> Objects.equals(device.getUid(), getInfo().getUid())).map(Utils::getProperties)
                .collect(Collectors.toSet());

        Map<String, String> map = new HashMap<>();
        for (Properties prop : props) {
            prop.stringPropertyNames().forEach(p -> map.put(p, prop.getProperty(p)));
        }

        // load properties
        try {
            getProperties().populateFrom(map);
        } catch (Exception ignored) {
        }

        super.start();
    }

    /**
     * Stores the information provided in the configuration. Initializes the
     * mqttRouter as device using the provided properties file and further
     * registers device in the cloud.
     * 
     * @param props
     *            required properties information.
     */
    @Override
    public void init(Properties props) {

        super.init(props);

        String method = "init";

        // get telemetry topic.
        String telemetryTopics = getProperties().getTelemetryTopics();
        logInfo(method, "TelemetryTopics : %s", telemetryTopics);

        if (StringUtils.isEmpty(telemetryTopics)) {
            throw new AcsSystemException("TelemetryTopics is missing in configuration");
        }

        String[] split = telemetryTopics.split(",");
        List<String> topics;

        // get registration topic.
        String deviceRegistrationTopic = getProperties().getDeviceRegistrationTopic();
        topics = new ArrayList<>(split.length + 4);

        if (!getProperties().isDeviceRegistrationOverMqtt()) {
            logInfo(method, "disable device registration over mqtt");
        } else if (getProperties().isDeviceRegistrationOverMqtt() && StringUtils.isNotEmpty(deviceRegistrationTopic)) {
            topics.add(deviceRegistrationTopic);
        } else
            throw new AcsLogicalException("deviceRegistrationTopic is empty");

        // add for status topic.
        if (!getProperties().getStatusTopic().isEmpty()) {
            topics.add(getProperties().getStatusTopic());
        }

        // router request and response topics.
        topics.add(getProperties().getRouterRequestTopic());
        topics.add(getProperties().getRouterResponseTopic());

        Arrays.asList(split).forEach(token -> {
            token = token.trim();
            logInfo(method, "adding topic: %s", token);
            topics.add(token);
        });
        setSubscriptionTopics(topics);

        // On startup register multiple devices.
        String inputDevices = getProperties().getDevices();

        if (inputDevices != null) {
            List<String> deviceArray = JsonUtils.fromJson(inputDevices, EngineConstants.LIST_TYPE_REF);
            deviceArray.forEach(uid -> createModule(MqttRouterInfo.DEFAULT_DEVICE_TYPE, uid, uid));
        }

        // validate deviceUidTag
        String deviceUidTag = getProperties().getDeviceUidTag();
        if (StringUtils.isBlank(getProperties().getRegistrationTransposerScript())
                && StringUtils.isBlank(deviceUidTag)) {
            throw new AcsLogicalException("deviceUidTag is missing in configuration!");
        }

        // Check mqtt broker certification.
        boolean mqttBrokerCertified = getProperties().isMqttBrokerCertified();
        String caCertPath = getProperties().getCaCertPath();
        String clientCertPath = getProperties().getClientCertPath();
        String privateKeyPath = getProperties().getPrivateKeyPath();

        if (mqttBrokerCertified && (StringUtils.isEmpty(caCertPath) || StringUtils.isEmpty(clientCertPath)
                || StringUtils.isEmpty(privateKeyPath))) {
            throw new AcsLogicalException(
                    "caCertPath or clientCertPath or privateKeyPath is missing in configuration!");
        }

    }

    /**
     * Processes requests received from device adapter, for example like
     * registration of device, status acknowledgement etc.
     * 
     * It is also responsible for providing device telemetry received from
     * device adaptor to cloud,
     * 
     * @param topic
     *            mqtt-topic on which data is received.
     * 
     * @param payload
     *            data-payload received over mqtt
     */
    @Override
    public void processMessage(String topic, byte[] payload) {
        String method = "processMessage";
        logInfo(method, "received topic: %s and payload: %s", topic, payload);

        // Parse topic and send to respective method
        if (topic.equals(getProperties().getStatusTopic())) {
            statusAcknowledgment(topic, payload);
        } else if (Pattern.matches(
                getProperties().getDeviceRegistrationTopic().replace("+", "[^/]+").replace("#", ".+"), topic)) {
            mqttDeviceRegister(payload);
        } else if (Pattern.matches(getProperties().getRouterRequestTopic().replace("+", "[^/]+").replace("#", ".+"),
                topic)) {
            processAdapterRequest(payload);
        } else if (Pattern.matches(getProperties().getRouterResponseTopic().replace("+", "[^/]+").replace("#", ".+"),
                topic)) {
            processAdapterResponse(payload);
        } else {
            sendDeviceTelemetry(topic, payload);
        }
    }

    /**
     * It is responsible to process different type of adapter requests.
     * 
     * @param payload
     *            data-payload required for process adapter requests.
     */
    @SuppressWarnings("unchecked")
    protected void processAdapterRequest(byte[] payload) {
        AdapterRequestData adapterRequestData = new AdapterRequestData()
                .populateFrom((Map<String, Object>) JsonUtils.fromJsonBytes(payload, HashMap.class));
        if (Objects.equals(adapterRequestData.getCommand(), "get_lookup_configuration")) {
            findLookup(adapterRequestData.getPayload());
        }
        if (Objects.equals(adapterRequestData.getCommand(), "register_device")) {
            mqttDeviceRegister(JsonUtils.toJsonBytes(adapterRequestData.getPayload()));
        }
    }

    /**
     * It is responsible to process different type of adapter response.
     * 
     * @param payload
     *            data-payload required for process adapter response.
     */
    protected void processAdapterResponse(byte[] payload) {
        processMessage(new String(payload));
    }

    /**
     * It is responsible for registering device in cloud based on formatted and
     * unformatted raw data.
     * 
     * @param payload
     *            data-payload required for device-registration.
     */
    protected void mqttDeviceRegister(byte[] payload) {

        if (StringUtils.isBlank(getProperties().getRegistrationTransposerScript())) {
            deviceRegisterWithRawData(payload);
        } else {
            deviceRegisterWithformattedData(payload);
        }
    }

    /**
     * It is responsible for registering device in cloud based on formatted
     * data.
     * 
     * @param payload
     *            data-payload required for device-registration.
     */
    protected void deviceRegisterWithformattedData(byte[] payload) {
        String method = "deviceRegisterWithformattedData";

        logDebug(method, "In deviceRegisterWithformattedData");

        String updatedPayload;
        updatedPayload = ScriptManager.getInstance().runRegistrationScript(
                getProperties().getRegistrationTransposerScript(), new String(payload, StandardCharsets.UTF_8));

        List<DeviceRegistrationInfo> devices;
        try {
            devices = formattedRegistrationDataParser(updatedPayload);
        } catch (Exception e) {
            throw new SeleneException("error to parse json object", e);
        }

        for (int i = 0; i < devices.size(); i++) {

            String deviceType = devices.get(i).getDeviceType();
            if (StringUtils.isBlank(deviceType)) {
                deviceType = MqttRouterInfo.DEFAULT_DEVICE_TYPE;
            }
            String deviceUid = devices.get(i).getDeviceUid();
            String deviceName = devices.get(i).getDeviceName();
            String deviceProperties = new String();
            String metadata = new String();

            if (!(devices.get(i).getProperties() == null || devices.get(i).getProperties().isEmpty())) {
                deviceProperties = JsonUtils.toJson(devices.get(i).getProperties());
            }

            if (!(devices.get(i).getMetadata() == null || devices.get(i).getMetadata().isEmpty())) {
                metadata = JsonUtils.toJson(devices.get(i).getMetadata());
            }

            logInfo(method, "device Type: %s, Uid: %s, name: %s, properties: %s, metadata: %s", deviceType, deviceUid,
                    deviceName, deviceProperties, metadata);

            if (StringUtils.isBlank(deviceProperties)) {
                createModule(deviceType, deviceUid, deviceName);
            } else if (StringUtils.isBlank(metadata)) {
                createModule(deviceType, deviceUid, deviceName, deviceProperties);
            } else {
                createModule(deviceType, deviceUid, deviceName, deviceProperties, metadata);
            }
        }

    }

    /**
     * It is responsible for registering device in cloud based on unformatted
     * raw data.
     * 
     * @param payload
     *            data-payload required for device-registration.
     */
    protected void deviceRegisterWithRawData(byte[] payload) {
        String method = "deviceRegisterWithRawData";

        logDebug(method, "in deviceRegisterWithRawData");
        List<Map<String, String>> registerDevices;

        try {
            registerDevices = rawRegistrationDataParser(payload);
        } catch (Exception e) {
            throw new SeleneException("error to parse json object", e);
        }

        String type = MqttRouterInfo.DEFAULT_DEVICE_TYPE;
        for (Map<String, String> modulePayload : registerDevices) {

            String name = modulePayload.get("deviceName");
            String uid = modulePayload.get("deviceUid");

            if (StringUtils.isBlank(name) || StringUtils.isBlank(uid)) {
                logError(method, "wrong payload for uid: %s and name: %s", uid, name);
            }
            createModule(type, uid.trim(), name.trim());

        }
    }

    /**
     * It is responsible for providing device-telemetry to cloud. It validates
     * whether device is registered or not, once validated it further publishes
     * data on to cloud.
     * 
     * @param topic
     *            mqtt-topic on which data is received
     * 
     * @param payload
     *            data-payload received over mqtt
     */
    protected void sendDeviceTelemetry(String topic, byte[] payload) {

        String method = "sendDeviceTelemetry";
        logDebug(method, "In send Device telemetry method");

        // Enter when telemetry topic receive.
        String uid;
        String[] tokens = topic.split("/");

        try {
            uid = tokens[getProperties().getDeviceUidToken() - 1].trim();
        } catch (Exception ignored) {
            logError(method, "error parsing topic: %s,  deviceUidToken: %d", topic,
                    getProperties().getDeviceUidToken());
            return;
        }

        // Find devices from db and send to particular device.
        MqttRouterDeviceModule module = findModule(uid);
        if (module != null) {
            String stringPayload;

            if (StringUtils.isNotBlank(getProperties().getTelemetryTransposerScript())) {
                logInfo(method, "in telemetry script");
                stringPayload = ScriptManager.getInstance().runTelemetryScript(
                        getProperties().getTelemetryTransposerScript(), new String(payload, StandardCharsets.UTF_8));
            } else
                stringPayload = new String(payload, StandardCharsets.UTF_8);

            module.processMessage(stringPayload);
        } else {
            logError(method, "module not found!!!");
        }
    }

    /**
     * It will be called when registration request is received over mqtt. It
     * further validates whether the device is already registered or not, if not
     * it will create new module based on the device information and will
     * register device with the cloud.
     * 
     * @param type
     *            type of the device.
     * 
     * @param uid
     *            unique device-ID with respect to device.
     * 
     * @param name
     *            name of the device.
     * 
     * @param properties
     *            properties details of device.
     * 
     * @param extraInfo
     *            extra information of the device.
     * 
     */
    synchronized void createModule(String type, String uid, String name, String properties, String extraInfo) {
        String method = "createModule";

        // Find at code level registered device.
        MqttRouterDeviceModule result = modules.get(uid);
        if (result != null) {
            logInfo(method, "mqtt device already registered, type: %s, uid: %s", type, uid);
        }

        try {
            boolean started = false;
            Properties props = new Properties();

            // find device from db.
            Device existing = DeviceDao.getInstance().findByTypeAndUid(type, uid);
            if (existing == null) {
                logInfo(method, "creating new mqtt device, type: %s, uid: %s", type, uid);

                result = new MqttRouterDeviceModule(this);

                props.setProperty("name", name);
                props.setProperty("uid", uid);
                props.setProperty("type", type);

                Map<String, String> devices = new HashMap<>();
                devices.put("deviceName", name);
                devices.put("deviceUid", uid);

                if (!properties.isEmpty()) {
                    props.setProperty("info.Properties", properties);
                }
                if (!extraInfo.isEmpty()) {
                    props.setProperty("info.Extra-info", extraInfo);
                }
                props.setProperty("info.thirdPartyDevice", "true");

                props.setProperty("persistTelemetry", Boolean.toString(getProperties().isPersistTelemetry()));
                props.setProperty("maxPollingIntervalMs", Long.toString(getProperties().getMaxPollingIntervalMs()));
                String dataParsingScriptFilename = getProperties().getDataParsingScriptFilename();

                if (!StringUtils.isBlank(dataParsingScriptFilename)) {
                    props.setProperty("dataParsingScriptFilename", dataParsingScriptFilename);
                }

            } else {
                result = (MqttRouterDeviceModule) ModuleService.getInstance().findDevice(existing.getHid());

                if (result == null) {
                    logWarn(method, "mqtt device not found for uid: %s, loading from DB ...", uid);
                    result = new MqttRouterDeviceModule(this);
                    DeviceService.getInstance().loadDeviceProperties(result, existing);
                } else {
                    started = true;
                }
            }

            if (!started) {
                logInfo(method, "initializing mqtt device: %s", uid);
                result.init(props);

                logInfo(method, "registering mqtt service: %s", uid);
                ModuleService.getInstance().registerModule(result);

                logInfo(method, "starting mqtt device: %s", uid);
                ModuleService.getInstance().startModule(result);

                logInfo(method, "waiting for device ready, timeout = 10s ...");
                boolean status = result.waitForModuleReady(1000L);
                logInfo(method, "status: %s", status);
            } else {
                Map<String, String> deviceInfo = result.getInfo().getInfo();
                // Update registered device.
                if (!properties.isEmpty()) {
                    deviceInfo.put("Properties", properties);
                }
                if (!extraInfo.isEmpty()) {
                    deviceInfo.put("Extra-info", extraInfo);
                }
                result.registerState();
                result.persistUpdatedDeviceInfo();
            }

            modules.put(uid, result);

        } catch (Exception e) {
            logError(method, "ERROR creating mqtt device", e);
        }

    }

    synchronized void createModule(String type, String uid, String name, String properties) {
        createModule(type, uid, name, properties, "");
    }

    synchronized void createModule(String type, String uid, String name) {
        createModule(type, uid, name, "", "");
    }

    /**
     * Finds the device from existing device-database based on the device-uid.
     * 
     * @param uid
     *            unique device-ID with respect to device.
     * 
     * @return module object of the found device.
     */
    synchronized MqttRouterDeviceModule findModule(String uid) {
        String method = "findModule";

        MqttRouterDeviceModule result = modules.get(uid);

        if (result == null) {
            try {
                Device existing = DeviceDao.getInstance().findByUid(uid);
                if (existing == null) {
                    throw new SeleneException(String.format("device not found for uid: %s", uid));
                } else {
                    result = (MqttRouterDeviceModule) ModuleService.getInstance().findDevice(existing.getHid());
                    if (result == null) {
                        logWarn(method, "mqtt device not found for uid: %s, loading from DB ...", uid);
                        result = new MqttRouterDeviceModule(this);
                        DeviceService.getInstance().loadDeviceProperties(result, existing);

                        Properties props = new Properties();

                        logInfo(method, "initializing mqtt device: %s", uid);
                        result.init(props);

                        logInfo(method, "registering mqtt service: %s", uid);
                        ModuleService.getInstance().registerModule(result);

                        logInfo(method, "starting mqtt device: %s", uid);
                        ModuleService.getInstance().startModule(result);

                        logInfo(method, "waiting for device ready, timeout = 10s ...");
                        boolean status = result.waitForModuleReady(1000L);
                        logInfo(method, "status: %s", status);
                    }
                }
            } catch (SeleneException e) {
                throw e;
            } catch (Exception e) {
                throw new SeleneException("ERROR creating mqtt device", e);
            }
        }
        return result;
    }

    /**
     * It is responsible for receiving request from cloud. It simply takes
     * received command as topic and publishes received data to device-adapter.
     * 
     * 
     * @param bytes
     *            It contains device command and payload in byte format.
     * 
     * @return status of the request received.
     */
    @Override
    public StatusModel performCommand(byte... bytes) {
        String method = "performCommand";

        if (!moduleStatus.get()) {
            logInfo(method, "module status is inactive, error to publish message");
            return (StatusModel) StatusModel.error("module status is inactive");
        }

        String payload = new String(bytes);
        routeCommand(getInfo().getType(), getInfo().getUid(), payload);

        return StatusModel.OK;

    }

    /**
     * routeCommand method publishes data-payload received from cloud to
     * device-adaptor based on the topic provided.
     * 
     * 
     * @param deviceType
     *            type of the device.
     * 
     * @param uid
     *            unique-ID of the device.
     * 
     * @param payload
     *            data-payload received over mqtt
     */
    public void routeCommand(String deviceType, String uid, String payload) {
        String method = "routeCommand";

        if (!moduleStatus.get()) {
            logInfo(method, "module status is inactive, error to publish message.");
        } else {
            Validate.notEmpty(deviceType, "deviceType is empty");
            Validate.notEmpty(uid, "uid is empty");
            Validate.notEmpty(payload, "payload is empty");
            String topic = getProperties().getCmdMqttTopic();
            logInfo(method, "topic: %s", topic);
            logInfo(method, "Payload: %s", payload);
            mqttClient.publish(topic, payload.getBytes(StandardCharsets.UTF_8), 2);
        }
    }

    /**
     * routeDeviceStates method publishes state data-payload received from cloud
     * to device-adaptor based on the topic provided.
     * 
     * 
     * @param deviceType
     *            type of the device.
     * 
     * @param deviceUid
     *            unique-ID of the device.
     * 
     * @param deviceName
     *            name of the device.
     * 
     * @param data
     *            data-payload received over mqtt
     */
    public void routeDeviceStates(String deviceType, String deviceUid, String deviceName, Map<String, State> data) {
        String method = "routeDeviceStates";

        final String stateJavaScript = getProperties().getStateTransposerScript();

        if (stateJavaScript.isEmpty()) {
            throw new SeleneException("stateJavaScript will required to send state change requiest to device.");
        }
        try {
            Validate.notEmpty(deviceUid, "uid is empty");
            Validate.notEmpty(data, "payload is empty");
            Validate.notEmpty(deviceName, "name is empty");

            String payload = ScriptManager.getInstance().runStateScript(stateJavaScript, data, deviceUid, deviceName);

            if (!moduleStatus.get()) {
                logInfo(method, "module status is inactive, error to publish message.");
            } else {
                String topic = getProperties().getStateControlTopic();
                Map<String, String> ctx = new HashMap<>();
                ctx.put("deviceType", deviceType);
                ctx.put("deviceUid", deviceUid);
                topic = StringSubstitutor.replace(topic, ctx);
                logInfo(method, "topic: %s", topic);
                logInfo(method, "payload: %s", payload);
                mqttClient.publish(topic, payload.getBytes(StandardCharsets.UTF_8), 2);
            }
        } catch (SeleneException e) {
            throw e;
        } catch (Exception e) {
            throw new SeleneException("Error to send state change request to device", e);
        }
    }

    /**
     * rawRegistrationDataParser method fetches device-UID and device-Name from
     * the raw payload received and stores it in the lists.
     * 
     * @param payload
     *            data-payload containing device-uid and device-name details.
     * 
     * @return List of Map which contains device-uid and device-name.
     */
    public List<Map<String, String>> rawRegistrationDataParser(byte[] payload) {
        String method = "rawRegistrationDataParser";
        List<Map<String, String>> registerDevices = new ArrayList<>();

        try {
            final JsonNode jsonPayload = JsonUtils.fromJsonBytes(payload, JsonNode.class);
            String deviceUidTag = getProperties().getDeviceUidTag();
            String deviceNameTag = getProperties().getDeviceNameTag();
            List<String> deviceNameList;
            List<String> deviceUidList;

            if (StringUtils.isEmpty(deviceUidTag)) {
                throw new SeleneException("deviceUidTag should not be empty");
            }
            deviceUidList = fetchTagValue(deviceUidTag, jsonPayload.toString());

            if (StringUtils.isEmpty(deviceNameTag)) {
                deviceNameList = deviceUidList;
            } else {
                deviceNameList = fetchTagValue(deviceNameTag, jsonPayload.toString());
            }

            if (deviceNameList.size() == deviceUidList.size()) {
                for (int i = 0; i < deviceNameList.size(); i++) {
                    Map<String, String> devices = new HashMap<>();
                    if (StringUtils.isNotBlank(deviceNameList.get(i)) && StringUtils.isNotBlank(deviceUidList.get(i))) {
                        devices.put("deviceName", deviceNameList.get(i));
                        devices.put("deviceUid", deviceUidList.get(i));
                        registerDevices.add(devices);
                    }
                }
            } else {
                throw new SeleneException("ERROR parsing json payload");
            }
        } catch (SeleneException e) {
            throw e;
        } catch (Exception e) {
            logError(method, "ERROR TO parse payload: %s", e);
            throw new SeleneException("ERROR to creating selene map from device registration payload", e);
        }
        return registerDevices;
    }

    /**
     * formattedRegistrationDataParser method fetches device-UID, device-Name,
     * device-Type, properties and metadata from the formatted payload received
     * and stores it in the lists of DeviceRegistrationInfo.
     * 
     * @param payload
     *            data-payload containing device-uid, device-name, device-Type,
     *            properties and metadata details.
     * 
     * @return List of Map which contains device-uid and device-name.
     */
    public List<DeviceRegistrationInfo> formattedRegistrationDataParser(String payload) {
        String method = "formattedRegistrationDataParser";
        List<DeviceRegistrationInfo> registerDevices = new ArrayList<>();

        try {
            @SuppressWarnings("unchecked")
            final ArrayList<Map<String, Object>> devicesList = JsonUtils.fromJson(payload, ArrayList.class);

            for (int i = 0; i < devicesList.size(); i++) {
                registerDevices.add(new DeviceRegistrationInfo().populateFrom(devicesList.get(i)));
            }
        } catch (SeleneException e) {
            throw e;
        } catch (Exception e) {
            logError(method, "ERROR TO parse payload: %s", e);
            throw new SeleneException("ERROR to creating selene map from device registration payload", e);
        }

        return registerDevices;

    }

    /**
     * fetchTagValue method fetches device Tags(like name, uid) from payload and
     * stores it in a list.
     * 
     * @param tag
     *            indicate device-uid or device-name which is present in device
     *            payload.
     * 
     * @param payload
     *            data-payload containing device-uid tag and device-name tag
     *            details.
     * 
     * @return List which contain tag information.
     */
    public List<String> fetchTagValue(String tag, String payload) {
        String method = "fetchTagValue";
        logDebug(method, "In fetchTagValue");

        Configuration defConf = Configuration.defaultConfiguration();
        List<String> values = new ArrayList<>();

        if (tag == null) {
            return values;
        }

        String regex = "$.." + tag;
        List<Object> list = JsonPath.using(defConf).parse(payload).read(regex);
        for (Object obj : list) {
            if (obj instanceof String) {
                values.add((String) obj);
            }
        }
        return values;
    }

    /**
     * Process-Message method will receive device-telemetry from third party
     * device-adapter and further it converts it to iot-parameters and adds it
     * to telemetry-queue.
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
     * statusAcknowledgment method receives status of device-adapter and
     * acknowledge's it with mqtt router status.
     * 
     * @param topic
     *            mqtt-topic on which status is received.
     * 
     * @param payload
     *            data-payload that contains device-adapter acknowledgment
     *            status.
     * 
     */
    protected void statusAcknowledgment(String topic, byte[] payload) {

        Map<String, String> jsonPayload = JsonUtils.fromJsonBytes(payload, EngineConstants.MAP_TYPE_REF);

        if (Objects.equals(jsonPayload.get("status"), "active")) {
            moduleStatus.compareAndSet(false, true);
        } else if (Objects.equals(jsonPayload.get("status"), "inactive")) {
            moduleStatus.compareAndSet(true, false);
        }

        processMessage(new String(payload));

        topic = getStatusResponseTopic(topic);
        String replyPayload = "{\"status\": \"active\"}";

        mqttClient.publish(topic, replyPayload.getBytes(StandardCharsets.UTF_8), 2);
    }

    /**
     * findLookup method find lookup configuration from gateway and publish to
     * device-adapter
     * 
     * @param payload
     *            data-payload that contains device-adapter lookup
     *            configuration. status.
     * 
     */
    protected void findLookup(Map<String, String> payload) {

        String protocol = payload.get("protocol");
        String vendor = payload.get("vendor");
        String model = payload.get("model");
        String deviceUid = payload.get("deviceUid");

        if (StringUtils.isBlank(protocol) && StringUtils.isBlank(vendor) && StringUtils.isBlank(model)
                && StringUtils.isBlank(deviceUid)) {
            throw new SeleneException(
                    String.format("invalid payload: protocol: %s, vendor: %s, model:%s, deviceUid: %s", protocol,
                            vendor, model, deviceUid));
        }

        String homeDirectory = ConfigService.getInstance().getEngineProperties().getHomeDirectory();
        if (StringUtils.isEmpty(homeDirectory)) {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("win")) {
                homeDirectory = EngineConstants.DEFAULT_HOME_DIRECTORY_WINDOWS;
            } else {
                homeDirectory = EngineConstants.DEFAULT_HOME_DIRECTORY_LINUX;
            }
        }

        String filePath = String.format("%s/central_knowledge_bank/%s/%s/%s.json", homeDirectory, protocol, vendor,
                model);
        String content;
        Map<String, String> jsonObject = new HashMap<>();

        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new SeleneException(String.format("Error to load lookup file %s", e));
        }
        jsonObject.put("deviceUid", deviceUid);
        jsonObject.put("lookup", content);

        Map<String, String> responsePayload = new HashMap<>();
        responsePayload.put("command", "set_lookup_configuration");
        responsePayload.put("payload", JsonUtils.toJson(jsonObject));

        mqttClient.publish(getProperties().getCmdMqttTopic(), JsonUtils.toJsonBytes(responsePayload), 2);

    }

    @Override
    protected MqttRouterProperties createProperties() {
        return new MqttRouterProperties();
    }

    @Override
    protected MqttRouterInfo createInfo() {
        return new MqttRouterInfo();
    }

    @Override
    protected MqttRouterStates createStates() {
        return new MqttRouterStates();
    }

    protected String getStatusResponseTopic(String topic) {
        return topic + "/response";
    }

}

package com.arrow.selene.device.harting.rfid;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import com.arrow.acn.client.cloud.CustomMqttClient;
import com.arrow.acn.client.cloud.MessageListener;
import com.arrow.acn.client.model.DeviceStateUpdateModel;
import com.arrow.acn.client.utils.Utils;
import com.arrow.acs.AcsSystemException;
import com.arrow.acs.JsonUtils;
import com.arrow.selene.SeleneEventNames;
import com.arrow.selene.SeleneException;
import com.arrow.selene.data.Device;
import com.arrow.selene.device.harting.rfid.command.BaudRateDetection;
import com.arrow.selene.device.harting.rfid.command.ClearDataBuffer;
import com.arrow.selene.device.harting.rfid.command.Command;
import com.arrow.selene.device.harting.rfid.command.EmptyResponse;
import com.arrow.selene.device.harting.rfid.command.ForceNotifyTrigger;
import com.arrow.selene.device.harting.rfid.command.GetInput;
import com.arrow.selene.device.harting.rfid.command.GetInputResponse;
import com.arrow.selene.device.harting.rfid.command.GetReaderInfo;
import com.arrow.selene.device.harting.rfid.command.GetReaderInfoResponse;
import com.arrow.selene.device.harting.rfid.command.GetSoftwareVersion;
import com.arrow.selene.device.harting.rfid.command.GetSoftwareVersionResponse;
import com.arrow.selene.device.harting.rfid.command.GetSystemTimeAndDate;
import com.arrow.selene.device.harting.rfid.command.GetSystemTimeAndDateResponse;
import com.arrow.selene.device.harting.rfid.command.GetSystemTimer;
import com.arrow.selene.device.harting.rfid.command.GetSystemTimerResponse;
import com.arrow.selene.device.harting.rfid.command.InitializeBuffer;
import com.arrow.selene.device.harting.rfid.command.Inventory;
import com.arrow.selene.device.harting.rfid.command.InventoryResponse;
import com.arrow.selene.device.harting.rfid.command.LockRegion;
import com.arrow.selene.device.harting.rfid.command.ReadBuffer;
import com.arrow.selene.device.harting.rfid.command.ReadBufferResponse;
import com.arrow.selene.device.harting.rfid.command.ReadBufferResponse.Dataset;
import com.arrow.selene.device.harting.rfid.command.ReadConfiguration;
import com.arrow.selene.device.harting.rfid.command.ReadConfiguration.CfgAddress;
import com.arrow.selene.device.harting.rfid.command.ReadConfiguration.Location;
import com.arrow.selene.device.harting.rfid.command.ReadConfigurationResponse;
import com.arrow.selene.device.harting.rfid.command.ReadDataBufferInfo;
import com.arrow.selene.device.harting.rfid.command.ReadDataBufferInfoResponse;
import com.arrow.selene.device.harting.rfid.command.ReadMultipleBlocks;
import com.arrow.selene.device.harting.rfid.command.ReadMultipleBlocksResponse;
import com.arrow.selene.device.harting.rfid.command.ReaderDiagnostic;
import com.arrow.selene.device.harting.rfid.command.ReaderDiagnostic.Mode;
import com.arrow.selene.device.harting.rfid.command.ReaderDiagnosticResponse;
import com.arrow.selene.device.harting.rfid.command.ReaderLogin;
import com.arrow.selene.device.harting.rfid.command.ResetConfiguration;
import com.arrow.selene.device.harting.rfid.command.Response;
import com.arrow.selene.device.harting.rfid.command.RfControllerReset;
import com.arrow.selene.device.harting.rfid.command.RfOutputOnOff;
import com.arrow.selene.device.harting.rfid.command.RfReset;
import com.arrow.selene.device.harting.rfid.command.SetOutput;
import com.arrow.selene.device.harting.rfid.command.SetSystemTimeAndDate;
import com.arrow.selene.device.harting.rfid.command.SetSystemTimeAndDate.Date;
import com.arrow.selene.device.harting.rfid.command.SetSystemTimeAndDate.Time;
import com.arrow.selene.device.harting.rfid.command.SetSystemTimer;
import com.arrow.selene.device.harting.rfid.command.SystemReset;
import com.arrow.selene.device.harting.rfid.command.WriteConfiguration;
import com.arrow.selene.device.harting.rfid.command.WriteMultipleBlocks;
import com.arrow.selene.device.harting.rfid.command.WriteMultipleBlocksResponse;
import com.arrow.selene.device.harting.rfid.config.ConfigParameter;
import com.arrow.selene.device.harting.rfid.config.InterfaceAndMode;
import com.arrow.selene.device.harting.rfid.config.InterfaceAndMode.ReaderMode;
import com.arrow.selene.device.harting.rfid.config.UserParameter;
import com.arrow.selene.device.harting.rfid.mqtt.MqttResponse;
import com.arrow.selene.device.harting.rfid.tag.GenericTagData;
import com.arrow.selene.device.harting.rfid.tag.GenericTagModule;
import com.arrow.selene.device.harting.rfid.tag.TagModuleAbstract;
import com.arrow.selene.device.self.SelfModule;
import com.arrow.selene.engine.DeviceModuleAbstract;
import com.arrow.selene.engine.EngineConstants;
import com.arrow.selene.engine.service.DeviceService;
import com.arrow.selene.engine.service.ModuleService;
import com.arrow.selene.engine.state.State;
import com.arrow.selene.engine.state.StateChangeHandler;
import com.arrow.selene.engine.state.StateUpdate;
import com.arrow.selene.model.SeleneEventModel;
import com.arrow.selene.model.StatusModel;
import com.arrow.selene.service.DatabusService;
import com.fasterxml.jackson.core.type.TypeReference;

public class RfidReaderModule
        extends DeviceModuleAbstract<RfidReaderInfo, RfidReaderProperties, RfidReaderStates, RfidReaderData>
        implements StateChangeHandler<RfidReaderStates>, MessageListener {
    private static final int READER_ADDRESS = 0xff;
    private static TypeReference<Map<String, String>> mapTypeRef;
    private Socket socket;
    private Timer timer;
    private OutputStream outputStream;
    private InputStream inputStream;
    private byte[] length = new byte[2];
    private Map<String, TagModuleAbstract> tags = new HashMap<>();
    private CustomMqttClient mqttClient;
    private Map<Command, Response<?>> commands = new HashMap<>();

    public RfidReaderModule() {
        handlers = Collections.singletonList(this);
    }

    @Override
    protected void startDevice() {
        super.startDevice();
        connectSocket();
        getStates().initParams();
        initCommands();
        readParams();
        publishParams();
        ByteBuffer buffer = ByteBuffer.wrap(getStates().getParam(UserParameter.class).getBytes());
        int configVersion = buffer.getInt();
        if (configVersion < getInfo().getConfigVersion()) {
            writeParams();
        }
        updateSoftwareVersion();
        switchMode(ReaderMode.NOTIFICATION);
        initializeBuffer();
        startTimer();
        mqttConnect();
    }

    private void updateSoftwareVersion() {
        GetSoftwareVersionResponse softwareVersion = getSoftwareVersion();
        if (softwareVersion != null) {
            getInfo().setHardwareType(softwareVersion.getHardwareType());
            getInfo().setMaxRxBufferSize(softwareVersion.getMaxRxBufferSize());
            getInfo().setMaxTxBufferSize(softwareVersion.getMaxTxBufferSize());
            getInfo().setReaderFirmware(softwareVersion.getReaderFirmware());
            getInfo().setTransponderTypes(softwareVersion.getTransponderTypes());
            getInfo().setSoftwareRevision(softwareVersion.getSoftwareRevision());
            persistUpdatedDeviceInfo();
        }
    }

    @Override
    public void stop() {
        stopTimer();
        disconnectSocket();
        super.stop();
    }

    @Override
    public void handle(RfidReaderStates states, Map<String, State> statesMap) {
        String method = "handle";
        Set<Class<? extends ConfigParameter>> affected = new HashSet<>();
        Map<String, String> newStates = new HashMap<>();
        for (Entry<String, State> entry : statesMap.entrySet()) {
            Class<? extends ConfigParameter> clazz = getStates().getHandlers().get(entry.getKey());
            if (clazz == null) {
                continue;
            }
            ConfigParameter<?> param = getStates().getParam(clazz);
            if (param == null) {
                continue;
            }
            if (param.updateState(entry.getKey(), entry.getValue().getValue())) {
                newStates.put(entry.getKey(), entry.getValue().getValue());
                affected.add(clazz);
            }
        }
        UserParameter param = getStates().getParam(UserParameter.class);
        int configVersion = getInfo().getConfigVersion() + 1;
        getInfo().setConfigVersion(configVersion);
        persistUpdatedDeviceInfo();
        param.withBytes(ByteBuffer.allocate(14).putInt(configVersion).array());
        affected.add(UserParameter.class);
        for (Class<? extends ConfigParameter> clazz : affected) {
            try {
                writeParam(getStates().getParam(clazz));
            } catch (ReaderCommunicationException e) {
                logError(method, "failed to write parameter", e);
            }
        }
        SelfModule.getInstance().getAcnClient().getDeviceStateApi().createStateUpdate(getDevice().getHid(),
                (DeviceStateUpdateModel) new DeviceStateUpdateModel().withStates(newStates));
    }

    private void stopTimer() {
        timer.cancel();
        timer.purge();
    }

    private void connectSocket() {
        String method = "connectSocket";
        logInfo(method, "opening TCP socket %s:%d...", getInfo().getAddress(), getInfo().getPort());
        InetSocketAddress endpoint = new InetSocketAddress(getInfo().getAddress(), getInfo().getPort());
        while (true) {
            try {
                socket = new Socket();
                socket.connect(endpoint);
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
                break;
            } catch (IOException e) {
                logError(method, "cannot connect TCP socket, retrying in %s ms", 5000l);
                Utils.sleep(5000l);
            }
        }
    }

    private void disconnectSocket() {
        String method = "disconnectSocket";
        try {
            socket.close();
        } catch (IOException e) {
            logError(method, "error during closing TCP socket", e);
        }
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new DiagnosticTask(), 0L, getProperties().getDiagnosticIntervalSec() * 1000L);
        timer.schedule(new SyncTimeTask(), 0L, getProperties().getSyncTimeIntervalMin() * 60000L);
        timer.schedule(new PollingTask(), 0L, getProperties().getMaxPollingIntervalMs());
    }

    @Override
    public StatusModel performCommand(byte... bytes) {
        Map<String, String> params = JsonUtils.fromJsonBytes(bytes, getMapTypeRef());
        String command = params.get("command");
        switch (command) {
        case "setSystemTimeAndDate": {
            Map<String, String> payload = JsonUtils.fromJson(params.get("payload"), EngineConstants.MAP_TYPE_REF);
            setSystemTimeAndDate(payload.getOrDefault("date", Instant.now().toString()));
        }
        }
        return StatusModel.OK;
    }

    boolean setSystemTimeAndDate(String date) {
        String method = "setSystemTimeAndDate";
        logInfo(method, "setting reader time and date...");
        TemporalAccessor temporalAccessor = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(date);
        int century = temporalAccessor.get(ChronoField.YEAR) / 100;
        int year = temporalAccessor.get(ChronoField.YEAR) % 100;
        int month = temporalAccessor.get(ChronoField.MONTH_OF_YEAR);
        int day = temporalAccessor.get(ChronoField.DAY_OF_MONTH);
        int hour = temporalAccessor.get(ChronoField.HOUR_OF_DAY);
        int minute = temporalAccessor.get(ChronoField.MINUTE_OF_HOUR);
        int millisecond = temporalAccessor.get(ChronoField.SECOND_OF_MINUTE) * 1000
                + temporalAccessor.get(ChronoField.MILLI_OF_SECOND);

        boolean result = false;
        try {
            byte[] data = sendCommand(new SetSystemTimeAndDate()
                    .withDate(new Date().withCentury(century).withYear(year).withMonth(month).withDay(day))
                    .withTime(new Time().withHour(hour).withMinute(minute).withMillisecond(millisecond)));
            int status = new EmptyResponse().parse(Command.IGNORED_MODE, data).getStatus();
            result = status == 0;
            if (result) {
                logInfo(method, "successfully set reader time and date");
            } else {
                logError(method, "unable to set reader time and date, reader responded with status of 0x%02x", status);
            }
        } catch (ReaderCommunicationException e) {
            logError(method, "unable to set reader time and date", e);
        }
        return result;
    }

    private static TypeReference<Map<String, String>> getMapTypeRef() {
        return mapTypeRef != null ? mapTypeRef : (mapTypeRef = new TypeReference<Map<String, String>>() {
        });
    }

    private GetSoftwareVersionResponse getSoftwareVersion() {
        String method = "getSoftwareVersion";
        logInfo(method, "obtaining software version...");
        GetSoftwareVersionResponse result = null;
        try {
            GetSoftwareVersionResponse response = new GetSoftwareVersionResponse().parse(Command.IGNORED_MODE,
                    sendCommand(new GetSoftwareVersion()));
            int status = response.getStatus();
            if (status == 0) {
                logInfo(method, "software version obtained successfully");
                result = response;
            } else {
                logError(method, "unable to get software version, reader responded with status of 0x%02x", status);
            }
        } catch (ReaderCommunicationException e) {
            logError(method, "unable to get software version", e);
        }
        return result;
    }

    private void sendMessageToTag() {
        switchMode(ReaderMode.HOST);

        switchMode(ReaderMode.NOTIFICATION);
    }

    private void readParams() {
        try {
            for (ConfigParameter<?> parameter : getStates().getParams().values()) {
                readParam(parameter);
            }
        } catch (ReaderCommunicationException e) {
            throw new SeleneException("cannot read reader parameters", e);
        }
    }

    private void publishParams() {
        Map<String, String> states = new HashMap<>();
        for (ConfigParameter<?> parameter : getStates().getParams().values()) {
            states.putAll(parameter.getStates());
        }
        queueStatesForSending(new StateUpdate().withHid(getDevice().getHid()).withStates(states));
    }

    private void writeParams() {
        try {
            for (ConfigParameter<?> parameter : getStates().getParams().values()) {
                writeParam(parameter);
            }
        } catch (ReaderCommunicationException e) {
            throw new SeleneException("cannot write reader parameters", e);
        }
    }

    synchronized byte[] sendCommand(Command command) throws ReaderCommunicationException {
        try {
            outputStream.write(command.build(READER_ADDRESS));
        } catch (IOException e) {
            throw new ReaderCommunicationException(e);
        }
        return readResponse();
    }

    private void initCommands() {
        commands.put(new BaudRateDetection(), new EmptyResponse());
        commands.put(new ClearDataBuffer(), new EmptyResponse());
        commands.put(new ForceNotifyTrigger(), new EmptyResponse());
        commands.put(new GetInput(), new GetInputResponse());
        commands.put(new GetReaderInfo(), new GetReaderInfoResponse());
        commands.put(new GetSoftwareVersion(), new GetSoftwareVersionResponse());
        commands.put(new GetSystemTimeAndDate(), new GetSystemTimeAndDateResponse());
        commands.put(new GetSystemTimer(), new GetSystemTimerResponse());
        commands.put(new InitializeBuffer(), new EmptyResponse());
        commands.put(new Inventory(), new InventoryResponse());
        commands.put(new LockRegion(), new EmptyResponse());
        commands.put(new ReadBuffer(), new ReadBufferResponse());
        commands.put(new ReadConfiguration(), new ReadConfigurationResponse());
        commands.put(new ReadDataBufferInfo(), new ReadDataBufferInfoResponse());
        commands.put(new ReaderDiagnostic(), new ReaderDiagnosticResponse());
        commands.put(new ReaderLogin(), new EmptyResponse());
        commands.put(new ReadMultipleBlocks(), new ReadMultipleBlocksResponse());
        commands.put(new ResetConfiguration(), new EmptyResponse());
        commands.put(new RfControllerReset(), new EmptyResponse());
        commands.put(new RfOutputOnOff(), new EmptyResponse());
        commands.put(new RfReset(), new EmptyResponse());
        commands.put(new SetOutput(), new EmptyResponse());
        commands.put(new SetSystemTimeAndDate(), new EmptyResponse());
        commands.put(new SetSystemTimer(), new EmptyResponse());
        commands.put(new SystemReset(), new EmptyResponse());
        commands.put(new WriteConfiguration(), new EmptyResponse());
        commands.put(new WriteMultipleBlocks(), new WriteMultipleBlocksResponse());
    }

    @Override
    protected RfidReaderProperties createProperties() {
        return new RfidReaderProperties();
    }

    @Override
    protected RfidReaderInfo createInfo() {
        return new RfidReaderInfo();
    }

    @Override
    protected RfidReaderStates createStates() {
        return new RfidReaderStates();
    }

    private void switchMode(ReaderMode readerMode) {
        String method = "switchMode";
        logInfo(method, "checking reader mode...");
        InterfaceAndMode interfaceAndMode = getStates().getParam(InterfaceAndMode.class);
        if (interfaceAndMode.getReaderMode() == readerMode) {
            logInfo(method, "reader operates in %s mode", readerMode);
            return;
        }
        try {
            logInfo(method, "switching reader to %s mode...", readerMode);
            writeParam(interfaceAndMode.withReaderMode(readerMode));
            byte[] data = sendCommand(new RfControllerReset());
            EmptyResponse response = new EmptyResponse().parse(Command.IGNORED_MODE, data);
            if (response.getStatus() == 0) {
                logInfo(method, "reader mode successfully switched");
            }
        } catch (ReaderCommunicationException e) {
            logError(method, "unable to switch reader mode", e);
        }
    }

    private void initializeBuffer() {
        String method = "initializeBuffer";
        logInfo(method, "initializing data buffer...");
        try {
            if (new EmptyResponse().parse(Command.IGNORED_MODE, sendCommand(new InitializeBuffer())).getStatus() == 0) {
                logInfo(method, "successfully initialized data buffer");
            }
        } catch (ReaderCommunicationException e) {
            logError(method, "unable to initialize data buffer", e);
        }
    }

    private void readParam(ConfigParameter<?> parameter) throws ReaderCommunicationException {
        byte[] data = sendCommand(new ReadConfiguration()
                .withCfgAddress(new CfgAddress().withLocation(Location.EEPROM).withAddress(parameter.getId())));
        ReadConfigurationResponse response = new ReadConfigurationResponse().parse(Command.IGNORED_MODE, data);
        int status = response.getStatus();
        if (status == 0) {
            getStates().getParams().computeIfPresent(parameter.getClass(),
                    (key, value) -> value.parse(response.getParameter()));
        } else {
            throw new ReaderCommunicationException(
                    String.format("unable to read CFG%d parameter, reader responds with status of 0x%02x",
                            parameter.getId(), status));
        }
    }

    private void writeParam(ConfigParameter<?> parameter) throws ReaderCommunicationException {
        byte[] data = sendCommand(new WriteConfiguration()
                .withCfgAddress(new CfgAddress().withLocation(Location.EEPROM).withAddress(parameter.getId()))
                .withParameter(parameter));
        int status = new EmptyResponse().parse(Command.IGNORED_MODE, data).getStatus();
        if (status != 0) {
            throw new ReaderCommunicationException(
                    String.format("unable to write CFG%d parameter, reader responds with status of 0x%02x",
                            parameter.getId(), status));
        }
    }

    private byte[] readResponse() throws ReaderCommunicationException {
        int type = -1;
        try {
            int retries = 0;
            while (type == -1 && retries < 100) {
                type = inputStream.read();
                retries++;
                Utils.sleep(100l);
            }
        } catch (IOException e) {
            throw new ReaderCommunicationException(e);
        }
        if (type == -1) {
            throw new ReaderCommunicationException("no response from reader");
        }
        if (type == 2) {
            try {
                inputStream.read(length);
                byte[] data = new byte[(Byte.toUnsignedInt(length[0]) << 8) + Byte.toUnsignedInt(length[1]) - 3];
                inputStream.read(data);
                return data;
            } catch (IOException e) {
                throw new ReaderCommunicationException(e);
            }
        } else {
            throw new ReaderCommunicationException("unsupported protocol");
        }
    }

    List<Dataset> readDataBuffer() {
        String method = "readDataBuffer";
        List<Dataset> datasets = Collections.emptyList();
        try {
            ReadBufferResponse response = new ReadBufferResponse().parse(Command.IGNORED_MODE,
                    sendCommand(new ReadBuffer().withDataSets(255)));
            if (response.getStatus() == 0) {
                datasets = response.getDatasets();
            }
        } catch (ReaderCommunicationException e) {
            logError(method, "failed to read data buffer", e);
        }
        return datasets;
    }

    private void sendData(List<Dataset> datasets) {
        String method = "sendData";
        logInfo(method, "preparing data for sending...");
        for (Dataset dataset : datasets) {
            tags.computeIfAbsent(DatatypeConverter.printHexBinary(dataset.getId()), this::createGenericModule)
                    .queueDataForSending(new GenericTagData(dataset));
        }
        logInfo(method, "data sent successfully");
    }

    private TagModuleAbstract createGenericModule(String id) {
        String method = "createGenericModule";
        logInfo(method, "creating module for new tag...");
        TagModuleAbstract tagModule = new GenericTagModule(id);
        tagModule.init(new Properties());
        tags.put(id, tagModule);
        ModuleService.getInstance().registerModule(tagModule);
        ModuleService.getInstance().startModule(tagModule);
        tagModule.persistUpdatedDeviceInfo();
        logInfo(method, "module for new tag created successfully");
        return tagModule;
    }

    void clearDataBuffer() {
        String method = "clearDataBuffer";
        try {
            if (new EmptyResponse().parse(Command.IGNORED_MODE, sendCommand(new ClearDataBuffer())).getStatus() == 0) {
                logDebug(method, "buffer successfully cleared");
            }
        } catch (ReaderCommunicationException e) {
            logError(method, "failed to clean data buffer", e);
        }
    }

    private void mqttConnect() {
        String method = "mqttConnect";

        if (mqttClient != null) {
            logWarn(method, "mqttClient is already initialized!");
            return;
        }

        String clientId = getClass().getSimpleName();
        logInfo(method, "creating new MQTT client, clientId: %s", clientId);
        mqttClient = new CustomMqttClient(getProperties().getMqttUrl(), clientId);

        String userName = getProperties().getMqttUserName();
        String password = getProperties().getMqttPassword();
        if (StringUtils.isNotEmpty(userName)) {
            mqttClient.getOptions().setUserName(userName);
        }
        if (StringUtils.isNotEmpty(password)) {
            mqttClient.getOptions().setPassword(password.toCharArray());
        }

        String commandTopic = getProperties().getMqttCommandTopic();
        if (StringUtils.isBlank(commandTopic)) {
            throw new AcsSystemException("command topic is not defined!");
        }
        String telemetryTopic = getProperties().getMqttTelemetryTopic();
        if (StringUtils.isBlank(telemetryTopic)) {
            throw new AcsSystemException("telemetry topic is not defined!");
        }
        String routeCommandTopic = getProperties().getMqttRouteCommandTopic();
        if (StringUtils.isBlank(routeCommandTopic)) {
            throw new AcsSystemException("route command topic is not defined!");
        }
        mqttClient.setTopics(commandTopic, telemetryTopic, routeCommandTopic);
        mqttClient.setListener(this);

        logInfo(method, "connecting to MQTT broker: %s", getProperties().getMqttUrl());
        mqttClient.connect(false);
    }

    @Override
    public void processMessage(String topic, byte[] bytes) {
        String method = "processMessage";
        if (Objects.equals(topic, getProperties().getMqttTelemetryTopic())) {
            processTelemetry(bytes);
        } else if (Objects.equals(topic, getProperties().getMqttCommandTopic())) {
            processCommand(bytes);
        } else if (Objects.equals(topic, getProperties().getMqttRouteCommandTopic())) {
            routeCommand(bytes);
        } else {
            logWarn(method, "received message from unknown topic: %s", topic);
        }
    }

    private void processTelemetry(byte[] bytes) {
        String method = "processTelemetry";
        logInfo(method, "processing incoming telemetry ...");
        queueDataForSending(new IncomingTelemetryData(
                JsonUtils.fromJsonBytes(bytes, com.arrow.selene.device.harting.rfid.Utils.getMapTypeRef())));
    }

    private void processCommand(byte[] bytes) {
        String method = "processCommand";
        logInfo(method, "processing incoming command ...");
        Map<String, String> data = JsonUtils.fromJsonBytes(bytes,
                com.arrow.selene.device.harting.rfid.Utils.getMapTypeRef());
        String command = data.get("command");
        String responseTopic = getProperties().getMqttResponseTopic();
        switch (command) {
        case "WriteParameter": {
            String name = data.get("name");
            String value = data.get("value");
            Class<? extends ConfigParameter> clazz = getStates().getHandlers().get(name);
            if (clazz == null) {
                logError(method, "parameter unknown");
            }
            ConfigParameter<?> param = getStates().getParam(clazz);
            if (param == null) {
                logError(method, "parameter unknown");
            }
            param.updateState(name, value);
            try {
                writeParam(param);
                mqttClient.publish(responseTopic,
                        JsonUtils.toJsonBytes(new MqttResponse().withResponse(new EmptyResponse().withStatus(0))), 2);
            } catch (ReaderCommunicationException e) {
                logError(method, "failed to write parameter", e);
                mqttClient.publish(responseTopic,
                        JsonUtils.toJsonBytes(new MqttResponse().withMessage("failed to write parameter")), 2);
            }
            break;
        }
        case "ReadParameter": {
            String name = data.get("name");
            Class<? extends ConfigParameter> clazz = getStates().getHandlers().get(name);
            if (clazz == null) {
                logError(method, "parameter unknown");
            }
            ConfigParameter<?> param = getStates().getParam(clazz);
            if (param == null) {
                logError(method, "parameter unknown");
            }
            try {
                readParam(param);
                mqttClient.publish(responseTopic, param.getStates().get(name).getBytes(), 2);
            } catch (ReaderCommunicationException e) {
                logError(method, "failed to read parameter", e);
                mqttClient.publish(responseTopic,
                        JsonUtils.toJsonBytes(new MqttResponse().withMessage("failed to read parameter")), 2);
            }
            break;
        }
        default: {
            boolean found = false;
            for (Entry<Command, Response<?>> entry : commands.entrySet()) {
                if (Objects.equals(command, entry.getKey().getClass().getSimpleName())) {
                    found = true;
                    Command request = SerializationUtils.clone(entry.getKey());
                    try {
                        request = JsonUtils.fromJson(data.get("payload"), request.getClass());
                        Response response = SerializationUtils.clone(entry.getValue());
                        response = response.parse(request.getCommandMode(), sendCommand(request));
                        mqttClient.publish(responseTopic,
                                JsonUtils.toJsonBytes(new MqttResponse().withResponse(response)), 2);
                    } catch (Exception e) {
                        logError(method, "failed to build command", e);
                        mqttClient.publish(responseTopic,
                                JsonUtils.toJsonBytes(new MqttResponse().withMessage("failed to build command")), 2);
                    }
                    break;
                }
            }
            if (!found) {
                mqttClient.publish(responseTopic,
                        JsonUtils.toJsonBytes(new MqttResponse().withMessage("unknown command")), 2);
            }
            break;
        }
        }
    }

    private void routeCommand(byte[] bytes) {
        String method = "routeCommand";
        logInfo(method, "routing incoming command ...");
        SeleneEventModel model = JsonUtils.fromJsonBytes(bytes, SeleneEventModel.class);
        model.withName(SeleneEventNames.PERFORM_COMMAND);
        model.withResponseQueue(getProperties().getMqttRouteResponseTopic());
        Device device = DeviceService.getInstance().findByUid(model.getId());
        if (device != null) {
            DatabusService.getInstance().send(EngineConstants.deviceCommandQueue(device.getId()),
                    JsonUtils.toJsonBytes(model));
        } else {
            logError(method, "destination device with uid: %s not found", model.getId());
        }
    }

    private class DiagnosticTask extends TimerTask {
        @Override
        public void run() {
            String method = "run";
            logInfo(method, "performing reader diagnostic...");
            try {
                ReaderDiagnosticResponse response = new ReaderDiagnosticResponse().parse(Mode.ALL.getValue(),
                        sendCommand(new ReaderDiagnostic().withMode(Mode.ALL)));
                int status = response.getStatus();
                if (status == 0) {
                    queueDataForSending(new RfidReaderData(response));
                } else {
                    logError(method, "unable to read diagnostic info, reader responded with status of 0x%02x", status);
                }
            } catch (ReaderCommunicationException e) {
                logError(method, e);
            }
        }
    }

    private class SyncTimeTask extends TimerTask {
        @Override
        public void run() {
            String method = "run";
            logInfo(method, "syncing reader time and date...");
            setSystemTimeAndDate(Instant.now().toString());
        }
    }

    private class PollingTask extends TimerTask {
        @Override
        public void run() {
            List<Dataset> datasets = readDataBuffer();
            if (!datasets.isEmpty()) {
                sendData(datasets);
                clearDataBuffer();
            }
        }
    }
}

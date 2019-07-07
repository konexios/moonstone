package com.arrow.selene.device.zigbee;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.acn.client.utils.Utils;
import com.arrow.selene.SeleneException;
import com.arrow.selene.dao.DaoManager;
import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.ZclClusterInfo;
import com.arrow.selene.device.xbee.zcl.ZclClusters;
import com.arrow.selene.device.xbee.zcl.domain.security.zone.commands.SecurityZoneClusterCommands;
import com.arrow.selene.device.xbee.zcl.general.HaProfileCommands;
import com.arrow.selene.device.xbee.zdo.BindUnbindRequest;
import com.arrow.selene.device.xbee.zdo.ManagementLqiRequest;
import com.arrow.selene.device.xbee.zdo.NodeDescriptorRequest;
import com.arrow.selene.device.xbee.zdo.ZdoCommands;
import com.arrow.selene.device.xbee.zdo.ZdoConstants;
import com.arrow.selene.device.zigbee.data.BindingInfo;
import com.arrow.selene.device.zigbee.data.CommandInfo;
import com.arrow.selene.device.zigbee.handlers.commands.CommandHandler;
import com.arrow.selene.device.zigbee.handlers.commands.PermitJoinCommandHandler;
import com.arrow.selene.device.zigbee.handlers.commands.StartDiscoveryCommandHandler;
import com.arrow.selene.device.zigbee.handlers.commands.StopDiscoveryCommandHandler;
import com.arrow.selene.device.zigbee.handlers.messages.ActiveEndpointsResponseHandler;
import com.arrow.selene.device.zigbee.handlers.messages.BindResponseHandler;
import com.arrow.selene.device.zigbee.handlers.messages.ConfigureReportingResponseHandler;
import com.arrow.selene.device.zigbee.handlers.messages.DefaultResponseHandler;
import com.arrow.selene.device.zigbee.handlers.messages.DeviceAnnounceHandler;
import com.arrow.selene.device.zigbee.handlers.messages.DiscoverAttributesExtendedHandler;
import com.arrow.selene.device.zigbee.handlers.messages.DiscoverCommandsGeneratedHandler;
import com.arrow.selene.device.zigbee.handlers.messages.DiscoverCommandsReceivedHandler;
import com.arrow.selene.device.zigbee.handlers.messages.ManagementLqiResponseHandler;
import com.arrow.selene.device.zigbee.handlers.messages.MatchDescriptorRequestHandler;
import com.arrow.selene.device.zigbee.handlers.messages.MessageHandler;
import com.arrow.selene.device.zigbee.handlers.messages.NetworkAddressResponseHandler;
import com.arrow.selene.device.zigbee.handlers.messages.NodeDescriptorResponseHandler;
import com.arrow.selene.device.zigbee.handlers.messages.ReadAttributesResponseHandler;
import com.arrow.selene.device.zigbee.handlers.messages.ReportAttributesHandler;
import com.arrow.selene.device.zigbee.handlers.messages.SimpleDescriptorResponseHandler;
import com.arrow.selene.device.zigbee.handlers.messages.UnbindResponseHandler;
import com.arrow.selene.device.zigbee.handlers.messages.ZoneEnrollRequestHandler;
import com.arrow.selene.engine.service.DeviceService;
import com.arrow.selene.engine.service.ModuleService;
import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.ZigBeeDevice;
import com.digi.xbee.api.exceptions.TimeoutException;
import com.digi.xbee.api.exceptions.TransmitException;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.listeners.IExplicitDataReceiveListener;
import com.digi.xbee.api.models.ExplicitXBeeMessage;
import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.models.XBee64BitAddress;
import com.digi.xbee.api.utils.ByteUtils;

public class ZigBeeCoordinatorModule extends
        ZigBeeModuleAbstract<ZigBeeCoordinatorInfo, ZigBeeCoordinatorProperties, ZigBeeCoordinatorStates, ZigBeeHealthData> {
    private static final int SRC_ENDPOINT = 0;
    private static final int NI_LENGTH = 20;
    private static final String DEFAULT_ZIGBEE_ENCRYPTION_KEY = "ZigBeeAlliance09";
    private static final String SPECIFIC_PORTS_PROPERTY = "gnu.io.rxtx.SerialPorts";
    private static final ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>> DEFAULT_VALUE = new ImmutablePair<>(
            CommandInfo.UNKNOWN_COMMAND, null);

    Map<String, ZigBeeEndDeviceModule> devices = new ConcurrentHashMap<>();
    private ZigBeeDevice coordinator;
    private Thread discoveryThread = new Thread(new DiscoveryTask(), "discoveryThread");
    private Thread healthCheckThread = new Thread(new HealthCheckTask(), "healthCheckThread");
    private Thread messageDeliveryThread = new Thread(new MessageDeliveryTask(), "messageDeliveryThread");
    private Set<String> neighbors = new HashSet<>();
    private ExplicitDataReceiveListener listener = new ExplicitDataReceiveListener();

    @Override
    protected void startDevice() {
        super.startDevice();
        startCoordinator();
    }

    @Override
    public void init(Properties props) {
        listener.addZdoMessageHandler(ZdoCommands.NODE_DESCRIPTOR_RSP, new NodeDescriptorResponseHandler());
        listener.addZdoMessageHandler(ZdoCommands.ACTIVE_ENDPOINTS_RSP, new ActiveEndpointsResponseHandler());
        listener.addZdoMessageHandler(ZdoCommands.SIMPLE_DESCRIPTOR_RSP, new SimpleDescriptorResponseHandler());
        listener.addZdoMessageHandler(ZdoCommands.NETWORK_ADDRESS_RSP, new NetworkAddressResponseHandler());

        listener.addMessageHandler(HaProfileCommands.DISCOVER_ATTRIBUTES_EXTENDED_RSP,
                new DiscoverAttributesExtendedHandler());
        listener.addMessageHandler(HaProfileCommands.DISCOVER_COMMANDS_RECEIVED_RSP,
                new DiscoverCommandsReceivedHandler());
        listener.addMessageHandler(HaProfileCommands.DISCOVER_COMMANDS_GENERATED_RSP,
                new DiscoverCommandsGeneratedHandler());
        listener.addMessageHandler(HaProfileCommands.READ_ATTRIBUTES_RSP, new ReadAttributesResponseHandler());
        listener.addMessageHandler(HaProfileCommands.DEFAULT_RSP, new DefaultResponseHandler());

        listener.addZdoMessageHandler(ZdoCommands.BIND_RSP, new BindResponseHandler());
        listener.addZdoMessageHandler(ZdoCommands.UNBIND_RSP, new UnbindResponseHandler());
        listener.addMessageHandler(HaProfileCommands.CONFIGURE_REPORTING_RSP, new ConfigureReportingResponseHandler());

        listener.addZdoMessageHandler(ZdoCommands.MANAGEMENT_LQI_RSP, new ManagementLqiResponseHandler());
        listener.addZdoMessageHandler(ZdoCommands.END_DEVICE_ANNOUNCE_REQ, new DeviceAnnounceHandler());
        listener.addZdoMessageHandler(ZdoCommands.MATCH_DESCRIPTOR_REQ, new MatchDescriptorRequestHandler());

        listener.addMessageHandler(HaProfileCommands.REPORT_ATTRIBUTES, new ReportAttributesHandler());

        listener.addHaMessageHandler(ZclClusters.IAS_ZONE, Collections.singletonMap(
                SecurityZoneClusterCommands.ZONE_ENROLL_REQUEST_COMMAND_ID, new ZoneEnrollRequestHandler()));

        commandHandlers.put(StartDiscoveryCommandHandler.COMMAND, new StartDiscoveryCommandHandler(this));
        commandHandlers.put(StopDiscoveryCommandHandler.COMMAND, new StopDiscoveryCommandHandler(this));
        commandHandlers.put(PermitJoinCommandHandler.COMMAND, new PermitJoinCommandHandler(this));

        commandHandlers.values()
                .forEach(handler -> props.setProperty(
                        String.format("info.%s:%s", CommandHandler.COMMAND_PROPERTY_NAME, handler.getName()),
                        handler.getPayload()));
        super.init(props);
    }

    private void startCoordinator() {
        String method = "startCoordinator";
        String port = getInfo().getPort();
        int baudRate = getInfo().getBaudRate();
        System.setProperty(SPECIFIC_PORTS_PROPERTY, port);
        try {
            coordinator = new ZigBeeDevice(port, baudRate);
            logInfo(method, "opening ZigBee coordinator %s @ %d", port, baudRate);
            coordinator.open();
            coordinator.addExplicitDataListener(listener);
            logDebug(method, "ZigBee port opened!");
            configureCoordinator();
            loadZigbeeModules();
            logDebug(method, "starting discovery thread ...");
            discoveryThread.start();
            logDebug(method, "starting health check thread ...");
            healthCheckThread.start();
            logDebug(method, "starting message delivery thread ...");
            messageDeliveryThread.start();
            logInfo(method, "coordinator started");
            getInfo().setAddress(coordinator.get64BitAddress().toString());
            persistUpdatedDeviceInfo();
        } catch (Exception e) {
            throw new SeleneException("unable to connect to ZigBee coordinator", e);
        }
    }

    private void loadZigbeeModules() {
        String method = "loadZigBeeModules";
        Map<String, Properties> properties = new HashMap<>();
        if (!DaoManager.getInstance().isFreshDatabase()) {
            properties = loadPropsFromDB();
        }

        // debug info
        if (isDebugEnabled()) {
            for (Entry<String, Properties> entry : properties.entrySet()) {
                Properties p = entry.getValue();
                for (String name : p.stringPropertyNames()) {
                    logDebug(method, "%s: %s ---> %s", entry.getKey(), name, p.getProperty(name));
                }
            }
        }

        logInfo(method, "addresses: %s", StringUtils.join(getInfo().getAddresses().getValues(), ", "));
        for (String address : getInfo().getAddresses().getValues()) {
            ZigBeeEndDeviceModule module = createModule(address, properties.get(address));
            devices.put(address, module);
            module.persistUpdatedDeviceInfo();
            restoreBindings(address, module);
        }
    }

    private void restoreBindings(String address, ZigBeeEndDeviceModule module) {
        String method = "restoreBindings";
        Set<BindingInfo> bindings = module.getInfo().getBindings().getBindings();
        if (bindings != null) {
            XBee64BitAddress address64 = new XBee64BitAddress(address);
            for (BindingInfo info : bindings) {
                int clusterId = info.getClusterId();
                int srcEndpoint = info.getSrcEndpoint();
                int dstEndpoint = info.getDstEndpoint();
                String dstAddress = info.getDstAddress();
                logInfo(method,
                        "binding cluster: 0x%04x (%s) on endpoint: %d of device: %s to endpoint %d of device: %s",
                        clusterId, ZclClusters.getName(clusterId), srcEndpoint, address, dstEndpoint, dstAddress);
                byte sequence = module.nextSequence();
                module.addMessage(new MessageInfo(sequence, MessageType.ZDO_MESSAGE, address,
                        ZdoConstants.ZDO_DST_ENDPOINT, ZdoCommands.BIND_REQ, BindUnbindRequest.toPayload(sequence,
                                address64, srcEndpoint, clusterId, new XBee64BitAddress(dstAddress), dstEndpoint),
                        "BIND_REQ", ZdoCommands.BIND_RSP));
            }
        }
    }

    private static Map<String, Properties> loadPropsFromDB() {
        return DeviceService.getInstance().find(ZigBeeEndDeviceInfo.DEFAULT_DEVICE_TYPE).stream()
                .filter(device -> device.getHid() != null).map(com.arrow.selene.engine.Utils::getProperties)
                .collect(Collectors.toMap(props -> props.getProperty("address"), props -> props));
    }

    private void configureCoordinator() {
        String method = "configureCoordinator";
        try {
            if (coordinator.isOpen()) {
                // scan channels (bitmask: 0x7FFF=All channels)
                coordinator.setParameter("SC", ByteUtils.intToByteArray(0x7FFF));
                // ZigBee stack profile (0=Network Specific, 1=ZigBee-2006,
                // 2=ZigBee-PRO)
                coordinator.setParameter("ZS", ByteUtils.intToByteArray(0x02));
                // coordinator enabled
                coordinator.setParameter("CE", ByteUtils.intToByteArray(0x01));

                // extended PAN ID
                String extendedPanId = getProperties().getExtendedPanId();
                if (StringUtils.isNotEmpty(extendedPanId)) {
                    logInfo(method, "using custom extended PAN ID: %s", extendedPanId);
                    coordinator.setParameter("ID", ByteUtils.longToByteArray(Long.parseLong(extendedPanId)));
                }

                // node id
                String nodeIdentifier = getInfo().getUid();
                if (nodeIdentifier.length() > NI_LENGTH) {
                    nodeIdentifier = nodeIdentifier.substring(0, NI_LENGTH - 3) + "...";
                    logWarn(method, "Network Identifier must not exceed 20 chars, truncating to: '%s'", nodeIdentifier);
                }
                coordinator.setParameter("NI", nodeIdentifier.getBytes());
                // encryption enable
                coordinator.setParameter("EE", ByteUtils.intToByteArray(0x01));
                // encryption options
                coordinator.setParameter("EO", ByteUtils.intToByteArray(0x02));
                // encryption key
                coordinator.setParameter("KY", DEFAULT_ZIGBEE_ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8));
                // network encryption key (0=Random)
                coordinator.setParameter("NK", ByteUtils.intToByteArray(0x00));
                // API enable (0=Transparent Mode, 1=API enabled, 2=API enabled
                // with escaping)
                coordinator.setParameter("AP", ByteUtils.intToByteArray(0x01));
                // API output mode (0=Native, 1=Explicit, 2=Reserved, 3=Explicit
                // with ZDO Passthru)
                coordinator.setParameter("AO", ByteUtils.intToByteArray(0x03));

                // node join time (in seconds, 0xFF=always allowed)
                coordinator.setParameter("NJ", ByteUtils.intToByteArray(getProperties().isAutoJoin() ? 0xFF : 0x50));
                coordinator.executeParameter("WR");
                logDebug(method, "all parameters set!");
            } else {
                logWarn(method, "coordinator is not connected");
            }
        } catch (XBeeException e) {
            throw new SeleneException("unable to configure ZigBee coordinator", e);
        }
    }

    @Override
    public void stop() {
        super.stop();
        String method = "stop";
        Utils.shutdownThread(discoveryThread);
        if (coordinator != null) {
            logInfo(method, "closing coordinator ...");
            coordinator.close();
        }
    }

    @Override
    protected ZigBeeCoordinatorProperties createProperties() {
        return new ZigBeeCoordinatorProperties();
    }

    @Override
    protected ZigBeeCoordinatorInfo createInfo() {
        return new ZigBeeCoordinatorInfo();
    }

    @Override
    protected ZigBeeCoordinatorStates createStates() {
        return new ZigBeeCoordinatorStates();
    }

    public void addNeighbor(String address) {
        neighbors.add(address);
    }

    public boolean hasNeighbor(String address) {
        return neighbors.contains(address);
    }

    @Override
    public void sendZdoMessage(XBee64BitAddress address, int clusterId, byte[] payload) {
        sendZdoMessage(new RemoteXBeeDevice(coordinator, address), clusterId, payload);
    }

    public void sendZdoMessage(RemoteXBeeDevice device, int clusterId, byte[] payload) {
        sendMessage(device, ZdoConstants.ZDO_DST_ENDPOINT, clusterId, ZdoConstants.ZDO_PROFILE_ID, payload);
    }

    @Override
    public void sendMessage(XBee64BitAddress address, int dstEndpoint, int clusterId, int profileId, byte[] payload) {
        sendMessage(new RemoteXBeeDevice(coordinator, address), dstEndpoint, clusterId, profileId, payload);
    }

    public void sendNodeDescriptorRequest(ZigBeeEndDeviceModule module) {
        String method = "sendNodeDescriptorRequest";
        logInfo(method, "module: %s, address: %s, localAddress: %s", module.getName(), module.getInfo().getAddress(),
                module.getLocalAddress());
        if (!StringUtils.isEmpty(module.getLocalAddress())) {
            byte sequence = module.nextSequence();
            module.addMessage(new MessageInfo(sequence, MessageType.ZDO_MESSAGE, module.getInfo().getAddress(),
                    ZdoConstants.ZDO_DST_ENDPOINT, ZdoCommands.NODE_DESCRIPTOR_REQ,
                    NodeDescriptorRequest.toPayload(sequence, new XBee16BitAddress(module.getLocalAddress())),
                    "NODE_DESCRIPTOR_REQ", ZdoCommands.NODE_DESCRIPTOR_RSP));
        } else {
            logError(method, "EndDevice does not have local address, possibly has not rejoined yet!");
        }
    }

    public void sendMessage(RemoteXBeeDevice device, int dstEndpoint, int clusterId, int profileId, byte[] payload) {
        String method = "sendMessage";
        try {
            coordinator.sendExplicitData(device, SRC_ENDPOINT, dstEndpoint, clusterId, profileId, payload);
        } catch (TimeoutException ignored) {
            // ignore
        } catch (TransmitException ignored) {
            logWarn(method, "device %s unreachable", device.get64BitAddress());
        } catch (Throwable e) {
            logError(method, e);
        }
    }

    public ZigBeeEndDeviceModule getModule(String address) {
        return devices.get(address);
    }

    private class DiscoveryTask implements Runnable {
        @Override
        public void run() {
            String method = "DiscoveryTask.run";
            while (!isShuttingDown()) {
                if (getProperties().isDiscovering()) {
                    if (!coordinator.getNetwork().isDiscoveryRunning()) {
                        logInfo(method, "starting discovery");
                        coordinator.getNetwork().startDiscoveryProcess();
                    }
                    List<RemoteXBeeDevice> currentDevices = coordinator.getNetwork().getDevices();
                    logInfo(method, "device list: %d", currentDevices.size());
                    for (RemoteXBeeDevice device : currentDevices) {
                        String address = device.get64BitAddress().toString();
                        ZigBeeEndDeviceModule module = devices.get(address);
                        if (module != null) {
                            logDebug(method, "existing device: %s", address);
                            module.setLocalAddress(device.get16BitAddress().toString());
                        } else {
                            if (Objects.equals(device.get16BitAddress(), XBee16BitAddress.COORDINATOR_ADDRESS)) {
                                logDebug(method, "skipping coordinator");
                            } else {
                                logInfo(method, "new device found: %s", address);
                                module = createModule(address, new Properties());
                                module.setLocalAddress(device.get16BitAddress().toString());
                                devices.put(address, module);
                                sendNodeDescriptorRequest(module);
                                getInfo().getAddresses().getValues().add(address);
                                persistUpdatedDeviceInfo();
                            }
                        }
                    }
                }
                Utils.sleep(getProperties().getNetworkDiscoveryInterval());
            }
            logInfo(method, "complete");
        }
    }

    private ZigBeeEndDeviceModule createModule(String address, Properties properties) {
        String method = "createModule";
        ZigBeeEndDeviceModule module = new ZigBeeEndDeviceModule(this, address);
        module.init(properties);
        ModuleService.getInstance().registerModule(module);
        ModuleService.getInstance().startModule(module);
        logInfo(method, "new ZigBee module initialized: %s", module.getInfo().getUid());
        return module;
    }

    private class HealthCheckTask implements Runnable {
        @Override
        public void run() {
            String method = "HealthCheckTask.run";
            while (!isShuttingDown()) {
                neighbors.clear();
                sendZdoMessage(XBee64BitAddress.COORDINATOR_ADDRESS, ZdoCommands.MANAGEMENT_LQI_REQ,
                        ManagementLqiRequest.toPayload(ZigBeeCoordinatorModule.this.nextSequence(), 0));
                Utils.sleep(getProperties().getHealthCheckInterval() * 60000L);
            }
            logInfo(method, "complete");
        }
    }

    private class MessageDeliveryTask implements Runnable {
        @Override
        public void run() {
            String method = "MessageDeliveryTask.run";
            while (!isShuttingDown()) {
                for (ZigBeeEndDeviceModule module : devices.values()) {
                    for (MessageInfo info : new ArrayList<>(module.getMessages().values())) {
                        if (info.getNumberOfRetries() == 0) {
                            logWarn(method, "no response received after many retries!");
                            module.removeMessage(info.getSequence());
                        } else if (info.shouldWeRetry()) {
                            logInfo(method, "retrying message now: %s", info.toString());
                            module.sendMessage(info);
                        }
                    }
                }
                Utils.sleep(getProperties().getMessageSendingRetryInterval());
            }
        }
    }

    private class ExplicitDataReceiveListener implements IExplicitDataReceiveListener {
        private Map<Integer, MessageHandler> messageHandlers = new HashMap<>();
        private Map<Integer, MessageHandler> zdoMessageHandlers = new HashMap<>();
        private Map<Integer, Map<Integer, MessageHandler>> haMessageHandlers = new HashMap<>();

        public void addMessageHandler(Integer commandId, MessageHandler handler) {
            messageHandlers.put(commandId, handler);
        }

        public void addZdoMessageHandler(Integer clusterId, MessageHandler handler) {
            zdoMessageHandlers.put(clusterId, handler);
        }

        public void addHaMessageHandler(Integer clusterId, Map<Integer, MessageHandler> handler) {
            haMessageHandlers.put(clusterId, handler);
        }

        @Override
        public void explicitDataReceived(ExplicitXBeeMessage explicitXBeeMessage) {
            String method = "ExplicitDataReceiveListener.explicitDataReceived";
            try {
                int profileId = explicitXBeeMessage.getProfileID();
                int clusterId = explicitXBeeMessage.getClusterID();
                String address = explicitXBeeMessage.getDevice().get64BitAddress().toString();
                logInfo(method, "profileId: 0x%04x, clusterId: 0x%04x, address: %s", profileId, clusterId, address);
                ZigBeeEndDeviceModule module = devices.get(address);
                if (module != null) {
                    logInfo(method, "found module: %s", module.getInfo().getAddress());
                }
                if (profileId == ZdoConstants.ZDO_PROFILE_ID) {
                    if (module != null) {
                        module.removeMessage(explicitXBeeMessage.getData()[0]);
                    }
                    handleZdoMessage(explicitXBeeMessage, clusterId);
                } else {
                    int manufacturerShift = (explicitXBeeMessage.getData()[0] & 0x04) == 0 ? 0 : 2;
                    int commandId = explicitXBeeMessage.getData()[2 + manufacturerShift];
                    if (module != null) {
                        module.removeMessage(explicitXBeeMessage.getData()[1 + manufacturerShift]);
                    }
                    if ((explicitXBeeMessage.getData()[0] & 0x01) == 0) {
                        handleEntireHaProfileMessage(explicitXBeeMessage, commandId);
                    } else {
                        handleProfileSpecificMessage(explicitXBeeMessage, commandId);
                    }
                }
            } catch (Throwable t) {
                logError(method, "error processing explicit message", t);
            }
        }

        private void handleZdoMessage(ExplicitXBeeMessage message, int commandId) {
            String method = "handleZdoMessage";
            MessageHandler handler = zdoMessageHandlers.get(commandId);
            if (handler == null) {
                logWarn(method, "no handler for ZDO command: 0x%04x (%s)", commandId, ZdoCommands.getName(commandId));
            } else {
                logDebug(method, "found ZDO message handler: %s", handler.getClass().getName());
                handler.handle(message, ZigBeeCoordinatorModule.this);
            }
        }

        private void handleEntireHaProfileMessage(ExplicitXBeeMessage message, int commandId) {
            String method = "handleEntireHaProfileMessage";
            logInfo(method, "received entire HA profile command: 0x%02x (%s)", commandId,
                    HaProfileCommands.getName(commandId));
            MessageHandler handler = messageHandlers.get(commandId);
            if (handler == null) {
                logWarn(method, "no handler found for entire HA profile command: 0x%02x (%s)", commandId,
                        HaProfileCommands.getName(commandId));
            } else {
                logDebug(method, "found entire HA profile message handler: %s", handler.getClass().getName());
                handler.handle(message, ZigBeeCoordinatorModule.this);
            }
        }

        private void handleProfileSpecificMessage(ExplicitXBeeMessage message, int commandId) {
            int clusterID = message.getClusterID();
            String method = "handleProfileSpecificMessage";
            ZclClusterInfo zclCluster = ZclClusters.getCluster(clusterID);
            if (zclCluster == null) {
                logWarn(method, "received command: 0x%02x of unknown clusterId: 0x%04x", commandId, clusterID);
                return;
            }
            String commandName;
            if ((message.getData()[0] & 0x08) == 0) {
                commandName = zclCluster.getReceivedCommands().getOrDefault(commandId, DEFAULT_VALUE).getLeft();
            } else {
                commandName = zclCluster.getGeneratedCommands().getOrDefault(commandId, CommandInfo.UNKNOWN_COMMAND);
            }
            logInfo(method, "received command: 0x%02x (%s) of cluster: 0x%04x (%s)", commandId, commandName,
                    zclCluster.getId(), zclCluster.getName());
            Map<Integer, MessageHandler> handlerMap = haMessageHandlers.get(clusterID);
            if (handlerMap == null) {
                logWarn(method, "no handler found for command: 0x%02x (%s) of cluster: 0x%04x (%s)", commandId,
                        commandName, clusterID, ZclClusters.getName(clusterID));
                return;
            }
            MessageHandler handler = handlerMap.get(commandId);
            if (handler == null) {
                logWarn(method, "no handler found for command: 0x%02x (%s) of cluster: 0x%04x (%s)", commandId,
                        commandName, clusterID, ZclClusters.getName(clusterID));
                return;
            }
            handler.handle(message, ZigBeeCoordinatorModule.this);
        }
    }
}

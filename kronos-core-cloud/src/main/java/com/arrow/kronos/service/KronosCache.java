package com.arrow.kronos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.arrow.kronos.data.AwsAccount;
import com.arrow.kronos.data.AzureAccount;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.DeviceState;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.IbmAccount;
import com.arrow.kronos.data.KronosApplication;
import com.arrow.kronos.data.KronosUser;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.data.NodeType;
import com.arrow.kronos.data.SocialEventDevice;
import com.arrow.kronos.data.SocialEventRegistration;
import com.arrow.kronos.data.TelemetryReplayType;
import com.arrow.kronos.data.TelemetryUnit;
import com.arrow.kronos.data.TestProcedure;
import com.arrow.kronos.data.action.GlobalAction;
import com.arrow.kronos.data.action.GlobalActionType;
import com.arrow.pegasus.service.CacheServiceAbstract;

@Service
public class KronosCache extends CacheServiceAbstract {
    public static final String KRONOS_TEST_PROCEDURE_HIDS = "kronos_test_procedure_hids";
    public static final String KRONOS_TEST_PROCEDURE_IDS = "kronos_test_procedure_ids";

    public static final String KRONOS_RP_DEVICE_IDS = "kronos_rp_device_ids";
    public static final String KRONOS_RP_APPLICATION_IDS = "kronos_rp_application_ids";

    public static final String KRONOS_DEVICE_STATE_HIDS = "kronos_device_state_hids";
    public static final String KRONOS_DEVICE_STATE_IDS = "kronos_device_state_ids";

    public static final String KRONOS_TELEMETRY_UNIT_SYSTEM_NAMES = "kronos_telemetry_unit_system_names";
    public static final String KRONOS_TELEMETRY_UNIT_IDS = "kronos_telemetry_unit_ids";
    public static final String KRONOS_TELEMETRY_UNIT_HIDS = "kronos_telemetry_unit_hids";

    public static final String KRONOS_TELEMETRY_REPLAY_TYPE_NAMES = "kronos_telemetry_replay_type_names";
    public static final String KRONOS_TELEMETRY_REPLAY_TYPE_IDS = "kronos_telemetry_replay_type_ids";

    public static final String KRONOS_NODE_NAMES = "kronos_node_names";
    public static final String KRONOS_NODE_HIDS = "kronos_node_hids";
    public static final String KRONOS_NODE_IDS = "kronos_node_ids";

    public static final String KRONOS_NODE_TYPE_NAMES = "kronos_node_type_names";
    public static final String KRONOS_NODE_TYPE_IDS = "kronos_node_type_ids";

    public static final String KRONOS_DEVICE_ACTION_TYPE_SYSTEM_NAMES = "kronos_device_action_type_system_names";
    public static final String KRONOS_DEVICE_ACTION_TYPE_IDS = "kronos_device_action_type_ids";

    public static final String KRONOS_DEVICE_IDS = "kronos_device_ids";
    public static final String KRONOS_DEVICE_HIDS = "kronos_device_hids";

    public static final String KRONOS_GATEWAY_IDS = "kronos_gateway_ids";
    public static final String KRONOS_GATEWAY_HIDS = "kronos_gateway_hids";

    public static final String KRONOS_DEVICE_TYPE_HIDS = "kronos_device_type_hids";
    public static final String KRONOS_DEVICE_TYPE_NAMES = "kronos_device_type_names";
    public static final String KRONOS_DEVICE_TYPE_IDS = "kronos_device_type_ids";

    public static final String KRONOS_AZURE_ACCOUNT_IDS = "kronos_azure_account_ids";
    public static final String KRONOS_AZURE_ACCOUNT_HIDS = "kronos_azure_account_hids";

    public static final String KRONOS_IBM_ACCOUNT_HIDS = "kronos_ibm_account_hids";
    public static final String KRONOS_IBM_ACCOUNT_IDS = "kronos_ibm_account_ids";

    public static final String KRONOS_AWS_ACCOUNT_HIDS = "kronos_aws_account_hids";
    public static final String KRONOS_AWS_ACCOUNT_IDS = "kronos_aws_account_ids";

    public static final String KRONOS_USER_HIDS = "kronos_user_hids";
    public static final String KRONOS_USER_IDS = "kronos_user_ids";

    public static final String KRONOS_APPLICATION_APPLICATION_IDS = "kronos_application_application_ids";
    public static final String KRONOS_APPLICATION_HIDS = "kronos_application_hids";
    public static final String KRONOS_APPLICATION_IDS = "kronos_application_ids";

    public static final String KRONOS_GLOBAL_ACTION_TYPE_IDS = "kronos_global_action_type_ids";
    public static final String KRONOS_GLOBAL_ACTION_TYPE_HIDS = "kronos_global_action_type_hids";
    public static final String KRONOS_GLOBAL_ACTION_TYPE_SYSTEM_NAMES = "kronos_global_action_type_system_names";
    public static final String KRONOS_GLOBAL_ACTION_TYPES = "kronos_global_action_types";
    public static final String KRONOS_GLOBAL_ACTION_IDS = "kronos_global_action_ids";
    public static final String KRONOS_GLOBAL_ACTION_HIDS = "kronos_global_action_hids";
    public static final String KRONOS_GLOBAL_ACTION_SYSTEM_NAMES = "kronos_global_action_system_names";

    public static final String KRONOS_SOCIAL_EVENT_DEVICE_HIDS = "kronos_social_event_device_hids";
    public static final String KRONOS_SOCIAL_EVENT_DEVICE_IDS = "kronos_social_event_device_ids";

    public static final String KRONOS_SOCIAL_EVENT_REGISTRATION_HIDS = "kronos_social_event_registration_hids";
    public static final String KRONOS_SOCIAL_EVENT_REGISTRATION_IDS = "kronos_social_event_registration_ids";
    public static final String KRONOS_SOCIAL_EVENT_REGISTRATION_APPLICATION_IDS = "kronos_social_event_registration_application_ids";

    @Autowired
    private KronosCacheProxy proxy;

    @Cacheable(KRONOS_APPLICATION_IDS)
    public KronosApplication findKronosApplicationById(String id) {
        return proxy.findKronosApplicationById(id);
    }

    @Cacheable(KRONOS_APPLICATION_HIDS)
    public KronosApplication findKronosApplicationByHid(String hid) {
        return proxy.findKronosApplicationByHid(hid);
    }

    @Cacheable(KRONOS_APPLICATION_APPLICATION_IDS)
    public KronosApplication findKronosApplicationByApplicationId(String applicationId) {
        return proxy.findKronosApplicationByApplicationId(applicationId);
    }

    @Caching(evict = {
            @CacheEvict(value = KRONOS_APPLICATION_IDS, key = "#kronosApplication.id", condition = "#kronosApplication != null"),
            @CacheEvict(value = KRONOS_APPLICATION_HIDS, key = "#kronosApplication.hid", condition = "#kronosApplication != null"),
            @CacheEvict(value = KRONOS_APPLICATION_APPLICATION_IDS, key = "#kronosApplication.applicationId", condition = "#kronosApplication != null") })
    public void clearKronosApplication(KronosApplication kronosApplication) {
        String method = "clearKronosApplication";
        if (kronosApplication != null) {
            logInfo(method, "id: %s", kronosApplication.getId());
            notifyCacheUpdate(KRONOS_APPLICATION_IDS, kronosApplication.getId());
            notifyCacheUpdate(KRONOS_APPLICATION_HIDS, kronosApplication.getHid());
            notifyCacheUpdate(KRONOS_APPLICATION_APPLICATION_IDS, kronosApplication.getApplicationId());
        }
    }

    @CacheEvict(cacheNames = { KRONOS_APPLICATION_IDS, KRONOS_APPLICATION_HIDS,
            KRONOS_APPLICATION_APPLICATION_IDS }, allEntries = true)
    public void clearKronosApplications() {
        String method = "clearKronosApplications";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_APPLICATION_IDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_APPLICATION_HIDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_APPLICATION_APPLICATION_IDS, ALL_KEYS);
    }

    @Cacheable(KRONOS_USER_IDS)
    public KronosUser findKronosUserById(String id) {
        return proxy.findKronosUserById(id);
    }

    @Cacheable(KRONOS_USER_HIDS)
    public KronosUser findKronosUserByHid(String hid) {
        return proxy.findKronosUserByHid(hid);
    }

    @Caching(evict = { @CacheEvict(value = KRONOS_USER_IDS, key = "#kronosUser.id", condition = "#kronosUser != null"),
            @CacheEvict(value = KRONOS_USER_HIDS, key = "#kronosUser.hid", condition = "#kronosUser != null") })
    public void clearKronosUser(KronosUser kronosUser) {
        String method = "clearKronosUser";
        if (kronosUser != null) {
            logInfo(method, "id: %s", kronosUser.getId());
            notifyCacheUpdate(KRONOS_USER_IDS, kronosUser.getId());
            notifyCacheUpdate(KRONOS_USER_HIDS, kronosUser.getHid());
        }
    }

    @CacheEvict(cacheNames = { KRONOS_USER_IDS, KRONOS_USER_HIDS }, allEntries = true)
    public void clearKronosUsers() {
        String method = "clearKronosUsers";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_USER_IDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_USER_HIDS, ALL_KEYS);
    }

    @Cacheable(KRONOS_AWS_ACCOUNT_IDS)
    public AwsAccount findAwsAccountById(String id) {
        return proxy.findAwsAccountById(id);
    }

    @Cacheable(KRONOS_AWS_ACCOUNT_HIDS)
    public AwsAccount findAwsAccountByHid(String hid) {
        return proxy.findAwsAccountByHid(hid);
    }

    @Caching(evict = { @CacheEvict(value = KRONOS_AWS_ACCOUNT_IDS, key = "#account.id", condition = "#account != null"),
            @CacheEvict(value = KRONOS_AWS_ACCOUNT_HIDS, key = "#account.hid", condition = "#account != null") })
    public void clearAwsAccount(AwsAccount account) {
        String method = "clearAwsAccount";
        if (account != null) {
            logInfo(method, "id: %s", account.getId());
            notifyCacheUpdate(KRONOS_AWS_ACCOUNT_IDS, account.getId());
            notifyCacheUpdate(KRONOS_AWS_ACCOUNT_HIDS, account.getHid());
        }
    }

    @CacheEvict(cacheNames = { KRONOS_AWS_ACCOUNT_IDS, KRONOS_AWS_ACCOUNT_HIDS }, allEntries = true)
    public void clearAwsAccounts() {
        String method = "clearAwsAccounts";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_AWS_ACCOUNT_IDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_AWS_ACCOUNT_HIDS, ALL_KEYS);
    }

    @Cacheable(KRONOS_IBM_ACCOUNT_IDS)
    public IbmAccount findIbmAccountById(String id) {
        return proxy.findIbmAccountById(id);
    }

    @Cacheable(KRONOS_IBM_ACCOUNT_HIDS)
    public IbmAccount findIbmAccountByHid(String hid) {
        return proxy.findIbmAccountByHid(hid);
    }

    @Caching(evict = { @CacheEvict(value = KRONOS_IBM_ACCOUNT_IDS, key = "#account.id", condition = "#account != null"),
            @CacheEvict(value = KRONOS_IBM_ACCOUNT_HIDS, key = "#account.hid", condition = "#account != null") })
    public void clearIbmAccount(IbmAccount account) {
        String method = "clearIbmAccount";
        if (account != null) {
            logInfo(method, "id: %s", account.getId());
            notifyCacheUpdate(KRONOS_IBM_ACCOUNT_IDS, account.getId());
            notifyCacheUpdate(KRONOS_IBM_ACCOUNT_HIDS, account.getHid());
        }
    }

    @CacheEvict(cacheNames = { KRONOS_IBM_ACCOUNT_IDS, KRONOS_IBM_ACCOUNT_HIDS }, allEntries = true)
    public void clearIbmAccounts() {
        String method = "clearIbmAccounts";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_IBM_ACCOUNT_IDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_IBM_ACCOUNT_HIDS, ALL_KEYS);
    }

    @Cacheable(KRONOS_AZURE_ACCOUNT_IDS)
    public AzureAccount findAzureAccountById(String id) {
        return proxy.findAzureAccountById(id);
    }

    @Cacheable(KRONOS_AZURE_ACCOUNT_HIDS)
    public AzureAccount findAzureAccountByHid(String hid) {
        return proxy.findAzureAccountByHid(hid);
    }

    @Caching(evict = {
            @CacheEvict(value = KRONOS_AZURE_ACCOUNT_IDS, key = "#account.id", condition = "#account != null"),
            @CacheEvict(value = KRONOS_AZURE_ACCOUNT_HIDS, key = "#account.hid", condition = "#account != null") })
    public void clearAzureAccount(AzureAccount account) {
        String method = "clearAzureAccount";
        if (account != null) {
            logInfo(method, "id: %s", account.getId());
            notifyCacheUpdate(KRONOS_AZURE_ACCOUNT_IDS, account.getId());
            notifyCacheUpdate(KRONOS_AZURE_ACCOUNT_HIDS, account.getHid());
        }
    }

    @CacheEvict(cacheNames = { KRONOS_AZURE_ACCOUNT_IDS, KRONOS_AZURE_ACCOUNT_HIDS }, allEntries = true)
    public void clearAzureAccounts() {
        String method = "clearAzureAccounts";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_AZURE_ACCOUNT_IDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_AZURE_ACCOUNT_HIDS, ALL_KEYS);
    }

    @Cacheable(KRONOS_DEVICE_TYPE_IDS)
    public DeviceType findDeviceTypeById(String id) {
        return proxy.findDeviceTypeById(id);
    }

    @Cacheable(value = KRONOS_DEVICE_TYPE_NAMES, key = "#p0.concat('-').concat(#p1)", unless = "#result == null")
    public DeviceType findDeviceTypeByName(String applicationId, String name) {
        return proxy.findDeviceTypeByName(applicationId, name);
    }

    @Cacheable(KRONOS_DEVICE_TYPE_HIDS)
    public DeviceType findDeviceTypeByHid(String hid) {
        return proxy.findDeviceTypeByHid(hid);
    }

    @Caching(evict = { @CacheEvict(value = KRONOS_DEVICE_TYPE_IDS, key = "#type.id", condition = "#type != null"),
            @CacheEvict(value = KRONOS_DEVICE_TYPE_NAMES, key = "#type.applicationId.concat('-').concat(#type.name)", condition = "#type != null"),
            @CacheEvict(value = KRONOS_DEVICE_TYPE_HIDS, key = "#type.hid", condition = "#type != null") })
    public void clearDeviceType(DeviceType type) {
        String method = "clearDeviceType";
        if (type != null) {
            logInfo(method, "id: %s", type.getId());
            notifyCacheUpdate(KRONOS_DEVICE_TYPE_IDS, type.getId());
            notifyCacheUpdate(KRONOS_DEVICE_TYPE_NAMES, type.getName());
            notifyCacheUpdate(KRONOS_DEVICE_TYPE_HIDS, type.getHid());
        }
    }

    @CacheEvict(cacheNames = { KRONOS_DEVICE_TYPE_IDS, KRONOS_DEVICE_TYPE_NAMES,
            KRONOS_DEVICE_TYPE_HIDS }, allEntries = true)
    public void clearDeviceTypes() {
        String method = "clearDeviceTypes";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_DEVICE_TYPE_IDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_DEVICE_TYPE_NAMES, ALL_KEYS);
        notifyCacheUpdate(KRONOS_DEVICE_TYPE_HIDS, ALL_KEYS);
    }

    @Cacheable(KRONOS_GATEWAY_HIDS)
    public Gateway findGatewayByHid(String hid) {
        return proxy.findGatewayByHid(hid);
    }

    @Cacheable(KRONOS_GATEWAY_IDS)
    public Gateway findGatewayById(String id) {
        return proxy.findGatewayById(id);
    }

    @Caching(evict = { @CacheEvict(value = KRONOS_GATEWAY_IDS, key = "#gateway.id", condition = "#gateway != null"),
            @CacheEvict(value = KRONOS_GATEWAY_HIDS, key = "#gateway.hid", condition = "#gateway != null") })
    public void clearGateway(Gateway gateway) {
        String method = "clearGateway";
        if (gateway != null) {
            logInfo(method, "id: %s", gateway.getId());
            notifyCacheUpdate(KRONOS_GATEWAY_IDS, gateway.getId());
            notifyCacheUpdate(KRONOS_GATEWAY_HIDS, gateway.getHid());
        }
    }

    @CacheEvict(cacheNames = { KRONOS_GATEWAY_HIDS, KRONOS_GATEWAY_IDS }, allEntries = true)
    public void clearGateways() {
        String method = "clearGateways";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_GATEWAY_HIDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_GATEWAY_IDS, ALL_KEYS);
    }

    @Cacheable(KRONOS_DEVICE_HIDS)
    public Device findDeviceByHid(String hid) {
        return proxy.findDeviceByHid(hid);
    }

    @Cacheable(KRONOS_DEVICE_IDS)
    public Device findDeviceById(String id) {
        return proxy.findDeviceById(id);
    }

    @CacheEvict(cacheNames = { KRONOS_DEVICE_IDS, KRONOS_DEVICE_HIDS }, allEntries = true)
    public void clearDevices() {
        String method = "clearDevices";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_DEVICE_IDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_DEVICE_HIDS, ALL_KEYS);
    }

    @Caching(evict = { @CacheEvict(value = KRONOS_DEVICE_IDS, key = "#device.id", condition = "#device != null"),
            @CacheEvict(value = KRONOS_DEVICE_HIDS, key = "#device.hid", condition = "#device != null") })
    public void clearDevice(Device device) {
        String method = "clearDevice";
        if (device != null) {
            logInfo(method, "id: %s", device.getId());
            notifyCacheUpdate(KRONOS_DEVICE_IDS, device.getId());
            notifyCacheUpdate(KRONOS_DEVICE_HIDS, device.getHid());
        }
    }

    @Cacheable(KRONOS_DEVICE_ACTION_TYPE_IDS)
    public DeviceActionType findDeviceActionTypeById(String id) {
        return proxy.findDeviceActionTypeById(id);
    }

    @Cacheable(value = KRONOS_DEVICE_ACTION_TYPE_SYSTEM_NAMES, key = "#p0.concat('-').concat(#p1)", unless = "#result == null")
    public DeviceActionType findDeviceActionTypeBySystemName(String applicationId, String systemName) {
        return proxy.findDeviceActionTypeBySystemName(applicationId, systemName);
    }

    @Caching(evict = {
            @CacheEvict(value = KRONOS_DEVICE_ACTION_TYPE_IDS, key = "#type.id", condition = "#type != null"),
            @CacheEvict(value = KRONOS_DEVICE_ACTION_TYPE_SYSTEM_NAMES, key = "#type.applicationId.concat('-').concat(#type.systemName)", condition = "#type != null") })
    public void clearDeviceActionType(DeviceActionType type) {
        String method = "clearDeviceActionType";
        if (type != null) {
            logInfo(method, "id: %s", type.getId());
            notifyCacheUpdate(KRONOS_DEVICE_ACTION_TYPE_IDS, type.getId());
            notifyCacheUpdate(KRONOS_DEVICE_ACTION_TYPE_SYSTEM_NAMES,
                    String.format("%s-%s", type.getApplicationId(), type.getSystemName()));
        }
    }

    @CacheEvict(cacheNames = { KRONOS_DEVICE_ACTION_TYPE_IDS,
            KRONOS_DEVICE_ACTION_TYPE_SYSTEM_NAMES }, allEntries = true)
    public void clearDeviceActionTypes() {
        String method = "clearDeviceActionTypes";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_DEVICE_ACTION_TYPE_IDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_DEVICE_ACTION_TYPE_SYSTEM_NAMES, ALL_KEYS);
    }

    @Cacheable(KRONOS_NODE_TYPE_IDS)
    public NodeType findNodeTypeById(String id) {
        return proxy.findNodeTypeById(id);
    }

    @Cacheable(value = KRONOS_NODE_TYPE_NAMES, key = "#p0.concat('-').concat(#p1)", unless = "#result == null")
    public NodeType findNodeTypeByName(String applicationId, String name) {
        return proxy.findNodeTypeByName(applicationId, name);
    }

    @Caching(evict = { @CacheEvict(value = KRONOS_NODE_TYPE_IDS, key = "#type.id", condition = "#type != null"),
            @CacheEvict(value = KRONOS_NODE_TYPE_NAMES, key = "#type.applicationId.concat('-').concat(#type.name)", condition = "#type != null") })
    public void clearNodeType(NodeType type) {
        String method = "clearNodeType";
        if (type != null) {
            logInfo(method, "id: %s", type.getId());
            notifyCacheUpdate(KRONOS_NODE_TYPE_IDS, type.getId());
            notifyCacheUpdate(KRONOS_NODE_TYPE_NAMES, String.format("%s-%s", type.getApplicationId(), type.getName()));
        }
    }

    @CacheEvict(cacheNames = { KRONOS_NODE_TYPE_IDS, KRONOS_NODE_TYPE_NAMES }, allEntries = true)
    public void clearNodeTypes() {
        String method = "clearNodeTypes";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_NODE_TYPE_IDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_NODE_TYPE_NAMES, ALL_KEYS);
    }

    @Cacheable(KRONOS_NODE_IDS)
    public Node findNodeById(String id) {
        return proxy.findNodeById(id);
    }

    @Cacheable(KRONOS_NODE_HIDS)
    public Node findNodeByHid(String hid) {
        return proxy.findNodeByHid(hid);
    }

    @Cacheable(value = KRONOS_NODE_NAMES, unless = "#result == null")
    public Node findNodeByName(String name) {
        return proxy.findNodeByName(name);
    }

    @Caching(evict = { @CacheEvict(value = KRONOS_NODE_IDS, key = "#node.id", condition = "#node != null"),
            @CacheEvict(value = KRONOS_NODE_NAMES, key = "#node.name", condition = "#node != null"),
            @CacheEvict(value = KRONOS_NODE_HIDS, key = "#node.hid", condition = "#node != null") })
    public void clearNode(Node node) {
        String method = "clearNode";
        if (node != null) {
            logInfo(method, "id: %s", node.getId());
            notifyCacheUpdate(KRONOS_NODE_IDS, node.getId());
            notifyCacheUpdate(KRONOS_NODE_NAMES, node.getName());
            notifyCacheUpdate(KRONOS_NODE_HIDS, node.getHid());
        }
    }

    @CacheEvict(cacheNames = { KRONOS_NODE_IDS, KRONOS_NODE_NAMES, KRONOS_NODE_HIDS }, allEntries = true)
    public void clearNodes() {
        String method = "clearNodes";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_NODE_IDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_NODE_NAMES, ALL_KEYS);
        notifyCacheUpdate(KRONOS_NODE_HIDS, ALL_KEYS);
    }

    @Cacheable(KRONOS_TELEMETRY_REPLAY_TYPE_IDS)
    public TelemetryReplayType findTelemetryReplayTypeById(String id) {
        return proxy.findTelemetryReplayTypeById(id);
    }

    @Cacheable(value = KRONOS_TELEMETRY_REPLAY_TYPE_NAMES, unless = "#result == null")
    public TelemetryReplayType findTelemetryReplayTypeByName(String name) {
        return proxy.findTelemetryReplayTypeByName(name);
    }

    @Caching(evict = {
            @CacheEvict(value = KRONOS_TELEMETRY_REPLAY_TYPE_IDS, key = "#type.id", condition = "#type != null"),
            @CacheEvict(value = KRONOS_TELEMETRY_REPLAY_TYPE_NAMES, key = "#type.name", condition = "#type != null") })
    public void clearTelemetryReplayType(TelemetryReplayType type) {
        String method = "clearTelemetryReplayType";
        if (type != null) {
            logInfo(method, "id: %s", type.getId());
            notifyCacheUpdate(KRONOS_TELEMETRY_REPLAY_TYPE_IDS, type.getId());
            notifyCacheUpdate(KRONOS_TELEMETRY_REPLAY_TYPE_NAMES, type.getName());
        }
    }

    @CacheEvict(cacheNames = { KRONOS_TELEMETRY_REPLAY_TYPE_IDS,
            KRONOS_TELEMETRY_REPLAY_TYPE_NAMES }, allEntries = true)
    public void clearTelemetryReplayTypes() {
        String method = "clearTelemetryReplayTypes";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_TELEMETRY_REPLAY_TYPE_IDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_TELEMETRY_REPLAY_TYPE_NAMES, ALL_KEYS);
    }

    @Cacheable(KRONOS_TELEMETRY_UNIT_IDS)
    public TelemetryUnit findTelemetryUnitById(String id) {
        return proxy.findTelemetryUnitById(id);
    }

    @Cacheable(value = KRONOS_TELEMETRY_UNIT_SYSTEM_NAMES, unless = "#result == null")
    public TelemetryUnit findTelemetryUnitBySystemName(String systemName) {
        return proxy.findTelemetryUnitBySystemName(systemName);
    }

    @Cacheable(KRONOS_TELEMETRY_UNIT_HIDS)
    public TelemetryUnit findTelemetryUnitByHid(String hid) {
        return proxy.findTelemetryUnitByHid(hid);
    }

    @Caching(evict = { @CacheEvict(value = KRONOS_TELEMETRY_UNIT_IDS, key = "#type.id", condition = "#type != null"),
            @CacheEvict(value = KRONOS_TELEMETRY_UNIT_SYSTEM_NAMES, key = "#type.systemName", condition = "#type != null"),
            @CacheEvict(value = KRONOS_TELEMETRY_UNIT_HIDS, key = "#type.hid", condition = "#type != null") })
    public void clearTelemetryUnit(TelemetryUnit type) {
        String method = "clearTelemetryUnit";
        if (type != null) {
            logInfo(method, "id: %s", type.getId());
            notifyCacheUpdate(KRONOS_TELEMETRY_UNIT_IDS, type.getId());
            notifyCacheUpdate(KRONOS_TELEMETRY_UNIT_SYSTEM_NAMES, type.getSystemName());
            notifyCacheUpdate(KRONOS_TELEMETRY_UNIT_HIDS, type.getHid());
        }
    }

    @CacheEvict(cacheNames = { KRONOS_TELEMETRY_UNIT_IDS, KRONOS_TELEMETRY_UNIT_SYSTEM_NAMES,
            KRONOS_TELEMETRY_UNIT_HIDS }, allEntries = true)
    public void clearTelemetryUnits() {
        String method = "clearTelemetryUnits";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_TELEMETRY_UNIT_IDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_TELEMETRY_UNIT_SYSTEM_NAMES, ALL_KEYS);
        notifyCacheUpdate(KRONOS_TELEMETRY_UNIT_HIDS, ALL_KEYS);
    }

    @Cacheable(KRONOS_DEVICE_STATE_IDS)
    public DeviceState findDeviceStateById(String id) {
        return proxy.findDeviceStateById(id);
    }

    @Cacheable(KRONOS_DEVICE_STATE_HIDS)
    public DeviceState findDeviceStateByHid(String hid) {
        return proxy.findDeviceStateByHid(hid);
    }

    @Caching(evict = {
            @CacheEvict(value = KRONOS_DEVICE_STATE_IDS, key = "#deviceState.id", condition = "#deviceState != null"),
            @CacheEvict(value = KRONOS_DEVICE_STATE_HIDS, key = "#deviceState.hid", condition = "#deviceState != null") })
    public void clearDeviceState(DeviceState deviceState) {
        String method = "clearDeviceState";
        if (deviceState != null) {
            logInfo(method, "id: %s", deviceState.getId());
            notifyCacheUpdate(KRONOS_DEVICE_STATE_IDS, deviceState.getId());
            notifyCacheUpdate(KRONOS_DEVICE_STATE_HIDS, deviceState.getHid());
        }
    }

    @CacheEvict(cacheNames = { KRONOS_DEVICE_STATE_IDS, KRONOS_DEVICE_STATE_HIDS }, allEntries = true)
    public void clearDeviceStates() {
        String method = "clearDeviceStates";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_DEVICE_STATE_IDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_DEVICE_STATE_HIDS, ALL_KEYS);
    }

    @Cacheable(KRONOS_TEST_PROCEDURE_IDS)
    public TestProcedure findTestProcedureById(String id) {
        return proxy.findTestProcedureById(id);
    }

    @Cacheable(KRONOS_TEST_PROCEDURE_HIDS)
    public TestProcedure findTestProcedureByHid(String hid) {
        return proxy.findTestProcedureByHid(hid);
    }

    @Caching(evict = {
            @CacheEvict(value = KRONOS_TEST_PROCEDURE_IDS, key = "#testProcedure.id", condition = "#testProcedure != null"),
            @CacheEvict(value = KRONOS_TEST_PROCEDURE_HIDS, key = "#testProcedure.hid", condition = "#testProcedure != null") })
    public void clearTestProcedure(TestProcedure testProcedure) {
        String method = "clearTestProcedure";
        if (testProcedure != null) {
            logInfo(method, "id: %s", testProcedure.getId());
            notifyCacheUpdate(KRONOS_TEST_PROCEDURE_IDS, testProcedure.getId());
            notifyCacheUpdate(KRONOS_TEST_PROCEDURE_HIDS, testProcedure.getHid());
        }
    }

    @CacheEvict(cacheNames = { KRONOS_TEST_PROCEDURE_IDS, KRONOS_TEST_PROCEDURE_HIDS }, allEntries = true)
    public void clearTestProcedures() {
        String method = "clearTestProcedurs";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_TEST_PROCEDURE_IDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_TEST_PROCEDURE_HIDS, ALL_KEYS);
    }

    @Cacheable(KRONOS_GLOBAL_ACTION_TYPE_IDS)
    public GlobalActionType findGlobalActionTypeById(String id) {
        return proxy.findGlobalActionTypeById(id);
    }

    @Cacheable(KRONOS_GLOBAL_ACTION_TYPE_HIDS)
    public GlobalActionType findGlobalActionTypeByHid(String hid) {
        return proxy.findGlobalActionTypeByHid(hid);
    }

    @Cacheable(value = KRONOS_GLOBAL_ACTION_TYPE_SYSTEM_NAMES, key = "#p0.concat('-').concat(#p1)", unless = "#result == null")
    public GlobalActionType findGlobalActionTypeBySystemName(String applicationId, String systemName) {
        return proxy.findGlobalActionTypeBySystemName(applicationId, systemName);
    }

    @Caching(evict = {
            @CacheEvict(value = KRONOS_GLOBAL_ACTION_TYPE_IDS, key = "#type.id", condition = "#type != null"),
            @CacheEvict(value = KRONOS_GLOBAL_ACTION_TYPE_HIDS, key = "#type.hid", condition = "#type != null"),
            @CacheEvict(value = KRONOS_GLOBAL_ACTION_TYPE_SYSTEM_NAMES, key = "#type.applicationId.concat('-').concat(#type.systemName)", condition = "#type != null") })
    public void clearGlobalActionType(GlobalActionType type) {
        String method = "clearGlobalActionType";
        if (type != null) {
            logInfo(method, "id: %s", type.getId());
            notifyCacheUpdate(KRONOS_GLOBAL_ACTION_TYPE_IDS, type.getId());
            notifyCacheUpdate(KRONOS_GLOBAL_ACTION_TYPE_HIDS, type.getHid());
            notifyCacheUpdate(KRONOS_GLOBAL_ACTION_TYPE_SYSTEM_NAMES,
                    String.format("%s-%s", type.getApplicationId(), type.getSystemName()));
        }
    }

    @Cacheable(KRONOS_GLOBAL_ACTION_IDS)
    public GlobalAction findGlobalActionById(String id) {
        return proxy.findGlobalActionById(id);
    }

    @Cacheable(KRONOS_GLOBAL_ACTION_HIDS)
    public GlobalAction findGlobalActionByHid(String hid) {
        return proxy.findGlobalActionByHid(hid);
    }

    @Cacheable(value = KRONOS_GLOBAL_ACTION_SYSTEM_NAMES, key = "#p0.concat('-').concat(#p1)", unless = "#result == null")
    public GlobalAction findGlobalActionBySystemName(String applicationId, String systemName) {
        return proxy.findGlobalActionBySystemName(applicationId, systemName);
    }

    @Caching(evict = { @CacheEvict(value = KRONOS_GLOBAL_ACTION_IDS, key = "#type.id", condition = "#type != null"),
            @CacheEvict(value = KRONOS_GLOBAL_ACTION_HIDS, key = "#type.hid", condition = "#type != null"),
            @CacheEvict(value = KRONOS_GLOBAL_ACTION_SYSTEM_NAMES, key = "#type.applicationId.concat('-').concat(#type.systemName)", condition = "#type != null") })
    public void clearGlobalAction(GlobalAction type) {
        String method = "clearGlobalAction";
        if (type != null) {
            logInfo(method, "id: %s", type.getId());
            notifyCacheUpdate(KRONOS_GLOBAL_ACTION_IDS, type.getId());
            notifyCacheUpdate(KRONOS_GLOBAL_ACTION_HIDS, type.getHid());
            notifyCacheUpdate(KRONOS_GLOBAL_ACTION_SYSTEM_NAMES,
                    String.format("%s-%s", type.getApplicationId(), type.getSystemName()));
        }
    }

    @Cacheable(KRONOS_SOCIAL_EVENT_DEVICE_IDS)
    public SocialEventDevice findSocialEventDeviceById(String id) {
        return proxy.findSocialEventDeviceById(id);
    }

    @Cacheable(KRONOS_SOCIAL_EVENT_DEVICE_HIDS)
    public SocialEventDevice findSocialEventDeviceByHid(String hid) {
        return proxy.findSocialEventDeviceByHid(hid);
    }

    @Caching(evict = {
            @CacheEvict(value = KRONOS_SOCIAL_EVENT_DEVICE_IDS, key = "#event.id", condition = "#event != null"),
            @CacheEvict(value = KRONOS_SOCIAL_EVENT_DEVICE_HIDS, key = "#event.hid", condition = "#event != null") })
    public void clearSocialEventDevice(SocialEventDevice event) {
        String method = "clearSocialEventDevice";
        if (event != null) {
            logInfo(method, "id: %s", event.getId());
            notifyCacheUpdate(KRONOS_SOCIAL_EVENT_DEVICE_IDS, event.getId());
            notifyCacheUpdate(KRONOS_SOCIAL_EVENT_DEVICE_HIDS, event.getHid());
        }
    }

    @CacheEvict(cacheNames = { KRONOS_SOCIAL_EVENT_DEVICE_IDS, KRONOS_SOCIAL_EVENT_DEVICE_HIDS }, allEntries = true)
    public void clearSocialEventDevices() {
        String method = "clearSocialEventDevices";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_SOCIAL_EVENT_DEVICE_IDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_SOCIAL_EVENT_DEVICE_HIDS, ALL_KEYS);
    }

    @Cacheable(KRONOS_SOCIAL_EVENT_REGISTRATION_IDS)
    public SocialEventRegistration findSocialEventRegistrationById(String id) {
        return proxy.findSocialEventRegistrationById(id);
    }

    @Cacheable(KRONOS_SOCIAL_EVENT_REGISTRATION_HIDS)
    public SocialEventRegistration findSocialEventRegistrationByHid(String hid) {
        return proxy.findSocialEventRegistrationByHid(hid);
    }

    @Cacheable(KRONOS_SOCIAL_EVENT_REGISTRATION_APPLICATION_IDS)
    public SocialEventRegistration findSocialEventRegistrationByApplicationId(String applicationId) {
        return proxy.findSocialEventRegistrationByApplicationId(applicationId);
    }

    @Caching(evict = {
            @CacheEvict(value = KRONOS_SOCIAL_EVENT_REGISTRATION_IDS, key = "#event.id", condition = "#event != null"),
            @CacheEvict(value = KRONOS_SOCIAL_EVENT_REGISTRATION_HIDS, key = "#event.hid", condition = "#event != null"),
            @CacheEvict(value = KRONOS_SOCIAL_EVENT_REGISTRATION_APPLICATION_IDS, key = "#event.applicationId", condition = "#event != null") })
    public void clearSocialEventRegistration(SocialEventRegistration event) {
        String method = "clearSocialEventRegistration";
        if (event != null) {
            logInfo(method, "id: %s", event.getId());
            notifyCacheUpdate(KRONOS_SOCIAL_EVENT_REGISTRATION_IDS, event.getId());
            notifyCacheUpdate(KRONOS_SOCIAL_EVENT_REGISTRATION_HIDS, event.getHid());
            notifyCacheUpdate(KRONOS_SOCIAL_EVENT_REGISTRATION_APPLICATION_IDS, event.getApplicationId());
        }
    }

    @CacheEvict(cacheNames = { KRONOS_SOCIAL_EVENT_REGISTRATION_IDS, KRONOS_SOCIAL_EVENT_REGISTRATION_HIDS,
            KRONOS_SOCIAL_EVENT_REGISTRATION_APPLICATION_IDS }, allEntries = true)
    public void clearSocialEventRegistrations() {
        String method = "clearSocialEventRegistrations";
        logInfo(method, "...");
        notifyCacheUpdate(KRONOS_SOCIAL_EVENT_REGISTRATION_IDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_SOCIAL_EVENT_REGISTRATION_HIDS, ALL_KEYS);
        notifyCacheUpdate(KRONOS_SOCIAL_EVENT_REGISTRATION_APPLICATION_IDS, ALL_KEYS);
    }
}

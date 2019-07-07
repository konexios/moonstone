package com.arrow.kronos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.arrow.acs.Loggable;
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
import com.arrow.kronos.repo.AwsAccountRepository;
import com.arrow.kronos.repo.AzureAccountRepository;
import com.arrow.kronos.repo.DeviceActionTypeRepository;
import com.arrow.kronos.repo.DeviceTypeRepository;
import com.arrow.kronos.repo.GlobalActionRepository;
import com.arrow.kronos.repo.GlobalActionTypeRepository;
import com.arrow.kronos.repo.IbmAccountRepository;
import com.arrow.kronos.repo.NodeRepository;
import com.arrow.kronos.repo.NodeTypeRepository;
import com.arrow.kronos.repo.TelemetryReplayTypeRepository;
import com.arrow.kronos.repo.TelemetryUnitRepository;

@Service
public class KronosCacheProxy extends Loggable {
    @Autowired
    private KronosApplicationService kronosApplicationService;
    @Autowired
    private KronosUserService kronosUserService;
    @Autowired
    private GatewayService gatewayService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceTypeRepository deviceTypeRepository;
    @Autowired
    private DeviceActionTypeRepository deviceActionTypeRepository;
    @Autowired
    private AwsAccountRepository awsAccountRepository;
    @Autowired
    private IbmAccountRepository ibmAccountRepository;
    @Autowired
    private NodeTypeRepository nodeTypeRepository;
    @Autowired
    private NodeRepository nodeRepository;
    @Autowired
    private TelemetryReplayTypeRepository telemetryReplayTypeRepository;
    @Autowired
    private AzureAccountRepository azureAccountRepository;
    @Autowired
    private TelemetryUnitRepository telemetryUnitRepository;
    @Autowired
    private DeviceStateService deviceStateService;
    @Autowired
    private TestProcedureService testProcedureService;
    @Autowired
    private GlobalActionTypeRepository globalActionTypeRepository;
    @Autowired
    private GlobalActionRepository globalActionRepository;
    @Autowired
    private SocialEventDeviceService socialEventDeviceService;
    @Autowired
    private SocialEventRegistrationService socialEventRegistrationService;

    @Caching(put = { @CachePut(value = "kronos_application_hids", key = "#result.hid", condition = "#result != null"),
            @CachePut(value = "kronos_application_application_ids", key = "#result.applicationId", condition = "#result != null") })
    public KronosApplication findKronosApplicationById(String id) {
        String method = "findKronosApplicationById";
        logDebug(method, "looking up kronos application id: %s", id);
        KronosApplication kronosApplication = kronosApplicationService.getKronosApplicationRepository().findById(id)
                .orElse(null);
        kronosApplication = kronosApplicationService.populateRefs(kronosApplication);
        return kronosApplication;
    }

    @Caching(put = { @CachePut(value = "kronos_application_ids", key = "#result.id", condition = "#result != null"),
            @CachePut(value = "kronos_application_application_ids", key = "#result.applicationId", condition = "#result != null") })
    public KronosApplication findKronosApplicationByHid(String hid) {
        String method = "findKronosApplicationByHid";
        logDebug(method, "looking up kronos application hid: %s", hid);
        KronosApplication kronosApplication = kronosApplicationService.getKronosApplicationRepository()
                .doFindByHid(hid);
        kronosApplication = kronosApplicationService.populateRefs(kronosApplication);
        return kronosApplication;
    }

    @Caching(put = { @CachePut(value = "kronos_application_ids", key = "#result.id", condition = "#result != null"),
            @CachePut(value = "kronos_application_hids", key = "#result.hid", condition = "#result != null") })
    public KronosApplication findKronosApplicationByApplicationId(String applicationId) {
        String method = "findKronosApplicationByApplicationId";
        logDebug(method, "looking up kronos application id: %s", applicationId);
        KronosApplication kronosApplication = kronosApplicationService.getKronosApplicationRepository()
                .findByApplicationId(applicationId);
        kronosApplication = kronosApplicationService.populateRefs(kronosApplication);
        return kronosApplication;
    }

    @CachePut(value = "kronos_user_hids", key = "#result.hid", condition = "#result != null")
    public KronosUser findKronosUserById(String id) {
        String method = "findKronosUserById";
        logDebug(method, "looking up kronos user id: %s", id);
        KronosUser kronosUser = kronosUserService.getKronosUserRepository().findById(id).orElse(null);
        kronosUser = kronosUserService.populateRefs(kronosUser);
        return kronosUser;
    }

    @CachePut(value = "kronos_user_ids", key = "#result.id", condition = "#result != null")
    public KronosUser findKronosUserByHid(String hid) {
        String method = "findKronosUserByHid";
        logDebug(method, "looking up kronos user hid: %s", hid);
        KronosUser kronosUser = kronosUserService.getKronosUserRepository().doFindByHid(hid);
        kronosUser = kronosUserService.populateRefs(kronosUser);
        return kronosUser;
    }

    @CachePut(value = "kronos_aws_account_hids", key = "#result.hid", condition = "#result != null")
    public AwsAccount findAwsAccountById(String id) {
        String method = "findAwsAccountById";
        logDebug(method, "looking up aws account id: %s", id);
        return awsAccountRepository.findById(id).orElse(null);
    }

    @CachePut(value = "kronos_aws_account_ids", key = "#result.id", condition = "#result != null")
    public AwsAccount findAwsAccountByHid(String hid) {
        String method = "findAwsAccountByHid";
        logDebug(method, "looking up aws account hid: %s", hid);
        return awsAccountRepository.doFindByHid(hid);
    }

    @CachePut(value = "kronos_ibm_account_hids", key = "#result.hid", condition = "#result != null")
    public IbmAccount findIbmAccountById(String id) {
        String method = "findIbmAccountById";
        logDebug(method, "looking up ibm account id: %s", id);
        return ibmAccountRepository.findById(id).orElse(null);
    }

    @CachePut(value = "kronos_ibm_account_ids", key = "#result.id", condition = "#result != null")
    public IbmAccount findIbmAccountByHid(String hid) {
        String method = "findIbmAccountByHid";
        logDebug(method, "looking up ibm account hid: %s", hid);
        return ibmAccountRepository.doFindByHid(hid);
    }

    @CachePut(value = "kronos_azure_account_hids", key = "#result.hid", condition = "#result != null")
    public AzureAccount findAzureAccountById(String id) {
        String method = "findAzureAccountById";
        logDebug(method, "looking up azure account id: %s", id);
        return azureAccountRepository.findById(id).orElse(null);
    }

    @CachePut(value = "kronos_azure_account_ids", key = "#result.id", condition = "#result != null")
    public AzureAccount findAzureAccountByHid(String hid) {
        String method = "findAzureAccountByHid";
        logDebug(method, "looking up azure account hid: %s", hid);
        return azureAccountRepository.doFindByHid(hid);
    }

    @Caching(put = {
            @CachePut(value = "kronos_device_type_names", key = "#result.applicationId.concat('-').concat(#result.name)", condition = "#result != null"),
            @CachePut(value = "kronos_device_type_hids", key = "#result.hid", condition = "#result != null") })
    public DeviceType findDeviceTypeById(String id) {
        String method = "findDeviceTypeById";
        logDebug(method, "looking up device type id: %s", id);
        return deviceTypeRepository.findById(id).orElse(null);
    }

    @Caching(put = { @CachePut(value = "kronos_device_type_ids", key = "#result.id", condition = "#result != null"),
            @CachePut(value = "kronos_device_type_hids", key = "#result.hid", condition = "#result != null") })
    public DeviceType findDeviceTypeByName(String applicationId, String name) {
        String method = "findDeviceTypeByName";
        logDebug(method, "looking up device type application id: %s, name: %s", applicationId, name);
        return deviceTypeRepository.findFirstByApplicationIdAndName(applicationId, name);
    }

    @Caching(put = {
            @CachePut(value = "kronos_device_type_names", key = "#result.applicationId.concat('-').concat(#result.name)", condition = "#result != null"),
            @CachePut(value = "kronos_device_type_ids", key = "#result.id", condition = "#result != null") })
    public DeviceType findDeviceTypeByHid(String hid) {
        String method = "findDeviceTypeByHid";
        logDebug(method, "looking up device type hid: %s", hid);
        return deviceTypeRepository.doFindByHid(hid);
    }

    @CachePut(value = "kronos_gateway_ids", key = "#result.id", condition = "#result != null")
    public Gateway findGatewayByHid(String hid) {
        String method = "findGatewayByHid";
        logDebug(method, "looking up gateway hid: %s", hid);
        return gatewayService.getGatewayRepository().doFindByHid(hid);
    }

    @CachePut(value = "kronos_gateway_hids", key = "#result.hid", condition = "#result != null")
    public Gateway findGatewayById(String id) {
        String method = "findGatewayById";
        logDebug(method, "looking up gateway id: %s", id);
        return gatewayService.getGatewayRepository().findById(id).orElse(null);
    }

    @CachePut(value = "kronos_device_hids", key = "#result.hid", condition = "#result != null")
    public Device findDeviceById(String id) {
        String method = "findDeviceById";
        logDebug(method, "looking up device id: %s", id);
        return deviceService.populate(deviceService.getDeviceRepository().findById(id).orElse(null));
    }

    @CachePut(value = "kronos_device_ids", key = "#result.id", condition = "#result != null")
    public Device findDeviceByHid(String hid) {
        String method = "findDeviceByHid";
        logDebug(method, "looking up device hid: %s", hid);
        return deviceService.populate(deviceService.getDeviceRepository().doFindByHid(hid));
    }

    @CachePut(value = "kronos_device_action_type_system_names", key = "#result.applicationId.concat('-').concat(#result.systemName)", condition = "#result != null")
    public DeviceActionType findDeviceActionTypeById(String id) {
        String method = "findDeviceActionTypeById";
        logDebug(method, "looking up device action type id: %s", id);
        return deviceActionTypeRepository.findById(id).orElse(null);
    }

    @CachePut(value = "kronos_device_action_type_ids", key = "#result.id", condition = "#result != null")
    public DeviceActionType findDeviceActionTypeBySystemName(String applicationId, String systemName) {
        String method = "findDeviceActionTypeBySystemName";
        logDebug(method, "looking up device action type system name: %s", systemName);
        return deviceActionTypeRepository.findByApplicationIdAndSystemName(applicationId, systemName);
    }

    @CachePut(value = "kronos_node_type_names", key = "#result.applicationId.concat('-').concat(#result.name)", condition = "#result != null")
    public NodeType findNodeTypeById(String id) {
        String method = "findNodeTypeById";
        logDebug(method, "looking up node type id: %s", id);
        return nodeTypeRepository.findById(id).orElse(null);
    }

    @CachePut(value = "kronos_node_type_ids", key = "#result.id", condition = "#result != null")
    public NodeType findNodeTypeByName(String applicationId, String name) {
        String method = "findNodeTypeByName";
        logDebug(method, "looking up node type, applicationId: %s, name: %s", applicationId, name);
        return nodeTypeRepository.findByApplicationIdAndName(applicationId, name);
    }

    @Caching(put = { @CachePut(value = "kronos_node_names", key = "#result.name", condition = "#result != null"),
            @CachePut(value = "kronos_node_hids", key = "#result.hid", condition = "#result != null") })
    public Node findNodeById(String id) {
        String method = "findNodeById";
        logDebug(method, "looking up node id: %s", id);
        return nodeRepository.findById(id).orElse(null);
    }

    @Caching(put = { @CachePut(value = "kronos_node_hids", key = "#result.hid", condition = "#result != null"),
            @CachePut(value = "kronos_node_ids", key = "#result.id", condition = "#result != null") })
    public Node findNodeByName(String name) {
        String method = "findNodeByName";
        logDebug(method, "looking up node name: %s", name);
        return nodeRepository.findByName(name);
    }

    @Caching(put = { @CachePut(value = "kronos_node_names", key = "#result.name", condition = "#result != null"),
            @CachePut(value = "kronos_node_ids", key = "#result.id", condition = "#result != null") })
    public Node findNodeByHid(String hid) {
        String method = "findNodeByHid";
        logDebug(method, "looking up node hid: %s", hid);
        return nodeRepository.doFindByHid(hid);
    }

    @CachePut(value = "kronos_telemetry_replay_type_names", key = "#result.name", condition = "#result != null")
    public TelemetryReplayType findTelemetryReplayTypeById(String id) {
        String method = "findTelemetryReplayTypeById";
        logDebug(method, "looking up telemetry replay type id: %s", id);
        return telemetryReplayTypeRepository.findById(id).orElse(null);
    }

    @CachePut(value = "kronos_telemetry_replay_type_ids", key = "#result.id", condition = "#result != null")
    public TelemetryReplayType findTelemetryReplayTypeByName(String name) {
        String method = "findTelemetryReplayTypeByName";
        logDebug(method, "looking up telemetry replay type name: %s", name);
        return telemetryReplayTypeRepository.findByName(name);
    }

    @Caching(put = {
            @CachePut(value = KronosCache.KRONOS_TELEMETRY_UNIT_SYSTEM_NAMES, key = "#result.systemName", condition = "#result != null"),
            @CachePut(value = KronosCache.KRONOS_TELEMETRY_UNIT_HIDS, key = "#result.hid", condition = "#result != null") })
    public TelemetryUnit findTelemetryUnitById(String id) {
        String method = "findTelemetryUnitById";
        logDebug(method, "looking up telemetry unit id: %s", id);
        return telemetryUnitRepository.findById(id).orElse(null);
    }

    @Caching(put = {
            @CachePut(value = KronosCache.KRONOS_TELEMETRY_UNIT_IDS, key = "#result.id", condition = "#result != null"),
            @CachePut(value = KronosCache.KRONOS_TELEMETRY_UNIT_HIDS, key = "#result.hid", condition = "#result != null") })
    public TelemetryUnit findTelemetryUnitBySystemName(String systemName) {
        String method = "findTelemetryUnitBySystemName";
        logDebug(method, "looking up telemetry unit systemName: %s", systemName);
        return telemetryUnitRepository.findBySystemName(systemName);
    }

    @Caching(put = {
            @CachePut(value = KronosCache.KRONOS_TELEMETRY_UNIT_IDS, key = "#result.id", condition = "#result != null"),
            @CachePut(value = KronosCache.KRONOS_TELEMETRY_UNIT_SYSTEM_NAMES, key = "#result.systemName", condition = "#result != null") })
    public TelemetryUnit findTelemetryUnitByHid(String hid) {
        String method = "findTelemetryUnitByHid";
        logDebug(method, "looking up telemetry unit hid: %s", hid);
        return telemetryUnitRepository.doFindByHid(hid);
    }

    @CachePut(value = "kronos_device_hids", key = "#result.hid", condition = "#result != null")
    public DeviceState findDeviceStateById(String id) {
        String method = "findDeviceStateById";
        logDebug(method, "looking up device state id: %s", id);
        return deviceStateService.getDeviceStateRepository().findById(id).orElse(null);
    }

    @CachePut(value = "kronos_device_state_ids", key = "#result.id", condition = "#result != null")
    public DeviceState findDeviceStateByHid(String hid) {
        String method = "findDeviceStateByHid";
        logDebug(method, "looking up device state hid: %s", hid);
        return deviceStateService.getDeviceStateRepository().doFindByHid(hid);
    }

    @CachePut(value = "kronos_test_procedure_ids", key = "#result.id", condition = "#result != null")
    public TestProcedure findTestProcedureByHid(String hid) {
        String method = "findTestProcedureByHid";
        logDebug(method, "looking up test procedure hid: %s", hid);
        return testProcedureService.getTestProcedureRepository().doFindByHid(hid);
    }

    @CachePut(value = "kronos_test_procedure_hids", key = "#result.hid", condition = "#result != null")
    public TestProcedure findTestProcedureById(String id) {
        String method = "findTestProcedureById";
        logDebug(method, "looking up test procedure id: %s", id);
        return testProcedureService.getTestProcedureRepository().findById(id).orElse(null);
    }

    @Caching(put = {
            @CachePut(value = KronosCache.KRONOS_GLOBAL_ACTION_TYPE_HIDS, key = "#result.hid", condition = "#result != null"),
            @CachePut(value = KronosCache.KRONOS_GLOBAL_ACTION_TYPE_SYSTEM_NAMES, key = "#result.applicationId.concat('-').concat(#result.systemName)", condition = "#result != null") })
    public GlobalActionType findGlobalActionTypeById(String id) {
        String method = "findGlobalActionTypeById";
        logDebug(method, "looking up global action type id: %s", id);
        return globalActionTypeRepository.findById(id).orElse(null);
    }

    @Caching(put = {
            @CachePut(value = KronosCache.KRONOS_GLOBAL_ACTION_TYPE_IDS, key = "#result.id", condition = "#result != null"),
            @CachePut(value = KronosCache.KRONOS_GLOBAL_ACTION_TYPE_SYSTEM_NAMES, key = "#result.applicationId.concat('-').concat(#result.systemName)", condition = "#result != null") })
    public GlobalActionType findGlobalActionTypeByHid(String hid) {
        String method = "findGlobalActionTypeByHid";
        logDebug(method, "looking up global action type hid: %s", hid);
        return globalActionTypeRepository.doFindByHid(hid);
    }

    @Caching(put = {
            @CachePut(value = KronosCache.KRONOS_GLOBAL_ACTION_TYPE_IDS, key = "#result.id", condition = "#result != null"),
            @CachePut(value = KronosCache.KRONOS_GLOBAL_ACTION_TYPE_HIDS, key = "#result.hid", condition = "#result != null") })
    public GlobalActionType findGlobalActionTypeBySystemName(String applicationId, String systemName) {
        String method = "findGlobalActionTypeBySystemName";
        logDebug(method, "looking up global action type system name: %s", systemName);
        return globalActionTypeRepository.findByApplicationIdAndSystemName(applicationId, systemName);
    }

    @Caching(put = {
            @CachePut(value = KronosCache.KRONOS_GLOBAL_ACTION_SYSTEM_NAMES, key = "#result.applicationId.concat('-').concat(#result.systemName)", condition = "#result != null"),
            @CachePut(value = KronosCache.KRONOS_GLOBAL_ACTION_HIDS, key = "#result.hid", condition = "#result != null") })
    public GlobalAction findGlobalActionById(String id) {
        String method = "findGlobalActionById";
        logDebug(method, "looking up global action id: %s", id);
        return globalActionRepository.findById(id).orElse(null);
    }

    @Caching(put = {
            @CachePut(value = KronosCache.KRONOS_GLOBAL_ACTION_IDS, key = "#result.id", condition = "#result != null"),
            @CachePut(value = KronosCache.KRONOS_GLOBAL_ACTION_SYSTEM_NAMES, key = "#result.applicationId.concat('-').concat(#result.systemName)", condition = "#result != null") })
    public GlobalAction findGlobalActionByHid(String hid) {
        String method = "findGlobalActionByHid";
        logDebug(method, "looking up global action hid: %s", hid);
        return globalActionRepository.doFindByHid(hid);
    }

    @Caching(put = {
            @CachePut(value = KronosCache.KRONOS_GLOBAL_ACTION_IDS, key = "#result.id", condition = "#result != null"),
            @CachePut(value = KronosCache.KRONOS_GLOBAL_ACTION_HIDS, key = "#result.hid", condition = "#result != null") })
    public GlobalAction findGlobalActionBySystemName(String applicationId, String systemName) {
        String method = "findGlobalActionBySystemName";
        logDebug(method, "looking up global action system name: %s", systemName);
        return globalActionRepository.findByApplicationIdAndSystemName(applicationId, systemName);
    }

    @CachePut(value = "kronos_social_event_device_ids", key = "#result.id", condition = "#result != null")
    public SocialEventDevice findSocialEventDeviceByHid(String hid) {
        String method = "findSocialEventDeviceByHid";
        logDebug(method, "looking up socialEventDevice hid: %s", hid);
        return socialEventDeviceService.getSocialEventDeviceRepository().doFindByHid(hid);
    }

    @CachePut(value = "kronos_social_event_device_hids", key = "#result.hid", condition = "#result != null")
    public SocialEventDevice findSocialEventDeviceById(String id) {
        String method = "findSocialEventRegistrationById";
        logDebug(method, "looking up test socialEventDevice id: %s", id);
        return socialEventDeviceService.getSocialEventDeviceRepository().findById(id).orElse(null);
    }

    @Caching(put = {
            @CachePut(value = KronosCache.KRONOS_SOCIAL_EVENT_REGISTRATION_IDS, key = "#result.id", condition = "#result != null"),
            @CachePut(value = KronosCache.KRONOS_SOCIAL_EVENT_REGISTRATION_APPLICATION_IDS, key = "#result.applicationId", condition = "#result != null") })
    public SocialEventRegistration findSocialEventRegistrationByHid(String hid) {
        String method = "findSocialEventRegistrationByHid";
        logDebug(method, "looking up socialEventRegistration hid: %s", hid);
        return socialEventRegistrationService.getSocialEventRegistrationRepository().doFindByHid(hid);
    }

    @Caching(put = {
            @CachePut(value = KronosCache.KRONOS_SOCIAL_EVENT_REGISTRATION_HIDS, key = "#result.hid", condition = "#result != null"),
            @CachePut(value = KronosCache.KRONOS_SOCIAL_EVENT_REGISTRATION_APPLICATION_IDS, key = "#result.applicationId", condition = "#result != null") })
    public SocialEventRegistration findSocialEventRegistrationById(String id) {
        String method = "findSocialEventRegistrationById";
        logDebug(method, "looking up socialEventRegistration id: %s", id);
        return socialEventRegistrationService.getSocialEventRegistrationRepository().findById(id).orElse(null);
    }

    @Caching(put = {
            @CachePut(value = KronosCache.KRONOS_SOCIAL_EVENT_REGISTRATION_IDS, key = "#result.id", condition = "#result != null"),
            @CachePut(value = KronosCache.KRONOS_SOCIAL_EVENT_REGISTRATION_HIDS, key = "#result.hid", condition = "#result != null") })
    public SocialEventRegistration findSocialEventRegistrationByApplicationId(String applicationId) {
        String method = "findSocialEventRegistrationByApplicationId";
        logDebug(method, "looking up socialEventRegistration applicationId: %s", applicationId);
        return socialEventRegistrationService.getSocialEventRegistrationRepository().findByApplicationId(applicationId);
    }
}

package com.arrow.kronos.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsRuntimeException;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.YesNoInherit;
import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.IoTProvider;
import com.arrow.kronos.data.KronosApplication;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.client.api.ClientApplicationApi;
import com.arrow.pegasus.client.api.ClientRoleApi;
import com.arrow.pegasus.cron.CronLogger;
import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.ConfigurationPropertyCategory;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.util.CoreConfigurationPropertyUtil;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class KronosApplicationProvisioningService extends KronosServiceAbstract {

    @Autowired
    private ClientRoleApi clientRoleApi;
    @Autowired
    private KronosApplicationService kronosApplicationService;
    @Autowired
    private DeviceTypeService deviceTypeService;
    @Autowired
    private DeviceActionTypeService deviceActionTypeService;
    @Autowired
    private ClientApplicationApi clientApplicationApi;
    @Autowired
    private CoreConfigurationPropertyUtil coreConfigurationPropertyUtil;

    static final String DEVICE_TYPES_TEMPLATES = "/templates/device-types.json";
    static final String DEVICE_ACTION_TYPES_TEMPLATES = "/templates/device-action-types.json";

    public int monitorNewApplications(CronLogger logger, String zoneSystemName, String who) {
        String method = "monitorNewApplications";
        int counter = 0;
        Zone zone = getCoreCacheService().findZoneBySystemName(zoneSystemName);
        Assert.notNull(zone, "invalid zoneSystemName: " + zoneSystemName);
        List<Application> all = clientApplicationApi
                .findByProductIdAndEnabled(
                        getCoreCacheService().findProductBySystemName(ProductSystemNames.KRONOS).getId(), true)
                .stream().filter(application -> application.getZoneId().equals(zone.getId()))
                .collect(Collectors.toList());
        addLog(logger, method, "all applications for zone: %s size: %d", zone, all.size());
        if (all.size() > 0) {
            Set<String> existing = new HashSet<>();
            kronosApplicationService.getKronosApplicationRepository().findAll().forEach(app -> {
                existing.add(app.getApplicationId());
            });
            for (Application application : all) {
                String applicationId = application.getId();
                if (!existing.contains(applicationId)) {
                    addLog(logger, method, "found new application: %s / %s, processing ...", applicationId,
                            application.getName());
                    try {
                        provisionApplication(applicationId, false, who);
                    } catch (Exception e) {
                        addException(logger, e, method, "error processing new application");
                    }
                    counter++;
                }
            }
        }
        return counter;
    }

    public KronosApplication provisionApplication(String applicationId, boolean allowCreateGatewayFromDifferentApp,
            String who) {
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(who, "who is empty");

        // create kronos application if it does not exist
        KronosApplication kronosApplication = kronosApplicationService.getKronosApplicationRepository()
                .findByApplicationId(applicationId);
        if (kronosApplication == null) {
            // kronos application does not exist
            kronosApplication = defaultKronosApplication(applicationId, allowCreateGatewayFromDifferentApp, who);
        }

        // TODO need to provide refApplicationName
        // provisionApplicationRoles(refApplicationName, who, applicationId,
        // roleNames);

        // device types
        provisionDeviceTypes(applicationId, who);

        // default gateway type
        deviceTypeService.checkCreateDefaultGatewayType(applicationId, who);

        // device action types
        provisionDeviceActionTypes(applicationId, who);

        return kronosApplication;
    }

    public KronosApplication defaultKronosApplication(String applicationId, boolean allowCreateGatewayFromDifferentApp,
            String who) {
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(who, "who is empty");

        Application application = getCoreCacheService().findApplicationById(applicationId);
        Assert.notNull(application, "application not found: applicationId = " + applicationId);
        Assert.isTrue(application.isEnabled(), "application: " + application.getName() + " is not enabled!");

        // create a kronos application related to the supplied applicationId and
        // assign default settings
        KronosApplication kronosApplication = new KronosApplication();
        kronosApplication.setApplicationId(applicationId);
        kronosApplication.setIotProvider(IoTProvider.ArrowConnect);
        kronosApplication.setIndexTelemetry(YesNoInherit.NO);
        kronosApplication.setPersistTelemetry(YesNoInherit.NO);
        kronosApplication.setAllowCreateGatewayFromDifferentApp(allowCreateGatewayFromDifferentApp);

        // create
        kronosApplication = kronosApplicationService.create(kronosApplication, who);

        return kronosApplication;
    }

    public void provisionDeviceTypes(String applicationId, String who) {
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(who, "who is empty");

        String method = "provisionDeviceTypes";
        logDebug(method, "entered...");

        try {
            Product product = getCoreCacheService().findProductBySystemName(ProductSystemNames.KRONOS);
            Assert.notNull(product, "Product not found! productSystemName=" + ProductSystemNames.KRONOS);

            logDebug(method, "loading device type templates...");
            // templates
            // InputStream templatesInputStream =
            // KronosApplicationProvisioningService.class
            // .getResourceAsStream(DEVICE_TYPES_TEMPLATES);
            // Assert.notNull(templatesInputStream,
            // "templatesInputStream are null, most likely unable to find " +
            // DEVICE_TYPES_TEMPLATES);
            //
            // String json = IOUtils.toString(templatesInputStream,
            // StandardCharsets.UTF_8);

            String configPropertyName = "deviceTypeTemplates";
            ConfigurationProperty deviceTypeTemplatesProperty = coreConfigurationPropertyUtil
                    .getConfigurationProperty(product, ConfigurationPropertyCategory.Settings, configPropertyName);
            Assert.notNull(deviceTypeTemplatesProperty, "Configuration Property not found! name=" + configPropertyName);

            String json = deviceTypeTemplatesProperty.getValue();
            if (!StringUtils.isEmpty(json)) {
                List<DeviceType> templates = JsonUtils.fromJson(json, new TypeReference<List<DeviceType>>() {
                });

                logDebug(method, "loading existing device types...");
                // existing device types for application
                Map<String, DeviceType> existingMap = new HashMap<>();
                List<DeviceType> existingDeviceTypes = deviceTypeService.getDeviceTypeRepository()
                        .findByApplicationId(applicationId);
                for (DeviceType deviceType : existingDeviceTypes) {
                    existingMap.put(deviceType.getName(), deviceType);
                }

                List<DeviceType> createList = new ArrayList<>();
                List<DeviceType> updateList = new ArrayList<>();
                for (DeviceType templateDeviceType : templates)
                    if (!existingMap.containsKey(templateDeviceType.getName()))
                        createList.add(templateDeviceType);
                    else {
                        DeviceType existing = existingMap.get(templateDeviceType.getName());
                        Assert.notNull(existing,
                                "Device Type not found! deviceTypeName=" + templateDeviceType.getName());
                        existing.setDescription(templateDeviceType.getDescription());
                        existing.setEnabled(templateDeviceType.isEnabled());
                        existing.setEditable(templateDeviceType.isEditable());
                        existing.setDeviceCategory(templateDeviceType.getDeviceCategory());
                        existing.setRheaDeviceTypeId(templateDeviceType.getRheaDeviceTypeId());
                        existing.setTelemetries(templateDeviceType.getTelemetries());

                        updateList.add(existing);
                    }

                // create new device types
                if (!createList.isEmpty()) {
                    for (DeviceType deviceType : createList) {
                        deviceType.setApplicationId(applicationId);
                        // deviceType.setDeviceCategory(AcnDeviceCategory.DEVICE);
                        deviceType = deviceTypeService.create(deviceType, who);
                        logDebug(method, "created device type: " + deviceType.getName() + " for applicationId: "
                                + applicationId + " by: " + who);
                    }
                }

                // update existing device types
                if (!updateList.isEmpty()) {
                    for (DeviceType deviceType : updateList) {
                        deviceType = deviceTypeService.update(deviceType, who);
                        logDebug(method, "updated device type: " + deviceType.getName() + " for applicationId: "
                                + applicationId + " by: " + who);
                    }
                }
            }

        } catch (Exception e) {
            throw new AcsRuntimeException(e.getMessage());
        }
    }

    public void provisionDeviceActionTypes(String applicationId, String who) {
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(who, "who is empty");

        String method = "provisionDeviceActionTypes";
        logDebug(method, "entered...");

        try {
            logDebug(method, "loading existing device action types...");

            Product product = getCoreCacheService().findProductBySystemName(ProductSystemNames.KRONOS);
            Assert.notNull(product, "Product not found! productSystemName=" + ProductSystemNames.KRONOS);

            // existing device action types for application
            Map<String, DeviceActionType> existingMap = new HashMap<>();
            List<DeviceActionType> existingDeviceActionTypes = deviceActionTypeService.getDeviceActionTypeRepository()
                    .findByApplicationId(applicationId);
            for (DeviceActionType deviceActionType : existingDeviceActionTypes) {
                existingMap.put(deviceActionType.getSystemName(), deviceActionType);
            }

            logDebug(method, "loading device action type templates...");
            // templates
            // InputStream templatesInputStream =
            // KronosApplicationProvisioningService.class
            // .getResourceAsStream(DEVICE_ACTION_TYPES_TEMPLATES);
            // Assert.notNull(templatesInputStream,
            // "templatesInputStream are null, most likely unable to find " +
            // DEVICE_ACTION_TYPES_TEMPLATES);
            //
            // String json = IOUtils.toString(templatesInputStream,
            // StandardCharsets.UTF_8);

            String configPropertyName = "deviceActionTypeTemplates";
            ConfigurationProperty deviceActionTypeTemplatesProperty = coreConfigurationPropertyUtil
                    .getConfigurationProperty(product, ConfigurationPropertyCategory.Settings, configPropertyName);
            Assert.notNull(deviceActionTypeTemplatesProperty,
                    "Configuration Property not found! name=" + configPropertyName);

            String json = deviceActionTypeTemplatesProperty.getValue();
            if (!StringUtils.isEmpty(json)) {
                List<DeviceActionType> templates = JsonUtils.fromJson(json,
                        new TypeReference<List<DeviceActionType>>() {
                        });

                List<DeviceActionType> createList = new ArrayList<>();
                for (DeviceActionType deviceActionType : templates) {
                    if (!existingMap.containsKey(deviceActionType.getSystemName()))
                        createList.add(deviceActionType);
                }

                if (!createList.isEmpty()) {
                    for (DeviceActionType deviceActionType : createList) {
                        deviceActionType.setApplicationId(applicationId);
                        deviceActionType = deviceActionTypeService.create(deviceActionType, who);
                        logDebug(method, "created device action type: " + deviceActionType.getSystemName()
                                + " for applicationId: " + applicationId + " by: " + who);
                    }
                }
            }

        } catch (Exception e) {
            throw new AcsRuntimeException(e.getMessage());
        }
    }

    public void provisionApplicationRoles(String refApplicationName, String who, String applicationId,
            String... roleNames) {
        Assert.hasText(refApplicationName, "refApplicationName is empty");
        Assert.hasText(who, "who is empty");
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.notEmpty(roleNames, "roleNames is empty");

        String method = "provisionApplicationRoles";

        Application refApplication = getCoreCacheService().findApplicationByName(refApplicationName);

        for (String roleName : roleNames) {
            Role roleExistsCheck = clientRoleApi.findByNameAndApplicationId(roleName, applicationId);

            if (roleExistsCheck == null) {
                Role roleTemplate = clientRoleApi.findByNameAndApplicationId(roleName, refApplication.getId());
                Assert.notNull(roleTemplate,
                        "Unable to find role template! name=" + roleName + " application=" + refApplicationName);

                Role newRole = new Role();
                newRole.setName(roleTemplate.getName());
                newRole.setDescription(roleTemplate.getDescription());
                newRole.setApplicationId(applicationId);
                newRole.setProductId(roleTemplate.getProductId());
                newRole.setEditable(roleTemplate.isEditable());
                newRole.setEnabled(roleTemplate.isEnabled());
                newRole.setPrivilegeIds(roleTemplate.getPrivilegeIds());
                newRole = clientRoleApi.create(newRole, who);

                logDebug(method, "created role, id: %s, name: %s, applicationId: %s", newRole.getId(),
                        newRole.getName(), applicationId);
            }
        }
    }
}

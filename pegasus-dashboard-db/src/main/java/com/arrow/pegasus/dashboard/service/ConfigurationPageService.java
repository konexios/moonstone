package com.arrow.pegasus.dashboard.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.dashboard.data.ConfigurationPage;
import com.arrow.pegasus.dashboard.data.PageProperty;
import com.arrow.pegasus.dashboard.repo.ConfigurationPageRepository;
import com.arrow.pegasus.dashboard.repo.PagePropertySearchParams;

import moonstone.acs.AcsLogicalException;

@Service
public class ConfigurationPageService extends DashboardServiceAbstract {

    @Autowired
    private ConfigurationPageRepository configurationPageRepository;
    @Autowired
    private PagePropertyService pagePropertyService;

    public ConfigurationPageService() {
        super();
    }

    public ConfigurationPageRepository getConfigurationPageRepository() {
        return configurationPageRepository;
    }

    public ConfigurationPage create(ConfigurationPage configurationPage, String who) {
        Assert.notNull(configurationPage, "serviceRuntime is null");
        Assert.hasText(who, "who is empty");

        String method = "create";
        logDebug(method, "...");

        if (StringUtils.isEmpty(configurationPage.getWidgetConfigurationId())) {
            logDebug(method, "widgetConfigurationId is empty");
            throw new AcsLogicalException("widgetConfigurationId is empty");
        }

        if (StringUtils.isEmpty(configurationPage.getName())) {
            logDebug(method, "name is empty");
            throw new AcsLogicalException("name is empty");
        }

        if (StringUtils.isEmpty(configurationPage.getLabel())) {
            logDebug(method, "label is empty");
            throw new AcsLogicalException("label is empty");
        }

        configurationPage = configurationPageRepository.doInsert(configurationPage, who);

        logDebug(method, "configurationPage: %s has been created for widgetConfiguration: %s",
                configurationPage.getId(), configurationPage.getWidgetConfigurationId());

        return configurationPage;
    }

    public ConfigurationPage update(ConfigurationPage configurationPage, String who) {
        Assert.notNull(configurationPage, "ConfigurationPage is null");
        Assert.hasText(who, "who is empty");

        String method = "update";
        logDebug(method, "...");

        if (StringUtils.isEmpty(configurationPage.getWidgetConfigurationId())) {
            logDebug(method, "widgetConfigurationId is empty");
            throw new AcsLogicalException("widgetConfigurationId is empty");
        }

        if (StringUtils.isEmpty(configurationPage.getName())) {
            logDebug(method, "name is empty");
            throw new AcsLogicalException("name is empty");
        }

        if (StringUtils.isEmpty(configurationPage.getLabel())) {
            logDebug(method, "label is empty");
            throw new AcsLogicalException("label is empty");
        }

        configurationPage = configurationPageRepository.doSave(configurationPage, who);

        logDebug(method, "configurationPage: %s has been updated for widgetConfiguration: %s",
                configurationPage.getId(), configurationPage.getWidgetConfigurationId());

        return configurationPage;
    }

    public void delete(String configurationPageId) {
        Assert.hasText(configurationPageId, "configurationPageId is empty");

        String method = "delete";
        logDebug(method, "...");

        ConfigurationPage configurationPage = configurationPageRepository.findById(configurationPageId).orElse(null);
        Assert.notNull(configurationPage, "Configuration Page not found! configurationPageId: " + configurationPageId);

        // page properties
        PagePropertySearchParams pagePropertySearchParams = new PagePropertySearchParams();
        pagePropertySearchParams.addConfigurationPageIds(configurationPage.getId());
        for (PageProperty pageProperty : pagePropertyService.getPagePropertyRepository()
                .findPageProperties(pagePropertySearchParams))
            pagePropertyService.delete(pageProperty.getId());

        // configuration page
        configurationPageRepository.delete(configurationPage);

        logDebug(method, "configurationPage: %s has been deleted from widgetConfiguration: %s", configurationPageId,
                configurationPage.getWidgetConfigurationId());
    }
}

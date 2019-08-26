package com.arrow.pegasus.dashboard.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.dashboard.data.ConfigurationPage;
import com.arrow.pegasus.dashboard.data.WidgetConfiguration;
import com.arrow.pegasus.dashboard.repo.ConfigurationPageSearchParams;
import com.arrow.pegasus.dashboard.repo.WidgetConfigurationRepository;

import moonstone.acs.AcsLogicalException;

@Service
public class WidgetConfigurationService extends DashboardServiceAbstract {

    @Autowired
    private WidgetConfigurationRepository widgetConfigurationRepository;
    @Autowired
    private ConfigurationPageService configurationPageService;

    public WidgetConfigurationService() {
        super();
    }

    public WidgetConfigurationRepository getWidgetConfigurationRepository() {
        return widgetConfigurationRepository;
    }

    public WidgetConfiguration create(WidgetConfiguration widgetConfiguration, String who) {
        Assert.notNull(widgetConfiguration, "widgetConfiguration is null");
        Assert.hasText(who, "who is empty");

        String method = "create";
        logDebug(method, "...");

        if (StringUtils.isEmpty(widgetConfiguration.getWidgetId())) {
            logDebug(method, "widgetId is empty");
            throw new AcsLogicalException("widgetId is empty");
        }

        if (StringUtils.isEmpty(widgetConfiguration.getName())) {
            logDebug(method, "name is empty");
            throw new AcsLogicalException("name is empty");
        }

        if (StringUtils.isEmpty(widgetConfiguration.getLabel())) {
            logDebug(method, "label is empty");
            throw new AcsLogicalException("label is empty");
        }

        widgetConfiguration = widgetConfigurationRepository.doInsert(widgetConfiguration, who);

        logDebug(method, "widgetConfiguration: %s has been created for widget: %s", widgetConfiguration.getId(),
                widgetConfiguration.getWidgetId());

        return widgetConfiguration;
    }

    public WidgetConfiguration update(WidgetConfiguration widgetConfiguration, String who) {
        Assert.notNull(widgetConfiguration, "WidgetConfiguration is null");
        Assert.hasText(who, "who is empty");

        String method = "update";
        logDebug(method, "...");

        if (StringUtils.isEmpty(widgetConfiguration.getWidgetId())) {
            logDebug(method, "widgetId is empty");
            throw new AcsLogicalException("widgetId is empty");
        }

        if (StringUtils.isEmpty(widgetConfiguration.getName())) {
            logDebug(method, "name is empty");
            throw new AcsLogicalException("name is empty");
        }

        if (StringUtils.isEmpty(widgetConfiguration.getLabel())) {
            logDebug(method, "label is empty");
            throw new AcsLogicalException("label is empty");
        }

        widgetConfiguration = widgetConfigurationRepository.doSave(widgetConfiguration, who);

        logDebug(method, "widgetConfiguration: %s has been updated for widget: %s", widgetConfiguration.getId(),
                widgetConfiguration.getWidgetId());

        return widgetConfiguration;
    }

    public void delete(String widgetConfigurationId) {
        Assert.hasText(widgetConfigurationId, "widgetConfigurationId is empty");

        String method = "delete";
        logDebug(method, "...");

        WidgetConfiguration widgetConfiguration = widgetConfigurationRepository.findById(widgetConfigurationId)
                .orElse(null);
        Assert.notNull(widgetConfiguration,
                "Widget Configuration not found! widgetConfigurationId: " + widgetConfigurationId);

        // configuration pages
        ConfigurationPageSearchParams configurationPageSearchParams = new ConfigurationPageSearchParams();
        configurationPageSearchParams.addWidgetConfigurationIds(widgetConfiguration.getId());
        for (ConfigurationPage configurationPage : configurationPageService.getConfigurationPageRepository()
                .findConfigurationPages(configurationPageSearchParams))
            configurationPageService.delete(configurationPage.getId());

        // widget configuration
        widgetConfigurationRepository.delete(widgetConfiguration);

        logDebug(method, "widgetConfiguration: %s has been deleted from widget: %s", widgetConfigurationId,
                widgetConfiguration.getWidgetId());
    }

}

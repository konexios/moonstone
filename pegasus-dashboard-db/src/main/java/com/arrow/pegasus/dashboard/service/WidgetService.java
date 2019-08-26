package com.arrow.pegasus.dashboard.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.dashboard.data.Widget;
import com.arrow.pegasus.dashboard.data.WidgetConfiguration;
import com.arrow.pegasus.dashboard.repo.WidgetConfigurationSearchParams;
import com.arrow.pegasus.dashboard.repo.WidgetRepository;

import moonstone.acs.AcsLogicalException;

@Service
public class WidgetService extends DashboardServiceAbstract {

    @Autowired
    private WidgetRepository widgetRepository;
    @Autowired
    private WidgetConfigurationService widgetConfigurationService;

    public WidgetService() {
        super();
    }

    public WidgetRepository getWidgetRepository() {
        return widgetRepository;
    }

    public Widget create(Widget widget, String who) {
        Assert.notNull(widget, "widget is null");
        Assert.hasText(who, "who is empty");

        String method = "create";
        logDebug(method, "...");

        if (StringUtils.isEmpty(widget.getParentId())) {
            logDebug(method, "parentId is empty");
            throw new AcsLogicalException("parentId is empty");
        }

        if (StringUtils.isEmpty(widget.getWidgetTypeId())) {
            logDebug(method, "widgetTypeId is empty");
            throw new AcsLogicalException("widgetTypeId is empty");
        }

        if (StringUtils.isEmpty(widget.getName())) {
            logDebug(method, "name is empty");
            throw new AcsLogicalException("name is empty");
        }

        widget = widgetRepository.doInsert(widget, who);

        logDebug(method, "widget: %s has been created for parent: %s", widget.getId(), widget.getParentId());

        return widget;
    }

    public Widget update(Widget widget, String who) {
        Assert.notNull(widget, "Widget is null");
        Assert.hasText(who, "who is empty");

        String method = "update";
        logDebug(method, "...");

        if (StringUtils.isEmpty(widget.getParentId())) {
            logDebug(method, "parentId is empty");
            throw new AcsLogicalException("parentId is empty");
        }

        if (StringUtils.isEmpty(widget.getWidgetTypeId())) {
            logDebug(method, "widgetTypeId is empty");
            throw new AcsLogicalException("widgetTypeId is empty");
        }

        if (StringUtils.isEmpty(widget.getName())) {
            logDebug(method, "name is empty");
            throw new AcsLogicalException("name is empty");
        }

        widget = widgetRepository.doSave(widget, who);

        logDebug(method, "widget: %s has been updated for parent: %s", widget.getId(), widget.getParentId());

        return widget;
    }

    public void delete(String widgetId) {
        Assert.hasText(widgetId, "widgetId is empty");

        String method = "delete";
        logDebug(method, "...");

        Widget widget = widgetRepository.findById(widgetId).orElse(null);
        Assert.notNull(widget, "Widget not found! widgetId: " + widgetId);

        // widget configurations
        WidgetConfigurationSearchParams widgetConfigurationSearchParams = new WidgetConfigurationSearchParams();
        widgetConfigurationSearchParams.addWidgetIds(widget.getId());
        for (WidgetConfiguration widgetConfiguration : widgetConfigurationService.getWidgetConfigurationRepository()
                .findWidgetConfigurations(widgetConfigurationSearchParams))
            widgetConfigurationService.delete(widgetConfiguration.getId());

        // widget
        widgetRepository.delete(widget);

        logDebug(method, "widget: %s has been deleted from parent: %s", widgetId, widget.getParentId());
    }

}

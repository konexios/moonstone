package com.arrow.apollo.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.apollo.data.ApolloWidget;
import com.arrow.apollo.repo.ApolloWidgetRepository;
import com.arrow.pegasus.dashboard.service.WidgetTypeService;
import com.arrow.pegasus.service.ServiceAbstract;

@Service
public class ApolloWidgetService extends ServiceAbstract {

    @Autowired
    private WidgetTypeService widgetTypeService;
    @Autowired
    private ApolloWidgetRepository apolloWidgetRepository;

    public ApolloWidgetRepository getApolloWidgetRepository() {
        return apolloWidgetRepository;
    }

    public ApolloWidget create(ApolloWidget apolloWidgetType, String who) {
        Assert.notNull(apolloWidgetType, "apolloWidgetType is null");
        Assert.hasText(apolloWidgetType.getName(), "name is empty");
        Assert.hasText(apolloWidgetType.getDescription(), "description is empty");
        Assert.hasText(apolloWidgetType.getWidgetTypeId(), "widgetTypeId is empty");
        Assert.notNull(apolloWidgetType.getCategory(), "category is null");
        Assert.notNull(apolloWidgetType.getIconType(), "iconType is null");
        Assert.hasText(apolloWidgetType.getIcon(), "icon is empty");
        Assert.hasText(who, "who is empty");

        String method = "create";
        logDebug(method, "...");

        apolloWidgetType = apolloWidgetRepository.doInsert(apolloWidgetType, who);

        return apolloWidgetType;
    }

    public ApolloWidget update(ApolloWidget apolloWidgetType, String who) {
        Assert.notNull(apolloWidgetType, "apolloWidgetType is null");
        Assert.hasText(apolloWidgetType.getId(), "id is empty");
        Assert.hasText(apolloWidgetType.getName(), "name is empty");
        Assert.hasText(apolloWidgetType.getDescription(), "description is empty");
        Assert.hasText(apolloWidgetType.getWidgetTypeId(), "widgetTypeId is empty");
        Assert.notNull(apolloWidgetType.getCategory(), "category is null");
        Assert.notNull(apolloWidgetType.getIconType(), "iconType is null");
        Assert.hasText(apolloWidgetType.getIcon(), "icon is empty");
        Assert.hasText(who, "who is empty");

        String method = "update";
        logDebug(method, "...");

        apolloWidgetType = apolloWidgetRepository.doSave(apolloWidgetType, who);

        return apolloWidgetType;
    }

    public void delete(String apolloWidgetTypeId) {
        Assert.hasText(apolloWidgetTypeId, "apolloWidgetTypeId is empty");

        String method = "delete";
        logDebug(method, "...");

        ApolloWidget apolloWidgetType = apolloWidgetRepository.findById(apolloWidgetTypeId).orElse(null);
        Assert.notNull(apolloWidgetType, "ApolloWidgetType not found! apolloWidgetTypeId:" + apolloWidgetTypeId);

        apolloWidgetRepository.deleteById(apolloWidgetTypeId);
    }

    public ApolloWidget populate(ApolloWidget apolloWidget) {

        if (apolloWidget != null) {
            if (!StringUtils.isEmpty(apolloWidget.getWidgetTypeId()) && apolloWidget.getRefWidgetType() == null)
                apolloWidget.setRefWidgetType(widgetTypeService.getWidgetTypeRepository()
                        .findById(apolloWidget.getWidgetTypeId()).orElse(null));
        }

        return apolloWidget;
    }
}
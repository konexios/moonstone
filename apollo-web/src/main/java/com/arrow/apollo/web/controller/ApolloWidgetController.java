package com.arrow.apollo.web.controller;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.apollo.data.ApolloWidget;
import com.arrow.apollo.data.ApolloWidgetTypeCategories;
import com.arrow.apollo.data.IconTypes;
import com.arrow.apollo.service.ApolloWidgetService;
import com.arrow.apollo.web.model.ApolloModelUtil;
import com.arrow.apollo.web.model.ApolloWidgetModels;
import com.arrow.apollo.web.model.ApolloWidgetModels.ApolloWidgetListModel;
import com.arrow.apollo.web.model.WidgetTypeModels;
import com.arrow.pegasus.dashboard.data.WidgetType;
import com.arrow.pegasus.dashboard.service.WidgetTypeService;

@RestController
@RequestMapping("/api/apollo/settings/widgets")
public class ApolloWidgetController extends ApolloControllerAbstract {

    @Autowired
    private WidgetTypeService widgetTypeService;

    @Autowired
    private ApolloWidgetService apolloWidgetService;

    @RequestMapping("/all")
    public List<ApolloWidgetModels.ApolloWidgetListModel> findAll(HttpSession session) {

        String method = "findAll";
        logDebug(method, "...");

        List<ApolloWidget> all = apolloWidgetService.getApolloWidgetRepository().findAll();

        List<ApolloWidgetModels.ApolloWidgetListModel> models = new ArrayList<>();
        for (ApolloWidget apolloWidget : all) {
            ApolloWidgetListModel model = ApolloModelUtil.toApolloWidgetListModel(apolloWidget);
            WidgetType widgetType = widgetTypeService.getWidgetTypeRepository().findById(apolloWidget.getWidgetTypeId())
                    .orElse(null);
            model.setWidgetTypeName(widgetType.getName());
            models.add(model);
        }

        return models;
    }

    @RequestMapping("/{widgetId}/widget")
    public ApolloWidgetModels.ApolloWidgetUpsertModel findWidget(@PathVariable String widgetId, HttpSession session) {
        Assert.hasText(widgetId, "widgetId is empty");

        String method = "findWidget";
        logDebug(method, "...");

        ApolloWidgetModels.ApolloWidgetUpsertModel model = new ApolloWidgetModels.ApolloWidgetUpsertModel();

        ApolloWidget widget = null;
        if (!widgetId.equals("new")) {
            widget = apolloWidgetService.getApolloWidgetRepository().findById(widgetId).orElse(null);
            Assert.notNull(widget, "Widget not found! widgetId: " + widgetId);
        } else {
            widget = new ApolloWidget();
            widget.setId("new");
        }

        List<WidgetTypeModels.WidgetTypeOptionModel> widgetTypeModels = new ArrayList<>();
        for (WidgetType widgetType : widgetTypeService.getWidgetTypeRepository().findAll())
            widgetTypeModels.add(new WidgetTypeModels.WidgetTypeOptionModel(widgetType.getId(), widgetType.getName(),
                    widgetType.getDescription()));

        model.withApolloWidget(ApolloModelUtil.toApolloWidgetModel(widget));
        model.withWidgetTypes(widgetTypeModels);
        model.withCategories(EnumSet.allOf(ApolloWidgetTypeCategories.class));
        model.withIconTypes(EnumSet.allOf(IconTypes.class));

        return model;
    }

    @RequestMapping(value = "/widget/create", method = RequestMethod.POST)
    public ApolloWidgetModels.ApolloWidgetModel createWidget(@RequestBody ApolloWidgetModels.ApolloWidgetModel model,
            HttpSession session) {
        Assert.notNull(model, "model is null");
        Assert.hasText(model.getName(), "name is empty");
        Assert.hasText(model.getDescription(), "description is empty");
        Assert.hasText(model.getWidgetTypeId(), "widgetTypeId is empty");
        Assert.notNull(model.getCategory(), "category is null");
        Assert.notNull(model.getIconType(), "iconType is null");
        Assert.hasText(model.getIcon(), "icon is empty");

        String method = "createWidget";
        logDebug(method, "...");

        String userId = getUserId();

        ApolloWidget widget = ApolloModelUtil.toApolloWidget(model);
        widget = apolloWidgetService.create(widget, userId);

        return ApolloModelUtil.toApolloWidgetModel(widget);
    }

    @RequestMapping(value = "/{widgetId}/widget/update", method = RequestMethod.PUT)
    public ApolloWidgetModels.ApolloWidgetModel updateWidget(@PathVariable String widgetId,
            @RequestBody ApolloWidgetModels.ApolloWidgetModel model, HttpSession session) {
        Assert.hasText(widgetId, "widgetId is empty");
        Assert.notNull(model, "model is null");
        Assert.hasText(model.getName(), "name is empty");
        Assert.hasText(model.getDescription(), "description is empty");
        Assert.hasText(model.getWidgetTypeId(), "widgetTypeId is empty");
        Assert.notNull(model.getCategory(), "category is null");
        Assert.notNull(model.getIconType(), "iconType is null");
        Assert.hasText(model.getIcon(), "icon is empty");

        String method = "updateWidget";
        logDebug(method, "...");

        ApolloWidget existing = apolloWidgetService.getApolloWidgetRepository().findById(widgetId).orElse(null);
        Assert.notNull(existing, "Widget not found! widgetId: " + widgetId);

        ApolloWidget apolloWidget = ApolloModelUtil.toApolloWidget(model, existing);
        apolloWidget = apolloWidgetService.update(apolloWidget, getUserId());

        return ApolloModelUtil.toApolloWidgetModel(apolloWidget);
    }
}
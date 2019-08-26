package com.arrow.apollo.web.controller;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.apollo.data.ApolloWidget;
import com.arrow.apollo.data.ApolloWidgetTypeCategories;
import com.arrow.apollo.repo.ApolloWidgetSearchParams;
import com.arrow.apollo.service.ApolloWidgetService;
import com.arrow.apollo.web.model.ApolloWidgetModels;
import com.arrow.apollo.web.model.ApolloWidgetModels.ApolloWidgetSupportedSizesModel;
import com.arrow.dashboard.runtime.WidgetRuntimeDefinitionManager;
import com.arrow.pegasus.dashboard.data.WidgetType;
import com.arrow.pegasus.dashboard.service.WidgetTypeService;

import moonstone.acs.AcsLogicalException;

@RestController
@RequestMapping("/api/apollo/runtime/boards")
public class ApolloBoardRuntimeController extends ApolloControllerAbstract {

    @Autowired
    private WidgetRuntimeDefinitionManager widgetRuntimeDefinitionManager;
    @Autowired
    private WidgetTypeService widgetTypeService;
    @Autowired
    private ApolloWidgetService apolloWidgetService;

    @RequestMapping(value = "/board/widgettypes/count", method = RequestMethod.GET)
    public ApolloWidgetModels.ApolloWidgetCountsModel getAvailableWidgetCounts(HttpSession session) {

        String method = "getAvailableWidgetCounts";
        logDebug(method, "...");

        ApolloWidgetModels.ApolloWidgetCountsModel model = new ApolloWidgetModels.ApolloWidgetCountsModel();
        for (ApolloWidgetTypeCategories category : ApolloWidgetTypeCategories.values()) {

            ApolloWidgetSearchParams params = new ApolloWidgetSearchParams();
            params.setCategories(EnumSet.of(category));
            params.setEnabled(true);

            long count = apolloWidgetService.getApolloWidgetRepository().countApolloWidgets(params);

            switch (category) {
            case Device:
                model.setDeviceWidgetCount(count);
                break;
            case Gateway:
                model.setGatewayWidgetCount(count);
                break;
            case Usage:
                model.setUsageWidgetCount(count);
                break;
            case General:
                model.setGeneralWidgetCount(count);
                break;
            default:
                throw new AcsLogicalException("Unsupported category! category:" + category);
            }
        }

        return model;
    }

    @RequestMapping(value = "/board/{category}/widgettypes", method = RequestMethod.GET)
    public List<ApolloWidgetModels.ApolloWidgetIconModel> getAvailableWidgetTypes(@PathVariable String category,
            HttpSession session) {

        String method = "getAvailableWidgetTypes";
        logDebug(method, "...");

        ApolloWidgetSearchParams params = new ApolloWidgetSearchParams();
        params.setCategories(EnumSet.of(ApolloWidgetTypeCategories.valueOf(category)));
        params.setEnabled(true);
        List<ApolloWidget> apolloWidgets = apolloWidgetService.getApolloWidgetRepository().findApolloWidgets(params,
                new Sort(Direction.ASC, "name"));

        List<ApolloWidgetModels.ApolloWidgetIconModel> models = new ArrayList<>();
        for (ApolloWidget apolloWidget : apolloWidgets) {
            // widgetTypes contains all widget definitions from database
            // some of them may not be presented in classpath (can not
            // instantiate) let's filter here to show only really existing
            // widget types
            if (widgetRuntimeDefinitionManager.widgetRuntimeDefinitionExists(apolloWidget.getWidgetTypeId())) {
                ApolloWidgetModels.ApolloWidgetIconModel model = new ApolloWidgetModels.ApolloWidgetIconModel(
                        apolloWidget.getId(), apolloWidget.getName(), apolloWidget.getDescription());
                model.setCategory(apolloWidget.getCategory());
                model.setIcon(apolloWidget.getIcon());
                model.setIconType(apolloWidget.getIconType());
                model.setWidgetTypeId(apolloWidget.getWidgetTypeId());

                // populate refWidgetType
                apolloWidget = apolloWidgetService.populate(apolloWidget);

                // sizes
                model.setSupportedSizes(
                        new ApolloWidgetSupportedSizesModel(apolloWidget.getRefWidgetType().getSizes()));
                models.add(model);
            }
        }

        return models;
    }

    @RequestMapping(value = "/board/widgettypes/{widgetTypeId}/sizes", method = RequestMethod.GET)
    public ApolloWidgetSupportedSizesModel getWidgetTypeSizes(@PathVariable String widgetTypeId, HttpSession session) {

        String method = "getWidgetTypeSizes";
        logDebug(method, "...");

        WidgetType widgetType = widgetTypeService.getWidgetTypeRepository().findById(widgetTypeId).orElse(null);
        Assert.notNull(widgetType, "WidgetType not found! widgetTypeId=" + widgetTypeId);

        // sizes
        return new ApolloWidgetSupportedSizesModel(widgetType.getSizes());
    }
}
package com.arrow.dashboard.web.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.dashboard.web.model.ConvertModelUtils;
import com.arrow.dashboard.web.model.WidgetModels;
import com.arrow.pegasus.dashboard.data.Board;
import com.arrow.pegasus.dashboard.data.Container;
import com.arrow.pegasus.dashboard.data.Widget;
import com.arrow.pegasus.dashboard.data.WidgetType;
import com.arrow.pegasus.dashboard.service.BoardService;
import com.arrow.pegasus.dashboard.service.ContainerService;
import com.arrow.pegasus.dashboard.service.WidgetService;
import com.arrow.pegasus.dashboard.service.WidgetTypeService;

@RestController
public class CoreWidgetController extends CoreDashboardControllerAbstract {

    @Autowired
    private WidgetService widgetService;
    @Autowired
    private WidgetTypeService widgetTypeService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private ContainerService containerService;

    public WidgetModels.CreateWidgetResponse createWidget(@RequestBody WidgetModels.CreateWidgetRequest model,
            HttpSession session) {
        Assert.notNull(model, "model is null");

        String method = "createWidget";
        logInfo(method, "...");

        Widget widget = ConvertModelUtils.toWidget(model.getWidget());
        widget = widgetService.create(widget, getUserId());

        return new WidgetModels.CreateWidgetResponse().withWidget(ConvertModelUtils.toReadWidget(widget));
    }

    public WidgetModels.ReadWidgetModel readWidget(@PathVariable String widgetId) {
        Assert.hasText(widgetId, "widgetId is empty");

        String method = "readWidget";
        logInfo(method, "...");

        Widget widget = widgetService.getWidgetRepository().findById(widgetId).orElse(null);
        Assert.notNull(widget, "Widget not found! widgetId=" + widgetId);

        WidgetType widgetType = widgetTypeService.getWidgetTypeRepository().findById(widget.getWidgetTypeId())
                .orElse(null);
        Assert.notNull(widgetType, "WidgetType not found! widgetTypeId=" + widget.getWidgetTypeId());

        String parentName = null;
        if (!StringUtils.isEmpty(widget.getParentId())) {
            // lookup board
            Board board = boardService.getBoardRepository().findById(widget.getParentId()).orElse(null);
            if (board == null) {
                // lookup container
                Container container = containerService.getContainerRepository().findById(widget.getParentId())
                        .orElse(null);
                if (container != null)
                    parentName = container.getName();
            } else {
                parentName = board.getName();
            }
        }

        WidgetModels.ReadWidgetModel model = ConvertModelUtils.toReadWidget(widget);
        model.withWidgetTypeName(widgetType.getName()).withParentName(parentName);

        return model;
    }

    public WidgetModels.UpdateWidgetResponse updateWidget(@RequestBody WidgetModels.UpdateWidgetRequest model,
            HttpSession session) {
        Assert.notNull(model, "model is null");
        Assert.notNull(model.getWidget(), "widget is null");
        Assert.hasText(model.getWidget().getId(), "widgetId is empty");

        String method = "updateWidget";
        logInfo(method, "...");

        Widget widget = widgetService.getWidgetRepository().findById(model.getWidget().getId()).orElse(null);
        Assert.notNull(widget, "Widget not found! widgetId=" + model.getWidget().getId());

        widget = ConvertModelUtils.toWidget(model.getWidget(), widget);
        widget = widgetService.update(widget, getUserId());

        return new WidgetModels.UpdateWidgetResponse().withWidget(ConvertModelUtils.toReadWidget(widget));
    }

    public WidgetModels.DeleteWidgetResponse deleteWidget(@PathVariable String widgetId) {
        Assert.hasText(widgetId, "widgetId is empty");

        String method = "deleteWidget";
        logInfo(method, "...");

        Widget widget = widgetService.getWidgetRepository().findById(widgetId).orElse(null);
        Assert.notNull(widget, "Widget not found! widgetId=" + widgetId);

        widgetService.delete(widgetId);

        return new WidgetModels.DeleteWidgetResponse();
    }

    // TODO support find widgets
}
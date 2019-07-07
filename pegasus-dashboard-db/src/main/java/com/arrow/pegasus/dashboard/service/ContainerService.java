package com.arrow.pegasus.dashboard.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.pegasus.dashboard.data.Container;
import com.arrow.pegasus.dashboard.data.Widget;
import com.arrow.pegasus.dashboard.repo.ContainerRepository;
import com.arrow.pegasus.dashboard.repo.WidgetSearchParams;

@Service
public class ContainerService extends DashboardServiceAbstract {

    @Autowired
    private ContainerRepository containerRepository;
    @Autowired
    private WidgetService widgetService;

    public ContainerService() {
        super();
    }

    public ContainerRepository getContainerRepository() {
        return containerRepository;
    }

    public Container create(Container container, String who) {
        Assert.notNull(container, "container is null");
        Assert.hasText(who, "who is empty");

        String method = "create";
        logDebug(method, "...");

        if (StringUtils.isEmpty(container.getBoardId())) {
            logDebug(method, "boardId is empty");
            throw new AcsLogicalException("boardId is empty");
        }

        if (StringUtils.isEmpty(container.getName())) {
            logDebug(method, "name is empty");
            throw new AcsLogicalException("name is empty");
        }

        container = containerRepository.doInsert(container, who);

        logDebug(method, "container: %s has been created for board: %s", container.getId(), container.getBoardId());

        return container;
    }

    public Container update(Container container, String who) {
        Assert.notNull(container, "Container is null");
        Assert.hasText(who, "who is empty");

        String method = "update";
        logDebug(method, "...");

        if (StringUtils.isEmpty(container.getBoardId())) {
            logDebug(method, "boardId is empty");
            throw new AcsLogicalException("boardId is empty");
        }

        if (StringUtils.isEmpty(container.getName())) {
            logDebug(method, "name is empty");
            throw new AcsLogicalException("name is empty");
        }

        container = containerRepository.doSave(container, who);

        logDebug(method, "container: %s has been updated for board: %s", container.getId(), container.getBoardId());

        return container;
    }

    public void delete(String containerId) {
        Assert.hasText(containerId, "containerId is empty");

        String method = "delete";
        logDebug(method, "...");

        Container container = containerRepository.findById(containerId).orElse(null);
        Assert.notNull(container, "Container not found! containerId: " + containerId);

        // widgets
        WidgetSearchParams widgetSearchParams = new WidgetSearchParams();
        widgetSearchParams.addParentIds(container.getId());
        for (Widget widget : widgetService.getWidgetRepository().findWidgets(widgetSearchParams))
            widgetService.delete(widget.getId());

        // container
        containerRepository.delete(container);

        logDebug(method, "container: %s has been deleted from board: %s", containerId, container.getBoardId());
    }

}

package com.arrow.dashboard.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.dashboard.web.model.ContainerModels;
import com.arrow.dashboard.web.model.ConvertModelUtils;
import com.arrow.pegasus.dashboard.data.Container;
import com.arrow.pegasus.dashboard.service.ContainerService;

@RestController
public class CoreContainerController extends CoreDashboardControllerAbstract {

    @Autowired
    private ContainerService containerService;

    public ContainerModels.CreateContainerResponse createContainer(
            @RequestBody ContainerModels.CreateContainerRequest model, HttpSession session) {
        Assert.notNull(model, "model is null");

        String method = "createContainer";
        logInfo(method, "...");

        Container container = ConvertModelUtils.toContainer(model.getContainer());
        container = containerService.create(container, getUserId());

        return new ContainerModels.CreateContainerResponse()
                .withContainer(ConvertModelUtils.toReadContainer(container));
    }

    public ContainerModels.ReadContainerModel readContainer(@PathVariable String containerId) {
        Assert.hasText(containerId, "containerId is empty");

        String method = "readContainer";
        logInfo(method, "...");

        Container container = containerService.getContainerRepository().findById(containerId).orElse(null);
        Assert.notNull(container, "Container not found! containerId=" + containerId);

        return ConvertModelUtils.toReadContainer(container);
    }

    public ContainerModels.UpdateContainerResponse updateContainer(
            @RequestBody ContainerModels.UpdateContainerRequest model, HttpSession session) {
        Assert.notNull(model, "model is null");
        Assert.notNull(model.getContainer(), "container is null");
        Assert.hasText(model.getContainer().getId(), "containerId is empty");

        String method = "updateContainer";
        logInfo(method, "...");

        Container container = containerService.getContainerRepository().findById(model.getContainer().getId())
                .orElse(null);
        Assert.notNull(container, "Container not found! containerId=" + model.getContainer().getId());

        container = ConvertModelUtils.toContainer(model.getContainer(), container);
        container = containerService.update(container, getUserId());

        return new ContainerModels.UpdateContainerResponse()
                .withContainer(ConvertModelUtils.toReadContainer(container));
    }

    public ContainerModels.DeleteContainerResponse deleteContainer(@PathVariable String containerId) {
        Assert.hasText(containerId, "containerId is empty");

        String method = "deleteContainer";
        logInfo(method, "...");

        Container container = containerService.getContainerRepository().findById(containerId).orElse(null);
        Assert.notNull(container, "Container not found! containerId=" + containerId);

        containerService.delete(containerId);

        return new ContainerModels.DeleteContainerResponse();
    }

    // TODO support find containers
}
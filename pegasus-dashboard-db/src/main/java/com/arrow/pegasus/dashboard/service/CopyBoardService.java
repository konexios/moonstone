package com.arrow.pegasus.dashboard.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.pegasus.dashboard.data.Board;
import com.arrow.pegasus.dashboard.data.ConfigurationPage;
import com.arrow.pegasus.dashboard.data.Container;
import com.arrow.pegasus.dashboard.data.PageProperty;
import com.arrow.pegasus.dashboard.data.PropertyValue;
import com.arrow.pegasus.dashboard.data.Widget;
import com.arrow.pegasus.dashboard.data.WidgetConfiguration;
import com.arrow.pegasus.dashboard.model.CopyBoardModels;
import com.arrow.pegasus.dashboard.repo.ConfigurationPageSearchParams;
import com.arrow.pegasus.dashboard.repo.ContainerSearchParams;
import com.arrow.pegasus.dashboard.repo.PagePropertySearchParams;
import com.arrow.pegasus.dashboard.repo.PropertyValueSearchParams;
import com.arrow.pegasus.dashboard.repo.WidgetConfigurationSearchParams;
import com.arrow.pegasus.dashboard.repo.WidgetSearchParams;

@Service
public class CopyBoardService extends DashboardServiceAbstract {

    @Autowired
    private BoardService boardService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    private WidgetService widgetService;
    @Autowired
    private WidgetConfigurationService widgetConfigurationService;
    @Autowired
    private ConfigurationPageService configurationPageService;
    @Autowired
    private PagePropertyService pagePropertyService;
    @Autowired
    private PropertyValueService propertyValueService;

    public CopyBoardModels.CopyOfBoard copyBoard(String boardId) {
        Assert.hasText(boardId, "boardId is empty");

        String method = "copyBoard";
        logDebug(method, "...");

        Board existing = boardService.getBoardRepository().findById(boardId).orElse(null);
        Assert.notNull(existing, "Board not found! boardId:" + boardId);

        logDebug(method, "Attempting to copy board: %s", existing.getName());

        Board clone = new Board();
        clone.setApplicationId(existing.getApplicationId());
        clone.setCategory(existing.getCategory());
        clone.setDescription(existing.getDescription());
        clone.setName(existing.getName());
        clone.setProductId(existing.getProductId());
        clone.setUserId(existing.getUserId());
        clone.setLayout(existing.getLayout());
        clone.setLayoutClass(existing.getLayoutClass());

        // containers
        ContainerSearchParams containerParams = new ContainerSearchParams();
        containerParams.addBoardIds(existing.getId());
        List<Container> existingContainers = containerService.getContainerRepository().findContainers(containerParams);
        logDebug(method, "# of containers: %s", existingContainers.size());

        List<CopyBoardModels.CopyOfContainer> clonedContainers = new ArrayList<>();
        for (Container container : existingContainers)
            clonedContainers.add(copyContainer(container));

        // widgets
        WidgetSearchParams widgetParams = new WidgetSearchParams();
        widgetParams.addParentIds(existing.getId());
        List<Widget> existingWidgets = widgetService.getWidgetRepository().findWidgets(widgetParams);
        logDebug(method, "# of widgets: %s", existingWidgets.size());

        List<CopyBoardModels.CopyOfWidget> clonedWidgets = new ArrayList<>();
        for (Widget widget : existingWidgets)
            clonedWidgets.add(copyWidget(widget));

        // build copy
        logDebug(method, "Building board copy...");
        CopyBoardModels.CopyOfBoard copy = new CopyBoardModels.CopyOfBoard().withBoard(clone);

        // containers
        if (!clonedContainers.isEmpty())
            copy.withContainers(clonedContainers);

        // widgets
        if (!clonedWidgets.isEmpty())
            copy.withWidgets(clonedWidgets);

        return copy;
    }

    public CopyBoardModels.CopyOfContainer copyContainer(String containerId) {
        Assert.hasText(containerId, "containerId is empty");

        String method = "copyContainer";
        logDebug(method, "...");

        Container existing = containerService.getContainerRepository().findById(containerId).orElse(null);
        Assert.notNull(existing, "Container not found! containerId:" + containerId);

        return copyContainer(existing);
    }

    public CopyBoardModels.CopyOfContainer copyContainer(Container existing) {
        Assert.notNull(existing, "container is null");

        String method = "copyContainer";
        logDebug(method, "...");

        Container clone = new Container();
        clone.setBoardId(null);
        clone.setDescription(existing.getDescription());
        clone.setLayout(existing.getLayout());
        clone.setName(existing.getName());
        clone.setLayout(existing.getLayout());
        clone.setLayoutClass(existing.getLayoutClass());

        // widgets
        WidgetSearchParams widgetParams = new WidgetSearchParams();
        widgetParams.addParentIds(existing.getId());
        List<Widget> existingWidgets = widgetService.getWidgetRepository().findWidgets(widgetParams);
        logDebug(method, "# of widgets: %s", existingWidgets.size());

        List<CopyBoardModels.CopyOfWidget> clonedWidgets = new ArrayList<>();
        for (Widget widget : existingWidgets)
            clonedWidgets.add(copyWidget(widget));

        // build copy
        logDebug(method, "Building container copy...");
        CopyBoardModels.CopyOfContainer copy = new CopyBoardModels.CopyOfContainer().withContainer(clone);

        // widgets
        if (!clonedWidgets.isEmpty())
            copy.withWidgets(clonedWidgets);

        return copy;
    }

    public CopyBoardModels.CopyOfWidget copyWidget(String widgetId) {
        Assert.hasText(widgetId, "widgetId is empty");

        String method = "copyWidget";
        logDebug(method, "...");

        Widget existing = widgetService.getWidgetRepository().findById(widgetId).orElse(null);
        Assert.notNull(existing, "Widget not found! widgetId:" + widgetId);

        return copyWidget(existing);
    }

    public CopyBoardModels.CopyOfWidget copyWidget(Widget existing) {
        Assert.notNull(existing, "widget is null");

        String method = "copyWidget";
        logDebug(method, "...");

        Widget clone = new Widget();
        clone.setDescription(existing.getDescription());
        clone.setLayoutClass(existing.getLayoutClass());
        clone.setLayout(existing.getLayout());
        clone.setName(existing.getName());
        clone.setParentId(null);
        clone.setParentType(existing.getParentType());
        clone.setWidgetTypeId(existing.getWidgetTypeId());
        clone.setLayout(existing.getLayout());
        clone.setLayoutClass(existing.getLayoutClass());

        // widget configurations
        WidgetConfigurationSearchParams params = new WidgetConfigurationSearchParams();
        params.addWidgetIds(existing.getId());
        List<WidgetConfiguration> existingWidgetConfigurations = widgetConfigurationService
                .getWidgetConfigurationRepository().findWidgetConfigurations(params);
        logDebug(method, "# of widget configurations: %s", existingWidgetConfigurations.size());

        List<CopyBoardModels.CopyOfWidgetConfiguration> clonedWidgetConfigurations = new ArrayList<>();
        for (WidgetConfiguration widgetConfiguration : existingWidgetConfigurations)
            clonedWidgetConfigurations.add(copyWidgetConfiguration(widgetConfiguration));

        // build copy
        logDebug(method, "Building widget copy...");
        CopyBoardModels.CopyOfWidget copy = new CopyBoardModels.CopyOfWidget().withWidget(clone);

        // widget configurations
        if (!clonedWidgetConfigurations.isEmpty())
            copy.withWidgetConfigurations(clonedWidgetConfigurations);

        return copy;
    }

    public CopyBoardModels.CopyOfWidgetConfiguration copyWidgetConfiguration(String widgetConfigurationId) {
        Assert.hasText(widgetConfigurationId, "widgetConfigurationId is empty");

        String method = "copyWidgetConfiguration";
        logDebug(method, "...");

        WidgetConfiguration existing = widgetConfigurationService.getWidgetConfigurationRepository()
                .findById(widgetConfigurationId).orElse(null);
        Assert.notNull(existing, "WidgetConfiguration not found! widgetConfigurationId:" + widgetConfigurationId);

        return copyWidgetConfiguration(existing);
    }

    public CopyBoardModels.CopyOfWidgetConfiguration copyWidgetConfiguration(WidgetConfiguration existing) {
        Assert.notNull(existing, "widgetConfiguration is null");

        String method = "copyWidgetConfiguration";
        logDebug(method, "...");

        WidgetConfiguration clone = new WidgetConfiguration();
        clone.setChangedPage(existing.getChangedPage());
        clone.setClosed(existing.isClosed());
        clone.setCurrentPage(existing.getCurrentPage());
        clone.setError(existing.getError());
        clone.setName(existing.getName());
        clone.setLabel(existing.getLabel());
        clone.setWidgetId(null);

        // configuration pages
        ConfigurationPageSearchParams params = new ConfigurationPageSearchParams();
        params.addWidgetConfigurationIds(existing.getId());
        List<ConfigurationPage> existingConfigurationPages = configurationPageService.getConfigurationPageRepository()
                .findConfigurationPages(params);
        logDebug(method, "# of configuration pages: %s", existingConfigurationPages.size());

        List<CopyBoardModels.CopyOfConfigurationPage> clonedConfigurationPages = new ArrayList<>();
        for (ConfigurationPage configurationPage : existingConfigurationPages)
            clonedConfigurationPages.add(copyConfigurationPage(configurationPage));

        // build copy
        logDebug(method, "Building widget configuration copy...");
        CopyBoardModels.CopyOfWidgetConfiguration copy = new CopyBoardModels.CopyOfWidgetConfiguration()
                .withWidgetConfiguration(clone);

        // configuration pages
        if (!clonedConfigurationPages.isEmpty())
            copy.withConfigurationPages(clonedConfigurationPages);

        return copy;
    }

    public CopyBoardModels.CopyOfConfigurationPage copyConfigurationPage(String configurationPageId) {
        Assert.hasText(configurationPageId, "configurationPageId is empty");

        String method = "copyConfigurationPage";
        logDebug(method, "...");

        ConfigurationPage existing = configurationPageService.getConfigurationPageRepository()
                .findById(configurationPageId).orElse(null);
        Assert.notNull(existing, "ConfigurationPage not found! configurationPageId:" + configurationPageId);

        return copyConfigurationPage(existing);
    }

    public CopyBoardModels.CopyOfConfigurationPage copyConfigurationPage(ConfigurationPage existing) {
        Assert.notNull(existing, "configurationPage is null");

        String method = "copyConfigurationPage";
        logDebug(method, "...");

        ConfigurationPage clone = new ConfigurationPage();
        clone.setError(existing.getError());
        clone.setName(existing.getName());
        clone.setLabel(existing.getLabel());
        clone.setPageNumber(existing.getPageNumber());
        clone.setWidgetConfigurationId(null);

        // page properties
        PagePropertySearchParams params = new PagePropertySearchParams();
        params.addConfigurationPageIds(existing.getId());
        List<PageProperty> existingPageProperties = pagePropertyService.getPagePropertyRepository()
                .findPageProperties(params);
        logDebug(method, "# of page properties: %s", existingPageProperties.size());

        List<CopyBoardModels.CopyOfPageProperty> clonedPageProperties = new ArrayList<>();
        for (PageProperty pageProperty : existingPageProperties)
            clonedPageProperties.add(copyPageProperty(pageProperty));

        // build copy
        logDebug(method, "Building configuration page copy...");
        CopyBoardModels.CopyOfConfigurationPage copy = new CopyBoardModels.CopyOfConfigurationPage()
                .withConfigurationPage(clone);

        // page properties
        if (!clonedPageProperties.isEmpty())
            copy.withPageProperties(clonedPageProperties);

        return copy;
    }

    public CopyBoardModels.CopyOfPageProperty copyPageProperty(String pagePropertyId) {
        Assert.hasText(pagePropertyId, "pagePropertyId is empty");

        String method = "copyPageProperty";
        logDebug(method, "...");

        PageProperty existing = pagePropertyService.getPagePropertyRepository().findById(pagePropertyId).orElse(null);
        Assert.notNull(existing, "PageProperty not found! pagePropertyId:" + pagePropertyId);

        return copyPageProperty(existing);
    }

    public CopyBoardModels.CopyOfPageProperty copyPageProperty(PageProperty existing) {
        Assert.notNull(existing, "pageProperty is null");

        String method = "copyPageProperty";
        logDebug(method, "...");

        PageProperty clone = new PageProperty();
        clone.setActive(existing.isActive());
        clone.setConfigurationPageId(null);
        clone.setDescription(existing.getDescription());
        clone.setError(existing.getError());
        clone.setName(existing.getName());
        clone.setLabel(existing.getLabel());
        clone.setPropertyNumber(existing.getPropertyNumber());
        clone.setRequired(existing.isRequired());

        // property value
        PropertyValueSearchParams params = new PropertyValueSearchParams();
        params.addPagePropertyIds(existing.getId());
        List<PropertyValue> existingPropertyValues = propertyValueService.getPropertyValueRepository()
                .findPropertyValues(params);
        logDebug(method, "# of property values: %s", existingPropertyValues.size());

        List<CopyBoardModels.CopyOfPropertyValue> clonedPropertyValues = new ArrayList<>();
        for (PropertyValue propertyValue : existingPropertyValues)
            clonedPropertyValues.add(copyPropertyValue(propertyValue));

        // build copy
        logDebug(method, "Building page property copy...");
        CopyBoardModels.CopyOfPageProperty copy = new CopyBoardModels.CopyOfPageProperty().withPageProperty(clone);

        // property value
        if (!clonedPropertyValues.isEmpty())
            copy.withPropertyValues(clonedPropertyValues);

        return copy;
    }

    public CopyBoardModels.CopyOfPropertyValue copyPropertyValue(String propertyValueId) {
        Assert.hasText(propertyValueId, "pagePropertyId is empty");

        String method = "copyPropertyValue";
        logDebug(method, "...");

        PropertyValue existing = propertyValueService.getPropertyValueRepository().findById(propertyValueId)
                .orElse(null);
        Assert.notNull(existing, "PropertyValue not found! propertyValueId:" + propertyValueId);

        return copyPropertyValue(existing);
    }

    public CopyBoardModels.CopyOfPropertyValue copyPropertyValue(PropertyValue existing) {
        Assert.notNull(existing, "propertyValue is null");

        String method = "copyPropertyValue";
        logDebug(method, "...");

        PropertyValue clone = new PropertyValue();
        clone.setPagePropertyId(null);
        clone.setType(existing.getType());
        clone.setValue(existing.getValue());
        clone.setVersion(existing.getVersion());

        // build copy
        logDebug(method, "Building property value copy...");
        CopyBoardModels.CopyOfPropertyValue copy = new CopyBoardModels.CopyOfPropertyValue().withPropertyValue(clone);

        return copy;
    }
}

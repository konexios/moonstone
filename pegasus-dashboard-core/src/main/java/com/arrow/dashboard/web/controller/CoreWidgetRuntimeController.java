package com.arrow.dashboard.web.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.arrow.dashboard.DashboardConstants;
import com.arrow.dashboard.DashboardEntityAbstract;
import com.arrow.dashboard.messaging.topic.TopicProviderMessenger;
import com.arrow.dashboard.runtime.BoardRuntimeManager;
import com.arrow.dashboard.runtime.RouterRuntimeManager;
import com.arrow.dashboard.runtime.WidgetRuntimeDefinitionManager;
import com.arrow.dashboard.runtime.WidgetRuntimeManager;
import com.arrow.dashboard.runtime.model.GridsterItemOptions;
import com.arrow.dashboard.runtime.model.UserRuntimeInstance;
import com.arrow.dashboard.runtime.model.WidgetRuntimeInstance;
import com.arrow.dashboard.runtime.model.WidgetStateChangeResponse;
import com.arrow.dashboard.web.UrlUtils;
import com.arrow.dashboard.web.model.runtime.ConvertRuntimeModelUtils;
import com.arrow.dashboard.web.model.runtime.WidgetRuntimeModels;
import com.arrow.dashboard.web.model.runtime.WidgetRuntimeModels.CancelNewWidgetRequest;
import com.arrow.dashboard.web.model.runtime.WidgetRuntimeModels.WidgetMetaData;
import com.arrow.dashboard.widget.annotation.messaging.MessageEndpoint;
import com.arrow.dashboard.widget.configuration.Configuration;
import com.arrow.dashboard.widget.configuration.Page;
import com.arrow.pegasus.dashboard.data.Board;
import com.arrow.pegasus.dashboard.data.Widget;
import com.arrow.pegasus.dashboard.data.WidgetParentTypes;
import com.arrow.pegasus.dashboard.service.BoardService;
import com.arrow.pegasus.dashboard.service.WidgetService;

import moonstone.acs.JsonUtils;

@Controller
public class CoreWidgetRuntimeController extends WebSocketController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private WidgetService widgetService;
    @Autowired
    private WidgetRuntimeDefinitionManager widgetRuntimeDefinitionManager;
    @Autowired
    private BoardRuntimeManager boardRuntimeManager;
    @Autowired
    private WidgetRuntimeManager widgetRuntimeManager;
    @Autowired
    private RouterRuntimeManager routerRuntimeManager;
    @Autowired
    private SimpMessagingTemplate messageTemplate;

    private boolean active = false;

    /**
     * Method to activate widget controller
     */
    @PostConstruct
    public void activate() {
        String method = "activate";

        synchronized (this) {
            logDebug(method, "Widget controller got request for activation");
            if (active) {
                logDebug(method, "Widget controller is already activated");
            } else {
                logInfo(method, "Widget controller start activation");

                routerRuntimeManager.setTopicProviderMessenger(new TopicMessageSender(messageTemplate));

                active = true;
                logInfo(method, "Widget controller activated");
            }
        }
    }

    @MessageMapping(DashboardConstants.Url.CHANGE_WIDGET_STATE_TO_CREATE)
    public ResponseEntity<WidgetStateChangeResponse> notifyCreate(@DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        return new ResponseEntity<WidgetStateChangeResponse>(widgetRuntimeManager.notifyOnCreate(widgetRuntimeId),
                HttpStatus.OK);
    }

    @MessageMapping(DashboardConstants.Url.CHANGE_WIDGET_STATE_TO_LOADING)
    public ResponseEntity<WidgetStateChangeResponse> notifyLoading(@DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        return new ResponseEntity<WidgetStateChangeResponse>(widgetRuntimeManager.notifyOnLoading(widgetRuntimeId),
                HttpStatus.OK);
    }

    @MessageMapping(DashboardConstants.Url.CHANGE_WIDGET_STATE_TO_READY)
    public ResponseEntity<WidgetStateChangeResponse> notifyReady(@DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        return new ResponseEntity<WidgetStateChangeResponse>(widgetRuntimeManager.notifyOnReady(widgetRuntimeId),
                HttpStatus.OK);
    }

    @MessageMapping(DashboardConstants.Url.CHANGE_WIDGET_STATE_TO_RUNNING)
    public ResponseEntity<WidgetStateChangeResponse> notifyRunning(@DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        return new ResponseEntity<WidgetStateChangeResponse>(widgetRuntimeManager.notifyOnRunning(widgetRuntimeId),
                HttpStatus.OK);
    }

    @MessageMapping(DashboardConstants.Url.CHANGE_WIDGET_STATE_TO_STOPPED)
    public ResponseEntity<WidgetStateChangeResponse> notifyStopped(@DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        return new ResponseEntity<WidgetStateChangeResponse>(widgetRuntimeManager.notifyOnStopped(widgetRuntimeId),
                HttpStatus.OK);
    }

    @MessageMapping(DashboardConstants.Url.CHANGE_WIDGET_STATE_TO_ERROR)
    public ResponseEntity<WidgetStateChangeResponse> notifyError(@DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        return new ResponseEntity<WidgetStateChangeResponse>(widgetRuntimeManager.notifyOnError(widgetRuntimeId),
                HttpStatus.OK);
    }

    @MessageMapping(DashboardConstants.Url.CHANGE_WIDGET_STATE_TO_CORRECTION)
    public ResponseEntity<WidgetStateChangeResponse> notifyCorrection(@DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        return new ResponseEntity<WidgetStateChangeResponse>(widgetRuntimeManager.notifyOnCorrection(widgetRuntimeId),
                HttpStatus.OK);
    }

    @MessageMapping(DashboardConstants.Url.CHANGE_WIDGET_STATE_TO_DESTROYED)
    public ResponseEntity<WidgetStateChangeResponse> notifyDestroyed(@DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        return new ResponseEntity<WidgetStateChangeResponse>(widgetRuntimeManager.notifyOnDestroyed(widgetRuntimeId),
                HttpStatus.OK);
    }

    @MessageMapping(DashboardConstants.Url.NEW_WIDGET_REQUEST)
    public void newWidget(StompHeaderAccessor headerAccessor, WidgetRuntimeModels.NewWidgetRequest newWidgetRequest) {
        Assert.notNull(newWidgetRequest, "newWidgetRequest is null");
        Assert.hasText(newWidgetRequest.getGeneratedParentRuntimeId(), "generatedParentRuntimeId is empty");
        Assert.hasText(newWidgetRequest.getWidgetTypeId(), "widgetTypeId is empty");

        String method = "newWidgetRequest";
        logInfo(method, "...");
        logDebug(method, "generatedParentRuntimeId: %s, widgetTypeId: %s",
                newWidgetRequest.getGeneratedParentRuntimeId(), newWidgetRequest.getWidgetTypeId());

        String generatedParentRuntimeId = newWidgetRequest.getGeneratedParentRuntimeId();

        UserRuntimeInstance userRuntime = getUserRuntimeInstance(headerAccessor);

        com.arrow.dashboard.runtime.model.WidgetRuntimeDefinition widgetRuntimeDefinition = widgetRuntimeDefinitionManager
                .getWidgetRuntimeDefinitionById(newWidgetRequest.getWidgetTypeId());
        Assert.notNull(widgetRuntimeDefinition,
                "WidgetRuntimeDefinition not found! widgetTypeId=" + newWidgetRequest.getWidgetTypeId());

        // create and persist widget
        Widget widget = new Widget();
        widget.setName(widgetRuntimeDefinition.getWidgetName());
        widget.setDescription(widgetRuntimeDefinition.getWidgetDescription());
        widget.setParentId(generatedParentRuntimeId);
        // TODO revisit, need to support WidgetParentTypes.BoardContainer
        widget.setParentType(WidgetParentTypes.Board);
        widget.setWidgetTypeId(widgetRuntimeDefinition.getId());

        // layout
        GridsterItemOptions options = new GridsterItemOptions();
        widget.setLayoutClass(options.getClass().getName());
        widget.setLayout(JsonUtils.toJson(options));

        // persist widget
        widget = widgetService.create(widget, getUserId(headerAccessor));

        WidgetRuntimeInstance widgetRuntimeInstance = widgetRuntimeManager.registerWidget(generatedParentRuntimeId,
                widget, userRuntime);
        Assert.notNull(widgetRuntimeInstance, "widgetRuntimeInstance is null");

        // notify widget of create state
        widgetRuntimeManager.notifyOnCreate(widgetRuntimeInstance);

        logDebug(method, "Adding new widget to board. generatedParentRuntimeId: %s", generatedParentRuntimeId);

        String topicURL = UrlUtils.withBoardRuntimeId(DashboardConstants.Topic.BOARD_ADD_WIDGET,
                generatedParentRuntimeId);
        logDebug(method, "topic: %s", topicURL);
        messageTemplate.convertAndSend(topicURL,
                ConvertRuntimeModelUtils.toWidgetRuntimeModel(generatedParentRuntimeId, widgetRuntimeInstance));
    }

    @MessageMapping(DashboardConstants.Url.CANCEL_NEW_WIDGET_REQUEST)
    public void cancelNewWidget(StompHeaderAccessor headerAccessor, CancelNewWidgetRequest cancelNewWidgetRequest) {
        Assert.notNull(cancelNewWidgetRequest, "cancelNewWidgetRequest is null");
        Assert.hasText(cancelNewWidgetRequest.getWidgetRuntimeId(), "widgetRuntimeId is empty");

        String method = "cancelNewWidget";
        logInfo(method, "...");
        logDebug(method, "widgetRuntimeId: %s", cancelNewWidgetRequest.getWidgetRuntimeId());

        // lookup existing widget runtime instance
        WidgetRuntimeInstance existingWidgetRuntimeInstance = widgetRuntimeManager
                .getWidgetRuntimeInstance(cancelNewWidgetRequest.getWidgetRuntimeId());
        Assert.notNull(existingWidgetRuntimeInstance,
                "WidgetRuntimeInstance not found! widgetRuntimeId=" + cancelNewWidgetRequest.getWidgetRuntimeId());

        // unregister
        WidgetRuntimeInstance unregisteredWidgetRuntimeInstance = widgetRuntimeManager
                .unregisterWidget(existingWidgetRuntimeInstance.getWidgetRuntimeId());
        Assert.notNull(existingWidgetRuntimeInstance, "WidgetRuntimeInstance not found! widgetRuntimeId="
                + existingWidgetRuntimeInstance.getWidgetRuntimeId());

        // notify widget of destroy state
        widgetRuntimeManager.notifyOnDestroyed(unregisteredWidgetRuntimeInstance);

        // delete widget
        widgetService.delete(unregisteredWidgetRuntimeInstance.getWidgetId());

        messageTemplate.convertAndSend(
                UrlUtils.withBoardRuntimeId(DashboardConstants.Topic.BOARD_REMOVE_WIDGET,
                        unregisteredWidgetRuntimeInstance.getParentRuntimeId()),
                unregisteredWidgetRuntimeInstance.getWidgetRuntimeId());
    }

    /**
     * Method to register new widget to it's new parent (move it from
     * currentParent to newParent)
     * 
     * @param registerWidgetToParentRequest
     */
    @MessageMapping(DashboardConstants.Url.REGISTER_WIDGET_TO_PARENT_REQUEST)
    public void registerWidgetToParent(StompHeaderAccessor headerAccessor,
            WidgetRuntimeModels.RegisterWidgetToParentRequest registerWidgetToParentRequest) {
        Assert.notNull(registerWidgetToParentRequest, "registerWidgetToParentRequest is null");
        Assert.hasText(registerWidgetToParentRequest.getParentRuntimeId(), "parentRuntimeId is empty");
        Assert.hasText(registerWidgetToParentRequest.getParentId(), "parentId is empty");
        Assert.hasText(registerWidgetToParentRequest.getWidgetRuntimeId(), "widgetRuntimeId is empty");

        // TODO future enhancement, support container

        String method = "registerWidgetToParent";
        logInfo(method, "...");
        logDebug(method, "parentRuntimeId: %s, parentId: %s, widgetRuntimeId: %s",
                registerWidgetToParentRequest.getParentRuntimeId(), registerWidgetToParentRequest.getParentId(),
                registerWidgetToParentRequest.getWidgetRuntimeId());

        WidgetRuntimeInstance existingWidgetRuntimeInstance = widgetRuntimeManager
                .getWidgetRuntimeInstance(registerWidgetToParentRequest.getWidgetRuntimeId());
        Assert.notNull(existingWidgetRuntimeInstance, "WidgetRuntimeInstance not found! widgetRuntimeId="
                + registerWidgetToParentRequest.getWidgetRuntimeId());

        // update meta data
        WidgetMetaData widgetMetaData = updateWidgetMetaData(existingWidgetRuntimeInstance.getWidgetId(),
                registerWidgetToParentRequest.getWidgetMetaData(), getUserId(headerAccessor));

        // update runtime instance
        existingWidgetRuntimeInstance.withName(widgetMetaData.getName());
        existingWidgetRuntimeInstance.withDescription(widgetMetaData.getDescription());
        // layout
        GridsterItemOptions options = populateGridsterItemOptions(null, widgetMetaData);
        existingWidgetRuntimeInstance.withLayout(options);

        widgetRuntimeManager.updateWidgetRuntimeInstance(existingWidgetRuntimeInstance.getWidgetRuntimeId(),
                existingWidgetRuntimeInstance);

        // process configuration update
        routerRuntimeManager.routeConfigurationSaveRequest(existingWidgetRuntimeInstance.getWidgetRuntimeId(),
                registerWidgetToParentRequest.getConfiguration(), getUserId(headerAccessor));

        // set parent id for widget to target board
        changeWidgetParent(existingWidgetRuntimeInstance.getWidgetId(), registerWidgetToParentRequest.getParentId(),
                getUserId(headerAccessor));

        WidgetRuntimeInstance registeredWidgetRuntimeInstance = boardRuntimeManager.registerWidgetToBoard(
                registerWidgetToParentRequest.getParentRuntimeId(), existingWidgetRuntimeInstance,
                getUserRuntimeInstance(headerAccessor));

        // stop original widget, already unregistered. this widget runtime is
        // not needed any more
        widgetRuntimeManager.notifyOnDestroyed(existingWidgetRuntimeInstance);

        WidgetRuntimeModels.WidgetRuntimeModel widgetRuntimeModel = ConvertRuntimeModelUtils.toWidgetRuntimeModel(
                registerWidgetToParentRequest.getParentRuntimeId(), registeredWidgetRuntimeInstance);

        String addWidgetToBoardTopic = UrlUtils.withBoardRuntimeId(DashboardConstants.Topic.BOARD_ADD_WIDGET,
                registerWidgetToParentRequest.getParentRuntimeId());
        logDebug(method, "Adding widgets to board. topic: %s", addWidgetToBoardTopic);

        // send message to board
        messageTemplate.convertAndSend(addWidgetToBoardTopic, widgetRuntimeModel);

        // notify widget of loading state
        WidgetStateChangeResponse notifyOnLoadingResult = widgetRuntimeManager
                .notifyOnLoading(registeredWidgetRuntimeInstance);
        if (notifyOnLoadingResult.isNotified()) {
            // notify widget of ready state
            WidgetStateChangeResponse notifyOnReadyResult = widgetRuntimeManager
                    .notifyOnReady(registeredWidgetRuntimeInstance);
            if (!notifyOnReadyResult.isNotified()) {
                logError(method, "NotifyOnReady failed!", notifyOnReadyResult.getError());
            }
        } else {
            logError(method, "NotifyOnLoading failed!", notifyOnLoadingResult.getError());
        }
    }

    private void changeWidgetParent(String widgetId, String parentId, String who) {
        Assert.hasText(widgetId, "widgetId is empty");
        Assert.hasText(parentId, "parentId is empty");
        Assert.hasText(who, "who is empty");

        String method = "changeParent";
        logInfo(method, "...");
        logDebug(method, "parentId: %s, widgetId: %s", parentId, widgetId);

        Board board = boardService.getBoardRepository().findById(parentId).orElse(null);
        Assert.notNull(board, "Board not found! boardId=" + parentId);

        // TODO add support for containers, board could be null in that case

        Widget widget = widgetService.getWidgetRepository().findById(widgetId).orElse(null);
        Assert.notNull(widget, "Widget not found! widgetId=" + widgetId);

        // change parent
        widget.setParentId(board.getId());
        widgetService.update(widget, who);
    }

    @MessageMapping(DashboardConstants.Url.EDIT_WIDGET)
    public void editWidget(StompHeaderAccessor headerAccessor, @DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        String method = "editWidget";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s, widgetRuntimeId: %s", boardRuntimeId, widgetRuntimeId);

        // notify widget of stopped state
        widgetRuntimeManager.notifyOnStopped(widgetRuntimeId);

        // send configuration to widget
        routerRuntimeManager.routeConfigurationRequest(widgetRuntimeId);
    }

    @MessageMapping(DashboardConstants.Url.UPDATE_WIDGET)
    public void updateWidget(StompHeaderAccessor headerAccessor, @DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId, WidgetRuntimeModels.UpdateWidgetRequest updateWidgetRequest) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");
        Assert.notNull(updateWidgetRequest, "widgetUpdateRequest is null");
        Assert.notNull(updateWidgetRequest.getWidgetMetaData(), "widgetMetaData is null");
        Assert.notNull(updateWidgetRequest.getConfiguration(), "configuration is null");

        String method = "updateWidget";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s, widgetRuntimeId: %s", boardRuntimeId, widgetRuntimeId);

        WidgetRuntimeInstance widgetRuntimeInstance = widgetRuntimeManager.getWidgetRuntimeInstance(widgetRuntimeId);
        Assert.notNull(widgetRuntimeInstance, "WidgetRuntimeInstance not found! widgetRuntimeId=" + widgetRuntimeId);

        // update meta data
        WidgetMetaData widgetMetaData = updateWidgetMetaData(widgetRuntimeInstance.getWidgetId(),
                updateWidgetRequest.getWidgetMetaData(), getUserId(headerAccessor));

        // update runtime instance
        widgetRuntimeInstance.withName(widgetMetaData.getName());
        widgetRuntimeInstance.withDescription(widgetMetaData.getDescription());
        // layout
        GridsterItemOptions options = populateGridsterItemOptions(null, widgetMetaData);
        widgetRuntimeInstance.withLayout(options);

        widgetRuntimeManager.updateWidgetRuntimeInstance(widgetRuntimeId, widgetRuntimeInstance);

        // process configuration update
        routerRuntimeManager.routeConfigurationSaveRequest(widgetRuntimeId, updateWidgetRequest.getConfiguration(),
                getUserId(headerAccessor));

        // send message to widget
        String widgetTopic = UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.MESSAGE_BROKER,
                DashboardConstants.Topic.UPDATE_WIDGET_META_DATA, boardRuntimeId, widgetRuntimeId);
        logDebug(method, "topic: %s, widgetRuntimeId: %s", widgetTopic, widgetRuntimeId);
        messageTemplate.convertAndSend(widgetTopic, widgetMetaData);

        // notify widget of ready state
        widgetRuntimeManager.notifyOnReady(widgetRuntimeId);

        WidgetRuntimeModels.WidgetRuntimeModel widgetRuntimeModel = ConvertRuntimeModelUtils
                .toWidgetRuntimeModel(boardRuntimeId, widgetRuntimeInstance);

        // send message to board
        String boardTopic = UrlUtils.withBoardRuntimeId(DashboardConstants.Topic.BOARD_UPDATE_WIDGET, boardRuntimeId);
        logDebug(method, "topic: %s, boardRuntimeId: %s", boardTopic, boardRuntimeId);
        messageTemplate.convertAndSend(boardTopic, widgetRuntimeModel);
    }

    @MessageMapping(DashboardConstants.Url.CANCEL_EDIT_WIDGET)
    public void cancelEditWidget(StompHeaderAccessor headerAccessor, @DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        String method = "cancelEditWidget";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s, widgetRuntimeId: %s", boardRuntimeId, widgetRuntimeId);

        // notify widget of stopped state
        widgetRuntimeManager.notifyOnRunning(widgetRuntimeId);
    }

    /**
     * Method to handle incoming message with parameter for the widget instance
     * 
     * @param boardRuntimeId
     *            dashboard instance id
     * @param widgetRuntimeId
     *            widget instance id
     * @param endpoint
     *            message endpoint name. Refer to
     *            {@link MessageEndpoint#value()}
     * @param parameter
     *            string parameter
     */
    @MessageMapping(DashboardConstants.Url.PROCESS_WIDGET_MESSAGE_WITH_PARAMETERS)
    public void handleIncomingMessageWithParameters(@DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId, @DestinationVariable String endpoint, String parameter) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");
        Assert.hasText(endpoint, "endpoint is empty");
        Assert.hasText(parameter, "parameter is empty");

        String method = "handleIncomingMessageWithParameters";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s, widgetRuntimeId: %s, endpoint: %s, parameter: %s", boardRuntimeId,
                widgetRuntimeId, endpoint, parameter);

        routerRuntimeManager.route(widgetRuntimeId, endpoint, parameter);
    }

    /**
     * Method to handle incoming message with no parameters for the widget
     * instance
     * 
     * @param dashboardId
     *            dashboard instance id
     * @param widgetRuntimeId
     *            widget instance id
     * @param endpoint
     *            message endpoint name. Refer to
     *            {@link MessageEndpoint#value()}
     */
    @MessageMapping(DashboardConstants.Url.PROCESS_WIDGET_MESSAGE_NO_PARAMETERS)
    public void handleIncomingMessageWithNoParameters(@DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId, @DestinationVariable String endpoint) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");
        Assert.hasText(endpoint, "endpoint is empty");

        String method = "handleIncomingMessageWithNoParameters";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s, widgetRuntimeId: %s, endpoint: %s", boardRuntimeId, widgetRuntimeId,
                endpoint);

        routerRuntimeManager.route(widgetRuntimeId, endpoint, null);
    }

    /**
     * Method to handle request for initial configuration of the widget
     * 
     * @param boardRuntimeId
     *            dashboard instance id
     * @param widgetRuntimeId
     *            widget instance id
     */
    @MessageMapping(DashboardConstants.Url.INITIALIZE_WIDGET_CONFIGURATION)
    public void handleIncomingRequestForConfguration(@DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        String method = "handleIncomingRequestForConfguration";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s, widgetRuntimeId: %s", boardRuntimeId, widgetRuntimeId);

        // send configuration to widget
        routerRuntimeManager.routeConfigurationRequest(widgetRuntimeId);
    }

    /**
     * Method to handle configuration processing for the widget
     * 
     * @param dashboardId
     *            dashboard instance id
     * @param widgetRuntimeId
     *            widget instance id
     * @param configuration
     *            {@link Configuration} configuration, modified by the user
     */
    @MessageMapping(DashboardConstants.Url.PROCESS_INBOUND_WIDGET_CONFIGURATION)
    public void handleConfigurationProcessingRequest(@DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId, Configuration configuration) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");
        Assert.notNull(configuration, "configuration is null");

        String method = "handleConfigurationProcessingRequest";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s, widgetRuntimeId: %s", boardRuntimeId, widgetRuntimeId);

        routerRuntimeManager.routeConfigurationPageRequest(widgetRuntimeId, configuration);
    }

    @MessageMapping(DashboardConstants.Url.SAVE_INBOUND_WIDGET_CONFIGURATION)
    public void handleConfigurationSavingRequest(StompHeaderAccessor headerAccessor,
            @DestinationVariable String boardRuntimeId, @DestinationVariable String widgetRuntimeId,
            Configuration configuration) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");
        Assert.notNull(configuration, "configuration is null");

        String method = "handleConfigurationSavingRequest";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s, widgetRuntimeId: %s", boardRuntimeId, widgetRuntimeId);

        routerRuntimeManager.routeConfigurationSaveRequest(widgetRuntimeId, configuration, getUserId(headerAccessor));

        // TODO revisit
        // notify widget of ready state
        // widgetRuntimeManager.notifyOnReady(widgetRuntimeId);
    }

    /**
     * Method to handle request for initial fast configuration page of the
     * widget
     * 
     * @param dashboardId
     *            dashboard instance id
     * @param widgetRuntimeId
     *            widget instance id
     */
    @MessageMapping(DashboardConstants.Url.INITIALIZE_WIDGET_FAST_CONFIGURATION)
    public void handleIncomingRequestForFastConfguration(@DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        String method = "handleIncomingRequestForFastConfguration";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s, widgetRuntimeId: %s", boardRuntimeId, widgetRuntimeId);

        routerRuntimeManager.routeFastConfigurationRequest(widgetRuntimeId);
    }

    /**
     * Method to handle fast configuration processing for the widget
     * 
     * @param dashboardId
     *            dashboard instance id
     * @param widgetRuntimeId
     *            widget instance id
     * @param page
     *            {@link Page} page, modified by the user
     */
    @MessageMapping(DashboardConstants.Url.PROCESS_INBOUND_WIDGET_FAST_CONFIGURATION)
    public void handleFastConfigurationProcessingRequest(@DestinationVariable String boardRuntimeId,
            @DestinationVariable String widgetRuntimeId, Page page) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");
        Assert.notNull(page, "page is null");

        String method = "handleFastConfigurationProcessingRequest";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s, widgetRuntimeId: %s, page: %s", boardRuntimeId, widgetRuntimeId,
                page.getName());

        routerRuntimeManager.routeFastConfigurationPageRequest(widgetRuntimeId, page);
    }

    // why does this method use topic????
    @MessageMapping(DashboardConstants.Topic.UPDATE_WIDGET_META_DATA)
    public void handleMetaDataUpdateRequest(StompHeaderAccessor headerAccessor,
            @DestinationVariable String boardRuntimeId, @DestinationVariable String widgetRuntimeId,
            WidgetRuntimeModels.WidgetMetaData widgetMetaData) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.hasText(widgetRuntimeId, "widgetRuntimeId is empty");

        String method = "handleMetaDataUpdateRequest";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s, widgetRuntimeId: %s", boardRuntimeId, widgetRuntimeId);

        WidgetRuntimeInstance widgetRuntimeInstance = widgetRuntimeManager.getWidgetRuntimeInstance(widgetRuntimeId);
        Assert.notNull(widgetRuntimeInstance, "WidgetRuntimeInstance not found! widgetRuntimeId=" + widgetRuntimeId);

        if (widgetMetaData != null) {
            // need to update meta data and notify FE views
            widgetMetaData = updateWidgetMetaData(widgetRuntimeInstance.getWidgetId(), widgetMetaData,
                    getUserId(headerAccessor));
        } else {
            // is it just 'ping'. send meta data to FE views without database
            // update
            widgetMetaData = getWidgetMetaData(widgetRuntimeInstance.getWidgetId());
        }

        String topic = UrlUtils.fromControllerUrlToWidgetRuntimeUrl(DashboardConstants.MESSAGE_BROKER,
                DashboardConstants.Topic.UPDATE_WIDGET_META_DATA, boardRuntimeId, widgetRuntimeId);
        logDebug(method, "topic: %s, widgetRuntimeId: %s", topic, widgetRuntimeId);

        messageTemplate.convertAndSend(topic, widgetMetaData);
    }

    private WidgetRuntimeModels.WidgetMetaData getWidgetMetaData(String widgetId) {
        Assert.hasText(widgetId, "widgetId is empty");

        Widget widget = widgetService.getWidgetRepository().findById(widgetId).orElse(null);
        Assert.notNull(widget, "Widget not found! widgetId=" + widgetId);

        return new WidgetRuntimeModels.WidgetMetaData().withName(widget.getName())
                .withDescription(widget.getDescription());
    }

    private WidgetRuntimeModels.WidgetMetaData updateWidgetMetaData(String widgetId,
            WidgetRuntimeModels.WidgetMetaData widgetMetaData, String who) {
        Assert.hasText(widgetId, "widgetId is empty");
        Assert.notNull(widgetMetaData, "widgetMetaData is null");

        Widget widget = widgetService.getWidgetRepository().findById(widgetId).orElse(null);
        Assert.notNull(widget, "Widget not found! widgetId=" + widgetId);

        widget.setName(widgetMetaData.getName());
        widget.setDescription(widgetMetaData.getDescription());

        // layout
        GridsterItemOptions options = populateGridsterItemOptions(widget, widgetMetaData);
        widget.setLayout(JsonUtils.toJson(options));
        widget.setLayoutClass(options.getClass().getName());

        widget = widgetService.update(widget, who);

        return widgetMetaData;
    }

    private GridsterItemOptions populateGridsterItemOptions(Widget widget, WidgetMetaData widgetMetaData) {
        GridsterItemOptions options = new GridsterItemOptions();

        if (widget != null) {
            Object layoutValue = widget.getLayoutValue();
            if (layoutValue != null)
                options = (GridsterItemOptions) layoutValue;
        }

        options.setSizeX(widgetMetaData.getWidth());
        options.setSizeY(widgetMetaData.getHeight());
        options.setRow(widgetMetaData.getPositionY());
        options.setCol(widgetMetaData.getPositionX());

        return options;
    }

    /**
     * Internal implementation of {@link TopicProviderMessenger}
     * 
     * @author dantonov
     *
     */
    private static class TopicMessageSender extends DashboardEntityAbstract implements TopicProviderMessenger {

        private SimpMessagingTemplate template;

        public TopicMessageSender(SimpMessagingTemplate template) {
            super();
            this.template = template;
        }

        @Override
        public void send(String topic, Object object) {

            String method = "send";
            logInfo(method, "...");
            logTrace(method, "Widget controller sends a message to topic: " + topic);
            template.convertAndSend(topic, object);
        }
    }
}
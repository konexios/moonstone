package com.arrow.dashboard.web.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

import com.arrow.acs.JsonUtils;
import com.arrow.dashboard.DashboardConstants;
import com.arrow.dashboard.runtime.BoardRuntimeManager;
import com.arrow.dashboard.runtime.WidgetRuntimeManager;
import com.arrow.dashboard.runtime.model.BoardRuntimeInstance;
import com.arrow.dashboard.runtime.model.GridsterItemOptions;
import com.arrow.dashboard.runtime.model.UserRuntimeInstance;
import com.arrow.dashboard.runtime.model.WidgetRuntimeConfigurationPatch;
import com.arrow.dashboard.runtime.model.WidgetRuntimeInstance;
import com.arrow.dashboard.runtime.model.WidgetStateChangeResponse;
import com.arrow.dashboard.web.UrlUtils;
import com.arrow.dashboard.web.model.runtime.BoardRuntimeModels;
import com.arrow.dashboard.web.model.runtime.BoardRuntimeModels.CloseBoardRequest;
import com.arrow.dashboard.web.model.runtime.BoardRuntimeModels.OpenBoardRequest;
import com.arrow.dashboard.web.model.runtime.BoardRuntimeModels.RemoveWidgetRequest;
import com.arrow.dashboard.web.model.runtime.BoardRuntimeModels.UnregisterBoardRequest;
import com.arrow.dashboard.web.model.runtime.ConvertRuntimeModelUtils;
import com.arrow.dashboard.web.model.runtime.WidgetRuntimeModels;
import com.arrow.pegasus.dashboard.data.Board;
import com.arrow.pegasus.dashboard.data.Widget;
import com.arrow.pegasus.dashboard.service.BoardService;
import com.arrow.pegasus.dashboard.service.WidgetService;

@Controller
public class CoreBoardRuntimeController extends WebSocketController {

    @Autowired
    private WebSocketMessageBrokerStats webSocketMessageBrokerStats;
    @Autowired
    private BoardService boardService;
    @Autowired
    private WidgetService widgetService;
    @Autowired
    private BoardRuntimeManager boardRuntimeManager;
    @Autowired
    private WidgetRuntimeManager widgetRuntimeManager;
    @Autowired
    private SimpMessagingTemplate messageTemplate;

    @PostConstruct
    void init() {
        sessionRepository.setRedisFlushMode(RedisFlushMode.IMMEDIATE);
    }

    @RequestMapping("/stats")
    public @ResponseBody WebSocketMessageBrokerStats showStats() {
        // expose as http endpoint or via JMX

        return webSocketMessageBrokerStats;
    }

    @RequestMapping(value = DashboardConstants.Url.REGISTER_BOARD, method = RequestMethod.POST)
    public @ResponseBody BoardRuntimeModels.RegisterBoardResponse registerBoard(
            @RequestBody BoardRuntimeModels.RegisterBoardRequest registerBoardRequest, HttpSession session) {

        long start = System.currentTimeMillis();

        Assert.notNull(registerBoardRequest, "registerBoardRequest is null");
        Assert.hasText(registerBoardRequest.getBoardId(), "boardId is empty");

        String method = "registerBoard";
        logInfo(method, "...");
        logDebug(method, "boardId: %s", registerBoardRequest.getBoardId());

        // String webSocketSessionId = headerAccessor.getSessionId();
        String webSocketSessionId = session.getId();
        Assert.hasText(webSocketSessionId, "webSocketSessionId is empty");
        logDebug(method, "webSocketSessionId: %s", webSocketSessionId);

        BoardRuntimeInstance boardRuntimeInstance = boardRuntimeManager.registerBoard(webSocketSessionId,
                registerBoardRequest.getBoardId());
        logDebug(method, "webSocketSessionId: %s, boardRuntimeId: %s", webSocketSessionId,
                boardRuntimeInstance.getBoardRuntimeId());

        long end = System.currentTimeMillis();
        long duration = end - start;

        logInfo(method, "start: %s, end: %s, duration: %s, size: %s", new Timestamp(start), new Timestamp(end),
                duration, boardRuntimeManager.getNumberOfRuntimeInstances());

        return new BoardRuntimeModels.RegisterBoardResponse()
                .withBoardRuntimeId(boardRuntimeInstance.getBoardRuntimeId())
                .withBoardId(boardRuntimeInstance.getBoardId()).withBoardName(boardRuntimeInstance.getBoardName())
                .withBoardDescription(boardRuntimeInstance.getBoardDescription())
                .withBoardLayout(boardRuntimeInstance.getLayout());
    }

    @MessageMapping(DashboardConstants.Url.OPEN_BOARD)
    public void openBoard(StompHeaderAccessor headerAccessor, OpenBoardRequest openBoardRequest) {

        long start = System.currentTimeMillis();

        Assert.notNull(openBoardRequest, "openBoardRequest is null");
        Assert.hasText(openBoardRequest.getBoardRuntimeId(), "boardRuntimeId is empty");

        String method = "openBoard";
        logInfo(method, "...");
        logDebug(method, "openBoardRequest: %s", openBoardRequest);

        BoardRuntimeInstance boardRuntimeInstance = boardRuntimeManager
                .getBoardRuntimeInstance(openBoardRequest.getBoardRuntimeId());
        Assert.notNull(boardRuntimeInstance,
                "BoardRuntimeInstance not found! boardRuntimeId: " + openBoardRequest.getBoardRuntimeId());

        boardRuntimeManager.putOpeningBoardRuntime(openBoardRequest.getBoardRuntimeId());

        UserRuntimeInstance userRuntime = getUserRuntimeInstance(headerAccessor);

        // lookup widgets
        List<WidgetRuntimeInstance> widgetRuntimeInstances = boardRuntimeManager.openBoard(
                openBoardRequest.getBoardRuntimeId(),
                new WidgetRuntimeConfigurationPatch(openBoardRequest.getConfigurationPatch()), userRuntime);
        logDebug(method, "# of widgets: %s", widgetRuntimeInstances.size());

        if (!widgetRuntimeInstances.isEmpty()) {

            // notify widgets
            String addWidgetToBoardTopic = UrlUtils.withBoardRuntimeId(DashboardConstants.Topic.BOARD_ADD_WIDGET,
                    openBoardRequest.getBoardRuntimeId());
            logDebug(method, "Adding widgets to board. topic: %s", addWidgetToBoardTopic);

            for (WidgetRuntimeInstance widgetRuntimeInstance : widgetRuntimeInstances) {
                logDebug(method, "Adding widget to board. widgetId: %s, widgetRuntimeId: %s",
                        widgetRuntimeInstance.getWidgetId(), widgetRuntimeInstance.getWidgetRuntimeId());

                WidgetRuntimeModels.WidgetRuntimeModel widgetRuntimeModel = ConvertRuntimeModelUtils
                        .toWidgetRuntimeModel(openBoardRequest.getBoardRuntimeId(), widgetRuntimeInstance);

                // send message to board
                messageTemplate.convertAndSend(addWidgetToBoardTopic, widgetRuntimeModel);
            }

            // notify widget of loading state
            for (WidgetRuntimeInstance widgetRuntimeInstance : widgetRuntimeInstances) {
                WidgetStateChangeResponse notifyOnLoadingResult = widgetRuntimeManager
                        .notifyOnLoading(widgetRuntimeInstance);
                if (!notifyOnLoadingResult.isNotified())
                    logError(method, "NotifyOnLoading failed!", notifyOnLoadingResult.getError());
            }

            // notify widget of ready state
            for (WidgetRuntimeInstance widgetRuntimeInstance : widgetRuntimeInstances) {
                WidgetStateChangeResponse notifyOnReadyResult = widgetRuntimeManager
                        .notifyOnReady(widgetRuntimeInstance);
                if (!notifyOnReadyResult.isNotified())
                    logError(method, "NotifyOnReady failed!", notifyOnReadyResult.getError());
            }
        }

        // @formatter:off
		messageTemplate.convertAndSend(
		        DashboardConstants.Topic.BOARD_OPENED,
        		new BoardRuntimeModels.OpenBoardResponse()
        			.withRuntimeBoardId(openBoardRequest.getBoardRuntimeId())
        			.withBoardId(boardRuntimeInstance.getBoardId()));
		// @formatter:on

        boardRuntimeManager.removeOpeningBoardRuntime(openBoardRequest.getBoardRuntimeId());

        long end = System.currentTimeMillis();
        long duration = end - start;

        logInfo(method, "start: %s, end: %s, duration: %s, size: %s", new Timestamp(start), new Timestamp(end),
                duration, boardRuntimeManager.getNumberOfRuntimeInstances());
    }

    @MessageMapping(DashboardConstants.Url.BOARD_UPDATE_LAYOUT)
    public void updateBoardSizes(StompHeaderAccessor headerAccessor, @DestinationVariable String boardRuntimeId,
            BoardRuntimeModels.BoardSizesModel boardSizesModel) {

        long start = System.currentTimeMillis();

        String method = "updateBoardSizes";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s", boardRuntimeId);

        BoardRuntimeInstance boardRuntimeInstance = boardRuntimeManager.getBoardRuntimeInstance(boardRuntimeId);
        Assert.notNull(boardRuntimeInstance, "BoardRuntimeInstance not found! boardRuntimeId: " + boardRuntimeId);

        Board board = boardService.getBoardRepository().findById(boardRuntimeInstance.getBoardId()).orElse(null);
        Assert.notNull(board, "Board not found! boardId=" + boardRuntimeInstance.getBoardId());

        // TODO update board layout, currently only supports columns and it's
        // always 4 for now

        for (BoardRuntimeModels.WidgetPositionModel widgetPositionModel : boardSizesModel.getWidgetPositions()) {

            WidgetRuntimeInstance widgetRuntimeInstance = widgetRuntimeManager
                    .getWidgetRuntimeInstance(widgetPositionModel.getId());
            Assert.notNull(widgetRuntimeInstance,
                    "WidgetRuntimeInstance not found! widgetRuntimeId=" + widgetPositionModel.getId());

            Widget widget = widgetService.getWidgetRepository().findById(widgetRuntimeInstance.getWidgetId())
                    .orElse(null);
            Assert.notNull(widget, "Widget not found! widgetId=" + widgetRuntimeInstance.getWidgetId());

            // layout
            GridsterItemOptions options = new GridsterItemOptions();
            Object layoutValue = widget.getLayoutValue();
            if (layoutValue != null)
                options = (GridsterItemOptions) layoutValue;

            options.setSizeX(widgetPositionModel.getSizeX());
            options.setSizeY(widgetPositionModel.getSizeY());
            options.setRow(widgetPositionModel.getRow());
            options.setCol(widgetPositionModel.getCol());

            widget.setLayout(JsonUtils.toJson(options));
            widget.setLayoutClass(options.getClass().getName());

            // persist changes
            widget = widgetService.update(widget, getUserId(headerAccessor));

            // update runtime instance
            widgetRuntimeInstance.withLayout(options);
            widgetRuntimeManager.updateWidgetRuntimeInstance(widgetRuntimeInstance.getWidgetRuntimeId(),
                    widgetRuntimeInstance);

            WidgetRuntimeModels.WidgetRuntimeModel widgetRuntimeModel = ConvertRuntimeModelUtils
                    .toWidgetRuntimeModel(boardRuntimeId, widgetRuntimeInstance);

            // send message to board
            String boardTopic = UrlUtils.withBoardRuntimeId(DashboardConstants.Topic.BOARD_UPDATE_WIDGET,
                    boardRuntimeId);
            logDebug(method, "topic: %s, boardRuntimeId: %s", boardTopic, boardRuntimeId);
            messageTemplate.convertAndSend(boardTopic, widgetRuntimeModel);
        }

        boardRuntimeInstance = boardRuntimeManager.getBoardRuntimeInstance(boardRuntimeId);
        Assert.notNull(boardRuntimeInstance, "BoardRuntimeInstance not found! boardRuntimeId=" + boardRuntimeId);

        List<WidgetRuntimeInstance> widgetRuntimeInstances = new ArrayList<>();
        for (String widgetRuntimeId : boardRuntimeInstance.getWidgetRuntimeIds())
            widgetRuntimeInstances.add(widgetRuntimeManager.getWidgetRuntimeInstance(widgetRuntimeId));
        logDebug(method, "# of widgets: %s", widgetRuntimeInstances.size());

        // restart widgets if any exist
        if (!widgetRuntimeInstances.isEmpty()) {
            // notify widget of stopped state
            for (WidgetRuntimeInstance widgetRuntimeInstance : widgetRuntimeInstances) {

                WidgetStateChangeResponse notifyOnStoppedResult = widgetRuntimeManager
                        .notifyOnStopped(widgetRuntimeInstance);
                if (!notifyOnStoppedResult.isNotified()) {
                    logError(method, "NotifyOnStopped failed!", notifyOnStoppedResult.getError());
                }
            }

            // notify widget of ready state
            for (WidgetRuntimeInstance widgetRuntimeInstance : widgetRuntimeInstances) {

                WidgetStateChangeResponse notifyOnReadyResult = widgetRuntimeManager
                        .notifyOnReady(widgetRuntimeInstance);
                if (!notifyOnReadyResult.isNotified()) {
                    logError(method, "NotifyOnReady failed!", notifyOnReadyResult.getError());
                }
            }
        }

        long end = System.currentTimeMillis();
        long duration = end - start;

        logInfo(method, "start: %s, end: %s, duration: %s, size: %s", new Timestamp(start), new Timestamp(end),
                duration, boardRuntimeManager.getNumberOfRuntimeInstances());
    }

    @RequestMapping(value = DashboardConstants.Url.CLOSE_BOARD, method = RequestMethod.POST)
    public @ResponseBody BoardRuntimeModels.CloseBoardResponse closeBoard(
            @RequestBody CloseBoardRequest closeBoardRequest) {

        long start = System.currentTimeMillis();

        Assert.notNull(closeBoardRequest, "closeBoardRequest is null");
        Assert.hasText(closeBoardRequest.getBoardRuntimeId(), "boardRuntimeId is empty");

        String method = "closeBoard";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s", closeBoardRequest.getBoardRuntimeId());

        BoardRuntimeInstance boardRuntimeInstance = boardRuntimeManager
                .getBoardRuntimeInstance(closeBoardRequest.getBoardRuntimeId());
        Assert.notNull(boardRuntimeInstance,
                "BoardRuntimeInstance not found! boardRuntimeId: " + closeBoardRequest.getBoardRuntimeId());

        if (boardRuntimeManager.isBoardRuntimeOpening(closeBoardRequest.getBoardRuntimeId())) {
            logWarn(method, "boardRuntimeId: %s is opening right now", closeBoardRequest.getBoardRuntimeId());

            int waitingCyclesCount = 60;
            int waitingCycleTimeMillis = 1000;
            while (boardRuntimeManager.isBoardRuntimeOpening(closeBoardRequest.getBoardRuntimeId())
                    && waitingCyclesCount > 0) {

                logDebug(method, "-----------> waitingCyclesCount: %s", waitingCyclesCount);

                try {
                    Thread.sleep(waitingCycleTimeMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                waitingCyclesCount--;
            }

            if (boardRuntimeManager.isBoardRuntimeOpening(closeBoardRequest.getBoardRuntimeId())) {
                logWarn(method, "boardRuntimeId: %s will be closed event it is opening",
                        closeBoardRequest.getBoardRuntimeId());
            }
        }

        List<WidgetRuntimeInstance> widgetRuntimeInstances = boardRuntimeManager
                .closeBoard(closeBoardRequest.getBoardRuntimeId());
        logDebug(method, "# of widgets: %s", widgetRuntimeInstances.size());

        if (!widgetRuntimeInstances.isEmpty()) {
            // notify widgets
            String removeWidgetToBoardTopic = UrlUtils.withBoardRuntimeId(DashboardConstants.Topic.BOARD_REMOVE_WIDGET,
                    closeBoardRequest.getBoardRuntimeId());
            logDebug(method, "Removing widgets from board. topic: %s", removeWidgetToBoardTopic);

            for (WidgetRuntimeInstance widgetRuntimeInstance : widgetRuntimeInstances) {

                // notify widget of destroy state
                WidgetStateChangeResponse notifyOnDestroyedResult = widgetRuntimeManager
                        .notifyOnDestroyed(widgetRuntimeInstance);
                if (notifyOnDestroyedResult.isNotified()) {
                    logDebug(method, "Removing widget from board. widgetId: %s, widgetRuntimeId: %s",
                            widgetRuntimeInstance.getWidgetId(), widgetRuntimeInstance.getWidgetRuntimeId());
                    messageTemplate.convertAndSend(removeWidgetToBoardTopic,
                            widgetRuntimeInstance.getWidgetRuntimeId());
                } else {
                    logError(method, "NotifyOnDestroyed failed!", notifyOnDestroyedResult.getError());
                }
            }
        }

        long end = System.currentTimeMillis();
        long duration = end - start;

        logInfo(method, "start: %s, end: %s, duration: %s, size: %s", new Timestamp(start), new Timestamp(end),
                duration, boardRuntimeManager.getNumberOfRuntimeInstances());

        return new BoardRuntimeModels.CloseBoardResponse().withBoardRuntimeId(boardRuntimeInstance.getBoardRuntimeId())
                .withBoardId(boardRuntimeInstance.getBoardId());
    }

    @MessageMapping(DashboardConstants.Url.UNREGISTER_BOARD)
    public void unregisterBoard(StompHeaderAccessor headerAccessor, UnregisterBoardRequest unregisterBoardRequest) {

        long start = System.currentTimeMillis();

        Assert.notNull(unregisterBoardRequest, "unregisterBoardRequest is null");
        Assert.hasText(unregisterBoardRequest.getBoardRuntimeId(), "boardRuntimeId is empty");

        String method = "closeBoard";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s", unregisterBoardRequest.getBoardRuntimeId());

        BoardRuntimeInstance boardRuntimeInstance = boardRuntimeManager
                .getBoardRuntimeInstance(unregisterBoardRequest.getBoardRuntimeId());
        Assert.notNull(boardRuntimeInstance,
                "BoardRuntimeInstance not found! boardRuntimeId: " + unregisterBoardRequest.getBoardRuntimeId());

        // unregister board
        boardRuntimeManager.unregisterBoard(unregisterBoardRequest.getBoardRuntimeId());

        // @formatter:off
		messageTemplate.convertAndSend(
		        DashboardConstants.Topic.BOARD_UNREGISTERED,
		        new BoardRuntimeModels.CloseBoardResponse()
		        	.withBoardRuntimeId(boardRuntimeInstance.getBoardRuntimeId())
		            .withBoardId(boardRuntimeInstance.getBoardId()));
		// @formatter:on

        long end = System.currentTimeMillis();
        long duration = end - start;

        logInfo(method, "start: %s, end: %s, duration: %s, size: %s", new Timestamp(start), new Timestamp(end),
                duration, boardRuntimeManager.getNumberOfRuntimeInstances());
    }

    @MessageMapping(DashboardConstants.Url.BOARD_REMOVE_WIDGET)
    public void removeWidget(@DestinationVariable String boardRuntimeId, RemoveWidgetRequest removeWidgetRequest,
            StompHeaderAccessor headerAccessor) {

        long start = System.currentTimeMillis();

        Assert.hasText(boardRuntimeId, "boardRuntimeId is null");
        Assert.notNull(removeWidgetRequest, "removeWidgetRequest is null");
        Assert.hasText(removeWidgetRequest.getWidgetRuntimeId(), "widgetRunTimeId is empty");

        String method = "removeWidget";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s, widgetRuntimeId: %s", boardRuntimeId,
                removeWidgetRequest.getWidgetRuntimeId());

        // notify widget of destroy state
        WidgetStateChangeResponse notifyOnDestroyedResult = widgetRuntimeManager
                .notifyOnDestroyed(removeWidgetRequest.getWidgetRuntimeId());

        if (notifyOnDestroyedResult.isNotified()) {
            // unregister widget
            WidgetRuntimeInstance widgetRuntimeInstance = boardRuntimeManager.removeWidget(boardRuntimeId,
                    removeWidgetRequest.getWidgetRuntimeId());
            Assert.notNull(widgetRuntimeInstance, "WidgetRuntimeInstance not found! boardRuntimeId=" + boardRuntimeId
                    + " widgetRuntimeId=" + removeWidgetRequest.getWidgetRuntimeId());

            // delete widget
            widgetService.delete(widgetRuntimeInstance.getWidgetId());

            messageTemplate.convertAndSend(
                    UrlUtils.withBoardRuntimeId(DashboardConstants.Topic.BOARD_REMOVE_WIDGET, boardRuntimeId),
                    removeWidgetRequest.getWidgetRuntimeId());
        } else {
            logError(method, "NotifyOnDestroyed failed!", notifyOnDestroyedResult.getError());
        }

        long end = System.currentTimeMillis();
        long duration = end - start;

        logInfo(method, "start: %s, end: %s, duration: %s, size: %s", new Timestamp(start), new Timestamp(end),
                duration, boardRuntimeManager.getNumberOfRuntimeInstances());
    }
}
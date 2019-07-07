package com.arrow.dashboard.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.arrow.dashboard.runtime.model.BoardRuntimeInstance;
import com.arrow.dashboard.runtime.model.UserRuntimeInstance;
import com.arrow.dashboard.runtime.model.WidgetRuntimeConfigurationPatch;
import com.arrow.dashboard.runtime.model.WidgetRuntimeInstance;
import com.arrow.pegasus.dashboard.data.Board;
import com.arrow.pegasus.dashboard.data.Widget;
import com.arrow.pegasus.dashboard.repo.WidgetSearchParams;
import com.arrow.pegasus.dashboard.service.BoardService;
import com.arrow.pegasus.dashboard.service.WidgetService;

public class BoardRuntimeManager extends RuntimeManagerAbstract<BoardRuntimeInstance> {

    @Autowired
    private BoardService boardService;
    @Autowired
    private WidgetService widgetService;
    @Autowired
    private WidgetRuntimeManager widgetRuntimeManager;

    private Set<String> openingBoardRuntimes = new HashSet<>();

    public void putOpeningBoardRuntime(String boardRuntimeId) {
        String method = "putOpeningBoardRuntime";
        synchronized (openingBoardRuntimes) {
            openingBoardRuntimes.add(boardRuntimeId);
            logDebug(method, "boardRuntimeId: " + boardRuntimeId);
        }
    }

    public void removeOpeningBoardRuntime(String boardRuntimeId) {
        String method = "removeOpeningBoardRuntime";
        synchronized (openingBoardRuntimes) {
            openingBoardRuntimes.remove(boardRuntimeId);
            logDebug(method, "boardRuntimeId: " + boardRuntimeId);
        }
    }

    public boolean isBoardRuntimeOpening(String boardRuntimeId) {
        String method = "removeOpeningBoardRuntime";
        synchronized (openingBoardRuntimes) {
            boolean isOpening = openingBoardRuntimes.contains(boardRuntimeId);
            logDebug(method, "boardRuntimeId: " + boardRuntimeId + ", isOpening: " + isOpening);
            return isOpening;
        }
    }

    public String generateRandomBoardRuntimeId() {
        return createRuntimeInstanceId();
    }

    public BoardRuntimeInstance getBoardRuntimeInstance(String boardRuntimeId) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");

        String method = "getBoardRuntimeInstance";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s", boardRuntimeId);

        return getRuntimeInstance(boardRuntimeId);
    }

    public BoardRuntimeInstance getBoardRuntimeInstanceByWebSocketSessionId(String webSocketSessionId) {
        Assert.hasText(webSocketSessionId, "webSocketSessionId is empty");

        String method = "getBoardRuntimeInstanceByWebSocketSessionId";
        logInfo(method, "...");
        logDebug(method, "webSocketSessionId: %s", webSocketSessionId);

        for (BoardRuntimeInstance boardRuntimeInstance : getRuntimeInstances())
            if (boardRuntimeInstance.getWebSocketSessionId().equals(webSocketSessionId))
                return boardRuntimeInstance;

        return null;
    }

    public BoardRuntimeInstance registerBoard(String websocketSessionId, String boardId) {
        Assert.hasText(boardId, "boardId is empty");
        Assert.hasText(websocketSessionId, "websocketSessionId is empty");

        String method = "registerBoard";
        logInfo(method, "...");
        logDebug(method, "boardId: %s", boardId);

        Board board = boardService.getBoardRepository().findById(boardId).orElse(null);
        Assert.notNull(board, "Board not found! boardId=" + boardId);

        // @formatter:off
		BoardRuntimeInstance boardRuntimeInstance = new BoardRuntimeInstance()
				.withWebSocketSessionId(websocketSessionId)
				.withBoardRuntimeId(createRuntimeInstanceId())
		        .withBoardId(boardId)
		        .withBoardName(board.getName())
		        .withBoardDescription(board.getDescription())
		        .withLayout(board.getLayoutValue());
		// @formatter:on

        // persist
        registerRuntimeInstance(boardRuntimeInstance.getBoardRuntimeId(), boardRuntimeInstance);

        return boardRuntimeInstance;
    }

    public List<WidgetRuntimeInstance> openBoard(String boardRuntimeId, UserRuntimeInstance userRuntime) {
        return openBoard(boardRuntimeId, null, userRuntime);
    }

    public List<WidgetRuntimeInstance> openBoard(String boardRuntimeId,
            WidgetRuntimeConfigurationPatch configurationPatch, UserRuntimeInstance userRuntime) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");

        String method = "openBoard";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s", boardRuntimeId);

        BoardRuntimeInstance boardRuntimeInstance = getRuntimeInstance(boardRuntimeId);
        Assert.notNull(boardRuntimeInstance, "BoardRuntimeInstance not found! boardRuntimeId=" + boardRuntimeId);

        WidgetSearchParams widgetSearchParams = new WidgetSearchParams();
        widgetSearchParams.addParentIds(boardRuntimeInstance.getBoardId());
        List<Widget> widgets = widgetService.getWidgetRepository().findWidgets(widgetSearchParams);
        logDebug(method, "# of widgets: %s", widgets.size());

        Set<String> widgetRuntimeIds = new HashSet<>();
        List<WidgetRuntimeInstance> widgetRuntimeInstances = new ArrayList<>();
        // register all widgets
        for (Widget widget : widgets) {
            logDebug(method, "name: %s, desciption: %s", widget.getName(), widget.getDescription());

            WidgetRuntimeInstance widgetRuntimeInstance = widgetRuntimeManager.registerWidget(boardRuntimeId, widget,
                    configurationPatch, userRuntime);
            widgetRuntimeInstances.add(widgetRuntimeInstance);
            widgetRuntimeIds.add(widgetRuntimeInstance.getWidgetRuntimeId());
        }

        // update with widget runtime ids
        boardRuntimeInstance.withWidgetRuntimeIds(widgetRuntimeIds);
        // persist
        registerRuntimeInstance(boardRuntimeInstance.getBoardRuntimeId(), boardRuntimeInstance);

        return widgetRuntimeInstances;
    }

    // known issue is - close board request may comes when this board is not
    // fully 'opened'
    public List<WidgetRuntimeInstance> closeBoard(String boardRuntimeId) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");

        String method = "closeBoard";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s", boardRuntimeId);

        BoardRuntimeInstance boardRuntimeInstance = getRuntimeInstance(boardRuntimeId);
        Assert.notNull(boardRuntimeInstance, "BoardRuntimeInstance not found! boardRuntimeId=" + boardRuntimeId);

        // unregister all widget runtime instances
        List<WidgetRuntimeInstance> widgetRuntimeInstances = new ArrayList<>();
        for (String widgetRuntimeId : boardRuntimeInstance.getWidgetRuntimeIds())
            widgetRuntimeInstances.add(widgetRuntimeManager.unregisterWidget(widgetRuntimeId));

        // update with empty widget runtime list
        boardRuntimeInstance.withWidgetRuntimeIds(Collections.emptySet());
        // persist
        registerRuntimeInstance(boardRuntimeInstance.getBoardRuntimeId(), boardRuntimeInstance);

        return widgetRuntimeInstances;
    }

    public BoardRuntimeInstance unregisterBoard(String boardRuntimeId) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");

        String method = "unregisterBoard";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s", boardRuntimeId);

        return unregisterRuntimeInstance(boardRuntimeId);
    }

    public void addWidget(String boardRuntimeId, String widgetRuntimeId) {
    }

    public WidgetRuntimeInstance registerWidgetToBoard(String boardRuntimeId,
            WidgetRuntimeInstance existingWidgetRuntimeInstance, UserRuntimeInstance userRuntime) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");
        Assert.notNull(existingWidgetRuntimeInstance, "existingWidgetRuntimeInstance is null");

        String method = "addWidget";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s, widgetRuntimeId: %s", boardRuntimeId,
                existingWidgetRuntimeInstance.getWidgetRuntimeId());

        BoardRuntimeInstance boardRuntimeInstance = getRuntimeInstance(boardRuntimeId);
        Assert.notNull(boardRuntimeInstance, "BoardRuntimeInstance not found! boardRuntimeId=" + boardRuntimeId);

        // unregister
        WidgetRuntimeInstance unregisterWidgetRuntimeInstance = widgetRuntimeManager
                .unregisterWidget(existingWidgetRuntimeInstance.getWidgetRuntimeId());

        // lookup widget
        Widget widget = widgetService.getWidgetRepository().findById(existingWidgetRuntimeInstance.getWidgetId())
                .orElse(null);
        Assert.notNull(widget, "Widget not found! widgetId=" + existingWidgetRuntimeInstance.getWidgetId());

        // register
        WidgetRuntimeInstance widgetRuntimeInstance = widgetRuntimeManager.registerWidget(boardRuntimeId, widget,
                userRuntime);

        Set<String> widgetRuntimeIds = new HashSet<>(boardRuntimeInstance.getWidgetRuntimeIds());
        widgetRuntimeIds.add(widgetRuntimeInstance.getWidgetRuntimeId());
        // update with widget runtime ids
        boardRuntimeInstance.withWidgetRuntimeIds(widgetRuntimeIds);
        // persist
        registerRuntimeInstance(boardRuntimeInstance.getBoardRuntimeId(), boardRuntimeInstance);

        // TODO revisit, may need to return both the unregistered and registered
        // widgetRuntimeInstances

        return widgetRuntimeInstance;
    }

    public WidgetRuntimeInstance removeWidget(String boardRuntimeId, String widgetRuntimeId) {
        Assert.hasText(boardRuntimeId, "boardRuntimeId is empty");

        String method = "removeWidget";
        logInfo(method, "...");
        logDebug(method, "boardRuntimeId: %s", boardRuntimeId);

        BoardRuntimeInstance boardRuntimeInstance = getRuntimeInstance(boardRuntimeId);
        Assert.notNull(boardRuntimeInstance, "BoardRuntimeInstance not found! boardRuntimeId=" + boardRuntimeId);

        // unregister widget runtime instances
        WidgetRuntimeInstance widgetRuntimeInstance = widgetRuntimeManager.unregisterWidget(widgetRuntimeId);

        // update with widget runtime ids
        Set<String> widgetRuntimeIds = new HashSet<>(boardRuntimeInstance.getWidgetRuntimeIds());
        // remove widget runtime id from widget runtime ids
        widgetRuntimeIds.remove(widgetRuntimeId);
        boardRuntimeInstance.withWidgetRuntimeIds(widgetRuntimeIds);

        // persist
        registerRuntimeInstance(boardRuntimeInstance.getBoardRuntimeId(), boardRuntimeInstance);

        return widgetRuntimeInstance;
    }
}
package com.arrow.pegasus.dashboard.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.pegasus.dashboard.data.Board;
import com.arrow.pegasus.dashboard.data.Container;
import com.arrow.pegasus.dashboard.data.Widget;
import com.arrow.pegasus.dashboard.repo.BoardRepository;
import com.arrow.pegasus.dashboard.repo.ContainerSearchParams;
import com.arrow.pegasus.dashboard.repo.WidgetSearchParams;

@Service
public class BoardService extends DashboardServiceAbstract {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ContainerService containerService;
    @Autowired
    private WidgetService widgetService;

    public BoardService() {
        super();
    }

    public BoardRepository getBoardRepository() {
        return boardRepository;
    }

    public Board create(Board board, String who) {
        Assert.notNull(board, "serviceRuntime is null");
        Assert.hasText(who, "who is empty");

        String method = "create";
        logDebug(method, "...");

        if (StringUtils.isEmpty(board.getProductId()) && StringUtils.isEmpty(board.getApplicationId())
                && StringUtils.isEmpty(board.getUserId())) {
            logDebug(method,
                    "board must be populated with at least one of the following: productId, applicationId or userId");
            throw new AcsLogicalException(
                    "board must be populated with at least one of the following: productId, applicationId or userId");
        }

        if (StringUtils.isEmpty(board.getName())) {
            logDebug(method, "name is empty");
            throw new AcsLogicalException("name is empty");
        }

        board = boardRepository.doInsert(board, who);

        logDebug(method, "board: %s has been created", board.getId());

        return board;
    }

    public Board update(Board board, String who) {
        Assert.notNull(board, "Board is null");
        Assert.hasText(who, "who is empty");

        String method = "update";
        logDebug(method, "...");

        if (StringUtils.isEmpty(board.getProductId()) && StringUtils.isEmpty(board.getApplicationId())
                && StringUtils.isEmpty(board.getUserId())) {
            logDebug(method,
                    "board must be populated with at least one of the following: productId, applicationId or userId");
            throw new AcsLogicalException(
                    "board must be populated with at least one of the following: productId, applicationId or userId");
        }

        board = boardRepository.doSave(board, who);

        logDebug(method, "board: %s has been updated", board.getId());

        return board;
    }

    public void delete(String boardId) {
        Assert.hasText(boardId, "boardId is empty");

        String method = "delete";
        logDebug(method, "...");

        Board board = boardRepository.findById(boardId).orElse(null);
        Assert.notNull(board, "Board not found! boardId: " + boardId);

        // containers
        ContainerSearchParams containerSearchParams = new ContainerSearchParams();
        containerSearchParams.addBoardIds(board.getId());
        for (Container container : containerService.getContainerRepository().findContainers(containerSearchParams))
            containerService.delete(container.getId());

        // widgets
        WidgetSearchParams widgetSearchParams = new WidgetSearchParams();
        widgetSearchParams.addParentIds(board.getId());
        for (Widget widget : widgetService.getWidgetRepository().findWidgets(widgetSearchParams))
            widgetService.delete(widget.getId());

        // board
        boardRepository.delete(board);

        logDebug(method, "board: %s has been deleted", boardId);
    }

    public Board populate(Board board) {

        if (board != null) {
            if (!StringUtils.isEmpty(board.getProductId()) && board.getRefProduct() == null)
                board.setRefProduct(getCoreCacheService().findProductById(board.getProductId()));

            if (!StringUtils.isEmpty(board.getApplicationId()) && board.getRefApplication() == null)
                board.setRefApplication(getCoreCacheService().findApplicationById(board.getApplicationId()));

            if (!StringUtils.isEmpty(board.getUserId()) && board.getRefUser() == null)
                board.setRefUser(getCoreCacheService().findUserById(board.getUserId()));
        }

        return board;
    }
}
package com.arrow.dashboard.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.dashboard.DashboardConstants;
import com.arrow.dashboard.runtime.BoardRuntimeManager;
import com.arrow.dashboard.web.model.BoardModels;
import com.arrow.dashboard.web.model.ConvertModelUtils;
import com.arrow.dashboard.web.model.runtime.BoardRuntimeModels;
import com.arrow.pegasus.dashboard.data.Board;
import com.arrow.pegasus.dashboard.service.BoardService;

@RestController
public class CoreBoardController extends CoreDashboardControllerAbstract {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRuntimeManager boardRuntimeManager;

    @RequestMapping(value = DashboardConstants.URL_BOARDS_BASE
            + "/generate/board/runtime/id", method = RequestMethod.GET)
    public BoardRuntimeModels.BoardRuntimeIdModel generateBoardRuntimeId() {
        return new BoardRuntimeModels.BoardRuntimeIdModel()
                .withBoardRuntimeId(boardRuntimeManager.generateRandomBoardRuntimeId());
    }

    public BoardModels.CreateBoardResponse createBoard(@RequestBody BoardModels.CreateBoardRequest model,
            HttpSession session) {
        Assert.notNull(model, "model is null");

        String method = "createBoard";
        logInfo(method, "...");

        Board board = ConvertModelUtils.toBoard(model.getBoard());
        board = boardService.create(board, getUserId());

        return new BoardModels.CreateBoardResponse().withBoard(ConvertModelUtils.toReadBoard(board));
    }

    public BoardModels.ReadBoardModel readBoard(@PathVariable String boardId) {
        Assert.hasText(boardId, "boardId is empty");

        String method = "readBoard";
        logInfo(method, "...");

        Board board = boardService.getBoardRepository().findById(boardId).orElse(null);
        Assert.notNull(board, "Board not found! boardId=" + boardId);

        return ConvertModelUtils.toReadBoard(board);
    }

    public BoardModels.UpdateBoardResponse updateBoard(@RequestBody BoardModels.UpdateBoardRequest model,
            HttpSession session) {
        Assert.notNull(model, "model is null");
        Assert.notNull(model.getBoard(), "board is null");
        Assert.hasText(model.getBoard().getId(), "boardId is empty");

        String method = "updateBoard";
        logInfo(method, "...");

        Board board = boardService.getBoardRepository().findById(model.getBoard().getId()).orElse(null);
        Assert.notNull(board, "Board not found! boardId=" + model.getBoard().getId());

        board = ConvertModelUtils.toBoard(model.getBoard(), board);
        board = boardService.update(board, getUserId());

        return new BoardModels.UpdateBoardResponse().withBoard(ConvertModelUtils.toReadBoard(board));
    }

    public BoardModels.DeleteBoardResponse deleteBoard(@PathVariable String boardId) {
        Assert.hasText(boardId, "boardId is empty");

        String method = "deleteBoard";
        logInfo(method, "...");

        Board board = boardService.getBoardRepository().findById(boardId).orElse(null);
        Assert.notNull(board, "Board not found! boardId=" + boardId);

        boardService.delete(boardId);

        return new BoardModels.DeleteBoardResponse();
    }

    // TODO support find boards
}
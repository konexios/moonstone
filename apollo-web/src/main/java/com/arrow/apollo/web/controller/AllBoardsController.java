package com.arrow.apollo.web.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.apollo.service.ApolloBoardService;
import com.arrow.apollo.web.model.AllBoardsModels;
import com.arrow.apollo.web.model.AllBoardsModels.BoardModel;
import com.arrow.apollo.web.model.ApolloModelUtil;
import com.arrow.pegasus.dashboard.data.Board;

@RestController
@RequestMapping("/api/apollo/all/boards")
public class AllBoardsController extends ApolloControllerAbstract {

    @Autowired
    private ApolloBoardService apolloBoardService;

    @RequestMapping("/default")
    public List<AllBoardsModels.BoardModel> findDefaultBoard(HttpSession session) {

        String method = "findDefaultBoard";
        logDebug(method, "...");

        List<AllBoardsModels.BoardModel> boards = new ArrayList<>();

        // user default
        Board board = apolloBoardService.findUserDefault(getApplicationId(session), getUserId());
        if (board != null)
            boards.add(ApolloModelUtil.toAllBoardModel(board));

        // system default
        board = apolloBoardService.findSystemDefault();
        if (board != null)
            boards.add(ApolloModelUtil.toAllBoardModel(board));

        return boards;
    }

    @RequestMapping("/all")
    public List<AllBoardsModels.BoardModel> findAllBoards(HttpSession session) {

        List<Board> allBoards = apolloBoardService.findAllBoards(getApplicationId(session), getUserId());
        for (Board board : allBoards)
            board = apolloBoardService.getBoardService().populate(board);

        List<BoardModel> allBoardModels = ApolloModelUtil.toAllBoardModels(allBoards);
        if (allBoardModels != null && !allBoardModels.isEmpty())
            allBoardModels.sort(Comparator.comparing(BoardModel::getName, String.CASE_INSENSITIVE_ORDER));

        return allBoardModels;
    }

    @RequestMapping("/custom")
    public List<AllBoardsModels.BoardModel> findCustomBoards(HttpSession session) {
        return ApolloModelUtil
                .toAllBoardModels(apolloBoardService.findCustomBoards(getApplicationId(session), getUserId()));
    }

    @RequestMapping("/favorite")
    public List<AllBoardsModels.BoardModel> findFavoriteBoards(HttpSession session) {
        return ApolloModelUtil
                .toAllBoardModels(apolloBoardService.findFavoriteBoards(getApplicationId(session), getUserId()));
    }

    @RequestMapping("/certified")
    public List<AllBoardsModels.BoardModel> findArrowCertifiedBoards(HttpSession session) {
        List<BoardModel> boardModels = ApolloModelUtil.toAllBoardModels(apolloBoardService.findArrowCertifiedBoards());
        if (boardModels != null && !boardModels.isEmpty())
            boardModels.sort(Comparator.comparing(BoardModel::getName, String.CASE_INSENSITIVE_ORDER));

        return boardModels;
    }

    @RequestMapping("/shared")
    public List<AllBoardsModels.BoardModel> findSharedBoards(HttpSession session) {

        return null;
    }
}
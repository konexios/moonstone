package com.arrow.dashboard.web.model.session;

import java.io.Serializable;

public class SessionBoardRuntimeModel implements Serializable {
	private static final long serialVersionUID = -826487154767592215L;

	private String boardId;
	private String boardRuntimeId;

	public String getBoardId() {
		return boardId;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public String getBoardRuntimeId() {
		return boardRuntimeId;
	}

	public void setBoardRuntimeId(String boardRuntimeId) {
		this.boardRuntimeId = boardRuntimeId;
	}

	public SessionBoardRuntimeModel withBoardId(String boardId) {
		setBoardId(boardId);

		return this;
	}

	public SessionBoardRuntimeModel withBoardRuntimeId(String boardRuntimeId) {
		setBoardRuntimeId(boardRuntimeId);

		return this;
	}
}

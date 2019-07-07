package com.arrow.dashboard.runtime.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class BoardRuntimeInstance implements Serializable {
	private static final long serialVersionUID = -3549941135452281420L;

	private String webSocketSessionId;
	private String boardRuntimeId;
	private String boardId;
	private String boardName;
	private String boardDescription;
	private Object layout;
	private Set<String> widgetRuntimeIds = new HashSet<>();

	public String getWebSocketSessionId() {
		return webSocketSessionId;
	}

	public void setWebSocketSessionId(String webSocketSessionId) {
		this.webSocketSessionId = webSocketSessionId;
	}

	public String getBoardRuntimeId() {
		return boardRuntimeId;
	}

	public void setBoardRuntimeId(String runtimeBoardId) {
		this.boardRuntimeId = runtimeBoardId;
	}

	public String getBoardId() {
		return boardId;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public String getBoardDescription() {
		return boardDescription;
	}

	public void setBoardDescription(String boardDescription) {
		this.boardDescription = boardDescription;
	}

	public Object getLayout() {
		return layout;
	}

	public void setLayout(Object layout) {
		this.layout = layout;
	}

	public Set<String> getWidgetRuntimeIds() {
		return widgetRuntimeIds;
	}

	public void setWidgetRuntimeIds(Set<String> widgetRuntimeIds) {
		if (widgetRuntimeIds != null)
			this.widgetRuntimeIds = widgetRuntimeIds;
	}

	public BoardRuntimeInstance withWebSocketSessionId(String webSocketSessionId) {
		setWebSocketSessionId(webSocketSessionId);

		return this;
	}

	public BoardRuntimeInstance withBoardRuntimeId(String boardRuntimeId) {
		setBoardRuntimeId(boardRuntimeId);

		return this;
	}

	public BoardRuntimeInstance withBoardId(String boardId) {
		setBoardId(boardId);

		return this;
	}

	public BoardRuntimeInstance withBoardName(String boardName) {
		setBoardName(boardName);

		return this;
	}

	public BoardRuntimeInstance withBoardDescription(String boardDescription) {
		setBoardDescription(boardDescription);

		return this;
	}

	public BoardRuntimeInstance withLayout(Object layout) {
		setLayout(layout);

		return this;
	}

	public BoardRuntimeInstance withWidgetRuntimeIds(Set<String> widgetRuntimeIds) {
		setWidgetRuntimeIds(widgetRuntimeIds);

		return this;
	}
}

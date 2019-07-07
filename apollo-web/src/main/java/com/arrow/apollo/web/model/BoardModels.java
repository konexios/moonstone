package com.arrow.apollo.web.model;

import java.io.Serializable;

import com.arrow.apollo.data.ApolloBoardCategory;

public class BoardModels {

	public static class BoardModel implements Serializable {
		private static final long serialVersionUID = 2174274552932081278L;

		private String id;
		private String name;
		private String description;
		private ApolloBoardCategory category;
		private String userId;
		private Integer numberOfColumns;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public ApolloBoardCategory getCategory() {
			return category;
		}

		public void setCategory(ApolloBoardCategory category) {
			this.category = category;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public Integer getNumberOfColumns() {
			return numberOfColumns;
		}

		public void setNumberOfColumns(Integer numberOfColumns) {
			this.numberOfColumns = numberOfColumns;
		}

		public BoardModel withId(String id) {
			setId(id);

			return this;
		}

		public BoardModel withName(String name) {
			setName(name);

			return this;
		}

		public BoardModel withDescription(String description) {
			setDescription(description);

			return this;
		}

		public BoardModel withCategory(ApolloBoardCategory category) {
			setCategory(category);

			return this;
		}

		public BoardModel withUserId(String userId) {
			setUserId(userId);

			return this;
		}

		public BoardModel withNumberOfColumns(Integer numberOfColumns) {
			setNumberOfColumns(numberOfColumns);

			return this;
		}
	}

	public static class BoardUpsertModel implements Serializable {
		private static final long serialVersionUID = 2956062601501280216L;

		private BoardModels.BoardModel board;
		private ApolloBoardCategory[] categories;

		public BoardUpsertModel(BoardModels.BoardModel board, ApolloBoardCategory[] categories) {
			this.board = board;
			this.categories = categories;
		}

		public BoardModels.BoardModel getBoard() {
			return board;
		}

		public void setBoard(BoardModels.BoardModel board) {
			this.board = board;
		}

		public ApolloBoardCategory[] getCategories() {
			return categories;
		}

		public void setCategories(ApolloBoardCategory[] categories) {
			this.categories = categories;
		}
	}

	public static class CreateBoardModel extends BoardModel {
		private static final long serialVersionUID = 1532311895211132864L;
	}

	public static class UpdateBoardModel extends BoardModel {
		private static final long serialVersionUID = -2414721883907253545L;
	}

	public static class CopyArrowCertifiedBoardModel implements Serializable {
		private static final long serialVersionUID = -5085453175038697280L;

		private String boardId;
		private String deviceUid;

		public String getBoardId() {
			return boardId;
		}

		public void setBoardId(String boardId) {
			this.boardId = boardId;
		}

		public String getDeviceUid() {
			return deviceUid;
		}

		public void setDeviceUid(String deviceUid) {
			this.deviceUid = deviceUid;
		}
	}
}

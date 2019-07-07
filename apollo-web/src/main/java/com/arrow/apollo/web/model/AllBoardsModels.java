package com.arrow.apollo.web.model;

import java.io.Serializable;

import com.arrow.apollo.data.ApolloBoardCategory;

public class AllBoardsModels {

	public static class BoardModel implements Serializable {
		private static final long serialVersionUID = -2087321894782081708L;

		private String id;
		private String name;
		private String description;
		private ApolloBoardCategory category;
		private String userId;
		private String owner;
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

		public String getOwner() {
			return owner;
		}

		public void setOwner(String owner) {
			this.owner = owner;
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

		public BoardModel withOwner(String owner) {
			setOwner(owner);

			return this;
		}
		
		public BoardModel withNumberOfColumns(Integer numberOfColumns) {
			setNumberOfColumns(numberOfColumns);
			
			return this;
		}
	}

}

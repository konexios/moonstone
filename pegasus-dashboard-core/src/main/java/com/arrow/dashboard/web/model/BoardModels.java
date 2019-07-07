package com.arrow.dashboard.web.model;

import java.io.Serializable;
import java.time.Instant;

public class BoardModels {

	public abstract static class BaseBoardModelAbstract<T extends BaseBoardModelAbstract<T>> implements Serializable {
		private static final long serialVersionUID = 6273741584806036374L;

		private String name;
		private String description;
		private String category;
		private String productId;
		private String applicationId;
		private String userId;

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

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getProductId() {
			return productId;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public String getApplicationId() {
			return applicationId;
		}

		public void setApplicationId(String applicationId) {
			this.applicationId = applicationId;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		protected abstract T self();

		public T withName(String name) {
			setName(name);

			return self();
		}

		public T withDescription(String description) {
			setDescription(description);

			return self();
		}

		public T withCategory(String category) {
			setCategory(category);

			return self();
		}

		public T withProductId(String productId) {
			setProductId(productId);

			return self();
		}

		public T withApplicationId(String applicationId) {
			setApplicationId(applicationId);

			return self();
		}

		public T withUserId(String userId) {
			setUserId(userId);

			return self();
		}

	}

	public static class CreateBoardModel extends BaseBoardModelAbstract<CreateBoardModel> {
		private static final long serialVersionUID = -5027707026520693543L;

		@Override
		protected CreateBoardModel self() {
			return this;
		};
	}

	public static class UpdateBoardModel extends BaseBoardModelAbstract<UpdateBoardModel> {
		private static final long serialVersionUID = -1437128343914605584L;

		private String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		@Override
		protected UpdateBoardModel self() {
			return this;
		};

		public UpdateBoardModel withId(String id) {
			setId(id);

			return self();
		}
	}

	public static class ReadBoardModel extends BaseBoardModelAbstract<ReadBoardModel> {
		private static final long serialVersionUID = -5601043218414252094L;

		private String id;
		private Instant createdDate;
		private String createdBy;
		private Instant lastModifiedDate;
		private String lastModifiedBy;
		private String productName;
		private String applicationName;
		private String userName;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Instant getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Instant createdDate) {
			this.createdDate = createdDate;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}

		public Instant getLastModifiedDate() {
			return lastModifiedDate;
		}

		public void setLastModifiedDate(Instant lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}

		public String getLastModifiedBy() {
			return lastModifiedBy;
		}

		public void setLastModifiedBy(String lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public String getApplicationName() {
			return applicationName;
		}

		public void setApplicationName(String applicationName) {
			this.applicationName = applicationName;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		@Override
		protected ReadBoardModel self() {
			return this;
		};

		public ReadBoardModel withId(String id) {
			setId(id);

			return self();
		}

		public ReadBoardModel withCreatedDate(Instant createdDate) {
			setCreatedDate(createdDate);

			return self();
		}

		public ReadBoardModel withCreatedBy(String createdBy) {
			setCreatedBy(createdBy);

			return self();
		}

		public ReadBoardModel withLastModifiedDate(Instant lastModifiedDate) {
			setLastModifiedDate(lastModifiedDate);

			return self();
		}

		public ReadBoardModel withLastModifiedBy(String lastModifiedBy) {
			setLastModifiedBy(lastModifiedBy);

			return self();
		}

		public ReadBoardModel withProductName(String productName) {
			setProductName(productName);

			return self();
		}

		public ReadBoardModel withApplicationName(String applicationName) {
			setApplicationName(applicationName);

			return self();
		}

		public ReadBoardModel withUserName(String userName) {
			setUserName(userName);

			return self();
		}
	}

	public static class CreateBoardRequest implements Serializable {
		private static final long serialVersionUID = 7598791355259560686L;

		private CreateBoardModel board;

		public CreateBoardModel getBoard() {
			return board;
		}

		public void setBoard(CreateBoardModel board) {
			this.board = board;
		}
	}

	public static class CreateBoardResponse implements Serializable {
		private static final long serialVersionUID = 1076690338088412520L;

		private ReadBoardModel board;

		public ReadBoardModel getBoard() {
			return board;
		}

		public void setBoard(ReadBoardModel board) {
			this.board = board;
		}

		public CreateBoardResponse withBoard(ReadBoardModel board) {
			setBoard(board);

			return this;
		}
	}

	public static class UpdateBoardRequest implements Serializable {
		private static final long serialVersionUID = -9206096497569001892L;

		private UpdateBoardModel board;

		public UpdateBoardModel getBoard() {
			return board;
		}

		public void setBoard(UpdateBoardModel board) {
			this.board = board;
		}
	}

	public static class UpdateBoardResponse implements Serializable {
		private static final long serialVersionUID = 6077283056642660963L;

		private ReadBoardModel board;

		public ReadBoardModel getBoard() {
			return board;
		}

		public void setBoard(ReadBoardModel board) {
			this.board = board;
		}

		public UpdateBoardResponse withBoard(ReadBoardModel board) {
			setBoard(board);

			return this;
		}
	}

	public static class DeleteBoardResponse implements Serializable {
		private static final long serialVersionUID = 3246764202800694085L;

	}
}
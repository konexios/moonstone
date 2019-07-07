package com.arrow.dashboard.web.model;

import java.io.Serializable;
import java.time.Instant;

public class ContainerModels {

	public abstract static class BaseContainerModelAbstract<T extends BaseContainerModelAbstract<T>>
	        implements Serializable {
		private static final long serialVersionUID = -5791758312119739689L;

		private String name;
		private String description;
		private String boardId;
		private String layout;

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

		public String getBoardId() {
			return boardId;
		}

		public void setBoardId(String boardId) {
			this.boardId = boardId;
		}

		public String getLayout() {
			return layout;
		}

		public void setLayout(String layout) {
			this.layout = layout;
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

		public T withBoardId(String containerId) {
			setBoardId(containerId);

			return self();
		}

		public T withLayout(String layout) {
			setLayout(layout);

			return self();
		}
	}

	public static class CreateContainerModel extends BaseContainerModelAbstract<CreateContainerModel> {
		private static final long serialVersionUID = -6200456336576938652L;

		@Override
		protected CreateContainerModel self() {
			return this;
		};
	}

	public static class UpdateContainerModel extends BaseContainerModelAbstract<UpdateContainerModel> {
		private static final long serialVersionUID = -6134182839533787873L;

		private String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		@Override
		protected UpdateContainerModel self() {
			return this;
		};

		public UpdateContainerModel withId(String id) {
			setId(id);

			return self();
		}
	}

	public static class ReadContainerModel extends BaseContainerModelAbstract<ReadContainerModel> {
		private static final long serialVersionUID = 1190217629813684054L;

		private String id;
		private Instant createdDate;
		private String createdBy;
		private Instant lastModifiedDate;
		private String lastModifiedBy;
		private String boardName;

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

		public String getBoardName() {
			return boardName;
		}

		public void setBoardName(String boardName) {
			this.boardName = boardName;
		}

		@Override
		protected ReadContainerModel self() {
			return this;
		};

		public ReadContainerModel withId(String id) {
			setId(id);

			return self();
		}

		public ReadContainerModel withCreatedDate(Instant createdDate) {
			setCreatedDate(createdDate);

			return self();
		}

		public ReadContainerModel withCreatedBy(String createdBy) {
			setCreatedBy(createdBy);

			return self();
		}

		public ReadContainerModel withLastModifiedDate(Instant lastModifiedDate) {
			setLastModifiedDate(lastModifiedDate);

			return self();
		}

		public ReadContainerModel withLastModifiedBy(String lastModifiedBy) {
			setLastModifiedBy(lastModifiedBy);

			return self();
		}

		public ReadContainerModel withBoardName(String boardName) {
			setBoardName(boardName);

			return self();
		}
	}

	public static class CreateContainerRequest implements Serializable {
		private static final long serialVersionUID = 7770268219362101920L;

		private CreateContainerModel container;

		public CreateContainerModel getContainer() {
			return container;
		}

		public void setContainer(CreateContainerModel container) {
			this.container = container;
		}
	}

	public static class CreateContainerResponse implements Serializable {
		private static final long serialVersionUID = 4981651889586582969L;

		private ReadContainerModel container;

		public ReadContainerModel getContainer() {
			return container;
		}

		public void setContainer(ReadContainerModel container) {
			this.container = container;
		}

		public CreateContainerResponse withContainer(ReadContainerModel container) {
			setContainer(container);

			return this;
		}
	}

	public static class UpdateContainerRequest implements Serializable {
		private static final long serialVersionUID = -9167644088210075441L;

		private UpdateContainerModel container;

		public UpdateContainerModel getContainer() {
			return container;
		}

		public void setContainer(UpdateContainerModel container) {
			this.container = container;
		}
	}

	public static class UpdateContainerResponse implements Serializable {
		private static final long serialVersionUID = 6576812895788395864L;

		private ReadContainerModel container;

		public ReadContainerModel getContainer() {
			return container;
		}

		public void setContainer(ReadContainerModel container) {
			this.container = container;
		}

		public UpdateContainerResponse withContainer(ReadContainerModel container) {
			setContainer(container);

			return this;
		}
	}

	public static class DeleteContainerResponse implements Serializable {
		private static final long serialVersionUID = -132116174418804169L;

	}
}
package com.arrow.dashboard.web.model.runtime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class BoardRuntimeModels {

	public static class BoardRuntimeIdModel implements Serializable {
		private static final long serialVersionUID = -4965167966307094803L;

		private String boardRuntimeId;

		public String getBoardRuntimeId() {
			return boardRuntimeId;
		}

		public void setBoardRuntimeId(String boardRuntimeId) {
			this.boardRuntimeId = boardRuntimeId;
		}

		public BoardRuntimeIdModel withBoardRuntimeId(String boardRuntimeId) {
			setBoardRuntimeId(boardRuntimeId);

			return this;
		}
	}

	public static class RegisterBoardRequest implements Serializable {
		private static final long serialVersionUID = 4634463946807150174L;

		private String boardId;

		public String getBoardId() {
			return boardId;
		}

		public void setBoardId(String boardId) {
			this.boardId = boardId;
		}
	}

	public static class RegisterBoardResponse implements Serializable {
		private static final long serialVersionUID = 7068318146127671833L;

		private String boardRuntimeId;
		private String boardId;
		private String boardName;
		private String boardDescription;
		private Object boardLayout;

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

		public Object getBoardLayout() {
			return boardLayout;
		}

		public void setBoardLayout(Object boardLayout) {
			this.boardLayout = boardLayout;
		}

		public RegisterBoardResponse withBoardRuntimeId(String boardRuntimeId) {
			setBoardRuntimeId(boardRuntimeId);

			return this;
		}

		public RegisterBoardResponse withBoardId(String boardId) {
			setBoardId(boardId);

			return this;
		}

		public RegisterBoardResponse withBoardName(String boardName) {
			setBoardName(boardName);

			return this;
		}

		public RegisterBoardResponse withBoardDescription(String boardDescription) {
			setBoardDescription(boardDescription);

			return this;
		}

		public RegisterBoardResponse withBoardLayout(Object boardLayout) {
			setBoardLayout(boardLayout);

			return this;
		}
	}

	public static class RuntimeConfigurationPatch implements Serializable {
		private static final long serialVersionUID = 6255133021675703337L;

		private Map<String, String> properties;
		
		public RuntimeConfigurationPatch() {
			super();
		}

		public Map<String, String> getProperties() {
			return properties;
		}

		public void setProperties(Map<String, String> properties) {
			this.properties = properties;
		}

		@Override
		public String toString() {
			return "RuntimeConfigurationPatch [properties=" + properties + "]";
		}
	}
	
	public static class OpenBoardRequest implements Serializable {
		private static final long serialVersionUID = 6255133021675703336L;

		private String boardRuntimeId;
		private RuntimeConfigurationPatch configurationPatch;

		public String getBoardRuntimeId() {
			return boardRuntimeId;
		}
		public void setBoardRuntimeId(String boardRuntimeId) {
			this.boardRuntimeId = boardRuntimeId;
		}
		public RuntimeConfigurationPatch getConfigurationPatch() {
			return configurationPatch;
		}
		public void setConfigurationPatch(RuntimeConfigurationPatch configurationPatch) {
			this.configurationPatch = configurationPatch;
		}
		@Override
		public String toString() {
			return "OpenBoardRequest [boardRuntimeId=" + boardRuntimeId + ", configurationPatch=" + configurationPatch
					+ "]";
		}		

	}

	public static class OpenBoardResponse implements Serializable {
		private static final long serialVersionUID = -8250791751987549694L;

		private String boardRuntimeId;
		private String boardId;

		public String getBoardRuntimeId() {
			return boardRuntimeId;
		}

		public void setRuntimeBoard(String boardRuntimeId) {
			this.boardRuntimeId = boardRuntimeId;
		}

		public String getBoardId() {
			return boardId;
		}

		public void setBoardId(String boardId) {
			this.boardId = boardId;
		}

		public OpenBoardResponse withRuntimeBoardId(String runtimeBoardId) {
			setRuntimeBoard(runtimeBoardId);

			return this;
		}

		public OpenBoardResponse withBoardId(String boardId) {
			setBoardId(boardId);

			return this;
		}
	}

	public static class AddWidgetRequest implements Serializable {
		private static final long serialVersionUID = 279976825148634478L;

		private String widgetId;

		public String getWidgetId() {
			return widgetId;
		}

		public void setWidgetId(String widgetId) {
			this.widgetId = widgetId;
		}
	}

	public static class AddWidgetResponse implements Serializable {
		private static final long serialVersionUID = -6922413020879356480L;
	}

	public static class RemoveWidgetRequest implements Serializable {
		private static final long serialVersionUID = -539116736163224336L;

		private String widgetRuntimeId;

		public String getWidgetRuntimeId() {
			return widgetRuntimeId;
		}

		public void setWidgetRuntimeId(String widgetRuntimeId) {
			this.widgetRuntimeId = widgetRuntimeId;
		}
	}

	public static class RemoveWidgetResponse implements Serializable {
		private static final long serialVersionUID = 2185096346993570713L;
	}

	public static class CloseBoardRequest implements Serializable {
		private static final long serialVersionUID = -6635993965376684824L;

		private String boardRuntimeId;

		public String getBoardRuntimeId() {
			return boardRuntimeId;
		}

		public void setBoardRuntimeId(String boardRuntimeId) {
			this.boardRuntimeId = boardRuntimeId;
		}
	}

	public static class CloseBoardResponse implements Serializable {
		private static final long serialVersionUID = -9197391764422297326L;

		private String boardRuntimeId;
		private String boardId;

		public String getBoardRuntimeId() {
			return boardRuntimeId;
		}

		public void setRuntimeBoard(String boardRuntimeId) {
			this.boardRuntimeId = boardRuntimeId;
		}

		public String getBoardId() {
			return boardId;
		}

		public void setBoardId(String boardId) {
			this.boardId = boardId;
		}

		public CloseBoardResponse withBoardRuntimeId(String boardRuntimeId) {
			setRuntimeBoard(boardRuntimeId);

			return this;
		}

		public CloseBoardResponse withBoardId(String boardId) {
			setBoardId(boardId);

			return this;
		}
	}

	public static class UnregisterBoardRequest implements Serializable {
		private static final long serialVersionUID = -3252331545476656628L;

		private String boardRuntimeId;

		public String getBoardRuntimeId() {
			return boardRuntimeId;
		}

		public void setBoardRuntimeId(String boardRuntimeId) {
			this.boardRuntimeId = boardRuntimeId;
		}
	}

	public static class UnregisterBoardResponse implements Serializable {
		private static final long serialVersionUID = 1376913315081065328L;

		private String boardRuntimeId;
		private String boardId;

		public String getBoardRuntimeId() {
			return boardRuntimeId;
		}

		public void setRuntimeBoard(String boardRuntimeId) {
			this.boardRuntimeId = boardRuntimeId;
		}

		public String getBoardId() {
			return boardId;
		}

		public void setBoardId(String boardId) {
			this.boardId = boardId;
		}

		public UnregisterBoardResponse withRuntimeBoardId(String runtimeBoardId) {
			setRuntimeBoard(runtimeBoardId);

			return this;
		}

		public UnregisterBoardResponse withBoardId(String boardId) {
			setBoardId(boardId);

			return this;
		}
	}

	public static class BoardRuntimeModel implements Serializable {
		private static final long serialVersionUID = -5596732248156187639L;

		private String boardRuntimeId;
		private String boardId;
		private List<String> runtimeWidgetIds = new ArrayList<>();

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

		public List<String> getRuntimeWidgetIds() {
			return runtimeWidgetIds;
		}

		public void setRuntimeWidgetIds(List<String> runtimeWidgetIds) {
			if (runtimeWidgetIds != null)
				this.runtimeWidgetIds = runtimeWidgetIds;
		}

		public BoardRuntimeModel withBoardRuntimeId(String boardRuntimeId) {
			setBoardRuntimeId(boardRuntimeId);

			return this;
		}

		public BoardRuntimeModel withBoardId(String boardId) {
			setBoardId(boardId);

			return this;
		}

		public BoardRuntimeModel withRuntimeWidgetIds(List<String> runtimeWidgetIds) {
			setRuntimeWidgetIds(runtimeWidgetIds);

			return this;
		}
	}

	public static class BoardSizesModel implements Serializable {
		private static final long serialVersionUID = 3207660566103636091L;

		private int boardSize;
		private List<WidgetPositionModel> widgetPositions;

		public BoardSizesModel() {
		}

		public int getBoardSize() {
			return boardSize;
		}

		public void setBoardSize(int boardSize) {
			this.boardSize = boardSize;
		}

		public List<WidgetPositionModel> getWidgetPositions() {
			return widgetPositions;
		}

		public void setWidgetPositions(List<WidgetPositionModel> widgetPositions) {
			this.widgetPositions = widgetPositions;
		}

		@Override
		public String toString() {
			return "BoardSizes [boardSize=" + boardSize + ", widgetPositions=" + widgetPositions + "]";
		}
	}

	public static class WidgetPositionModel implements Serializable {
		private static final long serialVersionUID = 4858182545275999827L;

		private String id;
		private int row;
		private int col;
		private int sizeX;
		private int sizeY;

		public WidgetPositionModel() {
		}

		public WidgetPositionModel(String id, int row, int col, int sizeX, int sizeY) {
			this.id = id;
			this.row = row;
			this.col = col;
			this.sizeX = sizeX;
			this.sizeY = sizeY;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getCol() {
			return col;
		}

		public void setCol(int col) {
			this.col = col;
		}

		public int getSizeX() {
			return sizeX;
		}

		public void setSizeX(int sizeX) {
			this.sizeX = sizeX;
		}

		public int getSizeY() {
			return sizeY;
		}

		public void setSizeY(int sizeY) {
			this.sizeY = sizeY;
		}

		@Override
		public String toString() {
			return "WidgetPosition [id=" + id + ", row=" + row + ", col=" + col + ", sizeX=" + sizeX + ", sizeY="
			        + sizeY + "]";
		}
	}
}
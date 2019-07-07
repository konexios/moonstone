package com.arrow.dashboard.web.model;

import com.arrow.pegasus.dashboard.data.Board;
import com.arrow.pegasus.dashboard.data.Container;
import com.arrow.pegasus.dashboard.data.Widget;
import com.arrow.pegasus.dashboard.data.WidgetType;

public class ConvertModelUtils {

	public static Board toBoard(BoardModels.CreateBoardModel model) {

		if (model == null)
			return null;

		Board board = new Board();
		board.setApplicationId(model.getApplicationId());
		board.setCategory(model.getCategory());
		board.setDescription(model.getDescription());
		board.setName(model.getName());
		board.setProductId(model.getProductId());
		board.setUserId(model.getUserId());

		return board;
	}

	public static Board toBoard(BoardModels.UpdateBoardModel model, Board board) {

		if (board == null || model == null)
			return null;

		board.setApplicationId(model.getApplicationId());
		board.setCategory(model.getCategory());
		board.setDescription(model.getDescription());
		board.setName(model.getName());
		board.setProductId(model.getProductId());
		board.setUserId(model.getUserId());

		return board;
	}

	public static BoardModels.ReadBoardModel toReadBoard(Board board) {

		if (board == null)
			return null;

		BoardModels.ReadBoardModel model = new BoardModels.ReadBoardModel();

		model.setApplicationId(board.getApplicationId());
		model.setCategory(board.getCategory());
		model.setCreatedBy(board.getCreatedBy());
		model.setCreatedDate(board.getCreatedDate());
		model.setDescription(board.getDescription());
		model.setId(board.getId());
		model.setLastModifiedBy(board.getLastModifiedBy());
		model.setLastModifiedDate(board.getLastModifiedDate());
		model.setName(board.getName());
		model.setProductId(board.getProductId());
		model.setUserId(board.getUserId());

		return model;
	}

	// **** CONTAINER **** //

	public static Container toContainer(ContainerModels.CreateContainerModel model) {

		if (model == null)
			return null;

		Container container = new Container();

		container.setBoardId(model.getBoardId());
		container.setDescription(model.getDescription());
		container.setLayout(model.getLayout());
		container.setName(model.getName());

		return container;
	}

	public static Container toContainer(ContainerModels.UpdateContainerModel model, Container container) {

		if (container == null || model == null)
			return null;

		container.setBoardId(model.getBoardId());
		container.setDescription(model.getDescription());
		container.setLayout(model.getLayout());
		container.setName(model.getName());

		return container;
	}

	public static ContainerModels.ReadContainerModel toReadContainer(Container container) {

		if (container == null)
			return null;

		ContainerModels.ReadContainerModel model = new ContainerModels.ReadContainerModel();

		model.setBoardId(container.getBoardId());
		model.setCreatedBy(container.getCreatedBy());
		model.setCreatedDate(container.getCreatedDate());
		model.setDescription(container.getDescription());
		model.setId(container.getId());
		model.setLastModifiedBy(container.getLastModifiedBy());
		model.setLastModifiedDate(container.getLastModifiedDate());
		model.setLayout(container.getLayout());
		model.setName(container.getName());

		return model;
	}

	// **** WIDGET TYPE **** //

	public static WidgetTypeModels.ReadWidgetTypeModel toReadWidgetTypeModel(WidgetType widgetType) {

		if (widgetType == null)
			return null;

		WidgetTypeModels.ReadWidgetTypeModel model = new WidgetTypeModels.ReadWidgetTypeModel();
		model.setClassName(widgetType.getClassName());
		model.setCreatedBy(widgetType.getCreatedBy());
		model.setCreatedDate(widgetType.getCreatedDate());
		model.setDescription(widgetType.getDescription());
		model.setDirective(widgetType.getDirective());
		model.setEnabled(widgetType.isEnabled());
		model.setId(widgetType.getId());
		model.setLastModifiedBy(widgetType.getLastModifiedBy());
		model.setLastModifiedDate(widgetType.getLastModifiedDate());
		model.setName(widgetType.getName());

		return model;
	}

	// **** WIDGET **** //

	public static Widget toWidget(WidgetModels.CreateWidgetModel model) {

		if (model == null)
			return null;

		Widget widget = new Widget();
		widget.setDescription(model.getDescription());
		widget.setLayout(model.getLayout());
		widget.setName(model.getName());
		widget.setParentId(model.getParentId());
		widget.setWidgetTypeId(model.getWidgetTypeId());

		return widget;
	}

	public static Widget toWidget(WidgetModels.UpdateWidgetModel model, Widget widget) {

		if (widget == null || model == null)
			return null;

		widget.setDescription(model.getDescription());
		widget.setLayout(model.getLayout());
		widget.setName(model.getName());
		widget.setParentId(model.getParentId());
		widget.setWidgetTypeId(model.getWidgetTypeId());

		return widget;
	}

	public static WidgetModels.ReadWidgetModel toReadWidget(Widget widget) {

		if (widget == null)
			return null;

		WidgetModels.ReadWidgetModel model = new WidgetModels.ReadWidgetModel();

		model.setCreatedBy(widget.getCreatedBy());
		model.setCreatedDate(widget.getCreatedDate());
		model.setDescription(widget.getDescription());
		model.setId(widget.getId());
		model.setLastModifiedBy(widget.getLastModifiedBy());
		model.setLastModifiedDate(widget.getLastModifiedDate());
		model.setLayout(widget.getLayout());
		model.setName(widget.getName());
		model.setParentId(widget.getParentId());
		model.setWidgetTypeId(widget.getWidgetTypeId());

		return model;
	}
}

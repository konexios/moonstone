package com.arrow.apollo.web.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

import com.arrow.apollo.data.ApolloBoardCategory;
import com.arrow.apollo.data.ApolloWidget;
import com.arrow.dashboard.runtime.model.GridsterGridOptions;
import com.arrow.pegasus.dashboard.data.Board;
import com.arrow.pegasus.dashboard.data.WidgetType;

import moonstone.acs.JsonUtils;

public class ApolloModelUtil {

	public static List<ApolloWidgetModels.ApolloWidgetModel> toApolloWidgetModels(
	        List<ApolloWidget> apolloWidgetTypes) {
		if (apolloWidgetTypes.isEmpty())
			return Collections.emptyList();

		List<ApolloWidgetModels.ApolloWidgetModel> models = new ArrayList<>();
		for (ApolloWidget widgetType : apolloWidgetTypes)
			models.add(toApolloWidgetModel(widgetType));

		return models;
	}

	public static ApolloWidgetModels.ApolloWidgetModel toApolloWidgetModel(ApolloWidget apolloWidget) {
		if (apolloWidget == null)
			return null;

		ApolloWidgetModels.ApolloWidgetModel model = new ApolloWidgetModels.ApolloWidgetModel(apolloWidget.getId(),
		        apolloWidget.getName(), apolloWidget.getDescription());
		model.setCategory(apolloWidget.getCategory());
		model.setIcon(apolloWidget.getIcon());
		model.setIconType(apolloWidget.getIconType());
		model.setWidgetTypeId(apolloWidget.getWidgetTypeId());
		model.setEnabled(apolloWidget.isEnabled());

		return model;
	}

	public static ApolloWidgetModels.ApolloWidgetListModel toApolloWidgetListModel(ApolloWidget apolloWidgetType) {
		if (apolloWidgetType == null)
			return null;

		ApolloWidgetModels.ApolloWidgetListModel model = new ApolloWidgetModels.ApolloWidgetListModel(
		        apolloWidgetType.getId(), apolloWidgetType.getName(), apolloWidgetType.getDescription());
		model.setCategory(apolloWidgetType.getCategory());
		model.setIcon(apolloWidgetType.getIcon());
		model.setIconType(apolloWidgetType.getIconType());
		model.setEnabled(apolloWidgetType.isEnabled());

		return model;
	}

	public static List<WidgetTypeModels.WidgetTypeModel> toWidgetTypeModels(List<WidgetType> widgetTypes) {
		if (widgetTypes.isEmpty())
			return Collections.emptyList();

		List<WidgetTypeModels.WidgetTypeModel> models = new ArrayList<>();
		for (WidgetType widgetType : widgetTypes)
			models.add(toWidgetTypeModel(widgetType));

		return models;
	}

	public static WidgetTypeModels.WidgetTypeModel toWidgetTypeModel(WidgetType widgetType) {
		if (widgetType == null)
			return null;

		WidgetTypeModels.WidgetTypeModel model = new WidgetTypeModels.WidgetTypeModel(widgetType.getId(),
		        widgetType.getName(), widgetType.getDescription());
		model.setClassName(widgetType.getClassName());
		model.setDirective(widgetType.getDirective());

		return model;
	}

	public static List<AllBoardsModels.BoardModel> toAllBoardModels(List<Board> boards) {
		if (boards.isEmpty())
			return Collections.emptyList();

		List<AllBoardsModels.BoardModel> models = new ArrayList<AllBoardsModels.BoardModel>();
		for (Board board : boards)
			models.add(toAllBoardModel(board));

		return models;
	}

	public static AllBoardsModels.BoardModel toAllBoardModel(Board board) {

		if (board == null)
			return null;

		AllBoardsModels.BoardModel model = new AllBoardsModels.BoardModel();
		model.setCategory(ApolloBoardCategory.valueOf(board.getCategory()));
		model.setDescription(board.getDescription());
		model.setId(board.getId());
		model.setName(board.getName());
		model.setUserId(board.getUserId());

		// layout
		GridsterGridOptions options = new GridsterGridOptions();
		Object layoutValue = board.getLayoutValue();
		if (layoutValue != null)
			options = (GridsterGridOptions) layoutValue;

		model.setNumberOfColumns(options.getColumns());

		if (board.getRefUser() != null)
			model.setOwner(board.getRefUser().getContact().fullName());
		else if (board.getRefApplication() != null)
			model.setOwner(board.getRefApplication().getName());
		else if (board.getRefProduct() != null)
			model.setOwner(board.getRefProduct().getDescription());
		else
			model.setOwner(null);

		return model;
	}

	public static BoardModels.BoardModel toBoardModel(Board board) {
		if (board == null)
			return null;

		BoardModels.BoardModel model = new BoardModels.BoardModel();
		model.setCategory(ApolloBoardCategory.valueOf(board.getCategory()));
		model.setDescription(board.getDescription());
		model.setName(board.getName());
		model.setUserId(board.getUserId());

		return model;
	}

	public static Board toBoard(BoardModels.CreateBoardModel model) {
		if (model == null)
			return null;

		Board board = new Board();
		board.setCategory(model.getCategory().name());
		board.setDescription(model.getDescription());
		board.setName(model.getName());

		// layout
		GridsterGridOptions options = new GridsterGridOptions();
		Object layoutValue = board.getLayoutValue();
		if (layoutValue != null)
			options = (GridsterGridOptions) layoutValue;

		options.setColumns(model.getNumberOfColumns());

		board.setLayout(JsonUtils.toJson(options));
		board.setLayoutClass(options.getClass().getName());

		return board;
	}

	public static Board toBoard(BoardModels.UpdateBoardModel model, Board board) {
		if (model == null || board == null)
			return null;

		board.setCategory(model.getCategory().name());
		board.setDescription(model.getDescription());
		board.setName(model.getName());

		// layout
		GridsterGridOptions options = new GridsterGridOptions();
		Object layoutValue = board.getLayoutValue();
		if (layoutValue != null)
			options = (GridsterGridOptions) layoutValue;

		options.setColumns(model.getNumberOfColumns());

		board.setLayout(JsonUtils.toJson(options));
		board.setLayoutClass(options.getClass().getName());
		
		return board;
	}

	public static ApolloWidget toApolloWidget(ApolloWidgetModels.ApolloWidgetModel model) {
		return toApolloWidget(model, new ApolloWidget());
	}

	public static ApolloWidget toApolloWidget(ApolloWidgetModels.ApolloWidgetModel model, ApolloWidget widget) {
		// if (model == null || widget == null)
		// return null;

		widget.setName(model.getName());
		widget.setDescription(model.getDescription());
		widget.setWidgetTypeId(model.getWidgetTypeId());
		widget.setCategory(model.getCategory());
		widget.setIconType(model.getIconType());
		widget.setIcon(model.getIcon());
		widget.setEnabled(model.isEnabled());

		Assert.notNull(model, "model is null");
		Assert.hasText(model.getName(), "name is empty");
		Assert.hasText(model.getDescription(), "description is empty");
		Assert.hasText(model.getWidgetTypeId(), "widgetTypeId is empty");
		Assert.notNull(model.getCategory(), "category is null");
		Assert.notNull(model.getIconType(), "iconType is null");
		Assert.hasText(model.getIcon(), "icon is empty");

		Assert.notNull(widget, "widget is null");
		Assert.hasText(widget.getName(), "name is empty");
		Assert.hasText(widget.getDescription(), "description is empty");
		Assert.hasText(widget.getWidgetTypeId(), "widgetTypeId is empty");
		Assert.notNull(widget.getCategory(), "category is null");
		Assert.notNull(widget.getIconType(), "iconType is null");
		Assert.hasText(widget.getIcon(), "icon is empty");

		return widget;
	}

}

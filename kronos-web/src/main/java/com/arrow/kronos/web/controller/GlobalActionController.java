package com.arrow.kronos.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.data.action.GlobalAction;
import com.arrow.kronos.data.action.GlobalActionType;
import com.arrow.kronos.repo.KronosDocumentSearchParams;
import com.arrow.kronos.service.GlobalActionService;
import com.arrow.kronos.web.model.GlobalActionModels.GlobalActionDetailsModel;
import com.arrow.kronos.web.model.GlobalActionTypeModels.GlobalActionTypeOption;
import com.arrow.kronos.web.model.SearchResultModels.GlobalActionSearchResultModel;
import com.arrow.pegasus.webapi.data.CoreSearchFilterModel;

@RestController
@RequestMapping("/api/kronos/globalaction")
public class GlobalActionController extends BaseControllerAbstract {

	@Autowired
	private GlobalActionService globalActionService;

	@PreAuthorize("hasAuthority('KRONOS_VIEW_GLOBAL_ACTIONS')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public GlobalActionSearchResultModel list(@RequestBody CoreSearchFilterModel searchFilter, HttpSession session) {

		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				Sort.by(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		KronosDocumentSearchParams params = new KronosDocumentSearchParams();
		params.addApplicationIds(getApplicationId(session));

		Page<GlobalAction> actions = globalActionService.getGlobalActionRepository().findGlobalActions(pageRequest,
				params);
		List<GlobalActionDetailsModel> actionModels = new ArrayList<>();
		actions.forEach(action -> actionModels.add(buildGlobalActionDetailsModel(action)));

		Page<GlobalActionDetailsModel> result = new PageImpl<>(actionModels, pageRequest, actions.getTotalElements());

		return new GlobalActionSearchResultModel(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('KRONOS_CREATE_GLOBAL_ACTION')")
	@RequestMapping(method = RequestMethod.POST)
	public GlobalActionDetailsModel add(@RequestBody GlobalActionDetailsModel actionModel, HttpSession session) {

		GlobalAction globalAction = new GlobalAction();
		globalAction.setApplicationId(getApplicationId(session));
		globalAction.setDescription(actionModel.getDescription());
		globalAction.setEnabled(actionModel.isEnabled());
		GlobalActionTypeOption globalActionTypeOption = actionModel.getGlobalActionType();
		Assert.notNull(globalActionTypeOption, "globalActionType is required");
		Assert.hasText(globalActionTypeOption.getId(), "globalActionType id empty");
		GlobalActionType globalActionType = getKronosCache().findGlobalActionTypeById(globalActionTypeOption.getId());
		Assert.notNull(globalActionType, "globalActionType is not found");
		globalAction.setGlobalActionTypeId(globalActionTypeOption.getId());
		globalAction.setInput(actionModel.getInput());
		globalAction.setName(actionModel.getName());
		globalAction.setProperties(actionModel.getProperties());
		globalAction.setSystemName(actionModel.getSystemName());

		globalAction = globalActionService.create(globalAction, getUserId());

		return new GlobalActionDetailsModel(globalAction);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_GLOBAL_ACTION')")
	@RequestMapping(value = "/{actionId}", method = RequestMethod.PUT)
	public GlobalActionDetailsModel edit(@PathVariable String actionId,
			@RequestBody GlobalActionDetailsModel actionModel, HttpSession session) {

		GlobalAction globalAction = getKronosCache().findGlobalActionById(actionId);
		Assert.notNull(globalAction, "action is not found");
		Assert.isTrue(getApplicationId(session).equals(globalAction.getApplicationId()), "application mismatch");
		globalAction.setDescription(actionModel.getDescription());
		globalAction.setEnabled(actionModel.isEnabled());
		globalAction.setInput(actionModel.getInput());
		globalAction.setName(actionModel.getName());
		globalAction.setProperties(actionModel.getProperties());
		globalAction.setSystemName(actionModel.getSystemName());

		globalAction = globalActionService.update(globalAction, getUserId());

		return new GlobalActionDetailsModel(globalAction);
	}

	private GlobalActionDetailsModel buildGlobalActionDetailsModel(GlobalAction action) {
		GlobalActionType actionType = getKronosCache().findGlobalActionTypeById(action.getGlobalActionTypeId());
		Assert.notNull(actionType, "actionType is not found");
		GlobalActionDetailsModel result = new GlobalActionDetailsModel(action, actionType);
		result.setLastModifiedBy(getKronosModelUtil().buildLastModifiedBy(action.getLastModifiedBy()));
		return result;
	}
}

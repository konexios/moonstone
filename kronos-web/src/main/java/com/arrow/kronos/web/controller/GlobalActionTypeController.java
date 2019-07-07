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

import com.arrow.kronos.data.action.GlobalActionType;
import com.arrow.kronos.repo.KronosDocumentSearchParams;
import com.arrow.kronos.service.GlobalActionTypeService;
import com.arrow.kronos.web.model.GlobalActionTypeModels.GlobalActionTypeDetailsModel;
import com.arrow.kronos.web.model.SearchResultModels.GlobalActionTypeSearchResultModel;
import com.arrow.pegasus.webapi.data.CoreSearchFilterModel;

@RestController
@RequestMapping("/api/kronos/globalactiontype")
public class GlobalActionTypeController extends BaseControllerAbstract {

	@Autowired
	private GlobalActionTypeService globalActionTypeService;

	@PreAuthorize("hasAuthority('KRONOS_VIEW_GLOBAL_ACTION_TYPES')")
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public GlobalActionTypeSearchResultModel list(@RequestBody CoreSearchFilterModel searchFilter,
			HttpSession session) {

		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				Sort.by(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		KronosDocumentSearchParams params = new KronosDocumentSearchParams();
		params.addApplicationIds(getApplicationId(session));

		Page<GlobalActionType> actionTypes = globalActionTypeService.getGlobalActionTypeRepository()
				.findGlobalActionTypes(pageRequest, params);

		List<GlobalActionTypeDetailsModel> actionTypeModels = new ArrayList<>();
		actionTypes.forEach(actionType -> actionTypeModels.add(buildGlobalActionTypeDetailsModel(actionType)));

		Page<GlobalActionTypeDetailsModel> result = new PageImpl<>(actionTypeModels, pageRequest,
				actionTypes.getTotalElements());

		return new GlobalActionTypeSearchResultModel(result, searchFilter);
	}

	@PreAuthorize("hasAuthority('KRONOS_VIEW_GLOBAL_ACTION_TYPES')")
	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public List<GlobalActionTypeDetailsModel> list(HttpSession session) {

		List<GlobalActionType> actionTypes = globalActionTypeService.getGlobalActionTypeRepository()
				.findByApplicationId(getApplicationId(session));

		List<GlobalActionTypeDetailsModel> actionTypeModels = new ArrayList<>();
		actionTypes.forEach(actionType -> actionTypeModels.add(buildGlobalActionTypeDetailsModel(actionType)));
		return actionTypeModels;
	}

	@PreAuthorize("hasAuthority('KRONOS_CREATE_GLOBAL_ACTION_TYPE')")
	@RequestMapping(method = RequestMethod.POST)
	public GlobalActionTypeDetailsModel add(@RequestBody GlobalActionTypeDetailsModel actionTypeModel,
			HttpSession session) {

		GlobalActionType globalActionType = new GlobalActionType();
		globalActionType.setApplicationId(getApplicationId(session));
		globalActionType.setDescription(actionTypeModel.getDescription());
		if (getAuthenticatedUser().isAdmin()) {
			globalActionType.setEditable(actionTypeModel.isEditable());
		} else {
			globalActionType.setEditable(true);
		}
		globalActionType.setEnabled(actionTypeModel.isEnabled());
		globalActionType.setName(actionTypeModel.getName());
		globalActionType.setParameters(actionTypeModel.getParameters());
		globalActionType.setSystemName(actionTypeModel.getSystemName());

		globalActionType = globalActionTypeService.create(globalActionType, getUserId());

		return new GlobalActionTypeDetailsModel(globalActionType);
	}

	@PreAuthorize("hasAuthority('KRONOS_EDIT_GLOBAL_ACTION_TYPE')")
	@RequestMapping(value = "/{actionTypeId}", method = RequestMethod.PUT)
	public GlobalActionTypeDetailsModel edit(@PathVariable String actionTypeId,
			@RequestBody GlobalActionTypeDetailsModel actionTypeModel, HttpSession session) {
		boolean isAdmin = getAuthenticatedUser().isAdmin();
		GlobalActionType globalActionType = getKronosCache().findGlobalActionTypeById(actionTypeId);
		Assert.notNull(globalActionType, "action type is not found");
		Assert.isTrue(getApplicationId(session).equals(globalActionType.getApplicationId()), "application mismatch");
		Assert.isTrue(isAdmin || globalActionType.isEditable(), "action type is not editable");

		globalActionType.setDescription(actionTypeModel.getDescription());
		if (isAdmin) {
			globalActionType.setEditable(actionTypeModel.isEditable());
		}
		globalActionType.setEnabled(actionTypeModel.isEnabled());
		globalActionType.setName(actionTypeModel.getName());
		globalActionType.setParameters(actionTypeModel.getParameters());
		globalActionType.setSystemName(actionTypeModel.getSystemName());

		globalActionType = globalActionTypeService.update(globalActionType, getUserId());

		return new GlobalActionTypeDetailsModel(globalActionType);
	}

	private GlobalActionTypeDetailsModel buildGlobalActionTypeDetailsModel(GlobalActionType actionType) {
		GlobalActionTypeDetailsModel result = new GlobalActionTypeDetailsModel(actionType);
		result.setLastModifiedBy(getKronosModelUtil().buildLastModifiedBy(actionType.getLastModifiedBy()));
		return result;
	}
}

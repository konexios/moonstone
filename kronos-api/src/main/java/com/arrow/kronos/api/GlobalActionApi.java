package com.arrow.kronos.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import com.arrow.acn.client.model.GlobalActionModel;
import com.arrow.acn.client.model.GlobalActionDetailsModel;
import com.arrow.acn.client.model.GlobalActionInputModel;
import com.arrow.acn.client.model.GlobalActionPropertyModel;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.action.GlobalAction;
import com.arrow.kronos.data.action.GlobalActionInput;
import com.arrow.kronos.data.action.GlobalActionInput.InputType;
import com.arrow.kronos.data.action.GlobalActionProperty;
import com.arrow.kronos.data.action.GlobalActionType;
import com.arrow.kronos.data.action.ParameterValidation;
import com.arrow.kronos.data.action.ParameterValidation.ValidationType;
import com.arrow.kronos.repo.KronosDocumentSearchParams;
import com.arrow.kronos.service.GlobalActionService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acs.AcsLogicalException;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Application;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/kronos/globalactions")
public class GlobalActionApi extends BaseApiAbstract {

	@Autowired
	private GlobalActionService globalActionService;

	@ApiOperation(value = "create new global action", hidden = true)
	@RequestMapping(path = "", method = RequestMethod.POST)
	public HidModel create(
	        @ApiParam(value = "global action model", required = true) @RequestBody(required = false) GlobalActionDetailsModel body,
	        HttpServletRequest request) {
		String method = "create";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());

		GlobalActionDetailsModel actionModel = JsonUtils.fromJson(getApiPayload(), GlobalActionDetailsModel.class);
		Assert.notNull(actionModel, "model is null");

		GlobalAction globalAction = new GlobalAction();
		globalAction.setApplicationId(accessKey.getApplicationId());
		GlobalActionType globalActionType = getKronosCache()
		        .findGlobalActionTypeByHid(actionModel.getGlobalActionTypeHid());
		Assert.notNull(globalActionType, "globalActionType is not found");
		Assert.isTrue(globalActionType.isEnabled(), "globalActionType is disabled");
		globalAction.setGlobalActionTypeId(globalActionType.getId());
		globalAction = buildGlobalAction(globalAction, actionModel);
		globalAction = globalActionService.create(globalAction, accessKey.getId());

		auditLog(method, globalAction.getApplicationId(), globalAction.getId(), accessKey.getId(), request);

		return new HidModel().withHid(globalAction.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "update existing global action", hidden = true)
	@RequestMapping(path = "/{actionHid}", method = RequestMethod.PUT)
	public HidModel update(@ApiParam(value = "global action hid", required = true) @PathVariable String actionHid,
	        @ApiParam(value = "global action model", required = true) @RequestBody(required = false) GlobalActionDetailsModel body,
	        HttpServletRequest request) {
		String method = "update";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());

		GlobalAction globalAction = getKronosCache().findGlobalActionByHid(actionHid);
		Assert.notNull(globalAction, "global action does not exist");
		auditLog(method, globalAction.getApplicationId(), globalAction.getId(), accessKey.getId(), request);
		Assert.isTrue(globalAction.getApplicationId().equals(accessKey.getApplicationId()),
		        "applicationId mismatched!");

		GlobalActionDetailsModel actionModel = JsonUtils.fromJson(getApiPayload(), GlobalActionDetailsModel.class);
		Assert.notNull(actionModel, "model is null");

		GlobalActionType globalActionType = getKronosCache()
		        .findGlobalActionTypeByHid(actionModel.getGlobalActionTypeHid());
		Assert.notNull(globalActionType, "globalActionType is not found");

		if (!globalActionType.getId().equals(globalAction.getGlobalActionTypeId()))
			Assert.isTrue(globalActionType.isEnabled(), "globalActionType is disabled");
		globalAction.setGlobalActionTypeId(globalActionType.getId());

		globalAction = buildGlobalAction(globalAction, actionModel);

		globalAction = globalActionService.update(globalAction, accessKey.getId());

		return new HidModel().withHid(globalAction.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "find global action by hid", response = GlobalActionModel.class, hidden = true)
	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public GlobalActionModel findByHid(@PathVariable(name = "hid") String hid) {
		AccessKey accessKey = validateCanReadApplication(getProductSystemName());
		GlobalAction globalAction = getKronosCache().findGlobalActionByHid(hid);
		Assert.notNull(globalAction, "global action was not found");
		Assert.isTrue(globalAction.getApplicationId().equals(accessKey.getApplicationId()),
		        "applicationId mismatched!");

		return buildGlobalActionModel(globalAction);
	}

	@ApiOperation(value = "find global actions", hidden = true)
	@RequestMapping(path = "", method = RequestMethod.GET)
	public PagingResultModel<GlobalActionModel> findAll(
	        @RequestParam(name = "enabled", required = false) String enabled,
	        @RequestParam(name = "_page", required = false, defaultValue = "0") int page,
	        @RequestParam(name = "_size", required = false, defaultValue = "100") int size) {

		Assert.isTrue(page >= 0, "page must be positive");
		Assert.isTrue(size >= 0 && size <= KronosConstants.PageResult.MAX_SIZE,
		        "size must be between 0 and " + KronosConstants.PageResult.MAX_SIZE);

		AccessKey accessKey = validateCanReadApplication(getProductSystemName());

		PagingResultModel<GlobalActionModel> result = new PagingResultModel<>();
		result.setPage(page);
		PageRequest pageRequest = PageRequest.of(page, size);

		KronosDocumentSearchParams params = new KronosDocumentSearchParams();
		params.addApplicationIds(accessKey.getApplicationId());

		if (StringUtils.isNotEmpty(enabled))
			params.setEnabled(enabled.equalsIgnoreCase(Boolean.TRUE.toString()));

		Page<GlobalAction> actions = globalActionService.getGlobalActionRepository().findGlobalActions(pageRequest,
		        params);

		List<GlobalActionModel> data = actions.getContent().stream().map(action -> buildGlobalActionModel(action))
		        .collect(Collectors.toCollection(ArrayList::new));

		result.withTotalPages(actions.getTotalPages()).withTotalSize(actions.getTotalElements())
		        .withSize(actions.getNumberOfElements()).withData(data);
		return result;
	}

	private List<GlobalActionProperty> buildGlobalActionProperties(List<GlobalActionPropertyModel> properties,
	        GlobalAction globalAction) {
		List<GlobalActionProperty> globalActionProperties = new ArrayList<>();
		GlobalActionType globalActionType = getKronosCache()
		        .findGlobalActionTypeById(globalAction.getGlobalActionTypeId());
		Assert.notNull(globalActionType, "globalActionType was not found");

		List<String> nameRequiredList = globalActionType.getParameters().stream()
		        .filter(parameter -> parameter.isRequired()).map(parameter -> parameter.getName())
		        .collect(Collectors.toList());
		List<String> namesFromModel = properties.stream().map(parameter -> parameter.getParameterName())
		        .collect(Collectors.toList());
		nameRequiredList.removeAll(namesFromModel);
		Assert.isTrue(nameRequiredList.size() == 0, "Model must contain all required parameters of global action type");

		Map<String, Set<ValidationType>> nameTypesList = new HashMap<>();
		globalActionType.getParameters().forEach(parameter -> {
			nameTypesList.put(parameter.getName(), parameter.getValidationTypes().stream()
			        .map(ParameterValidation::getType).collect(Collectors.toSet()));
		});

		properties.forEach(propertyModel -> {
			Assert.hasText(propertyModel.getParameterName(), "name of property is null");
			Assert.hasText(propertyModel.getParameterType(), "type of property is null");
			Assert.hasText(propertyModel.getParameterValue(), "value of property is null");
			GlobalActionProperty property = new GlobalActionProperty();
			if (nameTypesList.get(propertyModel.getParameterName()) != null
			        && nameTypesList.get(propertyModel.getParameterName())
			                .contains(ValidationType.valueOf(propertyModel.getParameterType()))) {
				property.setParameterName(propertyModel.getParameterName());
				property.setParameterType(propertyModel.getParameterType());
				property.setParameterValue(propertyModel.getParameterValue());
				globalActionProperties.add(property);
			} else {
				throw new AcsLogicalException("global action type doesn't contain the parameter with name: "
				        + propertyModel.getParameterName() + " and type: " + propertyModel.getParameterType());
			}
		});
		return globalActionProperties;
	}

	private List<GlobalActionInput> buildGlobalActionInput(List<GlobalActionInputModel> inputs) {
		List<GlobalActionInput> globalActionInput = new ArrayList<>();
		inputs.forEach(inputModel -> {
			Assert.hasText(inputModel.getName(), "name of input is null");
			Assert.notNull(inputModel.getType(), "type of input is null");
			GlobalActionInput input = new GlobalActionInput();
			input.setName(inputModel.getName());
			input.setRequired(inputModel.isRequired());
			input.setType(InputType.valueOf(inputModel.getType()));
			globalActionInput.add(input);
		});
		return globalActionInput;
	}

	private GlobalAction buildGlobalAction(GlobalAction globalAction, GlobalActionDetailsModel model) {

		if (globalAction == null) {
			globalAction = new GlobalAction();
		}
		Assert.notNull(model.getSystemName(), "SystemName is null");

		globalAction.setDescription(model.getDescription());
		globalAction.setEnabled(model.isEnabled());
		globalAction.setInput(buildGlobalActionInput(model.getInput()));
		globalAction.setName(model.getName());
		globalAction.setProperties(buildGlobalActionProperties(model.getProperties(), globalAction));
		globalAction.setSystemName(model.getSystemName());

		return globalAction;
	}

	private GlobalActionModel buildGlobalActionModel(GlobalAction globalAction) {
		Assert.notNull(globalAction, "globalAction is null");
		GlobalActionModel result = buildModel(new GlobalActionModel(), globalAction);

		Application application = getCoreCacheService().findApplicationById(globalAction.getApplicationId());
		Assert.notNull(application, "application is not found");
		GlobalActionType globalActionType = getKronosCache()
		        .findGlobalActionTypeById(globalAction.getGlobalActionTypeId());
		if (globalActionType != null)
			result.setGlobalActionTypeHid(globalActionType.getHid());
		result.setApplicationHid(application.getHid());
		result.setSystemName(globalAction.getSystemName());
		if (globalAction.getInput() != null) {
			result.setInput(buildGlobalActionInputModel(globalAction.getInput()));
		}
		if (globalAction.getProperties() != null) {
			result.setProperties(buildGlobalActionPropertiesModel(globalAction.getProperties()));
		}
		return result;
	}

	private List<GlobalActionPropertyModel> buildGlobalActionPropertiesModel(List<GlobalActionProperty> properties) {
		List<GlobalActionPropertyModel> result = new ArrayList<>();
		properties.forEach(property -> {
			result.add(new GlobalActionPropertyModel().withParameterName(property.getParameterName())
			        .withParameterType(property.getParameterType()).withParameterValue(property.getParameterValue()));
		});
		return result;
	}

	private List<GlobalActionInputModel> buildGlobalActionInputModel(List<GlobalActionInput> inputs) {
		List<GlobalActionInputModel> result = new ArrayList<>();
		inputs.forEach(input -> {
			result.add(new GlobalActionInputModel().withName(input.getName()).withType(input.getType().toString())
			        .withRequired(input.isRequired()));
		});
		return result;
	}

}

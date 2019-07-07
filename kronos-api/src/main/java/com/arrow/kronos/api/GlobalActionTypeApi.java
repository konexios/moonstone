package com.arrow.kronos.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.arrow.acn.client.model.GlobalActionTypeDetailsModel;
import com.arrow.acn.client.model.GlobalActionTypeModel;
import com.arrow.acn.client.model.GlobalActionTypeParameterModel;
import com.arrow.acn.client.model.ParameterValidationModel;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.action.GlobalActionType;
import com.arrow.kronos.data.action.GlobalActionTypeParameter;
import com.arrow.kronos.data.action.ParameterValidation;
import com.arrow.kronos.data.action.ParameterValidation.ValidationType;
import com.arrow.kronos.repo.KronosDocumentSearchParams;
import com.arrow.kronos.service.GlobalActionTypeService;

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
@RequestMapping("/api/v1/kronos/globalaction/types")
public class GlobalActionTypeApi extends BaseApiAbstract {

	@Autowired
	private GlobalActionTypeService globalActionTypeService;

	@ApiOperation(value = "find global action types", hidden = true)
	@RequestMapping(path = "", method = RequestMethod.GET)
	public PagingResultModel<GlobalActionTypeModel> findAll(
	        @RequestParam(name = "enabled", required = false) String enabled,
	        @RequestParam(name = "_page", required = false, defaultValue = "0") int page,
	        @RequestParam(name = "_size", required = false, defaultValue = "100") int size) {

		Assert.isTrue(page >= 0, "page must be positive");
		Assert.isTrue(size >= 0 && size <= KronosConstants.PageResult.MAX_SIZE,
		        "size must be between 0 and " + KronosConstants.PageResult.MAX_SIZE);

		AccessKey accessKey = validateCanReadApplication(getProductSystemName());

		PagingResultModel<GlobalActionTypeModel> result = new PagingResultModel<>();
		result.setPage(page);
		PageRequest pageRequest = PageRequest.of(page, size);

		KronosDocumentSearchParams params = new KronosDocumentSearchParams();
		params.addApplicationIds(accessKey.getApplicationId());

		if (StringUtils.isNotEmpty(enabled))
			params.setEnabled(enabled.equalsIgnoreCase(Boolean.TRUE.toString()));

		Page<GlobalActionType> actionTypes = globalActionTypeService.getGlobalActionTypeRepository()
		        .findGlobalActionTypes(pageRequest, params);
		List<GlobalActionTypeModel> data = actionTypes.getContent().stream()
		        .map(actionType -> buildGlobalActionTypeModel(actionType))
		        .collect(Collectors.toCollection(ArrayList::new));

		result.withTotalPages(actionTypes.getTotalPages()).withTotalSize(actionTypes.getTotalElements())
		        .withSize(actionTypes.getNumberOfElements()).withData(data);
		return result;
	}

	@ApiOperation(value = "find global action type by hid", response = GlobalActionTypeModel.class, hidden = true)
	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public GlobalActionTypeModel findByHid(@PathVariable(name = "hid") String hid) {
		AccessKey accessKey = validateCanReadApplication(getProductSystemName());
		GlobalActionType globalActionType = globalActionTypeService.getGlobalActionTypeRepository().doFindByHid(hid);
		Assert.notNull(globalActionType, "globalActionType is not found");
		Assert.isTrue(accessKey.getApplicationId().equals(globalActionType.getApplicationId()), "application mismatch");
		return buildGlobalActionTypeModel(globalActionType);
	}

	@ApiOperation(value = "create new global action Type", response = HidModel.class, hidden = true)
	@RequestMapping(path = "", method = RequestMethod.POST)
	public HidModel create(
	        @ApiParam(value = "global action type model", required = true) @RequestBody(required = false) GlobalActionTypeDetailsModel body,
	        HttpServletRequest request) {
		String method = "create";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());

		GlobalActionTypeDetailsModel model = JsonUtils.fromJson(getApiPayload(), GlobalActionTypeDetailsModel.class);

		GlobalActionType globalActionType = buildGlobalActionType(new GlobalActionType(), model);
		globalActionType.setApplicationId(accessKey.getApplicationId());
		globalActionType.setParameters(buildParameters(model.getParameters()));

		globalActionType = globalActionTypeService.create(globalActionType, accessKey.getId());

		auditLog(method, globalActionType.getApplicationId(), globalActionType.getId(), accessKey.getId(), request);

		return new HidModel().withHid(globalActionType.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "update existing global action type", response = HidModel.class, hidden = true)
	@RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
	public HidModel update(
	        @ApiParam(value = "global action type hid", required = true) @PathVariable(name = "hid") String hid,
	        @ApiParam(value = "global action type model", required = true) @RequestBody(required = false) GlobalActionTypeDetailsModel body,
	        HttpServletRequest request) {
		String method = "update";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());
		GlobalActionTypeDetailsModel model = JsonUtils.fromJson(getApiPayload(), GlobalActionTypeDetailsModel.class);

		GlobalActionType globalActionType = globalActionTypeService.getGlobalActionTypeRepository().doFindByHid(hid);
		Assert.notNull(globalActionType, "globalActionType is not found");
		Assert.isTrue(accessKey.getApplicationId().equals(globalActionType.getApplicationId()), "application mismatch");
		Assert.isTrue(globalActionType.isEditable(), "action type is not editable");

		auditLog(method, globalActionType.getApplicationId(), globalActionType.getId(), accessKey.getId(), request);

		globalActionType = buildGlobalActionType(globalActionType, model);
		globalActionType.setParameters(updateParameters(model.getParameters()));

		globalActionType = globalActionTypeService.update(globalActionType, accessKey.getId());

		return new HidModel().withHid(globalActionType.getHid()).withMessage("OK");
	}

	private GlobalActionTypeModel buildGlobalActionTypeModel(GlobalActionType globalActionType) {
		Assert.notNull(globalActionType, "globalActionType is null");
		GlobalActionTypeModel globalActionTypeModel = buildModel(new GlobalActionTypeModel(), globalActionType);
		globalActionTypeModel.setEditable(globalActionType.isEditable());
		globalActionTypeModel.setSystemName(globalActionType.getSystemName());

		Application application = getCoreCacheService().findApplicationById(globalActionType.getApplicationId());
		Assert.notNull(application, "application is not found");
		globalActionTypeModel.setApplicationHid(application.getHid());
		globalActionTypeModel.setParameters(buildParametersModel(globalActionType.getParameters()));
		return globalActionTypeModel;
	}

	private List<GlobalActionTypeParameterModel> buildParametersModel(List<GlobalActionTypeParameter> parameters) {
		List<GlobalActionTypeParameterModel> result = new ArrayList<>();
		parameters.stream().forEach(parameter -> {
			GlobalActionTypeParameterModel model = new GlobalActionTypeParameterModel();
			model.setName(parameter.getName());
			model.setDescription(parameter.getDescription());
			model.setOrder(parameter.getOrder());
			model.setRequired(parameter.isRequired());
			Set<ParameterValidationModel> validationTypeModels = new HashSet<>();
			parameter.getValidationTypes().stream().forEach(type -> {
				ParameterValidationModel parameterValidationModel = new ParameterValidationModel();
				parameterValidationModel.setData(type.getData());
				parameterValidationModel.setDefaultValue(type.getDefaultValue());
				parameterValidationModel
				        .setType(ParameterValidationModel.ValidationType.valueOf(type.getType().toString()));
				validationTypeModels.add(parameterValidationModel);
			});
			model.setValidationTypes(validationTypeModels);
			result.add(model);
		});
		return result;
	}

	private GlobalActionType buildGlobalActionType(GlobalActionType globalActionType,
	        GlobalActionTypeDetailsModel model) {

		if (globalActionType == null) {
			globalActionType = new GlobalActionType();
		}
		Assert.notNull(model.getSystemName(), "SystemName is null");

		globalActionType.setDescription(model.getDescription());
		globalActionType.setEditable(model.isEditable());
		globalActionType.setEnabled(model.isEnabled());
		globalActionType.setName(model.getName());
		globalActionType.setSystemName(model.getSystemName());

		return globalActionType;
	}

	private List<GlobalActionTypeParameter> buildParameters(List<GlobalActionTypeParameterModel> parameters) {
		List<GlobalActionTypeParameter> result = new ArrayList<>();
		if (parameters != null) {
			parameters.stream().forEach(parameterModel -> {
				GlobalActionTypeParameter parameter = new GlobalActionTypeParameter();
				Assert.hasText(parameterModel.getName(), "name in parameter model is null");
				parameter.setName(parameterModel.getName());
				parameter.setDescription(parameterModel.getDescription());
				parameter.setOrder(parameterModel.getOrder());
				parameter.setRequired(parameterModel.isRequired());
				Set<ParameterValidation> validationTypes = new HashSet<>();
				parameterModel.getValidationTypes().stream().forEach(typeModel -> {
					ParameterValidation parameterValidation = new ParameterValidation();
					parameterValidation.setData(typeModel.getData());
					parameterValidation.setDefaultValue(typeModel.getDefaultValue());
					parameterValidation.setType(ValidationType.valueOf(typeModel.getType().toString()));
					validationTypes.add(parameterValidation);
				});
				parameter.setValidationTypes(validationTypes);
				result.add(parameter);
			});
		}
		return result;
	}

	private List<GlobalActionTypeParameter> updateParameters(List<GlobalActionTypeParameterModel> parameters) {

		// check duplicates of ids
		List<String> parametersNameList = parameters.stream().map(s -> s.getName()).collect(Collectors.toList());
		Set<String> parametersNameListWithoutDuplicates = new HashSet<>(parametersNameList);
		if (parametersNameList.size() != parametersNameListWithoutDuplicates.size()) {
			throw new AcsLogicalException("duplicated global action type parameter names");
		}

		return buildParameters(parameters);
	}
}

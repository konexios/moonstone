package com.arrow.kronos.service;

import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.action.GlobalActionType;
import com.arrow.kronos.data.action.GlobalActionTypeParameter;
import com.arrow.kronos.data.action.ParameterValidation;
import com.arrow.kronos.repo.GlobalActionTypeRepository;
import com.arrow.pegasus.data.AuditLogBuilder;

import moonstone.acs.AcsLogicalException;

@Service
public class GlobalActionTypeService extends KronosServiceAbstract {

	private static final Pattern PARAMETER_PATTERN = Pattern.compile("^\\w([\\w\\s])*\\w$");

	@Autowired
	private GlobalActionTypeRepository globalActionTypeRepository;

	public GlobalActionTypeRepository getGlobalActionTypeRepository() {
		return globalActionTypeRepository;
	}

	public GlobalActionType create(GlobalActionType globalActionType, String who) {
		String method = "create";
		logInfo(method, "...");

		// validate
		Assert.hasText(who, "who is empty");
		validateGlobalActionType(globalActionType);
		globalActionType.getParameters().forEach(this::validateGlobalActionTypeParameter);
		GlobalActionType type = globalActionTypeRepository.findByApplicationIdAndSystemName(
		        globalActionType.getApplicationId(), globalActionType.getSystemName());
		if (type != null) {
			throw new AcsLogicalException("systemName already exists");
		}

		// create
		globalActionType = globalActionTypeRepository.doInsert(globalActionType, who);

		// audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.GlobalActionType.CreateGlobalActionType)
		        .applicationId(globalActionType.getApplicationId()).objectId(globalActionType.getId()).by(who)
		        .parameter("systemName", globalActionType.getSystemName())
		        .parameter("name", globalActionType.getName()));

		return globalActionType;
	}

	public GlobalActionType update(GlobalActionType globalActionType, String who) {
		String method = "update";
		logInfo(method, "...");

		// validate
		Assert.hasText(who, "who is empty");
		Assert.hasText(globalActionType.getId(), "id is empty");
		Assert.hasText(globalActionType.getHid(), "hid is empty");
		validateGlobalActionType(globalActionType);
		GlobalActionType type = getKronosCache().findGlobalActionTypeBySystemName(globalActionType.getApplicationId(),
		        globalActionType.getSystemName());
		Assert.isTrue(type == null || type.getId().equals(globalActionType.getId()), "systemName already exists");
		type = getKronosCache().findGlobalActionTypeById(globalActionType.getId());
		Assert.notNull(type, "global action type is not found");
		globalActionType.getParameters().forEach(param -> validateGlobalActionTypeParameter(param));

		// update
		globalActionType = globalActionTypeRepository.doSave(globalActionType, who);

		// audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.GlobalActionType.UpdateGlobalActionType)
		        .applicationId(globalActionType.getApplicationId()).objectId(globalActionType.getId()).by(who)
		        .parameter("systemName", globalActionType.getSystemName())
		        .parameter("name", globalActionType.getName()));

		// clear cache
		GlobalActionType cachedGlobalActionType = getKronosCache().findGlobalActionTypeById(globalActionType.getId());
		if (cachedGlobalActionType != null) {
			getKronosCache().clearGlobalActionType(cachedGlobalActionType);
		}

		return globalActionType;
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return globalActionTypeRepository.deleteByApplicationId(applicationId);
	}

	private GlobalActionType validateGlobalActionType(GlobalActionType globalActionType) {
		Assert.notNull(globalActionType, "globalActionType is null");
		Assert.hasText(globalActionType.getApplicationId(), "applicationId is empty");
		Assert.hasText(globalActionType.getName(), "name is empty");
		Assert.hasText(globalActionType.getSystemName(), "systemName is empty");
		Assert.hasText(globalActionType.getDescription(), "description is empty");
		Assert.notNull(globalActionType.getParameters(), "missing parameters");
		globalActionType.getParameters().stream().map(GlobalActionTypeParameter::getName)
		        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
		        .forEach((name, cnt) -> Assert.isTrue(cnt == 1, "duplicate parameter name " + name));
		return globalActionType;
	}

	private GlobalActionTypeParameter validateGlobalActionTypeParameter(GlobalActionTypeParameter param) {
		Assert.notNull(param, "parameter is null");
		Assert.hasText(param.getName(), "parameter name is empty");
		Assert.isTrue(PARAMETER_PATTERN.matcher(param.getName()).matches(),
		        "invalid parameter name " + param.getName());
		Assert.notNull(param.getValidationTypes(), "missing validationTypes");
		param.getValidationTypes().forEach(this::validateParameterValidation);
		return param;
	}

	private ParameterValidation validateParameterValidation(ParameterValidation validationType) {
		Assert.notNull(validationType, "validationType is null");
		Assert.notNull(validationType.getType(), "type is null");
		switch (validationType.getType()) {
		case SELECT:
			Assert.hasText(validationType.getData(), "data is empty");
			break;
		default:
			break;
		}
		return validationType;
	}
}

package com.arrow.kronos.service;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.action.GlobalAction;
import com.arrow.kronos.data.action.GlobalActionInput;
import com.arrow.kronos.data.action.GlobalActionProperty;
import com.arrow.kronos.data.action.GlobalActionType;
import com.arrow.kronos.data.action.GlobalActionTypeParameter;
import com.arrow.kronos.data.action.ParameterValidation;
import com.arrow.kronos.data.action.ParameterValidation.ValidationType;
import com.arrow.kronos.repo.GlobalActionRepository;
import com.arrow.pegasus.data.AuditLogBuilder;

@Service
public class GlobalActionService extends KronosServiceAbstract {

	private static final Pattern INPUT_PARAMETER_PATTERN = Pattern.compile("\\w+");

	@Autowired
	private GlobalActionRepository globalActionRepository;

	public GlobalActionRepository getGlobalActionRepository() {
		return globalActionRepository;
	}

	public GlobalAction create(GlobalAction globalAction, String who) {
		String method = "create";
		logInfo(method, "...");

		// validate
		Assert.hasText(who, "who is empty");
		validateGlobalAction(globalAction);

		// create
		globalAction = globalActionRepository.doInsert(globalAction, who);

		// audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.GlobalAction.CreateGlobalAction)
		        .applicationId(globalAction.getApplicationId()).objectId(globalAction.getId()).by(who)
		        .parameter("systemName", globalAction.getSystemName()).parameter("name", globalAction.getName()));

		return globalAction;
	}

	public GlobalAction update(GlobalAction globalAction, String who) {
		String method = "update";
		logInfo(method, "...");

		// validate
		Assert.hasText(who, "who is empty");
		validateGlobalAction(globalAction);
		Assert.hasText(globalAction.getId(), "id is empty");
		Assert.hasText(globalAction.getHid(), "hid is empty");

		// update
		globalAction = globalActionRepository.doSave(globalAction, who);

		// audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.GlobalAction.UpdateGlobalAction)
		        .applicationId(globalAction.getApplicationId()).objectId(globalAction.getId()).by(who)
		        .parameter("systemName", globalAction.getSystemName()).parameter("name", globalAction.getName()));

		// clear cache
		GlobalAction cachedGlobalAction = getKronosCache().findGlobalActionById(globalAction.getId());
		if (cachedGlobalAction != null) {
			getKronosCache().clearGlobalAction(cachedGlobalAction);
		}

		return globalAction;
	}

	private GlobalAction validateGlobalAction(GlobalAction globalAction) {
		Assert.notNull(globalAction, "globalAction is null");
		Assert.hasText(globalAction.getApplicationId(), "applicationId is empty");
		Assert.hasText(globalAction.getGlobalActionTypeId(), "globalActionTypeId is empty");
		Assert.hasText(globalAction.getName(), "name is empty");
		Assert.hasText(globalAction.getDescription(), "description is empty");
		Assert.hasText(globalAction.getSystemName(), "systemName is empty");

		// validate system name
		GlobalAction action = globalActionRepository.findByApplicationIdAndSystemName(globalAction.getApplicationId(),
		        globalAction.getSystemName());
		if (action != null && (globalAction.getId() == null || !globalAction.getId().equals(action.getId()))) {
			throw new AcsLogicalException("systemName already exists");
		}

		// validate properties
		GlobalActionType globalActionType = getKronosCache()
		        .findGlobalActionTypeById(globalAction.getGlobalActionTypeId());
		Assert.notNull(globalActionType, "global action type is not found");
		Assert.isTrue(globalActionType.isEnabled(), "global action type is disabled");
		globalAction.getProperties().forEach(prop -> validateGlobalActionProperty(prop, globalActionType));

		// validate input
		List<GlobalActionInput> input = globalAction.getInput();
		if (input != null && !input.isEmpty()) {
			input.forEach(inputParam -> validateGlobalActionInput(inputParam));
			input.stream().map(GlobalActionInput::getName)
			        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
			        .forEach((name, cnt) -> Assert.isTrue(cnt == 1, "duplicate input parameter name " + name));
		}

		return globalAction;
	}

	private GlobalActionProperty validateGlobalActionProperty(GlobalActionProperty property,
	        GlobalActionType globalActionType) {
		Assert.notNull(property, "property is null");

		// validate parameterName
		Assert.hasText(property.getParameterName(), "parameterName is empty");
		Map<String, GlobalActionTypeParameter> parameterMap = globalActionType.getParameters().stream()
		        .collect(Collectors.toMap(p -> p.getName(), p -> p));
		GlobalActionTypeParameter parameter = parameterMap.get(property.getParameterName());
		Assert.notNull(parameter, "Invalid parameter name");

		// validate parameterType
		Assert.hasText(property.getParameterType(), "parameterType is empty");
		ValidationType validationType = ValidationType.valueOf(property.getParameterType());
		HashSet<ValidationType> availableValidationTypes = parameter.getValidationTypes().stream()
		        .map(ParameterValidation::getType).collect(Collectors.toCollection(HashSet::new));
		Assert.isTrue(availableValidationTypes.contains(validationType), "Invalid parameter type");

		// validate parameterValue
		boolean hasText = StringUtils.hasText(property.getParameterValue());
		Assert.isTrue(!parameter.isRequired() || hasText, "parameterValue is required");
		if (hasText) {
			switch (validationType) {
			case XML:
				try {
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					dBuilder.parse(new ByteArrayInputStream(property.getParameterValue().getBytes()));
				} catch (Exception e) {
					throw new AcsLogicalException("Invalid xml");
				}
				break;
			case HTML:
				// TODO
				break;
			case JSON:
				// TODO
				break;
			case EMAIL:
				if (!EmailValidator.getInstance(true, false).isValid(property.getParameterValue())) {
					throw new AcsLogicalException("Invalid e-mail");
				}
				break;
			default:
				break;
			}
		}
		return property;
	}

	private GlobalActionInput validateGlobalActionInput(GlobalActionInput input) {
		Assert.notNull(input, "input is null");
		Assert.isTrue(INPUT_PARAMETER_PATTERN.matcher(input.getName()).matches(), "invalid input parameter name");
		return input;
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return globalActionRepository.deleteByApplicationId(applicationId);
	}
}

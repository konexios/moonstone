package com.arrow.kronos.action.global;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.arrow.kronos.data.action.GlobalAction;
import com.arrow.kronos.data.action.GlobalActionInput;
import com.arrow.kronos.data.action.GlobalActionProperty;
import com.arrow.kronos.data.action.ParameterValidation.ValidationType;

import moonstone.acs.Loggable;

public abstract class GlobalActionHandlerAbstract extends Loggable implements GlobalActionHandler {

	/**
	 * Method to remove all {input_name} inputs if this is a not required input
	 * and it is not provided.
	 * Return copy of properties to not affect global action
	 */
	protected List<GlobalActionProperty> clearOptionalInputsIfNotProvided(GlobalAction action,
			List<MessageInputModel> payload) {
		String method = "clearOptionalInputsIfNotProvided";
		logDebug(method, "action " + action.getName() + "[" + action.getId() + "]");
		List<GlobalActionProperty> propertiesCopy = copy(action.getProperties());
		for (GlobalActionInput inputDef : action.getInput()) {
			if (!inputDef.isRequired() && !isInputPresent(inputDef.getName(), payload)) {
				// optional input not provided
				logDebug(method, "clear input " + inputDef.getName());

				clearInputReferences(inputDef.getName(), propertiesCopy);
			}
		}
		return propertiesCopy;
	}

	private List<GlobalActionProperty> copy(List<GlobalActionProperty> properties) {

		List<GlobalActionProperty> copy = new ArrayList<>();
		properties.stream().forEach(property -> {
			GlobalActionProperty copyProp = new GlobalActionProperty();
			copyProp.setParameterName(property.getParameterName());
			copyProp.setParameterType(property.getParameterType());
			copyProp.setParameterValue(property.getParameterValue());
			copy.add(copyProp);
		});
		return copy;
	}

	private void clearInputReferences(String inputName, List<GlobalActionProperty> properties) {

		String method = "clearInputReferences";

		for (GlobalActionProperty prop : properties) {

			String rawValue = prop.getParameterValue();
			logDebug(method, "clear input " + inputName + " for property " + prop.getParameterName() + ". raw value: "
					+ rawValue);

			rawValue = rawValue.replaceAll("{" + inputName + "}", "");

			prop.setParameterValue(rawValue);

			logDebug(method, "cleared value for property " + prop.getParameterName() + ": " + rawValue);

		}

	}

	private boolean isInputPresent(String inputName, List<MessageInputModel> payload) {
		return payload.stream().anyMatch(input -> input.getName().equals(inputName));
	}

	protected String replaceVariables(GlobalActionProperty property, List<MessageInputModel> payload) {
		String propertyRawValue = property.getParameterValue();
		ValidationType propertyValidationType = ValidationType.valueOf(property.getParameterType());
		return replaceVariables(propertyRawValue, propertyValidationType, payload);
	}

	protected String replaceVariables(String propertyRawValue, ValidationType propertyValidationType,
			List<MessageInputModel> payload) {
		return replaceVariables(propertyRawValue, propertyValidationType, payload, Function.identity());
	}

	protected String replaceVariables(String propertyRawValue, ValidationType propertyValidationType,
			List<MessageInputModel> payload, Function<String, String> func) {
		String method = "replaceVariables";

		logDebug(method, "property raw value " + propertyRawValue);
		logDebug(method, "property type " + propertyValidationType.toString());

		for (MessageInputModel input : payload) {
			try {
				switch (input.getType()) {
				case Long:
				case String:
					propertyRawValue = propertyRawValue.replace("{" + input.getName() + "}",
							func.apply(input.getValue()));
					break;
				case List:
					List<String> list = GlobalActionUtils.parseList(input.getValue());
					propertyRawValue = propertyRawValue.replace("{" + input.getName() + "}",
							func.apply(String.join(",", list)));
					break;
				case Map:
					StringBuilder sb = new StringBuilder();
					switch (propertyValidationType) {
					case MULTILINE_STRING: {
						String newLine = String.format("%n");
						GlobalActionUtils.parseMap(input.getValue())
						        .forEach((k, v) -> sb.append(newLine).append(k).append(": ").append(v));
						break;
					}
					case HTML: {
						GlobalActionUtils.parseMap(input.getValue())
						        .forEach((k, v) -> sb.append("<p>" + k + ": </p><p><b>" + v + "</b></p>"));
						break;
					}
					case XML: {
						sb.append(input.getValue());
						break;
					}
					default: {
						break;
					}
					}
					propertyRawValue = propertyRawValue.replace("{" + input.getName() + "}", func.apply(sb.toString()));
					break;
				default:
					logWarn(method, "Unsupported input type: %s", input.getType());
					break;
				}
			} catch (Throwable e) {
				logError(method, "invalid parameter for input  " + input.getName(), e);
			}
		}

		logDebug(method, "processed property value " + propertyRawValue);

		return propertyRawValue;

	}
}

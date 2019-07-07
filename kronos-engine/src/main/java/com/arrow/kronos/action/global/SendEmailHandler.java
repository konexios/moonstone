package com.arrow.kronos.action.global;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.arrow.acs.JsonUtils;
import com.arrow.kronos.GlobalActionTypeConstants;
import com.arrow.kronos.data.action.GlobalAction;
import com.arrow.kronos.data.action.GlobalActionProperty;
import com.arrow.kronos.data.action.ParameterValidation.ValidationType;
import com.arrow.pegasus.util.EmailContentType;
import com.arrow.pegasus.util.SmtpEmailSender;

public class SendEmailHandler extends GlobalActionHandlerAbstract {

	@Autowired
	private SmtpEmailSender sender;

	@Override
	public void handle(DataMessageModel data, GlobalAction globalAction) {
		String method = "handle";
		logDebug(method, "...");

		logDebug(method, "clear optional not provided inputs");
		List<GlobalActionProperty> actionProperties = clearOptionalInputsIfNotProvided(globalAction, data.getPayload());

		// process Subject
		String subject = processSubject(data, actionProperties);
		logDebug(method, "final subject value: " + subject);

		// process To
		String[] to = processEmailAddresses(data, actionProperties, GlobalActionTypeConstants.SendEmail.PARAMETER_TO);
		logDebug(method, "final To value: " + to);

		// process Cc
		String[] cc = processEmailAddresses(data, actionProperties, GlobalActionTypeConstants.SendEmail.PARAMETER_CC);
		logDebug(method, "final Cc value: " + cc);

		// process Body
		String body = processBody(data, globalAction);
		logDebug(method, "final body value: " + body);

		// process Content Type
		EmailContentType contentType = processContentType(data, globalAction);
		logDebug(method, "final contentType value: " + contentType);

		sender.send(to, cc, subject, body, contentType);

		logDebug(method, "payload: %s", JsonUtils.toJson(data.getPayload()));
	}

	private EmailContentType processContentType(DataMessageModel data, GlobalAction globalAction) {
		EmailContentType contentType;
		GlobalActionProperty contentTypeProperty = GlobalActionUtils.getProperty(globalAction,
				GlobalActionTypeConstants.SendEmail.PARAMETER_CONTENT_TYPE);
		switch (contentTypeProperty.getParameterValue()) {
		case GlobalActionTypeConstants.SendEmail.PARAMETER_CONTENT_TYPE_OPTION_HTML:
			contentType = EmailContentType.HTML;
			break;
		case GlobalActionTypeConstants.SendEmail.PARAMETER_CONTENT_TYPE_OPTION_PLAIN:
			contentType = EmailContentType.PLAIN_TEXT;
			break;
		default:
			contentType = EmailContentType.PLAIN_TEXT;
			break;
		}
		return contentType;
	}

	private String processBody(DataMessageModel data, GlobalAction globalAction) {
		String method = "processBody";
		String body;
		GlobalActionProperty bodyProperty = GlobalActionUtils.getProperty(globalAction,
				GlobalActionTypeConstants.SendEmail.PARAMETER_BODY);
		body = bodyProperty.getParameterValue();
		logDebug(method, "body parameter value: " + body);
		body = replaceVariables(bodyProperty, data.getPayload());
		logDebug(method, "processed body parameter value: " + body);
		return body;
	}

	private String processSubject(DataMessageModel data, List<GlobalActionProperty> actionProperties) {
		String method = "processSubject";
		String subject;
		GlobalActionProperty subjectProperty = GlobalActionUtils.getProperty(actionProperties,
				GlobalActionTypeConstants.SendEmail.PARAMETER_SUBJECT);
		subject = subjectProperty.getParameterValue();
		logDebug(method, "subject parameter value: " + subject);
		subject = replaceVariables(subjectProperty, data.getPayload());
		logDebug(method, "processed subject parameter value: " + subject);
		return subject;
	}

	// separate handling for To parameter
	private String[] processEmailAddresses(DataMessageModel data, List<GlobalActionProperty> actionProperties,
			String propertyName) {
		String method = "processEmailAddresses";
		GlobalActionProperty toProperty = GlobalActionUtils.getProperty(actionProperties, propertyName);

		// currently it may be two types - email or string (multiple emails)

		String propertyType = toProperty.getParameterType();
		String propertyValue = toProperty.getParameterValue();

		logDebug(method, propertyName + " parameter value: " + propertyValue);

		// TODO check required
		if (StringUtils.isEmpty(propertyValue)) {
			logDebug(method, propertyName + " parameter is empty. return empty array");
			return new String[] {};
		}

		// replace variables
		propertyValue = replaceVariables(toProperty, data.getPayload());

		ValidationType propertyValidationType = ValidationType.valueOf(propertyType);

		String[] processedValue;

		switch (propertyValidationType) {
		case EMAIL:
			// one To value;
			// TODO validate. probably
			processedValue = new String[] { propertyValue };
			break;
		case STRING:
			// several To values. "somebody1@mail.com; somebody2@mail.com;"
			// need to get array and validate each address
			processedValue = parseEmailAddresses(propertyValue);
			break;
		default:
			throw new IllegalArgumentException("Unsupported To Property Type");
		}

		logDebug(method, propertyName + " processed parameter value: " + processedValue);
		return processedValue;
	}

	// TODO replace by matcher
	// expect "somebody1@mail.com; somebody2@mail.com;" or "somebody1@mail.com;"
	// or "somebody1@mail.com; somebody2@mail.com; somebody3@mail.com; ..."
	private String[] parseEmailAddresses(String emailAddressesString) {
		String method = "parseEmailAddresses";

		logDebug(method, "parsing emails from " + emailAddressesString);

		String removeSpaces = emailAddressesString.replaceAll("\\s", "");
		String removeLastSymbol = removeSpaces.replaceAll(";$", "");
		String[] emails = removeLastSymbol.split(";");

		logDebug(method, "parsed emails from " + emailAddressesString + ": " + emails);

		return emails;
	}

}

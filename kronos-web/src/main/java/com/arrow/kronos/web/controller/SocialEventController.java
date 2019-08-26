package com.arrow.kronos.web.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.data.SocialEventRegistration;
import com.arrow.kronos.data.SocialEventRegistrationStatuses;
import com.arrow.kronos.service.SocialEventRegistrationService;
import com.arrow.kronos.web.exception.AccountExistsException;
import com.arrow.kronos.web.exception.InvalidInputException;
import com.arrow.kronos.web.model.SocialEventModels.SocialEventCodeModel;
import com.arrow.kronos.web.model.SocialEventModels.SocialEventModel;
import com.arrow.kronos.web.model.SocialEventModels.SocialEventRegistrationModel;
import com.arrow.kronos.web.model.SocialEventModels.SocialEventRegistrationResultModel;
import com.arrow.kronos.web.model.SocialEventModels.SocialEventVerificationModel;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.client.api.ClientSocialEventApi;
import com.arrow.pegasus.data.SocialEvent;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;

import moonstone.acs.AcsLogicalException;
import moonstone.acs.JsonUtils;

@RestController
@RequestMapping("/api/kronos/social-event")
public class SocialEventController extends BaseControllerAbstract {

	@Autowired
	private ClientSocialEventApi clientSocialEventApi;
	@Autowired
	private SocialEventRegistrationService socialEventRegistrationService;

	@RequestMapping(value = "/current-events", method = RequestMethod.GET)
	public List<SocialEventModel> findCurrentEvents(@RequestParam(name = "date", required = false) String date) {
		Instant currentDate = Instant.now();
		if (date != null) {
			currentDate = Instant.parse(date);
		}
		return clientSocialEventApi.findByDate(currentDate).stream().map(SocialEventModel::new)
		        .collect(Collectors.toCollection(ArrayList::new));
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public SocialEventRegistrationResultModel registration(@RequestBody SocialEventRegistrationModel model) {
		Map<String, String> inputValidation = new HashMap<>();
		// validate name
		if (!StringUtils.hasText(model.getName()) || model.getName().length() < 2) {
			inputValidation.put("name", "Invalid name");
		}
		// validate email
		if (!EmailValidator.getInstance().isValid(model.getEmail())) {
			inputValidation.put("email", "Invalid email address");
		}
		// validate password
		if (!StringUtils.hasText(model.getPassword())) {
			inputValidation.put("password", "Password is required");
		}
		Application application = null;
		// validate eventCode
		if (!StringUtils.hasText(model.getEventCode())) {
			inputValidation.put("eventCode", "Application code is required");
		} else {
			application = getCoreCacheService().findApplicationByCode(model.getEventCode());
			if (application == null || !application.isEnabled()) {
				inputValidation.put("eventCode", "Invalid application code");
			}
		}
		if (!inputValidation.isEmpty()) {
			throw new InvalidInputException(JsonUtils.toJson(inputValidation));
		}
		// validate eventId
		Assert.hasText(model.getEventId(), "eventId is required");
		SocialEvent socialEvent = clientSocialEventApi.findById(model.getEventId());
		if (socialEvent == null) {
			throw new AcsLogicalException("Invalid event");
		}
		// check if this email exists in social event registrations collection
		SocialEventRegistration emailRegistration = socialEventRegistrationService
		        .findCaseInsensitiveByEmail(model.getEmail());
		if (emailRegistration != null) {
			switch (emailRegistration.getStatus()) {
			case REGISTERED:
				// if status is REGISTERED - it needs to be verified
				inputValidation.put("email", "Account has already been registered");
				throw new InvalidInputException(JsonUtils.toJson(inputValidation));
			case VERIFIED:
				// if status is VERIFIED - user may sign in to event
				throw new AccountExistsException("You already have an account, please sign in using your existing account");
			default:
				break;
			}
		}
		// check if there is a user in pegasus with the same login (social event registrations already checked)
		User user = getClientUserApi().findByLogin(model.getEmail());
		if (user != null) {
			inputValidation.put("email", "User with this login already exists");
			throw new InvalidInputException(JsonUtils.toJson(inputValidation));
		}
		// find event registration record by applicationId
		SocialEventRegistration event = socialEventRegistrationService.getSocialEventRegistrationRepository()
		        .findByApplicationId(application.getId());
		if (event == null || !event.getSocialEventId().equals(socialEvent.getId())) {
			inputValidation.put("eventCode", "Application code does not match selected event");
			throw new InvalidInputException(JsonUtils.toJson(inputValidation));
		}
		// validate if event registration is PENDING
		if (event.getStatus() != SocialEventRegistrationStatuses.PENDING) {
			inputValidation.put("event", "Account has already been registered");
			throw new InvalidInputException(JsonUtils.toJson(inputValidation));
		}

		event = socialEventRegistrationService.register(buildSocialEventRegistration(event, model),
		        CoreConstant.ADMIN_USER);

		return new SocialEventRegistrationResultModel(event);
	}

	@RequestMapping(value = "/resend", method = RequestMethod.POST)
	public void resend(@RequestBody SocialEventVerificationModel model) {
		Map<String, String> inputValidation = new HashMap<>();
		if (!EmailValidator.getInstance().isValid(model.getEmail())) {
			inputValidation.put("email", "Invalid email address");
			throw new InvalidInputException(JsonUtils.toJson(inputValidation));
		}
		
		SocialEventRegistration eventRegistration = socialEventRegistrationService
		        .findCaseInsensitiveByEmail(model.getEmail());
		if (eventRegistration == null || eventRegistration.getStatus() == SocialEventRegistrationStatuses.PENDING) {
			inputValidation.put("email", "Invalid email address");
			throw new InvalidInputException(JsonUtils.toJson(inputValidation));
		} else if (eventRegistration.getStatus() == SocialEventRegistrationStatuses.VERIFIED) {
			inputValidation.put("email", "Account has already been verified");
			throw new InvalidInputException(JsonUtils.toJson(inputValidation));
		}
		socialEventRegistrationService.resendVerificationCode(eventRegistration);
	}

	@RequestMapping(value = "/verification", method = RequestMethod.POST)
	public void verification(@RequestBody SocialEventCodeModel model) {
		Map<String, String> inputValidation = new HashMap<>();
		if (!StringUtils.hasText(model.getCode()) || model.getCode().length() > 100) {
			inputValidation.put("code", "Invalid verification code");
			throw new InvalidInputException(JsonUtils.toJson(inputValidation));
		}
		SocialEventRegistration eventRegistration = socialEventRegistrationService
		        .getSocialEventRegistrationRepository().findByVerificationCode(model.getCode());
		if (eventRegistration == null || eventRegistration.getStatus() == SocialEventRegistrationStatuses.PENDING) {
			inputValidation.put("code", "Invalid verification code");
			throw new InvalidInputException(JsonUtils.toJson(inputValidation));
		} else if (eventRegistration.getStatus() == SocialEventRegistrationStatuses.VERIFIED) {
			inputValidation.put("code", "Account has already been verified");
			throw new InvalidInputException(JsonUtils.toJson(inputValidation));
		}
		socialEventRegistrationService.verify(eventRegistration, CoreConstant.ADMIN_USER);
	}

	private SocialEventRegistration buildSocialEventRegistration(SocialEventRegistration event,
	        SocialEventRegistrationModel model) {
		String salt = getCryptoService().getCrypto().randomToken();
		String hashed = getCryptoService().getCrypto().hash(model.getPassword(), salt);
		event.setName(model.getName());
		event.setEmail(model.getEmail());
		event.setSalt(salt);
		event.setHashedPassword(hashed);
		return event;
	}
}

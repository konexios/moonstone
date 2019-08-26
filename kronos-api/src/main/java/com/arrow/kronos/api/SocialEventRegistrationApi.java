package com.arrow.kronos.api;

import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.api.model.AzureIntegrationPropertiesModel;
import com.arrow.kronos.api.model.SocialEventRegistrationModel;
import com.arrow.kronos.api.model.SocialEventRegistrationStatuses;
import com.arrow.kronos.api.model.SocialEventRegistrationVerifyModel;
import com.arrow.kronos.data.AzureIntegrationProperties;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.SocialEventRegistration;
import com.arrow.kronos.repo.SocialEventRegistrationSearchParams;
import com.arrow.kronos.service.SocialEventRegistrationService;
import com.arrow.pegasus.client.api.ClientSocialEventApi;
import com.arrow.pegasus.client.api.ClientUserApi;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AuditLog;
import com.arrow.pegasus.data.SocialEvent;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Contact;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.service.CryptoService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import moonstone.acn.client.model.CreateSocialEventRegistrationModel;
import moonstone.acn.client.model.RegisterSocialEventRegistrationModel;
import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.HidModel;
import moonstone.acs.client.model.PagingResultModel;
import moonstone.acs.client.model.StatusModel;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/kronos/socialevent/registrations")
public class SocialEventRegistrationApi extends BaseApiAbstract {

	@Autowired
	private SocialEventRegistrationService socialEventRegistrationService;
	@Autowired
	private CryptoService cryptoService;
	@Autowired
	private ClientUserApi clientUserApi;
	@Autowired
	private ClientSocialEventApi clientSocialEventApi;

	@ApiIgnore
	@ApiOperation(value = "delete social event registration", response = HidModel.class)
	@RequestMapping(path = "/{hid}", method = RequestMethod.DELETE)
	public StatusModel delete(
			@ApiParam(value = "social event hid", required = true) @PathVariable(name = "hid") String hid,
			HttpServletRequest request) {
		String method = "delete";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());

		SocialEventRegistration socialEventRegistration = getKronosCache().findSocialEventRegistrationByHid(hid);
		Assert.notNull(socialEventRegistration, "socialEventRegistration is not found");
		Assert.isTrue(accessKey.getApplicationId().equals(socialEventRegistration.getApplicationId()),
				"application does not match");

		socialEventRegistrationService.delete(socialEventRegistration, accessKey.getPri());

		auditLog(method, socialEventRegistration.getApplicationId(), socialEventRegistration.getId(), accessKey.getId(),
				request);

		return StatusModel.OK;
	}

	@ApiIgnore
	@ApiOperation(value = "find social event registration by hid", response = SocialEventRegistrationModel.class)
	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public SocialEventRegistrationModel findByHid(
			@ApiParam(value = "social event hid", required = true) @PathVariable(name = "hid") String hid) {
		AccessKey accessKey = validateCanReadApplication(getProductSystemName());
		SocialEventRegistration socialEventRegistration = getKronosCache().findSocialEventRegistrationByHid(hid);
		Assert.notNull(socialEventRegistration, "socialEventRegistration is not found");
		Assert.isTrue(accessKey.getApplicationId().equals(socialEventRegistration.getApplicationId()),
				"application does not match");
		return buildSocialEventRegistrationModel(socialEventRegistration);
	}

	@ApiIgnore
	@ApiOperation(value = "register social event registration", response = HidModel.class)
	@RequestMapping(path = "/register", method = RequestMethod.POST)
	public HidModel register(
			@ApiParam(value = "social event registration model", required = true) @RequestBody(required = false) RegisterSocialEventRegistrationModel body,
			HttpServletRequest request) {
		String method = "register";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());

		moonstone.acn.client.model.RegisterSocialEventRegistrationModel model = JsonUtils.fromJson(getApiPayload(),
				RegisterSocialEventRegistrationModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getName(), "name is empty");
		Assert.hasText(model.getPassword(), "password is empty");
		Assert.hasText(model.getEventCode(), "application code is empty");
		Assert.isTrue(EmailValidator.getInstance().isValid(model.getEmail()), "invalid email");
		Assert.hasText(model.getSocialEventHid(), "socialEventHid is empty");

		SocialEvent socialEvent = clientSocialEventApi.findByHid(model.getSocialEventHid());
		Assert.notNull(socialEvent, "socialEvent not found");

		SocialEventRegistration emailRegistration = socialEventRegistrationService
				.findCaseInsensitiveByEmail(model.getEmail());
		if (emailRegistration != null) {
			Assert.isTrue(
					!emailRegistration.getStatus().name().equals(SocialEventRegistrationStatuses.REGISTERED.name()),
					"Account has already been registered");
			Assert.isTrue(!emailRegistration.getStatus().name().equals(SocialEventRegistrationStatuses.VERIFIED.name()),
					"You already have an account, please sign in using your existing account");
		}

		User user = getClientUserApi().findByLogin(model.getEmail());
		Assert.isNull(user, "User with this login already exists");

		// 1. Find application by code
		Application application = getCoreCacheService().findApplicationByCode(model.getEventCode());
		Assert.notNull(application, "Invalid application code");
		Assert.isTrue(application.isEnabled(), "application is disabled");

		// 2. find SocialEventRegistrations by applicationId
		SocialEventRegistration socialEventRegistration = socialEventRegistrationService
				.getSocialEventRegistrationRepository().findByApplicationId(application.getId());
		Assert.notNull(socialEventRegistration, "Invalid application code");
		Assert.isTrue(socialEventRegistration.getSocialEventId().equals(socialEvent.getId()),
				"Application code does not match selected event");

		// 3. update SocialEventRegistration
		socialEventRegistration = buildSocialEventRegistration(model, socialEventRegistration);
		socialEventRegistration = socialEventRegistrationService.register(socialEventRegistration, accessKey.getId());

		auditLog(method, socialEventRegistration.getApplicationId(), socialEventRegistration.getId(), accessKey.getId(),
				request);

		return new HidModel().withHid(socialEventRegistration.getHid()).withMessage("OK");
	}

	@ApiIgnore
	@ApiOperation(value = "resend email", response = HidModel.class)
	@RequestMapping(path = "/resend", method = RequestMethod.POST)
	public StatusModel resend(@ApiParam(value = "email", required = true) @RequestParam(required = true) String email,
			HttpServletRequest request) {
		String method = "resend";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());

		SocialEventRegistration socialEventRegistration = socialEventRegistrationService
				.findCaseInsensitiveByEmail(email);
		Assert.notNull(socialEventRegistration, "Invalid email address");

		auditLog(method, socialEventRegistration.getApplicationId(), socialEventRegistration.getId(), accessKey.getId(),
				request);

		socialEventRegistrationService.resendVerificationCode(socialEventRegistration);

		return StatusModel.OK;
	}

	@ApiIgnore
	@ApiOperation(value = "verify social event registration", response = HidModel.class)
	@RequestMapping(path = "/verify", method = RequestMethod.POST)
	public SocialEventRegistrationVerifyModel verify(
			@ApiParam(value = "verification code", required = true) @RequestParam(required = true) String verificationCode,
			HttpServletRequest request) {
		String method = "verify";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());

		Assert.hasText(verificationCode, "verificationCode is empty");
		SocialEventRegistration socialEventRegistration = socialEventRegistrationService
				.getSocialEventRegistrationRepository().findByVerificationCode(verificationCode);
		Assert.notNull(socialEventRegistration, "Invalid verification code");

		socialEventRegistration = socialEventRegistrationService.verify(socialEventRegistration, accessKey.getId());
		auditLog(method, socialEventRegistration.getApplicationId(), socialEventRegistration.getId(), accessKey.getId(),
				request);

		SocialEventRegistrationVerifyModel result = new SocialEventRegistrationVerifyModel();
		result.setApplicationHid(findApplicationHid(socialEventRegistration.getApplicationId()));
		result.setCompanyHid(findTenantHid(socialEventRegistration.getApplicationId()));
		result.setUserHid(findUserHid(socialEventRegistration.getUserId()));

		return result;
	}

	@ApiIgnore
	@ApiOperation(value = "create new social event registration", response = HidModel.class)
	@RequestMapping(path = "", method = RequestMethod.POST)
	public HidModel create(
			@ApiParam(value = "social event registration model", required = true) @RequestBody(required = false) CreateSocialEventRegistrationModel body,
			HttpServletRequest request) {
		String method = "create";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());

		AuditLog auditLog = auditLog(method, accessKey.getApplicationId(), null, accessKey.getId(), request);

		CreateSocialEventRegistrationModel model = JsonUtils.fromJson(getApiPayload(),
				CreateSocialEventRegistrationModel.class);
		Assert.hasText(model.getOrigEmail(), "origEmail is empty");
		Assert.hasText(model.getOrigPassword(), "origPassword is empty");
		Assert.hasText(model.getSocialEventHid(), "socialEventHid is empty");
		Assert.hasText(model.getEmail(), "email is empty");
		Assert.hasText(model.getUserHid(), "userHid is empty");

		SocialEventRegistration socialEventRegistration = new SocialEventRegistration();
		socialEventRegistration.setApplicationId(accessKey.getApplicationId());
		socialEventRegistration.setStatus(com.arrow.kronos.data.SocialEventRegistrationStatuses.PENDING);
		socialEventRegistration.setOrigEmail(model.getOrigEmail());
		socialEventRegistration
				.setOrigEncryptedPassword(cryptoService.getCrypto().internalEncrypt(model.getOrigPassword()));
		socialEventRegistration.setSocialEventId(findSocialEventId(model.getSocialEventHid()));
		socialEventRegistration.setEmail(model.getEmail());
		socialEventRegistration.setUserId(findUserId(model.getUserHid()));

		socialEventRegistration = socialEventRegistrationService.create(socialEventRegistration, accessKey.getPri());

		auditLog.setObjectId(socialEventRegistration.getId());
		getAuditLogService().getAuditLogRepository().doSave(auditLog, accessKey.getId());

		return new HidModel().withHid(socialEventRegistration.getHid()).withMessage("OK");
	}

	@ApiIgnore
	@ApiOperation(value = "update existing social event registration", response = HidModel.class)
	@RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
	public HidModel update(
			@ApiParam(value = "social event hid", required = true) @PathVariable(name = "hid") String hid,
			@ApiParam(value = "social event registration model", required = true) @RequestBody(required = false) RegisterSocialEventRegistrationModel body,
			HttpServletRequest request) {
		String method = "update";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());
		RegisterSocialEventRegistrationModel model = JsonUtils.fromJson(getApiPayload(),
				RegisterSocialEventRegistrationModel.class);

		SocialEventRegistration socialEventRegistration = socialEventRegistrationService
				.getSocialEventRegistrationRepository().doFindByHid(hid);
		Assert.notNull(socialEventRegistration, "socialEventRegistration is not found");
		Assert.isTrue(accessKey.getApplicationId().equals(socialEventRegistration.getApplicationId()),
				"application does not match");

		auditLog(method, socialEventRegistration.getApplicationId(), socialEventRegistration.getId(), accessKey.getId(),
				request);

		socialEventRegistration = buildSocialEventRegistration(model, socialEventRegistration);

		socialEventRegistration = socialEventRegistrationService.update(socialEventRegistration, accessKey.getPri());

		// update user
		User user = getCoreCacheService().findUserById(socialEventRegistration.getUserId());
		if (StringUtils.hasText(model.getEmail())) {
			user.setLogin(socialEventRegistration.getEmail());
			Contact contact = user.getContact();
			contact.setEmail(socialEventRegistration.getEmail());
			user.setContact(contact);
		}
		if (StringUtils.hasText(model.getPassword())) {
			user.setPassword(model.getPassword());
		}
		user = clientUserApi.update(user, accessKey.getPri());

		// TODO question about reverifying account after change email
		return new HidModel().withHid(socialEventRegistration.getHid()).withMessage("OK");
	}

	@ApiIgnore
	@ApiOperation(value = "find social event registrations")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public PagingResultModel<SocialEventRegistrationModel> findAllBy(
			@ApiParam(value = "application hids", required = false) @RequestParam(name = "applicationHids", required = false) Set<String> applicationHids,
			@ApiParam(value = "social event hids", required = false) @RequestParam(name = "socialEventHids", required = false) Set<String> socialEventHids,
			@ApiParam(value = "statuses", required = false) @RequestParam(name = "statuses", required = false) Set<String> statuses,
			@ApiParam(value = "updatedBefore", required = false) @RequestParam(name = "updatedBefore", required = false) String updatedBefore,
			@ApiParam(value = "updatedAfter", required = false) @RequestParam(name = "updatedAfter", required = false) String updatedAfter,
			@ApiParam(value = "createdBefore", required = false) @RequestParam(name = "createdBefore", required = false) String createdBefore,
			@ApiParam(value = "createdAfter", required = false) @RequestParam(name = "createdAfter", required = false) String createdAfter,
			@ApiParam(value = "names", required = false) @RequestParam(name = "names", required = false) Set<String> names,
			@ApiParam(value = "emails", required = false) @RequestParam(name = "emails", required = false) Set<String> emails,
			@ApiParam(value = "origEmails", required = false) @RequestParam(name = "origEmails", required = false) Set<String> origEmails,
			@ApiParam(value = "page", required = false) @RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@ApiParam(value = "size", required = false) @RequestParam(name = "size", required = false, defaultValue = "100") int size) {

		Assert.isTrue(page >= 0, "page must be positive");
		Assert.isTrue(size >= 0 && size <= KronosConstants.PageResult.MAX_SIZE,
				"size must be between 0 and " + KronosConstants.PageResult.MAX_SIZE);

		validateCanReadApplication(getProductSystemName());

		PagingResultModel<SocialEventRegistrationModel> result = new PagingResultModel<>();
		result.setPage(page);
		PageRequest pageRequest = PageRequest.of(page, size);

		SocialEventRegistrationSearchParams params = new SocialEventRegistrationSearchParams();
		if (applicationHids != null) {
			applicationHids.forEach(hid -> params.addApplicationIds(findApplicationId(hid)));
		}

		if (StringUtils.hasText(updatedBefore)) {
			params.setUpdatedBefore(Instant.parse(updatedBefore));
		}

		if (StringUtils.hasText(updatedAfter)) {
			params.setUpdatedAfter(Instant.parse(updatedAfter));
		}

		if (StringUtils.hasText(createdBefore)) {
			params.setCreatedBefore(Instant.parse(createdBefore));
		}

		if (StringUtils.hasText(createdAfter)) {
			params.setCreatedAfter(Instant.parse(createdAfter));
		}

		if (socialEventHids != null) {
			socialEventHids.forEach(hid -> params.addSocialEventIds(findSocialEventId(hid)));
		}

		if (names != null) {
			names.forEach(name -> params.addNames(name));
		}

		if (emails != null) {
			emails.forEach(email -> params.addEmails(email));
		}

		if (origEmails != null) {
			origEmails.forEach(origEmail -> params.addOrigEmails(origEmail));
		}

		if (statuses != null) {
			EnumSet<com.arrow.kronos.data.SocialEventRegistrationStatuses> socialEventRegistrationStatuses = EnumSet
					.noneOf(com.arrow.kronos.data.SocialEventRegistrationStatuses.class);
			statuses.forEach(status -> socialEventRegistrationStatuses
					.add(com.arrow.kronos.data.SocialEventRegistrationStatuses.valueOf(status.toString())));
			params.setStatuses(socialEventRegistrationStatuses);
		}

		Page<SocialEventRegistration> socialEventRegistrations = socialEventRegistrationService
				.findSocialEventRegistrations(pageRequest, params);

		List<SocialEventRegistrationModel> data = socialEventRegistrations.getContent().stream()
				.map(socialEventRegistration -> buildSocialEventRegistrationModel(socialEventRegistration))
				.collect(Collectors.toCollection(ArrayList::new));

		result.withTotalPages(socialEventRegistrations.getTotalPages())
				.withTotalSize(socialEventRegistrations.getTotalElements())
				.withSize(socialEventRegistrations.getNumberOfElements()).withData(data);
		return result;
	}

	private String findSocialEventId(String hid) {
		Assert.hasText(hid, "socialEventHid is empty");
		SocialEvent socialEvent = socialEventRegistrationService.getClientSocialEventApi().findByHid(hid);
		Assert.notNull(socialEvent, "socialEvent is not found");
		return socialEvent.getId();
	}

	private SocialEventRegistrationStatuses buildSocialEventRegistrationStatusModel(
			com.arrow.kronos.data.SocialEventRegistrationStatuses status) {
		Assert.notNull(status, "status is null");
		return SocialEventRegistrationStatuses.valueOf(status.toString());
	}

	private SocialEventRegistrationModel buildSocialEventRegistrationModel(
			SocialEventRegistration socialEventRegistration) {
		Assert.notNull(socialEventRegistration, "socialEventRegistration is null");
		SocialEventRegistrationModel result = new SocialEventRegistrationModel();

		result.setHid(socialEventRegistration.getHid());
		result.setCreatedBy(socialEventRegistration.getCreatedBy());
		result.setLastModifiedBy(socialEventRegistration.getLastModifiedBy());
		result.setOrigEmail(socialEventRegistration.getOrigEmail());
		result.setOrigEncryptedPassword(socialEventRegistration.getOrigEncryptedPassword());
		result.setLastModifiedBy(socialEventRegistration.getLastModifiedBy());

		if (socialEventRegistration.getCreatedDate() != null)
			result.setCreatedDate(socialEventRegistration.getCreatedDate().toString());
		if (socialEventRegistration.getLastModifiedDate() != null)
			result.setLastModifiedDate(socialEventRegistration.getLastModifiedDate().toString());

		result.setApplicationHid(findApplicationHid(socialEventRegistration.getApplicationId()));

		result.setName(socialEventRegistration.getName());
		result.setEmail(socialEventRegistration.getEmail());
		result.setSalt(socialEventRegistration.getSalt());
		result.setHashedPassword(socialEventRegistration.getHashedPassword());

		SocialEvent socialEvent = socialEventRegistrationService.getClientSocialEventApi()
				.findById(socialEventRegistration.getSocialEventId());
		if (socialEvent != null) {
			result.setSocialEventHid(socialEvent.getHid());
		}
		result.setSocialEventHid(socialEventRegistration.getSocialEventId());

		result.setStatus(buildSocialEventRegistrationStatusModel(socialEventRegistration.getStatus()));

		result.setUserHid(findUserHid(socialEventRegistration.getUserId()));

		if (socialEventRegistration.getAzureIntegration() != null) {
			Application application = getCoreCacheService()
					.findApplicationById(socialEventRegistration.getApplicationId());
			result.setAzureIntegration(
					buildAzureIntegrationPropertiesModel(application, socialEventRegistration.getAzureIntegration()));
		}

		return result;
	}

	private AzureIntegrationPropertiesModel buildAzureIntegrationPropertiesModel(Application application,
			AzureIntegrationProperties azureIntegration) {
		if (azureIntegration == null) {
			return null;
		}
		AzureIntegrationPropertiesModel result = new AzureIntegrationPropertiesModel();
		result.setAccessPolicyName(azureIntegration.getAccessPolicyName());
		result.setAccessKeyName(azureIntegration.getAccessKeyName());
		result.setAccessKey(getCryptoService().decrypt(application.getId(), azureIntegration.getAccessKey()));
		result.setHostName(azureIntegration.getHostName());
		if (azureIntegration.getGatewayIds() != null && !azureIntegration.getGatewayIds().isEmpty()) {
			result.setGatewayHids(azureIntegration.getGatewayIds().stream().map(id -> {
				Gateway gateway = getKronosCache().findGatewayById(id);
				return gateway != null ? gateway.getHid() : null;
			}).filter(hid -> hid != null).collect(Collectors.toCollection(ArrayList::new)));
		}
		return result;
	}

	private SocialEventRegistration buildSocialEventRegistration(RegisterSocialEventRegistrationModel model,
			SocialEventRegistration socialEventRegistration) {
		Assert.notNull(model, "model is null");
		Assert.notNull(socialEventRegistration, "socialEventRegistration is null");

		if (StringUtils.hasText(model.getEmail())) {
			socialEventRegistration.setEmail(model.getEmail());
		}
		if (StringUtils.hasText(model.getName())) {
			socialEventRegistration.setName(model.getName());
		}
		// security
		if (StringUtils.hasText(model.getPassword())) {
			socialEventRegistration.setSalt(cryptoService.getCrypto().randomToken());
			socialEventRegistration.setHashedPassword(
					cryptoService.getCrypto().hash(model.getPassword(), socialEventRegistration.getSalt()));
		}

		return socialEventRegistration;
	}

	private String findApplicationId(String applicationHid) {
		Assert.hasText(applicationHid, "applicationHid is empty");
		Application app = getCoreCacheService().findApplicationByHid(applicationHid);
		Assert.notNull(app, "application is not found");
		Assert.isTrue(app.isEnabled(), "application is disabled");
		return app.getId();
	}

	private String findUserId(String userHid) {
		Assert.hasText(userHid, "userHid is empty");
		User user = getCoreCacheService().findUserByHid(userHid);
		Assert.notNull(user, "user is not found");
		return user.getId();
	}

	private String findApplicationHid(String applicationId) {
		String hid = null;
		if (StringUtils.hasText(applicationId)) {
			Application app = getCoreCacheService().findApplicationById(applicationId);
			if (app != null)
				hid = app.getHid();
		}
		return hid;
	}

	private String findTenantHid(String applicationId) {
		String hid = null;
		if (StringUtils.hasText(applicationId)) {
			Application app = getCoreCacheService().findApplicationById(applicationId);
			if (app != null) {
				Company tenant = getCoreCacheService().findCompanyById(app.getCompanyId());
				if (tenant != null)
					hid = tenant.getHid();
			}
		}
		return hid;
	}

	private String findUserHid(String userId) {
		String hid = null;
		if (StringUtils.hasText(userId)) {
			User user = getCoreCacheService().findUserById(userId);
			if (user != null)
				hid = user.getHid();
		}
		return hid;
	}

}

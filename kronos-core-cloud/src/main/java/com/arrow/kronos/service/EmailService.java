package com.arrow.kronos.service;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.StringEscapeUtils;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

import com.arrow.acs.AcsRuntimeException;
import com.arrow.acs.AcsSystemException;
import com.arrow.acs.client.model.EmailModel;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.ConfigurationPropertyCategory;
import com.arrow.pegasus.data.TempToken;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.util.CoreConfigurationPropertyUtil;
import com.arrow.pegasus.util.EmailContentType;
import com.arrow.pegasus.util.SmtpEmailSender;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class EmailService extends KronosServiceAbstract {

	// templates, configuration property name
	private static final String KIOSK_SIGNUP = "emailKioskSignupEmail";
	private static final String REGISTERED_DEVELOPER = "emailRegisteredDeveloperEmail";
	private static final String ACCOUNT_ACTIVATED = "emailAccountActivatedEmail";
	private static final String ACCOUNT_EXPIRED = "emailDeveloperAccountExpired";
	private static final String PASSWORD_RESET = "emailPasswordReset";
	private static final String PASSWORD_UPDATE = "emailPasswordUpdatedEmail";
	private static final String REACTIVATE_ACCOUNT = "emailReactivateDeveloperAccountEmail";
	private static final String UNVERIFIED_ACCOUNT_EXPIRED = "emailUnverifiedAccountExpiredEmail";
	private static final String SOFTWARE_RELEASE_SCHEDULE_SUBMIT = "emailSoftwareReleaseScheduleSubmit";
	private static final String SOFTWARE_RELEASE_SCHEDULE_START = "emailSoftwareReleaseScheduleStart";
	private static final String SOFTWARE_RELEASE_SCHEDULE_END = "emailSoftwareReleaseScheduleEnd";
	private static final String SOCIAL_EVENT_REGISTRATION = "emailSocialEventRegistration";
	private static final String SOCIAL_EVENT_REGISTRATION_VERIFIED = "emailSocialEventRegistrationVerified";

	@Autowired
	private CoreConfigurationPropertyUtil coreConfigurationPropertyUtil;
	@Autowired
	private SmtpEmailSender smtpEmailSender;

	private class TemplateWrapper implements Serializable {
		private static final long serialVersionUID = 3644825304889943752L;

		private Instant cachedDate;
		private Template bodyTemplate;
		private Template subjectTemplate;

		public TemplateWrapper(Template subjectTemplate, Template bodyTemplate) {
			this.cachedDate = Instant.now();
			this.subjectTemplate = subjectTemplate;
			this.bodyTemplate = bodyTemplate;
		}

		public Template getBodyTemplate() {
			return bodyTemplate;
		}

		public Template getSubjectTemplate() {
			return subjectTemplate;
		}

		public Instant getCachedDate() {
			return cachedDate;
		}
	}

	// template
	private Configuration freeMarker;
	private Map<String, TemplateWrapper> templates = new HashMap<>();

	private synchronized Pair<Template, Template> getEmailTemplate(String templateName) {
		Assert.hasText(templateName, "templateName is empty");

		String method = "getEmailTemplate";
		logInfo(method, "...");

		Product product = getCoreCacheService().findProductBySystemName(ProductSystemNames.KRONOS);
		Assert.notNull(product, "Unable to find product! product=" + ProductSystemNames.KRONOS);
		logInfo(method, "product: %s last updated: %s", product.getSystemName(),
		        product.getLastModifiedDate().toString());

		ConfigurationProperty emailConfigurationProperty = coreConfigurationPropertyUtil
		        .getConfigurationProperty(product, ConfigurationPropertyCategory.Email, templateName);
		Assert.notNull(emailConfigurationProperty, "Unable to find email template! templateName=" + templateName);

		EmailModel model = (EmailModel) emailConfigurationProperty.jsonValue();

		if (freeMarker == null) {
			freeMarker = new Configuration(Configuration.VERSION_2_3_25);
		}

		TemplateWrapper templateWrapper = templates.get(templateName);
		if (templateWrapper == null || product.getLastModifiedDate().isAfter(templateWrapper.getCachedDate())) {
			logInfo(method, "creating template for: %s", templateName);
			try {
				templates.put(templateName,
				        templateWrapper = new TemplateWrapper(
				                new Template(templateName + "/subject", model.getSubject(), freeMarker),
				                new Template(templateName + "/body", model.getBody(), freeMarker)));
			} catch (IOException e) {
				throw new AcsSystemException("unable to parse template: " + templateName, e);
			}
		}

		logInfo(method, "template: %s cached: %s", templateName, templateWrapper.cachedDate.toString());

		return new ImmutablePair<Template, Template>(templateWrapper.getSubjectTemplate(),
		        templateWrapper.getBodyTemplate());
	}

	public void sendKioskSignupEmail(String email, TempToken tempToken) {
		Assert.hasText(email, "login is empty");
		Assert.notNull(tempToken, "tempToken is null");

		Zone zone = getPlatformConfigService().getConfig().getRefZone();
		
		Map<String, Object> model = new HashMap<>();
		model.put("email", StringEscapeUtils.escapeHtml4(email));
		model.put("tempTokenHID", StringEscapeUtils.escapeHtml4(tempToken.getHid()));
		model.put("zoneSystemName", zone.getSystemName());
		sendEmail(email, model, KIOSK_SIGNUP);
	}

	public void sendRegisteredDeveloperEmail(String email, String firstName, TempToken tempToken) {
		Assert.hasText(email, "email is empty");
		Assert.hasText(firstName, "firstName is empty");
		Assert.notNull(tempToken, "tempToken is null");

		Zone zone = getPlatformConfigService().getConfig().getRefZone();
		
		Map<String, Object> model = new HashMap<>();
		model.put("firstName", StringEscapeUtils.escapeHtml4(firstName));
		model.put("tempTokenHID", StringEscapeUtils.escapeHtml4(tempToken.getHid()));
		model.put("zoneSystemName", zone.getSystemName());
		sendEmail(email, model, REGISTERED_DEVELOPER);
	}

	public void sendAccountActivatedEmail(String email, String firstName, String applicationCode) {
		Assert.hasText(email, "email is empty");
		Assert.hasText(firstName, "firstName is empty");
		Assert.hasText(applicationCode, "applicationCode is null");

		Map<String, Object> model = new HashMap<>();
		model.put("firstName", StringEscapeUtils.escapeHtml4(firstName));
		model.put("applicationCode", StringEscapeUtils.escapeHtml4(applicationCode));
		sendEmail(email, model, ACCOUNT_ACTIVATED);
	}

	public void sendUnverifiedAccountExpiredEmail(String email, String login, String fullName) {
		Assert.hasText(login, "login is empty");
		Assert.hasText(fullName, "fullName is empty");

		Map<String, Object> model = new HashMap<>();
		model.put("fullName", StringEscapeUtils.escapeHtml4(fullName));
		model.put("login", StringEscapeUtils.escapeHtml4(login));
		sendEmail(email, model, UNVERIFIED_ACCOUNT_EXPIRED);
	}

	public void sendDeveloperAccountExpiredEmail(String email, String login, String fullName) {
		Assert.hasText(login, "login is empty");
		Assert.hasText(fullName, "fullName is empty");

		Map<String, Object> model = new HashMap<>();
		model.put("fullName", StringEscapeUtils.escapeHtml4(fullName));
		model.put("login", StringEscapeUtils.escapeHtml4(login));
		sendEmail(email, model, ACCOUNT_EXPIRED);
	}

	public void sendReactivateDeveloperAccountEmail(String email, String login, String fullName) {
		Assert.hasText(login, "login is empty");
		Assert.hasText(fullName, "fullName is empty");

		Map<String, Object> model = new HashMap<>();
		model.put("fullName", StringEscapeUtils.escapeHtml4(fullName));
		model.put("login", StringEscapeUtils.escapeHtml4(login));
		sendEmail(email, model, REACTIVATE_ACCOUNT);
	}

	public void sendPasswordUpdatedEmail(String email, String login, String fullName) {
		Assert.hasText(login, "login is empty");
		Assert.hasText(fullName, "fullName is empty");

		Map<String, Object> model = new HashMap<>();
		model.put("fullName", StringEscapeUtils.escapeHtml4(fullName));
		model.put("login", StringEscapeUtils.escapeHtml4(login));
		sendEmail(email, model, PASSWORD_UPDATE);
	}

	public void sendPasswordResetEmail(String email, String login, String temporaryPassword, String fullName) {
		Assert.hasText(login, "login is empty");
		Assert.hasText(temporaryPassword, "password is empty");
		Assert.hasText(fullName, "fullName is empty");

		Map<String, Object> model = new HashMap<>();
		model.put("fullName", StringEscapeUtils.escapeHtml4(fullName));
		model.put("login", StringEscapeUtils.escapeHtml4(login));
		model.put("password", StringEscapeUtils.escapeHtml4(temporaryPassword));
		sendEmail(email, model, PASSWORD_RESET);
	}

	public void sendSoftwareReleaseScheduleStartEmail(String[] emails, Map<String, Object> model) {
		Assert.notNull(model, "model is null");
		Assert.notNull(model.get("jobName"), "jobName is required");
		Assert.notNull(model.get("requestor"), "requestor is required");
		Assert.notNull(model.get("assets"), "asset count is required");
		Assert.notNull(model.get("deviceType"), "deviceType is required");
		Assert.notNull(model.get("hwVersion"), "hwVersion is required");
		Assert.notNull(model.get("newSwVersion"), "newSwVersion is required");
		Assert.notNull(model.get("started"), "started is required");
		sendEmail(emails, model, SOFTWARE_RELEASE_SCHEDULE_START);
	}

	public void sendSoftwareReleaseScheduleEndEmail(String[] emails, Map<String, Object> model) {
		Assert.notNull(model, "model is null");
		Assert.notNull(model.get("jobName"), "jobName is required");
		Assert.notNull(model.get("requestor"), "requestor is required");
		Assert.notNull(model.get("assets"), "asset count is required");
		Assert.notNull(model.get("deviceType"), "deviceType is required");
		Assert.notNull(model.get("hwVersion"), "hwVersion is required");
		Assert.notNull(model.get("newSwVersion"), "newSwVersion is required");
		Assert.notNull(model.get("started"), "started is required");
		Assert.notNull(model.get("completed"), "completed is required");
		Assert.notNull(model.get("status"), "status is required");
		sendEmail(emails, model, SOFTWARE_RELEASE_SCHEDULE_END);
	}

	public void sendSoftwareReleaseScheduleSubmitEmail(String[] emails, Map<String, Object> model) {
		Assert.notNull(model, "model is null");
		Assert.notNull(model.get("jobName"), "jobName is required");
		Assert.notNull(model.get("requestor"), "requestor is required");
		Assert.notNull(model.get("assets"), "asset count is required");
		Assert.notNull(model.get("deviceType"), "deviceType is required");
		Assert.notNull(model.get("hwVersion"), "hwVersion is required");
		Assert.notNull(model.get("newSwVersion"), "newSwVersion is required");
		Assert.notNull(model.get("onDemand"), "onDemand is required");
		Boolean onDemand = (Boolean) model.get("onDemand");
		Assert.isTrue(onDemand || !onDemand && model.get("scheduledDate") != null, "scheduledDate is required");
		sendEmail(emails, model, SOFTWARE_RELEASE_SCHEDULE_SUBMIT);
	}

	public void sendSocialEventRegistrationVerifiedEmail(String email, Map<String, Object> model) {
		Assert.notNull(model, "model is null");
		Assert.hasText(email, "email is empty");
		Assert.notNull(model.get("name"), "name is required");
		Assert.notNull(model.get("zoneSystemName"), "zoneSystemName is required");
		Assert.notNull(model.get("zoneDescription"), "zoneDescription is required");
		sendEmail(email, model, SOCIAL_EVENT_REGISTRATION_VERIFIED);
	}

	private void sendEmail(String email, Map<String, Object> model, String templateName) {
		sendEmail(Arrays.array(email), model, templateName);
	}
	
	public void sendSocialEventRegistrationEmail(String email, Map<String, Object> model) {
		Assert.notNull(model, "model is null");
		Assert.hasText(email, "email is empty");
		Assert.notNull(model.get("name"), "name is required");
		Assert.notNull(model.get("verificationCode"), "verificationCode is required");
		Assert.notNull(model.get("zoneSystemName"), "zoneSystemName is required");
		Assert.notNull(model.get("zoneDescription"), "zoneDescription is required");
		sendEmail(email, model, SOCIAL_EVENT_REGISTRATION);
	}

	private void sendEmail(String[] emails, Map<String, Object> model, String templateName) {
		try {
			Pair<Template, Template> pair = getEmailTemplate(templateName);
			String subject = FreeMarkerTemplateUtils.processTemplateIntoString(pair.getLeft(), model);
			String body = FreeMarkerTemplateUtils.processTemplateIntoString(pair.getRight(), model);
			smtpEmailSender.send(emails, null, subject, body, EmailContentType.HTML);
		} catch (AcsRuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new AcsSystemException("unable to send email", e);
		}
	}

	public static class ModelBuilder {

		private Map<String, Object> model = new HashMap<String, Object>();

		public ModelBuilder withParameter(String name, Object value) {
			if (name != null && value != null) {
				model.put(name, value);
			}
			return this;
		}

		public ModelBuilder withParameter(String name, String value) {
			if (name != null && value != null) {
				model.put(name, StringEscapeUtils.escapeHtml4(value));
			}
			return this;
		}

		public ModelBuilder withParameter(String name, Instant value) {
			if (name != null && value != null) {
				model.put(name, Date.from(value));
			}
			return this;
		}

		public Map<String, Object> build() {
			return model;
		}
	}
}

package com.arrow.pegasus.cron;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.arrow.pegasus.LifeCycleAbstract;
import com.arrow.pegasus.util.EmailContentType;
import com.arrow.pegasus.util.SmtpEmailSender;

import freemarker.template.Configuration;
import freemarker.template.Template;
import moonstone.acs.AcsSystemException;
import moonstone.acs.AcsUtils;

public abstract class CronAbstract extends LifeCycleAbstract implements CronLogger {

	private final static String CRON_REPORT_TEMPLATE = "/cron/CronReport.html";

	@Autowired
	private SmtpEmailSender emailSender;

	@Autowired
	private Environment env;

	private CronReport report;
	private String toAddresses;
	private boolean started;
	private String reportSubject;
	private boolean enabled;
	private boolean sendEmail;

	// template
	private Configuration freeMarker;
	private Template emailTemplate;

	@PostConstruct
	protected void postConstruct() {
		String method = "postConstruct";
		String keytoAdress = getClass().getSimpleName() + ".toAddresses";
		toAddresses = env.getRequiredProperty(keytoAdress);
		logInfo(method, "key: %s, value: %s", keytoAdress, toAddresses);

		String keyReportSubject = getClass().getSimpleName() + ".reportSubject";
		reportSubject = env.getProperty(keyReportSubject, "");
		logInfo(method, "key: %s, value: %s", keyReportSubject, reportSubject);

		String keyEnabled = getClass().getSimpleName() + ".enabled";
		// cron is not disabled by default
		enabled = env.getProperty(keyEnabled, boolean.class, true);
		logInfo(method, "key: %s, value: %s", keyEnabled, enabled);
	}

	public CronAbstract() {
		super();
		initCron();
	}

	protected void initCron() {
		String method = "initCron";
		started = false;
		try {
			freeMarker = new Configuration(Configuration.VERSION_2_3_25);
			freeMarker.setDefaultEncoding("UTF-8");
			String content = AcsUtils.streamToString(getClass().getResourceAsStream(CRON_REPORT_TEMPLATE),
					StandardCharsets.UTF_8);
			logInfo(method, "loading email template: %s", CRON_REPORT_TEMPLATE);
			emailTemplate = new Template("CronReport", content, freeMarker);
		} catch (Exception e) {
			throw new AcsSystemException("unable to load email template: " + CRON_REPORT_TEMPLATE, e);
		}
	}

	public void start() {
		String method = "start";
		if (enabled) {
			run();
		} else {
			logInfo(method, "cron is disabled!");
		}
	}

	public void addLog(String method, String log) {
		Assert.isTrue(started, "cron is not started yet!");
		logInfo(method, log);
		report.addLog(String.format("[%s][%s] %s", Instant.now(), method, log));
	}

	public void addError(String method, String error) {
		Assert.isTrue(started, "cron is not started yet!");
		logInfo(method, error);
		report.addError(String.format("[%s][%s] %s", Instant.now(), method, error));
	}

	public void addException(String method, Throwable t) {
		Assert.isTrue(started, "cron is not started yet!");
		logInfo(method, ExceptionUtils.getStackTrace(t));
		report.addException(t);
	}

	protected void startCron() {
		Assert.isTrue(!started, "cron is already started!");
		String method = "startCron";
		logInfo(method, "...");
		report = new CronReport(getClass().getSimpleName());
		report.startCron();
		started = true;
		sendEmail = true;
	}

	protected void endCron() {
		Assert.isTrue(started, "cron is not started yet!");
		String method = "endCron";
		logInfo(method, "...");
		report.endCron();
		started = false;

		if (sendEmail) {
			sendEmail();
		} else {
			logWarn(method, "email will not be sent!");
		}
	}

	protected boolean isSendEmail() {
		return sendEmail;
	}

	protected void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	protected boolean isEnabled() {
		return enabled;
	}

	protected abstract void run();

	private void sendEmail() {
		String method = "sendEmail";
		try {
			Assert.hasText(toAddresses, "toAddresses is not set!");
			Assert.notNull(report, "report is not defined!");
			Assert.notNull(emailTemplate, "emailTemplate is not defined!");

			String body = FreeMarkerTemplateUtils.processTemplateIntoString(emailTemplate,
					Collections.singletonMap("report", report));
			logInfo(method, "sending email to: %s, message size: %d", toAddresses, body.length());
			if (!StringUtils.hasText(reportSubject)) {
				reportSubject = "CRON REPORT: " + report.getName();
			}
			emailSender.send(toAddresses.split(","), null, reportSubject, body, EmailContentType.HTML);
		} catch (Throwable t) {
			logError(method, "ERROR SENDING EMAIL", t);
		}
	}
}

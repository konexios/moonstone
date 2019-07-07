package com.arrow.pegasus.util;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.arrow.acs.AcsRuntimeException;
import com.arrow.acs.AcsSystemException;
import com.arrow.acs.AcsUtils;
import com.arrow.acs.Loggable;
import com.arrow.pegasus.security.Crypto;

public class SmtpEmailSender extends Loggable implements EmailSender {

	@Autowired
	private SmtpProperties properties;
	@Autowired
	private Crypto crypto;

	@Override
	public void send(String[] to, String[] cc, String subject, String body, EmailContentType contentType) {
		send(properties.getFrom(), to, cc, subject, body, contentType);
	}

	@Override
	public void send(String from, String[] to, String[] cc, String subject, String body, EmailContentType contentType) {
		String method = "send";

		if (AcsUtils.isEmpty(properties.getHost()) || AcsUtils.isEmpty(properties.getPort())) {
			logError(method, "Unable to send email.  SMTP configuration is missing!");
			return;
		}

		Assert.hasText(from, "from is empty");
		Assert.notEmpty(to, "to addresses is empty");
		Assert.hasText(subject, "subject is empty");
		Assert.hasText(body, "body is empty");

		Properties props = new Properties();
		props.put("mail.smtp.auth", properties.getAuth());
		props.put("mail.smtp.starttls.enable", properties.getStarttls());
		props.put("mail.smtp.host", properties.getHost());
		props.put("mail.smtp.port", properties.getPort());

		Session session = null;
		if (StringUtils.isNotEmpty(properties.getUserName()) && StringUtils.isNotEmpty(properties.getPassword())) {
			session = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					String password = properties.getPassword();
					if (password.startsWith("enc:")) {
						password = crypto.internalDecrypt(password.substring(4));
					}
					return new PasswordAuthentication(properties.getUserName(), password);
				}
			});
		} else {
			session = Session.getDefaultInstance(props);
		}
		logDebug(method, "email session created");

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(RecipientType.TO, convert(to));
			if (cc != null && cc.length > 0) {
				message.setRecipients(RecipientType.CC, convert(cc));
			}
			message.setSubject(subject);
			if (contentType == EmailContentType.PLAIN_TEXT) {
				message.setText(body, "utf-8");
			} else {
				message.setContent(body, "text/html;charset=utf-8");
			}

			logInfo(method, "sending email to[0] = %s", to[0]);
			Transport.send(message);
		} catch (AcsRuntimeException e) {
			throw e;
		} catch (Throwable t) {
			throw new AcsSystemException("Error sending email", t);
		}
	}

	private InternetAddress[] convert(String[] addresses) throws AddressException {
		if (addresses != null && addresses.length > 0) {
			return InternetAddress.parse(Arrays.stream(addresses).collect(Collectors.joining(",")));
		} else {
			return new InternetAddress[0];
		}
	}
}

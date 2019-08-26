package com.arrow.apollo.web.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.arrow.pegasus.security.Crypto;
import com.arrow.pegasus.util.EmailContentType;
import com.arrow.pegasus.util.EmailSender;

import moonstone.acs.AcsRuntimeException;
import moonstone.acs.AcsSystemException;
import moonstone.acs.Loggable;

public class ApolloEmailSender extends Loggable implements EmailSender {

	@Autowired
	private ApolloSmtpProperties apolloSmtpProperties;

	@Autowired
	private Crypto crypto;

	@Override
	public void send(String[] to, String[] cc, String subject, String body, EmailContentType contentType) {
		send(apolloSmtpProperties.getFrom(), to, cc, subject, body, contentType);
	}

	@Override
	public void send(String from, String[] to, String[] cc, String subject, String body, EmailContentType contentType) {
		String method = "send";

		Assert.hasText(from, "from is empty");
		Assert.notEmpty(to, "to addresses is empty");
		Assert.hasText(subject, "subject is empty");
		Assert.hasText(body, "body is empty");

		Properties props = new Properties();
		props.put("mail.smtp.auth", apolloSmtpProperties.getAuth());
		props.put("mail.smtp.starttls.enable", apolloSmtpProperties.getStarttls());
		props.put("mail.smtp.host", apolloSmtpProperties.getHost());
		props.put("mail.smtp.port", apolloSmtpProperties.getPort());

		Session session = null;
		if (StringUtils.isNotEmpty(apolloSmtpProperties.getUserName())
		        && StringUtils.isNotEmpty(apolloSmtpProperties.getPassword())) {
			session = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					String password = apolloSmtpProperties.getPassword();
					if (password.startsWith("enc:")) {
						password = crypto.internalDecrypt(password.substring(4));
					}
					return new PasswordAuthentication(apolloSmtpProperties.getUserName(), password);
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

	/**
	 * Send mail with inline images. Add images to template like
	 * <img src="cid:1">, <img src="cid:2"> logos param images will be placed to
	 * that location.
	 * 
	 * @param to
	 * @param cc
	 * @param subject
	 * @param body
	 * @param contentType
	 * @param logos
	 */
	public void sendWithAttachment(String[] to, String[] cc, String subject, String body, EmailContentType contentType,
	        List<File> logos) {
		String method = "send";

		Assert.hasText(apolloSmtpProperties.getFrom(), "from is empty");
		Assert.notEmpty(to, "to addresses is empty");
		Assert.hasText(subject, "subject is empty");
		Assert.hasText(body, "body is empty");

		Properties props = new Properties();
		props.put("mail.smtp.auth", apolloSmtpProperties.getAuth());
		props.put("mail.smtp.starttls.enable", apolloSmtpProperties.getStarttls());
		props.put("mail.smtp.host", apolloSmtpProperties.getHost());
		props.put("mail.smtp.port", apolloSmtpProperties.getPort());

		Session session = null;
		if (StringUtils.isNotEmpty(apolloSmtpProperties.getUserName())
		        && StringUtils.isNotEmpty(apolloSmtpProperties.getPassword())) {
			session = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					String password = apolloSmtpProperties.getPassword();
					if (password.startsWith("enc:")) {
						password = crypto.internalDecrypt(password.substring(4));
					}
					return new PasswordAuthentication(apolloSmtpProperties.getUserName(), password);
				}
			});
		} else {
			session = Session.getDefaultInstance(props);
		}

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(apolloSmtpProperties.getFrom()));
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

			MimeMultipart multipart = new MimeMultipart("LogoImages");

			BodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setContent(body, "text/html");
			multipart.addBodyPart(messageBodyPart);
			for (int indx = 0; indx < logos.size(); indx++) {

				// second part (the image)
				messageBodyPart = new MimeBodyPart();
				DataSource fds = new FileDataSource(logos.get(indx));

				messageBodyPart.setDataHandler(new DataHandler(fds));
				messageBodyPart.setHeader("Content-ID", "<" + indx + ">");

				multipart.addBodyPart(messageBodyPart);

			}
			message.setContent(multipart);
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

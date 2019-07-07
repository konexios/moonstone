package com.arrow.apollo.web.controller;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.apollo.web.model.LoginModels;
import com.arrow.apollo.web.util.ApolloEmailSender;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.PasswordPolicy;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.service.CryptoService;
import com.arrow.pegasus.util.EmailContentType;

@RestController
@RequestMapping("/api/apollo/users")
public class ApolloUserController extends ApolloControllerAbstract {
	@Autowired
	private ApolloEmailSender apolloEmailSender;
	private static final String NEW_USER_MAIL_TEMPLATE_PATH = "/static/assets/templates/newUserMail.vm";
	private static final String FORGOT_MAIL_TEMPLATE_PATH = "/static/assets/templates/forgotPasswordMail.vm";
	private static final String PASSWORD_CHANGE_MAIL_TEMPLATE_PATH = "/static/assets/templates/successPasswordChangeMail.vm";
	private static final String PATH_SEPERATOR = "#";

	@Value("${arrow.apollo.forgot.mail.logo.paths}")
	private String mailLogoPaths;

	@Value("${arrow.apollo.forgot.mail.subject}")
	private String mailSubject;

	@Value("${arrow.apollo.password.change.mail.subject}")
	private String successMailSubject;

	@RequestMapping(value = "/password/forgot/{userName:.+}", method = RequestMethod.PUT)
	public List<String> forgotPassword(@PathVariable String userName) {
		List<String> status = new ArrayList<String>();

		if (!StringUtils.isEmpty(userName)) {
			User user = getClientUserApi().findByLogin(userName);
			Assert.notNull(user, "User is null");

			String resetTempPassword = getClientUserApi().resetPassword(user, userName);
			Assert.notNull(resetTempPassword, "Reset Temporary password is null");

			// get forgot password mail template and replace required properties
			Template t = getVelocityEngine().getTemplate(FORGOT_MAIL_TEMPLATE_PATH);
			StringWriter mailBody = new StringWriter();
			VelocityContext velocityContext = new VelocityContext();
			velocityContext.put("userName", userName);
			velocityContext.put("password", resetTempPassword);
			t.merge(velocityContext, mailBody);

			// send email in other thread
			new Thread(() -> apolloEmailSender.sendWithAttachment(new String[] { user.getContact().getEmail() }, null,
			        mailSubject, mailBody.toString(), EmailContentType.HTML, getMailLogoList())).start();
			status.add("SUCCESS");
		}
		return status;
	}

	@RequestMapping(value = "/password/change/{userName:.+}", method = RequestMethod.PUT)
	public LoginModels.UserModel changePassword(HttpSession session, @PathVariable String userName,
	        @RequestBody LoginModels.PasswordChangeModel passwordModel) {

		User user = getClientUserApi().findByLogin(userName);

		Assert.isTrue(user.getStatus().equals(UserStatus.PasswordReset),
		        "Could not change password without authentication");

		// hashed current password
		String hashedCurrentPassword = getCryptoService().getCrypto().hash(passwordModel.getCurrentPassword(),
		        user.getSalt());

		Assert.isTrue(hashedCurrentPassword.equals(user.getPassword()), "Current password is wrong");

		// change password
		List<String> result = getClientUserApi().setNewPassword(user, passwordModel.getNewPassword(), user.getId());

		// if there are errors
		Assert.isTrue(result.isEmpty(), String.join("<br>", result));

		// get success mail template and replace required properties
		Template t = getVelocityEngine().getTemplate(PASSWORD_CHANGE_MAIL_TEMPLATE_PATH);
		StringWriter mailBody = new StringWriter();
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("userName", userName);
		t.merge(velocityContext, mailBody);

		// send email in other thread
		new Thread(() -> apolloEmailSender.sendWithAttachment(new String[] { user.getContact().getEmail() }, null,
		        successMailSubject, mailBody.toString(), EmailContentType.HTML, getMailLogoList())).start();

		return new LoginModels.UserModel(user);
	}

	private List<File> getMailLogoList() {
		// take logos path which need to use in email
		String[] logoPaths = mailLogoPaths.split(PATH_SEPERATOR);
		List<File> mailLogoList = new ArrayList<File>();
		for (String path : logoPaths) {
			File logoFile = new File(getClass().getClassLoader().getResource(path).getPath());
			if (logoFile != null && logoFile.exists()) {
				mailLogoList.add(logoFile);
			}
		}
		return mailLogoList;
	}

	@RequestMapping(value = "/password-policy/{login:.+}", method = RequestMethod.GET)
	public PasswordPolicy getPasswordPolicy(@PathVariable String login) {

		User user = getClientUserApi().findByLogin(login);
		return getPasswordPolicy(user);
	}

	private PasswordPolicy getPasswordPolicy(User user) {

		Assert.notNull(user, "user is null");
		Company company = getCoreCacheService().findCompanyById(user.getCompanyId());
		Assert.notNull(company, "company is null");
		return company.getPasswordPolicy();
	}
}